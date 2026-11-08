// Generated from EPA SWMM 5.2.4 (forcmain.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_forcmain")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var VISCOS: Double = 1.1E-5

// forcmain.c:37
fun forcemain_getEquivN(j: Int, k: Int): Double {
    var xsect: TXsect? = Link!![j]!!.xsect!!.copy()
    var f: Double = 0.0
    var d: Double = xsect!!.yFull
    when (ForceMainEqn) {
        H_W -> {
            return ((1.067 / xsect!!.rBot) * Glibm.pow((d / Conduit!![k]!!.slope), 0.04))
        }
        D_W -> {
            f = forcemain_getFricFactor(xsect!!.rBot, (d / 4.0), 1.0e12)
            return (sqrt((f / 185.0)) * Glibm.pow(d, ((1.0 / 6.0))))
        }
    }
    return Conduit!![k]!!.roughness
}

// forcmain.c:64
fun forcemain_getRoughFactor(j: Int, lengthFactor: Double): Double {
    var xsect: TXsect? = Link!![j]!!.xsect!!.copy()
    var r: Double = 0.0
    when (ForceMainEqn) {
        H_W -> {
            r = ((1.318 * xsect!!.rBot) * Glibm.pow(lengthFactor, 0.54))
            return (32.2 / Glibm.pow(r, 1.852))
        }
        D_W -> {
            return ((1.0 / 8.0) / lengthFactor)
        }
    }
    return 0.0
}

// forcmain.c:88
fun forcemain_getFricSlope(j: Int, v: Double, hrad: Double): Double {
    var re: Double = 0.0
    var f: Double = 0.0
    var xsect: TXsect? = Link!![j]!!.xsect!!.copy()
    when (ForceMainEqn) {
        H_W -> {
            return ((xsect!!.sBot * Glibm.pow(v, 0.852)) / Glibm.pow(hrad, 1.1667))
        }
        D_W -> {
            re = forcemain_getReynolds(v, hrad)
            f = forcemain_getFricFactor(xsect!!.rBot, hrad, re)
            return (((f * xsect!!.sBot) * v) / hrad)
        }
    }
    return 0.0
}

// forcmain.c:117
fun forcemain_getReynolds(v: Double, hrad: Double): Double {
    return (((4.0 * hrad) * v) / VISCOS)
}

// forcmain.c:130
fun forcemain_getFricFactor(e: Double, hrad: Double, __re: Double): Double {
    var re: Double = __re
    var f: Double = 0.0
    if ((re < 10.0)) {
        re = 10.0
    }
    if ((re <= 2000.0)) {
        f = (64.0 / re)
    } else if ((re < 4000.0)) {
        f = forcemain_getFricFactor(e, hrad, 4000.0)
        f = (0.032 + ((((f - 0.032)) * ((re - 2000.0))) / 2000.0))
    } else {
        f = ((e / 3.7) / ((4.0 * hrad)))
        if ((re < 1.0e10)) {
            f += (5.74 / Glibm.pow(re, 0.9))
        }
        f = Glibm.log10(f)
        f = ((0.25 / f) / f)
    }
    return f
}

