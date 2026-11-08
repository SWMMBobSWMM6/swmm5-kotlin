// Generated from EPA SWMM 5.2.4 (node.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_node")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// node.c:102
fun node_readParams(j: Int, type: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    when (type) {
        JUNCTION -> {
            return junc_readParams(j, k, tok, ntoks)
        }
        OUTFALL -> {
            return outfall_readParams(j, k, tok, ntoks)
        }
        STORAGE -> {
            return storage_readParams(j, k, tok, ntoks)
        }
        DIVIDER -> {
            return divider_readParams(j, k, tok, ntoks)
        }
        else -> {
            return 0
        }
    }
    throw IllegalStateException("node_readParams: no return")
}

// node.c:125
fun node_setParams(j: Int, type: Int, k: Int, x: DoubleArray?) {
    Node!![j]!!.type = type
    Node!![j]!!.subIndex = k
    Node!![j]!!.invertElev = (x!![0] / UCF(LENGTH))
    Node!![j]!!.crownElev = Node!![j]!!.invertElev
    Node!![j]!!.initDepth = 0.0
    Node!![j]!!.newVolume = 0.0
    Node!![j]!!.fullVolume = 0.0
    Node!![j]!!.fullDepth = 0.0
    Node!![j]!!.surDepth = 0.0
    Node!![j]!!.pondedArea = 0.0
    Node!![j]!!.degree = 0
    Node!![j]!!.inlet = NO_INLET
    when (type) {
        JUNCTION -> {
            Node!![j]!!.fullDepth = (x!![1] / UCF(LENGTH))
            Node!![j]!!.initDepth = (x!![2] / UCF(LENGTH))
            Node!![j]!!.surDepth = (x!![3] / UCF(LENGTH))
            Node!![j]!!.pondedArea = (x!![4] / ((UCF(LENGTH) * UCF(LENGTH))))
        }
        OUTFALL -> {
            Outfall!![k]!!.type = R.D2I(x!![1])
            Outfall!![k]!!.fixedStage = (x!![2] / UCF(LENGTH))
            Outfall!![k]!!.tideCurve = R.D2I(x!![3])
            Outfall!![k]!!.stageSeries = R.D2I(x!![4])
            Outfall!![k]!!.hasFlapGate = (R.D2I(x!![5])).toByte().toInt()
            Outfall!![k]!!.routeTo = R.D2I(x!![6])
            Outfall!![k]!!.wRouted = (null)
            if ((Outfall!![k]!!.routeTo >= 0)) {
                Outfall!![k]!!.wRouted = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
            }
        }
        STORAGE -> {
            Node!![j]!!.fullDepth = (x!![1] / UCF(LENGTH))
            Node!![j]!!.initDepth = (x!![2] / UCF(LENGTH))
            Storage!![k]!!.shape = R.D2I(x!![3])
            Storage!![k]!!.a1 = x!![4]
            Storage!![k]!!.a2 = x!![5]
            Storage!![k]!!.a0 = x!![6]
            Storage!![k]!!.aCurve = R.D2I(x!![7])
            Node!![j]!!.surDepth = (x!![8] / UCF(LENGTH))
            Storage!![k]!!.fEvap = x!![9]
        }
        DIVIDER -> {
            Divider!![k]!!.link = R.D2I(x!![1])
            Divider!![k]!!.type = R.D2I(x!![2])
            Divider!![k]!!.flowCurve = R.D2I(x!![3])
            Divider!![k]!!.qMin = (x!![4] / UCF(FLOW))
            Divider!![k]!!.dhMax = x!![5]
            Divider!![k]!!.cWeir = x!![6]
            Node!![j]!!.fullDepth = (x!![7] / UCF(LENGTH))
            Node!![j]!!.initDepth = (x!![8] / UCF(LENGTH))
            Node!![j]!!.surDepth = (x!![9] / UCF(LENGTH))
            Node!![j]!!.pondedArea = (x!![10] / ((UCF(LENGTH) * UCF(LENGTH))))
        }
    }
}

// node.c:200
fun node_validate(j: Int) {
    var inflow: TDwfInflow? = null
    if (((Node!![j]!!.fullDepth > Node!![j]!!.oldDepth) && (Node!![j]!!.oldDepth > 0.0))) {
        report_writeWarningMsg(__L317, Node!![j]!!.ID)
    }
    if ((Node!![j]!!.initDepth > (Node!![j]!!.fullDepth + Node!![j]!!.surDepth))) {
        report_writeErrorMsg(ERR_NODE_DEPTH, Node!![j]!!.ID)
    }
    if ((Node!![j]!!.type == STORAGE)) {
        if ((node_getVolume(j, Node!![j]!!.fullDepth) < 0.0)) {
            report_writeErrorMsg(ERR_STORAGE_VOLUME, Node!![j]!!.ID)
        }
    }
    if ((Node!![j]!!.type == DIVIDER)) {
        divider_validate(j)
    }
    inflow = Node!![j]!!.dwfInflow
    w1@ while ((inflow != null)) {
        inflow_initDwfInflow(inflow)
        inflow = inflow!!.next
    }
}

// node.c:237
fun node_initState(j: Int) {
    var p: Int = 0
    var k: Int = 0
    Node!![j]!!.oldDepth = Node!![j]!!.initDepth
    Node!![j]!!.newDepth = Node!![j]!!.oldDepth
    Node!![j]!!.crownElev = Node!![j]!!.invertElev
    Node!![j]!!.fullVolume = node_getVolume(j, Node!![j]!!.fullDepth)
    Node!![j]!!.oldVolume = node_getVolume(j, Node!![j]!!.oldDepth)
    Node!![j]!!.newVolume = Node!![j]!!.oldVolume
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        Node!![j]!!.oldQual!![p] = 0.0
        Node!![j]!!.newQual!![p] = 0.0
        p++
    }
    Node!![j]!!.oldLatFlow = 0.0
    Node!![j]!!.newLatFlow = 0.0
    Node!![j]!!.apiExtInflow = 0.0
    Node!![j]!!.losses = 0.0
    if ((Node!![j]!!.type == STORAGE)) {
        k = Node!![j]!!.subIndex
        Storage!![k]!!.hrt = 0.0
        if ((Storage!![k]!!.exfil != null)) {
            exfil_initState(k)
        }
    }
    if ((Node!![j]!!.type == OUTFALL)) {
        k = Node!![j]!!.subIndex
        if ((Outfall!![k]!!.routeTo >= 0)) {
            Outfall!![k]!!.vRouted = 0.0
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                Outfall!![k]!!.wRouted!![p] = 0.0
                p++
            }
        }
    }
}

// node.c:293
fun node_setOldHydState(j: Int) {
    Node!![j]!!.oldDepth = Node!![j]!!.newDepth
    Node!![j]!!.oldVolume = Node!![j]!!.newVolume
    Node!![j]!!.oldFlowInflow = Node!![j]!!.inflow
    Node!![j]!!.oldNetInflow = (Node!![j]!!.inflow - Node!![j]!!.outflow)
}

// node.c:308
fun node_setOldQualState(j: Int) {
    var p: Int = 0
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        Node!![j]!!.oldQual!![p] = Node!![j]!!.newQual!![p]
        Node!![j]!!.newQual!![p] = 0.0
        p++
    }
}

// node.c:325
fun node_initFlows(j: Int, tStep: Double) {
    Node!![j]!!.inflow = Node!![j]!!.newLatFlow
    Node!![j]!!.outflow = Node!![j]!!.losses
    if ((Node!![j]!!.newVolume > Node!![j]!!.fullVolume)) {
        Node!![j]!!.overflow = (((Node!![j]!!.newVolume - Node!![j]!!.fullVolume)) / tStep)
    } else {
        Node!![j]!!.overflow = 0.0
    }
}

// node.c:345
fun node_getDepth(j: Int, v: Double): Double {
    when (Node!![j]!!.type) {
        STORAGE -> {
            return storage_getDepth(j, v)
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("node_getDepth: no return")
}

// node.c:362
fun node_getVolume(j: Int, d: Double): Double {
    when (Node!![j]!!.type) {
        STORAGE -> {
            return storage_getVolume(j, d)
        }
        else -> {
            if ((Node!![j]!!.fullDepth > 0.0)) {
                return (Node!![j]!!.fullVolume * ((d / Node!![j]!!.fullDepth)))
            } else {
                return 0.0
            }
        }
    }
    throw IllegalStateException("node_getVolume: no return")
}

// node.c:383
fun node_getSurfArea(j: Int, d: Double): Double {
    when (Node!![j]!!.type) {
        STORAGE -> {
            return storage_getSurfArea(j, d)
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("node_getSurfArea: no return")
}

// node.c:400
fun node_getOutflow(j: Int, k: Int): Double {
    when (Node!![j]!!.type) {
        DIVIDER -> {
            return divider_getOutflow(j, k)
        }
        STORAGE -> {
            return storage_getOutflow(j, k)
        }
        else -> {
            return (Node!![j]!!.inflow + Node!![j]!!.overflow)
        }
    }
    throw IllegalStateException("node_getOutflow: no return")
}

// node.c:418
fun node_getMaxOutflow(j: Int, __q: Double, tStep: Double): Double {
    var q: Double = __q
    var qMax: Double = 0.0
    if ((Node!![j]!!.fullVolume > 0.0)) {
        qMax = (Node!![j]!!.inflow + (Node!![j]!!.oldVolume / tStep))
        if ((q > qMax)) {
            q = qMax
        }
    }
    return ((if ((((0.0) >= (q)))) (0.0) else (q)))
}

// node.c:438
fun node_getSystemOutflow(j: Int, isFlooded: IRef?): Double {
    var outflow: Double = 0.0
    isFlooded!!.set(0)
    if ((Node!![j]!!.type == OUTFALL)) {
        if ((Node!![j]!!.outflow == 0.0)) {
            outflow = Node!![j]!!.inflow
        } else {
            if ((Node!![j]!!.inflow == 0.0)) {
                outflow = (-Node!![j]!!.outflow)
                Node!![j]!!.inflow = abs(outflow)
            }
        }
        Node!![j]!!.overflow = 0.0
        Node!![j]!!.newVolume = 0.0
    } else if ((((RouteModel != DW) && (Node!![j]!!.degree == 0)) && (Node!![j]!!.type != STORAGE))) {
        if ((Node!![j]!!.outflow == 0.0)) {
            outflow = Node!![j]!!.inflow
        }
        Node!![j]!!.overflow = 0.0
        Node!![j]!!.newVolume = 0.0
    } else {
        if ((Node!![j]!!.newVolume <= Node!![j]!!.fullVolume)) {
            outflow = Node!![j]!!.overflow
        }
        if ((outflow > 0.0)) {
            isFlooded!!.set(1)
        }
    }
    return outflow
}

// node.c:497
fun node_getResults(j: Int, f: Double, x: FloatArray?) {
    var p: Int = 0
    var z: Double = 0.0
    var f1: Double = (1.0 - f)
    z = ((((f1 * Node!![j]!!.oldDepth) + (f * Node!![j]!!.newDepth))) * UCF(LENGTH))
    x!![NODE_DEPTH] = (z).toFloat()
    z = (Node!![j]!!.invertElev * UCF(LENGTH))
    x!![NODE_HEAD] = (x!![NODE_DEPTH] + (z).toFloat())
    z = ((((f1 * Node!![j]!!.oldVolume) + (f * Node!![j]!!.newVolume))) * UCF(VOLUME))
    x!![NODE_VOLUME] = (z).toFloat()
    z = ((((f1 * Node!![j]!!.oldLatFlow) + (f * Node!![j]!!.newLatFlow))) * UCF(FLOW))
    x!![NODE_LATFLOW] = (z).toFloat()
    z = ((((f1 * Node!![j]!!.oldFlowInflow) + (f * Node!![j]!!.inflow))) * UCF(FLOW))
    x!![NODE_INFLOW] = (z).toFloat()
    z = (Node!![j]!!.overflow * UCF(FLOW))
    x!![NODE_OVERFLOW] = (z).toFloat()
    if (!(IgnoreQuality != 0)) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            z = ((f1 * Node!![j]!!.oldQual!![p]) + (f * Node!![j]!!.newQual!![p]))
            x!![(NODE_QUAL + p)] = (z).toFloat()
            p++
        }
    }
}

// node.c:532
fun node_setOutletDepth(j: Int, yNorm: Double, yCrit: Double, z: Double) {
    when (Node!![j]!!.type) {
        STORAGE -> {
            return
        }
        OUTFALL -> {
            outfall_setOutletDepth(j, yNorm, yCrit, z)
        }
        else -> {
            if ((z > 0.0)) {
                Node!![j]!!.newDepth = 0.0
            } else {
                Node!![j]!!.newDepth = ((if ((((yNorm) <= (yCrit)))) (yNorm) else (yCrit)))
            }
        }
    }
}

// node.c:562
fun node_getPondedArea(j: Int, __d: Double): Double {
    var d: Double = __d
    var a: Double = 0.0
    if (((d <= Node!![j]!!.fullDepth) || (Node!![j]!!.pondedArea == 0.0))) {
        return node_getSurfArea(j, d)
    }
    d = (d - Node!![j]!!.fullDepth)
    a = Node!![j]!!.pondedArea
    if ((a <= 0.0)) {
        a = node_getSurfArea(j, Node!![j]!!.fullDepth)
    }
    return a
}

// node.c:589
fun node_getLosses(j: Int, tStep: Double): Double {
    if ((Node!![j]!!.type == STORAGE)) {
        return storage_getLosses(j, tStep)
    } else {
        return 0.0
    }
    throw IllegalStateException("node_getLosses: no return")
}

// node.c:606
fun junc_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    var id: CPtr? = null
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(NODE, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    i = 1
    f1@ while ((i <= 5)) {
        x!![(i - 1)] = 0.0
        if ((i < ntoks)) {
            if (!(getDouble(tok!![i], DElem(x!!, (i - 1))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
        }
        i++
    }
    i = 1
    f2@ while ((i <= 4)) {
        if ((x!![i] < 0.0)) {
            return error_setInpError(ERR_NUMBER, tok!![(i + 1)])
        }
        i++
    }
    Node!![j]!!.ID = id
    node_setParams(j, JUNCTION, k, x)
    return 0
}

// node.c:654
fun storage_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var m: Int = 0
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(10)
    var y: DoubleArray? = DoubleArray(3)
    var A: Double = 0.0
    var B: Double = 0.0
    var L: Double = 0.0
    var W: Double = 0.0
    var Z: Double = 0.0
    var id: CPtr? = null
    if ((ntoks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(NODE, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    i = 1
    f1@ while ((i <= 3)) {
        if (!(getDouble(tok!![i], DElem(x!!, (i - 1))) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        i++
    }
    m = findmatch(tok!![4], RelationWords)
    if ((m < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![4])
    }
    x!![3] = (m).toDouble()
    x!![4] = 0.0
    x!![5] = 0.0
    x!![6] = 0.0
    x!![7] = (-1.0)
    x!![8] = 0.0
    x!![9] = 0.0
    if ((m == TABULAR)) {
        i = project_findObject(CURVE, tok!![5])
        if ((i < 0)) {
            return error_setInpError(ERR_NAME, tok!![5])
        }
        x!![7] = (i).toDouble()
        n = 6
    } else {
        if ((ntoks < 8)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = 5
        f2@ while ((i <= 7)) {
            if (!(getDouble(tok!![i], DElem(y!!, (i - 5))) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
            i++
        }
        n = 8
    }
    when (m) {
        FUNCTIONAL -> {
            if ((y!![2] < 0.0)) {
                return error_setInpError(ERR_NUMBER, tok!![7])
            }
        }
        CYLINDRICAL, CONICAL, PARABOLOID, PYRAMIDAL -> {
            if ((y!![0] <= 0.0)) {
                return error_setInpError(ERR_NUMBER, tok!![5])
            }
            if ((y!![1] <= 0.0)) {
                return error_setInpError(ERR_NUMBER, tok!![6])
            }
            if ((y!![2] < 0.0)) {
                return error_setInpError(ERR_NUMBER, tok!![7])
            }
        }
    }
    if (((m == PARABOLOID) && (y!![2] == 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![7])
    }
    when (m) {
        FUNCTIONAL -> {
            x!![4] = y!![0]
            x!![5] = y!![1]
            x!![6] = y!![2]
        }
        CYLINDRICAL -> {
            A = (y!![0] / 2.0)
            B = (y!![1] / 2.0)
            x!![4] = 0.0
            x!![5] = 0.0
            x!![6] = ((3.141592654 * A) * B)
        }
        CONICAL -> {
            A = (y!![0] / 2.0)
            B = (y!![1] / 2.0)
            Z = y!![2]
            x!![4] = (((2.0 * 3.141592654) * B) * Z)
            x!![5] = ((((3.141592654 * B) / A) * Z) * Z)
            x!![6] = ((3.141592654 * A) * B)
        }
        PARABOLOID -> {
            A = (y!![0] / 2.0)
            B = (y!![1] / 2.0)
            Z = y!![2]
            x!![4] = (((3.141592654 * A) * B) / Z)
            x!![5] = 0.0
            x!![6] = 0.0
        }
        PYRAMIDAL -> {
            L = y!![0]
            W = y!![1]
            Z = y!![2]
            x!![4] = ((2.0 * ((L + W))) * Z)
            x!![5] = ((4.0 * Z) * Z)
            x!![6] = (L * W)
        }
    }
    if ((ntoks > n)) {
        if (!(getDouble(tok!![n], DElem(x!!, 8)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![n])
        }
        n++
    }
    if ((ntoks > n)) {
        if (!(getDouble(tok!![n], DElem(x!!, 9)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![n])
        }
        n++
    }
    Node!![j]!!.ID = id
    node_setParams(j, STORAGE, k, x)
    if ((ntoks > n)) {
        return exfil_readStorageParams(k, tok, ntoks, n)
    }
    return 0
}

// node.c:813
fun storage_getDepth(j: Int, __v: Double): Double {
    var v: Double = __v
    var k: Int = Node!![j]!!.subIndex
    var i: Int = Storage!![k]!!.aCurve
    var shape: Int = Storage!![k]!!.shape
    var a0: Double = Storage!![k]!!.a0
    var a1: Double = Storage!![k]!!.a1
    var a2: Double = Storage!![k]!!.a2
    val d = DBox(0.0)
    var e: Double = 0.0
    var storageVol: TStorageVol? = TStorageVol()
    if (((Node!![j]!!.fullVolume > 0.0) && (v >= Node!![j]!!.fullVolume))) {
        return Node!![j]!!.fullDepth
    }
    if ((v == 0.0)) {
        return 0.0
    }
    v *= (UCF(VOLUME))
    storageVol!!.k = k
    storageVol!!.v = v
    when (shape) {
        TABULAR -> {
            i = Storage!![k]!!.aCurve
            if ((i >= 0)) {
                d.v = table_getStorageDepth(Curve!![i], v)
            } else {
                d.v = 0.0
            }
        }
        CYLINDRICAL -> {
            d.v = (v / a0)
        }
        PARABOLOID -> {
            d.v = sqrt(((2.0 * v) / a1))
        }
        FUNCTIONAL -> {
            if ((a2 == 0.0)) {
                d.v = (v / ((a0 + a1)))
            } else if ((a0 == 0.0)) {
                e = (1.0 / ((a2 + 1.0)))
                d.v = Glibm.pow((v / ((a1 * e))), e)
            } else if (((a2 == 1.0) && (a1 > 0.0))) {
                d.v = (((sqrt(((a0 * a0) + ((2.0 * a1) * v))) - a0)) / a1)
            } else {
                d.v = (v / ((a0 + a1)))
                findroot_Newton(0.0, (Node!![j]!!.fullDepth * UCF(LENGTH)), d, 0.001, FP4(::storage_getVolDiff), storageVol)
            }
        }
        CONICAL, PYRAMIDAL -> {
            d.v = (v / a0)
            findroot_Newton(0.0, (Node!![j]!!.fullDepth * UCF(LENGTH)), d, 0.001, FP4(::storage_getVolDiff), storageVol)
        }
        else -> {
            d.v = 0.0
        }
    }
    d.v /= (UCF(LENGTH))
    if ((d.v > Node!![j]!!.fullDepth)) {
        d.v = Node!![j]!!.fullDepth
    }
    return d.v
}

// node.c:906
fun storage_getVolDiff(y: Double, f: DRef?, df: DRef?, p: Any?) {
    var k: Int = 0
    var storageVol: TStorageVol? = null
    storageVol = (p as TStorageVol?)
    k = storageVol!!.k
    f!!.set((storage_getVolume(k, y) - storageVol!!.v))
    df!!.set(storage_getSurfArea(k, y))
}

// node.c:930
fun storage_getVolume(j: Int, __d: Double): Double {
    var d: Double = __d
    var k: Int = Node!![j]!!.subIndex
    var i: Int = 0
    var n: Double = 0.0
    var v: Double = 0.0
    if ((d == 0.0)) {
        return 0.0
    }
    if (((d >= Node!![j]!!.fullDepth) && (Node!![j]!!.fullVolume > 0.0))) {
        return Node!![j]!!.fullVolume
    }
    when (Storage!![k]!!.shape) {
        TABULAR -> {
            i = Storage!![k]!!.aCurve
            if ((i >= 0)) {
                return (table_getStorageVolume(Curve!![i], (d * UCF(LENGTH))) / UCF(VOLUME))
            } else {
                return 0.0
            }
        }
        FUNCTIONAL -> {
            d *= (UCF(LENGTH))
            n = (Storage!![k]!!.a2 + 1.0)
            v = (((Storage!![k]!!.a0 * d)) + ((Storage!![k]!!.a1 / n) * Glibm.pow(d, n)))
            return (v / UCF(VOLUME))
        }
        CYLINDRICAL, CONICAL, PARABOLOID, PYRAMIDAL -> {
            d *= (UCF(LENGTH))
            v = (d * ((Storage!![k]!!.a0 + (d * (((Storage!![k]!!.a1 / 2.0) + ((d * Storage!![k]!!.a2) / 3.0)))))))
            return (v / UCF(VOLUME))
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("storage_getVolume: no return")
}

// node.c:979
fun storage_getSurfArea(j: Int, __d: Double): Double {
    var d: Double = __d
    var area: Double = 0.0
    var k: Int = Node!![j]!!.subIndex
    var i: Int = 0
    when (Storage!![k]!!.shape) {
        TABULAR -> {
            i = Storage!![k]!!.aCurve
            if ((i >= 0)) {
                area = table_lookupEx(Curve!![i], (d * UCF(LENGTH)))
            }
        }
        FUNCTIONAL -> {
            area = (Storage!![k]!!.a0 + (Storage!![k]!!.a1 * Glibm.pow((d * UCF(LENGTH)), Storage!![k]!!.a2)))
        }
        CYLINDRICAL, CONICAL, PARABOLOID, PYRAMIDAL -> {
            d *= (UCF(LENGTH))
            area = (Storage!![k]!!.a0 + (d * ((Storage!![k]!!.a1 + (d * Storage!![k]!!.a2)))))
        }
        else -> {
            return 0.0
        }
    }
    return ((area / UCF(LENGTH)) / UCF(LENGTH))
}

// node.c:1022
fun storage_getOutflow(j: Int, i: Int): Double {
    var k: Int = 0
    var a: Double = 0.0
    var y: Double = 0.0
    if ((Link!![i]!!.type != CONDUIT)) {
        return 0.0
    }
    y = (Node!![j]!!.newDepth - Link!![i]!!.offset1)
    if ((y <= 0.0)) {
        return 0.0
    }
    if ((y >= Link!![i]!!.xsect!!.yFull)) {
        return Link!![i]!!.qFull
    }
    k = Link!![i]!!.subIndex
    a = xsect_getAofY(Link!![i]!!.xsect, y)
    return (Conduit!![k]!!.beta * xsect_getSofA(Link!![i]!!.xsect, a))
}

// node.c:1052
fun storage_getLosses(j: Int, tStep: Double): Double {
    var k: Int = 0
    var depth: Double = 0.0
    var area: Double = 0.0
    var evapRate: Double = 0.0
    var exfilRate: Double = 0.0
    var totalLoss: Double = 0.0
    var lossRatio: Double = 0.0
    var exfil: TExfil? = null
    k = Node!![j]!!.subIndex
    evapRate = (Evap!!.rate * Storage!![k]!!.fEvap)
    exfil = Storage!![k]!!.exfil
    if (((evapRate > 0.0) || (exfil != null))) {
        depth = Node!![j]!!.newDepth
        area = storage_getSurfArea(j, depth)
        if ((Node!![j]!!.newVolume > 0.0001)) {
            evapRate = (area * evapRate)
        }
        if ((exfil != null)) {
            exfilRate = exfil_getLoss(exfil, tStep, depth, area)
        }
        totalLoss = (((evapRate + exfilRate)) * tStep)
        if ((totalLoss > Node!![j]!!.newVolume)) {
            lossRatio = (Node!![j]!!.newVolume / totalLoss)
            evapRate *= lossRatio
            exfilRate *= lossRatio
        }
    }
    Storage!![Node!![j]!!.subIndex]!!.evapLoss = (evapRate * tStep)
    Storage!![Node!![j]!!.subIndex]!!.exfilLoss = (exfilRate * tStep)
    return (evapRate + exfilRate)
}

// node.c:1113
fun divider_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var m: Int = 0
    var m1: Int = 0
    var m2: Int = 0
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(11)
    var id: CPtr? = null
    if ((ntoks < 4)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(NODE, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if (!(getDouble(tok!![1], DElem(x!!, 0)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![1])
    }
    i = 1
    f1@ while ((i < 11)) {
        x!![i] = 0.0
        i++
    }
    if (((R.strlen(tok!![2]) == 0L) || (R.strcmp(tok!![2], __L275) == 0))) {
        x!![1] = (-1.0)
    } else {
        m1 = project_findObject(LINK, tok!![2])
        if ((m1 < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
        x!![1] = (m1).toDouble()
    }
    n = 4
    m1 = findmatch(tok!![3], DividerTypeWords)
    if ((m1 < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![3])
    }
    x!![2] = (m1).toDouble()
    x!![3] = ((-1)).toDouble()
    if ((m1 == TABULAR_DIVIDER)) {
        if ((ntoks < 5)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        m2 = project_findObject(CURVE, tok!![4])
        if ((m2 < 0)) {
            return error_setInpError(ERR_NAME, tok!![4])
        }
        x!![3] = (m2).toDouble()
        n = 5
    }
    if ((m1 == CUTOFF_DIVIDER)) {
        if ((ntoks < 5)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if (!(getDouble(tok!![4], DElem(x!!, 4)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![4])
        }
        n = 5
    }
    if ((m1 == WEIR_DIVIDER)) {
        if ((ntoks < 7)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        i = 4
        f2@ while ((i < 7)) {
            if (!(getDouble(tok!![i], DElem(x!!, i)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![i])
            }
            i++
        }
        n = 7
    }
    if ((m1 == OVERFLOW_DIVIDER)) {
        n = 4
    }
    m = 7
    i = n
    f3@ while (((i < ntoks) && (m < 11))) {
        if (!(getDouble(tok!![i], DElem(x!!, m)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        m++
        i++
    }
    Node!![j]!!.ID = id
    node_setParams(j, DIVIDER, k, x)
    return 0
}

// node.c:1216
fun divider_validate(j: Int) {
    var i: Int = 0
    var k: Int = 0
    k = Node!![j]!!.subIndex
    i = Divider!![k]!!.link
    if (((i < 0) || (((Link!![i]!!.node1 != j) && (Link!![i]!!.node2 != j))))) {
        report_writeErrorMsg(ERR_DIVIDER_LINK, Node!![j]!!.ID)
    }
    if ((Divider!![k]!!.type == WEIR_DIVIDER)) {
        if (((Divider!![k]!!.dhMax <= 0.0) || (Divider!![k]!!.cWeir <= 0.0))) {
            report_writeErrorMsg(ERR_WEIR_DIVIDER, Node!![j]!!.ID)
        } else {
            Divider!![k]!!.qMax = ((Divider!![k]!!.cWeir * Glibm.pow(Divider!![k]!!.dhMax, 1.5)) / UCF(FLOW))
            if ((Divider!![k]!!.qMin > Divider!![k]!!.qMax)) {
                report_writeErrorMsg(ERR_WEIR_DIVIDER, Node!![j]!!.ID)
            }
        }
    }
}

// node.c:1251
fun divider_getOutflow(j: Int, k: Int): Double {
    var i: Int = 0
    var m: Int = 0
    var qIn: Double = 0.0
    var qOut: Double = 0.0
    var f: Double = 0.0
    qIn = (Node!![j]!!.inflow + Node!![j]!!.overflow)
    i = Node!![j]!!.subIndex
    when (Divider!![i]!!.type) {
        CUTOFF_DIVIDER -> {
            if ((qIn <= Divider!![i]!!.qMin)) {
                qOut = 0.0
            } else {
                qOut = (qIn - Divider!![i]!!.qMin)
            }
        }
        OVERFLOW_DIVIDER -> {
            if ((k != Divider!![i]!!.link)) {
                qOut = qIn
            } else {
                qOut = (qIn - Node!![j]!!.outflow)
            }
            if ((qOut < 1e-05)) {
                qOut = 0.0
            }
            return qOut
        }
        WEIR_DIVIDER -> {
            if ((qIn <= Divider!![i]!!.qMin)) {
                qOut = 0.0
            } else {
                f = (((qIn - Divider!![i]!!.qMin)) / ((Divider!![i]!!.qMax - Divider!![i]!!.qMin)))
                if ((f > 1.0)) {
                    qOut = (Divider!![i]!!.qMax * sqrt(f))
                } else {
                    qOut = ((Divider!![i]!!.cWeir * Glibm.pow((f * Divider!![i]!!.dhMax), 1.5)) / UCF(FLOW))
                }
            }
        }
        TABULAR_DIVIDER -> {
            m = Divider!![i]!!.flowCurve
            if ((m >= 0)) {
                qOut = (table_lookup(Curve!![m], (qIn * UCF(FLOW))) / UCF(FLOW))
            } else {
                qOut = 0.0
            }
        }
        else -> {
            qOut = 0.0
        }
    }
    if ((qOut > qIn)) {
        qOut = qIn
    }
    if ((k != Divider!![i]!!.link)) {
        qOut = (qIn - qOut)
    }
    return qOut
}

// node.c:1333
fun outfall_readParams(j: Int, k: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var m: Int = 0
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(7)
    var id: CPtr? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(NODE, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if (!(getDouble(tok!![1], DElem(x!!, 0)) != 0)) {
        return error_setInpError(ERR_NUMBER, tok!![1])
    }
    i = findmatch(tok!![2], OutfallTypeWords)
    if ((i < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![2])
    }
    x!![1] = (i).toDouble()
    x!![2] = 0.0
    x!![3] = (-1.0)
    x!![4] = (-1.0)
    x!![5] = 0.0
    x!![6] = (-1.0)
    n = 4
    if ((i >= FIXED_OUTFALL)) {
        if ((ntoks < 4)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        n = 5
        when (i) {
            FIXED_OUTFALL -> {
                if (!(getDouble(tok!![3], DElem(x!!, 2)) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![3])
                }
            }
            TIDAL_OUTFALL -> {
                m = project_findObject(CURVE, tok!![3])
                if ((m < 0)) {
                    return error_setInpError(ERR_NAME, tok!![3])
                }
                x!![3] = (m).toDouble()
            }
            TIMESERIES_OUTFALL -> {
                m = project_findObject(TSERIES, tok!![3])
                if ((m < 0)) {
                    return error_setInpError(ERR_NAME, tok!![3])
                }
                x!![4] = (m).toDouble()
                Tseries!![m]!!.refersTo = TIMESERIES_OUTFALL
            }
        }
    }
    if ((ntoks == n)) {
        m = findmatch(tok!![(n - 1)], NoYesWords)
        if ((m < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![(n - 1)])
        }
        x!![5] = (m).toDouble()
    }
    if ((ntoks == (n + 1))) {
        m = project_findObject(SUBCATCH, tok!![n])
        if ((m < 0)) {
            return error_setInpError(ERR_NAME, tok!![n])
        }
        x!![6] = (m).toDouble()
    }
    Node!![j]!!.ID = id
    node_setParams(j, OUTFALL, k, x)
    return 0
}

// node.c:1413
fun outfall_setOutletDepth(j: Int, yNorm: Double, __yCrit: Double, z: Double) {
    var yCrit: Double = __yCrit
    val x = DBox(0.0)
    val y = DBox(0.0)
    var yNew: Double = 0.0
    var stage: Double = 0.0
    var k: Int = 0
    var i: Int = Node!![j]!!.subIndex
    var currentDate: Double = 0.0
    when (Outfall!![i]!!.type) {
        FREE_OUTFALL -> {
            if ((z > 0.0)) {
                Node!![j]!!.newDepth = 0.0
            } else {
                Node!![j]!!.newDepth = ((if ((((yNorm) <= (yCrit)))) (yNorm) else (yCrit)))
            }
            return
        }
        NORMAL_OUTFALL -> {
            if ((z > 0.0)) {
                Node!![j]!!.newDepth = 0.0
            } else {
                Node!![j]!!.newDepth = yNorm
            }
            return
        }
        FIXED_OUTFALL -> {
            stage = Outfall!![i]!!.fixedStage
        }
        TIDAL_OUTFALL -> {
            k = Outfall!![i]!!.tideCurve
            table_getFirstEntry(Curve!![k], x, y)
            currentDate = (NewRoutingTime / 86400000.0)
            x.v += (((currentDate - floor(currentDate))) * 24.0)
            stage = (table_lookup(Curve!![k], x.v) / UCF(LENGTH))
        }
        TIMESERIES_OUTFALL -> {
            k = Outfall!![i]!!.stageSeries
            currentDate = (StartDateTime + (NewRoutingTime / 86400000.0))
            stage = (table_tseriesLookup(Tseries!![k], currentDate, 1) / UCF(LENGTH))
        }
        else -> {
            stage = Node!![j]!!.invertElev
        }
    }
    yCrit = ((if ((((yCrit) <= (yNorm)))) (yCrit) else (yNorm)))
    if ((((yCrit + z) + Node!![j]!!.invertElev) < stage)) {
        yNew = (stage - Node!![j]!!.invertElev)
    } else if ((z > 0.0)) {
        if ((stage < (Node!![j]!!.invertElev + z))) {
            yNew = ((if ((((0.0) >= (((stage - Node!![j]!!.invertElev)))))) (0.0) else (((stage - Node!![j]!!.invertElev)))))
        } else {
            yNew = (z + yCrit)
        }
    } else {
        yNew = yCrit
    }
    Node!![j]!!.newDepth = yNew
}

