// Generated from EPA SWMM 5.2.4 (report.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_report")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField val SysTime = LBox(0L)

// report.c:93
fun report_readOptions(tok: Array<CPtr?>?, ntoks: Int): Int {
    var k: Int = 0
    var j: Int = 0
    var m: Int = 0
    var t: Int = 0
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    k = (findmatch(tok!![0], ReportWords)).toByte().toInt()
    if ((k < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![0])
    }
    if (((k < 2) || (k > 4))) {
        m = findmatch(tok!![1], NoYesWords)
        if (((m < NO) || (m > YES))) {
            return error_setInpError(ERR_KEYWORD, tok!![1])
        }
        when (k) {
            0 -> {
                RptFlags!!.disabled = m
                return 0
            }
            1 -> {
                RptFlags!!.input = m
                return 0
            }
            5 -> {
                RptFlags!!.continuity = m
                return 0
            }
            6 -> {
                RptFlags!!.flowStats = m
                return 0
            }
            7 -> {
                RptFlags!!.controls = m
                return 0
            }
            8 -> {
                RptFlags!!.averages = m
                return 0
            }
            9 -> {
                return 0
            }
            else -> {
                return error_setInpError(ERR_KEYWORD, tok!![1])
            }
        }
    } else {
        when (k) {
            2 -> {
                m = SUBCATCH
            }
            3 -> {
                m = NODE
            }
            4 -> {
                m = LINK
            }
        }
    }
    if ((strcomp(tok!![1], __L355) != 0)) {
        k = NONE
    } else if ((strcomp(tok!![1], __L351) != 0)) {
        k = ALL
    } else {
        k = SOME
        t = 1
        f3@ while ((t < ntoks)) {
            j = project_findObject(m, tok!![t])
            if ((j < 0)) {
                return error_setInpError(ERR_NAME, tok!![t])
            }
            when (m) {
                SUBCATCH -> {
                    Subcatch!![j]!!.rptFlag = 1
                }
                NODE -> {
                    Node!![j]!!.rptFlag = 1
                }
                LINK -> {
                    Link!![j]!!.rptFlag = 1
                }
            }
            t++
        }
    }
    when (m) {
        SUBCATCH -> {
            RptFlags!!.subcatchments = k
        }
        NODE -> {
            RptFlags!!.nodes = k
        }
        LINK -> {
            RptFlags!!.links = k
        }
    }
    return 0
}

// report.c:171
fun report_writeLine(line: CPtr?) {
    if ((Frpt!!.file != null)) {
        R.fprintf(Frpt!!.file, __L356, line)
    }
}

// report.c:183
fun report_writeSysTime() {
    var theTime: CPtr? = R.buf(9)
    var elapsedTime: Double = 0.0
    val endTime = LBox(0L)
    if ((Frpt!!.file != null)) {
        R.fprintf(Frpt!!.file, __L357, R.ctime(SysTime.v))
        (R.time()).also { __v -> endTime.v = __v }
        R.fprintf(Frpt!!.file, __L358, R.ctime(endTime.v))
        elapsedTime = (endTime.v - SysTime.v).toDouble()
        R.fprintf(Frpt!!.file, __L359)
        if ((elapsedTime < 1.0)) {
            R.fprintf(Frpt!!.file, __L360)
        } else {
            elapsedTime /= 86400.0
            if ((elapsedTime >= 1.0)) {
                R.fprintf(Frpt!!.file, __L361, R.D2I(floor(elapsedTime)))
                elapsedTime -= (floor(elapsedTime))
            }
            datetime_timeToStr(elapsedTime, theTime)
            R.fprintf(Frpt!!.file, __L169, theTime)
        }
    }
}

// report.c:220
fun report_writeLogo() {
    R.fprintf(Frpt!!.file, __L362)
    R.fprintf(Frpt!!.file, __L363)
    R.fprintf(Frpt!!.file, __L276)
    (R.time()).also { __v -> SysTime.v = __v }
}

// report.c:235
fun report_writeTitle() {
    var i: Int = 0
    var lineCount: Int = 0
    if ((ErrorCode != 0)) {
        return
    }
    i = 0
    f1@ while ((i < 3)) {
        if ((R.strlen(Title!![i]) > 0L)) {
            report_writeLine((Title!![i]))
            lineCount++
        }
        i++
    }
}

// report.c:254
fun report_writeOptions() {
    var str: CPtr? = R.buf(80)
    report_writeLine((__L0))
    report_writeLine((__L227))
    report_writeLine((__L364))
    report_writeLine((__L227))
    R.fprintf(Frpt!!.file, __L365, FlowUnitWords!![FlowUnits])
    R.fprintf(Frpt!!.file, __L366)
    R.fprintf(Frpt!!.file, __L367)
    if (((IgnoreRainfall != 0) || (Nobjects!![GAGE] == 0))) {
        R.fprintf(Frpt!!.file, __L4)
    } else {
        R.fprintf(Frpt!!.file, __L5)
    }
    R.fprintf(Frpt!!.file, __L368)
    if (((IgnoreRDII != 0) || (Nobjects!![UNITHYD] == 0))) {
        R.fprintf(Frpt!!.file, __L4)
    } else {
        R.fprintf(Frpt!!.file, __L5)
    }
    R.fprintf(Frpt!!.file, __L369)
    if (((IgnoreSnowmelt != 0) || (Nobjects!![SNOWMELT] == 0))) {
        R.fprintf(Frpt!!.file, __L4)
    } else {
        R.fprintf(Frpt!!.file, __L5)
    }
    R.fprintf(Frpt!!.file, __L370)
    if (((IgnoreGwater != 0) || (Nobjects!![AQUIFER] == 0))) {
        R.fprintf(Frpt!!.file, __L4)
    } else {
        R.fprintf(Frpt!!.file, __L5)
    }
    R.fprintf(Frpt!!.file, __L371)
    if (((IgnoreRouting != 0) || (Nobjects!![LINK] == 0))) {
        R.fprintf(Frpt!!.file, __L4)
    } else {
        R.fprintf(Frpt!!.file, __L5)
        R.fprintf(Frpt!!.file, __L372)
        if ((AllowPonding != 0)) {
            R.fprintf(Frpt!!.file, __L5)
        } else {
            R.fprintf(Frpt!!.file, __L4)
        }
    }
    R.fprintf(Frpt!!.file, __L373)
    if (((IgnoreQuality != 0) || (Nobjects!![POLLUT] == 0))) {
        R.fprintf(Frpt!!.file, __L4)
    } else {
        R.fprintf(Frpt!!.file, __L5)
    }
    if ((Nobjects!![SUBCATCH] > 0)) {
        R.fprintf(Frpt!!.file, __L374, InfilModelWords!![InfilModel])
    }
    if ((Nobjects!![LINK] > 0)) {
        R.fprintf(Frpt!!.file, __L375, RouteModelWords!![RouteModel])
    }
    if ((RouteModel == DW)) {
        R.fprintf(Frpt!!.file, __L376, SurchargeWords!![SurchargeMethod])
    }
    datetime_dateToStr(StartDate.v, str)
    R.fprintf(Frpt!!.file, __L377, str)
    datetime_timeToStr(StartTime.v, str)
    R.fprintf(Frpt!!.file, __L378, str)
    datetime_dateToStr(EndDate.v, str)
    R.fprintf(Frpt!!.file, __L379, str)
    datetime_timeToStr(EndTime.v, str)
    R.fprintf(Frpt!!.file, __L378, str)
    R.fprintf(Frpt!!.file, __L380, StartDryDays)
    datetime_timeToStr(datetime_encodeTime(0, 0, ReportStep), str)
    R.fprintf(Frpt!!.file, __L381, str)
    if ((Nobjects!![SUBCATCH] > 0)) {
        datetime_timeToStr(datetime_encodeTime(0, 0, WetStep), str)
        R.fprintf(Frpt!!.file, __L382, str)
        datetime_timeToStr(datetime_encodeTime(0, 0, DryStep), str)
        R.fprintf(Frpt!!.file, __L383, str)
    }
    if ((Nobjects!![LINK] > 0)) {
        R.fprintf(Frpt!!.file, __L384, RouteStep)
        if ((RouteModel == DW)) {
            R.fprintf(Frpt!!.file, __L385)
            if ((CourantFactor.v > 0.0)) {
                R.fprintf(Frpt!!.file, __L5)
            } else {
                R.fprintf(Frpt!!.file, __L4)
            }
            R.fprintf(Frpt!!.file, __L386, MaxTrials)
            R.fprintf(Frpt!!.file, __L387, NumThreads)
            R.fprintf(Frpt!!.file, __L388, (HeadTol.v * UCF(LENGTH)))
            if ((UnitSystem == US)) {
                R.fprintf(Frpt!!.file, __L389)
            } else {
                R.fprintf(Frpt!!.file, __L390)
            }
        }
    }
    report_writeLine((__L0))
}

// report.c:355
fun report_writeRainStats(i: Int, r: TRainStats?) {
    var date1: CPtr? = R.bufFrom(__L391, 12)
    var date2: CPtr? = R.bufFrom(__L391, 12)
    if ((RptFlags!!.disabled != 0)) {
        return
    }
    if ((i < 0)) {
        report_writeLine((__L0))
        report_writeLine((__L255))
        report_writeLine((__L392))
        report_writeLine((__L255))
        R.fprintf(Frpt!!.file, __L393)
        R.fprintf(Frpt!!.file, __L394)
        R.fprintf(Frpt!!.file, __L245)
    } else {
        if ((r!!.startDate != ((-693594)).toDouble())) {
            datetime_dateToStr(r!!.startDate, date1)
        }
        if ((r!!.endDate != ((-693594)).toDouble())) {
            datetime_dateToStr(r!!.endDate, date2)
        }
        R.fprintf(Frpt!!.file, __L395, Gage!![i]!!.staID, date1, date2, (Gage!![i]!!.rainInterval / 60), r!!.periodsRain, r!!.periodsMissing, r!!.periodsMalfunc)
    }
    report_writeLine((__L0))
}

// report.c:396
fun report_writeRdiiStats(rainVol: Double, rdiiVol: Double) {
    var ratio: Double = 0.0
    var ucf1: Double = 0.0
    var ucf2: Double = 0.0
    if ((RptFlags!!.disabled != 0)) {
        return
    }
    ucf1 = (UCF(LENGTH) * UCF(LANDAREA))
    if ((UnitSystem == US)) {
        ucf2 = (0.64632 / 86400.0)
    } else {
        ucf2 = (2.4466 / 86400.0)
    }
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L396)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L397)
    } else {
        R.fprintf(Frpt!!.file, __L398)
    }
    R.fprintf(Frpt!!.file, __L399)
    R.fprintf(Frpt!!.file, __L400, (rainVol * ucf1), (rainVol * ucf2))
    R.fprintf(Frpt!!.file, __L401, (rdiiVol * ucf1), (rdiiVol * ucf2))
    if ((rainVol == 0.0)) {
        ratio = 0.0
    } else {
        ratio = (rdiiVol / rainVol)
    }
    R.fprintf(Frpt!!.file, __L402, ratio)
    report_writeLine((__L0))
}

// report.c:441
fun report_writeControlActionsHeading() {
    report_writeLine((__L0))
    report_writeLine((__L255))
    report_writeLine((__L403))
    report_writeLine((__L255))
    R.fprintf(Frpt!!.file, __L276)
}

// report.c:452
fun report_writeControlAction(aDate: Double, linkID: CPtr?, value_: Double, ruleID: CPtr?) {
    var theDate: CPtr? = R.buf(12)
    var theTime: CPtr? = R.buf(9)
    if ((RptFlags!!.disabled != 0)) {
        return
    }
    datetime_dateToStr(aDate, theDate)
    datetime_timeToStr(aDate, theTime)
    R.fprintf(Frpt!!.file, __L404, theDate, theTime, linkID, value_, ruleID)
}

// report.c:480
fun report_writeRunoffError(totals: TRunoffTotals?, totalArea: Double) {
    if ((Frunoff!!.mode == USE_FILE)) {
        report_writeLine((__L0))
        R.fprintf(Frpt!!.file, __L405, Frunoff!!.name)
        report_writeLine((__L0))
        return
    }
    if ((totalArea == 0.0)) {
        return
    }
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L406)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L407)
    } else {
        R.fprintf(Frpt!!.file, __L408)
    }
    R.fprintf(Frpt!!.file, __L409)
    if ((totals!!.initStorage > 0.0)) {
        R.fprintf(Frpt!!.file, __L410, ((totals!!.initStorage * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.initStorage / totalArea) * UCF(RAINDEPTH)))
    }
    if ((Nobjects!![SNOWMELT] > 0)) {
        R.fprintf(Frpt!!.file, __L411, ((totals!!.initSnowCover * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.initSnowCover / totalArea) * UCF(RAINDEPTH)))
    }
    R.fprintf(Frpt!!.file, __L412, ((totals!!.rainfall * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.rainfall / totalArea) * UCF(RAINDEPTH)))
    if ((totals!!.runon > 0.0)) {
        R.fprintf(Frpt!!.file, __L413, ((totals!!.runon * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.runon / totalArea) * UCF(RAINDEPTH)))
    }
    R.fprintf(Frpt!!.file, __L414, ((totals!!.evap * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.evap / totalArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L415, ((totals!!.infil * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.infil / totalArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L416, ((totals!!.runoff * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.runoff / totalArea) * UCF(RAINDEPTH)))
    if ((totals!!.drains > 0.0)) {
        R.fprintf(Frpt!!.file, __L417, ((totals!!.drains * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.drains / totalArea) * UCF(RAINDEPTH)))
    }
    if ((Nobjects!![SNOWMELT] > 0)) {
        R.fprintf(Frpt!!.file, __L418, ((totals!!.snowRemoved * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.snowRemoved / totalArea) * UCF(RAINDEPTH)))
        R.fprintf(Frpt!!.file, __L419, ((totals!!.finalSnowCover * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.finalSnowCover / totalArea) * UCF(RAINDEPTH)))
    }
    R.fprintf(Frpt!!.file, __L420, ((totals!!.finalStorage * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.finalStorage / totalArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L421, totals!!.pctError)
    report_writeLine((__L0))
}

// report.c:577
fun report_writeLoadingError(totals: Array<TLoadingTotals?>?) {
    var p1: Int = 0
    var p2: Int = 0
    p1 = 1
    p2 = ((if ((((5) <= (Nobjects!![POLLUT])))) (5) else (Nobjects!![POLLUT])))
    w1@ while ((p1 <= Nobjects!![POLLUT])) {
        report_LoadingErrors((p1 - 1), (p2 - 1), totals)
        p1 = (p2 + 1)
        p2 = (p1 + 4)
        p2 = ((if ((((p2) <= (Nobjects!![POLLUT])))) (p2) else (Nobjects!![POLLUT])))
    }
}

// report.c:599
fun report_LoadingErrors(p1: Int, p2: Int, totals: Array<TLoadingTotals?>?) {
    var i: Int = 0
    var p: Int = 0
    var cf: Double = 1.0
    var units: CPtr? = R.buf(15)
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L422)
    p = p1
    f1@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L423, Pollut!![p]!!.ID)
        p++
    }
    R.fprintf(Frpt!!.file, __L424)
    p = p1
    f2@ while ((p <= p2)) {
        i = UnitSystem
        if ((Pollut!![p]!!.units == COUNT)) {
            i = 2
        }
        sstrncpy(units, LoadUnitsWords!![i], 14L)
        R.fprintf(Frpt!!.file, __L423, units)
        p++
    }
    R.fprintf(Frpt!!.file, __L422)
    p = p1
    f3@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L425)
        p++
    }
    R.fprintf(Frpt!!.file, __L426)
    p = p1
    f4@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.initLoad * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L428)
    p = p1
    f5@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.buildup * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L429)
    p = p1
    f6@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.deposition * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L430)
    p = p1
    f7@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.sweeping * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L431)
    p = p1
    f8@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.infil * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L432)
    p = p1
    f9@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.bmpRemoval * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L433)
    p = p1
    f10@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.runoff * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L434)
    p = p1
    f11@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, (totals!![p]!!.finalLoad * cf))
        p++
    }
    R.fprintf(Frpt!!.file, __L435)
    p = p1
    f12@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, totals!![p]!!.pctError)
        p++
    }
    report_writeLine((__L0))
}

// report.c:685
fun report_writeGwaterError(totals: TGwaterTotals?, gwArea: Double) {
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L406)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L436)
    } else {
        R.fprintf(Frpt!!.file, __L437)
    }
    R.fprintf(Frpt!!.file, __L409)
    R.fprintf(Frpt!!.file, __L438, ((totals!!.initStorage * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.initStorage / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L439, ((totals!!.infil * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.infil / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L440, ((totals!!.upperEvap * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.upperEvap / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L441, ((totals!!.lowerEvap * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.lowerEvap / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L442, ((totals!!.lowerPerc * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.lowerPerc / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L443, ((totals!!.gwater * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.gwater / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L420, ((totals!!.finalStorage * UCF(LENGTH)) * UCF(LANDAREA)), ((totals!!.finalStorage / gwArea) * UCF(RAINDEPTH)))
    R.fprintf(Frpt!!.file, __L421, totals!!.pctError)
    report_writeLine((__L0))
}

// report.c:737
fun report_writeFlowError(totals: TRoutingTotals?) {
    var ucf1: Double = 0.0
    var ucf2: Double = 0.0
    ucf1 = (UCF(LENGTH) * UCF(LANDAREA))
    if ((UnitSystem == US)) {
        ucf2 = (0.64632 / 86400.0)
    } else {
        ucf2 = (2.4466 / 86400.0)
    }
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L444)
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L445)
    } else {
        R.fprintf(Frpt!!.file, __L446)
    }
    R.fprintf(Frpt!!.file, __L447)
    R.fprintf(Frpt!!.file, __L448, (totals!!.dwInflow * ucf1), (totals!!.dwInflow * ucf2))
    R.fprintf(Frpt!!.file, __L449, (totals!!.wwInflow * ucf1), (totals!!.wwInflow * ucf2))
    R.fprintf(Frpt!!.file, __L450, (totals!!.gwInflow * ucf1), (totals!!.gwInflow * ucf2))
    R.fprintf(Frpt!!.file, __L451, (totals!!.iiInflow * ucf1), (totals!!.iiInflow * ucf2))
    R.fprintf(Frpt!!.file, __L452, (totals!!.exInflow * ucf1), (totals!!.exInflow * ucf2))
    R.fprintf(Frpt!!.file, __L453, (totals!!.outflow * ucf1), (totals!!.outflow * ucf2))
    R.fprintf(Frpt!!.file, __L454, (totals!!.flooding * ucf1), (totals!!.flooding * ucf2))
    R.fprintf(Frpt!!.file, __L414, (totals!!.evapLoss * ucf1), (totals!!.evapLoss * ucf2))
    R.fprintf(Frpt!!.file, __L455, (totals!!.seepLoss * ucf1), (totals!!.seepLoss * ucf2))
    R.fprintf(Frpt!!.file, __L456, (totals!!.initStorage * ucf1), (totals!!.initStorage * ucf2))
    R.fprintf(Frpt!!.file, __L457, (totals!!.finalStorage * ucf1), (totals!!.finalStorage * ucf2))
    R.fprintf(Frpt!!.file, __L421, totals!!.pctError)
    report_writeLine((__L0))
}

// report.c:800
fun report_writeQualError(QualTotals: Array<TRoutingTotals?>?) {
    var p1: Int = 0
    var p2: Int = 0
    p1 = 1
    p2 = ((if ((((5) <= (Nobjects!![POLLUT])))) (5) else (Nobjects!![POLLUT])))
    w1@ while ((p1 <= Nobjects!![POLLUT])) {
        report_QualErrors((p1 - 1), (p2 - 1), QualTotals)
        p1 = (p2 + 1)
        p2 = (p1 + 4)
        p2 = ((if ((((p2) <= (Nobjects!![POLLUT])))) (p2) else (Nobjects!![POLLUT])))
    }
}

// report.c:821
fun report_QualErrors(p1: Int, p2: Int, QualTotals: Array<TRoutingTotals?>?) {
    var i: Int = 0
    var p: Int = 0
    var units: CPtr? = R.buf(15)
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L422)
    p = p1
    f1@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L423, Pollut!![p]!!.ID)
        p++
    }
    R.fprintf(Frpt!!.file, __L458)
    p = p1
    f2@ while ((p <= p2)) {
        i = UnitSystem
        if ((Pollut!![p]!!.units == COUNT)) {
            i = 2
        }
        sstrncpy(units, LoadUnitsWords!![i], 14L)
        R.fprintf(Frpt!!.file, __L423, units)
        p++
    }
    R.fprintf(Frpt!!.file, __L422)
    p = p1
    f3@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L425)
        p++
    }
    R.fprintf(Frpt!!.file, __L459)
    p = p1
    f4@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.dwInflow)
        p++
    }
    R.fprintf(Frpt!!.file, __L460)
    p = p1
    f5@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.wwInflow)
        p++
    }
    R.fprintf(Frpt!!.file, __L461)
    p = p1
    f6@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.gwInflow)
        p++
    }
    R.fprintf(Frpt!!.file, __L462)
    p = p1
    f7@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.iiInflow)
        p++
    }
    R.fprintf(Frpt!!.file, __L463)
    p = p1
    f8@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.exInflow)
        p++
    }
    R.fprintf(Frpt!!.file, __L464)
    p = p1
    f9@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.outflow)
        p++
    }
    R.fprintf(Frpt!!.file, __L465)
    p = p1
    f10@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.flooding)
        p++
    }
    R.fprintf(Frpt!!.file, __L466)
    p = p1
    f11@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.seepLoss)
        p++
    }
    R.fprintf(Frpt!!.file, __L467)
    p = p1
    f12@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.reacted)
        p++
    }
    R.fprintf(Frpt!!.file, __L468)
    p = p1
    f13@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.initStorage)
        p++
    }
    R.fprintf(Frpt!!.file, __L469)
    p = p1
    f14@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.finalStorage)
        p++
    }
    R.fprintf(Frpt!!.file, __L435)
    p = p1
    f15@ while ((p <= p2)) {
        R.fprintf(Frpt!!.file, __L427, QualTotals!![p]!!.pctError)
        p++
    }
    report_writeLine((__L0))
}

// report.c:923
fun report_writeMaxStats(maxMassBalErrs: Array<TMaxStats?>?, maxCourantCrit: Array<TMaxStats?>?, nMaxStats: Int) {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    if (((RouteModel != DW) || (Nobjects!![LINK] == 0))) {
        return
    }
    if ((nMaxStats <= 0)) {
        return
    }
    if ((maxMassBalErrs!![0]!!.index >= 0)) {
        report_writeLine((__L0))
        report_writeLine((__L470))
        report_writeLine((__L471))
        report_writeLine((__L470))
        i = 0
        f1@ while ((i < nMaxStats)) {
            j = maxMassBalErrs!![i]!!.index
            if ((j < 0)) {
                i++
                continue@f1
            }
            R.fprintf(Frpt!!.file, __L472, Node!![j]!!.ID, maxMassBalErrs!![i]!!.value_)
            i++
        }
        report_writeLine((__L0))
    }
    if ((CourantFactor.v == 0.0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L473))
    report_writeLine((__L474))
    report_writeLine((__L473))
    k = 0
    i = 0
    f2@ while ((i < nMaxStats)) {
        j = maxCourantCrit!![i]!!.index
        if ((j < 0)) {
            i++
            continue@f2
        }
        k++
        if ((maxCourantCrit!![i]!!.objType == NODE)) {
            R.fprintf(Frpt!!.file, __L475, Node!![j]!!.ID)
        } else {
            R.fprintf(Frpt!!.file, __L476, Link!![j]!!.ID)
        }
        R.fprintf(Frpt!!.file, __L477, maxCourantCrit!![i]!!.value_)
        i++
    }
    if ((k == 0)) {
        R.fprintf(Frpt!!.file, __L478)
    }
    report_writeLine((__L0))
}

// report.c:977
fun report_writeMaxFlowTurns(flowTurns: Array<TMaxStats?>?, nMaxStats: Int) {
    var i: Int = 0
    var j: Int = 0
    if ((Nobjects!![LINK] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L479))
    report_writeLine((__L480))
    report_writeLine((__L479))
    if (((nMaxStats <= 0) || (flowTurns!![0]!!.index <= 0))) {
        R.fprintf(Frpt!!.file, __L481)
    } else {
        i = 0
        f1@ while ((i < nMaxStats)) {
            j = flowTurns!![i]!!.index
            if ((j < 0)) {
                i++
                continue@f1
            }
            R.fprintf(Frpt!!.file, __L482, Link!![j]!!.ID, flowTurns!![i]!!.value_)
            i++
        }
    }
    report_writeLine((__L0))
}

// report.c:1011
fun report_writeNonconvergedStats(maxNonconverged: Array<TMaxStats?>?, nMaxStats: Int) {
    var i: Int = 0
    var j: Int = 0
    if (((Nobjects!![NODE] == 0) || (RouteModel != DW))) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L483))
    report_writeLine((__L484))
    report_writeLine((__L483))
    if ((((nMaxStats <= 0) || (maxNonconverged!![0]!!.index <= 0)) || (maxNonconverged!![0]!!.value_ < 0.00005))) {
        R.fprintf(Frpt!!.file, __L485)
    } else {
        i = 0
        f1@ while ((i < nMaxStats)) {
            j = maxNonconverged!![i]!!.index
            if (((j < 0) || (maxNonconverged!![i]!!.value_ <= 0.0))) {
                i++
                continue@f1
            }
            R.fprintf(Frpt!!.file, __L472, Node!![j]!!.ID, (100.0 * maxNonconverged!![i]!!.value_))
            i++
        }
    }
    report_writeLine((__L0))
}

// report.c:1037
fun report_writeTimeStepStats(timeStepStats: TTimeStepStats?) {
    var timeStepCount: Double = (timeStepStats!!.timeStepCount).toDouble()
    var totalRoutingTime: Double = 0.0
    var fSteadyState: Double = 0.0
    if (((Nobjects!![LINK] == 0) || ((timeStepStats!!.timeStepCount).toDouble() == 0.0))) {
        return
    }
    totalRoutingTime = (timeStepStats!!.steadyStateTime + timeStepStats!!.routingTime)
    if ((totalRoutingTime > 0.0)) {
        fSteadyState = ((100.0 * timeStepStats!!.steadyStateTime) / totalRoutingTime)
    }
    report_writeLine((__L0))
    report_writeLine((__L470))
    report_writeLine((__L486))
    report_writeLine((__L470))
    R.fprintf(Frpt!!.file, __L487, timeStepStats!!.minTimeStep)
    R.fprintf(Frpt!!.file, __L488, (timeStepStats!!.routingTime / timeStepCount))
    R.fprintf(Frpt!!.file, __L489, timeStepStats!!.maxTimeStep)
    R.fprintf(Frpt!!.file, __L490, ((if ((((fSteadyState) <= (100.0)))) (fSteadyState) else (100.0))))
    R.fprintf(Frpt!!.file, __L491, (timeStepStats!!.trialsCount / timeStepCount))
    R.fprintf(Frpt!!.file, __L492, ((100.0 * (NonConvergeCount).toDouble()) / timeStepCount))
    if (((RouteModel == DW) && (CourantFactor.v > 0.0))) {
        report_RouteStepFreq(timeStepStats)
    }
    report_writeLine((__L0))
}

// report.c:1085
fun report_RouteStepFreq(timeStepStats: TTimeStepStats?) {
    var totalSteps: Double = 0.0
    var i: Int = 0
    i = 1
    f1@ while ((i < 6)) {
        totalSteps += ((timeStepStats!!.timeStepCounts!![i]).toDouble())
        i++
    }
    if ((totalSteps == 0.0)) {
        return
    }
    R.fprintf(Frpt!!.file, __L493)
    i = 1
    f2@ while ((i < 6)) {
        R.fprintf(Frpt!!.file, __L494, timeStepStats!!.timeStepIntervals!![(i - 1)], timeStepStats!!.timeStepIntervals!![i], ((100.0 * ((timeStepStats!!.timeStepCounts!![i])).toDouble()) / totalSteps))
        i++
    }
}

// report.c:1113
fun report_writeReport() {
    if ((ErrorCode != 0)) {
        return
    }
    if ((Nperiods == 0L)) {
        return
    }
    if (((RptFlags!!.subcatchments != NONE) && ((((IgnoreRainfall == 0) || (IgnoreSnowmelt == 0)) || (IgnoreGwater == 0))))) {
        report_Subcatchments()
    }
    if (((IgnoreRouting == 1) && (IgnoreQuality == 1))) {
        return
    }
    if ((RptFlags!!.nodes != NONE)) {
        report_Nodes()
    }
    if ((RptFlags!!.links != NONE)) {
        report_Links()
    }
}

// report.c:1135
fun report_Subcatchments() {
    var j: Int = 0
    var p: Int = 0
    var k: Int = 0
    var period: Int = 0
    val days = DBox(0.0)
    var theDate: CPtr? = R.buf(12)
    var theTime: CPtr? = R.buf(9)
    var hasSnowmelt: Int = (if ((((Nobjects!![SNOWMELT] > 0) && (!(IgnoreSnowmelt != 0))))) 1 else 0)
    var hasGwater: Int = (if ((((Nobjects!![AQUIFER] > 0) && (!(IgnoreGwater != 0))))) 1 else 0)
    var hasQuality: Int = (if ((((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0))))) 1 else 0)
    if ((Nobjects!![SUBCATCH] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L479))
    report_writeLine((__L495))
    report_writeLine((__L479))
    j = 0
    f1@ while ((j < Nobjects!![SUBCATCH])) {
        k = (Subcatch!![j]!!.rptFlag - 1)
        if ((k >= 0)) {
            report_SubcatchHeader(Subcatch!![j]!!.ID)
            period = 1
            f2@ while (((period).toLong() <= Nperiods)) {
                output_readDateTime((period).toLong(), days)
                datetime_dateToStr(days.v, theDate)
                datetime_timeToStr(days.v, theTime)
                output_readSubcatchResults((period).toLong(), k)
                R.fprintf(Frpt!!.file, __L496, theDate, theTime, (SubcatchResults!![SUBCATCH_RAINFALL]).toDouble(), (((SubcatchResults!![SUBCATCH_EVAP]).toDouble() / 24.0) + (SubcatchResults!![SUBCATCH_INFIL]).toDouble()), (SubcatchResults!![SUBCATCH_RUNOFF]).toDouble())
                if ((hasSnowmelt != 0)) {
                    R.fprintf(Frpt!!.file, __L497, (SubcatchResults!![SUBCATCH_SNOWDEPTH]).toDouble())
                }
                if ((hasGwater != 0)) {
                    R.fprintf(Frpt!!.file, __L498, (SubcatchResults!![SUBCATCH_GW_ELEV]).toDouble(), (SubcatchResults!![SUBCATCH_GW_FLOW]).toDouble())
                }
                if ((hasQuality != 0)) {
                    p = 0
                    f3@ while ((p < Nobjects!![POLLUT])) {
                        R.fprintf(Frpt!!.file, __L499, (SubcatchResults!![(SUBCATCH_WASHOFF + p)]).toDouble())
                        p++
                    }
                }
                period++
            }
            report_writeLine((__L0))
        }
        j++
    }
}

// report.c:1192
fun report_SubcatchHeader(id: CPtr?) {
    var i: Int = 0
    var hasSnowmelt: Int = (if ((((Nobjects!![SNOWMELT] > 0) && (!(IgnoreSnowmelt != 0))))) 1 else 0)
    var hasGwater: Int = (if ((((Nobjects!![AQUIFER] > 0) && (!(IgnoreGwater != 0))))) 1 else 0)
    var hasQuality: Int = (if ((((Nobjects!![POLLUT] > 0) && (!(IgnoreQuality != 0))))) 1 else 0)
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L500, id)
    report_writeLine((__L501))
    if ((hasSnowmelt > 0)) {
        R.fprintf(Frpt!!.file, __L502)
    }
    if ((hasGwater != 0)) {
        R.fprintf(Frpt!!.file, __L503)
        R.fprintf(Frpt!!.file, __L503)
    }
    if ((hasQuality != 0)) {
        i = 0
        f1@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L503)
            i++
        }
    }
    R.fprintf(Frpt!!.file, __L504)
    if ((hasSnowmelt != 0)) {
        R.fprintf(Frpt!!.file, __L505)
    }
    if ((hasGwater != 0)) {
        R.fprintf(Frpt!!.file, __L506)
    }
    if ((hasQuality != 0)) {
        i = 0
        f2@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L507, Pollut!![i]!!.ID)
            i++
        }
    }
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L508, FlowUnitWords!![FlowUnits])
    } else {
        R.fprintf(Frpt!!.file, __L509, FlowUnitWords!![FlowUnits])
    }
    if ((hasSnowmelt != 0)) {
        if ((UnitSystem == US)) {
            R.fprintf(Frpt!!.file, __L510)
        } else {
            R.fprintf(Frpt!!.file, __L511)
        }
    }
    if ((hasGwater != 0)) {
        if ((UnitSystem == US)) {
            R.fprintf(Frpt!!.file, __L512, FlowUnitWords!![FlowUnits])
        } else {
            R.fprintf(Frpt!!.file, __L513, FlowUnitWords!![FlowUnits])
        }
    }
    if ((hasQuality != 0)) {
        i = 0
        f3@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L507, QualUnitsWords!![Pollut!![i]!!.units])
            i++
        }
    }
    report_writeLine((__L501))
    if ((hasSnowmelt != 0)) {
        R.fprintf(Frpt!!.file, __L502)
    }
    if ((hasGwater != 0)) {
        R.fprintf(Frpt!!.file, __L503)
        R.fprintf(Frpt!!.file, __L503)
    }
    if ((hasQuality != 0)) {
        i = 0
        f4@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L503)
            i++
        }
    }
}

// report.c:1261
fun report_Nodes() {
    var j: Int = 0
    var p: Int = 0
    var k: Int = 0
    var period: Int = 0
    val days = DBox(0.0)
    var theDate: CPtr? = R.buf(12)
    var theTime: CPtr? = R.buf(9)
    if ((Nobjects!![NODE] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L514))
    report_writeLine((__L515))
    report_writeLine((__L514))
    j = 0
    f1@ while ((j < Nobjects!![NODE])) {
        k = (Node!![j]!!.rptFlag - 1)
        if ((k >= 0)) {
            report_NodeHeader(Node!![j]!!.ID)
            period = 1
            f2@ while (((period).toLong() <= Nperiods)) {
                output_readDateTime((period).toLong(), days)
                datetime_dateToStr(days.v, theDate)
                datetime_timeToStr(days.v, theTime)
                output_readNodeResults((period).toLong(), k)
                R.fprintf(Frpt!!.file, __L516, theDate, theTime, (NodeResults!![NODE_INFLOW]).toDouble(), (NodeResults!![NODE_OVERFLOW]).toDouble(), (NodeResults!![NODE_DEPTH]).toDouble(), (NodeResults!![NODE_HEAD]).toDouble())
                if (!(IgnoreQuality != 0)) {
                    p = 0
                    f3@ while ((p < Nobjects!![POLLUT])) {
                        R.fprintf(Frpt!!.file, __L195, (NodeResults!![(NODE_QUAL + p)]).toDouble())
                        p++
                    }
                }
                period++
            }
            report_writeLine((__L0))
        }
        j++
    }
}

// report.c:1305
fun report_NodeHeader(id: CPtr?) {
    var i: Int = 0
    var lengthUnits: CPtr? = R.buf(9)
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L517, id)
    report_writeLine((__L518))
    i = 0
    f1@ while ((i < Nobjects!![POLLUT])) {
        R.fprintf(Frpt!!.file, __L503)
        i++
    }
    R.fprintf(Frpt!!.file, __L519)
    if (!(IgnoreQuality != 0)) {
        i = 0
        f2@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L507, Pollut!![i]!!.ID)
            i++
        }
    }
    if ((UnitSystem == US)) {
        sstrncpy(lengthUnits, __L520, 8L)
    } else {
        sstrncpy(lengthUnits, __L521, 8L)
    }
    R.fprintf(Frpt!!.file, __L522, FlowUnitWords!![FlowUnits], FlowUnitWords!![FlowUnits], lengthUnits, lengthUnits)
    if (!(IgnoreQuality != 0)) {
        i = 0
        f3@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L507, QualUnitsWords!![Pollut!![i]!!.units])
            i++
        }
    }
    report_writeLine((__L518))
    if (!(IgnoreQuality != 0)) {
        i = 0
        f4@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L503)
            i++
        }
    }
}

// report.c:1339
fun report_Links() {
    var j: Int = 0
    var p: Int = 0
    var k: Int = 0
    var period: Int = 0
    val days = DBox(0.0)
    var theDate: CPtr? = R.buf(12)
    var theTime: CPtr? = R.buf(9)
    if ((Nobjects!![LINK] == 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L514))
    report_writeLine((__L523))
    report_writeLine((__L514))
    j = 0
    f1@ while ((j < Nobjects!![LINK])) {
        k = (Link!![j]!!.rptFlag - 1)
        if ((k >= 0)) {
            report_LinkHeader(Link!![j]!!.ID)
            period = 1
            f2@ while (((period).toLong() <= Nperiods)) {
                output_readDateTime((period).toLong(), days)
                datetime_dateToStr(days.v, theDate)
                datetime_timeToStr(days.v, theTime)
                output_readLinkResults((period).toLong(), k)
                R.fprintf(Frpt!!.file, __L516, theDate, theTime, (LinkResults!![LINK_FLOW]).toDouble(), (LinkResults!![LINK_VELOCITY]).toDouble(), (LinkResults!![LINK_DEPTH]).toDouble(), (LinkResults!![LINK_CAPACITY]).toDouble())
                if (!(IgnoreQuality != 0)) {
                    p = 0
                    f3@ while ((p < Nobjects!![POLLUT])) {
                        R.fprintf(Frpt!!.file, __L195, (LinkResults!![(LINK_QUAL + p)]).toDouble())
                        p++
                    }
                }
                period++
            }
            report_writeLine((__L0))
        }
        j++
    }
}

// report.c:1383
fun report_LinkHeader(id: CPtr?) {
    var i: Int = 0
    report_writeLine((__L0))
    R.fprintf(Frpt!!.file, __L524, id)
    report_writeLine((__L518))
    i = 0
    f1@ while ((i < Nobjects!![POLLUT])) {
        R.fprintf(Frpt!!.file, __L503)
        i++
    }
    R.fprintf(Frpt!!.file, __L525)
    if (!(IgnoreQuality != 0)) {
        i = 0
        f2@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L507, Pollut!![i]!!.ID)
            i++
        }
    }
    if ((UnitSystem == US)) {
        R.fprintf(Frpt!!.file, __L526, FlowUnitWords!![FlowUnits])
    } else {
        R.fprintf(Frpt!!.file, __L527, FlowUnitWords!![FlowUnits])
    }
    if (!(IgnoreQuality != 0)) {
        i = 0
        f3@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L528, QualUnitsWords!![Pollut!![i]!!.units])
            i++
        }
    }
    report_writeLine((__L518))
    if (!(IgnoreQuality != 0)) {
        i = 0
        f4@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L503)
            i++
        }
    }
}

// report.c:1422
fun report_writeErrorMsg(code: Int, s: CPtr?) {
    if ((Frpt!!.file != null)) {
        report_writeLine((__L0))
        R.fprintf(Frpt!!.file, error_getMsg(code, Msg), s)
    }
    ErrorCode = code
    if (((ErrorCode <= ERR_INPUT) || (ErrorCode >= ERR_FILE_NAME))) {
        R.snprintf(ErrorMsg, (1024L).toInt(), error_getMsg(ErrorCode, Msg), s)
    }
}

// report.c:1446
fun report_writeErrorCode() {
    if ((Frpt!!.file != null)) {
        if ((((((ErrorCode >= ERR_MEMORY) && (ErrorCode <= ERR_TIMESTEP))) || (((ErrorCode >= ERR_FILE_NAME) && (ErrorCode <= ERR_OUT_FILE)))) || ((ErrorCode == ERR_SYSTEM)))) {
            R.fprintf(Frpt!!.file, __L169, error_getMsg(ErrorCode, Msg))
        }
    }
}

// report.c:1464
fun report_writeInputErrorMsg(k: Int, sect: Int, line: CPtr?, lineCount: Long) {
    if ((Frpt!!.file != null)) {
        report_writeErrorMsg(k, ErrString)
        if ((sect < 0)) {
            R.fprintf(Frpt!!.file, __L529, lineCount)
        } else {
            R.fprintf(Frpt!!.file, __L530, lineCount, SectWords!![sect])
        }
        R.fprintf(Frpt!!.file, __L356, line)
    }
}

// report.c:1485
fun report_writeWarningMsg(msg: CPtr?, id: CPtr?) {
    R.fprintf(Frpt!!.file, __L531, msg, id)
    Warnings++
}

// report.c:1499
fun report_writeTseriesErrorMsg(code: Int, tseries: TTable?) {
    var theDate: CPtr? = R.buf(20)
    var theTime: CPtr? = R.buf(20)
    var x: Double = 0.0
    if ((code == ERR_CURVE_SEQUENCE)) {
        x = tseries!!.x2
        datetime_dateToStr(x, theDate)
        datetime_timeToStr(x, theTime)
        report_writeErrorMsg(ERR_TIMESERIES_SEQUENCE, tseries!!.ID)
        R.fprintf(Frpt!!.file, __L532, theDate, theTime)
    } else {
        report_writeErrorMsg(code, tseries!!.ID)
    }
}

