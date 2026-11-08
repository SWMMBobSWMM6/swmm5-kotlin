// Generated from EPA SWMM 5.2.4 (subcatch.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_subcatch")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MCOEFF: Double = 1.49
@JvmField var MEXP: Double = 1.6666667
@JvmField var ODETOL: Double = 0.0001
@JvmField var Vevap: Double = 0.0
@JvmField var Vpevap: Double = 0.0
@JvmField var Vinfil: Double = 0.0
@JvmField var Vinflow: Double = 0.0
@JvmField var Voutflow: Double = 0.0
@JvmField var VlidIn: Double = 0.0
@JvmField var VlidInfil: Double = 0.0
@JvmField var VlidOut: Double = 0.0
@JvmField var VlidDrain: Double = 0.0
@JvmField var VlidReturn: Double = 0.0
@JvmField var theSubarea: TSubarea? = null
@JvmField var Dstore: Double = 0.0
@JvmField var Alpha: Double = 0.0
@JvmField var RunoffRoutingWords: Array<CPtr?>? = arrayOf<CPtr?>(__L663, __L2, __L3, null)

// subcatch.c:119
fun subcatch_readParams(j: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    var m: Int = 0
    var id: CPtr? = null
    var x: DoubleArray? = DoubleArray(9)
    if ((ntoks < 8)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(SUBCATCH, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = project_findObject(GAGE, tok!![1])
    if ((k < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    x!![0] = (k).toDouble()
    m = project_findObject(NODE, tok!![2])
    x!![1] = (m).toDouble()
    m = project_findObject(SUBCATCH, tok!![2])
    x!![2] = (m).toDouble()
    if (((x!![1] < 0.0) && (x!![2] < 0.0))) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    i = 3
    f1@ while ((i < 8)) {
        if (((!(getDouble(tok!![i], DElem(x!!, i)) != 0)) || (x!![i] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        i++
    }
    x!![8] = ((-1)).toDouble()
    if ((ntoks > 8)) {
        k = project_findObject(SNOWMELT, tok!![8])
        if ((k < 0)) {
            return error_setInpError(ERR_NAME, tok!![8])
        }
        x!![8] = (k).toDouble()
    }
    Subcatch!![j]!!.ID = id
    Subcatch!![j]!!.gage = R.D2I(x!![0])
    Subcatch!![j]!!.outNode = R.D2I(x!![1])
    Subcatch!![j]!!.outSubcatch = R.D2I(x!![2])
    Subcatch!![j]!!.area = (x!![3] / UCF(LANDAREA))
    Subcatch!![j]!!.fracImperv = (((if ((((x!![4]) <= (100.0)))) (x!![4]) else (100.0))) / 100.0)
    Subcatch!![j]!!.width = (x!![5] / UCF(LENGTH))
    Subcatch!![j]!!.slope = (x!![6] / 100.0)
    Subcatch!![j]!!.curbLength = x!![7]
    Subcatch!![j]!!.nPervPattern = (-1)
    Subcatch!![j]!!.dStorePattern = (-1)
    Subcatch!![j]!!.infilPattern = (-1)
    if ((x!![8] >= 0.0)) {
        if (!(snow_createSnowpack(j, R.D2I(x!![8])) != 0)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
    }
    return 0
}

// subcatch.c:196
fun subcatch_readSubareaParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    var x: DoubleArray? = DoubleArray(7)
    if ((ntoks < 7)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SUBCATCH, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    i = 0
    f1@ while ((i < 5)) {
        if (((!(getDouble(tok!![(i + 1)], DElem(x!!, i)) != 0)) || (x!![i] < 0.0))) {
            return error_setInpError(ERR_NAME, tok!![(i + 1)])
        }
        i++
    }
    m = findmatch(tok!![6], RunoffRoutingWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![6])
    }
    x!![5] = (m).toDouble()
    x!![6] = 1.0
    if ((ntoks >= 8)) {
        if ((((!(getDouble(tok!![7], DElem(x!!, 6)) != 0)) || (x!![6] < 0.0)) || (x!![6] > 100.0))) {
            return error_setInpError(ERR_NUMBER, tok!![7])
        }
        x!![6] /= 100.0
    }
    Subcatch!![j]!!.subArea!![IMPERV0]!!.N = x!![0]
    Subcatch!![j]!!.subArea!![IMPERV1]!!.N = x!![0]
    Subcatch!![j]!!.subArea!![PERV]!!.N = x!![1]
    Subcatch!![j]!!.subArea!![IMPERV0]!!.dStore = 0.0
    Subcatch!![j]!!.subArea!![IMPERV1]!!.dStore = (x!![2] / UCF(RAINDEPTH))
    Subcatch!![j]!!.subArea!![PERV]!!.dStore = (x!![3] / UCF(RAINDEPTH))
    Subcatch!![j]!!.subArea!![IMPERV0]!!.fArea = ((Subcatch!![j]!!.fracImperv * x!![4]) / 100.0)
    Subcatch!![j]!!.subArea!![IMPERV1]!!.fArea = (Subcatch!![j]!!.fracImperv * ((1.0 - (x!![4] / 100.0))))
    Subcatch!![j]!!.subArea!![PERV]!!.fArea = ((1.0 - Subcatch!![j]!!.fracImperv))
    i = IMPERV0
    f2@ while ((i <= PERV)) {
        Subcatch!![j]!!.subArea!![i]!!.routeTo = TO_OUTLET
        Subcatch!![j]!!.subArea!![i]!!.fOutlet = 1.0
        i++
    }
    k = R.D2I(x!![5])
    if (((Subcatch!![j]!!.fracImperv == 0.0) || (Subcatch!![j]!!.fracImperv == 1.0))) {
        k = TO_OUTLET
    }
    if (((k == TO_IMPERV) && (Subcatch!![j]!!.fracImperv != 0.0))) {
        Subcatch!![j]!!.subArea!![PERV]!!.routeTo = k
        Subcatch!![j]!!.subArea!![PERV]!!.fOutlet = (1.0 - x!![6])
    }
    if ((k == TO_PERV)) {
        Subcatch!![j]!!.subArea!![IMPERV0]!!.routeTo = k
        Subcatch!![j]!!.subArea!![IMPERV1]!!.routeTo = k
        Subcatch!![j]!!.subArea!![IMPERV0]!!.fOutlet = (1.0 - x!![6])
        Subcatch!![j]!!.subArea!![IMPERV1]!!.fOutlet = (1.0 - x!![6])
    }
    return 0
}

// subcatch.c:284
fun subcatch_readLanduseParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    val f = DBox(0.0)
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SUBCATCH, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = 2
    f1@ while ((k <= ntoks)) {
        m = project_findObject(LANDUSE, tok!![(k - 1)])
        if ((m < 0)) {
            return error_setInpError(ERR_NAME, tok!![(k - 1)])
        }
        if (((k + 1) > ntoks)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if (!(getDouble(tok!![k], f) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![k])
        }
        Subcatch!![j]!!.landFactor!![m]!!.fraction = (f.v / 100.0)
        k = (k + 2)
    }
    return 0
}

// subcatch.c:324
fun subcatch_readInitBuildup(tok: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    val x = DBox(0.0)
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SUBCATCH, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = 2
    f1@ while ((k <= ntoks)) {
        m = project_findObject(POLLUT, tok!![(k - 1)])
        if ((m < 0)) {
            return error_setInpError(ERR_NAME, tok!![(k - 1)])
        }
        if (((k + 1) > ntoks)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if (!(getDouble(tok!![k], x) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![k])
        }
        Subcatch!![j]!!.initBuildup!![m] = x.v
        k = (k + 2)
    }
    return 0
}

// subcatch.c:364
fun subcatch_validate(j: Int) {
    var i: Int = 0
    var area: Double = 0.0
    var nonLidArea: Double = Subcatch!![j]!!.area
    if (((Subcatch!![j]!!.outNode >= 0) && (Subcatch!![j]!!.outSubcatch >= 0))) {
        report_writeErrorMsg(ERR_SUBCATCH_OUTLET, Subcatch!![j]!!.ID)
    }
    gwater_validate(j)
    nonLidArea -= Subcatch!![j]!!.lidArea
    i = IMPERV0
    f1@ while ((i <= PERV)) {
        if ((i == PERV)) {
            area = (((1.0 - Subcatch!![j]!!.fracImperv)) * nonLidArea)
        } else {
            area = (Subcatch!![j]!!.fracImperv * nonLidArea)
        }
        Subcatch!![j]!!.subArea!![i]!!.alpha = 0.0
        if (((area > 0.0) && (Subcatch!![j]!!.subArea!![i]!!.N > 0.0))) {
            Subcatch!![j]!!.subArea!![i]!!.alpha = ((((MCOEFF * Subcatch!![j]!!.width) / area) * sqrt(Subcatch!![j]!!.slope)) / Subcatch!![j]!!.subArea!![i]!!.N)
        }
        i++
    }
    i = Subcatch!![j]!!.gage
    if ((i >= 0)) {
        Gage!![i]!!.isUsed = 1
    }
}

// subcatch.c:419
fun subcatch_initState(j: Int) {
    var i: Int = 0
    Subcatch!![j]!!.rainfall = 0.0
    Subcatch!![j]!!.oldRunoff = 0.0
    Subcatch!![j]!!.newRunoff = 0.0
    Subcatch!![j]!!.oldSnowDepth = 0.0
    Subcatch!![j]!!.newSnowDepth = 0.0
    Subcatch!![j]!!.runon = 0.0
    Subcatch!![j]!!.evapLoss = 0.0
    Subcatch!![j]!!.infilLoss = 0.0
    if ((Subcatch!![j]!!.infil == j)) {
        infil_initState(j)
    }
    if ((Subcatch!![j]!!.groundwater != null)) {
        gwater_initState(j)
    }
    if ((Subcatch!![j]!!.snowpack != null)) {
        snow_initSnowpack(j)
    }
    i = IMPERV0
    f1@ while ((i <= PERV)) {
        Subcatch!![j]!!.subArea!![i]!!.depth = 0.0
        Subcatch!![j]!!.subArea!![i]!!.inflow = 0.0
        Subcatch!![j]!!.subArea!![i]!!.runoff = 0.0
        i++
    }
    surfqual_initState(j)
}

// subcatch.c:457
fun subcatch_setOldState(j: Int) {
    var i: Int = 0
    Subcatch!![j]!!.oldRunoff = Subcatch!![j]!!.newRunoff
    Subcatch!![j]!!.oldSnowDepth = Subcatch!![j]!!.newSnowDepth
    i = IMPERV0
    f1@ while ((i <= PERV)) {
        Subcatch!![j]!!.subArea!![i]!!.inflow = 0.0
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![POLLUT])) {
        Subcatch!![j]!!.oldQual!![i] = Subcatch!![j]!!.newQual!![i]
        Subcatch!![j]!!.newQual!![i] = 0.0
        i++
    }
    lid_setOldGroupState(j)
}

// subcatch.c:481
fun subcatch_getFracPerv(j: Int): Double {
    var fracPerv: Double = (1.0 - Subcatch!![j]!!.fracImperv)
    if ((Subcatch!![j]!!.lidArea > 0.0)) {
        fracPerv = ((((fracPerv * ((Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea))) + lid_getPervArea(j))) / Subcatch!![j]!!.area)
        fracPerv = ((if ((((fracPerv) <= (1.0)))) (fracPerv) else (1.0)))
    }
    return fracPerv
}

// subcatch.c:502
fun subcatch_getStorage(j: Int): Double {
    var i: Int = 0
    var v: Double = 0.0
    i = IMPERV0
    f1@ while ((i <= PERV)) {
        v += (Subcatch!![j]!!.subArea!![i]!!.depth * Subcatch!![j]!!.subArea!![i]!!.fArea)
        i++
    }
    return ((v * ((Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea))) + lid_getStoredVolume(j))
}

// subcatch.c:523
fun subcatch_getRunon(j: Int) {
    var k: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var q1: Double = 0.0
    var q2: Double = 0.0
    var pervArea: Double = 0.0
    k = Subcatch!![j]!!.outSubcatch
    q = Subcatch!![j]!!.oldRunoff
    if (((k >= 0) && (k != j))) {
        subcatch_addRunonFlow(k, q)
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            Subcatch!![k]!!.newQual!![p] += ((q * Subcatch!![j]!!.oldQual!![p]) * 28.317)
            p++
        }
    }
    if ((Subcatch!![j]!!.lidArea > 0.0)) {
        lid_addDrainRunon(j)
    }
    if (((Subcatch!![j]!!.fracImperv < 1.0) && (Subcatch!![j]!!.subArea!![IMPERV0]!!.routeTo == TO_PERV))) {
        q1 = (Subcatch!![j]!!.subArea!![IMPERV0]!!.runoff * Subcatch!![j]!!.subArea!![IMPERV0]!!.fArea)
        q2 = (Subcatch!![j]!!.subArea!![IMPERV1]!!.runoff * Subcatch!![j]!!.subArea!![IMPERV1]!!.fArea)
        q = (q1 + q2)
        Subcatch!![j]!!.subArea!![PERV]!!.inflow += ((q * ((1.0 - Subcatch!![j]!!.subArea!![IMPERV0]!!.fOutlet))) / Subcatch!![j]!!.subArea!![PERV]!!.fArea)
    }
    if ((((Subcatch!![j]!!.fracImperv > 0.0) && (Subcatch!![j]!!.subArea!![PERV]!!.routeTo == TO_IMPERV)) && (Subcatch!![j]!!.subArea!![IMPERV1]!!.fArea > 0.0))) {
        q = Subcatch!![j]!!.subArea!![PERV]!!.runoff
        Subcatch!![j]!!.subArea!![IMPERV1]!!.inflow += (((q * ((1.0 - Subcatch!![j]!!.subArea!![PERV]!!.fOutlet))) * Subcatch!![j]!!.subArea!![PERV]!!.fArea) / Subcatch!![j]!!.subArea!![IMPERV1]!!.fArea)
    }
    if (((Subcatch!![j]!!.lidArea > 0.0) && (Subcatch!![j]!!.fracImperv < 1.0))) {
        pervArea = (Subcatch!![j]!!.subArea!![PERV]!!.fArea * ((Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)))
        q = lid_getFlowToPerv(j)
        if ((pervArea > 0.0)) {
            Subcatch!![j]!!.subArea!![PERV]!!.inflow += (q / pervArea)
        }
    }
}

// subcatch.c:600
fun subcatch_addRunonFlow(k: Int, __q: Double) {
    var q: Double = __q
    var i: Int = 0
    var nonLidArea: Double = 0.0
    if ((Subcatch!![k]!!.area <= 0.0)) {
        return
    }
    nonLidArea = (Subcatch!![k]!!.area - Subcatch!![k]!!.lidArea)
    if ((nonLidArea > 0.0)) {
        q = (q / nonLidArea)
    } else {
        q = (q / Subcatch!![k]!!.area)
    }
    Subcatch!![k]!!.runon += q
    i = IMPERV0
    f1@ while ((i <= PERV)) {
        Subcatch!![k]!!.subArea!![i]!!.inflow += q
        i++
    }
}

// subcatch.c:629
fun subcatch_getRunoff(j: Int, tStep: Double): Double {
    var i: Int = 0
    var nonLidArea: Double = 0.0
    var area: Double = 0.0
    var netPrecip: DoubleArray? = DoubleArray(3)
    var vRain: Double = 0.0
    var vRunon: Double = 0.0
    var vOutflow: Double = 0.0
    var runoff: Double = 0.0
    var evapRate: Double = 0.0
    var subAreaRunoff: Double = 0.0
    var vImpervRunoff: Double = 0.0
    var vPervRunoff: Double = 0.0
    Vevap = 0.0
    Vpevap = 0.0
    Vinfil = 0.0
    Voutflow = 0.0
    VlidIn = 0.0
    VlidInfil = 0.0
    VlidOut = 0.0
    VlidDrain = 0.0
    VlidReturn = 0.0
    nonLidArea = (Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)
    vRunon = ((Subcatch!![j]!!.runon * tStep) * nonLidArea)
    Vinflow = (vRunon + (subcatch_getDepth(j) * nonLidArea))
    if ((nonLidArea == 0.0)) {
        vRunon = ((Subcatch!![j]!!.runon * tStep) * Subcatch!![j]!!.area)
    }
    getNetPrecip(j, netPrecip, tStep)
    if (((Evap!!.dryOnly != 0) && (Subcatch!![j]!!.rainfall > 0.0))) {
        evapRate = 0.0
    } else {
        evapRate = Evap!!.rate
    }
    infil_setInfilFactor(j)
    if ((nonLidArea > 0.0)) {
        i = IMPERV0
        f1@ while ((i <= PERV)) {
            area = (nonLidArea * Subcatch!![j]!!.subArea!![i]!!.fArea)
            Subcatch!![j]!!.subArea!![i]!!.runoff = getSubareaRunoff(j, i, area, netPrecip!![i], evapRate, tStep)
            subAreaRunoff = (Subcatch!![j]!!.subArea!![i]!!.runoff * area)
            if ((i == PERV)) {
                vPervRunoff = (subAreaRunoff * tStep)
            } else {
                vImpervRunoff += (subAreaRunoff * tStep)
            }
            runoff += subAreaRunoff
            i++
        }
    }
    if ((Subcatch!![j]!!.lidArea > 0.0)) {
        lid_getRunoff(j, tStep)
    }
    if (((!(IgnoreGwater != 0)) && (Subcatch!![j]!!.groundwater != null))) {
        gwater_getGroundwater(j, Vpevap, (Vinfil + VlidInfil), tStep)
    }
    area = Subcatch!![j]!!.area
    Subcatch!![j]!!.evapLoss = ((Vevap / tStep) / area)
    Subcatch!![j]!!.infilLoss = ((((Vinfil + VlidInfil)) / tStep) / area)
    vOutflow = ((Voutflow - VlidIn) + VlidOut)
    Subcatch!![j]!!.newRunoff = (vOutflow / tStep)
    vRain = ((Subcatch!![j]!!.rainfall * tStep) * area)
    stats_updateSubcatchStats(j, vRain, vRunon, Vevap, (Vinfil + VlidInfil), vImpervRunoff, vPervRunoff, (vOutflow + VlidDrain), (Subcatch!![j]!!.newRunoff + (VlidDrain / tStep)))
    if (((Subcatch!![j]!!.outNode == (-1)) && (Subcatch!![j]!!.outSubcatch != j))) {
        vOutflow = 0.0
    }
    massbal_updateRunoffTotals(RUNOFF_RAINFALL, vRain)
    massbal_updateRunoffTotals(RUNOFF_EVAP, Vevap)
    massbal_updateRunoffTotals(RUNOFF_INFIL, (Vinfil + VlidInfil))
    massbal_updateRunoffTotals(RUNOFF_RUNOFF, vOutflow)
    return (runoff / area)
}

// subcatch.c:758
fun getNetPrecip(j: Int, netPrecip: DoubleArray?, tStep: Double) {
    var i: Int = 0
    var k: Int = 0
    val rainfall = DBox(0.0)
    val snowfall = DBox(0.0)
    k = Subcatch!![j]!!.gage
    if ((k >= 0)) {
        gage_getPrecip(k, rainfall, snowfall)
    }
    Subcatch!![j]!!.rainfall = (rainfall.v + snowfall.v)
    if (((Subcatch!![j]!!.snowpack != null) && (!(IgnoreSnowmelt != 0)))) {
        Subcatch!![j]!!.newSnowDepth = snow_getSnowMelt(j, rainfall.v, snowfall.v, tStep, netPrecip)
    } else {
        i = IMPERV0
        f1@ while ((i <= PERV)) {
            netPrecip!![i] = (rainfall.v + snowfall.v)
            i++
        }
    }
}

// subcatch.c:798
fun subcatch_getDepth(j: Int): Double {
    var i: Int = 0
    var fArea: Double = 0.0
    var depth: Double = 0.0
    i = IMPERV0
    f1@ while ((i <= PERV)) {
        fArea = Subcatch!![j]!!.subArea!![i]!!.fArea
        if ((fArea > 0.0)) {
            depth += (Subcatch!![j]!!.subArea!![i]!!.depth * fArea)
        }
        i++
    }
    return depth
}

// subcatch.c:820
fun subcatch_getWtdOutflow(j: Int, f: Double): Double {
    if ((Subcatch!![j]!!.area == 0.0)) {
        return 0.0
    }
    return ((((1.0 - f)) * Subcatch!![j]!!.oldRunoff) + (f * Subcatch!![j]!!.newRunoff))
}

// subcatch.c:834
fun subcatch_getResults(j: Int, f: Double, x: FloatArray?) {
    var p: Int = 0
    var k: Int = 0
    var f1: Double = (1.0 - f)
    var z: Double = 0.0
    var runoff: Double = 0.0
    var gw: TGroundwater? = null
    k = Subcatch!![j]!!.gage
    if ((k >= 0)) {
        x!![SUBCATCH_RAINFALL] = (Gage!![k]!!.reportRainfall).toFloat()
    } else {
        x!![SUBCATCH_RAINFALL] = 0.0f
    }
    z = ((((f1 * Subcatch!![j]!!.oldSnowDepth) + (f * Subcatch!![j]!!.newSnowDepth))) * UCF(RAINDEPTH))
    x!![SUBCATCH_SNOWDEPTH] = (z).toFloat()
    x!![SUBCATCH_EVAP] = (((Subcatch!![j]!!.evapLoss * UCF(EVAPRATE)))).toFloat()
    x!![SUBCATCH_INFIL] = (((Subcatch!![j]!!.infilLoss * UCF(RAINFALL)))).toFloat()
    runoff = ((f1 * Subcatch!![j]!!.oldRunoff) + (f * Subcatch!![j]!!.newRunoff))
    if ((Subcatch!![j]!!.lidArea > 0.0)) {
        runoff += ((f1 * lid_getDrainFlow(j, PREVIOUS)) + (f * lid_getDrainFlow(j, CURRENT)))
    }
    if ((runoff < (2.31481e-08 * Subcatch!![j]!!.area))) {
        runoff = 0.0
    }
    x!![SUBCATCH_RUNOFF] = (((runoff * UCF(FLOW)))).toFloat()
    gw = Subcatch!![j]!!.groundwater
    if ((gw != null)) {
        z = (((((f1 * gw!!.oldFlow) + (f * gw!!.newFlow))) * Subcatch!![j]!!.area) * UCF(FLOW))
        x!![SUBCATCH_GW_FLOW] = (z).toFloat()
        z = (((gw!!.bottomElev + gw!!.lowerDepth)) * UCF(LENGTH))
        x!![SUBCATCH_GW_ELEV] = (z).toFloat()
        z = gw!!.theta
        x!![SUBCATCH_SOIL_MOIST] = (z).toFloat()
    } else {
        x!![SUBCATCH_GW_FLOW] = 0.0f
        x!![SUBCATCH_GW_ELEV] = 0.0f
        x!![SUBCATCH_SOIL_MOIST] = 0.0f
    }
    if (!(IgnoreQuality != 0)) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            if ((runoff == 0.0)) {
                z = 0.0
            } else {
                z = ((f1 * Subcatch!![j]!!.oldQual!![p]) + (f * Subcatch!![j]!!.newQual!![p]))
            }
            x!![(SUBCATCH_WASHOFF + p)] = (z).toFloat()
            p++
        }
    }
}

// subcatch.c:907
fun getSubareaRunoff(j: Int, i: Int, area: Double, precip: Double, evap: Double, tStep: Double): Double {
    val tRunoff = DBox(0.0)
    var surfMoisture: Double = 0.0
    var surfEvap: Double = 0.0
    var infil: Double = 0.0
    var runoff: Double = 0.0
    var subarea: TSubarea? = null
    if ((area == 0.0)) {
        return 0.0
    }
    subarea = Subcatch!![j]!!.subArea!![i]
    tRunoff.v = tStep
    surfMoisture = (subarea!!.depth / tStep)
    surfEvap = ((if ((((surfMoisture) <= (evap)))) (surfMoisture) else (evap)))
    if ((i == PERV)) {
        infil = getSubareaInfil(j, subarea, precip, tStep)
    }
    subarea!!.inflow += precip
    surfMoisture += subarea!!.inflow
    Vinflow += ((precip * area) * tStep)
    Vevap += ((surfEvap * area) * tStep)
    if ((i == PERV)) {
        Vpevap += Vevap
    }
    Vinfil += ((infil * area) * tStep)
    Alpha = subarea!!.alpha
    Dstore = subarea!!.dStore
    adjustSubareaParams(i, j)
    if (((surfEvap + infil) >= surfMoisture)) {
        subarea!!.depth = 0.0
    } else {
        subarea!!.inflow -= (surfEvap + infil)
        updatePondedDepth(subarea, tRunoff)
    }
    runoff = findSubareaRunoff(subarea, tRunoff.v)
    Voutflow += (((subarea!!.fOutlet * runoff) * area) * tStep)
    return runoff
}

// subcatch.c:985
fun getSubareaInfil(j: Int, subarea: TSubarea?, precip: Double, tStep: Double): Double {
    var infil: Double = 0.0
    infil = infil_getInfil(j, tStep, precip, subarea!!.inflow, subarea!!.depth)
    if (((!(IgnoreGwater != 0)) && (Subcatch!![j]!!.groundwater != null))) {
        infil = ((if ((((infil) <= ((Subcatch!![j]!!.groundwater!!.maxInfilVol / tStep))))) (infil) else ((Subcatch!![j]!!.groundwater!!.maxInfilVol / tStep))))
    }
    return infil
}

// subcatch.c:1013
fun findSubareaRunoff(subarea: TSubarea?, tRunoff: Double): Double {
    var xDepth: Double = (subarea!!.depth - Dstore)
    var runoff: Double = 0.0
    if ((xDepth > 1e-10)) {
        if ((subarea!!.N > 0.0)) {
            runoff = (Alpha * Glibm.pow(xDepth, MEXP))
        } else {
            runoff = (xDepth / tRunoff)
            subarea!!.depth = Dstore
        }
    } else {
        runoff = 0.0
    }
    return runoff
}

// subcatch.c:1048
fun updatePondedDepth(subarea: TSubarea?, dt: DRef?) {
    var ix: Double = subarea!!.inflow
    var dx: Double = 0.0
    var tx: Double = dt!!.get()
    if (((subarea!!.depth + (ix * tx)) <= Dstore)) {
        subarea!!.depth += (ix * tx)
    } else {
        dx = (Dstore - subarea!!.depth)
        if (((dx > 0.0) && (ix > 0.0))) {
            tx -= (dx / ix)
            subarea!!.depth = Dstore
        }
        if (((Alpha > 0.0) && (tx > 0.0))) {
            theSubarea = subarea
            val __a1 = doubleArrayOf(subarea!!.depth)
            odesolve_integrate(__a1, 1, 0.0, tx, ODETOL, tx, OdeDerivs(::getDdDt))
            subarea!!.depth = __a1[0]
        } else {
            if ((tx < 0.0)) {
                tx = 0.0
            }
            subarea!!.depth += (ix * tx)
        }
    }
    if ((subarea!!.depth < 0.0)) {
        subarea!!.depth = 0.0
    }
    dt!!.set(tx)
}

// subcatch.c:1101
fun getDdDt(t: Double, d: DoubleArray?, dddt: DoubleArray?) {
    var ix: Double = theSubarea!!.inflow
    var rx: Double = (d!![0] - Dstore)
    if ((rx < 0.0)) {
        rx = 0.0
    } else {
        rx = (Alpha * Glibm.pow(rx, MEXP))
    }
    dddt!![0] = (ix - rx)
}

// subcatch.c:1125
fun adjustSubareaParams(i: Int, j: Int) {
    var p: Int = 0
    var m: Int = 0
    var f: Double = 0.0
    if ((i == PERV)) {
        p = Subcatch!![j]!!.dStorePattern
        if (((p >= 0) && (Pattern!![p]!!.type == MONTHLY_PATTERN))) {
            m = (datetime_monthOfYear(getDateTime(OldRunoffTime)) - 1)
            f = Pattern!![p]!!.factor!![m]
            if ((f >= 0.0)) {
                Dstore *= f
            }
        }
        p = Subcatch!![j]!!.nPervPattern
        if (((p >= 0) && (Pattern!![p]!!.type == MONTHLY_PATTERN))) {
            m = (datetime_monthOfYear(getDateTime(OldRunoffTime)) - 1)
            f = Pattern!![p]!!.factor!![m]
            if ((f <= 0.0)) {
                Alpha = 0.0
            } else {
                Alpha /= f
            }
        }
    }
}

