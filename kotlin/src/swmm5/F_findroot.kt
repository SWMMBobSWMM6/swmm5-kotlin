// Generated from EPA SWMM 5.2.4 (findroot.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_findroot")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

// findroot.c:20
fun findroot_Newton(x1: Double, x2: Double, rts: DRef?, xacc: Double, func: FP4?, p: Any?): Int {
    var j: Int = 0
    var n: Int = 0
    val df = DBox(0.0)
    var dx: Double = 0.0
    var dxold: Double = 0.0
    val f = DBox(0.0)
    var x: Double = 0.0
    var temp: Double = 0.0
    var xhi: Double = 0.0
    var xlo: Double = 0.0
    x = rts!!.get()
    xlo = x1
    xhi = x2
    dxold = abs((x2 - x1))
    dx = dxold
    func!!.call(x, f, df, p)
    n++
    j = 1
    f1@ while ((j <= 60)) {
        if ((((((((((x - xhi)) * df.v) - f.v)) * (((((x - xlo)) * df.v) - f.v))) >= 0.0) || ((abs((2.0 * f.v)) > abs((dxold * df.v))))))) {
            dxold = dx
            dx = (0.5 * ((xhi - xlo)))
            x = (xlo + dx)
            if ((xlo == x)) {
                break@f1
            }
        } else {
            dxold = dx
            dx = (f.v / df.v)
            temp = x
            x -= dx
            if ((temp == x)) {
                break@f1
            }
        }
        if ((abs(dx) < xacc)) {
            break@f1
        }
        func!!.call(x, f, df, p)
        n++
        if ((f.v < 0.0)) {
            xlo = x
        } else {
            xhi = x
        }
        j++
    }
    rts!!.set(x)
    if ((n <= 60)) {
        return n
    } else {
        return 0
    }
    throw IllegalStateException("findroot_Newton: no return")
}

// findroot.c:91
fun findroot_Ridder(x1: Double, x2: Double, xacc: Double, func: FP3?, p: Any?): Double {
    var j: Int = 0
    var ans: Double = 0.0
    var fhi: Double = 0.0
    var flo: Double = 0.0
    var fm: Double = 0.0
    var fnew: Double = 0.0
    var s: Double = 0.0
    var xhi: Double = 0.0
    var xlo: Double = 0.0
    var xm: Double = 0.0
    var xnew: Double = 0.0
    flo = func!!.call(x1, p)
    fhi = func!!.call(x2, p)
    if ((flo == 0.0)) {
        return x1
    }
    if ((fhi == 0.0)) {
        return x2
    }
    ans = (0.5 * ((x1 + x2)))
    if (((((flo > 0.0) && (fhi < 0.0))) || (((flo < 0.0) && (fhi > 0.0))))) {
        xlo = x1
        xhi = x2
        j = 1
        f1@ while ((j <= 60)) {
            xm = (0.5 * ((xlo + xhi)))
            fm = func!!.call(xm, p)
            s = sqrt(((fm * fm) - (flo * fhi)))
            if ((s == 0.0)) {
                return ans
            }
            xnew = (xm + (((xm - xlo)) * (((((if ((flo >= fhi)) 1.0 else (-1.0))) * fm) / s))))
            if ((abs((xnew - ans)) <= xacc)) {
                break@f1
            }
            ans = xnew
            fnew = func!!.call(ans, p)
            if ((((if (((fnew) >= 0.0)) abs(fm) else (-(abs(fm))))) != fm)) {
                xlo = xm
                flo = fm
                xhi = ans
                fhi = fnew
            } else if ((((if (((fnew) >= 0.0)) abs(flo) else (-(abs(flo))))) != flo)) {
                xhi = ans
                fhi = fnew
            } else if ((((if (((fnew) >= 0.0)) abs(fhi) else (-(abs(fhi))))) != fhi)) {
                xlo = ans
                flo = fnew
            } else {
                return ans
            }
            if ((abs((xhi - xlo)) <= xacc)) {
                return ans
            }
            j++
        }
        return ans
    }
    return (-1.0e20)
}

