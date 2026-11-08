// Generated from EPA SWMM 5.2.4 (gwater.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_gwater")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var GWTOL: Double = 0.0001
@JvmField var XTOL: Double = 0.001
@JvmField var GWVarWords: Array<CPtr?>? = arrayOf<CPtr?>(__L707, __L708, __L709, __L710, __L711, __L712, __L713, __L714, __L715, __L716, __L717, null)
@JvmField var Area: Double = 0.0
@JvmField var gwater_Infil: Double = 0.0
@JvmField var MaxEvap: Double = 0.0
@JvmField var AvailEvap: Double = 0.0
@JvmField var UpperEvap: Double = 0.0
@JvmField var LowerEvap: Double = 0.0
@JvmField var UpperPerc: Double = 0.0
@JvmField var LowerLoss: Double = 0.0
@JvmField var GWFlow: Double = 0.0
@JvmField var MaxUpperPerc: Double = 0.0
@JvmField var MaxGWFlowPos: Double = 0.0
@JvmField var MaxGWFlowNeg: Double = 0.0
@JvmField var FracPerv: Double = 0.0
@JvmField var TotalDepth: Double = 0.0
@JvmField var Theta: Double = 0.0
@JvmField var HydCon: Double = 0.0
@JvmField var Hgw: Double = 0.0
@JvmField var Hstar: Double = 0.0
@JvmField var Hsw: Double = 0.0
@JvmField var gwater_Tstep: Double = 0.0
@JvmField var A: TAquifer? = TAquifer()
@JvmField var GW: TGroundwater? = null
@JvmField var LatFlowExpr: MathExpr? = null
@JvmField var DeepFlowExpr: MathExpr? = null

// gwater.c:119
fun gwater_readAquiferParams(j: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var p: Int = 0
    var x: DoubleArray? = DoubleArray(12)
    var id: CPtr? = null
    if ((ntoks < 13)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    id = project_findID(AQUIFER, tok!![0])
    if ((id == null)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    i = 0
    f1@ while ((i < 11)) {
        x!![i] = 0.0
        i++
    }
    i = 1
    f2@ while ((i < 13)) {
        if (!(getDouble(tok!![i], DElem(x!!, (i - 1))) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![i])
        }
        i++
    }
    p = (-1)
    if ((ntoks > 13)) {
        p = project_findObject(TIMEPATTERN, tok!![13])
        if ((p < 0)) {
            return error_setInpError(ERR_NAME, tok!![13])
        }
    }
    Aquifer!![j]!!.ID = id
    Aquifer!![j]!!.porosity = x!![0]
    Aquifer!![j]!!.wiltingPoint = x!![1]
    Aquifer!![j]!!.fieldCapacity = x!![2]
    Aquifer!![j]!!.conductivity = (x!![3] / UCF(RAINFALL))
    Aquifer!![j]!!.conductSlope = x!![4]
    Aquifer!![j]!!.tensionSlope = (x!![5] / UCF(LENGTH))
    Aquifer!![j]!!.upperEvapFrac = x!![6]
    Aquifer!![j]!!.lowerEvapDepth = (x!![7] / UCF(LENGTH))
    Aquifer!![j]!!.lowerLossCoeff = (x!![8] / UCF(RAINFALL))
    Aquifer!![j]!!.bottomElev = (x!![9] / UCF(LENGTH))
    Aquifer!![j]!!.waterTableElev = (x!![10] / UCF(LENGTH))
    Aquifer!![j]!!.upperMoisture = x!![11]
    Aquifer!![j]!!.upperEvapPat = p
    return 0
}

// gwater.c:179
fun gwater_readGroundwaterParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(11)
    var gw: TGroundwater? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SUBCATCH, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    if ((ntoks < 11)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    k = project_findObject(AQUIFER, tok!![1])
    if ((k < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    n = project_findObject(NODE, tok!![2])
    if ((n < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    i = 0
    f1@ while ((i < 7)) {
        if (!(getDouble(tok!![(i + 3)], DElem(x!!, i)) != 0)) {
            return error_setInpError(ERR_NUMBER, tok!![(i + 3)])
        }
        i++
    }
    i = 7
    f2@ while ((i < 11)) {
        x!![i] = (-10000000000.0)
        m = (i + 3)
        if (((ntoks > m) && (tok!![m]!!.g(0) != 42))) {
            if (!(getDouble(tok!![m], DElem(x!!, i)) != 0)) {
                return error_setInpError(ERR_NUMBER, tok!![m])
            }
            if ((i < 10)) {
                x!![i] /= (UCF(LENGTH))
            }
        }
        i++
    }
    if (!(Subcatch!![j]!!.groundwater != null)) {
        gw = TGroundwater()
        if (!(gw != null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        Subcatch!![j]!!.groundwater = gw
    } else {
        gw = Subcatch!![j]!!.groundwater
    }
    gw!!.aquifer = k
    gw!!.node = n
    gw!!.surfElev = (x!![0] / UCF(LENGTH))
    gw!!.a1 = x!![1]
    gw!!.b1 = x!![2]
    gw!!.a2 = x!![3]
    gw!!.b2 = x!![4]
    gw!!.a3 = x!![5]
    gw!!.fixedDepth = (x!![6] / UCF(LENGTH))
    gw!!.nodeElev = x!![7]
    gw!!.bottomElev = x!![8]
    gw!!.waterTableElev = x!![9]
    gw!!.upperMoisture = x!![10]
    return 0
}

// gwater.c:258
fun gwater_readFlowExpression(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var exprStr: CPtr? = R.buf(1025)
    var expr: MathExpr? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    j = project_findObject(SUBCATCH, tok!![0])
    if ((j < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    k = 1
    if ((match(tok!![1], __L158) != 0)) {
        k = 1
    } else if ((match(tok!![1], __L159) != 0)) {
        k = 2
    } else {
        return error_setInpError(ERR_KEYWORD, tok!![1])
    }
    sstrncpy(exprStr, tok!![2], 1024L)
    i = 3
    f1@ while ((i < ntoks)) {
        sstrcat(exprStr, __L33, ((1024 + 1)).toLong())
        sstrcat(exprStr, tok!![i], ((1024 + 1)).toLong())
        i++
    }
    if ((k == 1)) {
        mathexpr_delete(Subcatch!![j]!!.gwLatFlowExpr)
    } else {
        mathexpr_delete(Subcatch!![j]!!.gwDeepFlowExpr)
    }
    expr = mathexpr_create(exprStr, FP1(::gwater_getVariableIndex))
    if ((expr == null)) {
        return error_setInpError(ERR_MATH_EXPR, __L0)
    }
    if ((k == 1)) {
        Subcatch!![j]!!.gwLatFlowExpr = expr
    } else {
        Subcatch!![j]!!.gwDeepFlowExpr = expr
    }
    return 0
}

// gwater.c:315
fun gwater_deleteFlowExpression(j: Int) {
    mathexpr_delete(Subcatch!![j]!!.gwLatFlowExpr)
    mathexpr_delete(Subcatch!![j]!!.gwDeepFlowExpr)
}

// gwater.c:328
fun gwater_validateAquifer(j: Int) {
    var p: Int = 0
    if ((((((((((((Aquifer!![j]!!.porosity <= 0.0) || (Aquifer!![j]!!.fieldCapacity >= Aquifer!![j]!!.porosity)) || (Aquifer!![j]!!.wiltingPoint >= Aquifer!![j]!!.fieldCapacity)) || (Aquifer!![j]!!.conductivity <= 0.0)) || (Aquifer!![j]!!.conductSlope < 0.0)) || (Aquifer!![j]!!.tensionSlope < 0.0)) || (Aquifer!![j]!!.upperEvapFrac < 0.0)) || (Aquifer!![j]!!.lowerEvapDepth < 0.0)) || (Aquifer!![j]!!.waterTableElev < Aquifer!![j]!!.bottomElev)) || (Aquifer!![j]!!.upperMoisture > Aquifer!![j]!!.porosity)) || (Aquifer!![j]!!.upperMoisture < Aquifer!![j]!!.wiltingPoint))) {
        report_writeErrorMsg(ERR_AQUIFER_PARAMS, Aquifer!![j]!!.ID)
    }
    p = Aquifer!![j]!!.upperEvapPat
    if (((p >= 0) && (Pattern!![p]!!.type != MONTHLY_PATTERN))) {
        report_writeErrorMsg(ERR_AQUIFER_PARAMS, Aquifer!![j]!!.ID)
    }
}

// gwater.c:359
fun gwater_validate(j: Int) {
    var a: TAquifer? = TAquifer()
    var gw: TGroundwater? = null
    gw = Subcatch!![j]!!.groundwater
    if ((gw != null)) {
        a = Aquifer!![gw!!.aquifer]!!.copy()
        if ((gw!!.bottomElev == (-10000000000.0))) {
            gw!!.bottomElev = a!!.bottomElev
        }
        if ((gw!!.waterTableElev == (-10000000000.0))) {
            gw!!.waterTableElev = a!!.waterTableElev
        }
        if ((gw!!.upperMoisture == (-10000000000.0))) {
            gw!!.upperMoisture = a!!.upperMoisture
        }
        if ((gw!!.surfElev < gw!!.waterTableElev)) {
            report_writeErrorMsg(ERR_GROUND_ELEV, Subcatch!![j]!!.ID)
        }
    }
}

// gwater.c:382
fun gwater_initState(j: Int) {
    var a: TAquifer? = TAquifer()
    var gw: TGroundwater? = null
    gw = Subcatch!![j]!!.groundwater
    if ((gw != null)) {
        a = Aquifer!![gw!!.aquifer]!!.copy()
        gw!!.theta = gw!!.upperMoisture
        if ((gw!!.theta >= a!!.porosity)) {
            gw!!.theta = (a!!.porosity - XTOL)
        }
        gw!!.lowerDepth = (gw!!.waterTableElev - gw!!.bottomElev)
        if ((gw!!.lowerDepth >= (gw!!.surfElev - gw!!.bottomElev))) {
            gw!!.lowerDepth = ((gw!!.surfElev - gw!!.bottomElev) - XTOL)
        }
        gw!!.oldFlow = 0.0
        gw!!.newFlow = 0.0
        gw!!.evapLoss = 0.0
        gw!!.maxInfilVol = ((((gw!!.surfElev - gw!!.waterTableElev)) * ((a!!.porosity - gw!!.theta))) / subcatch_getFracPerv(j))
    }
}

// gwater.c:425
fun gwater_getState(j: Int, x: DoubleArray?) {
    var gw: TGroundwater? = Subcatch!![j]!!.groundwater
    x!![0] = gw!!.theta
    x!![1] = (gw!!.bottomElev + gw!!.lowerDepth)
    x!![2] = gw!!.newFlow
    x!![3] = gw!!.maxInfilVol
}

// gwater.c:441
fun gwater_setState(j: Int, x: DoubleArray?) {
    var gw: TGroundwater? = Subcatch!![j]!!.groundwater
    if ((gw == null)) {
        return
    }
    gw!!.theta = x!![0]
    gw!!.lowerDepth = (x!![1] - gw!!.bottomElev)
    gw!!.oldFlow = x!![2]
    if ((x!![3] != (-10000000000.0))) {
        gw!!.maxInfilVol = x!![3]
    }
}

// gwater.c:458
fun gwater_getVolume(j: Int): Double {
    var a: TAquifer? = TAquifer()
    var gw: TGroundwater? = null
    var upperDepth: Double = 0.0
    gw = Subcatch!![j]!!.groundwater
    if ((gw == null)) {
        return 0.0
    }
    a = Aquifer!![gw!!.aquifer]!!.copy()
    upperDepth = ((gw!!.surfElev - gw!!.bottomElev) - gw!!.lowerDepth)
    return (((upperDepth * gw!!.theta)) + ((gw!!.lowerDepth * a!!.porosity)))
}

// gwater.c:477
fun gwater_getGroundwater(j: Int, __evap: Double, __infil: Double, tStep: Double) {
    var evap: Double = __evap
    var infil: Double = __infil
    var n: Int = 0
    var x: DoubleArray? = DoubleArray(2)
    var vUpper: Double = 0.0
    var nodeFlow: Double = 0.0
    GW = Subcatch!![j]!!.groundwater
    if ((GW == null)) {
        return
    }
    LatFlowExpr = Subcatch!![j]!!.gwLatFlowExpr
    DeepFlowExpr = Subcatch!![j]!!.gwDeepFlowExpr
    A = Aquifer!![GW!!.aquifer]!!.copy()
    FracPerv = subcatch_getFracPerv(j)
    if ((FracPerv <= 0.0)) {
        return
    }
    Area = Subcatch!![j]!!.area
    infil = ((infil / Area) / tStep)
    gwater_Infil = infil
    gwater_Tstep = tStep
    evap = ((evap / Area) / tStep)
    MaxEvap = (Evap!!.rate * FracPerv)
    AvailEvap = ((if ((((((MaxEvap - evap))) >= (0.0)))) (((MaxEvap - evap))) else (0.0)))
    TotalDepth = (GW!!.surfElev - GW!!.bottomElev)
    if ((TotalDepth <= 0.0)) {
        return
    }
    n = GW!!.node
    if ((GW!!.nodeElev != (-10000000000.0))) {
        Hstar = (GW!!.nodeElev - GW!!.bottomElev)
    } else {
        Hstar = (Node!![n]!!.invertElev - GW!!.bottomElev)
    }
    if ((GW!!.fixedDepth > 0.0)) {
        Hsw = ((GW!!.fixedDepth + Node!![n]!!.invertElev) - GW!!.bottomElev)
    } else {
        Hsw = ((Node!![n]!!.newDepth + Node!![n]!!.invertElev) - GW!!.bottomElev)
    }
    x!![THETA] = GW!!.theta
    x!![LOWERDEPTH] = GW!!.lowerDepth
    vUpper = (((TotalDepth - x!![LOWERDEPTH])) * ((x!![THETA] - A!!.fieldCapacity)))
    vUpper = ((if ((((0.0) >= (vUpper)))) (0.0) else (vUpper)))
    MaxUpperPerc = (vUpper / tStep)
    MaxGWFlowPos = ((x!![LOWERDEPTH] * A!!.porosity) / tStep)
    MaxGWFlowNeg = ((((TotalDepth - x!![LOWERDEPTH])) * ((A!!.porosity - x!![THETA]))) / tStep)
    nodeFlow = (((Node!![n]!!.inflow + (Node!![n]!!.newVolume / tStep))) / Area)
    MaxGWFlowNeg = (-((if ((((MaxGWFlowNeg) <= (nodeFlow)))) (MaxGWFlowNeg) else (nodeFlow))))
    odesolve_integrate(x, 2, 0.0, tStep, GWTOL, tStep, OdeDerivs(::getDxDt))
    x!![THETA] = ((if ((((x!![THETA]) >= (A!!.wiltingPoint)))) (x!![THETA]) else (A!!.wiltingPoint)))
    if ((x!![THETA] >= A!!.porosity)) {
        x!![THETA] = (A!!.porosity - XTOL)
        x!![LOWERDEPTH] = (TotalDepth - XTOL)
    }
    x!![LOWERDEPTH] = ((if ((((x!![LOWERDEPTH]) >= (0.0)))) (x!![LOWERDEPTH]) else (0.0)))
    if ((x!![LOWERDEPTH] >= TotalDepth)) {
        x!![LOWERDEPTH] = (TotalDepth - XTOL)
    }
    GW!!.theta = x!![THETA]
    GW!!.lowerDepth = x!![LOWERDEPTH]
    getFluxes(GW!!.theta, GW!!.lowerDepth)
    GW!!.oldFlow = GW!!.newFlow
    GW!!.newFlow = GWFlow
    GW!!.evapLoss = (UpperEvap + LowerEvap)
    GW!!.maxInfilVol = ((((TotalDepth - x!![LOWERDEPTH])) * ((A!!.porosity - x!![THETA]))) / FracPerv)
    updateMassBal(Area, tStep)
    stats_updateGwaterStats(j, infil, GW!!.evapLoss, GWFlow, LowerLoss, GW!!.theta, (GW!!.lowerDepth + GW!!.bottomElev), tStep)
}

// gwater.c:605
fun updateMassBal(area: Double, tStep: Double) {
    var vInfil: Double = 0.0
    var vUpperEvap: Double = 0.0
    var vLowerEvap: Double = 0.0
    var vLowerPerc: Double = 0.0
    var vGwater: Double = 0.0
    var ft2sec: Double = (area * tStep)
    vInfil = (gwater_Infil * ft2sec)
    vUpperEvap = (UpperEvap * ft2sec)
    vLowerEvap = (LowerEvap * ft2sec)
    vLowerPerc = (LowerLoss * ft2sec)
    vGwater = ((0.5 * ((GW!!.oldFlow + GW!!.newFlow))) * ft2sec)
    massbal_updateGwaterTotals(vInfil, vUpperEvap, vLowerEvap, vLowerPerc, vGwater)
}

// gwater.c:631
fun getFluxes(theta: Double, __lowerDepth: Double) {
    var lowerDepth: Double = __lowerDepth
    var upperDepth: Double = 0.0
    lowerDepth = ((if ((((lowerDepth) >= (0.0)))) (lowerDepth) else (0.0)))
    lowerDepth = ((if ((((lowerDepth) <= (TotalDepth)))) (lowerDepth) else (TotalDepth)))
    upperDepth = (TotalDepth - lowerDepth)
    Hgw = lowerDepth
    Theta = theta
    gwater_getEvapRates(theta, upperDepth)
    UpperPerc = getUpperPerc(theta, upperDepth)
    UpperPerc = ((if ((((UpperPerc) <= (MaxUpperPerc)))) (UpperPerc) else (MaxUpperPerc)))
    if ((DeepFlowExpr != null)) {
        LowerLoss = (mathexpr_eval(DeepFlowExpr, FP2(::gwater_getVariableValue)) / UCF(RAINFALL))
    } else {
        LowerLoss = ((A!!.lowerLossCoeff * lowerDepth) / TotalDepth)
    }
    LowerLoss = ((if ((((LowerLoss) <= ((lowerDepth / gwater_Tstep))))) (LowerLoss) else ((lowerDepth / gwater_Tstep))))
    GWFlow = getGWFlow(lowerDepth)
    if ((LatFlowExpr != null)) {
        GWFlow += (mathexpr_eval(LatFlowExpr, FP2(::gwater_getVariableValue)) / UCF(GWFLOW))
    }
    if ((GWFlow >= 0.0)) {
        GWFlow = ((if ((((GWFlow) <= (MaxGWFlowPos)))) (GWFlow) else (MaxGWFlowPos)))
    } else {
        GWFlow = ((if ((((GWFlow) >= (MaxGWFlowNeg)))) (GWFlow) else (MaxGWFlowNeg)))
    }
}

// gwater.c:677
fun getDxDt(t: Double, x: DoubleArray?, dxdt: DoubleArray?) {
    var qUpper: Double = 0.0
    var qLower: Double = 0.0
    var denom: Double = 0.0
    getFluxes(x!![THETA], x!![LOWERDEPTH])
    qUpper = ((gwater_Infil - UpperEvap) - UpperPerc)
    qLower = (((UpperPerc - LowerLoss) - LowerEvap) - GWFlow)
    denom = (TotalDepth - x!![LOWERDEPTH])
    if ((denom > 0.0)) {
        dxdt!![THETA] = (qUpper / denom)
    } else {
        dxdt!![THETA] = 0.0
    }
    denom = (A!!.porosity - x!![THETA])
    if ((denom > 0.0)) {
        dxdt!![LOWERDEPTH] = (qLower / denom)
    } else {
        dxdt!![LOWERDEPTH] = 0.0
    }
}

// gwater.c:713
fun gwater_getEvapRates(theta: Double, upperDepth: Double) {
    var p: Int = 0
    var month: Int = 0
    var f: Double = 0.0
    var lowerFrac: Double = 0.0
    var upperFrac: Double = 0.0
    UpperEvap = 0.0
    LowerEvap = 0.0
    if ((gwater_Infil > 0.0)) {
        return
    }
    upperFrac = A!!.upperEvapFrac
    f = 1.0
    p = A!!.upperEvapPat
    if ((p >= 0)) {
        month = datetime_monthOfYear(getDateTime(NewRunoffTime))
        f = Pattern!![p]!!.factor!![(month - 1)]
    }
    upperFrac *= f
    if ((theta > A!!.wiltingPoint)) {
        UpperEvap = (upperFrac * MaxEvap)
        UpperEvap = ((if ((((UpperEvap) <= (AvailEvap)))) (UpperEvap) else (AvailEvap)))
    }
    if ((A!!.lowerEvapDepth > 0.0)) {
        lowerFrac = (((A!!.lowerEvapDepth - upperDepth)) / A!!.lowerEvapDepth)
        lowerFrac = ((if ((((0.0) >= (lowerFrac)))) (0.0) else (lowerFrac)))
        lowerFrac = ((if ((((lowerFrac) <= (1.0)))) (lowerFrac) else (1.0)))
        LowerEvap = ((lowerFrac * ((1.0 - upperFrac))) * MaxEvap)
        LowerEvap = ((if ((((LowerEvap) <= (((AvailEvap - UpperEvap)))))) (LowerEvap) else (((AvailEvap - UpperEvap)))))
    }
}

// gwater.c:769
fun getUpperPerc(theta: Double, upperDepth: Double): Double {
    var delta: Double = 0.0
    var dhdz: Double = 0.0
    var hydcon: Double = 0.0
    if (((upperDepth <= 0.0) || (theta <= A!!.fieldCapacity))) {
        return 0.0
    }
    delta = (theta - A!!.porosity)
    hydcon = (A!!.conductivity * Glibm.exp((delta * A!!.conductSlope)))
    delta = (theta - A!!.fieldCapacity)
    dhdz = (1.0 + (((A!!.tensionSlope * 2.0) * delta) / upperDepth))
    HydCon = hydcon
    return (hydcon * dhdz)
}

// gwater.c:799
fun getGWFlow(lowerDepth: Double): Double {
    var q: Double = 0.0
    var t1: Double = 0.0
    var t2: Double = 0.0
    var t3: Double = 0.0
    if ((lowerDepth <= Hstar)) {
        return 0.0
    }
    if ((GW!!.b1 == 0.0)) {
        t1 = GW!!.a1
    } else {
        t1 = (GW!!.a1 * Glibm.pow((((lowerDepth - Hstar)) * UCF(LENGTH)), GW!!.b1))
    }
    if ((GW!!.b2 == 0.0)) {
        t2 = GW!!.a2
    } else if ((Hsw > Hstar)) {
        t2 = (GW!!.a2 * Glibm.pow((((Hsw - Hstar)) * UCF(LENGTH)), GW!!.b2))
    } else {
        t2 = 0.0
    }
    t3 = ((((GW!!.a3 * lowerDepth) * Hsw) * UCF(LENGTH)) * UCF(LENGTH))
    q = ((((t1 - t2) + t3)) / UCF(GWFLOW))
    if (((q < 0.0) && (GW!!.a3 != 0.0))) {
        q = 0.0
    }
    return q
}

// gwater.c:834
fun gwater_getVariableIndex(s: CPtr?): Int {
    var k: Int = 0
    k = findmatch(s, GWVarWords)
    if ((k >= 0)) {
        return k
    }
    return (-1)
}

// gwater.c:850
fun gwater_getVariableValue(varIndex: Int): Double {
    when (varIndex) {
        gwvHGW -> {
            return (Hgw * UCF(LENGTH))
        }
        gwvHSW -> {
            return (Hsw * UCF(LENGTH))
        }
        gwvHCB -> {
            return (Hstar * UCF(LENGTH))
        }
        gwvHGS -> {
            return (TotalDepth * UCF(LENGTH))
        }
        gwvKS -> {
            return (A!!.conductivity * UCF(RAINFALL))
        }
        gwvK -> {
            return (HydCon * UCF(RAINFALL))
        }
        gwvTHETA -> {
            return Theta
        }
        gwvPHI -> {
            return A!!.porosity
        }
        gwvFI -> {
            return (gwater_Infil * UCF(RAINFALL))
        }
        gwvFU -> {
            return (UpperPerc * UCF(RAINFALL))
        }
        gwvA -> {
            return (Area * UCF(LANDAREA))
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("getVariableValue: no return")
}

