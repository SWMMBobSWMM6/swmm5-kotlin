#!/bin/bash
# run_libm_tests.sh [N]
# Check the Kotlin translation of glibc's math routines (src/swmm5/Glibm.kt + GlibmRt.kt) bit for bit against the
# system libm (the library EPA's runswmm calls), the way tools/libm/test/run_tests.sh checks the TypeScript, Java
# and Go ones: N random arguments per function over SWMM's ranges plus special values (tools/libm/test/mkvectors.c)
# and an edge-case set (mkedge.c). Uses the Kotlin compiler build.sh uses; deletes the vectors (about 260 MB at
# N = 1000000; KT_LIBM_WORK puts them elsewhere, e.g. in /dev/shm) when done. Written by Claude (Anthropic) for Robert Dickinson; see ../README.md.
set -e
HERE=$(cd "$(dirname "$0")" && pwd); K=$(cd "$HERE/.." && pwd); ROOT=$(cd "$K/.." && pwd)
N=${1:-1000000}; W=${KT_LIBM_WORK:-$ROOT/build/kt_libm_test}; rm -rf "$W"; mkdir -p "$W"
gcc -O2 -o "$W/mkvectors" "$ROOT/tools/libm/test/mkvectors.c" -lm
gcc -O2 -o "$W/mkedge" "$ROOT/tools/libm/test/mkedge.c" -lm
"$W/mkvectors" "$N" "$W/vectors.bin" > /dev/null
"$W/mkedge" 100000 "$W/edge.bin" > /dev/null
L=${KOTLIN_LIB:-/opt/gradle/lib}
KV=$(ls "$L"/kotlin-compiler-embeddable-*.jar | sed 's/.*embeddable-\(.*\)\.jar/\1/' | sort -V | tail -1)
STDLIB=$L/kotlin-stdlib-$KV.jar
CP=$L/kotlin-compiler-embeddable-$KV.jar:$STDLIB:$L/kotlin-script-runtime-$KV.jar:$L/kotlin-reflect-$KV.jar
CP=$CP:$L/kotlin-daemon-embeddable-$KV.jar:$(ls $L/trove4j-*.jar | head -1):$(ls $L/annotations-*.jar | head -1)
CP=$CP:$(ls $L/kotlinx-coroutines-core-jvm-*.jar | head -1)
java -Xmx2g -cp "$CP" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler -no-stdlib -no-reflect -cp "$STDLIB" -jvm-target 17 \
    -nowarn -d "$W/classes" "$K/src/swmm5/Glibm.kt" "$K/src/swmm5/GlibmRt.kt" "$HERE/Check.kt" 2>&1 | grep -v '^Picked up' || true
echo "== Kotlin $KV: vectors.bin (N = $N per function)"
java -cp "$W/classes:$STDLIB" swmm5.CheckKt "$W/vectors.bin" 2>&1 | grep -v '^Picked up'
echo "== Kotlin $KV: edge.bin"
java -cp "$W/classes:$STDLIB" swmm5.CheckKt "$W/edge.bin" 2>&1 | grep -v '^Picked up'
rm -rf "$W"
