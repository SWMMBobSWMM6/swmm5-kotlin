// Generated from EPA SWMM 5.2.4 (lidproc.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_lidproc")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var theLidUnit: TLidUnit? = null
@JvmField var theLidProc: TLidProc? = null
@JvmField var lidproc_Tstep: Double = 0.0
@JvmField var lidproc_EvapRate: Double = 0.0
@JvmField var lidproc_MaxNativeInfil: Double = 0.0
@JvmField var SurfaceInflow: Double = 0.0
@JvmField var SurfaceInfil: Double = 0.0
@JvmField var SurfaceEvap: Double = 0.0
@JvmField var SurfaceOutflow: Double = 0.0
@JvmField var SurfaceVolume: Double = 0.0
@JvmField var PaveEvap: Double = 0.0
@JvmField var PavePerc: Double = 0.0
@JvmField var PaveVolume: Double = 0.0
@JvmField var SoilEvap: Double = 0.0
@JvmField var SoilPerc: Double = 0.0
@JvmField var SoilVolume: Double = 0.0
@JvmField var StorageInflow: Double = 0.0
@JvmField var StorageExfil: Double = 0.0
@JvmField var StorageEvap: Double = 0.0
@JvmField var StorageDrain: Double = 0.0
@JvmField var StorageVolume: Double = 0.0
@JvmField var Xold: DoubleArray? = DoubleArray(4)

// lidproc.c:178
fun lidproc_initWaterBalance(lidUnit: TLidUnit?, initVol: Double) {
    lidUnit!!.waterBalance!!.inflow = 0.0
    lidUnit!!.waterBalance!!.evap = 0.0
    lidUnit!!.waterBalance!!.infil = 0.0
    lidUnit!!.waterBalance!!.surfFlow = 0.0
    lidUnit!!.waterBalance!!.drainFlow = 0.0
    lidUnit!!.waterBalance!!.initVol = initVol
    lidUnit!!.waterBalance!!.finalVol = initVol
}

// lidproc.c:197
fun lidproc_getOutflow(lidUnit: TLidUnit?, lidProc: TLidProc?, inflow: Double, evap: Double, infil: Double, maxInfil: Double, tStep: Double, lidEvap: DRef?, lidInfil: DRef?, lidDrain: DRef?): Double {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(4)
    var xOld: DoubleArray? = DoubleArray(4)
    var xPrev: DoubleArray? = DoubleArray(4)
    var xMin: DoubleArray? = DoubleArray(4)
    var xMax: DoubleArray? = DoubleArray(4)
    var fOld: DoubleArray? = DoubleArray(4)
    var f: DoubleArray? = DoubleArray(4)
    var xTol: DoubleArray? = doubleArrayOf(0.00328, 0.00328, 0.00328, 0.00328)
    var omega: Double = 0.0
    var fluxRates: FP5? = (null)
    theLidProc = lidProc
    theLidUnit = lidUnit
    lidproc_EvapRate = evap
    lidproc_MaxNativeInfil = maxInfil
    lidproc_Tstep = tStep
    x!![SURF] = theLidUnit!!.surfaceDepth
    x!![SOIL] = theLidUnit!!.soilMoisture
    x!![STOR] = theLidUnit!!.storageDepth
    x!![PAVE] = theLidUnit!!.paveDepth
    SurfaceVolume = 0.0
    PaveVolume = 0.0
    SoilVolume = 0.0
    StorageVolume = 0.0
    SurfaceInflow = inflow
    SurfaceInfil = 0.0
    SurfaceEvap = 0.0
    SurfaceOutflow = 0.0
    PaveEvap = 0.0
    PavePerc = 0.0
    SoilEvap = 0.0
    SoilPerc = 0.0
    StorageInflow = 0.0
    StorageExfil = 0.0
    StorageEvap = 0.0
    StorageDrain = 0.0
    i = 0
    f1@ while ((i < 4)) {
        f!![i] = 0.0
        fOld!![i] = theLidUnit!!.oldFluxRates!![i]
        xMin!![i] = 0.0
        xMax!![i] = 10000000000.0
        Xold!![i] = x!![i]
        i++
    }
    if ((theLidProc!!.lidType == POROUS_PAVEMENT)) {
        SurfaceInfil = 0.0
    } else if ((theLidUnit!!.soilInfil!!.Ks > 0.0)) {
        SurfaceInfil = grnampt_getInfil(theLidUnit!!.soilInfil, lidproc_Tstep, SurfaceInflow, theLidUnit!!.surfaceDepth, MOD_GREEN_AMPT)
    } else {
        SurfaceInfil = infil
    }
    if ((theLidProc!!.soil!!.thickness > 0.0)) {
        xMin!![SOIL] = theLidProc!!.soil!!.wiltPoint
        xMax!![SOIL] = theLidProc!!.soil!!.porosity
    }
    if ((theLidProc!!.pavement!!.thickness > 0.0)) {
        xMax!![PAVE] = theLidProc!!.pavement!!.thickness
    }
    if ((theLidProc!!.storage!!.thickness > 0.0)) {
        xMax!![STOR] = theLidProc!!.storage!!.thickness
    }
    if ((theLidProc!!.lidType == GREEN_ROOF)) {
        xMax!![STOR] = theLidProc!!.drainMat!!.thickness
    }
    when (theLidProc!!.lidType) {
        BIO_CELL, RAIN_GARDEN -> {
            fluxRates = FP5(::biocellFluxRates)
        }
        GREEN_ROOF -> {
            fluxRates = FP5(::greenRoofFluxRates)
        }
        INFIL_TRENCH -> {
            fluxRates = FP5(::trenchFluxRates)
        }
        POROUS_PAVEMENT -> {
            fluxRates = FP5(::pavementFluxRates)
        }
        RAIN_BARREL -> {
            fluxRates = FP5(::barrelFluxRates)
        }
        ROOF_DISCON -> {
            fluxRates = FP5(::roofFluxRates)
        }
        VEG_SWALE -> {
            fluxRates = FP5(::swaleFluxRates)
            omega = 0.5
        }
        else -> {
            return 0.0
        }
    }
    i = modpuls_solve(4, x, xOld, xPrev, xMin, xMax, xTol, fOld, f, tStep, omega, fluxRates)
    if (((theLidProc!!.surface!!.canOverflow != 0) || (theLidUnit!!.fullWidth == 0.0))) {
        SurfaceOutflow += (getSurfaceOverflowRate(DElem(x!!, SURF)))
    }
    theLidUnit!!.surfaceDepth = x!![SURF]
    theLidUnit!!.paveDepth = x!![PAVE]
    theLidUnit!!.soilMoisture = x!![SOIL]
    theLidUnit!!.storageDepth = x!![STOR]
    i = 0
    f3@ while ((i < 4)) {
        theLidUnit!!.oldFluxRates!![i] = f!![i]
        i++
    }
    lidEvap!!.set((((SurfaceEvap + PaveEvap) + SoilEvap) + StorageEvap))
    lidInfil!!.set(StorageExfil)
    lidDrain!!.set(StorageDrain)
    return SurfaceOutflow
}

// lidproc.c:360
fun lidproc_saveResults(lidUnit: TLidUnit?, ucfRainfall: Double, ucfRainDepth: Double) {
    var ucf: Double = 0.0
    var totalEvap: Double = 0.0
    var totalVolume: Double = 0.0
    var rptVars: DoubleArray? = DoubleArray(12)
    var isDry: Int = 0
    var timeStamp: CPtr? = R.buf(22)
    var elapsedHrs: Double = 0.0
    totalEvap = (((SurfaceEvap + PaveEvap) + SoilEvap) + StorageEvap)
    totalVolume = (((SurfaceVolume + PaveVolume) + SoilVolume) + StorageVolume)
    updateWaterBalance(theLidUnit, SurfaceInflow, totalEvap, StorageExfil, SurfaceOutflow, StorageDrain, totalVolume)
    if ((((((SurfaceInflow < 2.3e-08) && (SurfaceOutflow < 2.3e-08)) && (StorageDrain < 2.3e-08)) && (StorageExfil < 2.3e-08)) && (totalEvap < 2.3e-08))) {
        isDry = 1
    }
    if (!(isDry != 0)) {
        HasWetLids = 1
    }
    if ((lidUnit!!.rptFile != null)) {
        ucf = ucfRainfall
        rptVars!![SURF_INFLOW] = (SurfaceInflow * ucf)
        rptVars!![TOTAL_EVAP] = (totalEvap * ucf)
        rptVars!![SURF_INFIL] = (SurfaceInfil * ucf)
        rptVars!![PAVE_PERC] = (PavePerc * ucf)
        rptVars!![SOIL_PERC] = (SoilPerc * ucf)
        rptVars!![STOR_EXFIL] = (StorageExfil * ucf)
        rptVars!![SURF_OUTFLOW] = (SurfaceOutflow * ucf)
        rptVars!![STOR_DRAIN] = (StorageDrain * ucf)
        ucf = ucfRainDepth
        rptVars!![SURF_DEPTH] = (theLidUnit!!.surfaceDepth * ucf)
        rptVars!![PAVE_DEPTH] = (theLidUnit!!.paveDepth * ucf)
        rptVars!![SOIL_MOIST] = theLidUnit!!.soilMoisture
        rptVars!![STOR_DEPTH] = (theLidUnit!!.storageDepth * ucf)
        if (((!(isDry != 0)) && (theLidUnit!!.rptFile!!.wasDry > 1))) {
            R.fprintf(theLidUnit!!.rptFile!!.file, __L169, theLidUnit!!.rptFile!!.results)
        }
        elapsedHrs = ((NewRunoffTime / 1000.0) / 3600.0)
        datetime_getTimeStamp(1, getDateTime(NewRunoffTime), 21, timeStamp)
        R.snprintf(theLidUnit!!.rptFile!!.results, (256L).toInt(), __L304, timeStamp, elapsedHrs, rptVars!![0], rptVars!![1], rptVars!![2], rptVars!![3], rptVars!![4], rptVars!![5], rptVars!![6], rptVars!![7], rptVars!![8], rptVars!![9], rptVars!![10], rptVars!![11])
        if ((isDry != 0)) {
            if ((theLidUnit!!.rptFile!!.wasDry == 0)) {
                R.fprintf(theLidUnit!!.rptFile!!.file, __L169, theLidUnit!!.rptFile!!.results)
            }
            theLidUnit!!.rptFile!!.wasDry++
        } else {
            R.fprintf(theLidUnit!!.rptFile!!.file, __L169, theLidUnit!!.rptFile!!.results)
            theLidUnit!!.rptFile!!.wasDry = 0
        }
    }
}

// lidproc.c:469
fun roofFluxRates(x: DoubleArray?, f: DoubleArray?) {
    val surfaceDepth = DBox(x!![SURF])
    lidproc_getEvapRates(surfaceDepth.v, 0.0, 0.0, 0.0, 1.0)
    SurfaceVolume = surfaceDepth.v
    SurfaceInfil = 0.0
    if ((theLidProc!!.surface!!.alpha > 0.0)) {
        SurfaceOutflow = getSurfaceOutflowRate(surfaceDepth.v)
    } else {
        getSurfaceOverflowRate(surfaceDepth)
    }
    StorageDrain = ((if (((((theLidProc!!.drain!!.coeff / UCF(RAINFALL))) <= (SurfaceOutflow)))) ((theLidProc!!.drain!!.coeff / UCF(RAINFALL))) else (SurfaceOutflow)))
    SurfaceOutflow -= StorageDrain
    f!![SURF] = ((((SurfaceInflow - SurfaceEvap) - StorageDrain) - SurfaceOutflow))
}

// lidproc.c:491
fun greenRoofFluxRates(x: DoubleArray?, f: DoubleArray?) {
    var surfaceDepth: Double = 0.0
    var soilTheta: Double = 0.0
    var storageDepth: Double = 0.0
    var availVolume: Double = 0.0
    var maxRate: Double = 0.0
    var soilThickness: Double = theLidProc!!.soil!!.thickness
    var storageThickness: Double = theLidProc!!.storage!!.thickness
    var soilPorosity: Double = theLidProc!!.soil!!.porosity
    var storageVoidFrac: Double = theLidProc!!.storage!!.voidFrac
    var soilFieldCap: Double = theLidProc!!.soil!!.fieldCap
    var soilWiltPoint: Double = theLidProc!!.soil!!.wiltPoint
    surfaceDepth = x!![SURF]
    soilTheta = x!![SOIL]
    storageDepth = x!![STOR]
    SurfaceVolume = (surfaceDepth * theLidProc!!.surface!!.voidFrac)
    SoilVolume = (soilTheta * soilThickness)
    StorageVolume = (storageDepth * storageVoidFrac)
    availVolume = (SoilVolume - (soilWiltPoint * soilThickness))
    lidproc_getEvapRates(SurfaceVolume, 0.0, availVolume, StorageVolume, 1.0)
    if ((soilTheta >= soilPorosity)) {
        StorageEvap = 0.0
    }
    SoilPerc = getSoilPercRate(soilTheta)
    availVolume = (((soilTheta - soilFieldCap)) * soilThickness)
    maxRate = ((((if ((((availVolume) >= (0.0)))) (availVolume) else (0.0))) / lidproc_Tstep) - SoilEvap)
    SoilPerc = ((if ((((SoilPerc) <= (maxRate)))) (SoilPerc) else (maxRate)))
    SoilPerc = ((if ((((SoilPerc) >= (0.0)))) (SoilPerc) else (0.0)))
    StorageExfil = 0.0
    StorageDrain = getDrainMatOutflow(storageDepth)
    if (((soilTheta >= soilPorosity) && (storageDepth >= storageThickness))) {
        maxRate = ((if ((((SoilPerc) <= (StorageDrain)))) (SoilPerc) else (StorageDrain)))
        SoilPerc = maxRate
        StorageDrain = maxRate
        SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
    } else {
        maxRate = (((storageDepth * storageVoidFrac) / lidproc_Tstep) - StorageEvap)
        if ((storageDepth >= storageThickness)) {
            maxRate += SoilPerc
        }
        maxRate = ((if ((((maxRate) >= (0.0)))) (maxRate) else (0.0)))
        StorageDrain = ((if ((((StorageDrain) <= (maxRate)))) (StorageDrain) else (maxRate)))
        maxRate = ((((((storageThickness - storageDepth)) * storageVoidFrac) / lidproc_Tstep) + StorageDrain) + StorageEvap)
        SoilPerc = ((if ((((SoilPerc) <= (maxRate)))) (SoilPerc) else (maxRate)))
        maxRate = ((((((soilPorosity - soilTheta)) * soilThickness) / lidproc_Tstep) + SoilPerc) + SoilEvap)
        SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
    }
    SurfaceOutflow = getSurfaceOutflowRate(surfaceDepth)
    f!![SURF] = (((((SurfaceInflow - SurfaceEvap) - SurfaceInfil) - SurfaceOutflow)) / theLidProc!!.surface!!.voidFrac)
    f!![SOIL] = ((((SurfaceInfil - SoilEvap) - SoilPerc)) / theLidProc!!.soil!!.thickness)
    f!![STOR] = ((((SoilPerc - StorageEvap) - StorageDrain)) / theLidProc!!.storage!!.voidFrac)
}

// lidproc.c:589
fun biocellFluxRates(x: DoubleArray?, f: DoubleArray?) {
    var surfaceDepth: Double = 0.0
    var soilTheta: Double = 0.0
    var storageDepth: Double = 0.0
    var availVolume: Double = 0.0
    var maxRate: Double = 0.0
    var soilThickness: Double = theLidProc!!.soil!!.thickness
    var soilPorosity: Double = theLidProc!!.soil!!.porosity
    var soilFieldCap: Double = theLidProc!!.soil!!.fieldCap
    var soilWiltPoint: Double = theLidProc!!.soil!!.wiltPoint
    var storageThickness: Double = theLidProc!!.storage!!.thickness
    var storageVoidFrac: Double = theLidProc!!.storage!!.voidFrac
    surfaceDepth = x!![SURF]
    soilTheta = x!![SOIL]
    storageDepth = x!![STOR]
    SurfaceVolume = (surfaceDepth * theLidProc!!.surface!!.voidFrac)
    SoilVolume = (soilTheta * soilThickness)
    StorageVolume = (storageDepth * storageVoidFrac)
    availVolume = (SoilVolume - (soilWiltPoint * soilThickness))
    lidproc_getEvapRates(SurfaceVolume, 0.0, availVolume, StorageVolume, 1.0)
    if ((soilTheta >= soilPorosity)) {
        StorageEvap = 0.0
    }
    SoilPerc = getSoilPercRate(soilTheta)
    availVolume = (((soilTheta - soilFieldCap)) * soilThickness)
    maxRate = ((((if ((((availVolume) >= (0.0)))) (availVolume) else (0.0))) / lidproc_Tstep) - SoilEvap)
    SoilPerc = ((if ((((SoilPerc) <= (maxRate)))) (SoilPerc) else (maxRate)))
    SoilPerc = ((if ((((SoilPerc) >= (0.0)))) (SoilPerc) else (0.0)))
    StorageExfil = getStorageExfilRate()
    StorageDrain = 0.0
    if ((theLidProc!!.drain!!.coeff > 0.0)) {
        StorageDrain = getStorageDrainRate(storageDepth, soilTheta, 0.0, surfaceDepth)
    }
    if ((storageThickness == 0.0)) {
        StorageEvap = 0.0
        maxRate = ((if ((((SoilPerc) <= (StorageExfil)))) (SoilPerc) else (StorageExfil)))
        SoilPerc = maxRate
        StorageExfil = maxRate
        maxRate = ((((((soilPorosity - soilTheta)) * soilThickness) / lidproc_Tstep) + SoilPerc) + SoilEvap)
        SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
    } else {
        if (((soilTheta >= soilPorosity) && (storageDepth >= storageThickness))) {
            maxRate = (StorageExfil + StorageDrain)
            if ((SoilPerc < maxRate)) {
                maxRate = SoilPerc
                if ((maxRate > StorageExfil)) {
                    StorageDrain = (maxRate - StorageExfil)
                } else {
                    StorageExfil = maxRate
                    StorageDrain = 0.0
                }
            } else {
                SoilPerc = maxRate
            }
            SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
        } else {
            maxRate = ((SoilPerc - StorageEvap) + ((storageDepth * storageVoidFrac) / lidproc_Tstep))
            StorageExfil = ((if ((((StorageExfil) <= (maxRate)))) (StorageExfil) else (maxRate)))
            StorageExfil = ((if ((((StorageExfil) >= (0.0)))) (StorageExfil) else (0.0)))
            if ((StorageDrain > 0.0)) {
                maxRate = ((-StorageExfil) - StorageEvap)
                if ((storageDepth >= storageThickness)) {
                    maxRate += SoilPerc
                }
                if ((theLidProc!!.drain!!.offset <= storageDepth)) {
                    maxRate += ((((storageDepth - theLidProc!!.drain!!.offset)) * storageVoidFrac) / lidproc_Tstep)
                }
                maxRate = ((if ((((maxRate) >= (0.0)))) (maxRate) else (0.0)))
                StorageDrain = ((if ((((StorageDrain) <= (maxRate)))) (StorageDrain) else (maxRate)))
            }
            maxRate = (((StorageExfil + StorageDrain) + StorageEvap) + ((((storageThickness - storageDepth)) * storageVoidFrac) / lidproc_Tstep))
            SoilPerc = ((if ((((SoilPerc) <= (maxRate)))) (SoilPerc) else (maxRate)))
            maxRate = ((((((soilPorosity - soilTheta)) * soilThickness) / lidproc_Tstep) + SoilPerc) + SoilEvap)
            SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
        }
    }
    SurfaceOutflow = getSurfaceOutflowRate(surfaceDepth)
    f!![SURF] = (((((SurfaceInflow - SurfaceEvap) - SurfaceInfil) - SurfaceOutflow)) / theLidProc!!.surface!!.voidFrac)
    f!![SOIL] = ((((SurfaceInfil - SoilEvap) - SoilPerc)) / theLidProc!!.soil!!.thickness)
    if ((storageThickness == 0.0)) {
        f!![STOR] = 0.0
    } else {
        f!![STOR] = (((((SoilPerc - StorageEvap) - StorageExfil) - StorageDrain)) / theLidProc!!.storage!!.voidFrac)
    }
}

// lidproc.c:735
fun trenchFluxRates(x: DoubleArray?, f: DoubleArray?) {
    var surfaceDepth: Double = 0.0
    var storageDepth: Double = 0.0
    var availVolume: Double = 0.0
    var maxRate: Double = 0.0
    var storageThickness: Double = theLidProc!!.storage!!.thickness
    var storageVoidFrac: Double = theLidProc!!.storage!!.voidFrac
    surfaceDepth = x!![SURF]
    storageDepth = x!![STOR]
    SurfaceVolume = (surfaceDepth * theLidProc!!.surface!!.voidFrac)
    SoilVolume = 0.0
    StorageVolume = (storageDepth * storageVoidFrac)
    availVolume = (((storageThickness - storageDepth)) * storageVoidFrac)
    lidproc_getEvapRates(SurfaceVolume, 0.0, 0.0, StorageVolume, 1.0)
    if ((surfaceDepth > 0.0)) {
        StorageEvap = 0.0
    }
    StorageInflow = (SurfaceInflow + (SurfaceVolume / lidproc_Tstep))
    StorageExfil = getStorageExfilRate()
    StorageDrain = 0.0
    if ((theLidProc!!.drain!!.coeff > 0.0)) {
        StorageDrain = getStorageDrainRate(storageDepth, 0.0, 0.0, surfaceDepth)
    }
    maxRate = ((StorageInflow - StorageEvap) + ((storageDepth * storageVoidFrac) / lidproc_Tstep))
    StorageExfil = ((if ((((StorageExfil) <= (maxRate)))) (StorageExfil) else (maxRate)))
    StorageExfil = ((if ((((StorageExfil) >= (0.0)))) (StorageExfil) else (0.0)))
    if ((StorageDrain > 0.0)) {
        maxRate = ((-StorageExfil) - StorageEvap)
        if ((storageDepth >= storageThickness)) {
            maxRate += StorageInflow
        }
        if ((theLidProc!!.drain!!.offset <= storageDepth)) {
            maxRate += ((((storageDepth - theLidProc!!.drain!!.offset)) * storageVoidFrac) / lidproc_Tstep)
        }
        maxRate = ((if ((((maxRate) >= (0.0)))) (maxRate) else (0.0)))
        StorageDrain = ((if ((((StorageDrain) <= (maxRate)))) (StorageDrain) else (maxRate)))
    }
    maxRate = (((((((storageThickness - storageDepth)) * storageVoidFrac) / lidproc_Tstep) + StorageExfil) + StorageEvap) + StorageDrain)
    StorageInflow = ((if ((((StorageInflow) <= (maxRate)))) (StorageInflow) else (maxRate)))
    SurfaceInfil = StorageInflow
    SurfaceOutflow = getSurfaceOutflowRate(surfaceDepth)
    f!![SURF] = (((((SurfaceInflow - SurfaceEvap) - StorageInflow) - SurfaceOutflow)) / theLidProc!!.surface!!.voidFrac)
    f!![STOR] = (((((StorageInflow - StorageEvap) - StorageExfil) - StorageDrain)) / theLidProc!!.storage!!.voidFrac)
    f!![SOIL] = 0.0
}

// lidproc.c:823
fun pavementFluxRates(x: DoubleArray?, f: DoubleArray?) {
    var surfaceDepth: Double = 0.0
    var paveDepth: Double = 0.0
    var soilTheta: Double = 0.0
    var storageDepth: Double = 0.0
    var pervFrac: Double = ((1.0 - theLidProc!!.pavement!!.impervFrac))
    var storageInflow: Double = 0.0
    var availVolume: Double = 0.0
    var maxRate: Double = 0.0
    var paveVoidFrac: Double = (theLidProc!!.pavement!!.voidFrac * pervFrac)
    var paveThickness: Double = theLidProc!!.pavement!!.thickness
    var soilThickness: Double = theLidProc!!.soil!!.thickness
    var soilPorosity: Double = theLidProc!!.soil!!.porosity
    var soilFieldCap: Double = theLidProc!!.soil!!.fieldCap
    var soilWiltPoint: Double = theLidProc!!.soil!!.wiltPoint
    var storageThickness: Double = theLidProc!!.storage!!.thickness
    var storageVoidFrac: Double = theLidProc!!.storage!!.voidFrac
    surfaceDepth = x!![SURF]
    paveDepth = x!![PAVE]
    soilTheta = x!![SOIL]
    storageDepth = x!![STOR]
    SurfaceVolume = (surfaceDepth * theLidProc!!.surface!!.voidFrac)
    PaveVolume = (paveDepth * paveVoidFrac)
    SoilVolume = (soilTheta * soilThickness)
    StorageVolume = (storageDepth * storageVoidFrac)
    availVolume = (SoilVolume - (soilWiltPoint * soilThickness))
    lidproc_getEvapRates(SurfaceVolume, PaveVolume, availVolume, StorageVolume, pervFrac)
    if (((paveDepth >= paveThickness) || (((soilThickness > 0.0) && (soilTheta >= soilPorosity))))) {
        StorageEvap = 0.0
    }
    SurfaceInfil = (SurfaceInflow + ((SurfaceVolume / lidproc_Tstep)))
    PavePerc = (getPavementPermRate() * pervFrac)
    SurfaceInfil = ((if ((((SurfaceInfil) <= (PavePerc)))) (SurfaceInfil) else (PavePerc)))
    maxRate = (((PaveVolume / lidproc_Tstep) + SurfaceInfil) - PaveEvap)
    maxRate = ((if ((((maxRate) >= (0.0)))) (maxRate) else (0.0)))
    PavePerc = ((if ((((PavePerc) <= (maxRate)))) (PavePerc) else (maxRate)))
    if ((soilThickness > 0.0)) {
        SoilPerc = getSoilPercRate(soilTheta)
        availVolume = (((soilTheta - soilFieldCap)) * soilThickness)
        maxRate = ((((if ((((availVolume) >= (0.0)))) (availVolume) else (0.0))) / lidproc_Tstep) - SoilEvap)
        SoilPerc = ((if ((((SoilPerc) <= (maxRate)))) (SoilPerc) else (maxRate)))
        SoilPerc = ((if ((((SoilPerc) >= (0.0)))) (SoilPerc) else (0.0)))
    } else {
        SoilPerc = PavePerc
    }
    StorageExfil = getStorageExfilRate()
    StorageDrain = 0.0
    if ((theLidProc!!.drain!!.coeff > 0.0)) {
        StorageDrain = getStorageDrainRate(storageDepth, soilTheta, paveDepth, surfaceDepth)
    }
    if ((((soilThickness == 0.0) && (storageDepth >= storageThickness)) && (paveDepth >= paveThickness))) {
        maxRate = ((StorageEvap + StorageDrain) + StorageExfil)
        if ((PavePerc > maxRate)) {
            PavePerc = maxRate
        } else {
            StorageExfil = ((if ((((StorageExfil) <= (PavePerc)))) (StorageExfil) else (PavePerc)))
            StorageDrain = (PavePerc - StorageExfil)
        }
        SoilPerc = PavePerc
        SurfaceInfil = ((if ((((SurfaceInfil) <= (PavePerc)))) (SurfaceInfil) else (PavePerc)))
    } else if (((((soilThickness > 0.0) && (storageDepth >= storageThickness)) && (soilTheta >= soilPorosity)) && (paveDepth >= paveThickness))) {
        maxRate = (StorageExfil + StorageDrain)
        if ((SoilPerc < maxRate)) {
            maxRate = SoilPerc
        } else {
            maxRate = ((if ((((maxRate) <= (PavePerc)))) (maxRate) else (PavePerc)))
        }
        if ((maxRate > StorageExfil)) {
            StorageDrain = (maxRate - StorageExfil)
        } else {
            StorageExfil = maxRate
            StorageDrain = 0.0
        }
        SoilPerc = maxRate
        PavePerc = maxRate
        SurfaceInfil = ((if ((((SurfaceInfil) <= (PavePerc)))) (SurfaceInfil) else (PavePerc)))
    } else if ((((soilThickness > 0.0) && (storageDepth >= storageThickness)) && (soilTheta >= soilPorosity))) {
        maxRate = (StorageDrain + StorageExfil)
        if ((SoilPerc > maxRate)) {
            SoilPerc = maxRate
        } else {
            StorageExfil = ((if ((((StorageExfil) <= (SoilPerc)))) (StorageExfil) else (SoilPerc)))
            StorageDrain = (SoilPerc - StorageExfil)
        }
        PavePerc = ((if ((((PavePerc) <= (SoilPerc)))) (PavePerc) else (SoilPerc)))
        availVolume = (((paveThickness - paveDepth)) * paveVoidFrac)
        maxRate = (((availVolume / lidproc_Tstep) + PavePerc) + PaveEvap)
        SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
    } else if ((((soilThickness > 0.0) && (paveDepth >= paveThickness)) && (soilTheta >= soilPorosity))) {
        PavePerc = ((if ((((PavePerc) <= (SoilPerc)))) (PavePerc) else (SoilPerc)))
        SoilPerc = PavePerc
        SurfaceInfil = ((if ((((SurfaceInfil) <= (PavePerc)))) (SurfaceInfil) else (PavePerc)))
        maxRate = ((if (((((((StorageVolume / lidproc_Tstep) + SoilPerc) - StorageEvap)) >= (0.0)))) ((((StorageVolume / lidproc_Tstep) + SoilPerc) - StorageEvap)) else (0.0)))
        StorageExfil = ((if ((((StorageExfil) <= (maxRate)))) (StorageExfil) else (maxRate)))
    } else {
        maxRate = ((SoilPerc - StorageEvap) + (StorageVolume / lidproc_Tstep))
        maxRate = ((if ((((0.0) >= (maxRate)))) (0.0) else (maxRate)))
        StorageExfil = ((if ((((StorageExfil) <= (maxRate)))) (StorageExfil) else (maxRate)))
        if ((StorageDrain > 0.0)) {
            maxRate = ((-StorageExfil) - StorageEvap)
            if ((storageDepth >= storageThickness)) {
                maxRate += SoilPerc
            }
            if ((theLidProc!!.drain!!.offset <= storageDepth)) {
                maxRate += ((((storageDepth - theLidProc!!.drain!!.offset)) * storageVoidFrac) / lidproc_Tstep)
            }
            maxRate = ((if ((((maxRate) >= (0.0)))) (maxRate) else (0.0)))
            StorageDrain = ((if ((((StorageDrain) <= (maxRate)))) (StorageDrain) else (maxRate)))
        }
        availVolume = (((storageThickness - storageDepth)) * storageVoidFrac)
        maxRate = ((((availVolume / lidproc_Tstep) + StorageEvap) + StorageDrain) + StorageExfil)
        maxRate = ((if ((((maxRate) >= (0.0)))) (maxRate) else (0.0)))
        if ((soilThickness > 0.0)) {
            SoilPerc = ((if ((((SoilPerc) <= (maxRate)))) (SoilPerc) else (maxRate)))
            maxRate = (((((soilPorosity - soilTheta)) * soilThickness) / lidproc_Tstep) + SoilPerc)
        }
        PavePerc = ((if ((((PavePerc) <= (maxRate)))) (PavePerc) else (maxRate)))
        availVolume = (((paveThickness - paveDepth)) * paveVoidFrac)
        maxRate = (((availVolume / lidproc_Tstep) + PavePerc) + PaveEvap)
        SurfaceInfil = ((if ((((SurfaceInfil) <= (maxRate)))) (SurfaceInfil) else (maxRate)))
    }
    SurfaceOutflow = getSurfaceOutflowRate(surfaceDepth)
    f!![SURF] = (((SurfaceInflow - SurfaceEvap) - SurfaceInfil) - SurfaceOutflow)
    f!![PAVE] = ((((SurfaceInfil - PaveEvap) - PavePerc)) / paveVoidFrac)
    if ((theLidProc!!.soil!!.thickness > 0.0)) {
        f!![SOIL] = ((((PavePerc - SoilEvap) - SoilPerc)) / soilThickness)
        storageInflow = SoilPerc
    } else {
        f!![SOIL] = 0.0
        storageInflow = PavePerc
        SoilPerc = 0.0
    }
    f!![STOR] = (((((storageInflow - StorageEvap) - StorageExfil) - StorageDrain)) / storageVoidFrac)
}

// lidproc.c:1061
fun swaleFluxRates(x: DoubleArray?, f: DoubleArray?) {
    var depth: Double = 0.0
    var topWidth: Double = 0.0
    var botWidth: Double = 0.0
    var length: Double = 0.0
    var surfInflow: Double = 0.0
    var surfWidth: Double = 0.0
    var surfArea: Double = 0.0
    var flowArea: Double = 0.0
    var lidArea: Double = 0.0
    var hydRadius: Double = 0.0
    var slope: Double = 0.0
    var volume: Double = 0.0
    var dVdT: Double = 0.0
    var dStore: Double = 0.0
    var xDepth: Double = 0.0
    depth = x!![SURF]
    depth = ((if ((((depth) <= (theLidProc!!.surface!!.thickness)))) (depth) else (theLidProc!!.surface!!.thickness)))
    dStore = 0.0
    slope = theLidProc!!.surface!!.sideSlope
    topWidth = theLidUnit!!.fullWidth
    topWidth = ((if ((((topWidth) >= (0.5)))) (topWidth) else (0.5)))
    botWidth = (topWidth - ((2.0 * slope) * theLidProc!!.surface!!.thickness))
    if ((botWidth < 0.5)) {
        botWidth = 0.5
        slope = ((0.5 * ((topWidth - 0.5))) / theLidProc!!.surface!!.thickness)
    }
    lidArea = theLidUnit!!.area
    length = (lidArea / topWidth)
    surfWidth = (botWidth + ((2.0 * slope) * depth))
    surfArea = (length * surfWidth)
    flowArea = (((depth * ((botWidth + (slope * depth))))) * theLidProc!!.surface!!.voidFrac)
    volume = (length * flowArea)
    surfInflow = (SurfaceInflow * lidArea)
    SurfaceEvap = (lidproc_EvapRate * surfArea)
    SurfaceEvap = ((if ((((SurfaceEvap) <= ((volume / lidproc_Tstep))))) (SurfaceEvap) else ((volume / lidproc_Tstep))))
    StorageExfil = (SurfaceInfil * surfArea)
    xDepth = (depth - dStore)
    if ((xDepth <= 1e-10)) {
        SurfaceOutflow = 0.0
    } else {
        flowArea -= (((dStore * ((botWidth + (slope * dStore))))) * theLidProc!!.surface!!.voidFrac)
        if ((flowArea < 1e-10)) {
            SurfaceOutflow = 0.0
        } else {
            botWidth = (botWidth + ((2.0 * dStore) * slope))
            hydRadius = (botWidth + ((2.0 * xDepth) * sqrt((1.0 + (slope * slope)))))
            hydRadius = (flowArea / hydRadius)
            SurfaceOutflow = ((theLidProc!!.surface!!.alpha * flowArea) * Glibm.pow(hydRadius, (2.0 / 3.0)))
        }
    }
    dVdT = (((surfInflow - SurfaceEvap) - StorageExfil) - SurfaceOutflow)
    if (((depth == theLidProc!!.surface!!.thickness) && (dVdT > 0.0))) {
        SurfaceOutflow += dVdT
        dVdT = 0.0
    }
    SurfaceEvap /= lidArea
    StorageExfil /= lidArea
    SurfaceOutflow /= lidArea
    f!![SURF] = (dVdT / surfArea)
    f!![SOIL] = 0.0
    f!![STOR] = 0.0
    SurfaceVolume = (volume / lidArea)
    SoilVolume = 0.0
    StorageVolume = 0.0
}

// lidproc.c:1176
fun barrelFluxRates(x: DoubleArray?, f: DoubleArray?) {
    var storageDepth: Double = x!![STOR]
    var head: Double = 0.0
    var maxValue: Double = 0.0
    SurfaceVolume = 0.0
    SoilVolume = 0.0
    StorageVolume = storageDepth
    SurfaceInfil = 0.0
    SurfaceOutflow = 0.0
    StorageDrain = 0.0
    if (((theLidProc!!.drain!!.delay == 0.0) || (theLidUnit!!.dryTime >= theLidProc!!.drain!!.delay))) {
        head = (storageDepth - theLidProc!!.drain!!.offset)
        if ((head > 0.0)) {
            StorageDrain = getStorageDrainRate(storageDepth, 0.0, 0.0, 0.0)
            maxValue = ((head / lidproc_Tstep))
            StorageDrain = ((if ((((StorageDrain) <= (maxValue)))) (StorageDrain) else (maxValue)))
        }
    }
    StorageInflow = SurfaceInflow
    maxValue = ((((theLidProc!!.storage!!.thickness - storageDepth)) / lidproc_Tstep) + StorageDrain)
    StorageInflow = ((if ((((StorageInflow) <= (maxValue)))) (StorageInflow) else (maxValue)))
    SurfaceInfil = StorageInflow
    f!![SURF] = (SurfaceInflow - StorageInflow)
    f!![STOR] = (StorageInflow - StorageDrain)
    f!![SOIL] = 0.0
}

// lidproc.c:1226
fun getSurfaceOutflowRate(depth: Double): Double {
    var delta: Double = 0.0
    var outflow: Double = 0.0
    delta = (depth - theLidProc!!.surface!!.thickness)
    if ((delta < 0.0)) {
        return 0.0
    }
    outflow = (((theLidProc!!.surface!!.alpha * Glibm.pow(delta, (5.0 / 3.0))) * theLidUnit!!.fullWidth) / theLidUnit!!.area)
    outflow = ((if ((((outflow) <= ((delta / lidproc_Tstep))))) (outflow) else ((delta / lidproc_Tstep))))
    return outflow
}

// lidproc.c:1251
fun getPavementPermRate(): Double {
    var permReduction: Double = 0.0
    var clogFactor: Double = theLidProc!!.pavement!!.clogFactor
    var regenDays: Double = theLidProc!!.pavement!!.regenDays
    if ((clogFactor > 0.0)) {
        if ((regenDays > 0.0)) {
            if ((((OldRunoffTime / 1000.0) / 86400.0) >= theLidUnit!!.nextRegenDay)) {
                theLidUnit!!.volTreated *= ((1.0 - theLidProc!!.pavement!!.regenDegree))
                theLidUnit!!.nextRegenDay += regenDays
            }
        }
        permReduction = (theLidUnit!!.volTreated / clogFactor)
        permReduction = ((if ((((permReduction) <= (1.0)))) (permReduction) else (1.0)))
    }
    return (theLidProc!!.pavement!!.kSat * ((1.0 - permReduction)))
}

// lidproc.c:1293
fun getSoilPercRate(theta: Double): Double {
    var delta: Double = 0.0
    if ((theta <= theLidProc!!.soil!!.fieldCap)) {
        return 0.0
    }
    delta = (theLidProc!!.soil!!.porosity - theta)
    return (theLidProc!!.soil!!.kSat * Glibm.exp(((-delta) * theLidProc!!.soil!!.kSlope)))
}

// lidproc.c:1313
fun getStorageExfilRate(): Double {
    var infil: Double = 0.0
    var clogFactor: Double = 0.0
    if ((theLidProc!!.storage!!.kSat == 0.0)) {
        return 0.0
    }
    if ((lidproc_MaxNativeInfil == 0.0)) {
        return 0.0
    }
    clogFactor = theLidProc!!.storage!!.clogFactor
    if ((clogFactor > 0.0)) {
        clogFactor = (theLidUnit!!.waterBalance!!.inflow / clogFactor)
        clogFactor = ((if ((((clogFactor) <= (1.0)))) (clogFactor) else (1.0)))
    }
    infil = (theLidProc!!.storage!!.kSat * ((1.0 - clogFactor)))
    return ((if ((((infil) <= (lidproc_MaxNativeInfil)))) (infil) else (lidproc_MaxNativeInfil)))
}

// lidproc.c:1344
fun getStorageDrainRate(storageDepth: Double, soilTheta: Double, paveDepth: Double, surfaceDepth: Double): Double {
    var curve: Int = theLidProc!!.drain!!.qCurve
    var head: Double = storageDepth
    var outflow: Double = 0.0
    var paveThickness: Double = theLidProc!!.pavement!!.thickness
    var soilThickness: Double = theLidProc!!.soil!!.thickness
    var soilPorosity: Double = theLidProc!!.soil!!.porosity
    var soilFieldCap: Double = theLidProc!!.soil!!.fieldCap
    var storageThickness: Double = theLidProc!!.storage!!.thickness
    if ((storageDepth >= storageThickness)) {
        if ((soilThickness > 0.0)) {
            if ((soilTheta > soilFieldCap)) {
                head += ((((soilTheta - soilFieldCap)) / ((soilPorosity - soilFieldCap))) * soilThickness)
                if ((soilTheta >= soilPorosity)) {
                    if ((paveThickness > 0.0)) {
                        head += paveDepth
                        if ((paveDepth >= paveThickness)) {
                            head += surfaceDepth
                        }
                    } else {
                        head += surfaceDepth
                    }
                }
            }
        } else if ((paveThickness > 0.0)) {
            head += paveDepth
            if ((paveDepth >= paveThickness)) {
                head += surfaceDepth
            }
        }
    }
    if (((theLidUnit!!.oldDrainFlow == 0.0) && (head <= theLidProc!!.drain!!.hOpen))) {
        return 0.0
    }
    if (((theLidUnit!!.oldDrainFlow > 0.0) && (head <= theLidProc!!.drain!!.hClose))) {
        return 0.0
    }
    head -= theLidProc!!.drain!!.offset
    if ((head > 1e-10)) {
        head *= (UCF(RAINDEPTH))
        outflow = (theLidProc!!.drain!!.coeff * Glibm.pow(head, theLidProc!!.drain!!.expon))
        if ((curve >= 0)) {
            outflow *= (table_lookup(Curve!![curve], head))
        }
        outflow /= (UCF(RAINFALL))
    }
    return outflow
}

// lidproc.c:1436
fun getDrainMatOutflow(depth: Double): Double {
    var result: Double = SoilPerc
    if ((theLidProc!!.drainMat!!.alpha > 0.0)) {
        result = ((((theLidProc!!.drainMat!!.alpha * Glibm.pow(depth, (5.0 / 3.0))) * theLidUnit!!.fullWidth) / theLidUnit!!.area) * theLidProc!!.drainMat!!.voidFrac)
    }
    return result
}

// lidproc.c:1458
fun lidproc_getEvapRates(surfaceVol: Double, paveVol: Double, soilVol: Double, storageVol: Double, pervFrac: Double) {
    var availEvap: Double = 0.0
    availEvap = lidproc_EvapRate
    SurfaceEvap = ((if ((((availEvap) <= ((surfaceVol / lidproc_Tstep))))) (availEvap) else ((surfaceVol / lidproc_Tstep))))
    SurfaceEvap = ((if ((((0.0) >= (SurfaceEvap)))) (0.0) else (SurfaceEvap)))
    availEvap = ((if ((((0.0) >= (((availEvap - SurfaceEvap)))))) (0.0) else (((availEvap - SurfaceEvap)))))
    availEvap *= pervFrac
    if ((SurfaceInfil > 0.0)) {
        PaveEvap = 0.0
        SoilEvap = 0.0
        StorageEvap = 0.0
    } else {
        PaveEvap = ((if ((((availEvap) <= ((paveVol / lidproc_Tstep))))) (availEvap) else ((paveVol / lidproc_Tstep))))
        availEvap = ((if ((((0.0) >= (((availEvap - PaveEvap)))))) (0.0) else (((availEvap - PaveEvap)))))
        SoilEvap = ((if ((((availEvap) <= ((soilVol / lidproc_Tstep))))) (availEvap) else ((soilVol / lidproc_Tstep))))
        availEvap = ((if ((((0.0) >= (((availEvap - SoilEvap)))))) (0.0) else (((availEvap - SoilEvap)))))
        StorageEvap = ((if ((((availEvap) <= ((storageVol / lidproc_Tstep))))) (availEvap) else ((storageVol / lidproc_Tstep))))
    }
}

// lidproc.c:1503
fun getSurfaceOverflowRate(surfaceDepth: DRef?): Double {
    var delta: Double = (surfaceDepth!!.get() - theLidProc!!.surface!!.thickness)
    if ((delta <= 0.0)) {
        return 0.0
    }
    surfaceDepth!!.set(theLidProc!!.surface!!.thickness)
    return ((delta * theLidProc!!.surface!!.voidFrac) / lidproc_Tstep)
}

// lidproc.c:1518
fun updateWaterBalance(lidUnit: TLidUnit?, inflow: Double, evap: Double, infil: Double, surfFlow: Double, drainFlow: Double, storage: Double) {
    lidUnit!!.volTreated += (inflow * lidproc_Tstep)
    lidUnit!!.waterBalance!!.inflow += (inflow * lidproc_Tstep)
    lidUnit!!.waterBalance!!.evap += (evap * lidproc_Tstep)
    lidUnit!!.waterBalance!!.infil += (infil * lidproc_Tstep)
    lidUnit!!.waterBalance!!.surfFlow += (surfFlow * lidproc_Tstep)
    lidUnit!!.waterBalance!!.drainFlow += (drainFlow * lidproc_Tstep)
    lidUnit!!.waterBalance!!.finalVol = storage
}

// lidproc.c:1544
fun modpuls_solve(n: Int, x: DoubleArray?, xOld: DoubleArray?, xPrev: DoubleArray?, xMin: DoubleArray?, xMax: DoubleArray?, xTol: DoubleArray?, qOld: DoubleArray?, q: DoubleArray?, dt: Double, omega: Double, derivs: FP5?): Int {
    var i: Int = 0
    var canStop: Int = 0
    var steps: Int = 1
    var maxSteps: Int = 20
    i = 0
    f1@ while ((i < n)) {
        xOld!![i] = x!![i]
        xPrev!![i] = x!![i]
        i++
    }
    w2@ while ((steps < maxSteps)) {
        canStop = 1
        derivs!!.call(x, q)
        i = 0
        f3@ while ((i < n)) {
            x!![i] = (xOld!![i] + ((((omega * qOld!![i]) + (((1.0 - omega)) * q!![i]))) * dt))
            x!![i] = ((if ((((x!![i]) <= (xMax!![i])))) (x!![i]) else (xMax!![i])))
            x!![i] = ((if ((((x!![i]) >= (xMin!![i])))) (x!![i]) else (xMin!![i])))
            if (((omega > 0.0) && (abs((x!![i] - xPrev!![i])) > xTol!![i]))) {
                canStop = 0
            }
            xPrev!![i] = x!![i]
            i++
        }
        if ((canStop != 0)) {
            return steps
        }
        steps++
    }
    return 0
}

