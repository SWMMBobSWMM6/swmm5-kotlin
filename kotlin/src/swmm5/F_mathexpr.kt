// Generated from EPA SWMM 5.2.4 (mathexpr.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_mathexpr")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var Err: Int = 0
@JvmField var Bc: Int = 0
@JvmField var PrevLex: Int = 0
@JvmField var CurLex: Int = 0
@JvmField var Len: Int = 0
@JvmField var Pos: Int = 0
@JvmField var S: CPtr? = null
@JvmField var Token: CPtr? = R.buf(255)
@JvmField var Ivar: Int = 0
@JvmField var Fvalue: Double = 0.0
@JvmField var MathFunc: Array<CPtr?>? = arrayOf<CPtr?>(__L1004, __L1005, __L1006, __L1007, __L1008, __L1009, __L1010, __L1011, __L739, __L1012, __L1013, __L1014, __L1015, __L1016, __L1017, __L1018, __L1019, __L1020, __L1021, null)
@JvmField var mathexpr_getVariableIndex: FP1? = null

// mathexpr.c:109
fun sametext(s1: CPtr?, s2: CPtr?): Int {
    var i: Int = 0
    i = 0
    f1@ while ((R.toupper(s1!!.g(i)) == R.toupper(s2!!.g(i)))) {
        if (((!(s1!!.g((i + 1)) != 0)) && (!(s2!!.g((i + 1)) != 0)))) {
            return (1)
        }
        i++
    }
    return (0)
}

// mathexpr.c:130
fun isDigit(c: Int): Int {
    if (((c >= 49) && (c <= 57))) {
        return 1
    }
    if ((c == 48)) {
        return 1
    }
    return 0
}

// mathexpr.c:139
fun isLetter(c: Int): Int {
    if (((c >= 97) && (c <= 122))) {
        return 1
    }
    if (((c >= 65) && (c <= 90))) {
        return 1
    }
    if ((c == 95)) {
        return 1
    }
    return 0
}

// mathexpr.c:149
fun getToken() {
    var c: CPtr? = R.bufFrom(__L33, 2)
    Token!!.s(0, 0)
    w1@ while (((Pos <= Len) && (((isLetter(S!!.g(Pos)) != 0) || (isDigit(S!!.g(Pos)) != 0))))) {
        c!!.s(0, S!!.g(Pos))
        R.strcat(Token, c)
        Pos++
    }
    Pos--
}

// mathexpr.c:165
fun getMathFunc(): Int {
    var i: Int = 0
    w1@ while ((MathFunc!![i] != null)) {
        if ((sametext(MathFunc!![i], Token) != 0)) {
            return (i + 10)
        }
        i++
    }
    return (0)
}

// mathexpr.c:178
fun getVariable(): Int {
    if (!(mathexpr_getVariableIndex != null)) {
        return 0
    }
    Ivar = mathexpr_getVariableIndex!!.call(Token)
    if ((Ivar >= 0)) {
        return 8
    }
    return 0
}

// mathexpr.c:188
fun getNumber(): Double {
    var c: CPtr? = R.bufFrom(__L33, 2)
    var sNumber: CPtr? = R.buf(255)
    var errflag: Int = 0
    var decimalCount: Int = 0
    sNumber!!.s(0, 0)
    w1@ while (((Pos < Len) && (isDigit(S!!.g(Pos)) != 0))) {
        c!!.s(0, S!!.g(Pos))
        R.strcat(sNumber, c)
        Pos++
    }
    if ((Pos < Len)) {
        if ((S!!.g(Pos) == 46)) {
            decimalCount++
            if ((decimalCount > 1)) {
                Err = 1
            }
            R.strcat(sNumber, __L315)
            Pos++
            w2@ while (((Pos < Len) && (isDigit(S!!.g(Pos)) != 0))) {
                c!!.s(0, S!!.g(Pos))
                R.strcat(sNumber, c)
                Pos++
            }
        }
        if (((Pos < Len) && (((S!!.g(Pos) == 101) || (S!!.g(Pos) == 69))))) {
            R.strcat(sNumber, __L316)
            Pos++
            if ((Pos >= Len)) {
                errflag = 1
            } else {
                if (((S!!.g(Pos) == 45) || (S!!.g(Pos) == 43))) {
                    c!!.s(0, S!!.g(Pos))
                    R.strcat(sNumber, c)
                    Pos++
                }
                if (((Pos >= Len) || (!(isDigit(S!!.g(Pos)) != 0)))) {
                    errflag = 1
                } else {
                    w3@ while (((Pos < Len) && (isDigit(S!!.g(Pos)) != 0))) {
                        c!!.s(0, S!!.g(Pos))
                        R.strcat(sNumber, c)
                        Pos++
                    }
                }
            }
        }
    }
    Pos--
    if ((errflag != 0)) {
        return 0.0
    } else {
        return R.atof(sNumber)
    }
    throw IllegalStateException("getNumber: no return")
}

// mathexpr.c:252
fun getOperand(): Int {
    var code: Int = 0
    when (S!!.g(Pos)) {
        40 -> {
            code = 1
        }
        41 -> {
            code = 2
        }
        43 -> {
            code = 3
        }
        45 -> {
            code = 4
            if ((((Pos < (Len - 1)) && (isDigit(S!!.g((Pos + 1))) != 0)) && (((CurLex <= 6) || (CurLex == 31))))) {
                Pos++
                Fvalue = (-(getNumber()))
                code = 7
            }
        }
        42 -> {
            code = 5
        }
        47 -> {
            code = 6
        }
        94 -> {
            code = 31
        }
        else -> {
            code = 0
        }
    }
    return code
}

// mathexpr.c:280
fun getLex(): Int {
    var n: Int = 0
    w1@ while (((Pos < Len) && (S!!.g(Pos) == 32))) {
        Pos++
    }
    if ((Pos >= Len)) {
        return 0
    }
    n = getOperand()
    if ((n == 0)) {
        if ((isLetter(S!!.g(Pos)) != 0)) {
            getToken()
            n = getMathFunc()
            if ((n == 0)) {
                n = getVariable()
            }
        } else if (((S!!.g(Pos) == 46) || (isDigit(S!!.g(Pos)) != 0))) {
            n = 7
            Fvalue = getNumber()
        }
    }
    Pos++
    PrevLex = CurLex
    CurLex = n
    return n
}

// mathexpr.c:314
fun newNode(): ExprTree? {
    var node: ExprTree? = null
    node = ExprTree()
    if (!(node != null)) {
        Err = 2
    } else {
        node!!.opcode = 0
        node!!.ivar = (-1)
        node!!.fvalue = 0.0
        node!!.left = (null)
        node!!.right = (null)
    }
    return node
}

// mathexpr.c:332
fun getSingleOp(lex: IRef?): ExprTree? {
    var opcode: Int = 0
    var left: ExprTree? = null
    var node: ExprTree? = null
    if ((lex!!.get() == 1)) {
        Bc++
        left = getTree()
    } else {
        if ((((lex!!.get() < 7) || (lex!!.get() == 9)) || (lex!!.get() > 30))) {
            Err = 1
            return null
        }
        opcode = lex!!.get()
        if (((lex!!.get() == 7) || (lex!!.get() == 8))) {
            left = newNode()
            left!!.opcode = opcode
            if ((lex!!.get() == 7)) {
                left!!.fvalue = Fvalue
            }
            if ((lex!!.get() == 8)) {
                left!!.ivar = Ivar
            }
        } else {
            lex!!.set(getLex())
            if ((lex!!.get() != 1)) {
                Err = 1
                return null
            }
            Bc++
            left = newNode()
            left!!.left = getTree()
            left!!.opcode = opcode
        }
    }
    lex!!.set(getLex())
    if ((lex!!.get() == 31)) {
        node = newNode()
        node!!.left = left
        node!!.opcode = lex!!.get()
        lex!!.set(getLex())
        node!!.right = getSingleOp(lex)
        left = node
    }
    return left
}

// mathexpr.c:397
fun getOp(lex: IRef?): ExprTree? {
    var opcode: Int = 0
    var left: ExprTree? = null
    var right: ExprTree? = null
    var node: ExprTree? = null
    var neg: Int = 0
    lex!!.set(getLex())
    if (((PrevLex == 0) || (PrevLex == 1))) {
        if ((lex!!.get() == 4)) {
            neg = 1
            lex!!.set(getLex())
        } else if ((lex!!.get() == 3)) {
            lex!!.set(getLex())
        }
    }
    left = getSingleOp(lex)
    w1@ while (((lex!!.get() == 5) || (lex!!.get() == 6))) {
        opcode = lex!!.get()
        lex!!.set(getLex())
        right = getSingleOp(lex)
        node = newNode()
        if ((Err != 0)) {
            return null
        }
        node!!.left = left
        node!!.right = right
        node!!.opcode = opcode
        left = node
    }
    if ((neg != 0)) {
        node = newNode()
        if ((Err != 0)) {
            return null
        }
        node!!.left = left
        node!!.right = (null)
        node!!.opcode = 9
        left = node
    }
    return left
}

// mathexpr.c:442
fun getTree(): ExprTree? {
    val lex = IBox(0)
    var opcode: Int = 0
    var left: ExprTree? = null
    var right: ExprTree? = null
    var node: ExprTree? = null
    left = getOp(lex)
    f1@ while (true) {
        if (((lex.v == 0) || (lex.v == 2))) {
            if ((lex.v == 2)) {
                Bc--
            }
            break@f1
        }
        if (((lex.v != 3) && (lex.v != 4))) {
            Err = 1
            break@f1
        }
        opcode = lex.v
        right = getOp(lex)
        node = newNode()
        if ((Err != 0)) {
            break@f1
        }
        node!!.left = left
        node!!.right = right
        node!!.opcode = opcode
        left = node
    }
    return left
}

// mathexpr.c:479
fun traverseTree(tree: ExprTree?, expr: ORef<MathExpr?>?) {
    var node: MathExpr? = null
    if ((tree == null)) {
        return
    }
    traverseTree(tree!!.left, expr)
    traverseTree(tree!!.right, expr)
    node = MathExpr()
    if ((node != null)) {
        node!!.fvalue = tree!!.fvalue
        node!!.opcode = tree!!.opcode
        node!!.ivar = tree!!.ivar
        node!!.next = (null)
        node!!.prev = (expr!!.get())
    }
    if ((expr!!.get() != null)) {
        (expr!!.get())!!.next = node
    }
    expr!!.set(node)
}

// mathexpr.c:501
fun deleteTree(tree: ExprTree?) {
    if ((tree != null)) {
        if ((tree!!.left != null)) {
            deleteTree(tree!!.left)
        }
        if ((tree!!.right != null)) {
            deleteTree(tree!!.right)
        }
        R.free(tree)
    }
}

// mathexpr.c:516
fun mathexpr_eval(expr: MathExpr?, getVariableValue: FP2?): Double {
    var ExprStack: DoubleArray? = DoubleArray(1024)
    var node: MathExpr? = expr
    var r1: Double = 0.0
    var r2: Double = 0.0
    var stackindex: Int = 0
    ExprStack!![0] = 0.0
    w1@ while (((node != null) && (stackindex >= 0))) {
        s2@ do {
            when (node!!.opcode) {
                3 -> {
                    r1 = ExprStack!![stackindex]
                    stackindex--
                    if ((stackindex < 0)) {
                        break@s2
                    }
                    r2 = ExprStack!![stackindex]
                    ExprStack!![stackindex] = (r2 + r1)
                }
                4 -> {
                    r1 = ExprStack!![stackindex]
                    stackindex--
                    if ((stackindex < 0)) {
                        break@s2
                    }
                    r2 = ExprStack!![stackindex]
                    ExprStack!![stackindex] = (r2 - r1)
                }
                5 -> {
                    r1 = ExprStack!![stackindex]
                    stackindex--
                    if ((stackindex < 0)) {
                        break@s2
                    }
                    r2 = ExprStack!![stackindex]
                    ExprStack!![stackindex] = (r2 * r1)
                }
                6 -> {
                    r1 = ExprStack!![stackindex]
                    stackindex--
                    if ((stackindex < 0)) {
                        break@s2
                    }
                    r2 = ExprStack!![stackindex]
                    ExprStack!![stackindex] = (r2 / r1)
                }
                7 -> {
                    stackindex++
                    if ((stackindex >= 1024)) {
                        break@s2
                    }
                    ExprStack!![stackindex] = node!!.fvalue
                }
                8 -> {
                    if ((getVariableValue != null)) {
                        r1 = getVariableValue!!.call(node!!.ivar)
                    } else {
                        r1 = 0.0
                    }
                    stackindex++
                    if ((stackindex >= 1024)) {
                        break@s2
                    }
                    ExprStack!![stackindex] = r1
                }
                9 -> {
                    ExprStack!![stackindex] = (-ExprStack!![stackindex])
                }
                10 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.cos(r1)
                    ExprStack!![stackindex] = r2
                }
                11 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.sin(r1)
                    ExprStack!![stackindex] = r2
                }
                12 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.tan(r1)
                    ExprStack!![stackindex] = r2
                }
                13 -> {
                    r1 = ExprStack!![stackindex]
                    if ((r1 == 0.0)) {
                        r2 = 0.0
                    } else {
                        r2 = (1.0 / Glibm.tan(r1))
                    }
                    ExprStack!![stackindex] = r2
                }
                14 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = abs(r1)
                    ExprStack!![stackindex] = r2
                }
                15 -> {
                    r1 = ExprStack!![stackindex]
                    if ((r1 < 0.0)) {
                        r2 = (-1.0)
                    } else if ((r1 > 0.0)) {
                        r2 = 1.0
                    } else {
                        r2 = 0.0
                    }
                    ExprStack!![stackindex] = r2
                }
                16 -> {
                    r1 = ExprStack!![stackindex]
                    if ((r1 < 0.0)) {
                        r2 = 0.0
                    } else {
                        r2 = sqrt(r1)
                    }
                    ExprStack!![stackindex] = r2
                }
                17 -> {
                    r1 = ExprStack!![stackindex]
                    if ((r1 <= 0.0)) {
                        r2 = 0.0
                    } else {
                        r2 = Glibm.log(r1)
                    }
                    ExprStack!![stackindex] = r2
                }
                18 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.exp(r1)
                    ExprStack!![stackindex] = r2
                }
                19 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.asin(r1)
                    ExprStack!![stackindex] = r2
                }
                20 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.acos(r1)
                    ExprStack!![stackindex] = r2
                }
                21 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = Glibm.atan(r1)
                    ExprStack!![stackindex] = r2
                }
                22 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = (1.57079632679489661923 - Glibm.atan(r1))
                    ExprStack!![stackindex] = r2
                }
                23 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = (((Glibm.exp(r1) - Glibm.exp((-r1)))) / 2.0)
                    ExprStack!![stackindex] = r2
                }
                24 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = (((Glibm.exp(r1) + Glibm.exp((-r1)))) / 2.0)
                    ExprStack!![stackindex] = r2
                }
                25 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = (((Glibm.exp(r1) - Glibm.exp((-r1)))) / ((Glibm.exp(r1) + Glibm.exp((-r1)))))
                    ExprStack!![stackindex] = r2
                }
                26 -> {
                    r1 = ExprStack!![stackindex]
                    r2 = (((Glibm.exp(r1) + Glibm.exp((-r1)))) / ((Glibm.exp(r1) - Glibm.exp((-r1)))))
                    ExprStack!![stackindex] = r2
                }
                27 -> {
                    r1 = ExprStack!![stackindex]
                    if ((r1 == 0.0)) {
                        r2 = 0.0
                    } else {
                        r2 = Glibm.log10(r1)
                    }
                    ExprStack!![stackindex] = r2
                }
                28 -> {
                    r1 = ExprStack!![stackindex]
                    if ((r1 <= 0.0)) {
                        r2 = 0.0
                    } else {
                        r2 = 1.0
                    }
                    ExprStack!![stackindex] = r2
                }
                31 -> {
                    r1 = ExprStack!![stackindex]
                    stackindex--
                    if ((stackindex < 0)) {
                        break@s2
                    }
                    r2 = ExprStack!![stackindex]
                    if ((r2 <= 0.0)) {
                        r2 = 0.0
                    } else {
                        r2 = Glibm.pow(r2, r1)
                    }
                    ExprStack!![stackindex] = r2
                }
            }
        } while (false)
        node = node!!.next
    }
    if ((stackindex >= 0)) {
        r1 = ExprStack!![stackindex]
    } else {
        r1 = 0.0
    }
    if ((r1 != r1)) {
        r1 = 0.0
    }
    return r1
}

// mathexpr.c:735
fun mathexpr_delete(expr: MathExpr?) {
    if ((expr != null)) {
        mathexpr_delete(expr!!.next)
    }
    R.free(expr)
}

// mathexpr.c:743
fun mathexpr_create(formula: CPtr?, getVar: FP1?): MathExpr? {
    var tree: ExprTree? = null
    val expr = OBox<MathExpr?>((null))
    var result: MathExpr? = (null)
    mathexpr_getVariableIndex = getVar
    Err = 0
    PrevLex = 0
    CurLex = 0
    S = formula
    Len = (R.strlen(S)).toInt()
    Pos = 0
    Bc = 0
    tree = getTree()
    if (((Bc == 0) && (Err == 0))) {
        traverseTree(tree, expr)
        w1@ while ((expr.v != null)) {
            result = expr.v
            expr.v = expr.v!!.prev
        }
    }
    deleteTree(tree)
    return result
}

