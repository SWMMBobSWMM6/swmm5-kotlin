// Generated from EPA SWMM 5.2.4 (controls.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_controls")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var ObjectWords: Array<CPtr?>? = arrayOf<CPtr?>(__L656, __L657, __L658, __L659, __L660, __L661, __L662, __L663, __L32, null)
@JvmField var AttribWords: Array<CPtr?>? = arrayOf<CPtr?>(__L664, __L665, __L314, __L666, __L667, __L183, __L668, __L669, __L670, __L671, __L672, __L673, __L674, __L675, __L676, __L677, __L21, __L678, __L679, __L680, __L681, null)
@JvmField var RelOpWords: Array<CPtr?>? = arrayOf<CPtr?>(__L31, __L682, __L683, __L684, __L685, __L686, null)
@JvmField var StatusWords: Array<CPtr?>? = arrayOf<CPtr?>(__L687, __L688, null)
@JvmField var ConduitWords: Array<CPtr?>? = arrayOf<CPtr?>(__L689, __L690, null)
@JvmField var SettingTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L691, __L692, __L693, null)
@JvmField var IntensityWord: CPtr? = __L694
@JvmField var Rules: Array<TRule?>? = null
@JvmField var ActionList: TActionList? = null
@JvmField var InputState: Int = 0
@JvmField var RuleCount: Int = 0
@JvmField var ControlValue: Double = 0.0
@JvmField var SetPoint: Double = 0.0
@JvmField var CurrentDate: Double = 0.0
@JvmField var CurrentTime: Double = 0.0
@JvmField var VariableCount: Int = 0
@JvmField var ExpressionCount: Int = 0
@JvmField var CurrentVariable: Int = 0
@JvmField var CurrentExpression: Int = 0
@JvmField var NamedVariable: Array<TNamedVariable?>? = null
@JvmField var Expression: Array<TExpression?>? = null

// controls.c:226
fun controls_init() {
    Rules = (null)
    NamedVariable = (null)
    Expression = (null)
    RuleCount = 0
    VariableCount = 0
    ExpressionCount = 0
}

// controls.c:243
fun controls_addToCount(s: CPtr?) {
    if ((match(s, __L29) != 0)) {
        VariableCount++
    } else if ((match(s, __L30) != 0)) {
        ExpressionCount++
    }
}

// controls.c:257
fun controls_create(n: Int): Int {
    var r: Int = 0
    ActionList = (null)
    InputState = r_PRIORITY
    RuleCount = n
    if ((RuleCount > 0)) {
        Rules = Array<TRule?>(((RuleCount).toLong()).toInt()) { TRule() }
        if ((Rules == null)) {
            return ERR_MEMORY
        }
        r = 0
        f1@ while ((r < RuleCount)) {
            Rules!![r]!!.ID = null
            Rules!![r]!!.firstPremise = (null)
            Rules!![r]!!.lastPremise = (null)
            Rules!![r]!!.thenActions = (null)
            Rules!![r]!!.elseActions = (null)
            Rules!![r]!!.priority = 0.0
            r++
        }
    }
    CurrentVariable = (-1)
    CurrentExpression = (-1)
    if ((VariableCount > 0)) {
        NamedVariable = Array<TNamedVariable?>(((VariableCount).toLong()).toInt()) { TNamedVariable() }
        if ((NamedVariable == null)) {
            return ERR_MEMORY
        }
    }
    if ((ExpressionCount > 0)) {
        Expression = Array<TExpression?>(((ExpressionCount).toLong()).toInt()) { TExpression() }
        if ((Expression == null)) {
            return ERR_MEMORY
        }
    }
    return 0
}

// controls.c:302
fun controls_delete() {
    var i: Int = 0
    i = 0
    f1@ while ((i < ExpressionCount)) {
        mathexpr_delete(Expression!![i]!!.expression)
        Expression!![i]!!.expression = (null)
        i++
    }
    if ((Expression != null)) {
        R.free(Expression)
        Expression = (null)
    }
    if ((NamedVariable != null)) {
        R.free(NamedVariable)
        NamedVariable = (null)
    }
    if ((RuleCount == 0)) {
        return
    }
    deleteActionList()
    deleteRules()
}

// controls.c:326
fun controls_addVariable(tok: Array<CPtr?>?, nToks: Int): Int {
    var v1: TVariable? = TVariable()
    val k = IBox(0)
    var err: Int = 0
    CurrentVariable++
    if ((nToks < 5)) {
        return ERR_ITEMS
    }
    if ((findExactMatch(tok!![1], AttribWords) >= 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    if (!(match(tok!![2], __L31) != 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![2])
    }
    if (((!(match(tok!![3], __L32) != 0)) && (nToks < 6))) {
        return ERR_ITEMS
    }
    k.v = 3
    err = getPremiseVariable(tok, nToks, k, v1)
    if ((err > 0)) {
        return err
    }
    k.v = CurrentVariable
    NamedVariable!![k.v]!!.variable = v1!!.copy()
    sstrncpy(NamedVariable!![k.v]!!.name, tok!![1], 32L)
    return 0
}

// controls.c:357
fun controls_addExpression(tok: Array<CPtr?>?, nToks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    var s: CPtr? = R.buf(1025)
    var expr: MathExpr? = null
    CurrentExpression++
    if ((nToks < 4)) {
        return ERR_ITEMS
    }
    k = CurrentExpression
    Expression!![k]!!.expression = (null)
    sstrncpy(Expression!![k]!!.name, tok!![1], 32L)
    sstrncpy(s, tok!![3], 1024L)
    i = 4
    f1@ while ((i < nToks)) {
        sstrcat(s, __L33, 1024L)
        sstrcat(s, tok!![i], 1024L)
        i++
    }
    expr = mathexpr_create(s, FP1(::getVariableIndex))
    if ((expr == null)) {
        return error_setInpError(ERR_MATH_EXPR, __L0)
    }
    Expression!![k]!!.expression = expr
    return 0
}

// controls.c:393
fun getVariableIndex(varName: CPtr?): Int {
    var i: Int = 0
    i = 0
    f1@ while ((i < VariableCount)) {
        if ((match(varName, NamedVariable!![i]!!.name) != 0)) {
            return i
        }
        i++
    }
    return (-1)
}

// controls.c:410
fun getNamedVariableValue(varIndex: Int): Double {
    return getVariableValue(NamedVariable!![varIndex]!!.variable)
}

// controls.c:422
fun getExpressionIndex(exprName: CPtr?): Int {
    var i: Int = 0
    i = 0
    f1@ while ((i < ExpressionCount)) {
        if ((match(exprName, Expression!![i]!!.name) != 0)) {
            return i
        }
        i++
    }
    return (-1)
}

// controls.c:439
fun controls_addRuleClause(r: Int, keyword: Int, tok: Array<CPtr?>?, nToks: Int): Int {
    when (keyword) {
        r_RULE -> {
            if ((Rules!![r]!!.ID == null)) {
                Rules!![r]!!.ID = project_findID(CONTROL, tok!![1])
            }
            InputState = r_RULE
            if ((nToks > 2)) {
                return ERR_RULE
            }
            return 0
        }
        r_IF -> {
            if ((InputState != r_RULE)) {
                return ERR_RULE
            }
            InputState = r_IF
            return addPremise(r, r_AND, tok, nToks)
        }
        r_AND -> {
            if ((InputState == r_IF)) {
                return addPremise(r, r_AND, tok, nToks)
            } else if (((InputState == r_THEN) || (InputState == r_ELSE))) {
                return addAction(r, tok, nToks)
            } else {
                return ERR_RULE
            }
        }
        r_OR -> {
            if ((InputState != r_IF)) {
                return ERR_RULE
            }
            return addPremise(r, r_OR, tok, nToks)
        }
        r_THEN -> {
            if ((InputState != r_IF)) {
                return ERR_RULE
            }
            InputState = r_THEN
            return addAction(r, tok, nToks)
        }
        r_ELSE -> {
            if ((InputState != r_THEN)) {
                return ERR_RULE
            }
            InputState = r_ELSE
            return addAction(r, tok, nToks)
        }
        r_PRIORITY -> {
            if (((InputState != r_THEN) && (InputState != r_ELSE))) {
                return ERR_RULE
            }
            InputState = r_PRIORITY
            if (!(getDouble(tok!![1], DField<TRule>(Rules!![r]!!, { __o -> __o.priority }, { __o, __x -> __o.priority = __x })) != 0)) {
                return ERR_NUMBER
            }
            if ((nToks > 2)) {
                return ERR_RULE
            }
            return 0
        }
    }
    return 0
}

// controls.c:495
fun controls_evaluate(currentTime: Double, elapsedTime: Double, tStep: Double): Int {
    var r: Int = 0
    var result: Int = 0
    var p: TPremise? = null
    var a: TAction? = null
    CurrentDate = floor(currentTime)
    CurrentTime = (currentTime - floor(currentTime))
    ElapsedTime = elapsedTime
    if ((RuleCount == 0)) {
        return 0
    }
    clearActionList()
    r = 0
    f1@ while ((r < RuleCount)) {
        result = 1
        p = Rules!![r]!!.firstPremise
        w2@ while ((p != null)) {
            if ((p!!.type == r_OR)) {
                if ((result == 0)) {
                    result = evaluatePremise(p, tStep)
                }
            } else {
                if ((result == 0)) {
                    break@w2
                }
                result = evaluatePremise(p, tStep)
            }
            p = p!!.next
        }
        if ((result == 1)) {
            a = Rules!![r]!!.thenActions
        } else {
            a = Rules!![r]!!.elseActions
        }
        w3@ while ((a != null)) {
            updateActionValue(a, currentTime, tStep)
            updateActionList(a)
            a = a!!.next
        }
        r++
    }
    if ((ActionList != null)) {
        return executeActionList(currentTime)
    } else {
        return 0
    }
    throw IllegalStateException("controls_evaluate: no return")
}

// controls.c:556
fun addPremise(r: Int, type: Int, tok: Array<CPtr?>?, nToks: Int): Int {
    var relation: Int = 0
    val n = IBox(0)
    var err: Int = 0
    val value_ = DBox((-10000000000.0))
    var p: TPremise? = null
    var v1: TVariable? = TVariable()
    var v2: TVariable? = TVariable()
    var obj: Int = 0
    var exprIndex: Int = 0
    var varIndex: Int = (-1)
    if ((nToks < 4)) {
        return ERR_ITEMS
    }
    v1!!.attribute = (-1)
    v1!!.object_ = (-1)
    v1!!.index = (-1)
    n.v = 1
    exprIndex = getExpressionIndex(tok!![1])
    if ((exprIndex < 0)) {
        varIndex = getVariableIndex(tok!![n.v])
        if ((varIndex >= 0)) {
            v1 = NamedVariable!![varIndex]!!.variable!!.copy()
        } else {
            err = getPremiseVariable(tok, nToks, n, v1)
            if ((err > 0)) {
                return err
            }
        }
    }
    n.v++
    if ((n.v >= nToks)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    relation = findExactMatch(tok!![n.v], RelOpWords)
    if ((relation < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![n.v])
    }
    v2!!.attribute = (-1)
    v2!!.object_ = (-1)
    v2!!.index = (-1)
    n.v++
    if ((n.v >= nToks)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    varIndex = getVariableIndex(tok!![n.v])
    if ((varIndex >= 0)) {
        v2 = NamedVariable!![varIndex]!!.variable!!.copy()
    } else {
        obj = findmatch(tok!![n.v], ObjectWords)
        if ((obj >= 0)) {
            err = getPremiseVariable(tok, nToks, n, v2)
            if ((err > 0)) {
                return ERR_RULE
            }
            if (((exprIndex < 0) && (v1!!.attribute != v2!!.attribute))) {
                report_writeWarningMsg(__L34, Rules!![r]!!.ID)
            }
        } else {
            err = getPremiseValue(tok!![n.v], v1!!.attribute, value_)
            if ((err > 0)) {
                return err
            }
        }
    }
    n.v++
    if (((n.v < nToks) && (findmatch(tok!![n.v], RuleKeyWords) >= 0))) {
        return ERR_RULE
    }
    p = TPremise()
    if (!(p != null)) {
        return ERR_MEMORY
    }
    p!!.type = type
    p!!.exprIndex = exprIndex
    p!!.lhsVar = v1!!.copy()
    p!!.rhsVar = v2!!.copy()
    p!!.relation = relation
    p!!.value_ = value_.v
    p!!.next = (null)
    if ((Rules!![r]!!.firstPremise == null)) {
        Rules!![r]!!.firstPremise = p
    } else {
        Rules!![r]!!.lastPremise!!.next = p
    }
    Rules!![r]!!.lastPremise = p
    return 0
}

// controls.c:668
fun getPremiseVariable(tok: Array<CPtr?>?, nToks: Int, k: IRef?, v: TVariable?): Int {
    var n: Int = k!!.get()
    var object_: Int = (-1)
    var index: Int = (-1)
    var obj: Int = 0
    var attrib: Int = 0
    obj = findmatch(tok!![n], ObjectWords)
    if ((obj < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![n])
    }
    n++
    if ((n >= nToks)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    when (obj) {
        r_GAGE -> {
            index = project_findObject(GAGE, tok!![n])
            if ((index < 0)) {
                return error_setInpError(ERR_NAME, tok!![n])
            }
            object_ = r_GAGE
        }
        r_NODE -> {
            index = project_findObject(NODE, tok!![n])
            if ((index < 0)) {
                return error_setInpError(ERR_NAME, tok!![n])
            }
            object_ = r_NODE
        }
        r_LINK, r_CONDUIT, r_PUMP, r_ORIFICE, r_WEIR, r_OUTLET -> {
            index = project_findObject(LINK, tok!![n])
            if ((index < 0)) {
                return error_setInpError(ERR_NAME, tok!![n])
            }
            object_ = r_LINK
        }
        else -> {
            n--
        }
    }
    n++
    if ((n >= nToks)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if ((object_ == r_GAGE)) {
        attrib = getGageAttrib(tok!![n])
    } else {
        attrib = findmatch(tok!![n], AttribWords)
    }
    if ((attrib < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![n])
    }
    if ((obj == r_GAGE)) {
    } else if ((obj == r_NODE)) {
        when (attrib) {
            r_DEPTH, r_MAXDEPTH, r_HEAD, r_VOLUME, r_INFLOW -> {
            }
            else -> {
                return error_setInpError(ERR_KEYWORD, tok!![n])
            }
        }
    } else if ((((object_ == r_LINK) && (index >= 0)) && ((((attrib == r_TIMEOPEN) || (attrib == r_TIMECLOSED)))))) {
    } else if (((obj == r_LINK) || (obj == r_CONDUIT))) {
        when (attrib) {
            r_STATUS, r_DEPTH, r_FULLFLOW, r_FULLDEPTH, r_FLOW, r_LENGTH, r_SLOPE, r_VELOCITY -> {
            }
            else -> {
                return error_setInpError(ERR_KEYWORD, tok!![n])
            }
        }
    } else if ((obj == r_PUMP)) {
        when (attrib) {
            r_FLOW, r_SETTING, r_STATUS -> {
            }
            else -> {
                return error_setInpError(ERR_KEYWORD, tok!![n])
            }
        }
    } else if ((((obj == r_ORIFICE) || (obj == r_WEIR)) || (obj == r_OUTLET))) {
        when (attrib) {
            r_FLOW, r_SETTING -> {
            }
            else -> {
                return error_setInpError(ERR_KEYWORD, tok!![n])
            }
        }
    } else {
        when (attrib) {
            r_TIME, r_DATE, r_CLOCKTIME, r_DAY, r_MONTH, r_DAYOFYEAR -> {
            }
            else -> {
                return error_setInpError(ERR_KEYWORD, tok!![n])
            }
        }
    }
    v!!.object_ = object_
    v!!.index = index
    v!!.attribute = attrib
    k!!.set(n)
    return 0
}

// controls.c:797
fun getGageAttrib(token: CPtr?): Int {
    var attrib: Int = 0
    if ((match(token, IntensityWord) != 0)) {
        return 0
    }
    attrib = R.atoi(token)
    if (((attrib < 1) || (attrib > 48))) {
        return (-1)
    }
    return attrib
}

// controls.c:824
fun getPremiseValue(token: CPtr?, attrib: Int, value_: DRef?): Int {
    var strDate: CPtr? = R.buf(25)
    when (attrib) {
        r_STATUS -> {
            value_!!.set((findmatch(token, StatusWords)).toDouble())
            if ((value_!!.get() < 0.0)) {
                value_!!.set((findmatch(token, ConduitWords)).toDouble())
            }
            if ((value_!!.get() < 0.0)) {
                return error_setInpError(ERR_KEYWORD, token)
            }
        }
        r_TIME, r_CLOCKTIME, r_TIMEOPEN, r_TIMECLOSED -> {
            if (!(datetime_strToTime(token, value_) != 0)) {
                return error_setInpError(ERR_DATETIME, token)
            }
        }
        r_DATE -> {
            if (!(datetime_strToDate(token, value_) != 0)) {
                return error_setInpError(ERR_DATETIME, token)
            }
        }
        r_DAY -> {
            if (!(getDouble(token, value_) != 0)) {
                return error_setInpError(ERR_NUMBER, token)
            }
            if (((value_!!.get() < 1.0) || (value_!!.get() > 7.0))) {
                return error_setInpError(ERR_DATETIME, token)
            }
        }
        r_MONTH -> {
            if (!(getDouble(token, value_) != 0)) {
                return error_setInpError(ERR_NUMBER, token)
            }
            if (((value_!!.get() < 1.0) || (value_!!.get() > 12.0))) {
                return error_setInpError(ERR_DATETIME, token)
            }
        }
        r_DAYOFYEAR -> {
            sstrncpy(strDate, token, 6L)
            sstrcat(strDate, __L35, 25L)
            if ((datetime_strToDate(strDate, value_) != 0)) {
                value_!!.set((datetime_dayOfYear(value_!!.get())).toDouble())
            } else if ((((!(getDouble(token, value_) != 0)) || (value_!!.get() < 1.0)) || (value_!!.get() > 365.0))) {
                return error_setInpError(ERR_DATETIME, token)
            }
        }
        else -> {
            if (!(getDouble(token, value_) != 0)) {
                return error_setInpError(ERR_NUMBER, token)
            }
        }
    }
    return 0
}

// controls.c:889
fun addAction(r: Int, tok: Array<CPtr?>?, nToks: Int): Int {
    var obj: Int = 0
    var link: Int = 0
    val attrib = IBox(0)
    val curve = IBox((-1))
    val tseries = IBox((-1))
    var n: Int = 0
    var err: Int = 0
    var values: DoubleArray? = doubleArrayOf(1.0, 0.0, 0.0)
    var a: TAction? = null
    if ((nToks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    obj = findmatch(tok!![1], ObjectWords)
    if (((((((obj != r_LINK) && (obj != r_CONDUIT)) && (obj != r_PUMP)) && (obj != r_ORIFICE)) && (obj != r_WEIR)) && (obj != r_OUTLET))) {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    link = project_findObject(LINK, tok!![2])
    if ((link < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    when (obj) {
        r_CONDUIT -> {
            if ((Link!![link]!!.type != CONDUIT)) {
                return error_setInpError(ERR_NAME, tok!![2])
            }
        }
        r_PUMP -> {
            if ((Link!![link]!!.type != PUMP)) {
                return error_setInpError(ERR_NAME, tok!![2])
            }
        }
        r_ORIFICE -> {
            if ((Link!![link]!!.type != ORIFICE)) {
                return error_setInpError(ERR_NAME, tok!![2])
            }
        }
        r_WEIR -> {
            if ((Link!![link]!!.type != WEIR)) {
                return error_setInpError(ERR_NAME, tok!![2])
            }
        }
        r_OUTLET -> {
            if ((Link!![link]!!.type != OUTLET)) {
                return error_setInpError(ERR_NAME, tok!![2])
            }
        }
    }
    attrib.v = findmatch(tok!![3], AttribWords)
    if ((attrib.v < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![3])
    }
    if ((obj == r_CONDUIT)) {
        if ((attrib.v == r_STATUS)) {
            values!![0] = (findmatch(tok!![5], ConduitWords)).toDouble()
            if ((values!![0] < 0.0)) {
                return error_setInpError(ERR_KEYWORD, tok!![5])
            }
        } else {
            return error_setInpError(ERR_KEYWORD, tok!![3])
        }
    } else if ((obj == r_PUMP)) {
        if ((attrib.v == r_STATUS)) {
            values!![0] = (findmatch(tok!![5], StatusWords)).toDouble()
            if ((values!![0] < 0.0)) {
                return error_setInpError(ERR_KEYWORD, tok!![5])
            }
        } else if ((attrib.v == r_SETTING)) {
            err = setActionSetting(tok, nToks, curve, tseries, attrib, values)
            if ((err > 0)) {
                return err
            }
        } else {
            return error_setInpError(ERR_KEYWORD, tok!![3])
        }
    } else if ((((obj == r_ORIFICE) || (obj == r_WEIR)) || (obj == r_OUTLET))) {
        if ((attrib.v == r_SETTING)) {
            err = setActionSetting(tok, nToks, curve, tseries, attrib, values)
            if ((err > 0)) {
                return err
            }
            if (((attrib.v == r_SETTING) && (((values!![0] < 0.0) || (values!![0] > 1.0))))) {
                return error_setInpError(ERR_NUMBER, tok!![5])
            }
        } else {
            return error_setInpError(ERR_KEYWORD, tok!![3])
        }
    } else {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    n = 6
    if (((curve.v >= 0) || (tseries.v >= 0))) {
        n = 7
    }
    if ((attrib.v == r_PID)) {
        n = 9
    }
    if (((n < nToks) && (findmatch(tok!![n], RuleKeyWords) >= 0))) {
        return ERR_RULE
    }
    a = TAction()
    if (!(a != null)) {
        return ERR_MEMORY
    }
    a!!.rule = r
    a!!.link = link
    a!!.attribute = attrib.v
    a!!.curve = curve.v
    a!!.tseries = tseries.v
    a!!.value_ = values!![0]
    if ((attrib.v == r_PID)) {
        a!!.kp = values!![0]
        a!!.ki = values!![1]
        a!!.kd = values!![2]
        a!!.e1 = 0.0
        a!!.e2 = 0.0
    }
    if ((InputState == r_THEN)) {
        a!!.next = Rules!![r]!!.thenActions
        Rules!![r]!!.thenActions = a
    } else {
        a!!.next = Rules!![r]!!.elseActions
        Rules!![r]!!.elseActions = a
    }
    return 0
}

// controls.c:1028
fun setActionSetting(tok: Array<CPtr?>?, nToks: Int, curve: IRef?, tseries: IRef?, attrib: IRef?, values: DoubleArray?): Int {
    var k: Int = 0
    var m: Int = 0
    if ((nToks < 6)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    k = findmatch(tok!![5], SettingTypeWords)
    if (((k >= 0) && (nToks < 7))) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    when (k) {
        r_CURVE -> {
            m = project_findObject(CURVE, tok!![6])
            if ((m < 0)) {
                return error_setInpError(ERR_NAME, tok!![6])
            }
            curve!!.set(m)
        }
        r_TIMESERIES -> {
            m = project_findObject(TSERIES, tok!![6])
            if ((m < 0)) {
                return error_setInpError(ERR_NAME, tok!![6])
            }
            tseries!!.set(m)
            Tseries!![m]!!.refersTo = CONTROL
        }
        r_PID -> {
            if ((nToks < 9)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            m = 6
            f2@ while ((m <= 8)) {
                if (!(getDouble(tok!![m], DElem(values!!, (m - 6))) != 0)) {
                    return error_setInpError(ERR_NUMBER, tok!![m])
                }
                m++
            }
            attrib!!.set(r_PID)
        }
        else -> {
            if (!(getDouble(tok!![5], DElem(values!!, 0)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![5])
            }
        }
    }
    return 0
}

// controls.c:1086
fun updateActionValue(a: TAction?, currentTime: Double, dt: Double) {
    if ((a!!.curve >= 0)) {
        a!!.value_ = table_lookup(Curve!![a!!.curve], ControlValue)
    } else if ((a!!.tseries >= 0)) {
        a!!.value_ = table_tseriesLookup(Tseries!![a!!.tseries], currentTime, 1)
    } else if ((a!!.attribute == r_PID)) {
        a!!.value_ = getPIDSetting(a, dt)
    }
}

// controls.c:1111
fun getPIDSetting(a: TAction?, __dt: Double): Double {
    var dt: Double = __dt
    var e0: Double = 0.0
    var setting: Double = 0.0
    var p: Double = 0.0
    var i: Double = 0.0
    var d: Double = 0.0
    var update: Double = 0.0
    var tolerance: Double = 0.0001
    dt *= 1440.0
    e0 = (SetPoint - ControlValue)
    if ((abs(e0) > 1e-06)) {
        if ((SetPoint != 0.0)) {
            e0 = (e0 / SetPoint)
        } else {
            e0 = (e0 / ControlValue)
        }
    }
    if ((abs((e0 - a!!.e1)) < tolerance)) {
        a!!.e2 = 0.0
        a!!.e1 = 0.0
    }
    p = ((e0 - a!!.e1))
    if ((a!!.ki == 0.0)) {
        i = 0.0
    } else {
        i = ((e0 * dt) / a!!.ki)
    }
    d = ((a!!.kd * (((e0 - (2.0 * a!!.e1)) + a!!.e2))) / dt)
    update = (a!!.kp * (((p + i) + d)))
    if ((abs(update) < tolerance)) {
        update = 0.0
    }
    setting = (Link!![a!!.link]!!.targetSetting + update)
    a!!.e2 = a!!.e1
    a!!.e1 = e0
    if ((setting < 0.0)) {
        setting = 0.0
    }
    if (((Link!![a!!.link]!!.type != PUMP) && (setting > 1.0))) {
        setting = 1.0
    }
    return setting
}

// controls.c:1168
fun updateActionList(a: TAction?) {
    var listItem: TActionList? = null
    var a1: TAction? = null
    var priority: Double = Rules!![a!!.rule]!!.priority
    listItem = ActionList
    w1@ while ((listItem != null)) {
        a1 = listItem!!.action
        if (!(a1 != null)) {
            break@w1
        }
        if ((a1!!.link == a!!.link)) {
            if ((priority > Rules!![a1!!.rule]!!.priority)) {
                listItem!!.action = a
            }
            return
        }
        listItem = listItem!!.next
    }
    if (!(listItem != null)) {
        listItem = TActionList()
        listItem!!.next = ActionList
        ActionList = listItem
    }
    listItem!!.action = a
}

// controls.c:1206
fun executeActionList(currentTime: Double): Int {
    var listItem: TActionList? = null
    var nextItem: TActionList? = null
    var a1: TAction? = null
    var count: Int = 0
    listItem = ActionList
    w1@ while ((listItem != null)) {
        a1 = listItem!!.action
        if (!(a1 != null)) {
            break@w1
        }
        if ((a1!!.link >= 0)) {
            if ((Link!![a1!!.link]!!.targetSetting != a1!!.value_)) {
                Link!![a1!!.link]!!.targetSetting = a1!!.value_
                if (((((RptFlags!!.controls != 0) && (a1!!.curve < 0)) && (a1!!.tseries < 0)) && (a1!!.attribute != r_PID))) {
                    report_writeControlAction(currentTime, Link!![a1!!.link]!!.ID, a1!!.value_, Rules!![a1!!.rule]!!.ID)
                }
                count++
            }
        }
        nextItem = listItem!!.next
        listItem = nextItem
    }
    return count
}

// controls.c:1243
fun evaluatePremise(p: TPremise?, tStep: Double): Int {
    var lhsValue: Double = 0.0
    var rhsValue: Double = 0.0
    var result: Int = 0
    if ((p!!.exprIndex >= 0)) {
        lhsValue = mathexpr_eval(Expression!![p!!.exprIndex]!!.expression, FP2(::getNamedVariableValue))
    } else {
        lhsValue = getVariableValue(p!!.lhsVar)
    }
    if ((p!!.value_ == (-10000000000.0))) {
        rhsValue = getVariableValue(p!!.rhsVar)
    } else {
        rhsValue = p!!.value_
    }
    if (((lhsValue == (-10000000000.0)) || (rhsValue == (-10000000000.0)))) {
        return 0
    }
    when (p!!.lhsVar!!.attribute) {
        r_TIME, r_CLOCKTIME -> {
            return compareTimes(lhsValue, p!!.relation, rhsValue, (tStep / 2.0))
        }
        r_TIMEOPEN, r_TIMECLOSED -> {
            result = compareTimes(lhsValue, p!!.relation, rhsValue, (tStep / 2.0))
            ControlValue = (lhsValue * 24.0)
            return result
        }
        else -> {
            return compareValues(lhsValue, p!!.relation, rhsValue)
        }
    }
    throw IllegalStateException("evaluatePremise: no return")
}

// controls.c:1286
fun getVariableValue(v: TVariable?): Double {
    var i: Int = (-1)
    var j: Int = (-1)
    if ((v!!.object_ == r_GAGE)) {
        return getRainValue(v)
    }
    if ((v!!.object_ == r_NODE)) {
        i = v!!.index
    }
    if ((v!!.object_ == r_LINK)) {
        j = v!!.index
    }
    when (v!!.attribute) {
        r_TIME -> {
            return ElapsedTime
        }
        r_DATE -> {
            return CurrentDate
        }
        r_CLOCKTIME -> {
            return CurrentTime
        }
        r_DAY -> {
            return (datetime_dayOfWeek(CurrentDate)).toDouble()
        }
        r_MONTH -> {
            return (datetime_monthOfYear(CurrentDate)).toDouble()
        }
        r_DAYOFYEAR -> {
            return (datetime_dayOfYear(CurrentDate)).toDouble()
        }
        r_STATUS -> {
            if (((j < 0) || (((Link!![j]!!.type != CONDUIT) && (Link!![j]!!.type != PUMP))))) {
                return (-10000000000.0)
            } else {
                return Link!![j]!!.setting
            }
        }
        r_SETTING -> {
            if (((j < 0) || ((((Link!![j]!!.type != PUMP) && (Link!![j]!!.type != ORIFICE)) && (Link!![j]!!.type != WEIR))))) {
                return (-10000000000.0)
            } else {
                return Link!![j]!!.setting
            }
        }
        r_FLOW -> {
            if ((j < 0)) {
                return (-10000000000.0)
            } else {
                return (((Link!![j]!!.direction).toDouble() * Link!![j]!!.newFlow) * UCF(FLOW))
            }
        }
        r_FULLFLOW, r_FULLDEPTH, r_VELOCITY, r_LENGTH, r_SLOPE -> {
            if ((j < 0)) {
                return (-10000000000.0)
            } else if ((Link!![j]!!.type != CONDUIT)) {
                return (-10000000000.0)
            }
            when (v!!.attribute) {
                r_FULLFLOW -> {
                    return (Link!![j]!!.qFull * UCF(FLOW))
                }
                r_FULLDEPTH -> {
                    return (Link!![j]!!.xsect!!.yFull * UCF(LENGTH))
                }
                r_VELOCITY -> {
                    return (link_getVelocity(j, Link!![j]!!.newFlow, Link!![j]!!.newDepth) * UCF(LENGTH))
                }
                r_LENGTH -> {
                    return (Conduit!![Link!![j]!!.subIndex]!!.length * UCF(LENGTH))
                }
                r_SLOPE -> {
                    return Conduit!![Link!![j]!!.subIndex]!!.slope
                }
                else -> {
                    return (-10000000000.0)
                }
            }
        }
        r_DEPTH -> {
            if ((j >= 0)) {
                return (Link!![j]!!.newDepth * UCF(LENGTH))
            } else if ((i >= 0)) {
                return (Node!![i]!!.newDepth * UCF(LENGTH))
            } else {
                return (-10000000000.0)
            }
        }
        r_MAXDEPTH -> {
            if ((i >= 0)) {
                return (Node!![i]!!.fullDepth * UCF(LENGTH))
            } else {
                return (-10000000000.0)
            }
        }
        r_HEAD -> {
            if ((i < 0)) {
                return (-10000000000.0)
            }
            return (((Node!![i]!!.newDepth + Node!![i]!!.invertElev)) * UCF(LENGTH))
        }
        r_VOLUME -> {
            if ((i < 0)) {
                return (-10000000000.0)
            }
            return ((Node!![i]!!.newVolume * UCF(VOLUME)))
        }
        r_INFLOW -> {
            if ((i < 0)) {
                return (-10000000000.0)
            } else {
                return (Node!![i]!!.newLatFlow * UCF(FLOW))
            }
        }
        r_TIMEOPEN -> {
            if ((j < 0)) {
                return (-10000000000.0)
            }
            if ((Link!![j]!!.setting <= 0.0)) {
                return (-10000000000.0)
            }
            return ((CurrentDate + CurrentTime) - Link!![j]!!.timeLastSet)
        }
        r_TIMECLOSED -> {
            if ((j < 0)) {
                return (-10000000000.0)
            }
            if ((Link!![j]!!.setting > 0.0)) {
                return (-10000000000.0)
            }
            return ((CurrentDate + CurrentTime) - Link!![j]!!.timeLastSet)
        }
        else -> {
            return (-10000000000.0)
        }
    }
    throw IllegalStateException("getVariableValue: no return")
}

// controls.c:1388
fun getRainValue(v: TVariable?): Double {
    if ((v!!.index < 0)) {
        return (-10000000000.0)
    } else if ((Gage!![v!!.index]!!.isUsed == 0)) {
        return 0.0
    } else if ((v!!.attribute == 0)) {
        return Gage!![v!!.index]!!.rainfall
    } else {
        return gage_getPastRain(v!!.index, v!!.attribute)
    }
    throw IllegalStateException("getRainValue: no return")
}

// controls.c:1405
fun compareTimes(lhsValue: Double, relation: Int, rhsValue: Double, halfStep: Double): Int {
    if ((relation == EQ)) {
        if (((lhsValue >= (rhsValue - halfStep)) && (lhsValue < (rhsValue + halfStep)))) {
            return 1
        }
        return 0
    } else if ((relation == NE)) {
        if (((lhsValue < (rhsValue - halfStep)) || (lhsValue >= (rhsValue + halfStep)))) {
            return 1
        }
        return 0
    } else {
        return compareValues(lhsValue, relation, rhsValue)
    }
    throw IllegalStateException("compareTimes: no return")
}

// controls.c:1432
fun compareValues(lhsValue: Double, relation: Int, rhsValue: Double): Int {
    SetPoint = rhsValue
    ControlValue = lhsValue
    when (relation) {
        EQ -> {
            if ((lhsValue == rhsValue)) {
                return 1
            }
        }
        NE -> {
            if ((lhsValue != rhsValue)) {
                return 1
            }
        }
        LT -> {
            if ((lhsValue < rhsValue)) {
                return 1
            }
        }
        LE -> {
            if ((lhsValue <= rhsValue)) {
                return 1
            }
        }
        GT -> {
            if ((lhsValue > rhsValue)) {
                return 1
            }
        }
        GE -> {
            if ((lhsValue >= rhsValue)) {
                return 1
            }
        }
    }
    return 0
}

// controls.c:1455
fun clearActionList() {
    var listItem: TActionList? = null
    listItem = ActionList
    w1@ while ((listItem != null)) {
        listItem!!.action = (null)
        listItem = listItem!!.next
    }
}

// controls.c:1473
fun deleteActionList() {
    var listItem: TActionList? = null
    var nextItem: TActionList? = null
    listItem = ActionList
    w1@ while ((listItem != null)) {
        nextItem = listItem!!.next
        R.free(listItem)
        listItem = nextItem
    }
    ActionList = (null)
}

// controls.c:1494
fun deleteRules() {
    var p: TPremise? = null
    var pnext: TPremise? = null
    var a: TAction? = null
    var anext: TAction? = null
    var r: Int = 0
    r = 0
    f1@ while ((r < RuleCount)) {
        p = Rules!![r]!!.firstPremise
        w2@ while ((p != null)) {
            pnext = p!!.next
            R.free(p)
            p = pnext
        }
        a = Rules!![r]!!.thenActions
        w3@ while ((a != null)) {
            anext = a!!.next
            R.free(a)
            a = anext
        }
        a = Rules!![r]!!.elseActions
        w4@ while ((a != null)) {
            anext = a!!.next
            R.free(a)
            a = anext
        }
        r++
    }
    if ((Rules != null)) {
        R.free(Rules)
        Rules = (null)
    }
    RuleCount = 0
}

// controls.c:1536
fun findExactMatch(s: CPtr?, keyword: Array<CPtr?>?): Int {
    var i: Int = 0
    w1@ while ((keyword!![i] != null)) {
        if ((strcomp(s, keyword!![i]) != 0)) {
            return (i)
        }
        i++
    }
    return ((-1))
}

