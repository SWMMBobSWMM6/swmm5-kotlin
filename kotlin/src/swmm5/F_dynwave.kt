// Generated from EPA SWMM 5.2.4 (dynwave.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_dynwave")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MINTIMESTEP: Double = 0.001
@JvmField var dynwave_OMEGA: Double = 0.5
@JvmField var DEFAULT_SURFAREA: Double = 12.566
@JvmField var DEFAULT_HEADTOL: Double = 0.005
@JvmField var EXTRAN_CROWN_CUTOFF: Double = 0.96
@JvmField var SLOT_CROWN_CUTOFF: Double = 0.985257
@JvmField var DEFAULT_MAXTRIALS: Int = 8
@JvmField var VariableStep: Double = 0.0
@JvmField var Xnode: Array<TXnode?>? = null
@JvmField var Omega: Double = 0.0
@JvmField var Steps: Int = 0

// dynwave.c:117
fun dynwave_init() {
    var i: Int = 0
    var j: Int = 0
    var z: Double = 0.0
    VariableStep = 0.0
    Xnode = Array<TXnode?>(((Nobjects!![NODE]).toLong()).toInt()) { TXnode() }
    if ((Xnode == null)) {
        report_writeErrorMsg(ERR_MEMORY, __L45)
        return
    }
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        Xnode!![i]!!.newSurfArea = 0.0
        Xnode!![i]!!.oldSurfArea = 0.0
        Node!![i]!!.crownElev = Node!![i]!!.invertElev
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        j = Link!![i]!!.node1
        z = ((Node!![j]!!.invertElev + Link!![i]!!.offset1) + Link!![i]!!.xsect!!.yFull)
        Node!![j]!!.crownElev = ((if ((((Node!![j]!!.crownElev) >= (z)))) (Node!![j]!!.crownElev) else (z)))
        j = Link!![i]!!.node2
        z = ((Node!![j]!!.invertElev + Link!![i]!!.offset2) + Link!![i]!!.xsect!!.yFull)
        Node!![j]!!.crownElev = ((if ((((Node!![j]!!.crownElev) >= (z)))) (Node!![j]!!.crownElev) else (z)))
        Link!![i]!!.flowClass = DRY
        Link!![i]!!.dqdh = 0.0
        i++
    }
    if ((SurchargeMethod == SLOT)) {
        CrownCutoff = SLOT_CROWN_CUTOFF
    } else {
        CrownCutoff = EXTRAN_CROWN_CUTOFF
    }
}

// dynwave.c:165
fun dynwave_close() {
    if ((Xnode != null)) {
        R.free(Xnode)
        Xnode = (null)
    }
}

// dynwave.c:177
fun dynwave_validate() {
    if ((MinRouteStep.v > RouteStep)) {
        MinRouteStep.v = RouteStep
    }
    if ((MinRouteStep.v < MINTIMESTEP)) {
        MinRouteStep.v = MINTIMESTEP
    }
    if ((MinSurfArea.v == 0.0)) {
        MinSurfArea.v = DEFAULT_SURFAREA
    } else {
        MinSurfArea.v /= (UCF(LENGTH) * UCF(LENGTH))
    }
    if ((HeadTol.v == 0.0)) {
        HeadTol.v = DEFAULT_HEADTOL
    } else {
        HeadTol.v /= (UCF(LENGTH))
    }
    if ((MaxTrials == 0)) {
        MaxTrials = DEFAULT_MAXTRIALS
    }
}

// dynwave.c:195
fun dynwave_getRoutingStep(fixedStep: Double): Double {
    if ((CourantFactor.v == 0.0)) {
        return fixedStep
    }
    if ((fixedStep < MINTIMESTEP)) {
        return fixedStep
    }
    if ((VariableStep == 0.0)) {
        VariableStep = MinRouteStep.v
    } else {
        VariableStep = getVariableStep(fixedStep)
    }
    VariableStep = (floor((1000.0 * VariableStep)) / 1000.0)
    return VariableStep
}

// dynwave.c:224
fun dynwave_execute(tStep: Double): Int {
    var converged: Int = 0
    if ((ErrorCode != 0)) {
        return 0
    }
    Steps = 0
    converged = 0
    Omega = dynwave_OMEGA
    initRoutingStep()
    w1@ while ((Steps < MaxTrials)) {
        initNodeStates()
        findLinkFlows(tStep)
        converged = findNodeDepths(tStep)
        Steps++
        if ((Steps > 1)) {
            if ((converged != 0)) {
                break@w1
            }
            findBypassedLinks()
        }
    }
    if (!(converged != 0)) {
        updateConvergenceStats()
    }
    findLimitedLinks()
    return Steps
}

// dynwave.c:266
fun updateConvergenceStats() {
    var i: Int = 0
    NonConvergeCount++
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        stats_updateConvergenceStats(i, Xnode!![i]!!.converged)
        i++
    }
}

// dynwave.c:276
fun initRoutingStep() {
    var i: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        Xnode!![i]!!.converged = 0
        Xnode!![i]!!.dYdT = 0.0
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        Link!![i]!!.bypassed = 0
        Link!![i]!!.surfArea1 = 0.0
        Link!![i]!!.surfArea2 = 0.0
        i++
    }
    i = 0
    f3@ while ((i < Nlinks!![CONDUIT])) {
        Conduit!![i]!!.a2 = Conduit!![i]!!.a1
        i++
    }
}

// dynwave.c:297
fun initNodeStates() {
    var i: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if ((AllowPonding != 0)) {
            Xnode!![i]!!.newSurfArea = node_getPondedArea(i, Node!![i]!!.newDepth)
        } else {
            Xnode!![i]!!.newSurfArea = node_getSurfArea(i, Node!![i]!!.newDepth)
        }
        Node!![i]!!.inflow = 0.0
        Node!![i]!!.outflow = Node!![i]!!.losses
        if ((Node!![i]!!.newLatFlow >= 0.0)) {
            Node!![i]!!.inflow += Node!![i]!!.newLatFlow
        } else {
            Node!![i]!!.outflow -= Node!![i]!!.newLatFlow
        }
        Xnode!![i]!!.sumdqdh = 0.0
        i++
    }
}

// dynwave.c:335
fun findBypassedLinks() {
    var i: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        if (((Xnode!![Link!![i]!!.node1]!!.converged != 0) && (Xnode!![Link!![i]!!.node2]!!.converged != 0))) {
            Link!![i]!!.bypassed = 1
        } else {
            Link!![i]!!.bypassed = 0
        }
        i++
    }
}

// dynwave.c:349
fun findLimitedLinks() {
    var j: Int = 0
    var n1: Int = 0
    var n2: Int = 0
    var k: Int = 0
    var h1: Double = 0.0
    var h2: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        if (!(isTrueConduit(j) != 0)) {
            j++
            continue@f1
        }
        k = Link!![j]!!.subIndex
        Conduit!![k]!!.capacityLimited = 0
        if ((Conduit!![k]!!.a1 >= Link!![j]!!.xsect!!.aFull)) {
            n1 = Link!![j]!!.node1
            n2 = Link!![j]!!.node2
            h1 = (Node!![n1]!!.newDepth + Node!![n1]!!.invertElev)
            h2 = (Node!![n2]!!.newDepth + Node!![n2]!!.invertElev)
            if ((((h1 - h2)) > (abs(Conduit!![k]!!.slope) * Conduit!![k]!!.length))) {
                Conduit!![k]!!.capacityLimited = 1
            }
        }
        j++
    }
}

// dynwave.c:382
fun findLinkFlows(dt: Double) {
    var i: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        if (((isTrueConduit(i) != 0) && (!(Link!![i]!!.bypassed != 0)))) {
            dwflow_findConduitFlow(i, Steps, Omega, dt)
        }
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        if ((isTrueConduit(i) != 0)) {
            updateNodeFlows(i)
        }
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![LINK])) {
        if (!(isTrueConduit(i) != 0)) {
            if (!(Link!![i]!!.bypassed != 0)) {
                findNonConduitFlow(i, dt)
            }
            updateNodeFlows(i)
        }
        i++
    }
}

// dynwave.c:416
fun isTrueConduit(j: Int): Int {
    return (if ((((Link!![j]!!.type == CONDUIT) && (Link!![j]!!.xsect!!.type != DUMMY)))) 1 else 0)
}

// dynwave.c:423
fun findNonConduitFlow(i: Int, dt: Double) {
    var qLast: Double = 0.0
    var qNew: Double = 0.0
    qLast = Link!![i]!!.newFlow
    Link!![i]!!.dqdh = 0.0
    qNew = link_getInflow(i)
    if ((Link!![i]!!.type == PUMP)) {
        qNew = getModPumpFlow(i, qNew, dt)
    }
    findNonConduitSurfArea(i)
    if (((Steps > 0) && (Link!![i]!!.type != PUMP))) {
        qNew = ((((1.0 - Omega)) * qLast) + (Omega * qNew))
        if (((qNew * qLast) < 0.0)) {
            qNew = (0.001 * (((if ((((qNew) < 0.0))) ((-1)) else (1)))).toDouble())
        }
    }
    Link!![i]!!.newFlow = qNew
}

// dynwave.c:458
fun getModPumpFlow(i: Int, q: Double, dt: Double): Double {
    var j: Int = Link!![i]!!.node1
    var k: Int = Link!![i]!!.subIndex
    var newNetInflow: Double = 0.0
    var netFlowVolume: Double = 0.0
    var y: Double = 0.0
    if ((q == 0.0)) {
        return q
    }
    if ((Node!![j]!!.type == STORAGE)) {
        return node_getMaxOutflow(j, q, dt)
    }
    when (Pump!![k]!!.type) {
        TYPE1_PUMP -> {
            return node_getMaxOutflow(j, q, dt)
        }
        TYPE2_PUMP, TYPE4_PUMP, TYPE3_PUMP -> {
            newNetInflow = ((Node!![j]!!.inflow - Node!![j]!!.outflow) - q)
            netFlowVolume = ((0.5 * ((Node!![j]!!.oldNetInflow + newNetInflow))) * dt)
            y = (Node!![j]!!.oldDepth + (netFlowVolume / Xnode!![j]!!.newSurfArea))
            if ((y <= 0.0)) {
                return Node!![j]!!.inflow
            }
        }
    }
    return q
}

// dynwave.c:503
fun findNonConduitSurfArea(i: Int) {
    if ((Link!![i]!!.type == ORIFICE)) {
        Link!![i]!!.surfArea1 = (Orifice!![Link!![i]!!.subIndex]!!.surfArea / 2.0)
    } else {
        Link!![i]!!.surfArea1 = 0.0
    }
    Link!![i]!!.surfArea2 = Link!![i]!!.surfArea1
    if (((Link!![i]!!.flowClass == UP_CRITICAL) || (Node!![Link!![i]!!.node1]!!.type == STORAGE))) {
        Link!![i]!!.surfArea1 = 0.0
    }
    if (((Link!![i]!!.flowClass == DN_CRITICAL) || (Node!![Link!![i]!!.node2]!!.type == STORAGE))) {
        Link!![i]!!.surfArea2 = 0.0
    }
}

// dynwave.c:528
fun updateNodeFlows(i: Int) {
    var k: Int = 0
    var barrels: Int = 1
    var n1: Int = Link!![i]!!.node1
    var n2: Int = Link!![i]!!.node2
    var q: Double = Link!![i]!!.newFlow
    var conduitLossRate: Double = 0.0
    if ((q >= 0.0)) {
        Node!![n1]!!.outflow += q
        Node!![n2]!!.inflow += q
    } else {
        Node!![n1]!!.inflow -= q
        Node!![n2]!!.outflow -= q
    }
    if ((Link!![i]!!.type == CONDUIT)) {
        k = Link!![i]!!.subIndex
        barrels = Conduit!![k]!!.barrels
        conduitLossRate = (((Conduit!![k]!!.evapLossRate + Conduit!![k]!!.seepLossRate)) * (barrels).toDouble())
        if ((conduitLossRate > 0.0)) {
            if (((Node!![n1]!!.type != OUTFALL) && (Node!![n2]!!.type != OUTFALL))) {
                conduitLossRate /= 2.0
            }
            if ((Node!![n1]!!.type != OUTFALL)) {
                Node!![n1]!!.outflow += conduitLossRate
            }
            if ((Node!![n2]!!.type != OUTFALL)) {
                Node!![n2]!!.outflow += conduitLossRate
            }
        }
    }
    Xnode!![Link!![i]!!.node1]!!.newSurfArea += (Link!![i]!!.surfArea1 * (barrels).toDouble())
    Xnode!![Link!![i]!!.node2]!!.newSurfArea += (Link!![i]!!.surfArea2 * (barrels).toDouble())
    Xnode!![Link!![i]!!.node1]!!.sumdqdh += Link!![i]!!.dqdh
    if ((Link!![i]!!.type == PUMP)) {
        k = Link!![i]!!.subIndex
        if ((Pump!![k]!!.type != TYPE4_PUMP)) {
            Xnode!![n2]!!.sumdqdh += Link!![i]!!.dqdh
        }
    } else {
        Xnode!![n2]!!.sumdqdh += Link!![i]!!.dqdh
    }
}

// dynwave.c:593
fun findNodeDepths(dt: Double): Int {
    var i: Int = 0
    var yOld: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        link_setOutfallDepth(i)
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type == OUTFALL)) {
            i++
            continue@f2
        }
        yOld = Node!![i]!!.newDepth
        setNodeDepth(i, dt)
        Xnode!![i]!!.converged = 1
        if ((abs((yOld - Node!![i]!!.newDepth)) > HeadTol.v)) {
            Xnode!![i]!!.converged = 0
        }
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type == OUTFALL)) {
            i++
            continue@f3
        }
        if ((Xnode!![i]!!.converged == 0)) {
            return 0
        }
        i++
    }
    return 1
}

// dynwave.c:636
fun setNodeDepth(i: Int, dt: Double) {
    var canPond: Int = 0
    var isPonded: Int = 0
    var isSurcharged: Int = 0
    var dQ: Double = 0.0
    var dV: Double = 0.0
    var dy: Double = 0.0
    var yMax: Double = 0.0
    var yOld: Double = 0.0
    var yLast: Double = 0.0
    var yNew: Double = 0.0
    var yCrown: Double = 0.0
    var surfArea: Double = 0.0
    var denom: Double = 0.0
    var corr: Double = 0.0
    var f: Double = 0.0
    canPond = (if ((((AllowPonding != 0) && (Node!![i]!!.pondedArea > 0.0)))) 1 else 0)
    isPonded = (if ((((canPond != 0) && (Node!![i]!!.newDepth > Node!![i]!!.fullDepth)))) 1 else 0)
    yCrown = (Node!![i]!!.crownElev - Node!![i]!!.invertElev)
    yOld = Node!![i]!!.oldDepth
    yLast = Node!![i]!!.newDepth
    Node!![i]!!.overflow = 0.0
    surfArea = Xnode!![i]!!.newSurfArea
    surfArea = ((if ((((surfArea) >= (MinSurfArea.v)))) (surfArea) else (MinSurfArea.v)))
    dQ = (Node!![i]!!.inflow - Node!![i]!!.outflow)
    dV = ((0.5 * ((Node!![i]!!.oldNetInflow + dQ))) * dt)
    if ((SurchargeMethod == EXTRAN)) {
        if ((isPonded != 0)) {
            isSurcharged = 0
        } else if ((Node!![i]!!.type == STORAGE)) {
            isSurcharged = (if ((((Node!![i]!!.surDepth > 0.0) && (yLast > Node!![i]!!.fullDepth)))) 1 else 0)
        } else {
            isSurcharged = (if ((((yCrown > 0.0) && (yLast > yCrown)))) 1 else 0)
        }
    }
    if (!(isSurcharged != 0)) {
        dy = (dV / surfArea)
        yNew = (yOld + dy)
        if (!(isPonded != 0)) {
            Xnode!![i]!!.oldSurfArea = surfArea
        }
        if ((Steps > 0)) {
            yNew = ((((1.0 - Omega)) * yLast) + (Omega * yNew))
        }
        if (((isPonded != 0) && (yNew < Node!![i]!!.fullDepth))) {
            yNew = (Node!![i]!!.fullDepth - 0.0001)
        }
    } else {
        corr = 1.0
        if ((Node!![i]!!.degree < 0)) {
            corr = 0.6
        }
        denom = Xnode!![i]!!.sumdqdh
        if ((yLast < (1.25 * yCrown))) {
            f = (((yLast - yCrown)) / yCrown)
            denom += ((((Xnode!![i]!!.oldSurfArea / dt) - Xnode!![i]!!.sumdqdh)) * Glibm.exp(((-15.0) * f)))
        }
        if ((denom == 0.0)) {
            dy = 0.0
        } else {
            dy = ((corr * dQ) / denom)
        }
        yNew = (yLast + dy)
        if ((yNew < yCrown)) {
            yNew = (yCrown - 0.0001)
        }
        if (((canPond != 0) && (yNew > Node!![i]!!.fullDepth))) {
            yNew = (Node!![i]!!.fullDepth + 0.0001)
        }
    }
    if ((yNew < 0.0)) {
        yNew = 0.0
    }
    yMax = Node!![i]!!.fullDepth
    if ((canPond == 0)) {
        yMax += Node!![i]!!.surDepth
    }
    if ((yNew > yMax)) {
        yNew = getFloodedDepth(i, canPond, dV, yNew, yMax, dt)
    } else {
        Node!![i]!!.newVolume = node_getVolume(i, yNew)
    }
    Xnode!![i]!!.dYdT = (abs((yNew - yOld)) / dt)
    Node!![i]!!.newDepth = yNew
}

// dynwave.c:766
fun getFloodedDepth(i: Int, canPond: Int, dV: Double, __yNew: Double, yMax: Double, dt: Double): Double {
    var yNew: Double = __yNew
    if ((canPond == 0)) {
        Node!![i]!!.overflow = (dV / dt)
        Node!![i]!!.newVolume = Node!![i]!!.fullVolume
        yNew = yMax
    } else {
        Node!![i]!!.newVolume = ((if ((((((Node!![i]!!.oldVolume + dV))) >= (Node!![i]!!.fullVolume)))) (((Node!![i]!!.oldVolume + dV))) else (Node!![i]!!.fullVolume)))
        Node!![i]!!.overflow = (((Node!![i]!!.newVolume - ((if ((((Node!![i]!!.oldVolume) >= (Node!![i]!!.fullVolume)))) (Node!![i]!!.oldVolume) else (Node!![i]!!.fullVolume))))) / dt)
    }
    if ((Node!![i]!!.overflow < 0.0001)) {
        Node!![i]!!.overflow = 0.0
    }
    return yNew
}

// dynwave.c:799
fun getVariableStep(maxStep: Double): Double {
    val minLink = IBox((-1))
    val minNode = IBox((-1))
    var tMin: Double = 0.0
    var tMinLink: Double = 0.0
    var tMinNode: Double = 0.0
    tMin = maxStep
    tMinLink = getLinkStep(tMin, minLink)
    tMinNode = getNodeStep(tMinLink, minNode)
    tMin = tMinLink
    if ((tMinNode < tMin)) {
        tMin = tMinNode
        minLink.v = (-1)
    }
    stats_updateCriticalTimeCount(minNode.v, minLink.v)
    if ((tMin < MinRouteStep.v)) {
        tMin = MinRouteStep.v
    }
    return tMin
}

// dynwave.c:836
fun getLinkStep(tMin: Double, minLink: IRef?): Double {
    var i: Int = 0
    var k: Int = 0
    var q: Double = 0.0
    var t: Double = 0.0
    var tLink: Double = tMin
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        if ((Link!![i]!!.type == CONDUIT)) {
            k = Link!![i]!!.subIndex
            q = (abs(Link!![i]!!.newFlow) / (Conduit!![k]!!.barrels).toDouble())
            if ((((q <= 0.0001) || (Conduit!![k]!!.a1 <= 0.0001)) || (Link!![i]!!.froude <= 0.01))) {
                i++
                continue@f1
            }
            t = ((Link!![i]!!.newVolume / (Conduit!![k]!!.barrels).toDouble()) / q)
            t = ((t * Conduit!![k]!!.modLength) / link_getLength(i))
            t = (((t * Link!![i]!!.froude) / ((1.0 + Link!![i]!!.froude))) * CourantFactor.v)
            if ((t < tLink)) {
                tLink = t
                minLink!!.set(i)
            }
        }
        i++
    }
    return tLink
}

// dynwave.c:881
fun getNodeStep(tMin: Double, minNode: IRef?): Double {
    var i: Int = 0
    var maxDepth: Double = 0.0
    var dYdT: Double = 0.0
    var t1: Double = 0.0
    var tNode: Double = tMin
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type == OUTFALL)) {
            i++
            continue@f1
        }
        if ((Node!![i]!!.newDepth <= 0.0001)) {
            i++
            continue@f1
        }
        if (((Node!![i]!!.newDepth + 0.0001) >= (Node!![i]!!.crownElev - Node!![i]!!.invertElev))) {
            i++
            continue@f1
        }
        maxDepth = (((Node!![i]!!.crownElev - Node!![i]!!.invertElev)) * 0.25)
        if ((maxDepth < 0.0001)) {
            i++
            continue@f1
        }
        dYdT = Xnode!![i]!!.dYdT
        if ((dYdT < 0.0001)) {
            i++
            continue@f1
        }
        t1 = (maxDepth / dYdT)
        if ((t1 < tNode)) {
            tNode = t1
            minNode!!.set(i)
        }
        i++
    }
    return tNode
}

