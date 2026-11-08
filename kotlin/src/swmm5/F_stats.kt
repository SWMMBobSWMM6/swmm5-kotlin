// Generated from EPA SWMM 5.2.4 (stats.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_stats")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var TimeStepStats: TTimeStepStats? = TTimeStepStats()
@JvmField var MaxMassBalErrs: Array<TMaxStats?>? = Array<TMaxStats?>(5) { TMaxStats() }
@JvmField var MaxCourantCrit: Array<TMaxStats?>? = Array<TMaxStats?>(5) { TMaxStats() }
@JvmField var MaxFlowTurns: Array<TMaxStats?>? = Array<TMaxStats?>(5) { TMaxStats() }
@JvmField var MaxNonConverged: Array<TMaxStats?>? = Array<TMaxStats?>(5) { TMaxStats() }
@JvmField var SysOutfallFlow: Double = 0.0
@JvmField var SubcatchStats: Array<TSubcatchStats?>? = null
@JvmField var NodeStats: Array<TNodeStats?>? = null
@JvmField var LinkStats: Array<TLinkStats?>? = null
@JvmField var StorageStats: Array<TStorageStats?>? = null
@JvmField var OutfallStats: Array<TOutfallStats?>? = null
@JvmField var PumpStats: Array<TPumpStats?>? = null
@JvmField var MaxOutfallFlow: Double = 0.0
@JvmField var MaxRunoffFlow: Double = 0.0
@JvmField var RoutingTimeSpan: Double = 0.0

// stats.c:102
fun stats_open(): Int {
    var j: Int = 0
    var k: Int = 0
    var timeStepDelta: Double = 0.0
    var logMaxTimeStep: Double = 0.0
    var logMinTimeStep: Double = 0.0
    NodeStats = (null)
    LinkStats = (null)
    StorageStats = (null)
    OutfallStats = (null)
    PumpStats = (null)
    SubcatchStats = (null)
    if ((Nobjects!![SUBCATCH] > 0)) {
        SubcatchStats = Array<TSubcatchStats?>(((Nobjects!![SUBCATCH]).toLong()).toInt()) { TSubcatchStats() }
        if (!(SubcatchStats != null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
        j = 0
        f1@ while ((j < Nobjects!![SUBCATCH])) {
            SubcatchStats!![j]!!.precip = 0.0
            SubcatchStats!![j]!!.runon = 0.0
            SubcatchStats!![j]!!.evap = 0.0
            SubcatchStats!![j]!!.infil = 0.0
            SubcatchStats!![j]!!.runoff = 0.0
            SubcatchStats!![j]!!.maxFlow = 0.0
            SubcatchStats!![j]!!.impervRunoff = 0.0
            SubcatchStats!![j]!!.pervRunoff = 0.0
            j++
        }
        j = 0
        f2@ while ((j < Nobjects!![SUBCATCH])) {
            if ((Subcatch!![j]!!.groundwater == null)) {
                j++
                continue@f2
            }
            Subcatch!![j]!!.groundwater!!.stats!!.avgUpperMoist = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.avgWaterTable = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.infil = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.latFlow = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.deepFlow = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.evap = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.maxFlow = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.finalUpperMoist = 0.0
            Subcatch!![j]!!.groundwater!!.stats!!.finalWaterTable = 0.0
            j++
        }
    }
    if ((Nobjects!![LINK] > 0)) {
        NodeStats = Array<TNodeStats?>(((Nobjects!![NODE]).toLong()).toInt()) { TNodeStats() }
        LinkStats = Array<TLinkStats?>(((Nobjects!![LINK]).toLong()).toInt()) { TLinkStats() }
        if (((!(NodeStats != null)) || (!(LinkStats != null)))) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
    }
    if ((NodeStats != null)) {
        j = 0
        f3@ while ((j < Nobjects!![NODE])) {
            NodeStats!![j]!!.avgDepth = 0.0
            NodeStats!![j]!!.maxDepth = 0.0
            NodeStats!![j]!!.maxDepthDate = StartDateTime
            NodeStats!![j]!!.maxRptDepth = 0.0
            NodeStats!![j]!!.volFlooded = 0.0
            NodeStats!![j]!!.timeFlooded = 0.0
            NodeStats!![j]!!.timeSurcharged = 0.0
            NodeStats!![j]!!.timeCourantCritical = 0.0
            NodeStats!![j]!!.totLatFlow = 0.0
            NodeStats!![j]!!.maxLatFlow = 0.0
            NodeStats!![j]!!.maxInflow = 0.0
            NodeStats!![j]!!.maxOverflow = 0.0
            NodeStats!![j]!!.maxPondedVol = 0.0
            NodeStats!![j]!!.nonConvergedCount = 0
            NodeStats!![j]!!.maxInflowDate = StartDateTime
            NodeStats!![j]!!.maxOverflowDate = StartDateTime
            j++
        }
    }
    if ((LinkStats != null)) {
        j = 0
        f4@ while ((j < Nobjects!![LINK])) {
            LinkStats!![j]!!.maxFlow = 0.0
            LinkStats!![j]!!.maxVeloc = 0.0
            LinkStats!![j]!!.maxDepth = 0.0
            LinkStats!![j]!!.maxStreetFilled = 0.0
            LinkStats!![j]!!.timeSurcharged = 0.0
            LinkStats!![j]!!.timeFullUpstream = 0.0
            LinkStats!![j]!!.timeFullDnstream = 0.0
            LinkStats!![j]!!.timeFullFlow = 0.0
            LinkStats!![j]!!.timeCapacityLimited = 0.0
            LinkStats!![j]!!.timeCourantCritical = 0.0
            k = 0
            f5@ while ((k < MAX_FLOW_CLASSES)) {
                LinkStats!![j]!!.timeInFlowClass!![k] = 0.0
                k++
            }
            LinkStats!![j]!!.flowTurns = 0L
            LinkStats!![j]!!.flowTurnSign = 0
            j++
        }
    }
    if ((Nnodes!![STORAGE] > 0)) {
        StorageStats = Array<TStorageStats?>(((Nnodes!![STORAGE]).toLong()).toInt()) { TStorageStats() }
        if (!(StorageStats != null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        } else {
            k = 0
            f6@ while ((k < Nobjects!![NODE])) {
                if ((Node!![k]!!.type != STORAGE)) {
                    k++
                    continue@f6
                }
                j = Node!![k]!!.subIndex
                StorageStats!![j]!!.initVol = Node!![k]!!.newVolume
                StorageStats!![j]!!.avgVol = 0.0
                StorageStats!![j]!!.maxVol = 0.0
                StorageStats!![j]!!.maxFlow = 0.0
                StorageStats!![j]!!.evapLosses = 0.0
                StorageStats!![j]!!.exfilLosses = 0.0
                StorageStats!![j]!!.maxVolDate = StartDateTime
                k++
            }
        }
    }
    if ((Nnodes!![OUTFALL] > 0)) {
        OutfallStats = Array<TOutfallStats?>(((Nnodes!![OUTFALL]).toLong()).toInt()) { TOutfallStats() }
        if (!(OutfallStats != null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        } else {
            j = 0
            f7@ while ((j < Nnodes!![OUTFALL])) {
                OutfallStats!![j]!!.avgFlow = 0.0
                OutfallStats!![j]!!.maxFlow = 0.0
                OutfallStats!![j]!!.totalPeriods = 0
                if ((Nobjects!![POLLUT] > 0)) {
                    OutfallStats!![j]!!.totalLoad = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
                    if (!(OutfallStats!![j]!!.totalLoad != null)) {
                        report_writeErrorMsg(ERR_MEMORY, __L0)
                        return ErrorCode
                    }
                    k = 0
                    f8@ while ((k < Nobjects!![POLLUT])) {
                        OutfallStats!![j]!!.totalLoad!![k] = 0.0
                        k++
                    }
                } else {
                    OutfallStats!![j]!!.totalLoad = (null)
                }
                j++
            }
        }
    }
    if ((Nlinks!![PUMP] > 0)) {
        PumpStats = Array<TPumpStats?>(((Nlinks!![PUMP]).toLong()).toInt()) { TPumpStats() }
        if (!(PumpStats != null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        } else {
            j = 0
            f9@ while ((j < Nlinks!![PUMP])) {
                PumpStats!![j]!!.utilized = 0.0
                PumpStats!![j]!!.minFlow = 0.0
                PumpStats!![j]!!.avgFlow = 0.0
                PumpStats!![j]!!.maxFlow = 0.0
                PumpStats!![j]!!.volume = 0.0
                PumpStats!![j]!!.energy = 0.0
                PumpStats!![j]!!.startUps = 0
                PumpStats!![j]!!.offCurveLow = 0.0
                PumpStats!![j]!!.offCurveHigh = 0.0
                j++
            }
        }
    }
    MaxRunoffFlow = 0.0
    MaxOutfallFlow = 0.0
    TimeStepStats!!.maxTimeStep = 0.0
    TimeStepStats!!.minTimeStep = RouteStep
    TimeStepStats!!.routingTime = 0.0
    TimeStepStats!!.trialsCount = 0.0
    TimeStepStats!!.steadyStateTime = 0.0
    TimeStepStats!!.timeStepCount = 0
    logMaxTimeStep = Glibm.log10(RouteStep)
    logMinTimeStep = Glibm.log10(MinRouteStep.v)
    timeStepDelta = (((logMaxTimeStep - logMinTimeStep)) / (((6 - 1))).toDouble())
    TimeStepStats!!.timeStepIntervals!![0] = RouteStep
    j = 1
    f10@ while ((j < 6)) {
        TimeStepStats!!.timeStepIntervals!![j] = Glibm.pow(10.0, (logMaxTimeStep - ((j).toDouble() * timeStepDelta)))
        TimeStepStats!!.timeStepCounts!![j] = 0
        j++
    }
    TimeStepStats!!.timeStepIntervals!![(6 - 1)] = MinRouteStep.v
    RoutingTimeSpan = 0.0
    return 0
}

// stats.c:318
fun stats_close() {
    var j: Int = 0
    if ((SubcatchStats != null)) {
        R.free(SubcatchStats)
        SubcatchStats = (null)
    }
    if ((NodeStats != null)) {
        R.free(NodeStats)
        NodeStats = (null)
    }
    if ((LinkStats != null)) {
        R.free(LinkStats)
        LinkStats = (null)
    }
    if ((StorageStats != null)) {
        R.free(StorageStats)
        StorageStats = (null)
    }
    if ((OutfallStats != null)) {
        j = 0
        f1@ while ((j < Nnodes!![OUTFALL])) {
            if ((OutfallStats!![j]!!.totalLoad != null)) {
                R.free(OutfallStats!![j]!!.totalLoad)
                OutfallStats!![j]!!.totalLoad = (null)
            }
            j++
        }
        if ((OutfallStats != null)) {
            R.free(OutfallStats)
            OutfallStats = (null)
        }
    }
    if ((PumpStats != null)) {
        R.free(PumpStats)
        PumpStats = (null)
    }
}

// stats.c:342
fun stats_report() {
    if (((Nobjects!![LINK] > 0) && (RouteModel != NO_ROUTING))) {
        stats_findMaxStats()
        if (((!(RptFlags!!.disabled != 0)) && (RptFlags!!.flowStats != 0))) {
            report_writeMaxStats(MaxMassBalErrs, MaxCourantCrit, 5)
            report_writeMaxFlowTurns(MaxFlowTurns, 5)
            report_writeNonconvergedStats(MaxNonConverged, 5)
            report_writeTimeStepStats(TimeStepStats)
        }
    }
    if (!(RptFlags!!.disabled != 0)) {
        statsrpt_writeReport()
    }
}

// stats.c:369
fun stats_updateSubcatchStats(j: Int, rainVol: Double, runonVol: Double, evapVol: Double, infilVol: Double, impervVol: Double, pervVol: Double, runoffVol: Double, runoff: Double) {
    SubcatchStats!![j]!!.precip += rainVol
    SubcatchStats!![j]!!.runon += runonVol
    SubcatchStats!![j]!!.evap += evapVol
    SubcatchStats!![j]!!.infil += infilVol
    SubcatchStats!![j]!!.runoff += runoffVol
    SubcatchStats!![j]!!.maxFlow = ((if ((((SubcatchStats!![j]!!.maxFlow) >= (runoff)))) (SubcatchStats!![j]!!.maxFlow) else (runoff)))
    SubcatchStats!![j]!!.impervRunoff += impervVol
    SubcatchStats!![j]!!.pervRunoff += pervVol
}

// stats.c:399
fun stats_updateGwaterStats(j: Int, infil: Double, evap: Double, latFlow: Double, deepFlow: Double, theta: Double, waterTable: Double, tStep: Double) {
    Subcatch!![j]!!.groundwater!!.stats!!.infil += (infil * tStep)
    Subcatch!![j]!!.groundwater!!.stats!!.evap += (evap * tStep)
    Subcatch!![j]!!.groundwater!!.stats!!.latFlow += (latFlow * tStep)
    Subcatch!![j]!!.groundwater!!.stats!!.deepFlow += (deepFlow * tStep)
    Subcatch!![j]!!.groundwater!!.stats!!.avgUpperMoist += (theta * tStep)
    Subcatch!![j]!!.groundwater!!.stats!!.avgWaterTable += (waterTable * tStep)
    Subcatch!![j]!!.groundwater!!.stats!!.finalUpperMoist = theta
    Subcatch!![j]!!.groundwater!!.stats!!.finalWaterTable = waterTable
    if ((abs(latFlow) > abs(Subcatch!![j]!!.groundwater!!.stats!!.maxFlow))) {
        Subcatch!![j]!!.groundwater!!.stats!!.maxFlow = latFlow
    }
}

// stats.c:419
fun stats_updateMaxRunoff() {
    var j: Int = 0
    var sysRunoff: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        sysRunoff += Subcatch!![j]!!.newRunoff
        j++
    }
    MaxRunoffFlow = ((if ((((MaxRunoffFlow) >= (sysRunoff)))) (MaxRunoffFlow) else (sysRunoff)))
}

// stats.c:435
fun stats_updateMaxNodeDepth(j: Int, depth: Double) {
    if ((NodeStats != null)) {
        NodeStats!![j]!!.maxRptDepth = ((if ((((NodeStats!![j]!!.maxRptDepth) >= (depth)))) (NodeStats!![j]!!.maxRptDepth) else (depth)))
    }
}

// stats.c:449
fun stats_updateFlowStats(tStep: Double, aDate: Double) {
    var j: Int = 0
    if ((aDate < ReportStart)) {
        return
    }
    SysOutfallFlow = 0.0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        stats_updateNodeStats(j, tStep, aDate)
        j++
    }
    j = 0
    f2@ while ((j < Nobjects!![LINK])) {
        stats_updateLinkStats(j, tStep, aDate)
        j++
    }
    ReportStepCount++
    RoutingTimeSpan += tStep
    MaxOutfallFlow = ((if ((((MaxOutfallFlow) >= (SysOutfallFlow)))) (MaxOutfallFlow) else (SysOutfallFlow)))
}

// stats.c:484
fun stats_updateTimeStepStats(tStep: Double, trialsCount: Int, steadyState: Int) {
    var j: Int = 0
    TimeStepStats!!.steadyStateTime += ((steadyState).toDouble() * tStep)
    if ((steadyState == 0)) {
        if ((OldRoutingTime > 0.0)) {
            TimeStepStats!!.minTimeStep = ((if ((((TimeStepStats!!.minTimeStep) <= (tStep)))) (TimeStepStats!!.minTimeStep) else (tStep)))
            j = 1
            f1@ while ((j < 6)) {
                if ((tStep >= TimeStepStats!!.timeStepIntervals!![j])) {
                    TimeStepStats!!.timeStepCounts!![j]++
                    break@f1
                }
                j++
            }
        }
        TimeStepStats!!.maxTimeStep = ((if ((((TimeStepStats!!.maxTimeStep) >= (tStep)))) (TimeStepStats!!.maxTimeStep) else (tStep)))
        TimeStepStats!!.routingTime += tStep
        TimeStepStats!!.timeStepCount++
        TimeStepStats!!.trialsCount += ((trialsCount).toDouble())
    }
}

// stats.c:522
fun stats_updateCriticalTimeCount(node: Int, link: Int) {
    if ((node >= 0)) {
        NodeStats!![node]!!.timeCourantCritical += 1.0
    } else if ((link >= 0)) {
        LinkStats!![link]!!.timeCourantCritical += 1.0
    }
}

// stats.c:536
fun stats_updateConvergenceStats(node: Int, converged: Int) {
    if ((converged == 0)) {
        NodeStats!![node]!!.nonConvergedCount++
    }
}

// stats.c:543
fun stats_updateNodeStats(j: Int, tStep: Double, aDate: Double) {
    var k: Int = 0
    var p: Int = 0
    var newVolume: Double = Node!![j]!!.newVolume
    var newDepth: Double = Node!![j]!!.newDepth
    var canPond: Int = (if ((((AllowPonding != 0) && (Node!![j]!!.pondedArea > 0.0)))) 1 else 0)
    NodeStats!![j]!!.avgDepth += newDepth
    if ((newDepth > NodeStats!![j]!!.maxDepth)) {
        NodeStats!![j]!!.maxDepth = newDepth
        NodeStats!![j]!!.maxDepthDate = aDate
    }
    if ((Node!![j]!!.type != OUTFALL)) {
        if (((newVolume > Node!![j]!!.fullVolume) || (Node!![j]!!.overflow > 0.0))) {
            NodeStats!![j]!!.timeFlooded += tStep
            NodeStats!![j]!!.volFlooded += (Node!![j]!!.overflow * tStep)
            if ((canPond != 0)) {
                NodeStats!![j]!!.maxPondedVol = ((if ((((NodeStats!![j]!!.maxPondedVol) >= (((newVolume - Node!![j]!!.fullVolume)))))) (NodeStats!![j]!!.maxPondedVol) else (((newVolume - Node!![j]!!.fullVolume)))))
            }
        }
        if ((RouteModel == DW)) {
            if (((((Node!![j]!!.type != STORAGE) || (Node!![j]!!.surDepth > 0.0))) && (((newDepth + Node!![j]!!.invertElev) + 0.0001) >= Node!![j]!!.crownElev))) {
                NodeStats!![j]!!.timeSurcharged += tStep
            }
        }
    }
    if ((Node!![j]!!.type == STORAGE)) {
        k = Node!![j]!!.subIndex
        StorageStats!![k]!!.avgVol += newVolume
        StorageStats!![k]!!.evapLosses += Storage!![Node!![j]!!.subIndex]!!.evapLoss
        StorageStats!![k]!!.exfilLosses += Storage!![Node!![j]!!.subIndex]!!.exfilLoss
        newVolume = ((if ((((newVolume) <= (Node!![j]!!.fullVolume)))) (newVolume) else (Node!![j]!!.fullVolume)))
        if ((newVolume > StorageStats!![k]!!.maxVol)) {
            StorageStats!![k]!!.maxVol = newVolume
            StorageStats!![k]!!.maxVolDate = aDate
        }
        StorageStats!![k]!!.maxFlow = ((if ((((StorageStats!![k]!!.maxFlow) >= (Node!![j]!!.outflow)))) (StorageStats!![k]!!.maxFlow) else (Node!![j]!!.outflow)))
    }
    if ((Node!![j]!!.type == OUTFALL)) {
        k = Node!![j]!!.subIndex
        if ((Node!![j]!!.inflow >= 0.001)) {
            OutfallStats!![k]!!.avgFlow += Node!![j]!!.inflow
            OutfallStats!![k]!!.maxFlow = ((if ((((OutfallStats!![k]!!.maxFlow) >= (Node!![j]!!.inflow)))) (OutfallStats!![k]!!.maxFlow) else (Node!![j]!!.inflow)))
            OutfallStats!![k]!!.totalPeriods++
        }
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            OutfallStats!![k]!!.totalLoad!![p] += ((Node!![j]!!.inflow * Node!![j]!!.newQual!![p]) * tStep)
            p++
        }
        SysOutfallFlow += Node!![j]!!.inflow
    }
    NodeStats!![j]!!.totLatFlow += (((((Node!![j]!!.oldLatFlow + Node!![j]!!.newLatFlow)) * 0.5) * tStep))
    if ((abs(Node!![j]!!.newLatFlow) > abs(NodeStats!![j]!!.maxLatFlow))) {
        NodeStats!![j]!!.maxLatFlow = Node!![j]!!.newLatFlow
    }
    if ((Node!![j]!!.inflow > NodeStats!![j]!!.maxInflow)) {
        NodeStats!![j]!!.maxInflow = Node!![j]!!.inflow
        NodeStats!![j]!!.maxInflowDate = aDate
    }
    if ((Node!![j]!!.overflow > NodeStats!![j]!!.maxOverflow)) {
        NodeStats!![j]!!.maxOverflow = Node!![j]!!.overflow
        NodeStats!![j]!!.maxOverflowDate = aDate
    }
}

// stats.c:647
fun stats_updateLinkStats(j: Int, tStep: Double, aDate: Double) {
    var k: Int = 0
    var q: Double = 0.0
    var v: Double = 0.0
    var dq: Double = 0.0
    dq = (Link!![j]!!.newFlow - Link!![j]!!.oldFlow)
    q = abs(Link!![j]!!.newFlow)
    if ((q > LinkStats!![j]!!.maxFlow)) {
        LinkStats!![j]!!.maxFlow = q
        LinkStats!![j]!!.maxFlowDate = aDate
    }
    v = link_getVelocity(j, q, Link!![j]!!.newDepth)
    if ((v > LinkStats!![j]!!.maxVeloc)) {
        LinkStats!![j]!!.maxVeloc = v
    }
    if ((Link!![j]!!.newDepth > LinkStats!![j]!!.maxDepth)) {
        LinkStats!![j]!!.maxDepth = Link!![j]!!.newDepth
    }
    if ((Link!![j]!!.type == PUMP)) {
        if ((q >= Link!![j]!!.qFull)) {
            LinkStats!![j]!!.timeFullFlow += tStep
        }
        if ((q > 0.001)) {
            k = Link!![j]!!.subIndex
            PumpStats!![k]!!.minFlow = ((if ((((PumpStats!![k]!!.minFlow) <= (q)))) (PumpStats!![k]!!.minFlow) else (q)))
            PumpStats!![k]!!.maxFlow = LinkStats!![j]!!.maxFlow
            PumpStats!![k]!!.avgFlow += q
            PumpStats!![k]!!.volume += (q * tStep)
            PumpStats!![k]!!.utilized += tStep
            PumpStats!![k]!!.energy += ((link_getPower(j) * tStep) / 3600.0)
            if ((Link!![j]!!.flowClass == DN_DRY)) {
                PumpStats!![k]!!.offCurveLow += tStep
            }
            if ((Link!![j]!!.flowClass == UP_DRY)) {
                PumpStats!![k]!!.offCurveHigh += tStep
            }
            if ((Link!![j]!!.oldFlow < 0.001)) {
                PumpStats!![k]!!.startUps++
            }
            PumpStats!![k]!!.totalPeriods++
            LinkStats!![j]!!.timeSurcharged += tStep
            LinkStats!![j]!!.timeFullUpstream += tStep
            LinkStats!![j]!!.timeFullDnstream += tStep
        }
    } else if ((Link!![j]!!.type == CONDUIT)) {
        if ((Link!![j]!!.normalFlow != 0)) {
            LinkStats!![j]!!.timeNormalFlow += tStep
        }
        if ((Link!![j]!!.inletControl != 0)) {
            LinkStats!![j]!!.timeInletControl += tStep
        }
        k = Link!![j]!!.flowClass
        if (((k >= 0) && (k < MAX_FLOW_CLASSES))) {
            LinkStats!![j]!!.timeInFlowClass!![k] += tStep
        }
        k = Link!![j]!!.subIndex
        if ((q >= (Link!![j]!!.qFull * (Conduit!![k]!!.barrels).toDouble()))) {
            LinkStats!![j]!!.timeFullFlow += tStep
        }
        if ((Conduit!![k]!!.capacityLimited != 0)) {
            LinkStats!![j]!!.timeCapacityLimited += tStep
        }
        when (Conduit!![k]!!.fullState) {
            ALL_FULL -> {
                LinkStats!![j]!!.timeSurcharged += tStep
                LinkStats!![j]!!.timeFullUpstream += tStep
                LinkStats!![j]!!.timeFullDnstream += tStep
            }
            UP_FULL -> {
                LinkStats!![j]!!.timeFullUpstream += tStep
            }
            DN_FULL -> {
                LinkStats!![j]!!.timeFullDnstream += tStep
            }
        }
        if ((Link!![j]!!.xsect!!.type == STREET_XSECT)) {
            LinkStats!![j]!!.maxStreetFilled = ((if ((((LinkStats!![j]!!.maxStreetFilled) >= (street_getExtentFilled(j))))) (LinkStats!![j]!!.maxStreetFilled) else (street_getExtentFilled(j))))
        }
    }
    k = LinkStats!![j]!!.flowTurnSign
    LinkStats!![j]!!.flowTurnSign = ((if ((((dq) < 0.0))) ((-1)) else (1)))
    if (((abs(dq) > 0.001) && ((k * LinkStats!![j]!!.flowTurnSign) < 0))) {
        LinkStats!![j]!!.flowTurns++
    }
}

// stats.c:756
fun stats_findMaxStats() {
    var j: Int = 0
    var x: Double = 0.0
    var z: Double = 0.0
    var stepCount: Double = 0.0
    j = 0
    f1@ while ((j < 5)) {
        MaxMassBalErrs!![j]!!.objType = NODE
        MaxMassBalErrs!![j]!!.index = (-1)
        MaxMassBalErrs!![j]!!.value_ = (-1.0)
        MaxCourantCrit!![j]!!.index = (-1)
        MaxCourantCrit!![j]!!.value_ = (-1.0)
        MaxFlowTurns!![j]!!.index = (-1)
        MaxFlowTurns!![j]!!.value_ = (-1.0)
        MaxNonConverged!![j]!!.index = (-1)
        MaxNonConverged!![j]!!.value_ = 0.0
        j++
    }
    if ((ReportStepCount > 2L)) {
        stepCount = (ReportStepCount).toDouble()
        z = (100.0 / (((2.0 / 3.0) * ((stepCount - 2.0)))))
        j = 0
        f2@ while ((j < Nobjects!![LINK])) {
            x = ((LinkStats!![j]!!.flowTurns).toDouble() * z)
            stats_updateMaxStats(MaxFlowTurns, LINK, j, x)
            j++
        }
    }
    j = 0
    f3@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.degree <= 0)) {
            j++
            continue@f3
        }
        if ((NodeInflow!![j] <= 0.1)) {
            j++
            continue@f3
        }
        if ((NodeInflow!![j] > 0.0)) {
            x = (1.0 - (NodeOutflow!![j] / NodeInflow!![j]))
        } else if ((NodeOutflow!![j] > 0.0)) {
            x = (-1.0)
        } else {
            x = 0.0
        }
        stats_updateMaxStats(MaxMassBalErrs, NODE, j, (100.0 * x))
        j++
    }
    stepCount = (TimeStepStats!!.timeStepCount).toDouble()
    if ((RouteModel == DW)) {
        j = 0
        f4@ while ((j < Nobjects!![NODE])) {
            stats_updateMaxStats(MaxNonConverged, NODE, j, ((NodeStats!![j]!!.nonConvergedCount).toDouble() / stepCount))
            j++
        }
    }
    if (((RouteModel != DW) || (CourantFactor.v == 0.0))) {
        return
    }
    if ((stepCount == 0.0)) {
        return
    }
    j = 0
    f5@ while ((j < Nobjects!![NODE])) {
        x = (NodeStats!![j]!!.timeCourantCritical / stepCount)
        stats_updateMaxStats(MaxCourantCrit, NODE, j, (100.0 * x))
        j++
    }
    j = 0
    f6@ while ((j < Nobjects!![LINK])) {
        x = (LinkStats!![j]!!.timeCourantCritical / stepCount)
        stats_updateMaxStats(MaxCourantCrit, LINK, j, (100.0 * x))
        j++
    }
}

// stats.c:841
fun stats_updateMaxStats(maxStats: Array<TMaxStats?>?, i: Int, j: Int, x: Double) {
    var k: Int = 0
    var maxStats1: TMaxStats? = TMaxStats()
    var maxStats2: TMaxStats? = TMaxStats()
    maxStats1!!.objType = i
    maxStats1!!.index = j
    maxStats1!!.value_ = x
    k = 0
    f1@ while ((k < 5)) {
        if ((abs(maxStats1!!.value_) > abs(maxStats!![k]!!.value_))) {
            maxStats2 = maxStats!![k]!!.copy()
            maxStats!![k] = maxStats1!!.copy()
            maxStats1 = maxStats2!!.copy()
        }
        k++
    }
}

