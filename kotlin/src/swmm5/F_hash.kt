// Generated from EPA SWMM 5.2.4 (hash.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_hash")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// hash.c:24
fun samestr(s1: CPtr?, s2: CPtr?): Int {
    var i: Int = 0
    i = 0
    f1@ while ((((if (((((s1!!.g(i)) >= 97) && ((s1!!.g(i)) <= 122)))) (((s1!!.g(i)) and 32.inv())) else (s1!!.g(i)))) == ((if (((((s2!!.g(i)) >= 97) && ((s2!!.g(i)) <= 122)))) (((s2!!.g(i)) and 32.inv())) else (s2!!.g(i)))))) {
        if (((!(s1!!.g((i + 1)) != 0)) && (!(s2!!.g((i + 1)) != 0)))) {
            return (1)
        }
        i++
    }
    return (0)
}

// hash.c:33
fun hash(__str: CPtr?): Long {
    var str: CPtr? = __str
    var sum1: Long = 0L
    var check1: Long = 0L
    var sum2: Long = 0L
    w1@ while ((0 != str!!.g(0))) {
        sum1 = ((sum1 + ((((if (((((str!!.g(0)) >= 97) && ((str!!.g(0)) <= 122)))) (((str!!.g(0)) and 32.inv())) else (str!!.g(0))))).toLong() and 0xFFFFFFFFL)) and 0xFFFFFFFFL)
        str = str!!.add(1)
        if ((255L <= sum1)) {
            sum1 = ((sum1 - 255L) and 0xFFFFFFFFL)
        }
        sum2 += sum1
    }
    check1 = (sum2 and 0xFFFFFFFFL)
    check1 = (check1 % 255L)
    check1 = ((255L - ((((sum1 + check1) and 0xFFFFFFFFL)) % 255L)) and 0xFFFFFFFFL)
    sum1 = ((255L - ((((sum1 + check1) and 0xFFFFFFFFL)) % 255L)) and 0xFFFFFFFFL)
    return (((((((check1 shl 8) and 0xFFFFFFFFL)) or sum1)) % 1999L))
}

// hash.c:51
fun HTcreate(): Array<HTentry?>? {
    var i: Int = 0
    var ht: Array<HTentry?>? = arrayOfNulls<HTentry>((1999L).toInt())
    if ((ht != null)) {
        i = 0
        f1@ while ((i < 1999)) {
            ht!![i] = (null)
            i++
        }
    }
    return (ht)
}

// hash.c:59
fun HTinsert(ht: Array<HTentry?>?, key: CPtr?, data: Int): Int {
    var i: Long = hash(key)
    var entry: HTentry? = null
    if ((i >= 1999L)) {
        return (0)
    }
    entry = HTentry()
    if ((entry == null)) {
        return (0)
    }
    entry!!.key = key
    entry!!.data = data
    entry!!.next = ht!![(i).toInt()]
    ht!![(i).toInt()] = entry
    return (1)
}

// hash.c:73
fun HTfind(ht: Array<HTentry?>?, key: CPtr?): Int {
    var i: Long = hash(key)
    var entry: HTentry? = null
    if ((i >= 1999L)) {
        return ((-1))
    }
    entry = ht!![(i).toInt()]
    w1@ while ((entry != null)) {
        if ((samestr(entry!!.key, key) != 0)) {
            return (entry!!.data)
        }
        entry = entry!!.next
    }
    return ((-1))
}

// hash.c:87
fun HTfindKey(ht: Array<HTentry?>?, key: CPtr?): CPtr? {
    var i: Long = hash(key)
    var entry: HTentry? = null
    if ((i >= 1999L)) {
        return null
    }
    entry = ht!![(i).toInt()]
    w1@ while ((entry != null)) {
        if ((samestr(entry!!.key, key) != 0)) {
            return (entry!!.key)
        }
        entry = entry!!.next
    }
    return null
}

// hash.c:101
fun HTfree(ht: Array<HTentry?>?) {
    var entry: HTentry? = null
    var nextentry: HTentry? = null
    var i: Int = 0
    i = 0
    f1@ while ((i < 1999)) {
        entry = ht!![i]
        w2@ while ((entry != null)) {
            nextentry = entry!!.next
            R.free(entry)
            entry = nextentry
        }
        i++
    }
    R.free(ht)
}

