// PowTime.kt - stopwatch for the Kotlin glibm translation (lessons/17): 2 million calls each of pow, exp and sin,
// six repetitions (the first includes JIT compilation). Compare with powtime_glibc.c, the same loop over glibc.
// Written by Claude (Anthropic) for Robert Dickinson.
package swmm5
fun main() {
    val n = 2_000_000
    val xs = DoubleArray(n); val ys = DoubleArray(n)
    var s = 0x2545F4914F6CDD1DL
    for (i in 0 until n) {
        s = s xor (s shl 13); s = s xor (s ushr 7); s = s xor (s shl 17)
        xs[i] = 0.01 + 100.0 * ((s ushr 11).toDouble() / 9007199254740992.0)
        ys[i] = if (i % 3 == 0) 5.0 / 3.0 else if (i % 3 == 1) 0.5 else -2.5 + 5.0 * ((s ushr 20).toDouble() / 8796093022208.0)
    }
    for (rep in 0 until 6) {
        var acc = 0.0
        val t0 = System.nanoTime()
        for (i in 0 until n) acc += Glibm.pow(xs[i], ys[i])
        val t1 = System.nanoTime()
        var acc2 = 0.0
        for (i in 0 until n) acc2 += Glibm.exp(ys[i])
        val t2 = System.nanoTime()
        var acc3 = 0.0
        for (i in 0 until n) acc3 += Glibm.sin(xs[i])
        val t3 = System.nanoTime()
        println(String.format("rep %d: pow %.1f ns, exp %.1f ns, sin %.1f ns  (%g %g %g)", rep, (t1 - t0).toDouble() / n, (t2 - t1).toDouble() / n, (t3 - t2).toDouble() / n, acc, acc2, acc3))
    }
}
