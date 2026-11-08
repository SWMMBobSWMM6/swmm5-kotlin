// Generated from EPA SWMM 5.2.4 (climate.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_climate")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MAXCLIMATEVARS: Int = 4
@JvmField var MAXDAYSPERMONTH: Int = 32
@JvmField var ClimateVarWords: Array<CPtr?>? = arrayOf<CPtr?>(__L22, __L23, __L7, __L24, __L25, null)
@JvmField var TempUnitsWords: Array<CPtr?>? = arrayOf<CPtr?>(__L653, __L654, __L655, null)
@JvmField var Tmin: Double = 0.0
@JvmField var Tmax: Double = 0.0
@JvmField var Trng: Double = 0.0
@JvmField var Trng1: Double = 0.0
@JvmField var Tave: Double = 0.0
@JvmField var Hrsr: Double = 0.0
@JvmField var Hrss: Double = 0.0
@JvmField var Hrday: Double = 0.0
@JvmField var Dhrdy: Double = 0.0
@JvmField var Dydif: Double = 0.0
@JvmField var LastDay: Double = 0.0
@JvmField var Tma: TMovAve? = TMovAve()
@JvmField val NextEvapDate = DBox(0.0)
@JvmField val NextEvapRate = DBox(0.0)
@JvmField var FileFormat: Int = 0
@JvmField val FileYear = IBox(0)
@JvmField val FileMonth = IBox(0)
@JvmField val FileDay = IBox(0)
@JvmField var FileLastDay: Int = 0
@JvmField var FileElapsedDays: Int = 0
@JvmField var FileValue: DoubleArray? = DoubleArray(4)
@JvmField var FileData: Array<DoubleArray?>? = Array<DoubleArray?>(4) { DoubleArray(32) }
@JvmField var FileLine: CPtr? = R.buf(1025)
@JvmField var FileFieldPos: IntArray? = IntArray(4)
@JvmField var FileDateFieldPos: Int = 0
@JvmField var FileWindType: Int = 0
@JvmField var FileTempUnits: Int = 0

// climate.c:153
fun climate_readParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    val y = DBox(0.0)
    val aDate = DBox(0.0)
    var fname: CPtr? = R.buf(260)
    k = findmatch(tok!![0], TempKeyWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![0])
    }
    when (k) {
        0 -> {
            if ((ntoks < 2)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            i = project_findObject(TSERIES, tok!![1])
            if ((i < 0)) {
                return error_setInpError(ERR_NAME, tok!![1])
            }
            Temp!!.dataSource = TSERIES_TEMP
            Temp!!.tSeries = i
            Tseries!![i]!!.refersTo = TSERIES_TEMP
        }
        1 -> {
            if ((ntoks < 2)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            Temp!!.dataSource = FILE_TEMP
            Fclimate!!.mode = USE_FILE
            sstrncpy(fname, tok!![1], 259L)
            sstrncpy(Fclimate!!.name, addAbsolutePath(fname), 259L)
            Temp!!.fileStartDate = ((-693594)).toDouble()
            if ((ntoks > 2)) {
                if ((tok!![2]!!.g(0) != 42)) {
                    if (!(datetime_strToDate(tok!![2], aDate) != 0)) {
                        return error_setInpError(ERR_DATETIME, tok!![2])
                    }
                    Temp!!.fileStartDate = aDate.v
                }
            }
            FileTempUnits = DEG_F
            if ((UnitSystem == SI)) {
                FileTempUnits = DEG_C
            }
            if ((ntoks > 3)) {
                i = findmatch(tok!![3], TempUnitsWords)
                if ((i < 0)) {
                    return error_setInpError(ERR_KEYWORD, tok!![3])
                }
                FileTempUnits = i
            }
        }
        2 -> {
            if ((strcomp(tok!![1], __L1) != 0)) {
                Wind!!.type = FILE_WIND
            } else {
                if ((ntoks < 14)) {
                    return error_setInpError(ERR_ITEMS, __L0)
                }
                Wind!!.type = MONTHLY_WIND
                i = 0
                f2@ while ((i < 12)) {
                    if (!(getDouble(tok!![(i + 2)], y) != 0)) {
                        return error_setInpError(ERR_NUMBER, tok!![(i + 2)])
                    }
                    Wind!!.aws!![i] = y.v
                    i++
                }
            }
        }
        3 -> {
            if ((ntoks < 7)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            i = 1
            f3@ while ((i < 7)) {
                if (!(getDouble(tok!![i], DElem(x!!, (i - 1))) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![i])
                }
                i++
            }
            if ((UnitSystem == SI)) {
                x!![0] = (((9.0 / 5.0) * x!![0]) + 32.0)
            }
            Snow!!.snotmp = x!![0]
            Snow!!.tipm = x!![1]
            Snow!!.rnm = x!![2]
            Temp!!.elev = (x!![3] / UCF(LENGTH))
            Temp!!.anglat = x!![4]
            Temp!!.dtlong = (x!![5] / 60.0)
        }
        4 -> {
            if ((ntoks < 12)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            if ((match(tok!![1], __L2) != 0)) {
                i = 0
            } else if ((match(tok!![1], __L3) != 0)) {
                i = 1
            } else {
                return error_setInpError(ERR_KEYWORD, tok!![1])
            }
            j = 0
            f4@ while ((j < 10)) {
                if ((((!(getDouble(tok!![(j + 2)], y) != 0)) || (y.v < 0.0)) || (y.v > 1.0))) {
                    return error_setInpError(ERR_NUMBER, tok!![(j + 2)])
                }
                Snow!!.adc!![i]!![j] = y.v
                j++
            }
        }
    }
    return 0
}

// climate.c:285
fun climate_readEvapParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    val x = DBox(0.0)
    k = findmatch(tok!![0], EvapTypeWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![0])
    }
    if ((k == RECOVERY)) {
        if ((ntoks < 2)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = project_findObject(TIMEPATTERN, tok!![1])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![1])
        }
        Evap!!.recoveryPattern = i
        return 0
    }
    if ((k == DRYONLY)) {
        if ((ntoks < 2)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if ((strcomp(tok!![1], __L4) != 0)) {
            Evap!!.dryOnly = 0
        } else if ((strcomp(tok!![1], __L5) != 0)) {
            Evap!!.dryOnly = 1
        } else {
            return error_setInpError(ERR_KEYWORD, tok!![1])
        }
        return 0
    }
    Evap!!.type = k
    if (((k != TEMPERATURE_EVAP) && (ntoks < 2))) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    when (k) {
        CONSTANT_EVAP -> {
            if (!(getDouble(tok!![1], x) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![1])
            }
            i = 0
            f2@ while ((i < 12)) {
                Evap!!.monthlyEvap!![i] = x.v
                i++
            }
        }
        MONTHLY_EVAP -> {
            if ((ntoks < 13)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            i = 0
            f3@ while ((i < 12)) {
                if (!(getDouble(tok!![(i + 1)], DElem(Evap!!.monthlyEvap!!, i)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![(i + 1)])
                }
                i++
            }
        }
        TIMESERIES_EVAP -> {
            i = project_findObject(TSERIES, tok!![1])
            if ((i < 0)) {
                return error_setInpError(ERR_NAME, tok!![1])
            }
            Evap!!.tSeries = i
            Tseries!![i]!!.refersTo = TIMESERIES_EVAP
        }
        FILE_EVAP -> {
            if ((ntoks > 1)) {
                if ((ntoks < 13)) {
                    return error_setInpError(ERR_ITEMS, __L0)
                }
                i = 0
                f4@ while ((i < 12)) {
                    if (!(getDouble(tok!![(i + 1)], DElem(Evap!!.panCoeff!!, i)) != 0)) {
                        return error_setInpError(ERR_NUMBER, tok!![(i + 1)])
                    }
                    i++
                }
            }
        }
    }
    return 0
}

// climate.c:377
fun climate_readAdjustments(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    if ((ntoks == 1)) {
        return 0
    }
    if ((match(tok!![0], __L6) != 0)) {
        if ((ntoks < 13)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = 1
        f1@ while ((i < 13)) {
            if (!(getDouble(tok!![i], DElem(Adjust!!.temp!!, (i - 1))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
            i++
        }
        return 0
    }
    if ((match(tok!![0], __L7) != 0)) {
        if ((ntoks < 13)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = 1
        f2@ while ((i < 13)) {
            if (!(getDouble(tok!![i], DElem(Adjust!!.evap!!, (i - 1))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
            i++
        }
        return 0
    }
    if ((match(tok!![0], __L8) != 0)) {
        if ((ntoks < 13)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = 1
        f3@ while ((i < 13)) {
            if (!(getDouble(tok!![i], DElem(Adjust!!.rain!!, (i - 1))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
            i++
        }
        return 0
    }
    if ((match(tok!![0], __L9) != 0)) {
        if ((ntoks < 13)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = 1
        f4@ while ((i < 13)) {
            if (!(getDouble(tok!![i], DElem(Adjust!!.hydcon!!, (i - 1))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
            if ((Adjust!!.hydcon!![(i - 1)] <= 0.0)) {
                Adjust!!.hydcon!![(i - 1)] = 1.0
            }
            i++
        }
        return 0
    }
    if ((match(tok!![0], __L10) != 0)) {
        if ((ntoks < 3)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = project_findObject(SUBCATCH, tok!![1])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![1])
        }
        j = project_findObject(TIMEPATTERN, tok!![2])
        if ((j < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        Subcatch!![i]!!.nPervPattern = j
        return 0
    }
    if ((match(tok!![0], __L11) != 0)) {
        if ((ntoks < 3)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = project_findObject(SUBCATCH, tok!![1])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![1])
        }
        j = project_findObject(TIMEPATTERN, tok!![2])
        if ((j < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        Subcatch!![i]!!.dStorePattern = j
        return 0
    }
    if ((match(tok!![0], __L12) != 0)) {
        if ((ntoks < 3)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = project_findObject(SUBCATCH, tok!![1])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![1])
        }
        j = project_findObject(TIMEPATTERN, tok!![2])
        if ((j < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        Subcatch!![i]!!.infilPattern = j
        return 0
    }
    return error_setInpError(ERR_KEYWORD, tok!![0])
}

// climate.c:480
fun climate_validate() {
    var i: Int = 0
    var a: Double = 0.0
    var z: Double = 0.0
    var pa: Double = 0.0
    if ((((Wind!!.type == FILE_WIND) || (Evap!!.type == FILE_EVAP)) || (Evap!!.type == TEMPERATURE_EVAP))) {
        if ((Fclimate!!.mode == NO_FILE)) {
            report_writeErrorMsg(ERR_NO_CLIMATE_FILE, __L0)
        }
    }
    if ((Fclimate!!.mode == USE_FILE)) {
        climate_openFile()
    }
    if (((((Snow!!.tipm < 0.0) || (Snow!!.tipm > 1.0)) || (Snow!!.rnm < 0.0)) || (Snow!!.rnm > 1.0))) {
        report_writeErrorMsg(ERR_SNOWMELT_PARAMS, __L0)
    }
    a = Temp!!.anglat
    if (((a <= (-89.99)) || (a >= 89.99))) {
        report_writeErrorMsg(ERR_SNOWMELT_PARAMS, __L0)
    } else {
        Temp!!.tanAnglat = Glibm.tan(((a * 3.141592654) / 180.0))
    }
    z = (Temp!!.elev / 1000.0)
    if ((z <= 0.0)) {
        pa = 29.9
    } else {
        pa = ((29.9 - (1.02 * z)) + (0.0032 * Glibm.pow(z, 2.4)))
    }
    Temp!!.gamma = (0.000359 * pa)
    i = 0
    f1@ while ((i < 12)) {
        if ((UnitSystem == SI)) {
            Adjust!!.temp!![i] *= (9.0 / 5.0)
        }
        Adjust!!.evap!![i] /= (UCF(EVAPRATE))
        i++
    }
}

// climate.c:531
fun climate_openFile() {
    var i: Int = 0
    val m = IBox(0)
    val y = IBox(0)
    if ((((R.fopen(Fclimate!!.name, __L13)).also { __v -> Fclimate!!.file = __v }) == null)) {
        report_writeErrorMsg(ERR_CLIMATE_FILE_OPEN, Fclimate!!.name)
        return
    }
    FileValue!![TMIN] = Temp!!.ta
    FileValue!![TMAX] = Temp!!.ta
    FileValue!![EVAP] = 0.0
    FileValue!![WIND] = 0.0
    FileFormat = getFileFormat()
    if ((FileFormat == UNKNOWN_FORMAT)) {
        report_writeErrorMsg(ERR_CLIMATE_FILE_READ, Fclimate!!.name)
        return
    }
    R.rewind(Fclimate!!.file)
    sstrncpy(FileLine, __L0, 0L)
    if ((Temp!!.fileStartDate == ((-693594)).toDouble())) {
        datetime_decodeDate(StartDate.v, FileYear, FileMonth, FileDay)
    } else {
        datetime_decodeDate(Temp!!.fileStartDate, FileYear, FileMonth, FileDay)
    }
    w1@ while (!(R.feof(Fclimate!!.file) != 0)) {
        sstrncpy(FileLine, __L0, 0L)
        readFileLine(y, m)
        if (((y.v == FileYear.v) && (m.v == FileMonth.v))) {
            break@w1
        }
    }
    if ((R.feof(Fclimate!!.file) != 0)) {
        report_writeErrorMsg(ERR_CLIMATE_END_OF_FILE, Fclimate!!.name)
        return
    }
    if (!(ErrorCode != 0)) {
        FileElapsedDays = 0
        FileLastDay = datetime_daysPerMonth(FileYear.v, FileMonth.v)
        readFileValues()
        i = TMIN
        f2@ while ((i <= WIND)) {
            if ((FileData!![i]!![FileDay.v] == (-10000000000.0))) {
                i++
                continue@f2
            }
            FileValue!![i] = FileData!![i]!![FileDay.v]
            i++
        }
    }
}

// climate.c:598
fun climate_initState() {
    LastDay = ((-693594)).toDouble()
    Temp!!.tmax = (-10000000000.0)
    Snow!!.removed = 0.0
    NextEvapDate.v = StartDate.v
    NextEvapRate.v = 0.0
    if (((Evap!!.type == TIMESERIES_EVAP) && (Evap!!.tSeries >= 0))) {
        table_getFirstEntry(Tseries!![Evap!!.tSeries], NextEvapDate, NextEvapRate)
        if ((NextEvapDate.v < StartDate.v)) {
            setNextEvapDate(StartDate.v)
        }
        Evap!!.rate = (NextEvapRate.v / UCF(EVAPRATE))
        setNextEvapDate(NextEvapDate.v)
    }
    if ((Evap!!.type == TEMPERATURE_EVAP)) {
        Tma!!.maxCount = ((56L / 8L)).toInt()
        Tma!!.count = 0
        Tma!!.front = 0
        Tma!!.tAve = 0.0
        Tma!!.tRng = 0.0
    }
}

// climate.c:641
fun climate_setState(theDate: Double) {
    if ((Fclimate!!.mode == USE_FILE)) {
        updateFileValues(theDate)
    }
    if ((Temp!!.dataSource != NO_TEMP)) {
        setTemp(theDate)
    }
    setEvap(theDate)
    setWind(theDate)
    Adjust!!.rainFactor = Adjust!!.rain!![(datetime_monthOfYear(theDate) - 1)]
    Adjust!!.hydconFactor = Adjust!!.hydcon!![(datetime_monthOfYear(theDate) - 1)]
    setNextEvapDate(theDate)
}

// climate.c:659
fun climate_getNextEvapDate(): Double {
    return NextEvapDate.v
}

// climate.c:671
fun setNextEvapDate(theDate: Double) {
    val yr = IBox(0)
    val mon = IBox(0)
    val day = IBox(0)
    var k: Int = 0
    val d = DBox(0.0)
    val e = DBox(0.0)
    if ((NextEvapDate.v > theDate)) {
        return
    }
    when (Evap!!.type) {
        CONSTANT_EVAP -> {
            NextEvapDate.v = (theDate + 365.0)
        }
        MONTHLY_EVAP -> {
            datetime_decodeDate(theDate, yr, mon, day)
            if ((mon.v == 12)) {
                mon.v = 1
                yr.v++
            } else {
                mon.v++
            }
            NextEvapDate.v = datetime_encodeDate(yr.v, mon.v, 1)
        }
        TIMESERIES_EVAP -> {
            k = Evap!!.tSeries
            if ((k >= 0)) {
                NextEvapDate.v = (theDate + 365.0)
                w2@ while (((table_getNextEntry(Tseries!![k], d, e) != 0) && (d.v <= EndDateTime))) {
                    if ((d.v >= theDate)) {
                        NextEvapDate.v = d.v
                        NextEvapRate.v = e.v
                        break@w2
                    }
                }
            }
        }
        FILE_EVAP -> {
            NextEvapDate.v = (floor(theDate) + 1.0)
        }
        else -> {
            NextEvapDate.v = (theDate + 365.0)
        }
    }
}

// climate.c:734
fun updateFileValues(theDate: Double) {
    var i: Int = 0
    var deltaDays: Int = 0
    deltaDays = R.D2I(((floor(theDate) - floor(StartDateTime))))
    if ((deltaDays > FileElapsedDays)) {
        FileElapsedDays++
        FileDay.v++
        if ((FileDay.v > FileLastDay)) {
            FileMonth.v++
            if ((FileMonth.v > 12)) {
                FileMonth.v = 1
                FileYear.v++
            }
            readFileValues()
            FileDay.v = 1
            FileLastDay = datetime_daysPerMonth(FileYear.v, FileMonth.v)
        }
        i = TMIN
        f1@ while ((i <= WIND)) {
            if ((FileData!![i]!![FileDay.v] == (-10000000000.0))) {
                i++
                continue@f1
            }
            FileValue!![i] = FileData!![i]!![FileDay.v]
            i++
        }
    }
}

// climate.c:782
fun setTemp(theDate: Double) {
    var j: Int = 0
    var k: Int = 0
    var mon: Int = 0
    var day: Int = 0
    var theDay: Double = 0.0
    var hour: Double = 0.0
    var tmp: Double = 0.0
    mon = datetime_monthOfYear(theDate)
    theDay = floor(theDate)
    if ((theDay > LastDay)) {
        day = datetime_dayOfYear(theDate)
        if ((Temp!!.dataSource == FILE_TEMP)) {
            Tmin = (FileValue!![TMIN] + Adjust!!.temp!![(mon - 1)])
            Tmax = (FileValue!![TMAX] + Adjust!!.temp!![(mon - 1)])
            if ((Tmin > Tmax)) {
                tmp = Tmin
                Tmin = Tmax
                Tmax = tmp
            }
            updateTempTimes(day)
            if ((Evap!!.type == TEMPERATURE_EVAP)) {
                updateTempMoveAve(Tmin, Tmax)
                FileValue!![EVAP] = getTempEvap(day, Tma!!.tAve, Tma!!.tRng)
            }
        }
        Snow!!.season = Glibm.sin((0.0172615 * (((day).toDouble() - 81.0))))
        j = 0
        f1@ while ((j < Nobjects!![SNOWMELT])) {
            snow_setMeltCoeffs(j, Snow!!.season)
            j++
        }
        LastDay = theDate
    }
    if ((Temp!!.dataSource == FILE_TEMP)) {
        hour = (((theDate - theDay)) * 24.0)
        if ((hour < Hrsr)) {
            Temp!!.ta = (Tmin + ((Trng1 / 2.0) * Glibm.sin(((3.141592654 / Dydif) * ((Hrsr - hour))))))
        } else if (((hour >= Hrsr) && (hour <= Hrss))) {
            Temp!!.ta = (Tave + (Trng * Glibm.sin(((3.141592654 / Dhrdy) * ((Hrday - hour))))))
        } else {
            Temp!!.ta = (Tmax - (Trng * Glibm.sin(((3.141592654 / Dydif) * ((hour - Hrss))))))
        }
    }
    if ((Temp!!.dataSource == TSERIES_TEMP)) {
        k = Temp!!.tSeries
        if ((k >= 0)) {
            Temp!!.ta = table_tseriesLookup(Tseries!![k], theDate, 1)
            if ((UnitSystem == SI)) {
                Temp!!.ta = ((((9.0 / 5.0)) * Temp!!.ta) + 32.0)
            }
            Temp!!.ta += (Adjust!!.temp!![(mon - 1)])
        }
    }
    Temp!!.ea = (8.1175e6 * Glibm.exp(((-7701.544) / ((Temp!!.ta + 405.0265)))))
}

// climate.c:872
fun setEvap(theDate: Double) {
    var k: Int = 0
    var mon: Int = datetime_monthOfYear(theDate)
    when (Evap!!.type) {
        CONSTANT_EVAP -> {
            Evap!!.rate = (Evap!!.monthlyEvap!![0] / UCF(EVAPRATE))
        }
        MONTHLY_EVAP -> {
            Evap!!.rate = (Evap!!.monthlyEvap!![(mon - 1)] / UCF(EVAPRATE))
        }
        TIMESERIES_EVAP -> {
            if ((theDate >= NextEvapDate.v)) {
                Evap!!.rate = (NextEvapRate.v / UCF(EVAPRATE))
            }
        }
        FILE_EVAP -> {
            Evap!!.rate = (FileValue!![EVAP] / UCF(EVAPRATE))
            Evap!!.rate *= (Evap!!.panCoeff!![(mon - 1)])
        }
        TEMPERATURE_EVAP -> {
            Evap!!.rate = (FileValue!![EVAP] / UCF(EVAPRATE))
        }
        else -> {
            Evap!!.rate = 0.0
        }
    }
    Evap!!.rate += (Adjust!!.evap!![(mon - 1)])
    Evap!!.recoveryFactor = 1.0
    k = Evap!!.recoveryPattern
    if (((k >= 0) && (Pattern!![k]!!.type == MONTHLY_PATTERN))) {
        Evap!!.recoveryFactor = Pattern!![k]!!.factor!![(mon - 1)]
    }
}

// climate.c:923
fun setWind(theDate: Double) {
    val yr = IBox(0)
    val mon = IBox(0)
    val day = IBox(0)
    when (Wind!!.type) {
        MONTHLY_WIND -> {
            datetime_decodeDate(theDate, yr, mon, day)
            Wind!!.ws = (Wind!!.aws!![(mon.v - 1)] / UCF(WINDSPEED))
        }
        FILE_WIND -> {
            Wind!!.ws = FileValue!![WIND]
        }
        else -> {
            Wind!!.ws = 0.0
        }
    }
}

// climate.c:949
fun updateTempTimes(day: Int) {
    var decl: Double = 0.0
    var hrang: Double = 0.0
    var arg: Double = 0.0
    decl = (0.40928 * Glibm.cos((0.017202 * ((172.0 - (day).toDouble())))))
    arg = ((-(Glibm.tan(decl))) * Temp!!.tanAnglat)
    if ((arg <= (-1.0))) {
        arg = 3.141592654
    } else if ((arg >= 1.0)) {
        arg = 0.0
    } else {
        arg = Glibm.acos(arg)
    }
    hrang = (3.8197 * arg)
    Hrsr = ((12.0 - hrang) + Temp!!.dtlong)
    Hrss = (((12.0 + hrang) + Temp!!.dtlong) - 3.0)
    Dhrdy = (Hrsr - Hrss)
    Dydif = ((24.0 + Hrsr) - Hrss)
    Hrday = (((Hrsr + Hrss)) / 2.0)
    Tave = (((Tmin + Tmax)) / 2.0)
    Trng = (((Tmax - Tmin)) / 2.0)
    if ((Temp!!.tmax == (-10000000000.0))) {
        Trng1 = (Tmax - Tmin)
    } else {
        Trng1 = (Temp!!.tmax - Tmin)
    }
    Temp!!.tmax = Tmax
}

// climate.c:981
fun getTempEvap(day: Int, tave: Double, trng: Double): Double {
    var a: Double = ((2.0 * 3.141592654) / 365.0)
    var ta: Double = ((((tave - 32.0)) * 5.0) / 9.0)
    var tr: Double = ((trng * 5.0) / 9.0)
    var lamda: Double = (2.50 - (0.002361 * ta))
    var dr: Double = (1.0 + (0.033 * Glibm.cos((a * (day).toDouble()))))
    var phi: Double = (((Temp!!.anglat * 2.0) * 3.141592654) / 360.0)
    var del: Double = (0.4093 * Glibm.sin((a * ((284.0 + (day).toDouble())))))
    var omega: Double = Glibm.acos(((-(Glibm.tan(phi))) * Glibm.tan(del)))
    var ra: Double = ((37.6 * dr) * ((((omega * Glibm.sin(phi)) * Glibm.sin(del)) + ((Glibm.cos(phi) * Glibm.cos(del)) * Glibm.sin(omega)))))
    var e: Double = ((((0.0023 * ra) / lamda) * sqrt(tr)) * ((ta + 17.8)))
    if ((e < 0.0)) {
        e = 0.0
    }
    if ((UnitSystem == US)) {
        e /= 25.4
    }
    return e
}

// climate.c:1010
fun getFileFormat(): Int {
    var recdType: CPtr? = R.bufFrom(__L0, 4)
    var elemType: CPtr? = R.bufFrom(__L0, 4)
    var filler: CPtr? = R.bufFrom(__L0, 5)
    var staID: CPtr? = R.buf(80)
    var s: CPtr? = R.buf(80)
    var line: CPtr? = R.buf(1024)
    val y = IBox(0)
    val m = IBox(0)
    val d = IBox(0)
    var n: Int = 0
    if (((R.fgets(line, 1024, Fclimate!!.file)) == null)) {
        return UNKNOWN_FORMAT
    }
    sstrncpy(recdType, line, 3L)
    sstrncpy(filler, line!!.add(23), 4L)
    if (((R.strcmp(recdType, __L14) == 0) && (R.strcmp(filler, __L15) == 0))) {
        return TD3200
    }
    if ((R.strlen(line) >= 233L)) {
        sstrncpy(elemType, line!!.add(13), 3L)
        n = R.atoi(elemType)
        if ((((n == 1) || (n == 2)) || (n == 151))) {
            return DLY0204
        }
    }
    n = R.sscanf(line, __L16, staID, y, m, d, s)
    if ((n == 5)) {
        return USER_PREPARED
    }
    if ((isGhcndFormat(line) != 0)) {
        return GHCND
    }
    return UNKNOWN_FORMAT
}

// climate.c:1055
fun readFileLine(y: IRef?, m: IRef?) {
    w1@ while ((R.strlen(FileLine) == 0L)) {
        if (((R.fgets(FileLine, 1024, Fclimate!!.file)) == null)) {
            return
        }
        if ((FileLine!!.g(0) == 10)) {
            FileLine!!.s(0, 0)
        }
    }
    when (FileFormat) {
        USER_PREPARED -> {
            readUserFileLine(y, m)
        }
        TD3200 -> {
            readTD3200FileLine(y, m)
        }
        DLY0204 -> {
            readDLY0204FileLine(y, m)
        }
        GHCND -> {
            readGhcndFileLine(y, m)
        }
    }
}

// climate.c:1082
fun readUserFileLine(y: IRef?, m: IRef?) {
    var n: Int = 0
    var staID: CPtr? = R.buf(80)
    n = R.sscanf(FileLine, __L17, staID, y, m)
    if ((n < 3)) {
        report_writeErrorMsg(ERR_CLIMATE_FILE_READ, Fclimate!!.name)
    }
}

// climate.c:1101
fun readTD3200FileLine(y: IRef?, m: IRef?) {
    var recdType: CPtr? = R.bufFrom(__L0, 4)
    var year: CPtr? = R.bufFrom(__L0, 5)
    var month: CPtr? = R.bufFrom(__L0, 3)
    if ((R.strlen(FileLine) < 30L)) {
        report_writeErrorMsg(ERR_CLIMATE_FILE_READ, Fclimate!!.name)
        return
    }
    sstrncpy(recdType, FileLine, 3L)
    if ((R.strcmp(recdType, __L14) != 0)) {
        report_writeErrorMsg(ERR_CLIMATE_FILE_READ, Fclimate!!.name)
        return
    }
    sstrncpy(year, FileLine!!.add(17), 4L)
    sstrncpy(month, FileLine!!.add(21), 2L)
    y!!.set(R.atoi(year))
    m!!.set(R.atoi(month))
}

// climate.c:1137
fun readDLY0204FileLine(y: IRef?, m: IRef?) {
    var year: CPtr? = R.bufFrom(__L0, 5)
    var month: CPtr? = R.bufFrom(__L0, 3)
    if ((R.strlen(FileLine) < 16L)) {
        report_writeErrorMsg(ERR_CLIMATE_FILE_READ, Fclimate!!.name)
        return
    }
    sstrncpy(year, FileLine!!.add(7), 4L)
    sstrncpy(month, FileLine!!.add(11), 2L)
    y!!.set(R.atoi(year))
    m!!.set(R.atoi(month))
}

// climate.c:1164
fun readFileValues() {
    var i: Int = 0
    var j: Int = 0
    val y = IBox(0)
    val m = IBox(0)
    i = 0
    f1@ while ((i < MAXCLIMATEVARS)) {
        j = 0
        f2@ while ((j < MAXDAYSPERMONTH)) {
            FileData!![i]!![j] = (-10000000000.0)
            j++
        }
        i++
    }
    w3@ while (!(ErrorCode != 0)) {
        if ((R.feof(Fclimate!!.file) != 0)) {
            return
        }
        readFileLine(y, m)
        if (((y.v > FileYear.v) || (m.v > FileMonth.v))) {
            return
        }
        when (FileFormat) {
            USER_PREPARED -> {
                parseUserFileLine()
            }
            TD3200 -> {
                parseTD3200FileLine()
            }
            DLY0204 -> {
                parseDLY0204FileLine()
            }
            GHCND -> {
                parseGhcndFileLine()
            }
        }
        sstrncpy(FileLine, __L0, 0L)
    }
}

// climate.c:1201
fun parseUserFileLine() {
    var n: Int = 0
    val y = IBox(0)
    val m = IBox(0)
    val d = IBox(0)
    var staID: CPtr? = R.buf(80)
    var s0: CPtr? = R.bufFrom(__L0, 80)
    var s1: CPtr? = R.bufFrom(__L0, 80)
    var s2: CPtr? = R.bufFrom(__L0, 80)
    var s3: CPtr? = R.bufFrom(__L0, 80)
    var x: Double = 0.0
    n = R.sscanf(FileLine, __L18, staID, y, m, d, s0, s1, s2, s3)
    if ((n < 4)) {
        return
    }
    if (((d.v < 1) || (d.v > 31))) {
        return
    }
    if (((R.strlen(s0) > 0L) && (s0!!.g(0) != 42))) {
        x = R.atof(s0)
        if ((UnitSystem == SI)) {
            x = (((9.0 / 5.0) * x) + 32.0)
        }
        FileData!![TMAX]!![d.v] = x
    }
    if (((R.strlen(s1) > 0L) && (s1!!.g(0) != 42))) {
        x = R.atof(s1)
        if ((UnitSystem == SI)) {
            x = (((9.0 / 5.0) * x) + 32.0)
        }
        FileData!![TMIN]!![d.v] = x
    }
    if (((R.strlen(s2) > 0L) && (s2!!.g(0) != 42))) {
        FileData!![EVAP]!![d.v] = R.atof(s2)
    }
    if (((R.strlen(s3) > 0L) && (s3!!.g(0) != 42))) {
        FileData!![WIND]!![d.v] = R.atof(s3)
    }
}

// climate.c:1249
fun parseTD3200FileLine() {
    var i: Int = 0
    var param: CPtr? = R.bufFrom(__L0, 5)
    sstrncpy(param, FileLine!!.add(11), 4L)
    i = 0
    f1@ while ((i < MAXCLIMATEVARS)) {
        if ((R.strcmp(param, ClimateVarWords!![i]) == 0)) {
            setTD3200FileValues(i)
        }
        i++
    }
}

// climate.c:1271
fun setTD3200FileValues(i: Int) {
    var valCount: CPtr? = R.bufFrom(__L0, 4)
    var day: CPtr? = R.bufFrom(__L0, 3)
    var sign: CPtr? = R.bufFrom(__L0, 2)
    var value_: CPtr? = R.bufFrom(__L0, 6)
    var flag2: CPtr? = R.bufFrom(__L0, 2)
    var x: Double = 0.0
    var nValues: Int = 0
    var j: Int = 0
    var k: Int = 0
    var d: Int = 0
    sstrncpy(valCount, FileLine!!.add(27), 3L)
    nValues = R.atoi(valCount)
    if (((R.strlen(FileLine)).toInt() >= ((12 * nValues) + 30))) {
        j = 0
        f1@ while ((j < nValues)) {
            k = (30 + (j * 12))
            sstrncpy(day, FileLine!!.add(k), 2L)
            sstrncpy(sign, FileLine!!.add((k + 4)), 1L)
            sstrncpy(value_, FileLine!!.add((k + 5)), 5L)
            sstrncpy(flag2, FileLine!!.add((k + 11)), 1L)
            d = R.atoi(day)
            if (((((R.strcmp(value_, __L19) != 0) && (((flag2!!.g(0) == 48) || (flag2!!.g(0) == 49)))) && (d > 0)) && (d <= 31))) {
                x = R.atof(value_)
                if ((sign!!.g(0) == 45)) {
                    x = (-x)
                }
                if ((i == EVAP)) {
                    x /= 100.0
                    if ((UnitSystem == SI)) {
                        x *= 25.4
                    }
                }
                if ((i == WIND)) {
                    x /= 24.0
                }
                FileData!![i]!![d] = x
            }
            j++
        }
    }
}

// climate.c:1336
fun parseDLY0204FileLine() {
    var j: Int = 0
    var k: Int = 0
    var p: Int = 0
    var param: CPtr? = R.bufFrom(__L0, 4)
    var sign: CPtr? = R.bufFrom(__L0, 2)
    var value_: CPtr? = R.bufFrom(__L0, 6)
    var code: CPtr? = R.bufFrom(__L0, 2)
    var x: Double = 0.0
    sstrncpy(param, FileLine!!.add(13), 3L)
    p = R.atoi(param)
    if ((p == 1)) {
        p = TMAX
    } else if ((p == 2)) {
        p = TMIN
    } else if ((p == 151)) {
        p = EVAP
    } else {
        return
    }
    if ((R.strlen(FileLine) < 233L)) {
        return
    }
    k = 16
    j = 1
    f1@ while ((j <= 31)) {
        sstrncpy(sign, FileLine!!.add(k), 1L)
        sstrncpy(value_, FileLine!!.add((k + 1)), 5L)
        sstrncpy(code, FileLine!!.add((k + 6)), 1L)
        k += 7
        if (((R.strcmp(value_, __L19) != 0) && (R.strcmp(value_, __L20) != 0))) {
            when (p) {
                TMAX, TMIN -> {
                    x = (R.atof(value_) / 10.0)
                    if ((sign!!.g(0) == 45)) {
                        x = (-x)
                    }
                    x = (((9.0 / 5.0) * x) + 32.0)
                }
                EVAP -> {
                    x = (R.atof(value_) / 10.0)
                    if ((UnitSystem == US)) {
                        x /= 25.4
                    }
                }
                else -> {
                    return
                }
            }
            FileData!![p]!![j] = x
        }
        j++
    }
}

// climate.c:1401
fun isGhcndFormat(line: CPtr?): Int {
    var i: Int = 0
    var ptr: CPtr? = null
    ptr = R.strstr(line, __L21)
    if ((ptr == null)) {
        return 0
    }
    FileDateFieldPos = (((ptr!!.o - line!!.o).toLong())).toInt()
    i = TMIN
    f1@ while ((i <= WIND)) {
        FileFieldPos!![i] = (-1)
        i++
    }
    ptr = R.strstr(line, __L22)
    if ((ptr != null)) {
        FileFieldPos!![TMIN] = (((ptr!!.o - line!!.o).toLong())).toInt()
    }
    ptr = R.strstr(line, __L23)
    if ((ptr != null)) {
        FileFieldPos!![TMAX] = (((ptr!!.o - line!!.o).toLong())).toInt()
    }
    ptr = R.strstr(line, __L7)
    if ((ptr != null)) {
        FileFieldPos!![EVAP] = (((ptr!!.o - line!!.o).toLong())).toInt()
    }
    FileWindType = WDMV
    ptr = R.strstr(line, __L24)
    if ((ptr == null)) {
        FileWindType = AWND
        ptr = R.strstr(line, __L25)
    }
    if ((ptr != null)) {
        FileFieldPos!![WIND] = (((ptr!!.o - line!!.o).toLong())).toInt()
    }
    i = TMIN
    f2@ while ((i <= WIND)) {
        if ((FileFieldPos!![i] >= 0)) {
            return 1
        }
        i++
    }
    return 0
}

// climate.c:1445
fun readGhcndFileLine(y: IRef?, m: IRef?) {
    var n: Int = R.sscanf(FileLine!!.add(FileDateFieldPos), __L26, y, m)
    if ((n != 2)) {
        y!!.set((-99999))
        m!!.set((-99999))
    }
}

// climate.c:1463
fun parseGhcndFileLine() {
    val y = IBox(0)
    val m = IBox(0)
    val d = IBox(0)
    var n: Int = 0
    var i: Int = 0
    val v = DBox(0.0)
    n = R.sscanf(FileLine!!.add(FileDateFieldPos), __L27, y, m, d)
    if ((n < 3)) {
        return
    }
    if (((d.v < 1) || (d.v > 31))) {
        return
    }
    i = TMIN
    f1@ while ((i <= WIND)) {
        if ((FileFieldPos!![i] >= 0)) {
            if ((R.sscanf(FileLine!!.add(FileFieldPos!![i]), __L28, v) > 0)) {
                if ((abs(v.v) < 9999.0)) {
                    FileData!![i]!![d.v] = convertGhcndValue(i, v.v)
                }
            }
        }
        i++
    }
}

// climate.c:1496
fun convertGhcndValue(var_: Int, __v: Double): Double {
    var v: Double = __v
    when (var_) {
        TMIN, TMAX -> {
            when (FileTempUnits) {
                DEG_C10 -> {
                    return ((((v / 10.0) * 9.0) / 5.0) + 32.0)
                }
                DEG_C -> {
                    return (((v * 9.0) / 5.0) + 32.0)
                }
                else -> {
                    return v
                }
            }
        }
        EVAP -> {
            when (FileTempUnits) {
                DEG_C10 -> {
                    v /= 10.0
                    if ((UnitSystem == US)) {
                        v /= 25.4
                    }
                    return v
                }
                DEG_C -> {
                    if ((UnitSystem == US)) {
                        v /= 25.4
                    }
                    return v
                }
                else -> {
                    if ((UnitSystem == SI)) {
                        v *= 25.4
                    }
                    return v
                }
            }
        }
        WIND -> {
            when (FileTempUnits) {
                DEG_C10 -> {
                    if ((FileWindType == WDMV)) {
                        return ((v * 0.62137) / 24.0)
                    } else {
                        return ((((v / 10.0) / 1000.0) * 0.62137) * 3600.0)
                    }
                }
                DEG_C -> {
                    if ((FileWindType == WDMV)) {
                        return ((v * 0.62137) / 24.0)
                    } else {
                        return (((v / 1000.0) * 0.62137) * 3600.0)
                    }
                }
                else -> {
                    if ((FileWindType == WDMV)) {
                        return (v / 24.0)
                    } else {
                        return v
                    }
                }
            }
        }
        else -> {
            return v
        }
    }
    throw IllegalStateException("convertGhcndValue: no return")
}

// climate.c:1569
fun updateTempMoveAve(tmin: Double, tmax: Double) {
    var ta: Double = 0.0
    var tr: Double = 0.0
    var kount: Int = Tma!!.count
    var count: Double = (kount).toDouble()
    ta = (((tmin + tmax)) / 2.0)
    tr = abs((tmax - tmin))
    if ((kount == Tma!!.maxCount)) {
        Tma!!.tAve = (((((Tma!!.tAve * count) + ta) - Tma!!.ta!![Tma!!.front])) / count)
        Tma!!.tRng = (((((Tma!!.tRng * count) + tr) - Tma!!.tr!![Tma!!.front])) / count)
        Tma!!.ta!![Tma!!.front] = ta
        Tma!!.tr!![Tma!!.front] = tr
        Tma!!.front++
        if (((Tma!!.front).toDouble() == count)) {
            Tma!!.front = 0
        }
    } else {
        Tma!!.tAve = ((((Tma!!.tAve * count) + ta)) / ((count + 1.0)))
        Tma!!.tRng = ((((Tma!!.tRng * count) + tr)) / ((count + 1.0)))
        Tma!!.ta!![Tma!!.front] = ta
        Tma!!.tr!![Tma!!.front] = tr
        Tma!!.count++
        Tma!!.front++
        if ((Tma!!.count == Tma!!.maxCount)) {
            Tma!!.front = 0
        }
    }
}

