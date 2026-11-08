// Generated from EPA SWMM 5.2.4 (runoff.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_runoff")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var IsRaining: Int = 0
@JvmField var HasRunoff: Int = 0
@JvmField var HasSnow: Int = 0
@JvmField val runoff_Nsteps = IBox(0)
@JvmField val MaxSteps = IBox(0)
@JvmField var MaxStepsPos: Long = 0L
@JvmField var HasWetLids: Int = 0
@JvmField var OutflowLoad: DoubleArray? = null

// runoff.c:82
fun runoff_open(): Int {
    IsRaining = 0
    HasRunoff = 0
    HasSnow = 0
    runoff_Nsteps.v = 0
    if (!(odesolve_open(4) != 0)) {
        report_writeErrorMsg(ERR_ODE_SOLVER, __L0)
    }
    OutflowLoad = (null)
    if ((Nobjects!![POLLUT] > 0)) {
        OutflowLoad = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        if (!(OutflowLoad != null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
        }
    }
    when (Frunoff!!.mode) {
        USE_FILE -> {
            if ((((R.fopen(Frunoff!!.name, __L165)).also { __v -> Frunoff!!.file = __v }) == null)) {
                report_writeErrorMsg(ERR_RUNOFF_FILE_OPEN, Frunoff!!.name)
            } else {
                runoff_initFile()
            }
        }
        SAVE_FILE -> {
            if ((((R.fopen(Frunoff!!.name, __L166)).also { __v -> Frunoff!!.file = __v }) == null)) {
                report_writeErrorMsg(ERR_RUNOFF_FILE_OPEN, Frunoff!!.name)
            } else {
                runoff_initFile()
            }
        }
    }
    return ErrorCode
}

// runoff.c:124
fun runoff_close() {
    odesolve_close()
    if ((OutflowLoad != null)) {
        R.free(OutflowLoad)
        OutflowLoad = (null)
    }
    if ((Frunoff!!.file != null)) {
        if ((Frunoff!!.mode == SAVE_FILE)) {
            R.fseek(Frunoff!!.file, MaxStepsPos, 0)
            R.fwrite1(runoff_Nsteps.v, 4, Frunoff!!.file)
        }
        R.fclose(Frunoff!!.file)
    }
    if ((Fclimate!!.file != null)) {
        R.fclose(Fclimate!!.file)
    }
}

// runoff.c:155
fun runoff_execute() {
    var j: Int = 0
    var day: Int = 0
    var runoffStep: Double = 0.0
    var oldRunoffStep: Double = 0.0
    var runoff: Double = 0.0
    var currentDate: Double = 0.0
    var canSweep: Int = 0
    if ((ErrorCode != 0)) {
        return
    }
    oldRunoffStep = (((NewRunoffTime - OldRunoffTime)) / 1000.0)
    currentDate = getDateTime(NewRunoffTime)
    climate_setState(currentDate)
    if ((Nobjects!![SUBCATCH] == 0)) {
        OldRunoffTime = NewRunoffTime
        NewRunoffTime += (((DryStep)).toDouble() * 1000.0)
        NewRunoffTime = ((if ((((NewRunoffTime) <= (TotalDuration)))) (NewRunoffTime) else (TotalDuration)))
        return
    }
    IsRaining = 0
    j = 0
    f1@ while ((j < Nobjects!![GAGE])) {
        gage_setState(j, currentDate)
        if ((Gage!![j]!!.rainfall > 0.0)) {
            IsRaining = 1
        }
        j++
    }
    if ((Frunoff!!.mode == USE_FILE)) {
        runoff_readFromFile()
        return
    }
    day = datetime_dayOfYear(currentDate)
    canSweep = 0
    if ((SweepStart <= SweepEnd)) {
        if (((day >= SweepStart) && (day <= SweepEnd))) {
            canSweep = 1
        }
    } else if (((day <= SweepEnd) || (day >= SweepStart))) {
        canSweep = 1
    }
    runoffStep = runoff_getTimeStep(currentDate)
    if ((runoffStep <= 0.0)) {
        ErrorCode = ERR_TIMESTEP
        return
    }
    OldRunoffTime = NewRunoffTime
    NewRunoffTime += ((1000.0 * runoffStep))
    if ((NewRunoffTime > TotalDuration)) {
        runoffStep = (((TotalDuration - OldRunoffTime)) / 1000.0)
        NewRunoffTime = TotalDuration
    }
    j = 0
    f2@ while ((j < Nobjects!![GAGE])) {
        gage_updatePastRain(j, R.D2I(runoffStep))
        j++
    }
    j = 0
    f3@ while ((j < Nobjects!![SUBCATCH])) {
        subcatch_setOldState(j)
        j++
    }
    if ((oldRunoffStep > 0.0)) {
        runoff_getOutfallRunon(oldRunoffStep)
    }
    j = 0
    f4@ while ((j < Nobjects!![SUBCATCH])) {
        if ((Subcatch!![j]!!.area == 0.0)) {
            j++
            continue@f4
        }
        subcatch_getRunon(j)
        if (!(IgnoreSnowmelt != 0)) {
            snow_plowSnow(j, runoffStep)
        }
        j++
    }
    HasSnow = 0
    HasRunoff = 0
    HasWetLids = 0
    j = 0
    f5@ while ((j < Nobjects!![SUBCATCH])) {
        if ((Subcatch!![j]!!.area == 0.0)) {
            j++
            continue@f5
        }
        runoff = subcatch_getRunoff(j, runoffStep)
        if ((runoff > 0.0)) {
            HasRunoff = 1
        }
        if ((Subcatch!![j]!!.newSnowDepth > 0.0)) {
            HasSnow = 1
        }
        if ((IgnoreQuality != 0)) {
            j++
            continue@f5
        }
        if ((runoff < 2.31481e-08)) {
            surfqual_getBuildup(j, runoffStep)
        }
        if (((canSweep != 0) && (Subcatch!![j]!!.rainfall <= 2.31481e-08))) {
            surfqual_sweepBuildup(j, currentDate)
        }
        surfqual_getWashoff(j, runoff, runoffStep)
        j++
    }
    stats_updateMaxRunoff()
    runoff_Nsteps.v++
    if ((Frunoff!!.mode == SAVE_FILE)) {
        runoff_saveToFile((runoffStep).toFloat())
    }
    j = 0
    f6@ while ((j < Nobjects!![SUBCATCH])) {
        Subcatch!![j]!!.runon = 0.0
        j++
    }
}

// runoff.c:299
fun runoff_getTimeStep(currentDate: Double): Double {
    var j: Int = 0
    var timeStep: Long = 0L
    var maxStep: Long = (DryStep).toLong()
    timeStep = datetime_timeDiff(climate_getNextEvapDate(), currentDate)
    if ((((timeStep).toDouble() > 0.0) && (timeStep < maxStep))) {
        maxStep = timeStep
    }
    j = 0
    f1@ while ((j < Nobjects!![GAGE])) {
        timeStep = datetime_timeDiff(gage_getNextRainDate(j, currentDate), currentDate)
        if (((timeStep > 0L) && (timeStep < maxStep))) {
            maxStep = timeStep
        }
        j++
    }
    if (((((IsRaining != 0) || (HasSnow != 0)) || (HasRunoff != 0)) || (HasWetLids != 0))) {
        timeStep = (WetStep).toLong()
    } else {
        timeStep = (DryStep).toLong()
    }
    if ((timeStep > maxStep)) {
        timeStep = maxStep
    }
    return (timeStep).toDouble()
}

// runoff.c:335
fun runoff_initFile() {
    val nSubcatch = IBox(0)
    val nPollut = IBox(0)
    val flowUnits = IBox(0)
    var fileStamp: CPtr? = R.bufFrom(__L533, 13)
    var fStamp: CPtr? = R.bufFrom(__L533, 13)
    MaxSteps.v = 0
    if ((Frunoff!!.mode == SAVE_FILE)) {
        nSubcatch.v = Nobjects!![SUBCATCH]
        nPollut.v = Nobjects!![POLLUT]
        flowUnits.v = FlowUnits
        R.fwrite(fileStamp, 0, (R.strlen(fileStamp)).toInt(), Frunoff!!.file)
        R.fwrite1(nSubcatch.v, 4, Frunoff!!.file)
        R.fwrite1(nPollut.v, 4, Frunoff!!.file)
        R.fwrite1(flowUnits.v, 4, Frunoff!!.file)
        MaxStepsPos = R.ftell(Frunoff!!.file)
        R.fwrite1(MaxSteps.v, 4, Frunoff!!.file)
    }
    if ((Frunoff!!.mode == USE_FILE)) {
        R.fread(fStamp, 0, (R.strlen(fileStamp)).toInt(), Frunoff!!.file)
        if ((R.strcmp(fStamp, fileStamp) != 0)) {
            report_writeErrorMsg(ERR_RUNOFF_FILE_FORMAT, __L0)
            return
        }
        nSubcatch.v = (-1)
        nPollut.v = (-1)
        flowUnits.v = (-1)
        R.fread1(nSubcatch, 4, Frunoff!!.file)
        R.fread1(nPollut, 4, Frunoff!!.file)
        R.fread1(flowUnits, 4, Frunoff!!.file)
        R.fread1(MaxSteps, 4, Frunoff!!.file)
        if (((((nSubcatch.v != Nobjects!![SUBCATCH]) || (nPollut.v != Nobjects!![POLLUT])) || (flowUnits.v != FlowUnits)) || (MaxSteps.v <= 0))) {
            report_writeErrorMsg(ERR_RUNOFF_FILE_FORMAT, __L0)
        }
    }
}

// runoff.c:391
fun runoff_saveToFile(__tStep: Float) {
    val tStep = FBox(__tStep)
    var j: Int = 0
    var n: Int = ((9 + Nobjects!![POLLUT]) - 1)
    R.fwrite1(tStep.v, 4, Frunoff!!.file)
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        subcatch_getResults(j, 1.0, SubcatchResults)
        R.fwrite(SubcatchResults, 0, ((n).toLong()).toInt(), Frunoff!!.file)
        j++
    }
}

// runoff.c:412
fun runoff_readFromFile() {
    var i: Int = 0
    var j: Int = 0
    var nResults: Int = 0
    var kount: Int = 0
    val tStep = FBox(0.0f)
    var gw: TGroundwater? = null
    if ((runoff_Nsteps.v > MaxSteps.v)) {
        report_writeErrorMsg(ERR_RUNOFF_FILE_END, __L0)
        return
    }
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        subcatch_setOldState(j)
        j++
    }
    kount = 0
    kount += ((R.fread1(tStep, 4, Frunoff!!.file)).toInt())
    nResults = ((9 + Nobjects!![POLLUT]) - 1)
    j = 0
    f2@ while ((j < Nobjects!![SUBCATCH])) {
        kount += ((R.fread(SubcatchResults, 0, ((nResults).toLong()).toInt(), Frunoff!!.file)).toInt())
        Subcatch!![j]!!.newSnowDepth = ((SubcatchResults!![SUBCATCH_SNOWDEPTH]).toDouble() / UCF(RAINDEPTH))
        Subcatch!![j]!!.evapLoss = ((SubcatchResults!![SUBCATCH_EVAP]).toDouble() / UCF(RAINFALL))
        Subcatch!![j]!!.infilLoss = ((SubcatchResults!![SUBCATCH_INFIL]).toDouble() / UCF(RAINFALL))
        Subcatch!![j]!!.newRunoff = ((SubcatchResults!![SUBCATCH_RUNOFF]).toDouble() / UCF(FLOW))
        gw = Subcatch!![j]!!.groundwater
        if ((gw != null)) {
            gw!!.newFlow = ((SubcatchResults!![SUBCATCH_GW_FLOW]).toDouble() / UCF(FLOW))
            gw!!.lowerDepth = (Aquifer!![gw!!.aquifer]!!.bottomElev - (((SubcatchResults!![SUBCATCH_GW_ELEV]).toDouble() / UCF(LENGTH))))
            gw!!.theta = (SubcatchResults!![SUBCATCH_SOIL_MOIST]).toDouble()
        }
        i = 0
        f3@ while ((i < Nobjects!![POLLUT])) {
            Subcatch!![j]!!.newQual!![i] = (SubcatchResults!![(SUBCATCH_WASHOFF + i)]).toDouble()
            i++
        }
        j++
    }
    if ((kount < (1 + (Nobjects!![SUBCATCH] * nResults)))) {
        report_writeErrorMsg(ERR_RUNOFF_FILE_READ, __L0)
        return
    }
    OldRunoffTime = NewRunoffTime
    NewRunoffTime = (OldRunoffTime + (((tStep.v)).toDouble() * 1000.0))
    NewRunoffTime = ((if ((((NewRunoffTime) <= (TotalDuration)))) (NewRunoffTime) else (TotalDuration)))
    runoff_Nsteps.v++
}

// runoff.c:491
fun runoff_getOutfallRunon(tStep: Double) {
    var i: Int = 0
    var k: Int = 0
    var p: Int = 0
    var w: Double = 0.0
    i = 0
    f1@ while ((i < Nnodes!![OUTFALL])) {
        k = Outfall!![i]!!.routeTo
        if ((k < 0)) {
            i++
            continue@f1
        }
        if ((Subcatch!![k]!!.area == 0.0)) {
            i++
            continue@f1
        }
        subcatch_addRunonFlow(k, (Outfall!![i]!!.vRouted / tStep))
        massbal_updateRunoffTotals(RUNOFF_RUNON, Outfall!![i]!!.vRouted)
        Outfall!![i]!!.vRouted = 0.0
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            w = (Outfall!![i]!!.wRouted!![p] * 28.317)
            massbal_updateLoadingTotals(DEPOSITION_LOAD, p, (w * Pollut!![p]!!.mcf))
            Subcatch!![k]!!.newQual!![p] += (w / tStep)
            Outfall!![i]!!.wRouted!![p] = 0.0
            p++
        }
        i++
    }
}

