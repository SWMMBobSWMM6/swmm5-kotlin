// Generated from EPA SWMM 5.2.4 (rdii.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_rdii")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var ZERO_RDII: Double = 0.0001
@JvmField var FileStamp: CPtr? = R.bufFrom(__L352, 11)
@JvmField var UHGroup: Array<TUHGroup?>? = null
@JvmField val RdiiStep = IBox(0)
@JvmField val NumRdiiNodes = IBox(0)
@JvmField var RdiiNodeIndex: IntArray? = null
@JvmField var RdiiNodeFlow: FloatArray? = null
@JvmField var RdiiFlowUnits: Int = 0
@JvmField val RdiiStartDate = DBox(0.0)
@JvmField var RdiiEndDate: Double = 0.0
@JvmField var TotalRainVol: Double = 0.0
@JvmField var TotalRdiiVol: Double = 0.0
@JvmField var RdiiFileType: Int = 0

// rdii.c:152
fun rdii_readRdiiInflow(tok: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var k: Int = 0
    val a = DBox(0.0)
    var inflow: TRdiiInflow? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(NODE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = project_findObject(UNITHYD, tok!![1])
    if ((k < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    if (((!(getDouble(tok!![2], a) != 0)) || (a.v < 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![2])
    }
    inflow = Node!![j]!!.rdiiInflow
    if ((inflow == null)) {
        inflow = TRdiiInflow()
        if (!(inflow != null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
    }
    inflow!!.unitHyd = k
    inflow!!.area = (a.v / UCF(LANDAREA))
    Node!![j]!!.rdiiInflow = inflow
    return 0
}

// rdii.c:198
fun rdii_initUnitHyd(j: Int) {
    var i: Int = 0
    var m: Int = 0
    m = 0
    f1@ while ((m < 12)) {
        i = 0
        f2@ while ((i < 3)) {
            UnitHyd!![j]!!.iaMax!![m]!![i] = 0.0
            UnitHyd!![j]!!.iaRecov!![m]!![i] = 0.0
            UnitHyd!![j]!!.iaInit!![m]!![i] = 0.0
            UnitHyd!![j]!!.r!![m]!![i] = 0.0
            UnitHyd!![j]!!.tPeak!![m]!![i] = 0L
            UnitHyd!![j]!!.tBase!![m]!![i] = 0L
            i++
        }
        m++
    }
}

// rdii.c:224
fun rdii_readUnitHydParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    var g: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    j = project_findObject(UNITHYD, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if ((UnitHyd!![j]!!.ID == null)) {
        UnitHyd!![j]!!.ID = project_findID(UNITHYD, tok!![0])
    }
    if ((ntoks == 2)) {
        g = project_findObject(GAGE, tok!![1])
        if ((g < 0)) {
            return error_setInpError(ERR_NAME, tok!![1])
        }
        UnitHyd!![j]!!.rainGage = g
        Gage!![g]!!.isUsed = 1
        return 0
    } else if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    m = datetime_findMonth(tok!![1])
    if ((m == 0)) {
        if (!(match(tok!![1], __L351) != 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![1])
        }
    }
    k = findmatch(tok!![2], UHTypeWords)
    if ((k < 0)) {
        return readOldUHFormat(j, m, tok, ntoks)
    }
    i = 0
    f1@ while ((i < 3)) {
        if (!(getDouble(tok!![(i + 3)], DElem(x!!, i)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![(i + 3)])
        }
        i++
    }
    i = 3
    f2@ while ((i < 6)) {
        x!![i] = 0.0
        if ((ntoks > (i + 3))) {
            if (!(getDouble(tok!![(i + 3)], DElem(x!!, i)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![(i + 2)])
            }
        }
        i++
    }
    setUnitHydParams(j, k, m, x)
    return 0
}

// rdii.c:293
fun readOldUHFormat(j: Int, m: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    var p: DoubleArray? = DoubleArray(9)
    var x: DoubleArray? = DoubleArray(6)
    if ((ntoks < 11)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 0
    f1@ while ((i < 9)) {
        if (!(getDouble(tok!![(i + 2)], DElem(p!!, i)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![(i + 2)])
        }
        i++
    }
    i = 0
    f2@ while ((i < 3)) {
        x!![(i + 3)] = 0.0
        if ((ntoks > (i + 11))) {
            if (!(getDouble(tok!![(i + 11)], DElem(x!!, (i + 3))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![(i + 11)])
            }
        }
        i++
    }
    k = 0
    f3@ while ((k < 3)) {
        i = 0
        f4@ while ((i < 3)) {
            x!![i] = p!![((3 * k) + i)]
            setUnitHydParams(j, k, m, x)
            i++
        }
        k++
    }
    return 0
}

// rdii.c:342
fun setUnitHydParams(j: Int, i: Int, __m: Int, x: DoubleArray?) {
    var m: Int = __m
    var m1: Int = 0
    var m2: Int = 0
    var t: Double = 0.0
    var k: Double = 0.0
    var tBase: Double = 0.0
    if ((m == 0)) {
        m1 = 0
        m2 = 11
    } else {
        m1 = (m - 1)
        m2 = m1
    }
    m = m1
    f1@ while ((m <= m2)) {
        UnitHyd!![j]!!.r!![m]!![i] = x!![0]
        t = x!![1]
        k = x!![2]
        tBase = (t * ((1.0 + k)))
        UnitHyd!![j]!!.tPeak!![m]!![i] = R.D2L(((t * 3600.0)))
        UnitHyd!![j]!!.tBase!![m]!![i] = R.D2L(((tBase * 3600.0)))
        UnitHyd!![j]!!.iaMax!![m]!![i] = x!![3]
        UnitHyd!![j]!!.iaRecov!![m]!![i] = x!![4]
        UnitHyd!![j]!!.iaInit!![m]!![i] = x!![5]
        m++
    }
}

// rdii.c:389
fun rdii_deleteRdiiInflow(j: Int) {
    if ((Node!![j]!!.rdiiInflow != null)) {
        R.free(Node!![j]!!.rdiiInflow)
        Node!![j]!!.rdiiInflow = (null)
    }
}

// rdii.c:408
fun rdii_openRdii() {
    var fStamp: CPtr? = R.bufFrom(__L352, 11)
    RdiiNodeIndex = (null)
    RdiiNodeFlow = (null)
    NumRdiiNodes.v = 0
    RdiiStartDate.v = ((-693594)).toDouble()
    if ((IgnoreRDII != 0)) {
        return
    }
    if ((Frdii!!.mode != USE_FILE)) {
        createRdiiFile()
    }
    if (((Frdii!!.mode == NO_FILE) || (ErrorCode != 0))) {
        return
    }
    Frdii!!.file = R.fopen(Frdii!!.name, __L353)
    if ((Frdii!!.file == null)) {
        if ((Frdii!!.mode == SCRATCH_FILE)) {
            report_writeErrorMsg(ERR_RDII_FILE_SCRATCH, __L0)
        } else {
            report_writeErrorMsg(ERR_RDII_FILE_OPEN, Frdii!!.name)
        }
        return
    }
    R.fread(fStamp, 0, (R.strlen(FileStamp)).toInt(), Frdii!!.file)
    if ((R.strcmp(fStamp, FileStamp) == 0)) {
        RdiiFileType = BINARY
        ErrorCode = readRdiiFileHeader()
    } else {
        R.fclose(Frdii!!.file)
        RdiiFileType = TEXT
        openRdiiTextFile()
    }
    if ((ErrorCode != 0)) {
        report_writeErrorMsg(ErrorCode, Frdii!!.name)
    } else {
        readRdiiFlows()
    }
}

// rdii.c:470
fun openRdiiTextFile() {
    Frdii!!.file = R.fopen(Frdii!!.name, __L13)
    if ((Frdii!!.file == null)) {
        if ((Frdii!!.mode == SCRATCH_FILE)) {
            report_writeErrorMsg(ERR_RDII_FILE_SCRATCH, __L0)
        } else {
            report_writeErrorMsg(ERR_RDII_FILE_OPEN, Frdii!!.name)
        }
        return
    }
    ErrorCode = readRdiiTextFileHeader()
    if ((ErrorCode != 0)) {
        report_writeErrorMsg(ErrorCode, Frdii!!.name)
    }
}

// rdii.c:497
fun rdii_closeRdii() {
    if ((Frdii!!.file != null)) {
        R.fclose(Frdii!!.file)
    }
    if ((Frdii!!.mode == SCRATCH_FILE)) {
        R.remove(Frdii!!.name)
    }
    if ((RdiiNodeIndex != null)) {
        R.free(RdiiNodeIndex)
        RdiiNodeIndex = (null)
    }
    if ((RdiiNodeFlow != null)) {
        R.free(RdiiNodeFlow)
        RdiiNodeFlow = (null)
    }
}

// rdii.c:512
fun rdii_getNumRdiiFlows(aDate: Double): Int {
    if ((NumRdiiNodes.v == 0)) {
        return 0
    }
    if (!(Frdii!!.file != null)) {
        return 0
    }
    w1@ while (!(R.feof(Frdii!!.file) != 0)) {
        if ((RdiiStartDate.v == ((-693594)).toDouble())) {
            return 0
        }
        if ((aDate < RdiiStartDate.v)) {
            return 0
        }
        if ((aDate < RdiiEndDate)) {
            return NumRdiiNodes.v
        } else {
            readRdiiFlows()
        }
    }
    return 0
}

// rdii.c:542
fun rdii_getRdiiFlow(i: Int, j: IRef?, q: DRef?) {
    if (((i >= 0) && (i < NumRdiiNodes.v))) {
        j!!.set(RdiiNodeIndex!![i])
        q!!.set((RdiiNodeFlow!![i]).toDouble())
    }
}

// rdii.c:560
fun readRdiiFileHeader(): Int {
    var i: Int = 0
    var j: Int = 0
    R.fread1(RdiiStep, 4, Frdii!!.file)
    if ((RdiiStep.v <= 0)) {
        return ERR_RDII_FILE_FORMAT
    }
    R.fread1(NumRdiiNodes, 4, Frdii!!.file)
    if ((NumRdiiNodes.v <= 0)) {
        return ERR_RDII_FILE_FORMAT
    }
    RdiiNodeIndex = IntArray(((NumRdiiNodes.v).toLong()).toInt())
    if (!(RdiiNodeIndex != null)) {
        return ERR_MEMORY
    }
    RdiiNodeFlow = FloatArray(((NumRdiiNodes.v).toLong()).toInt())
    if (!(RdiiNodeFlow != null)) {
        return ERR_MEMORY
    }
    if ((R.feof(Frdii!!.file) != 0)) {
        return ERR_RDII_FILE_FORMAT
    }
    R.fread(RdiiNodeIndex, 0, ((NumRdiiNodes.v).toLong()).toInt(), Frdii!!.file)
    i = 0
    f1@ while ((i < NumRdiiNodes.v)) {
        j = RdiiNodeIndex!![i]
        if ((Node!![j]!!.rdiiInflow == null)) {
            return ERR_RDII_FILE_FORMAT
        }
        i++
    }
    if ((R.feof(Frdii!!.file) != 0)) {
        return ERR_RDII_FILE_FORMAT
    }
    return 0
}

// rdii.c:595
fun readRdiiTextFileHeader(): Int {
    var i: Int = 0
    var j: Int = 0
    var line: CPtr? = R.buf(1025)
    var s1: CPtr? = R.bufFrom(__L0, 1025)
    var s2: CPtr? = R.buf(1025)
    R.fgets(line, 1024, Frdii!!.file)
    if (((!(R.sscanf(line, __L169, s1) != 0)) || (R.strcmp(s1, __L181) != 0))) {
        return ERR_RDII_FILE_FORMAT
    }
    R.fgets(line, 1024, Frdii!!.file)
    RdiiStep.v = 0
    R.fgets(line, 1024, Frdii!!.file)
    if (((!(R.sscanf(line, __L182, RdiiStep) != 0)) || (RdiiStep.v <= 0))) {
        return ERR_RDII_FILE_FORMAT
    }
    R.fgets(line, 1024, Frdii!!.file)
    R.fgets(line, 1024, Frdii!!.file)
    if ((R.sscanf(line, __L44, s1, s2) < 2)) {
        return ERR_RDII_FILE_FORMAT
    }
    RdiiFlowUnits = findmatch(s2, FlowUnitWords)
    if ((RdiiFlowUnits < 0)) {
        return ERR_RDII_FILE_FORMAT
    }
    R.fgets(line, 1024, Frdii!!.file)
    if (((R.sscanf(line, __L182, NumRdiiNodes) < 1) || (NumRdiiNodes.v <= 0))) {
        return ERR_RDII_FILE_FORMAT
    }
    RdiiNodeIndex = IntArray(((NumRdiiNodes.v).toLong()).toInt())
    if (!(RdiiNodeIndex != null)) {
        return ERR_MEMORY
    }
    RdiiNodeFlow = FloatArray(((NumRdiiNodes.v).toLong()).toInt())
    if (!(RdiiNodeFlow != null)) {
        return ERR_MEMORY
    }
    i = 0
    f1@ while ((i < NumRdiiNodes.v)) {
        if ((R.feof(Frdii!!.file) != 0)) {
            return ERR_RDII_FILE_FORMAT
        }
        R.fgets(line, 1024, Frdii!!.file)
        if (!(R.sscanf(line, __L169, s1) != 0)) {
            return ERR_RDII_FILE_FORMAT
        }
        j = project_findObject(NODE, s1)
        if ((j < 0)) {
            return ERR_RDII_FILE_FORMAT
        }
        RdiiNodeIndex!![i] = j
        i++
    }
    if ((R.feof(Frdii!!.file) != 0)) {
        return ERR_RDII_FILE_FORMAT
    }
    R.fgets(line, 1024, Frdii!!.file)
    return 0
}

// rdii.c:663
fun readRdiiFlows() {
    if ((RdiiFileType == TEXT)) {
        readRdiiTextFlows()
    } else {
        RdiiStartDate.v = ((-693594)).toDouble()
        RdiiEndDate = ((-693594)).toDouble()
        if ((R.feof(Frdii!!.file) != 0)) {
            return
        }
        R.fread1(RdiiStartDate, 8, Frdii!!.file)
        if ((RdiiStartDate.v == ((-693594)).toDouble())) {
            return
        }
        if ((R.fread(RdiiNodeFlow, 0, ((NumRdiiNodes.v).toLong()).toInt(), Frdii!!.file) < (NumRdiiNodes.v).toLong())) {
            RdiiStartDate.v = ((-693594)).toDouble()
        } else {
            RdiiEndDate = datetime_addSeconds(RdiiStartDate.v, (RdiiStep.v).toDouble())
        }
    }
}

// rdii.c:686
fun readRdiiTextFlows() {
    var i: Int = 0
    var n: Int = 0
    val yr = IBox(0)
    val mon = IBox(0)
    val day = IBox(0)
    val hr = IBox(0)
    val min = IBox(0)
    val sec = IBox(0)
    val x = DBox(0.0)
    var line: CPtr? = R.buf(1025)
    var s: CPtr? = R.buf(1025)
    RdiiStartDate.v = ((-693594)).toDouble()
    i = 0
    f1@ while ((i < NumRdiiNodes.v)) {
        if ((R.feof(Frdii!!.file) != 0)) {
            return
        }
        R.fgets(line, 1024, Frdii!!.file)
        n = R.sscanf(line, __L354, s, yr, mon, day, hr, min, sec, x)
        if ((n < 8)) {
            return
        }
        RdiiNodeFlow!![i] = (((x.v / Qcf!![RdiiFlowUnits]))).toFloat()
        i++
    }
    RdiiStartDate.v = (datetime_encodeDate(yr.v, mon.v, day.v) + datetime_encodeTime(hr.v, min.v, sec.v))
    RdiiEndDate = datetime_addSeconds(RdiiStartDate.v, (RdiiStep.v).toDouble())
}

// rdii.c:720
fun createRdiiFile() {
    var hasRdii: Int = 0
    var elapsedTime: Double = 0.0
    var duration: Double = 0.0
    var currentDate: Double = 0.0
    RdiiStep.v = WetStep
    NumRdiiNodes.v = getNumRdiiNodes()
    if ((NumRdiiNodes.v == 0)) {
        Frdii!!.mode = NO_FILE
        return
    } else if ((Frdii!!.mode == NO_FILE)) {
        Frdii!!.mode = SCRATCH_FILE
    }
    validateRdii()
    initGageData()
    if ((ErrorCode != 0)) {
        return
    }
    openRdiiProcessor()
    if (!(ErrorCode != 0)) {
        initUnitHydData()
        duration = (TotalDuration / 1000.0)
        elapsedTime = 0.0
        w1@ while (((elapsedTime <= duration) && (!(ErrorCode != 0)))) {
            currentDate = (StartDateTime + (elapsedTime / 86400.0))
            getRainfall(currentDate)
            getUnitHydRdii(currentDate)
            hasRdii = getNodeRdii()
            if ((hasRdii != 0)) {
                saveRdiiFlows(currentDate)
            }
            elapsedTime += ((RdiiStep.v).toDouble())
        }
    }
    closeRdiiProcessor()
}

// rdii.c:793
fun getNumRdiiNodes(): Int {
    var j: Int = 0
    var n: Int = 0
    n = 0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.rdiiInflow != null)) {
            n++
        }
        j++
    }
    return n
}

// rdii.c:813
fun validateRdii() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    var rsum: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![UNITHYD])) {
        m = 0
        f2@ while ((m < 12)) {
            rsum = 0.0
            k = 0
            f3@ while ((k < 3)) {
                if ((UnitHyd!![j]!!.tBase!![m]!![k] == 0L)) {
                    k++
                    continue@f3
                }
                if (((UnitHyd!![j]!!.tPeak!![m]!![k]).toDouble() < 0.0)) {
                    report_writeErrorMsg(ERR_UNITHYD_TIMES, UnitHyd!![j]!!.ID)
                }
                if ((UnitHyd!![j]!!.r!![m]!![k] < 0.0)) {
                    report_writeErrorMsg(ERR_UNITHYD_RATIOS, UnitHyd!![j]!!.ID)
                } else {
                    rsum += UnitHyd!![j]!!.r!![m]!![k]
                }
                k++
            }
            if ((rsum > 1.01)) {
                report_writeErrorMsg(ERR_UNITHYD_RATIOS, UnitHyd!![j]!!.ID)
            }
            m++
        }
        j++
    }
    i = 0
    f4@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.rdiiInflow != null)) {
            if ((Node!![i]!!.rdiiInflow!!.area < 0.0)) {
                report_writeErrorMsg(ERR_RDII_AREA, Node!![i]!!.ID)
            }
        }
        i++
    }
}

// rdii.c:877
fun openRdiiProcessor() {
    var j: Int = 0
    var n: Int = 0
    UHGroup = (null)
    RdiiNodeIndex = (null)
    RdiiNodeFlow = (null)
    TotalRainVol = 0.0
    TotalRdiiVol = 0.0
    if (!(allocRdiiMemory() != 0)) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
        return
    }
    if (!(openNewRdiiFile() != 0)) {
        report_writeErrorMsg(ERR_RDII_FILE_SCRATCH, __L0)
        return
    }
    n = 0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        if (((Node!![j]!!.rdiiInflow != null) && (RdiiNodeIndex != null))) {
            RdiiNodeIndex!![n] = j
            n++
        }
        j++
    }
}

// rdii.c:922
fun allocRdiiMemory(): Int {
    var i: Int = 0
    var k: Int = 0
    var n: Int = 0
    UHGroup = Array<TUHGroup?>(((Nobjects!![UNITHYD]).toLong()).toInt()) { TUHGroup() }
    if (!(UHGroup != null)) {
        return 0
    }
    i = 0
    f1@ while ((i < Nobjects!![UNITHYD])) {
        UHGroup!![i]!!.rainInterval = getRainInterval(i)
        k = 0
        f2@ while ((k < 3)) {
            UHGroup!![i]!!.uh!![k]!!.pastRain = (null)
            UHGroup!![i]!!.uh!![k]!!.pastMonth = null
            UHGroup!![i]!!.uh!![k]!!.maxPeriods = getMaxPeriods(i, k)
            n = UHGroup!![i]!!.uh!![k]!!.maxPeriods
            if ((n > 0)) {
                UHGroup!![i]!!.uh!![k]!!.pastRain = DoubleArray(((n).toLong()).toInt())
                if (!(UHGroup!![i]!!.uh!![k]!!.pastRain != null)) {
                    return 0
                }
                UHGroup!![i]!!.uh!![k]!!.pastMonth = R.buf(((n).toLong()).toInt())
                if (!(UHGroup!![i]!!.uh!![k]!!.pastMonth != null)) {
                    return 0
                }
            }
            k++
        }
        i++
    }
    RdiiNodeIndex = IntArray(((NumRdiiNodes.v).toLong()).toInt())
    if (!(RdiiNodeIndex != null)) {
        return 0
    }
    RdiiNodeFlow = FloatArray(((NumRdiiNodes.v).toLong()).toInt())
    if (!(RdiiNodeFlow != null)) {
        return 0
    }
    return 1
}

// rdii.c:970
fun getRainInterval(i: Int): Int {
    var ri: Int = 0
    var tLimb: Int = 0
    var k: Int = 0
    var m: Int = 0
    ri = WetStep
    m = 0
    f1@ while ((m < 12)) {
        k = 0
        f2@ while ((k < 3)) {
            if ((UnitHyd!![i]!!.tPeak!![m]!![k] > 0L)) {
                tLimb = (UnitHyd!![i]!!.tPeak!![m]!![k]).toInt()
                ri = ((if ((((ri) <= (tLimb)))) (ri) else (tLimb)))
                tLimb = ((UnitHyd!![i]!!.tBase!![m]!![k] - (tLimb).toLong())).toInt()
                if ((tLimb > 0)) {
                    ri = ((if ((((ri) <= (tLimb)))) (ri) else (tLimb)))
                }
            }
            k++
        }
        m++
    }
    return ri
}

// rdii.c:1005
fun getMaxPeriods(i: Int, k: Int): Int {
    var m: Int = 0
    var n: Int = 0
    var nMax: Int = 0
    var rainInterval: Int = 0
    rainInterval = UHGroup!![i]!!.rainInterval
    nMax = 0
    m = 0
    f1@ while ((m < 12)) {
        n = ((((UnitHyd!![i]!!.tBase!![m]!![k] / (rainInterval).toLong())) + 1L)).toInt()
        nMax = ((if ((((n) >= (nMax)))) (n) else (nMax)))
        m++
    }
    return nMax
}

// rdii.c:1034
fun initGageData() {
    var i: Int = 0
    var g: Int = 0
    g = 0
    f1@ while ((g < Nobjects!![GAGE])) {
        if ((Gage!![g]!!.tSeries >= 0)) {
            table_tseriesInit(Tseries!![Gage!![g]!!.tSeries])
        }
        gage_initState(g)
        g++
    }
    i = 0
    f2@ while ((i < Nobjects!![UNITHYD])) {
        g = UnitHyd!![i]!!.rainGage
        if ((g >= 0)) {
            if ((Gage!![g]!!.coGage >= 0)) {
                UnitHyd!![i]!!.rainGage = Gage!![g]!!.coGage
            }
        }
        i++
    }
}

// rdii.c:1072
fun initUnitHydData() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var n: Int = 0
    var month: Int = 0
    month = (datetime_monthOfYear(StartDateTime) - 1)
    i = 0
    f1@ while ((i < Nobjects!![UNITHYD])) {
        k = 0
        f2@ while ((k < 3)) {
            UHGroup!![i]!!.uh!![k]!!.drySeconds = ((((UHGroup!![i]!!.uh!![k]!!.maxPeriods * UHGroup!![i]!!.rainInterval)) + 1)).toLong()
            UHGroup!![i]!!.uh!![k]!!.period = (UHGroup!![i]!!.uh!![k]!!.maxPeriods + 1)
            UHGroup!![i]!!.uh!![k]!!.hasPastRain = 0
            UHGroup!![i]!!.uh!![k]!!.iaUsed = UnitHyd!![i]!!.iaInit!![month]!![k]
            k++
        }
        UHGroup!![i]!!.gageDate = StartDateTime
        UHGroup!![i]!!.area = 0.0
        UHGroup!![i]!!.rdii = 0.0
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![UNITHYD])) {
        UHGroup!![i]!!.isUsed = 0
        i++
    }
    n = 0
    f4@ while ((n < NumRdiiNodes.v)) {
        j = RdiiNodeIndex!![n]
        i = Node!![j]!!.rdiiInflow!!.unitHyd
        UHGroup!![i]!!.isUsed = 1
        UHGroup!![i]!!.lastDate = StartDateTime
        UHGroup!![i]!!.area += Node!![j]!!.rdiiInflow!!.area
        n++
    }
}

// rdii.c:1128
fun openNewRdiiFile(): Int {
    val j = IBox(0)
    if ((Frdii!!.mode == SCRATCH_FILE)) {
        getTempFileName(Frdii!!.name)
    }
    Frdii!!.file = R.fopen(Frdii!!.name, __L166)
    if ((Frdii!!.file == null)) {
        return 0
    }
    R.fwrite(FileStamp, 0, (R.strlen(FileStamp)).toInt(), Frdii!!.file)
    R.fwrite1(RdiiStep.v, 4, Frdii!!.file)
    R.fwrite1(NumRdiiNodes.v, 4, Frdii!!.file)
    j.v = 0
    f1@ while ((j.v < Nobjects!![NODE])) {
        if ((Node!![j.v]!!.rdiiInflow != null)) {
            R.fwrite1(j.v, 4, Frdii!!.file)
        }
        j.v++
    }
    return 1
}

// rdii.c:1163
fun getRainfall(currentDate: Double) {
    var j: Int = 0
    var k: Int = 0
    var g: Int = 0
    var i: Int = 0
    var month: Int = 0
    var rainInterval: Int = 0
    var rainDepth: Double = 0.0
    var excessDepth: Double = 0.0
    var gageDate: Double = 0.0
    month = (datetime_monthOfYear(currentDate) - 1)
    g = 0
    f1@ while ((g < Nobjects!![GAGE])) {
        Gage!![g]!!.isCurrent = 0
        g++
    }
    j = 0
    f2@ while ((j < Nobjects!![UNITHYD])) {
        g = UnitHyd!![j]!!.rainGage
        rainInterval = UHGroup!![j]!!.rainInterval
        w3@ while ((UHGroup!![j]!!.gageDate < currentDate)) {
            gageDate = UHGroup!![j]!!.gageDate
            Adjust!!.rainFactor = Adjust!!.rain!![(datetime_monthOfYear(gageDate) - 1)]
            if (!(Gage!![g]!!.isCurrent != 0)) {
                gage_setState(g, gageDate)
                Gage!![g]!!.isCurrent = 1
            }
            rainDepth = ((Gage!![g]!!.rainfall * (rainInterval).toDouble()) / 3600.0)
            TotalRainVol += ((rainDepth / UCF(RAINDEPTH)) * UHGroup!![j]!!.area)
            k = 0
            f4@ while ((k < 3)) {
                excessDepth = applyIA(j, k, gageDate, (rainInterval).toDouble(), rainDepth)
                updateDryPeriod(j, k, excessDepth, rainInterval)
                i = UHGroup!![j]!!.uh!![k]!!.period
                if ((i >= UHGroup!![j]!!.uh!![k]!!.maxPeriods)) {
                    i = 0
                }
                UHGroup!![j]!!.uh!![k]!!.pastRain!![i] = excessDepth
                UHGroup!![j]!!.uh!![k]!!.pastMonth!!.s(i, (month).toByte().toInt())
                UHGroup!![j]!!.uh!![k]!!.period = (i + 1)
                k++
            }
            UHGroup!![j]!!.gageDate = datetime_addSeconds(gageDate, (rainInterval).toDouble())
        }
        j++
    }
}

// rdii.c:1231
fun applyIA(j: Int, k: Int, aDate: Double, dt: Double, rainDepth: Double): Double {
    var m: Int = 0
    var ia: Double = 0.0
    var netRainDepth: Double = 0.0
    m = (datetime_monthOfYear(aDate) - 1)
    ia = (UnitHyd!![j]!!.iaMax!![m]!![k] - UHGroup!![j]!!.uh!![k]!!.iaUsed)
    ia = ((if ((((ia) >= (0.0)))) (ia) else (0.0)))
    if ((rainDepth > 0.0)) {
        netRainDepth = (rainDepth - ia)
        netRainDepth = ((if ((((netRainDepth) >= (0.0)))) (netRainDepth) else (0.0)))
        ia = (rainDepth - netRainDepth)
        UHGroup!![j]!!.uh!![k]!!.iaUsed += ia
    } else {
        UHGroup!![j]!!.uh!![k]!!.iaUsed -= ((dt / 86400.0) * UnitHyd!![j]!!.iaRecov!![m]!![k])
        UHGroup!![j]!!.uh!![k]!!.iaUsed = ((if ((((UHGroup!![j]!!.uh!![k]!!.iaUsed) >= (0.0)))) (UHGroup!![j]!!.uh!![k]!!.iaUsed) else (0.0)))
        netRainDepth = 0.0
    }
    return netRainDepth
}

// rdii.c:1276
fun updateDryPeriod(j: Int, k: Int, rainDepth: Double, rainInterval: Int) {
    var i: Int = 0
    if ((rainDepth > 0.0)) {
        if ((UHGroup!![j]!!.uh!![k]!!.drySeconds >= ((rainInterval * UHGroup!![j]!!.uh!![k]!!.maxPeriods)).toLong())) {
            i = 0
            f1@ while ((i < UHGroup!![j]!!.uh!![k]!!.maxPeriods)) {
                UHGroup!![j]!!.uh!![k]!!.pastRain!![i] = 0.0
                i++
            }
            UHGroup!![j]!!.uh!![k]!!.period = 0
        }
        UHGroup!![j]!!.uh!![k]!!.drySeconds = 0L
        UHGroup!![j]!!.uh!![k]!!.hasPastRain = 1
    } else {
        UHGroup!![j]!!.uh!![k]!!.drySeconds += ((rainInterval).toLong())
        if ((UHGroup!![j]!!.uh!![k]!!.drySeconds >= ((rainInterval * UHGroup!![j]!!.uh!![k]!!.maxPeriods)).toLong())) {
            UHGroup!![j]!!.uh!![k]!!.hasPastRain = 0
        } else {
            UHGroup!![j]!!.uh!![k]!!.hasPastRain = 1
        }
    }
}

// rdii.c:1321
fun getUnitHydRdii(currentDate: Double) {
    var j: Int = 0
    var k: Int = 0
    var rainInterval: Int = 0
    j = 0
    f1@ while ((j < Nobjects!![UNITHYD])) {
        if (!(UHGroup!![j]!!.isUsed != 0)) {
            j++
            continue@f1
        }
        if ((currentDate < UHGroup!![j]!!.lastDate)) {
            j++
            continue@f1
        }
        UHGroup!![j]!!.lastDate = UHGroup!![j]!!.gageDate
        rainInterval = UHGroup!![j]!!.rainInterval
        UHGroup!![j]!!.rdii = 0.0
        k = 0
        f2@ while ((k < 3)) {
            if ((UHGroup!![j]!!.uh!![k]!!.hasPastRain != 0)) {
                UHGroup!![j]!!.rdii += (getUnitHydConvol(j, k, rainInterval))
            }
            k++
        }
        j++
    }
}

// rdii.c:1358
fun getUnitHydConvol(j: Int, k: Int, rainInterval: Int): Double {
    var i: Int = 0
    var m: Int = 0
    var p: Int = 0
    var pMax: Int = 0
    var t: Double = 0.0
    var u: Double = 0.0
    var v: Double = 0.0
    var rdii: Double = 0.0
    var uh: TUHData? = null
    rdii = 0.0
    uh = UHGroup!![j]!!.uh!![k]
    i = (uh!!.period - 1)
    if ((i < 0)) {
        i = (uh!!.maxPeriods - 1)
    }
    pMax = uh!!.maxPeriods
    p = 1
    w1@ while ((p < pMax)) {
        v = uh!!.pastRain!![i]
        m = uh!!.pastMonth!!.g(i)
        if ((v > 0.0)) {
            t = (((((p)).toDouble() - 0.5)) * (rainInterval).toDouble())
            u = (getUnitHydOrd(j, m, k, t) * UnitHyd!![j]!!.r!![m]!![k])
            rdii += (u * v)
        }
        p = (p + 1)
        i = (i - 1)
        if ((i < 0)) {
            i = (uh!!.maxPeriods - 1)
        }
    }
    return rdii
}

// rdii.c:1411
fun getUnitHydOrd(h: Int, m: Int, k: Int, t: Double): Double {
    var qPeak: Double = 0.0
    var f: Double = 0.0
    var t1: Double = 0.0
    var t2: Double = 0.0
    var tBase: Double = 0.0
    tBase = (UnitHyd!![h]!!.tBase!![m]!![k]).toDouble()
    if ((t >= tBase)) {
        return 0.0
    }
    qPeak = ((2.0 / tBase) * 3600.0)
    t1 = (UnitHyd!![h]!!.tPeak!![m]!![k]).toDouble()
    t2 = (tBase - t1)
    if ((t <= t1)) {
        f = (t / t1)
    } else {
        f = (1.0 - (((t - t1)) / t2))
    }
    return (((if ((((f) >= (0.0)))) (f) else (0.0))) * qPeak)
}

// rdii.c:1446
fun getNodeRdii(): Int {
    var hasRdii: Int = 0
    var i: Int = 0
    var j: Int = 0
    var n: Int = 0
    var rdii: Double = 0.0
    n = 0
    f1@ while ((n < NumRdiiNodes.v)) {
        j = RdiiNodeIndex!![n]
        i = Node!![j]!!.rdiiInflow!!.unitHyd
        rdii = ((UHGroup!![i]!!.rdii * Node!![j]!!.rdiiInflow!!.area) / UCF(RAINFALL))
        if ((rdii < ZERO_RDII)) {
            rdii = 0.0
        } else {
            hasRdii = 1
        }
        RdiiNodeFlow!![n] = (rdii).toFloat()
        if ((rdii > 0.0)) {
            TotalRdiiVol += (rdii * (RdiiStep.v).toDouble())
        }
        n++
    }
    return hasRdii
}

// rdii.c:1483
fun saveRdiiFlows(__currentDate: Double) {
    val currentDate = DBox(__currentDate)
    R.fwrite1(currentDate.v, 8, Frdii!!.file)
    R.fwrite(RdiiNodeFlow, 0, ((NumRdiiNodes.v).toLong()).toInt(), Frdii!!.file)
}

// rdii.c:1496
fun closeRdiiProcessor() {
    if (!(ErrorCode != 0)) {
        report_writeRdiiStats(TotalRainVol, TotalRdiiVol)
    }
    freeRdiiMemory()
    if ((Frdii!!.file != null)) {
        R.fclose(Frdii!!.file)
    }
}

// rdii.c:1516
fun freeRdiiMemory() {
    var i: Int = 0
    var k: Int = 0
    if ((UHGroup != null)) {
        i = 0
        f1@ while ((i < Nobjects!![UNITHYD])) {
            k = 0
            f2@ while ((k < 3)) {
                if ((UHGroup!![i]!!.uh!![k]!!.pastRain != null)) {
                    R.free(UHGroup!![i]!!.uh!![k]!!.pastRain)
                    UHGroup!![i]!!.uh!![k]!!.pastRain = (null)
                }
                if ((UHGroup!![i]!!.uh!![k]!!.pastMonth != null)) {
                    R.free(UHGroup!![i]!!.uh!![k]!!.pastMonth)
                    UHGroup!![i]!!.uh!![k]!!.pastMonth = null
                }
                k++
            }
            i++
        }
        if ((UHGroup != null)) {
            R.free(UHGroup)
            UHGroup = (null)
        }
    }
    if ((RdiiNodeIndex != null)) {
        R.free(RdiiNodeIndex)
        RdiiNodeIndex = (null)
    }
    if ((RdiiNodeFlow != null)) {
        R.free(RdiiNodeFlow)
        RdiiNodeFlow = (null)
    }
}

