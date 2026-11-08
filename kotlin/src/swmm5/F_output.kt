// Generated from EPA SWMM 5.2.4 (output.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_output")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField val IDStartPos = LBox(0L)
@JvmField val InputStartPos = LBox(0L)
@JvmField val OutputStartPos = LBox(0L)
@JvmField var BytesPerPeriod: Long = 0L
@JvmField var NumSubcatchVars: Int = 0
@JvmField var NumNodeVars: Int = 0
@JvmField var NumLinkVars: Int = 0
@JvmField var NumSubcatch: Int = 0
@JvmField var NumNodes: Int = 0
@JvmField var NumLinks: Int = 0
@JvmField var NumPolluts: Int = 0
@JvmField var SysResults: FloatArray? = FloatArray(15)
@JvmField var AvgLinkResults: Array<TAvgResults?>? = null
@JvmField var AvgNodeResults: Array<TAvgResults?>? = null
@JvmField var output_Nsteps: Int = 0
@JvmField var SubcatchResults: FloatArray? = null
@JvmField var NodeResults: FloatArray? = null
@JvmField var LinkResults: FloatArray? = null

// output.c:121
fun output_open(): Int {
    var j: Int = 0
    var m: Int = 0
    val k = IBox(0)
    var x: Float = 0.0f
    val z = DBox(0.0)
    var numResults: Long = 0L
    output_openOutFile()
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    if ((IgnoreQuality != 0)) {
        NumPolluts = 0
    } else {
        NumPolluts = Nobjects!![POLLUT]
    }
    NumSubcatchVars = ((9 - 1) + NumPolluts)
    NumNodeVars = ((7 - 1) + NumPolluts)
    NumLinkVars = ((6 - 1) + NumPolluts)
    NumSubcatch = 0
    NumNodes = 0
    NumLinks = 0
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        if ((Subcatch!![j]!!.rptFlag != 0)) {
            NumSubcatch++
        }
        j++
    }
    j = 0
    f2@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.rptFlag != 0)) {
            NumNodes++
        }
        j++
    }
    j = 0
    f3@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.rptFlag != 0)) {
            NumLinks++
        }
        j++
    }
    numResults = ((((((NumSubcatch).toLong() * (NumSubcatchVars).toLong())) + (((NumNodes).toLong() * (NumNodeVars).toLong()))) + (((NumLinks).toLong() * (NumLinkVars).toLong()))) + 15L)
    BytesPerPeriod = (8L + ((numResults * 4L)))
    Nperiods = 0L
    SubcatchResults = (null)
    NodeResults = (null)
    LinkResults = (null)
    SubcatchResults = FloatArray(((NumSubcatchVars).toLong()).toInt())
    NodeResults = FloatArray(((NumNodeVars).toLong()).toInt())
    LinkResults = FloatArray(((NumLinkVars).toLong()).toInt())
    if ((((!(SubcatchResults != null)) || (!(NodeResults != null))) || (!(LinkResults != null)))) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
        return ErrorCode
    }
    AvgNodeResults = (null)
    AvgLinkResults = (null)
    if (((RptFlags!!.averages != 0) && (!(output_openAvgResults() != 0)))) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
        return ErrorCode
    }
    R.fseek(Fout!!.file, 0L, 0)
    k.v = 516114522
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = 52004
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = FlowUnits
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NumSubcatch
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NumNodes
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NumLinks
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NumPolluts
    R.fwrite1(k.v, 4, Fout!!.file)
    IDStartPos.v = R.ftell(Fout!!.file)
    j = 0
    f4@ while ((j < Nobjects!![SUBCATCH])) {
        if ((Subcatch!![j]!!.rptFlag != 0)) {
            output_saveID(Subcatch!![j]!!.ID, Fout!!.file)
        }
        j++
    }
    j = 0
    f5@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.rptFlag != 0)) {
            output_saveID(Node!![j]!!.ID, Fout!!.file)
        }
        j++
    }
    j = 0
    f6@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.rptFlag != 0)) {
            output_saveID(Link!![j]!!.ID, Fout!!.file)
        }
        j++
    }
    j = 0
    f7@ while ((j < NumPolluts)) {
        output_saveID(Pollut!![j]!!.ID, Fout!!.file)
        j++
    }
    j = 0
    f8@ while ((j < NumPolluts)) {
        k.v = Pollut!![j]!!.units
        R.fwrite1(k.v, 4, Fout!!.file)
        j++
    }
    InputStartPos.v = R.ftell(Fout!!.file)
    k.v = 1
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_AREA
    R.fwrite1(k.v, 4, Fout!!.file)
    j = 0
    f9@ while ((j < Nobjects!![SUBCATCH])) {
        if (!(Subcatch!![j]!!.rptFlag != 0)) {
            j++
            continue@f9
        }
        SubcatchResults!![0] = (((Subcatch!![j]!!.area * UCF(LANDAREA)))).toFloat()
        R.fwrite(SubcatchResults, 0, (1L).toInt(), Fout!!.file)
        j++
    }
    k.v = 3
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_TYPE_CODE
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_INVERT
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_MAX_DEPTH
    R.fwrite1(k.v, 4, Fout!!.file)
    j = 0
    f10@ while ((j < Nobjects!![NODE])) {
        if (!(Node!![j]!!.rptFlag != 0)) {
            j++
            continue@f10
        }
        k.v = Node!![j]!!.type
        NodeResults!![0] = (((Node!![j]!!.invertElev * UCF(LENGTH)))).toFloat()
        NodeResults!![1] = (((Node!![j]!!.fullDepth * UCF(LENGTH)))).toFloat()
        R.fwrite1(k.v, 4, Fout!!.file)
        R.fwrite(NodeResults, 0, (2L).toInt(), Fout!!.file)
        j++
    }
    k.v = 5
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_TYPE_CODE
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_OFFSET
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_OFFSET
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_MAX_DEPTH
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = INPUT_LENGTH
    R.fwrite1(k.v, 4, Fout!!.file)
    j = 0
    f11@ while ((j < Nobjects!![LINK])) {
        if (!(Link!![j]!!.rptFlag != 0)) {
            j++
            continue@f11
        }
        k.v = Link!![j]!!.type
        if ((k.v == PUMP)) {
            m = 0
            f12@ while ((m < 4)) {
                LinkResults!![m] = 0.0f
                m++
            }
        } else {
            LinkResults!![0] = (((Link!![j]!!.offset1 * UCF(LENGTH)))).toFloat()
            LinkResults!![1] = (((Link!![j]!!.offset2 * UCF(LENGTH)))).toFloat()
            if ((Link!![j]!!.direction < 0)) {
                x = LinkResults!![0]
                LinkResults!![0] = LinkResults!![1]
                LinkResults!![1] = x
            }
            if ((k.v == OUTLET)) {
                LinkResults!![2] = 0.0f
            } else {
                LinkResults!![2] = (((Link!![j]!!.xsect!!.yFull * UCF(LENGTH)))).toFloat()
            }
            if ((k.v == CONDUIT)) {
                m = Link!![j]!!.subIndex
                LinkResults!![3] = (((Conduit!![m]!!.length * UCF(LENGTH)))).toFloat()
            } else {
                LinkResults!![3] = 0.0f
            }
        }
        R.fwrite1(k.v, 4, Fout!!.file)
        R.fwrite(LinkResults, 0, (4L).toInt(), Fout!!.file)
        j++
    }
    k.v = NumSubcatchVars
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_RAINFALL
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_SNOWDEPTH
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_EVAP
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_INFIL
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_RUNOFF
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_GW_FLOW
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_GW_ELEV
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = SUBCATCH_SOIL_MOIST
    R.fwrite1(k.v, 4, Fout!!.file)
    j = 0
    f13@ while ((j < NumPolluts)) {
        k.v = (SUBCATCH_WASHOFF + j)
        R.fwrite1(k.v, 4, Fout!!.file)
        j++
    }
    k.v = NumNodeVars
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NODE_DEPTH
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NODE_HEAD
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NODE_VOLUME
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NODE_LATFLOW
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NODE_INFLOW
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = NODE_OVERFLOW
    R.fwrite1(k.v, 4, Fout!!.file)
    j = 0
    f14@ while ((j < NumPolluts)) {
        k.v = (NODE_QUAL + j)
        R.fwrite1(k.v, 4, Fout!!.file)
        j++
    }
    k.v = NumLinkVars
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = LINK_FLOW
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = LINK_DEPTH
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = LINK_VELOCITY
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = LINK_VOLUME
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = LINK_CAPACITY
    R.fwrite1(k.v, 4, Fout!!.file)
    j = 0
    f15@ while ((j < NumPolluts)) {
        k.v = (LINK_QUAL + j)
        R.fwrite1(k.v, 4, Fout!!.file)
        j++
    }
    k.v = 15
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = 0
    f16@ while ((k.v < 15)) {
        R.fwrite1(k.v, 4, Fout!!.file)
        k.v++
    }
    z.v = ((ReportStep).toDouble() / 86400.0)
    if (((StartDateTime + z.v) > ReportStart)) {
        z.v = StartDateTime
    } else {
        z.v = (floor((((ReportStart - StartDateTime)) / z.v)) - 1.0)
        z.v = (StartDateTime + ((z.v * (ReportStep).toDouble()) / 86400.0))
    }
    R.fwrite1(z.v, 8, Fout!!.file)
    k.v = ReportStep
    if ((R.fwrite1(k.v, 4, Fout!!.file) < 1L)) {
        report_writeErrorMsg(ERR_OUT_WRITE, __L0)
        return ErrorCode
    }
    OutputStartPos.v = R.ftell(Fout!!.file)
    return ErrorCode
}

// output.c:427
fun output_openOutFile() {
    if ((Fout!!.file != null)) {
        R.fclose(Fout!!.file)
    } else if ((R.strlen(Fout!!.name) != 0L)) {
        Fout!!.mode = SAVE_FILE
    } else {
        Fout!!.mode = SCRATCH_FILE
        getTempFileName(Fout!!.name)
    }
    if ((((R.fopen(Fout!!.name, __L166)).also { __v -> Fout!!.file = __v }) == null)) {
        writecon(__L318)
        ErrorCode = ERR_OUT_FILE
    }
}

// output.c:457
fun output_saveResults(reportTime: Double) {
    var i: Int = 0
    var reportDate: Double = getDateTime(reportTime)
    val date = DBox(0.0)
    if ((reportDate < ReportStart)) {
        return
    }
    i = 0
    f1@ while ((i < 15)) {
        SysResults!![i] = 0.0f
        i++
    }
    date.v = reportDate
    R.fwrite1(date.v, 8, Fout!!.file)
    if ((Nobjects!![SUBCATCH] > 0)) {
        output_saveSubcatchResults(reportTime, Fout!!.file)
    }
    if ((RptFlags!!.averages != 0)) {
        output_saveAvgResults(Fout!!.file)
    } else {
        if ((Nobjects!![NODE] > 0)) {
            output_saveNodeResults(reportTime, Fout!!.file)
        }
        if ((Nobjects!![LINK] > 0)) {
            output_saveLinkResults(reportTime, Fout!!.file)
        }
    }
    SysResults!![SYS_FLOODING] = (((StepFlowTotals!!.flooding * UCF(FLOW)))).toFloat()
    SysResults!![SYS_OUTFLOW] = (((StepFlowTotals!!.outflow * UCF(FLOW)))).toFloat()
    SysResults!![SYS_DWFLOW] = (((StepFlowTotals!!.dwInflow * UCF(FLOW)))).toFloat()
    SysResults!![SYS_GWFLOW] = (((StepFlowTotals!!.gwInflow * UCF(FLOW)))).toFloat()
    SysResults!![SYS_IIFLOW] = (((StepFlowTotals!!.iiInflow * UCF(FLOW)))).toFloat()
    SysResults!![SYS_EXFLOW] = (((StepFlowTotals!!.exInflow * UCF(FLOW)))).toFloat()
    SysResults!![SYS_INFLOW] = ((((SysResults!![SYS_RUNOFF] + SysResults!![SYS_DWFLOW]) + SysResults!![SYS_GWFLOW]) + SysResults!![SYS_IIFLOW]) + SysResults!![SYS_EXFLOW])
    R.fwrite(SysResults, 0, (15L).toInt(), Fout!!.file)
    if (((Foutflows!!.mode == SAVE_FILE) && (!(IgnoreRouting != 0)))) {
        iface_saveOutletResults(reportDate, Foutflows!!.file)
    }
    Nperiods++
}

// output.c:515
fun output_end() {
    val k = IBox(0)
    R.fwrite1(IDStartPos.v, 4, Fout!!.file)
    R.fwrite1(InputStartPos.v, 4, Fout!!.file)
    R.fwrite1(OutputStartPos.v, 4, Fout!!.file)
    k.v = (Nperiods).toInt()
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = ErrorCode
    R.fwrite1(k.v, 4, Fout!!.file)
    k.v = 516114522
    if ((R.fwrite1(k.v, 4, Fout!!.file) < 1L)) {
        report_writeErrorMsg(ERR_OUT_WRITE, __L0)
    }
}

// output.c:539
fun output_close() {
    if ((SubcatchResults != null)) {
        R.free(SubcatchResults)
        SubcatchResults = (null)
    }
    if ((NodeResults != null)) {
        R.free(NodeResults)
        NodeResults = (null)
    }
    if ((LinkResults != null)) {
        R.free(LinkResults)
        LinkResults = (null)
    }
    output_closeAvgResults()
}

// output.c:554
fun output_saveID(id: CPtr?, file: CFile?) {
    val n = IBox((R.strlen(id)).toInt())
    R.fwrite1(n.v, 4, file)
    R.fwrite(id, 0, ((n.v).toLong()).toInt(), file)
}

// output.c:569
fun output_saveSubcatchResults(reportTime: Double, file: CFile?) {
    var j: Int = 0
    var f: Double = 0.0
    var area: Double = 0.0
    var totalArea: Float = 0.0f
    var reportDate: Double = getDateTime(reportTime)
    j = 0
    f1@ while ((j < Nobjects!![GAGE])) {
        gage_setReportRainfall(j, reportDate)
        j++
    }
    f = (((reportTime - OldRunoffTime)) / ((NewRunoffTime - OldRunoffTime)))
    j = 0
    f2@ while ((j < Nobjects!![SUBCATCH])) {
        subcatch_getResults(j, f, SubcatchResults)
        if ((Subcatch!![j]!!.rptFlag != 0)) {
            R.fwrite(SubcatchResults, 0, ((NumSubcatchVars).toLong()).toInt(), file)
        }
        area = (Subcatch!![j]!!.area * UCF(LANDAREA))
        totalArea += ((area).toFloat())
        SysResults!![SYS_RAINFALL] += (((((SubcatchResults!![SUBCATCH_RAINFALL]).toDouble() * area))).toFloat())
        SysResults!![SYS_SNOWDEPTH] += (((((SubcatchResults!![SUBCATCH_SNOWDEPTH]).toDouble() * area))).toFloat())
        SysResults!![SYS_EVAP] += (((((SubcatchResults!![SUBCATCH_EVAP]).toDouble() * area))).toFloat())
        if ((Subcatch!![j]!!.groundwater != null)) {
            SysResults!![SYS_EVAP] += (((((Subcatch!![j]!!.groundwater!!.evapLoss * UCF(EVAPRATE)) * area))).toFloat())
        }
        SysResults!![SYS_INFIL] += (((((SubcatchResults!![SUBCATCH_INFIL]).toDouble() * area))).toFloat())
        SysResults!![SYS_RUNOFF] += SubcatchResults!![SUBCATCH_RUNOFF]
        j++
    }
    if (((totalArea).toDouble() > 0.0)) {
        SysResults!![SYS_EVAP] /= totalArea
        SysResults!![SYS_RAINFALL] /= totalArea
        SysResults!![SYS_SNOWDEPTH] /= totalArea
        SysResults!![SYS_INFIL] /= totalArea
    }
    if ((UnitSystem == SI)) {
        f = (((5.0 / 9.0)) * ((Temp!!.ta - 32.0)))
    } else {
        f = Temp!!.ta
    }
    SysResults!![SYS_TEMPERATURE] = (f).toFloat()
    f = (Evap!!.rate * UCF(EVAPRATE))
    SysResults!![SYS_PET] = (f).toFloat()
}

// output.c:636
fun output_saveNodeResults(reportTime: Double, file: CFile?) {
    var j: Int = 0
    var f: Double = (((reportTime - OldRoutingTime)) / ((NewRoutingTime - OldRoutingTime)))
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        node_getResults(j, f, NodeResults)
        if ((Node!![j]!!.rptFlag != 0)) {
            R.fwrite(NodeResults, 0, ((NumNodeVars).toLong()).toInt(), file)
        }
        stats_updateMaxNodeDepth(j, (NodeResults!![NODE_DEPTH]).toDouble())
        SysResults!![SYS_STORAGE] += NodeResults!![NODE_VOLUME]
        j++
    }
}

// output.c:666
fun output_saveLinkResults(reportTime: Double, file: CFile?) {
    var j: Int = 0
    var f: Double = 0.0
    var z: Double = 0.0
    f = (((reportTime - OldRoutingTime)) / ((NewRoutingTime - OldRoutingTime)))
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.rptFlag != 0)) {
            link_getResults(j, f, LinkResults)
            R.fwrite(LinkResults, 0, ((NumLinkVars).toLong()).toInt(), file)
        }
        z = ((((((1.0 - f)) * Link!![j]!!.oldVolume) + (f * Link!![j]!!.newVolume))) * UCF(VOLUME))
        SysResults!![SYS_STORAGE] += ((z).toFloat())
        j++
    }
}

// output.c:699
fun output_readDateTime(period: Long, days: DRef?) {
    var p: Long = period
    var bytePos: Long = (OutputStartPos.v + (((p - 1L)) * BytesPerPeriod))
    R.fseek(Fout!!.file, bytePos, 0)
    days!!.set(((-693594)).toDouble())
    R.fread1(days, 8, Fout!!.file)
}

// output.c:716
fun output_readSubcatchResults(period: Long, index: Int) {
    var offset: Long = ((index * NumSubcatchVars)).toLong()
    var p: Long = period
    var bytePos: Long = (((OutputStartPos.v + (((p - 1L)) * BytesPerPeriod)) + 8L) + (offset * 4L))
    R.fseek(Fout!!.file, bytePos, 0)
    R.fread(SubcatchResults, 0, ((NumSubcatchVars).toLong()).toInt(), Fout!!.file)
}

// output.c:735
fun output_readNodeResults(period: Long, index: Int) {
    var offset: Long = (((NumSubcatch * NumSubcatchVars) + (index * NumNodeVars))).toLong()
    var p: Long = period
    var bytePos: Long = (((OutputStartPos.v + (((p - 1L)) * BytesPerPeriod)) + 8L) + (offset * 4L))
    R.fseek(Fout!!.file, bytePos, 0)
    R.fread(NodeResults, 0, ((NumNodeVars).toLong()).toInt(), Fout!!.file)
}

// output.c:753
fun output_readLinkResults(period: Long, index: Int) {
    var offset: Long = (((((NumSubcatch * NumSubcatchVars) + (NumNodes * NumNodeVars)) + (index * NumLinkVars)))).toLong()
    var p: Long = period
    var bytePos: Long = (((OutputStartPos.v + (((p - 1L)) * BytesPerPeriod)) + 8L) + (offset * 4L))
    R.fseek(Fout!!.file, bytePos, 0)
    R.fread(LinkResults, 0, ((NumLinkVars).toLong()).toInt(), Fout!!.file)
    R.fread(SysResults, 0, (15L).toInt(), Fout!!.file)
}

// output.c:774
fun output_openAvgResults(): Int {
    var i: Int = 0
    AvgNodeResults = Array<TAvgResults?>(((NumNodes).toLong()).toInt()) { TAvgResults() }
    if ((AvgNodeResults == null)) {
        return 0
    }
    i = 0
    f1@ while ((i < NumNodes)) {
        AvgNodeResults!![i]!!.xAvg = (null)
        i++
    }
    AvgLinkResults = Array<TAvgResults?>(((NumLinks).toLong()).toInt()) { TAvgResults() }
    if ((AvgLinkResults == null)) {
        output_closeAvgResults()
        return 0
    }
    i = 0
    f2@ while ((i < NumLinks)) {
        AvgLinkResults!![i]!!.xAvg = (null)
        i++
    }
    i = 0
    f3@ while ((i < NumNodes)) {
        AvgNodeResults!![i]!!.xAvg = FloatArray(((NumNodeVars).toLong()).toInt())
        if ((AvgNodeResults!![i]!!.xAvg == null)) {
            output_closeAvgResults()
            return 0
        }
        i++
    }
    i = 0
    f4@ while ((i < NumLinks)) {
        AvgLinkResults!![i]!!.xAvg = FloatArray(((NumLinkVars).toLong()).toInt())
        if ((AvgLinkResults!![i]!!.xAvg == null)) {
            output_closeAvgResults()
            return 0
        }
        i++
    }
    return 1
}

// output.c:820
fun output_closeAvgResults() {
    var i: Int = 0
    if ((AvgNodeResults != null)) {
        i = 0
        f1@ while ((i < NumNodes)) {
            if ((AvgNodeResults!![i]!!.xAvg != null)) {
                R.free(AvgNodeResults!![i]!!.xAvg)
                AvgNodeResults!![i]!!.xAvg = (null)
            }
            i++
        }
        if ((AvgNodeResults != null)) {
            R.free(AvgNodeResults)
            AvgNodeResults = (null)
        }
    }
    if ((AvgLinkResults != null)) {
        i = 0
        f2@ while ((i < NumLinks)) {
            if ((AvgLinkResults!![i]!!.xAvg != null)) {
                R.free(AvgLinkResults!![i]!!.xAvg)
                AvgLinkResults!![i]!!.xAvg = (null)
            }
            i++
        }
        if ((AvgLinkResults != null)) {
            R.free(AvgLinkResults)
            AvgLinkResults = (null)
        }
    }
}

// output.c:839
fun output_initAvgResults() {
    var i: Int = 0
    var j: Int = 0
    output_Nsteps = 0
    i = 0
    f1@ while ((i < NumNodes)) {
        j = 0
        f2@ while ((j < NumNodeVars)) {
            AvgNodeResults!![i]!!.xAvg!![j] = (0.0).toFloat()
            j++
        }
        i++
    }
    i = 0
    f3@ while ((i < NumLinks)) {
        j = 0
        f4@ while ((j < NumLinkVars)) {
            AvgLinkResults!![i]!!.xAvg!![j] = (0.0).toFloat()
            j++
        }
        i++
    }
}

// output.c:857
fun output_updateAvgResults() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var sign: Int = 0
    k = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if (!(Node!![i]!!.rptFlag != 0)) {
            i++
            continue@f1
        }
        node_getResults(i, 1.0, NodeResults)
        j = 0
        f2@ while ((j < NumNodeVars)) {
            AvgNodeResults!![k]!!.xAvg!![j] += NodeResults!![j]
            j++
        }
        k++
        i++
    }
    k = 0
    i = 0
    f3@ while ((i < Nobjects!![LINK])) {
        if (!(Link!![i]!!.rptFlag != 0)) {
            i++
            continue@f3
        }
        link_getResults(i, 1.0, LinkResults)
        sign = ((if ((((LinkResults!![LINK_FLOW]) < 0.0f))) ((-1)) else (1)))
        j = 0
        f4@ while ((j < NumLinkVars)) {
            if ((j == LINK_CAPACITY)) {
                if ((Link!![i]!!.type == CONDUIT)) {
                    AvgLinkResults!![k]!!.xAvg!![j] += LinkResults!![j]
                } else {
                    AvgLinkResults!![k]!!.xAvg!![j] = (LinkResults!![j] * (((output_Nsteps + 1))).toFloat())
                }
            } else {
                AvgLinkResults!![k]!!.xAvg!![j] += LinkResults!![j]
            }
            j++
        }
        k++
        i++
    }
    output_Nsteps++
}

// output.c:911
fun output_saveAvgResults(file: CFile?) {
    var i: Int = 0
    var j: Int = 0
    i = 0
    f1@ while ((i < NumNodes)) {
        j = 0
        f2@ while ((j < NumNodeVars)) {
            NodeResults!![j] = (AvgNodeResults!![i]!!.xAvg!![j] / (output_Nsteps).toFloat())
            j++
        }
        R.fwrite(NodeResults, 0, ((NumNodeVars).toLong()).toInt(), file)
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![NODE])) {
        stats_updateMaxNodeDepth(i, (Node!![i]!!.newDepth * UCF(LENGTH)))
        SysResults!![SYS_STORAGE] += ((((Node!![i]!!.newVolume * UCF(VOLUME)))).toFloat())
        i++
    }
    i = 0
    f4@ while ((i < NumLinks)) {
        j = 0
        f5@ while ((j < NumLinkVars)) {
            LinkResults!![j] = (AvgLinkResults!![i]!!.xAvg!![j] / (output_Nsteps).toFloat())
            j++
        }
        R.fwrite(LinkResults, 0, ((NumLinkVars).toLong()).toInt(), file)
        i++
    }
    i = 0
    f6@ while ((i < Nobjects!![LINK])) {
        SysResults!![SYS_STORAGE] += ((((Link!![i]!!.newVolume * UCF(VOLUME)))).toFloat())
        i++
    }
    output_initAvgResults()
}

