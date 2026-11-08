// Generated from EPA SWMM 5.2.4 (datetime.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_datetime")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MonthTxt: Array<CPtr?>? = arrayOf<CPtr?>(__L695, __L696, __L697, __L698, __L699, __L700, __L701, __L702, __L703, __L704, __L705, __L706, null)
@JvmField var DaysPerMonth: Array<IntArray?>? = arrayOf<IntArray?>(intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31), intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31))
@JvmField var DateDelta: Int = 693594
@JvmField var SecsPerDay: Double = 86400.0
@JvmField var DateFormat: Int = 0

// datetime.c:51
fun divMod(n: Int, d: Int, result: IRef?, remainder: IRef?) {
    if ((d == 0)) {
        result!!.set(0)
        remainder!!.set(0)
    } else {
        result!!.set((n / d))
        remainder!!.set((n - (d * (result!!.get()))))
    }
}

// datetime.c:74
fun isLeapYear(year: Int): Int {
    if (((((year % 4) == 0)) && (((((year % 100) != 0)) || (((year % 400) == 0)))))) {
        return 1
    } else {
        return 0
    }
    throw IllegalStateException("isLeapYear: no return")
}

// datetime.c:89
fun datetime_findMonth(month: CPtr?): Int {
    var i: Int = 0
    i = 0
    f1@ while ((i < 12)) {
        if ((((((if (((((month!!.g(0)) >= 97) && ((month!!.g(0)) <= 122)))) (((month!!.g(0)) and 32.inv())) else (month!!.g(0)))) == MonthTxt!![i]!!.g(0)) && (((if (((((month!!.g(1)) >= 97) && ((month!!.g(1)) <= 122)))) (((month!!.g(1)) and 32.inv())) else (month!!.g(1)))) == MonthTxt!![i]!!.g(1))) && (((if (((((month!!.g(2)) >= 97) && ((month!!.g(2)) <= 122)))) (((month!!.g(2)) and 32.inv())) else (month!!.g(2)))) == MonthTxt!![i]!!.g(2)))) {
            return (i + 1)
        }
        i++
    }
    return 0
}

// datetime.c:108
fun datetime_encodeDate(year: Int, month: Int, __day: Int): Double {
    var day: Int = __day
    var i: Int = 0
    var j: Int = 0
    i = isLeapYear(year)
    if ((((((((year >= 1)) && ((year <= 9999))) && ((month >= 1))) && ((month <= 12))) && ((day >= 1))) && ((day <= DaysPerMonth!![i]!![(month - 1)])))) {
        j = 0
        f1@ while ((j < (month - 1))) {
            day += DaysPerMonth!![i]!![j]
            j++
        }
        i = (year - 1)
        i = ((((((i * 365) + (i / 4)) - (i / 100)) + (i / 400)) + day) - DateDelta)
        return (i).toDouble()
    } else {
        return ((-DateDelta)).toDouble()
    }
    throw IllegalStateException("datetime_encodeDate: no return")
}

// datetime.c:136
fun datetime_encodeTime(hour: Int, minute: Int, second: Int): Double {
    var s: Int = 0
    if (((((hour >= 0)) && ((minute >= 0))) && ((second >= 0)))) {
        s = ((((hour * 3600) + (minute * 60)) + second))
        return ((s).toDouble() / SecsPerDay)
    } else {
        return 0.0
    }
    throw IllegalStateException("datetime_encodeTime: no return")
}

// datetime.c:158
fun datetime_decodeDate(date: Double, year: IRef?, month: IRef?, day: IRef?) {
    var D1: Int = 0
    var D4: Int = 0
    var D100: Int = 0
    var D400: Int = 0
    var y: Int = 0
    var m: Int = 0
    val d = IBox(0)
    val i = IBox(0)
    var k: Int = 0
    var t: Int = 0
    D1 = 365
    D4 = ((D1 * 4) + 1)
    D100 = ((D4 * 25) - 1)
    D400 = ((D100 * 4) + 1)
    t = (R.D2I((floor(date))) + DateDelta)
    if ((t <= 0)) {
        year!!.set(0)
        month!!.set(1)
        day!!.set(1)
    } else {
        t--
        y = 1
        w1@ while ((t >= D400)) {
            t -= D400
            y += 400
        }
        divMod(t, D100, i, d)
        if ((i.v == 4)) {
            i.v--
            d.v += D100
        }
        y += (i.v * 100)
        divMod(d.v, D4, i, d)
        y += (i.v * 4)
        divMod(d.v, D1, i, d)
        if ((i.v == 4)) {
            i.v--
            d.v += D1
        }
        y += i.v
        k = isLeapYear(y)
        m = 1
        f2@ while (true) {
            i.v = DaysPerMonth!![k]!![(m - 1)]
            if ((d.v < i.v)) {
                break@f2
            }
            d.v -= i.v
            m++
        }
        year!!.set(y)
        month!!.set(m)
        day!!.set((d.v + 1))
    }
}

// datetime.c:224
fun datetime_decodeTime(time: Double, h: IRef?, m: IRef?, s: IRef?) {
    var secs: Int = 0
    val mins = IBox(0)
    var fracDay: Double = (((time - floor(time))) * SecsPerDay)
    secs = R.D2I((floor((fracDay + 0.5))))
    if ((secs >= 86400)) {
        secs = 86399
    }
    divMod(secs, 60, mins, s)
    divMod(mins.v, 60, h, m)
    if ((h!!.get() > 23)) {
        h!!.set(0)
    }
}

// datetime.c:245
fun datetime_dateToStr(date: Double, s: CPtr?) {
    val y = IBox(0)
    val m = IBox(0)
    val d = IBox(0)
    datetime_decodeDate(date, y, m, d)
    when (DateFormat) {
        0 -> {
            R.snprintf(s, (12L).toInt(), __L36, y.v, MonthTxt!![(m.v - 1)], d.v)
        }
        1 -> {
            R.snprintf(s, (12L).toInt(), __L37, m.v, d.v, y.v)
        }
        else -> {
            R.snprintf(s, (12L).toInt(), __L38, d.v, MonthTxt!![(m.v - 1)], y.v)
        }
    }
}

// datetime.c:270
fun datetime_timeToStr(time: Double, s: CPtr?) {
    val hr = IBox(0)
    val min = IBox(0)
    val sec = IBox(0)
    datetime_decodeTime(time, hr, min, sec)
    R.snprintf(s, (9L).toInt(), __L39, hr.v, min.v, sec.v)
}

// datetime.c:284
fun datetime_strToDate(s: CPtr?, d: DRef?): Int {
    val yr = IBox(0)
    val mon = IBox(0)
    val day = IBox(0)
    var n: Int = 0
    var month: CPtr? = R.buf(4)
    val sep1 = IBox(0)
    val sep2 = IBox(0)
    d!!.set(((-DateDelta)).toDouble())
    if (((R.strchr(s, 45) != null) || (R.strchr(s, 47) != null))) {
        when (DateFormat) {
            0 -> {
                n = R.sscanf(s, __L40, yr, sep1, mon, sep2, day)
                if ((n < 3)) {
                    mon.v = 0
                    n = R.sscanf(s, __L41, yr, sep1, month, sep2, day)
                    if ((n < 3)) {
                        return 0
                    }
                }
            }
            2 -> {
                n = R.sscanf(s, __L40, day, sep1, mon, sep2, yr)
                if ((n < 3)) {
                    mon.v = 0
                    n = R.sscanf(s, __L41, day, sep1, month, sep2, yr)
                    if ((n < 3)) {
                        return 0
                    }
                }
            }
            else -> {
                n = R.sscanf(s, __L40, mon, sep1, day, sep2, yr)
                if ((n < 3)) {
                    mon.v = 0
                    n = R.sscanf(s, __L42, month, sep1, day, sep2, yr)
                    if ((n < 3)) {
                        return 0
                    }
                }
            }
        }
        if ((mon.v == 0)) {
            mon.v = datetime_findMonth(month)
        }
        d!!.set(datetime_encodeDate(yr.v, mon.v, day.v))
    }
    if ((d!!.get() == ((-DateDelta)).toDouble())) {
        return 0
    } else {
        return 1
    }
    throw IllegalStateException("datetime_strToDate: no return")
}

// datetime.c:338
fun datetime_strToTime(s: CPtr?, t: DRef?): Int {
    var n: Int = 0
    val hr = IBox(0)
    val min = IBox(0)
    val sec = IBox(0)
    val endptr = OBox<CPtr?>(null)
    t!!.set(R.strtod(s, endptr))
    if ((endptr.v!!.g(0) == 0)) {
        t!!.set((t!!.get() / 24.0))
        return 1
    }
    t!!.set(0.0)
    n = R.sscanf(s, __L43, hr, min, sec)
    if ((n == 0)) {
        return 0
    }
    t!!.set(datetime_encodeTime(hr.v, min.v, sec.v))
    if (((((hr.v >= 0)) && ((min.v >= 0))) && ((sec.v >= 0)))) {
        return 1
    } else {
        return 0
    }
    throw IllegalStateException("datetime_strToTime: no return")
}

// datetime.c:369
fun datetime_setDateFormat(fmt: Int) {
    if (((fmt >= 0) && (fmt <= 1))) {
        DateFormat = fmt
    }
}

// datetime.c:381
fun datetime_addSeconds(date1: Double, seconds: Double): Double {
    var d: Double = floor(date1)
    val h = IBox(0)
    val m = IBox(0)
    val s = IBox(0)
    datetime_decodeTime(date1, h, m, s)
    return (d + ((((((3600.0 * (h.v).toDouble()) + (60.0 * (m.v).toDouble())) + (s.v).toDouble()) + seconds)) / SecsPerDay))
}

// datetime.c:397
fun datetime_addDays(date1: Double, date2: Double): Double {
    var d1: Double = floor(date1)
    var d2: Double = floor(date2)
    val h1 = IBox(0)
    val m1 = IBox(0)
    val s1 = IBox(0)
    val h2 = IBox(0)
    val m2 = IBox(0)
    val s2 = IBox(0)
    datetime_decodeTime(date1, h1, m1, s1)
    datetime_decodeTime(date2, h2, m2, s2)
    return ((d1 + d2) + datetime_encodeTime((h1.v + h2.v), (m1.v + m2.v), (s1.v + s2.v)))
}

// datetime.c:416
fun datetime_timeDiff(date1: Double, date2: Double): Long {
    var d1: Double = floor(date1)
    var d2: Double = floor(date2)
    val h = IBox(0)
    val m = IBox(0)
    val s = IBox(0)
    var s1: Long = 0L
    var s2: Long = 0L
    var secs: Long = 0L
    datetime_decodeTime(date1, h, m, s)
    s1 = ((((3600 * h.v) + (60 * m.v)) + s.v)).toLong()
    datetime_decodeTime(date2, h, m, s)
    s2 = ((((3600 * h.v) + (60 * m.v)) + s.v)).toLong()
    secs = (R.D2I((floor(((((d1 - d2)) * SecsPerDay) + 0.5))))).toLong()
    secs += ((s1 - s2))
    return secs
}

// datetime.c:439
fun datetime_monthOfYear(date: Double): Int {
    val year = IBox(0)
    val month = IBox(0)
    val day = IBox(0)
    datetime_decodeDate(date, year, month, day)
    return month.v
}

// datetime.c:453
fun datetime_dayOfYear(date: Double): Int {
    val year = IBox(0)
    val month = IBox(0)
    val day = IBox(0)
    var startOfYear: Double = 0.0
    datetime_decodeDate(date, year, month, day)
    startOfYear = datetime_encodeDate(year.v, 1, 1)
    return (R.D2I((floor((date - startOfYear)))) + 1)
}

// datetime.c:469
fun datetime_dayOfWeek(date: Double): Int {
    var t: Int = (R.D2I((floor(date))) + DateDelta)
    return (((t % 7)) + 1)
}

// datetime.c:482
fun datetime_hourOfDay(date: Double): Int {
    val hour = IBox(0)
    val min = IBox(0)
    val sec = IBox(0)
    datetime_decodeTime(date, hour, min, sec)
    return hour.v
}

// datetime.c:496
fun datetime_daysPerMonth(year: Int, month: Int): Int {
    if (((month < 1) || (month > 12))) {
        return 0
    }
    return (DaysPerMonth!![isLeapYear(year)])!![(month - 1)]
}

// datetime.c:510
fun datetime_getTimeStamp(fmt: Int, aDate: Double, stampSize: Int, timeStamp: CPtr?) {
    var dateStr: CPtr? = R.buf(12)
    var timeStr: CPtr? = R.buf(9)
    var oldDateFormat: Int = DateFormat
    if ((stampSize < 21)) {
        return
    }
    datetime_setDateFormat(fmt)
    datetime_dateToStr(aDate, dateStr)
    DateFormat = oldDateFormat
    datetime_timeToStr(aDate, timeStr)
    R.snprintf(timeStamp, ((stampSize).toLong()).toInt(), __L44, dateStr, timeStr)
}

