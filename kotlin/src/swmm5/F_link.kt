// Generated from EPA SWMM 5.2.4 (link.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_link")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MIN_DELTA_Z: Double = 0.001

// link.c:137
fun link_readParams(j: Int, type: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    when (type) {
        CONDUIT -> {
            return conduit_readParams(j, k, tok, ntoks)
        }
        PUMP -> {
            return pump_readParams(j, k, tok, ntoks)
        }
        ORIFICE -> {
            return orifice_readParams(j, k, tok, ntoks)
        }
        WEIR -> {
            return weir_readParams(j, k, tok, ntoks)
        }
        OUTLET -> {
            return outlet_readParams(j, k, tok, ntoks)
        }
        else -> {
            return 0
        }
    }
    throw IllegalStateException("link_readParams: no return")
}

// link.c:162
fun link_readXsectParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var x: DoubleArray? = DoubleArray(4)
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(LINK, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = findmatch(tok!![1], XsectTypeWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    if ((Link!![j]!!.type == CONDUIT)) {
        Conduit!![Link!![j]!!.subIndex]!!.barrels = 1
    }
    Link!![j]!!.xsect!!.culvertCode = 0
    if ((k == IRREGULAR)) {
        i = project_findObject(TRANSECT, tok!![2])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        Link!![j]!!.xsect!!.type = k
        Link!![j]!!.xsect!!.transect = i
        return 0
    } else if ((k == STREET_XSECT)) {
        i = project_findObject(STREET, tok!![2])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        Link!![j]!!.xsect!!.type = k
        Link!![j]!!.xsect!!.transect = i
        return 0
    } else {
        if ((ntoks < 6)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if ((k == CUSTOM)) {
            if (((!(getDouble(tok!![2], DElem(x!!, 0)) != 0)) || (x!![0] <= 0.0))) {
                return error_setInpError(ERR_NUMBER, tok!![2])
            }
            i = project_findObject(CURVE, tok!![3])
            if ((i < 0)) {
                return error_setInpError(ERR_NAME, tok!![3])
            }
            Link!![j]!!.xsect!!.type = k
            Link!![j]!!.xsect!!.transect = i
            Link!![j]!!.xsect!!.yFull = (x!![0] / UCF(LENGTH))
        } else {
            i = 2
            f1@ while ((i <= 5)) {
                if (!(getDouble(tok!![i], DElem(x!!, (i - 2))) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![i])
                }
                i++
            }
        }
        if (((Link!![j]!!.type != CONDUIT) && (k == RECT_OPEN))) {
            x!![2] = 0.0
            x!![3] = 0.0
        }
        if (!(xsect_setParams(Link!![j]!!.xsect, k, x, UCF(LENGTH)) != 0)) {
            return error_setInpError(ERR_NUMBER, __L0)
        }
        if (((Link!![j]!!.type == CONDUIT) && (ntoks >= 7))) {
            i = R.atoi(tok!![6])
            if ((i <= 0)) {
                return error_setInpError(ERR_NUMBER, tok!![6])
            } else {
                Conduit!![Link!![j]!!.subIndex]!!.barrels = (i).toByte().toInt()
            }
        }
        if (((Link!![j]!!.type == CONDUIT) && (ntoks >= 8))) {
            i = R.atoi(tok!![7])
            if ((i < 0)) {
                return error_setInpError(ERR_NUMBER, tok!![7])
            } else {
                Link!![j]!!.xsect!!.culvertCode = i
            }
        }
    }
    return 0
}

// link.c:271
fun link_readLossParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var x: DoubleArray? = DoubleArray(3)
    val seepRate = DBox(0.0)
    if ((ntoks < 4)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(LINK, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    i = 1
    f1@ while ((i <= 3)) {
        if (((!(getDouble(tok!![i], DElem(x!!, (i - 1))) != 0)) || (x!![(i - 1)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        i++
    }
    k = 0
    if ((ntoks >= 5)) {
        k = findmatch(tok!![4], NoYesWords)
        if ((k < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![4])
        }
    }
    if ((ntoks >= 6)) {
        if (!(getDouble(tok!![5], seepRate) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
    }
    Link!![j]!!.cLossInlet = x!![0]
    Link!![j]!!.cLossOutlet = x!![1]
    Link!![j]!!.cLossAvg = x!![2]
    Link!![j]!!.hasFlapGate = k
    Link!![j]!!.seepRate = (seepRate.v / UCF(RAINFALL))
    return 0
}

// link.c:315
fun link_setParams(j: Int, type: Int, n1: Int, n2: Int, k: Int, x: DoubleArray?) {
    Link!![j]!!.node1 = n1
    Link!![j]!!.node2 = n2
    Link!![j]!!.type = type
    Link!![j]!!.subIndex = k
    Link!![j]!!.offset1 = 0.0
    Link!![j]!!.offset2 = 0.0
    Link!![j]!!.q0 = 0.0
    Link!![j]!!.qFull = 0.0
    Link!![j]!!.setting = 1.0
    Link!![j]!!.targetSetting = 1.0
    Link!![j]!!.hasFlapGate = 0
    Link!![j]!!.qLimit = 0.0
    Link!![j]!!.direction = 1
    when (type) {
        CONDUIT -> {
            Conduit!![k]!!.length = (x!![0] / UCF(LENGTH))
            Conduit!![k]!!.modLength = Conduit!![k]!!.length
            Conduit!![k]!!.roughness = x!![1]
            Link!![j]!!.offset1 = (x!![2] / UCF(LENGTH))
            Link!![j]!!.offset2 = (x!![3] / UCF(LENGTH))
            Link!![j]!!.q0 = (x!![4] / UCF(FLOW))
            Link!![j]!!.qLimit = (x!![5] / UCF(FLOW))
        }
        PUMP -> {
            Pump!![k]!!.pumpCurve = R.D2I(x!![0])
            Link!![j]!!.hasFlapGate = 0
            Pump!![k]!!.initSetting = x!![1]
            Pump!![k]!!.yOn = (x!![2] / UCF(LENGTH))
            Pump!![k]!!.yOff = (x!![3] / UCF(LENGTH))
            Pump!![k]!!.xMin = 0.0
            Pump!![k]!!.xMax = 0.0
        }
        ORIFICE -> {
            Orifice!![k]!!.type = R.D2I(x!![0])
            Link!![j]!!.offset1 = (x!![1] / UCF(LENGTH))
            Link!![j]!!.offset2 = Link!![j]!!.offset1
            Orifice!![k]!!.cDisch = x!![2]
            Link!![j]!!.hasFlapGate = (if (((x!![3] > 0.0))) 1 else 0)
            Orifice!![k]!!.orate = (x!![4] * 3600.0)
        }
        WEIR -> {
            Weir!![k]!!.type = R.D2I(x!![0])
            Link!![j]!!.offset1 = (x!![1] / UCF(LENGTH))
            Link!![j]!!.offset2 = Link!![j]!!.offset1
            Weir!![k]!!.cDisch1 = x!![2]
            Link!![j]!!.hasFlapGate = (if (((x!![3] > 0.0))) 1 else 0)
            Weir!![k]!!.endCon = x!![4]
            Weir!![k]!!.cDisch2 = x!![5]
            Weir!![k]!!.canSurcharge = R.D2I(x!![6])
            Weir!![k]!!.roadWidth = (x!![7] / UCF(LENGTH))
            Weir!![k]!!.roadSurface = R.D2I(x!![8])
            Weir!![k]!!.cdCurve = R.D2I(x!![9])
        }
        OUTLET -> {
            Link!![j]!!.offset1 = (x!![0] / UCF(LENGTH))
            Link!![j]!!.offset2 = Link!![j]!!.offset1
            Outlet!![k]!!.qCoeff = x!![1]
            Outlet!![k]!!.qExpon = x!![2]
            Outlet!![k]!!.qCurve = R.D2I(x!![3])
            Link!![j]!!.hasFlapGate = (if (((x!![4] > 0.0))) 1 else 0)
            Outlet!![k]!!.curveType = R.D2I(x!![5])
            xsect_setParams(Link!![j]!!.xsect, DUMMY, (null), 0.0)
        }
    }
}

// link.c:403
fun link_validate(j: Int) {
    var n: Int = 0
    if ((LinkOffsets == ELEV_OFFSET)) {
        link_convertOffsets(j)
    }
    when (Link!![j]!!.type) {
        CONDUIT -> {
            conduit_validate(j, Link!![j]!!.subIndex)
        }
        PUMP -> {
            pump_validate(j, Link!![j]!!.subIndex)
        }
        ORIFICE -> {
            orifice_validate(j, Link!![j]!!.subIndex)
        }
        WEIR -> {
            weir_validate(j, Link!![j]!!.subIndex)
        }
    }
    when (Link!![j]!!.type) {
        ORIFICE, WEIR, OUTLET -> {
            if (((Node!![Link!![j]!!.node1]!!.invertElev + Link!![j]!!.offset1) < Node!![Link!![j]!!.node2]!!.invertElev)) {
                if ((RouteModel == DW)) {
                    Link!![j]!!.offset1 = (Node!![Link!![j]!!.node2]!!.invertElev - Node!![Link!![j]!!.node1]!!.invertElev)
                    report_writeWarningMsg(__L305, Link!![j]!!.ID)
                } else {
                    report_writeWarningMsg(__L306, Link!![j]!!.ID)
                }
            }
        }
    }
    if (((Link!![j]!!.type == PUMP) || (((Link!![j]!!.type == ORIFICE) && (Orifice!![Link!![j]!!.subIndex]!!.type == BOTTOM_ORIFICE))))) {
        return
    }
    n = Link!![j]!!.node1
    if (((Node!![n]!!.type != STORAGE) || (Node!![n]!!.surDepth > 0.0))) {
        Node!![n]!!.fullDepth = ((if ((((Node!![n]!!.fullDepth) >= ((Link!![j]!!.offset1 + Link!![j]!!.xsect!!.yFull))))) (Node!![n]!!.fullDepth) else ((Link!![j]!!.offset1 + Link!![j]!!.xsect!!.yFull))))
    }
    n = Link!![j]!!.node2
    if (((((Node!![n]!!.type != STORAGE) || (Node!![n]!!.surDepth > 0.0))) && (Link!![j]!!.type == CONDUIT))) {
        Node!![n]!!.fullDepth = ((if ((((Node!![n]!!.fullDepth) >= ((Link!![j]!!.offset2 + Link!![j]!!.xsect!!.yFull))))) (Node!![n]!!.fullDepth) else ((Link!![j]!!.offset2 + Link!![j]!!.xsect!!.yFull))))
    }
}

// link.c:468
fun link_convertOffsets(j: Int) {
    var elev: Double = 0.0
    elev = Node!![Link!![j]!!.node1]!!.invertElev
    Link!![j]!!.offset1 = link_getOffsetHeight(j, Link!![j]!!.offset1, elev)
    if ((Link!![j]!!.type == CONDUIT)) {
        elev = Node!![Link!![j]!!.node2]!!.invertElev
        Link!![j]!!.offset2 = link_getOffsetHeight(j, Link!![j]!!.offset2, elev)
    } else {
        Link!![j]!!.offset2 = Link!![j]!!.offset1
    }
}

// link.c:489
fun link_getOffsetHeight(j: Int, __offset: Double, elev: Double): Double {
    var offset: Double = __offset
    if (((offset <= (-10000000000.0)) || (Link!![j]!!.type == PUMP))) {
        return 0.0
    }
    offset -= elev
    if ((offset >= 0.0)) {
        return offset
    }
    if ((offset >= (-MIN_DELTA_Z))) {
        return 0.0
    }
    report_writeWarningMsg(__L307, Link!![j]!!.ID)
    return 0.0
}

// link.c:508
fun link_initState(j: Int) {
    var p: Int = 0
    Link!![j]!!.oldFlow = Link!![j]!!.q0
    Link!![j]!!.newFlow = Link!![j]!!.q0
    Link!![j]!!.oldDepth = 0.0
    Link!![j]!!.newDepth = 0.0
    Link!![j]!!.oldVolume = 0.0
    Link!![j]!!.newVolume = 0.0
    Link!![j]!!.setting = 1.0
    Link!![j]!!.targetSetting = 1.0
    Link!![j]!!.timeLastSet = StartDate.v
    Link!![j]!!.inletControl = 0
    Link!![j]!!.normalFlow = 0
    if ((Link!![j]!!.type == CONDUIT)) {
        conduit_initState(j, Link!![j]!!.subIndex)
    }
    if ((Link!![j]!!.type == PUMP)) {
        pump_initState(j, Link!![j]!!.subIndex)
    }
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        Link!![j]!!.oldQual!![p] = 0.0
        Link!![j]!!.newQual!![p] = 0.0
        Link!![j]!!.totalLoad!![p] = 0.0
        p++
    }
}

// link.c:543
fun link_getInflow(j: Int): Double {
    if ((Link!![j]!!.setting == 0.0)) {
        return 0.0
    }
    when (Link!![j]!!.type) {
        CONDUIT -> {
            return conduit_getInflow(j)
        }
        PUMP -> {
            return pump_getInflow(j)
        }
        ORIFICE -> {
            return orifice_getInflow(j)
        }
        WEIR -> {
            return weir_getInflow(j)
        }
        OUTLET -> {
            return outlet_getInflow(j)
        }
        else -> {
            return node_getOutflow(Link!![j]!!.node1, j)
        }
    }
    throw IllegalStateException("link_getInflow: no return")
}

// link.c:564
fun link_setOldHydState(j: Int) {
    var k: Int = 0
    Link!![j]!!.oldDepth = Link!![j]!!.newDepth
    Link!![j]!!.oldFlow = Link!![j]!!.newFlow
    Link!![j]!!.oldVolume = Link!![j]!!.newVolume
    if ((Link!![j]!!.type == CONDUIT)) {
        k = Link!![j]!!.subIndex
        Conduit!![k]!!.q1Old = Conduit!![k]!!.q1
        Conduit!![k]!!.q2Old = Conduit!![k]!!.q2
    }
}

// link.c:587
fun link_setOldQualState(j: Int) {
    var p: Int = 0
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        Link!![j]!!.oldQual!![p] = Link!![j]!!.newQual!![p]
        Link!![j]!!.newQual!![p] = 0.0
        p++
    }
}

// link.c:604
fun link_setTargetSetting(j: Int) {
    var k: Int = 0
    var n1: Int = 0
    if ((Link!![j]!!.type == PUMP)) {
        k = Link!![j]!!.subIndex
        n1 = Link!![j]!!.node1
        Link!![j]!!.targetSetting = Link!![j]!!.setting
        if ((((Pump!![k]!!.yOff > 0.0) && (Link!![j]!!.setting > 0.0)) && (Node!![n1]!!.newDepth < Pump!![k]!!.yOff))) {
            Link!![j]!!.targetSetting = 0.0
        }
        if ((((Pump!![k]!!.yOn > 0.0) && (Link!![j]!!.setting == 0.0)) && (Node!![n1]!!.newDepth > Pump!![k]!!.yOn))) {
            Link!![j]!!.targetSetting = 1.0
        }
    }
}

// link.c:628
fun link_setSetting(j: Int, tstep: Double) {
    if ((Link!![j]!!.type == ORIFICE)) {
        orifice_setSetting(j, tstep)
    } else if ((Link!![j]!!.type == WEIR)) {
        weir_setSetting(j)
    } else {
        Link!![j]!!.setting = Link!![j]!!.targetSetting
    }
}

// link.c:643
fun link_setFlapGate(j: Int, n1: Int, n2: Int, q: Double): Int {
    var n: Int = (-1)
    if ((Link!![j]!!.hasFlapGate != 0)) {
        if (((q * (Link!![j]!!.direction).toDouble()) < 0.0)) {
            return 1
        }
    }
    if ((q < 0.0)) {
        n = n2
    }
    if ((q > 0.0)) {
        n = n1
    }
    if ((((n >= 0) && (Node!![n]!!.type == OUTFALL)) && (Outfall!![Node!![n]!!.subIndex]!!.hasFlapGate != 0))) {
        return 1
    }
    return 0
}

// link.c:674
fun link_getResults(j: Int, f: Double, x: FloatArray?) {
    var p: Int = 0
    var y: Double = 0.0
    var q: Double = 0.0
    var u: Double = 0.0
    var v: Double = 0.0
    var c: Double = 0.0
    var f1: Double = (1.0 - f)
    y = ((f1 * Link!![j]!!.oldDepth) + (f * Link!![j]!!.newDepth))
    q = ((f1 * Link!![j]!!.oldFlow) + (f * Link!![j]!!.newFlow))
    v = ((f1 * Link!![j]!!.oldVolume) + (f * Link!![j]!!.newVolume))
    u = link_getVelocity(j, q, y)
    c = 0.0
    if ((Link!![j]!!.type == CONDUIT)) {
        if ((Link!![j]!!.xsect!!.type != DUMMY)) {
            c = (xsect_getAofY(Link!![j]!!.xsect, y) / Link!![j]!!.xsect!!.aFull)
        }
    } else {
        c = Link!![j]!!.setting
    }
    if (((Link!![j]!!.type == PUMP) && ((Link!![j]!!.oldFlow * Link!![j]!!.newFlow) == 0.0))) {
        if ((f >= f1)) {
            q = Link!![j]!!.newFlow
        } else {
            q = Link!![j]!!.oldFlow
        }
    }
    y *= (UCF(LENGTH))
    v *= (UCF(VOLUME))
    q *= (UCF(FLOW) * (Link!![j]!!.direction).toDouble())
    u *= (UCF(LENGTH) * (Link!![j]!!.direction).toDouble())
    x!![LINK_DEPTH] = (y).toFloat()
    x!![LINK_FLOW] = (q).toFloat()
    x!![LINK_VELOCITY] = (u).toFloat()
    x!![LINK_VOLUME] = (v).toFloat()
    x!![LINK_CAPACITY] = (c).toFloat()
    if (!(IgnoreQuality != 0)) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            c = ((f1 * Link!![j]!!.oldQual!![p]) + (f * Link!![j]!!.newQual!![p]))
            x!![(LINK_QUAL + p)] = (c).toFloat()
            p++
        }
    }
}

// link.c:728
fun link_setOutfallDepth(j: Int) {
    var k: Int = 0
    var n: Int = 0
    var z: Double = 0.0
    var q: Double = 0.0
    var yCrit: Double = 0.0
    var yNorm: Double = 0.0
    if ((Node!![Link!![j]!!.node2]!!.type == OUTFALL)) {
        n = Link!![j]!!.node2
        z = Link!![j]!!.offset2
    } else if ((Node!![Link!![j]!!.node1]!!.type == OUTFALL)) {
        n = Link!![j]!!.node1
        z = Link!![j]!!.offset1
    } else {
        return
    }
    if ((Link!![j]!!.type == CONDUIT)) {
        k = Link!![j]!!.subIndex
        q = abs((Link!![j]!!.newFlow / (Conduit!![k]!!.barrels).toDouble()))
        yNorm = link_getYnorm(j, q)
        yCrit = link_getYcrit(j, q)
    }
    node_setOutletDepth(n, yNorm, yCrit, z)
}

// link.c:770
fun link_getYcrit(j: Int, q: Double): Double {
    return xsect_getYcrit(Link!![j]!!.xsect, q)
}

// link.c:783
fun link_getYnorm(j: Int, __q: Double): Double {
    var q: Double = __q
    var k: Int = 0
    var s: Double = 0.0
    var a: Double = 0.0
    var y: Double = 0.0
    if ((Link!![j]!!.type != CONDUIT)) {
        return 0.0
    }
    if ((Link!![j]!!.xsect!!.type == DUMMY)) {
        return 0.0
    }
    q = abs(q)
    k = Link!![j]!!.subIndex
    if ((q > Conduit!![k]!!.qMax)) {
        q = Conduit!![k]!!.qMax
    }
    if ((q <= 0.0)) {
        return 0.0
    }
    s = (q / Conduit!![k]!!.beta)
    a = xsect_getAofS(Link!![j]!!.xsect, s)
    y = xsect_getYofA(Link!![j]!!.xsect, a)
    return y
}

// link.c:808
fun link_getLength(j: Int): Double {
    if ((Link!![j]!!.type == CONDUIT)) {
        return conduit_getLength(j)
    }
    return 0.0
}

// link.c:821
fun link_getVelocity(j: Int, __flow: Double, depth: Double): Double {
    var flow: Double = __flow
    var area: Double = 0.0
    var veloc: Double = 0.0
    var k: Int = 0
    if ((depth <= 0.01)) {
        return 0.0
    }
    if ((Link!![j]!!.type == CONDUIT)) {
        k = Link!![j]!!.subIndex
        flow /= ((Conduit!![k]!!.barrels).toDouble())
        area = xsect_getAofY(Link!![j]!!.xsect, depth)
        if ((area > 0.0001)) {
            veloc = (flow / area)
        }
    }
    return veloc
}

// link.c:847
fun link_getFroude(j: Int, v: Double, __y: Double): Double {
    var y: Double = __y
    var xsect: TXsect? = Link!![j]!!.xsect
    if ((Link!![j]!!.type != CONDUIT)) {
        return 0.0
    }
    if ((y <= 0.0001)) {
        return 0.0
    }
    if (((!(xsect_isOpen(xsect!!.type) != 0)) && ((xsect!!.yFull - y) <= 0.0001))) {
        return 0.0
    }
    y = (xsect_getAofY(xsect, y) / xsect_getWofY(xsect, y))
    return (abs(v) / sqrt((32.2 * y)))
}

// link.c:875
fun link_getPower(j: Int): Double {
    var n1: Int = Link!![j]!!.node1
    var n2: Int = Link!![j]!!.node2
    var dh: Double = (((Node!![n1]!!.invertElev + Node!![n1]!!.newDepth)) - ((Node!![n2]!!.invertElev + Node!![n2]!!.newDepth)))
    var q: Double = abs(Link!![j]!!.newFlow)
    return (((abs(dh) * q) / 8.814) * 0.7457)
}

// link.c:893
fun link_getLossRate(j: Int, routeModel: Int, q: Double, tstep: Double): Double {
    if ((Link!![j]!!.type == CONDUIT)) {
        return conduit_getLossRate(j, routeModel, q, tstep)
    } else {
        return 0.0
    }
    throw IllegalStateException("link_getLossRate: no return")
}

// link.c:911
fun link_getFullState(a1: Double, a2: Double, aFull: Double): Int {
    if ((a1 >= aFull)) {
        if ((a2 >= aFull)) {
            return ALL_FULL
        } else {
            return UP_FULL
        }
    }
    if ((a2 >= aFull)) {
        return DN_FULL
    }
    return 0
}

// link.c:933
fun conduit_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var n1: Int = 0
    var n2: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    var id: CPtr? = null
    if ((ntoks < 7)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(LINK, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    n1 = project_findObject(NODE, tok!![1])
    if ((n1 < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    n2 = project_findObject(NODE, tok!![2])
    if ((n2 < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    if (!(getDouble(tok!![3], DElem(x!!, 0)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![3])
    }
    if (!(getDouble(tok!![4], DElem(x!!, 1)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![4])
    }
    if (((LinkOffsets == ELEV_OFFSET) && (tok!![5]!!.g(0) == 42))) {
        x!![2] = (-10000000000.0)
    } else if (!(getDouble(tok!![5], DElem(x!!, 2)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![5])
    }
    if (((LinkOffsets == ELEV_OFFSET) && (tok!![6]!!.g(0) == 42))) {
        x!![3] = (-10000000000.0)
    } else if (!(getDouble(tok!![6], DElem(x!!, 3)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![6])
    }
    x!![4] = 0.0
    if ((ntoks >= 8)) {
        if (!(getDouble(tok!![7], DElem(x!!, 4)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![7])
        }
    }
    x!![5] = 0.0
    if ((ntoks >= 9)) {
        if (!(getDouble(tok!![8], DElem(x!!, 5)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![8])
        }
    }
    Link!![j]!!.ID = id
    link_setParams(j, CONDUIT, n1, n2, k, x)
    return 0
}

// link.c:992
fun conduit_validate(j: Int, k: Int) {
    var aa: Double = 0.0
    var lengthFactor: Double = 0.0
    var roughness: Double = 0.0
    var slope: Double = 0.0
    if (((Link!![j]!!.xsect!!.type == DUMMY) && (RouteModel == DW))) {
        if ((Node!![Link!![j]!!.node1]!!.type == STORAGE)) {
            report_writeErrorMsg(ERR_DUMMY_LINK, Node!![Link!![j]!!.node1]!!.ID)
            return
        }
    }
    if ((Link!![j]!!.xsect!!.type == CUSTOM)) {
        xsect_setCustomXsectParams(Link!![j]!!.xsect)
    }
    if ((Link!![j]!!.xsect!!.type == IRREGULAR)) {
        xsect_setIrregXsectParams(Link!![j]!!.xsect)
        Conduit!![k]!!.roughness = Transect!![Link!![j]!!.xsect!!.transect]!!.roughness
    }
    if ((Link!![j]!!.xsect!!.type == STREET_XSECT)) {
        xsect_setStreetXsectParams(Link!![j]!!.xsect)
        Conduit!![k]!!.roughness = Street!![Link!![j]!!.xsect!!.transect]!!.roughness
    }
    if ((Link!![j]!!.xsect!!.type == FORCE_MAIN)) {
        if ((ForceMainEqn == D_W)) {
            Link!![j]!!.xsect!!.rBot /= (UCF(RAINDEPTH))
        }
        if ((Link!![j]!!.xsect!!.rBot <= 0.0)) {
            report_writeErrorMsg(ERR_XSECT, Link!![j]!!.ID)
        }
    }
    if ((Conduit!![k]!!.length <= 0.0)) {
        report_writeErrorMsg(ERR_LENGTH, Link!![j]!!.ID)
    }
    if ((Conduit!![k]!!.roughness <= 0.0)) {
        report_writeErrorMsg(ERR_ROUGHNESS, Link!![j]!!.ID)
    }
    if ((Conduit!![k]!!.barrels <= 0)) {
        report_writeErrorMsg(ERR_BARRELS, Link!![j]!!.ID)
    }
    if ((Link!![j]!!.xsect!!.type != DUMMY)) {
        if ((Link!![j]!!.xsect!!.type < 0)) {
            report_writeErrorMsg(ERR_NO_XSECT, Link!![j]!!.ID)
        } else if ((Link!![j]!!.xsect!!.aFull <= 0.0)) {
            report_writeErrorMsg(ERR_XSECT, Link!![j]!!.ID)
        }
    }
    if ((ErrorCode != 0)) {
        return
    }
    if ((Link!![j]!!.offset1 < 0.0)) {
        report_writeWarningMsg(__L307, Link!![j]!!.ID)
        Link!![j]!!.offset1 = 0.0
    }
    if ((Link!![j]!!.offset2 < 0.0)) {
        report_writeWarningMsg(__L307, Link!![j]!!.ID)
        Link!![j]!!.offset2 = 0.0
    }
    if ((Link!![j]!!.xsect!!.type == FILLED_CIRCULAR)) {
        Link!![j]!!.offset1 += Link!![j]!!.xsect!!.yBot
        Link!![j]!!.offset2 += Link!![j]!!.xsect!!.yBot
    }
    slope = conduit_getSlope(j)
    Conduit!![k]!!.slope = slope
    if ((((RouteModel == DW) && (slope < 0.0)) && (Link!![j]!!.xsect!!.type != DUMMY))) {
        conduit_reverse(j, k)
    }
    roughness = Conduit!![k]!!.roughness
    if (((RouteModel == DW) && (Link!![j]!!.xsect!!.type == FORCE_MAIN))) {
        roughness = forcemain_getEquivN(j, k)
    }
    if ((Link!![j]!!.xsect!!.type == IRREGULAR)) {
        lengthFactor = Transect!![Link!![j]!!.xsect!!.transect]!!.lengthFactor
        roughness *= (sqrt(lengthFactor))
    }
    lengthFactor = 1.0
    if ((((RouteModel == DW) && (LengtheningStep > 0.0)) && (Link!![j]!!.xsect!!.type != DUMMY))) {
        lengthFactor = conduit_getLengthFactor(j, k, roughness)
    }
    if ((lengthFactor != 1.0)) {
        Conduit!![k]!!.modLength = (lengthFactor * conduit_getLength(j))
        slope /= lengthFactor
        roughness = (roughness / sqrt(lengthFactor))
    }
    if (((RouteModel == DW) && (Link!![j]!!.xsect!!.type == FORCE_MAIN))) {
        Link!![j]!!.xsect!!.sBot = forcemain_getRoughFactor(j, lengthFactor)
    }
    Conduit!![k]!!.roughFactor = (32.2 * ((((roughness / 1.486)) * ((roughness / 1.486)))))
    if ((Link!![j]!!.xsect!!.type == DUMMY)) {
        Conduit!![k]!!.beta = 0.0
    } else {
        Conduit!![k]!!.beta = ((1.486 * sqrt(abs(slope))) / roughness)
    }
    Link!![j]!!.qFull = (Link!![j]!!.xsect!!.sFull * Conduit!![k]!!.beta)
    Conduit!![k]!!.qMax = (Link!![j]!!.xsect!!.sMax * Conduit!![k]!!.beta)
    aa = (((Conduit!![k]!!.beta / sqrt(32.2)) * Glibm.pow(Link!![j]!!.xsect!!.yFull, 0.1666667)) * 0.3)
    if ((aa >= 1.0)) {
        Conduit!![k]!!.superCritical = 1
    } else {
        Conduit!![k]!!.superCritical = 0
    }
    if ((((Link!![j]!!.cLossInlet == 0.0) && (Link!![j]!!.cLossOutlet == 0.0)) && (Link!![j]!!.cLossAvg == 0.0))) {
        Conduit!![k]!!.hasLosses = 0
    } else {
        Conduit!![k]!!.hasLosses = 1
    }
}

// link.c:1158
fun conduit_reverse(j: Int, k: Int) {
    var i: Int = 0
    var z: Double = 0.0
    var cLoss: Double = 0.0
    i = Link!![j]!!.node1
    Link!![j]!!.node1 = Link!![j]!!.node2
    Link!![j]!!.node2 = i
    z = Link!![j]!!.offset1
    Link!![j]!!.offset1 = Link!![j]!!.offset2
    Link!![j]!!.offset2 = z
    cLoss = Link!![j]!!.cLossInlet
    Link!![j]!!.cLossInlet = Link!![j]!!.cLossOutlet
    Link!![j]!!.cLossOutlet = cLoss
    Conduit!![k]!!.slope = (-Conduit!![k]!!.slope)
    Link!![j]!!.direction *= (((-1)).toByte().toInt())
    Link!![j]!!.q0 = (-Link!![j]!!.q0)
}

// link.c:1195
fun conduit_getLength(j: Int): Double {
    var k: Int = Link!![j]!!.subIndex
    var t: Int = 0
    if ((Link!![j]!!.xsect!!.type != IRREGULAR)) {
        return Conduit!![k]!!.length
    }
    t = Link!![j]!!.xsect!!.transect
    if (((t < 0) || (t >= Nobjects!![TRANSECT]))) {
        return Conduit!![k]!!.length
    }
    return (Conduit!![k]!!.length / Transect!![t]!!.lengthFactor)
}

// link.c:1217
fun conduit_getLengthFactor(j: Int, k: Int, roughness: Double): Double {
    var ratio: Double = 0.0
    var yFull: Double = 0.0
    var vFull: Double = 0.0
    var tStep: Double = 0.0
    yFull = Link!![j]!!.xsect!!.yFull
    if ((xsect_isOpen(Link!![j]!!.xsect!!.type) != 0)) {
        yFull = (Link!![j]!!.xsect!!.aFull / xsect_getWofY(Link!![j]!!.xsect, yFull))
    }
    vFull = ((((1.486 / roughness) * Link!![j]!!.xsect!!.sFull) * sqrt(abs(Conduit!![k]!!.slope))) / Link!![j]!!.xsect!!.aFull)
    if ((LengtheningStep == 0.0)) {
        tStep = RouteStep
    } else {
        tStep = ((if ((((RouteStep) <= (LengtheningStep)))) (RouteStep) else (LengtheningStep)))
    }
    ratio = ((((sqrt((32.2 * yFull)) + vFull)) * tStep) / conduit_getLength(j))
    if ((ratio > 1.0)) {
        return ratio
    } else {
        return 1.0
    }
    throw IllegalStateException("conduit_getLengthFactor: no return")
}

// link.c:1258
fun conduit_getSlope(j: Int): Double {
    var elev1: Double = 0.0
    var elev2: Double = 0.0
    var delta: Double = 0.0
    var slope: Double = 0.0
    var length: Double = conduit_getLength(j)
    elev1 = (Link!![j]!!.offset1 + Node!![Link!![j]!!.node1]!!.invertElev)
    elev2 = (Link!![j]!!.offset2 + Node!![Link!![j]!!.node2]!!.invertElev)
    delta = abs((elev1 - elev2))
    if ((delta < MIN_DELTA_Z)) {
        report_writeWarningMsg(__L308, Link!![j]!!.ID)
        delta = MIN_DELTA_Z
    }
    if ((delta >= length)) {
        report_writeWarningMsg(__L309, Link!![j]!!.ID)
        slope = (delta / length)
    } else {
        slope = (delta / sqrt(((((length) * (length))) - (((delta) * (delta))))))
    }
    if (((MinSlope.v > 0.0) && (slope < MinSlope.v))) {
        report_writeWarningMsg(__L310, Link!![j]!!.ID)
        slope = MinSlope.v
        if (((RouteModel == SF) || (RouteModel == KW))) {
            return slope
        }
    }
    if ((elev1 < elev2)) {
        slope = (-slope)
    }
    return slope
}

// link.c:1304
fun conduit_initState(j: Int, k: Int) {
    Link!![j]!!.newDepth = link_getYnorm(j, (Link!![j]!!.q0 / (Conduit!![k]!!.barrels).toDouble()))
    Link!![j]!!.oldDepth = Link!![j]!!.newDepth
    Conduit!![k]!!.evapLossRate = 0.0
    Conduit!![k]!!.seepLossRate = 0.0
}

// link.c:1320
fun conduit_getInflow(j: Int): Double {
    var qIn: Double = node_getOutflow(Link!![j]!!.node1, j)
    if ((Link!![j]!!.qLimit > 0.0)) {
        qIn = ((if ((((qIn) <= (Link!![j]!!.qLimit)))) (qIn) else (Link!![j]!!.qLimit)))
    }
    return qIn
}

// link.c:1334
fun conduit_getLossRate(j: Int, routeModel: Int, __q: Double, tstep: Double): Double {
    var q: Double = __q
    var xsect: TXsect? = null
    var depth: Double = (0.5 * ((Link!![j]!!.oldDepth + Link!![j]!!.newDepth)))
    var length: Double = 0.0
    var width: Double = 0.0
    var topWidth: Double = 0.0
    var evapLossRate: Double = 0.0
    var seepLossRate: Double = 0.0
    var totalLossRate: Double = 0.0
    if ((depth > 0.0001)) {
        xsect = Link!![j]!!.xsect
        length = conduit_getLength(j)
        if (((xsect_isOpen(xsect!!.type) != 0) && (Evap!!.rate > 0.0))) {
            topWidth = xsect_getWofY(xsect, depth)
            evapLossRate = ((topWidth * length) * Evap!!.rate)
        }
        if ((Link!![j]!!.seepRate > 0.0)) {
            if ((xsect!!.type == RECT_CLOSED)) {
                width = xsect!!.wMax
            } else {
                if ((depth >= xsect!!.ywMax)) {
                    depth = xsect!!.ywMax
                }
                width = xsect_getWofY(xsect, depth)
            }
            seepLossRate = ((Link!![j]!!.seepRate * width) * length)
            seepLossRate *= Adjust!!.hydconFactor
        }
        totalLossRate = (evapLossRate + seepLossRate)
        if ((routeModel == DW)) {
            q = (Link!![j]!!.newVolume / tstep)
        } else {
            q = ((if ((((q) < 0.0))) (-(q)) else (q)))
        }
        if ((totalLossRate > q)) {
            evapLossRate = ((evapLossRate * q) / totalLossRate)
            seepLossRate = ((seepLossRate * q) / totalLossRate)
            totalLossRate = q
        }
    }
    Conduit!![Link!![j]!!.subIndex]!!.evapLossRate = evapLossRate
    Conduit!![Link!![j]!!.subIndex]!!.seepLossRate = seepLossRate
    return totalLossRate
}

// link.c:1406
fun pump_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var m: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var x: DoubleArray? = DoubleArray(4)
    var id: CPtr? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(LINK, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    n1 = project_findObject(NODE, tok!![1])
    if ((n1 < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    n2 = project_findObject(NODE, tok!![2])
    if ((n2 < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    x!![0] = (-1.0)
    if ((ntoks >= 4)) {
        if (!(strcomp(tok!![3], __L275) != 0)) {
            m = project_findObject(CURVE, tok!![3])
            if ((m < 0)) {
                return error_setInpError(ERR_NAME, tok!![3])
            }
            x!![0] = (m).toDouble()
        }
    }
    x!![1] = 1.0
    if ((ntoks >= 5)) {
        m = findmatch(tok!![4], OffOnWords)
        if ((m < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![4])
        }
        x!![1] = (m).toDouble()
    }
    x!![2] = 0.0
    if ((ntoks >= 6)) {
        if (((!(getDouble(tok!![5], DElem(x!!, 2)) != 0)) || (x!![2] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
    }
    x!![3] = 0.0
    if ((ntoks >= 7)) {
        if (((!(getDouble(tok!![6], DElem(x!!, 3)) != 0)) || (x!![3] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![6])
        }
    }
    Link!![j]!!.ID = id
    link_setParams(j, PUMP, n1, n2, k, x)
    return 0
}

// link.c:1473
fun pump_validate(j: Int, k: Int) {
    var m: Int = 0
    var n1: Int = 0
    val x = DBox(0.0)
    val y = DBox(0.0)
    Link!![j]!!.xsect!!.yFull = 0.0
    m = Pump!![k]!!.pumpCurve
    if ((m < 0)) {
        Pump!![k]!!.type = IDEAL_PUMP
    } else {
        if (((Curve!![m]!!.curveType < PUMP1_CURVE) || (Curve!![m]!!.curveType > PUMP5_CURVE))) {
            report_writeErrorMsg(ERR_NO_CURVE, Link!![j]!!.ID)
        } else {
            Pump!![k]!!.type = (Curve!![m]!!.curveType - PUMP1_CURVE)
            if ((table_getFirstEntry(Curve!![m], x, y) != 0)) {
                Link!![j]!!.qFull = y.v
                Pump!![k]!!.xMin = x.v
                Pump!![k]!!.xMax = x.v
                w1@ while ((table_getNextEntry(Curve!![m], x, y) != 0)) {
                    Link!![j]!!.qFull = ((if ((((y.v) >= (Link!![j]!!.qFull)))) (y.v) else (Link!![j]!!.qFull)))
                    Pump!![k]!!.xMax = x.v
                }
            }
            Link!![j]!!.qFull /= (UCF(FLOW))
        }
    }
    if (((Pump!![k]!!.yOn > 0.0) && (Pump!![k]!!.yOn <= Pump!![k]!!.yOff))) {
        report_writeErrorMsg(ERR_PUMP_LIMITS, Link!![j]!!.ID)
    }
    if ((Pump!![k]!!.type == TYPE1_PUMP)) {
        n1 = Link!![j]!!.node1
        if ((Node!![n1]!!.type != STORAGE)) {
            Node!![n1]!!.fullVolume = ((if ((((Node!![n1]!!.fullVolume) >= ((Pump!![k]!!.xMax / UCF(VOLUME)))))) (Node!![n1]!!.fullVolume) else ((Pump!![k]!!.xMax / UCF(VOLUME)))))
        }
    }
}

// link.c:1534
fun pump_initState(j: Int, k: Int) {
    Link!![j]!!.setting = Pump!![k]!!.initSetting
    Link!![j]!!.targetSetting = Pump!![k]!!.initSetting
}

// link.c:1548
fun pump_getInflow(j: Int): Double {
    var k: Int = 0
    var m: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var vol: Double = 0.0
    var depth: Double = 0.0
    var head: Double = 0.0
    var qIn: Double = 0.0
    var qIn1: Double = 0.0
    var dh: Double = 0.001
    var s: Double = 1.0
    k = Link!![j]!!.subIndex
    m = Pump!![k]!!.pumpCurve
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    Link!![j]!!.flowClass = NO
    Link!![j]!!.setting = Link!![j]!!.targetSetting
    if ((Link!![j]!!.setting == 0.0)) {
        return 0.0
    }
    if ((Pump!![k]!!.type == IDEAL_PUMP)) {
        qIn = (Node!![n1]!!.inflow + Node!![n1]!!.overflow)
    } else {
        when (Curve!![m]!!.curveType) {
            PUMP1_CURVE -> {
                vol = (Node!![n1]!!.newVolume * UCF(VOLUME))
                qIn = (table_intervalLookup(Curve!![m], vol) / UCF(FLOW))
                if (((vol < Pump!![k]!!.xMin) || (vol > Pump!![k]!!.xMax))) {
                    Link!![j]!!.flowClass = YES
                }
            }
            PUMP2_CURVE -> {
                depth = (Node!![n1]!!.newDepth * UCF(LENGTH))
                qIn = (table_intervalLookup(Curve!![m], depth) / UCF(FLOW))
                if (((depth < Pump!![k]!!.xMin) || (depth > Pump!![k]!!.xMax))) {
                    Link!![j]!!.flowClass = YES
                }
            }
            PUMP3_CURVE, PUMP5_CURVE -> {
                if ((Curve!![m]!!.curveType == PUMP5_CURVE)) {
                    s = Link!![j]!!.setting
                }
                head = ((((((Node!![n2]!!.newDepth + Node!![n2]!!.invertElev)) - ((Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)))) / s) / s)
                head = (((if ((((head) >= (0.0)))) (head) else (0.0))) * UCF(LENGTH))
                qIn = (table_lookup(Curve!![m], head) / UCF(FLOW))
                Link!![j]!!.dqdh = ((((-(table_getSlope(Curve!![m], head))) * UCF(LENGTH)) / UCF(FLOW)) / s)
                if (((head < Pump!![k]!!.xMin) || (head > Pump!![k]!!.xMax))) {
                    Link!![j]!!.flowClass = YES
                }
            }
            PUMP4_CURVE -> {
                depth = Node!![n1]!!.newDepth
                qIn = (table_lookup(Curve!![m], (depth * UCF(LENGTH))) / UCF(FLOW))
                qIn1 = (table_lookup(Curve!![m], (((depth + dh)) * UCF(LENGTH))) / UCF(FLOW))
                Link!![j]!!.dqdh = (((qIn1 - qIn)) / dh)
                depth *= (UCF(LENGTH))
                if ((depth < Pump!![k]!!.xMin)) {
                    Link!![j]!!.flowClass = DN_DRY
                }
                if ((depth > Pump!![k]!!.xMax)) {
                    Link!![j]!!.flowClass = UP_DRY
                }
            }
            else -> {
                qIn = 0.0
            }
        }
    }
    if ((qIn < 0.0)) {
        qIn = 0.0
    }
    return (qIn * Link!![j]!!.setting)
}

// link.c:1641
fun orifice_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var m: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var x: DoubleArray? = DoubleArray(5)
    var id: CPtr? = null
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(LINK, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    n1 = project_findObject(NODE, tok!![1])
    if ((n1 < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    n2 = project_findObject(NODE, tok!![2])
    if ((n2 < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    m = findmatch(tok!![3], OrificeTypeWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![3])
    }
    x!![0] = (m).toDouble()
    if (((LinkOffsets == ELEV_OFFSET) && (tok!![4]!!.g(0) == 42))) {
        x!![1] = (-10000000000.0)
    } else if (!(getDouble(tok!![4], DElem(x!!, 1)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![4])
    }
    if (((!(getDouble(tok!![5], DElem(x!!, 2)) != 0)) || (x!![2] < 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![5])
    }
    x!![3] = 0.0
    if ((ntoks >= 7)) {
        m = findmatch(tok!![6], NoYesWords)
        if ((m < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![6])
        }
        x!![3] = (m).toDouble()
    }
    x!![4] = 0.0
    if ((ntoks >= 8)) {
        if (((!(getDouble(tok!![7], DElem(x!!, 4)) != 0)) || (x!![4] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![7])
        }
    }
    Link!![j]!!.ID = id
    link_setParams(j, ORIFICE, n1, n2, k, x)
    return 0
}

// link.c:1696
fun orifice_validate(j: Int, k: Int) {
    var err: Int = 0
    if (((Link!![j]!!.xsect!!.type != RECT_CLOSED) && (Link!![j]!!.xsect!!.type != CIRCULAR))) {
        err = ERR_REGULATOR_SHAPE
    }
    if ((err > 0)) {
        report_writeErrorMsg(err, Link!![j]!!.ID)
        return
    }
    if ((Link!![j]!!.offset1 < 0.0)) {
        Link!![j]!!.offset1 = 0.0
    }
    orifice_setSetting(j, 0.0)
    Orifice!![k]!!.length = ((2.0 * RouteStep) * sqrt((32.2 * Link!![j]!!.xsect!!.yFull)))
    Orifice!![k]!!.length = ((if ((((200.0) >= (Orifice!![k]!!.length)))) (200.0) else (Orifice!![k]!!.length)))
    Orifice!![k]!!.surfArea = 0.0
}

// link.c:1729
fun orifice_setSetting(j: Int, tstep: Double) {
    var k: Int = Link!![j]!!.subIndex
    var delta: Double = 0.0
    var step: Double = 0.0
    var h: Double = 0.0
    var f: Double = 0.0
    if (((Orifice!![k]!!.orate == 0.0) || (tstep == 0.0))) {
        Link!![j]!!.setting = Link!![j]!!.targetSetting
    } else {
        delta = (Link!![j]!!.targetSetting - Link!![j]!!.setting)
        step = (tstep / Orifice!![k]!!.orate)
        if (((step + 0.001) >= abs(delta))) {
            Link!![j]!!.setting = Link!![j]!!.targetSetting
        } else {
            Link!![j]!!.setting += ((((if ((((delta) < 0.0))) ((-1)) else (1)))).toDouble() * step)
        }
    }
    h = (Link!![j]!!.setting * Link!![j]!!.xsect!!.yFull)
    f = (xsect_getAofY(Link!![j]!!.xsect, h) * sqrt((2.0 * 32.2)))
    Orifice!![k]!!.cOrif = (Orifice!![k]!!.cDisch * f)
    Orifice!![k]!!.cWeir = (orifice_getWeirCoeff(j, k, h) * f)
}

// link.c:1766
fun orifice_getWeirCoeff(j: Int, k: Int, __h: Double): Double {
    var h: Double = __h
    var w: Double = 0.0
    var aOverL: Double = 0.0
    if ((Orifice!![k]!!.type == BOTTOM_ORIFICE)) {
        if ((Link!![j]!!.xsect!!.type == CIRCULAR)) {
            aOverL = (h / 4.0)
        } else {
            w = Link!![j]!!.xsect!!.wMax
            aOverL = (((h * w)) / ((2.0 * ((h + w)))))
        }
        h = ((Orifice!![k]!!.cDisch / 0.414) * aOverL)
        Orifice!![k]!!.hCrit = h
    } else {
        Orifice!![k]!!.hCrit = h
        h = (h / 2.0)
    }
    return (Orifice!![k]!!.cDisch * sqrt(h))
}

// link.c:1812
fun orifice_getInflow(j: Int): Double {
    var k: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var head: Double = 0.0
    var h1: Double = 0.0
    var h2: Double = 0.0
    var y1: Double = 0.0
    var dir: Double = 0.0
    var f: Double = 0.0
    var hcrest: Double = 0.0
    var hcrown: Double = 0.0
    var hmidpt: Double = 0.0
    var q: Double = 0.0
    var ratio: Double = 0.0
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    k = Link!![j]!!.subIndex
    if ((RouteModel == DW)) {
        h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
        h2 = (Node!![n2]!!.newDepth + Node!![n2]!!.invertElev)
    } else {
        h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
        h2 = Node!![n1]!!.invertElev
    }
    dir = (if (((h1 >= h2))) 1.0 else (-1.0))
    y1 = Node!![n1]!!.newDepth
    if ((dir < 0.0)) {
        head = h1
        h1 = h2
        h2 = head
        y1 = Node!![n2]!!.newDepth
    }
    if ((Orifice!![k]!!.type == BOTTOM_ORIFICE)) {
        hcrest = (Node!![n1]!!.invertElev + Link!![j]!!.offset1)
        if ((h1 < hcrest)) {
            head = 0.0
        } else if ((h2 > hcrest)) {
            head = (h1 - h2)
        } else {
            head = (h1 - hcrest)
        }
        f = (head / Orifice!![k]!!.hCrit)
        f = ((if ((((f) <= (1.0)))) (f) else (1.0)))
    } else {
        hcrest = (Node!![n1]!!.invertElev + Link!![j]!!.offset1)
        hcrown = (hcrest + (Link!![j]!!.xsect!!.yFull * Link!![j]!!.setting))
        hmidpt = (((hcrest + hcrown)) / 2.0)
        if (((h1 < hcrown) && (hcrown > hcrest))) {
            f = (((h1 - hcrest)) / ((hcrown - hcrest)))
        } else {
            f = 1.0
        }
        if ((f < 1.0)) {
            head = (h1 - hcrest)
        } else if ((h2 < hmidpt)) {
            head = (h1 - hmidpt)
        } else {
            head = (h1 - h2)
        }
    }
    if ((((head <= 0.0001) || (y1 <= 0.0001)) || (link_setFlapGate(j, n1, n2, dir) != 0))) {
        Link!![j]!!.newDepth = 0.0
        Link!![j]!!.flowClass = DRY
        Orifice!![k]!!.surfArea = (0.0001 * Orifice!![k]!!.length)
        Link!![j]!!.dqdh = 0.0
        return 0.0
    }
    Link!![j]!!.flowClass = SUBCRITICAL
    if ((hcrest > h2)) {
        if ((dir == 1.0)) {
            Link!![j]!!.flowClass = DN_CRITICAL
        } else {
            Link!![j]!!.flowClass = UP_CRITICAL
        }
    }
    y1 = (Link!![j]!!.xsect!!.yFull * Link!![j]!!.setting)
    if ((Orifice!![k]!!.type == SIDE_ORIFICE)) {
        Link!![j]!!.newDepth = (y1 * f)
        Orifice!![k]!!.surfArea = (xsect_getWofY(Link!![j]!!.xsect, Link!![j]!!.newDepth) * Orifice!![k]!!.length)
    } else {
        Link!![j]!!.newDepth = y1
        Orifice!![k]!!.surfArea = xsect_getAofY(Link!![j]!!.xsect, y1)
    }
    q = (dir * orifice_getFlow(j, k, head, f, Link!![j]!!.hasFlapGate))
    if (((f < 1.0) && (h2 > hcrest))) {
        ratio = (((h2 - hcrest)) / ((h1 - hcrest)))
        q *= (Glibm.pow(((1.0 - Glibm.pow(ratio, 1.5))), 0.385))
    }
    return q
}

// link.c:1938
fun orifice_getFlow(j: Int, k: Int, __head: Double, __f: Double, hasFlapGate: Int): Double {
    var head: Double = __head
    var f: Double = __f
    var area: Double = 0.0
    var q: Double = 0.0
    var veloc: Double = 0.0
    var hLoss: Double = 0.0
    if (((head == 0.0) || (f <= 0.0))) {
        Link!![j]!!.dqdh = 0.0
        return 0.0
    } else if ((f < 1.0)) {
        q = (Orifice!![k]!!.cWeir * Glibm.pow(f, 1.5))
        Link!![j]!!.dqdh = ((1.5 * q) / ((f * Orifice!![k]!!.hCrit)))
    } else {
        q = (Orifice!![k]!!.cOrif * sqrt(head))
        Link!![j]!!.dqdh = (q / ((2.0 * head)))
    }
    if ((hasFlapGate != 0)) {
        area = xsect_getAofY(Link!![j]!!.xsect, (Link!![j]!!.setting * Link!![j]!!.xsect!!.yFull))
        veloc = (q / area)
        hLoss = (((((4.0 / 32.2)) * veloc) * veloc) * Glibm.exp((((-1.15) * veloc) / sqrt(head))))
        if ((f < 1.0)) {
            f = (f - (hLoss / Orifice!![k]!!.hCrit))
            if ((f < 0.0)) {
                f = 0.0
            }
        } else {
            head = (head - hLoss)
            if ((head < 0.0)) {
                head = 0.0
            }
        }
        q = orifice_getFlow(j, k, head, f, 0)
    }
    return q
}

// link.c:2010
fun weir_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var m: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var x: DoubleArray? = DoubleArray(10)
    var id: CPtr? = null
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(LINK, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    n1 = project_findObject(NODE, tok!![1])
    if ((n1 < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    n2 = project_findObject(NODE, tok!![2])
    if ((n2 < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    m = findmatch(tok!![3], WeirTypeWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![3])
    }
    x!![0] = (m).toDouble()
    if (((LinkOffsets == ELEV_OFFSET) && (tok!![4]!!.g(0) == 42))) {
        x!![1] = (-10000000000.0)
    } else if (!(getDouble(tok!![4], DElem(x!!, 1)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![4])
    }
    if (((!(getDouble(tok!![5], DElem(x!!, 2)) != 0)) || (x!![2] < 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![5])
    }
    x!![3] = 0.0
    x!![4] = 0.0
    x!![5] = 0.0
    x!![6] = 1.0
    x!![7] = 0.0
    x!![8] = 0.0
    x!![9] = (-1.0)
    if (((ntoks >= 7) && (tok!![6]!!.g(0) != 42))) {
        m = findmatch(tok!![6], NoYesWords)
        if ((m < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![6])
        }
        x!![3] = (m).toDouble()
    }
    if (((ntoks >= 8) && (tok!![7]!!.g(0) != 42))) {
        if (((!(getDouble(tok!![7], DElem(x!!, 4)) != 0)) || (x!![4] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![7])
        }
    }
    if (((ntoks >= 9) && (tok!![8]!!.g(0) != 42))) {
        if (((!(getDouble(tok!![8], DElem(x!!, 5)) != 0)) || (x!![5] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![8])
        }
    }
    if (((ntoks >= 10) && (tok!![9]!!.g(0) != 42))) {
        m = findmatch(tok!![9], NoYesWords)
        if ((m < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![9])
        }
        x!![6] = (m).toDouble()
    }
    if ((((R.D2I(x!![0])).also { __v -> m = __v }) == ROADWAY_WEIR)) {
        if ((ntoks >= 11)) {
            if (((!(getDouble(tok!![10], DElem(x!!, 7)) != 0)) || (x!![7] < 0.0))) {
                return error_setInpError(ERR_NUMBER, tok!![10])
            }
        }
        if ((ntoks >= 12)) {
            if ((strcomp(tok!![11], __L311) != 0)) {
                x!![8] = 1.0
            } else if ((strcomp(tok!![11], __L312) != 0)) {
                x!![8] = 2.0
            }
        }
    }
    if (((ntoks >= 13) && (tok!![12]!!.g(0) != 42))) {
        m = project_findObject(CURVE, tok!![12])
        if ((m < 0)) {
            return error_setInpError(ERR_NAME, tok!![12])
        }
        x!![9] = (m).toDouble()
    }
    Link!![j]!!.ID = id
    link_setParams(j, WEIR, n1, n2, k, x)
    return 0
}

// link.c:2103
fun weir_validate(j: Int, k: Int) {
    var err: Int = 0
    var q: Double = 0.0
    val q1 = DBox(0.0)
    val q2 = DBox(0.0)
    var head: Double = 0.0
    when (Weir!![k]!!.type) {
        TRANSVERSE_WEIR, SIDEFLOW_WEIR, ROADWAY_WEIR -> {
            if ((Link!![j]!!.xsect!!.type != RECT_OPEN)) {
                err = ERR_REGULATOR_SHAPE
            }
            Weir!![k]!!.slope = 0.0
        }
        VNOTCH_WEIR -> {
            if ((Link!![j]!!.xsect!!.type != TRIANGULAR)) {
                err = ERR_REGULATOR_SHAPE
            } else {
                Weir!![k]!!.slope = Link!![j]!!.xsect!!.sBot
            }
        }
        TRAPEZOIDAL_WEIR -> {
            if ((Link!![j]!!.xsect!!.type != TRAPEZOIDAL)) {
                err = ERR_REGULATOR_SHAPE
            } else {
                Weir!![k]!!.slope = Link!![j]!!.xsect!!.sBot
            }
        }
    }
    if ((err > 0)) {
        report_writeErrorMsg(err, Link!![j]!!.ID)
        return
    }
    if ((Link!![j]!!.offset1 < 0.0)) {
        Link!![j]!!.offset1 = 0.0
    }
    Weir!![k]!!.length = ((2.0 * RouteStep) * sqrt((32.2 * Link!![j]!!.xsect!!.yFull)))
    Weir!![k]!!.length = ((if ((((200.0) >= (Weir!![k]!!.length)))) (200.0) else (Weir!![k]!!.length)))
    Weir!![k]!!.surfArea = 0.0
    head = Link!![j]!!.xsect!!.yFull
    weir_getFlow(j, k, head, 1.0, 0, q1, q2)
    q = (q1.v + q2.v)
    head = (head / 2.0)
    Weir!![k]!!.cSurcharge = (q / sqrt(head))
}

// link.c:2166
fun weir_setSetting(j: Int) {
    var k: Int = Link!![j]!!.subIndex
    var h: Double = 0.0
    var q: Double = 0.0
    val q1 = DBox(0.0)
    val q2 = DBox(0.0)
    Link!![j]!!.setting = Link!![j]!!.targetSetting
    if (!(Weir!![k]!!.canSurcharge != 0)) {
        return
    }
    if ((Weir!![k]!!.type == ROADWAY_WEIR)) {
        return
    }
    if ((Link!![j]!!.setting == 0.0)) {
        Weir!![k]!!.cSurcharge = 0.0
    } else {
        h = (Link!![j]!!.setting * Link!![j]!!.xsect!!.yFull)
        weir_getFlow(j, k, h, 1.0, 0, q1, q2)
        q = (q1.v + q2.v)
        h = (h / 2.0)
        Weir!![k]!!.cSurcharge = (q / sqrt(h))
    }
}

// link.c:2198
fun weir_getInflow(j: Int): Double {
    var n1: Int = 0
    var n2: Int = 0
    var k: Int = 0
    val q1 = DBox(0.0)
    val q2 = DBox(0.0)
    var head: Double = 0.0
    var h1: Double = 0.0
    var h2: Double = 0.0
    var hcrest: Double = 0.0
    var hcrown: Double = 0.0
    var y: Double = 0.0
    var dir: Double = 0.0
    var ratio: Double = 0.0
    var weirPower: DoubleArray? = doubleArrayOf(1.5, (5.0 / 3.0), 2.5, 1.5)
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    k = Link!![j]!!.subIndex
    if ((RouteModel == DW)) {
        h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
        h2 = (Node!![n2]!!.newDepth + Node!![n2]!!.invertElev)
    } else {
        h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
        h2 = Node!![n1]!!.invertElev
    }
    dir = (if (((h1 > h2))) 1.0 else (-1.0))
    if ((dir < 0.0)) {
        head = h1
        h1 = h2
        h2 = head
    }
    hcrest = (Node!![n1]!!.invertElev + Link!![j]!!.offset1)
    hcrown = (hcrest + Link!![j]!!.xsect!!.yFull)
    if ((Weir!![k]!!.type == ROADWAY_WEIR)) {
        return roadway_getInflow(j, dir, hcrest, h1, h2)
    }
    hcrest += (((1.0 - Link!![j]!!.setting)) * Link!![j]!!.xsect!!.yFull)
    head = (h1 - hcrest)
    Link!![j]!!.dqdh = 0.0
    if ((((head <= 0.0001) || (hcrest >= hcrown)) || (link_setFlapGate(j, n1, n2, dir) != 0))) {
        Link!![j]!!.newDepth = 0.0
        Link!![j]!!.flowClass = DRY
        return 0.0
    }
    Link!![j]!!.flowClass = SUBCRITICAL
    if ((hcrest > h2)) {
        if ((dir == 1.0)) {
            Link!![j]!!.flowClass = DN_CRITICAL
        } else {
            Link!![j]!!.flowClass = UP_CRITICAL
        }
    }
    y = (Link!![j]!!.xsect!!.yFull - ((hcrown - ((if ((((h1) <= (hcrown)))) (h1) else (hcrown))))))
    Weir!![k]!!.surfArea = (xsect_getWofY(Link!![j]!!.xsect, y) * Weir!![k]!!.length)
    if ((h1 >= hcrown)) {
        if ((Weir!![k]!!.canSurcharge != 0)) {
            y = (((hcrest + hcrown)) / 2.0)
            if ((h2 < y)) {
                head = (h1 - y)
            } else {
                head = (h1 - h2)
            }
            y = (hcrown - hcrest)
            q1.v = weir_getOrificeFlow(j, head, y, Weir!![k]!!.cSurcharge)
            Link!![j]!!.newDepth = y
            return (dir * q1.v)
        } else {
            head = (hcrown - hcrest)
        }
    }
    weir_getFlow(j, k, head, dir, Link!![j]!!.hasFlapGate, q1, q2)
    if ((h2 > hcrest)) {
        ratio = (((h2 - hcrest)) / ((h1 - hcrest)))
        q1.v *= (Glibm.pow(((1.0 - Glibm.pow(ratio, weirPower!![Weir!![k]!!.type]))), 0.385))
        if ((q2.v > 0.0)) {
            q2.v *= (Glibm.pow(((1.0 - Glibm.pow(ratio, weirPower!![VNOTCH_WEIR]))), 0.385))
        }
    }
    Link!![j]!!.newDepth = ((if ((((((h1 - hcrest))) <= (Link!![j]!!.xsect!!.yFull)))) (((h1 - hcrest))) else (Link!![j]!!.xsect!!.yFull)))
    return (dir * ((q1.v + q2.v)))
}

// link.c:2321
fun weir_getFlow(j: Int, k: Int, __head: Double, dir: Double, hasFlapGate: Int, q1: DRef?, q2: DRef?) {
    var head: Double = __head
    var length: Double = 0.0
    var h: Double = 0.0
    var y: Double = 0.0
    var hLoss: Double = 0.0
    var area: Double = 0.0
    var veloc: Double = 0.0
    var wType: Int = 0
    var cdCurve: Int = Weir!![k]!!.cdCurve
    var cDisch1: Double = Weir!![k]!!.cDisch1
    q1!!.set(0.0)
    q2!!.set(0.0)
    Link!![j]!!.dqdh = 0.0
    if ((head <= 0.0)) {
        return
    }
    length = (Link!![j]!!.xsect!!.wMax * UCF(LENGTH))
    h = (head * UCF(LENGTH))
    if ((cdCurve >= 0)) {
        cDisch1 = table_lookup(Curve!![cdCurve], h)
    }
    wType = Weir!![k]!!.type
    if (((wType == VNOTCH_WEIR) && (Link!![j]!!.setting < 1.0))) {
        wType = TRAPEZOIDAL_WEIR
    }
    when (wType) {
        TRANSVERSE_WEIR -> {
            length -= ((0.1 * Weir!![k]!!.endCon) * h)
            length = ((if ((((length) >= (0.0)))) (length) else (0.0)))
            q1!!.set(((cDisch1 * length) * Glibm.pow(h, 1.5)))
        }
        SIDEFLOW_WEIR -> {
            length -= ((0.1 * Weir!![k]!!.endCon) * h)
            length = ((if ((((length) >= (0.0)))) (length) else (0.0)))
            if ((dir < 0.0)) {
                q1!!.set(((cDisch1 * length) * Glibm.pow(h, 1.5)))
            } else {
                q1!!.set(((cDisch1 * Glibm.pow(length, 0.83)) * Glibm.pow(h, 1.67)))
            }
        }
        VNOTCH_WEIR -> {
            q1!!.set(((cDisch1 * Weir!![k]!!.slope) * Glibm.pow(h, 2.5)))
        }
        TRAPEZOIDAL_WEIR -> {
            y = (((1.0 - Link!![j]!!.setting)) * Link!![j]!!.xsect!!.yFull)
            length = (xsect_getWofY(Link!![j]!!.xsect, y) * UCF(LENGTH))
            q1!!.set(((cDisch1 * length) * Glibm.pow(h, 1.5)))
            q2!!.set(((Weir!![k]!!.cDisch2 * Weir!![k]!!.slope) * Glibm.pow(h, 2.5)))
        }
    }
    if ((UnitSystem == SI)) {
        q1!!.set((q1!!.get() / 0.028317))
        q2!!.set((q2!!.get() / 0.028317))
    }
    if ((hasFlapGate != 0)) {
        area = weir_getOpenArea(j, head)
        if ((area > 1e-06)) {
            veloc = (((q1!!.get() + q2!!.get())) / area)
            hLoss = (((((4.0 / 32.2)) * veloc) * veloc) * Glibm.exp((((-1.15) * veloc) / sqrt(head))))
            head = (head - hLoss)
            if ((head < 0.0)) {
                head = 0.0
            }
            weir_getFlow(j, k, head, dir, 0, q1, q2)
        }
    }
    Link!![j]!!.dqdh = weir_getdqdh(k, dir, head, q1!!.get(), q2!!.get())
}

// link.c:2432
fun weir_getOrificeFlow(j: Int, __head: Double, y: Double, cOrif: Double): Double {
    var head: Double = __head
    var a: Double = 0.0
    var q: Double = 0.0
    var v: Double = 0.0
    var hloss: Double = 0.0
    q = (cOrif * sqrt(head))
    if ((Link!![j]!!.hasFlapGate != 0)) {
        a = weir_getOpenArea(j, y)
        if ((a > 0.0)) {
            v = (q / a)
            hloss = (((((4.0 / 32.2)) * v) * v) * Glibm.exp((((-1.15) * v) / sqrt(y))))
            head -= hloss
            head = ((if ((((head) >= (0.0)))) (head) else (0.0)))
            q = (cOrif * sqrt(head))
        }
    }
    if ((head > 0.0)) {
        Link!![j]!!.dqdh = (q / ((2.0 * head)))
    } else {
        Link!![j]!!.dqdh = 0.0
    }
    return q
}

// link.c:2467
fun weir_getOpenArea(j: Int, y: Double): Double {
    var z: Double = 0.0
    var zy: Double = 0.0
    z = (((1.0 - Link!![j]!!.setting)) * Link!![j]!!.xsect!!.yFull)
    zy = (z + y)
    zy = ((if ((((zy) <= (Link!![j]!!.xsect!!.yFull)))) (zy) else (Link!![j]!!.xsect!!.yFull)))
    return (xsect_getAofY(Link!![j]!!.xsect, zy) - xsect_getAofY(Link!![j]!!.xsect, z))
}

// link.c:2492
fun weir_getdqdh(k: Int, dir: Double, h: Double, q1: Double, q2: Double): Double {
    var q1h: Double = 0.0
    var q2h: Double = 0.0
    if ((abs(h) < 0.0001)) {
        return 0.0
    }
    q1h = abs((q1 / h))
    q2h = abs((q2 / h))
    when (Weir!![k]!!.type) {
        TRANSVERSE_WEIR -> {
            return (1.5 * q1h)
        }
        SIDEFLOW_WEIR -> {
            if ((dir < 0.0)) {
                return (1.5 * q1h)
            } else {
                return (1.67 * q1h)
            }
        }
        VNOTCH_WEIR -> {
            if ((q2h == 0.0)) {
                return (2.5 * q1h)
            } else {
                return ((1.5 * q1h) + (2.5 * q2h))
            }
        }
        TRAPEZOIDAL_WEIR -> {
            return ((1.5 * q1h) + (2.5 * q2h))
        }
    }
    return 0.0
}

// link.c:2524
fun outlet_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var m: Int = 0
    var n: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    var id: CPtr? = null
    var s: CPtr? = null
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(LINK, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    n1 = project_findObject(NODE, tok!![1])
    if ((n1 < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    n2 = project_findObject(NODE, tok!![2])
    if ((n2 < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    if (((LinkOffsets == ELEV_OFFSET) && (tok!![3]!!.g(0) == 42))) {
        x!![0] = (-10000000000.0)
    } else {
        if (!(getDouble(tok!![3], DElem(x!!, 0)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![3])
        }
        if (((LinkOffsets == DEPTH_OFFSET) && (x!![0] < 0.0))) {
            x!![0] = 0.0
        }
    }
    m = findmatch(tok!![4], RelationWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![4])
    }
    x!![1] = 0.0
    x!![2] = 0.0
    x!![3] = (-1.0)
    x!![4] = 0.0
    x!![5] = (NODE_DEPTH).toDouble()
    s = R.strtok(tok!![4], __L313)
    s = R.strtok(null, __L313)
    if ((strcomp(s, __L314) != 0)) {
        x!![5] = (NODE_HEAD).toDouble()
    }
    if ((m == FUNCTIONAL)) {
        if ((ntoks < 7)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if (!(getDouble(tok!![5], DElem(x!!, 1)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
        if (!(getDouble(tok!![6], DElem(x!!, 2)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![6])
        }
        n = 7
    } else {
        i = project_findObject(CURVE, tok!![5])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![5])
        }
        x!![3] = (i).toDouble()
        n = 6
    }
    if ((ntoks > n)) {
        i = findmatch(tok!![n], NoYesWords)
        if ((i < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![n])
        }
        x!![4] = (i).toDouble()
    }
    Link!![j]!!.ID = id
    link_setParams(j, OUTLET, n1, n2, k, x)
    return 0
}

// link.c:2608
fun outlet_getInflow(j: Int): Double {
    var k: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var head: Double = 0.0
    var hcrest: Double = 0.0
    var h1: Double = 0.0
    var h2: Double = 0.0
    var y1: Double = 0.0
    var dir: Double = 0.0
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    k = Link!![j]!!.subIndex
    if ((RouteModel == DW)) {
        h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
        h2 = (Node!![n2]!!.newDepth + Node!![n2]!!.invertElev)
    } else {
        h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
        h2 = Node!![n1]!!.invertElev
    }
    dir = (if (((h1 >= h2))) 1.0 else (-1.0))
    y1 = Node!![n1]!!.newDepth
    if ((dir < 0.0)) {
        y1 = h1
        h1 = h2
        h2 = y1
        y1 = Node!![n2]!!.newDepth
    }
    hcrest = (Node!![n1]!!.invertElev + Link!![j]!!.offset1)
    if (((Outlet!![k]!!.curveType == NODE_HEAD) && (RouteModel == DW))) {
        head = (h1 - ((if ((((h2) >= (hcrest)))) (h2) else (hcrest))))
    } else {
        head = (h1 - hcrest)
    }
    if ((((head <= 0.0001) || (y1 <= 0.0001)) || (link_setFlapGate(j, n1, n2, dir) != 0))) {
        Link!![j]!!.newDepth = 0.0
        Link!![j]!!.flowClass = DRY
        return 0.0
    }
    Link!![j]!!.newDepth = head
    Link!![j]!!.flowClass = SUBCRITICAL
    return ((dir * Link!![j]!!.setting) * outlet_getFlow(k, head))
}

// link.c:2672
fun outlet_getFlow(k: Int, head: Double): Double {
    var m: Int = 0
    var h: Double = 0.0
    h = (head * UCF(LENGTH))
    m = Outlet!![k]!!.qCurve
    if ((m >= 0)) {
        return (table_lookup(Curve!![m], h) / UCF(FLOW))
    } else {
        return ((Outlet!![k]!!.qCoeff * Glibm.pow(h, Outlet!![k]!!.qExpon)) / UCF(FLOW))
    }
    throw IllegalStateException("outlet_getFlow: no return")
}

