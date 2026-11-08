// Generated from EPA SWMM 5.2.4 (massbal.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_massbal")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MAX_RUNOFF_BALANCE_ERR: Double = 10.0
@JvmField var MAX_FLOW_BALANCE_ERR: Double = 10.0
@JvmField var RunoffTotals: TRunoffTotals? = TRunoffTotals()
@JvmField var LoadingTotals: Array<TLoadingTotals?>? = null
@JvmField var GwaterTotals: TGwaterTotals? = TGwaterTotals()
@JvmField var FlowTotals: TRoutingTotals? = TRoutingTotals()
@JvmField var QualTotals: Array<TRoutingTotals?>? = null
@JvmField var StepFlowTotals: TRoutingTotals? = TRoutingTotals()
@JvmField var OldStepFlowTotals: TRoutingTotals? = TRoutingTotals()
@JvmField var StepQualTotals: Array<TRoutingTotals?>? = null
@JvmField var NodeInflow: DoubleArray? = null
@JvmField var NodeOutflow: DoubleArray? = null
@JvmField var TotalArea: Double = 0.0

// massbal.c:100
fun massbal_open(): Int {
    var j: Int = 0
    var n: Int = 0
    RunoffError = 0.0
    GwaterError = 0.0
    FlowError = 0.0
    QualError = 0.0
    RunoffTotals!!.rainfall = 0.0
    RunoffTotals!!.evap = 0.0
    RunoffTotals!!.infil = 0.0
    RunoffTotals!!.runoff = 0.0
    RunoffTotals!!.runon = 0.0
    RunoffTotals!!.drains = 0.0
    RunoffTotals!!.snowRemoved = 0.0
    RunoffTotals!!.initStorage = 0.0
    RunoffTotals!!.initSnowCover = 0.0
    TotalArea = 0.0
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        RunoffTotals!!.initStorage += (subcatch_getStorage(j))
        RunoffTotals!!.initSnowCover += (snow_getSnowCover(j))
        TotalArea += Subcatch!![j]!!.area
        j++
    }
    GwaterTotals!!.infil = 0.0
    GwaterTotals!!.upperEvap = 0.0
    GwaterTotals!!.lowerEvap = 0.0
    GwaterTotals!!.lowerPerc = 0.0
    GwaterTotals!!.gwater = 0.0
    GwaterTotals!!.initStorage = 0.0
    GwaterTotals!!.finalStorage = 0.0
    j = 0
    f2@ while ((j < Nobjects!![SUBCATCH])) {
        GwaterTotals!!.initStorage += (gwater_getVolume(j) * Subcatch!![j]!!.area)
        j++
    }
    FlowTotals!!.dwInflow = 0.0
    FlowTotals!!.wwInflow = 0.0
    FlowTotals!!.gwInflow = 0.0
    FlowTotals!!.iiInflow = 0.0
    FlowTotals!!.exInflow = 0.0
    FlowTotals!!.flooding = 0.0
    FlowTotals!!.outflow = 0.0
    FlowTotals!!.evapLoss = 0.0
    FlowTotals!!.seepLoss = 0.0
    FlowTotals!!.reacted = 0.0
    FlowTotals!!.initStorage = 0.0
    j = 0
    f3@ while ((j < Nobjects!![NODE])) {
        FlowTotals!!.initStorage += Node!![j]!!.newVolume
        j++
    }
    j = 0
    f4@ while ((j < Nobjects!![LINK])) {
        FlowTotals!!.initStorage += Link!![j]!!.newVolume
        j++
    }
    StepFlowTotals = FlowTotals!!.copy()
    LoadingTotals = (null)
    QualTotals = (null)
    StepQualTotals = (null)
    NodeInflow = (null)
    NodeOutflow = (null)
    n = Nobjects!![POLLUT]
    if ((n > 0)) {
        LoadingTotals = Array<TLoadingTotals?>(((n).toLong()).toInt()) { TLoadingTotals() }
        if ((LoadingTotals == null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
        j = 0
        f5@ while ((j < n)) {
            LoadingTotals!![j]!!.initLoad = massbal_getBuildup(j)
            LoadingTotals!![j]!!.buildup = 0.0
            LoadingTotals!![j]!!.deposition = 0.0
            LoadingTotals!![j]!!.sweeping = 0.0
            LoadingTotals!![j]!!.infil = 0.0
            LoadingTotals!![j]!!.bmpRemoval = 0.0
            LoadingTotals!![j]!!.runoff = 0.0
            LoadingTotals!![j]!!.finalLoad = 0.0
            j++
        }
    }
    if ((n > 0)) {
        QualTotals = Array<TRoutingTotals?>(((n).toLong()).toInt()) { TRoutingTotals() }
        StepQualTotals = Array<TRoutingTotals?>(((n).toLong()).toInt()) { TRoutingTotals() }
        if (((QualTotals == null) || (StepQualTotals == null))) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
    }
    j = 0
    f6@ while ((j < n)) {
        QualTotals!![j]!!.dwInflow = 0.0
        QualTotals!![j]!!.wwInflow = 0.0
        QualTotals!![j]!!.gwInflow = 0.0
        QualTotals!![j]!!.exInflow = 0.0
        QualTotals!![j]!!.flooding = 0.0
        QualTotals!![j]!!.outflow = 0.0
        QualTotals!![j]!!.evapLoss = 0.0
        QualTotals!![j]!!.seepLoss = 0.0
        QualTotals!![j]!!.reacted = 0.0
        QualTotals!![j]!!.initStorage = massbal_getStoredMass(j)
        j++
    }
    massbal_initTimeStepTotals()
    if ((Nobjects!![NODE] > 0)) {
        NodeInflow = DoubleArray(((Nobjects!![NODE]).toLong()).toInt())
        if ((NodeInflow == null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
        NodeOutflow = DoubleArray(((Nobjects!![NODE]).toLong()).toInt())
        if ((NodeOutflow == null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
        j = 0
        f7@ while ((j < Nobjects!![NODE])) {
            NodeInflow!![j] = Node!![j]!!.newVolume
            j++
        }
    }
    return ErrorCode
}

// massbal.c:246
fun massbal_close() {
    if ((LoadingTotals != null)) {
        R.free(LoadingTotals)
        LoadingTotals = (null)
    }
    if ((QualTotals != null)) {
        R.free(QualTotals)
        QualTotals = (null)
    }
    if ((StepQualTotals != null)) {
        R.free(StepQualTotals)
        StepQualTotals = (null)
    }
    if ((NodeInflow != null)) {
        R.free(NodeInflow)
        NodeInflow = (null)
    }
    if ((NodeOutflow != null)) {
        R.free(NodeOutflow)
        NodeOutflow = (null)
    }
}

// massbal.c:262
fun massbal_report() {
    var j: Int = 0
    var gwArea: Double = 0.0
    if ((Nobjects!![SUBCATCH] > 0)) {
        if (((massbal_getRunoffError() > MAX_RUNOFF_BALANCE_ERR) || (RptFlags!!.continuity == 1))) {
            report_writeRunoffError(RunoffTotals, TotalArea)
        }
        if (((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0)))) {
            if (((massbal_getLoadingError() > MAX_RUNOFF_BALANCE_ERR) || (RptFlags!!.continuity == 1))) {
                report_writeLoadingError(LoadingTotals)
            }
        }
    }
    if (((Nobjects!![AQUIFER] > 0) && (!(IgnoreGwater != 0)))) {
        if (((massbal_getGwaterError() > MAX_RUNOFF_BALANCE_ERR) || (RptFlags!!.continuity == 1))) {
            j = 0
            f1@ while ((j < Nobjects!![SUBCATCH])) {
                if ((Subcatch!![j]!!.groundwater != null)) {
                    gwArea += Subcatch!![j]!!.area
                }
                j++
            }
            if ((gwArea > 0.0)) {
                report_writeGwaterError(GwaterTotals, gwArea)
            }
        }
    }
    if (((Nobjects!![NODE] > 0) && (!(IgnoreRouting != 0)))) {
        if (((massbal_getFlowError() > MAX_FLOW_BALANCE_ERR) || (RptFlags!!.continuity == 1))) {
            report_writeFlowError(FlowTotals)
        }
        if (((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0)))) {
            if (((massbal_getQualError() > MAX_FLOW_BALANCE_ERR) || (RptFlags!!.continuity == 1))) {
                report_writeQualError(QualTotals)
            }
        }
    }
}

// massbal.c:316
fun massbal_getBuildup(p: Int): Double {
    var i: Int = 0
    var j: Int = 0
    var load: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        i = 0
        f2@ while ((i < Nobjects!![LANDUSE])) {
            load += Subcatch!![j]!!.landFactor!![i]!!.buildup!![p]
            i++
        }
        load += (Subcatch!![j]!!.pondedQual!![p] * Pollut!![p]!!.mcf)
        j++
    }
    return load
}

// massbal.c:339
fun massbal_updateRunoffTotals(flowType: Int, v: Double) {
    when (flowType) {
        RUNOFF_RAINFALL -> {
            RunoffTotals!!.rainfall += v
        }
        RUNOFF_EVAP -> {
            RunoffTotals!!.evap += v
        }
        RUNOFF_INFIL -> {
            RunoffTotals!!.infil += v
        }
        RUNOFF_RUNOFF -> {
            RunoffTotals!!.runoff += v
        }
        RUNOFF_DRAINS -> {
            RunoffTotals!!.drains += v
        }
        RUNOFF_RUNON -> {
            RunoffTotals!!.runon += v
        }
    }
}

// massbal.c:360
fun massbal_updateGwaterTotals(vInfil: Double, vUpperEvap: Double, vLowerEvap: Double, vLowerPerc: Double, vGwater: Double) {
    GwaterTotals!!.infil += vInfil
    GwaterTotals!!.upperEvap += vUpperEvap
    GwaterTotals!!.lowerEvap += vLowerEvap
    GwaterTotals!!.lowerPerc += vLowerPerc
    GwaterTotals!!.gwater += vGwater
}

// massbal.c:381
fun massbal_initTimeStepTotals() {
    var j: Int = 0
    OldStepFlowTotals = StepFlowTotals!!.copy()
    StepFlowTotals!!.dwInflow = 0.0
    StepFlowTotals!!.wwInflow = 0.0
    StepFlowTotals!!.gwInflow = 0.0
    StepFlowTotals!!.iiInflow = 0.0
    StepFlowTotals!!.exInflow = 0.0
    StepFlowTotals!!.flooding = 0.0
    StepFlowTotals!!.outflow = 0.0
    StepFlowTotals!!.evapLoss = 0.0
    StepFlowTotals!!.seepLoss = 0.0
    StepFlowTotals!!.reacted = 0.0
    j = 0
    f1@ while ((j < Nobjects!![POLLUT])) {
        StepQualTotals!![j]!!.dwInflow = 0.0
        StepQualTotals!![j]!!.wwInflow = 0.0
        StepQualTotals!![j]!!.gwInflow = 0.0
        StepQualTotals!![j]!!.iiInflow = 0.0
        StepQualTotals!![j]!!.exInflow = 0.0
        StepQualTotals!![j]!!.flooding = 0.0
        StepQualTotals!![j]!!.outflow = 0.0
        StepQualTotals!![j]!!.reacted = 0.0
        StepQualTotals!![j]!!.seepLoss = 0.0
        StepQualTotals!![j]!!.initStorage = 0.0
        StepQualTotals!![j]!!.finalStorage = 0.0
        j++
    }
}

// massbal.c:418
fun massbal_addInflowFlow(type: Int, q: Double) {
    when (type) {
        DRY_WEATHER_INFLOW -> {
            StepFlowTotals!!.dwInflow += q
        }
        WET_WEATHER_INFLOW -> {
            StepFlowTotals!!.wwInflow += q
        }
        GROUNDWATER_INFLOW -> {
            StepFlowTotals!!.gwInflow += q
        }
        RDII_INFLOW -> {
            StepFlowTotals!!.iiInflow += q
        }
        EXTERNAL_INFLOW -> {
            StepFlowTotals!!.exInflow += q
        }
    }
}

// massbal.c:438
fun massbal_updateLoadingTotals(type: Int, p: Int, w: Double) {
    when (type) {
        BUILDUP_LOAD -> {
            LoadingTotals!![p]!!.buildup += w
        }
        DEPOSITION_LOAD -> {
            LoadingTotals!![p]!!.deposition += w
        }
        SWEEPING_LOAD -> {
            LoadingTotals!![p]!!.sweeping += w
        }
        INFIL_LOAD -> {
            LoadingTotals!![p]!!.infil += w
        }
        BMP_REMOVAL_LOAD -> {
            LoadingTotals!![p]!!.bmpRemoval += w
        }
        RUNOFF_LOAD -> {
            LoadingTotals!![p]!!.runoff += w
        }
        FINAL_LOAD -> {
            LoadingTotals!![p]!!.finalLoad += w
        }
    }
}

// massbal.c:461
fun massbal_addInflowQual(type: Int, p: Int, w: Double) {
    if (((p < 0) || (p >= Nobjects!![POLLUT]))) {
        return
    }
    when (type) {
        DRY_WEATHER_INFLOW -> {
            StepQualTotals!![p]!!.dwInflow += w
        }
        WET_WEATHER_INFLOW -> {
            StepQualTotals!![p]!!.wwInflow += w
        }
        GROUNDWATER_INFLOW -> {
            StepQualTotals!![p]!!.gwInflow += w
        }
        EXTERNAL_INFLOW -> {
            StepQualTotals!![p]!!.exInflow += w
        }
        RDII_INFLOW -> {
            StepQualTotals!![p]!!.iiInflow += w
        }
    }
}

// massbal.c:483
fun massbal_addOutflowFlow(q: Double, isFlooded: Int) {
    if ((isFlooded != 0)) {
        StepFlowTotals!!.flooding += q
    } else {
        StepFlowTotals!!.outflow += q
    }
}

// massbal.c:497
fun massbal_addOutflowQual(p: Int, w: Double, isFlooded: Int) {
    if (((p < 0) || (p >= Nobjects!![POLLUT]))) {
        return
    }
    if ((w >= 0.0)) {
        if ((isFlooded != 0)) {
            StepQualTotals!![p]!!.flooding += w
        } else {
            StepQualTotals!![p]!!.outflow += w
        }
    } else {
        StepQualTotals!![p]!!.exInflow -= w
    }
}

// massbal.c:517
fun massbal_addReactedMass(p: Int, w: Double) {
    if (((p < 0) || (p >= Nobjects!![POLLUT]))) {
        return
    }
    StepQualTotals!![p]!!.reacted += w
}

// massbal.c:531
fun massbal_addSeepageLoss(p: Int, w: Double) {
    if (((p < 0) || (p >= Nobjects!![POLLUT]))) {
        return
    }
    StepQualTotals!![p]!!.seepLoss += w
}

// massbal.c:545
fun massbal_addToFinalStorage(p: Int, w: Double) {
    if (((p < 0) || (p >= Nobjects!![POLLUT]))) {
        return
    }
    StepQualTotals!![p]!!.finalStorage += w
}

// massbal.c:559
fun massbal_addNodeLosses(evapLoss: Double, seepLoss: Double) {
    StepFlowTotals!!.evapLoss += evapLoss
    StepFlowTotals!!.seepLoss += seepLoss
}

// massbal.c:573
fun massbal_addLinkLosses(evapLoss: Double, seepLoss: Double) {
    StepFlowTotals!!.evapLoss += evapLoss
    StepFlowTotals!!.seepLoss += seepLoss
}

// massbal.c:587
fun massbal_updateRoutingTotals(tStep: Double) {
    var j: Int = 0
    FlowTotals!!.dwInflow += (StepFlowTotals!!.dwInflow * tStep)
    FlowTotals!!.wwInflow += (StepFlowTotals!!.wwInflow * tStep)
    FlowTotals!!.gwInflow += (StepFlowTotals!!.gwInflow * tStep)
    FlowTotals!!.iiInflow += (StepFlowTotals!!.iiInflow * tStep)
    FlowTotals!!.exInflow += (StepFlowTotals!!.exInflow * tStep)
    FlowTotals!!.flooding += (StepFlowTotals!!.flooding * tStep)
    FlowTotals!!.outflow += (StepFlowTotals!!.outflow * tStep)
    FlowTotals!!.evapLoss += (StepFlowTotals!!.evapLoss * tStep)
    FlowTotals!!.seepLoss += (StepFlowTotals!!.seepLoss * tStep)
    j = 0
    f1@ while ((j < Nobjects!![POLLUT])) {
        QualTotals!![j]!!.dwInflow += (StepQualTotals!![j]!!.dwInflow * tStep)
        QualTotals!![j]!!.wwInflow += (StepQualTotals!![j]!!.wwInflow * tStep)
        QualTotals!![j]!!.gwInflow += (StepQualTotals!![j]!!.gwInflow * tStep)
        QualTotals!![j]!!.iiInflow += (StepQualTotals!![j]!!.iiInflow * tStep)
        QualTotals!![j]!!.exInflow += (StepQualTotals!![j]!!.exInflow * tStep)
        QualTotals!![j]!!.flooding += (StepQualTotals!![j]!!.flooding * tStep)
        QualTotals!![j]!!.outflow += (StepQualTotals!![j]!!.outflow * tStep)
        QualTotals!![j]!!.reacted += (StepQualTotals!![j]!!.reacted * tStep)
        QualTotals!![j]!!.seepLoss += (StepQualTotals!![j]!!.seepLoss * tStep)
        QualTotals!![j]!!.finalStorage += StepQualTotals!![j]!!.finalStorage
        j++
    }
    j = 0
    f2@ while ((j < Nobjects!![NODE])) {
        NodeInflow!![j] += (Node!![j]!!.inflow * tStep)
        if (((Node!![j]!!.type == OUTFALL) || (((Node!![j]!!.degree == 0) && (Node!![j]!!.type != STORAGE))))) {
            NodeOutflow!![j] += (Node!![j]!!.inflow * tStep)
        } else {
            NodeOutflow!![j] += (Node!![j]!!.outflow * tStep)
            if ((Node!![j]!!.newVolume <= Node!![j]!!.fullVolume)) {
                NodeOutflow!![j] += (Node!![j]!!.overflow * tStep)
            }
        }
        j++
    }
}

// massbal.c:638
fun massbal_getStorage(isFinalStorage: Int): Double {
    var j: Int = 0
    var totalStorage: Double = 0.0
    var nodeStorage: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        nodeStorage = Node!![j]!!.newVolume
        if ((isFinalStorage != 0)) {
            NodeOutflow!![j] += nodeStorage
        }
        totalStorage += nodeStorage
        j++
    }
    if (((isFinalStorage != 0) && (RouteModel == SF))) {
        return totalStorage
    }
    j = 0
    f2@ while ((j < Nobjects!![LINK])) {
        totalStorage += Link!![j]!!.newVolume
        j++
    }
    return totalStorage
}

// massbal.c:670
fun massbal_getSysFlows(f: Double, sysFlows: DoubleArray?) {
    var f1: Double = (1.0 - f)
    sysFlows!![SYS_DWFLOW] = ((((f1 * OldStepFlowTotals!!.dwInflow) + (f * StepFlowTotals!!.dwInflow))) * UCF(FLOW))
    sysFlows!![SYS_GWFLOW] = ((((f1 * OldStepFlowTotals!!.gwInflow) + (f * StepFlowTotals!!.gwInflow))) * UCF(FLOW))
    sysFlows!![SYS_IIFLOW] = ((((f1 * OldStepFlowTotals!!.iiInflow) + (f * StepFlowTotals!!.iiInflow))) * UCF(FLOW))
    sysFlows!![SYS_EXFLOW] = ((((f1 * OldStepFlowTotals!!.exInflow) + (f * StepFlowTotals!!.exInflow))) * UCF(FLOW))
    sysFlows!![SYS_FLOODING] = ((((f1 * OldStepFlowTotals!!.flooding) + (f * StepFlowTotals!!.flooding))) * UCF(FLOW))
    sysFlows!![SYS_OUTFLOW] = ((((f1 * OldStepFlowTotals!!.outflow) + (f * StepFlowTotals!!.outflow))) * UCF(FLOW))
    sysFlows!![SYS_STORAGE] = ((((f1 * OldStepFlowTotals!!.finalStorage) + (f * StepFlowTotals!!.finalStorage))) * UCF(VOLUME))
}

// massbal.c:696
fun massbal_getRunoffError(): Double {
    var j: Int = 0
    var totalInflow: Double = 0.0
    var totalOutflow: Double = 0.0
    RunoffTotals!!.finalStorage = 0.0
    RunoffTotals!!.finalSnowCover = 0.0
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        RunoffTotals!!.finalStorage += (subcatch_getStorage(j))
        RunoffTotals!!.finalSnowCover += (snow_getSnowCover(j))
        j++
    }
    RunoffTotals!!.snowRemoved = Snow!!.removed
    totalInflow = (((RunoffTotals!!.rainfall + RunoffTotals!!.runon) + RunoffTotals!!.initStorage) + RunoffTotals!!.initSnowCover)
    totalOutflow = ((((((RunoffTotals!!.evap + RunoffTotals!!.infil) + RunoffTotals!!.runoff) + RunoffTotals!!.drains) + RunoffTotals!!.snowRemoved) + RunoffTotals!!.finalStorage) + RunoffTotals!!.finalSnowCover)
    RunoffTotals!!.pctError = 0.0
    if ((abs((totalInflow - totalOutflow)) < 1.0)) {
        RunoffTotals!!.pctError = 1e-06
    } else if ((totalInflow > 0.0)) {
        RunoffTotals!!.pctError = (100.0 * ((1.0 - (totalOutflow / totalInflow))))
    } else if ((totalOutflow > 0.0)) {
        RunoffTotals!!.pctError = (100.0 * (((totalInflow / totalOutflow) - 1.0)))
    }
    RunoffError = RunoffTotals!!.pctError
    return RunoffTotals!!.pctError
}

// massbal.c:750
fun massbal_getLoadingError(): Double {
    var j: Int = 0
    var loadIn: Double = 0.0
    var loadOut: Double = 0.0
    var maxError: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![POLLUT])) {
        LoadingTotals!![j]!!.finalLoad += (massbal_getBuildup(j))
        loadIn = ((LoadingTotals!![j]!!.initLoad + LoadingTotals!![j]!!.buildup) + LoadingTotals!![j]!!.deposition)
        loadOut = ((((LoadingTotals!![j]!!.sweeping + LoadingTotals!![j]!!.infil) + LoadingTotals!![j]!!.bmpRemoval) + LoadingTotals!![j]!!.runoff) + LoadingTotals!![j]!!.finalLoad)
        LoadingTotals!![j]!!.pctError = 0.0
        if ((abs((loadIn - loadOut)) < 0.001)) {
            LoadingTotals!![j]!!.pctError = 1e-06
        } else if ((loadIn > 0.0)) {
            LoadingTotals!![j]!!.pctError = (100.0 * ((1.0 - (loadOut / loadIn))))
        } else if ((loadOut > 0.0)) {
            LoadingTotals!![j]!!.pctError = (100.0 * (((loadIn / loadOut) - 1.0)))
        }
        maxError = ((if ((((maxError) >= (LoadingTotals!![j]!!.pctError)))) (maxError) else (LoadingTotals!![j]!!.pctError)))
        if ((Pollut!![j]!!.units == COUNT)) {
            LoadingTotals!![j]!!.initLoad = ((if (((LoadingTotals!![j]!!.initLoad) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.initLoad)) else (LoadingTotals!![j]!!.initLoad)))
            LoadingTotals!![j]!!.buildup = ((if (((LoadingTotals!![j]!!.buildup) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.buildup)) else (LoadingTotals!![j]!!.buildup)))
            LoadingTotals!![j]!!.deposition = ((if (((LoadingTotals!![j]!!.deposition) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.deposition)) else (LoadingTotals!![j]!!.deposition)))
            LoadingTotals!![j]!!.sweeping = ((if (((LoadingTotals!![j]!!.sweeping) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.sweeping)) else (LoadingTotals!![j]!!.sweeping)))
            LoadingTotals!![j]!!.infil = ((if (((LoadingTotals!![j]!!.infil) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.infil)) else (LoadingTotals!![j]!!.infil)))
            LoadingTotals!![j]!!.bmpRemoval = ((if (((LoadingTotals!![j]!!.bmpRemoval) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.bmpRemoval)) else (LoadingTotals!![j]!!.bmpRemoval)))
            LoadingTotals!![j]!!.runoff = ((if (((LoadingTotals!![j]!!.runoff) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.runoff)) else (LoadingTotals!![j]!!.runoff)))
            LoadingTotals!![j]!!.finalLoad = ((if (((LoadingTotals!![j]!!.finalLoad) > 0.0)) Glibm.log10((LoadingTotals!![j]!!.finalLoad)) else (LoadingTotals!![j]!!.finalLoad)))
        }
        j++
    }
    return maxError
}

// massbal.c:813
fun massbal_getGwaterError(): Double {
    var j: Int = 0
    var totalInflow: Double = 0.0
    var totalOutflow: Double = 0.0
    GwaterTotals!!.finalStorage = 0.0
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        GwaterTotals!!.finalStorage += (gwater_getVolume(j) * Subcatch!![j]!!.area)
        j++
    }
    totalInflow = (GwaterTotals!!.infil + GwaterTotals!!.initStorage)
    totalOutflow = ((((GwaterTotals!!.upperEvap + GwaterTotals!!.lowerEvap) + GwaterTotals!!.lowerPerc) + GwaterTotals!!.gwater) + GwaterTotals!!.finalStorage)
    GwaterTotals!!.pctError = 0.0
    if ((abs((totalInflow - totalOutflow)) < 1.0)) {
        GwaterTotals!!.pctError = 1e-06
    } else if ((totalInflow > 0.0)) {
        GwaterTotals!!.pctError = (100.0 * ((1.0 - (totalOutflow / totalInflow))))
    } else if ((totalOutflow > 0.0)) {
        GwaterTotals!!.pctError = (100.0 * (((totalInflow / totalOutflow) - 1.0)))
    }
    GwaterError = GwaterTotals!!.pctError
    return GwaterTotals!!.pctError
}

// massbal.c:858
fun massbal_getFlowError(): Double {
    var totalInflow: Double = 0.0
    var totalOutflow: Double = 0.0
    FlowTotals!!.finalStorage = massbal_getStorage(1)
    totalInflow = ((FlowTotals!!.initStorage + FlowTotals!!.wwInflow) + FlowTotals!!.iiInflow)
    totalOutflow = ((((FlowTotals!!.finalStorage + FlowTotals!!.flooding) + FlowTotals!!.evapLoss) + FlowTotals!!.seepLoss) + FlowTotals!!.reacted)
    if ((FlowTotals!!.dwInflow >= 0.0)) {
        totalInflow += FlowTotals!!.dwInflow
    } else {
        totalOutflow -= FlowTotals!!.dwInflow
    }
    if ((FlowTotals!!.gwInflow >= 0.0)) {
        totalInflow += FlowTotals!!.gwInflow
    } else {
        totalOutflow -= FlowTotals!!.gwInflow
    }
    if ((FlowTotals!!.exInflow >= 0.0)) {
        totalInflow += FlowTotals!!.exInflow
    } else {
        totalOutflow -= FlowTotals!!.exInflow
    }
    if ((FlowTotals!!.outflow >= 0.0)) {
        totalOutflow += FlowTotals!!.outflow
    } else {
        totalInflow -= FlowTotals!!.outflow
    }
    FlowTotals!!.pctError = 0.0
    if ((abs((totalInflow - totalOutflow)) < 1.0)) {
        FlowTotals!!.pctError = 1e-06
    } else if ((abs(totalInflow) > 0.0)) {
        FlowTotals!!.pctError = (100.0 * ((1.0 - (totalOutflow / totalInflow))))
    } else if ((abs(totalOutflow) > 0.0)) {
        FlowTotals!!.pctError = (100.0 * (((totalInflow / totalOutflow) - 1.0)))
    }
    FlowError = FlowTotals!!.pctError
    return FlowTotals!!.pctError
}

// massbal.c:906
fun massbal_getQualError(): Double {
    var p: Int = 0
    var maxQualError: Double = 0.0
    var totalInflow: Double = 0.0
    var totalOutflow: Double = 0.0
    var cf: Double = 0.0
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        QualTotals!![p]!!.finalStorage += (massbal_getStoredMass(p))
        totalInflow = (((((QualTotals!![p]!!.dwInflow + QualTotals!![p]!!.wwInflow) + QualTotals!![p]!!.gwInflow) + QualTotals!![p]!!.iiInflow) + QualTotals!![p]!!.exInflow) + QualTotals!![p]!!.initStorage)
        totalOutflow = ((((QualTotals!![p]!!.flooding + QualTotals!![p]!!.outflow) + QualTotals!![p]!!.reacted) + QualTotals!![p]!!.seepLoss) + QualTotals!![p]!!.finalStorage)
        QualTotals!![p]!!.pctError = 0.0
        if ((abs((totalInflow - totalOutflow)) < 0.001)) {
            QualTotals!![p]!!.pctError = 1e-06
        } else if ((totalInflow > 0.0)) {
            QualTotals!![p]!!.pctError = (100.0 * ((1.0 - (totalOutflow / totalInflow))))
        } else if ((totalOutflow > 0.0)) {
            QualTotals!![p]!!.pctError = (100.0 * (((totalInflow / totalOutflow) - 1.0)))
        }
        if ((abs(QualTotals!![p]!!.pctError) > abs(maxQualError))) {
            maxQualError = QualTotals!![p]!!.pctError
        }
        cf = 28.317
        if ((Pollut!![p]!!.units == COUNT)) {
            QualTotals!![p]!!.dwInflow = ((if ((((cf * QualTotals!![p]!!.dwInflow)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.dwInflow))) else ((cf * QualTotals!![p]!!.dwInflow))))
            QualTotals!![p]!!.wwInflow = ((if ((((cf * QualTotals!![p]!!.wwInflow)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.wwInflow))) else ((cf * QualTotals!![p]!!.wwInflow))))
            QualTotals!![p]!!.gwInflow = ((if ((((cf * QualTotals!![p]!!.gwInflow)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.gwInflow))) else ((cf * QualTotals!![p]!!.gwInflow))))
            QualTotals!![p]!!.iiInflow = ((if ((((cf * QualTotals!![p]!!.iiInflow)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.iiInflow))) else ((cf * QualTotals!![p]!!.iiInflow))))
            QualTotals!![p]!!.exInflow = ((if ((((cf * QualTotals!![p]!!.exInflow)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.exInflow))) else ((cf * QualTotals!![p]!!.exInflow))))
            QualTotals!![p]!!.flooding = ((if ((((cf * QualTotals!![p]!!.flooding)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.flooding))) else ((cf * QualTotals!![p]!!.flooding))))
            QualTotals!![p]!!.outflow = ((if ((((cf * QualTotals!![p]!!.outflow)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.outflow))) else ((cf * QualTotals!![p]!!.outflow))))
            QualTotals!![p]!!.reacted = ((if ((((cf * QualTotals!![p]!!.reacted)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.reacted))) else ((cf * QualTotals!![p]!!.reacted))))
            QualTotals!![p]!!.seepLoss = ((if ((((cf * QualTotals!![p]!!.seepLoss)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.seepLoss))) else ((cf * QualTotals!![p]!!.seepLoss))))
            QualTotals!![p]!!.initStorage = ((if ((((cf * QualTotals!![p]!!.initStorage)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.initStorage))) else ((cf * QualTotals!![p]!!.initStorage))))
            QualTotals!![p]!!.finalStorage = ((if ((((cf * QualTotals!![p]!!.finalStorage)) > 0.0)) Glibm.log10(((cf * QualTotals!![p]!!.finalStorage))) else ((cf * QualTotals!![p]!!.finalStorage))))
        } else {
            cf = (cf * UCF(MASS))
            if ((Pollut!![p]!!.units == UG)) {
                cf /= 1000.0
            }
            QualTotals!![p]!!.dwInflow *= cf
            QualTotals!![p]!!.wwInflow *= cf
            QualTotals!![p]!!.gwInflow *= cf
            QualTotals!![p]!!.iiInflow *= cf
            QualTotals!![p]!!.exInflow *= cf
            QualTotals!![p]!!.flooding *= cf
            QualTotals!![p]!!.outflow *= cf
            QualTotals!![p]!!.reacted *= cf
            QualTotals!![p]!!.seepLoss *= cf
            QualTotals!![p]!!.initStorage *= cf
            QualTotals!![p]!!.finalStorage *= cf
        }
        p++
    }
    QualError = maxQualError
    return maxQualError
}

// massbal.c:995
fun massbal_getStepFlowError(): Double {
    var totalInflow: Double = 0.0
    var totalOutflow: Double = 0.0
    totalInflow = ((((StepFlowTotals!!.dwInflow + StepFlowTotals!!.wwInflow) + StepFlowTotals!!.gwInflow) + StepFlowTotals!!.iiInflow) + StepFlowTotals!!.exInflow)
    totalOutflow = ((((StepFlowTotals!!.flooding + StepFlowTotals!!.outflow) + StepFlowTotals!!.evapLoss) + StepFlowTotals!!.seepLoss) + StepFlowTotals!!.reacted)
    if ((abs(totalInflow) > 0.0)) {
        return (1.0 - (totalOutflow / totalInflow))
    } else if ((abs(totalOutflow) > 0.0)) {
        return ((totalInflow / totalOutflow) - 1.0)
    } else {
        return 0.0
    }
    throw IllegalStateException("massbal_getStepFlowError: no return")
}

// massbal.c:1025
fun massbal_getStoredMass(p: Int): Double {
    var j: Int = 0
    var storedMass: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        storedMass += (Node!![j]!!.newVolume * Node!![j]!!.newQual!![p])
        j++
    }
    if ((RouteModel != SF)) {
        j = 0
        f2@ while ((j < Nobjects!![LINK])) {
            storedMass += (Link!![j]!!.newVolume * Link!![j]!!.newQual!![p])
            j++
        }
    }
    return storedMass
}

