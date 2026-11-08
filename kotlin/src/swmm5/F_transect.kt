// Generated from EPA SWMM 5.2.4 (transect.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_transect")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var Ntransects: Int = 0
@JvmField var Nstations: Int = 0
@JvmField var Station: DoubleArray? = DoubleArray(1501)
@JvmField var Elev: DoubleArray? = DoubleArray(1501)
@JvmField var Nleft: Double = 0.0
@JvmField var Nright: Double = 0.0
@JvmField var Nchannel: Double = 0.0
@JvmField var Xleftbank: Double = 0.0
@JvmField var Xrightbank: Double = 0.0
@JvmField var Xfactor: Double = 0.0
@JvmField var Yfactor: Double = 0.0
@JvmField var Lfactor: Double = 0.0

// transect.c:70
fun transect_create(n: Int): Int {
    Ntransects = n
    if ((n == 0)) {
        return 0
    }
    Transect = Array<TTransect?>(((Ntransects).toLong()).toInt()) { TTransect() }
    if ((Transect == null)) {
        return ERR_MEMORY
    }
    Nchannel = 0.0
    Nleft = 0.0
    Nright = 0.0
    Nstations = 0
    return 0
}

// transect.c:90
fun transect_delete() {
    if ((Ntransects == 0)) {
        return
    }
    if ((Transect != null)) {
        R.free(Transect)
        Transect = (null)
    }
    Ntransects = 0
}

// transect.c:104
fun transect_readParams(count: IRef?, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    var index: Int = count!!.get()
    var errcode: Int = 0
    var x: DoubleArray? = DoubleArray(10)
    var id: CPtr? = null
    k = findmatch(tok!![0], TransectKeyWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![0])
    }
    when (k) {
        0 -> {
            transect_validate((index - 1))
            if ((ntoks < 4)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            i = 1
            f2@ while ((i <= 3)) {
                if (!(getDouble(tok!![i], DElem(x!!, i)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![i])
                }
                i++
            }
            return setManning(x)
        }
        1 -> {
            if ((ntoks < 10)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            id = project_findID(TRANSECT, tok!![1])
            if ((id == null)) {
                return error_setInpError(ERR_NAME, tok!![1])
            }
            i = 2
            f3@ while ((i < 10)) {
                if (!(getDouble(tok!![i], DElem(x!!, i)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![i])
                }
                i++
            }
            count!!.set((index + 1))
            return setParams(index, id, x)
        }
        2 -> {
            if (((((ntoks - 1)) % 2) > 0)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            i = 1
            w4@ while ((i < ntoks)) {
                if (!(getDouble(tok!![i], DElem(x!!, 1)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![i])
                }
                if (!(getDouble(tok!![(i + 1)], DElem(x!!, 2)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![(i + 1)])
                }
                errcode = addStation(x!![1], x!![2])
                if ((errcode != 0)) {
                    return errcode
                }
                i += 2
            }
            return 0
        }
    }
    return 0
}

// transect.c:194
fun transect_validate(j: Int) {
    var i: Int = 0
    var ymin: Double = 0.0
    var ymax: Double = 0.0
    var oldNchannel: Double = Nchannel
    if (((j < 0) || (j >= Ntransects))) {
        return
    }
    if ((Nstations < 2)) {
        report_writeErrorMsg(ERR_TRANSECT_TOO_FEW, Transect!![j]!!.ID)
        return
    }
    if ((Nstations >= 1500)) {
        report_writeErrorMsg(ERR_TRANSECT_TOO_MANY, Transect!![j]!!.ID)
        return
    }
    if ((Nchannel <= 0.0)) {
        report_writeErrorMsg(ERR_TRANSECT_MANNING, Transect!![j]!!.ID)
        return
    }
    if ((Xleftbank > Xrightbank)) {
        report_writeErrorMsg(ERR_TRANSECT_OVERBANK, Transect!![j]!!.ID)
        return
    }
    Nchannel = (Nchannel * sqrt(Lfactor))
    Transect!![j]!!.lengthFactor = Lfactor
    ymax = Elev!![1]
    ymin = Elev!![1]
    i = 2
    f1@ while ((i <= Nstations)) {
        ymax = ((if ((((Elev!![i]) >= (ymax)))) (Elev!![i]) else (ymax)))
        ymin = ((if ((((Elev!![i]) <= (ymin)))) (Elev!![i]) else (ymin)))
        i++
    }
    if ((ymin >= ymax)) {
        report_writeErrorMsg(ERR_TRANSECT_NO_DEPTH, Transect!![j]!!.ID)
        return
    }
    Station!![0] = Station!![1]
    Elev!![0] = ymax
    Nstations++
    Station!![Nstations] = Station!![(Nstations - 1)]
    Elev!![Nstations] = Elev!![0]
    Transect!![j]!!.nTbl = 51
    createTables(Transect!![j], ymin, ymax)
    Transect!![j]!!.roughness = oldNchannel
}

// transect.c:264
fun createTables(transect: TTransect?, ymin: Double, ymax: Double) {
    var i: Int = 0
    var nLast: Int = 0
    var dy: Double = 0.0
    var y: Double = 0.0
    transect!!.yFull = (ymax - ymin)
    transect!!.wMax = 0.0
    transect!!.areaTbl!![0] = 0.0
    transect!!.hradTbl!![0] = 0.0
    transect!!.widthTbl!![0] = 0.0
    dy = (((ymax - ymin)) / ((((transect!!.nTbl)).toDouble() - 1.0)))
    y = ymin
    i = 1
    f1@ while ((i < transect!!.nTbl)) {
        y += dy
        transect!!.areaTbl!![i] = 0.0
        transect!!.hradTbl!![i] = 0.0
        transect!!.widthTbl!![i] = 0.0
        getGeometry(transect, i, y)
        i++
    }
    setMaxSectionFactor(transect)
    nLast = (transect!!.nTbl - 1)
    transect!!.aFull = transect!!.areaTbl!![nLast]
    transect!!.rFull = transect!!.hradTbl!![nLast]
    transect!!.wMax = transect!!.widthTbl!![nLast]
    i = 1
    f2@ while ((i <= nLast)) {
        transect!!.areaTbl!![i] /= transect!!.aFull
        transect!!.hradTbl!![i] /= transect!!.rFull
        transect!!.widthTbl!![i] /= transect!!.wMax
        i++
    }
    transect!!.widthTbl!![0] = transect!!.widthTbl!![1]
}

// transect.c:312
fun setManning(n: DoubleArray?): Int {
    var i: Int = 0
    i = 1
    f1@ while ((i <= 3)) {
        if ((n!![i] < 0.0)) {
            return ERR_NUMBER
        }
        i++
    }
    if ((n!![1] > 0.0)) {
        Nleft = n!![1]
    }
    if ((n!![2] > 0.0)) {
        Nright = n!![2]
    }
    if ((n!![3] > 0.0)) {
        Nchannel = n!![3]
    }
    if ((Nleft == 0.0)) {
        Nleft = Nchannel
    }
    if ((Nright == 0.0)) {
        Nright = Nchannel
    }
    return 0
}

// transect.c:334
fun setParams(j: Int, id: CPtr?, x: DoubleArray?): Int {
    if (((j < 0) || (j >= Ntransects))) {
        return ERR_NUMBER
    }
    Transect!![j]!!.ID = id
    Xleftbank = (x!![3] / UCF(LENGTH))
    Xrightbank = (x!![4] / UCF(LENGTH))
    Lfactor = x!![7]
    if ((Lfactor == 0.0)) {
        Lfactor = 1.0
    }
    Xfactor = x!![8]
    if ((Xfactor == 0.0)) {
        Xfactor = 1.0
    }
    Xleftbank *= Xfactor
    Xrightbank *= Xfactor
    Yfactor = (x!![9] / UCF(LENGTH))
    Nstations = 0
    return 0
}

// transect.c:360
fun addStation(y: Double, x: Double): Int {
    if ((Nstations < 0)) {
        return ERR_TRANSECT_UNKNOWN
    }
    Nstations++
    if ((Nstations >= 1500)) {
        return 0
    }
    Station!![Nstations] = ((x * Xfactor) / UCF(LENGTH))
    Elev!![Nstations] = (((y + Yfactor)) / UCF(LENGTH))
    if (((Nstations > 1) && (Station!![Nstations] < Station!![(Nstations - 1)]))) {
        return ERR_TRANSECT_SEQUENCE
    }
    return 0
}

// transect.c:388
fun getGeometry(transect: TTransect?, i: Int, y: Double) {
    var k: Int = 0
    var ylo: Double = 0.0
    var yhi: Double = 0.0
    val w = DBox(0.0)
    val wp = DBox(0.0)
    var wpSum: Double = 0.0
    val a = DBox(0.0)
    var aSum: Double = 0.0
    var q: Double = 0.0
    var qSum: Double = 0.0
    var findFlow: Int = 0
    wpSum = 0.0
    aSum = 0.0
    qSum = 0.0
    k = 1
    f1@ while ((k <= Nstations)) {
        if ((Elev!![(k - 1)] >= Elev!![k])) {
            yhi = Elev!![(k - 1)]
            ylo = Elev!![k]
        } else {
            yhi = Elev!![k]
            ylo = Elev!![(k - 1)]
        }
        if ((ylo >= y)) {
            k++
            continue@f1
        }
        getSliceGeom(k, y, ylo, yhi, w, a, wp)
        wpSum += wp.v
        aSum += a.v
        transect!!.areaTbl!![i] += a.v
        transect!!.widthTbl!![i] += w.v
        if ((Elev!![k] >= y)) {
            findFlow = 1
        } else {
            findFlow = 0
        }
        q = getFlow(k, aSum, wpSum, findFlow)
        if ((q > 0.0)) {
            qSum += q
            aSum = 0.0
            wpSum = 0.0
        }
        k++
    }
    aSum = transect!!.areaTbl!![i]
    if ((aSum == 0.0)) {
        transect!!.hradTbl!![i] = transect!!.hradTbl!![(i - 1)]
    } else {
        transect!!.hradTbl!![i] = Glibm.pow((((qSum * Nchannel) / 1.49) / aSum), 1.5)
    }
}

// transect.c:468
fun getSliceGeom(k: Int, y: Double, ylo: Double, yhi: Double, w: DRef?, a: DRef?, wp: DRef?) {
    var width: Double = 0.0
    var ratio: Double = 0.0
    width = abs((Station!![k] - Station!![(k - 1)]))
    w!!.set(width)
    wp!!.set(sqrt(((width * width) + (((yhi - ylo)) * ((yhi - ylo))))))
    a!!.set(0.0)
    if ((y > yhi)) {
        a!!.set(((width * ((((y - yhi)) + ((y - ylo))))) / 2.0))
    } else if ((yhi > ylo)) {
        ratio = (((y - ylo)) / ((yhi - ylo)))
        a!!.set(((((width * ((yhi - ylo))) / 2.0) * ratio) * ratio))
        w!!.set((w!!.get() * ratio))
        wp!!.set((wp!!.get() * ratio))
    }
}

// transect.c:519
fun getFlow(k: Int, a: Double, wp: Double, __findFlow: Int): Double {
    var findFlow: Int = __findFlow
    var n: Double = 0.0
    if ((findFlow == 0)) {
        if ((k == (Nstations - 1))) {
            findFlow = 1
        } else if ((Station!![k] == Xleftbank)) {
            if (((Nleft != Nchannel) && (Station!![k] != Station!![(k - 1)]))) {
                findFlow = 1
            }
        } else if ((Station!![k] == Xrightbank)) {
            if (((Nright != Nchannel) && (Station!![k] != Station!![(k + 1)]))) {
                findFlow = 1
            }
        }
    }
    if ((findFlow != 0)) {
        n = Nchannel
        if ((Station!![(k - 1)] < Xleftbank)) {
            n = Nleft
        }
        if ((Station!![k] > Xrightbank)) {
            n = Nright
        }
        return (((1.486 / n) * a) * Glibm.pow((a / wp), (2.0 / 3.0)))
    }
    return 0.0
}

// transect.c:570
fun setMaxSectionFactor(transect: TTransect?) {
    var i: Int = 0
    var sf: Double = 0.0
    transect!!.aMax = 0.0
    transect!!.sMax = 0.0
    i = 1
    f1@ while ((i < transect!!.nTbl)) {
        sf = (transect!!.areaTbl!![i] * Glibm.pow(transect!!.hradTbl!![i], (2.0 / 3.0)))
        if ((sf > transect!!.sMax)) {
            transect!!.sMax = sf
            transect!!.aMax = transect!!.areaTbl!![i]
        }
        i++
    }
}

// transect.c:596
fun transect_createStreetTransect(street: TStreet?) {
    var ymin: Double = 0.0
    var ymax: Double = 0.0
    var y1: Double = 0.0
    var y3: Double = 0.0
    var y4: Double = 0.0
    var w1: Double = 0.0
    var w2: Double = 0.0
    var w3: Double = 0.0
    var w4: Double = 0.0
    ymin = 0.0
    w1 = street!!.backWidth
    w2 = street!!.gutterWidth
    w3 = street!!.width
    w4 = (w3 - w2)
    y3 = (street!!.gutterDepression + (street!!.slope * w2))
    y1 = (street!!.curbHeight + street!!.gutterDepression)
    ymax = ((street!!.backSlope * street!!.backWidth) + y1)
    y4 = (y3 + (street!!.slope * w4))
    ymax = ((if ((((ymax) >= (y4)))) (ymax) else (y4)))
    Station!![0] = 0.0
    Elev!![0] = ymax
    Station!![1] = w1
    Elev!![1] = y1
    Station!![2] = w1
    Elev!![2] = 0.0
    Station!![3] = (w1 + w2)
    Elev!![3] = y3
    Station!![4] = (w1 + w3)
    Elev!![4] = y4
    if ((street!!.sides == 1)) {
        Station!![5] = Station!![4]
        Elev!![5] = ymax
        Nstations = 5
        street!!.transect!!.nTbl = 51
    } else {
        Station!![5] = (Station!![4] + w4)
        Elev!![5] = y3
        Station!![6] = (Station!![5] + w2)
        Elev!![6] = 0.0
        Station!![7] = Station!![6]
        Elev!![7] = y1
        Station!![8] = (Station!![7] + w1)
        Elev!![8] = ymax
        Nstations = 8
        street!!.transect!!.nTbl = 51
    }
    Nchannel = street!!.roughness
    if ((street!!.backWidth == 0.0)) {
        Nleft = Nchannel
        Nright = Nchannel
        Xleftbank = Station!![0]
        Xrightbank = Station!![Nstations]
    } else {
        Nleft = street!!.backRoughness
        Nright = Nleft
        Xleftbank = Station!![1]
        if ((street!!.sides == 2)) {
            Xrightbank = Station!![(Nstations - 1)]
        } else {
            Xrightbank = Station!![Nstations]
        }
    }
    createTables(street!!.transect, ymin, ymax)
    street!!.transect!!.roughness = street!!.roughness
}

