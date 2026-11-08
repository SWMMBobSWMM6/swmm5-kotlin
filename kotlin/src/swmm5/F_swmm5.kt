// Generated from EPA SWMM 5.2.4 (swmm5.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_swmm5")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var Finp: TFile? = TFile()
@JvmField var Fout: TFile? = TFile()
@JvmField var Frpt: TFile? = TFile()
@JvmField var Fclimate: TFile? = TFile()
@JvmField var Frain: TFile? = TFile()
@JvmField var Frunoff: TFile? = TFile()
@JvmField var Frdii: TFile? = TFile()
@JvmField var Fhotstart1: TFile? = TFile()
@JvmField var Fhotstart2: TFile? = TFile()
@JvmField var Finflows: TFile? = TFile()
@JvmField var Foutflows: TFile? = TFile()
@JvmField var Nperiods: Long = 0L
@JvmField var TotalStepCount: Long = 0L
@JvmField var ReportStepCount: Long = 0L
@JvmField var NonConvergeCount: Long = 0L
@JvmField var Msg: CPtr? = R.buf(1025)
@JvmField var ErrorMsg: CPtr? = R.buf(1025)
@JvmField var Title: Array<CPtr?>? = R.bufs(3, 1025)
@JvmField var TempDir: CPtr? = R.buf(260)
@JvmField var InpDir: CPtr? = R.buf(260)
@JvmField var RptFlags: TRptFlags? = TRptFlags()
@JvmField var Nobjects: IntArray? = IntArray(18)
@JvmField var Nnodes: IntArray? = IntArray(5)
@JvmField var Nlinks: IntArray? = IntArray(5)
@JvmField var UnitSystem: Int = 0
@JvmField var FlowUnits: Int = 0
@JvmField var InfilModel: Int = 0
@JvmField var RouteModel: Int = 0
@JvmField var ForceMainEqn: Int = 0
@JvmField var LinkOffsets: Int = 0
@JvmField var SurchargeMethod: Int = 0
@JvmField var AllowPonding: Int = 0
@JvmField var InertDamping: Int = 0
@JvmField var NormalFlowLtd: Int = 0
@JvmField var SlopeWeighting: Int = 0
@JvmField var Compatibility: Int = 0
@JvmField var SkipSteadyState: Int = 0
@JvmField var IgnoreRainfall: Int = 0
@JvmField var IgnoreRDII: Int = 0
@JvmField var IgnoreSnowmelt: Int = 0
@JvmField var IgnoreGwater: Int = 0
@JvmField var IgnoreRouting: Int = 0
@JvmField var IgnoreQuality: Int = 0
@JvmField var ErrorCode: Int = 0
@JvmField var Warnings: Int = 0
@JvmField var WetStep: Int = 0
@JvmField var DryStep: Int = 0
@JvmField var ReportStep: Int = 0
@JvmField var RuleStep: Int = 0
@JvmField var SweepStart: Int = 0
@JvmField var SweepEnd: Int = 0
@JvmField var MaxTrials: Int = 0
@JvmField var NumThreads: Int = 0
@JvmField var NumEvents: Int = 0
@JvmField var RouteStep: Double = 0.0
@JvmField val MinRouteStep = DBox(0.0)
@JvmField var LengtheningStep: Double = 0.0
@JvmField var StartDryDays: Double = 0.0
@JvmField val CourantFactor = DBox(0.0)
@JvmField val MinSurfArea = DBox(0.0)
@JvmField val MinSlope = DBox(0.0)
@JvmField var RunoffError: Double = 0.0
@JvmField var GwaterError: Double = 0.0
@JvmField var FlowError: Double = 0.0
@JvmField var QualError: Double = 0.0
@JvmField val HeadTol = DBox(0.0)
@JvmField val SysFlowTol = DBox(0.0)
@JvmField val LatFlowTol = DBox(0.0)
@JvmField var CrownCutoff: Double = 0.0
@JvmField val StartDate = DBox(0.0)
@JvmField val StartTime = DBox(0.0)
@JvmField var StartDateTime: Double = 0.0
@JvmField val EndDate = DBox(0.0)
@JvmField val EndTime = DBox(0.0)
@JvmField var EndDateTime: Double = 0.0
@JvmField val ReportStartDate = DBox(0.0)
@JvmField val ReportStartTime = DBox(0.0)
@JvmField var ReportStart: Double = 0.0
@JvmField var ReportTime: Double = 0.0
@JvmField var OldRunoffTime: Double = 0.0
@JvmField var NewRunoffTime: Double = 0.0
@JvmField var OldRoutingTime: Double = 0.0
@JvmField var NewRoutingTime: Double = 0.0
@JvmField var TotalDuration: Double = 0.0
@JvmField var ElapsedTime: Double = 0.0
@JvmField var Temp: TTemp? = TTemp()
@JvmField var Evap: TEvap? = TEvap()
@JvmField var Wind: TWind? = TWind()
@JvmField var Snow: TSnow? = TSnow()
@JvmField var Adjust: TAdjust? = TAdjust()
@JvmField var Snowmelt: Array<TSnowmelt?>? = null
@JvmField var Gage: Array<TGage?>? = null
@JvmField var Subcatch: Array<TSubcatch?>? = null
@JvmField var Aquifer: Array<TAquifer?>? = null
@JvmField var UnitHyd: Array<TUnitHyd?>? = null
@JvmField var Node: Array<TNode?>? = null
@JvmField var Outfall: Array<TOutfall?>? = null
@JvmField var Divider: Array<TDivider?>? = null
@JvmField var Storage: Array<TStorage?>? = null
@JvmField var Link: Array<TLink?>? = null
@JvmField var Conduit: Array<TConduit?>? = null
@JvmField var Pump: Array<TPump?>? = null
@JvmField var Orifice: Array<TOrifice?>? = null
@JvmField var Weir: Array<TWeir?>? = null
@JvmField var Outlet: Array<TOutlet?>? = null
@JvmField var Pollut: Array<TPollut?>? = null
@JvmField var Landuse: Array<TLanduse?>? = null
@JvmField var Pattern: Array<TPattern?>? = null
@JvmField var Curve: Array<TTable?>? = null
@JvmField var Tseries: Array<TTable?>? = null
@JvmField var Transect: Array<TTransect?>? = null
@JvmField var Street: Array<TStreet?>? = null
@JvmField var Shape: Array<TShape?>? = null
@JvmField var Event: Array<TEvent?>? = null
@JvmField var Ucf: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(43200.0, 1097280.0), doubleArrayOf(12.0, 304.8), doubleArrayOf(1036800.0, 26334720.0), doubleArrayOf(1.0, 0.3048), doubleArrayOf(2.2956e-5, 0.92903e-5), doubleArrayOf(1.0, 0.02832), doubleArrayOf(1.0, 1.608), doubleArrayOf(1.0, 1.8), doubleArrayOf(2.203e-6, 1.0e-6), doubleArrayOf(43560.0, 3048.0))
@JvmField var Qcf: DoubleArray? = doubleArrayOf(1.0, 448.831, 0.64632, 0.02832, 28.317, 2.4466)
@JvmField var IsOpenFlag: Int = 0
@JvmField var IsStartedFlag: Int = 0
@JvmField var SaveResultsFlag: Int = 0
@JvmField var ExceptionCount: Int = 0
@JvmField var DoRunoff: Int = 0
@JvmField var DoRouting: Int = 0
@JvmField var RoutingDuration: Double = 0.0

// swmm5.c:186
fun swmm_run(f1: CPtr?, f2: CPtr?, f3: CPtr?): Int {
    var newHour: Long = 0L
    var oldHour: Long = 0L
    var theDay: Long = 0L
    var theHour: Long = 0L
    val elapsedTime = DBox(0.0)
    IsOpenFlag = 0
    IsStartedFlag = 0
    SaveResultsFlag = 1
    ErrorCode = 0
    writecon(__L642)
    swmm_open(f1, f2, f3)
    if (!(ErrorCode != 0)) {
        swmm_start(1)
        if (!(ErrorCode != 0)) {
            writecon(__L643)
            d1@ do {
                swmm_step(elapsedTime)
                newHour = R.D2L(((elapsedTime.v * 24.0)))
                if ((newHour > oldHour)) {
                    theDay = R.D2L(elapsedTime.v)
                    theHour = R.D2L(((((elapsedTime.v - floor(elapsedTime.v))) * 24.0)))
                    writecon(__L644)
                    R.snprintf(Msg, (1024L).toInt(), __L645, theDay, theHour)
                    writecon(Msg)
                    oldHour = newHour
                }
            } while (((elapsedTime.v > 0.0) && (!(ErrorCode != 0))))
            writecon(__L646)
            writecon(__L647)
        }
        swmm_end()
    }
    if (((!(ErrorCode != 0)) && (Fout!!.mode == SCRATCH_FILE))) {
        writecon(__L648)
        swmm_report()
    }
    swmm_close()
    return ErrorCode
}

// swmm5.c:256
fun swmm_open(f1: CPtr?, f2: CPtr?, f3: CPtr?): Int {
    datetime_setDateFormat(1)
    ErrorCode = 0
    ErrorMsg!!.s(0, 0)
    Warnings = 0
    IsOpenFlag = 0
    IsStartedFlag = 0
    ExceptionCount = 0
    R.strcpy(InpDir, __L0)
    project_open(f1, f2, f3)
    getAbsolutePath(f1, InpDir, 260L)
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    IsOpenFlag = 1
    report_writeLogo()
    project_readInput()
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    report_writeTitle()
    project_validate()
    return ErrorCode
}

// swmm5.c:314
fun swmm_start(saveResults: Int): Int {
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    if (!(IsOpenFlag != 0)) {
        return ((ERR_API_NOT_OPEN).also { __v -> ErrorCode = __v })
    }
    if ((IsStartedFlag != 0)) {
        return ((ERR_API_NOT_ENDED).also { __v -> ErrorCode = __v })
    }
    if (!(RptFlags!!.disabled != 0)) {
        if ((RptFlags!!.input != 0)) {
            inputrpt_writeInput()
        }
        report_writeOptions()
    }
    SaveResultsFlag = saveResults
    ExceptionCount = 0
    ElapsedTime = 0.0
    RoutingDuration = TotalDuration
    NewRunoffTime = 0.0
    NewRoutingTime = 0.0
    ReportTime = (1000.0 * (ReportStep).toDouble())
    TotalStepCount = 0L
    ReportStepCount = 0L
    NonConvergeCount = 0L
    IsStartedFlag = 1
    RunoffError = 0.0
    GwaterError = 0.0
    FlowError = 0.0
    QualError = 0.0
    if (!(IgnoreRainfall != 0)) {
        rain_open()
    }
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    project_init()
    if ((Nobjects!![SUBCATCH] > 0)) {
        DoRunoff = 1
    } else {
        DoRunoff = 0
    }
    if (((Nobjects!![NODE] > 0) && (!(IgnoreRouting != 0)))) {
        DoRouting = 1
    } else {
        DoRouting = 0
    }
    output_open()
    if ((DoRunoff != 0)) {
        runoff_open()
    }
    if (!(hotstart_open() != 0)) {
        return ErrorCode
    }
    if ((DoRouting != 0)) {
        routing_open()
    }
    massbal_open()
    stats_open()
    if (((!(RptFlags!!.disabled != 0)) && (RptFlags!!.controls != 0))) {
        report_writeControlActionsHeading()
    }
    return ErrorCode
}

// swmm5.c:410
fun swmm_step(elapsedTime: DRef?): Int {
    elapsedTime!!.set(0.0)
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    if (!(IsOpenFlag != 0)) {
        return ((ERR_API_NOT_OPEN).also { __v -> ErrorCode = __v })
    }
    if (!(IsStartedFlag != 0)) {
        return ((ERR_API_NOT_STARTED).also { __v -> ErrorCode = __v })
    }
    if ((NewRoutingTime < RoutingDuration)) {
        execRouting()
    }
    if ((SaveResultsFlag != 0)) {
        saveResults()
    }
    if ((NewRoutingTime < RoutingDuration)) {
        ElapsedTime = (NewRoutingTime / 86400000.0)
    } else {
        ElapsedTime = 0.0
    }
    elapsedTime!!.set(ElapsedTime)
    return ErrorCode
}

// swmm5.c:466
fun swmm_stride(strideStep: Int, elapsedTime: DRef?): Int {
    var realRouteStep: Double = RouteStep
    elapsedTime!!.set(0.0)
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    if (!(IsOpenFlag != 0)) {
        return ((ERR_API_NOT_OPEN).also { __v -> ErrorCode = __v })
    }
    if (!(IsStartedFlag != 0)) {
        return ((ERR_API_NOT_STARTED).also { __v -> ErrorCode = __v })
    }
    RoutingDuration = (NewRoutingTime + (1000.0 * (strideStep).toDouble()))
    RoutingDuration = ((if ((((TotalDuration) <= (RoutingDuration)))) (TotalDuration) else (RoutingDuration)))
    if (((strideStep).toDouble() < RouteStep)) {
        RouteStep = (strideStep).toDouble()
    }
    d1@ do {
        swmm_step(elapsedTime)
    } while (((elapsedTime!!.get() > 0.0) && (!(ErrorCode != 0))))
    RouteStep = realRouteStep
    RoutingDuration = TotalDuration
    if ((NewRoutingTime < TotalDuration)) {
        ElapsedTime = (NewRoutingTime / 86400000.0)
    } else {
        ElapsedTime = 0.0
    }
    elapsedTime!!.set(ElapsedTime)
    return ErrorCode
}

// swmm5.c:514
fun execRouting() {
    var nextRoutingTime: Double = 0.0
    var routingStep: Double = 0.0
    TotalStepCount++
    if (!(DoRouting != 0)) {
        routingStep = (((if ((((WetStep) <= (ReportStep)))) (WetStep) else (ReportStep)))).toDouble()
    } else {
        routingStep = routing_getRoutingStep(RouteModel, RouteStep)
    }
    if ((routingStep <= 0.0)) {
        ErrorCode = ERR_TIMESTEP
        return
    }
    nextRoutingTime = (NewRoutingTime + (1000.0 * routingStep))
    if ((nextRoutingTime > RoutingDuration)) {
        routingStep = (((RoutingDuration - NewRoutingTime)) / 1000.0)
        routingStep = ((if ((((routingStep) >= ((1.0 / 1000.0))))) (routingStep) else ((1.0 / 1000.0))))
        nextRoutingTime = RoutingDuration
    }
    if ((DoRunoff != 0)) {
        w1@ while ((NewRunoffTime < nextRoutingTime)) {
            runoff_execute()
            if ((ErrorCode != 0)) {
                return
            }
        }
    } else {
        climate_setState(getDateTime(NewRoutingTime))
    }
    if ((DoRouting != 0)) {
        routing_execute(RouteModel, routingStep)
    } else {
        NewRoutingTime = nextRoutingTime
    }
}

// swmm5.c:579
fun saveResults() {
    if ((NewRoutingTime >= ReportTime)) {
        if ((RptFlags!!.averages != 0)) {
            if ((NewRoutingTime == ReportTime)) {
                output_updateAvgResults()
            }
            output_saveResults(ReportTime)
            if ((NewRoutingTime > ReportTime)) {
                output_updateAvgResults()
            }
        } else {
            output_saveResults(ReportTime)
        }
        ReportTime = (ReportTime + (1000.0 * (ReportStep).toDouble()))
    } else if ((RptFlags!!.averages != 0)) {
        output_updateAvgResults()
    }
}

// swmm5.c:618
fun swmm_end(): Int {
    if (!(IsOpenFlag != 0)) {
        return ((ERR_API_NOT_OPEN).also { __v -> ErrorCode = __v })
    }
    if ((IsStartedFlag != 0)) {
        if ((Fout!!.file != null)) {
            output_end()
        }
        if (((!(ErrorCode != 0)) && (RptFlags!!.disabled == 0))) {
            massbal_report()
            stats_report()
        }
        stats_close()
        massbal_close()
        if (!(IgnoreRainfall != 0)) {
            rain_close()
        }
        if ((DoRunoff != 0)) {
            runoff_close()
        }
        if ((DoRouting != 0)) {
            routing_close(RouteModel)
        }
        hotstart_close()
        IsStartedFlag = 0
    }
    return ErrorCode
}

// swmm5.c:655
fun swmm_report(): Int {
    if (!(ErrorCode != 0)) {
        report_writeReport()
    }
    return ErrorCode
}

// swmm5.c:669
fun swmm_writeLine(line: CPtr?) {
    if ((IsOpenFlag != 0)) {
        report_writeLine(line)
    }
}

// swmm5.c:682
fun swmm_close(): Int {
    if ((Fout!!.file != null)) {
        output_close()
    }
    if ((IsOpenFlag != 0)) {
        project_close()
    }
    report_writeSysTime()
    if ((Finp!!.file != null)) {
        R.fclose(Finp!!.file)
    }
    if ((Frpt!!.file != null)) {
        R.fclose(Frpt!!.file)
    }
    if ((Fout!!.file != null)) {
        R.fclose(Fout!!.file)
        if ((Fout!!.mode == SCRATCH_FILE)) {
            R.remove(Fout!!.name)
        }
    }
    IsOpenFlag = 0
    IsStartedFlag = 0
    return 0
}

// swmm5.c:708
fun swmm_getMassBalErr(runoffErr: FRef?, flowErr: FRef?, qualErr: FRef?): Int {
    runoffErr!!.set((0.0).toFloat())
    flowErr!!.set((0.0).toFloat())
    qualErr!!.set((0.0).toFloat())
    if (((IsOpenFlag != 0) && (!(IsStartedFlag != 0)))) {
        runoffErr!!.set((RunoffError).toFloat())
        flowErr!!.set((FlowError).toFloat())
        qualErr!!.set((QualError).toFloat())
    }
    return 0
}

// swmm5.c:734
fun swmm_getVersion(): Int {
    return 52004
}

// swmm5.c:749
fun swmm_getWarnings(): Int {
    return Warnings
}

// swmm5.c:760
fun swmm_getError(errMsg: CPtr?, msgLen: Int): Int {
    if (((ErrorCode > 0) && (R.strlen(ErrorMsg) == 0L))) {
        error_getMsg(ErrorCode, ErrorMsg)
    }
    sstrncpy(errMsg, ErrorMsg, (msgLen).toLong())
    if (((msgLen > 0) && (errMsg!!.g(0) == 10))) {
        errMsg!!.s(0, 32)
    }
    return ErrorCode
}

// swmm5.c:780
fun swmm_getCount(objType: Int): Int {
    if (!(IsOpenFlag != 0)) {
        return 0
    }
    if (((objType < swmm_GAGE) || (objType > swmm_LINK))) {
        return 0
    }
    return Nobjects!![objType]
}

// swmm5.c:795
fun swmm_getName(objType: Int, index: Int, name: CPtr?, size: Int) {
    var idName: CPtr? = null
    name!!.s(0, 0)
    if (!(IsOpenFlag != 0)) {
        return
    }
    if (((objType < swmm_GAGE) || (objType > swmm_LINK))) {
        return
    }
    if (((index < 0) || (index >= Nobjects!![objType]))) {
        return
    }
    when (objType) {
        GAGE -> {
            idName = Gage!![index]!!.ID
        }
        SUBCATCH -> {
            idName = Subcatch!![index]!!.ID
        }
        NODE -> {
            idName = Node!![index]!!.ID
        }
        LINK -> {
            idName = Link!![index]!!.ID
        }
    }
    if ((idName != null)) {
        sstrncpy(name, idName, (size).toLong())
    }
}

// swmm5.c:826
fun swmm_getIndex(objType: Int, name: CPtr?): Int {
    if (!(IsOpenFlag != 0)) {
        return (-1)
    }
    if (((objType < swmm_GAGE) || (objType > swmm_LINK))) {
        return (-1)
    }
    return project_findObject(objType, name)
}

// swmm5.c:842
fun swmm_getValue(property: Int, index: Int): Double {
    if (!(IsOpenFlag != 0)) {
        return 0.0
    }
    if ((property < 100)) {
        return getSystemValue(property)
    }
    if ((property < 200)) {
        return getGageValue(property, index)
    }
    if ((property < 300)) {
        return getSubcatchValue(property, index)
    }
    if ((property < 400)) {
        return getNodeValue(property, index)
    }
    if ((property < 500)) {
        return getLinkValue(property, index)
    }
    return 0.0
}

// swmm5.c:867
fun swmm_setValue(property: Int, index: Int, value_: Double) {
    if (!(IsOpenFlag != 0)) {
        return
    }
    when (property) {
        swmm_GAGE_RAINFALL -> {
            if (((index < 0) || (index >= Nobjects!![GAGE]))) {
                return
            }
            if ((value_ >= 0.0)) {
                Gage!![index]!!.apiRainfall = value_
            }
            return
        }
        swmm_SUBCATCH_RPTFLAG -> {
            if ((((!(IsStartedFlag != 0)) && (index >= 0)) && (index < Nobjects!![SUBCATCH]))) {
                Subcatch!![index]!!.rptFlag = (if (((value_ > 0.0))) 1 else 0)
            }
            return
        }
        swmm_NODE_LATFLOW -> {
            setNodeLatFlow(index, value_)
            return
        }
        swmm_NODE_HEAD -> {
            setOutfallStage(index, value_)
            return
        }
        swmm_NODE_RPTFLAG -> {
            if ((((!(IsStartedFlag != 0)) && (index >= 0)) && (index < Nobjects!![NODE]))) {
                Node!![index]!!.rptFlag = (if (((value_ > 0.0))) 1 else 0)
            }
            return
        }
        swmm_LINK_SETTING -> {
            setLinkSetting(index, value_)
            return
        }
        swmm_LINK_RPTFLAG -> {
            if ((((!(IsStartedFlag != 0)) && (index >= 0)) && (index < Nobjects!![LINK]))) {
                Link!![index]!!.rptFlag = (if (((value_ > 0.0))) 1 else 0)
            }
            return
        }
        swmm_ROUTESTEP -> {
            setRoutingStep(value_)
            return
        }
        swmm_REPORTSTEP -> {
            if (((!(IsStartedFlag != 0)) && (value_ > 0.0))) {
                ReportStep = R.D2I(value_)
            }
            return
        }
        swmm_NOREPORT -> {
            if (!(IsStartedFlag != 0)) {
                RptFlags!!.disabled = (if (((value_ > 0.0))) 1 else 0)
            }
            return
        }
    }
}

// swmm5.c:922
fun swmm_getSavedValue(property: Int, index: Int, period: Int): Double {
    if (!(IsOpenFlag != 0)) {
        return 0.0
    }
    if ((IsStartedFlag != 0)) {
        return 0.0
    }
    if (((period < 1) || ((period).toLong() > Nperiods))) {
        return 0.0
    }
    if ((property == swmm_CURRENTDATE)) {
        return getSavedDate(period)
    }
    if (((property >= 200) && (property < 300))) {
        return getSavedSubcatchValue(property, index, period)
    }
    if ((property < 400)) {
        return getSavedNodeValue(property, index, period)
    }
    if ((property < 500)) {
        return getSavedLinkValue(property, index, period)
    }
    return 0.0
}

// swmm5.c:949
fun swmm_decodeDate(date: Double, year: IRef?, month: IRef?, day: IRef?, hour: IRef?, minute: IRef?, second: IRef?, dayOfWeek: IRef?) {
    datetime_decodeDate(date, year, month, day)
    datetime_decodeTime(date, hour, minute, second)
    dayOfWeek!!.set(datetime_dayOfWeek(date))
}

// swmm5.c:966
fun getGageValue(property: Int, index: Int): Double {
    if (((index < 0) || (index >= Nobjects!![GAGE]))) {
        return 0.0
    }
    if ((property == swmm_GAGE_RAINFALL)) {
        return Gage!![index]!!.reportRainfall
    }
    return 0.0
}

// swmm5.c:982
fun getSubcatchValue(property: Int, index: Int): Double {
    var subcatch: TSubcatch? = null
    if (((index < 0) || (index >= Nobjects!![SUBCATCH]))) {
        return 0.0
    }
    subcatch = Subcatch!![index]
    when (property) {
        swmm_SUBCATCH_AREA -> {
            return (subcatch!!.area * UCF(LANDAREA))
        }
        swmm_SUBCATCH_RAINGAGE -> {
            return (subcatch!!.gage).toDouble()
        }
        swmm_SUBCATCH_RAINFALL -> {
            if ((subcatch!!.gage >= 0)) {
                return Gage!![subcatch!!.gage]!!.reportRainfall
            } else {
                return 0.0
            }
        }
        swmm_SUBCATCH_EVAP -> {
            return (subcatch!!.evapLoss * UCF(EVAPRATE))
        }
        swmm_SUBCATCH_INFIL -> {
            return (subcatch!!.infilLoss * UCF(RAINFALL))
        }
        swmm_SUBCATCH_RUNOFF -> {
            return (subcatch!!.newRunoff * UCF(FLOW))
        }
        swmm_SUBCATCH_RPTFLAG -> {
            return ((if (((subcatch!!.rptFlag > 0))) 1 else 0)).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getSubcatchValue: no return")
}

// swmm5.c:1019
fun getNodeValue(property: Int, index: Int): Double {
    var node: TNode? = null
    if (((index < 0) || (index >= Nobjects!![NODE]))) {
        return 0.0
    }
    node = Node!![index]
    when (property) {
        swmm_NODE_TYPE -> {
            return (node!!.type).toDouble()
        }
        swmm_NODE_ELEV -> {
            return (node!!.invertElev * UCF(LENGTH))
        }
        swmm_NODE_MAXDEPTH -> {
            return (node!!.fullDepth * UCF(LENGTH))
        }
        swmm_NODE_DEPTH -> {
            return (node!!.newDepth * UCF(LENGTH))
        }
        swmm_NODE_HEAD -> {
            return (((node!!.newDepth + node!!.invertElev)) * UCF(LENGTH))
        }
        swmm_NODE_VOLUME -> {
            return (node!!.newVolume * UCF(VOLUME))
        }
        swmm_NODE_LATFLOW -> {
            return (node!!.newLatFlow * UCF(FLOW))
        }
        swmm_NODE_INFLOW -> {
            return (node!!.inflow * UCF(FLOW))
        }
        swmm_NODE_OVERFLOW -> {
            return (node!!.overflow * UCF(FLOW))
        }
        swmm_NODE_RPTFLAG -> {
            return ((if (((node!!.rptFlag > 0))) 1 else 0)).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getNodeValue: no return")
}

// swmm5.c:1059
fun getLinkValue(property: Int, index: Int): Double {
    var link: TLink? = null
    if (((index < 0) || (index >= Nobjects!![LINK]))) {
        return 0.0
    }
    link = Link!![index]
    when (property) {
        swmm_LINK_TYPE -> {
            return (link!!.type).toDouble()
        }
        swmm_LINK_NODE1 -> {
            return (link!!.node1).toDouble()
        }
        swmm_LINK_NODE2 -> {
            return (link!!.node2).toDouble()
        }
        swmm_LINK_LENGTH -> {
            if ((link!!.type == CONDUIT)) {
                return (Conduit!![link!!.subIndex]!!.length * UCF(LENGTH))
            } else {
                return 0.0
            }
        }
        swmm_LINK_SLOPE -> {
            if ((link!!.type == CONDUIT)) {
                return Conduit!![link!!.subIndex]!!.slope
            } else {
                return 0.0
            }
        }
        swmm_LINK_FULLDEPTH -> {
            return (link!!.xsect!!.yFull * UCF(LENGTH))
        }
        swmm_LINK_FULLFLOW -> {
            return (link!!.qFull * UCF(FLOW))
        }
        swmm_LINK_FLOW -> {
            return ((link!!.newFlow * UCF(FLOW)) * (link!!.direction).toDouble())
        }
        swmm_LINK_VELOCITY -> {
            return (link_getVelocity(index, abs(link!!.newFlow), link!!.newDepth) * UCF(LENGTH))
        }
        swmm_LINK_DEPTH -> {
            return (link!!.newDepth * UCF(LENGTH))
        }
        swmm_LINK_TOPWIDTH -> {
            if ((link!!.type == CONDUIT)) {
                return (xsect_getWofY(link!!.xsect, link!!.newDepth) * UCF(LENGTH))
            } else {
                return 0.0
            }
        }
        swmm_LINK_SETTING -> {
            return link!!.setting
        }
        swmm_LINK_TIMEOPEN -> {
            if ((link!!.setting > 0.0)) {
                return (((getDateTime(NewRoutingTime) - link!!.timeLastSet)) * 24.0)
            } else {
                return 0.0
            }
        }
        swmm_LINK_TIMECLOSED -> {
            if ((link!!.setting == 0.0)) {
                return (((getDateTime(NewRoutingTime) - link!!.timeLastSet)) * 24.0)
            } else {
                return 0.0
            }
        }
        swmm_LINK_RPTFLAG -> {
            return ((if (((link!!.rptFlag > 0))) 1 else 0)).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getLinkValue: no return")
}

// swmm5.c:1126
fun getSystemValue(property: Int): Double {
    when (property) {
        swmm_STARTDATE -> {
            return StartDateTime
        }
        swmm_CURRENTDATE -> {
            return (StartDateTime + ElapsedTime)
        }
        swmm_ELAPSEDTIME -> {
            return ElapsedTime
        }
        swmm_ROUTESTEP -> {
            return RouteStep
        }
        swmm_MAXROUTESTEP -> {
            return getMaxRouteStep()
        }
        swmm_REPORTSTEP -> {
            return (ReportStep).toDouble()
        }
        swmm_TOTALSTEPS -> {
            return (Nperiods).toDouble()
        }
        swmm_NOREPORT -> {
            return (RptFlags!!.disabled).toDouble()
        }
        swmm_FLOWUNITS -> {
            return (FlowUnits).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getSystemValue: no return")
}

// swmm5.c:1159
fun setNodeLatFlow(index: Int, value_: Double) {
    if (((index < 0) || (index >= Nobjects!![NODE]))) {
        return
    }
    Node!![index]!!.apiExtInflow = (value_ / UCF(FLOW))
}

// swmm5.c:1173
fun setOutfallStage(index: Int, value_: Double) {
    var node: TNode? = null
    if (((index < 0) || (index >= Nobjects!![NODE]))) {
        return
    }
    node = Node!![index]
    if ((node!!.type != OUTFALL)) {
        return
    }
    Outfall!![node!!.subIndex]!!.fixedStage = (value_ / UCF(LENGTH))
    Outfall!![node!!.subIndex]!!.type = FIXED_OUTFALL
}

// swmm5.c:1192
fun setLinkSetting(index: Int, __value_: Double) {
    var value_: Double = __value_
    var link: TLink? = null
    if (((index < 0) || (index >= Nobjects!![LINK]))) {
        return
    }
    link = Link!![index]
    if (((value_ < 0.0) || (link!!.type == CONDUIT))) {
        return
    }
    if (((link!!.type != PUMP) && (value_ > 1.0))) {
        value_ = 1.0
    }
    if ((link!!.targetSetting == value_)) {
        return
    }
    link!!.targetSetting = value_
    if (((link!!.targetSetting * link!!.setting) == 0.0)) {
        link!!.timeLastSet = (StartDateTime + ElapsedTime)
    }
    link_setSetting(index, 0.0)
}

// swmm5.c:1217
fun getSavedDate(period: Int): Double {
    val days = DBox(0.0)
    output_readDateTime((period).toLong(), days)
    return days.v
}

// swmm5.c:1230
fun getSavedSubcatchValue(property: Int, index: Int, period: Int): Double {
    var outIndex: Int = (Subcatch!![index]!!.rptFlag - 1)
    if ((outIndex < 0)) {
        return 0.0
    }
    output_readSubcatchResults((period).toLong(), outIndex)
    when (property) {
        swmm_SUBCATCH_RAINFALL -> {
            return (SubcatchResults!![SUBCATCH_RAINFALL]).toDouble()
        }
        swmm_SUBCATCH_EVAP -> {
            return (SubcatchResults!![SUBCATCH_EVAP]).toDouble()
        }
        swmm_SUBCATCH_INFIL -> {
            return (SubcatchResults!![SUBCATCH_INFIL]).toDouble()
        }
        swmm_SUBCATCH_RUNOFF -> {
            return (SubcatchResults!![SUBCATCH_RUNOFF]).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getSavedSubcatchValue: no return")
}

// swmm5.c:1265
fun getSavedNodeValue(property: Int, index: Int, period: Int): Double {
    var outIndex: Int = (Node!![index]!!.rptFlag - 1)
    if ((outIndex < 0)) {
        return 0.0
    }
    output_readNodeResults((period).toLong(), outIndex)
    when (property) {
        swmm_NODE_DEPTH -> {
            return (NodeResults!![NODE_DEPTH]).toDouble()
        }
        swmm_NODE_HEAD -> {
            return (NodeResults!![NODE_HEAD]).toDouble()
        }
        swmm_NODE_VOLUME -> {
            return (NodeResults!![NODE_VOLUME]).toDouble()
        }
        swmm_NODE_LATFLOW -> {
            return (NodeResults!![NODE_LATFLOW]).toDouble()
        }
        swmm_NODE_INFLOW -> {
            return (NodeResults!![NODE_INFLOW]).toDouble()
        }
        swmm_NODE_OVERFLOW -> {
            return (NodeResults!![NODE_OVERFLOW]).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getSavedNodeValue: no return")
}

// swmm5.c:1304
fun getSavedLinkValue(property: Int, index: Int, period: Int): Double {
    var y: Double = 0.0
    var w: Double = 0.0
    var outIndex: Int = (Link!![index]!!.rptFlag - 1)
    if ((outIndex < 0)) {
        return 0.0
    }
    output_readLinkResults((period).toLong(), outIndex)
    when (property) {
        swmm_LINK_FLOW -> {
            return (LinkResults!![LINK_FLOW]).toDouble()
        }
        swmm_LINK_DEPTH -> {
            return (LinkResults!![LINK_DEPTH]).toDouble()
        }
        swmm_LINK_VELOCITY -> {
            return (LinkResults!![LINK_VELOCITY]).toDouble()
        }
        swmm_LINK_TOPWIDTH -> {
            y = ((LinkResults!![LINK_DEPTH]).toDouble() / UCF(LENGTH))
            w = xsect_getWofY(Link!![index]!!.xsect, y)
            return (w * UCF(LENGTH))
        }
        swmm_LINK_SETTING -> {
            return (LinkResults!![LINK_CAPACITY]).toDouble()
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getSavedLinkValue: no return")
}

// swmm5.c:1345
fun getMaxRouteStep(): Double {
    var tmpCourantFactor: Double = CourantFactor.v
    var result: Double = RouteStep
    if (((!(IsStartedFlag != 0)) || (RouteModel != DW))) {
        return result
    }
    CourantFactor.v = 1.0
    result = routing_getRoutingStep(RouteModel, MinRouteStep.v)
    CourantFactor.v = tmpCourantFactor
    return result
}

// swmm5.c:1360
fun setRoutingStep(__value_: Double) {
    var value_: Double = __value_
    if ((value_ <= 0.0)) {
        return
    }
    if ((value_ <= MinRouteStep.v)) {
        value_ = MinRouteStep.v
    }
    CourantFactor.v = 0.0
    RouteStep = value_
}

// swmm5.c:1378
fun UCF(u: Int): Double {
    if ((u < FLOW)) {
        return Ucf!![u]!![UnitSystem]
    } else {
        return Qcf!![FlowUnits]
    }
    throw IllegalStateException("UCF: no return")
}

// swmm5.c:1453
fun strcomp(s1: CPtr?, s2: CPtr?): Int {
    var i: Int = 0
    i = 0
    f1@ while ((((if (((((s1!!.g(i)) >= 97) && ((s1!!.g(i)) <= 122)))) (((s1!!.g(i)) and 32.inv())) else (s1!!.g(i)))) == ((if (((((s2!!.g(i)) >= 97) && ((s2!!.g(i)) <= 122)))) (((s2!!.g(i)) and 32.inv())) else (s2!!.g(i)))))) {
        if (((!(s1!!.g((i + 1)) != 0)) && (!(s2!!.g((i + 1)) != 0)))) {
            return (1)
        }
        i++
    }
    return (0)
}

// swmm5.c:1518
fun getElapsedTime(aDate: Double, days: IRef?, hrs: IRef?, mins: IRef?) {
    var x: Double = 0.0
    val secs = IBox(0)
    x = (aDate - ReportStart)
    if ((x <= 0.0)) {
        days!!.set(0)
        hrs!!.set(0)
        mins!!.set(0)
    } else {
        days!!.set(R.D2I(x))
        datetime_decodeTime(x, hrs, mins, secs)
    }
}

// swmm5.c:1543
fun getDateTime(elapsedMsec: Double): Double {
    return datetime_addSeconds(StartDateTime, (((elapsedMsec + 1.0)) / 1000.0))
}

// swmm5.c:1556
fun isRelativePath(fname: CPtr?): Int {
    if ((R.strchr(fname, 58) != null)) {
        return 0
    }
    if ((fname!!.g(0) == 92)) {
        return 0
    }
    if ((fname!!.g(0) == 47)) {
        return 0
    }
    return 1
}

// swmm5.c:1617
fun addAbsolutePath(fname: CPtr?): CPtr? {
    var n: Long = 0L
    var buffer: CPtr? = R.buf(259)
    if ((isRelativePath(fname) != 0)) {
        n = (R.snprintf(buffer, (259L).toInt(), __L649, InpDir, fname)).toLong()
        if ((n > 0L)) {
            sstrncpy(fname, buffer, 259L)
        }
    }
    return fname
}

// swmm5.c:1638
fun writecon(s: CPtr?) {
    R.fprintf(R.stdout, __L169, s)
    R.fflush(R.stdout)
}

