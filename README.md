# swmm5-kotlin

EPA SWMM 5.2.4 in Kotlin, written by a translator that reads EPA's C, and judged against EPA itself:
the binary `.out` file byte for byte, and the `.rpt` report line for line except the three lines that
print the wall clock.

**Result: 480 of 480 models identical.** Gate run `kt_1` (6 November 2026): every `.out` byte-identical, every `.rpt`
identical except the three clock lines, exit code 0 on every model, as EPA's. Median CPU time 1.3 to 2.6 times
EPA's on six longer models (EPA against itself: 0.99 to 1.00), a little above the Java port's 1.3 to 2.1 in the
same interleaved runs.

It is not a new engine and not a wrapper: there is no C in it and it calls no other port. Every number it
writes is EPA 5.2.4's, including the results of EPA's own defects wherever EPA's behaviour is defined (see
`lessons/02-epa-defects-found.md`).

## The parity contract

The same as the other ports' (see the Java port's README for the full text):

- Reference: EPA SWMM 5.2.4 built from the USEPA `v5.2.4` tag with gcc 13.3 `-O2` and `_FORTIFY_SOURCE` off
  (`ref/Makefile`), on x86-64 Linux with glibc 2.39, on a processor with FMA and AVX2.
- Each model runs in its own folder, as EPA's command line runs it, with the same exit code (EPA's command line
  always exits 0).
- Pass = `.out` byte-identical (when EPA writes one) and `.rpt` identical after removing the lines
  `Analysis begun on`, `Analysis ended on` and `Total elapsed time`. When an input error stops EPA before it
  writes results, the engine must not write a results file either. Nothing else is excused.
- Corpus: the 480 gate models of SWMMBobSWMM6/1729-SWMM5-Models-2030 (`gate/models_all.tsv`); reference
  outputs in `gate/ref_outputs/`.

## Build and run

Requires a JDK 17 or newer (built and tested with OpenJDK 21.0.12) and a Kotlin compiler.

```sh
kotlin/build.sh                                  # writes kotlin/swmm5.jar (the Kotlin standard library inside)
kotlin/runswmm model.inp model.rpt model.out
```

`build.sh` uses `kotlinc` if one is on the PATH; otherwise it runs the Kotlin compiler that ships inside
Gradle (`KOTLIN_LIB`, default `/opt/gradle/lib`). **Toolchain used here: Kotlin 2.0.21**, the
`kotlin-compiler-embeddable-2.0.21.jar` and `kotlin-stdlib-2.0.21.jar` bundled with Gradle 8.14.3 at
`/opt/gradle-8.14.3/lib`, run on OpenJDK 21.0.12. Why that one: this sandbox refused Maven Central
(`repo1.maven.org` and `repo.maven.apache.org` answered 403), and running a compiler downloaded from npm
(JetBrains' own `kotlin-compiler` 2.4.21 package) was refused by the session's safety check as code from
outside; the compiler already installed with Gradle was neither. A full build takes about 40 seconds.

`runswmm` gives the JVM three quarters of memory and the parallel collector, as the Java port's launcher does:
one corpus model (`Hydrology/sewer_rdii_problem.inp`) allocates 3.38 GB of RDII rainfall history, as EPA does
(`lessons/14-the-jvm-heap-is-a-quarter-of-memory.md`).

## How it was made

`tools/c2x/front.py` parses EPA's 54 C files with libclang and classifies every pointer declaration;
`tools/c2x/emit_kotlin.py` writes the Kotlin engine (37,246 lines, generated: `src/swmm5/F_*.kt`, `Types.kt`,
`Consts.kt`). It reuses the Java back end's analysis (which locals live in boxes, pointer modes, names, Java's
reachability rules) and its type model, because Kotlin runs on the same JVM; the code it writes is its own.
A hand-written runtime (1,069 lines: `R.kt`, `Refs.kt`, `Overrides.kt`, `Main.kt`) supplies the C library
pieces SWMM uses (glibc-exact printf, strtod and sscanf, stdio with C's positioning and end-of-file rules,
string functions, x86's double-to-int rule) and hand ports of `odesolve.c`, `mempool.c` and six helper
functions. Both are Kotlin ports of the Java port's runtime; nothing calls the Java port.

What Kotlin needed that Java did not (each is in the emitter's header):

- No implicit numeric conversions: every C conversion, clang's implicit ones included, is written out
  (`.toDouble()`, `.toLong()`, `R.D2I(...)` for x86's double-to-int), so each operation has C's types.
- Bitwise operators and shifts are infix functions (`and`, `or`, `xor`, `shl`, `shr`, `ushr`, `inv()`).
- Null safety: every reference type is nullable and every dereference is `!!`, which faults where C would.
- Assignments, `++` and `+=` are statements; used as values they become `(v).also { __v -> x = __v }`.
- Parameters are read-only: a parameter the C code assigns is copied into a local of the same name.
- No C `for`: `init; while (cond) { body; inc }`, and each `continue` runs `inc` first.
- No `switch`: `when`, with C's fall-through written out (the following case bodies appended up to the first
  that cannot complete), inside a labelled `do { } while (false)` when a `break` leaves a case early. Every
  loop is labelled and every `break` and `continue` names its target.
- A bare `{ }` is a lambda in Kotlin, so nested C blocks are flattened (local names are unique per function).
- Private top-level names clash with public ones in the same package (the hand-written odesolve constants had
  to be renamed); `$` in a string literal is escaped.

To regenerate: `python3 tools/c2x/emit_kotlin.py kotlin/src/swmm5` from the repository root (needs
`pip install libclang==18.1.1` and the clang 18 headers at `/usr/lib/llvm-18/lib/clang/18/include`).

## The math library

EPA calls glibc for `exp`, `log`, `log10`, `pow`, `sin`, `cos`, `tan`, `asin`, `acos` and `atan`, and on x86-64
glibc runs versions compiled with fused multiply-add. Kotlin on the JVM has only Java's `Math`, which rounds
differently, so the port does not use it for any of these: `src/swmm5/Glibm.kt` (4,944 lines) is glibc
2.39's FMA build translated to Kotlin from GCC's optimized GIMPLE by `tools/libm/gim2x.py`'s Kotlin back end
(`gim2x.py kt`; `make_glibm.sh` runs it), with `GlibmRt.kt` (137 lines, hand-written: bit casts,
x86's conversions, unsigned arithmetic, glibc's error helpers and a port of `branred.c`). Each fused
multiply-add GCC placed is a `Math.fma` (exactly rounded); `fabs`, `sqrt`, `floor` and `ceil` are exact in
both libraries and use Kotlin's.

Checked bit for bit against the system libm with `test/run_libm_tests.sh` (`test/Check.kt` reading the vectors
of `tools/libm/test/mkvectors.c` and `mkedge.c`): 11,000,672 random arguments over SWMM's ranges plus special
values (1,000,256 of them for fma), and 3,300,000 edge cases (huge trig arguments, negative bases, overflow
and underflow, subnormals, domain errors), NaNs included: **0 differences and 0 NaN-sign differences** for every function and for fma
(`kotlin/results/libm_tests.txt`). The census samples the other ports were also tested on were not kept, so
they are not in this test.

Adding the Kotlin back end left the Go, Java and TypeScript outputs of `gim2x.py` unchanged: regenerated from
the same GIMPLE and tables, `ts/src/glibm.ts`, `java/src/swmm5/Glibm.java` and `go/glibm/glibm.go` (after
gofmt) are byte-identical to the shipped files (`kotlin/results/gim2x_unchanged.txt`).

## Re-running the gate

```sh
SWMM_CORPUS=/path/to/1729-SWMM5-Models-2030 python3 gate/gate.py kt "$PWD/kotlin/gate_saves.sh" -j 1 --timeout 3600 --prune
```

`gate_saves.sh` backs up the files each deck SAVEs to before the run and restores them after (it calls
swmm6-rust's `gate/savefiles.py` to list them), so a deck that SAVEs RDII or runoff files cannot change a file
another deck USEs.

The run behind the result: `gate/kt_1_run.txt` (summary) and `kotlin/results/kt_1.json` (per model, a copy of
`gate/results/kt_1.json`). It took 397 seconds of engine run time for the 480 models, one at a time (6 minutes
39 seconds of wall time, start to finish). Free disk was under 1 GB when it ran, so its
output folder was a tmpfs (`gate/results/kt_1` linked to `/dev/shm`; `lessons/22-a-gate-can-run-in-memory.md`):
every model passed and was pruned, so nothing was kept. `Hydrology/cho.txt`, which two decks SAVE and one USEs, was
checked afterwards: 1,114,474 bytes, md5 5b0ca1fe78588202fa779dbfa417b089, as before.

## What this does not show

- That SWMM is right: identical to EPA means EPA's answers, good or bad.
- Other platforms: the port carries glibc's FMA routines, so it matches EPA run on a processor with FMA and
  AVX2, and would not follow EPA onto one without (where EPA itself changes 43 of 480 models).
- Paths no gate model exercises (counting only models that ran without an input error): streets and inlets,
  climate files, USE HOTSTART, USE INFLOWS, USE RUNOFF. The stepping API (`swmm_open`/`swmm_step`/...) is
  translated but only the command line was gated.
- Speed: `kotlin/test/bench_kotlin.py` ran EPA (twice), this port and the Java port on six of the longer gate
  models, 3 repetitions in a fresh random order each, on copies of the decks in `/dev/shm`, each run's report
  checked against EPA's. Median CPU time against EPA: Kotlin 1.31 to 2.63 times, Java 1.26 to 2.06, EPA again
  0.99 to 1.00 (`kotlin/results/bench_kotlin.txt`). Six models on a shared 2-core machine; short models, where
  the JVM's start-up dominates, were not timed. glibc's pow, exp and sin take about 10, 6 and 31 ns a call in
  Kotlin against 9.5, 3.7 and 15 ns in glibc (`kotlin/results/libm_speed.txt`).
- No person has reviewed the generated code line by line; the evidence is the gate and the libm test.

## Licence and credit

EPA SWMM is a work of the U.S. Environmental Protection Agency and is in the public domain; its engine is Lewis
Rossman's design, and this port keeps every algorithm as he wrote it. No licence has been chosen yet for the
translator and the runtime. `src/swmm5/Glibm.kt` and `src/swmm5/GlibmRt.kt` are translations of glibc 2.39
code and remain under glibc's licence, the GNU Lesser General Public License 2.1 or later
(`ref/LGPL-2.1.txt`).

## AI use

The emitter (`tools/c2x/emit_kotlin.py`), the Kotlin back end of `tools/libm/gim2x.py`, the runtime, the build
and gate scripts, the libm test and this README were written by Claude (Anthropic's AI model, Claude Opus 5.5)
in a Claude Code session on 6 November 2026, for Robert (Bob) Dickinson, who chose Kotlin for his Code Rosetta
Stone app and directs the project. Claude also ran the gate, the libm test and the timing runs, and fixed what the compiler rejected (the first gate
run found no mismatch). The
generated files are the emitter's output, not edited by hand. EPA SWMM 5.2.4 is public domain; the
glibc-derived math is LGPL-2.1-or-later. No person has reviewed the code line by line.
