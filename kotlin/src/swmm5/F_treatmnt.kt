// Generated from EPA SWMM 5.2.4 (treatmnt.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_treatmnt")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var PVMAX: Int = 5
@JvmField var ErrCode: Int = 0
@JvmField var J: Int = 0
@JvmField var Dt: Double = 0.0
@JvmField var Q: Double = 0.0
@JvmField var V: Double = 0.0
@JvmField var R_: DoubleArray? = null
@JvmField var Cin: DoubleArray? = null

// treatmnt.c:66
fun treatmnt_open(): Int {
    R_ = (null)
    Cin = (null)
    if ((Nobjects!![POLLUT] > 0)) {
        R_ = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        Cin = DoubleArray(((Nobjects!![POLLUT]).toLong()).toInt())
        if (((R_ == null) || (Cin == null))) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return 0
        }
    }
    return 1
}

// treatmnt.c:90
fun treatmnt_close() {
    if ((R_ != null)) {
        R.free(R_)
        R_ = (null)
    }
    if ((Cin != null)) {
        R.free(Cin)
        Cin = (null)
    }
}

// treatmnt.c:103
fun treatmnt_readExpression(tok: Array<CPtr?>?, ntoks: Int): Int {
    var s: CPtr? = R.buf(1025)
    var expr: CPtr? = null
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var p: Int = 0
    var equation: MathExpr? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(NODE, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    p = project_findObject(POLLUT, tok!![1])
    if ((p < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    sstrncpy(s, tok!![2], 1024L)
    i = 3
    f1@ while ((i < ntoks)) {
        sstrcat(s, __L33, 1024L)
        sstrcat(s, tok!![i], 1024L)
        i++
    }
    if ((((if (((((s!!.g(0)) >= 97) && ((s!!.g(0)) <= 122)))) (((s!!.g(0)) and 32.inv())) else (s!!.g(0)))) == 82)) {
        k = 0
    } else if ((((if (((((s!!.g(0)) >= 97) && ((s!!.g(0)) <= 122)))) (((s!!.g(0)) and 32.inv())) else (s!!.g(0)))) == 67)) {
        k = 1
    } else {
        return error_setInpError(ERR_KEYWORD, tok!![2])
    }
    expr = R.strchr(s, 61)
    if ((expr == null)) {
        return error_setInpError(ERR_KEYWORD, __L0)
    } else {
        expr = expr!!.add(1)
    }
    if ((Node!![j]!!.treatment == null)) {
        if (!(createTreatment(j) != 0)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
    }
    equation = mathexpr_create(expr, FP1(::treatmnt_getVariableIndex))
    if ((equation == null)) {
        return error_setInpError(ERR_MATH_EXPR, __L0)
    }
    if ((Node!![j]!!.treatment != null)) {
        Node!![j]!!.treatment!![p]!!.treatType = k
        Node!![j]!!.treatment!![p]!!.equation = equation
    }
    return 0
}

// treatmnt.c:165
fun treatmnt_delete(j: Int) {
    var p: Int = 0
    if ((Node!![j]!!.treatment != null)) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            mathexpr_delete(Node!![j]!!.treatment!![p]!!.equation)
            p++
        }
        R.free(Node!![j]!!.treatment)
    }
    Node!![j]!!.treatment = (null)
}

// treatmnt.c:184
fun treatmnt_setInflow(qIn: Double, wIn: DoubleArray?) {
    var p: Int = 0
    if ((qIn > 0.0)) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            Cin!![p] = (wIn!![p] / qIn)
            p++
        }
    } else {
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            Cin!![p] = 0.0
            p++
        }
    }
}

// treatmnt.c:202
fun treatmnt_treat(j: Int, q: Double, v: Double, tStep: Double) {
    var p: Int = 0
    var cOut: Double = 0.0
    var massLost: Double = 0.0
    var treatment: TTreatment? = null
    if ((Node!![j]!!.treatment == null)) {
        return
    }
    ErrCode = 0
    J = j
    Dt = tStep
    Q = q
    V = v
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        R_!![p] = (-1.0)
        p++
    }
    p = 0
    f2@ while ((p < Nobjects!![POLLUT])) {
        treatment = Node!![j]!!.treatment!![p]
        if ((treatment!!.equation == null)) {
            R_!![p] = 0.0
        } else if (((treatment!!.treatType == REMOVAL) && (q <= 1e-10))) {
            R_!![p] = 0.0
        } else {
            getRemoval(p)
        }
        p++
    }
    if ((ErrCode == ERR_CYCLIC_TREATMENT)) {
        report_writeErrorMsg(ERR_CYCLIC_TREATMENT, Node!![J]!!.ID)
    } else {
        p = 0
        f3@ while ((p < Nobjects!![POLLUT])) {
            if ((R_!![p] == 0.0)) {
                p++
                continue@f3
            }
            treatment = Node!![j]!!.treatment!![p]
            if ((treatment!!.treatType == REMOVAL)) {
                if ((Cin!![p] == 0.0)) {
                    cOut = Node!![j]!!.newQual!![p]
                } else {
                    cOut = (((1.0 - R_!![p])) * Cin!![p])
                }
                cOut = ((if ((((cOut) <= (Node!![j]!!.newQual!![p])))) (cOut) else (Node!![j]!!.newQual!![p])))
            } else {
                cOut = (((1.0 - R_!![p])) * Node!![j]!!.newQual!![p])
            }
            massLost = ((((((Cin!![p] * q) * tStep) + (Node!![j]!!.oldQual!![p] * Node!![j]!!.oldVolume)) - (cOut * (((q * tStep) + Node!![j]!!.oldVolume))))) / tStep)
            massLost = ((if ((((0.0) >= (massLost)))) (0.0) else (massLost)))
            massbal_addReactedMass(p, massLost)
            Node!![j]!!.newQual!![p] = cOut
            p++
        }
    }
}

// treatmnt.c:288
fun createTreatment(j: Int): Int {
    var p: Int = 0
    Node!![j]!!.treatment = Array<TTreatment?>(((Nobjects!![POLLUT]).toLong()).toInt()) { TTreatment() }
    if ((Node!![j]!!.treatment == null)) {
        return 0
    }
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        Node!![j]!!.treatment!![p]!!.equation = (null)
        p++
    }
    return 1
}

// treatmnt.c:311
fun treatmnt_getVariableIndex(s: CPtr?): Int {
    var k: Int = 0
    var m: Int = PVMAX
    k = findmatch(s, ProcessVarWords)
    if ((k >= 0)) {
        return k
    }
    k = project_findObject(POLLUT, s)
    if ((k >= 0)) {
        return ((k + m))
    }
    if (((((if (((((s!!.g(0)) >= 97) && ((s!!.g(0)) <= 122)))) (((s!!.g(0)) and 32.inv())) else (s!!.g(0)))) == 82) && (s!!.g(1) == 95))) {
        k = project_findObject(POLLUT, s!!.add(2))
        if ((k >= 0)) {
            return (((Nobjects!![POLLUT] + k) + m))
        }
    }
    return (-1)
}

// treatmnt.c:340
fun treatmnt_getVariableValue(varCode: Int): Double {
    var p: Int = 0
    var a1: Double = 0.0
    var a2: Double = 0.0
    var y: Double = 0.0
    var treatment: TTreatment? = null
    if ((varCode < PVMAX)) {
        when (varCode) {
            pvHRT -> {
                if ((Node!![J]!!.type == STORAGE)) {
                    return (Storage!![Node!![J]!!.subIndex]!!.hrt / 3600.0)
                } else {
                    return 0.0
                }
            }
            pvDT -> {
                return Dt
            }
            pvFLOW -> {
                return (Q * UCF(FLOW))
            }
            pvDEPTH -> {
                y = (((Node!![J]!!.oldDepth + Node!![J]!!.newDepth)) / 2.0)
                return (y * UCF(LENGTH))
            }
            pvAREA -> {
                a1 = node_getSurfArea(J, Node!![J]!!.oldDepth)
                a2 = node_getSurfArea(J, Node!![J]!!.newDepth)
                return (((((a1 + a2)) / 2.0) * UCF(LENGTH)) * UCF(LENGTH))
            }
            else -> {
                return 0.0
            }
        }
    } else if ((varCode < (PVMAX + Nobjects!![POLLUT]))) {
        p = (varCode - PVMAX)
        treatment = Node!![J]!!.treatment!![p]
        if ((treatment!!.treatType == REMOVAL)) {
            return Cin!![p]
        }
        return Node!![J]!!.newQual!![p]
    } else {
        p = ((varCode - PVMAX) - Nobjects!![POLLUT])
        if ((p >= Nobjects!![POLLUT])) {
            return 0.0
        }
        return getRemoval(p)
    }
    throw IllegalStateException("getVariableValue: no return")
}

// treatmnt.c:404
fun getRemoval(p: Int): Double {
    var c0: Double = Node!![J]!!.newQual!![p]
    var r: Double = 0.0
    var treatment: TTreatment? = null
    if (((R_!![p] > 1.0) || (ErrCode != 0))) {
        ErrCode = 1
        return 0.0
    }
    if (((R_!![p] >= 0.0) && (R_!![p] <= 1.0))) {
        return R_!![p]
    }
    R_!![p] = 10.0
    if ((c0 == 0.0)) {
        R_!![p] = 0.0
        return 0.0
    }
    treatment = Node!![J]!!.treatment!![p]
    r = mathexpr_eval(treatment!!.equation, FP2(::treatmnt_getVariableValue))
    r = ((if ((((0.0) >= (r)))) (0.0) else (r)))
    if ((treatment!!.treatType == REMOVAL)) {
        r = ((if ((((1.0) <= (r)))) (1.0) else (r)))
        R_!![p] = r
    } else {
        r = ((if ((((c0) <= (r)))) (c0) else (r)))
        R_!![p] = (1.0 - (r / c0))
    }
    return R_!![p]
}

