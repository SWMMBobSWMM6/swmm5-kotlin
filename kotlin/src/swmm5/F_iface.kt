// Generated from EPA SWMM 5.2.4 (iface.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_iface")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var IfaceFlowUnits: Int = 0
@JvmField val IfaceStep = IBox(0)
@JvmField val NumIfacePolluts = IBox(0)
@JvmField var IfacePolluts: IntArray? = null
@JvmField val NumIfaceNodes = IBox(0)
@JvmField var IfaceNodes: IntArray? = null
@JvmField var OldIfaceValues: Array<DoubleArray?>? = null
@JvmField var NewIfaceValues: Array<DoubleArray?>? = null
@JvmField var IfaceFrac: Double = 0.0
@JvmField var OldIfaceDate: Double = 0.0
@JvmField var NewIfaceDate: Double = 0.0

// iface.c:66
fun iface_readFileParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var k: Int = 0
    var j: Int = 0
    var fname: CPtr? = R.buf(260)
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    k = (findmatch(tok!![0], FileModeWords)).toByte().toInt()
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![0])
    }
    j = findmatch(tok!![1], FileTypeWords)
    if ((j < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    if ((ntoks < 3)) {
        return 0
    }
    sstrncpy(fname, tok!![2], 259L)
    when (j) {
        RAINFALL_FILE -> {
            Frain!!.mode = k
            sstrncpy(Frain!!.name, addAbsolutePath(fname), 259L)
        }
        RUNOFF_FILE -> {
            Frunoff!!.mode = k
            sstrncpy(Frunoff!!.name, addAbsolutePath(fname), 259L)
        }
        HOTSTART_FILE -> {
            if ((k == USE_FILE)) {
                Fhotstart1!!.mode = k
                sstrncpy(Fhotstart1!!.name, addAbsolutePath(fname), 259L)
            } else if ((k == SAVE_FILE)) {
                Fhotstart2!!.mode = k
                sstrncpy(Fhotstart2!!.name, addAbsolutePath(fname), 259L)
            }
        }
        RDII_FILE -> {
            Frdii!!.mode = k
            sstrncpy(Frdii!!.name, fname, 259L)
        }
        INFLOWS_FILE -> {
            if ((k != USE_FILE)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            Finflows!!.mode = k
            sstrncpy(Finflows!!.name, addAbsolutePath(fname), 259L)
        }
        OUTFLOWS_FILE -> {
            if ((k != SAVE_FILE)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            Foutflows!!.mode = k
            sstrncpy(Foutflows!!.name, addAbsolutePath(fname), 259L)
        }
    }
    return 0
}

// iface.c:138
fun iface_openRoutingFiles() {
    NumIfacePolluts.v = 0
    IfacePolluts = (null)
    NumIfaceNodes.v = 0
    IfaceNodes = (null)
    OldIfaceValues = (null)
    NewIfaceValues = (null)
    if (((Foutflows!!.mode != NO_FILE) && (Finflows!!.mode != NO_FILE))) {
        if ((strcomp(Foutflows!!.name, Finflows!!.name) != 0)) {
            report_writeErrorMsg(ERR_ROUTING_FILE_NAMES, __L0)
            return
        }
    }
    if ((Foutflows!!.mode == SAVE_FILE)) {
        openFileForOutput()
    }
    if ((Finflows!!.mode == USE_FILE)) {
        openFileForInput()
    }
}

// iface.c:170
fun iface_closeRoutingFiles() {
    if ((IfacePolluts != null)) {
        R.free(IfacePolluts)
        IfacePolluts = (null)
    }
    if ((IfaceNodes != null)) {
        R.free(IfaceNodes)
        IfaceNodes = (null)
    }
    if ((OldIfaceValues != null)) {
        project_freeMatrix(OldIfaceValues)
    }
    if ((NewIfaceValues != null)) {
        project_freeMatrix(NewIfaceValues)
    }
    if ((Finflows!!.file != null)) {
        R.fclose(Finflows!!.file)
    }
    if ((Foutflows!!.file != null)) {
        R.fclose(Foutflows!!.file)
    }
}

// iface.c:187
fun iface_getNumIfaceNodes(currentDate: Double): Int {
    if ((OldIfaceDate > currentDate)) {
        return 0
    }
    w1@ while (((NewIfaceDate < currentDate) && (NewIfaceDate != ((-693594)).toDouble()))) {
        setOldIfaceValues()
        readNewIfaceValues()
    }
    if ((NewIfaceDate == ((-693594)).toDouble())) {
        return 0
    }
    IfaceFrac = (((currentDate - OldIfaceDate)) / ((NewIfaceDate - OldIfaceDate)))
    IfaceFrac = ((if ((((0.0) >= (IfaceFrac)))) (0.0) else (IfaceFrac)))
    IfaceFrac = ((if ((((IfaceFrac) <= (1.0)))) (IfaceFrac) else (1.0)))
    return NumIfaceNodes.v
}

// iface.c:219
fun iface_getIfaceNode(index: Int): Int {
    if (((index >= 0) && (index < NumIfaceNodes.v))) {
        return IfaceNodes!![index]
    } else {
        return (-1)
    }
    throw IllegalStateException("iface_getIfaceNode: no return")
}

// iface.c:232
fun iface_getIfaceFlow(index: Int): Double {
    var q1: Double = 0.0
    var q2: Double = 0.0
    if (((index >= 0) && (index < NumIfaceNodes.v))) {
        q1 = OldIfaceValues!![index]!![0]
        q2 = NewIfaceValues!![index]!![0]
        return ((((1.0 - IfaceFrac)) * q1) + (IfaceFrac * q2))
    } else {
        return 0.0
    }
    throw IllegalStateException("iface_getIfaceFlow: no return")
}

// iface.c:253
fun iface_getIfaceQual(index: Int, pollut: Int): Double {
    var j: Int = 0
    var c1: Double = 0.0
    var c2: Double = 0.0
    if (((index >= 0) && (index < NumIfaceNodes.v))) {
        j = IfacePolluts!![pollut]
        if ((j < 0)) {
            return 0.0
        }
        c1 = OldIfaceValues!![index]!![(j + 1)]
        c2 = NewIfaceValues!![index]!![(j + 1)]
        return ((((1.0 - IfaceFrac)) * c1) + (IfaceFrac * c2))
    } else {
        return 0.0
    }
    throw IllegalStateException("iface_getIfaceQual: no return")
}

// iface.c:281
fun iface_saveOutletResults(reportDate: Double, file: CFile?) {
    var i: Int = 0
    var p: Int = 0
    val yr = IBox(0)
    val mon = IBox(0)
    val day = IBox(0)
    val hr = IBox(0)
    val min = IBox(0)
    val sec = IBox(0)
    var theDate: CPtr? = R.buf(26)
    datetime_decodeDate(reportDate, yr, mon, day)
    datetime_decodeTime(reportDate, hr, min, sec)
    R.snprintf(theDate, (26L).toInt(), __L167, yr.v, mon.v, day.v, hr.v, min.v, sec.v)
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if (!(isOutletNode(i) != 0)) {
            i++
            continue@f1
        }
        R.fprintf(file, __L168, Node!![i]!!.ID)
        R.fprintf(file, __L169, theDate)
        R.fprintf(file, __L170, (Node!![i]!!.inflow * UCF(FLOW)))
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            R.fprintf(file, __L170, Node!![i]!!.newQual!![p])
            p++
        }
        i++
    }
}

// iface.c:313
fun openFileForOutput() {
    var i: Int = 0
    var n: Int = 0
    Foutflows!!.file = R.fopen(Foutflows!!.name, __L171)
    if ((Foutflows!!.file == null)) {
        report_writeErrorMsg(ERR_ROUTING_FILE_OPEN, Foutflows!!.name)
        return
    }
    R.fprintf(Foutflows!!.file, __L172)
    R.fprintf(Foutflows!!.file, __L173, Title!![0])
    R.fprintf(Foutflows!!.file, __L174, ReportStep)
    R.fprintf(Foutflows!!.file, __L175, (Nobjects!![POLLUT] + 1))
    R.fprintf(Foutflows!!.file, __L176, FlowUnitWords!![FlowUnits])
    i = 0
    f1@ while ((i < Nobjects!![POLLUT])) {
        R.fprintf(Foutflows!!.file, __L177, Pollut!![i]!!.ID, QualUnitsWords!![Pollut!![i]!!.units])
        i++
    }
    n = 0
    i = 0
    f2@ while ((i < Nobjects!![NODE])) {
        if ((isOutletNode(i) != 0)) {
            n++
        }
        i++
    }
    R.fprintf(Foutflows!!.file, __L178, n)
    i = 0
    f3@ while ((i < Nobjects!![NODE])) {
        if ((isOutletNode(i) != 0)) {
            R.fprintf(Foutflows!!.file, __L173, Node!![i]!!.ID)
        }
        i++
    }
    R.fprintf(Foutflows!!.file, __L179)
    i = 0
    f4@ while ((i < Nobjects!![POLLUT])) {
        R.fprintf(Foutflows!!.file, __L180, Pollut!![i]!!.ID)
        i++
    }
    if ((ReportStart == StartDateTime)) {
        iface_saveOutletResults(ReportStart, Foutflows!!.file)
    }
}

// iface.c:377
fun openFileForInput() {
    var err: Int = 0
    var line: CPtr? = R.buf(1025)
    var s: CPtr? = R.buf(1025)
    Finflows!!.file = R.fopen(Finflows!!.name, __L13)
    if ((Finflows!!.file == null)) {
        report_writeErrorMsg(ERR_ROUTING_FILE_OPEN, Finflows!!.name)
        return
    }
    R.fgets(line, 1024, Finflows!!.file)
    if (((!(R.sscanf(line, __L169, s) != 0)) || (!(strcomp(s, __L181) != 0)))) {
        report_writeErrorMsg(ERR_ROUTING_FILE_FORMAT, Finflows!!.name)
        return
    }
    R.fgets(line, 1024, Finflows!!.file)
    IfaceStep.v = 0
    R.fgets(line, 1024, Finflows!!.file)
    if (((!(R.sscanf(line, __L182, IfaceStep) != 0)) || (IfaceStep.v <= 0))) {
        report_writeErrorMsg(ERR_ROUTING_FILE_FORMAT, Finflows!!.name)
        return
    }
    err = getIfaceFilePolluts()
    if ((err > 0)) {
        report_writeErrorMsg(err, Finflows!!.name)
        return
    }
    err = getIfaceFileNodes()
    if ((err > 0)) {
        report_writeErrorMsg(err, Finflows!!.name)
        return
    }
    OldIfaceValues = project_createMatrix(NumIfaceNodes.v, (1 + NumIfacePolluts.v))
    NewIfaceValues = project_createMatrix(NumIfaceNodes.v, (1 + NumIfacePolluts.v))
    if (((OldIfaceValues == null) || (NewIfaceValues == null))) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
        return
    }
    readNewIfaceValues()
    OldIfaceDate = NewIfaceDate
}

// iface.c:450
fun getIfaceFilePolluts(): Int {
    var i: Int = 0
    var j: Int = 0
    var line: CPtr? = R.buf(1025)
    var s1: CPtr? = R.buf(1025)
    var s2: CPtr? = R.buf(1025)
    R.fgets(line, 1024, Finflows!!.file)
    NumIfacePolluts.v = (-1)
    if ((R.sscanf(line, __L182, NumIfacePolluts) != 0)) {
        NumIfacePolluts.v--
    }
    if ((NumIfacePolluts.v < 0)) {
        return ERR_ROUTING_FILE_FORMAT
    }
    R.fgets(line, 1024, Finflows!!.file)
    if ((R.sscanf(line, __L44, s1, s2) < 2)) {
        return ERR_ROUTING_FILE_FORMAT
    }
    if (!(strcomp(s1, __L183) != 0)) {
        return ERR_ROUTING_FILE_FORMAT
    }
    IfaceFlowUnits = findmatch(s2, FlowUnitWords)
    if ((IfaceFlowUnits < 0)) {
        return ERR_ROUTING_FILE_FORMAT
    }
    if ((Nobjects!![POLLUT] > 0)) {
        IfacePolluts = IntArray(((Nobjects!![POLLUT]).toLong()).toInt())
        if (!(IfacePolluts != null)) {
            return ERR_MEMORY
        }
        i = 0
        f1@ while ((i < Nobjects!![POLLUT])) {
            IfacePolluts!![i] = (-1)
            i++
        }
    }
    if (((NumIfacePolluts.v > 0) && (Nobjects!![POLLUT] > 0))) {
        i = 0
        f2@ while ((i < NumIfacePolluts.v)) {
            if ((R.feof(Finflows!!.file) != 0)) {
                return ERR_ROUTING_FILE_FORMAT
            }
            R.fgets(line, 1024, Finflows!!.file)
            if ((R.sscanf(line, __L44, s1, s2) < 2)) {
                return ERR_ROUTING_FILE_FORMAT
            }
            j = project_findObject(POLLUT, s1)
            if ((j < 0)) {
                i++
                continue@f2
            }
            if (!(strcomp(s2, QualUnitsWords!![Pollut!![j]!!.units]) != 0)) {
                return ERR_ROUTING_FILE_NOMATCH
            }
            IfacePolluts!![j] = i
            i++
        }
    }
    return 0
}

// iface.c:510
fun getIfaceFileNodes(): Int {
    var i: Int = 0
    var line: CPtr? = R.buf(1025)
    var s: CPtr? = R.buf(1025)
    R.fgets(line, 1024, Finflows!!.file)
    if (((!(R.sscanf(line, __L182, NumIfaceNodes) != 0)) || (NumIfaceNodes.v <= 0))) {
        return ERR_ROUTING_FILE_FORMAT
    }
    IfaceNodes = IntArray(((NumIfaceNodes.v).toLong()).toInt())
    if (!(IfaceNodes != null)) {
        return ERR_MEMORY
    }
    i = 0
    f1@ while ((i < NumIfaceNodes.v)) {
        if ((R.feof(Finflows!!.file) != 0)) {
            return ERR_ROUTING_FILE_FORMAT
        }
        IfaceNodes!![i] = 0
        R.fgets(line, 1024, Finflows!!.file)
        if (!(R.sscanf(line, __L169, s) != 0)) {
            return ERR_ROUTING_FILE_FORMAT
        }
        IfaceNodes!![i] = project_findObject(NODE, s)
        i++
    }
    if ((R.feof(Finflows!!.file) != 0)) {
        return ERR_ROUTING_FILE_FORMAT
    }
    R.fgets(line, 1024, Finflows!!.file)
    return 0
}

// iface.c:550
fun readNewIfaceValues() {
    var i: Int = 0
    var j: Int = 0
    var s: CPtr? = null
    var yr: Int = 0
    var mon: Int = 0
    var day: Int = 0
    var hr: Int = 0
    var min: Int = 0
    var sec: Int = 0
    var line: CPtr? = R.buf(1025)
    NewIfaceDate = ((-693594)).toDouble()
    i = 0
    f1@ while ((i < NumIfaceNodes.v)) {
        if ((R.feof(Finflows!!.file) != 0)) {
            return
        }
        R.fgets(line, 1024, Finflows!!.file)
        if (((R.strtok(line, __L184)) == null)) {
            return
        }
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        yr = R.atoi(s)
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        mon = R.atoi(s)
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        day = R.atoi(s)
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        hr = R.atoi(s)
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        min = R.atoi(s)
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        sec = R.atoi(s)
        s = R.strtok(null, __L184)
        if ((s == null)) {
            return
        }
        NewIfaceValues!![i]!![0] = (R.atof(s) / Qcf!![IfaceFlowUnits])
        j = 1
        f2@ while ((j <= NumIfacePolluts.v)) {
            s = R.strtok(null, __L184)
            if ((s == null)) {
                return
            }
            NewIfaceValues!![i]!![j] = R.atof(s)
            j++
        }
        i++
    }
    NewIfaceDate = (datetime_encodeDate(yr, mon, day) + datetime_encodeTime(hr, min, sec))
}

// iface.c:613
fun setOldIfaceValues() {
    var i: Int = 0
    var j: Int = 0
    OldIfaceDate = NewIfaceDate
    i = 0
    f1@ while ((i < NumIfaceNodes.v)) {
        j = 0
        f2@ while ((j < (NumIfacePolluts.v + 1))) {
            OldIfaceValues!![i]!![j] = NewIfaceValues!![i]!![j]
            j++
        }
        i++
    }
}

// iface.c:633
fun isOutletNode(i: Int): Int {
    if ((RouteModel == DW)) {
        return (if (((Node!![i]!!.type == OUTFALL))) 1 else 0)
    } else {
        return (if (((Node!![i]!!.degree == 0))) 1 else 0)
    }
    throw IllegalStateException("isOutletNode: no return")
}

