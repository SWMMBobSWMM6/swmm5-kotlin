// Generated from EPA SWMM 5.2.4 (inputrpt.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_inputrpt")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// inputrpt.c:27
fun inputrpt_writeInput() {
    var m: Int = 0
    var i: Int = 0
    var k: Int = 0
    var lidCount: Int = 0
    if ((ErrorCode != 0)) {
        return
    }
    report_writeLine((__L0))
    report_writeLine((__L206))
    report_writeLine((__L207))
    report_writeLine((__L206))
    R.fprintf(Frpt!!.file, __L208, Nobjects!![GAGE])
    R.fprintf(Frpt!!.file, __L209, Nobjects!![SUBCATCH])
    R.fprintf(Frpt!!.file, __L210, Nobjects!![NODE])
    R.fprintf(Frpt!!.file, __L211, Nobjects!![LINK])
    R.fprintf(Frpt!!.file, __L212, Nobjects!![POLLUT])
    R.fprintf(Frpt!!.file, __L213, Nobjects!![LANDUSE])
    if ((Nobjects!![POLLUT] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L214))
        report_writeLine((__L215))
        report_writeLine((__L214))
        R.fprintf(Frpt!!.file, __L216)
        R.fprintf(Frpt!!.file, __L217)
        R.fprintf(Frpt!!.file, __L218)
        i = 0
        f1@ while ((i < Nobjects!![POLLUT])) {
            R.fprintf(Frpt!!.file, __L219, Pollut!![i]!!.ID, QualUnitsWords!![Pollut!![i]!!.units], Pollut!![i]!!.pptConcen, Pollut!![i]!!.gwConcen, (Pollut!![i]!!.kDecay * 86400.0))
            if ((Pollut!![i]!!.coPollut >= 0)) {
                R.fprintf(Frpt!!.file, __L220, Pollut!![Pollut!![i]!!.coPollut]!!.ID, Pollut!![i]!!.coFraction)
            }
            i++
        }
    }
    if ((Nobjects!![LANDUSE] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L221))
        report_writeLine((__L222))
        report_writeLine((__L221))
        R.fprintf(Frpt!!.file, __L223)
        R.fprintf(Frpt!!.file, __L224)
        R.fprintf(Frpt!!.file, __L225)
        i = 0
        f2@ while ((i < Nobjects!![LANDUSE])) {
            R.fprintf(Frpt!!.file, __L226, Landuse!![i]!!.ID, Landuse!![i]!!.sweepInterval, Landuse!![i]!!.sweepRemoval, Landuse!![i]!!.sweepDays0)
            i++
        }
    }
    if ((Nobjects!![GAGE] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L227))
        report_writeLine((__L228))
        report_writeLine((__L227))
        R.fprintf(Frpt!!.file, __L229)
        R.fprintf(Frpt!!.file, __L230)
        R.fprintf(Frpt!!.file, __L231)
        i = 0
        f3@ while ((i < Nobjects!![GAGE])) {
            if ((Gage!![i]!!.tSeries >= 0)) {
                R.fprintf(Frpt!!.file, __L232, Gage!![i]!!.ID, Tseries!![Gage!![i]!!.tSeries]!!.ID)
                R.fprintf(Frpt!!.file, __L233, RainTypeWords!![Gage!![i]!!.rainType], ((Gage!![i]!!.rainInterval) / 60))
            } else {
                R.fprintf(Frpt!!.file, __L234, Gage!![i]!!.ID, Gage!![i]!!.fname)
            }
            i++
        }
    }
    if ((Nobjects!![SUBCATCH] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L235))
        report_writeLine((__L236))
        report_writeLine((__L235))
        R.fprintf(Frpt!!.file, __L237)
        R.fprintf(Frpt!!.file, __L238)
        i = 0
        f4@ while ((i < Nobjects!![SUBCATCH])) {
            R.fprintf(Frpt!!.file, __L239, Subcatch!![i]!!.ID, (Subcatch!![i]!!.area * UCF(LANDAREA)), (Subcatch!![i]!!.width * UCF(LENGTH)), (Subcatch!![i]!!.fracImperv * 100.0), (Subcatch!![i]!!.slope * 100.0), Gage!![Subcatch!![i]!!.gage]!!.ID)
            if ((Subcatch!![i]!!.outNode >= 0)) {
                R.fprintf(Frpt!!.file, __L240, Node!![Subcatch!![i]!!.outNode]!!.ID)
            } else if ((Subcatch!![i]!!.outSubcatch >= 0)) {
                R.fprintf(Frpt!!.file, __L240, Subcatch!![Subcatch!![i]!!.outSubcatch]!!.ID)
            }
            if ((Subcatch!![i]!!.lidArea != 0.0)) {
                lidCount++
            }
            i++
        }
    }
    if ((lidCount > 0)) {
        lid_writeSummary()
    }
    if ((Nobjects!![NODE] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L241))
        report_writeLine((__L242))
        report_writeLine((__L241))
        R.fprintf(Frpt!!.file, __L243)
        R.fprintf(Frpt!!.file, __L244)
        R.fprintf(Frpt!!.file, __L245)
        i = 0
        f5@ while ((i < Nobjects!![NODE])) {
            R.fprintf(Frpt!!.file, __L246, Node!![i]!!.ID, NodeTypeWords!![(Node!![i]!!.type - JUNCTION)], (Node!![i]!!.invertElev * UCF(LENGTH)), (Node!![i]!!.fullDepth * UCF(LENGTH)), ((Node!![i]!!.pondedArea * UCF(LENGTH)) * UCF(LENGTH)))
            if ((((Node!![i]!!.extInflow != null) || (Node!![i]!!.dwfInflow != null)) || (Node!![i]!!.rdiiInflow != null))) {
                R.fprintf(Frpt!!.file, __L247)
            }
            i++
        }
    }
    if ((Nobjects!![LINK] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L241))
        report_writeLine((__L248))
        report_writeLine((__L241))
        R.fprintf(Frpt!!.file, __L249)
        R.fprintf(Frpt!!.file, __L250)
        i = 0
        f6@ while ((i < Nobjects!![LINK])) {
            if ((Link!![i]!!.direction == 1)) {
                R.fprintf(Frpt!!.file, __L251, Link!![i]!!.ID, Node!![Link!![i]!!.node1]!!.ID, Node!![Link!![i]!!.node2]!!.ID)
            } else {
                R.fprintf(Frpt!!.file, __L251, Link!![i]!!.ID, Node!![Link!![i]!!.node2]!!.ID, Node!![Link!![i]!!.node1]!!.ID)
            }
            if ((Link!![i]!!.type == PUMP)) {
                k = Link!![i]!!.subIndex
                R.fprintf(Frpt!!.file, __L252, PumpTypeWords!![Pump!![k]!!.type])
            } else {
                R.fprintf(Frpt!!.file, __L253, LinkTypeWords!![(Link!![i]!!.type - CONDUIT)])
            }
            if ((Link!![i]!!.type == CONDUIT)) {
                k = Link!![i]!!.subIndex
                R.fprintf(Frpt!!.file, __L254, (Conduit!![k]!!.length * UCF(LENGTH)), ((Conduit!![k]!!.slope * 100.0) * (Link!![i]!!.direction).toDouble()), Conduit!![k]!!.roughness)
            }
            i++
        }
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L255))
        report_writeLine((__L256))
        report_writeLine((__L255))
        R.fprintf(Frpt!!.file, __L257)
        R.fprintf(Frpt!!.file, __L258)
        R.fprintf(Frpt!!.file, __L259)
        i = 0
        f7@ while ((i < Nobjects!![LINK])) {
            if ((Link!![i]!!.type == CONDUIT)) {
                k = Link!![i]!!.subIndex
                R.fprintf(Frpt!!.file, __L260, Link!![i]!!.ID)
                if ((Link!![i]!!.xsect!!.type == CUSTOM)) {
                    R.fprintf(Frpt!!.file, __L261, Curve!![Link!![i]!!.xsect!!.transect]!!.ID)
                } else if ((Link!![i]!!.xsect!!.type == IRREGULAR)) {
                    R.fprintf(Frpt!!.file, __L261, Transect!![Link!![i]!!.xsect!!.transect]!!.ID)
                } else if ((Link!![i]!!.xsect!!.type == STREET_XSECT)) {
                    R.fprintf(Frpt!!.file, __L261, Street!![Link!![i]!!.xsect!!.transect]!!.ID)
                } else {
                    R.fprintf(Frpt!!.file, __L261, XsectTypeWords!![Link!![i]!!.xsect!!.type])
                }
                R.fprintf(Frpt!!.file, __L262, (Link!![i]!!.xsect!!.yFull * UCF(LENGTH)), ((Link!![i]!!.xsect!!.aFull * UCF(LENGTH)) * UCF(LENGTH)), (Link!![i]!!.xsect!!.rFull * UCF(LENGTH)), (Link!![i]!!.xsect!!.wMax * UCF(LENGTH)), Conduit!![k]!!.barrels, (Link!![i]!!.qFull * UCF(FLOW)))
            }
            i++
        }
    }
    if ((Nobjects!![SHAPE] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L206))
        report_writeLine((__L263))
        report_writeLine((__L206))
        i = 0
        f8@ while ((i < Nobjects!![SHAPE])) {
            k = Shape!![i]!!.curve
            R.fprintf(Frpt!!.file, __L264, Curve!![k]!!.ID)
            R.fprintf(Frpt!!.file, __L265)
            m = 1
            f9@ while ((m < 51)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Shape!![i]!!.areaTbl!![m])
                m++
            }
            R.fprintf(Frpt!!.file, __L268)
            m = 1
            f10@ while ((m < 51)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Shape!![i]!!.hradTbl!![m])
                m++
            }
            R.fprintf(Frpt!!.file, __L269)
            m = 1
            f11@ while ((m < 51)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Shape!![i]!!.widthTbl!![m])
                m++
            }
            i++
        }
    }
    if ((Nobjects!![TRANSECT] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L227))
        report_writeLine((__L270))
        report_writeLine((__L227))
        i = 0
        f12@ while ((i < Nobjects!![TRANSECT])) {
            R.fprintf(Frpt!!.file, __L271, Transect!![i]!!.ID)
            R.fprintf(Frpt!!.file, __L265)
            m = 1
            f13@ while ((m < 51)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Transect!![i]!!.areaTbl!![m])
                m++
            }
            R.fprintf(Frpt!!.file, __L268)
            m = 1
            f14@ while ((m < 51)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Transect!![i]!!.hradTbl!![m])
                m++
            }
            R.fprintf(Frpt!!.file, __L269)
            m = 1
            f15@ while ((m < 51)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Transect!![i]!!.widthTbl!![m])
                m++
            }
            i++
        }
    }
    if ((Nobjects!![STREET] > 0)) {
        report_writeLine((__L0))
        report_writeLine((__L0))
        report_writeLine((__L272))
        report_writeLine((__L273))
        report_writeLine((__L272))
        i = 0
        f16@ while ((i < Nobjects!![STREET])) {
            R.fprintf(Frpt!!.file, __L274, Street!![i]!!.ID)
            R.fprintf(Frpt!!.file, __L265)
            m = 1
            f17@ while ((m < Street!![i]!!.transect!!.nTbl)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Street!![i]!!.transect!!.areaTbl!![m])
                m++
            }
            R.fprintf(Frpt!!.file, __L268)
            m = 1
            f18@ while ((m < Street!![i]!!.transect!!.nTbl)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Street!![i]!!.transect!!.hradTbl!![m])
                m++
            }
            R.fprintf(Frpt!!.file, __L269)
            m = 1
            f19@ while ((m < Street!![i]!!.transect!!.nTbl)) {
                if (((m % 5) == 1)) {
                    R.fprintf(Frpt!!.file, __L266)
                }
                R.fprintf(Frpt!!.file, __L267, Street!![i]!!.transect!!.widthTbl!![m])
                m++
            }
            i++
        }
    }
    report_writeLine((__L0))
}

