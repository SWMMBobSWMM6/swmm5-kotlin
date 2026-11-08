// Generated from EPA SWMM 5.2.4 (street.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_street")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// street.c:29
fun street_create(nStreets: Int): Int {
    Street = (null)
    Nobjects!![STREET] = 0
    Street = Array<TStreet?>(((nStreets).toLong()).toInt()) { TStreet() }
    if ((Street == null)) {
        return ERR_MEMORY
    }
    Nobjects!![STREET] = nStreets
    return 0
}

// street.c:46
fun street_delete() {
    if ((Street != null)) {
        R.free(Street)
        Street = (null)
    }
}

// street.c:58
fun street_readParams(tok: Array<CPtr?>?, ntoks: Int): Int {
    var i: Int = 0
    var k: Int = 0
    val sides = IBox(0)
    var x: DoubleArray? = DoubleArray(11)
    var street: TStreet? = null
    if ((ntoks < 5)) {
        return error_setInpError(ERR_ITEMS, __L0)
    }
    i = project_findObject(STREET, tok!![0])
    if ((i < 0)) {
        return error_setInpError(ERR_NAME, tok!![0])
    }
    Street!![i]!!.ID = project_findID(STREET, tok!![0])
    k = 0
    f1@ while ((k <= 9)) {
        x!![k] = 0.0
        k++
    }
    k = 1
    f2@ while ((k <= 4)) {
        if (((!(getDouble(tok!![k], DElem(x!!, k)) != 0)) || (x!![k] <= 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![k])
        }
        k++
    }
    if ((ntoks > 5)) {
        if (((!(getDouble(tok!![5], DElem(x!!, 5)) != 0)) || (x!![5] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![5])
        }
    }
    if ((ntoks > 6)) {
        if (((!(getDouble(tok!![6], DElem(x!!, 6)) != 0)) || (x!![6] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![6])
        }
    }
    sides.v = 2
    if ((ntoks > 7)) {
        if ((((!(getInt(tok!![7], sides) != 0)) || (sides.v < 1)) || (sides.v > 2))) {
            return error_setInpError(ERR_NUMBER, tok!![7])
        }
    }
    if ((ntoks > 8)) {
        if (((!(getDouble(tok!![8], DElem(x!!, 8)) != 0)) || (x!![8] < 0.0))) {
            return error_setInpError(ERR_NUMBER, tok!![8])
        }
        if ((x!![8] > 0.0)) {
            if ((ntoks < 11)) {
                return error_setInpError(ERR_ITEMS, __L0)
            }
            k = 9
            f3@ while ((k <= 10)) {
                if (((!(getDouble(tok!![k], DElem(x!!, k)) != 0)) || (x!![k] <= 0.0))) {
                    return error_setInpError(ERR_NUMBER, tok!![k])
                }
                k++
            }
        }
    }
    street = Street!![i]
    street!!.width = (x!![1] / UCF(LENGTH))
    street!!.curbHeight = (x!![2] / UCF(LENGTH))
    street!!.slope = (x!![3] / 100.0)
    street!!.roughness = x!![4]
    street!!.gutterDepression = (x!![5] / UCF(LENGTH))
    street!!.gutterWidth = (x!![6] / UCF(LENGTH))
    street!!.sides = sides.v
    street!!.backWidth = (x!![8] / UCF(LENGTH))
    street!!.backSlope = (x!![9] / 100.0)
    street!!.backRoughness = x!![10]
    transect_createStreetTransect(street)
    return 0
}

// street.c:141
fun street_getExtentFilled(link: Int): Double {
    var k: Int = 0
    var t: Int = 0
    var filled: Double = 0.0
    t = Link!![link]!!.xsect!!.transect
    if ((t < 0)) {
        return 0.0
    }
    if ((RouteModel == DW)) {
        filled = ((if ((((Node!![Link!![link]!!.node1]!!.newDepth) >= (Node!![Link!![link]!!.node2]!!.newDepth)))) (Node!![Link!![link]!!.node1]!!.newDepth) else (Node!![Link!![link]!!.node2]!!.newDepth)))
    } else {
        k = Link!![link]!!.subIndex
        filled = ((if ((((Conduit!![k]!!.a1) >= (Conduit!![k]!!.a2)))) (Conduit!![k]!!.a1) else (Conduit!![k]!!.a2)))
    }
    return filled
}

