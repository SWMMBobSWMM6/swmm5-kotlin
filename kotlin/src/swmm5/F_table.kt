// Generated from EPA SWMM 5.2.4 (table.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_table")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// table.c:51
fun table_interpolate(x: Double, x1: Double, y1: Double, x2: Double, y2: Double): Double {
    var dx: Double = (x2 - x1)
    if ((abs(dx) < 1.0e-20)) {
        return (((y1 + y2)) / 2.0)
    }
    return (y1 + ((((x - x1)) * ((y2 - y1))) / dx))
}

// table.c:67
fun table_readCurve(tok: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var m: Int = 0
    var k: Int = 0
    var k1: Int = 1
    val x = DBox(0.0)
    val y = DBox(0.0)
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(CURVE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if ((Curve!![j]!!.ID == null)) {
        Curve!![j]!!.ID = project_findID(CURVE, tok!![0])
        m = findmatch(tok!![1], CurveTypeWords)
        if ((m < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![1])
        }
        Curve!![j]!!.curveType = m
        if ((ntoks == 2)) {
            return 0
        }
        k1 = 2
    }
    k = k1
    f1@ while ((k < ntoks)) {
        if (((k + 1) >= ntoks)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if (!(getDouble(tok!![k], x) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![k])
        }
        if (!(getDouble(tok!![(k + 1)], y) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![(k + 1)])
        }
        table_addEntry(Curve!![j], x.v, y.v)
        k = (k + 2)
    }
    return 0
}

// table.c:113
fun table_readTimeseries(tok: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var k: Int = 0
    var state: Int = 0
    var x: Double = 0.0
    val y = DBox(0.0)
    val d = DBox(0.0)
    val t = DBox(0.0)
    var fname: CPtr? = R.buf(260)
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(TSERIES, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if ((Tseries!![j]!!.ID == null)) {
        Tseries!![j]!!.ID = project_findID(TSERIES, tok!![0])
    }
    if ((strcomp(tok!![1], __L1) != 0)) {
        sstrncpy(fname, tok!![2], 259L)
        sstrncpy(Tseries!![j]!!.file!!.name, addAbsolutePath(fname), 259L)
        Tseries!![j]!!.file!!.mode = USE_FILE
        return 0
    }
    x = 0.0
    k = 1
    state = 1
    w1@ while ((k < ntoks)) {
        when (state) {
            1 -> {
                if ((datetime_strToDate(tok!![k], d) != 0)) {
                    Tseries!![j]!!.lastDate = d.v
                    k++
                }
                state = 2
            }
            2 -> {
                if ((k >= ntoks)) {
                    return error_setInpError(ERR_ITEMS, __L0)
                }
                if ((getDouble(tok!![k], t) != 0)) {
                    t.v /= 24.0
                } else if (!(datetime_strToTime(tok!![k], t) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![k])
                }
                x = (Tseries!![j]!!.lastDate + t.v)
                k++
                state = 3
            }
            3 -> {
                if ((k >= ntoks)) {
                    return error_setInpError(ERR_ITEMS, __L0)
                }
                if (!(getDouble(tok!![k], y) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![k])
                }
                table_addEntry(Tseries!![j], x, y.v)
                k++
                state = 1
            }
        }
    }
    return 0
}

// table.c:208
fun table_addEntry(table: TTable?, x: Double, y: Double): Int {
    var entry: TTableEntry? = null
    entry = TTableEntry()
    if (!(entry != null)) {
        return 0
    }
    entry!!.x = x
    entry!!.y = y
    entry!!.next = (null)
    if ((table!!.firstEntry == null)) {
        table!!.firstEntry = entry
    } else {
        table!!.lastEntry!!.next = entry
    }
    table!!.lastEntry = entry
    return 1
}

// table.c:231
fun table_deleteEntries(table: TTable?) {
    var entry: TTableEntry? = null
    var nextEntry: TTableEntry? = null
    entry = table!!.firstEntry
    w1@ while ((entry != null)) {
        nextEntry = entry!!.next
        R.free(entry)
        entry = nextEntry
    }
    table!!.firstEntry = (null)
    table!!.lastEntry = (null)
    table!!.thisEntry = (null)
    if ((table!!.file!!.file != null)) {
        R.fclose(table!!.file!!.file)
        table!!.file!!.file = (null)
    }
}

// table.c:260
fun table_init(table: TTable?) {
    table!!.ID = null
    table!!.refersTo = (-1)
    table!!.firstEntry = (null)
    table!!.lastEntry = (null)
    table!!.thisEntry = table!!.firstEntry
    table!!.lastDate = 0.0
    table!!.x1 = 0.0
    table!!.x2 = 0.0
    table!!.y1 = 0.0
    table!!.y2 = 0.0
    table!!.dxMin = 0.0
    table!!.file!!.mode = NO_FILE
    table!!.file!!.file = (null)
    table!!.curveType = (-1)
}

// table.c:285
fun table_validate(table: TTable?): Int {
    var result: Int = 0
    val x1 = DBox(0.0)
    val x2 = DBox(0.0)
    val y1 = DBox(0.0)
    val y2 = DBox(0.0)
    var dx: Double = 0.0
    var dxMin: Double = 10000000000.0
    if ((table!!.file!!.mode == USE_FILE)) {
        table!!.file!!.file = R.fopen(table!!.file!!.name, __L13)
        if ((table!!.file!!.file == null)) {
            return ERR_TABLE_FILE_OPEN
        }
    }
    result = table_getFirstEntry(table, x1, y1)
    if (((!(result != 0)) && (table!!.file!!.mode == USE_FILE))) {
        return ERR_TABLE_FILE_READ
    }
    w1@ while ((table_getNextEntry(table, x2, y2) != 0)) {
        dx = (x2.v - x1.v)
        if ((dx <= 0.0)) {
            table!!.x2 = x2.v
            return ERR_CURVE_SEQUENCE
        }
        dxMin = ((if ((((dxMin) <= (dx)))) (dxMin) else (dx)))
        x1.v = x2.v
    }
    table!!.dxMin = dxMin
    if (((table!!.file!!.mode == USE_FILE) && (!(R.feof(table!!.file!!.file) != 0)))) {
        return ERR_TABLE_FILE_READ
    }
    return 0
}

// table.c:331
fun table_getFirstEntry(table: TTable?, x: DRef?, y: DRef?): Int {
    var entry: TTableEntry? = null
    x!!.set(0.0)
    y!!.set(0.0)
    if ((table!!.file!!.mode == USE_FILE)) {
        if ((table!!.file!!.file == null)) {
            return 0
        }
        R.rewind(table!!.file!!.file)
        return table_getNextFileEntry(table, x, y)
    }
    entry = table!!.firstEntry
    if ((entry != null)) {
        x!!.set(entry!!.x)
        y!!.set(entry!!.y)
        table!!.thisEntry = entry
        return 1
    } else {
        return 0
    }
    throw IllegalStateException("table_getFirstEntry: no return")
}

// table.c:366
fun table_getNextEntry(table: TTable?, x: DRef?, y: DRef?): Int {
    var entry: TTableEntry? = null
    if ((table!!.file!!.mode == USE_FILE)) {
        return table_getNextFileEntry(table, x, y)
    }
    entry = table!!.thisEntry!!.next
    if ((entry != null)) {
        x!!.set(entry!!.x)
        y!!.set(entry!!.y)
        table!!.thisEntry = entry
        return 1
    } else {
        return 0
    }
    throw IllegalStateException("table_getNextEntry: no return")
}

// table.c:395
fun table_lookup(table: TTable?, x: Double): Double {
    var x1: Double = 0.0
    var y1: Double = 0.0
    var x2: Double = 0.0
    var y2: Double = 0.0
    var entry: TTableEntry? = null
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    x1 = entry!!.x
    y1 = entry!!.y
    if ((x <= x1)) {
        return y1
    }
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        x2 = entry!!.x
        y2 = entry!!.y
        if ((x <= x2)) {
            return table_interpolate(x, x1, y1, x2, y2)
        }
        x1 = x2
        y1 = y2
    }
    return y1
}

// table.c:430
fun table_getSlope(table: TTable?, x: Double): Double {
    var x1: Double = 0.0
    var y1: Double = 0.0
    var x2: Double = 0.0
    var y2: Double = 0.0
    var dx: Double = 0.0
    var entry: TTableEntry? = null
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    x1 = entry!!.x
    y1 = entry!!.y
    x2 = x1
    y2 = y1
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        x2 = entry!!.x
        y2 = entry!!.y
        if ((x <= x2)) {
            break@w1
        }
        x1 = x2
        y1 = y2
    }
    dx = (x2 - x1)
    if ((dx == 0.0)) {
        return 0.0
    }
    return (((y2 - y1)) / dx)
}

// table.c:464
fun table_lookupEx(table: TTable?, x: Double): Double {
    var x1: Double = 0.0
    var y1: Double = 0.0
    var x2: Double = 0.0
    var y2: Double = 0.0
    var s: Double = 0.0
    var entry: TTableEntry? = null
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    x1 = entry!!.x
    y1 = entry!!.y
    if ((x <= x1)) {
        if ((x1 > 0.0)) {
            return ((x / x1) * y1)
        } else {
            return y1
        }
    }
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        x2 = entry!!.x
        y2 = entry!!.y
        if ((x2 != x1)) {
            s = (((y2 - y1)) / ((x2 - x1)))
        }
        if ((x <= x2)) {
            return table_interpolate(x, x1, y1, x2, y2)
        }
        x1 = x2
        y1 = y2
    }
    if ((s < 0.0)) {
        s = 0.0
    }
    return (y1 + (s * ((x - x1))))
}

// table.c:503
fun table_intervalLookup(table: TTable?, x: Double): Double {
    var entry: TTableEntry? = null
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    if ((x < entry!!.x)) {
        return entry!!.y
    }
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        if ((x < entry!!.x)) {
            return entry!!.y
        }
    }
    return entry!!.y
}

// table.c:527
fun table_inverseLookup(table: TTable?, y: Double): Double {
    var x1: Double = 0.0
    var y1: Double = 0.0
    var x2: Double = 0.0
    var y2: Double = 0.0
    var entry: TTableEntry? = null
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    x1 = entry!!.x
    y1 = entry!!.y
    if ((y <= y1)) {
        return x1
    }
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        x2 = entry!!.x
        y2 = entry!!.y
        if ((y <= y2)) {
            return table_interpolate(y, y1, x1, y2, x2)
        }
        x1 = x2
        y1 = y2
    }
    return x1
}

// table.c:562
fun table_getMaxY(table: TTable?, x: Double): Double {
    var ymax: Double = 0.0
    var entry: TTableEntry? = null
    entry = table!!.firstEntry
    ymax = entry!!.y
    w1@ while (((x > entry!!.x) && (entry!!.next != null))) {
        entry = entry!!.next
        if ((entry!!.y < ymax)) {
            return ymax
        }
        ymax = entry!!.y
    }
    return 0.0
}

// table.c:587
fun table_getStorageVolume(table: TTable?, x: Double): Double {
    var a: Double = 0.0
    var a1: Double = 0.0
    var x1: Double = 0.0
    var v: Double = 0.0
    var dx: Double = 0.0
    var dy: Double = 0.0
    var s: Double = 0.0
    var entry: TTableEntry? = null
    v = 0.0
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    x1 = entry!!.x
    a1 = entry!!.y
    if ((x <= x1)) {
        if ((x1 < 1.0e-6)) {
            return 0.0
        }
        return (((((a1 / x1)) * x) * x) / 2.0)
    }
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        if ((entry!!.x >= x)) {
            a = table_interpolate(x, x1, a1, entry!!.x, entry!!.y)
            return (v + ((((a1 + a)) / 2.0) * ((x - x1))))
        } else {
            dx = (entry!!.x - x1)
            dy = (entry!!.y - a1)
            v = (v + ((((a1 + entry!!.y)) / 2.0) * dx))
            x1 = entry!!.x
            a1 = entry!!.y
        }
    }
    if ((dx > 1.0e-6)) {
        s = (dy / dx)
        a = (a1 + (s * ((x - x1))))
        if ((a < 0.0)) {
            v = (v - (((a1 * a1) / s) / 2.0))
        } else {
            v = (v + ((((a1 + a)) / 2.0) * ((x - x1))))
        }
    }
    return v
}

// table.c:651
fun table_getStorageDepth(table: TTable?, v: Double): Double {
    var a1: Double = 0.0
    var a2: Double = 0.0
    var d1: Double = 0.0
    var d2: Double = 0.0
    var dd: Double = 0.0
    var da: Double = 0.0
    var v1: Double = 0.0
    var v2: Double = 0.0
    var s: Double = 0.0
    var entry: TTableEntry? = null
    if ((v == 0.0)) {
        return 0.0
    }
    entry = table!!.firstEntry
    if ((entry == null)) {
        return 0.0
    }
    d1 = entry!!.x
    a1 = entry!!.y
    v1 = ((a1 * d1) / 2.0)
    if ((v <= v1)) {
        if ((a1 > 0.0)) {
            return sqrt((((2.0 * v) * d1) / a1))
        } else {
            return 0.0
        }
    }
    w1@ while ((entry!!.next != null)) {
        entry = entry!!.next
        d2 = entry!!.x
        a2 = entry!!.y
        dd = (d2 - d1)
        da = (a2 - a1)
        v2 = (v1 + ((((a1 + a2)) / 2.0) * dd))
        if ((v <= v2)) {
            if ((dd <= 0.0)) {
                return d1
            }
            if ((da == 0.0)) {
                if ((abs((v2 - v1)) < 1.0e-6)) {
                    return d1
                } else {
                    return (d1 + ((dd * ((v - v1))) / ((v2 - v1))))
                }
            }
            if ((da < 0.0)) {
                d1 = d2
                a1 = a2
                v1 = v2
            }
            s = (da / dd)
            return (d1 + (((sqrt(((a1 * a1) + ((2.0 * s) * ((v - v1))))) - a1)) / s))
        }
        d1 = d2
        a1 = a2
        v1 = v2
    }
    if (((dd == 0.0) || (da == 0.0))) {
        if ((a1 > 0.0)) {
            dd = (((v - v1)) / a1)
        } else {
            dd = 0.0
        }
    } else {
        s = (da / dd)
        dd = (((sqrt(((a1 * a1) + ((2.0 * s) * ((v - v1))))) - a1)) / s)
        if ((dd < 0.0)) {
            dd = 0.0
        }
    }
    return (d1 + dd)
}

// table.c:730
fun table_tseriesInit(table: TTable?) {
    table_getFirstEntry(table, DField<TTable>(table!!, { __o -> __o.x1 }, { __o, __x -> __o.x1 = __x }), DField<TTable>(table!!, { __o -> __o.y1 }, { __o, __x -> __o.y1 = __x }))
    table!!.x2 = table!!.x1
    table!!.y2 = table!!.y1
    table_getNextEntry(table, DField<TTable>(table!!, { __o -> __o.x2 }, { __o, __x -> __o.x2 = __x }), DField<TTable>(table!!, { __o -> __o.y2 }, { __o, __x -> __o.y2 = __x }))
}

// table.c:745
fun table_tseriesLookup(table: TTable?, x: Double, extend: Int): Double {
    if ((((table!!.x1 <= x) && (table!!.x2 >= x)) && (table!!.x1 != table!!.x2))) {
        return table_interpolate(x, table!!.x1, table!!.y1, table!!.x2, table!!.y2)
    }
    if (((table!!.file!!.mode == USE_FILE) && (R.feof(table!!.file!!.file) != 0))) {
        if ((extend == 1)) {
            return table!!.y1
        } else {
            return 0.0
        }
    }
    if (((table!!.x1 == table!!.x2) || (x < table!!.x1))) {
        table_getFirstEntry(table, DField<TTable>(table!!, { __o -> __o.x1 }, { __o, __x -> __o.x1 = __x }), DField<TTable>(table!!, { __o -> __o.y1 }, { __o, __x -> __o.y1 = __x }))
        if ((x < table!!.x1)) {
            if ((extend == 1)) {
                return table!!.y1
            } else {
                return 0.0
            }
        }
    }
    table!!.x1 = table!!.x2
    table!!.y1 = table!!.y2
    w1@ while ((table_getNextEntry(table, DField<TTable>(table!!, { __o -> __o.x2 }, { __o, __x -> __o.x2 = __x }), DField<TTable>(table!!, { __o -> __o.y2 }, { __o, __x -> __o.y2 = __x })) != 0)) {
        if ((x <= table!!.x2)) {
            return table_interpolate(x, table!!.x1, table!!.y1, table!!.x2, table!!.y2)
        }
        table!!.x1 = table!!.x2
        table!!.y1 = table!!.y2
    }
    if ((extend == 1)) {
        return table!!.y1
    } else {
        return 0.0
    }
    throw IllegalStateException("table_tseriesLookup: no return")
}

// table.c:808
fun table_getNextFileEntry(table: TTable?, x: DRef?, y: DRef?): Int {
    var line: CPtr? = R.buf(1025)
    var code: Int = 0
    if ((table!!.file!!.file == null)) {
        return 0
    }
    w1@ while (((!(R.feof(table!!.file!!.file) != 0)) && ((R.fgets(line, 1024, table!!.file!!.file)) != null))) {
        code = table_parseFileLine(line, table, x, y)
        if ((code < 0)) {
            continue@w1
        }
        return code
    }
    return 0
}

// table.c:833
fun table_parseFileLine(line: CPtr?, table: TTable?, x: DRef?, y: DRef?): Int {
    var n: Int = 0
    var s1: CPtr? = R.buf(50)
    var s2: CPtr? = R.buf(50)
    var s3: CPtr? = R.buf(50)
    var tStr: CPtr? = null
    var yStr: CPtr? = null
    val yy = DBox(0.0)
    val d = DBox(0.0)
    val t = DBox(0.0)
    n = R.sscanf(line, __L650, s1, s2, s3)
    tStr = R.strtok(line, __L184)
    if (((tStr == null) || (tStr!!.g(0) == 59))) {
        return (-1)
    }
    if ((n == 2)) {
        d.v = table!!.lastDate
        tStr = s1
        yStr = s2
    } else if ((n == 3)) {
        if (!(datetime_strToDate(s1, d) != 0)) {
            return 0
        }
        table!!.lastDate = d.v
        tStr = s2
        yStr = s3
    } else {
        return 0
    }
    if ((getDouble(tStr, t) != 0)) {
        t.v /= 24.0
    } else if (!(datetime_strToTime(tStr, t) != 0)) {
        return 0
    }
    if (!(getDouble(yStr, yy) != 0)) {
        return 0
    }
    x!!.set((d.v + t.v))
    y!!.set(yy.v)
    return 1
}

