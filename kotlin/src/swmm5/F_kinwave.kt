// Generated from EPA SWMM 5.2.4 (kinwave.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_kinwave")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var WX: Double = 0.6
@JvmField var WT: Double = 0.6
@JvmField var EPSIL: Double = 0.001
@JvmField var Beta1: Double = 0.0
@JvmField var C1: Double = 0.0
@JvmField var C2: Double = 0.0
@JvmField var Afull: Double = 0.0
@JvmField var Qfull: Double = 0.0
@JvmField var pXsect: TXsect? = null

// kinwave.c:57
fun kinwave_execute(j: Int, qinflow: DRef?, qoutflow: DRef?, tStep: Double): Int {
    var k: Int = 0
    var result: Int = 1
    var dxdt: Double = 0.0
    var dq: Double = 0.0
    var ain: Double = 0.0
    val aout = DBox(0.0)
    var qin: Double = 0.0
    var qout: Double = 0.0
    var a1: Double = 0.0
    var a2: Double = 0.0
    var q1: Double = 0.0
    var q2: Double = 0.0
    var q3: Double = 0.0
    qoutflow!!.set((qinflow!!.get()))
    if ((Link!![j]!!.type != CONDUIT)) {
        return result
    }
    if ((Link!![j]!!.xsect!!.type == DUMMY)) {
        return result
    }
    pXsect = Link!![j]!!.xsect
    Qfull = Link!![j]!!.qFull
    Afull = Link!![j]!!.xsect!!.aFull
    k = Link!![j]!!.subIndex
    Beta1 = (Conduit!![k]!!.beta / Qfull)
    q1 = (Conduit!![k]!!.q1 / Qfull)
    q2 = (Conduit!![k]!!.q2 / Qfull)
    qin = (((qinflow!!.get()) / (Conduit!![k]!!.barrels).toDouble()) / Qfull)
    q3 = (link_getLossRate(j, KW, (qin * Qfull), tStep) / Qfull)
    a1 = (Conduit!![k]!!.a1 / Afull)
    a2 = (Conduit!![k]!!.a2 / Afull)
    if ((qin >= 1.0)) {
        ain = 1.0
    } else {
        ain = (xsect_getAofS(pXsect, (qin / Beta1)) / Afull)
    }
    if (((qin <= 1e-06) && (q2 <= 1e-06))) {
        qout = 0.0
        aout.v = 0.0
    } else {
        dxdt = (((link_getLength(j) / tStep) * Afull) / Qfull)
        dq = (q2 - q1)
        C1 = ((dxdt * WT) / WX)
        C2 = (((1.0 - WT)) * ((ain - a1)))
        C2 = (C2 - (WT * a2))
        C2 = ((C2 * dxdt) / WX)
        C2 = ((C2 + ((((1.0 - WX)) / WX) * dq)) - qin)
        C2 = (C2 + (q3 / WX))
        aout.v = a2
        result = solveContinuity(qin, ain, aout)
        if ((result == (-1))) {
            report_writeErrorMsg(ERR_KINWAVE, Link!![j]!!.ID)
            return 1
        }
        if ((result <= 0)) {
            result = 1
        }
        qout = (Beta1 * xsect_getSofA(pXsect, (aout.v * Afull)))
        if ((qin > 1.0)) {
            qin = 1.0
        }
    }
    Conduit!![k]!!.q1 = (qin * Qfull)
    Conduit!![k]!!.a1 = (ain * Afull)
    Conduit!![k]!!.q2 = (qout * Qfull)
    Conduit!![k]!!.a2 = (aout.v * Afull)
    Conduit!![k]!!.fullState = link_getFullState(Conduit!![k]!!.a1, Conduit!![k]!!.a2, Afull)
    qinflow!!.set((Conduit!![k]!!.q1 * (Conduit!![k]!!.barrels).toDouble()))
    qoutflow!!.set((Conduit!![k]!!.q2 * (Conduit!![k]!!.barrels).toDouble()))
    return result
}

// kinwave.c:170
fun solveContinuity(qin: Double, ain: Double, aout: DRef?): Int {
    var n: Int = 0
    var aLo: Double = 0.0
    var aHi: Double = 0.0
    var aTmp: Double = 0.0
    var fLo: Double = 0.0
    var fHi: Double = 0.0
    var tol: Double = EPSIL
    aHi = 1.0
    fHi = ((1.0 + C1) + C2)
    aLo = (xsect_getAmax(pXsect) / Afull)
    if ((aLo < aHi)) {
        fLo = ((((Beta1 * pXsect!!.sMax)) + ((C1 * aLo))) + C2)
    } else {
        fLo = fHi
    }
    if (((fHi * fLo) > 0.0)) {
        aHi = aLo
        fHi = fLo
        aLo = 0.0
        fLo = C2
    }
    if (((fHi * fLo) <= 0.0)) {
        if (((aout!!.get() < aLo) || (aout!!.get() > aHi))) {
            aout!!.set((0.5 * ((aLo + aHi))))
        }
        if ((fLo > fHi)) {
            aTmp = aLo
            aLo = aHi
            aHi = aTmp
        }
        n = findroot_Newton(aLo, aHi, aout, tol, FP4(::evalContinuity), null)
        if ((n <= 0)) {
            n = (-1)
        }
    } else if ((fLo < 0.0)) {
        if ((qin > 1.0)) {
            aout!!.set(ain)
        } else {
            aout!!.set(1.0)
        }
        n = (-2)
    } else if ((fLo > 0.0)) {
        aout!!.set(0.0)
        n = (-3)
    } else {
        n = (-1)
    }
    return n
}

// kinwave.c:261
fun evalContinuity(a: Double, f: DRef?, df: DRef?, p: Any?) {
    f!!.set(((((Beta1 * xsect_getSofA(pXsect, (a * Afull)))) + ((C1 * a))) + C2))
    df!!.set(((((Beta1 * Afull) * xsect_getdSdA(pXsect, (a * Afull)))) + C1))
}

