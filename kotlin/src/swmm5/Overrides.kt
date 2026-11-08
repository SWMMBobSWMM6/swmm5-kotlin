// Overrides.kt - hand-ported pieces of EPA SWMM 5.2.4 that use C pointer arithmetic the translator does
// not map: odesolve.c (whole file), mempool.c (its pool becomes plain byte buffers) and six helpers from
// swmm5.c and project.c. Each keeps the C statement order and floating-point expressions exactly; pow is
// glibc's (Glibm.kt). A Kotlin port of the Java port's Overrides.java.
// Written by Claude (Anthropic) for Robert Dickinson; see README.md.
@file:Suppress("FunctionName", "LocalVariableName", "UNUSED_PARAMETER", "ObjectPropertyName")
@file:JvmName("Overrides")
package swmm5

import kotlin.math.abs

private const val OV_MAXFNAME = 259

fun sstrncpy(dest: CPtr?, src: CPtr?, n: Long): Long {
    var offset = 0
    if (n > 0) {
        while (src!!.g(offset) != 0) {
            if (offset.toLong() == n) break
            dest!!.s(offset, src.g(offset))
            offset++
        }
    }
    dest!!.s(offset, 0)
    return R.strlen(dest)
}

fun sstrcat(dest: CPtr?, src: CPtr?, size: Long): Long {
    var offset = R.strlenI(dest)
    var srcIndex = 0
    while (src!!.g(srcIndex) != 0) {
        dest!!.s(offset, src.g(srcIndex))
        offset++
        srcIndex++
        if (offset.toLong() == size - 1) break
    }
    dest!!.s(offset, 0)
    return R.strlen(dest)
}

private fun ovIsRelative(fname: CPtr): Boolean {
    if (R.strchr(fname, ':'.code) != null) return false
    if (fname.g(0) == '\\'.code) return false
    if (fname.g(0) == '/'.code) return false
    return true
}

fun getAbsolutePath(fname: CPtr?, absPath: CPtr?, size: Long) {
    if (fname == null || R.strlen(fname) == 0L) return
    if (ovIsRelative(fname)) {
        val full = R.realpathOf(R.strOf(fname))          // realpath(fname, absPath)
        val m = minOf(full.length, absPath!!.b.size - absPath.o - 1)
        for (i in 0 until m) absPath.s(i, full[i].code)
        absPath.s(m, 0)
    } else {
        sstrncpy(absPath, fname, R.strlen(fname))
    }
    val endOfDir = R.strrchr(absPath, '/'.code)
    endOfDir?.s(1, 0)
}

fun getTempFileName(fname: CPtr?): CPtr? {
    sstrncpy(fname, R.lit("swmmXXXXXX"), OV_MAXFNAME.toLong())
    R.mkstemp(fname)
    return fname
}

fun project_createMatrix(nrows: Int, ncols: Int): Array<DoubleArray?>? {
    if (nrows <= 0 || ncols <= 0) return null
    return Array(nrows) { DoubleArray(ncols) }
}

fun project_freeMatrix(a: Array<DoubleArray?>?) {}

fun AllocInit(): Any? = Any()
fun Alloc(n: Long): CPtr? = R.buf(n)
fun AllocSetPool(p: Any?): Any? = p
fun AllocReset() {}
fun AllocFreePool() {}

// ------------------------------------------------------------ odesolve.c (Cash-Karp Runge-Kutta)
private const val ODE_MAXSTP = 10000
private const val ODE_TINY = 1.0e-30
private const val ODE_SAFETY = 0.9
private const val ODE_PGROW = -0.2
private const val ODE_PSHRNK = -0.25
private const val ODE_ERRCON = 1.89e-4
private var odeNmax = 0
private var odeY = DoubleArray(0)
private var odeYscal = DoubleArray(0)
private var odeDydx = DoubleArray(0)
private var odeYerr = DoubleArray(0)
private var odeYtemp = DoubleArray(0)
private var odeAk2 = DoubleArray(0)
private var odeAk3 = DoubleArray(0)
private var odeAk4 = DoubleArray(0)
private var odeAk5 = DoubleArray(0)
private var odeAk6 = DoubleArray(0)

fun odesolve_open(n: Int): Int {
    odeNmax = 0
    odeY = DoubleArray(n); odeYscal = DoubleArray(n); odeDydx = DoubleArray(n); odeYerr = DoubleArray(n); odeYtemp = DoubleArray(n)
    odeAk2 = DoubleArray(n); odeAk3 = DoubleArray(n); odeAk4 = DoubleArray(n); odeAk5 = DoubleArray(n); odeAk6 = DoubleArray(n)
    odeNmax = n
    return 1
}

fun odesolve_close() { odeNmax = 0 }

fun odesolve_integrate(ystart: DoubleArray?, n: Int, x1: Double, x2: Double, eps: Double, h1: Double, derivs: OdeDerivs?): Int {
    val hdid = DoubleArray(1); val hnext = DoubleArray(1); val x = DoubleArray(1)
    x[0] = x1
    var h = h1
    if (odeNmax < n) return 1
    for (i in 0 until n) odeY[i] = ystart!![i]
    for (nstp in 1..ODE_MAXSTP) {
        derivs!!.call(x[0], odeY, odeDydx)
        for (i in 0 until n) odeYscal[i] = abs(odeY[i]) + abs(odeDydx[i] * h) + ODE_TINY
        if ((x[0] + h - x2) * (x[0] + h - x1) > 0.0) h = x2 - x[0]
        val errcode = odeRkqs(x, n, h, eps, hdid, hnext, derivs)
        if (errcode != 0) break
        if ((x[0] - x2) * (x2 - x1) >= 0.0) {
            for (i in 0 until n) ystart!![i] = odeY[i]
            return 0
        }
        if (abs(hnext[0]) <= 0.0) return 2
        h = hnext[0]
    }
    return 3
}

private fun odeRkqs(x: DoubleArray, n: Int, htry: Double, eps: Double, hdid: DoubleArray, hnext: DoubleArray, derivs: OdeDerivs): Int {
    val xold = x[0]
    var h = htry
    while (true) {
        odeRkck(xold, n, h, derivs)
        var errmax = 0.0
        for (i in 0 until n) {
            val err = abs(odeYerr[i] / odeYscal[i])
            if (err > errmax) errmax = err
        }
        errmax /= eps
        if (errmax > 1.0) {
            val htemp = ODE_SAFETY * h * Glibm.pow(errmax, ODE_PSHRNK)
            h = if (h >= 0) (if (htemp > 0.1 * h) htemp else 0.1 * h)
                else (if (htemp < 0.1 * h) htemp else 0.1 * h)
            val xnew = xold + h
            if (xnew == xold) return 2
            continue
        } else {
            if (errmax > ODE_ERRCON) hnext[0] = ODE_SAFETY * h * Glibm.pow(errmax, ODE_PGROW)
            else hnext[0] = 5.0 * h
            hdid[0] = h
            x[0] += h
            for (i in 0 until n) odeY[i] = odeYtemp[i]
            return 0
        }
    }
}

private fun odeRkck(x: Double, n: Int, h: Double, derivs: OdeDerivs) {
    val a2 = 0.2; val a3 = 0.3; val a4 = 0.6; val a5 = 1.0; val a6 = 0.875
    val b21 = 0.2; val b31 = 3.0 / 40.0; val b32 = 9.0 / 40.0; val b41 = 0.3; val b42 = -0.9; val b43 = 1.2
    val b51 = -11.0 / 54.0; val b52 = 2.5; val b53 = -70.0 / 27.0; val b54 = 35.0 / 27.0
    val b61 = 1631.0 / 55296.0; val b62 = 175.0 / 512.0; val b63 = 575.0 / 13824.0
    val b64 = 44275.0 / 110592.0; val b65 = 253.0 / 4096.0; val c1 = 37.0 / 378.0
    val c3 = 250.0 / 621.0; val c4 = 125.0 / 594.0; val c6 = 512.0 / 1771.0
    val dc5 = -277.0 / 14336.0
    val dc1 = c1 - 2825.0 / 27648.0; val dc3 = c3 - 18575.0 / 48384.0
    val dc4 = c4 - 13525.0 / 55296.0; val dc6 = c6 - 0.25
    val y = odeY; val dydx = odeDydx; val ytemp = odeYtemp
    val ak2 = odeAk2; val ak3 = odeAk3; val ak4 = odeAk4; val ak5 = odeAk5; val ak6 = odeAk6
    for (i in 0 until n) ytemp[i] = y[i] + b21 * h * dydx[i]
    derivs.call(x + a2 * h, ytemp, ak2)
    for (i in 0 until n) ytemp[i] = y[i] + h * (b31 * dydx[i] + b32 * ak2[i])
    derivs.call(x + a3 * h, ytemp, ak3)
    for (i in 0 until n) ytemp[i] = y[i] + h * (b41 * dydx[i] + b42 * ak2[i] + b43 * ak3[i])
    derivs.call(x + a4 * h, ytemp, ak4)
    for (i in 0 until n) ytemp[i] = y[i] + h * (b51 * dydx[i] + b52 * ak2[i] + b53 * ak3[i] + b54 * ak4[i])
    derivs.call(x + a5 * h, ytemp, ak5)
    for (i in 0 until n) ytemp[i] = y[i] + h * (b61 * dydx[i] + b62 * ak2[i] + b63 * ak3[i] + b64 * ak4[i] + b65 * ak5[i])
    derivs.call(x + a6 * h, ytemp, ak6)
    for (i in 0 until n) ytemp[i] = y[i] + h * (c1 * dydx[i] + c3 * ak3[i] + c4 * ak4[i] + c6 * ak6[i])
    for (i in 0 until n) odeYerr[i] = h * (dc1 * dydx[i] + dc3 * ak3[i] + dc4 * ak4[i] + dc5 * ak5[i] + dc6 * ak6[i])
}
