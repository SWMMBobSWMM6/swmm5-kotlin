// Generated from EPA SWMM 5.2.4 (error.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_error")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var ErrString: CPtr? = R.buf(256)

// error.c:30
fun error_getMsg(errCode: Int, msg: CPtr?): CPtr? {
    when (errCode) {
        101 -> {
            R.strcpy(msg, __L46)
        }
        103 -> {
            R.strcpy(msg, __L47)
        }
        105 -> {
            R.strcpy(msg, __L48)
        }
        107 -> {
            R.strcpy(msg, __L49)
        }
        108 -> {
            R.strcpy(msg, __L50)
        }
        109 -> {
            R.strcpy(msg, __L51)
        }
        110 -> {
            R.strcpy(msg, __L52)
        }
        111 -> {
            R.strcpy(msg, __L53)
        }
        112 -> {
            R.strcpy(msg, __L54)
        }
        113 -> {
            R.strcpy(msg, __L55)
        }
        114 -> {
            R.strcpy(msg, __L56)
        }
        115 -> {
            R.strcpy(msg, __L57)
        }
        117 -> {
            R.strcpy(msg, __L58)
        }
        119 -> {
            R.strcpy(msg, __L59)
        }
        121 -> {
            R.strcpy(msg, __L60)
        }
        122 -> {
            R.strcpy(msg, __L61)
        }
        131 -> {
            R.strcpy(msg, __L62)
        }
        133 -> {
            R.strcpy(msg, __L63)
        }
        134 -> {
            R.strcpy(msg, __L64)
        }
        135 -> {
            R.strcpy(msg, __L65)
        }
        136 -> {
            R.strcpy(msg, __L66)
        }
        137 -> {
            R.strcpy(msg, __L67)
        }
        138 -> {
            R.strcpy(msg, __L68)
        }
        139 -> {
            R.strcpy(msg, __L69)
        }
        140 -> {
            R.strcpy(msg, __L70)
        }
        141 -> {
            R.strcpy(msg, __L71)
        }
        143 -> {
            R.strcpy(msg, __L72)
        }
        145 -> {
            R.strcpy(msg, __L73)
        }
        151 -> {
            R.strcpy(msg, __L74)
        }
        153 -> {
            R.strcpy(msg, __L75)
        }
        155 -> {
            R.strcpy(msg, __L76)
        }
        156 -> {
            R.strcpy(msg, __L77)
        }
        157 -> {
            R.strcpy(msg, __L78)
        }
        158 -> {
            R.strcpy(msg, __L79)
        }
        159 -> {
            R.strcpy(msg, __L80)
        }
        161 -> {
            R.strcpy(msg, __L81)
        }
        171 -> {
            R.strcpy(msg, __L82)
        }
        173 -> {
            R.strcpy(msg, __L83)
        }
        181 -> {
            R.strcpy(msg, __L84)
        }
        182 -> {
            R.strcpy(msg, __L85)
        }
        183 -> {
            R.strcpy(msg, __L86)
        }
        184 -> {
            R.strcpy(msg, __L87)
        }
        185 -> {
            R.strcpy(msg, __L88)
        }
        187 -> {
            R.strcpy(msg, __L89)
        }
        188 -> {
            R.strcpy(msg, __L90)
        }
        191 -> {
            R.strcpy(msg, __L91)
        }
        193 -> {
            R.strcpy(msg, __L92)
        }
        195 -> {
            R.strcpy(msg, __L93)
        }
        200 -> {
            R.strcpy(msg, __L94)
        }
        201 -> {
            R.strcpy(msg, __L95)
        }
        203 -> {
            R.strcpy(msg, __L96)
        }
        205 -> {
            R.strcpy(msg, __L97)
        }
        207 -> {
            R.strcpy(msg, __L98)
        }
        209 -> {
            R.strcpy(msg, __L99)
        }
        211 -> {
            R.strcpy(msg, __L100)
        }
        213 -> {
            R.strcpy(msg, __L101)
        }
        217 -> {
            R.strcpy(msg, __L102)
        }
        219 -> {
            R.strcpy(msg, __L103)
        }
        221 -> {
            R.strcpy(msg, __L104)
        }
        223 -> {
            R.strcpy(msg, __L105)
        }
        225 -> {
            R.strcpy(msg, __L106)
        }
        227 -> {
            R.strcpy(msg, __L107)
        }
        229 -> {
            R.strcpy(msg, __L108)
        }
        231 -> {
            R.strcpy(msg, __L109)
        }
        233 -> {
            R.strcpy(msg, __L110)
        }
        235 -> {
            R.strcpy(msg, __L111)
        }
        301 -> {
            R.strcpy(msg, __L112)
        }
        303 -> {
            R.strcpy(msg, __L113)
        }
        305 -> {
            R.strcpy(msg, __L114)
        }
        307 -> {
            R.strcpy(msg, __L115)
        }
        308 -> {
            R.strcpy(msg, __L116)
        }
        309 -> {
            R.strcpy(msg, __L117)
        }
        311 -> {
            R.strcpy(msg, __L118)
        }
        313 -> {
            R.strcpy(msg, __L119)
        }
        315 -> {
            R.strcpy(msg, __L120)
        }
        317 -> {
            R.strcpy(msg, __L121)
        }
        318 -> {
            R.strcpy(msg, __L122)
        }
        319 -> {
            R.strcpy(msg, __L123)
        }
        320 -> {
            R.strcpy(msg, __L124)
        }
        321 -> {
            R.strcpy(msg, __L125)
        }
        323 -> {
            R.strcpy(msg, __L126)
        }
        325 -> {
            R.strcpy(msg, __L127)
        }
        327 -> {
            R.strcpy(msg, __L128)
        }
        329 -> {
            R.strcpy(msg, __L129)
        }
        331 -> {
            R.strcpy(msg, __L130)
        }
        333 -> {
            R.strcpy(msg, __L131)
        }
        335 -> {
            R.strcpy(msg, __L132)
        }
        336 -> {
            R.strcpy(msg, __L133)
        }
        337 -> {
            R.strcpy(msg, __L134)
        }
        338 -> {
            R.strcpy(msg, __L135)
        }
        339 -> {
            R.strcpy(msg, __L136)
        }
        341 -> {
            R.strcpy(msg, __L137)
        }
        343 -> {
            R.strcpy(msg, __L138)
        }
        345 -> {
            R.strcpy(msg, __L139)
        }
        351 -> {
            R.strcpy(msg, __L140)
        }
        353 -> {
            R.strcpy(msg, __L141)
        }
        355 -> {
            R.strcpy(msg, __L142)
        }
        357 -> {
            R.strcpy(msg, __L143)
        }
        361 -> {
            R.strcpy(msg, __L144)
        }
        363 -> {
            R.strcpy(msg, __L145)
        }
        500 -> {
            R.strcpy(msg, __L146)
        }
        501 -> {
            R.strcpy(msg, __L147)
        }
        502 -> {
            R.strcpy(msg, __L148)
        }
        503 -> {
            R.strcpy(msg, __L149)
        }
        504 -> {
            R.strcpy(msg, __L150)
        }
        505 -> {
            R.strcpy(msg, __L151)
        }
        506 -> {
            R.strcpy(msg, __L152)
        }
        507 -> {
            R.strcpy(msg, __L153)
        }
        508 -> {
            R.strcpy(msg, __L154)
        }
        509 -> {
            R.strcpy(msg, __L155)
        }
        else -> {
            R.strcpy(msg, __L0)
        }
    }
    return (msg)
}

// error.c:45
fun error_setInpError(errcode: Int, s: CPtr?): Int {
    R.strcpy(ErrString, s)
    return errcode
}

