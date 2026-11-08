#!/bin/bash
# gate_saves.sh - the Kotlin port's command line for gate/gate.py, which runs `ENGINE model.inp report.rpt
# output.out` in the model's folder. The files the deck SAVEs to ([FILES] SAVE ..., and LID report files;
# listed by swmm6-rust's gate/savefiles.py, which this only calls) are backed up first and restored after,
# so a SAVE in the shared corpus never changes a file another deck USEs. The exit status is the engine's.
# Modelled on swmm6-rust/gate/openswmm_saves.sh. Written by Claude (Anthropic) for Robert Dickinson.
H=$(cd "$(dirname "$0")" && pwd)
SAVEFILES=${SAVEFILES:-$H/../gate/savefiles.py}
d="$PWD/$1"
B=$(mktemp -d); python3 "$SAVEFILES" "$d" > "$B/saves.txt" 2>/dev/null; k=0
while IFS= read -r f; do k=$((k+1)); [ -f "$f" ] && cp -p "$f" "$B/$k"; done < "$B/saves.txt"
"$H/runswmm" "$@"; rc=$?
k=0; while IFS= read -r f; do k=$((k+1)); if [ -f "$B/$k" ]; then cp -p "$B/$k" "$f"; else rm -f "$f"; fi; done < "$B/saves.txt"
rm -rf "$B"; exit $rc
