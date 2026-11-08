// Generated from EPA SWMM 5.2.4 (inlet.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_inlet")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var InletDesigns: Array<TInletDesign?>? = null
@JvmField var InletDesignCount: Int = 0
@JvmField var UsesInlets: Int = 0
@JvmField var InletTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L718, __L719, __L0, __L720, __L721, __L722, __L723, null)
@JvmField var GrateTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L724, __L725, __L726, __L727, __L728, __L729, __L730, __L731, null)
@JvmField var ThroatAngleWords: Array<CPtr?>? = arrayOf<CPtr?>(__L732, __L733, __L734, null)
@JvmField var PlacementTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L735, __L736, __L737, null)
@JvmField var SplashCoeffs: Array<DoubleArray?>? = arrayOf<DoubleArray?>(doubleArrayOf(2.22, 4.03, 0.65, 0.06), doubleArrayOf(0.74, 2.44, 0.27, 0.02), doubleArrayOf(1.76, 3.12, 0.45, 0.03), doubleArrayOf(0.30, 4.85, 1.31, 0.15), doubleArrayOf(0.99, 2.64, 0.36, 0.03), doubleArrayOf(0.51, 2.34, 0.2, 0.01), doubleArrayOf(0.28, 2.28, 0.18, 0.01))
@JvmField var GrateOpeningRatios: DoubleArray? = doubleArrayOf(0.90, 0.80, 0.60, 0.35, 0.17, 0.34, 0.80, 1.00)
@JvmField var Sx: Double = 0.0
@JvmField var SL: Double = 0.0
@JvmField var Sw: Double = 0.0
@JvmField var a: Double = 0.0
@JvmField var W: Double = 0.0
@JvmField var T: Double = 0.0
@JvmField var n: Double = 0.0
@JvmField var Nsides: Int = 0
@JvmField var Tcrown: Double = 0.0
@JvmField var Beta: Double = 0.0
@JvmField var Qfactor: Double = 0.0
@JvmField var xsect: TXsect? = null
@JvmField var InletFlow: DoubleArray? = null
@JvmField var FirstInlet: TInlet? = null

// inlet.c:257
fun inlet_create(numInlets: Int): Int {
    var i: Int = 0
    InletDesigns = (null)
    InletFlow = (null)
    InletDesignCount = 0
    UsesInlets = 0
    FirstInlet = (null)
    InletDesigns = Array<TInletDesign?>(((numInlets).toLong()).toInt()) { TInletDesign() }
    if ((InletDesigns == null)) {
        return ERR_MEMORY
    }
    InletDesignCount = numInlets
    InletFlow = DoubleArray(((Nobjects!![NODE]).toLong()).toInt())
    if ((InletFlow == null)) {
        return ERR_MEMORY
    }
    i = 0
    f1@ while ((i < InletDesignCount)) {
        InletDesigns!![i]!!.customCurve = (-1)
        InletDesigns!![i]!!.curbInlet!!.length = 0.0
        InletDesigns!![i]!!.grateInlet!!.length = 0.0
        InletDesigns!![i]!!.slottedInlet!!.length = 0.0
        InletDesigns!![i]!!.type = CUSTOM_INLET
        i++
    }
    return 0
}

// inlet.c:291
fun inlet_delete() {
    var inlet: TInlet? = FirstInlet
    var nextInlet: TInlet? = null
    w1@ while ((inlet != null)) {
        nextInlet = inlet!!.nextInlet
        R.free(inlet)
        inlet = nextInlet
    }
    FirstInlet = (null)
    if ((InletFlow != null)) {
        R.free(InletFlow)
        InletFlow = (null)
    }
    if ((InletDesigns != null)) {
        R.free(InletDesigns)
        InletDesigns = (null)
    }
}

// inlet.c:313
fun inlet_readDesignParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = project_findObject(INLET, tok!![0])
    if ((i < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    InletDesigns!![i]!!.ID = project_findID(INLET, tok!![0])
    InletDesigns!![i]!!.type = findmatch(tok!![1], InletTypeWords)
    when (InletDesigns!![i]!!.type) {
        GRATE_INLET, DROP_GRATE_INLET -> {
            return readGrateInletParams(i, tok, ntoks)
        }
        CURB_INLET, DROP_CURB_INLET -> {
            return readCurbInletParams(i, tok, ntoks)
        }
        SLOTTED_INLET -> {
            return readSlottedInletParams(i, tok, ntoks)
        }
        CUSTOM_INLET -> {
            return readCustomInletParams(i, tok, ntoks)
        }
        else -> {
            return error_setInpError(ERR_KEYWORD, tok!![1])
        }
    }
    throw IllegalStateException("inlet_readDesignParams: no return")
}

// inlet.c:362
fun inlet_readUsageParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var linkIndex: Int = 0
    var designIndex: Int = 0
    var nodeIndex: Int = 0
    val numInlets = IBox(1)
    var placement: Int = AUTOMATIC
    val flowLimit = DBox(0.0)
    val pctClogged = DBox(0.0)
    val aLocal = DBox(0.0)
    val wLocal = DBox(0.0)
    var inlet: TInlet? = null
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    linkIndex = project_findObject(LINK, tok!![0])
    if ((linkIndex < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    designIndex = project_findObject(INLET, tok!![1])
    if ((designIndex < 0)) {
        return error_setInpError(ERR_NAME, tok!![1])
    }
    nodeIndex = project_findObject(NODE, tok!![2])
    if ((nodeIndex < 0)) {
        return error_setInpError(ERR_NAME, tok!![2])
    }
    if ((ntoks > 3)) {
        if (((!(getInt(tok!![3], numInlets) != 0)) || (numInlets.v < 1))) {
            return error_setInpError(ERR_NUMBER, tok!![3])
        }
    }
    if ((ntoks > 4)) {
        if ((((!(getDouble(tok!![4], pctClogged) != 0)) || (pctClogged.v < 0.0)) || (pctClogged.v > 99.0))) {
            return error_setInpError(ERR_NUMBER, tok!![4])
        }
    }
    if ((ntoks > 5)) {
        if (((!(getDouble(tok!![5], flowLimit) != 0)) || (flowLimit.v < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
    }
    if ((ntoks > 6)) {
        if (((!(getDouble(tok!![6], aLocal) != 0)) || (aLocal.v < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![6])
        }
    }
    if ((ntoks > 7)) {
        if (((!(getDouble(tok!![7], wLocal) != 0)) || (wLocal.v < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![7])
        }
    }
    if ((ntoks > 8)) {
        placement = findmatch(tok!![8], PlacementTypeWords)
        if ((placement < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![8])
        }
    }
    inlet = Link!![linkIndex]!!.inlet
    if ((inlet == null)) {
        inlet = TInlet()
        if (!(inlet != null)) {
            return error_setInpError(ERR_MEMORY, __L0)
        }
        Link!![linkIndex]!!.inlet = inlet
        inlet!!.nextInlet = FirstInlet
        FirstInlet = inlet
    }
    inlet!!.linkIndex = linkIndex
    inlet!!.designIndex = designIndex
    inlet!!.nodeIndex = nodeIndex
    inlet!!.numInlets = numInlets.v
    inlet!!.placement = placement
    inlet!!.clogFactor = (1.0 - ((pctClogged.v / 100.0)))
    inlet!!.flowLimit = (flowLimit.v / UCF(FLOW))
    inlet!!.localDepress = (aLocal.v / UCF(LENGTH))
    inlet!!.localWidth = (wLocal.v / UCF(LENGTH))
    inlet!!.flowFactor = 0.0
    inlet!!.backflowRatio = 0.0
    initInletStats(inlet)
    UsesInlets = 1
    return 0
}

// inlet.c:464
fun inlet_validate() {
    var i: Int = 0
    var j: Int = 0
    var inletType: Int = 0
    var inletValid: Int = 0
    var inlet: TInlet? = null
    var prevInlet: TInlet? = null
    if (!(UsesInlets != 0)) {
        return
    }
    prevInlet = FirstInlet
    inlet = FirstInlet
    w1@ while ((inlet != null)) {
        inletValid = 0
        i = inlet!!.linkIndex
        xsect = Link!![i]!!.xsect
        inletType = InletDesigns!![inlet!!.designIndex]!!.type
        if ((inletType == CUSTOM_INLET)) {
            j = InletDesigns!![inlet!!.designIndex]!!.customCurve
            if ((j >= 0)) {
                if (((Curve!![j]!!.curveType == DIVERSION_CURVE) || (Curve!![j]!!.curveType == RATING_CURVE))) {
                    inletValid = 1
                }
            }
        } else if (((((xsect!!.type == TRAPEZOIDAL) || (xsect!!.type == RECT_OPEN))) && (((inletType == DROP_GRATE_INLET) || (inletType == DROP_CURB_INLET))))) {
            inletValid = 1
        } else if ((((xsect!!.type == STREET_XSECT) && (inletType != DROP_GRATE_INLET)) && (inletType != DROP_CURB_INLET))) {
            inletValid = 1
        }
        if ((inletValid != 0)) {
            Node!![Link!![i]!!.node2]!!.inlet = BYPASS
            Node!![inlet!!.nodeIndex]!!.inlet = CAPTURE
            inlet!!.backflow = 0.0
            getConduitGeometry(inlet)
            inlet!!.flowFactor = ((((0.56 / n)) * Glibm.pow(SL, 0.5)) * Glibm.pow(Sx, 1.67))
            prevInlet = inlet
            inlet = inlet!!.nextInlet
        } else {
            report_writeWarningMsg(__L187, Link!![i]!!.ID)
            if ((inlet === FirstInlet)) {
                FirstInlet = inlet!!.nextInlet
                prevInlet = FirstInlet
                R.free(inlet)
                inlet = FirstInlet
            } else {
                prevInlet!!.nextInlet = inlet!!.nextInlet
                R.free(inlet)
                inlet = prevInlet!!.nextInlet
            }
            Link!![i]!!.inlet = (null)
        }
    }
    getBackflowRatios()
}

// inlet.c:555
fun inlet_findCapturedFlows(tStep: Double) {
    var i: Int = 0
    var j: Int = 0
    var m: Int = 0
    var placement: Int = 0
    var q: Double = 0.0
    var inlet: TInlet? = null
    if (!(UsesInlets != 0)) {
        return
    }
    R.memset(InletFlow, 0, ((Nobjects!![NODE]).toLong() * 8L))
    if ((RouteModel != DW)) {
        j = 0
        f1@ while ((j < Nobjects!![NODE])) {
            Node!![j]!!.inflow = ((if ((((0.0) >= (Node!![j]!!.newLatFlow)))) (0.0) else (Node!![j]!!.newLatFlow)))
            j++
        }
        i = 0
        f2@ while ((i < Nobjects!![LINK])) {
            Node!![Link!![i]!!.node2]!!.inflow += ((if ((((0.0) >= (Link!![i]!!.newFlow)))) (0.0) else (Link!![i]!!.newFlow)))
            i++
        }
    }
    inlet = FirstInlet
    f3@ while ((inlet != null)) {
        i = inlet!!.linkIndex
        j = Link!![i]!!.node2
        m = inlet!!.nodeIndex
        placement = getInletPlacement(inlet, j)
        if ((InletDesigns!![inlet!!.designIndex]!!.type == CUSTOM_INLET)) {
            q = abs(Link!![i]!!.newFlow)
            inlet!!.flowCapture = getCustomCapturedFlow(inlet, q, Node!![j]!!.newDepth)
        } else if ((placement == ON_GRADE)) {
            q = abs(Link!![i]!!.newFlow)
            inlet!!.flowCapture = getOnGradeCapturedFlow(inlet, q, Node!![j]!!.newDepth)
        } else {
            q = Node!![j]!!.inflow
            inlet!!.flowCapture = getOnSagCapturedFlow(inlet, q, Node!![j]!!.newDepth)
        }
        if ((abs(inlet!!.flowCapture) < 0.0001)) {
            inlet!!.flowCapture = 0.0
        }
        InletFlow!![j] += inlet!!.flowCapture
        inlet!!.backflow = (Node!![m]!!.overflow * inlet!!.backflowRatio)
        if ((abs(inlet!!.backflow) < 0.0001)) {
            inlet!!.backflow = 0.0
        }
        inlet = inlet!!.nextInlet
    }
    inlet = FirstInlet
    f4@ while ((inlet != null)) {
        i = inlet!!.linkIndex
        j = Link!![i]!!.node2
        m = inlet!!.nodeIndex
        if (((RouteModel != DW) && (getInletPlacement(inlet, j) == ON_SAG))) {
            q = (Node!![j]!!.newVolume / tStep)
            q += ((if ((((Node!![j]!!.inflow) >= (0.0)))) (Node!![j]!!.inflow) else (0.0)))
            if ((InletFlow!![j] > q)) {
                inlet!!.flowCapture *= (q / InletFlow!![j])
            }
        }
        Node!![j]!!.newLatFlow -= ((inlet!!.flowCapture - inlet!!.backflow))
        Node!![m]!!.newLatFlow += inlet!!.flowCapture
        if ((getDateTime(NewRoutingTime) > ReportStart)) {
            updateInletStats(inlet, abs(Link!![i]!!.newFlow))
        }
        inlet = inlet!!.nextInlet
    }
}

// inlet.c:655
fun inlet_adjustQualInflows() {
    var i: Int = 0
    var j: Int = 0
    var m: Int = 0
    var p: Int = 0
    var qNet: Double = 0.0
    var inlet: TInlet? = null
    if (!(UsesInlets != 0)) {
        return
    }
    if (((IgnoreQuality != 0) || (Nobjects!![POLLUT] == 0))) {
        return
    }
    inlet = FirstInlet
    f1@ while ((inlet != null)) {
        i = inlet!!.linkIndex
        j = Link!![i]!!.node2
        m = inlet!!.nodeIndex
        qNet = (inlet!!.flowCapture - inlet!!.backflow)
        if ((qNet > 0.0)) {
            Node!![m]!!.qualInflow += qNet
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                Node!![m]!!.newQual!![p] += (qNet * Node!![j]!!.oldQual!![p])
                p++
            }
        } else {
            qNet = (-qNet)
            Node!![j]!!.qualInflow += qNet
            p = 0
            f3@ while ((p < Nobjects!![POLLUT])) {
                Node!![j]!!.newQual!![p] += (qNet * Node!![m]!!.oldQual!![p])
                p++
            }
        }
        inlet = inlet!!.nextInlet
    }
}

// inlet.c:706
fun inlet_adjustQualOutflows() {
    var j: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var inlet: TInlet? = null
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        if ((Node!![j]!!.inlet == CAPTURE)) {
            q = Node!![j]!!.overflow
            if ((q > 0.0)) {
                StepFlowTotals!!.flooding -= q
                if (!(IgnoreQuality != 0)) {
                    p = 0
                    f2@ while ((p < Nobjects!![POLLUT])) {
                        w = (q * Node!![j]!!.newQual!![p])
                        StepQualTotals!![p]!!.flooding -= w
                        p++
                    }
                }
            }
        }
        j++
    }
    if (((!(IgnoreQuality != 0)) && (Nobjects!![POLLUT] > 0))) {
        inlet = FirstInlet
        f3@ while ((inlet != null)) {
            j = Link!![inlet!!.linkIndex]!!.node2
            q = (inlet!!.flowCapture - inlet!!.backflow)
            if (((q > 0.0) && (Node!![j]!!.newLatFlow < 0.0))) {
                p = 0
                f4@ while ((p < Nobjects!![POLLUT])) {
                    w = (q * Node!![j]!!.newQual!![p])
                    StepQualTotals!![p]!!.outflow -= w
                    p++
                }
            }
            inlet = inlet!!.nextInlet
        }
    }
}

// inlet.c:774
fun inlet_writeStatsReport() {
    var j: Int = 0
    var header: Int = 0
    if ((Nobjects!![STREET] == 0)) {
        return
    }
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.xsect!!.type == STREET_XSECT)) {
            if (!(header != 0)) {
                writeStreetStatsHeader()
                header = 1
            }
            writeStreetStats(j)
        }
        j++
    }
    report_writeLine(__L0)
}

// inlet.c:801
fun inlet_capturedFlow(i: Int): Double {
    if ((Link!![i]!!.inlet != null)) {
        return Link!![i]!!.inlet!!.flowCapture
    }
    return 0.0
}

// inlet.c:814
fun readGrateInletParams(i: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var grateType: Int = 0
    val width = DBox(0.0)
    val length = DBox(0.0)
    val areaRatio = DBox(0.0)
    val vSplash = DBox(0.0)
    if ((ntoks < 5)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if (((!(getDouble(tok!![2], length) != 0)) || (length.v <= 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![2])
    }
    if (((!(getDouble(tok!![3], width) != 0)) || (width.v <= 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![3])
    }
    grateType = findmatch(tok!![4], GrateTypeWords)
    if ((grateType < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![4])
    }
    if ((grateType == GENERIC)) {
        if ((ntoks < 6)) {
            return error_setInpError(ERR_ITEMS, __L0)
        }
        if ((((!(getDouble(tok!![5], areaRatio) != 0)) || (areaRatio.v <= 0.0)) || (areaRatio.v > 1.0))) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
        if ((ntoks > 6)) {
            if (((!(getDouble(tok!![6], vSplash) != 0)) || (vSplash.v < 0.0))) {
                return error_setInpError(ERR_NUMBER, tok!![6])
            }
        }
    }
    InletDesigns!![i]!!.grateInlet!!.length = (length.v / UCF(LENGTH))
    InletDesigns!![i]!!.grateInlet!!.width = (width.v / UCF(LENGTH))
    InletDesigns!![i]!!.grateInlet!!.type = grateType
    InletDesigns!![i]!!.grateInlet!!.fracOpenArea = areaRatio.v
    InletDesigns!![i]!!.grateInlet!!.splashVeloc = (vSplash.v / UCF(LENGTH))
    if (((InletDesigns!![i]!!.type == GRATE_INLET) && (InletDesigns!![i]!!.curbInlet!!.length > 0.0))) {
        InletDesigns!![i]!!.type = COMBO_INLET
    }
    return 0
}

// inlet.c:868
fun readCurbInletParams(i: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var throatAngle: Int = 0
    val height = DBox(0.0)
    val length = DBox(0.0)
    if ((ntoks < 4)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if (((!(getDouble(tok!![2], length) != 0)) || (length.v <= 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![2])
    }
    if (((!(getDouble(tok!![3], height) != 0)) || (height.v <= 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![3])
    }
    throatAngle = VERTICAL_THROAT
    if (((InletDesigns!![i]!!.type == CURB_INLET) && (ntoks > 4))) {
        throatAngle = findmatch(tok!![4], ThroatAngleWords)
        if ((throatAngle < 0)) {
            return error_setInpError(ERR_KEYWORD, tok!![4])
        }
    }
    InletDesigns!![i]!!.curbInlet!!.length = (length.v / UCF(LENGTH))
    InletDesigns!![i]!!.curbInlet!!.height = (height.v / UCF(LENGTH))
    InletDesigns!![i]!!.curbInlet!!.throatAngle = throatAngle
    if (((InletDesigns!![i]!!.type == CURB_INLET) && (InletDesigns!![i]!!.grateInlet!!.length > 0.0))) {
        InletDesigns!![i]!!.type = COMBO_INLET
    }
    return 0
}

// inlet.c:911
fun readSlottedInletParams(i: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    val width = DBox(0.0)
    val length = DBox(0.0)
    if ((ntoks < 4)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if (((!(getDouble(tok!![2], length) != 0)) || (length.v <= 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![2])
    }
    if (((!(getDouble(tok!![3], width) != 0)) || (width.v <= 0.0))) {
        return error_setInpError(ERR_NUMBER, tok!![3])
    }
    InletDesigns!![i]!!.slottedInlet!!.length = (length.v / UCF(LENGTH))
    InletDesigns!![i]!!.slottedInlet!!.width = (width.v / UCF(LENGTH))
    return 0
}

// inlet.c:939
fun readCustomInletParams(i: Int, tok: Array<CPtr?>?, ntoks: Int): Int {
    var c: Int = 0
    if ((ntoks < 3)) {
        return error_setInpError(ERR_ITEMS, __L0)
    } else {
        c = project_findObject(CURVE, tok!![2])
        if ((c < 0)) {
            return error_setInpError(ERR_NAME, tok!![2])
        }
    }
    InletDesigns!![i]!!.customCurve = c
    return 0
}

// inlet.c:962
fun initInletStats(inlet: TInlet?) {
    if ((inlet != null)) {
        inlet!!.flowCapture = 0.0
        inlet!!.backflow = 0.0
        inlet!!.stats!!.flowPeriods = 0
        inlet!!.stats!!.capturePeriods = 0
        inlet!!.stats!!.backflowPeriods = 0
        inlet!!.stats!!.peakFlow = 0.0
        inlet!!.stats!!.peakFlowCapture = 0.0
        inlet!!.stats!!.avgFlowCapture = 0.0
        inlet!!.stats!!.bypassFreq = 0.0
    }
}

// inlet.c:985
fun updateInletStats(inlet: TInlet?, q: Double) {
    var qCapture: Double = inlet!!.flowCapture
    var qBackflow: Double = inlet!!.backflow
    var qNet: Double = (qCapture - qBackflow)
    var qBypass: Double = (q - qNet)
    var fCapture: Double = 0.0
    if (((q < 0.001) && (qBackflow <= 0.0))) {
        return
    }
    inlet!!.stats!!.flowPeriods++
    if ((qNet > 0.0)) {
        inlet!!.stats!!.capturePeriods++
        fCapture = (qNet / q)
        fCapture = ((if ((((fCapture) <= (1.0)))) (fCapture) else (1.0)))
        inlet!!.stats!!.avgFlowCapture += fCapture
        if ((qBypass > 0.001)) {
            inlet!!.stats!!.bypassFreq++
        }
    } else {
        inlet!!.stats!!.backflowPeriods++
    }
    if ((q > inlet!!.stats!!.peakFlow)) {
        inlet!!.stats!!.peakFlow = q
        inlet!!.stats!!.peakFlowCapture = (fCapture * 100.0)
    }
}

// inlet.c:1026
fun writeStreetStatsHeader() {
    report_writeLine(__L0)
    report_writeLine(__L188)
    report_writeLine(__L189)
    report_writeLine(__L188)
    report_writeLine(__L0)
    R.fprintf(Frpt!!.file, __L190)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L191, FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits])
    } else {
        R.fprintf(Frpt!!.file, __L192, FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits])
    }
    R.fprintf(Frpt!!.file, __L193)
}

// inlet.c:1055
fun writeStreetStats(link: Int) {
    var k: Int = 0
    var t: Int = 0
    var placement: Int = 0
    var maxSpread: Double = 0.0
    var maxDepth: Double = 0.0
    var maxFlow: Double = 0.0
    var fp: Double = 0.0
    var cp: Double = 0.0
    var afc: Double = 0.0
    var bpf: Double = 0.0
    var inlet: TInlet? = null
    k = Link!![link]!!.subIndex
    t = Link!![link]!!.xsect!!.transect
    inlet = Link!![link]!!.inlet
    maxFlow = LinkStats!![link]!!.maxFlow
    maxDepth = LinkStats!![link]!!.maxDepth
    maxSpread = (xsect_getWofY(Link!![link]!!.xsect, maxDepth) / (Street!![t]!!.sides).toDouble())
    maxSpread = ((if ((((maxSpread) <= (Street!![t]!!.width)))) (maxSpread) else (Street!![t]!!.width)))
    R.fprintf(Frpt!!.file, __L194, Link!![link]!!.ID)
    R.fprintf(Frpt!!.file, __L195, (maxFlow * UCF(FLOW)))
    R.fprintf(Frpt!!.file, __L195, (maxSpread * UCF(LENGTH)))
    R.fprintf(Frpt!!.file, __L195, (maxDepth * UCF(LENGTH)))
    if ((inlet != null)) {
        R.fprintf(Frpt!!.file, __L196, InletDesigns!![inlet!!.designIndex]!!.ID)
        placement = getInletPlacement(inlet, Link!![inlet!!.linkIndex]!!.node2)
        if ((placement == ON_GRADE)) {
            R.fprintf(Frpt!!.file, __L197)
        } else {
            R.fprintf(Frpt!!.file, __L198)
        }
        R.fprintf(Frpt!!.file, __L199, inlet!!.numInlets)
        fp = ((inlet!!.stats!!.flowPeriods).toDouble() / 100.0)
        if ((fp > 0.0)) {
            cp = ((inlet!!.stats!!.capturePeriods).toDouble() / 100.0)
            R.fprintf(Frpt!!.file, __L200, inlet!!.stats!!.peakFlowCapture)
            if ((cp > 0.0)) {
                afc = (inlet!!.stats!!.avgFlowCapture / cp)
                bpf = (inlet!!.stats!!.bypassFreq / cp)
            }
            R.fprintf(Frpt!!.file, __L200, afc)
            R.fprintf(Frpt!!.file, __L200, bpf)
            R.fprintf(Frpt!!.file, __L200, ((inlet!!.stats!!.backflowPeriods).toDouble() / fp))
            R.fprintf(Frpt!!.file, __L200, ((((((maxFlow / (Street!![t]!!.sides).toDouble())) * UCF(FLOW)) * 0.01) * inlet!!.stats!!.peakFlowCapture) / (inlet!!.numInlets).toDouble()))
            R.fprintf(Frpt!!.file, __L200, (((maxFlow * UCF(FLOW)) * 0.01) * ((100.0 - inlet!!.stats!!.peakFlowCapture))))
        }
    }
}

// inlet.c:1129
fun getInletPlacement(inlet: TInlet?, j: Int): Int {
    if ((inlet!!.placement == AUTOMATIC)) {
        if ((Node!![j]!!.degree > 0)) {
            return ON_GRADE
        } else {
            return ON_SAG
        }
    } else {
        return inlet!!.placement
    }
    throw IllegalStateException("getInletPlacement: no return")
}

// inlet.c:1147
fun getConduitGeometry(inlet: TInlet?) {
    var linkIndex: Int = inlet!!.linkIndex
    var t: Int = 0
    var k: Int = Link!![linkIndex]!!.subIndex
    SL = Conduit!![k]!!.slope
    Beta = Conduit!![k]!!.beta
    xsect = Link!![linkIndex]!!.xsect
    if ((xsect!!.type == STREET_XSECT)) {
        t = xsect!!.transect
        Sx = Street!![t]!!.slope
        a = Street!![t]!!.gutterDepression
        W = Street!![t]!!.gutterWidth
        n = Street!![t]!!.roughness
        Nsides = Street!![t]!!.sides
        Tcrown = Street!![t]!!.width
        Qfactor = inlet!!.flowFactor
        if (((inlet != null) && ((inlet!!.localDepress * inlet!!.localWidth) > 0.0))) {
            a += inlet!!.localDepress
            W = inlet!!.localWidth
        }
        if (((W * a) > 0.0)) {
            Sw = (Sx + (a / W))
        } else {
            Sw = Sx
        }
    } else {
        a = 0.0
        W = 0.0
        n = Conduit!![k]!!.roughness
        Nsides = 1
        Sx = 0.01
        Sw = Sx
    }
}

// inlet.c:1200
fun getFlowSpread(Q: Double): Double {
    var iter: Int = 0
    var f: Double = 0.0
    var f1: Double = 0.0
    var Sr: Double = 0.0
    var Ts1: Double = 0.0
    var Ts2: Double = 0.0
    var Tw: Double = 0.0
    var Qs: Double = 0.0
    var Eo: Double = 0.0
    f = Qfactor
    if ((a == 0.0)) {
        Ts1 = Glibm.pow((Q / f), 0.375)
    } else {
        f1 = (f * Glibm.pow((((a / W)) / Sx), 1.67))
        Tw = Glibm.pow((Q / f1), 0.375)
        if ((Tw <= W)) {
            Ts1 = Tw
        } else {
            Sr = (((Sx + (a / W))) / Sx)
            iter = 1
            Ts1 = (Glibm.pow((Q / f), 0.375) - W)
            if ((Ts1 <= 0.0)) {
                Ts1 = (Tw - W)
            }
            w1@ while ((iter < 11)) {
                Eo = getEo(Sr, Ts1, W)
                Qs = (((1.0 - Eo)) * Q)
                Ts2 = Glibm.pow((Qs / f), 0.375)
                if ((abs((Ts2 - Ts1)) < 0.01)) {
                    break@w1
                }
                Ts1 = Ts2
                iter++
            }
            Ts1 = (Ts2 + W)
        }
    }
    return ((if ((((Ts1) <= (Tcrown)))) (Ts1) else (Tcrown)))
}

// inlet.c:1248
fun getEo(Sr: Double, Ts: Double, w: Double): Double {
    var x: Double = 0.0
    x = (Sr / ((Ts / w)))
    x = (Glibm.pow(((1.0 + x)), 2.67) - 1.0)
    x = (1.0 + (Sr / x))
    return (1.0 / x)
}

// inlet.c:1267
fun getOnGradeCapturedFlow(inlet: TInlet?, q: Double, d: Double): Double {
    var i: Int = 0
    var linkIndex: Int = 0
    var qApproach: Double = 0.0
    var qc: Double = 0.0
    var qCaptured: Double = 0.0
    var qBypassed: Double = 0.0
    var qMax: Double = 0.0
    if ((inlet!!.numInlets == 0)) {
        return 0.0
    }
    linkIndex = inlet!!.linkIndex
    qApproach = q
    if ((qApproach < 0.001)) {
        return 0.0
    }
    getConduitGeometry(inlet)
    qApproach /= ((Nsides).toDouble())
    qBypassed = qApproach
    qCaptured = 0.0
    qMax = 10000000000.0
    if ((inlet!!.flowLimit > 0.0)) {
        qMax = inlet!!.flowLimit
    }
    i = 1
    f1@ while ((i <= inlet!!.numInlets)) {
        qc = (getOnGradeInletCapture(inlet!!.designIndex, qBypassed, d) * inlet!!.clogFactor)
        qc = ((if ((((qc) <= (qMax)))) (qc) else (qMax)))
        qc = ((if ((((qc) <= (qBypassed)))) (qc) else (qBypassed)))
        qCaptured += qc
        qBypassed -= qc
        if ((qBypassed < 0.001)) {
            break@f1
        }
        i++
    }
    return ((qCaptured * ((Nsides).toDouble()))).also { __v -> qCaptured = __v }
}

// inlet.c:1325
fun getOnGradeInletCapture(i: Int, Q: Double, d: Double): Double {
    var Q1: Double = Q
    var Qc: Double = 0.0
    var Lsweep: Double = 0.0
    var Lcurb: Double = 0.0
    var Lgrate: Double = 0.0
    if ((InletDesigns!![i]!!.type == DROP_CURB_INLET)) {
        Qc = getOnSagInletCapture(i, d)
        return ((if ((((Qc) <= (Q)))) (Qc) else (Q)))
    }
    if ((InletDesigns!![i]!!.type == DROP_GRATE_INLET)) {
        Qc = getGrateInletCapture(i, Q)
        return ((if ((((Qc) <= (Q)))) (Qc) else (Q)))
    }
    T = getFlowSpread(Q)
    if ((InletDesigns!![i]!!.type == SLOTTED_INLET)) {
        Qc = getCurbInletCapture(Q, InletDesigns!![i]!!.slottedInlet!!.length)
        return ((if ((((Qc) <= (Q)))) (Qc) else (Q)))
    }
    Lcurb = InletDesigns!![i]!!.curbInlet!!.length
    Lgrate = InletDesigns!![i]!!.grateInlet!!.length
    if ((Lcurb > 0.0)) {
        Lsweep = (Lcurb - Lgrate)
        if ((Lsweep > 0.0)) {
            Qc = getCurbInletCapture(Q1, Lsweep)
            Q1 -= Qc
        }
    }
    if (((Lgrate > 0.0) && (Q1 > 0.0))) {
        if ((Q1 != Q)) {
            T = getFlowSpread(Q1)
        }
        Qc += (getGrateInletCapture(i, Q1))
    }
    return Qc
}

// inlet.c:1387
fun getGrateInletCapture(i: Int, Q: Double): Double {
    var grateType: Int = 0
    var Lg: Double = 0.0
    var Wg: Double = 0.0
    var A: Double = 0.0
    var Y: Double = 0.0
    var Eo: Double = 0.0
    var V: Double = 0.0
    var Vo: Double = 0.0
    var Qo: Double = Q
    var Rf: Double = 1.0
    var Rs: Double = 0.0
    Lg = InletDesigns!![i]!!.grateInlet!!.length
    Wg = InletDesigns!![i]!!.grateInlet!!.width
    if (((xsect!!.type == TRAPEZOIDAL) || (xsect!!.type == RECT_OPEN))) {
        A = xsect_getAofS(xsect, (Q / Beta))
        Y = xsect_getYofA(xsect, A)
        T = xsect_getWofY(xsect, Y)
        Eo = (((Beta * Glibm.pow((Y * Wg), 1.67)) / Glibm.pow((Wg + (2.0 * Y)), 0.67)) / Q)
        if ((((Wg > (0.99 * xsect!!.yBot)) && (xsect!!.type == TRAPEZOIDAL)) && (xsect!!.sBot > 0.0))) {
            Wg = xsect!!.yBot
            Sx = (1.0 / xsect!!.sBot)
        }
    } else if ((a == 0.0)) {
        A = (((T * T) * Sx) / 2.0)
        Eo = getGutterFlowRatio(Wg)
        if ((T >= Tcrown)) {
            Qo = (Qfactor * Glibm.pow(Tcrown, 2.67))
        }
    } else {
        if ((T <= W)) {
            A = (((T * T) * Sw) / 2.0)
        } else {
            A = (((((T * T) * Sx) + (a * W))) / 2.0)
        }
        Eo = getGutterFlowRatio(W)
        if ((Eo < 1.0)) {
            if ((T >= Tcrown)) {
                Qo = ((Qfactor * Glibm.pow(Tcrown, 2.67)) / ((1.0 - Eo)))
            }
            Eo = (Eo * getGutterAreaRatio(Wg, A))
        }
    }
    V = (Qo / A)
    grateType = InletDesigns!![i]!!.grateInlet!!.type
    if (((grateType < 0) || (grateType == GENERIC))) {
        Vo = InletDesigns!![i]!!.grateInlet!!.splashVeloc
    } else {
        Vo = getSplashOverVelocity(grateType, Lg)
    }
    if ((V > Vo)) {
        Rf = (1.0 - (0.09 * ((V - Vo))))
    }
    if ((Eo < 1.0)) {
        Rs = (1.0 / ((1.0 + ((((0.15 * Glibm.pow(V, 1.8)) / Sx) / Glibm.pow(Lg, 2.3))))))
    }
    return (Q * (((Rf * Eo) + (Rs * ((1.0 - Eo))))))
}

// inlet.c:1477
fun getCurbInletCapture(Q: Double, L: Double): Double {
    var Se: Double = Sx
    var Lt: Double = 0.0
    var Sr: Double = 0.0
    var Eo: Double = 0.0
    var E: Double = 1.0
    if ((a > 0.0)) {
        Sr = (Sw / Sx)
        Eo = getEo(Sr, (T - W), W)
        Se = (Sx + (((a / W)) * Eo))
    }
    Lt = (((0.6 * Glibm.pow(Q, 0.42)) * Glibm.pow(SL, 0.3)) * Glibm.pow((1.0 / ((n * Se))), 0.6))
    if ((L < Lt)) {
        E = (1.0 - ((L / Lt)))
        E = (1.0 - Glibm.pow(E, 1.8))
    }
    E = ((if ((((E) <= (1.0)))) (E) else (1.0)))
    E = ((if ((((E) >= (0.0)))) (E) else (0.0)))
    return (E * Q)
}

// inlet.c:1518
fun getGutterFlowRatio(w: Double): Double {
    if ((T <= w)) {
        return 1.0
    } else if ((a > 0.0)) {
        return getEo((Sw / Sx), (T - w), w)
    } else {
        return (1.0 - Glibm.pow(((1.0 - (w / T))), 2.67))
    }
    throw IllegalStateException("getGutterFlowRatio: no return")
}

// inlet.c:1535
fun getGutterAreaRatio(Wg: Double, A: Double): Double {
    var As: Double = 0.0
    var Ag: Double = 0.0
    if ((Wg >= W)) {
        return 1.0
    }
    if ((T <= Wg)) {
        return 1.0
    }
    if ((T <= W)) {
        return (Wg / T)
    }
    As = ((0.5 * (((((T - W))) * (((T - W)))))) * Sx)
    Ag = (Wg * (((((T * Sx)) + a) - (((Wg * Sw) / 2.0)))))
    return (Ag / ((A - As)))
}

// inlet.c:1557
fun getSplashOverVelocity(grateType: Int, L: Double): Double {
    return (((SplashCoeffs!![grateType]!![0] + (SplashCoeffs!![grateType]!![1] * L)) - ((SplashCoeffs!![grateType]!![2] * L) * L)) + (((SplashCoeffs!![grateType]!![3] * L) * L) * L))
}

// inlet.c:1574
fun getOnSagCapturedFlow(inlet: TInlet?, q: Double, d: Double): Double {
    var linkIndex: Int = 0
    var designIndex: Int = 0
    var totalInlets: Int = 0
    var qCaptured: Double = 0.0
    var qMax: Double = 10000000000.0
    if ((inlet!!.numInlets == 0)) {
        return 0.0
    }
    totalInlets = (Nsides * inlet!!.numInlets)
    linkIndex = inlet!!.linkIndex
    designIndex = inlet!!.designIndex
    getConduitGeometry(inlet)
    if ((inlet!!.flowLimit > 0.0)) {
        qMax = inlet!!.flowLimit
    }
    qCaptured = getOnSagInletCapture(designIndex, abs(d))
    qCaptured *= inlet!!.clogFactor
    qCaptured = ((if ((((qCaptured) <= (qMax)))) (qCaptured) else (qMax)))
    qCaptured *= ((totalInlets).toDouble())
    return qCaptured
}

// inlet.c:1610
fun getOnSagInletCapture(i: Int, d: Double): Double {
    var Lsweep: Double = 0.0
    var Lcurb: Double = 0.0
    var Lgrate: Double = 0.0
    val Qsw = DBox(0.0)
    val Qso = DBox(0.0)
    val Qgw = DBox(0.0)
    val Qgo = DBox(0.0)
    val Qcw = DBox(0.0)
    val Qco = DBox(0.0)
    if ((InletDesigns!![i]!!.slottedInlet!!.length > 0.0)) {
        return getOnSagSlottedFlow(i, d)
    }
    Lgrate = InletDesigns!![i]!!.grateInlet!!.length
    if ((Lgrate > 0.0)) {
        findOnSagGrateFlows(i, d, Qgw, Qgo)
    }
    Lcurb = InletDesigns!![i]!!.curbInlet!!.length
    if ((Lcurb > 0.0)) {
        Lsweep = (Lcurb - Lgrate)
        if ((Lsweep > 0.0)) {
            findOnSagCurbFlows(i, d, Lsweep, Qsw, Qso)
        }
        if ((Qgo.v > 0.0)) {
            findOnSagCurbFlows(i, d, Lgrate, Qcw, Qco)
        }
    }
    return ((((Qgw.v + Qgo.v) + Qsw.v) + Qso.v) + Qco.v)
}

// inlet.c:1644
fun findOnSagGrateFlows(i: Int, d: Double, Qw: DRef?, Qo: DRef?) {
    var grateType: Int = InletDesigns!![i]!!.grateInlet!!.type
    var Lg: Double = InletDesigns!![i]!!.grateInlet!!.length
    var Wg: Double = InletDesigns!![i]!!.grateInlet!!.width
    var P: Double = 0.0
    var Ao: Double = 0.0
    var di: Double = 0.0
    if ((InletDesigns!![i]!!.type == DROP_GRATE_INLET)) {
        di = d
        P = (2.0 * ((Lg + Wg)))
    } else {
        if ((d <= (Wg * Sw))) {
            Wg = (d / Sw)
        }
        di = (d - (((Wg / 2.0)) * Sw))
        P = (Lg + (2.0 * Wg))
    }
    if ((grateType == GENERIC)) {
        Ao = ((Lg * Wg) * InletDesigns!![i]!!.grateInlet!!.fracOpenArea)
    } else {
        Ao = ((Lg * Wg) * GrateOpeningRatios!![grateType])
    }
    if ((d <= ((1.79 * Ao) / P))) {
        Qw!!.set(((3.0 * P) * Glibm.pow(di, 1.5)))
    } else {
        Qo!!.set(((0.67 * Ao) * sqrt(((2.0 * 32.16) * di))))
    }
}

// inlet.c:1703
fun findOnSagCurbFlows(i: Int, d: Double, __L: Double, Qw: DRef?, Qo: DRef?) {
    var L: Double = __L
    var throatAngle: Int = InletDesigns!![i]!!.curbInlet!!.throatAngle
    var h: Double = InletDesigns!![i]!!.curbInlet!!.height
    var Qweir: Double = 0.0
    var Qorif: Double = 0.0
    var P: Double = 0.0
    var dweir: Double = 0.0
    var dorif: Double = 0.0
    var r: Double = 0.0
    if ((L <= 0.0)) {
        return
    }
    if ((InletDesigns!![i]!!.type == DROP_CURB_INLET)) {
        L = (L * 4.0)
    }
    dorif = (1.4 * h)
    if ((d > dorif)) {
        Qo!!.set(getCurbOrificeFlow(d, h, L, throatAngle))
        return
    }
    if (((a == 0.0) || (L > 12.0))) {
        dweir = h
        if ((d < dweir)) {
            Qw!!.set(((3.0 * L) * Glibm.pow(d, 1.5)))
            return
        } else {
            Qweir = ((3.0 * L) * Glibm.pow(dweir, 1.5))
        }
    } else {
        P = (L + (1.8 * W))
        dweir = (h + a)
        if ((d < dweir)) {
            Qw!!.set(((2.3 * P) * Glibm.pow(d, 1.5)))
            return
        } else {
            Qweir = ((2.3 * P) * Glibm.pow(dweir, 1.5))
        }
    }
    Qorif = getCurbOrificeFlow(dorif, h, L, throatAngle)
    r = (((d - dweir)) / ((dorif - dweir)))
    Qw!!.set((((1.0 - r)) * Qweir))
    Qo!!.set((r * Qorif))
}

// inlet.c:1764
fun getCurbOrificeFlow(di: Double, h: Double, L: Double, throatAngle: Int): Double {
    var d: Double = di
    if ((throatAngle == HORIZONTAL_THROAT)) {
        d = (di - (h / 2.0))
    } else if ((throatAngle == INCLINED_THROAT)) {
        d = (di - (((h / 2.0)) * 0.7071))
    }
    return (((0.67 * h) * L) * sqrt(((2.0 * 32.16) * d)))
}

// inlet.c:1785
fun getOnSagSlottedFlow(i: Int, d: Double): Double {
    var L: Double = InletDesigns!![i]!!.slottedInlet!!.length
    var w: Double = InletDesigns!![i]!!.slottedInlet!!.width
    if ((d <= (2.587 * w))) {
        return ((2.48 * L) * Glibm.pow(d, 1.5))
    } else {
        return (((0.8 * L) * w) * sqrt((64.32 * d)))
    }
    throw IllegalStateException("getOnSagSlottedFlow: no return")
}

// inlet.c:1805
fun getBackflowRatios() {
    var inlet: TInlet? = null
    var area: Double = 0.0
    var f: Double = 0.0
    var n: Int = 0
    var inletNodes: Array<TInletNode?>? = Array<TInletNode?>(((Nobjects!![NODE]).toLong()).toInt()) { TInletNode() }
    if ((inletNodes == null)) {
        return
    }
    inlet = FirstInlet
    f1@ while ((inlet != null)) {
        n = inlet!!.nodeIndex
        inletNodes!![n]!!.numInletLinks++
        area = getInletArea(inlet)
        if ((area > 0.0)) {
            inletNodes!![n]!!.numStdInletLinks++
            inletNodes!![n]!!.totalInletArea += area
        } else {
            inletNodes!![n]!!.numCustomInlets += inlet!!.numInlets
        }
        inlet = inlet!!.nextInlet
    }
    inlet = FirstInlet
    f2@ while ((inlet != null)) {
        n = inlet!!.nodeIndex
        f = ((inletNodes!![n]!!.numStdInletLinks).toDouble() / (inletNodes!![n]!!.numInletLinks).toDouble())
        area = getInletArea(inlet)
        if ((area == 0.0)) {
            inlet!!.backflowRatio = (((inlet!!.numInlets).toDouble() / (inletNodes!![n]!!.numCustomInlets).toDouble()) * ((1.0 - f)))
        } else {
            inlet!!.backflowRatio = ((area / inletNodes!![n]!!.totalInletArea) * f)
        }
        inlet = inlet!!.nextInlet
    }
    R.free(inletNodes)
}

// inlet.c:1870
fun getInletArea(inlet: TInlet?): Double {
    var area: Double = 0.0
    var curbLength: Double = 0.0
    var i: Int = inlet!!.designIndex
    var grateType: Int = InletDesigns!![i]!!.grateInlet!!.type
    if ((InletDesigns!![i]!!.grateInlet!!.length > 0.0)) {
        area = (InletDesigns!![i]!!.grateInlet!!.length * InletDesigns!![i]!!.grateInlet!!.width)
        if ((grateType == GENERIC)) {
            area *= InletDesigns!![i]!!.grateInlet!!.fracOpenArea
        } else {
            area *= GrateOpeningRatios!![grateType]
        }
    }
    curbLength = (InletDesigns!![i]!!.curbInlet!!.length - InletDesigns!![i]!!.grateInlet!!.length)
    if ((curbLength > 0.0)) {
        area += (curbLength * InletDesigns!![i]!!.curbInlet!!.height)
    }
    if ((InletDesigns!![i]!!.slottedInlet!!.length > 0.0)) {
        area = (InletDesigns!![i]!!.slottedInlet!!.length * InletDesigns!![i]!!.slottedInlet!!.width)
    }
    return ((area * (inlet!!.numInlets).toDouble()) * inlet!!.clogFactor)
}

// inlet.c:1902
fun getCustomCapturedFlow(inlet: TInlet?, q: Double, d: Double): Double {
    var i: Int = inlet!!.designIndex
    var j: Int = 0
    var sides: Int = 1
    var c: Int = 0
    var qApproach: Double = 0.0
    var qBypassed: Double = 0.0
    var qCaptured: Double = 0.0
    var qIncrement: Double = 0.0
    var qMax: Double = 10000000000.0
    if ((inlet!!.numInlets == 0)) {
        return 0.0
    }
    qMax = 10000000000.0
    if ((inlet!!.flowLimit > 0.0)) {
        qMax = inlet!!.flowLimit
    }
    xsect = Link!![inlet!!.linkIndex]!!.xsect
    if ((xsect!!.type == STREET_XSECT)) {
        sides = Street!![xsect!!.transect]!!.sides
    }
    qApproach = (q / (sides).toDouble())
    qBypassed = qApproach
    qCaptured = 0.0
    c = InletDesigns!![i]!!.customCurve
    if ((c >= 0)) {
        if ((Curve!![c]!!.curveType == DIVERSION_CURVE)) {
            j = 1
            f1@ while ((j <= inlet!!.numInlets)) {
                qIncrement = ((inlet!!.clogFactor * table_lookupEx(Curve!![c], (qBypassed * UCF(FLOW)))) / UCF(FLOW))
                qIncrement = ((if ((((qIncrement) <= (qMax)))) (qIncrement) else (qMax)))
                qIncrement = ((if ((((qIncrement) <= (qBypassed)))) (qIncrement) else (qBypassed)))
                qCaptured += qIncrement
                qBypassed -= qIncrement
                if ((qBypassed < 0.001)) {
                    break@f1
                }
                j++
            }
        } else if ((Curve!![c]!!.curveType == RATING_CURVE)) {
            qCaptured = ((((inlet!!.numInlets).toDouble() * inlet!!.clogFactor) * table_lookupEx(Curve!![c], (d * UCF(LENGTH)))) / UCF(FLOW))
        }
        qCaptured *= ((sides).toDouble())
    }
    return qCaptured
}

