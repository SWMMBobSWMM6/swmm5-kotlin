// Generated from EPA SWMM 5.2.4 (infil.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_infil")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var Infil: Array<TInfil?>? = null
@JvmField var Fumax: Double = 0.0
@JvmField var InfilFactor: Double = 0.0

// infil.c:101
fun infil_create(n: Int) {
    Infil = Array<TInfil?>(((n).toLong()).toInt()) { TInfil() }
    if ((Infil == null)) {
        ErrorCode = ERR_MEMORY
    }
    InfilFactor = 1.0
    return
}

// infil.c:116
fun infil_delete() {
    if ((Infil != null)) {
        R.free(Infil)
        Infil = (null)
    }
}

// infil.c:128
fun infil_readParams(__m: Int, tok: Array<CPtr?>?, __ntoks: Int): Int {
    var m: Int = __m
    var ntoks: Int = __ntoks
    var i: Int = 0
    var j: Int = 0
    var n: Int = 0
    var status: Int = 0
    var x: DoubleArray? = DoubleArray(5)
    j = project_findObject(SUBCATCH, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    i = findmatch(tok!![(ntoks - 1)], InfilModelWords)
    if ((i >= 0)) {
        m = i
        ntoks--
    }
    if ((m == HORTON)) {
        n = 5
    } else if ((m == MOD_HORTON)) {
        n = 5
    } else if ((m == GREEN_AMPT)) {
        n = 4
    } else if ((m == MOD_GREEN_AMPT)) {
        n = 4
    } else if ((m == CURVE_NUMBER)) {
        n = 4
    } else {
        return 0
    }
    if ((ntoks < n)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 0
    f1@ while ((i < 5)) {
        x!![i] = 0.0
        i++
    }
    i = 1
    f2@ while ((i < n)) {
        if (!(getDouble(tok!![i], DElem(x!!, (i - 1))) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        i++
    }
    if (((((m == HORTON) || (m == MOD_HORTON))) && (ntoks > n))) {
        if (!(getDouble(tok!![n], DElem(x!!, (n - 1))) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![n])
        }
    }
    Subcatch!![j]!!.infil = j
    Subcatch!![j]!!.infilModel = m
    when (m) {
        HORTON, MOD_HORTON -> {
            status = horton_setParams(Infil!![j]!!.horton, x)
        }
        GREEN_AMPT, MOD_GREEN_AMPT -> {
            status = grnampt_setParams(Infil!![j]!!.grnAmpt, x)
        }
        CURVE_NUMBER -> {
            status = curvenum_setParams(Infil!![j]!!.curveNum, x)
        }
        else -> {
            status = 1
        }
    }
    if (!(status != 0)) {
        return error_setInpError(ERR_INFIL_PARAMS, __L0)
    }
    return 0
}

// infil.c:201
fun infil_initState(j: Int) {
    when (Subcatch!![j]!!.infilModel) {
        HORTON, MOD_HORTON -> {
            horton_initState(Infil!![j]!!.horton)
        }
        GREEN_AMPT, MOD_GREEN_AMPT -> {
            grnampt_initState(Infil!![j]!!.grnAmpt)
        }
        CURVE_NUMBER -> {
            curvenum_initState(Infil!![j]!!.curveNum)
        }
    }
}

// infil.c:221
fun infil_getState(j: Int, x: DoubleArray?) {
    when (Subcatch!![j]!!.infilModel) {
        HORTON, MOD_HORTON -> {
            horton_getState(Infil!![j]!!.horton, x)
        }
        GREEN_AMPT, MOD_GREEN_AMPT -> {
            grnampt_getState(Infil!![j]!!.grnAmpt, x)
        }
        CURVE_NUMBER -> {
            curvenum_getState(Infil!![j]!!.curveNum, x)
        }
    }
}

// infil.c:241
fun infil_setState(j: Int, x: DoubleArray?) {
    when (Subcatch!![j]!!.infilModel) {
        HORTON, MOD_HORTON -> {
            horton_setState(Infil!![j]!!.horton, x)
        }
        GREEN_AMPT, MOD_GREEN_AMPT -> {
            grnampt_setState(Infil!![j]!!.grnAmpt, x)
        }
        CURVE_NUMBER -> {
            curvenum_setState(Infil!![j]!!.curveNum, x)
        }
    }
}

// infil.c:262
fun infil_setInfilFactor(j: Int) {
    var m: Int = 0
    var p: Int = 0
    InfilFactor = Adjust!!.hydconFactor
    if ((j >= 0)) {
        p = Subcatch!![j]!!.infilPattern
        if (((p >= 0) && (Pattern!![p]!!.type == MONTHLY_PATTERN))) {
            m = (datetime_monthOfYear(getDateTime(OldRunoffTime)) - 1)
            InfilFactor = Pattern!![p]!!.factor!![m]
        }
    }
}

// infil.c:288
fun infil_getInfil(j: Int, tstep: Double, rainfall: Double, runon: Double, __depth: Double): Double {
    var depth: Double = __depth
    when (Subcatch!![j]!!.infilModel) {
        HORTON -> {
            return horton_getInfil(Infil!![j]!!.horton, tstep, (rainfall + runon), depth)
        }
        MOD_HORTON -> {
            return modHorton_getInfil(Infil!![j]!!.horton, tstep, (rainfall + runon), depth)
        }
        GREEN_AMPT, MOD_GREEN_AMPT -> {
            return grnampt_getInfil(Infil!![j]!!.grnAmpt, tstep, (rainfall + runon), depth, Subcatch!![j]!!.infilModel)
        }
        CURVE_NUMBER -> {
            depth += (runon * tstep)
            return curvenum_getInfil(Infil!![j]!!.curveNum, tstep, rainfall, depth)
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("infil_getInfil: no return")
}

// infil.c:325
fun horton_setParams(infil: THorton?, p: DoubleArray?): Int {
    var k: Int = 0
    k = 0
    f1@ while ((k < 5)) {
        if ((p!![k] < 0.0)) {
            return 0
        }
        k++
    }
    infil!!.f0 = (p!![0] / UCF(RAINFALL))
    infil!!.fmin = (p!![1] / UCF(RAINFALL))
    infil!!.decay = (p!![2] / 3600.0)
    if ((p!![3] == 0.0)) {
        p!![3] = 1e-06
    }
    infil!!.regen = (((-(Glibm.log((1.0 - 0.98)))) / p!![3]) / 86400.0)
    infil!!.Fmax = (p!![4] / UCF(RAINDEPTH))
    if ((infil!!.f0 < infil!!.fmin)) {
        return 0
    }
    return 1
}

// infil.c:357
fun horton_initState(infil: THorton?) {
    infil!!.tp = 0.0
    infil!!.Fe = 0.0
}

// infil.c:370
fun horton_getState(infil: THorton?, x: DoubleArray?) {
    x!![0] = infil!!.tp
    x!![1] = infil!!.Fe
}

// infil.c:376
fun horton_setState(infil: THorton?, x: DoubleArray?) {
    infil!!.tp = x!![0]
    infil!!.Fe = x!![1]
}

// infil.c:384
fun horton_getInfil(infil: THorton?, tstep: Double, irate: Double, depth: Double): Double {
    var iter: Int = 0
    var fa: Double = 0.0
    var fp: Double = 0.0
    var Fp: Double = 0.0
    var F1: Double = 0.0
    var t1: Double = 0.0
    var tlim: Double = 0.0
    var ex: Double = 0.0
    var kt: Double = 0.0
    var FF: Double = 0.0
    var FF1: Double = 0.0
    var r: Double = 0.0
    var f0: Double = (infil!!.f0 * InfilFactor)
    var fmin: Double = (infil!!.fmin * InfilFactor)
    var Fmax: Double = infil!!.Fmax
    var tp: Double = infil!!.tp
    var df: Double = (f0 - fmin)
    var kd: Double = infil!!.decay
    var kr: Double = (infil!!.regen * Evap!!.recoveryFactor)
    if ((((df < 0.0) || (kd < 0.0)) || (kr < 0.0))) {
        return 0.0
    }
    if (((df == 0.0) || (kd == 0.0))) {
        fp = f0
        fa = (irate + (depth / tstep))
        if ((fp > fa)) {
            fp = fa
        }
        return ((if ((((0.0) >= (fp)))) (0.0) else (fp)))
    }
    fa = (irate + (depth / tstep))
    if ((fa > 1e-10)) {
        t1 = (tp + tstep)
        tlim = (16.0 / kd)
        if ((tp >= tlim)) {
            Fp = ((fmin * tp) + (df / kd))
            F1 = (Fp + (fmin * tstep))
        } else {
            Fp = ((fmin * tp) + ((df / kd) * ((1.0 - Glibm.exp(((-kd) * tp))))))
            F1 = ((fmin * t1) + ((df / kd) * ((1.0 - Glibm.exp(((-kd) * t1))))))
        }
        fp = (((F1 - Fp)) / tstep)
        fp = ((if ((((fp) >= (fmin)))) (fp) else (fmin)))
        if ((fp > fa)) {
            fp = fa
        }
        if ((t1 > tlim)) {
            tp = t1
        } else if ((fp < fa)) {
            tp = t1
        } else {
            F1 = (Fp + (fp * tstep))
            tp = (tp + (tstep / 2.0))
            iter = 1
            f1@ while ((iter <= 20)) {
                kt = ((if ((((60.0) <= ((kd * tp))))) (60.0) else ((kd * tp))))
                ex = Glibm.exp((-kt))
                FF = (((fmin * tp) + ((df / kd) * ((1.0 - ex)))) - F1)
                FF1 = (fmin + (df * ex))
                r = (FF / FF1)
                tp = (tp - r)
                if ((abs(r) <= (0.001 * tstep))) {
                    break@f1
                }
                iter++
            }
        }
        if ((Fmax > 0.0)) {
            if (((infil!!.Fe + (fp * tstep)) > Fmax)) {
                fp = (((Fmax - infil!!.Fe)) / tstep)
            }
            fp = ((if ((((fp) >= (0.0)))) (fp) else (0.0)))
            infil!!.Fe += (fp * tstep)
        }
    } else if ((kr > 0.0)) {
        r = Glibm.exp(((-kr) * tstep))
        tp = (1.0 - Glibm.exp(((-kd) * tp)))
        tp = ((-(Glibm.log((1.0 - (r * tp))))) / kd)
        if ((Fmax > 0.0)) {
            infil!!.Fe = ((fmin * tp) + (((df / kd)) * ((1.0 - Glibm.exp(((-kd) * tp))))))
        }
    }
    infil!!.tp = tp
    return fp
}

// infil.c:496
fun modHorton_getInfil(infil: THorton?, tstep: Double, irate: Double, depth: Double): Double {
    var f: Double = 0.0
    var fp: Double = 0.0
    var fa: Double = 0.0
    var f0: Double = (infil!!.f0 * InfilFactor)
    var fmin: Double = (infil!!.fmin * InfilFactor)
    var df: Double = (f0 - fmin)
    var kd: Double = infil!!.decay
    var kr: Double = (infil!!.regen * Evap!!.recoveryFactor)
    if ((((df < 0.0) || (kd < 0.0)) || (kr < 0.0))) {
        return 0.0
    }
    if (((df == 0.0) || (kd == 0.0))) {
        fp = f0
        fa = (irate + (depth / tstep))
        if ((fp > fa)) {
            fp = fa
        }
        return ((if ((((0.0) >= (fp)))) (0.0) else (fp)))
    }
    fa = (irate + (depth / tstep))
    if ((fa > 1e-10)) {
        if (((infil!!.Fmax > 0.0) && (infil!!.Fe >= infil!!.Fmax))) {
            return 0.0
        }
        fp = (f0 - (kd * infil!!.Fe))
        fp = ((if ((((fp) >= (fmin)))) (fp) else (fmin)))
        f = ((if ((((fa) <= (fp)))) (fa) else (fp)))
        infil!!.Fe += (((if ((((((f - fmin))) >= (0.0)))) (((f - fmin))) else (0.0))) * tstep)
        if ((infil!!.Fmax > 0.0)) {
            infil!!.Fe = ((if ((((infil!!.Fe) >= (infil!!.Fmax)))) (infil!!.Fe) else (infil!!.Fmax)))
        }
    } else if ((kr > 0.0)) {
        infil!!.Fe *= (Glibm.exp(((-kr) * tstep)))
        infil!!.Fe = ((if ((((infil!!.Fe) >= (0.0)))) (infil!!.Fe) else (0.0)))
    }
    return f
}

// infil.c:559
fun grnampt_getParams(j: Int, p: DoubleArray?) {
    p!![0] = (Infil!![j]!!.grnAmpt!!.S * UCF(RAINDEPTH))
    p!![1] = (Infil!![j]!!.grnAmpt!!.Ks * UCF(RAINFALL))
    p!![2] = Infil!![j]!!.grnAmpt!!.IMDmax
}

// infil.c:574
fun grnampt_setParams(infil: TGrnAmpt?, p: DoubleArray?): Int {
    var ksat: Double = 0.0
    if (((((p!![0] < 0.0) || (p!![1] <= 0.0)) || (p!![2] < 0.0)) || (p!![2] > 1.0))) {
        return 0
    }
    infil!!.S = (p!![0] / UCF(RAINDEPTH))
    infil!!.Ks = (p!![1] / UCF(RAINFALL))
    infil!!.IMDmax = p!![2]
    ksat = ((infil!!.Ks * 12.0) * 3600.0)
    infil!!.Lu = ((4.0 * sqrt(ksat)) / 12.0)
    return 1
}

// infil.c:597
fun grnampt_initState(infil: TGrnAmpt?) {
    if ((infil == null)) {
        return
    }
    infil!!.IMD = infil!!.IMDmax
    infil!!.Fu = 0.0
    infil!!.F = 0.0
    infil!!.Sat = 0
    infil!!.T = 0.0
}

// infil.c:612
fun grnampt_getState(infil: TGrnAmpt?, x: DoubleArray?) {
    x!![0] = infil!!.IMD
    x!![1] = infil!!.F
    x!![2] = infil!!.Fu
    x!![3] = (infil!!.Sat).toDouble()
    x!![4] = infil!!.T
}

// infil.c:621
fun grnampt_setState(infil: TGrnAmpt?, x: DoubleArray?) {
    infil!!.IMD = x!![0]
    infil!!.F = x!![1]
    infil!!.Fu = x!![2]
    infil!!.Sat = (R.D2I(x!![3])).toByte().toInt()
    infil!!.T = x!![4]
}

// infil.c:632
fun grnampt_getInfil(infil: TGrnAmpt?, tstep: Double, irate: Double, depth: Double, modelType: Int): Double {
    Fumax = ((infil!!.IMDmax * infil!!.Lu) * sqrt(InfilFactor))
    infil!!.T -= tstep
    if ((infil!!.Sat != 0)) {
        return grnampt_getSatInfil(infil, tstep, irate, depth)
    } else {
        return grnampt_getUnsatInfil(infil, tstep, irate, depth, modelType)
    }
    throw IllegalStateException("grnampt_getInfil: no return")
}

// infil.c:660
fun grnampt_getUnsatInfil(infil: TGrnAmpt?, tstep: Double, irate: Double, depth: Double, modelType: Int): Double {
    var ia: Double = 0.0
    var c1: Double = 0.0
    var F2: Double = 0.0
    var dF: Double = 0.0
    var Fs: Double = 0.0
    var kr: Double = 0.0
    var ts: Double = 0.0
    var ks: Double = (infil!!.Ks * InfilFactor)
    var lu: Double = (infil!!.Lu * sqrt(InfilFactor))
    ia = (irate + (depth / tstep))
    if ((ia < 1e-10)) {
        ia = 0.0
    }
    if ((ia == 0.0)) {
        if ((infil!!.Fu <= 0.0)) {
            return 0.0
        }
        kr = ((lu / 90000.0) * Evap!!.recoveryFactor)
        dF = ((kr * Fumax) * tstep)
        infil!!.F -= dF
        infil!!.Fu -= dF
        if ((infil!!.Fu <= 0.0)) {
            infil!!.Fu = 0.0
            infil!!.F = 0.0
            infil!!.IMD = infil!!.IMDmax
            return 0.0
        }
        if ((infil!!.T <= 0.0)) {
            infil!!.IMD = (((Fumax - infil!!.Fu)) / lu)
            infil!!.F = 0.0
        }
        return 0.0
    }
    if ((ia <= ks)) {
        dF = (ia * tstep)
        infil!!.F += dF
        infil!!.Fu += dF
        infil!!.Fu = ((if ((((infil!!.Fu) <= (Fumax)))) (infil!!.Fu) else (Fumax)))
        if (((modelType == GREEN_AMPT) && (infil!!.T <= 0.0))) {
            infil!!.IMD = (((Fumax - infil!!.Fu)) / lu)
            infil!!.F = 0.0
        }
        return ia
    }
    infil!!.T = ((5400.0 / lu) / Evap!!.recoveryFactor)
    Fs = (((ks * ((infil!!.S + depth))) * infil!!.IMD) / ((ia - ks)))
    if ((infil!!.F > Fs)) {
        infil!!.Sat = 1
        return grnampt_getSatInfil(infil, tstep, irate, depth)
    }
    if (((infil!!.F + (ia * tstep)) < Fs)) {
        dF = (ia * tstep)
        infil!!.F += dF
        infil!!.Fu += dF
        infil!!.Fu = ((if ((((infil!!.Fu) <= (Fumax)))) (infil!!.Fu) else (Fumax)))
        return ia
    }
    ts = (tstep - (((Fs - infil!!.F)) / ia))
    if ((ts <= 0.0)) {
        ts = 0.0
    }
    c1 = (((infil!!.S + depth)) * infil!!.IMD)
    F2 = grnampt_getF2(Fs, c1, ks, ts)
    if ((F2 > (Fs + (ia * ts)))) {
        F2 = (Fs + (ia * ts))
    }
    dF = (F2 - infil!!.F)
    infil!!.F = F2
    infil!!.Fu += dF
    infil!!.Fu = ((if ((((infil!!.Fu) <= (Fumax)))) (infil!!.Fu) else (Fumax)))
    infil!!.Sat = 1
    return (dF / tstep)
}

// infil.c:767
fun grnampt_getSatInfil(infil: TGrnAmpt?, tstep: Double, irate: Double, depth: Double): Double {
    var ia: Double = 0.0
    var c1: Double = 0.0
    var dF: Double = 0.0
    var F2: Double = 0.0
    var ks: Double = (infil!!.Ks * InfilFactor)
    var lu: Double = (infil!!.Lu * sqrt(InfilFactor))
    ia = (irate + (depth / tstep))
    if ((ia < 1e-10)) {
        return 0.0
    }
    infil!!.T = ((5400.0 / lu) / Evap!!.recoveryFactor)
    c1 = (((infil!!.S + depth)) * infil!!.IMD)
    F2 = grnampt_getF2(infil!!.F, c1, ks, tstep)
    dF = (F2 - infil!!.F)
    if ((dF > (ia * tstep))) {
        dF = (ia * tstep)
        infil!!.Sat = 0
    }
    infil!!.F += dF
    infil!!.Fu += dF
    infil!!.Fu = ((if ((((infil!!.Fu) <= (Fumax)))) (infil!!.Fu) else (Fumax)))
    return (dF / tstep)
}

// infil.c:813
fun grnampt_getF2(f1: Double, c1: Double, ks: Double, ts: Double): Double {
    var i: Int = 0
    var f2: Double = f1
    var f2min: Double = 0.0
    var df2: Double = 0.0
    var c2: Double = 0.0
    f2min = (f1 + (ks * ts))
    if ((c1 == 0.0)) {
        return f2min
    }
    if (((ts < 10.0) && (f1 > (0.01 * c1)))) {
        f2 = (f1 + ((ks * ((1.0 + (c1 / f1)))) * ts))
        return ((if ((((f2) >= (f2min)))) (f2) else (f2min)))
    }
    c2 = ((c1 * Glibm.log((f1 + c1))) - (ks * ts))
    i = 1
    f1@ while ((i <= 20)) {
        df2 = (((((f2 - f1) - (c1 * Glibm.log((f2 + c1)))) + c2)) / ((1.0 - (c1 / ((f2 + c1))))))
        if ((abs(df2) < 0.00001)) {
            return ((if ((((f2) >= (f2min)))) (f2) else (f2min)))
        }
        f2 -= df2
        i++
    }
    return f2min
}

// infil.c:861
fun curvenum_setParams(infil: TCurveNum?, p: DoubleArray?): Int {
    if ((p!![0] < 10.0)) {
        p!![0] = 10.0
    }
    if ((p!![0] > 99.0)) {
        p!![0] = 99.0
    }
    infil!!.Smax = ((((1000.0 / p!![0]) - 10.0)) / 12.0)
    if ((infil!!.Smax < 0.0)) {
        return 0
    }
    if ((p!![2] > 0.0)) {
        infil!!.regen = (1.0 / ((p!![2] * 86400.0)))
    } else {
        return 0
    }
    infil!!.Tmax = (0.06 / infil!!.regen)
    return 1
}

// infil.c:888
fun curvenum_initState(infil: TCurveNum?) {
    infil!!.S = infil!!.Smax
    infil!!.P = 0.0
    infil!!.F = 0.0
    infil!!.T = 0.0
    infil!!.Se = infil!!.Smax
    infil!!.f = 0.0
}

// infil.c:903
fun curvenum_getState(infil: TCurveNum?, x: DoubleArray?) {
    x!![0] = infil!!.S
    x!![1] = infil!!.P
    x!![2] = infil!!.F
    x!![3] = infil!!.T
    x!![4] = infil!!.Se
    x!![5] = infil!!.f
}

// infil.c:913
fun curvenum_setState(infil: TCurveNum?, x: DoubleArray?) {
    infil!!.S = x!![0]
    infil!!.P = x!![1]
    infil!!.F = x!![2]
    infil!!.T = x!![3]
    infil!!.Se = x!![4]
    infil!!.f = x!![5]
}

// infil.c:925
fun curvenum_getInfil(infil: TCurveNum?, tstep: Double, irate: Double, depth: Double): Double {
    var F1: Double = 0.0
    var f1: Double = 0.0
    var fa: Double = (irate + (depth / tstep))
    if ((irate > 1e-10)) {
        if ((infil!!.T >= infil!!.Tmax)) {
            infil!!.P = 0.0
            infil!!.F = 0.0
            infil!!.f = 0.0
            infil!!.Se = infil!!.S
        }
        infil!!.T = 0.0
        infil!!.P += (irate * tstep)
        F1 = (infil!!.P * ((1.0 - (infil!!.P / ((infil!!.P + infil!!.Se))))))
        f1 = (((F1 - infil!!.F)) / tstep)
        if (((f1 < 0.0) || (infil!!.S <= 0.0))) {
            f1 = 0.0
        }
    } else {
        if (((depth > 0.004167) && (infil!!.S > 0.0))) {
            f1 = infil!!.f
            if (((f1 * tstep) > infil!!.S)) {
                f1 = (infil!!.S / tstep)
            }
        } else {
            infil!!.T += tstep
        }
    }
    if ((f1 > 0.0)) {
        f1 = ((if ((((f1) <= (fa)))) (f1) else (fa)))
        f1 = ((if ((((f1) >= (0.0)))) (f1) else (0.0)))
        infil!!.F += (f1 * tstep)
        if ((infil!!.regen > 0.0)) {
            infil!!.S -= (f1 * tstep)
            if ((infil!!.S < 0.0)) {
                infil!!.S = 0.0
            }
        }
    } else {
        infil!!.S += (((infil!!.regen * infil!!.Smax) * tstep) * Evap!!.recoveryFactor)
        if ((infil!!.S > infil!!.Smax)) {
            infil!!.S = infil!!.Smax
        }
    }
    infil!!.f = f1
    return f1
}

