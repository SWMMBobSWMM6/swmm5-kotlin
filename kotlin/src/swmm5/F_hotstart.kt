// Generated from EPA SWMM 5.2.4 (hotstart.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_hotstart")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var fileVersion: Int = 0

// hotstart.c:72
fun hotstart_open(): Int {
    if (!(openHotstartFile1() != 0)) {
        return 0
    }
    if (!(openHotstartFile2() != 0)) {
        return 0
    }
    return 1
}

// hotstart.c:82
fun hotstart_close() {
    if ((Fhotstart2!!.file != null)) {
        saveRunoff()
        saveRouting()
        R.fclose(Fhotstart2!!.file)
    }
}

// hotstart.c:94
fun openHotstartFile1(): Int {
    val nSubcatch = IBox(0)
    val nNodes = IBox(0)
    val nLinks = IBox(0)
    val nPollut = IBox(0)
    val nLandUses = IBox(0)
    val flowUnits = IBox(0)
    var fStamp: CPtr? = R.bufFrom(__L160, 15)
    var fileStamp: CPtr? = R.bufFrom(__L160, 15)
    var fStampx: CPtr? = R.bufFrom(__L161, 16)
    var fileStamp2: CPtr? = R.bufFrom(__L162, 16)
    var fileStamp3: CPtr? = R.bufFrom(__L163, 16)
    var fileStamp4: CPtr? = R.bufFrom(__L164, 16)
    if ((Fhotstart1!!.mode != USE_FILE)) {
        return 1
    }
    if ((((R.fopen(Fhotstart1!!.name, __L165)).also { __v -> Fhotstart1!!.file = __v }) == null)) {
        report_writeErrorMsg(ERR_HOTSTART_FILE_OPEN, Fhotstart1!!.name)
        return 0
    }
    R.fread(fStampx, 0, (R.strlen(fileStamp2)).toInt(), Fhotstart1!!.file)
    if ((R.strcmp(fStampx, fileStamp4) == 0)) {
        fileVersion = 4
    } else if ((R.strcmp(fStampx, fileStamp3) == 0)) {
        fileVersion = 3
    } else if ((R.strcmp(fStampx, fileStamp2) == 0)) {
        fileVersion = 2
    } else {
        R.rewind(Fhotstart1!!.file)
        R.fread(fStamp, 0, (R.strlen(fileStamp)).toInt(), Fhotstart1!!.file)
        if ((R.strcmp(fStamp, fileStamp) != 0)) {
            report_writeErrorMsg(ERR_HOTSTART_FILE_FORMAT, __L0)
            return 0
        }
        fileVersion = 1
    }
    nSubcatch.v = (-1)
    nNodes.v = (-1)
    nLinks.v = (-1)
    nPollut.v = (-1)
    nLandUses.v = (-1)
    flowUnits.v = (-1)
    if ((fileVersion >= 2)) {
        R.fread1(nSubcatch, 4, Fhotstart1!!.file)
    } else {
        nSubcatch.v = Nobjects!![SUBCATCH]
    }
    if ((fileVersion >= 3)) {
        R.fread1(nLandUses, 4, Fhotstart1!!.file)
    } else {
        nLandUses.v = Nobjects!![LANDUSE]
    }
    R.fread1(nNodes, 4, Fhotstart1!!.file)
    R.fread1(nLinks, 4, Fhotstart1!!.file)
    R.fread1(nPollut, 4, Fhotstart1!!.file)
    R.fread1(flowUnits, 4, Fhotstart1!!.file)
    if (((((((nSubcatch.v != Nobjects!![SUBCATCH]) || (nLandUses.v != Nobjects!![LANDUSE])) || (nNodes.v != Nobjects!![NODE])) || (nLinks.v != Nobjects!![LINK])) || (nPollut.v != Nobjects!![POLLUT])) || (flowUnits.v != FlowUnits))) {
        report_writeErrorMsg(ERR_HOTSTART_FILE_FORMAT, __L0)
        return 0
    }
    if ((fileVersion >= 3)) {
        readRunoff()
    }
    readRouting()
    R.fclose(Fhotstart1!!.file)
    if ((ErrorCode != 0)) {
        return 0
    } else {
        return 1
    }
    throw IllegalStateException("openHotstartFile1: no return")
}

// hotstart.c:180
fun openHotstartFile2(): Int {
    val nSubcatch = IBox(0)
    val nLandUses = IBox(0)
    val nNodes = IBox(0)
    val nLinks = IBox(0)
    val nPollut = IBox(0)
    val flowUnits = IBox(0)
    var fileStamp: CPtr? = R.bufFrom(__L164, 16)
    if ((Fhotstart2!!.mode != SAVE_FILE)) {
        return 1
    }
    if ((((R.fopen(Fhotstart2!!.name, __L166)).also { __v -> Fhotstart2!!.file = __v }) == null)) {
        report_writeErrorMsg(ERR_HOTSTART_FILE_OPEN, Fhotstart2!!.name)
        return 0
    }
    nSubcatch.v = Nobjects!![SUBCATCH]
    nLandUses.v = Nobjects!![LANDUSE]
    nNodes.v = Nobjects!![NODE]
    nLinks.v = Nobjects!![LINK]
    nPollut.v = Nobjects!![POLLUT]
    flowUnits.v = FlowUnits
    R.fwrite(fileStamp, 0, (R.strlen(fileStamp)).toInt(), Fhotstart2!!.file)
    R.fwrite1(nSubcatch.v, 4, Fhotstart2!!.file)
    R.fwrite1(nLandUses.v, 4, Fhotstart2!!.file)
    R.fwrite1(nNodes.v, 4, Fhotstart2!!.file)
    R.fwrite1(nLinks.v, 4, Fhotstart2!!.file)
    R.fwrite1(nPollut.v, 4, Fhotstart2!!.file)
    R.fwrite1(flowUnits.v, 4, Fhotstart2!!.file)
    return 1
}

// hotstart.c:222
fun saveRouting() {
    var i: Int = 0
    var j: Int = 0
    var x: FloatArray? = FloatArray(3)
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        x!![0] = (Node!![i]!!.newDepth).toFloat()
        x!![1] = (Node!![i]!!.newLatFlow).toFloat()
        R.fwrite(x, 0, (2L).toInt(), Fhotstart2!!.file)
        if ((Node!![i]!!.type == STORAGE)) {
            j = Node!![i]!!.subIndex
            x!![0] = (Storage!![j]!!.hrt).toFloat()
            R.fwrite(x, 0, (1L).toInt(), Fhotstart2!!.file)
        }
        j = 0
        f2@ while ((j < Nobjects!![POLLUT])) {
            x!![0] = (Node!![i]!!.newQual!![j]).toFloat()
            R.fwrite(x, 0, (1L).toInt(), Fhotstart2!!.file)
            j++
        }
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![LINK])) {
        x!![0] = (Link!![i]!!.newFlow).toFloat()
        x!![1] = (Link!![i]!!.newDepth).toFloat()
        x!![2] = (Link!![i]!!.setting).toFloat()
        R.fwrite(x, 0, (3L).toInt(), Fhotstart2!!.file)
        j = 0
        f4@ while ((j < Nobjects!![POLLUT])) {
            x!![0] = (Link!![i]!!.newQual!![j]).toFloat()
            R.fwrite(x, 0, (1L).toInt(), Fhotstart2!!.file)
            j++
        }
        i++
    }
}

// hotstart.c:267
fun readRouting() {
    var i: Int = 0
    var j: Int = 0
    val x = FBox(0.0f)
    var xgw: DoubleArray? = DoubleArray(4)
    var f: CFile? = Fhotstart1!!.file
    if ((fileVersion == 2)) {
        xgw!![2] = 0.0
        xgw!![3] = (-10000000000.0)
        i = 0
        f1@ while ((i < Nobjects!![SUBCATCH])) {
            if (!(readFloat(x, f) != 0)) {
                return
            }
            xgw!![0] = (x.v).toDouble()
            if (!(readFloat(x, f) != 0)) {
                return
            }
            xgw!![1] = (x.v).toDouble()
            if ((Subcatch!![i]!!.groundwater != null)) {
                gwater_setState(i, xgw)
            }
            i++
        }
    }
    i = 0
    f2@ while ((i < Nobjects!![NODE])) {
        if (!(readFloat(x, f) != 0)) {
            return
        }
        Node!![i]!!.newDepth = (x.v).toDouble()
        if (!(readFloat(x, f) != 0)) {
            return
        }
        Node!![i]!!.newLatFlow = (x.v).toDouble()
        if (((fileVersion >= 4) && (Node!![i]!!.type == STORAGE))) {
            if (!(readFloat(x, f) != 0)) {
                return
            }
            j = Node!![i]!!.subIndex
            Storage!![j]!!.hrt = (x.v).toDouble()
        }
        j = 0
        f3@ while ((j < Nobjects!![POLLUT])) {
            if (!(readFloat(x, f) != 0)) {
                return
            }
            Node!![i]!!.newQual!![j] = (x.v).toDouble()
            j++
        }
        if ((fileVersion <= 2)) {
            j = 0
            f4@ while ((j < Nobjects!![POLLUT])) {
                if (!(readFloat(x, f) != 0)) {
                    return
                }
                j++
            }
        }
        i++
    }
    i = 0
    f5@ while ((i < Nobjects!![LINK])) {
        if (!(readFloat(x, f) != 0)) {
            return
        }
        Link!![i]!!.newFlow = (x.v).toDouble()
        if (!(readFloat(x, f) != 0)) {
            return
        }
        Link!![i]!!.newDepth = (x.v).toDouble()
        if (!(readFloat(x, f) != 0)) {
            return
        }
        Link!![i]!!.setting = (x.v).toDouble()
        Link!![i]!!.targetSetting = (x.v).toDouble()
        link_setTargetSetting(i)
        link_setSetting(i, 0.0)
        j = 0
        f6@ while ((j < Nobjects!![POLLUT])) {
            if (!(readFloat(x, f) != 0)) {
                return
            }
            Link!![i]!!.newQual!![j] = (x.v).toDouble()
            j++
        }
        i++
    }
}

// hotstart.c:356
fun saveRunoff() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    var f: CFile? = Fhotstart2!!.file
    i = 0
    f1@ while ((i < Nobjects!![SUBCATCH])) {
        j = 0
        f2@ while ((j < 3)) {
            x!![j] = Subcatch!![i]!!.subArea!![j]!!.depth
            j++
        }
        x!![3] = Subcatch!![i]!!.newRunoff
        R.fwrite(x, 0, (4L).toInt(), f)
        j = 0
        f3@ while ((j < 6)) {
            x!![j] = 0.0
            j++
        }
        infil_getState(i, x)
        R.fwrite(x, 0, (6L).toInt(), f)
        if ((Subcatch!![i]!!.groundwater != null)) {
            gwater_getState(i, x)
            R.fwrite(x, 0, (4L).toInt(), f)
        }
        if ((Subcatch!![i]!!.snowpack != null)) {
            j = 0
            f4@ while ((j < 3)) {
                snow_getState(i, j, x)
                R.fwrite(x, 0, (5L).toInt(), f)
                j++
            }
        }
        if ((Nobjects!![POLLUT] > 0)) {
            j = 0
            f5@ while ((j < Nobjects!![POLLUT])) {
                x!![0] = Subcatch!![i]!!.newQual!![j]
                R.fwrite(x, 0, (1L).toInt(), f)
                j++
            }
            j = 0
            f6@ while ((j < Nobjects!![POLLUT])) {
                x!![0] = Subcatch!![i]!!.pondedQual!![j]
                R.fwrite(x, 0, (1L).toInt(), f)
                j++
            }
            k = 0
            f7@ while ((k < Nobjects!![LANDUSE])) {
                j = 0
                f8@ while ((j < Nobjects!![POLLUT])) {
                    x!![0] = Subcatch!![i]!!.landFactor!![k]!!.buildup!![j]
                    R.fwrite(x, 0, ((Nobjects!![POLLUT]).toLong()).toInt(), f)
                    j++
                }
                x!![0] = Subcatch!![i]!!.landFactor!![k]!!.lastSwept
                R.fwrite(x, 0, (1L).toInt(), f)
                k++
            }
        }
        i++
    }
}

// hotstart.c:430
fun readRunoff() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var x: DoubleArray? = DoubleArray(6)
    var f: CFile? = Fhotstart1!!.file
    i = 0
    f1@ while ((i < Nobjects!![SUBCATCH])) {
        j = 0
        f2@ while ((j < 3)) {
            if (!(readDouble(DField<TSubarea>(Subcatch!![i]!!.subArea!![j]!!, { __o -> __o.depth }, { __o, __x -> __o.depth = __x }), f) != 0)) {
                return
            }
            j++
        }
        if (!(readDouble(DField<TSubcatch>(Subcatch!![i]!!, { __o -> __o.newRunoff }, { __o, __x -> __o.newRunoff = __x }), f) != 0)) {
            return
        }
        j = 0
        f3@ while ((j < 6)) {
            if (!(readDouble(DElem(x!!, j), f) != 0)) {
                return
            }
            j++
        }
        infil_setState(i, x)
        if ((Subcatch!![i]!!.groundwater != null)) {
            j = 0
            f4@ while ((j < 4)) {
                if (!(readDouble(DElem(x!!, j), f) != 0)) {
                    return
                }
                j++
            }
            gwater_setState(i, x)
        }
        if ((Subcatch!![i]!!.snowpack != null)) {
            j = 0
            f5@ while ((j < 3)) {
                k = 0
                f6@ while ((k < 5)) {
                    if (!(readDouble(DElem(x!!, k), f) != 0)) {
                        return
                    }
                    k++
                }
                snow_setState(i, j, x)
                j++
            }
        }
        if ((Nobjects!![POLLUT] > 0)) {
            j = 0
            f7@ while ((j < Nobjects!![POLLUT])) {
                if (!(readDouble(DElem(Subcatch!![i]!!.newQual!!, j), f) != 0)) {
                    return
                }
                j++
            }
            j = 0
            f8@ while ((j < Nobjects!![POLLUT])) {
                if (!(readDouble(DElem(Subcatch!![i]!!.pondedQual!!, j), f) != 0)) {
                    return
                }
                j++
            }
            k = 0
            f9@ while ((k < Nobjects!![LANDUSE])) {
                j = 0
                f10@ while ((j < Nobjects!![POLLUT])) {
                    if (!(readDouble(DElem(Subcatch!![i]!!.landFactor!![k]!!.buildup!!, j), f) != 0)) {
                        return
                    }
                    j++
                }
                if (!(readDouble(DField<TLandFactor>(Subcatch!![i]!!.landFactor!![k]!!, { __o -> __o.lastSwept }, { __o, __x -> __o.lastSwept = __x }), f) != 0)) {
                    return
                }
                k++
            }
        }
        i++
    }
}

// hotstart.c:499
fun readFloat(x: FRef?, f: CFile?): Int {
    R.fread1(x, 4, f)
    if (((x)!!.get() != (x)!!.get())) {
        report_writeErrorMsg(ERR_HOTSTART_FILE_READ, __L0)
        (x)!!.set((0.0).toFloat())
        return 0
    }
    return 1
}

// hotstart.c:521
fun readDouble(x: DRef?, f: CFile?): Int {
    if ((R.feof(f) != 0)) {
        (x)!!.set(0.0)
        report_writeErrorMsg(ERR_HOTSTART_FILE_READ, __L0)
        return 0
    }
    R.fread1(x, 8, f)
    if (((x)!!.get() != (x)!!.get())) {
        (x)!!.set(0.0)
        return 0
    }
    return 1
}

