#!/bin/bash
# build.sh - compile the Kotlin port and package it, with the Kotlin standard library, as swmm5.jar.
#   ./build.sh            then   ./runswmm <input file> <report file> [output file]
# The compiler: `kotlinc` if one is on the PATH, otherwise the Kotlin compiler that ships inside Gradle
# (KOTLIN_LIB, default /opt/gradle/lib: kotlin-compiler-embeddable-2.0.21.jar and its dependencies), run
# with the JDK's java. Needs a JDK 17 or newer. Written by Claude (Anthropic) for Robert Dickinson; see README.md.
set -e
cd "$(dirname "$0")"
L=${KOTLIN_LIB:-/opt/gradle/lib}
rm -rf classes && mkdir classes
if command -v kotlinc >/dev/null 2>&1; then
    STDLIB=$(dirname "$(readlink -f "$(command -v kotlinc)")")/../lib/kotlin-stdlib.jar
    JAVA_OPTS="-Xmx3g" kotlinc -jvm-target 17 -nowarn -d classes src/swmm5/*.kt
else
    KV=$(ls "$L"/kotlin-compiler-embeddable-*.jar | sed 's/.*embeddable-\(.*\)\.jar/\1/' | sort -V | tail -1)
    STDLIB=$L/kotlin-stdlib-$KV.jar
    CP=$L/kotlin-compiler-embeddable-$KV.jar:$STDLIB:$L/kotlin-script-runtime-$KV.jar:$L/kotlin-reflect-$KV.jar
    CP=$CP:$L/kotlin-daemon-embeddable-$KV.jar:$(ls $L/trove4j-*.jar | head -1):$(ls $L/annotations-*.jar | head -1)
    CP=$CP:$(ls $L/kotlinx-coroutines-core-jvm-*.jar | head -1)
    java -Xmx3g -Xss16m -cp "$CP" org.jetbrains.kotlin.cli.jvm.K2JVMCompiler -no-stdlib -no-reflect -cp "$STDLIB" \
        -jvm-target 17 -nowarn -d classes src/swmm5/*.kt
fi
# the Kotlin standard library goes into the jar, so `java -jar swmm5.jar` needs nothing else
(cd classes && unzip -qo "$STDLIB" -x 'META-INF/*' && rm -rf META-INF)
jar cfe swmm5.jar swmm5.Main -C classes .
rm -rf classes
echo "built $(pwd)/swmm5.jar"
