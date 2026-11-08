// Generated from EPA SWMM 5.2.4 (flowrout.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_flowrout")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var flowrout_OMEGA: Double = 0.55
@JvmField var MAXITER: Int = 10
@JvmField var STOPTOL: Double = 0.005

// flowrout.c:75
fun flowrout_init(routingModel: Int) {
    if ((routingModel == DW)) {
        validateGeneralLayout()
        dynwave_init()
        if ((Fhotstart1!!.mode == NO_FILE)) {
            initNodeDepths()
            initLinkDepths()
        }
    } else {
        validateTreeLayout()
    }
    initNodes()
    initLinks(routingModel)
}

// flowrout.c:107
fun flowrout_close(routingModel: Int) {
    if ((routingModel == DW)) {
        dynwave_close()
    }
}

// flowrout.c:119
fun flowrout_getRoutingStep(routingModel: Int, fixedStep: Double): Double {
    if ((routingModel == DW)) {
        return dynwave_getRoutingStep(fixedStep)
    }
    return fixedStep
}

// flowrout.c:136
fun flowrout_execute(links: IntArray?, routingModel: Int, tStep: Double): Int {
    var i: Int = 0
    var j: Int = 0
    var n1: Int = 0
    val qin = DBox(0.0)
    val qout = DBox(0.0)
    var steps: Double = 0.0
    if ((ErrorCode != 0)) {
        return 0
    }
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.updated = 0
        Node!![j]!!.overflow = 0.0
        if (((Node!![j]!!.type != STORAGE) && (Node!![j]!!.newVolume > Node!![j]!!.fullVolume))) {
            Node!![j]!!.overflow = (((Node!![j]!!.newVolume - Node!![j]!!.fullVolume)) / tStep)
        }
        j++
    }
    if ((routingModel == DW)) {
        return dynwave_execute(tStep)
    }
    steps = 0.0
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        j = links!![i]
        n1 = Link!![j]!!.node1
        if ((Node!![n1]!!.type == STORAGE)) {
            updateStorageState(n1, i, links, tStep)
        }
        qin.v = getLinkInflow(j, tStep)
        if ((routingModel == SF)) {
            steps += ((steadyflow_execute(j, qin, qout, tStep)).toDouble())
        } else {
            steps += ((kinwave_execute(j, qin, qout, tStep)).toDouble())
        }
        Link!![j]!!.newFlow = qout.v
        Node!![Link!![j]!!.node1]!!.outflow += qin.v
        Node!![Link!![j]!!.node2]!!.inflow += qout.v
        i++
    }
    if ((Nobjects!![LINK] > 0)) {
        steps /= ((Nobjects!![LINK]).toDouble())
    }
    j = 0
    f3@ while ((j < Nobjects!![NODE])) {
        setNewNodeState(j, tStep)
        j++
    }
    j = 0
    f4@ while ((j < Nobjects!![LINK])) {
        setNewLinkState(j)
        j++
    }
    return R.D2I(((steps + 0.5)))
}

// flowrout.c:203
fun validateTreeLayout() {
    var j: Int = 0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        when (Node!![j]!!.type) {
            DIVIDER -> {
                if ((Node!![j]!!.degree > 2)) {
                    report_writeErrorMsg(ERR_DIVIDER, Node!![j]!!.ID)
                }
            }
            OUTFALL -> {
                if ((Node!![j]!!.degree > 0)) {
                    report_writeErrorMsg(ERR_OUTFALL, Node!![j]!!.ID)
                }
            }
            STORAGE -> {
            }
            else -> {
                if ((Node!![j]!!.degree > 1)) {
                    report_writeErrorMsg(ERR_MULTI_OUTLET, Node!![j]!!.ID)
                }
            }
        }
        j++
    }
    j = 0
    f3@ while ((j < Nobjects!![LINK])) {
        when (Link!![j]!!.type) {
            CONDUIT -> {
                if (((Conduit!![Link!![j]!!.subIndex]!!.slope < 0.0) && (Link!![j]!!.xsect!!.type != DUMMY))) {
                    report_writeErrorMsg(ERR_SLOPE, Link!![j]!!.ID)
                }
            }
            ORIFICE, WEIR, OUTLET -> {
                if ((Node!![Link!![j]!!.node1]!!.type != STORAGE)) {
                    report_writeErrorMsg(ERR_REGULATOR, Link!![j]!!.ID)
                }
            }
        }
        j++
    }
}

// flowrout.c:274
fun validateGeneralLayout() {
    var i: Int = 0
    var j: Int = 0
    var outletCount: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        Node!![i]!!.inflow = 0.0
        i++
    }
    j = 0
    f2@ while ((j < Nobjects!![LINK])) {
        i = Link!![j]!!.node1
        if ((Node!![i]!!.type != OUTFALL)) {
            i = Link!![j]!!.node2
        }
        Node!![i]!!.inflow += 1.0
        if (((((Link!![j]!!.type == CONDUIT) && (Link!![j]!!.xsect!!.type == DUMMY))) || (((Link!![j]!!.type == PUMP) && (Pump!![Link!![j]!!.subIndex]!!.type == IDEAL_PUMP))))) {
            i = Link!![j]!!.node1
            if ((Link!![j]!!.direction < 0)) {
                i = Link!![j]!!.node2
            }
            if ((Node!![i]!!.degree > 1)) {
                report_writeErrorMsg(ERR_DUMMY_LINK, Node!![i]!!.ID)
            }
        }
        j++
    }
    i = 0
    f3@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type == OUTFALL)) {
            if (((Node!![i]!!.degree + R.D2I(Node!![i]!!.inflow)) > 1)) {
                report_writeErrorMsg(ERR_OUTFALL, Node!![i]!!.ID)
            } else {
                outletCount++
            }
        }
        i++
    }
    if ((outletCount == 0)) {
        report_writeErrorMsg(ERR_NO_OUTLETS, __L0)
    }
    i = 0
    f4@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.inflow == 0.0)) {
            Node!![i]!!.degree = (-Node!![i]!!.degree)
        }
        Node!![i]!!.inflow = 0.0
        i++
    }
}

// flowrout.c:337
fun initNodeDepths() {
    var i: Int = 0
    var n: Int = 0
    var y: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        Node!![i]!!.inflow = 0.0
        Node!![i]!!.outflow = 0.0
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        if ((Link!![i]!!.newDepth > 0.0001)) {
            y = (Link!![i]!!.newDepth + Link!![i]!!.offset1)
        } else {
            y = 0.0
        }
        n = Link!![i]!!.node1
        Node!![n]!!.inflow += y
        Node!![n]!!.outflow += 1.0
        n = Link!![i]!!.node2
        Node!![n]!!.inflow += y
        Node!![n]!!.outflow += 1.0
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type == OUTFALL)) {
            i++
            continue@f3
        }
        if ((Node!![i]!!.type == STORAGE)) {
            i++
            continue@f3
        }
        if ((Node!![i]!!.initDepth > 0.0)) {
            i++
            continue@f3
        }
        if ((Node!![i]!!.outflow > 0.0)) {
            Node!![i]!!.newDepth = (Node!![i]!!.inflow / Node!![i]!!.outflow)
        }
        i++
    }
    i = 0
    f4@ while ((i < Nobjects!![LINK])) {
        link_setOutfallDepth(i)
        i++
    }
}

// flowrout.c:389
fun initLinkDepths() {
    var i: Int = 0
    var y: Double = 0.0
    var y1: Double = 0.0
    var y2: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        if ((Link!![i]!!.type == CONDUIT)) {
            if ((Link!![i]!!.q0 != 0.0)) {
                i++
                continue@f1
            }
            y1 = (Node!![Link!![i]!!.node1]!!.newDepth - Link!![i]!!.offset1)
            y1 = ((if ((((y1) >= (0.0)))) (y1) else (0.0)))
            y1 = ((if ((((y1) <= (Link!![i]!!.xsect!!.yFull)))) (y1) else (Link!![i]!!.xsect!!.yFull)))
            y2 = (Node!![Link!![i]!!.node2]!!.newDepth - Link!![i]!!.offset2)
            y2 = ((if ((((y2) >= (0.0)))) (y2) else (0.0)))
            y2 = ((if ((((y2) <= (Link!![i]!!.xsect!!.yFull)))) (y2) else (Link!![i]!!.xsect!!.yFull)))
            y = (0.5 * ((y1 + y2)))
            y = ((if ((((y) >= (0.0001)))) (y) else (0.0001)))
            Link!![i]!!.newDepth = y
        }
        i++
    }
}

// flowrout.c:425
fun initNodes() {
    var i: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        Node!![i]!!.inflow = Node!![i]!!.newLatFlow
        Node!![i]!!.outflow = 0.0
        Node!![i]!!.newVolume = 0.0
        if ((((AllowPonding != 0) && (Node!![i]!!.pondedArea > 0.0)) && (Node!![i]!!.newDepth > Node!![i]!!.fullDepth))) {
            Node!![i]!!.newVolume = (Node!![i]!!.fullVolume + (((Node!![i]!!.newDepth - Node!![i]!!.fullDepth)) * Node!![i]!!.pondedArea))
        } else {
            Node!![i]!!.newVolume = node_getVolume(i, Node!![i]!!.newDepth)
        }
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        if ((Link!![i]!!.newFlow >= 0.0)) {
            Node!![Link!![i]!!.node1]!!.outflow += Link!![i]!!.newFlow
            Node!![Link!![i]!!.node2]!!.inflow += Link!![i]!!.newFlow
        } else {
            Node!![Link!![i]!!.node1]!!.inflow -= Link!![i]!!.newFlow
            Node!![Link!![i]!!.node2]!!.outflow -= Link!![i]!!.newFlow
        }
        i++
    }
}

// flowrout.c:472
fun initLinks(routingModel: Int) {
    var i: Int = 0
    var k: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        if ((routingModel == SF)) {
            Link!![i]!!.newFlow = 0.0
        } else if ((Link!![i]!!.type == CONDUIT)) {
            k = Link!![i]!!.subIndex
            Conduit!![k]!!.q1 = (Link!![i]!!.newFlow / (Conduit!![k]!!.barrels).toDouble())
            Conduit!![k]!!.q2 = Conduit!![k]!!.q1
            Conduit!![k]!!.a1 = xsect_getAofY(Link!![i]!!.xsect, Link!![i]!!.newDepth)
            Conduit!![k]!!.a2 = Conduit!![k]!!.a1
            Link!![i]!!.newVolume = ((Conduit!![k]!!.a1 * link_getLength(i)) * (Conduit!![k]!!.barrels).toDouble())
            Link!![i]!!.oldVolume = Link!![i]!!.newVolume
        }
        i++
    }
}

// flowrout.c:511
fun getLinkInflow(j: Int, dt: Double): Double {
    var n1: Int = Link!![j]!!.node1
    var q: Double = 0.0
    if ((((Link!![j]!!.type == CONDUIT) || (Link!![j]!!.type == PUMP)) || (Node!![n1]!!.type == STORAGE))) {
        q = link_getInflow(j)
    } else {
        q = 0.0
    }
    return node_getMaxOutflow(n1, q, dt)
}

// flowrout.c:531
fun updateStorageState(i: Int, j: Int, links: IntArray?, dt: Double) {
    var iter: Int = 0
    var stopped: Int = 0
    var vFixed: Double = 0.0
    var v2: Double = 0.0
    var d1: Double = 0.0
    var d2: Double = 0.0
    if ((Node!![i]!!.type != STORAGE)) {
        return
    }
    if ((Node!![i]!!.updated != 0)) {
        return
    }
    vFixed = (Node!![i]!!.oldVolume + ((0.5 * (((Node!![i]!!.oldNetInflow + Node!![i]!!.inflow) - Node!![i]!!.outflow))) * dt))
    d1 = Node!![i]!!.newDepth
    iter = 1
    stopped = 0
    w1@ while (((iter < MAXITER) && (!(stopped != 0)))) {
        v2 = (vFixed - ((0.5 * getStorageOutflow(i, j, links, dt)) * dt))
        v2 = ((if ((((0.0) >= (v2)))) (0.0) else (v2)))
        Node!![i]!!.overflow = 0.0
        if ((v2 > Node!![i]!!.fullVolume)) {
            Node!![i]!!.overflow = (((v2 - ((if ((((Node!![i]!!.oldVolume) >= (Node!![i]!!.fullVolume)))) (Node!![i]!!.oldVolume) else (Node!![i]!!.fullVolume))))) / dt)
            if ((Node!![i]!!.overflow < 0.0001)) {
                Node!![i]!!.overflow = 0.0
            }
            if (((!(AllowPonding != 0)) || (Node!![i]!!.pondedArea == 0.0))) {
                v2 = Node!![i]!!.fullVolume
            }
        }
        Node!![i]!!.newVolume = v2
        d2 = node_getDepth(i, v2)
        Node!![i]!!.newDepth = d2
        d2 = ((((1.0 - flowrout_OMEGA)) * d1) + (flowrout_OMEGA * d2))
        if ((abs((d2 - d1)) <= STOPTOL)) {
            stopped = 1
        }
        d1 = d2
        iter++
    }
    Node!![i]!!.updated = 1
}

// flowrout.c:605
fun getStorageOutflow(i: Int, j: Int, links: IntArray?, dt: Double): Double {
    var k: Int = 0
    var m: Int = 0
    var outflow: Double = 0.0
    k = j
    f1@ while ((k < Nobjects!![LINK])) {
        m = links!![k]
        if ((Link!![m]!!.node1 != i)) {
            break@f1
        }
        outflow += (getLinkInflow(m, dt))
        k++
    }
    return outflow
}

// flowrout.c:629
fun setNewNodeState(j: Int, dt: Double) {
    var canPond: Int = 0
    var newNetInflow: Double = 0.0
    if ((Node!![j]!!.type == STORAGE)) {
        if ((Node!![j]!!.updated == 0)) {
            updateStorageState(j, Nobjects!![LINK], (null), dt)
        }
        return
    }
    newNetInflow = ((Node!![j]!!.inflow - Node!![j]!!.outflow) - Node!![j]!!.losses)
    Node!![j]!!.newVolume = (Node!![j]!!.oldVolume + (newNetInflow * dt))
    if ((Node!![j]!!.newVolume < 0.0001)) {
        Node!![j]!!.newVolume = 0.0
    }
    Node!![j]!!.overflow = 0.0
    canPond = (if ((((AllowPonding != 0) && (Node!![j]!!.pondedArea > 0.0)))) 1 else 0)
    if ((Node!![j]!!.newVolume > Node!![j]!!.fullVolume)) {
        Node!![j]!!.overflow = (((Node!![j]!!.newVolume - ((if ((((Node!![j]!!.oldVolume) >= (Node!![j]!!.fullVolume)))) (Node!![j]!!.oldVolume) else (Node!![j]!!.fullVolume))))) / dt)
        if ((Node!![j]!!.overflow < 0.0001)) {
            Node!![j]!!.overflow = 0.0
        }
        if (!(canPond != 0)) {
            Node!![j]!!.newVolume = Node!![j]!!.fullVolume
        }
    }
    Node!![j]!!.newDepth = node_getDepth(j, Node!![j]!!.newVolume)
}

// flowrout.c:673
fun setNewLinkState(j: Int) {
    var k: Int = 0
    var a: Double = 0.0
    var y1: Double = 0.0
    var y2: Double = 0.0
    Link!![j]!!.newDepth = 0.0
    Link!![j]!!.newVolume = 0.0
    if ((Link!![j]!!.type == CONDUIT)) {
        k = Link!![j]!!.subIndex
        a = (0.5 * ((Conduit!![k]!!.a1 + Conduit!![k]!!.a2)))
        Link!![j]!!.newVolume = ((a * link_getLength(j)) * (Conduit!![k]!!.barrels).toDouble())
        y1 = xsect_getYofA(Link!![j]!!.xsect, Conduit!![k]!!.a1)
        y2 = xsect_getYofA(Link!![j]!!.xsect, Conduit!![k]!!.a2)
        Link!![j]!!.newDepth = (0.5 * ((y1 + y2)))
        updateNodeDepth(Link!![j]!!.node1, (y1 + Link!![j]!!.offset1))
        updateNodeDepth(Link!![j]!!.node2, (y2 + Link!![j]!!.offset2))
        if ((Conduit!![k]!!.a1 >= Link!![j]!!.xsect!!.aFull)) {
            Conduit!![k]!!.capacityLimited = 1
            Conduit!![k]!!.fullState = ALL_FULL
        } else {
            Conduit!![k]!!.capacityLimited = 0
            Conduit!![k]!!.fullState = 0
        }
    }
}

// flowrout.c:717
fun updateNodeDepth(i: Int, __y: Double) {
    var y: Double = __y
    if ((Node!![i]!!.type == STORAGE)) {
        return
    }
    if ((((Node!![i]!!.type != OUTFALL) && (Node!![i]!!.degree > 0)) && (Node!![i]!!.overflow > 0.0))) {
        y = Node!![i]!!.fullDepth
    }
    if ((Node!![i]!!.newDepth < y)) {
        Node!![i]!!.newDepth = y
        if (((Node!![i]!!.fullDepth > 0.0) && (y > Node!![i]!!.fullDepth))) {
            Node!![i]!!.newDepth = Node!![i]!!.fullDepth
        }
    }
}

// flowrout.c:748
fun steadyflow_execute(j: Int, qin: DRef?, qout: DRef?, tStep: Double): Int {
    var k: Int = 0
    var s: Double = 0.0
    var q: Double = 0.0
    if ((Link!![j]!!.type == CONDUIT)) {
        k = Link!![j]!!.subIndex
        q = ((qin!!.get()) / (Conduit!![k]!!.barrels).toDouble())
        if ((Link!![j]!!.xsect!!.type == DUMMY)) {
            Conduit!![k]!!.a1 = 0.0
        } else {
            q -= (link_getLossRate(j, SF, q, tStep))
            if ((q > Link!![j]!!.qFull)) {
                q = Link!![j]!!.qFull
                Conduit!![k]!!.a1 = Link!![j]!!.xsect!!.aFull
                qin!!.set((q * (Conduit!![k]!!.barrels).toDouble()))
            } else {
                s = (q / Conduit!![k]!!.beta)
                Conduit!![k]!!.a1 = xsect_getAofS(Link!![j]!!.xsect, s)
            }
        }
        Conduit!![k]!!.a2 = Conduit!![k]!!.a1
        Conduit!![k]!!.q1Old = Conduit!![k]!!.q1
        Conduit!![k]!!.q2Old = Conduit!![k]!!.q2
        Conduit!![k]!!.q1 = q
        Conduit!![k]!!.q2 = q
        qout!!.set((q * (Conduit!![k]!!.barrels).toDouble()))
    } else {
        qout!!.set((qin!!.get()))
    }
    return 1
}

