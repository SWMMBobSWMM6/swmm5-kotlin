// Generated from EPA SWMM 5.2.4 (culvert.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_culvert")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MAX_CULVERT_CODE: Int = 57
@JvmField var Params: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.00), doubleArrayOf(1.0, 0.0098, 2.00, 0.0398, 0.67), doubleArrayOf(1.0, 0.0018, 2.00, 0.0292, 0.74), doubleArrayOf(1.0, 0.0045, 2.00, 0.0317, 0.69), doubleArrayOf(1.0, 0.0078, 2.00, 0.0379, 0.69), doubleArrayOf(1.0, 0.0210, 1.33, 0.0463, 0.75), doubleArrayOf(1.0, 0.0340, 1.50, 0.0553, 0.54), doubleArrayOf(1.0, 0.0018, 2.50, 0.0300, 0.74), doubleArrayOf(1.0, 0.0018, 2.50, 0.0243, 0.83), doubleArrayOf(1.0, 0.026, 1.0, 0.0347, 0.81), doubleArrayOf(1.0, 0.061, 0.75, 0.0400, 0.80), doubleArrayOf(1.0, 0.061, 0.75, 0.0423, 0.82), doubleArrayOf(2.0, 0.510, 0.667, 0.0309, 0.80), doubleArrayOf(2.0, 0.486, 0.667, 0.0249, 0.83), doubleArrayOf(2.0, 0.515, 0.667, 0.0375, 0.79), doubleArrayOf(2.0, 0.495, 0.667, 0.0314, 0.82), doubleArrayOf(2.0, 0.486, 0.667, 0.0252, 0.865), doubleArrayOf(2.0, 0.545, 0.667, 0.04505, 0.73), doubleArrayOf(2.0, 0.533, 0.667, 0.0425, 0.705), doubleArrayOf(2.0, 0.522, 0.667, 0.0402, 0.68), doubleArrayOf(2.0, 0.498, 0.667, 0.0327, 0.75), doubleArrayOf(2.0, 0.497, 0.667, 0.0339, 0.803), doubleArrayOf(2.0, 0.493, 0.667, 0.0361, 0.806), doubleArrayOf(2.0, 0.495, 0.667, 0.0386, 0.71), doubleArrayOf(2.0, 0.497, 0.667, 0.0302, 0.835), doubleArrayOf(2.0, 0.495, 0.667, 0.0252, 0.881), doubleArrayOf(2.0, 0.493, 0.667, 0.0227, 0.887), doubleArrayOf(1.0, 0.0083, 2.00, 0.0379, 0.69), doubleArrayOf(1.0, 0.0145, 1.75, 0.0419, 0.64), doubleArrayOf(1.0, 0.0340, 1.50, 0.0496, 0.57), doubleArrayOf(1.0, 0.0100, 2.00, 0.0398, 0.67), doubleArrayOf(1.0, 0.0018, 2.50, 0.0292, 0.74), doubleArrayOf(1.0, 0.0045, 2.00, 0.0317, 0.69), doubleArrayOf(1.0, 0.0100, 2.00, 0.0398, 0.67), doubleArrayOf(1.0, 0.0018, 2.50, 0.0292, 0.74), doubleArrayOf(1.0, 0.0095, 2.00, 0.0317, 0.69), doubleArrayOf(1.0, 0.0083, 2.00, 0.0379, 0.69), doubleArrayOf(1.0, 0.0300, 1.00, 0.0463, 0.75), doubleArrayOf(1.0, 0.0340, 1.50, 0.0496, 0.57), doubleArrayOf(1.0, 0.0300, 1.50, 0.0496, 0.57), doubleArrayOf(1.0, 0.0088, 2.00, 0.0368, 0.68), doubleArrayOf(1.0, 0.0030, 2.00, 0.0269, 0.77), doubleArrayOf(1.0, 0.0300, 1.50, 0.0496, 0.57), doubleArrayOf(1.0, 0.0088, 2.00, 0.0368, 0.68), doubleArrayOf(1.0, 0.0030, 2.00, 0.0269, 0.77), doubleArrayOf(1.0, 0.0083, 2.00, 0.0379, 0.69), doubleArrayOf(1.0, 0.0300, 1.00, 0.0473, 0.75), doubleArrayOf(1.0, 0.0340, 1.50, 0.0496, 0.57), doubleArrayOf(2.0, 0.534, 0.555, 0.0196, 0.90), doubleArrayOf(2.0, 0.519, 0.640, 0.0210, 0.90), doubleArrayOf(2.0, 0.536, 0.622, 0.0368, 0.83), doubleArrayOf(2.0, 0.5035, 0.719, 0.0478, 0.80), doubleArrayOf(2.0, 0.547, 0.800, 0.0598, 0.75), doubleArrayOf(2.0, 0.475, 0.667, 0.0179, 0.97), doubleArrayOf(2.0, 0.560, 0.667, 0.0446, 0.85), doubleArrayOf(2.0, 0.560, 0.667, 0.0378, 0.87), doubleArrayOf(2.0, 0.500, 0.667, 0.0446, 0.65), doubleArrayOf(2.0, 0.500, 0.667, 0.0378, 0.71))

// culvert.c:174
fun culvert_getInflow(j: Int, q0: Double, h: Double): Double {
    var code: Int = 0
    var k: Int = 0
    var condition: Int = 0
    var y: Double = 0.0
    var y1: Double = 0.0
    var y2: Double = 0.0
    var q: Double = 0.0
    var culvert: TCulvert? = TCulvert()
    if ((Link!![j]!!.type != CONDUIT)) {
        return q0
    }
    culvert!!.xsect = Link!![j]!!.xsect
    code = culvert!!.xsect!!.culvertCode
    if (((code <= 0) || (code > MAX_CULVERT_CODE))) {
        return q0
    }
    k = Link!![j]!!.subIndex
    culvert!!.yFull = culvert!!.xsect!!.yFull
    culvert!!.ad = (culvert!!.xsect!!.aFull * sqrt(culvert!!.yFull))
    when (code) {
        5, 37, 46 -> {
            culvert!!.scf = ((-7.0) * Conduit!![k]!!.slope)
        }
        else -> {
            culvert!!.scf = (0.5 * Conduit!![k]!!.slope)
        }
    }
    y = (h - ((Node!![Link!![j]!!.node1]!!.invertElev + Link!![j]!!.offset1)))
    y2 = (culvert!!.yFull * ((((16.0 * Params!![code]!![C]) + Params!![code]!![Y]) - culvert!!.scf)))
    if ((y >= y2)) {
        q = getSubmergedFlow(code, y, culvert)
        condition = 2
    } else {
        y1 = (0.95 * culvert!!.yFull)
        if ((y <= y1)) {
            q = getUnsubmergedFlow(code, y, culvert)
            condition = 1
        } else {
            q = getTransitionFlow(code, y, y1, y2, culvert)
            condition = 0
        }
    }
    if ((q < q0)) {
        Link!![j]!!.inletControl = 1
        Link!![j]!!.dqdh = culvert!!.dQdH
        return q
    } else {
        return q0
    }
    throw IllegalStateException("culvert_getInflow: no return")
}

// culvert.c:257
fun getUnsubmergedFlow(code: Int, h: Double, culvert: TCulvert?): Double {
    var arg: Double = 0.0
    var q: Double = 0.0
    culvert!!.kk = Params!![code]!![K]
    culvert!!.mm = Params!![code]!![M]
    arg = ((h / culvert!!.yFull) / culvert!!.kk)
    if ((Params!![code]!![FORM] == 1.0)) {
        q = getForm1Flow(h, culvert)
    } else {
        q = (culvert!!.ad * Glibm.pow(arg, (1.0 / culvert!!.mm)))
    }
    culvert!!.dQdH = ((q / h) / culvert!!.mm)
    return q
}

// culvert.c:288
fun getSubmergedFlow(code: Int, h: Double, culvert: TCulvert?): Double {
    var cc: Double = Params!![code]!![C]
    var yy: Double = Params!![code]!![Y]
    var arg: Double = (((((h / culvert!!.yFull) - yy) + culvert!!.scf)) / cc)
    var q: Double = 0.0
    if ((arg <= 0.0)) {
        culvert!!.dQdH = 0.0
        return 10000000000.0
    }
    q = (sqrt(arg) * culvert!!.ad)
    culvert!!.dQdH = ((((0.5 * q) / arg) / culvert!!.yFull) / cc)
    return q
}

// culvert.c:316
fun getTransitionFlow(code: Int, h: Double, h1: Double, h2: Double, culvert: TCulvert?): Double {
    var q1: Double = getUnsubmergedFlow(code, h1, culvert)
    var q2: Double = getSubmergedFlow(code, h2, culvert)
    var q: Double = (q1 + ((((q2 - q1)) * ((h - h1))) / ((h2 - h1))))
    culvert!!.dQdH = (((q2 - q1)) / ((h2 - h1)))
    return q
}

// culvert.c:339
fun getForm1Flow(h: Double, culvert: TCulvert?): Double {
    culvert!!.hPlus = ((h / culvert!!.yFull) + culvert!!.scf)
    findroot_Ridder((0.01 * h), h, 0.001, FP3(::form1Eqn), culvert)
    return culvert!!.qc
}

// culvert.c:363
fun form1Eqn(yc: Double, p: Any?): Double {
    var ac: Double = 0.0
    var wc: Double = 0.0
    var yh: Double = 0.0
    var culvert: TCulvert? = (p as TCulvert?)
    ac = xsect_getAofY(culvert!!.xsect, yc)
    wc = xsect_getWofY(culvert!!.xsect, yc)
    yh = (ac / wc)
    culvert!!.qc = (ac * sqrt((32.2 * yh)))
    return (((culvert!!.hPlus - (yc / culvert!!.yFull)) - ((yh / 2.0) / culvert!!.yFull)) - (culvert!!.kk * Glibm.pow((culvert!!.qc / culvert!!.ad), culvert!!.mm)))
}

