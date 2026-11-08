// Generated from EPA SWMM 5.2.4 (gage.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_gage")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var OneSecond: Double = 1.1574074e-5

// gage.c:58
fun gage_readParams(j: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var k: Int = 0
    var err: Int = 0
    var id: CPtr? = null
    var fname: CPtr? = R.buf(260)
    var staID: CPtr? = R.buf(1025)
    var x: DoubleArray? = DoubleArray(7)
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(GAGE, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    x!![0] = (-1.0)
    x!![1] = 1.0
    x!![2] = 3600.0
    x!![3] = 1.0
    x!![4] = ((-693594)).toDouble()
    x!![5] = ((-693594)).toDouble()
    x!![6] = 0.0
    fname!!.s(0, 0)
    staID!!.s(0, 0)
    if ((ntoks < 5)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    k = findmatch(tok!![4], GageDataWords)
    if ((k == RAIN_TSERIES)) {
        err = readGageSeriesFormat(tok, ntoks, x)
    } else if ((k == RAIN_FILE)) {
        if ((ntoks < 8)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        sstrncpy(fname, tok!![5], 259L)
        sstrncpy(staID, tok!![6], 1024L)
        err = readGageFileFormat(tok, ntoks, x)
    } else {
        return error_setInpError(ERR_KEYWORD, tok!![4])
    }
    if ((err > 0)) {
        return err
    }
    Gage!![j]!!.ID = id
    Gage!![j]!!.tSeries = R.D2I(x!![0])
    Gage!![j]!!.rainType = R.D2I(x!![1])
    Gage!![j]!!.rainInterval = R.D2I(x!![2])
    Gage!![j]!!.snowFactor = x!![3]
    Gage!![j]!!.rainUnits = R.D2I(x!![6])
    if ((Gage!![j]!!.tSeries >= 0)) {
        Gage!![j]!!.dataSource = RAIN_TSERIES
    } else {
        Gage!![j]!!.dataSource = RAIN_FILE
    }
    if ((Gage!![j]!!.dataSource == RAIN_FILE)) {
        sstrncpy(Gage!![j]!!.fname, addAbsolutePath(fname), 259L)
        sstrncpy(Gage!![j]!!.staID, staID, 1024L)
        Gage!![j]!!.startFileDate = x!![4]
        Gage!![j]!!.endFileDate = x!![5]
    }
    Gage!![j]!!.unitsFactor = 1.0
    Gage!![j]!!.coGage = (-1)
    Gage!![j]!!.isUsed = 0
    return 0
}

// gage.c:133
fun readGageSeriesFormat(tok: Array<CPtr?>?, ntoks: Int, x: DoubleArray?): Int {
    var m: Int = 0
    var ts: Int = 0
    val aTime = DBox(0.0)
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    m = findmatch(tok!![1], RainTypeWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    x!![1] = (m).toDouble()
    if ((getDouble(tok!![2], DElem(x!!, 2)) != 0)) {
        x!![2] = floor(((x!![2] * 3600.0) + 0.5))
    } else if ((datetime_strToTime(tok!![2], aTime) != 0)) {
        x!![2] = floor(((aTime.v * 86400.0) + 0.5))
    } else {
        return error_setInpError(ERR_DATETIME, tok!![2])
    }
    if ((x!![2] <= 0.0)) {
        return error_setInpError(ERR_DATETIME, tok!![2])
    }
    if (!(getDouble(tok!![3], DElem(x!!, 3)) != 0)) {
        return error_setInpError(ERR_DATETIME, tok!![3])
    }
    ts = project_findObject(TSERIES, tok!![5])
    if ((ts < 0)) {
        return error_setInpError(ERR_NAME, tok!![5])
    }
    x!![0] = (ts).toDouble()
    sstrncpy(tok!![2], __L0, 0L)
    return 0
}

// gage.c:168
fun readGageFileFormat(tok: Array<CPtr?>?, ntoks: Int, x: DoubleArray?): Int {
    var m: Int = 0
    var u: Int = 0
    val aDate = DBox(0.0)
    val aTime = DBox(0.0)
    m = findmatch(tok!![1], RainTypeWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    x!![1] = (m).toDouble()
    if ((getDouble(tok!![2], DElem(x!!, 2)) != 0)) {
        x!![2] *= 3600.0
    } else if ((datetime_strToTime(tok!![2], aTime) != 0)) {
        x!![2] = floor(((aTime.v * 86400.0) + 0.5))
    } else {
        return error_setInpError(ERR_DATETIME, tok!![2])
    }
    if ((x!![2] <= 0.0)) {
        return error_setInpError(ERR_DATETIME, tok!![2])
    }
    if (!(getDouble(tok!![3], DElem(x!!, 3)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![3])
    }
    u = findmatch(tok!![7], RainUnitsWords)
    if ((u < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![7])
    }
    x!![6] = (u).toDouble()
    if (((ntoks > 8) && (tok!![8]!!.g(0) != 42))) {
        if (!(datetime_strToDate(tok!![8], aDate) != 0)) {
            return error_setInpError(ERR_DATETIME, tok!![8])
        }
        x!![4] = ((aDate.v).toFloat()).toDouble()
    }
    return 0
}

// gage.c:209
fun gage_validate(j: Int) {
    var i: Int = 0
    var k: Int = 0
    var gageInterval: Int = 0
    if ((Gage!![j]!!.dataSource == RAIN_TSERIES)) {
        if (!(Gage!![j]!!.isUsed != 0)) {
            return
        }
        k = Gage!![j]!!.tSeries
        i = 0
        f1@ while ((i < j)) {
            if ((((Gage!![i]!!.dataSource == RAIN_TSERIES) && (Gage!![i]!!.tSeries == k)) && (Gage!![i]!!.isUsed != 0))) {
                Gage!![j]!!.coGage = i
                if ((Gage!![j]!!.rainType != Gage!![i]!!.rainType)) {
                    report_writeErrorMsg(ERR_RAIN_GAGE_FORMAT, Gage!![j]!!.ID)
                }
                return
            }
            i++
        }
        if ((Tseries!![k]!!.refersTo >= 0)) {
            report_writeErrorMsg(ERR_RAIN_GAGE_TSERIES, Gage!![j]!!.ID)
        }
        gageInterval = R.D2I((floor(((Tseries!![k]!!.dxMin * 86400.0) + 0.5))))
        if (((gageInterval > 0) && (Gage!![j]!!.rainInterval > gageInterval))) {
            report_writeErrorMsg(ERR_RAIN_GAGE_INTERVAL, Gage!![j]!!.ID)
        }
        if ((Gage!![j]!!.rainInterval < gageInterval)) {
            report_writeWarningMsg(__L156, Gage!![j]!!.ID)
        }
        if ((Gage!![j]!!.rainInterval < WetStep)) {
            report_writeWarningMsg(__L157, Gage!![j]!!.ID)
            WetStep = Gage!![j]!!.rainInterval
        }
    }
}

// gage.c:270
fun gage_initState(j: Int) {
    Gage!![j]!!.rainfall = 0.0
    Gage!![j]!!.apiRainfall = (-10000000000.0)
    Gage!![j]!!.reportRainfall = 0.0
    if ((IgnoreRainfall != 0)) {
        return
    }
    if ((Gage!![j]!!.dataSource == RAIN_FILE)) {
        Gage!![j]!!.currentFilePos = Gage!![j]!!.startFilePos
        if ((UnitSystem == SI)) {
            Gage!![j]!!.unitsFactor = 25.4
        }
    }
    if ((getFirstRainfall(j) != 0)) {
        Gage!![j]!!.endDate = datetime_addSeconds(Gage!![j]!!.startDate, (Gage!![j]!!.rainInterval).toDouble())
        if ((Gage!![j]!!.startDate > StartDateTime)) {
            Gage!![j]!!.nextDate = Gage!![j]!!.startDate
            Gage!![j]!!.nextRainfall = Gage!![j]!!.rainfall
            Gage!![j]!!.startDate = StartDateTime
            Gage!![j]!!.endDate = Gage!![j]!!.nextDate
            Gage!![j]!!.rainfall = 0.0
        } else if (!(getNextRainfall(j) != 0)) {
            Gage!![j]!!.nextDate = ((-693594)).toDouble()
        }
    } else {
        Gage!![j]!!.startDate = ((-693594)).toDouble()
    }
    initPastRain(j)
}

// gage.c:323
fun gage_setState(j: Int, __t: Double) {
    var t: Double = __t
    if ((Gage!![j]!!.isUsed == 0)) {
        return
    }
    if ((IgnoreRainfall != 0)) {
        Gage!![j]!!.rainfall = 0.0
        return
    }
    if ((Gage!![j]!!.coGage >= 0)) {
        Gage!![j]!!.rainfall = Gage!![Gage!![j]!!.coGage]!!.rainfall
        return
    }
    if ((Gage!![j]!!.apiRainfall != (-10000000000.0))) {
        Gage!![j]!!.rainfall = Gage!![j]!!.apiRainfall
        return
    }
    t += OneSecond
    f1@ while (true) {
        if ((Gage!![j]!!.startDate == ((-693594)).toDouble())) {
            Gage!![j]!!.rainfall = 0.0
            return
        }
        if ((t < Gage!![j]!!.startDate)) {
            Gage!![j]!!.rainfall = 0.0
            return
        }
        if ((t < Gage!![j]!!.endDate)) {
            return
        }
        if ((Gage!![j]!!.nextDate == ((-693594)).toDouble())) {
            Gage!![j]!!.rainfall = 0.0
            return
        }
        if ((t < Gage!![j]!!.nextDate)) {
            Gage!![j]!!.rainfall = 0.0
            return
        }
        Gage!![j]!!.startDate = Gage!![j]!!.nextDate
        Gage!![j]!!.endDate = datetime_addSeconds(Gage!![j]!!.startDate, (Gage!![j]!!.rainInterval).toDouble())
        Gage!![j]!!.rainfall = Gage!![j]!!.nextRainfall
        if (!(getNextRainfall(j) != 0)) {
            Gage!![j]!!.nextDate = ((-693594)).toDouble()
        }
    }
}

// gage.c:406
fun initPastRain(j: Int) {
    var i: Int = 0
    i = 0
    f1@ while ((i <= 48)) {
        Gage!![j]!!.pastRain!![i] = 0.0
        i++
    }
    Gage!![j]!!.pastInterval = 0
}

// gage.c:417
fun gage_updatePastRain(j: Int, __tStep: Int) {
    var tStep: Int = __tStep
    var i: Int = 0
    var t: Int = 0
    var r: Double = 0.0
    r = (Gage!![j]!!.rainfall / 3600.0)
    w1@ while ((tStep > 0)) {
        t = (3600 - Gage!![j]!!.pastInterval)
        if ((tStep > t)) {
            Gage!![j]!!.pastRain!![0] += ((t).toDouble() * r)
            i = 48
            f2@ while ((i > 0)) {
                Gage!![j]!!.pastRain!![i] = Gage!![j]!!.pastRain!![(i - 1)]
                i--
            }
            Gage!![j]!!.pastInterval = 0
            Gage!![j]!!.pastRain!![0] = 0.0
            tStep -= t
        } else {
            Gage!![j]!!.pastRain!![0] += ((tStep).toDouble() * r)
            Gage!![j]!!.pastInterval += tStep
            tStep = 0
        }
    }
}

// gage.c:468
fun gage_getPastRain(j: Int, n: Int): Double {
    var i: Int = 0
    var result: Double = 0.0
    if (((n < 1) || (n > 48))) {
        return 0.0
    }
    i = 1
    f1@ while ((i <= n)) {
        result += Gage!![j]!!.pastRain!![i]
        i++
    }
    return result
}

// gage.c:486
fun gage_getNextRainDate(j: Int, __aDate: Double): Double {
    var aDate: Double = __aDate
    if ((Gage!![j]!!.isUsed == 0)) {
        return aDate
    }
    aDate += OneSecond
    if ((aDate < Gage!![j]!!.startDate)) {
        return Gage!![j]!!.startDate
    }
    if ((aDate < Gage!![j]!!.endDate)) {
        return Gage!![j]!!.endDate
    }
    return Gage!![j]!!.nextDate
}

// gage.c:503
fun gage_getPrecip(j: Int, rainfall: DRef?, snowfall: DRef?): Double {
    rainfall!!.set(0.0)
    snowfall!!.set(0.0)
    if (((!(IgnoreSnowmelt != 0)) && (Temp!!.ta <= Snow!!.snotmp))) {
        snowfall!!.set(((Gage!![j]!!.rainfall * Gage!![j]!!.snowFactor) / UCF(RAINFALL)))
    } else {
        rainfall!!.set((Gage!![j]!!.rainfall / UCF(RAINFALL)))
    }
    return ((rainfall!!.get()) + (snowfall!!.get()))
}

// gage.c:524
fun gage_setReportRainfall(j: Int, __reportDate: Double) {
    var reportDate: Double = __reportDate
    var result: Double = 0.0
    if ((Gage!![j]!!.coGage >= 0)) {
        Gage!![j]!!.reportRainfall = Gage!![Gage!![j]!!.coGage]!!.reportRainfall
        return
    }
    if ((Gage!![j]!!.apiRainfall != (-10000000000.0))) {
        Gage!![j]!!.reportRainfall = Gage!![j]!!.apiRainfall
        return
    }
    reportDate += OneSecond
    if ((reportDate < Gage!![j]!!.endDate)) {
        result = Gage!![j]!!.rainfall
    } else if ((reportDate < Gage!![j]!!.nextDate)) {
        result = 0.0
    } else {
        result = Gage!![j]!!.nextRainfall
    }
    Gage!![j]!!.reportRainfall = result
}

// gage.c:567
fun getFirstRainfall(j: Int): Int {
    var k: Int = 0
    val vFirst = FBox(0.0f)
    val rFirst = DBox(0.0)
    Gage!![j]!!.startDate = ((-693594)).toDouble()
    Gage!![j]!!.rainfall = 0.0
    Gage!![j]!!.rainAccum = 0.0
    if ((Gage!![j]!!.dataSource == RAIN_FILE)) {
        if (((Frain!!.file != null) && (Gage!![j]!!.endFilePos > Gage!![j]!!.startFilePos))) {
            R.fseek(Frain!!.file, Gage!![j]!!.startFilePos, 0)
            R.fread1(DField<TGage>(Gage!![j]!!, { __o -> __o.startDate }, { __o, __x -> __o.startDate = __x }), 8, Frain!!.file)
            R.fread1(vFirst, 4, Frain!!.file)
            Gage!![j]!!.currentFilePos = R.ftell(Frain!!.file)
            Gage!![j]!!.rainfall = convertRainfall(j, (vFirst.v).toDouble())
            return 1
        }
        return 0
    } else {
        k = Gage!![j]!!.tSeries
        if ((k >= 0)) {
            if ((table_getFirstEntry(Tseries!![k], DField<TGage>(Gage!![j]!!, { __o -> __o.startDate }, { __o, __x -> __o.startDate = __x }), rFirst) != 0)) {
                Gage!![j]!!.rainfall = convertRainfall(j, rFirst.v)
                return 1
            }
        }
        return 0
    }
    throw IllegalStateException("getFirstRainfall: no return")
}

// gage.c:624
fun getNextRainfall(j: Int): Int {
    var k: Int = 0
    val vNext = FBox(0.0f)
    val rNext = DBox(0.0)
    Gage!![j]!!.nextRainfall = 0.0
    d1@ do {
        if ((Gage!![j]!!.dataSource == RAIN_FILE)) {
            if (((Frain!!.file != null) && (Gage!![j]!!.currentFilePos < Gage!![j]!!.endFilePos))) {
                R.fseek(Frain!!.file, Gage!![j]!!.currentFilePos, 0)
                R.fread1(DField<TGage>(Gage!![j]!!, { __o -> __o.nextDate }, { __o, __x -> __o.nextDate = __x }), 8, Frain!!.file)
                R.fread1(vNext, 4, Frain!!.file)
                Gage!![j]!!.currentFilePos = R.ftell(Frain!!.file)
                rNext.v = convertRainfall(j, (vNext.v).toDouble())
            } else {
                return 0
            }
        } else {
            k = Gage!![j]!!.tSeries
            if ((k >= 0)) {
                if (!(table_getNextEntry(Tseries!![k], DField<TGage>(Gage!![j]!!, { __o -> __o.nextDate }, { __o, __x -> __o.nextDate = __x }), rNext) != 0)) {
                    return 0
                }
                rNext.v = convertRainfall(j, rNext.v)
            } else {
                return 0
            }
        }
    } while ((rNext.v == 0.0))
    Gage!![j]!!.nextRainfall = rNext.v
    return 1
}

// gage.c:674
fun convertRainfall(j: Int, r: Double): Double {
    var r1: Double = 0.0
    when (Gage!![j]!!.rainType) {
        RAINFALL_INTENSITY -> {
            r1 = r
        }
        RAINFALL_VOLUME -> {
            r1 = ((r / (Gage!![j]!!.rainInterval).toDouble()) * 3600.0)
        }
        CUMULATIVE_RAINFALL -> {
            if ((r < Gage!![j]!!.rainAccum)) {
                r1 = ((r / (Gage!![j]!!.rainInterval).toDouble()) * 3600.0)
            } else {
                r1 = ((((r - Gage!![j]!!.rainAccum)) / (Gage!![j]!!.rainInterval).toDouble()) * 3600.0)
            }
            Gage!![j]!!.rainAccum = r
        }
        else -> {
            r1 = r
        }
    }
    return ((r1 * Gage!![j]!!.unitsFactor) * Adjust!!.rainFactor)
}

