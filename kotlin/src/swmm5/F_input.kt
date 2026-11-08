// Generated from EPA SWMM 5.2.4 (input.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_input")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var MAXERRS: Int = 100
@JvmField var Tok: Array<CPtr?>? = arrayOfNulls<CPtr>(40)
@JvmField var Ntokens: Int = 0
@JvmField var Mobjects: IntArray? = IntArray(18)
@JvmField var Mnodes: IntArray? = IntArray(5)
@JvmField var Mlinks: IntArray? = IntArray(5)
@JvmField var Mevents: Int = 0

// input.c:69
fun input_countObjects(): Int {
    var line: CPtr? = R.buf(1025)
    var wLine: CPtr? = R.buf(1025)
    var tok: CPtr? = null
    var sect: Int = (-1)
    var newsect: Int = 0
    var errcode: Int = 0
    var errsum: Int = 0
    var i: Int = 0
    var lineCount: Long = 0L
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    error_setInpError(0, __L0)
    i = 0
    f1@ while ((i < MAX_OBJ_TYPES)) {
        Nobjects!![i] = 0
        i++
    }
    i = 0
    f2@ while ((i < 5)) {
        Nnodes!![i] = 0
        i++
    }
    i = 0
    f3@ while ((i < 5)) {
        Nlinks!![i] = 0
        i++
    }
    controls_init()
    w4@ while (((R.fgets(line, 1024, Finp!!.file)) != null)) {
        lineCount++
        sstrncpy(wLine, line, 1024L)
        tok = R.strtok(wLine, __L184)
        if ((tok == null)) {
            continue@w4
        }
        if ((tok!!.g(0) == 59)) {
            continue@w4
        }
        if ((tok!!.g(0) == 91)) {
            newsect = findmatch(tok, SectWords)
            if ((newsect >= 0)) {
                sect = newsect
                continue@w4
            } else {
                sect = (-1)
                errcode = ERR_KEYWORD
            }
        }
        if ((sect == s_OPTION)) {
            errcode = readOption(line)
        } else if ((sect >= 0)) {
            errcode = addObject(sect, tok)
        }
        if ((errcode != 0)) {
            report_writeInputErrorMsg(errcode, sect, line, lineCount)
            errsum++
            if ((errsum >= MAXERRS)) {
                break@w4
            }
        }
    }
    if ((errsum > 0)) {
        ErrorCode = ERR_INPUT
    }
    return ErrorCode
}

// input.c:141
fun input_readData(): Int {
    var line: CPtr? = R.buf(1025)
    var wLine: CPtr? = R.buf(1025)
    var comment: CPtr? = null
    var sect: Int = 0
    var newsect: Int = 0
    var inperr: Int = 0
    var errsum: Int = 0
    var lineLength: Int = 0
    var i: Int = 0
    var lineCount: Long = 0L
    if ((ErrorCode != 0)) {
        return ErrorCode
    }
    error_setInpError(0, __L0)
    i = 0
    f1@ while ((i < MAX_OBJ_TYPES)) {
        Mobjects!![i] = 0
        i++
    }
    i = 0
    f2@ while ((i < 5)) {
        Mnodes!![i] = 0
        i++
    }
    i = 0
    f3@ while ((i < 5)) {
        Mlinks!![i] = 0
        i++
    }
    Mevents = 0
    i = 0
    f4@ while ((i < Nobjects!![TSERIES])) {
        Tseries!![i]!!.lastDate = (StartDate.v + StartTime.v)
        i++
    }
    sect = 0
    errsum = 0
    R.rewind(Finp!!.file)
    w5@ while (((R.fgets(line, 1024, Finp!!.file)) != null)) {
        lineCount++
        sstrncpy(wLine, line, 1024L)
        Ntokens = getTokens(wLine)
        if ((Ntokens == 0)) {
            continue@w5
        }
        if ((Tok!![0]!!.g(0) == 59)) {
            continue@w5
        }
        lineLength = (R.strlen(line)).toInt()
        if ((lineLength >= 1024)) {
            comment = R.strchr(line, 59)
            if ((comment != null)) {
                lineLength = (((comment!!.o - line!!.o).toLong())).toInt()
            }
            if ((lineLength >= 1024)) {
                inperr = ERR_LINE_LENGTH
                report_writeInputErrorMsg(inperr, sect, line, lineCount)
                errsum++
            }
        }
        if ((Tok!![0]!!.g(0) == 91)) {
            newsect = findmatch(Tok!![0], SectWords)
            if ((newsect >= 0)) {
                if ((sect == s_TRANSECT)) {
                    transect_validate((Nobjects!![TRANSECT] - 1))
                }
                sect = newsect
                continue@w5
            } else {
                inperr = error_setInpError(ERR_KEYWORD, Tok!![0])
                report_writeInputErrorMsg(inperr, sect, line, lineCount)
                errsum++
                break@w5
            }
        } else {
            inperr = parseLine(sect, line)
            if ((inperr > 0)) {
                errsum++
                if ((errsum > MAXERRS)) {
                    report_writeLine(__L201)
                } else {
                    report_writeInputErrorMsg(inperr, sect, line, lineCount)
                }
            }
        }
        if ((errsum > MAXERRS)) {
            break@w5
        }
    }
    if ((errsum > 0)) {
        ErrorCode = ERR_INPUT
    }
    return ErrorCode
}

// input.c:251
fun addObject(objType: Int, __id: CPtr?): Int {
    var id: CPtr? = __id
    var errcode: Int = 0
    when (objType) {
        s_RAINGAGE -> {
            if (!(project_addObject(GAGE, id, Nobjects!![GAGE]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![GAGE]++
        }
        s_SUBCATCH -> {
            if (!(project_addObject(SUBCATCH, id, Nobjects!![SUBCATCH]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![SUBCATCH]++
        }
        s_AQUIFER -> {
            if (!(project_addObject(AQUIFER, id, Nobjects!![AQUIFER]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![AQUIFER]++
        }
        s_UNITHYD -> {
            if ((project_findObject(UNITHYD, id) < 0)) {
                if (!(project_addObject(UNITHYD, id, Nobjects!![UNITHYD]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![UNITHYD]++
            }
        }
        s_SNOWMELT -> {
            if ((project_findObject(SNOWMELT, id) < 0)) {
                if (!(project_addObject(SNOWMELT, id, Nobjects!![SNOWMELT]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![SNOWMELT]++
            }
        }
        s_JUNCTION -> {
            if (!(project_addObject(NODE, id, Nobjects!![NODE]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![NODE]++
            Nnodes!![JUNCTION]++
        }
        s_OUTFALL -> {
            if (!(project_addObject(NODE, id, Nobjects!![NODE]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![NODE]++
            Nnodes!![OUTFALL]++
        }
        s_STORAGE -> {
            if (!(project_addObject(NODE, id, Nobjects!![NODE]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![NODE]++
            Nnodes!![STORAGE]++
        }
        s_DIVIDER -> {
            if (!(project_addObject(NODE, id, Nobjects!![NODE]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![NODE]++
            Nnodes!![DIVIDER]++
        }
        s_CONDUIT -> {
            if (!(project_addObject(LINK, id, Nobjects!![LINK]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![LINK]++
            Nlinks!![CONDUIT]++
        }
        s_PUMP -> {
            if (!(project_addObject(LINK, id, Nobjects!![LINK]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![LINK]++
            Nlinks!![PUMP]++
        }
        s_ORIFICE -> {
            if (!(project_addObject(LINK, id, Nobjects!![LINK]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![LINK]++
            Nlinks!![ORIFICE]++
        }
        s_WEIR -> {
            if (!(project_addObject(LINK, id, Nobjects!![LINK]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![LINK]++
            Nlinks!![WEIR]++
        }
        s_OUTLET -> {
            if (!(project_addObject(LINK, id, Nobjects!![LINK]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![LINK]++
            Nlinks!![OUTLET]++
        }
        s_POLLUTANT -> {
            if (!(project_addObject(POLLUT, id, Nobjects!![POLLUT]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![POLLUT]++
        }
        s_LANDUSE -> {
            if (!(project_addObject(LANDUSE, id, Nobjects!![LANDUSE]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![LANDUSE]++
        }
        s_PATTERN -> {
            if ((project_findObject(TIMEPATTERN, id) < 0)) {
                if (!(project_addObject(TIMEPATTERN, id, Nobjects!![TIMEPATTERN]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![TIMEPATTERN]++
            }
        }
        s_CURVE -> {
            if ((project_findObject(CURVE, id) < 0)) {
                if (!(project_addObject(CURVE, id, Nobjects!![CURVE]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![CURVE]++
                id = R.strtok(null, __L184)
                if ((findmatch(id, CurveTypeWords) == SHAPE_CURVE)) {
                    Nobjects!![SHAPE]++
                }
            }
        }
        s_TIMESERIES -> {
            if ((project_findObject(TSERIES, id) < 0)) {
                if (!(project_addObject(TSERIES, id, Nobjects!![TSERIES]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![TSERIES]++
            }
        }
        s_CONTROL -> {
            if ((match(id, __L202) != 0)) {
                Nobjects!![CONTROL]++
            } else {
                controls_addToCount(id)
            }
        }
        s_TRANSECT -> {
            if ((match(id, __L203) != 0)) {
                id = R.strtok(null, __L184)
                if ((id != null)) {
                    if (!(project_addObject(TRANSECT, id, Nobjects!![TRANSECT]) != 0)) {
                        errcode = error_setInpError(ERR_DUP_NAME, id)
                    }
                    Nobjects!![TRANSECT]++
                }
            }
        }
        s_LID_CONTROL -> {
            if ((project_findObject(LID, id) < 0)) {
                if (!(project_addObject(LID, id, Nobjects!![LID]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![LID]++
            }
        }
        s_EVENT -> {
            NumEvents++
        }
        s_STREET -> {
            if (!(project_addObject(STREET, id, Nobjects!![STREET]) != 0)) {
                errcode = error_setInpError(ERR_DUP_NAME, id)
            }
            Nobjects!![STREET]++
        }
        s_INLET -> {
            if ((project_findObject(INLET, id) < 0)) {
                if (!(project_addObject(INLET, id, Nobjects!![INLET]) != 0)) {
                    errcode = error_setInpError(ERR_DUP_NAME, id)
                }
                Nobjects!![INLET]++
            }
        }
    }
    return errcode
}

// input.c:464
fun parseLine(sect: Int, line: CPtr?): Int {
    var j: Int = 0
    var err: Int = 0
    when (sect) {
        s_TITLE -> {
            return readTitle(line)
        }
        s_RAINGAGE -> {
            j = Mobjects!![GAGE]
            err = gage_readParams(j, Tok, Ntokens)
            Mobjects!![GAGE]++
            return err
        }
        s_TEMP -> {
            return climate_readParams(Tok, Ntokens)
        }
        s_EVAP -> {
            return climate_readEvapParams(Tok, Ntokens)
        }
        s_ADJUST -> {
            return climate_readAdjustments(Tok, Ntokens)
        }
        s_SUBCATCH -> {
            j = Mobjects!![SUBCATCH]
            err = subcatch_readParams(j, Tok, Ntokens)
            Mobjects!![SUBCATCH]++
            return err
        }
        s_SUBAREA -> {
            return subcatch_readSubareaParams(Tok, Ntokens)
        }
        s_INFIL -> {
            return infil_readParams(InfilModel, Tok, Ntokens)
        }
        s_AQUIFER -> {
            j = Mobjects!![AQUIFER]
            err = gwater_readAquiferParams(j, Tok, Ntokens)
            Mobjects!![AQUIFER]++
            return err
        }
        s_GROUNDWATER -> {
            return gwater_readGroundwaterParams(Tok, Ntokens)
        }
        s_GWF -> {
            return gwater_readFlowExpression(Tok, Ntokens)
        }
        s_SNOWMELT -> {
            return snow_readMeltParams(Tok, Ntokens)
        }
        s_JUNCTION -> {
            return readNode(JUNCTION)
        }
        s_OUTFALL -> {
            return readNode(OUTFALL)
        }
        s_STORAGE -> {
            return readNode(STORAGE)
        }
        s_DIVIDER -> {
            return readNode(DIVIDER)
        }
        s_CONDUIT -> {
            return readLink(CONDUIT)
        }
        s_PUMP -> {
            return readLink(PUMP)
        }
        s_ORIFICE -> {
            return readLink(ORIFICE)
        }
        s_WEIR -> {
            return readLink(WEIR)
        }
        s_OUTLET -> {
            return readLink(OUTLET)
        }
        s_XSECTION -> {
            return link_readXsectParams(Tok, Ntokens)
        }
        s_TRANSECT -> {
            return transect_readParams(IElem(Mobjects!!, TRANSECT), Tok, Ntokens)
        }
        s_LOSSES -> {
            return link_readLossParams(Tok, Ntokens)
        }
        s_POLLUTANT -> {
            j = Mobjects!![POLLUT]
            err = landuse_readPollutParams(j, Tok, Ntokens)
            Mobjects!![POLLUT]++
            return err
        }
        s_LANDUSE -> {
            j = Mobjects!![LANDUSE]
            err = landuse_readParams(j, Tok, Ntokens)
            Mobjects!![LANDUSE]++
            return err
        }
        s_BUILDUP -> {
            return landuse_readBuildupParams(Tok, Ntokens)
        }
        s_WASHOFF -> {
            return landuse_readWashoffParams(Tok, Ntokens)
        }
        s_COVERAGE -> {
            return subcatch_readLanduseParams(Tok, Ntokens)
        }
        s_INFLOW -> {
            return inflow_readExtInflow(Tok, Ntokens)
        }
        s_DWF -> {
            return inflow_readDwfInflow(Tok, Ntokens)
        }
        s_PATTERN -> {
            return inflow_readDwfPattern(Tok, Ntokens)
        }
        s_RDII -> {
            return rdii_readRdiiInflow(Tok, Ntokens)
        }
        s_UNITHYD -> {
            return rdii_readUnitHydParams(Tok, Ntokens)
        }
        s_LOADING -> {
            return subcatch_readInitBuildup(Tok, Ntokens)
        }
        s_TREATMENT -> {
            return treatmnt_readExpression(Tok, Ntokens)
        }
        s_CURVE -> {
            return table_readCurve(Tok, Ntokens)
        }
        s_TIMESERIES -> {
            return table_readTimeseries(Tok, Ntokens)
        }
        s_CONTROL -> {
            return readControl(Tok, Ntokens)
        }
        s_REPORT -> {
            return report_readOptions(Tok, Ntokens)
        }
        s_FILE -> {
            return iface_readFileParams(Tok, Ntokens)
        }
        s_LID_CONTROL -> {
            return lid_readProcParams(Tok, Ntokens)
        }
        s_LID_USAGE -> {
            return lid_readGroupParams(Tok, Ntokens)
        }
        s_EVENT -> {
            return readEvent(Tok, Ntokens)
        }
        s_STREET -> {
            return street_readParams(Tok, Ntokens)
        }
        s_INLET -> {
            return inlet_readDesignParams(Tok, Ntokens)
        }
        s_INLET_USAGE -> {
            return inlet_readUsageParams(Tok, Ntokens)
        }
        else -> {
            return 0
        }
    }
    throw IllegalStateException("parseLine: no return")
}

// input.c:637
fun readControl(tok: Array<CPtr?>?, ntoks: Int): Int {
    var index: Int = 0
    var keyword: Int = 0
    if ((ntoks < 2)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if ((match(tok!![0], __L29) != 0)) {
        return controls_addVariable(tok, ntoks)
    }
    if ((match(tok!![0], __L30) != 0)) {
        return controls_addExpression(tok, ntoks)
    }
    keyword = findmatch(tok!![0], RuleKeyWords)
    if ((keyword < 0)) {
        return error_setInpError(ERR_KEYWORD, tok!![0])
    }
    if ((keyword == 0)) {
        if (!(project_addObject(CONTROL, tok!![1], Mobjects!![CONTROL]) != 0)) {
            return error_setInpError(ERR_DUP_NAME, Tok!![1])
        }
        Mobjects!![CONTROL]++
    }
    index = (Mobjects!![CONTROL] - 1)
    if ((index < 0)) {
        return error_setInpError(ERR_RULE, __L0)
    }
    return controls_addRuleClause(index, keyword, Tok, Ntokens)
}

// input.c:680
fun readOption(line: CPtr?): Int {
    Ntokens = getTokens(line)
    if ((Ntokens < 2)) {
        return 0
    }
    return project_readOption(Tok!![0], Tok!![1])
}

// input.c:694
fun readTitle(line: CPtr?): Int {
    var i: Int = 0
    var n: Int = 0
    i = 0
    f1@ while ((i < 3)) {
        if ((R.strlen(Title!![i]) == 0L)) {
            n = (R.strlen(line)).toInt()
            if ((line!!.g((n - 1)) == 10)) {
                line!!.s((n - 1), 32)
            }
            sstrncpy(Title!![i], line, 1024L)
            break@f1
        }
        i++
    }
    return 0
}

// input.c:721
fun readNode(type: Int): Int {
    var j: Int = Mobjects!![NODE]
    var k: Int = Mnodes!![type]
    var err: Int = node_readParams(j, type, k, Tok, Ntokens)
    Mobjects!![NODE]++
    Mnodes!![type]++
    return err
}

// input.c:738
fun readLink(type: Int): Int {
    var j: Int = Mobjects!![LINK]
    var k: Int = Mlinks!![type]
    var err: Int = link_readParams(j, type, k, Tok, Ntokens)
    Mobjects!![LINK]++
    Mlinks!![type]++
    return err
}

// input.c:755
fun readEvent(tok: Array<CPtr?>?, ntoks: Int): Int {
    var x: DoubleArray? = DoubleArray(4)
    if ((ntoks < 4)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    if (!(datetime_strToDate(tok!![0], DElem(x!!, 0)) != 0)) {
        return error_setInpError(ERR_DATETIME, tok!![0])
    }
    if (!(datetime_strToTime(tok!![1], DElem(x!!, 1)) != 0)) {
        return error_setInpError(ERR_DATETIME, tok!![1])
    }
    if (!(datetime_strToDate(tok!![2], DElem(x!!, 2)) != 0)) {
        return error_setInpError(ERR_DATETIME, tok!![2])
    }
    if (!(datetime_strToTime(tok!![3], DElem(x!!, 3)) != 0)) {
        return error_setInpError(ERR_DATETIME, tok!![3])
    }
    Event!![Mevents]!!.start = (x!![0] + x!![1])
    Event!![Mevents]!!.end = (x!![2] + x!![3])
    if ((Event!![Mevents]!!.start >= Event!![Mevents]!!.end)) {
        return error_setInpError(ERR_DATETIME, __L204)
    }
    Mevents++
    return 0
}

// input.c:779
fun findmatch(s: CPtr?, keyword: Array<CPtr?>?): Int {
    var i: Int = 0
    w1@ while ((keyword!![i] != null)) {
        if ((match(s, keyword!![i]) != 0)) {
            return (i)
        }
        i++
    }
    return ((-1))
}

// input.c:798
fun match(str: CPtr?, substr: CPtr?): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    if (!(substr!!.g(0) != 0)) {
        return (0)
    }
    k = 0
    f1@ while ((str!!.g(k) != 0)) {
        if ((str!!.g(k) != 32)) {
            break@f1
        }
        k++
    }
    i = k; j = 0
    f2@ while ((substr!!.g(j) != 0)) {
        if (((!(str!!.g(i) != 0)) || (((if (((((str!!.g(i)) >= 97) && ((str!!.g(i)) <= 122)))) (((str!!.g(i)) and 32.inv())) else (str!!.g(i)))) != ((if (((((substr!!.g(j)) >= 97) && ((substr!!.g(j)) <= 122)))) (((substr!!.g(j)) and 32.inv())) else (substr!!.g(j))))))) {
            return (0)
        }
        i++; j++
    }
    return (1)
}

// input.c:828
fun getInt(s: CPtr?, y: IRef?): Int {
    val x = DBox(0.0)
    if ((getDouble(s, x) != 0)) {
        if ((x.v < 0.0)) {
            x.v -= 0.01
        } else {
            x.v += 0.01
        }
        y!!.set(R.D2I(x.v))
        return 1
    }
    y!!.set(0)
    return 0
}

// input.c:850
fun getFloat(s: CPtr?, y: FRef?): Int {
    val endptr = OBox<CPtr?>(null)
    y!!.set((R.strtod(s, endptr)).toFloat())
    if ((endptr.v!!.g(0) > 0)) {
        return (0)
    }
    return (1)
}

// input.c:866
fun getDouble(s: CPtr?, y: DRef?): Int {
    val endptr = OBox<CPtr?>(null)
    y!!.set(R.strtod(s, endptr))
    if ((endptr.v!!.g(0) > 0)) {
        return (0)
    }
    return (1)
}

// input.c:882
fun getTokens(__s: CPtr?): Int {
    var s: CPtr? = __s
    var len: Int = 0
    var n: Int = 0
    var m: Int = 0
    var c: CPtr? = null
    n = 0
    f1@ while ((n < 40)) {
        Tok!![n] = null
        n++
    }
    n = 0
    c = R.strchr(s, 59)
    if ((c != null)) {
        c!!.s(0, 0)
    }
    len = (R.strlen(s)).toInt()
    w2@ while (((len > 0) && (n < 40))) {
        m = (R.strcspn(s, __L184)).toInt()
        if ((m == 0)) {
            s = s!!.add(1)
        } else {
            if ((s!!.g(0) == 34)) {
                s = s!!.add(1)
                len--
                m = (R.strcspn(s, __L205)).toInt()
            }
            s!!.s(m, 0)
            Tok!![n] = s
            n++
            s = s!!.add((m + 1))
        }
        len -= (m + 1)
    }
    return n
}

