// Generated from EPA SWMM 5.2.4 (routing.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_routing")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var SortedLinks: IntArray? = null
@JvmField var NextEvent: Int = 0
@JvmField var BetweenEvents: Int = 0
@JvmField var NewRuleTime: Double = 0.0

// routing.c:93
fun routing_open(): Int {
    if (!(treatmnt_open() != 0)) {
        return ErrorCode
    }
    SortedLinks = (null)
    if ((Nobjects!![LINK] > 0)) {
        SortedLinks = IntArray(((Nobjects!![LINK]).toLong()).toInt())
        if (!(SortedLinks != null)) {
            report_writeErrorMsg(ERR_MEMORY, __L0)
            return ErrorCode
        }
        toposort_sortLinks(SortedLinks)
        if ((ErrorCode != 0)) {
            return ErrorCode
        }
    }
    iface_openRoutingFiles()
    flowrout_init(RouteModel)
    if ((Fhotstart1!!.mode == NO_FILE)) {
        qualrout_init()
    }
    if ((NumEvents > 0)) {
        sortEvents()
    }
    NextEvent = 0
    BetweenEvents = (if (((NumEvents > 0))) 1 else 0)
    NewRuleTime = 0.0
    return ErrorCode
}

// routing.c:134
fun routing_close(routingModel: Int) {
    iface_closeRoutingFiles()
    flowrout_close(routingModel)
    treatmnt_close()
    if ((SortedLinks != null)) {
        R.free(SortedLinks)
        SortedLinks = (null)
    }
}

// routing.c:152
fun routing_getRoutingStep(routingModel: Int, fixedStep: Double): Double {
    var date1: Double = 0.0
    var date2: Double = 0.0
    var nextTime: Double = 0.0
    var routingStep: Double = 0.0
    var nextRuleTime: Double = 0.0
    var nextRoutingTime: Double = 0.0
    if ((Nobjects!![LINK] == 0)) {
        return fixedStep
    }
    if (((NumEvents > 0) && (BetweenEvents != 0))) {
        nextTime = ((if ((((NewRunoffTime) <= (ReportTime)))) (NewRunoffTime) else (ReportTime)))
        date1 = getDateTime(NewRoutingTime)
        date2 = getDateTime(nextTime)
        if (((date2 > date1) && (date2 < Event!![NextEvent]!!.start))) {
            routingStep = (((nextTime - NewRoutingTime)) / 1000.0)
        } else {
            date1 = getDateTime((NewRoutingTime + (1000.0 * fixedStep)))
            if ((date1 < Event!![NextEvent]!!.start)) {
                return fixedStep
            }
        }
    }
    if ((routingStep == 0.0)) {
        routingStep = flowrout_getRoutingStep(routingModel, fixedStep)
    }
    if ((RuleStep > 0)) {
        nextRuleTime = (NewRuleTime + (1000.0 * (RuleStep).toDouble()))
        nextRoutingTime = (NewRoutingTime + (1000.0 * routingStep))
        if ((nextRoutingTime >= nextRuleTime)) {
            routingStep = (((nextRuleTime - NewRoutingTime)) / 1000.0)
        }
    }
    return routingStep
}

// routing.c:203
fun routing_execute(routingModel: Int, routingStep: Double) {
    var trialsCount: Int = 1
    var actionCount: Int = 0
    var inSteadyState: Int = 1
    var currentDate: Double = 0.0
    var stepFlowError: Double = 0.0
    if ((ErrorCode != 0)) {
        return
    }
    massbal_updateRoutingTotals((routingStep / 2.0))
    currentDate = getDateTime(NewRoutingTime)
    actionCount = evaluateControlRules(currentDate, routingStep)
    stepFlowError = massbal_getStepFlowError()
    massbal_initTimeStepTotals()
    initSystemInflows()
    BetweenEvents = isBetweenEvents(currentDate)
    if ((BetweenEvents == 0)) {
        addSystemInflows(currentDate, routingStep)
        inlet_findCapturedFlows(routingStep)
        inSteadyState = isInSteadyState(actionCount, stepFlowError)
        if ((inSteadyState == 0)) {
            trialsCount = routeFlow(routingModel, routingStep)
        }
        if (((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0)))) {
            inlet_adjustQualInflows()
            qualrout_execute(routingStep)
        }
        removeSystemOutflows(routingStep)
        inlet_adjustQualOutflows()
        if ((Nobjects!![LINK] > 0)) {
            stats_updateFlowStats(routingStep, getDateTime(NewRoutingTime))
            stats_updateTimeStepStats(routingStep, trialsCount, inSteadyState)
        }
    }
    massbal_updateRoutingTotals((routingStep / 2.0))
}

// routing.c:269
fun evaluateControlRules(currentDate: Double, routingStep: Double): Int {
    var j: Int = 0
    var actionCount: Int = 0
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        link_setTargetSetting(j)
        j++
    }
    if (((RuleStep == 0) || (abs((NewRoutingTime - NewRuleTime)) < 1.0))) {
        controls_evaluate(currentDate, (currentDate - StartDateTime), (routingStep / 86400.0))
    }
    j = 0
    f2@ while ((j < Nobjects!![LINK])) {
        if ((Link!![j]!!.targetSetting != Link!![j]!!.setting)) {
            if (((Link!![j]!!.targetSetting * Link!![j]!!.setting) == 0.0)) {
                Link!![j]!!.timeLastSet = currentDate
            }
            link_setSetting(j, routingStep)
            actionCount++
        }
        j++
    }
    OldRoutingTime = NewRoutingTime
    NewRoutingTime = (NewRoutingTime + (1000.0 * routingStep))
    if ((abs((NewRoutingTime - ((NewRuleTime + (1000.0 * (RuleStep).toDouble()))))) < 1.0)) {
        NewRuleTime += (1000.0 * (RuleStep).toDouble())
    }
    return actionCount
}

// routing.c:312
fun initSystemInflows() {
    var j: Int = 0
    if ((Nobjects!![POLLUT] > 0)) {
        j = 0
        f1@ while ((j < Nobjects!![NODE])) {
            node_setOldQualState(j)
            j++
        }
        j = 0
        f2@ while ((j < Nobjects!![LINK])) {
            link_setOldQualState(j)
            j++
        }
    }
    infil_setInfilFactor((-1))
    j = 0
    f3@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.oldLatFlow = Node!![j]!!.newLatFlow
        Node!![j]!!.newLatFlow = 0.0
        j++
    }
}

// routing.c:337
fun isBetweenEvents(currentDate: Double): Int {
    if ((NumEvents == 0)) {
        return 0
    }
    if ((currentDate > Event!![NextEvent]!!.end)) {
        NextEvent++
        return 1
    } else if ((currentDate >= Event!![NextEvent]!!.start)) {
        return 0
    }
    return 1
}

// routing.c:359
fun addSystemInflows(currentDate: Double, routingStep: Double) {
    var j: Int = 0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.losses = node_getLosses(j, routingStep)
        j++
    }
    addExternalInflows(currentDate)
    addDryWeatherInflows(currentDate)
    addWetWeatherInflows(OldRoutingTime)
    addGroundwaterInflows(OldRoutingTime)
    addLidDrainInflows(OldRoutingTime)
    addRdiiInflows(currentDate)
    addIfaceInflows(currentDate)
    j = 0
    f2@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.qualInflow = ((if ((((0.0) >= (Node!![j]!!.newLatFlow)))) (0.0) else (Node!![j]!!.newLatFlow)))
        j++
    }
}

// routing.c:383
fun isInSteadyState(actionCount: Int, stepFlowError: Double): Int {
    if ((SkipSteadyState != 0)) {
        if (((((OldRoutingTime == 0.0) || (actionCount > 0)) || (abs(stepFlowError) > SysFlowTol.v)) || (inflowHasChanged() != 0))) {
            return 0
        } else {
            return 1
        }
    }
    return 0
}

// routing.c:399
fun routeFlow(routingModel: Int, routingStep: Double): Int {
    var j: Int = 0
    var stepCount: Int = 1
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        link_setOldHydState(j)
        j++
    }
    j = 0
    f2@ while ((j < Nobjects!![NODE])) {
        node_setOldHydState(j)
        j++
    }
    j = 0
    f3@ while ((j < Nobjects!![NODE])) {
        node_initFlows(j, routingStep)
        j++
    }
    if ((Nobjects!![LINK] > 0)) {
        stepCount = flowrout_execute(SortedLinks, routingModel, routingStep)
    }
    return stepCount
}

// routing.c:425
fun removeSystemOutflows(routingStep: Double) {
    removeStorageLosses(routingStep)
    removeConduitLosses()
    removeOutflows(routingStep)
}

// routing.c:435
fun addExternalInflows(currentDate: Double) {
    var j: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var inflow: TExtInflow? = null
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        q = Node!![j]!!.apiExtInflow
        inflow = Node!![j]!!.extInflow
        w2@ while ((inflow != null)) {
            if ((inflow!!.type == FLOW_INFLOW)) {
                q += (inflow_getExtInflow(inflow, currentDate))
                break@w2
            } else {
                inflow = inflow!!.next
            }
        }
        if ((abs(q) < 1e-05)) {
            q = 0.0
        }
        Node!![j]!!.newLatFlow += q
        if ((q >= 0.0)) {
            massbal_addInflowFlow(EXTERNAL_INFLOW, q)
        } else {
            massbal_addOutflowFlow((-q), 0)
            j++
            continue@f1
        }
        if (((Node!![j]!!.type == OUTFALL) && (Node!![j]!!.oldNetInflow < 0.0))) {
            q = (q - Node!![j]!!.oldNetInflow)
        }
        inflow = Node!![j]!!.extInflow
        w3@ while ((inflow != null)) {
            if ((inflow!!.type != FLOW_INFLOW)) {
                p = inflow!!.param
                w = inflow_getExtInflow(inflow, currentDate)
                if ((inflow!!.type == CONCEN_INFLOW)) {
                    w *= q
                }
                Node!![j]!!.newQual!![p] += w
                massbal_addInflowQual(EXTERNAL_INFLOW, p, w)
            }
            inflow = inflow!!.next
        }
        j++
    }
}

// routing.c:498
fun addDryWeatherInflows(currentDate: Double) {
    var j: Int = 0
    var p: Int = 0
    var month: Int = 0
    var day: Int = 0
    var hour: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var inflow: TDwfInflow? = null
    month = (datetime_monthOfYear(currentDate) - 1)
    day = (datetime_dayOfWeek(currentDate) - 1)
    hour = datetime_hourOfDay(currentDate)
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        inflow = Node!![j]!!.dwfInflow
        if (!(inflow != null)) {
            j++
            continue@f1
        }
        q = 0.0
        w2@ while ((inflow != null)) {
            if ((inflow!!.param < 0)) {
                q = inflow_getDwfInflow(inflow, month, day, hour)
                break@w2
            }
            inflow = inflow!!.next
        }
        if ((abs(q) < 1e-05)) {
            q = 0.0
        }
        Node!![j]!!.newLatFlow += q
        massbal_addInflowFlow(DRY_WEATHER_INFLOW, q)
        if ((q <= 0.0)) {
            j++
            continue@f1
        }
        p = 0
        f3@ while ((p < Nobjects!![POLLUT])) {
            if ((Pollut!![p]!!.dwfConcen > 0.0)) {
                w = (q * Pollut!![p]!!.dwfConcen)
                Node!![j]!!.newQual!![p] += w
                massbal_addInflowQual(DRY_WEATHER_INFLOW, p, w)
            }
            p++
        }
        inflow = Node!![j]!!.dwfInflow
        w4@ while ((inflow != null)) {
            if ((inflow!!.param >= 0)) {
                p = inflow!!.param
                w = (q * inflow_getDwfInflow(inflow, month, day, hour))
                Node!![j]!!.newQual!![p] += w
                massbal_addInflowQual(DRY_WEATHER_INFLOW, p, w)
                if ((Pollut!![p]!!.dwfConcen > 0.0)) {
                    w = (q * Pollut!![p]!!.dwfConcen)
                    Node!![j]!!.newQual!![p] -= w
                    massbal_addInflowQual(DRY_WEATHER_INFLOW, p, (-w))
                }
            }
            inflow = inflow!!.next
        }
        j++
    }
}

// routing.c:579
fun addWetWeatherInflows(routingTime: Double) {
    var i: Int = 0
    var j: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var f: Double = 0.0
    if ((Nobjects!![SUBCATCH] == 0)) {
        return
    }
    f = (((routingTime - OldRunoffTime)) / ((NewRunoffTime - OldRunoffTime)))
    if ((f < 0.0)) {
        f = 0.0
    }
    if ((f > 1.0)) {
        f = 1.0
    }
    i = 0
    f1@ while ((i < Nobjects!![SUBCATCH])) {
        j = Subcatch!![i]!!.outNode
        if ((j >= 0)) {
            q = subcatch_getWtdOutflow(i, f)
            Node!![j]!!.newLatFlow += q
            massbal_addInflowFlow(WET_WEATHER_INFLOW, q)
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                w = surfqual_getWtdWashoff(i, p, f)
                Node!![j]!!.newQual!![p] += w
                massbal_addInflowQual(WET_WEATHER_INFLOW, p, w)
                p++
            }
        }
        i++
    }
}

// routing.c:621
fun addGroundwaterInflows(routingTime: Double) {
    var i: Int = 0
    var j: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var f: Double = 0.0
    var gw: TGroundwater? = null
    if ((Nobjects!![SUBCATCH] == 0)) {
        return
    }
    f = (((routingTime - OldRunoffTime)) / ((NewRunoffTime - OldRunoffTime)))
    if ((f < 0.0)) {
        f = 0.0
    }
    if ((f > 1.0)) {
        f = 1.0
    }
    i = 0
    f1@ while ((i < Nobjects!![SUBCATCH])) {
        gw = Subcatch!![i]!!.groundwater
        if ((gw != null)) {
            j = gw!!.node
            if ((j >= 0)) {
                q = ((((((1.0 - f)) * (gw!!.oldFlow)) + (f * (gw!!.newFlow)))) * Subcatch!![i]!!.area)
                if ((abs(q) < 1e-05)) {
                    i++
                    continue@f1
                }
                Node!![j]!!.newLatFlow += q
                massbal_addInflowFlow(GROUNDWATER_INFLOW, q)
                if ((q > 0.0)) {
                    p = 0
                    f2@ while ((p < Nobjects!![POLLUT])) {
                        w = (q * Pollut!![p]!!.gwConcen)
                        Node!![j]!!.newQual!![p] += w
                        massbal_addInflowQual(GROUNDWATER_INFLOW, p, w)
                        p++
                    }
                }
            }
        }
        i++
    }
}

// routing.c:674
fun addLidDrainInflows(routingTime: Double) {
    var j: Int = 0
    var f: Double = 0.0
    if ((Nobjects!![SUBCATCH] == 0)) {
        return
    }
    f = (((routingTime - OldRunoffTime)) / ((NewRunoffTime - OldRunoffTime)))
    if ((f < 0.0)) {
        f = 0.0
    }
    if ((f > 1.0)) {
        f = 1.0
    }
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        if (((Subcatch!![j]!!.area > 0.0) && (Subcatch!![j]!!.lidArea > 0.0))) {
            lid_addDrainInflow(j, f)
        }
        j++
    }
}

// routing.c:698
fun addRdiiInflows(currentDate: Double) {
    var i: Int = 0
    val j = IBox(0)
    var p: Int = 0
    val q = DBox(0.0)
    var w: Double = 0.0
    var numRdiiNodes: Int = 0
    numRdiiNodes = rdii_getNumRdiiFlows(currentDate)
    i = 0
    f1@ while ((i < numRdiiNodes)) {
        rdii_getRdiiFlow(i, j, q)
        if ((j.v < 0)) {
            i++
            continue@f1
        }
        if ((abs(q.v) < 1e-05)) {
            i++
            continue@f1
        }
        Node!![j.v]!!.newLatFlow += q.v
        massbal_addInflowFlow(RDII_INFLOW, q.v)
        if ((q.v > 0.0)) {
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                w = (q.v * Pollut!![p]!!.rdiiConcen)
                Node!![j.v]!!.newQual!![p] += w
                massbal_addInflowQual(RDII_INFLOW, p, w)
                p++
            }
        }
        i++
    }
}

// routing.c:736
fun addIfaceInflows(currentDate: Double) {
    var i: Int = 0
    var j: Int = 0
    var p: Int = 0
    var q: Double = 0.0
    var w: Double = 0.0
    var numIfaceNodes: Int = 0
    if ((Finflows!!.mode != USE_FILE)) {
        return
    }
    numIfaceNodes = iface_getNumIfaceNodes(currentDate)
    i = 0
    f1@ while ((i < numIfaceNodes)) {
        j = iface_getIfaceNode(i)
        if ((j < 0)) {
            i++
            continue@f1
        }
        q = iface_getIfaceFlow(i)
        if ((abs(q) < 1e-05)) {
            i++
            continue@f1
        }
        Node!![j]!!.newLatFlow += q
        massbal_addInflowFlow(EXTERNAL_INFLOW, q)
        if ((q > 0.0)) {
            p = 0
            f2@ while ((p < Nobjects!![POLLUT])) {
                w = (q * iface_getIfaceQual(i, p))
                Node!![j]!!.newQual!![p] += w
                massbal_addInflowQual(EXTERNAL_INFLOW, p, w)
                p++
            }
        }
        i++
    }
}

// routing.c:776
fun inflowHasChanged(): Int {
    var j: Int = 0
    var diff: Double = 0.0
    var qOld: Double = 0.0
    var qNew: Double = 0.0
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        qOld = Node!![j]!!.oldLatFlow
        qNew = Node!![j]!!.newLatFlow
        if ((abs(qOld) > 1e-06)) {
            diff = (((qNew / qOld)) - 1.0)
        } else if ((abs(qNew) > 1e-06)) {
            diff = 1.0
        } else {
            diff = 0.0
        }
        if ((abs(diff) > LatFlowTol.v)) {
            return 1
        }
        if (((Node!![j]!!.type == OUTFALL) || (Node!![j]!!.degree == 0))) {
            qOld = Node!![j]!!.oldFlowInflow
            qNew = Node!![j]!!.inflow
            if ((abs(qOld) > 1e-06)) {
                diff = (((qNew / qOld)) - 1.0)
            } else if ((abs(qNew) > 1e-06)) {
                diff = 1.0
            } else {
                diff = 0.0
            }
            if ((abs(diff) > LatFlowTol.v)) {
                return 1
            }
        }
        j++
    }
    return 0
}

// routing.c:812
fun removeStorageLosses(tStep: Double) {
    var i: Int = 0
    var evapLoss: Double = 0.0
    var exfilLoss: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type == STORAGE)) {
            evapLoss += Storage!![Node!![i]!!.subIndex]!!.evapLoss
            exfilLoss += Storage!![Node!![i]!!.subIndex]!!.exfilLoss
        }
        i++
    }
    massbal_addNodeLosses((evapLoss / tStep), (exfilLoss / tStep))
}

// routing.c:841
fun removeConduitLosses() {
    var i: Int = 0
    var k: Int = 0
    var barrels: Double = 0.0
    var evapLoss: Double = 0.0
    var seepLoss: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        if ((Link!![i]!!.type == CONDUIT)) {
            k = Link!![i]!!.subIndex
            barrels = (Conduit!![k]!!.barrels).toDouble()
            evapLoss += (Conduit!![k]!!.evapLossRate * barrels)
            seepLoss += (Conduit!![k]!!.seepLossRate * barrels)
        }
        i++
    }
    massbal_addLinkLosses(evapLoss, seepLoss)
}

// routing.c:872
fun removeOutflows(tStep: Double) {
    var i: Int = 0
    var p: Int = 0
    var k: Int = 0
    val isFlooded = IBox(0)
    var q: Double = 0.0
    var w: Double = 0.0
    var v: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if (((Node!![i]!!.type == OUTFALL) && (Node!![i]!!.inflow > 0.0))) {
            k = Node!![i]!!.subIndex
            if ((Outfall!![k]!!.routeTo >= 0)) {
                v = (Node!![i]!!.inflow * tStep)
                Outfall!![k]!!.vRouted += v
                p = 0
                f2@ while ((p < Nobjects!![POLLUT])) {
                    Outfall!![k]!!.wRouted!![p] += (Node!![i]!!.newQual!![p] * v)
                    p++
                }
            }
        }
        q = node_getSystemOutflow(i, isFlooded)
        if ((q > 0.0)) {
            massbal_addOutflowFlow(q, isFlooded.v)
            p = 0
            f3@ while ((p < Nobjects!![POLLUT])) {
                w = (q * Node!![i]!!.newQual!![p])
                massbal_addOutflowQual(p, w, isFlooded.v)
                p++
            }
        } else {
            massbal_addInflowFlow(EXTERNAL_INFLOW, (-q))
        }
        q = Node!![i]!!.newLatFlow
        if ((q < 0.0)) {
            p = 0
            f4@ while ((p < Nobjects!![POLLUT])) {
                w = ((-q) * Node!![i]!!.newQual!![p])
                massbal_addOutflowQual(p, w, 0)
                p++
            }
        }
        i++
    }
}

// routing.c:929
fun sortEvents() {
    var i: Int = 0
    var j: Int = 0
    var gap: Int = 0
    var temp: TEvent? = TEvent()
    gap = (NumEvents / 2)
    f1@ while ((gap >= 1)) {
        i = gap
        f2@ while ((i < NumEvents)) {
            temp = Event!![i]!!.copy()
            j = (i - gap)
            w3@ while (((j >= 0) && (Event!![j]!!.start > temp!!.start))) {
                Event!![(j + gap)] = Event!![j]!!.copy()
                j -= gap
            }
            if ((j != (i - gap))) {
                Event!![(j + gap)] = temp!!.copy()
            }
            i += gap
        }
        gap /= 2
    }
    i = 0
    f4@ while ((i < (NumEvents - 1))) {
        if ((Event!![i]!!.end > (Event!![(i + 1)])!!.start)) {
            Event!![i]!!.end = (Event!![(i + 1)])!!.start
        }
        i++
    }
}

