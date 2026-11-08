#!/usr/bin/env python3
# bench_kotlin.py - kept as run for kotlin/results/bench_kotlin.{txt,json}. Written by Claude (Anthropic) for Robert Dickinson.
"""interleaved CPU-time comparison of EPA 5.2.4, the Kotlin port and the Java port on copies of corpus decks
(in /dev/shm). usage: kt_bench.py REPS ID... ; prints per-run rows and medians. Checks each run's .rpt against the
reference (clock lines excluded) so every engine did the same work."""
import os, re, sys, random, resource, shutil, subprocess, time, statistics, json
ROOT = '/home/claude/swmm5-ports'
W = '/dev/shm/ktbench'
TS = re.compile(rb'Analysis begun|Analysis ended|Total elapsed time')
E = {'EPA': [ROOT + '/ref/runswmm'], 'EPA again': [ROOT + '/ref/runswmm'],
     'Kotlin': [ROOT + '/kotlin/runswmm'], 'Java': [ROOT + '/java/runswmm']}
models = dict(l.rstrip('\n').split('\t', 1) for l in open(ROOT + '/gate/models_all.tsv'))
reps = int(sys.argv[1]); ids = sys.argv[2:]
rng = random.Random(1729)
rows = []
def lines(f):
    return [l for l in open(f, 'rb').read().split(b'\n') if not TS.search(l)]
for rep in range(reps):
    for mid in ids:
        order = list(E); rng.shuffle(order)
        for e in order:
            d = os.path.join(W, mid); shutil.rmtree(d, ignore_errors=True); os.makedirs(d)
            shutil.copy(models[mid], d)
            r0 = resource.getrusage(resource.RUSAGE_CHILDREN); t0 = time.perf_counter()
            p = subprocess.run(E[e] + [os.path.basename(models[mid]), d + '/r.rpt', d + '/r.out'], cwd=d,
                               stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            wall = time.perf_counter() - t0; r1 = resource.getrusage(resource.RUSAGE_CHILDREN)
            cpu = (r1.ru_utime - r0.ru_utime) + (r1.ru_stime - r0.ru_stime)
            same = lines(d + '/r.rpt') == lines(ROOT + '/gate/ref_outputs/%s/r.rpt' % mid)
            rows.append({'rep': rep, 'id': mid, 'engine': e, 'cpu': round(cpu, 3), 'wall': round(wall, 3), 'rc': p.returncode, 'rpt_same': same})
            print(rep, mid, e, round(cpu, 3), round(wall, 3), same, flush=True)
            shutil.rmtree(d)
json.dump(rows, open(os.path.join(ROOT, 'kotlin/results/bench_kotlin.json'), 'w'), indent=1)
print('median CPU seconds over reps, and ratio to EPA:')
for mid in ids:
    med = {e: statistics.median(r['cpu'] for r in rows if r['id'] == mid and r['engine'] == e) for e in E}
    print('%s %-45s EPA %.2f  EPA again %.2f (x%.2f)  Kotlin %.2f (x%.2f)  Java %.2f (x%.2f)' % (
        mid, os.path.basename(models[mid])[:45], med['EPA'], med['EPA again'], med['EPA again'] / med['EPA'],
        med['Kotlin'], med['Kotlin'] / med['EPA'], med['Java'], med['Java'] / med['EPA']))
