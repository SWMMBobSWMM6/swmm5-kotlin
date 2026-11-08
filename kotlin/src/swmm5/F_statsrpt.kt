// Generated from EPA SWMM 5.2.4 (statsrpt.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_statsrpt")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var FlowFmt: CPtr? = R.buf(6)
@JvmField var Vcf: Double = 0.0

// statsrpt.c:84
fun statsrpt_writeReport() {
    if (((FlowUnits == MGD) || (FlowUnits == CMS))) {
        sstrncpy(FlowFmt, __L534, 5L)
    } else {
        sstrncpy(FlowFmt, __L535, 5L)
    }
    if ((UnitSystem == US)) {
        Vcf = (7.48 / 1.0e6)
    } else {
        Vcf = (28.317 / 1.0e6)
    }
    if ((Nobjects!![SUBCATCH] > 0)) {
        if ((((!(IgnoreRainfall != 0)) || (((Nobjects!![SNOWMELT] > 0) && (!(IgnoreSnowmelt != 0))))) || (((Nobjects!![AQUIFER] > 0) && (!(IgnoreGwater != 0)))))) {
            writeSubcatchRunoff()
            lid_writeWaterBalance()
            if (!(IgnoreGwater != 0)) {
                writeGroundwater()
            }
            if (((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0)))) {
                writeSubcatchLoads()
            }
        }
    }
    if (((Nobjects!![LINK] > 0) && (!(IgnoreRouting != 0)))) {
        writeNodeDepths()
        writeNodeFlows()
        if ((RouteModel == DW)) {
            writeNodeSurcharge()
        }
        writeNodeFlooding()
        writeStorageVolumes()
        writeOutfallLoads()
        inlet_writeStatsReport()
        writeLinkFlows()
        writeFlowClass()
        writeLinkSurcharge()
        writePumpFlows()
        if (((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0)))) {
            writeLinkLoads()
        }
    }
}

// statsrpt.c:133
fun writeSubcatchRunoff() {
    var j: Int = 0
    var a: Double = 0.0
    var x: Double = 0.0
    var r: Double = 0.0
    if ((Nobjects!![SUBCATCH] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L473))
    report_writeLine((__L536))
    report_writeLine((__L473))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L537)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L538, VolUnitsWords!![UnitSystem], FlowUnitWords!![FlowUnits])
    } else {
        R.fprintf(Frpt!!.file, __L539, VolUnitsWords!![UnitSystem], FlowUnitWords!![FlowUnits])
    }
    R.fprintf(Frpt!!.file, __L540)
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        a = Subcatch!![j]!!.area
        if ((a == 0.0)) {
            j++
            continue@f1
        }
        R.fprintf(Frpt!!.file, __L541, Subcatch!![j]!!.ID)
        x = (SubcatchStats!![j]!!.precip * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.runon * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.evap * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.infil * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.impervRunoff * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.pervRunoff * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.runoff * UCF(RAINDEPTH))
        R.fprintf(Frpt!!.file, __L542, (x / a))
        x = (SubcatchStats!![j]!!.runoff * Vcf)
        R.fprintf(Frpt!!.file, __L543, x)
        x = (SubcatchStats!![j]!!.maxFlow * UCF(FLOW))
        R.fprintf(Frpt!!.file, __L544, x)
        r = (SubcatchStats!![j]!!.precip + SubcatchStats!![j]!!.runon)
        if ((r > 0.0)) {
            r = (SubcatchStats!![j]!!.runoff / r)
        }
        R.fprintf(Frpt!!.file, __L545, r)
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:190
fun writeGroundwater() {
    var i: Int = 0
    var j: Int = 0
    var count: Int = 0
    var totalSeconds: Double = (NewRunoffTime / 1000.0)
    var x: DoubleArray? = DoubleArray(9)
    if ((Nobjects!![SUBCATCH] == 0)) {
        return
    }
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        if ((Subcatch!![j]!!.groundwater != null)) {
            count++
        }
        j++
    }
    if ((count == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L188))
    report_writeLine((__L546))
    report_writeLine((__L188))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L547)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L548, FlowUnitWords!![FlowUnits])
    } else {
        R.fprintf(Frpt!!.file, __L549, FlowUnitWords!![FlowUnits])
    }
    R.fprintf(Frpt!!.file, __L550)
    j = 0
    f2@ while ((j < Nobjects!![SUBCATCH])) {
        if (((Subcatch!![j]!!.area == 0.0) || (Subcatch!![j]!!.groundwater == null))) {
            j++
            continue@f2
        }
        R.fprintf(Frpt!!.file, __L541, Subcatch!![j]!!.ID)
        x!![0] = (Subcatch!![j]!!.groundwater!!.stats!!.infil * UCF(RAINDEPTH))
        x!![1] = (Subcatch!![j]!!.groundwater!!.stats!!.evap * UCF(RAINDEPTH))
        x!![2] = (Subcatch!![j]!!.groundwater!!.stats!!.deepFlow * UCF(RAINDEPTH))
        x!![3] = (Subcatch!![j]!!.groundwater!!.stats!!.latFlow * UCF(RAINDEPTH))
        x!![4] = ((Subcatch!![j]!!.groundwater!!.stats!!.maxFlow * UCF(FLOW)) * Subcatch!![j]!!.area)
        x!![5] = (Subcatch!![j]!!.groundwater!!.stats!!.avgUpperMoist / totalSeconds)
        x!![6] = ((Subcatch!![j]!!.groundwater!!.stats!!.avgWaterTable * UCF(LENGTH)) / totalSeconds)
        x!![7] = Subcatch!![j]!!.groundwater!!.stats!!.finalUpperMoist
        x!![8] = (Subcatch!![j]!!.groundwater!!.stats!!.finalWaterTable * UCF(LENGTH))
        i = 0
        f3@ while ((i < 9)) {
            R.fprintf(Frpt!!.file, __L544, x!![i])
            i++
        }
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:245
fun writeSubcatchLoads() {
    var i: Int = 0
    var j: Int = 0
    var p: Int = 0
    var x: Double = 0.0
    var totals: DoubleArray? = null
    var units: CPtr? = R.buf(15)
    var subcatchLine: CPtr? = R.bufFrom(__L551, 21)
    var pollutLine: CPtr? = R.bufFrom(__L552, 15)
    totals = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
    if ((totals != null)) {
        report_writeLine((__L0))
        report_writeLine((__L553))
        report_writeLine((__L554))
        report_writeLine((__L553))
        report_writeLine((__L0))
        R.fprintf(Frpt!!.file, __L356, subcatchLine)
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L169, pollutLine)
            p++
        }
        R.fprintf(Frpt!!.file, __L555)
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L423, Pollut!![p]!!.ID)
            p++
        }
        R.fprintf(Frpt!!.file, __L556)
        p = 0
        f3@ while ((p < Nobjects!![POLLUT])) {
            i = UnitSystem
            if ((Pollut!![p]!!.units == COUNT)) {
                i = 2
            }
            sstrncpy(units, LoadUnitsWords!![i], 14L)
            R.fprintf(Frpt!!.file, __L423, units)
            totals!![p] = 0.0
            p++
        }
        R.fprintf(Frpt!!.file, __L356, subcatchLine)
        p = 0
        f4@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L169, pollutLine)
            p++
        }
        j = 0
        f5@ while ((j < Nobjects!![SUBCATCH])) {
            R.fprintf(Frpt!!.file, __L541, Subcatch!![j]!!.ID)
            p = 0
            f6@ while ((p < Nobjects!![POLLUT])) {
                x = Subcatch!![j]!!.totalLoad!![p]
                totals!![p] += x
                if ((Pollut!![p]!!.units == COUNT)) {
                    x = ((if (((x) > 0.0)) Glibm.log10((x)) else (x)))
                }
                R.fprintf(Frpt!!.file, __L427, x)
                p++
            }
            j++
        }
        R.fprintf(Frpt!!.file, __L356, subcatchLine)
        p = 0
        f7@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L169, pollutLine)
            p++
        }
        R.fprintf(Frpt!!.file, __L557)
        p = 0
        f8@ while ((p < Nobjects!![POLLUT])) {
            x = totals!![p]
            if ((Pollut!![p]!!.units == COUNT)) {
                x = ((if (((x) > 0.0)) Glibm.log10((x)) else (x)))
            }
            R.fprintf(Frpt!!.file, __L427, x)
            p++
        }
        R.free(totals)
        report_writeLine((__L0))
    }
}

// statsrpt.c:310
fun writeNodeDepths() {
    var j: Int = 0
    val days = IBox(0)
    val hrs = IBox(0)
    val mins = IBox(0)
    if ((Nobjects!![LINK] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L558))
    report_writeLine((__L559))
    report_writeLine((__L558))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L560)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L561)
    } else {
        R.fprintf(Frpt!!.file, __L562)
    }
    R.fprintf(Frpt!!.file, __L563)
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        R.fprintf(Frpt!!.file, __L541, Node!![j]!!.ID)
        R.fprintf(Frpt!!.file, __L564, NodeTypeWords!![Node!![j]!!.type])
        getElapsedTime(NodeStats!![j]!!.maxDepthDate, days, hrs, mins)
        R.fprintf(Frpt!!.file, __L565, ((NodeStats!![j]!!.avgDepth / (ReportStepCount).toDouble()) * UCF(LENGTH)), (NodeStats!![j]!!.maxDepth * UCF(LENGTH)), (((NodeStats!![j]!!.maxDepth + Node!![j]!!.invertElev)) * UCF(LENGTH)), days.v, hrs.v, mins.v, NodeStats!![j]!!.maxRptDepth)
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:353
fun writeNodeFlows() {
    var j: Int = 0
    val days1 = IBox(0)
    val hrs1 = IBox(0)
    val mins1 = IBox(0)
    report_writeLine((__L0))
    report_writeLine((__L188))
    report_writeLine((__L566))
    report_writeLine((__L188))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L567, FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], VolUnitsWords!![UnitSystem], VolUnitsWords!![UnitSystem])
    R.fprintf(Frpt!!.file, __L568)
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        R.fprintf(Frpt!!.file, __L541, Node!![j]!!.ID)
        R.fprintf(Frpt!!.file, __L569, NodeTypeWords!![Node!![j]!!.type])
        getElapsedTime(NodeStats!![j]!!.maxInflowDate, days1, hrs1, mins1)
        R.fprintf(Frpt!!.file, FlowFmt, (NodeStats!![j]!!.maxLatFlow * UCF(FLOW)))
        R.fprintf(Frpt!!.file, FlowFmt, (NodeStats!![j]!!.maxInflow * UCF(FLOW)))
        R.fprintf(Frpt!!.file, __L570, days1.v, hrs1.v, mins1.v)
        R.fprintf(Frpt!!.file, __L571, (NodeStats!![j]!!.totLatFlow * Vcf))
        R.fprintf(Frpt!!.file, __L571, (NodeInflow!![j] * Vcf))
        if ((abs(NodeOutflow!![j]) < 1.0)) {
            R.fprintf(Frpt!!.file, __L572, ((((NodeInflow!![j] - NodeOutflow!![j])) * Vcf) * 1.0e6), VolUnitsWords2!![UnitSystem])
        } else {
            R.fprintf(Frpt!!.file, __L573, ((((NodeInflow!![j] - NodeOutflow!![j])) / NodeOutflow!![j]) * 100.0))
        }
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:403
fun writeNodeSurcharge() {
    var j: Int = 0
    var n: Int = 0
    var t: Double = 0.0
    var d1: Double = 0.0
    var d2: Double = 0.0
    report_writeLine((__L0))
    report_writeLine((__L574))
    report_writeLine((__L575))
    report_writeLine((__L574))
    report_writeLine((__L0))
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.type == OUTFALL)) {
            j++
            continue@f1
        }
        if ((NodeStats!![j]!!.timeSurcharged == 0.0)) {
            j++
            continue@f1
        }
        t = ((if ((((0.01) >= (((NodeStats!![j]!!.timeSurcharged / 3600.0)))))) (0.01) else (((NodeStats!![j]!!.timeSurcharged / 3600.0)))))
        if ((n == 0)) {
            report_writeLine((__L576))
            R.fprintf(Frpt!!.file, __L577)
            if ((UnitSystem == US)) {
                R.fprintf(Frpt!!.file, __L578)
            } else {
                R.fprintf(Frpt!!.file, __L579)
            }
            R.fprintf(Frpt!!.file, __L580)
            n = 1
        }
        R.fprintf(Frpt!!.file, __L541, Node!![j]!!.ID)
        R.fprintf(Frpt!!.file, __L569, NodeTypeWords!![Node!![j]!!.type])
        d1 = ((NodeStats!![j]!!.maxDepth + Node!![j]!!.invertElev) - Node!![j]!!.crownElev)
        if ((d1 < 0.0)) {
            d1 = 0.0
        }
        d2 = (Node!![j]!!.fullDepth - NodeStats!![j]!!.maxDepth)
        if ((d2 < 0.0)) {
            d2 = 0.0
        }
        R.fprintf(Frpt!!.file, __L581, t, (d1 * UCF(LENGTH)), (d2 * UCF(LENGTH)))
        j++
    }
    if ((n == 0)) {
        report_writeLine((__L582))
    }
    report_writeLine((__L0))
}

// statsrpt.c:449
fun writeNodeFlooding() {
    var j: Int = 0
    var n: Int = 0
    val days = IBox(0)
    val hrs = IBox(0)
    val mins = IBox(0)
    var t: Double = 0.0
    report_writeLine((__L0))
    report_writeLine((__L255))
    report_writeLine((__L583))
    report_writeLine((__L255))
    report_writeLine((__L0))
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.type == OUTFALL)) {
            j++
            continue@f1
        }
        if ((NodeStats!![j]!!.timeFlooded == 0.0)) {
            j++
            continue@f1
        }
        t = ((if ((((0.01) >= (((NodeStats!![j]!!.timeFlooded / 3600.0)))))) (0.01) else (((NodeStats!![j]!!.timeFlooded / 3600.0)))))
        if ((n == 0)) {
            report_writeLine((__L584))
            R.fprintf(Frpt!!.file, __L585)
            if ((RouteModel == DW)) {
                R.fprintf(Frpt!!.file, __L586)
            } else {
                R.fprintf(Frpt!!.file, __L587)
            }
            R.fprintf(Frpt!!.file, __L588, FlowUnitWords!![FlowUnits], VolUnitsWords!![UnitSystem])
            if ((RouteModel == DW)) {
                R.fprintf(Frpt!!.file, __L589, PondingUnitsWords!![UnitSystem])
            } else if ((UnitSystem == US)) {
                R.fprintf(Frpt!!.file, __L590)
            } else {
                R.fprintf(Frpt!!.file, __L591)
            }
            R.fprintf(Frpt!!.file, __L592)
            n = 1
        }
        R.fprintf(Frpt!!.file, __L541, Node!![j]!!.ID)
        R.fprintf(Frpt!!.file, __L593, t)
        R.fprintf(Frpt!!.file, FlowFmt, (NodeStats!![j]!!.maxOverflow * UCF(FLOW)))
        getElapsedTime(NodeStats!![j]!!.maxOverflowDate, days, hrs, mins)
        R.fprintf(Frpt!!.file, __L594, days.v, hrs.v, mins.v)
        R.fprintf(Frpt!!.file, __L573, (NodeStats!![j]!!.volFlooded * Vcf))
        if ((RouteModel == DW)) {
            R.fprintf(Frpt!!.file, __L195, (((NodeStats!![j]!!.maxDepth - Node!![j]!!.fullDepth)) * UCF(LENGTH)))
        } else {
            R.fprintf(Frpt!!.file, __L195, ((NodeStats!![j]!!.maxPondedVol / 1000.0) * UCF(VOLUME)))
        }
        j++
    }
    if ((n == 0)) {
        report_writeLine((__L595))
    }
    report_writeLine((__L0))
}

// statsrpt.c:508
fun writeStorageVolumes() {
    var j: Int = 0
    var k: Int = 0
    val days = IBox(0)
    val hrs = IBox(0)
    val mins = IBox(0)
    var avgVol: Double = 0.0
    var maxVol: Double = 0.0
    var pctAvgVol: Double = 0.0
    var pctMaxVol: Double = 0.0
    var pctEvapLoss: Double = 0.0
    var pctSeepLoss: Double = 0.0
    if ((Nnodes!![STORAGE] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L574))
        report_writeLine((__L596))
        report_writeLine((__L574))
        report_writeLine((__L0))
        R.fprintf(Frpt!!.file, __L597)
        if ((UnitSystem == US)) {
            R.fprintf(Frpt!!.file, __L598)
        } else {
            R.fprintf(Frpt!!.file, __L599)
        }
        R.fprintf(Frpt!!.file, __L600, FlowUnitWords!![FlowUnits])
        R.fprintf(Frpt!!.file, __L601)
        j = 0
        f1@ while ((j < Nobjects!![NODE])) {
            if ((Node!![j]!!.type != STORAGE)) {
                j++
                continue@f1
            }
            k = Node!![j]!!.subIndex
            R.fprintf(Frpt!!.file, __L541, Node!![j]!!.ID)
            avgVol = (StorageStats!![k]!!.avgVol / (ReportStepCount).toDouble())
            maxVol = StorageStats!![k]!!.maxVol
            pctMaxVol = 0.0
            pctAvgVol = 0.0
            if ((Node!![j]!!.fullVolume > 0.0)) {
                pctAvgVol = ((avgVol / Node!![j]!!.fullVolume) * 100.0)
                pctMaxVol = ((maxVol / Node!![j]!!.fullVolume) * 100.0)
            }
            pctEvapLoss = 0.0
            pctSeepLoss = 0.0
            if ((NodeInflow!![j] > 0.0)) {
                pctEvapLoss = ((StorageStats!![k]!!.evapLosses / NodeInflow!![j]) * 100.0)
                pctSeepLoss = ((StorageStats!![k]!!.exfilLosses / NodeInflow!![j]) * 100.0)
            }
            R.fprintf(Frpt!!.file, __L602, ((avgVol * UCF(VOLUME)) / 1000.0), pctAvgVol, pctEvapLoss, pctSeepLoss, ((maxVol * UCF(VOLUME)) / 1000.0), pctMaxVol)
            getElapsedTime(StorageStats!![k]!!.maxVolDate, days, hrs, mins)
            R.fprintf(Frpt!!.file, __L603, days.v, hrs.v, mins.v)
            R.fprintf(Frpt!!.file, FlowFmt, (StorageStats!![k]!!.maxFlow * UCF(FLOW)))
            j++
        }
        report_writeLine((__L0))
    }
}

// statsrpt.c:575
fun writeOutfallLoads() {
    var units: CPtr? = R.buf(15)
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var p: Int = 0
    var x: Double = 0.0
    var outfallCount: Double = 0.0
    var flowCount: Double = 0.0
    var flowSum: Double = 0.0
    var freqSum: Double = 0.0
    var volSum: Double = 0.0
    var totals: DoubleArray? = null
    if ((Nnodes!![OUTFALL] > 0)) {
        totals = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            totals!![p] = 0.0
            p++
        }
        flowSum = 0.0
        freqSum = 0.0
        volSum = 0.0
        report_writeLine((__L0))
        report_writeLine((__L604))
        report_writeLine((__L605))
        report_writeLine((__L604))
        report_writeLine((__L0))
        R.fprintf(Frpt!!.file, __L606)
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L552)
            p++
        }
        R.fprintf(Frpt!!.file, __L607)
        p = 0
        f3@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L608)
            p++
        }
        R.fprintf(Frpt!!.file, __L609)
        p = 0
        f4@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L423, Pollut!![p]!!.ID)
            p++
        }
        R.fprintf(Frpt!!.file, __L610, FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], VolUnitsWords!![UnitSystem])
        p = 0
        f5@ while ((p < Nobjects!![POLLUT])) {
            i = UnitSystem
            if ((Pollut!![p]!!.units == COUNT)) {
                i = 2
            }
            sstrncpy(units, LoadUnitsWords!![i], 14L)
            R.fprintf(Frpt!!.file, __L423, units)
            p++
        }
        R.fprintf(Frpt!!.file, __L606)
        p = 0
        f6@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L552)
            p++
        }
        j = 0
        f7@ while ((j < Nobjects!![NODE])) {
            if ((Node!![j]!!.type != OUTFALL)) {
                j++
                continue@f7
            }
            k = Node!![j]!!.subIndex
            flowCount = (OutfallStats!![k]!!.totalPeriods).toDouble()
            R.fprintf(Frpt!!.file, __L541, Node!![j]!!.ID)
            x = ((100.0 * flowCount) / (ReportStepCount).toDouble())
            R.fprintf(Frpt!!.file, __L611, x)
            freqSum += x
            if ((flowCount > 0.0)) {
                x = ((OutfallStats!![k]!!.avgFlow * UCF(FLOW)) / flowCount)
            } else {
                x = 0.0
            }
            flowSum += x
            R.fprintf(Frpt!!.file, __L33)
            R.fprintf(Frpt!!.file, FlowFmt, x)
            R.fprintf(Frpt!!.file, __L33)
            R.fprintf(Frpt!!.file, FlowFmt, (OutfallStats!![k]!!.maxFlow * UCF(FLOW)))
            R.fprintf(Frpt!!.file, __L573, (NodeInflow!![j] * Vcf))
            volSum += NodeInflow!![j]
            p = 0
            f8@ while ((p < Nobjects!![POLLUT])) {
                x = ((OutfallStats!![k]!!.totalLoad!![p] * 28.317) * Pollut!![p]!!.mcf)
                totals!![p] += x
                if ((Pollut!![p]!!.units == COUNT)) {
                    x = ((if (((x) > 0.0)) Glibm.log10((x)) else (x)))
                }
                R.fprintf(Frpt!!.file, __L427, x)
                p++
            }
            j++
        }
        outfallCount = (Nnodes!![OUTFALL]).toDouble()
        R.fprintf(Frpt!!.file, __L606)
        p = 0
        f9@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L552)
            p++
        }
        R.fprintf(Frpt!!.file, __L612, (freqSum / outfallCount))
        R.fprintf(Frpt!!.file, FlowFmt, flowSum)
        R.fprintf(Frpt!!.file, __L33)
        R.fprintf(Frpt!!.file, FlowFmt, (MaxOutfallFlow * UCF(FLOW)))
        R.fprintf(Frpt!!.file, __L573, (volSum * Vcf))
        p = 0
        f10@ while ((p < Nobjects!![POLLUT])) {
            x = totals!![p]
            if ((Pollut!![p]!!.units == COUNT)) {
                x = ((if (((x) > 0.0)) Glibm.log10((x)) else (x)))
            }
            R.fprintf(Frpt!!.file, __L427, x)
            p++
        }
        report_writeLine((__L0))
        R.free(totals)
    }
}

// statsrpt.c:691
fun writeLinkFlows() {
    var j: Int = 0
    var k: Int = 0
    val days = IBox(0)
    val hrs = IBox(0)
    val mins = IBox(0)
    var v: Double = 0.0
    var fullDepth: Double = 0.0
    if ((Nobjects!![LINK] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L235))
    report_writeLine((__L613))
    report_writeLine((__L235))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L614)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L615, FlowUnitWords!![FlowUnits])
    } else {
        R.fprintf(Frpt!!.file, __L616, FlowUnitWords!![FlowUnits])
    }
    R.fprintf(Frpt!!.file, __L617)
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        k = Link!![j]!!.subIndex
        R.fprintf(Frpt!!.file, __L541, Link!![j]!!.ID)
        if ((Link!![j]!!.xsect!!.type == DUMMY)) {
            R.fprintf(Frpt!!.file, __L618)
        } else if ((Link!![j]!!.xsect!!.type == IRREGULAR)) {
            R.fprintf(Frpt!!.file, __L619)
        } else {
            R.fprintf(Frpt!!.file, __L620, LinkTypeWords!![Link!![j]!!.type])
        }
        getElapsedTime(LinkStats!![j]!!.maxFlowDate, days, hrs, mins)
        R.fprintf(Frpt!!.file, FlowFmt, (LinkStats!![j]!!.maxFlow * UCF(FLOW)))
        R.fprintf(Frpt!!.file, __L570, days.v, hrs.v, mins.v)
        if (((Link!![j]!!.type == PUMP) && (Link!![j]!!.qFull > 0.0))) {
            R.fprintf(Frpt!!.file, __L621)
            R.fprintf(Frpt!!.file, __L622, (LinkStats!![j]!!.maxFlow / Link!![j]!!.qFull))
            j++
            continue@f1
        }
        if ((Link!![j]!!.xsect!!.type == DUMMY)) {
            j++
            continue@f1
        }
        if ((Link!![j]!!.type == OUTLET)) {
            j++
            continue@f1
        }
        if ((Link!![j]!!.type == CONDUIT)) {
            v = (LinkStats!![j]!!.maxVeloc * UCF(LENGTH))
            if ((v > 50.0)) {
                R.fprintf(Frpt!!.file, __L623)
            } else {
                R.fprintf(Frpt!!.file, __L624, v)
            }
            R.fprintf(Frpt!!.file, __L622, ((LinkStats!![j]!!.maxFlow / Link!![j]!!.qFull) / (Conduit!![k]!!.barrels).toDouble()))
        } else {
            R.fprintf(Frpt!!.file, __L625)
        }
        fullDepth = Link!![j]!!.xsect!!.yFull
        if (((Link!![j]!!.type == ORIFICE) && (Orifice!![k]!!.type == BOTTOM_ORIFICE))) {
            fullDepth = 0.0
        }
        if ((fullDepth > 0.0)) {
            R.fprintf(Frpt!!.file, __L622, (LinkStats!![j]!!.maxDepth / fullDepth))
        } else {
            R.fprintf(Frpt!!.file, __L626)
        }
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:778
fun writeFlowClass() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var totalSeconds: Double = RoutingTimeSpan
    if ((RouteModel != DW)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L473))
    report_writeLine((__L627))
    report_writeLine((__L473))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L628)
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.type != CONDUIT)) {
            j++
            continue@f1
        }
        if ((Link!![j]!!.xsect!!.type == DUMMY)) {
            j++
            continue@f1
        }
        k = Link!![j]!!.subIndex
        R.fprintf(Frpt!!.file, __L541, Link!![j]!!.ID)
        R.fprintf(Frpt!!.file, __L629, (Conduit!![k]!!.modLength / Conduit!![k]!!.length))
        i = 0
        f2@ while ((i < MAX_FLOW_CLASSES)) {
            R.fprintf(Frpt!!.file, __L630, ((LinkStats!![j]!!.timeInFlowClass!![i] / totalSeconds)).also { __v -> LinkStats!![j]!!.timeInFlowClass!![i] = __v })
            i++
        }
        R.fprintf(Frpt!!.file, __L630, (LinkStats!![j]!!.timeNormalFlow / totalSeconds))
        R.fprintf(Frpt!!.file, __L630, (LinkStats!![j]!!.timeInletControl / totalSeconds))
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:821
fun writeLinkSurcharge() {
    var i: Int = 0
    var j: Int = 0
    var n: Int = 0
    var t: DoubleArray? = DoubleArray(5)
    report_writeLine((__L0))
    report_writeLine((__L470))
    report_writeLine((__L631))
    report_writeLine((__L470))
    report_writeLine((__L0))
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        if (((Link!![j]!!.type != CONDUIT) || (Link!![j]!!.xsect!!.type == DUMMY))) {
            j++
            continue@f1
        }
        t!![0] = (LinkStats!![j]!!.timeSurcharged / 3600.0)
        t!![1] = (LinkStats!![j]!!.timeFullUpstream / 3600.0)
        t!![2] = (LinkStats!![j]!!.timeFullDnstream / 3600.0)
        t!![3] = (LinkStats!![j]!!.timeFullFlow / 3600.0)
        if (((((t!![0] + t!![1]) + t!![2]) + t!![3]) == 0.0)) {
            j++
            continue@f1
        }
        t!![4] = (LinkStats!![j]!!.timeCapacityLimited / 3600.0)
        i = 0
        f2@ while ((i < 5)) {
            t!![i] = ((if ((((0.01) >= (t!![i])))) (0.01) else (t!![i])))
            i++
        }
        if ((n == 0)) {
            R.fprintf(Frpt!!.file, __L632)
            n = 1
        }
        R.fprintf(Frpt!!.file, __L541, Link!![j]!!.ID)
        R.fprintf(Frpt!!.file, __L633, t!![0], t!![1], t!![2], t!![3], t!![4])
        j++
    }
    if ((n == 0)) {
        report_writeLine((__L634))
    }
    report_writeLine((__L0))
}

// statsrpt.c:862
fun writePumpFlows() {
    var j: Int = 0
    var k: Int = 0
    var avgFlow: Double = 0.0
    var pctUtilized: Double = 0.0
    var pctOffCurve1: Double = 0.0
    var pctOffCurve2: Double = 0.0
    var totalSeconds: Double = RoutingTimeSpan
    if ((Nlinks!![PUMP] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L221))
    report_writeLine((__L635))
    report_writeLine((__L221))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L636, FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], VolUnitsWords!![UnitSystem])
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.type != PUMP)) {
            j++
            continue@f1
        }
        k = Link!![j]!!.subIndex
        R.fprintf(Frpt!!.file, __L541, Link!![j]!!.ID)
        pctUtilized = ((PumpStats!![k]!!.utilized / totalSeconds) * 100.0)
        avgFlow = PumpStats!![k]!!.avgFlow
        if ((PumpStats!![k]!!.totalPeriods > 0)) {
            avgFlow /= ((PumpStats!![k]!!.totalPeriods).toDouble())
        }
        R.fprintf(Frpt!!.file, __L637, pctUtilized, PumpStats!![k]!!.startUps, (PumpStats!![k]!!.minFlow * UCF(FLOW)), (avgFlow * UCF(FLOW)), (PumpStats!![k]!!.maxFlow * UCF(FLOW)), (PumpStats!![k]!!.volume * Vcf), PumpStats!![k]!!.energy)
        pctOffCurve1 = PumpStats!![k]!!.offCurveLow
        pctOffCurve2 = PumpStats!![k]!!.offCurveHigh
        if ((PumpStats!![k]!!.utilized > 0.0)) {
            pctOffCurve1 = ((pctOffCurve1 / PumpStats!![k]!!.utilized) * 100.0)
            pctOffCurve2 = ((pctOffCurve2 / PumpStats!![k]!!.utilized) * 100.0)
        }
        R.fprintf(Frpt!!.file, __L638, pctOffCurve1, pctOffCurve2)
        j++
    }
    report_writeLine((__L0))
}

// statsrpt.c:916
fun writeLinkLoads() {
    var i: Int = 0
    var j: Int = 0
    var p: Int = 0
    var x: Double = 0.0
    var units: CPtr? = R.buf(15)
    var linkLine: CPtr? = R.bufFrom(__L551, 21)
    var pollutLine: CPtr? = R.bufFrom(__L552, 15)
    report_writeLine((__L0))
    report_writeLine((__L473))
    report_writeLine((__L639))
    report_writeLine((__L473))
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L356, linkLine)
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        R.fprintf(Frpt!!.file, __L169, pollutLine)
        p++
    }
    R.fprintf(Frpt!!.file, __L555)
    p = 0
    f2@ while ((p < Nobjects!![POLLUT])) {
        R.fprintf(Frpt!!.file, __L423, Pollut!![p]!!.ID)
        p++
    }
    R.fprintf(Frpt!!.file, __L640)
    p = 0
    f3@ while ((p < Nobjects!![POLLUT])) {
        i = UnitSystem
        if ((Pollut!![p]!!.units == COUNT)) {
            i = 2
        }
        sstrncpy(units, LoadUnitsWords!![i], 14L)
        R.fprintf(Frpt!!.file, __L423, units)
        p++
    }
    R.fprintf(Frpt!!.file, __L356, linkLine)
    p = 0
    f4@ while ((p < Nobjects!![POLLUT])) {
        R.fprintf(Frpt!!.file, __L169, pollutLine)
        p++
    }
    j = 0
    f5@ while ((j < Nobjects!![LINK])) {
        R.fprintf(Frpt!!.file, __L541, Link!![j]!!.ID)
        p = 0
        f6@ while ((p < Nobjects!![POLLUT])) {
            x = ((Link!![j]!!.totalLoad!![p] * 28.317) * Pollut!![p]!!.mcf)
            if ((Pollut!![p]!!.units == COUNT)) {
                x = ((if (((x) > 0.0)) Glibm.log10((x)) else (x)))
            }
            if ((x < 10000.0)) {
                R.fprintf(Frpt!!.file, __L427, x)
            } else {
                R.fprintf(Frpt!!.file, __L641, x)
            }
            p++
        }
        j++
    }
    report_writeLine((__L0))
}

