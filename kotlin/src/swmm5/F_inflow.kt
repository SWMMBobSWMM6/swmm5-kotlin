// Generated from EPA SWMM 5.2.4 (inflow.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_inflow")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// inflow.c:41
fun inflow_readExtInflow(tok: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var param: Int = 0
    var type: Int = CONCEN_INFLOW
    var tseries: Int = (-1)
    var basePat: Int = (-1)
    val cf = DBox(1.0)
    val sf = DBox(1.0)
    val baseline = DBox(0.0)
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(NODE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    param = project_findObject(POLLUT, tok!![1])
    if ((param < 0)) {
        if ((match(tok!![1], __L183) != 0)) {
            param = (-1)
        } else {
            return error_setInpError(ERR_NAME, tok!![1])
        }
    }
    if ((R.strlen(tok!![2]) > 0L)) {
        tseries = project_findObject(TSERIES, tok!![2])
        if ((tseries < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        Tseries!![tseries]!!.refersTo = EXTERNAL_INFLOW
    }
    if ((param == (-1))) {
        type = FLOW_INFLOW
        cf.v = (1.0 / UCF(FLOW))
    }
    if (((ntoks >= 4) && (param > (-1)))) {
        if ((match(tok!![3], __L185) != 0)) {
            type = CONCEN_INFLOW
        } else if ((match(tok!![3], __L186) != 0)) {
            type = MASS_INFLOW
        } else {
            return error_setInpError(ERR_KEYWORD, tok!![3])
        }
        if (((ntoks >= 5) && (type == MASS_INFLOW))) {
            if (!(getDouble(tok!![4], cf) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![4])
            }
            if ((cf.v <= 0.0)) {
                return error_setInpError(ERR_NUMBER, tok!![4])
            }
        }
    }
    if ((ntoks >= 6)) {
        if (!(getDouble(tok!![5], sf) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
    }
    if ((ntoks >= 7)) {
        if (!(getDouble(tok!![6], baseline) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![6])
        }
    }
    if ((ntoks >= 8)) {
        basePat = project_findObject(TIMEPATTERN, tok!![7])
        if ((basePat < 0)) {
            return error_setInpError(ERR_NAME, tok!![7])
        }
    }
    if ((type == MASS_INFLOW)) {
        cf.v /= 28.317
    }
    return (inflow_setExtInflow(j, param, type, tseries, basePat, cf.v, baseline.v, sf.v))
}

// inflow.c:138
fun inflow_setExtInflow(j: Int, param: Int, type: Int, tseries: Int, basePat: Int, cf: Double, baseline: Double, sf: Double): Int {
    var inflow: TExtInflow? = null
    inflow = Node!![j]!!.extInflow
    w1@ while ((inflow != null)) {
        if ((inflow!!.param == param)) {
            break@w1
        }
        inflow = inflow!!.next
    }
    if ((inflow == null)) {
        inflow = TExtInflow()
        if ((inflow == null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        inflow!!.next = Node!![j]!!.extInflow
        Node!![j]!!.extInflow = inflow
    }
    inflow!!.param = param
    inflow!!.type = type
    inflow!!.tSeries = tseries
    inflow!!.cFactor = cf
    inflow!!.sFactor = sf
    inflow!!.baseline = baseline
    inflow!!.basePat = basePat
    return 0
}

// inflow.c:187
fun inflow_deleteExtInflows(j: Int) {
    var inflow1: TExtInflow? = null
    var inflow2: TExtInflow? = null
    inflow1 = Node!![j]!!.extInflow
    w1@ while ((inflow1 != null)) {
        inflow2 = inflow1!!.next
        R.free(inflow1)
        inflow1 = inflow2
    }
}

// inflow.c:207
fun inflow_getExtInflow(inflow: TExtInflow?, aDate: Double): Double {
    var month: Int = 0
    var day: Int = 0
    var hour: Int = 0
    var p: Int = inflow!!.basePat
    var k: Int = inflow!!.tSeries
    var cf: Double = inflow!!.cFactor
    var sf: Double = inflow!!.sFactor
    var blv: Double = inflow!!.baseline
    var tsv: Double = 0.0
    if ((p >= 0)) {
        month = (datetime_monthOfYear(aDate) - 1)
        day = (datetime_dayOfWeek(aDate) - 1)
        hour = datetime_hourOfDay(aDate)
        blv *= (getPatternFactor(p, month, day, hour))
    }
    if ((k >= 0)) {
        tsv = (table_tseriesLookup(Tseries!![k], aDate, 0) * sf)
    }
    return (cf * ((tsv + blv)))
}

// inflow.c:237
fun inflow_readDwfInflow(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    var pats: IntArray? = IntArray(4)
    val x = DBox(0.0)
    var inflow: TDwfInflow? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(NODE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = project_findObject(POLLUT, tok!![1])
    if ((k < 0)) {
        if ((match(tok!![1], __L183) != 0)) {
            k = (-1)
        } else {
            return error_setInpError(ERR_NAME, tok!![1])
        }
    }
    if (!(getDouble(tok!![2], x) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![2])
    }
    if ((k == (-1))) {
        x.v /= (UCF(FLOW))
    }
    i = 0
    f1@ while ((i < 4)) {
        pats!![i] = (-1)
        i++
    }
    i = 3
    f2@ while ((i < 7)) {
        if ((i >= ntoks)) {
            break@f2
        }
        if ((R.strlen(tok!![i]) == 0L)) {
            i++
            continue@f2
        }
        m = project_findObject(TIMEPATTERN, tok!![i])
        if ((m < 0)) {
            return error_setInpError(ERR_NAME, tok!![i])
        }
        pats!![(i - 3)] = m
        i++
    }
    inflow = Node!![j]!!.dwfInflow
    w3@ while ((inflow != null)) {
        if ((inflow!!.param == k)) {
            break@w3
        }
        inflow = inflow!!.next
    }
    if ((inflow == null)) {
        inflow = TDwfInflow()
        if ((inflow == null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        inflow!!.next = Node!![j]!!.dwfInflow
        Node!![j]!!.dwfInflow = inflow
    }
    inflow!!.param = k
    inflow!!.avgValue = x.v
    i = 0
    f4@ while ((i < 4)) {
        inflow!!.patterns!![i] = pats!![i]
        i++
    }
    return 0
}

// inflow.c:311
fun inflow_deleteDwfInflows(j: Int) {
    var inflow1: TDwfInflow? = null
    var inflow2: TDwfInflow? = null
    inflow1 = Node!![j]!!.dwfInflow
    w1@ while ((inflow1 != null)) {
        inflow2 = inflow1!!.next
        R.free(inflow1)
        inflow1 = inflow2
    }
}

// inflow.c:331
fun inflow_initDwfInflow(inflow: TDwfInflow?) {
    var i: Int = 0
    var p: Int = 0
    var tmpPattern: IntArray? = IntArray(4)
    i = 0
    f1@ while ((i < 4)) {
        tmpPattern!![i] = (-1)
        i++
    }
    i = 0
    f2@ while ((i < 4)) {
        p = inflow!!.patterns!![i]
        if ((p >= 0)) {
            tmpPattern!![Pattern!![p]!!.type] = p
        }
        i++
    }
    i = 0
    f3@ while ((i < 4)) {
        inflow!!.patterns!![i] = tmpPattern!![i]
        i++
    }
}

// inflow.c:361
fun inflow_getDwfInflow(inflow: TDwfInflow?, month: Int, day: Int, hour: Int): Double {
    var p1: Int = 0
    var p2: Int = 0
    var f: Double = 1.0
    p1 = inflow!!.patterns!![MONTHLY_PATTERN]
    if ((p1 >= 0)) {
        f *= (getPatternFactor(p1, month, day, hour))
    }
    p1 = inflow!!.patterns!![DAILY_PATTERN]
    if ((p1 >= 0)) {
        f *= (getPatternFactor(p1, month, day, hour))
    }
    p1 = inflow!!.patterns!![HOURLY_PATTERN]
    p2 = inflow!!.patterns!![WEEKEND_PATTERN]
    if ((p2 >= 0)) {
        if (((day == 0) || (day == 6))) {
            f *= (getPatternFactor(p2, month, day, hour))
        } else if ((p1 >= 0)) {
            f *= (getPatternFactor(p1, month, day, hour))
        }
    } else if ((p1 >= 0)) {
        f *= (getPatternFactor(p1, month, day, hour))
    }
    return (f * inflow!!.avgValue)
}

// inflow.c:394
fun inflow_initDwfPattern(j: Int) {
    var i: Int = 0
    i = 0
    f1@ while ((i < 24)) {
        Pattern!![j]!!.factor!![i] = 1.0
        i++
    }
    Pattern!![j]!!.count = 0
    Pattern!![j]!!.type = (-1)
    Pattern!![j]!!.ID = null
}

// inflow.c:410
fun inflow_readDwfPattern(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var n: Int = 1
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(TIMEPATTERN, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if ((Pattern!![j]!!.ID == null)) {
        Pattern!![j]!!.ID = project_findID(TIMEPATTERN, tok!![0])
        k = findmatch(tok!![1], PatternTypeWords)
        if ((k < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![1])
        }
        Pattern!![j]!!.type = k
        n = 2
    }
    w1@ while (((ntoks > n) && (Pattern!![j]!!.count < 24))) {
        i = Pattern!![j]!!.count
        if (!(getDouble(tok!![n], DElem(Pattern!![j]!!.factor!!, i)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![n])
        }
        Pattern!![j]!!.count++
        n++
    }
    return 0
}

// inflow.c:456
fun getPatternFactor(p: Int, month: Int, day: Int, hour: Int): Double {
    when (Pattern!![p]!!.type) {
        MONTHLY_PATTERN -> {
            if (((month >= 0) && (month < 12))) {
                return Pattern!![p]!!.factor!![month]
            }
        }
        DAILY_PATTERN -> {
            if (((day >= 0) && (day < 7))) {
                return Pattern!![p]!!.factor!![day]
            }
        }
        HOURLY_PATTERN -> {
            if (((hour >= 0) && (hour < 24))) {
                return Pattern!![p]!!.factor!![hour]
            }
        }
        WEEKEND_PATTERN -> {
            if (((day == 0) || (day == 6))) {
                if (((hour >= 0) && (hour < 24))) {
                    return Pattern!![p]!!.factor!![hour]
                }
            }
        }
    }
    return 1.0
}

