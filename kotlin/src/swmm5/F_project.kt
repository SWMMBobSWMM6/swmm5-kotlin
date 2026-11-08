// Generated from EPA SWMM 5.2.4 (project.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_project")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var Htable: Array<Array<HTentry?>?>? = arrayOfNulls<Array<HTentry?>>(18)
@JvmField var MemPoolAllocated: Int = 0

// project.c:70
fun omp_get_max_threads(): Int {
    return 1
}

// project.c:113
fun project_open(f1: CPtr?, f2: CPtr?, f3: CPtr?) {
    initPointers()
    setDefaults()
    openFiles(f1, f2, f3)
}

// project.c:129
fun project_readInput() {
    createHashTables()
    input_countObjects()
    createObjects()
    input_readData()
    if ((ErrorCode != 0)) {
        return
    }
    StartDateTime = (StartDate.v + StartTime.v)
    EndDateTime = (EndDate.v + EndTime.v)
    ReportStart = (ReportStartDate.v + ReportStartTime.v)
    ReportStart = ((if ((((ReportStart) >= (StartDateTime)))) (ReportStart) else (StartDateTime)))
    if ((EndDateTime <= StartDateTime)) {
        report_writeErrorMsg(ERR_START_DATE, __L0)
    } else if ((EndDateTime <= ReportStart)) {
        report_writeErrorMsg(ERR_REPORT_DATE, __L0)
    } else {
        TotalDuration = floor((((EndDateTime - StartDateTime)) * 86400.0))
        if (((ReportStep).toDouble() > TotalDuration)) {
            ReportStep = R.D2I((TotalDuration))
        }
        if (((ReportStep).toDouble() < RouteStep)) {
            report_writeErrorMsg(ERR_REPORT_STEP, __L0)
        }
        TotalDuration *= 1000.0
    }
}

// project.c:186
fun project_validate() {
    var i: Int = 0
    var j: Int = 0
    var err: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![CURVE])) {
        err = table_validate(Curve!![i])
        if ((err != 0)) {
            report_writeErrorMsg(ERR_CURVE_SEQUENCE, Curve!![i]!!.ID)
        }
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![TSERIES])) {
        err = table_validate(Tseries!![i])
        if ((err != 0)) {
            report_writeTseriesErrorMsg(err, Tseries!![i])
        }
        i++
    }
    climate_validate()
    lid_validate()
    if ((Nobjects!![SNOWMELT] == 0)) {
        IgnoreSnowmelt = 1
    }
    if ((Nobjects!![AQUIFER] == 0)) {
        IgnoreGwater = 1
    }
    i = 0
    f3@ while ((i < Nobjects!![AQUIFER])) {
        gwater_validateAquifer(i)
        i++
    }
    i = 0
    f4@ while ((i < Nobjects!![SUBCATCH])) {
        subcatch_validate(i)
        i++
    }
    i = 0
    f5@ while ((i < Nobjects!![GAGE])) {
        gage_validate(i)
        i++
    }
    i = 0
    f6@ while ((i < Nobjects!![SNOWMELT])) {
        snow_validateSnowmelt(i)
        i++
    }
    j = 0
    i = 0
    f7@ while ((i < Nobjects!![CURVE])) {
        if ((Curve!![i]!!.curveType == SHAPE_CURVE)) {
            Curve!![i]!!.refersTo = j
            Shape!![j]!!.curve = i
            if (!(shape_validate(Shape!![j], Curve!![i]) != 0)) {
                report_writeErrorMsg(ERR_CURVE_SEQUENCE, Curve!![i]!!.ID)
            }
            j++
        }
        i++
    }
    i = 0
    f8@ while ((i < Nobjects!![NODE])) {
        Node!![i]!!.oldDepth = Node!![i]!!.fullDepth
        i++
    }
    i = 0
    f9@ while ((i < Nobjects!![LINK])) {
        link_validate(i)
        i++
    }
    i = 0
    f10@ while ((i < Nobjects!![NODE])) {
        node_validate(i)
        i++
    }
    if ((DryStep < WetStep)) {
        report_writeWarningMsg(__L319, __L0)
        DryStep = WetStep
    }
    if ((RouteStep > (WetStep).toDouble())) {
        report_writeWarningMsg(__L320, __L0)
        RouteStep = (WetStep).toDouble()
    }
    if ((RptFlags!!.subcatchments == ALL)) {
        i = 0
        f11@ while ((i < Nobjects!![SUBCATCH])) {
            Subcatch!![i]!!.rptFlag = 1
            i++
        }
    }
    if ((RptFlags!!.nodes == ALL)) {
        i = 0
        f12@ while ((i < Nobjects!![NODE])) {
            Node!![i]!!.rptFlag = 1
            i++
        }
    }
    if ((RptFlags!!.links == ALL)) {
        i = 0
        f13@ while ((i < Nobjects!![LINK])) {
            Link!![i]!!.rptFlag = 1
            i++
        }
    }
    if ((RouteModel == DW)) {
        dynwave_validate()
    }
    inlet_validate()
    if ((NumThreads == 0)) {
        NumThreads = omp_get_max_threads()
    } else {
        NumThreads = ((if ((((NumThreads) <= (omp_get_max_threads())))) (NumThreads) else (omp_get_max_threads())))
    }
    if ((Nobjects!![LINK] < (4 * NumThreads))) {
        NumThreads = 1
    }
}

// project.c:274
fun project_close() {
    deleteObjects()
    deleteHashTables()
}

// project.c:287
fun project_init(): Int {
    var j: Int = 0
    var k: Int = 0
    climate_initState()
    lid_initState()
    j = 0
    f1@ while ((j < Nobjects!![TSERIES])) {
        table_tseriesInit(Tseries!![j])
        j++
    }
    j = 0
    f2@ while ((j < Nobjects!![GAGE])) {
        gage_initState(j)
        j++
    }
    k = 1
    j = 0
    f3@ while ((j < Nobjects!![SUBCATCH])) {
        subcatch_initState(j)
        if ((Subcatch!![j]!!.rptFlag > 0)) {
            Subcatch!![j]!!.rptFlag = k
            k++
        }
        j++
    }
    k = 1
    j = 0
    f4@ while ((j < Nobjects!![NODE])) {
        node_initState(j)
        if ((Node!![j]!!.rptFlag > 0)) {
            Node!![j]!!.rptFlag = k
            k++
        }
        j++
    }
    k = 1
    j = 0
    f5@ while ((j < Nobjects!![LINK])) {
        link_initState(j)
        if ((Link!![j]!!.rptFlag > 0)) {
            Link!![j]!!.rptFlag = k
            k++
        }
        j++
    }
    return ErrorCode
}

// project.c:334
fun project_addObject(type: Int, id: CPtr?, n: Int): Int {
    var result: Int = 0
    var len: Long = 0L
    var newID: CPtr? = null
    if ((project_findObject(type, id) >= 0)) {
        return 0
    }
    len = R.strlen(id)
    newID = Alloc((((len + 1L)) * 1L))
    sstrncpy(newID, id, len)
    result = HTinsert(Htable!![type], newID, n)
    if ((result == 0)) {
        result = (-1)
    }
    return result
}

// project.c:364
fun project_findObject(type: Int, id: CPtr?): Int {
    return HTfind(Htable!![type], id)
}

// project.c:377
fun project_findID(type: Int, id: CPtr?): CPtr? {
    return HTfindKey(Htable!![type], id)
}

// project.c:445
fun project_readOption(s1: CPtr?, s2: CPtr?): Int {
    var k: Int = 0
    val m = IBox(0)
    val h = IBox(0)
    val s = IBox(0)
    val tStep = DBox(0.0)
    var strDate: CPtr? = R.buf(25)
    val aTime = DBox(0.0)
    val aDate = DBox(0.0)
    k = findmatch(s1, OptionWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, s1)
    }
    when (k) {
        FLOW_UNITS -> {
            m.v = findmatch(s2, FlowUnitWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            FlowUnits = m.v
            if ((FlowUnits <= MGD)) {
                UnitSystem = US
            } else {
                UnitSystem = SI
            }
        }
        INFIL_MODEL -> {
            m.v = findmatch(s2, InfilModelWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            InfilModel = m.v
        }
        ROUTE_MODEL -> {
            m.v = findmatch(s2, RouteModelWords)
            if ((m.v < 0)) {
                m.v = findmatch(s2, OldRouteModelWords)
            }
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            if ((m.v == NO_ROUTING)) {
                IgnoreRouting = 1
            } else {
                RouteModel = m.v
            }
            if ((RouteModel == EKW)) {
                RouteModel = KW
            }
        }
        START_DATE -> {
            if (!(datetime_strToDate(s2, StartDate) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
        }
        START_TIME -> {
            if (!(datetime_strToTime(s2, StartTime) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
        }
        END_DATE -> {
            if (!(datetime_strToDate(s2, EndDate) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
        }
        END_TIME -> {
            if (!(datetime_strToTime(s2, EndTime) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
        }
        REPORT_START_DATE -> {
            if (!(datetime_strToDate(s2, ReportStartDate) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
        }
        REPORT_START_TIME -> {
            if (!(datetime_strToTime(s2, ReportStartTime) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
        }
        SWEEP_START, SWEEP_END -> {
            sstrncpy(strDate, s2, 24L)
            sstrcat(strDate, __L35, 25L)
            if (!(datetime_strToDate(strDate, aDate) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
            m.v = datetime_dayOfYear(aDate.v)
            if ((k == SWEEP_START)) {
                SweepStart = m.v
            } else {
                SweepEnd = m.v
            }
        }
        START_DRY_DAYS -> {
            StartDryDays = R.atof(s2)
            if ((StartDryDays < 0.0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
        }
        WET_STEP, DRY_STEP, REPORT_STEP, RULE_STEP -> {
            if (!(datetime_strToTime(s2, aTime) != 0)) {
                return error_setInpError(ERR_DATETIME, s2)
            }
            datetime_decodeTime(aTime.v, h, m, s)
            h.v += (24 * R.D2I(aTime.v))
            s.v = ((s.v + (60 * m.v)) + (3600 * h.v))
            if ((k == RULE_STEP)) {
                if ((s.v < 0)) {
                    return error_setInpError(ERR_NUMBER, s2)
                }
            } else if ((s.v <= 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            when (k) {
                WET_STEP -> {
                    WetStep = s.v
                }
                DRY_STEP -> {
                    DryStep = s.v
                }
                REPORT_STEP -> {
                    ReportStep = s.v
                }
                RULE_STEP -> {
                    RuleStep = s.v
                }
            }
        }
        INERT_DAMPING -> {
            m.v = findmatch(s2, InertDampingWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            } else {
                InertDamping = m.v
            }
        }
        ALLOW_PONDING, SLOPE_WEIGHTING, SKIP_STEADY_STATE, IGNORE_RAINFALL, IGNORE_SNOWMELT, IGNORE_GWATER, IGNORE_ROUTING, IGNORE_QUALITY, IGNORE_RDII -> {
            m.v = findmatch(s2, NoYesWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            when (k) {
                ALLOW_PONDING -> {
                    AllowPonding = m.v
                }
                SLOPE_WEIGHTING -> {
                    SlopeWeighting = m.v
                }
                SKIP_STEADY_STATE -> {
                    SkipSteadyState = m.v
                }
                IGNORE_RAINFALL -> {
                    IgnoreRainfall = m.v
                }
                IGNORE_SNOWMELT -> {
                    IgnoreSnowmelt = m.v
                }
                IGNORE_GWATER -> {
                    IgnoreGwater = m.v
                }
                IGNORE_ROUTING -> {
                    IgnoreRouting = m.v
                }
                IGNORE_QUALITY -> {
                    IgnoreQuality = m.v
                }
                IGNORE_RDII -> {
                    IgnoreRDII = m.v
                }
            }
        }
        NORMAL_FLOW_LTD -> {
            m.v = findmatch(s2, NormalFlowWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            NormalFlowLtd = m.v
        }
        FORCE_MAIN_EQN -> {
            m.v = findmatch(s2, ForceMainEqnWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            ForceMainEqn = m.v
        }
        LINK_OFFSETS -> {
            m.v = findmatch(s2, LinkOffsetWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            LinkOffsets = m.v
        }
        COMPATIBILITY -> {
            if ((strcomp(s2, __L321) != 0)) {
                Compatibility = SWMM3
            } else if ((strcomp(s2, __L322) != 0)) {
                Compatibility = SWMM4
            } else if ((strcomp(s2, __L323) != 0)) {
                Compatibility = SWMM5
            } else {
                return error_setInpError(ERR_KEYWORD, s2)
            }
        }
        ROUTE_STEP, LENGTHENING_STEP -> {
            if (!(getDouble(s2, tStep) != 0)) {
                if (!(datetime_strToTime(s2, aTime) != 0)) {
                    return error_setInpError(ERR_NUMBER, s2)
                } else {
                    datetime_decodeTime(aTime.v, h, m, s)
                    h.v += (24 * R.D2I(aTime.v))
                    s.v = ((s.v + (60 * m.v)) + (3600 * h.v))
                    tStep.v = (s.v).toDouble()
                }
            }
            if ((k == ROUTE_STEP)) {
                if ((tStep.v <= 0.0)) {
                    return error_setInpError(ERR_NUMBER, s2)
                }
                RouteStep = tStep.v
            } else {
                LengtheningStep = ((if ((((0.0) >= (tStep.v)))) (0.0) else (tStep.v)))
            }
        }
        MIN_ROUTE_STEP -> {
            if (((!(getDouble(s2, MinRouteStep) != 0)) || (MinRouteStep.v < 0.0))) {
                return error_setInpError(ERR_NUMBER, s2)
            }
        }
        NUM_THREADS -> {
            m.v = R.atoi(s2)
            if ((m.v < 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            NumThreads = m.v
        }
        VARIABLE_STEP -> {
            if (!(getDouble(s2, CourantFactor) != 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            if (((CourantFactor.v < 0.0) || (CourantFactor.v > 2.0))) {
                return error_setInpError(ERR_NUMBER, s2)
            }
        }
        MIN_SURFAREA -> {
            if (!(getDouble(s2, MinSurfArea) != 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            if ((MinSurfArea.v < 0.0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
        }
        MIN_SLOPE -> {
            if (!(getDouble(s2, MinSlope) != 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            if (((MinSlope.v < 0.0) || (MinSlope.v >= 100.0))) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            MinSlope.v /= 100.0
        }
        MAX_TRIALS -> {
            m.v = R.atoi(s2)
            if ((m.v < 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            MaxTrials = m.v
        }
        HEAD_TOL -> {
            if (!(getDouble(s2, HeadTol) != 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
        }
        SYS_FLOW_TOL -> {
            if (!(getDouble(s2, SysFlowTol) != 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            SysFlowTol.v /= 100.0
        }
        LAT_FLOW_TOL -> {
            if (!(getDouble(s2, LatFlowTol) != 0)) {
                return error_setInpError(ERR_NUMBER, s2)
            }
            LatFlowTol.v /= 100.0
        }
        SURCHARGE_METHOD -> {
            m.v = findmatch(s2, SurchargeWords)
            if ((m.v < 0)) {
                return error_setInpError(ERR_KEYWORD, s2)
            }
            SurchargeMethod = m.v
        }
        TEMPDIR -> {
            sstrncpy(TempDir, s2, 259L)
        }
    }
    return 0
}

// project.c:773
fun initPointers() {
    Gage = (null)
    Subcatch = (null)
    Node = (null)
    Outfall = (null)
    Divider = (null)
    Storage = (null)
    Link = (null)
    Conduit = (null)
    Pump = (null)
    Orifice = (null)
    Weir = (null)
    Outlet = (null)
    Pollut = (null)
    Landuse = (null)
    Pattern = (null)
    Curve = (null)
    Tseries = (null)
    Transect = (null)
    Shape = (null)
    Aquifer = (null)
    UnitHyd = (null)
    Snowmelt = (null)
    Event = (null)
    MemPoolAllocated = 0
}

// project.c:808
fun setDefaults() {
    var i: Int = 0
    var j: Int = 0
    i = 0
    f1@ while ((i < 3)) {
        sstrncpy(Title!![i], __L0, 0L)
        i++
    }
    sstrncpy(TempDir, __L0, 0L)
    Frain!!.mode = SCRATCH_FILE
    Fclimate!!.mode = NO_FILE
    Frunoff!!.mode = NO_FILE
    Frdii!!.mode = NO_FILE
    Fhotstart1!!.mode = NO_FILE
    Fhotstart2!!.mode = NO_FILE
    Finflows!!.mode = NO_FILE
    Foutflows!!.mode = NO_FILE
    Frain!!.file = (null)
    Fclimate!!.file = (null)
    Frunoff!!.file = (null)
    Frdii!!.file = (null)
    Fhotstart1!!.file = (null)
    Fhotstart2!!.file = (null)
    Finflows!!.file = (null)
    Foutflows!!.file = (null)
    Fout!!.file = (null)
    Fout!!.mode = NO_FILE
    UnitSystem = US
    FlowUnits = CFS
    InfilModel = HORTON
    RouteModel = DW
    SurchargeMethod = EXTRAN
    CrownCutoff = 0.96
    AllowPonding = 0
    InertDamping = PARTIAL_DAMPING
    NormalFlowLtd = BOTH
    ForceMainEqn = H_W
    LinkOffsets = DEPTH_OFFSET
    LengtheningStep = 0.0
    CourantFactor.v = 0.75
    MinSurfArea.v = 0.0
    MinSlope.v = 0.0
    SkipSteadyState = 0
    IgnoreRainfall = 0
    IgnoreRDII = 0
    IgnoreSnowmelt = 0
    IgnoreGwater = 0
    IgnoreRouting = 0
    IgnoreQuality = 0
    WetStep = 300
    DryStep = 3600
    RuleStep = 0
    RouteStep = 20.0
    MinRouteStep.v = 0.5
    ReportStep = 900
    StartDryDays = 0.0
    MaxTrials = 0
    HeadTol.v = 0.0
    SysFlowTol.v = 0.05
    LatFlowTol.v = 0.05
    NumThreads = 1
    NumEvents = 0
    SlopeWeighting = 1
    Compatibility = SWMM4
    StartDate.v = datetime_encodeDate(2004, 1, 1)
    StartTime.v = datetime_encodeTime(0, 0, 0)
    StartDateTime = (StartDate.v + StartTime.v)
    EndDate.v = StartDate.v
    EndTime.v = 0.0
    ReportStartDate.v = ((-693594)).toDouble()
    ReportStartTime.v = ((-693594)).toDouble()
    SweepStart = 1
    SweepEnd = 365
    RptFlags!!.disabled = 0
    RptFlags!!.input = 0
    RptFlags!!.continuity = 1
    RptFlags!!.flowStats = 1
    RptFlags!!.controls = 0
    RptFlags!!.subcatchments = 0
    RptFlags!!.nodes = 0
    RptFlags!!.links = 0
    RptFlags!!.averages = 0
    Temp!!.dataSource = NO_TEMP
    Temp!!.tSeries = (-1)
    Temp!!.ta = 70.0
    Temp!!.elev = 0.0
    Temp!!.anglat = 40.0
    Temp!!.dtlong = 0.0
    Temp!!.tmax = (-10000000000.0)
    Wind!!.type = MONTHLY_WIND
    i = 0
    f2@ while ((i < 12)) {
        Wind!!.aws!![i] = 0.0
        i++
    }
    Snow!!.snotmp = 34.0
    Snow!!.tipm = 0.5
    Snow!!.rnm = 0.6
    i = 0
    f3@ while ((i < 2)) {
        j = 0
        f4@ while ((j < 10)) {
            Snow!!.adc!![i]!![j] = 1.0
            j++
        }
        i++
    }
    Evap!!.type = CONSTANT_EVAP
    i = 0
    f5@ while ((i < 12)) {
        Evap!!.monthlyEvap!![i] = 0.0
        Evap!!.panCoeff!![i] = 1.0
        i++
    }
    Evap!!.recoveryPattern = (-1)
    Evap!!.recoveryFactor = 1.0
    Evap!!.tSeries = (-1)
    Evap!!.dryOnly = 0
    i = 0
    f6@ while ((i < 12)) {
        Adjust!!.temp!![i] = 0.0
        Adjust!!.evap!![i] = 0.0
        Adjust!!.rain!![i] = 1.0
        Adjust!!.hydcon!![i] = 1.0
        i++
    }
    Adjust!!.rainFactor = 1.0
    Adjust!!.hydconFactor = 1.0
}

// project.c:954
fun openFiles(f1: CPtr?, f2: CPtr?, f3: CPtr?) {
    Finp!!.file = (null)
    Frpt!!.file = (null)
    Fout!!.file = (null)
    sstrncpy(Finp!!.name, f1, 259L)
    sstrncpy(Frpt!!.name, f2, 259L)
    sstrncpy(Fout!!.name, f3, 259L)
    if ((((strcomp(f1, f2) != 0) || (strcomp(f1, f3) != 0)) || (strcomp(f2, f3) != 0))) {
        writecon(__L324)
        ErrorCode = ERR_FILE_NAME
        return
    }
    if ((((R.fopen(f1, __L13)).also { __v -> Finp!!.file = __v }) == null)) {
        writecon(__L325)
        writecon(f1)
        ErrorCode = ERR_INP_FILE
        return
    }
    if ((((R.fopen(f2, __L171)).also { __v -> Frpt!!.file = __v }) == null)) {
        writecon(__L326)
        ErrorCode = ERR_RPT_FILE
        return
    }
}

// project.c:999
fun createObjects() {
    var j: Int = 0
    var k: Int = 0
    if ((ErrorCode != 0)) {
        return
    }
    Gage = Array<TGage?>(((Nobjects!![GAGE]).toLong()).toInt()) { TGage() }
    Subcatch = Array<TSubcatch?>(((Nobjects!![SUBCATCH]).toLong()).toInt()) { TSubcatch() }
    Node = Array<TNode?>(((Nobjects!![NODE]).toLong()).toInt()) { TNode() }
    Outfall = Array<TOutfall?>(((Nnodes!![OUTFALL]).toLong()).toInt()) { TOutfall() }
    Divider = Array<TDivider?>(((Nnodes!![DIVIDER]).toLong()).toInt()) { TDivider() }
    Storage = Array<TStorage?>(((Nnodes!![STORAGE]).toLong()).toInt()) { TStorage() }
    Link = Array<TLink?>(((Nobjects!![LINK]).toLong()).toInt()) { TLink() }
    Conduit = Array<TConduit?>(((Nlinks!![CONDUIT]).toLong()).toInt()) { TConduit() }
    Pump = Array<TPump?>(((Nlinks!![PUMP]).toLong()).toInt()) { TPump() }
    Orifice = Array<TOrifice?>(((Nlinks!![ORIFICE]).toLong()).toInt()) { TOrifice() }
    Weir = Array<TWeir?>(((Nlinks!![WEIR]).toLong()).toInt()) { TWeir() }
    Outlet = Array<TOutlet?>(((Nlinks!![OUTLET]).toLong()).toInt()) { TOutlet() }
    Pollut = Array<TPollut?>(((Nobjects!![POLLUT]).toLong()).toInt()) { TPollut() }
    Landuse = Array<TLanduse?>(((Nobjects!![LANDUSE]).toLong()).toInt()) { TLanduse() }
    Pattern = Array<TPattern?>(((Nobjects!![TIMEPATTERN]).toLong()).toInt()) { TPattern() }
    Curve = Array<TTable?>(((Nobjects!![CURVE]).toLong()).toInt()) { TTable() }
    Tseries = Array<TTable?>(((Nobjects!![TSERIES]).toLong()).toInt()) { TTable() }
    Aquifer = Array<TAquifer?>(((Nobjects!![AQUIFER]).toLong()).toInt()) { TAquifer() }
    UnitHyd = Array<TUnitHyd?>(((Nobjects!![UNITHYD]).toLong()).toInt()) { TUnitHyd() }
    Snowmelt = Array<TSnowmelt?>(((Nobjects!![SNOWMELT]).toLong()).toInt()) { TSnowmelt() }
    Shape = Array<TShape?>(((Nobjects!![SHAPE]).toLong()).toInt()) { TShape() }
    Event = Array<TEvent?>((((NumEvents).toLong() + 1L)).toInt()) { TEvent() }
    Event!![NumEvents]!!.start = 10000000000.0
    Event!![NumEvents]!!.end = (10000000000.0 + 1.0)
    lid_create(Nobjects!![LID], Nobjects!![SUBCATCH])
    ErrorCode = controls_create(Nobjects!![CONTROL])
    if ((ErrorCode != 0)) {
        return
    }
    ErrorCode = transect_create(Nobjects!![TRANSECT])
    if ((ErrorCode != 0)) {
        return
    }
    ErrorCode = street_create(Nobjects!![STREET])
    if ((ErrorCode != 0)) {
        return
    }
    ErrorCode = inlet_create(Nobjects!![INLET])
    if ((ErrorCode != 0)) {
        return
    }
    infil_create(Nobjects!![SUBCATCH])
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        Subcatch!![j]!!.initBuildup = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Subcatch!![j]!!.oldQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Subcatch!![j]!!.newQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Subcatch!![j]!!.pondedQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Subcatch!![j]!!.totalLoad = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        j++
    }
    j = 0
    f2@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.oldQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Node!![j]!!.newQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Node!![j]!!.extInflow = (null)
        Node!![j]!!.dwfInflow = (null)
        Node!![j]!!.rdiiInflow = (null)
        Node!![j]!!.treatment = (null)
        j++
    }
    j = 0
    f3@ while ((j < Nobjects!![LINK])) {
        Link!![j]!!.inlet = (null)
        Link!![j]!!.oldQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Link!![j]!!.newQual = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Link!![j]!!.totalLoad = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        j++
    }
    j = 0
    f4@ while ((j < Nobjects!![LANDUSE])) {
        Landuse!![j]!!.buildupFunc = Array<TBuildup?>(((Nobjects!![POLLUT]).toLong()).toInt()) { TBuildup() }
        Landuse!![j]!!.washoffFunc = Array<TWashoff?>(((Nobjects!![POLLUT]).toLong()).toInt()) { TWashoff() }
        j++
    }
    j = 0
    f5@ while ((j < Nobjects!![SUBCATCH])) {
        Subcatch!![j]!!.landFactor = Array<TLandFactor?>(((Nobjects!![LANDUSE]).toLong()).toInt()) { TLandFactor() }
        k = 0
        f6@ while ((k < Nobjects!![LANDUSE])) {
            Subcatch!![j]!!.landFactor!![k]!!.buildup = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
            k++
        }
        j++
    }
    j = 0
    f7@ while ((j < Nobjects!![LANDUSE])) {
        k = 0
        f8@ while ((k < Nobjects!![POLLUT])) {
            Landuse!![j]!!.buildupFunc!![k]!!.funcType = NO_BUILDUP
            Landuse!![j]!!.buildupFunc!![k]!!.normalizer = PER_AREA
            Landuse!![j]!!.washoffFunc!![k]!!.funcType = NO_WASHOFF
            k++
        }
        j++
    }
    j = 0
    f9@ while ((j < Nobjects!![GAGE])) {
        Gage!![j]!!.tSeries = (-1)
        sstrncpy(Gage!![j]!!.fname, __L0, 0L)
        j++
    }
    j = 0
    f10@ while ((j < Nobjects!![SUBCATCH])) {
        Subcatch!![j]!!.outSubcatch = (-1)
        Subcatch!![j]!!.outNode = (-1)
        Subcatch!![j]!!.infil = (-1)
        Subcatch!![j]!!.groundwater = (null)
        Subcatch!![j]!!.gwLatFlowExpr = (null)
        Subcatch!![j]!!.gwDeepFlowExpr = (null)
        Subcatch!![j]!!.snowpack = (null)
        Subcatch!![j]!!.lidArea = 0.0
        k = 0
        f11@ while ((k < Nobjects!![POLLUT])) {
            Subcatch!![j]!!.initBuildup!![k] = 0.0
            k++
        }
        j++
    }
    j = 0
    f12@ while ((j < Nobjects!![UNITHYD])) {
        rdii_initUnitHyd(j)
        j++
    }
    j = 0
    f13@ while ((j < Nobjects!![SNOWMELT])) {
        snow_initSnowmelt(j)
        j++
    }
    j = 0
    f14@ while ((j < Nnodes!![STORAGE])) {
        Storage!![j]!!.exfil = (null)
        j++
    }
    j = 0
    f15@ while ((j < Nobjects!![LINK])) {
        Link!![j]!!.xsect!!.type = (-1)
        Link!![j]!!.cLossInlet = 0.0
        Link!![j]!!.cLossOutlet = 0.0
        Link!![j]!!.cLossAvg = 0.0
        Link!![j]!!.hasFlapGate = 0
        j++
    }
    j = 0
    f16@ while ((j < Nlinks!![PUMP])) {
        Pump!![j]!!.pumpCurve = (-1)
        j++
    }
    j = 0
    f17@ while ((j < Nobjects!![SUBCATCH])) {
        Subcatch!![j]!!.rptFlag = 0
        j++
    }
    j = 0
    f18@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.rptFlag = 0
        j++
    }
    j = 0
    f19@ while ((j < Nobjects!![LINK])) {
        Link!![j]!!.rptFlag = 0
        j++
    }
    j = 0
    f20@ while ((j < Nobjects!![CURVE])) {
        table_init(Curve!![j])
        j++
    }
    j = 0
    f21@ while ((j < Nobjects!![TSERIES])) {
        table_init(Tseries!![j])
        j++
    }
    j = 0
    f22@ while ((j < Nobjects!![TIMEPATTERN])) {
        inflow_initDwfPattern(j)
        j++
    }
}

// project.c:1176
fun deleteObjects() {
    var j: Int = 0
    var k: Int = 0
    if ((Subcatch != null)) {
        j = 0
        f1@ while ((j < Nobjects!![SUBCATCH])) {
            k = 0
            f2@ while ((k < Nobjects!![LANDUSE])) {
                if ((Subcatch!![j]!!.landFactor!![k]!!.buildup != null)) {
                    R.free(Subcatch!![j]!!.landFactor!![k]!!.buildup)
                    Subcatch!![j]!!.landFactor!![k]!!.buildup = (null)
                }
                k++
            }
            if ((Subcatch!![j]!!.landFactor != null)) {
                R.free(Subcatch!![j]!!.landFactor)
                Subcatch!![j]!!.landFactor = (null)
            }
            if ((Subcatch!![j]!!.groundwater != null)) {
                R.free(Subcatch!![j]!!.groundwater)
                Subcatch!![j]!!.groundwater = (null)
            }
            gwater_deleteFlowExpression(j)
            if ((Subcatch!![j]!!.snowpack != null)) {
                R.free(Subcatch!![j]!!.snowpack)
                Subcatch!![j]!!.snowpack = (null)
            }
            j++
        }
    }
    if ((Landuse != null)) {
        j = 0
        f3@ while ((j < Nobjects!![LANDUSE])) {
            if ((Landuse!![j]!!.buildupFunc != null)) {
                R.free(Landuse!![j]!!.buildupFunc)
                Landuse!![j]!!.buildupFunc = (null)
            }
            if ((Landuse!![j]!!.washoffFunc != null)) {
                R.free(Landuse!![j]!!.washoffFunc)
                Landuse!![j]!!.washoffFunc = (null)
            }
            j++
        }
    }
    if ((Subcatch != null)) {
        j = 0
        f4@ while ((j < Nobjects!![SUBCATCH])) {
            if ((Subcatch!![j]!!.initBuildup != null)) {
                R.free(Subcatch!![j]!!.initBuildup)
                Subcatch!![j]!!.initBuildup = (null)
            }
            if ((Subcatch!![j]!!.oldQual != null)) {
                R.free(Subcatch!![j]!!.oldQual)
                Subcatch!![j]!!.oldQual = (null)
            }
            if ((Subcatch!![j]!!.newQual != null)) {
                R.free(Subcatch!![j]!!.newQual)
                Subcatch!![j]!!.newQual = (null)
            }
            if ((Subcatch!![j]!!.pondedQual != null)) {
                R.free(Subcatch!![j]!!.pondedQual)
                Subcatch!![j]!!.pondedQual = (null)
            }
            if ((Subcatch!![j]!!.totalLoad != null)) {
                R.free(Subcatch!![j]!!.totalLoad)
                Subcatch!![j]!!.totalLoad = (null)
            }
            j++
        }
    }
    if ((Node != null)) {
        j = 0
        f5@ while ((j < Nobjects!![NODE])) {
            if ((Node!![j]!!.oldQual != null)) {
                R.free(Node!![j]!!.oldQual)
                Node!![j]!!.oldQual = (null)
            }
            if ((Node!![j]!!.newQual != null)) {
                R.free(Node!![j]!!.newQual)
                Node!![j]!!.newQual = (null)
            }
            j++
        }
    }
    if ((Link != null)) {
        j = 0
        f6@ while ((j < Nobjects!![LINK])) {
            if ((Link!![j]!!.oldQual != null)) {
                R.free(Link!![j]!!.oldQual)
                Link!![j]!!.oldQual = (null)
            }
            if ((Link!![j]!!.newQual != null)) {
                R.free(Link!![j]!!.newQual)
                Link!![j]!!.newQual = (null)
            }
            if ((Link!![j]!!.totalLoad != null)) {
                R.free(Link!![j]!!.totalLoad)
                Link!![j]!!.totalLoad = (null)
            }
            j++
        }
    }
    infil_delete()
    if ((Node != null)) {
        j = 0
        f7@ while ((j < Nnodes!![STORAGE])) {
            if ((Storage!![j]!!.exfil != null)) {
                if ((Storage!![j]!!.exfil!!.btmExfil != null)) {
                    R.free(Storage!![j]!!.exfil!!.btmExfil)
                    Storage!![j]!!.exfil!!.btmExfil = (null)
                }
                if ((Storage!![j]!!.exfil!!.bankExfil != null)) {
                    R.free(Storage!![j]!!.exfil!!.bankExfil)
                    Storage!![j]!!.exfil!!.bankExfil = (null)
                }
                if ((Storage!![j]!!.exfil != null)) {
                    R.free(Storage!![j]!!.exfil)
                    Storage!![j]!!.exfil = (null)
                }
            }
            j++
        }
    }
    if ((Node != null)) {
        j = 0
        f8@ while ((j < Nnodes!![OUTFALL])) {
            if ((Outfall!![j]!!.wRouted != null)) {
                R.free(Outfall!![j]!!.wRouted)
                Outfall!![j]!!.wRouted = (null)
            }
            j++
        }
    }
    if ((Node != null)) {
        j = 0
        f9@ while ((j < Nobjects!![NODE])) {
            inflow_deleteExtInflows(j)
            inflow_deleteDwfInflows(j)
            rdii_deleteRdiiInflow(j)
            treatmnt_delete(j)
            j++
        }
    }
    if ((Tseries != null)) {
        j = 0
        f10@ while ((j < Nobjects!![TSERIES])) {
            table_deleteEntries(Tseries!![j])
            j++
        }
    }
    if ((Curve != null)) {
        j = 0
        f11@ while ((j < Nobjects!![CURVE])) {
            table_deleteEntries(Curve!![j])
            j++
        }
    }
    transect_delete()
    street_delete()
    inlet_delete()
    controls_delete()
    lid_delete()
    if ((Gage != null)) {
        R.free(Gage)
        Gage = (null)
    }
    if ((Subcatch != null)) {
        R.free(Subcatch)
        Subcatch = (null)
    }
    if ((Node != null)) {
        R.free(Node)
        Node = (null)
    }
    if ((Outfall != null)) {
        R.free(Outfall)
        Outfall = (null)
    }
    if ((Divider != null)) {
        R.free(Divider)
        Divider = (null)
    }
    if ((Storage != null)) {
        R.free(Storage)
        Storage = (null)
    }
    if ((Link != null)) {
        R.free(Link)
        Link = (null)
    }
    if ((Conduit != null)) {
        R.free(Conduit)
        Conduit = (null)
    }
    if ((Pump != null)) {
        R.free(Pump)
        Pump = (null)
    }
    if ((Orifice != null)) {
        R.free(Orifice)
        Orifice = (null)
    }
    if ((Weir != null)) {
        R.free(Weir)
        Weir = (null)
    }
    if ((Outlet != null)) {
        R.free(Outlet)
        Outlet = (null)
    }
    if ((Pollut != null)) {
        R.free(Pollut)
        Pollut = (null)
    }
    if ((Landuse != null)) {
        R.free(Landuse)
        Landuse = (null)
    }
    if ((Pattern != null)) {
        R.free(Pattern)
        Pattern = (null)
    }
    if ((Curve != null)) {
        R.free(Curve)
        Curve = (null)
    }
    if ((Tseries != null)) {
        R.free(Tseries)
        Tseries = (null)
    }
    if ((Aquifer != null)) {
        R.free(Aquifer)
        Aquifer = (null)
    }
    if ((UnitHyd != null)) {
        R.free(UnitHyd)
        UnitHyd = (null)
    }
    if ((Snowmelt != null)) {
        R.free(Snowmelt)
        Snowmelt = (null)
    }
    if ((Shape != null)) {
        R.free(Shape)
        Shape = (null)
    }
    if ((Event != null)) {
        R.free(Event)
        Event = (null)
    }
}

// project.c:1304
fun createHashTables() {
    var j: Int = 0
    MemPoolAllocated = 0
    j = 0
    f1@ while ((j < MAX_OBJ_TYPES)) {
        Htable!![j] = HTcreate()
        if ((Htable!![j] == null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
        }
        j++
    }
    if (((AllocInit()) == null)) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
    } else {
        MemPoolAllocated = 1
    }
}

// project.c:1325
fun deleteHashTables() {
    var j: Int = 0
    j = 0
    f1@ while ((j < MAX_OBJ_TYPES)) {
        if ((Htable!![j] != null)) {
            HTfree(Htable!![j])
        }
        j++
    }
    if ((MemPoolAllocated != 0)) {
        AllocFreePool()
    }
}

