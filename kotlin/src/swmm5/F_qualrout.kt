// Generated from EPA SWMM 5.2.4 (qualrout.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_qualrout")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var ZeroVolume: Double = 0.0353147
@JvmField var ZeroDepth: Double = 0.003281

// qualrout.c:63
fun qualrout_init() {
    var i: Int = 0
    var p: Int = 0
    var isWet: Int = 0
    var c: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        isWet = (if (((Node!![i]!!.newDepth > ZeroDepth))) 1 else 0)
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            c = 0.0
            if ((isWet != 0)) {
                c = Pollut!![p]!!.initConcen
            }
            Node!![i]!!.oldQual!![p] = c
            Node!![i]!!.newQual!![p] = c
            p++
        }
        i++
    }
    i = 0
    f3@ while ((i < Nobjects!![LINK])) {
        isWet = (if (((Link!![i]!!.newDepth > ZeroDepth))) 1 else 0)
        p = 0
        f4@ while ((p < Nobjects!![POLLUT])) {
            c = 0.0
            if ((isWet != 0)) {
                c = Pollut!![p]!!.initConcen
            }
            Link!![i]!!.oldQual!![p] = c
            Link!![i]!!.newQual!![p] = c
            p++
        }
        i++
    }
}

// qualrout.c:100
fun qualrout_execute(tStep: Double) {
    var i: Int = 0
    var j: Int = 0
    var qIn: Double = 0.0
    var vAvg: Double = 0.0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        findLinkMassFlow(i, tStep)
        i++
    }
    j = 0
    f2@ while ((j < Nobjects!![NODE])) {
        Node!![j]!!.qualInflow = Node!![j]!!.inflow
        qIn = Node!![j]!!.qualInflow
        vAvg = (((Node!![j]!!.oldVolume + Node!![j]!!.newVolume)) / 2.0)
        if ((Node!![j]!!.treatment != null)) {
            if ((qIn < 1e-10)) {
                qIn = 0.0
            }
            treatmnt_setInflow(qIn, Node!![j]!!.newQual)
        }
        if (((Node!![j]!!.type == STORAGE) || (Node!![j]!!.oldVolume > ZeroVolume))) {
            findStorageQual(j, tStep)
        } else {
            findNodeQual(j)
        }
        if ((Node!![j]!!.treatment != null)) {
            treatmnt_treat(j, qIn, vAvg, tStep)
        }
        j++
    }
    i = 0
    f3@ while ((i < Nobjects!![LINK])) {
        findLinkQual(i, tStep)
        i++
    }
}

// qualrout.c:146
fun getMixedQual(__c: Double, v1: Double, wIn: Double, qIn: Double, tStep: Double): Double {
    var c: Double = __c
    var vIn: Double = 0.0
    var cIn: Double = 0.0
    var cMax: Double = 0.0
    if ((qIn <= 1e-10)) {
        return c
    }
    vIn = (qIn * tStep)
    cIn = ((wIn * tStep) / vIn)
    cMax = ((if ((((c) >= (cIn)))) (c) else (cIn)))
    c = ((((c * v1) + (wIn * tStep))) / ((v1 + vIn)))
    c = ((if ((((c) <= (cMax)))) (c) else (cMax)))
    c = ((if ((((c) >= (0.0)))) (c) else (0.0)))
    return c
}

// qualrout.c:179
fun findLinkMassFlow(i: Int, tStep: Double) {
    var j: Int = 0
    var p: Int = 0
    var qLink: Double = 0.0
    var w: Double = 0.0
    qLink = Link!![i]!!.newFlow
    j = Link!![i]!!.node2
    if ((qLink < 0.0)) {
        j = Link!![i]!!.node1
    }
    qLink = abs(qLink)
    if ((RouteModel != DW)) {
        qLink -= (inlet_capturedFlow(i))
    }
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        w = (qLink * Link!![i]!!.oldQual!![p])
        Node!![j]!!.newQual!![p] += w
        Link!![i]!!.totalLoad!![p] += (w * tStep)
        p++
    }
    Node!![j]!!.qualInflow += qLink
}

// qualrout.c:221
fun findNodeQual(j: Int) {
    var p: Int = 0
    var qNode: Double = 0.0
    qNode = Node!![j]!!.qualInflow
    if ((qNode > 1e-10)) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            Node!![j]!!.newQual!![p] /= qNode
            p++
        }
    } else {
        p = 0
        f2@ while ((p < Nobjects!![POLLUT])) {
            if ((Node!![j]!!.newDepth > ZeroDepth)) {
                Node!![j]!!.newQual!![p] = Node!![j]!!.oldQual!![p]
            } else {
                Node!![j]!!.newQual!![p] = 0.0
            }
            p++
        }
    }
}

// qualrout.c:253
fun findLinkQual(i: Int, tStep: Double) {
    var j: Int = 0
    var k: Int = 0
    var p: Int = 0
    var wIn: Double = 0.0
    var qIn: Double = 0.0
    var qSeep: Double = 0.0
    var v1: Double = 0.0
    var v2: Double = 0.0
    var c1: Double = 0.0
    var c2: Double = 0.0
    var vEvap: Double = 0.0
    var vLosses: Double = 0.0
    var fEvap: Double = 0.0
    var barrels: Double = 0.0
    j = Link!![i]!!.node1
    if ((Link!![i]!!.newFlow < 0.0)) {
        j = Link!![i]!!.node2
    }
    if (((Link!![i]!!.type != CONDUIT) || (Link!![i]!!.xsect!!.type == DUMMY))) {
        p = 0
        f1@ while ((p < Nobjects!![POLLUT])) {
            Link!![i]!!.newQual!![p] = Node!![j]!!.newQual!![p]
            p++
        }
        return
    }
    k = Link!![i]!!.subIndex
    barrels = (Conduit!![k]!!.barrels).toDouble()
    qIn = (abs(Conduit!![k]!!.q1) * barrels)
    qSeep = (Conduit!![k]!!.seepLossRate * barrels)
    vEvap = ((Conduit!![k]!!.evapLossRate * barrels) * tStep)
    v1 = Link!![i]!!.oldVolume
    v2 = Link!![i]!!.newVolume
    vLosses = ((qSeep * tStep) + vEvap)
    fEvap = 1.0
    if (((vEvap > 0.0) && (v1 > ZeroVolume))) {
        fEvap += (vEvap / v1)
    }
    if ((RouteModel == SF)) {
        findSFLinkQual(i, qSeep, fEvap, tStep)
        return
    }
    if ((RouteModel == DW)) {
        qIn = (qIn + ((((v2 + vLosses) - v1)) / tStep))
        qIn = ((if ((((qIn) >= (0.0)))) (qIn) else (0.0)))
    }
    p = 0
    f2@ while ((p < Nobjects!![POLLUT])) {
        c1 = Link!![i]!!.oldQual!![p]
        massbal_addSeepageLoss(p, (qSeep * c1))
        c1 *= fEvap
        c2 = getReactedQual(p, c1, v1, tStep)
        wIn = (Node!![j]!!.newQual!![p] * qIn)
        c2 = getMixedQual(c2, v1, wIn, qIn, tStep)
        if (((v2 < ZeroVolume) || (Link!![i]!!.newDepth <= ZeroDepth))) {
            massbal_addToFinalStorage(p, (c2 * v2))
            c2 = 0.0
        }
        Link!![i]!!.newQual!![p] = c2
        p++
    }
}

// qualrout.c:357
fun findSFLinkQual(i: Int, qSeep: Double, fEvap: Double, tStep: Double) {
    var j: Int = Link!![i]!!.node1
    var p: Int = 0
    var c1: Double = 0.0
    var c2: Double = 0.0
    var lossRate: Double = 0.0
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        c1 = Node!![j]!!.newQual!![p]
        massbal_addSeepageLoss(p, (qSeep * c1))
        c1 *= fEvap
        c2 = c1
        if ((Pollut!![p]!!.kDecay > 0.0)) {
            c2 = (c1 * Glibm.exp(((-Pollut!![p]!!.kDecay) * tStep)))
            c2 = ((if ((((0.0) >= (c2)))) (0.0) else (c2)))
            lossRate = (((c1 - c2)) * Link!![i]!!.newFlow)
            massbal_addReactedMass(p, lossRate)
        }
        Link!![i]!!.newQual!![p] = c2
        p++
    }
}

// qualrout.c:398
fun findStorageQual(j: Int, tStep: Double) {
    var p: Int = 0
    var k: Int = 0
    var qIn: Double = 0.0
    var wIn: Double = 0.0
    var v1: Double = 0.0
    var c1: Double = 0.0
    var c2: Double = 0.0
    var qExfil: Double = 0.0
    var vEvap: Double = 0.0
    var fEvap: Double = 1.0
    qIn = Node!![j]!!.qualInflow
    v1 = Node!![j]!!.oldVolume
    if ((Node!![j]!!.type == STORAGE)) {
        updateHRT(j, Node!![j]!!.oldVolume, qIn, tStep)
        k = Node!![j]!!.subIndex
        qExfil = (Storage!![k]!!.exfilLoss / tStep)
        vEvap = Storage!![k]!!.evapLoss
        if (((vEvap > 0.0) && (v1 > ZeroVolume))) {
            fEvap += (vEvap / v1)
        }
    }
    p = 0
    f1@ while ((p < Nobjects!![POLLUT])) {
        c1 = Node!![j]!!.oldQual!![p]
        massbal_addSeepageLoss(p, (qExfil * c1))
        c1 *= fEvap
        if (((Node!![j]!!.treatment == null) || (Node!![j]!!.treatment!![p]!!.equation == null))) {
            c1 = getReactedQual(p, c1, v1, tStep)
        }
        wIn = Node!![j]!!.newQual!![p]
        c2 = getMixedQual(c1, v1, wIn, qIn, tStep)
        if (((((Node!![j]!!.newVolume <= ZeroVolume) || (Node!![j]!!.newDepth <= ZeroDepth))) && (qIn <= 1e-10))) {
            massbal_addToFinalStorage(p, (c2 * Node!![j]!!.newVolume))
            c2 = 0.0
        }
        Node!![j]!!.newQual!![p] = c2
        p++
    }
}

// qualrout.c:478
fun updateHRT(j: Int, v: Double, q: Double, tStep: Double) {
    var k: Int = Node!![j]!!.subIndex
    var hrt: Double = Storage!![k]!!.hrt
    if ((v < 1e-10)) {
        hrt = 0.0
    } else {
        hrt = ((((hrt + tStep)) * v) / ((v + (q * tStep))))
    }
    Storage!![k]!!.hrt = ((if ((((hrt) >= (0.0)))) (hrt) else (0.0)))
}

// qualrout.c:498
fun getReactedQual(p: Int, c: Double, v1: Double, tStep: Double): Double {
    var c2: Double = 0.0
    var lossRate: Double = 0.0
    var kDecay: Double = Pollut!![p]!!.kDecay
    if ((kDecay == 0.0)) {
        return c
    }
    c2 = (c * ((1.0 - (kDecay * tStep))))
    c2 = ((if ((((0.0) >= (c2)))) (0.0) else (c2)))
    lossRate = ((((c - c2)) * v1) / tStep)
    massbal_addReactedMass(p, lossRate)
    return c2
}

