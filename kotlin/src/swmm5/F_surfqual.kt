// Generated from EPA SWMM 5.2.4 (surfqual.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_surfqual")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// surfqual.c:65
fun surfqual_initState(j: Int) {
    var p: Int = 0
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        Subcatch!![j]!!.oldQual!![p] = 0.0
        Subcatch!![j]!!.newQual!![p] = 0.0
        Subcatch!![j]!!.pondedQual!![p] = 0.0
        p++
    }
    landuse_getInitBuildup(Subcatch!![j]!!.landFactor, Subcatch!![j]!!.initBuildup, Subcatch!![j]!!.area, Subcatch!![j]!!.curbLength)
}

// surfqual.c:89
fun surfqual_getBuildup(j: Int, tStep: Double) {
    var i: Int = 0
    var p: Int = 0
    var f: Double = 0.0
    var area: Double = 0.0
    var curb: Double = 0.0
    var oldBuildup: Double = 0.0
    var newBuildup: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![LANDUSE])) {
        f = Subcatch!![j]!!.landFactor!![i]!!.fraction
        if ((f == 0.0)) {
            i++
            continue@f1
        }
        area = ((f * Subcatch!![j]!!.area) * UCF(LANDAREA))
        curb = (f * Subcatch!![j]!!.curbLength)
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            if (((Pollut!![p]!!.snowOnly != 0) && (Subcatch!![j]!!.newSnowDepth < (0.001 / 12.0)))) {
                p++
                continue@f2
            }
            oldBuildup = Subcatch!![j]!!.landFactor!![i]!!.buildup!![p]
            newBuildup = landuse_getBuildup(i, p, area, curb, oldBuildup, tStep)
            newBuildup = ((if ((((newBuildup) >= (oldBuildup)))) (newBuildup) else (oldBuildup)))
            Subcatch!![j]!!.landFactor!![i]!!.buildup!![p] = newBuildup
            massbal_updateLoadingTotals(BUILDUP_LOAD, p, ((newBuildup - oldBuildup)))
            p++
        }
        i++
    }
}

// surfqual.c:137
fun surfqual_sweepBuildup(j: Int, aDate: Double) {
    var i: Int = 0
    var p: Int = 0
    var oldBuildup: Double = 0.0
    var newBuildup: Double = 0.0
    if (((Subcatch!![j]!!.snowpack != null) && (Subcatch!![j]!!.snowpack!!.wsnow!![IMPERV0] > 0.004167))) {
        return
    }
    i = 0
    f1@ while ((i < Nobjects!![LANDUSE])) {
        if ((Subcatch!![j]!!.landFactor!![i]!!.fraction == 0.0)) {
            i++
            continue@f1
        }
        if ((Landuse!![i]!!.sweepInterval == 0.0)) {
            i++
            continue@f1
        }
        if (((aDate - Subcatch!![j]!!.landFactor!![i]!!.lastSwept) >= Landuse!![i]!!.sweepInterval)) {
            Subcatch!![j]!!.landFactor!![i]!!.lastSwept = aDate
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                oldBuildup = Subcatch!![j]!!.landFactor!![i]!!.buildup!![p]
                newBuildup = (oldBuildup * ((1.0 - (Landuse!![i]!!.sweepRemoval * Landuse!![i]!!.washoffFunc!![p]!!.sweepEffic))))
                newBuildup = ((if ((((oldBuildup) <= (newBuildup)))) (oldBuildup) else (newBuildup)))
                newBuildup = ((if ((((0.0) >= (newBuildup)))) (0.0) else (newBuildup)))
                Subcatch!![j]!!.landFactor!![i]!!.buildup!![p] = newBuildup
                massbal_updateLoadingTotals(SWEEPING_LOAD, p, (oldBuildup - newBuildup))
                p++
            }
        }
        i++
    }
}

// surfqual.c:192
fun surfqual_getWashoff(j: Int, runoff: Double, tStep: Double) {
    var p: Int = 0
    var hasOutflow: Int = 0
    var cOut: Double = 0.0
    var massLoad: Double = 0.0
    var vLidRain: Double = 0.0
    var vLidRunon: Double = 0.0
    var vSurfOut: Double = 0.0
    var vOut1: Double = 0.0
    var vOut2: Double = 0.0
    var area: Double = 0.0
    area = Subcatch!![j]!!.area
    if (((Nobjects!![POLLUT] == 0) || (area == 0.0))) {
        return
    }
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        OutflowLoad!![p] = 0.0
        p++
    }
    findWashoffLoads(j, runoff)
    findPondedLoads(j, tStep)
    findLidLoads(j, tStep)
    vLidRain = ((Subcatch!![j]!!.rainfall * Subcatch!![j]!!.lidArea) * tStep)
    vLidRunon = 0.0
    if ((area == Subcatch!![j]!!.lidArea)) {
        vLidRunon = ((Subcatch!![j]!!.runon * area) * tStep)
    }
    vOut1 = ((Voutflow + vLidRain) + vLidRunon)
    vSurfOut = (Subcatch!![j]!!.newRunoff * tStep)
    vOut2 = (vSurfOut + VlidDrain)
    hasOutflow = (if (((vOut2 > ((2.31481e-08 * area) * tStep)))) 1 else 0)
    p = 0
    f2@ while ((p < Nobjects!![POLLUT])) {
        cOut = 0.0
        if (((vOut1 > 0.0) && (hasOutflow != 0))) {
            cOut = (OutflowLoad!![p] / vOut1)
        }
        if ((Subcatch!![j]!!.lidArea > 0.0)) {
            massLoad = ((cOut * ((vOut1 - vOut2))) * Pollut!![p]!!.mcf)
            if ((massLoad > 0.0)) {
                massbal_updateLoadingTotals(BMP_REMOVAL_LOAD, p, massLoad)
            }
        }
        massLoad = ((cOut * vOut2) * Pollut!![p]!!.mcf)
        Subcatch!![j]!!.totalLoad!![p] += massLoad
        if ((((Subcatch!![j]!!.outNode >= 0) || (Subcatch!![j]!!.outSubcatch == j)))) {
            massLoad = ((cOut * vSurfOut) * Pollut!![p]!!.mcf)
            massbal_updateLoadingTotals(RUNOFF_LOAD, p, massLoad)
        }
        Subcatch!![j]!!.newQual!![p] = (cOut / 28.317)
        p++
    }
    if ((Subcatch!![j]!!.lidArea > 0.0)) {
        lid_addDrainLoads(j, Subcatch!![j]!!.newQual, tStep)
    }
}

// surfqual.c:298
fun surfqual_getWtdWashoff(j: Int, p: Int, f: Double): Double {
    return (((((1.0 - f)) * Subcatch!![j]!!.oldRunoff) * Subcatch!![j]!!.oldQual!![p]) + ((f * Subcatch!![j]!!.newRunoff) * Subcatch!![j]!!.newQual!![p]))
}

// surfqual.c:313
fun findPondedLoads(j: Int, tStep: Double) {
    var p: Int = 0
    var cPonded: Double = 0.0
    var wPonded: Double = 0.0
    var bmpRemoval: Double = 0.0
    var vRain: Double = 0.0
    var wRain: Double = 0.0
    var wRunon: Double = 0.0
    var wInfil: Double = 0.0
    var wOutflow: Double = 0.0
    var fullArea: Double = 0.0
    var nonLidArea: Double = 0.0
    if ((Subcatch!![j]!!.area == Subcatch!![j]!!.lidArea)) {
        return
    }
    fullArea = Subcatch!![j]!!.area
    nonLidArea = (fullArea - Subcatch!![j]!!.lidArea)
    vRain = ((Subcatch!![j]!!.rainfall * nonLidArea) * tStep)
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        wRain = ((Pollut!![p]!!.pptConcen * 28.317) * vRain)
        massbal_updateLoadingTotals(DEPOSITION_LOAD, p, (wRain * Pollut!![p]!!.mcf))
        if ((Vinflow == 0.0)) {
            massbal_updateLoadingTotals(FINAL_LOAD, p, (Subcatch!![j]!!.pondedQual!![p] * Pollut!![p]!!.mcf))
            Subcatch!![j]!!.pondedQual!![p] = 0.0
        } else {
            wRunon = (Subcatch!![j]!!.newQual!![p] * tStep)
            wPonded = ((Subcatch!![j]!!.pondedQual!![p] + wRain) + wRunon)
            cPonded = (wPonded / Vinflow)
            wInfil = (cPonded * Vinfil)
            wInfil = ((if ((((wInfil) <= (wPonded)))) (wInfil) else (wPonded)))
            massbal_updateLoadingTotals(INFIL_LOAD, p, (wInfil * Pollut!![p]!!.mcf))
            wPonded -= wInfil
            wOutflow = (cPonded * Voutflow)
            wOutflow = ((if ((((wOutflow) <= (wPonded)))) (wOutflow) else (wPonded)))
            wPonded -= wOutflow
            bmpRemoval = (landuse_getAvgBmpEffic(j, p) * wOutflow)
            massbal_updateLoadingTotals(BMP_REMOVAL_LOAD, p, (bmpRemoval * Pollut!![p]!!.mcf))
            wOutflow -= bmpRemoval
            Subcatch!![j]!!.pondedQual!![p] = ((cPonded * subcatch_getDepth(j)) * nonLidArea)
            OutflowLoad!![p] += wOutflow
        }
        p++
    }
}

// surfqual.c:390
fun findWashoffLoads(j: Int, runoff: Double) {
    var i: Int = 0
    var p: Int = 0
    var k: Int = 0
    var w: Double = 0.0
    var area: Double = Subcatch!![j]!!.area
    if ((runoff < 2.31481e-08)) {
        return
    }
    i = 0
    f1@ while ((i < Nobjects!![LANDUSE])) {
        if ((Subcatch!![j]!!.landFactor!![i]!!.fraction > 0.0)) {
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                OutflowLoad!![p] += (landuse_getWashoffLoad(i, p, area, Subcatch!![j]!!.landFactor, runoff, Voutflow))
                p++
            }
        }
        i++
    }
    p = 0
    f3@ while ((p < Nobjects!![POLLUT])) {
        k = Pollut!![p]!!.coPollut
        if ((k >= 0)) {
            w = (Pollut!![p]!!.coFraction * OutflowLoad!![k])
            massbal_updateLoadingTotals(BUILDUP_LOAD, p, (w * Pollut!![p]!!.mcf))
            OutflowLoad!![p] += w
        }
        p++
    }
}

// surfqual.c:443
fun findLidLoads(j: Int, tStep: Double) {
    var p: Int = 0
    var useRunon: Int = 0
    var lidArea: Double = 0.0
    var vLidRain: Double = 0.0
    var wLidRain: Double = 0.0
    var wLidRunon: Double = 0.0
    lidArea = Subcatch!![j]!!.lidArea
    if ((lidArea == 0.0)) {
        return
    }
    vLidRain = ((Subcatch!![j]!!.rainfall * lidArea) * tStep)
    useRunon = (if (((lidArea == Subcatch!![j]!!.area))) 1 else 0)
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        wLidRain = ((Pollut!![p]!!.pptConcen * vLidRain) * 28.317)
        massbal_updateLoadingTotals(DEPOSITION_LOAD, p, (wLidRain * Pollut!![p]!!.mcf))
        if ((useRunon != 0)) {
            wLidRunon = (Subcatch!![j]!!.newQual!![p] * tStep)
        } else {
            wLidRunon = 0.0
        }
        OutflowLoad!![p] += (wLidRain + wLidRunon)
        p++
    }
}

