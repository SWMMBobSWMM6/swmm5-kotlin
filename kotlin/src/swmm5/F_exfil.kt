// Generated from EPA SWMM 5.2.4 (exfil.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_exfil")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// exfil.c:34
fun exfil_readStorageParams(k: Int, tok: Array<CPtr?>?, ntoks: Int, n: Int): Int {
    var i: Int = 0
    var x: DoubleArray? = DoubleArray(3)
    if ((ntoks == (n + 1))) {
        if (!(getDouble(tok!![n], DElem(x!!, 1)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![n])
        }
        x!![0] = 0.0
        x!![2] = 0.0
    } else if ((ntoks < (n + 3))) {
        return error_setInpError(ERR_ITEMS, __L0)
    } else {
        i = 0
        f1@ while ((i < 3)) {
            if (!(getDouble(tok!![(n + i)], DElem(x!!, i)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![(n + i)])
            }
            i++
        }
    }
    if ((x!![1] == 0.0)) {
        return 0
    }
    return createStorageExfil(k, x)
}

// exfil.c:74
fun exfil_initState(k: Int) {
    var i: Int = 0
    val a = DBox(0.0)
    var alast: Double = 0.0
    val d = DBox(0.0)
    var aCurve: TTable? = null
    var exfil: TExfil? = Storage!![k]!!.exfil
    if ((exfil != null)) {
        grnampt_initState(exfil!!.btmExfil)
        grnampt_initState(exfil!!.bankExfil)
        when (Storage!![k]!!.shape) {
            TABULAR -> {
                i = Storage!![k]!!.aCurve
                exfil!!.btmArea = 0.0
                exfil!!.bankMinDepth = 0.0
                exfil!!.bankMaxDepth = 0.0
                exfil!!.bankMaxArea = 0.0
                if ((i >= 0)) {
                    aCurve = Curve!![i]
                    Storage!![k]!!.exfil!!.btmArea = table_lookupEx(aCurve, 0.0)
                    table_getFirstEntry(aCurve, d, a)
                    alast = a.v
                    w2@ while ((table_getNextEntry(aCurve, d, a) != 0)) {
                        if ((a.v < alast)) {
                            break@w2
                        } else if ((a.v > alast)) {
                            exfil!!.bankMaxArea = a.v
                            exfil!!.bankMaxDepth = d.v
                        } else if ((exfil!!.bankMaxArea == 0.0)) {
                            exfil!!.bankMinDepth = d.v
                        } else {
                            break@w2
                        }
                        alast = a.v
                    }
                    exfil!!.btmArea /= (UCF(LENGTH) * UCF(LENGTH))
                    exfil!!.bankMaxArea /= (UCF(LENGTH) * UCF(LENGTH))
                    exfil!!.bankMinDepth /= (UCF(LENGTH))
                    exfil!!.bankMaxDepth /= (UCF(LENGTH))
                }
            }
            FUNCTIONAL -> {
                exfil!!.btmArea = Storage!![k]!!.a0
                if ((Storage!![k]!!.a2 == 0.0)) {
                    exfil!!.btmArea += Storage!![k]!!.a1
                }
                exfil!!.bankMinDepth = 0.0
                exfil!!.bankMaxDepth = 10000000000.0
                exfil!!.bankMaxArea = 10000000000.0
            }
            CYLINDRICAL, CONICAL, PYRAMIDAL -> {
                exfil!!.btmArea = Storage!![k]!!.a0
                exfil!!.bankMinDepth = 0.0
                exfil!!.bankMaxDepth = 10000000000.0
                exfil!!.bankMaxArea = 10000000000.0
            }
        }
    }
}

// exfil.c:158
fun exfil_getLoss(exfil: TExfil?, tStep: Double, __depth: Double, __area: Double): Double {
    var depth: Double = __depth
    var area: Double = __area
    var exfilRate: Double = 0.0
    if ((exfil!!.btmExfil!!.IMDmax == 0.0)) {
        exfilRate = (exfil!!.btmExfil!!.Ks * Adjust!!.hydconFactor)
    } else {
        exfilRate = grnampt_getInfil(exfil!!.btmExfil, tStep, 0.0, depth, MOD_GREEN_AMPT)
    }
    exfilRate *= exfil!!.btmArea
    if ((depth > exfil!!.bankMinDepth)) {
        area = (((if ((((area) <= (exfil!!.bankMaxArea)))) (area) else (exfil!!.bankMaxArea))) - exfil!!.btmArea)
        if ((area > 0.0)) {
            if ((exfil!!.btmExfil!!.IMDmax == 0.0)) {
                exfilRate += ((area * exfil!!.btmExfil!!.Ks) * Adjust!!.hydconFactor)
            } else {
                if ((depth > exfil!!.bankMaxDepth)) {
                    depth = ((depth - exfil!!.bankMaxDepth) + (((exfil!!.bankMaxDepth - exfil!!.bankMinDepth)) / 2.0))
                } else {
                    depth = (((depth - exfil!!.bankMinDepth)) / 2.0)
                }
                exfilRate += (area * grnampt_getInfil(exfil!!.bankExfil, tStep, 0.0, depth, MOD_GREEN_AMPT))
            }
        }
    }
    return exfilRate
}

// exfil.c:218
fun createStorageExfil(k: Int, x: DoubleArray?): Int {
    var exfil: TExfil? = null
    exfil = Storage!![k]!!.exfil
    if ((exfil == null)) {
        exfil = TExfil()
        if ((exfil == null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        Storage!![k]!!.exfil = exfil
        exfil!!.btmExfil = (null)
        exfil!!.bankExfil = (null)
        exfil!!.btmExfil = TGrnAmpt()
        if ((exfil!!.btmExfil == null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        exfil!!.bankExfil = TGrnAmpt()
        if ((exfil!!.bankExfil == null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
    }
    if (!(grnampt_setParams(exfil!!.btmExfil, x) != 0)) {
        return error_setInpError(ERR_NUMBER, __L0)
    }
    grnampt_setParams(exfil!!.bankExfil, x)
    return 0
}

