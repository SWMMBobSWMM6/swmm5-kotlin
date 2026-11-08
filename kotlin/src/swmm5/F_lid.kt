// Generated from EPA SWMM 5.2.4 (lid.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_lid")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var LidLayerWords: Array<CPtr?>? = arrayOf<CPtr?>(__L991, __L992, __L742, __L993, __L994, __L995, __L996, null)
@JvmField var LidTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L997, __L998, __L953, __L999, __L1000, __L1001, __L1002, __L1003, null)
@JvmField var LidProcs: Array<TLidProc?>? = null
@JvmField var LidCount: Int = 0
@JvmField var LidGroups: Array<LidGroup?>? = null
@JvmField var GroupCount: Int = 0
@JvmField var lid_EvapRate: Double = 0.0
@JvmField var NativeInfil: Double = 0.0
@JvmField var lid_MaxNativeInfil: Double = 0.0
@JvmField var initLidRptFile_colCount: Int = 14
@JvmField var initLidRptFile_head1: Array<CPtr?>? = arrayOf<CPtr?>(__L1022, __L1023, __L1024, __L1024, __L1025, __L1026, __L1027, __L1028, __L1025, __L1029, __L1025, __L1026, __L1027, __L1030, null)
@JvmField var initLidRptFile_head2: Array<CPtr?>? = arrayOf<CPtr?>(__L1022, __L1031, __L1032, __L1033, __L1034, __L1035, __L1035, __L1036, __L1037, __L1038, __L1039, __L1039, __L1040, __L1041, null)
@JvmField var initLidRptFile_units1: Array<CPtr?>? = arrayOf<CPtr?>(__L1042, __L1043, __L1044, __L1044, __L1044, __L1044, __L1044, __L1044, __L1044, __L1044, __L1045, __L1045, __L1046, __L1047, null)
@JvmField var initLidRptFile_units2: Array<CPtr?>? = arrayOf<CPtr?>(__L1042, __L1043, __L1048, __L1048, __L1048, __L1048, __L1048, __L1048, __L1048, __L1048, __L1049, __L1049, __L1046, __L1050, null)
@JvmField var initLidRptFile_line9: CPtr? = R.bufFrom(__L1051, 11)

// lid.c:237
fun lid_create(lidCount: Int, subcatchCount: Int) {
    var j: Int = 0
    LidProcs = (null)
    LidGroups = (null)
    LidCount = lidCount
    GroupCount = subcatchCount
    if ((GroupCount > 0)) {
        LidGroups = arrayOfNulls<LidGroup>(((GroupCount).toLong()).toInt())
        if ((LidGroups == null)) {
            ErrorCode = ERR_MEMORY
            return
        }
    }
    j = 0
    f1@ while ((j < GroupCount)) {
        LidGroups!![j] = (null)
        j++
    }
    if ((LidCount == 0)) {
        return
    }
    LidProcs = Array<TLidProc?>(((LidCount).toLong()).toInt()) { TLidProc() }
    if ((LidProcs == null)) {
        ErrorCode = ERR_MEMORY
        return
    }
    j = 0
    f2@ while ((j < LidCount)) {
        LidProcs!![j]!!.lidType = (-1)
        LidProcs!![j]!!.surface!!.thickness = 0.0
        LidProcs!![j]!!.surface!!.voidFrac = 1.0
        LidProcs!![j]!!.surface!!.roughness = 0.0
        LidProcs!![j]!!.surface!!.surfSlope = 0.0
        LidProcs!![j]!!.pavement!!.thickness = 0.0
        LidProcs!![j]!!.soil!!.thickness = 0.0
        LidProcs!![j]!!.storage!!.thickness = 0.0
        LidProcs!![j]!!.storage!!.kSat = 0.0
        LidProcs!![j]!!.drain!!.coeff = 0.0
        LidProcs!![j]!!.drain!!.offset = 0.0
        LidProcs!![j]!!.drainMat!!.thickness = 0.0
        LidProcs!![j]!!.drainMat!!.roughness = 0.0
        LidProcs!![j]!!.drainRmvl = (null)
        LidProcs!![j]!!.drainRmvl = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        if ((LidProcs!![j]!!.drainRmvl == null)) {
            ErrorCode = ERR_MEMORY
            return
        }
        j++
    }
}

// lid.c:304
fun lid_delete() {
    var j: Int = 0
    j = 0
    f1@ while ((j < GroupCount)) {
        freeLidGroup(j)
        j++
    }
    if ((LidGroups != null)) {
        R.free(LidGroups)
        LidGroups = (null)
    }
    j = 0
    f2@ while ((j < LidCount)) {
        if ((LidProcs!![j]!!.drainRmvl != null)) {
            R.free(LidProcs!![j]!!.drainRmvl)
            LidProcs!![j]!!.drainRmvl = (null)
        }
        j++
    }
    if ((LidProcs != null)) {
        R.free(LidProcs)
        LidProcs = (null)
    }
    GroupCount = 0
    LidCount = 0
}

// lid.c:322
fun freeLidGroup(j: Int) {
    var lidGroup: LidGroup? = LidGroups!![j]
    var lidList: TLidList? = null
    var lidUnit: TLidUnit? = null
    var nextLidUnit: TLidList? = null
    if ((lidGroup == null)) {
        return
    }
    lidList = lidGroup!!.lidList
    w1@ while ((lidList != null)) {
        lidUnit = lidList!!.lidUnit
        if ((lidUnit!!.rptFile != null)) {
            if ((lidUnit!!.rptFile!!.file != null)) {
                R.fclose(lidUnit!!.rptFile!!.file)
            }
            R.free(lidUnit!!.rptFile)
        }
        nextLidUnit = lidList!!.nextLidUnit
        R.free(lidUnit)
        R.free(lidList)
        lidList = nextLidUnit
    }
    R.free(lidGroup)
    LidGroups!![j] = (null)
}

// lid.c:355
fun lid_readProcParams(toks: Array<CPtr?>?, ntoks: Int): Int {
    var j: Int = 0
    var m: Int = 0
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(LID, toks!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, toks!![0])
    }
    if ((LidProcs!![j]!!.ID == null)) {
        LidProcs!![j]!!.ID = project_findID(LID, toks!![0])
    }
    m = findmatch(toks!![1], LidTypeWords)
    if ((m >= 0)) {
        LidProcs!![j]!!.lidType = m
        return 0
    } else {
        m = findmatch(toks!![1], LidLayerWords)
    }
    when (m) {
        SURF -> {
            return readSurfaceData(j, toks, ntoks)
        }
        SOIL -> {
            return readSoilData(j, toks, ntoks)
        }
        STOR -> {
            return readStorageData(j, toks, ntoks)
        }
        PAVE -> {
            return readPavementData(j, toks, ntoks)
        }
        5 -> {
            return readDrainData(j, toks, ntoks)
        }
        DRAINMAT -> {
            return readDrainMatData(j, toks, ntoks)
        }
        REMOVALS -> {
            return readRemovalsData(j, toks, ntoks)
        }
    }
    return error_setInpError(ERR_KEYWORD, toks!![1])
}

// lid.c:415
fun lid_readGroupParams(toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    var fname: CPtr? = null
    var drainSubcatch: Int = (-1)
    var drainNode: Int = (-1)
    if ((ntoks < 8)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SUBCATCH, toks!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, toks!![0])
    }
    k = project_findObject(LID, toks!![1])
    if ((k < 0)) {
        return error_setInpError(ERR_NAME, toks!![1])
    }
    n = R.atoi(toks!![2])
    if ((n < 0)) {
        return error_setInpError(ERR_NUMBER, toks!![2])
    }
    if ((n == 0)) {
        return 0
    }
    i = 3
    f1@ while ((i <= 7)) {
        if (((!(getDouble(toks!![i], DElem(x!!, (i - 3))) != 0)) || (x!![(i - 3)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    i = 2
    f2@ while ((i <= 3)) {
        if ((x!![i] > 100.0)) {
            return error_setInpError(ERR_NUMBER, toks!![(i + 3)])
        }
        i++
    }
    if (((ntoks >= 9) && (R.strcmp(toks!![8], __L275) != 0))) {
        fname = toks!![8]
    }
    if (((ntoks >= 10) && (R.strcmp(toks!![9], __L275) != 0))) {
        drainSubcatch = project_findObject(SUBCATCH, toks!![9])
        if ((drainSubcatch < 0)) {
            drainNode = project_findObject(NODE, toks!![9])
            if ((drainNode < 0)) {
                return error_setInpError(ERR_NAME, toks!![9])
            }
        }
    }
    x!![5] = 0.0
    if ((ntoks >= 11)) {
        if ((((!(getDouble(toks!![10], DElem(x!!, 5)) != 0)) || (x!![5] < 0.0)) || (x!![5] > 100.0))) {
            return error_setInpError(ERR_NUMBER, toks!![10])
        }
    }
    return addLidUnit(j, k, n, x, fname, drainSubcatch, drainNode)
}

// lid.c:499
fun addLidUnit(j: Int, k: Int, n: Int, x: DoubleArray?, fname: CPtr?, drainSubcatch: Int, drainNode: Int): Int {
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if (!(lidGroup != null)) {
        lidGroup = LidGroup()
        if (!(lidGroup != null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        lidGroup!!.lidList = (null)
        LidGroups!![j] = lidGroup
    }
    lidUnit = TLidUnit()
    if (!(lidUnit != null)) {
        return error_setInpError(ERR_MEMORY, __L0)
    }
    lidUnit!!.rptFile = (null)
    lidList = TLidList()
    if (!(lidList != null)) {
        R.free(lidUnit)
        return error_setInpError(ERR_MEMORY, __L0)
    }
    lidList!!.lidUnit = lidUnit
    lidList!!.nextLidUnit = lidGroup!!.lidList
    lidGroup!!.lidList = lidList
    lidUnit!!.lidIndex = k
    lidUnit!!.number = n
    lidUnit!!.area = (x!![0] / (((UCF(LENGTH)) * (UCF(LENGTH)))))
    lidUnit!!.fullWidth = (x!![1] / UCF(LENGTH))
    lidUnit!!.initSat = (x!![2] / 100.0)
    lidUnit!!.fromImperv = (x!![3] / 100.0)
    lidUnit!!.toPerv = (if (((x!![4] > 0.0))) 1 else 0)
    lidUnit!!.fromPerv = (x!![5] / 100.0)
    lidUnit!!.drainSubcatch = drainSubcatch
    lidUnit!!.drainNode = drainNode
    if ((fname != null)) {
        if (!(createLidRptFile(lidUnit, fname) != 0)) {
            return error_setInpError(ERR_RPT_FILE, fname)
        }
    }
    return 0
}

// lid.c:567
fun createLidRptFile(lidUnit: TLidUnit?, fname: CPtr?): Int {
    var rptFile: TLidRptFile? = null
    rptFile = TLidRptFile()
    if ((rptFile == null)) {
        return 0
    }
    lidUnit!!.rptFile = rptFile
    rptFile!!.file = R.fopen(fname, __L171)
    if ((rptFile!!.file == null)) {
        return 0
    }
    return 1
}

// lid.c:581
fun readSurfaceData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(5)
    if ((ntoks < 7)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 2
    f1@ while ((i < 7)) {
        if (((!(getDouble(toks!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    if ((x!![1] >= 1.0)) {
        return error_setInpError(ERR_NUMBER, toks!![3])
    }
    if ((x!![0] == 0.0)) {
        x!![1] = 0.0
    }
    LidProcs!![j]!!.surface!!.thickness = (x!![0] / UCF(RAINDEPTH))
    LidProcs!![j]!!.surface!!.voidFrac = (1.0 - x!![1])
    LidProcs!![j]!!.surface!!.roughness = x!![2]
    LidProcs!![j]!!.surface!!.surfSlope = (x!![3] / 100.0)
    LidProcs!![j]!!.surface!!.sideSlope = x!![4]
    return 0
}

// lid.c:616
fun readPavementData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(7)
    if ((ntoks < 7)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 2
    f1@ while ((i < 7)) {
        if (((!(getDouble(toks!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    x!![5] = 0.0
    if ((ntoks > 7)) {
        if (((!(getDouble(toks!![7], DElem(x!!, 5)) != 0)) || (x!![5] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![7])
        }
    }
    x!![6] = 0.0
    if ((ntoks > 8)) {
        if ((((!(getDouble(toks!![8], DElem(x!!, 6)) != 0)) || (x!![6] < 0.0)) || (x!![6] > 1.0))) {
            return error_setInpError(ERR_NUMBER, toks!![8])
        }
    }
    x!![1] = (x!![1] / ((x!![1] + 1.0)))
    LidProcs!![j]!!.pavement!!.thickness = (x!![0] / UCF(RAINDEPTH))
    LidProcs!![j]!!.pavement!!.voidFrac = x!![1]
    LidProcs!![j]!!.pavement!!.impervFrac = x!![2]
    LidProcs!![j]!!.pavement!!.kSat = (x!![3] / UCF(RAINFALL))
    LidProcs!![j]!!.pavement!!.clogFactor = x!![4]
    LidProcs!![j]!!.pavement!!.regenDays = x!![5]
    LidProcs!![j]!!.pavement!!.regenDegree = x!![6]
    return 0
}

// lid.c:669
fun readSoilData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(7)
    if ((ntoks < 9)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 2
    f1@ while ((i < 9)) {
        if (((!(getDouble(toks!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    LidProcs!![j]!!.soil!!.thickness = (x!![0] / UCF(RAINDEPTH))
    LidProcs!![j]!!.soil!!.porosity = x!![1]
    LidProcs!![j]!!.soil!!.fieldCap = x!![2]
    LidProcs!![j]!!.soil!!.wiltPoint = x!![3]
    LidProcs!![j]!!.soil!!.kSat = (x!![4] / UCF(RAINFALL))
    LidProcs!![j]!!.soil!!.kSlope = x!![5]
    LidProcs!![j]!!.soil!!.suction = (x!![6] / UCF(RAINDEPTH))
    return 0
}

// lid.c:703
fun readStorageData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var covered: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 2
    f1@ while ((i < 6)) {
        if (((!(getDouble(toks!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    if ((ntoks > 6)) {
        if ((match(toks!![6], __L5) != 0)) {
            covered = 1
        }
    }
    x!![1] = (x!![1] / ((x!![1] + 1.0)))
    LidProcs!![j]!!.storage!!.thickness = (x!![0] / UCF(RAINDEPTH))
    LidProcs!![j]!!.storage!!.voidFrac = x!![1]
    LidProcs!![j]!!.storage!!.kSat = (x!![2] / UCF(RAINFALL))
    LidProcs!![j]!!.storage!!.clogFactor = x!![3]
    LidProcs!![j]!!.storage!!.covered = covered
    return 0
}

// lid.c:749
fun readDrainData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = 0
    f1@ while ((i < 6)) {
        x!![i] = 0.0
        i++
    }
    i = 2
    f2@ while ((i < 8)) {
        if ((((ntoks > i)) && (((!(getDouble(toks!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    i = (-1)
    if ((ntoks >= 9)) {
        i = project_findObject(CURVE, toks!![8])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, toks!![8])
        }
    }
    LidProcs!![j]!!.drain!!.coeff = x!![0]
    LidProcs!![j]!!.drain!!.expon = x!![1]
    LidProcs!![j]!!.drain!!.offset = (x!![2] / UCF(RAINDEPTH))
    LidProcs!![j]!!.drain!!.delay = (x!![3] * 3600.0)
    LidProcs!![j]!!.drain!!.hOpen = (x!![4] / UCF(RAINDEPTH))
    LidProcs!![j]!!.drain!!.hClose = (x!![5] / UCF(RAINDEPTH))
    LidProcs!![j]!!.drain!!.qCurve = i
    return 0
}

// lid.c:794
fun readDrainMatData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(3)
    if ((ntoks < 5)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if ((LidProcs!![j]!!.lidType != GREEN_ROOF)) {
        return 0
    }
    i = 2
    f1@ while ((i < 5)) {
        if (((!(getDouble(toks!![i], DElem(x!!, (i - 2))) != 0)) || (x!![(i - 2)] < 0.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        i++
    }
    LidProcs!![j]!!.drainMat!!.thickness = (x!![0] / UCF(RAINDEPTH))
    LidProcs!![j]!!.drainMat!!.voidFrac = x!![1]
    LidProcs!![j]!!.drainMat!!.roughness = x!![2]
    return 0
}

// lid.c:828
fun readRemovalsData(j: Int, toks: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 2
    var p: Int = 0
    val rmvl = DBox(0.0)
    if ((ntoks < 4)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    w1@ while ((ntoks > i)) {
        p = project_findObject(POLLUT, toks!![i])
        if ((p < 0)) {
            return error_setInpError(ERR_NAME, toks!![i])
        }
        i++
        if ((ntoks == i)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if ((((!(getDouble(toks!![i], rmvl) != 0)) || (rmvl.v < 0.0)) || (rmvl.v > 100.0))) {
            return error_setInpError(ERR_NUMBER, toks!![i])
        }
        LidProcs!![j]!!.drainRmvl!![p] = (rmvl.v / 100.0)
        i++
    }
    return 0
}

// lid.c:869
fun lid_writeSummary() {
    var j: Int = 0
    var k: Int = 0
    var pctArea: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    R.fprintf(Frpt!!.file, __L276)
    R.fprintf(Frpt!!.file, __L276)
    R.fprintf(Frpt!!.file, __L277)
    R.fprintf(Frpt!!.file, __L278)
    R.fprintf(Frpt!!.file, __L277)
    R.fprintf(Frpt!!.file, __L279)
    R.fprintf(Frpt!!.file, __L280)
    R.fprintf(Frpt!!.file, __L281)
    j = 0
    f1@ while ((j < GroupCount)) {
        lidGroup = LidGroups!![j]
        if ((lidGroup == null)) {
            j++
            continue@f1
        }
        lidList = lidGroup!!.lidList
        w2@ while ((lidList != null)) {
            lidUnit = lidList!!.lidUnit
            k = lidUnit!!.lidIndex
            pctArea = (((lidUnit!!.area * (lidUnit!!.number).toDouble()) / Subcatch!![j]!!.area) * 100.0)
            R.fprintf(Frpt!!.file, __L282, Subcatch!![j]!!.ID, LidProcs!![k]!!.ID)
            R.fprintf(Frpt!!.file, __L283, lidUnit!!.number, (lidUnit!!.area * (((UCF(LENGTH)) * (UCF(LENGTH))))), (lidUnit!!.fullWidth * UCF(LENGTH)), pctArea, (lidUnit!!.fromImperv * 100.0), (lidUnit!!.fromPerv * 100.0))
            lidList = lidList!!.nextLidUnit
        }
        j++
    }
}

// lid.c:918
fun lid_validate() {
    var j: Int = 0
    j = 0
    f1@ while ((j < LidCount)) {
        validateLidProc(j)
        j++
    }
    j = 0
    f2@ while ((j < GroupCount)) {
        validateLidGroup(j)
        j++
    }
}

// lid.c:932
fun validateLidProc(j: Int) {
    var layerMissing: Int = 0
    if ((LidProcs!![j]!!.lidType < 0)) {
        report_writeErrorMsg(ERR_LID_TYPE, LidProcs!![j]!!.ID)
        return
    }
    when (LidProcs!![j]!!.lidType) {
        BIO_CELL, RAIN_GARDEN -> {
            if ((LidProcs!![j]!!.soil!!.thickness <= 0.0)) {
                layerMissing = 1
            }
        }
        GREEN_ROOF -> {
            if ((LidProcs!![j]!!.soil!!.thickness <= 0.0)) {
                layerMissing = 1
            }
            if ((LidProcs!![j]!!.drainMat!!.thickness <= 0.0)) {
                layerMissing = 1
            }
        }
        POROUS_PAVEMENT -> {
            if ((LidProcs!![j]!!.pavement!!.thickness <= 0.0)) {
                layerMissing = 1
            }
        }
        INFIL_TRENCH -> {
            if ((LidProcs!![j]!!.storage!!.thickness <= 0.0)) {
                layerMissing = 1
            }
        }
    }
    if ((layerMissing != 0)) {
        report_writeErrorMsg(ERR_LID_LAYER, LidProcs!![j]!!.ID)
        return
    }
    if ((LidProcs!![j]!!.lidType == POROUS_PAVEMENT)) {
        if ((((((LidProcs!![j]!!.pavement!!.thickness <= 0.0) || (LidProcs!![j]!!.pavement!!.kSat <= 0.0)) || (LidProcs!![j]!!.pavement!!.voidFrac <= 0.0)) || (LidProcs!![j]!!.pavement!!.voidFrac > 1.0)) || (LidProcs!![j]!!.pavement!!.impervFrac > 1.0))) {
            sstrncpy(Msg, LidProcs!![j]!!.ID, 1024L)
            sstrcat(Msg, __L284, 1024L)
            report_writeErrorMsg(ERR_LID_PARAMS, Msg)
        }
    }
    if ((LidProcs!![j]!!.soil!!.thickness > 0.0)) {
        if ((((((LidProcs!![j]!!.soil!!.porosity <= 0.0) || (LidProcs!![j]!!.soil!!.fieldCap >= LidProcs!![j]!!.soil!!.porosity)) || (LidProcs!![j]!!.soil!!.wiltPoint >= LidProcs!![j]!!.soil!!.fieldCap)) || (LidProcs!![j]!!.soil!!.kSat <= 0.0)) || (LidProcs!![j]!!.soil!!.kSlope < 0.0))) {
            sstrncpy(Msg, LidProcs!![j]!!.ID, 1024L)
            sstrcat(Msg, __L285, 1024L)
            report_writeErrorMsg(ERR_LID_PARAMS, Msg)
        }
    }
    if ((LidProcs!![j]!!.storage!!.thickness > 0.0)) {
        if (((LidProcs!![j]!!.storage!!.voidFrac <= 0.0) || (LidProcs!![j]!!.storage!!.voidFrac > 1.0))) {
            sstrncpy(Msg, LidProcs!![j]!!.ID, 1024L)
            sstrcat(Msg, __L286, 1024L)
            report_writeErrorMsg(ERR_LID_PARAMS, Msg)
        }
    } else {
        LidProcs!![j]!!.storage!!.voidFrac = 1.0
        LidProcs!![j]!!.drain!!.offset = 0.0
    }
    if (((LidProcs!![j]!!.drain!!.hOpen > 0.0) && (LidProcs!![j]!!.drain!!.hOpen <= LidProcs!![j]!!.drain!!.hClose))) {
        sstrncpy(Msg, LidProcs!![j]!!.ID, 1024L)
        sstrcat(Msg, __L287, 1024L)
        report_writeErrorMsg(ERR_LID_PARAMS, Msg)
    }
    if ((LidProcs!![j]!!.lidType == VEG_SWALE)) {
        if ((((LidProcs!![j]!!.surface!!.roughness * LidProcs!![j]!!.surface!!.surfSlope) <= 0.0) || (LidProcs!![j]!!.surface!!.thickness == 0.0))) {
            sstrncpy(Msg, LidProcs!![j]!!.ID, 1024L)
            sstrcat(Msg, __L288, 1024L)
            report_writeErrorMsg(ERR_LID_PARAMS, Msg)
        } else {
            LidProcs!![j]!!.surface!!.alpha = ((1.49 * sqrt(LidProcs!![j]!!.surface!!.surfSlope)) / LidProcs!![j]!!.surface!!.roughness)
        }
    } else {
        if ((LidProcs!![j]!!.surface!!.roughness > 0.0)) {
            LidProcs!![j]!!.surface!!.alpha = ((1.49 / LidProcs!![j]!!.surface!!.roughness) * sqrt(LidProcs!![j]!!.surface!!.surfSlope))
        } else {
            LidProcs!![j]!!.surface!!.alpha = 0.0
        }
    }
    if ((LidProcs!![j]!!.drainMat!!.roughness > 0.0)) {
        LidProcs!![j]!!.drainMat!!.alpha = ((1.49 / LidProcs!![j]!!.drainMat!!.roughness) * sqrt(LidProcs!![j]!!.surface!!.surfSlope))
    } else {
        LidProcs!![j]!!.drainMat!!.alpha = 0.0
    }
    if ((LidProcs!![j]!!.pavement!!.thickness > 0.0)) {
        LidProcs!![j]!!.pavement!!.clogFactor *= ((LidProcs!![j]!!.pavement!!.thickness * LidProcs!![j]!!.pavement!!.voidFrac) * ((1.0 - LidProcs!![j]!!.pavement!!.impervFrac)))
    }
    if ((LidProcs!![j]!!.storage!!.thickness > 0.0)) {
        LidProcs!![j]!!.storage!!.clogFactor *= (LidProcs!![j]!!.storage!!.thickness * LidProcs!![j]!!.storage!!.voidFrac)
    } else {
        LidProcs!![j]!!.storage!!.clogFactor = 0.0
    }
    LidProcs!![j]!!.surface!!.canOverflow = 1
    when (LidProcs!![j]!!.lidType) {
        ROOF_DISCON -> {
            LidProcs!![j]!!.surface!!.canOverflow = 0
        }
        INFIL_TRENCH, POROUS_PAVEMENT, BIO_CELL, RAIN_GARDEN, GREEN_ROOF -> {
            if ((LidProcs!![j]!!.surface!!.alpha > 0.0)) {
                LidProcs!![j]!!.surface!!.canOverflow = 0
            }
        }
    }
    if ((LidProcs!![j]!!.lidType == RAIN_BARREL)) {
        LidProcs!![j]!!.storage!!.voidFrac = 1.0
        LidProcs!![j]!!.storage!!.kSat = 0.0
    }
    if ((LidProcs!![j]!!.lidType == GREEN_ROOF)) {
        LidProcs!![j]!!.storage!!.thickness = LidProcs!![j]!!.drainMat!!.thickness
        LidProcs!![j]!!.storage!!.voidFrac = LidProcs!![j]!!.drainMat!!.voidFrac
        LidProcs!![j]!!.storage!!.clogFactor = 0.0
        LidProcs!![j]!!.storage!!.kSat = 0.0
    }
}

// lid.c:1113
fun validateLidGroup(j: Int) {
    var k: Int = 0
    var p: DoubleArray? = DoubleArray(3)
    var totalArea: Double = Subcatch!![j]!!.area
    var totalLidArea: Double = 0.0
    var fromImperv: Double = 0.0
    var fromPerv: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if ((lidGroup == null)) {
        return
    }
    lidList = lidGroup!!.lidList
    w1@ while ((lidList != null)) {
        lidUnit = lidList!!.lidUnit
        k = lidUnit!!.lidIndex
        totalLidArea += ((lidUnit!!.area * (lidUnit!!.number).toDouble()))
        fromImperv += lidUnit!!.fromImperv
        fromPerv += lidUnit!!.fromPerv
        lidUnit!!.soilInfil!!.Ks = 0.0
        if ((LidProcs!![k]!!.soil!!.thickness > 0.0)) {
            p!![0] = (LidProcs!![k]!!.soil!!.suction * UCF(RAINDEPTH))
            p!![1] = (LidProcs!![k]!!.soil!!.kSat * UCF(RAINFALL))
            p!![2] = (((LidProcs!![k]!!.soil!!.porosity - LidProcs!![k]!!.soil!!.wiltPoint)) * ((1.0 - lidUnit!!.initSat)))
            if ((grnampt_setParams(lidUnit!!.soilInfil, p) == 0)) {
                sstrncpy(Msg, LidProcs!![k]!!.ID, 1024L)
                sstrcat(Msg, __L285, 1024L)
                report_writeErrorMsg(ERR_LID_PARAMS, Msg)
            }
        }
        if ((LidProcs!![k]!!.lidType == VEG_SWALE)) {
            if (((Subcatch!![j]!!.infilModel == GREEN_AMPT) || (Subcatch!![j]!!.infilModel == MOD_GREEN_AMPT))) {
                grnampt_getParams(j, p)
                if ((grnampt_setParams(lidUnit!!.soilInfil, p) == 0)) {
                    sstrncpy(Msg, LidProcs!![k]!!.ID, 1024L)
                    sstrcat(Msg, __L289, 1024L)
                    report_writeErrorMsg(ERR_LID_PARAMS, Msg)
                }
            }
            if ((lidUnit!!.fullWidth <= 0.0)) {
                sstrncpy(Msg, LidProcs!![k]!!.ID, 1024L)
                sstrcat(Msg, __L290, 1024L)
                report_writeErrorMsg(ERR_LID_PARAMS, Msg)
            }
        }
        if ((Subcatch!![j]!!.fracImperv >= 0.999)) {
            lidUnit!!.toPerv = 0
        }
        if (((lidUnit!!.drainNode == (-1)) && (lidUnit!!.drainSubcatch == (-1)))) {
            lidUnit!!.drainNode = Subcatch!![j]!!.outNode
            lidUnit!!.drainSubcatch = Subcatch!![j]!!.outSubcatch
        }
        lidList = lidList!!.nextLidUnit
    }
    if ((totalLidArea > (1.001 * totalArea))) {
        report_writeErrorMsg(ERR_LID_AREAS, Subcatch!![j]!!.ID)
    }
    if (((fromImperv > 1.001) || (fromPerv > 1.001))) {
        report_writeErrorMsg(ERR_LID_CAPTURE_AREA, Subcatch!![j]!!.ID)
    }
    if ((totalLidArea > (0.999 * totalArea))) {
        totalLidArea = totalArea
    }
    Subcatch!![j]!!.lidArea = totalLidArea
}

// lid.c:1211
fun lid_initState() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    var initVol: Double = 0.0
    var initDryTime: Double = (StartDryDays * 86400.0)
    HasWetLids = 0
    j = 0
    f1@ while ((j < GroupCount)) {
        lidGroup = LidGroups!![j]
        if ((lidGroup == null)) {
            j++
            continue@f1
        }
        lidGroup!!.pervArea = 0.0
        lidGroup!!.flowToPerv = 0.0
        lidGroup!!.oldDrainFlow = 0.0
        lidGroup!!.newDrainFlow = 0.0
        lidList = lidGroup!!.lidList
        w2@ while ((lidList != null)) {
            lidUnit = lidList!!.lidUnit
            k = lidUnit!!.lidIndex
            lidUnit!!.surfaceDepth = 0.0
            lidUnit!!.storageDepth = 0.0
            lidUnit!!.soilMoisture = 0.0
            lidUnit!!.paveDepth = 0.0
            lidUnit!!.dryTime = initDryTime
            lidUnit!!.volTreated = 0.0
            lidUnit!!.nextRegenDay = LidProcs!![k]!!.pavement!!.regenDays
            initVol = 0.0
            if ((LidProcs!![k]!!.soil!!.thickness > 0.0)) {
                lidUnit!!.soilMoisture = (LidProcs!![k]!!.soil!!.wiltPoint + (lidUnit!!.initSat * ((LidProcs!![k]!!.soil!!.porosity - LidProcs!![k]!!.soil!!.wiltPoint))))
                initVol += (lidUnit!!.soilMoisture * LidProcs!![k]!!.soil!!.thickness)
            }
            if ((LidProcs!![k]!!.storage!!.thickness > 0.0)) {
                lidUnit!!.storageDepth = (lidUnit!!.initSat * LidProcs!![k]!!.storage!!.thickness)
                initVol += (lidUnit!!.storageDepth * LidProcs!![k]!!.storage!!.voidFrac)
            }
            if ((lidUnit!!.initSat > 0.0)) {
                HasWetLids = 1
            }
            lidproc_initWaterBalance(lidUnit, initVol)
            lidUnit!!.volTreated = 0.0
            if ((lidUnit!!.rptFile != null)) {
                initLidRptFile(Title!![0], LidProcs!![k]!!.ID, Subcatch!![j]!!.ID, lidUnit)
            }
            lidUnit!!.oldDrainFlow = 0.0
            lidUnit!!.newDrainFlow = 0.0
            i = 0
            f3@ while ((i < 4)) {
                lidUnit!!.oldFluxRates!![i] = 0.0
                i++
            }
            if ((lidUnit!!.soilInfil!!.Ks > 0.0)) {
                grnampt_initState(lidUnit!!.soilInfil)
            }
            if ((isLidPervious(lidUnit!!.lidIndex) != 0)) {
                lidGroup!!.pervArea += ((lidUnit!!.area * (lidUnit!!.number).toDouble()))
            }
            lidList = lidList!!.nextLidUnit
        }
        j++
    }
}

// lid.c:1302
fun lid_setOldGroupState(j: Int) {
    var lidList: TLidList? = null
    if ((LidGroups!![j] != null)) {
        LidGroups!![j]!!.oldDrainFlow = LidGroups!![j]!!.newDrainFlow
        LidGroups!![j]!!.newDrainFlow = 0.0
        lidList = LidGroups!![j]!!.lidList
        w1@ while ((lidList != null)) {
            lidList!!.lidUnit!!.oldDrainFlow = lidList!!.lidUnit!!.newDrainFlow
            lidList!!.lidUnit!!.newDrainFlow = 0.0
            lidList = lidList!!.nextLidUnit
        }
    }
}

// lid.c:1326
fun isLidPervious(k: Int): Int {
    return (if ((((LidProcs!![k]!!.storage!!.thickness == 0.0) || (LidProcs!![k]!!.storage!!.kSat > 0.0)))) 1 else 0)
}

// lid.c:1339
fun getSurfaceDepth(j: Int): Double {
    var k: Int = 0
    var depth: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if ((lidGroup == null)) {
        return 0.0
    }
    if ((Subcatch!![j]!!.lidArea == 0.0)) {
        return 0.0
    }
    lidList = lidGroup!!.lidList
    w1@ while ((lidList != null)) {
        lidUnit = lidList!!.lidUnit
        k = lidUnit!!.lidIndex
        depth += (((lidUnit!!.surfaceDepth * LidProcs!![k]!!.surface!!.voidFrac) * lidUnit!!.area) * (lidUnit!!.number).toDouble())
        lidList = lidList!!.nextLidUnit
    }
    return (depth / Subcatch!![j]!!.lidArea)
}

// lid.c:1370
fun lid_getPervArea(j: Int): Double {
    if ((LidGroups!![j] != null)) {
        return LidGroups!![j]!!.pervArea
    } else {
        return 0.0
    }
    throw IllegalStateException("lid_getPervArea: no return")
}

// lid.c:1383
fun lid_getFlowToPerv(j: Int): Double {
    if ((LidGroups!![j] != null)) {
        return LidGroups!![j]!!.flowToPerv
    }
    return 0.0
}

// lid.c:1397
fun lid_getStoredVolume(j: Int): Double {
    var total: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if (((lidGroup == null) || (Subcatch!![j]!!.lidArea == 0.0))) {
        return 0.0
    }
    lidList = lidGroup!!.lidList
    w1@ while ((lidList != null)) {
        lidUnit = lidList!!.lidUnit
        total += ((lidUnit!!.waterBalance!!.finalVol * lidUnit!!.area) * (lidUnit!!.number).toDouble())
        lidList = lidList!!.nextLidUnit
    }
    return total
}

// lid.c:1424
fun lid_getDrainFlow(j: Int, timePeriod: Int): Double {
    if ((LidGroups!![j] != null)) {
        if ((timePeriod == PREVIOUS)) {
            return LidGroups!![j]!!.oldDrainFlow
        } else {
            return LidGroups!![j]!!.newDrainFlow
        }
    }
    return 0.0
}

// lid.c:1442
fun lid_addDrainLoads(j: Int, c: DoubleArray?, tStep: Double) {
    var isRunoffLoad: Int = 0
    var p: Int = 0
    var r: Double = 0.0
    var w: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if ((lidGroup != null)) {
        lidList = lidGroup!!.lidList
        w1@ while ((lidList != null)) {
            lidUnit = lidList!!.lidUnit
            isRunoffLoad = (if ((((lidUnit!!.drainNode >= 0) || (lidUnit!!.drainSubcatch == j)))) 1 else 0)
            if (!(lidUnit!!.toPerv != 0)) {
                p = 0
                f2@ while ((p < Nobjects!![POLLUT])) {
                    w = ((((lidUnit!!.newDrainFlow * c!![p]) * tStep) * 28.317) * Pollut!![p]!!.mcf)
                    r = LidProcs!![lidUnit!!.lidIndex]!!.drainRmvl!![p]
                    massbal_updateLoadingTotals(BMP_REMOVAL_LOAD, p, (r * w))
                    if ((isRunoffLoad != 0)) {
                        massbal_updateLoadingTotals(RUNOFF_LOAD, p, (w * ((1.0 - r))))
                    }
                    p++
                }
            }
            lidList = lidList!!.nextLidUnit
        }
    }
}

// lid.c:1497
fun lid_addDrainRunon(j: Int) {
    var i: Int = 0
    var k: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if ((lidGroup != null)) {
        lidList = lidGroup!!.lidList
        w1@ while ((lidList != null)) {
            lidUnit = lidList!!.lidUnit
            i = lidUnit!!.lidIndex
            k = lidUnit!!.drainSubcatch
            if (((k >= 0) && (k != j))) {
                q = lidUnit!!.oldDrainFlow
                subcatch_addRunonFlow(k, q)
                p = 0
                f2@ while ((p < Nobjects!![POLLUT])) {
                    w = ((q * Subcatch!![j]!!.oldQual!![p]) * 28.317)
                    w = (w * ((1.0 - LidProcs!![i]!!.drainRmvl!![p])))
                    Subcatch!![k]!!.newQual!![p] += w
                    p++
                }
            }
            lidList = lidList!!.nextLidUnit
        }
    }
}

// lid.c:1549
fun lid_addDrainInflow(j: Int, f: Double) {
    var i: Int = 0
    var k: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var w1: Double = 0.0
    var w2: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    lidGroup = LidGroups!![j]
    if ((lidGroup != null)) {
        lidList = lidGroup!!.lidList
        w1@ while ((lidList != null)) {
            lidUnit = lidList!!.lidUnit
            i = lidUnit!!.lidIndex
            k = lidUnit!!.drainNode
            if ((k >= 0)) {
                q = ((((1.0 - f)) * lidUnit!!.oldDrainFlow) + (f * lidUnit!!.newDrainFlow))
                Node!![k]!!.newLatFlow += q
                massbal_addInflowFlow(WET_WEATHER_INFLOW, q)
                p = 0
                f2@ while ((p < Nobjects!![POLLUT])) {
                    w1 = (lidUnit!!.oldDrainFlow * Subcatch!![j]!!.oldQual!![p])
                    w2 = (lidUnit!!.newDrainFlow * Subcatch!![j]!!.newQual!![p])
                    w = ((((1.0 - f)) * w1) + (f * w2))
                    w = (w * ((1.0 - LidProcs!![i]!!.drainRmvl!![p])))
                    Node!![k]!!.newQual!![p] += w
                    massbal_addInflowQual(WET_WEATHER_INFLOW, p, w)
                    p++
                }
            }
            lidList = lidList!!.nextLidUnit
        }
    }
}

// lid.c:1609
fun lid_getRunoff(j: Int, tStep: Double) {
    var theLidGroup: LidGroup? = null
    var lidList: TLidList? = null
    var lidUnit: TLidUnit? = null
    var lidArea: Double = 0.0
    var qImperv: Double = 0.0
    var qPerv: Double = 0.0
    var lidInflow: Double = 0.0
    val qRunoff = DBox(0.0)
    val qDrain = DBox(0.0)
    val qReturn = DBox(0.0)
    theLidGroup = LidGroups!![j]
    if (!(theLidGroup != null)) {
        return
    }
    lidList = theLidGroup!!.lidList
    if (!(lidList != null)) {
        return
    }
    lid_EvapRate = Evap!!.rate
    if (((Evap!!.dryOnly != 0) && (Subcatch!![j]!!.rainfall > 0.0))) {
        lid_EvapRate = 0.0
    }
    findNativeInfil(j, tStep)
    if ((Subcatch!![j]!!.area > Subcatch!![j]!!.lidArea)) {
        qImperv = getImpervAreaRunoff(j)
        qPerv = getPervAreaRunoff(j)
    }
    w1@ while ((lidList != null)) {
        lidUnit = lidList!!.lidUnit
        lidArea = (lidUnit!!.area * (lidUnit!!.number).toDouble())
        if ((lidArea > 0.0)) {
            lidInflow = ((((qImperv * lidUnit!!.fromImperv) + (qPerv * lidUnit!!.fromPerv))) / lidArea)
            VlidIn += ((lidInflow * lidArea) * tStep)
            lidInflow = (lidInflow + getRainInflow(j, lidUnit))
            if ((Subcatch!![j]!!.area == Subcatch!![j]!!.lidArea)) {
                lidInflow += Subcatch!![j]!!.runon
            }
            evalLidUnit(j, lidUnit, lidArea, lidInflow, tStep, qRunoff, qDrain, qReturn)
        }
        lidList = lidList!!.nextLidUnit
    }
    theLidGroup!!.newDrainFlow = qDrain.v
    theLidGroup!!.flowToPerv = qReturn.v
    VlidOut = (qRunoff.v * tStep)
    VlidDrain = (qDrain.v * tStep)
    VlidReturn = (qReturn.v * tStep)
}

// lid.c:1697
fun findNativeInfil(j: Int, tStep: Double) {
    var nonLidArea: Double = 0.0
    nonLidArea = (Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)
    if (((nonLidArea > 0.0) && (Subcatch!![j]!!.fracImperv < 1.0))) {
        NativeInfil = ((Vinfil / nonLidArea) / tStep)
    } else {
        NativeInfil = infil_getInfil(j, tStep, Subcatch!![j]!!.rainfall, Subcatch!![j]!!.runon, getSurfaceDepth(j))
    }
    if (((!(IgnoreGwater != 0)) && (Subcatch!![j]!!.groundwater != null))) {
        lid_MaxNativeInfil = (Subcatch!![j]!!.groundwater!!.maxInfilVol / tStep)
    } else {
        lid_MaxNativeInfil = 10000000000.0
    }
}

// lid.c:1734
fun getRainInflow(j: Int, lidUnit: TLidUnit?): Double {
    var lidProc: TLidProc? = LidProcs!![lidUnit!!.lidIndex]
    if (((lidProc!!.lidType == RAIN_BARREL) && (lidProc!!.storage!!.covered == 1))) {
        return 0.0
    }
    return Subcatch!![j]!!.rainfall
}

// lid.c:1751
fun getImpervAreaRunoff(j: Int): Double {
    var i: Int = 0
    var q: Double = 0.0
    var nonLidArea: Double = 0.0
    i = IMPERV0
    f1@ while ((i <= IMPERV1)) {
        q += (Subcatch!![j]!!.subArea!![i]!!.runoff * Subcatch!![j]!!.subArea!![i]!!.fArea)
        i++
    }
    if (((Subcatch!![j]!!.subArea!![IMPERV0]!!.routeTo == TO_PERV) && (Subcatch!![j]!!.fracImperv < 1.0))) {
        q *= Subcatch!![j]!!.subArea!![IMPERV0]!!.fOutlet
    }
    nonLidArea = (Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)
    return (q * nonLidArea)
}

// lid.c:1781
fun getPervAreaRunoff(j: Int): Double {
    var q: Double = 0.0
    var nonLidArea: Double = 0.0
    q = (Subcatch!![j]!!.subArea!![PERV]!!.runoff * Subcatch!![j]!!.subArea!![PERV]!!.fArea)
    if (((Subcatch!![j]!!.subArea!![PERV]!!.routeTo == TO_IMPERV) && (Subcatch!![j]!!.fracImperv > 0.0))) {
        q *= Subcatch!![j]!!.subArea!![PERV]!!.fOutlet
    }
    nonLidArea = (Subcatch!![j]!!.area - Subcatch!![j]!!.lidArea)
    return (q * nonLidArea)
}

// lid.c:1807
fun evalLidUnit(j: Int, lidUnit: TLidUnit?, lidArea: Double, lidInflow: Double, tStep: Double, qRunoff: DRef?, qDrain: DRef?, qReturn: DRef?) {
    var lidProc: TLidProc? = null
    var lidRunoff: Double = 0.0
    val lidEvap = DBox(0.0)
    val lidInfil = DBox(0.0)
    val lidDrain = DBox(0.0)
    lidProc = LidProcs!![lidUnit!!.lidIndex]
    lidEvap.v = 0.0
    lidInfil.v = 0.0
    lidRunoff = (lidproc_getOutflow(lidUnit, lidProc, lidInflow, lid_EvapRate, NativeInfil, lid_MaxNativeInfil, tStep, lidEvap, lidInfil, lidDrain) * lidArea)
    lidDrain.v *= lidArea
    if (((lidUnit!!.toPerv != 0) && (Subcatch!![j]!!.area > Subcatch!![j]!!.lidArea))) {
        qReturn!!.set((qReturn!!.get() + lidRunoff))
        lidRunoff = 0.0
        if (((lidUnit!!.drainNode == Subcatch!![j]!!.outNode) && (lidUnit!!.drainSubcatch == Subcatch!![j]!!.outSubcatch))) {
            qReturn!!.set((qReturn!!.get() + lidDrain.v))
            lidDrain.v = 0.0
        }
    }
    if ((lidUnit!!.drainNode >= 0)) {
        massbal_updateRunoffTotals(RUNOFF_DRAINS, (lidDrain.v * tStep))
    }
    lidUnit!!.newDrainFlow = lidDrain.v
    Vevap += ((lidEvap.v * tStep) * lidArea)
    VlidInfil += ((lidInfil.v * tStep) * lidArea)
    if ((isLidPervious(lidUnit!!.lidIndex) != 0)) {
        Vpevap += ((lidEvap.v * tStep) * lidArea)
    }
    if ((Subcatch!![j]!!.rainfall > 2.31481e-08)) {
        lidUnit!!.dryTime = 0.0
    } else {
        lidUnit!!.dryTime += tStep
    }
    lidproc_saveResults(lidUnit, UCF(RAINFALL), UCF(RAINDEPTH))
    qRunoff!!.set((qRunoff!!.get() + lidRunoff))
    qDrain!!.set((qDrain!!.get() + lidDrain.v))
}

// lid.c:1890
fun lid_writeWaterBalance() {
    var j: Int = 0
    var k: Int = 0
    var ucf: Double = UCF(RAINDEPTH)
    var inflow: Double = 0.0
    var outflow: Double = 0.0
    var err: Double = 0.0
    var lidUnit: TLidUnit? = null
    var lidList: TLidList? = null
    var lidGroup: LidGroup? = null
    j = 0
    f1@ while ((j < GroupCount)) {
        if ((LidGroups!![j] != null)) {
            k++
        }
        j++
    }
    if ((k == 0)) {
        return
    }
    R.fprintf(Frpt!!.file, __L291)
    R.fprintf(Frpt!!.file, __L292)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L293)
    } else {
        R.fprintf(Frpt!!.file, __L294)
    }
    R.fprintf(Frpt!!.file, __L295)
    j = 0
    f2@ while ((j < GroupCount)) {
        lidGroup = LidGroups!![j]
        if (((!(lidGroup != null)) || (Subcatch!![j]!!.lidArea == 0.0))) {
            j++
            continue@f2
        }
        lidList = lidGroup!!.lidList
        w3@ while ((lidList != null)) {
            lidUnit = lidList!!.lidUnit
            k = lidUnit!!.lidIndex
            R.fprintf(Frpt!!.file, __L296, Subcatch!![j]!!.ID, LidProcs!![k]!!.ID)
            R.fprintf(Frpt!!.file, __L297, (lidUnit!!.waterBalance!!.inflow * ucf), (lidUnit!!.waterBalance!!.evap * ucf), (lidUnit!!.waterBalance!!.infil * ucf), (lidUnit!!.waterBalance!!.surfFlow * ucf), (lidUnit!!.waterBalance!!.drainFlow * ucf), (lidUnit!!.waterBalance!!.initVol * ucf), (lidUnit!!.waterBalance!!.finalVol * ucf))
            inflow = (lidUnit!!.waterBalance!!.initVol + lidUnit!!.waterBalance!!.inflow)
            outflow = ((((lidUnit!!.waterBalance!!.finalVol + lidUnit!!.waterBalance!!.evap) + lidUnit!!.waterBalance!!.infil) + lidUnit!!.waterBalance!!.surfFlow) + lidUnit!!.waterBalance!!.drainFlow)
            if ((inflow > 0.0)) {
                err = (((inflow - outflow)) / inflow)
            } else {
                err = 1.0
            }
            R.fprintf(Frpt!!.file, __L298, (err * 100.0))
            lidList = lidList!!.nextLidUnit
        }
        j++
    }
}

// lid.c:1972
fun initLidRptFile(title: CPtr?, lidID: CPtr?, subcatchID: CPtr?, lidUnit: TLidUnit?) {
    var i: Int = 0
    var f: CFile? = lidUnit!!.rptFile!!.file
    if ((f == null)) {
        return
    }
    R.fprintf(f, __L299)
    R.fprintf(f, __L300, title)
    R.fprintf(f, __L301, lidID, subcatchID)
    i = 0
    f1@ while ((i < initLidRptFile_colCount)) {
        R.fprintf(f, __L169, initLidRptFile_head1!![i])
        i++
    }
    i = 0
    f2@ while ((i < initLidRptFile_colCount)) {
        R.fprintf(f, __L169, initLidRptFile_head2!![i])
        i++
    }
    if ((UnitSystem == US)) {
        i = 0
        f3@ while ((i < initLidRptFile_colCount)) {
            R.fprintf(f, __L169, initLidRptFile_units1!![i])
            i++
        }
    } else {
        i = 0
        f4@ while ((i < initLidRptFile_colCount)) {
            R.fprintf(f, __L169, initLidRptFile_units2!![i])
            i++
        }
    }
    R.fprintf(f, __L302)
    i = 1
    f5@ while ((i < initLidRptFile_colCount)) {
        R.fprintf(f, __L303, initLidRptFile_line9)
        i++
    }
    lidUnit!!.rptFile!!.wasDry = 1
    sstrncpy(lidUnit!!.rptFile!!.results, __L0, 0L)
}

