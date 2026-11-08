// Generated from EPA SWMM 5.2.4 (snow.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_snow")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// snow.c:68
fun snow_readMeltParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(7)
    if ((ntoks < 8)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SNOWMELT, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if ((Snowmelt!![j]!!.ID == null)) {
        Snowmelt!![j]!!.ID = project_findID(SNOWMELT, tok!![0])
    }
    k = findmatch(tok!![1], SnowmeltWords)
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    n = 7
    if ((k == SNOW_REMOVAL)) {
        n = 6
    }
    if ((ntoks < (n + 2))) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 0
    f1@ while ((i < 7)) {
        x!![i] = 0.0
        i++
    }
    i = 0
    f2@ while ((i < n)) {
        if (!(getDouble(tok!![(i + 2)], DElem(x!!, i)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![(i + 2)])
        }
        i++
    }
    if ((k == SNOW_REMOVAL)) {
        x!![6] = (-1.0)
        if ((ntoks >= 9)) {
            m = project_findObject(SUBCATCH, tok!![8])
            if ((m < 0)) {
                return error_setInpError(ERR_NAME, tok!![8])
            }
            x!![6] = (m).toDouble()
        }
    }
    setMeltParams(j, k, x)
    return 0
}

// snow.c:126
fun snow_createSnowpack(j: Int, k: Int): Int {
    var snowpack: TSnowpack? = null
    snowpack = TSnowpack()
    if (!(snowpack != null)) {
        return 0
    }
    Subcatch!![j]!!.snowpack = snowpack
    snowpack!!.snowmeltIndex = k
    return 1
}

// snow.c:144
fun snow_initSnowpack(j: Int) {
    var i: Int = 0
    var k: Int = 0
    var f: Double = 0.0
    var snowDepth: Double = 0.0
    var snowpack: TSnowpack? = null
    snowpack = Subcatch!![j]!!.snowpack
    if ((snowpack == null)) {
        return
    }
    k = Subcatch!![j]!!.snowpack!!.snowmeltIndex
    f = Snowmelt!![k]!!.snn
    snowpack!!.fArea!![SNOW_PLOWABLE] = (f * Subcatch!![j]!!.fracImperv)
    snowpack!!.fArea!![SNOW_IMPERV] = (((1.0 - f)) * Subcatch!![j]!!.fracImperv)
    snowpack!!.fArea!![SNOW_PERV] = (1.0 - Subcatch!![j]!!.fracImperv)
    i = SNOW_PLOWABLE
    f1@ while ((i <= SNOW_PERV)) {
        if ((snowpack!!.fArea!![i] > 0.0)) {
            snowpack!!.wsnow!![i] = Snowmelt!![k]!!.wsnow!![i]
            snowpack!!.fw!![i] = Snowmelt!![k]!!.fwnow!![i]
        } else {
            snowpack!!.wsnow!![i] = 0.0
            snowpack!!.fw!![i] = 0.0
        }
        snowpack!!.coldc!![i] = 0.0
        snowpack!!.ati!![i] = Snowmelt!![k]!!.tbase!![i]
        snowpack!!.awe!![i] = 1.0
        snowDepth += (snowpack!!.wsnow!![i] * snowpack!!.fArea!![i])
        i++
    }
    Subcatch!![j]!!.newSnowDepth = snowDepth
}

// snow.c:193
fun snow_initSnowmelt(j: Int) {
    var i: Int = 0
    var k: Int = 0
    i = 0
    f1@ while ((i < 3)) {
        Snowmelt!![j]!!.snn = 0.0
        Snowmelt!![j]!!.si!![i] = 0.0
        Snowmelt!![j]!!.dhmin!![i] = 0.0
        Snowmelt!![j]!!.dhmax!![i] = 0.0
        Snowmelt!![j]!!.tbase!![i] = 0.0
        Snowmelt!![j]!!.fwfrac!![i] = 0.0
        Snowmelt!![j]!!.wsnow!![i] = 0.0
        Snowmelt!![j]!!.fwnow!![i] = 0.0
        Snowmelt!![j]!!.weplow = 1.0e6
        k = 0
        f2@ while ((k < 5)) {
            Snowmelt!![j]!!.sfrac!![k] = 0.0
            k++
        }
        Snowmelt!![j]!!.toSubcatch = (-1)
        i++
    }
}

// snow.c:219
fun snow_validateSnowmelt(j: Int) {
    var k: Int = 0
    var err: Int = 0
    var sum: Double = 0.0
    k = SNOW_PLOWABLE
    f1@ while ((k <= SNOW_PERV)) {
        if ((Snowmelt!![j]!!.dhmin!![k] > Snowmelt!![j]!!.dhmax!![k])) {
            err = 1
        }
        if (((Snowmelt!![j]!!.fwfrac!![k] < 0.0) || (Snowmelt!![j]!!.fwfrac!![k] > 1.0))) {
            err = 1
        }
        k++
    }
    if (((Snowmelt!![j]!!.snn < 0.0) || (Snowmelt!![j]!!.snn > 1.0))) {
        err = 1
    }
    k = 0
    f2@ while ((k < 5)) {
        sum += Snowmelt!![j]!!.sfrac!![k]
        k++
    }
    if ((sum > 1.01)) {
        err = 1
    }
    if ((err != 0)) {
        report_writeErrorMsg(ERR_SNOWPACK_PARAMS, Snowmelt!![j]!!.ID)
    }
}

// snow.c:251
fun snow_getState(i: Int, j: Int, x: DoubleArray?) {
    var snowpack: TSnowpack? = Subcatch!![i]!!.snowpack
    if ((snowpack == null)) {
        return
    }
    x!![0] = snowpack!!.wsnow!![j]
    x!![1] = snowpack!!.fw!![j]
    x!![2] = snowpack!!.coldc!![j]
    x!![3] = snowpack!!.ati!![j]
    x!![4] = snowpack!!.awe!![j]
}

// snow.c:270
fun snow_setState(i: Int, j: Int, x: DoubleArray?) {
    var snowpack: TSnowpack? = Subcatch!![i]!!.snowpack
    if ((snowpack == null)) {
        return
    }
    snowpack!!.wsnow!![j] = x!![0]
    snowpack!!.fw!![j] = x!![1]
    snowpack!!.coldc!![j] = x!![2]
    snowpack!!.ati!![j] = x!![3]
    snowpack!!.awe!![j] = x!![4]
}

// snow.c:290
fun setMeltParams(j: Int, k: Int, x: DoubleArray?) {
    var i: Int = 0
    if (((k >= SNOW_PLOWABLE) && (k <= SNOW_PERV))) {
        Snowmelt!![j]!!.dhmin!![k] = ((x!![0] * UCF(TEMPERATURE)) / UCF(RAINFALL))
        Snowmelt!![j]!!.dhmax!![k] = ((x!![1] * UCF(TEMPERATURE)) / UCF(RAINFALL))
        Snowmelt!![j]!!.tbase!![k] = x!![2]
        if ((UnitSystem == SI)) {
            Snowmelt!![j]!!.tbase!![k] = ((((9.0 / 5.0)) * Snowmelt!![j]!!.tbase!![k]) + 32.0)
        }
        Snowmelt!![j]!!.fwfrac!![k] = x!![3]
        Snowmelt!![j]!!.wsnow!![k] = (x!![4] / UCF(RAINDEPTH))
        x!![5] = ((if ((((x!![5]) <= (((x!![3] * x!![4])))))) (x!![5]) else (((x!![3] * x!![4])))))
        Snowmelt!![j]!!.fwnow!![k] = (x!![5] / UCF(RAINDEPTH))
        if ((k == SNOW_PLOWABLE)) {
            Snowmelt!![j]!!.snn = x!![6]
        } else {
            Snowmelt!![j]!!.si!![k] = (x!![6] / UCF(RAINDEPTH))
        }
    } else if ((k == SNOW_REMOVAL)) {
        Snowmelt!![j]!!.weplow = (x!![0] / UCF(RAINDEPTH))
        i = 0
        f1@ while ((i <= 4)) {
            Snowmelt!![j]!!.sfrac!![i] = x!![(i + 1)]
            i++
        }
        if ((x!![6] >= 0.0)) {
            Snowmelt!![j]!!.toSubcatch = R.D2I(((x!![6] + 0.01)))
        } else {
            Snowmelt!![j]!!.toSubcatch = (-1)
        }
    }
}

// snow.c:341
fun snow_setMeltCoeffs(j: Int, s: Double) {
    var k: Int = 0
    k = SNOW_PLOWABLE
    f1@ while ((k <= SNOW_PERV)) {
        Snowmelt!![j]!!.dhm!![k] = (0.5 * (((Snowmelt!![j]!!.dhmax!![k] * ((1.0 + s))) + (Snowmelt!![j]!!.dhmin!![k] * ((1.0 - s))))))
        k++
    }
}

// snow.c:360
fun snow_plowSnow(j: Int, tStep: Double) {
    var i: Int = 0
    var k: Int = 0
    var m: Int = 0
    val rainfall = DBox(0.0)
    val snowfall = DBox(0.0)
    var exc: Double = 0.0
    var f: Double = 0.0
    var sfracTotal: Double = 0.0
    var snowpack: TSnowpack? = null
    snowpack = Subcatch!![j]!!.snowpack
    if (!(snowpack != null)) {
        return
    }
    gage_getPrecip(Subcatch!![j]!!.gage, rainfall, snowfall)
    i = SNOW_PLOWABLE
    f1@ while ((i <= SNOW_PERV)) {
        if ((snowpack!!.fArea!![i] > 0.0)) {
            snowpack!!.wsnow!![i] += (snowfall.v * tStep)
            snowpack!!.imelt!![i] = 0.0
        }
        i++
    }
    if ((snowpack!!.fArea!![SNOW_PLOWABLE] > 0.0)) {
        k = snowpack!!.snowmeltIndex
        if ((snowpack!!.wsnow!![SNOW_PLOWABLE] >= Snowmelt!![k]!!.weplow)) {
            exc = snowpack!!.wsnow!![SNOW_PLOWABLE]
            f = (snowpack!!.fArea!![SNOW_PLOWABLE] * ((Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)))
            Snow!!.removed += ((Snowmelt!![k]!!.sfrac!![0] * exc) * f)
            sfracTotal = Snowmelt!![k]!!.sfrac!![0]
            if ((snowpack!!.fArea!![SNOW_IMPERV] > 0.0)) {
                f = (snowpack!!.fArea!![SNOW_PLOWABLE] / snowpack!!.fArea!![SNOW_IMPERV])
                snowpack!!.wsnow!![SNOW_IMPERV] += ((Snowmelt!![k]!!.sfrac!![1] * exc) * f)
                sfracTotal += Snowmelt!![k]!!.sfrac!![1]
            }
            if ((snowpack!!.fArea!![SNOW_PERV] > 0.0)) {
                f = (snowpack!!.fArea!![SNOW_PLOWABLE] / snowpack!!.fArea!![SNOW_PERV])
                snowpack!!.wsnow!![SNOW_PERV] += ((Snowmelt!![k]!!.sfrac!![2] * exc) * f)
                sfracTotal += Snowmelt!![k]!!.sfrac!![2]
            }
            snowpack!!.imelt!![SNOW_PLOWABLE] = ((Snowmelt!![k]!!.sfrac!![3] * exc) / tStep)
            sfracTotal += Snowmelt!![k]!!.sfrac!![3]
            if ((Snowmelt!![k]!!.sfrac!![4] > 0.0)) {
                m = Snowmelt!![k]!!.toSubcatch
                if ((Subcatch!![m]!!.snowpack != null)) {
                    f = Subcatch!![m]!!.snowpack!!.fArea!![SNOW_PERV]
                } else {
                    f = 0.0
                }
                if ((f > 0.0)) {
                    f = (snowpack!!.fArea!![SNOW_PLOWABLE] / f)
                    Subcatch!![m]!!.snowpack!!.wsnow!![SNOW_PERV] += ((Snowmelt!![k]!!.sfrac!![4] * exc) * f)
                    sfracTotal += Snowmelt!![k]!!.sfrac!![4]
                }
            }
            sfracTotal = ((if ((((sfracTotal) <= (1.0)))) (sfracTotal) else (1.0)))
            snowpack!!.wsnow!![SNOW_PLOWABLE] = (exc * ((1.0 - sfracTotal)))
        }
    }
}

// snow.c:458
fun snow_getSnowMelt(j: Int, rainfall: Double, snowfall: Double, tStep: Double, netPrecip: DoubleArray?): Double {
    var i: Int = 0
    var rmelt: Double = 0.0
    var smelt: Double = 0.0
    var asc: Double = 0.0
    var snowDepth: Double = 0.0
    var impervPrecip: Double = 0.0
    var snowpack: TSnowpack? = null
    snowpack = Subcatch!![j]!!.snowpack
    rmelt = getRainmelt(rainfall)
    i = SNOW_PLOWABLE
    f1@ while ((i <= SNOW_PERV)) {
        if ((snowpack!!.wsnow!![i] <= (0.001 / 12.0))) {
            asc = 0.0
            smelt = 0.0
            snowpack!!.imelt!![i] += (((snowpack!!.wsnow!![i] + snowpack!!.fw!![i])) / tStep)
            snowpack!!.wsnow!![i] = 0.0
            snowpack!!.fw!![i] = 0.0
            snowpack!!.coldc!![i] = 0.0
        } else {
            asc = getArealDepletion(snowpack, i, snowfall, tStep)
            smelt = meltSnowpack(snowpack, i, rmelt, asc, snowfall, tStep)
            smelt = routeSnowmelt(snowpack, i, smelt, asc, rainfall, tStep)
        }
        netPrecip!![i] = ((smelt + snowpack!!.imelt!![i]) + (rainfall * ((1.0 - asc))))
        snowDepth += (snowpack!!.wsnow!![i] * snowpack!!.fArea!![i])
        i++
    }
    if ((Subcatch!![j]!!.fracImperv > 0.0)) {
        impervPrecip = ((((netPrecip!![SNOW_PLOWABLE] * snowpack!!.fArea!![SNOW_PLOWABLE]) + (netPrecip!![SNOW_IMPERV] * snowpack!!.fArea!![SNOW_IMPERV]))) / Subcatch!![j]!!.fracImperv)
        netPrecip!![IMPERV0] = impervPrecip
        netPrecip!![IMPERV1] = impervPrecip
    }
    return snowDepth
}

// snow.c:532
fun snow_getSnowCover(j: Int): Double {
    var i: Int = 0
    var snowCover: Double = 0.0
    var snowpack: TSnowpack? = null
    snowpack = Subcatch!![j]!!.snowpack
    if (!(snowpack != null)) {
        return 0.0
    }
    i = SNOW_PLOWABLE
    f1@ while ((i <= SNOW_PERV)) {
        snowCover += (((snowpack!!.wsnow!![i] + snowpack!!.fw!![i])) * snowpack!!.fArea!![i])
        i++
    }
    return (snowCover * ((Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)))
}

// snow.c:555
fun getArealDepletion(snowpack: TSnowpack?, i: Int, snowfall: Double, tStep: Double): Double {
    var k: Int = 0
    var asc: Double = 0.0
    var si: Double = 0.0
    var awesi: Double = 0.0
    var awe: Double = 0.0
    var sba: Double = 0.0
    var sbws: Double = 0.0
    if ((i == SNOW_PLOWABLE)) {
        return 1.0
    }
    k = snowpack!!.snowmeltIndex
    si = Snowmelt!![k]!!.si!![i]
    if (((si == 0.0) || (snowpack!!.wsnow!![i] >= si))) {
        snowpack!!.awe!![i] = 1.0
        return 1.0
    }
    if ((snowpack!!.wsnow!![i] == 0.0)) {
        snowpack!!.awe!![i] = 1.0
        return 0.0
    }
    if ((snowfall > 0.0)) {
        awe = (((snowpack!!.wsnow!![i] - (snowfall * tStep))) / si)
        awe = ((if ((((awe) >= (0.0)))) (awe) else (0.0)))
        sba = getArealSnowCover(i, awe)
        sbws = (awe + ((((0.75 * snowfall) * tStep)) / si))
        sbws = ((if ((((sbws) <= (1.0)))) (sbws) else (1.0)))
        snowpack!!.awe!![i] = awe
        snowpack!!.sba!![i] = sba
        snowpack!!.sbws!![i] = sbws
        return 1.0
    } else {
        awe = snowpack!!.awe!![i]
        sba = snowpack!!.sba!![i]
        sbws = snowpack!!.sbws!![i]
        awesi = (snowpack!!.wsnow!![i] / si)
        if ((awesi < snowpack!!.awe!![i])) {
            snowpack!!.awe!![i] = 1.0
            asc = getArealSnowCover(i, awesi)
        } else if ((awesi >= snowpack!!.sbws!![i])) {
            asc = 1.0
        } else {
            asc = (sba + ((((1.0 - sba)) / ((sbws - awe))) * ((awesi - awe))))
        }
        return asc
    }
    throw IllegalStateException("getArealDepletion: no return")
}

// snow.c:636
fun getArealSnowCover(i: Int, awesi: Double): Double {
    var k: Int = 0
    var m: Int = 0
    var asc1: Double = 0.0
    var asc2: Double = 0.0
    if ((i == SNOW_IMPERV)) {
        k = 0
    } else if ((i == SNOW_PERV)) {
        k = 1
    } else {
        return 1.0
    }
    if ((awesi <= 0.0)) {
        return 0.0
    }
    if ((awesi >= 0.9999)) {
        return 1.0
    }
    m = R.D2I((((awesi * 10.0) + 0.00001)))
    asc1 = Snow!!.adc!![k]!![m]
    if ((m >= 9)) {
        asc2 = 1.0
    } else {
        asc2 = Snow!!.adc!![k]!![(m + 1)]
    }
    return (asc1 + ((((asc2 - asc1)) / 0.1) * ((awesi - (0.1 * ((m).toFloat()).toDouble())))))
}

// snow.c:674
fun meltSnowpack(snowpack: TSnowpack?, i: Int, rmelt: Double, asc: Double, snowfall: Double, tStep: Double): Double {
    var k: Int = 0
    var smelt: Double = 0.0
    var ccFactor: Double = 0.0
    k = snowpack!!.snowmeltIndex
    if ((rmelt > 0.0)) {
        smelt = rmelt
    } else if ((Temp!!.ta >= Snowmelt!![k]!!.tbase!![i])) {
        smelt = (Snowmelt!![k]!!.dhm!![i] * ((Temp!!.ta - Snowmelt!![k]!!.tbase!![i])))
    } else {
        updateColdContent(snowpack, i, asc, snowfall, tStep)
        return 0.0
    }
    smelt *= asc
    ccFactor = ((tStep * Snow!!.rnm) * asc)
    smelt = reduceColdContent(snowpack, i, smelt, ccFactor)
    snowpack!!.ati!![i] = Snowmelt!![k]!!.tbase!![i]
    return smelt
}

// snow.c:720
fun getRainmelt(__rainfall: Double): Double {
    var rainfall: Double = __rainfall
    var uadj: Double = 0.0
    var t1: Double = 0.0
    var t2: Double = 0.0
    var t3: Double = 0.0
    var smelt: Double = 0.0
    rainfall = (rainfall * 43200.0)
    if ((rainfall > 0.02)) {
        uadj = (0.006 * Wind!!.ws)
        t1 = (Temp!!.ta - 32.0)
        t2 = ((7.5 * Temp!!.gamma) * uadj)
        t3 = ((8.5 * uadj) * ((Temp!!.ea - 0.18)))
        smelt = ((t1 * (((0.001167 + t2) + (0.007 * rainfall)))) + t3)
        return (smelt / 43200.0)
    } else {
        return 0.0
    }
    throw IllegalStateException("getRainmelt: no return")
}

// snow.c:746
fun updateColdContent(snowpack: TSnowpack?, i: Int, asc: Double, snowfall: Double, tStep: Double) {
    var k: Int = 0
    var ati: Double = 0.0
    var cc: Double = 0.0
    var ccMax: Double = 0.0
    var tipm: Double = 0.0
    ati = snowpack!!.ati!![i]
    cc = snowpack!!.coldc!![i]
    if (((snowfall * 43200.0) > 0.02)) {
        ati = Temp!!.ta
    } else {
        tipm = (1.0 - Glibm.pow((1.0 - Snow!!.tipm), (tStep / ((6.0 * 3600.0)))))
        ati += (tipm * ((Temp!!.ta - ati)))
    }
    k = snowpack!!.snowmeltIndex
    ati = ((if ((((ati) <= (Snowmelt!![k]!!.tbase!![i])))) (ati) else (Snowmelt!![k]!!.tbase!![i])))
    cc += ((((Snow!!.rnm * Snowmelt!![k]!!.dhm!![i]) * ((ati - Temp!!.ta))) * tStep) * asc)
    cc = ((if ((((cc) >= (0.0)))) (cc) else (0.0)))
    ccMax = (((snowpack!!.wsnow!![i] * 0.007) / 12.0) * ((Snowmelt!![k]!!.tbase!![i] - ati)))
    cc = ((if ((((cc) <= (ccMax)))) (cc) else (ccMax)))
    snowpack!!.coldc!![i] = cc
    snowpack!!.ati!![i] = ati
}

// snow.c:799
fun reduceColdContent(snowpack: TSnowpack?, i: Int, __smelt: Double, ccFactor: Double): Double {
    var smelt: Double = __smelt
    var cc: Double = 0.0
    cc = snowpack!!.coldc!![i]
    if (((smelt * ccFactor) > cc)) {
        smelt -= (cc / ccFactor)
        cc = 0.0
    } else {
        cc -= (smelt * ccFactor)
        smelt = 0.0
    }
    snowpack!!.coldc!![i] = cc
    return smelt
}

// snow.c:828
fun routeSnowmelt(snowpack: TSnowpack?, i: Int, smelt: Double, asc: Double, rainfall: Double, tStep: Double): Double {
    var k: Int = 0
    var vmelt: Double = 0.0
    k = snowpack!!.snowmeltIndex
    vmelt = (smelt * tStep)
    vmelt = ((if ((((vmelt) <= (snowpack!!.wsnow!![i])))) (vmelt) else (snowpack!!.wsnow!![i])))
    snowpack!!.wsnow!![i] -= vmelt
    snowpack!!.fw!![i] += (vmelt + ((rainfall * tStep) * asc))
    vmelt = (snowpack!!.fw!![i] - (Snowmelt!![k]!!.fwfrac!![i] * snowpack!!.wsnow!![i]))
    vmelt = ((if ((((vmelt) >= (0.0)))) (vmelt) else (0.0)))
    snowpack!!.fw!![i] -= vmelt
    return (vmelt / tStep)
}

