// Generated from EPA SWMM 5.2.4 (dwflow.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_dwflow")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MAXVELOCITY: Double = 50.0

// dwflow.c:57
fun dwflow_findConduitFlow(j: Int, steps: Int, omega: Double, dt: Double) {
    var k: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var z1: Double = 0.0
    var z2: Double = 0.0
    val h1 = DBox(0.0)
    val h2 = DBox(0.0)
    val y1 = DBox(0.0)
    val y2 = DBox(0.0)
    var a1: Double = 0.0
    var a2: Double = 0.0
    var r1: Double = 0.0
    var yMid: Double = 0.0
    var rMid: Double = 0.0
    var aMid: Double = 0.0
    var aWtd: Double = 0.0
    var rWtd: Double = 0.0
    var qLast: Double = 0.0
    var qOld: Double = 0.0
    var aOld: Double = 0.0
    var v: Double = 0.0
    var rho: Double = 0.0
    var sigma: Double = 0.0
    var length: Double = 0.0
    var wSlot: Double = 0.0
    var dq1: Double = 0.0
    var dq2: Double = 0.0
    var dq3: Double = 0.0
    var dq4: Double = 0.0
    var dq5: Double = 0.0
    var dq6: Double = 0.0
    var denom: Double = 0.0
    var q: Double = 0.0
    var barrels: Double = 0.0
    var xsect: TXsect? = Link!![j]!!.xsect
    var isFull: Int = 0
    var isClosed: Int = 0
    if ((Link!![j]!!.setting == 0.0)) {
        isClosed = 1
    }
    k = Link!![j]!!.subIndex
    barrels = (Conduit!![k]!!.barrels).toDouble()
    qOld = (Link!![j]!!.oldFlow / barrels)
    qLast = Conduit!![k]!!.q1
    Conduit!![k]!!.evapLossRate = 0.0
    Conduit!![k]!!.seepLossRate = 0.0
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    z1 = (Node!![n1]!!.invertElev + Link!![j]!!.offset1)
    z2 = (Node!![n2]!!.invertElev + Link!![j]!!.offset2)
    h1.v = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
    h2.v = (Node!![n2]!!.newDepth + Node!![n2]!!.invertElev)
    h1.v = ((if ((((h1.v) >= (z1)))) (h1.v) else (z1)))
    h2.v = ((if ((((h2.v) >= (z2)))) (h2.v) else (z2)))
    y1.v = (h1.v - z1)
    y2.v = (h2.v - z2)
    y1.v = ((if ((((y1.v) >= (0.0001)))) (y1.v) else (0.0001)))
    y2.v = ((if ((((y2.v) >= (0.0001)))) (y2.v) else (0.0001)))
    if ((SurchargeMethod != SLOT)) {
        y1.v = ((if ((((y1.v) <= (xsect!!.yFull)))) (y1.v) else (xsect!!.yFull)))
        y2.v = ((if ((((y2.v) <= (xsect!!.yFull)))) (y2.v) else (xsect!!.yFull)))
    }
    aOld = Conduit!![k]!!.a2
    aOld = ((if ((((aOld) >= (0.0001)))) (aOld) else (0.0001)))
    length = Conduit!![k]!!.modLength
    findSurfArea(j, qLast, length, h1, h2, y1, y2)
    wSlot = getSlotWidth(xsect, y1.v)
    a1 = dwflow_getArea(xsect, y1.v, wSlot)
    r1 = getHydRad(xsect, y1.v)
    wSlot = getSlotWidth(xsect, y2.v)
    a2 = dwflow_getArea(xsect, y2.v, wSlot)
    yMid = (0.5 * ((y1.v + y2.v)))
    wSlot = getSlotWidth(xsect, yMid)
    aMid = dwflow_getArea(xsect, yMid, wSlot)
    rMid = getHydRad(xsect, yMid)
    if (((y1.v >= xsect!!.yFull) && (y2.v >= xsect!!.yFull))) {
        isFull = 1
    }
    if ((((((Link!![j]!!.flowClass == DRY) || (Link!![j]!!.flowClass == UP_DRY)) || (Link!![j]!!.flowClass == DN_DRY)) || (isClosed != 0)) || (aMid <= 0.0001))) {
        Conduit!![k]!!.a1 = (0.5 * ((a1 + a2)))
        Conduit!![k]!!.q1 = 0.0
        Conduit!![k]!!.q2 = 0.0
        Link!![j]!!.dqdh = ((((32.2 * dt) * aMid) / length) * barrels)
        Link!![j]!!.froude = 0.0
        Link!![j]!!.newDepth = ((if ((((yMid) <= (Link!![j]!!.xsect!!.yFull)))) (yMid) else (Link!![j]!!.xsect!!.yFull)))
        Link!![j]!!.newVolume = ((Conduit!![k]!!.a1 * link_getLength(j)) * barrels)
        Link!![j]!!.newFlow = 0.0
        return
    }
    v = (qLast / aMid)
    if ((abs(v) > MAXVELOCITY)) {
        v = (MAXVELOCITY * (((if ((((qLast) < 0.0))) ((-1)) else (1)))).toDouble())
    }
    Link!![j]!!.froude = link_getFroude(j, v, yMid)
    if (((Link!![j]!!.flowClass == SUBCRITICAL) && (Link!![j]!!.froude > 1.0))) {
        Link!![j]!!.flowClass = SUPCRITICAL
    }
    if ((Link!![j]!!.froude <= 0.5)) {
        sigma = 1.0
    } else if ((Link!![j]!!.froude >= 1.0)) {
        sigma = 0.0
    } else {
        sigma = (2.0 * ((1.0 - Link!![j]!!.froude)))
    }
    rho = 1.0
    if ((((!(isFull != 0)) && (qLast > 0.0)) && (h1.v >= h2.v))) {
        rho = sigma
    }
    aWtd = (a1 + (((aMid - a1)) * rho))
    rWtd = (r1 + (((rMid - r1)) * rho))
    if ((InertDamping == NO_DAMPING)) {
        sigma = 1.0
    } else if ((InertDamping == FULL_DAMPING)) {
        sigma = 0.0
    }
    if (((isFull != 0) && (!(xsect_isOpen(xsect!!.type) != 0)))) {
        sigma = 0.0
    }
    if (((xsect!!.type == FORCE_MAIN) && (isFull != 0))) {
        dq1 = (dt * forcemain_getFricSlope(j, abs(v), rMid))
    } else {
        dq1 = (((dt * Conduit!![k]!!.roughFactor) / Glibm.pow(rWtd, 1.33333)) * abs(v))
    }
    dq2 = ((((dt * 32.2) * aWtd) * ((h2.v - h1.v))) / length)
    dq3 = 0.0
    dq4 = 0.0
    if ((sigma > 0.0)) {
        dq3 = (((2.0 * v) * ((aMid - aOld))) * sigma)
        dq4 = (((((dt * v) * v) * ((a2 - a1))) / length) * sigma)
    }
    dq5 = 0.0
    if ((Conduit!![k]!!.hasLosses != 0)) {
        dq5 = (((findLocalLosses(j, a1, a2, aMid, qLast) / 2.0) / length) * dt)
    }
    dq6 = ((((link_getLossRate(j, DW, qLast, dt) * 2.5) * dt) * v) / link_getLength(j))
    denom = ((1.0 + dq1) + dq5)
    q = ((((((qOld - dq2) + dq3) + dq4) + dq6)) / denom)
    Link!![j]!!.dqdh = ((((((1.0 / denom) * 32.2) * dt) * aWtd) / length) * barrels)
    Link!![j]!!.inletControl = 0
    Link!![j]!!.normalFlow = 0
    if ((q > 0.0)) {
        if (((xsect!!.culvertCode > 0) && (!(isFull != 0)))) {
            q = culvert_getInflow(j, q, h1.v)
        } else if ((((NormalFlowLtd != NEITHER) && (y1.v < Link!![j]!!.xsect!!.yFull)) && (((Link!![j]!!.flowClass == SUBCRITICAL) || (Link!![j]!!.flowClass == SUPCRITICAL))))) {
            q = checkNormalFlow(j, q, y1.v, y2.v, a1, r1)
        }
    }
    if ((steps > 0)) {
        q = ((((1.0 - omega)) * qLast) + (omega * q))
        if (((q * qLast) < 0.0)) {
            q = (0.001 * (((if ((((q) < 0.0))) ((-1)) else (1)))).toDouble())
        }
    }
    if ((Link!![j]!!.qLimit > 0.0)) {
        if ((abs(q) > Link!![j]!!.qLimit)) {
            q = ((((if ((((q) < 0.0))) ((-1)) else (1)))).toDouble() * Link!![j]!!.qLimit)
        }
    }
    if ((link_setFlapGate(j, n1, n2, q) != 0)) {
        q = 0.0
    }
    if (((q > 0.0001) && (Node!![n1]!!.newDepth <= 0.0001))) {
        q = 0.0001
    }
    if (((q < (-0.0001)) && (Node!![n2]!!.newDepth <= 0.0001))) {
        q = (-0.0001)
    }
    Conduit!![k]!!.a1 = aMid
    Conduit!![k]!!.q1 = q
    Conduit!![k]!!.q2 = q
    Link!![j]!!.newDepth = ((if ((((yMid) <= (xsect!!.yFull)))) (yMid) else (xsect!!.yFull)))
    aMid = (((a1 + a2)) / 2.0)
    Conduit!![k]!!.fullState = link_getFullState(a1, a2, xsect!!.aFull)
    Link!![j]!!.newVolume = ((aMid * link_getLength(j)) * barrels)
    Link!![j]!!.newFlow = (q * barrels)
}

// dwflow.c:297
fun getFlowClass(j: Int, q: Double, h1: Double, h2: Double, y1: Double, y2: Double, yC: DRef?, yN: DRef?, fasnh: DRef?): Int {
    var n1: Int = 0
    var n2: Int = 0
    var flowClass: Int = 0
    var ycMin: Double = 0.0
    var ycMax: Double = 0.0
    var z1: Double = 0.0
    var z2: Double = 0.0
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    z1 = Link!![j]!!.offset1
    z2 = Link!![j]!!.offset2
    if ((Node!![n1]!!.type == OUTFALL)) {
        z1 = ((if ((((0.0) >= (((z1 - Node!![n1]!!.newDepth)))))) (0.0) else (((z1 - Node!![n1]!!.newDepth)))))
    }
    if ((Node!![n2]!!.type == OUTFALL)) {
        z2 = ((if ((((0.0) >= (((z2 - Node!![n2]!!.newDepth)))))) (0.0) else (((z2 - Node!![n2]!!.newDepth)))))
    }
    flowClass = SUBCRITICAL
    fasnh!!.set(1.0)
    if (((y1 > 0.0001) && (y2 > 0.0001))) {
        if ((q < 0.0)) {
            if ((z1 > 0.0)) {
                yN!!.set(link_getYnorm(j, abs(q)))
                yC!!.set(link_getYcrit(j, abs(q)))
                ycMin = ((if ((((yN!!.get()) <= (yC!!.get())))) (yN!!.get()) else (yC!!.get())))
                if ((y1 < ycMin)) {
                    flowClass = UP_CRITICAL
                }
            }
        } else {
            if ((z2 > 0.0)) {
                yN!!.set(link_getYnorm(j, abs(q)))
                yC!!.set(link_getYcrit(j, abs(q)))
                ycMin = ((if ((((yN!!.get()) <= (yC!!.get())))) (yN!!.get()) else (yC!!.get())))
                ycMax = ((if ((((yN!!.get()) >= (yC!!.get())))) (yN!!.get()) else (yC!!.get())))
                if ((y2 < ycMin)) {
                    flowClass = DN_CRITICAL
                } else if ((y2 < ycMax)) {
                    if (((ycMax - ycMin) < 0.0001)) {
                        fasnh!!.set(0.0)
                    } else {
                        fasnh!!.set((((ycMax - y2)) / ((ycMax - ycMin))))
                    }
                }
            }
        }
    } else if (((y1 <= 0.0001) && (y2 <= 0.0001))) {
        flowClass = DRY
    } else if ((y2 > 0.0001)) {
        if ((h2 < (Node!![n1]!!.invertElev + Link!![j]!!.offset1))) {
            flowClass = UP_DRY
        } else if ((z1 > 0.0)) {
            yN!!.set(link_getYnorm(j, abs(q)))
            yC!!.set(link_getYcrit(j, abs(q)))
            flowClass = UP_CRITICAL
        }
    } else {
        if ((h1 < (Node!![n2]!!.invertElev + Link!![j]!!.offset2))) {
            flowClass = DN_DRY
        } else if ((z2 > 0.0)) {
            yN!!.set(link_getYnorm(j, abs(q)))
            yC!!.set(link_getYcrit(j, abs(q)))
            flowClass = DN_CRITICAL
        }
    }
    return flowClass
}

// dwflow.c:417
fun findSurfArea(j: Int, q: Double, length: Double, h1: DRef?, h2: DRef?, y1: DRef?, y2: DRef?) {
    var n1: Int = 0
    var n2: Int = 0
    var flowDepth1: Double = 0.0
    var flowDepth2: Double = 0.0
    var flowDepthMid: Double = 0.0
    var width1: Double = 0.0
    var width2: Double = 0.0
    var widthMid: Double = 0.0
    var surfArea1: Double = 0.0
    var surfArea2: Double = 0.0
    val criticalDepth = DBox(0.0)
    val normalDepth = DBox(0.0)
    var fullDepth: Double = 0.0
    val fasnh = DBox(1.0)
    var xsect: TXsect? = Link!![j]!!.xsect
    n1 = Link!![j]!!.node1
    n2 = Link!![j]!!.node2
    flowDepth1 = y1!!.get()
    flowDepth2 = y2!!.get()
    normalDepth.v = (((flowDepth1 + flowDepth2)) / 2.0)
    criticalDepth.v = normalDepth.v
    fullDepth = xsect!!.yFull
    if (((flowDepth1 >= fullDepth) && (flowDepth2 >= fullDepth))) {
        Link!![j]!!.flowClass = SUBCRITICAL
    } else {
        Link!![j]!!.flowClass = getFlowClass(j, q, h1!!.get(), h2!!.get(), y1!!.get(), y2!!.get(), criticalDepth, normalDepth, fasnh)
    }
    when (Link!![j]!!.flowClass) {
        SUBCRITICAL -> {
            flowDepthMid = (0.5 * ((flowDepth1 + flowDepth2)))
            if ((flowDepthMid < 0.0001)) {
                flowDepthMid = 0.0001
            }
            width1 = dwflow_getWidth(xsect, flowDepth1)
            width2 = dwflow_getWidth(xsect, flowDepth2)
            widthMid = dwflow_getWidth(xsect, flowDepthMid)
            surfArea1 = ((((width1 + widthMid)) * length) / 4.0)
            surfArea2 = (((((widthMid + width2)) * length) / 4.0) * fasnh.v)
        }
        UP_CRITICAL -> {
            flowDepth1 = criticalDepth.v
            if ((normalDepth.v < criticalDepth.v)) {
                flowDepth1 = normalDepth.v
            }
            flowDepth1 = ((if ((((flowDepth1) >= (0.0001)))) (flowDepth1) else (0.0001)))
            h1!!.set(((Node!![n1]!!.invertElev + Link!![j]!!.offset1) + flowDepth1))
            flowDepthMid = (0.5 * ((flowDepth1 + flowDepth2)))
            if ((flowDepthMid < 0.0001)) {
                flowDepthMid = 0.0001
            }
            width2 = dwflow_getWidth(xsect, flowDepth2)
            widthMid = dwflow_getWidth(xsect, flowDepthMid)
            surfArea2 = ((((widthMid + width2)) * length) * 0.5)
        }
        DN_CRITICAL -> {
            flowDepth2 = criticalDepth.v
            if ((normalDepth.v < criticalDepth.v)) {
                flowDepth2 = normalDepth.v
            }
            flowDepth2 = ((if ((((flowDepth2) >= (0.0001)))) (flowDepth2) else (0.0001)))
            h2!!.set(((Node!![n2]!!.invertElev + Link!![j]!!.offset2) + flowDepth2))
            width1 = dwflow_getWidth(xsect, flowDepth1)
            flowDepthMid = (0.5 * ((flowDepth1 + flowDepth2)))
            if ((flowDepthMid < 0.0001)) {
                flowDepthMid = 0.0001
            }
            widthMid = dwflow_getWidth(xsect, flowDepthMid)
            surfArea1 = ((((width1 + widthMid)) * length) * 0.5)
        }
        UP_DRY -> {
            flowDepth1 = 0.0001
            flowDepthMid = (0.5 * ((flowDepth1 + flowDepth2)))
            if ((flowDepthMid < 0.0001)) {
                flowDepthMid = 0.0001
            }
            width1 = dwflow_getWidth(xsect, flowDepth1)
            width2 = dwflow_getWidth(xsect, flowDepth2)
            widthMid = dwflow_getWidth(xsect, flowDepthMid)
            surfArea2 = ((((widthMid + width2)) * length) / 4.0)
            if ((Link!![j]!!.offset1 <= 0.0)) {
                surfArea1 = ((((width1 + widthMid)) * length) / 4.0)
            }
        }
        DN_DRY -> {
            flowDepth2 = 0.0001
            flowDepthMid = (0.5 * ((flowDepth1 + flowDepth2)))
            if ((flowDepthMid < 0.0001)) {
                flowDepthMid = 0.0001
            }
            width1 = dwflow_getWidth(xsect, flowDepth1)
            width2 = dwflow_getWidth(xsect, flowDepth2)
            widthMid = dwflow_getWidth(xsect, flowDepthMid)
            surfArea1 = ((((widthMid + width1)) * length) / 4.0)
            if ((Link!![j]!!.offset2 <= 0.0)) {
                surfArea2 = ((((width2 + widthMid)) * length) / 4.0)
            }
        }
        DRY -> {
            surfArea1 = ((0.0001 * length) / 2.0)
            surfArea2 = surfArea1
        }
    }
    Link!![j]!!.surfArea1 = surfArea1
    Link!![j]!!.surfArea2 = surfArea2
    y1!!.set(flowDepth1)
    y2!!.set(flowDepth2)
}

// dwflow.c:554
fun findLocalLosses(j: Int, a1: Double, a2: Double, aMid: Double, __q: Double): Double {
    var q: Double = __q
    var losses: Double = 0.0
    q = abs(q)
    if ((a1 > 0.0001)) {
        losses += (Link!![j]!!.cLossInlet * ((q / a1)))
    }
    if ((a2 > 0.0001)) {
        losses += (Link!![j]!!.cLossOutlet * ((q / a2)))
    }
    if ((aMid > 0.0001)) {
        losses += (Link!![j]!!.cLossAvg * ((q / aMid)))
    }
    return losses
}

// dwflow.c:575
fun getSlotWidth(xsect: TXsect?, y: Double): Double {
    var yNorm: Double = (y / xsect!!.yFull)
    if ((((SurchargeMethod != SLOT) || (xsect_isOpen(xsect!!.type) != 0)) || (yNorm < CrownCutoff))) {
        return 0.0
    }
    if ((yNorm > 1.78)) {
        return (0.01 * xsect!!.wMax)
    }
    return ((xsect!!.wMax * 0.5423) * Glibm.exp((-(Glibm.pow(yNorm, 2.4)))))
}

// dwflow.c:592
fun dwflow_getWidth(xsect: TXsect?, __y: Double): Double {
    var y: Double = __y
    var wSlot: Double = getSlotWidth(xsect, y)
    if ((wSlot > 0.0)) {
        return wSlot
    }
    if ((((y / xsect!!.yFull) >= CrownCutoff) && (!(xsect_isOpen(xsect!!.type) != 0)))) {
        y = (CrownCutoff * xsect!!.yFull)
    }
    return xsect_getWofY(xsect, y)
}

// dwflow.c:609
fun dwflow_getArea(xsect: TXsect?, y: Double, wSlot: Double): Double {
    if ((y >= xsect!!.yFull)) {
        return (xsect!!.aFull + (((y - xsect!!.yFull)) * wSlot))
    }
    return xsect_getAofY(xsect, y)
}

// dwflow.c:623
fun getHydRad(xsect: TXsect?, y: Double): Double {
    if ((y >= xsect!!.yFull)) {
        return xsect!!.rFull
    }
    return xsect_getRofY(xsect, y)
}

// dwflow.c:637
fun checkNormalFlow(j: Int, q: Double, y1: Double, y2: Double, a1: Double, r1: Double): Double {
    var check: Int = 0
    var k: Int = Link!![j]!!.subIndex
    var n1: Int = Link!![j]!!.node1
    var n2: Int = Link!![j]!!.node2
    var hasOutfall: Int = (if ((((Node!![n1]!!.type == OUTFALL) || (Node!![n2]!!.type == OUTFALL)))) 1 else 0)
    var qNorm: Double = 0.0
    var f1: Double = 0.0
    if ((((NormalFlowLtd == SLOPE) || (NormalFlowLtd == BOTH)) || (hasOutfall != 0))) {
        if ((y1 < y2)) {
            check = 1
        }
    }
    if ((((!(check != 0)) && (((NormalFlowLtd == FROUDE) || (NormalFlowLtd == BOTH)))) && (!(hasOutfall != 0)))) {
        if (((y1 > 0.0001) && (y2 > 0.0001))) {
            f1 = link_getFroude(j, (q / a1), y1)
            if ((f1 >= 1.0)) {
                check = 1
            }
        }
    }
    if ((check != 0)) {
        qNorm = ((Conduit!![k]!!.beta * a1) * Glibm.pow(r1, (2.0 / 3.0)))
        if ((qNorm < q)) {
            Link!![j]!!.normalFlow = 1
            return qNorm
        }
    }
    return q
}

