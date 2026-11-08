// Generated from EPA SWMM 5.2.4 (shape.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_shape")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var Atotal: Double = 0.0
@JvmField var Ptotal: Double = 0.0

// shape.c:42
fun shape_validate(shape: TShape?, curve: TTable?): Int {
    if (!(computeShapeTables(shape, curve) != 0)) {
        return 0
    }
    if (!(normalizeShapeTables(shape) != 0)) {
        return 0
    }
    return 1
}

// shape.c:64
fun computeShapeTables(shape: TShape?, curve: TTable?): Int {
    var i: Int = 0
    var n: Int = 0
    var dy: Double = 0.0
    var y: Double = 0.0
    val y1 = DBox(0.0)
    val y2 = DBox(0.0)
    var w: Double = 0.0
    val w1 = DBox(0.0)
    val w2 = DBox(0.0)
    var yLast: Double = 0.0
    var wLast: Double = 0.0
    val wMax = DBox(0.0)
    if (!(table_getFirstEntry(curve, y1, w1) != 0)) {
        return 0
    }
    if ((((y1.v < 0.0) || (y1.v >= 1.0)) || (w1.v < 0.0))) {
        return 0
    }
    wMax.v = w1.v
    if ((y1.v != 0.0)) {
        y2.v = y1.v
        w2.v = w1.v
        y1.v = 0.0
        w1.v = 0.0
    } else {
        if (!(table_getNextEntry(curve, y2, w2) != 0)) {
            return 0
        }
        if (((y2.v < y1.v) || (w2.v < 0.0))) {
            return 0
        }
        if ((y2.v > 1.0)) {
            y2.v = 1.0
        }
        if ((w2.v > wMax.v)) {
            wMax.v = w2.v
        }
    }
    shape!!.nTbl = 51
    n = (shape!!.nTbl - 1)
    dy = (1.0 / ((n)).toDouble())
    shape!!.areaTbl!![0] = 0.0
    shape!!.hradTbl!![0] = 0.0
    shape!!.widthTbl!![0] = w1.v
    Ptotal = w1.v
    Atotal = 0.0
    y = 0.0
    w = w1.v
    i = 1
    f1@ while ((i <= n)) {
        yLast = y
        wLast = w
        y = (y + dy)
        if ((abs((y - 1.0)) < 1e-06)) {
            y = 1.0
        }
        if ((y > y2.v)) {
            if (!(getNextInterval(curve, y, yLast, wLast, y1, y2, w1, w2, wMax) != 0)) {
                return 0
            }
            yLast = y1.v
            wLast = w1.v
        }
        w = shape_getWidth(y, y1.v, y2.v, w1.v, w2.v)
        Atotal += (shape_getArea(y, w, yLast, wLast))
        Ptotal += (getPerim(y, w, yLast, wLast))
        if ((y == 1.0)) {
            Ptotal += w2.v
        }
        shape!!.widthTbl!![i] = w
        shape!!.areaTbl!![i] = Atotal
        if ((Ptotal > 0.0)) {
            shape!!.hradTbl!![i] = (Atotal / Ptotal)
        } else {
            shape!!.hradTbl!![i] = 0.0
        }
        i++
    }
    shape!!.aFull = shape!!.areaTbl!![n]
    shape!!.rFull = shape!!.hradTbl!![n]
    shape!!.wMax = wMax.v
    getSmax(shape)
    return 1
}

// shape.c:189
fun getSmax(shape: TShape?) {
    var i: Int = 0
    var n: Int = (shape!!.nTbl - 1)
    var sf: Double = 0.0
    shape!!.sMax = 0.0
    shape!!.aMax = 0.0
    i = 1
    f1@ while ((i <= n)) {
        sf = (shape!!.areaTbl!![i] * Glibm.pow(shape!!.hradTbl!![i], (2.0 / 3.0)))
        if ((sf > shape!!.sMax)) {
            shape!!.sMax = sf
            shape!!.aMax = shape!!.areaTbl!![i]
        }
        i++
    }
}

// shape.c:216
fun normalizeShapeTables(shape: TShape?): Int {
    var i: Int = 0
    var n: Int = (shape!!.nTbl - 1)
    var aFull: Double = shape!!.aFull
    var rFull: Double = shape!!.rFull
    var wMax: Double = shape!!.wMax
    if ((((aFull == 0.0) || (rFull == 0.0)) || (wMax == 0.0))) {
        return 0
    }
    i = 0
    f1@ while ((i <= n)) {
        shape!!.areaTbl!![i] /= aFull
        shape!!.hradTbl!![i] /= rFull
        shape!!.widthTbl!![i] /= wMax
        i++
    }
    return 1
}

// shape.c:246
fun getNextInterval(curve: TTable?, y: Double, __yLast: Double, __wLast: Double, y1: DRef?, y2: DRef?, w1: DRef?, w2: DRef?, wMax: DRef?): Int {
    var yLast: Double = __yLast
    var wLast: Double = __wLast
    w1@ while ((y > y2!!.get())) {
        if ((y2!!.get() > yLast)) {
            Atotal += (shape_getArea(y2!!.get(), w2!!.get(), yLast, wLast))
            Ptotal += (getPerim(y2!!.get(), w2!!.get(), yLast, wLast))
            yLast = y2!!.get()
            wLast = w2!!.get()
        }
        y1!!.set(y2!!.get())
        w1!!.set(w2!!.get())
        if (!(table_getNextEntry(curve, y2, w2) != 0)) {
            y2!!.set(1.0)
            return 1
        }
        if ((w2!!.get() > wMax!!.get())) {
            wMax!!.set(w2!!.get())
        }
        if (((y2!!.get() < y1!!.get()) || (w2!!.get() < 0.0))) {
            return 0
        }
        if ((y2!!.get() > 1.0)) {
            y2!!.set(1.0)
        }
    }
    return 1
}

// shape.c:307
fun shape_getWidth(y: Double, y1: Double, y2: Double, w1: Double, w2: Double): Double {
    if ((y2 == y1)) {
        return w2
    }
    return (w1 + ((((y - y1)) / ((y2 - y1))) * ((w2 - w1))))
}

// shape.c:328
fun shape_getArea(y: Double, w: Double, y1: Double, w1: Double): Double {
    var wMin: Double = 0.0
    var wMax: Double = 0.0
    if ((w > w1)) {
        wMin = w1
        wMax = w
    } else {
        wMin = w
        wMax = w1
    }
    return (((wMin + (((wMax - wMin)) / 2.0))) * ((y - y1)))
}

// shape.c:353
fun getPerim(y: Double, w: Double, y1: Double, w1: Double): Double {
    var dy: Double = (y - y1)
    var dw: Double = (abs((w - w1)) / 2.0)
    return (2.0 * sqrt(((dy * dy) + (dw * dw))))
}

