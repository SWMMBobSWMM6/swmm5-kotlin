// Generated from EPA SWMM 5.2.4 (roadway.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_roadway")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var N_Cr_Low_Paved: Int = 4
@JvmField var Cr_Low_Paved: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.0, 2.85), doubleArrayOf(0.2, 2.95), doubleArrayOf(0.7, 3.03), doubleArrayOf(4.0, 3.05))
@JvmField var N_Cr_Low_Gravel: Int = 8
@JvmField var Cr_Low_Gravel: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.0, 2.5), doubleArrayOf(0.5, 2.7), doubleArrayOf(1.0, 2.8), doubleArrayOf(1.5, 2.9), doubleArrayOf(2.0, 2.98), doubleArrayOf(2.5, 3.02), doubleArrayOf(3.0, 3.03), doubleArrayOf(4.0, 3.05))
@JvmField var N_Cr_High_Paved: Int = 2
@JvmField var Cr_High_Paved: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.15, 3.05), doubleArrayOf(0.25, 3.10))
@JvmField var N_Cr_High_Gravel: Int = 2
@JvmField var Cr_High_Gravel: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.15, 2.95), doubleArrayOf(0.30, 3.10))
@JvmField var N_Kt_Paved: Int = 9
@JvmField var Kt_Paved: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.8, 1.0), doubleArrayOf(0.85, 0.98), doubleArrayOf(0.90, 0.92), doubleArrayOf(0.93, 0.85), doubleArrayOf(0.95, 0.80), doubleArrayOf(0.97, 0.70), doubleArrayOf(0.98, 0.60), doubleArrayOf(0.99, 0.50), doubleArrayOf(1.00, 0.40))
@JvmField var N_Kt_Gravel: Int = 12
@JvmField var Kt_Gravel: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.75, 1.00), doubleArrayOf(0.80, 0.985), doubleArrayOf(0.83, 0.97), doubleArrayOf(0.86, 0.93), doubleArrayOf(0.89, 0.90), doubleArrayOf(0.90, 0.87), doubleArrayOf(0.92, 0.80), doubleArrayOf(0.94, 0.70), doubleArrayOf(0.96, 0.60), doubleArrayOf(0.98, 0.50), doubleArrayOf(0.99, 0.40), doubleArrayOf(1.00, 0.24))

// roadway.c:85
fun roadway_getInflow(j: Int, dir: Double, hRoad: Double, h1: Double, h2: Double): Double {
    var k: Int = 0
    var roadSurf: Int = 0
    var useVariableCd: Int = 0
    var length: Double = 0.0
    var roadWidth: Double = 0.0
    var hWr: Double = 0.0
    var ht: Double = 0.0
    var cD: Double = 0.0
    var q: Double = 0.0
    var dqdh: Double = 0.0
    if ((Link!![j]!!.type != WEIR)) {
        return 0.0
    }
    k = Link!![j]!!.subIndex
    roadWidth = Weir!![k]!!.roadWidth
    roadSurf = Weir!![k]!!.roadSurface
    cD = Weir!![k]!!.cDisch1
    if ((UnitSystem == SI)) {
        cD = (cD / 0.552)
    }
    useVariableCd = 0
    if (((roadWidth > 0.0) && (roadSurf >= 1))) {
        useVariableCd = 1
    }
    hWr = (h1 - hRoad)
    ht = (h2 - hRoad)
    if ((hWr > 0.0001)) {
        if ((useVariableCd != 0)) {
            cD = getCd(hWr, ht, roadWidth, roadSurf)
        }
        length = Link!![j]!!.xsect!!.wMax
        q = ((cD * length) * Glibm.pow(hWr, 1.5))
        dqdh = ((1.5 * q) / hWr)
    }
    Link!![j]!!.dqdh = dqdh
    Link!![j]!!.newDepth = ((if (((((h1 - hRoad)) >= (0.0)))) ((h1 - hRoad)) else (0.0)))
    Link!![j]!!.flowClass = SUBCRITICAL
    if ((hRoad > h2)) {
        if ((dir == 1.0)) {
            Link!![j]!!.flowClass = DN_CRITICAL
        } else {
            Link!![j]!!.flowClass = UP_CRITICAL
        }
    }
    return (dir * q)
}

// roadway.c:146
fun getCd(hWr: Double, ht: Double, roadWidth: Double, roadSurf: Int): Double {
    var kT: Double = 1.0
    var hL: Double = 0.0
    var htH: Double = 0.0
    var cR: Double = 0.0
    if ((hWr <= 0.0)) {
        return 0.0
    }
    hL = (hWr / roadWidth)
    if ((hL <= 0.15)) {
        if ((roadSurf == PAVED)) {
            cR = getY(hWr, Cr_Low_Paved, N_Cr_Low_Paved)
        } else {
            cR = getY(hWr, Cr_Low_Gravel, N_Cr_Low_Gravel)
        }
    } else {
        if ((roadSurf == PAVED)) {
            cR = getY(hL, Cr_High_Paved, N_Cr_High_Paved)
        } else {
            cR = getY(hL, Cr_High_Gravel, N_Cr_High_Gravel)
        }
    }
    if ((ht > 0.0)) {
        htH = (ht / hWr)
        if ((roadSurf == PAVED)) {
            kT = getY(htH, Kt_Paved, N_Kt_Paved)
        } else {
            kT = getY(htH, Kt_Gravel, N_Kt_Gravel)
        }
    }
    return (cR * kT)
}

// roadway.c:176
fun getY(x: Double, table: Array<DoubleArray?>?, n: Int): Double {
    var i: Int = 0
    var x1: Double = 0.0
    var y1: Double = 0.0
    var dx: Double = 0.0
    var dy: Double = 0.0
    if ((x <= table!![0]!![0])) {
        return table!![0]!![1]
    }
    if ((x >= (table!![(n - 1)])!![0])) {
        return (table!![(n - 1)])!![1]
    }
    i = 1
    f1@ while ((i < n)) {
        if ((x <= table!![i]!![0])) {
            x1 = (table!![(i - 1)])!![0]
            dx = (table!![i]!![0] - x1)
            y1 = (table!![(i - 1)])!![1]
            dy = (table!![i]!![1] - y1)
            return (y1 + ((((x - x1)) * dy) / dx))
        }
        i++
    }
    return (table!![(n - 1)])!![1]
}

