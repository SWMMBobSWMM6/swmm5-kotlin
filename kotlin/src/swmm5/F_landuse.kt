// Generated from EPA SWMM 5.2.4 (landuse.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_landuse")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// landuse.c:51
fun landuse_readParams(j: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var id: CPtr? = null
    if ((ntoks < 1)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(LANDUSE, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    Landuse!![j]!!.ID = id
    if ((ntoks > 1)) {
        if ((ntoks < 4)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if (!(getDouble(tok!![1], DField<TLanduse>(Landuse!![j]!!, { __o -> __o.sweepInterval }, { __o, __x -> __o.sweepInterval = __x })) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![1])
        }
        if (!(getDouble(tok!![2], DField<TLanduse>(Landuse!![j]!!, { __o -> __o.sweepRemoval }, { __o, __x -> __o.sweepRemoval = __x })) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![2])
        }
        if (!(getDouble(tok!![3], DField<TLanduse>(Landuse!![j]!!, { __o -> __o.sweepDays0 }, { __o, __x -> __o.sweepDays0 = __x })) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![3])
        }
    } else {
        Landuse!![j]!!.sweepInterval = 0.0
        Landuse!![j]!!.sweepRemoval = 0.0
        Landuse!![j]!!.sweepDays0 = 0.0
    }
    if (((Landuse!![j]!!.sweepRemoval < 0.0) || (Landuse!![j]!!.sweepRemoval > 1.0))) {
        return error_setInpError(ERR_NUMBER, tok!![2])
    }
    return 0
}

// landuse.c:92
fun landuse_readPollutParams(j: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    var coPollut: Int = 0
    var snowFlag: Int = 0
    var x: DoubleArray? = DoubleArray(4)
    val coFrac = DBox(0.0)
    val cDWF = DBox(0.0)
    val cInit = DBox(0.0)
    var id: CPtr? = null
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(POLLUT, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = findmatch(tok!![1], QualUnitsWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    i = 2
    f1@ while ((i <= 4)) {
        if (((!(getDouble(tok!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        i++
    }
    if (!(getDouble(tok!![5], DElem(x!!, 3)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![5])
    }
    snowFlag = 0
    coPollut = (-1)
    coFrac.v = 0.0
    cDWF.v = 0.0
    cInit.v = 0.0
    if ((ntoks >= 7)) {
        snowFlag = findmatch(tok!![6], NoYesWords)
        if ((snowFlag < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![6])
        }
    }
    if ((ntoks >= 9)) {
        if (!(strcomp(tok!![7], __L275) != 0)) {
            coPollut = project_findObject(POLLUT, tok!![7])
            if ((coPollut < 0)) {
                return error_setInpError(ERR_NAME, tok!![7])
            }
            if (((!(getDouble(tok!![8], coFrac) != 0)) || (coFrac.v < 0.0))) {
                return error_setInpError(ERR_NUMBER, tok!![8])
            }
        }
    }
    if ((ntoks >= 10)) {
        if (((!(getDouble(tok!![9], cDWF) != 0)) || (cDWF.v < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![9])
        }
    }
    if ((ntoks >= 11)) {
        if (((!(getDouble(tok!![10], cInit) != 0)) || (cInit.v < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![9])
        }
    }
    Pollut!![j]!!.ID = id
    Pollut!![j]!!.units = k
    if ((Pollut!![j]!!.units == MG)) {
        Pollut!![j]!!.mcf = UCF(MASS)
    } else if ((Pollut!![j]!!.units == UG)) {
        Pollut!![j]!!.mcf = (UCF(MASS) / 1000.0)
    } else {
        Pollut!![j]!!.mcf = 1.0
    }
    Pollut!![j]!!.pptConcen = x!![0]
    Pollut!![j]!!.gwConcen = x!![1]
    Pollut!![j]!!.rdiiConcen = x!![2]
    Pollut!![j]!!.kDecay = (x!![3] / 86400.0)
    Pollut!![j]!!.snowOnly = snowFlag
    Pollut!![j]!!.coPollut = coPollut
    Pollut!![j]!!.coFraction = coFrac.v
    Pollut!![j]!!.dwfConcen = cDWF.v
    Pollut!![j]!!.initConcen = cInit.v
    return 0
}

// landuse.c:190
fun landuse_readBuildupParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var n: Int = 0
    var p: Int = 0
    var c: DoubleArray? = doubleArrayOf(0.0, 0.0, 0.0)
    var tmax: Double = 0.0
    if ((ntoks < 3)) {
        return 0
    }
    j = project_findObject(LANDUSE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    p = project_findObject(POLLUT, tok!![1])
    if ((p < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    k = findmatch(tok!![2], BuildupTypeWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![2])
    }
    Landuse!![j]!!.buildupFunc!![p]!!.funcType = k
    if ((k > NO_BUILDUP)) {
        if ((ntoks < 7)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if ((k != EXTERNAL_BUILDUP)) {
            i = 0
            f1@ while ((i < 3)) {
                if (((!(getDouble(tok!![(i + 3)], DElem(c!!, i)) != 0)) || (c!![i] < 0.0))) {
                    return error_setInpError(ERR_NUMBER, tok!![(i + 3)])
                }
                i++
            }
        }
        n = findmatch(tok!![6], NormalizerWords)
        if ((n < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![6])
        }
        Landuse!![j]!!.buildupFunc!![p]!!.normalizer = n
    }
    when (Landuse!![j]!!.buildupFunc!![p]!!.funcType) {
        POWER_BUILDUP -> {
            if (((c!![2] > 0.0) && (((c!![2] < 0.01) || (c!![2] > 10.0))))) {
                return error_setInpError(ERR_KEYWORD, tok!![5])
            }
            if (((c!![1] * c!![2]) == 0.0)) {
                tmax = 0.0
            } else if (((Glibm.log10(c!![0]) / c!![2]) > 3.5)) {
                tmax = 3650.0
            } else {
                tmax = Glibm.pow((c!![0] / c!![1]), (1.0 / c!![2]))
            }
        }
        EXPON_BUILDUP -> {
            if ((c!![1] == 0.0)) {
                tmax = 0.0
            } else {
                tmax = ((-(Glibm.log(0.001))) / c!![1])
            }
        }
        SATUR_BUILDUP -> {
            tmax = (1000.0 * c!![2])
        }
        EXTERNAL_BUILDUP -> {
            if (((!(getDouble(tok!![3], DElem(c!!, 0)) != 0)) || (c!![0] < 0.0))) {
                return error_setInpError(ERR_NUMBER, tok!![3])
            }
            if (((!(getDouble(tok!![4], DElem(c!!, 1)) != 0)) || (c!![1] < 0.0))) {
                return error_setInpError(ERR_NUMBER, tok!![3])
            }
            n = project_findObject(TSERIES, tok!![5])
            if ((n < 0)) {
                return error_setInpError(ERR_NAME, tok!![4])
            }
            Tseries!![n]!!.refersTo = EXTERNAL_BUILDUP
            c!![2] = (n).toDouble()
            tmax = 0.0
        }
        else -> {
            tmax = 0.0
        }
    }
    Landuse!![j]!!.buildupFunc!![p]!!.coeff!![0] = c!![0]
    Landuse!![j]!!.buildupFunc!![p]!!.coeff!![1] = c!![1]
    Landuse!![j]!!.buildupFunc!![p]!!.coeff!![2] = c!![2]
    Landuse!![j]!!.buildupFunc!![p]!!.maxDays = tmax
    return 0
}

// landuse.c:281
fun landuse_readWashoffParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var p: Int = 0
    var func: Int = 0
    var x: DoubleArray? = DoubleArray(4)
    if ((ntoks < 3)) {
        return 0
    }
    i = 0
    f1@ while ((i < 4)) {
        x!![i] = 0.0
        i++
    }
    func = NO_WASHOFF
    j = project_findObject(LANDUSE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    p = project_findObject(POLLUT, tok!![1])
    if ((p < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    if ((ntoks > 2)) {
        func = findmatch(tok!![2], WashoffTypeWords)
        if ((func < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![2])
        }
        if ((func != NO_WASHOFF)) {
            if ((ntoks < 5)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            if (!(getDouble(tok!![3], DElem(x!!, 0)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![3])
            }
            if (!(getDouble(tok!![4], DElem(x!!, 1)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![4])
            }
            if ((ntoks >= 6)) {
                if (!(getDouble(tok!![5], DElem(x!!, 2)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![5])
                }
            }
            if ((ntoks >= 7)) {
                if (!(getDouble(tok!![6], DElem(x!!, 3)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![6])
                }
            }
        }
    }
    if ((x!![0] < 0.0)) {
        return error_setInpError(ERR_NUMBER, tok!![3])
    }
    if (((x!![1] < (-10.0)) || (x!![1] > 10.0))) {
        return error_setInpError(ERR_NUMBER, tok!![4])
    }
    if (((x!![2] < 0.0) || (x!![2] > 100.0))) {
        return error_setInpError(ERR_NUMBER, tok!![5])
    }
    if (((x!![3] < 0.0) || (x!![3] > 100.0))) {
        return error_setInpError(ERR_NUMBER, tok!![6])
    }
    if ((func == EXPON_WASHOFF)) {
        x!![0] /= 3600.0
    }
    if ((func == RATING_WASHOFF)) {
        x!![0] *= (Glibm.pow(UCF(FLOW), x!![1]))
    }
    if ((func == EMC_WASHOFF)) {
        x!![0] *= 28.317
    }
    Landuse!![j]!!.washoffFunc!![p]!!.funcType = func
    Landuse!![j]!!.washoffFunc!![p]!!.coeff = x!![0]
    Landuse!![j]!!.washoffFunc!![p]!!.expon = x!![1]
    Landuse!![j]!!.washoffFunc!![p]!!.sweepEffic = (x!![2] / 100.0)
    Landuse!![j]!!.washoffFunc!![p]!!.bmpEffic = (x!![3] / 100.0)
    return 0
}

// landuse.c:355
fun landuse_getInitBuildup(landFactor: Array<TLandFactor?>?, initBuildup: DoubleArray?, area: Double, curb: Double) {
    var i: Int = 0
    var p: Int = 0
    var startDrySeconds: Double = 0.0
    var f: Double = 0.0
    var fArea: Double = 0.0
    var fCurb: Double = 0.0
    var buildup: Double = 0.0
    startDrySeconds = (StartDryDays * 86400.0)
    i = 0
    f1@ while ((i < Nobjects!![LANDUSE])) {
        landFactor!![i]!!.lastSwept = (StartDateTime - Landuse!![i]!!.sweepDays0)
        f = landFactor!![i]!!.fraction
        fArea = ((f * area) * UCF(LANDAREA))
        fCurb = (f * curb)
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            buildup = 0.0
            if ((initBuildup!![p] > 0.0)) {
                buildup = (initBuildup!![p] * fArea)
            } else {
                buildup = landuse_getBuildup(i, p, fArea, fCurb, buildup, startDrySeconds)
            }
            landFactor!![i]!!.buildup!![p] = buildup
            p++
        }
        i++
    }
}

// landuse.c:411
fun landuse_getBuildup(i: Int, p: Int, area: Double, curb: Double, buildup: Double, tStep: Double): Double {
    var n: Int = 0
    var days: Double = 0.0
    var perUnit: Double = 0.0
    if (((Landuse!![i]!!.buildupFunc!![p]!!.funcType == NO_BUILDUP) || (tStep == 0.0))) {
        return buildup
    }
    n = Landuse!![i]!!.buildupFunc!![p]!!.normalizer
    perUnit = 1.0
    if ((n == PER_AREA)) {
        perUnit = area
    }
    if ((n == PER_CURB)) {
        perUnit = curb
    }
    if ((perUnit == 0.0)) {
        return 0.0
    }
    if ((Landuse!![i]!!.buildupFunc!![p]!!.funcType == EXTERNAL_BUILDUP)) {
        return (landuse_getExternalBuildup(i, p, (buildup / perUnit), tStep) * perUnit)
    }
    days = landuse_getBuildupDays(i, p, (buildup / perUnit))
    days += (tStep / 86400.0)
    return (landuse_getBuildupMass(i, p, days) * perUnit)
}

// landuse.c:458
fun landuse_getBuildupDays(i: Int, p: Int, buildup: Double): Double {
    var c0: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![0]
    var c1: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![1]
    var c2: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![2]
    if ((buildup == 0.0)) {
        return 0.0
    }
    if ((buildup >= c0)) {
        return Landuse!![i]!!.buildupFunc!![p]!!.maxDays
    }
    when (Landuse!![i]!!.buildupFunc!![p]!!.funcType) {
        POWER_BUILDUP -> {
            if (((c1 * c2) == 0.0)) {
                return 0.0
            } else {
                return Glibm.pow(((buildup / c1)), ((1.0 / c2)))
            }
        }
        EXPON_BUILDUP -> {
            if (((c0 * c1) == 0.0)) {
                return 0.0
            } else {
                return ((-(Glibm.log((1.0 - (buildup / c0))))) / c1)
            }
        }
        SATUR_BUILDUP -> {
            if ((c0 == 0.0)) {
                return 0.0
            } else {
                return ((buildup * c2) / ((c0 - buildup)))
            }
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("landuse_getBuildupDays: no return")
}

// landuse.c:494
fun landuse_getBuildupMass(i: Int, p: Int, days: Double): Double {
    var b: Double = 0.0
    var c0: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![0]
    var c1: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![1]
    var c2: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![2]
    if ((days == 0.0)) {
        return 0.0
    }
    if ((days >= Landuse!![i]!!.buildupFunc!![p]!!.maxDays)) {
        return c0
    }
    when (Landuse!![i]!!.buildupFunc!![p]!!.funcType) {
        POWER_BUILDUP -> {
            b = (c1 * Glibm.pow(days, c2))
            if ((b > c0)) {
                b = c0
            }
        }
        EXPON_BUILDUP -> {
            b = (c0 * ((1.0 - Glibm.exp(((-days) * c1)))))
        }
        SATUR_BUILDUP -> {
            b = ((days * c0) / ((c2 + days)))
        }
        else -> {
            b = 0.0
        }
    }
    return b
}

// landuse.c:532
fun landuse_getAvgBmpEffic(j: Int, p: Int): Double {
    var i: Int = 0
    var r: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![LANDUSE])) {
        r += (Subcatch!![j]!!.landFactor!![i]!!.fraction * Landuse!![i]!!.washoffFunc!![p]!!.bmpEffic)
        i++
    }
    return r
}

// landuse.c:553
fun landuse_getWashoffLoad(i: Int, p: Int, area: Double, landFactor: Array<TLandFactor?>?, runoff: Double, vOutflow: Double): Double {
    var landuseArea: Double = 0.0
    var buildup: Double = 0.0
    var washoffQual: Double = 0.0
    var washoffLoad: Double = 0.0
    var bmpRemoval: Double = 0.0
    buildup = landFactor!![i]!!.buildup!![p]
    landuseArea = (landFactor!![i]!!.fraction * area)
    washoffQual = landuse_getWashoffQual(i, p, buildup, runoff, landuseArea)
    washoffLoad = ((((washoffQual * vOutflow) * landuseArea) / area) * Pollut!![p]!!.mcf)
    if (((Landuse!![i]!!.buildupFunc!![p]!!.funcType != NO_BUILDUP) || (buildup > washoffLoad))) {
        washoffLoad = ((if ((((washoffLoad) <= (buildup)))) (washoffLoad) else (buildup)))
        buildup -= washoffLoad
        landFactor!![i]!!.buildup!![p] = buildup
    } else {
        massbal_updateLoadingTotals(BUILDUP_LOAD, p, washoffLoad)
        landFactor!![i]!!.buildup!![p] = 0.0
    }
    bmpRemoval = (Landuse!![i]!!.washoffFunc!![p]!!.bmpEffic * washoffLoad)
    if ((bmpRemoval > 0.0)) {
        massbal_updateLoadingTotals(BMP_REMOVAL_LOAD, p, bmpRemoval)
        washoffLoad -= bmpRemoval
    }
    return (washoffLoad / Pollut!![p]!!.mcf)
}

// landuse.c:612
fun landuse_getWashoffQual(i: Int, p: Int, buildup: Double, runoff: Double, area: Double): Double {
    var cWashoff: Double = 0.0
    var coeff: Double = Landuse!![i]!!.washoffFunc!![p]!!.coeff
    var expon: Double = Landuse!![i]!!.washoffFunc!![p]!!.expon
    var func: Int = Landuse!![i]!!.washoffFunc!![p]!!.funcType
    if (((func == NO_WASHOFF) || (runoff == 0.0))) {
        return 0.0
    }
    if (((Landuse!![i]!!.buildupFunc!![p]!!.funcType != NO_BUILDUP) && (buildup == 0.0))) {
        return 0.0
    }
    if ((func == EXPON_WASHOFF)) {
        cWashoff = (((coeff * Glibm.pow((runoff * UCF(RAINFALL)), expon)) * buildup) / Pollut!![p]!!.mcf)
        cWashoff /= (runoff * area)
    } else if ((func == RATING_WASHOFF)) {
        cWashoff = (coeff * Glibm.pow((runoff * area), (expon - 1.0)))
    } else if ((func == EMC_WASHOFF)) {
        cWashoff = coeff
    }
    return cWashoff
}

// landuse.c:665
fun landuse_getCoPollutLoad(p: Int, washoff: DoubleArray?): Double {
    var k: Int = 0
    var w: Double = 0.0
    k = Pollut!![p]!!.coPollut
    if ((k >= 0)) {
        w = (Pollut!![p]!!.coFraction * washoff!![k])
        massbal_updateLoadingTotals(BUILDUP_LOAD, p, (w * Pollut!![p]!!.mcf))
        return w
    }
    return 0.0
}

// landuse.c:693
fun landuse_getExternalBuildup(i: Int, p: Int, __buildup: Double, tStep: Double): Double {
    var buildup: Double = __buildup
    var maxBuildup: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![0]
    var sf: Double = Landuse!![i]!!.buildupFunc!![p]!!.coeff!![1]
    var ts: Int = R.D2I(floor(Landuse!![i]!!.buildupFunc!![p]!!.coeff!![2]))
    var rate: Double = 0.0
    if ((NewRunoffTime == 0.0)) {
        return 0.0
    }
    if ((ts >= 0)) {
        rate = (sf * table_tseriesLookup(Tseries!![ts], getDateTime(NewRunoffTime), 0))
    }
    buildup = (buildup + ((rate * tStep) / 86400.0))
    buildup = ((if ((((buildup) <= (maxBuildup)))) (buildup) else (maxBuildup)))
    return buildup
}

