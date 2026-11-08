// Generated from EPA SWMM 5.2.4 (rain.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_rain")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var RainStats: TRainStats? = TRainStats()
@JvmField var Condition: Int = 0
@JvmField var TimeOffset: Int = 0
@JvmField var DataOffset: Int = 0
@JvmField var ValueOffset: Int = 0
@JvmField var RainType: Int = 0
@JvmField var Interval: Int = 0
@JvmField var UnitsFactor: Double = 0.0
@JvmField var RainAccum: Float = 0.0f
@JvmField var StationID: CPtr? = null
@JvmField var AccumStartDate: Double = 0.0
@JvmField var PreviousDate: Double = 0.0
@JvmField var GageIndex: Int = 0
@JvmField var hasStationName: Int = 0

// rain.c:118
fun rain_open() {
    var i: Int = 0
    var count: Int = 0
    count = 0
    i = 0
    f1@ while ((i < Nobjects!![GAGE])) {
        if ((Gage!![i]!!.dataSource == RAIN_FILE)) {
            count++
        }
        i++
    }
    Frain!!.file = (null)
    if ((count == 0)) {
        Frain!!.mode = NO_FILE
    } else {
        when (Frain!!.mode) {
            SCRATCH_FILE -> {
                getTempFileName(Frain!!.name)
                if ((((R.fopen(Frain!!.name, __L166)).also { __v -> Frain!!.file = __v }) == null)) {
                    report_writeErrorMsg(ERR_RAIN_FILE_SCRATCH, __L0)
                    return
                }
            }
            USE_FILE -> {
                if ((((R.fopen(Frain!!.name, __L165)).also { __v -> Frain!!.file = __v }) == null)) {
                    report_writeErrorMsg(ERR_RAIN_FILE_OPEN, Frain!!.name)
                    return
                }
            }
            SAVE_FILE -> {
                if ((((R.fopen(Frain!!.name, __L166)).also { __v -> Frain!!.file = __v }) == null)) {
                    report_writeErrorMsg(ERR_RAIN_FILE_OPEN, Frain!!.name)
                    return
                }
            }
        }
    }
    if (((Frain!!.mode == SCRATCH_FILE) || (Frain!!.mode == SAVE_FILE))) {
        createRainFile(count)
    }
    if ((Frain!!.mode != NO_FILE)) {
        initRainFile()
    }
    rdii_openRdii()
}

// rain.c:184
fun rain_close() {
    if ((Frain!!.file != null)) {
        R.fclose(Frain!!.file)
        if ((Frain!!.mode == SCRATCH_FILE)) {
            R.remove(Frain!!.name)
        }
    }
    Frain!!.file = (null)
    rdii_closeRdii()
}

// rain.c:202
fun createRainFile(count: Int) {
    var i: Int = 0
    var k: Int = 0
    val kount = IBox(count)
    var filePos1: Int = 0
    val filePos2 = IBox(0)
    val filePos3 = IBox(0)
    val interval = IBox(0)
    val dummy = IBox((-1))
    var staID: CPtr? = R.buf(1025)
    var fileStamp: CPtr? = R.bufFrom(__L327, 11)
    if (((ErrorCode != 0) || (!(Frain!!.file != null)))) {
        return
    }
    R.fwrite(fileStamp, 0, (R.strlen(fileStamp)).toInt(), Frain!!.file)
    R.fwrite1(kount.v, 4, Frain!!.file)
    filePos1 = (R.ftell(Frain!!.file)).toInt()
    if ((count > 0)) {
        report_writeRainStats((-1), RainStats)
    }
    R.strcpy(staID, __L33)
    i = 0
    f1@ while ((i < count)) {
        R.fwrite(staID, 0, (((1024 + 1)).toLong()).toInt(), Frain!!.file)
        k = 1
        f2@ while ((k <= 3)) {
            R.fwrite1(dummy.v, 4, Frain!!.file)
            k++
        }
        i++
    }
    filePos2.v = (R.ftell(Frain!!.file)).toInt()
    i = 0
    f3@ while ((i < Nobjects!![GAGE])) {
        if (((ErrorCode != 0) || (Gage!![i]!!.dataSource != RAIN_FILE))) {
            i++
            continue@f3
        }
        if ((rainFileConflict(i) != 0)) {
            break@f3
        }
        R.fseek(Frain!!.file, (filePos2.v).toLong(), 0)
        if ((addGageToRainFile(i) != 0)) {
            filePos3.v = (R.ftell(Frain!!.file)).toInt()
            R.fseek(Frain!!.file, (filePos1).toLong(), 0)
            sstrncpy(staID, Gage!![i]!!.staID, 1024L)
            interval.v = Interval
            R.fwrite(staID, 0, (((1024 + 1)).toLong()).toInt(), Frain!!.file)
            R.fwrite1(interval.v, 4, Frain!!.file)
            R.fwrite1(filePos2.v, 4, Frain!!.file)
            R.fwrite1(filePos3.v, 4, Frain!!.file)
            filePos1 = (R.ftell(Frain!!.file)).toInt()
            filePos2.v = filePos3.v
            report_writeRainStats(i, RainStats)
        }
        i++
    }
    if ((ErrorCode != 0)) {
        R.fclose(Frain!!.file)
        Frain!!.file = (null)
        R.remove(Frain!!.name)
    }
}

// rain.c:278
fun rainFileConflict(i: Int): Int {
    var j: Int = 0
    var staID: CPtr? = Gage!![i]!!.staID
    var fname: CPtr? = Gage!![i]!!.fname
    j = 0
    f1@ while ((j < i)) {
        if (((strcomp(Gage!![j]!!.staID, staID) != 0) && (!(strcomp(Gage!![j]!!.fname, fname) != 0)))) {
            report_writeErrorMsg(ERR_RAIN_FILE_CONFLICT, Gage!![i]!!.ID)
            return 1
        }
        j++
    }
    return 0
}

// rain.c:302
fun addGageToRainFile(i: Int): Int {
    var f: CFile? = null
    var fileFormat: Int = 0
    val hdrLines = IBox(0)
    StationID = null
    if ((((R.fopen(Gage!![i]!!.fname, __L13)).also { __v -> f = __v }) == null)) {
        report_writeErrorMsg(ERR_RAIN_FILE_DATA, Gage!![i]!!.fname)
    } else {
        fileFormat = findFileFormat(f, i, hdrLines)
        if ((fileFormat == UNKNOWN_FORMAT)) {
            report_writeErrorMsg(ERR_RAIN_FILE_FORMAT, Gage!![i]!!.fname)
        } else {
            GageIndex = i
            readFile(f, fileFormat, hdrLines.v, Gage!![i]!!.startFileDate, Gage!![i]!!.endFileDate)
        }
        R.fclose(f)
    }
    if ((ErrorCode != 0)) {
        return 0
    } else {
        return 1
    }
    throw IllegalStateException("addGageToRainFile: no return")
}

// rain.c:341
fun initRainFile() {
    var fileStamp: CPtr? = R.bufFrom(__L327, 11)
    var fStamp: CPtr? = R.bufFrom(__L327, 11)
    var i: Int = 0
    val kount = IBox(0)
    var filePos: Long = 0L
    if (((ErrorCode != 0) || (!(Frain!!.file != null)))) {
        return
    }
    R.rewind(Frain!!.file)
    R.fread(fStamp, 0, (R.strlen(fileStamp)).toInt(), Frain!!.file)
    if ((R.strcmp(fStamp, fileStamp) != 0)) {
        report_writeErrorMsg(ERR_RAIN_IFACE_FORMAT, __L0)
        return
    }
    R.fread1(kount, 4, Frain!!.file)
    filePos = R.ftell(Frain!!.file)
    i = 0
    f1@ while ((i < Nobjects!![GAGE])) {
        if (((ErrorCode != 0) || (Gage!![i]!!.dataSource != RAIN_FILE))) {
            i++
            continue@f1
        }
        R.fseek(Frain!!.file, filePos, 0)
        if (((!(findGageInFile(i, kount.v) != 0)) || (Gage!![i]!!.startFilePos == Gage!![i]!!.endFilePos))) {
            report_writeErrorMsg(ERR_RAIN_FILE_GAGE, Gage!![i]!!.ID)
        }
        i++
    }
}

// rain.c:385
fun findGageInFile(i: Int, kount: Int): Int {
    var k: Int = 0
    val interval = IBox(0)
    val filePos1 = IBox(0)
    val filePos2 = IBox(0)
    var staID: CPtr? = R.bufFrom(__L0, 1025)
    k = 1
    f1@ while ((k <= kount)) {
        R.fread(staID, 0, (((1024 + 1)).toLong()).toInt(), Frain!!.file)
        R.fread1(interval, 4, Frain!!.file)
        R.fread1(filePos1, 4, Frain!!.file)
        R.fread1(filePos2, 4, Frain!!.file)
        if ((R.strcmp(staID, Gage!![i]!!.staID) == 0)) {
            Gage!![i]!!.rainType = RAINFALL_VOLUME
            Gage!![i]!!.rainInterval = interval.v
            Gage!![i]!!.startFilePos = (filePos1.v).toLong()
            Gage!![i]!!.endFilePos = (filePos2.v).toLong()
            Gage!![i]!!.currentFilePos = Gage!![i]!!.startFilePos
            return 1
        }
        k++
    }
    return 0
}

// rain.c:420
fun findFileFormat(f: CFile?, i: Int, hdrLines: IRef?): Int {
    var fileFormat: Int = 0
    var lineCount: Int = 0
    var maxCount: Int = 5
    var n: Int = 0
    val div = IBox(0)
    val sn2 = LBox(0L)
    var recdType: CPtr? = R.buf(4)
    var elemType: CPtr? = R.buf(5)
    var coopID: CPtr? = R.bufFrom(__L0, 6)
    var line: CPtr? = R.buf(1024)
    val year = IBox(0)
    val month = IBox(0)
    val day = IBox(0)
    val hour = IBox(0)
    val minute = IBox(0)
    val elem = IBox(0)
    val x = FBox(0.0f)
    fileFormat = UNKNOWN_FORMAT
    hasStationName = 0
    UnitsFactor = 1.0
    Interval = 0
    hdrLines!!.set(0)
    lineCount = 1
    f1@ while ((lineCount <= maxCount)) {
        if (((R.fgets(line, 1024, f)) == null)) {
            return fileFormat
        }
        n = R.sscanf(line, __L328, sn2, div, elemType)
        if ((n == 3)) {
            Interval = getNWSInterval(elemType)
            TimeOffset = Interval
            if ((Interval > 0)) {
                fileFormat = NWS_SPACE_DELIMITED
                break@f1
            }
        }
        n = R.sscanf(line!!.add(37), __L329, div, elemType, recdType, year)
        if ((n == 4)) {
            Interval = getNWSInterval(elemType)
            TimeOffset = Interval
            if ((Interval > 0)) {
                fileFormat = NWS_SPACE_DELIMITED
                hasStationName = 1
                break@f1
            }
        }
        n = R.sscanf(line, __L330, sn2, div, elemType)
        if ((n == 3)) {
            Interval = getNWSInterval(elemType)
            TimeOffset = Interval
            if ((Interval > 0)) {
                fileFormat = NWS_COMMA_DELIMITED
                break@f1
            }
        }
        n = R.sscanf(line!!.add(37), __L331, div, elemType, recdType, year)
        if ((n == 4)) {
            Interval = getNWSInterval(elemType)
            TimeOffset = Interval
            if ((Interval > 0)) {
                fileFormat = NWS_COMMA_DELIMITED
                hasStationName = 1
                break@f1
            }
        }
        n = R.sscanf(line, __L332, recdType, sn2, div, elemType)
        if ((n == 4)) {
            Interval = getNWSInterval(elemType)
            TimeOffset = Interval
            if ((Interval > 0)) {
                fileFormat = NWS_TAPE
                break@f1
            }
        }
        n = R.sscanf(line, __L333, coopID, sn2)
        if (((n == 2) && (R.strcmp(coopID, __L334) == 0))) {
            fileFormat = findNWSOnlineFormat(f, line)
            break@f1
        }
        n = R.sscanf(line, __L335, sn2, year, month, day, elem)
        if ((n == 5)) {
            if (((elem.v == 123) && (R.strlen(line) >= 185L))) {
                fileFormat = AES_HLY
                Interval = 3600
                TimeOffset = Interval
                UnitsFactor = (1.0 / 25.4)
                break@f1
            }
        }
        n = R.sscanf(line, __L336, sn2, year, month, day, elem)
        if ((n == 5)) {
            if (((elem.v == 159) && (R.strlen(line) >= 691L))) {
                fileFormat = CMC_FIF
                Interval = 900
            } else if (((elem.v == 123) && (R.strlen(line) >= 186L))) {
                fileFormat = CMC_HLY
                Interval = 3600
            }
            if (((fileFormat == CMC_FIF) || (fileFormat == CMC_HLY))) {
                TimeOffset = Interval
                UnitsFactor = (1.0 / 25.4)
                break@f1
            }
        }
        if ((parseStdLine(line, year, month, day, hour, minute, x) != 0)) {
            fileFormat = STD_SPACE_DELIMITED
            RainType = Gage!![i]!!.rainType
            Interval = Gage!![i]!!.rainInterval
            if ((Gage!![i]!!.rainUnits == SI)) {
                UnitsFactor = (1.0 / 25.4)
            }
            TimeOffset = 0
            StationID = Gage!![i]!!.staID
            break@f1
        }
        hdrLines!!.set(hdrLines!!.get() + 1)
        lineCount++
    }
    if ((fileFormat != UNKNOWN_FORMAT)) {
        Gage!![i]!!.rainInterval = Interval
    }
    return fileFormat
}

// rain.c:584
fun findNWSOnlineFormat(f: CFile?, line: CPtr?): Int {
    var n: Int = 0
    var fileFormat: Int = UNKNOWN_FORMAT
    var str: CPtr? = null
    R.rewind(f)
    R.fgets(line, 1024, f)
    if ((((R.strstr(line, __L337)).also { __v -> str = __v }) != null)) {
        Interval = 3600
        TimeOffset = Interval
        ValueOffset = (((str!!.o - line!!.o).toLong())).toInt()
        fileFormat = NWS_ONLINE_60
    } else if ((((R.strstr(line, __L338)).also { __v -> str = __v }) != null)) {
        Interval = 900
        TimeOffset = Interval
        ValueOffset = (((str!!.o - line!!.o).toLong())).toInt()
        fileFormat = NWS_ONLINE_15
    } else {
        return UNKNOWN_FORMAT
    }
    n = 1
    f1@ while ((n <= 5)) {
        if (((R.fgets(line, 1024, f)) == null)) {
            return UNKNOWN_FORMAT
        }
        if (((R.strstr(line, __L334)) == null)) {
            n++
            continue@f1
        }
        str = R.strrchr(line, 58)
        if ((str == null)) {
            return UNKNOWN_FORMAT
        }
        n = (((str!!.o - line!!.o).toLong())).toInt()
        DataOffset = (n - 11)
        return fileFormat
    }
    return UNKNOWN_FORMAT
}

// rain.c:641
fun getNWSInterval(elemType: CPtr?): Int {
    if ((R.strcmp(elemType, __L337) == 0)) {
        return 3600
    } else if ((R.strcmp(elemType, __L338) == 0)) {
        return 900
    } else if ((R.strcmp(elemType, __L339) == 0)) {
        return 900
    } else {
        return 0
    }
    throw IllegalStateException("getNWSInterval: no return")
}

// rain.c:656
fun readFile(f: CFile?, fileFormat: Int, hdrLines: Int, day1: Double, day2: Double) {
    var line: CPtr? = R.buf(1024)
    var i: Int = 0
    var n: Int = 0
    R.rewind(f)
    RainStats!!.startDate = ((-693594)).toDouble()
    RainStats!!.endDate = ((-693594)).toDouble()
    RainStats!!.periodsRain = 0L
    RainStats!!.periodsMissing = 0L
    RainStats!!.periodsMalfunc = 0L
    RainAccum = (0.0).toFloat()
    AccumStartDate = ((-693594)).toDouble()
    PreviousDate = ((-693594)).toDouble()
    i = 1
    f1@ while ((i <= hdrLines)) {
        if (((R.fgets(line, 1024, f)) == null)) {
            return
        }
        i++
    }
    w2@ while (((R.fgets(line, 1024, f)) != null)) {
        when (fileFormat) {
            STD_SPACE_DELIMITED -> {
                n = readStdLine(line, day1, day2)
            }
            NWS_TAPE, NWS_SPACE_DELIMITED, NWS_COMMA_DELIMITED, NWS_ONLINE_60, NWS_ONLINE_15 -> {
                n = readNWSLine(line, fileFormat, day1, day2)
            }
            AES_HLY, CMC_FIF, CMC_HLY -> {
                n = readCMCLine(line, fileFormat, day1, day2)
            }
            else -> {
                n = (-1)
            }
        }
        if ((n < 0)) {
            break@w2
        }
    }
}

// rain.c:717
fun readNWSLine(line: CPtr?, fileFormat: Int, day1: Double, day2: Double): Int {
    val flag1 = IBox(0)
    val flag2 = IBox(0)
    var isMissing: Int = 0
    var date1: Double = 0.0
    var result: Long = 1L
    var k: Int = 0
    val y = IBox(0)
    val m = IBox(0)
    val d = IBox(0)
    val n = IBox(0)
    val hour = IBox(0)
    val minute = IBox(0)
    val v = LBox(0L)
    var x: Float = 0.0f
    var lineLength: Int = ((R.strlen(line)).toInt() - 1)
    var nameLength: Int = 0
    when (fileFormat) {
        NWS_TAPE -> {
            if ((lineLength <= 30)) {
                return 0
            }
            if ((R.sscanf(line!!.add(17), __L340, y, m, d, n) < 4)) {
                return 0
            }
            k = 30
        }
        NWS_SPACE_DELIMITED -> {
            if ((hasStationName != 0)) {
                nameLength = 31
            }
            if ((lineLength <= (28 + nameLength))) {
                return 0
            }
            k = (18 + nameLength)
            if ((R.sscanf(line!!.add(k), __L341, y, m, d) < 3)) {
                return 0
            }
            k = (k + 10)
        }
        NWS_COMMA_DELIMITED -> {
            if ((lineLength <= 28)) {
                return 0
            }
            if ((R.sscanf(line!!.add(18), __L342, y, m, d) < 3)) {
                return 0
            }
            k = 28
        }
        NWS_ONLINE_60, NWS_ONLINE_15 -> {
            if ((lineLength <= (DataOffset + 23))) {
                return 0
            }
            if ((R.sscanf(line!!.add(DataOffset), __L27, y, m, d) < 3)) {
                return 0
            }
            k = (DataOffset + 8)
        }
        else -> {
            return 0
        }
    }
    date1 = datetime_encodeDate(y.v, m.v, d.v)
    if (((day1 != ((-693594)).toDouble()) && (date1 < day1))) {
        return 0
    }
    if (((day2 != ((-693594)).toDouble()) && (date1 > day2))) {
        return (-1)
    }
    w2@ while ((k < lineLength)) {
        flag1.v = 0
        flag2.v = 0
        v.v = 99999L
        hour.v = 25
        minute.v = 0
        when (fileFormat) {
            NWS_TAPE -> {
                n.v = R.sscanf(line!!.add(k), __L343, hour, minute, v, flag1, flag2)
                k += 12
            }
            NWS_SPACE_DELIMITED -> {
                n.v = R.sscanf(line!!.add(k), __L344, hour, minute, v, flag1, flag2)
                k += 16
            }
            NWS_COMMA_DELIMITED -> {
                n.v = R.sscanf(line!!.add(k), __L345, hour, minute, v, flag1, flag2)
                k += 16
            }
            NWS_ONLINE_60, NWS_ONLINE_15 -> {
                n.v = R.sscanf(line!!.add(k), __L346, hour, minute)
                val __c1 = R.buf(2); __c1.s(0, flag1.v)
                val __c2 = R.buf(2); __c2.s(0, flag1.v)
                n.v += (readNwsOnlineValue(line!!.add(ValueOffset), v, __c2))
                flag1.v = __c1.g(0)
                flag1.v = __c2.g(0)
                if ((hour.v == 0)) {
                    hour.v = 24
                    date1 -= 1.0
                }
                k += lineLength
            }
            else -> {
                n.v = 0
            }
        }
        if (((n.v < 3) || (hour.v >= 25))) {
            break@w2
        }
        setCondition(flag1.v)
        if ((((Condition == DELETED_PERIOD) || (Condition == MISSING_PERIOD)) || (flag1.v == 77))) {
            isMissing = 1
        } else if ((v.v >= 9999L)) {
            isMissing = 1
        } else {
            isMissing = 0
        }
        if ((flag1.v == 97)) {
            AccumStartDate = (date1 + datetime_encodeTime(hour.v, minute.v, 0))
        } else if ((flag1.v == 65)) {
            saveAccumRainfall(date1, hour.v, minute.v, v.v)
        } else {
            x = ((v.v).toFloat() / 100.0f)
            if (((x > 0.0f) || (isMissing != 0))) {
                saveRainfall(date1, hour.v, minute.v, x, isMissing)
            }
        }
        if ((((flag1.v == 65) || (flag1.v == 125)) || (flag1.v == 93))) {
            Condition = 0
        }
    }
    return (result).toInt()
}

// rain.c:862
fun readNwsOnlineValue(s: CPtr?, v: LRef?, flag: CPtr?): Int {
    var n: Int = 0
    val x = FBox(99.98999786376953f)
    if ((R.strchr(s, 46) != null)) {
        n = R.sscanf(s, __L347, x, flag)
        v!!.set(R.D2L(((((100.0f * x.v) + 0.5f))).toDouble()))
    } else {
        n = R.sscanf(s, __L348, v, flag)
    }
    return n
}

// rain.c:891
fun setCondition(flag: Int) {
    when (flag) {
        97, 65 -> {
            Condition = ACCUMULATED_PERIOD
        }
        123, 125 -> {
            Condition = DELETED_PERIOD
        }
        91, 93 -> {
            Condition = MISSING_PERIOD
        }
        else -> {
            Condition = NO_CONDITION
        }
    }
}

// rain.c:914
fun readCMCLine(line: CPtr?, fileFormat: Int, day1: Double, day2: Double): Int {
    val flag = IBox(0)
    var isMissing: Int = 0
    var date1: Double = 0.0
    val sn = LBox(0L)
    val v = LBox(0L)
    var col: Int = 0
    var j: Int = 0
    var jMax: Int = 0
    val elem = IBox(0)
    val y = IBox(0)
    val m = IBox(0)
    val d = IBox(0)
    var hour: Int = 0
    var minute: Int = 0
    var x: Float = 0.0f
    if ((fileFormat == AES_HLY)) {
        if ((R.sscanf(line, __L335, sn, y, m, d, elem) < 5)) {
            return 0
        }
        if ((y.v < 100)) {
            y.v = (y.v + 2000)
        } else {
            y.v = (y.v + 1000)
        }
        col = 17
    } else {
        if ((R.sscanf(line, __L336, sn, y, m, d, elem) < 5)) {
            return 0
        }
        col = 18
    }
    date1 = datetime_encodeDate(y.v, m.v, d.v)
    if (((day1 != ((-693594)).toDouble()) && (date1 < day1))) {
        return 0
    }
    if (((day2 != ((-693594)).toDouble()) && (date1 > day2))) {
        return (-1)
    }
    if (((fileFormat == AES_HLY) && (elem.v != 123))) {
        return 0
    } else if (((fileFormat == CMC_FIF) && (elem.v != 159))) {
        return 0
    } else if (((fileFormat == CMC_HLY) && (elem.v != 123))) {
        return 0
    }
    hour = 0
    minute = 0
    jMax = 24
    if ((fileFormat == CMC_FIF)) {
        jMax = 96
    }
    j = 1
    f1@ while ((j <= jMax)) {
        if ((R.sscanf(line!!.add(col), __L349, v, flag) < 2)) {
            return 0
        }
        col += 7
        if ((v.v == ((-99999)).toLong())) {
            isMissing = 1
        } else {
            isMissing = 0
        }
        x = (((((v.v).toDouble() / 10.0) / 25.4))).toFloat()
        if (((x > 0.0f) || (isMissing != 0))) {
            saveRainfall(date1, hour, minute, x, isMissing)
        }
        if ((fileFormat == CMC_FIF)) {
            minute += 15
            if ((minute == 60)) {
                minute = 0
                hour++
            }
        } else {
            hour++
        }
        j++
    }
    return 1
}

// rain.c:994
fun readStdLine(line: CPtr?, day1: Double, day2: Double): Int {
    var date1: Double = 0.0
    var date2: Double = 0.0
    val year = IBox(0)
    val month = IBox(0)
    val day = IBox(0)
    val hour = IBox(0)
    val minute = IBox(0)
    val x = FBox(0.0f)
    if (!(parseStdLine(line, year, month, day, hour, minute, x) != 0)) {
        return 0
    }
    date1 = datetime_encodeDate(year.v, month.v, day.v)
    if (((day1 != ((-693594)).toDouble()) && (date1 < day1))) {
        return 0
    }
    if (((day2 != ((-693594)).toDouble()) && (date1 > day2))) {
        return (-1)
    }
    date2 = (date1 + datetime_encodeTime(hour.v, minute.v, 0))
    if ((date2 <= PreviousDate)) {
        report_writeErrorMsg(ERR_RAIN_FILE_SEQUENCE, Gage!![GageIndex]!!.fname)
        report_writeLine(line)
        return (-1)
    }
    PreviousDate = date2
    when (RainType) {
        RAINFALL_INTENSITY -> {
            x.v = ((x.v * (Interval).toFloat()) / 3600.0f)
        }
        CUMULATIVE_RAINFALL -> {
            if ((x.v >= RainAccum)) {
                x.v = (x.v - RainAccum)
                RainAccum += x.v
            } else {
                RainAccum = x.v
            }
        }
    }
    x.v *= ((UnitsFactor).toFloat())
    saveRainfall(date1, hour.v, minute.v, x.v, 0)
    return 1
}

// rain.c:1052
fun parseStdLine(line: CPtr?, year: IRef?, month: IRef?, day: IRef?, hour: IRef?, minute: IRef?, value_: FRef?): Int {
    var n: Int = 0
    var token: CPtr? = R.buf(1024)
    n = R.sscanf(line, __L350, token, year, month, day, hour, minute, value_)
    if ((n < 7)) {
        return 0
    }
    if (((StationID != null) && (!(strcomp(token, StationID) != 0)))) {
        return 0
    }
    return 1
}

// rain.c:1078
fun saveAccumRainfall(date1: Double, hour: Int, minute: Int, v: Long) {
    val date2 = DBox(0.0)
    var n: Int = 0
    var j: Int = 0
    val x = FBox(0.0f)
    if ((AccumStartDate == ((-693594)).toDouble())) {
        return
    }
    date2.v = (date1 + datetime_encodeTime(hour, minute, 0))
    n = ((((datetime_timeDiff(date2.v, AccumStartDate) / (Interval).toLong())) + 1L)).toInt()
    if ((v == 99999L)) {
        RainStats!!.periodsMissing += ((n).toLong())
        return
    }
    RainStats!!.periodsRain += ((n).toLong())
    x.v = (((v).toFloat() / (n).toFloat()) / 100.0f)
    if ((x.v > 0.0f)) {
        date2.v = datetime_addSeconds(AccumStartDate, ((-TimeOffset)).toDouble())
        if ((RainStats!!.startDate == ((-693594)).toDouble())) {
            RainStats!!.startDate = date2.v
        }
        j = 0
        f1@ while ((j < n)) {
            R.fwrite1(date2.v, 8, Frain!!.file)
            R.fwrite1(x.v, 4, Frain!!.file)
            date2.v = datetime_addSeconds(date2.v, (Interval).toDouble())
            RainStats!!.endDate = date2.v
            j++
        }
    }
    AccumStartDate = ((-693594)).toDouble()
}

// rain.c:1133
fun saveRainfall(date1: Double, hour: Int, minute: Int, __x: Float, isMissing: Int) {
    val x = FBox(__x)
    val date2 = DBox(0.0)
    var seconds: Int = 0
    if ((isMissing != 0)) {
        RainStats!!.periodsMissing++
    } else {
        RainStats!!.periodsRain++
    }
    if (!(isMissing != 0)) {
        seconds = (((3600 * hour) + (60 * minute)) - TimeOffset)
        date2.v = datetime_addSeconds(date1, (seconds).toDouble())
        R.fwrite1(date2.v, 8, Frain!!.file)
        R.fwrite1(x.v, 4, Frain!!.file)
        if ((RainStats!!.startDate == ((-693594)).toDouble())) {
            RainStats!!.startDate = date2.v
        }
        RainStats!!.endDate = date2.v
    }
}

