// R.kt - C runtime layer for the Kotlin port of EPA SWMM 5.2.4 (a Kotlin port of the Java port's R.java).
//
// Reproduces the glibc and x86-64 behaviour SWMM's output depends on: printf formatting with glibc's
// rounding (round half to even on the exact binary value, done here with BigDecimal), strtod/sscanf
// parsing, NUL-terminated byte strings, stdio files with C's positioning and end-of-file rules, and x86's
// float-to-integer conversion. The math functions are in Glibm.kt (glibc 2.39's FMA variants translated
// to Kotlin). Written by Claude (Anthropic) for Robert Dickinson; see README.md.
@file:Suppress("NAME_SHADOWING", "FunctionName", "LocalVariableName", "UNUSED_PARAMETER")
package swmm5

import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.RandomAccessFile
import java.io.UncheckedIOException
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Paths

/** C char*: a byte array plus an offset */
class CPtr(@JvmField val b: ByteArray, @JvmField val o: Int) {
    fun g(i: Int): Int = b[o + i].toInt()                     // signed char, as x86 C
    fun s(i: Int, v: Int): Int { b[o + i] = v.toByte(); return v }
    fun add(n: Int): CPtr = CPtr(b, o + n)
    fun add(n: Long): CPtr = CPtr(b, o + n.toInt())
}

/** C FILE*: positioned reads and writes over a RandomAccessFile, with C's end-of-file flag */
class CFile(@JvmField val raf: RandomAccessFile?, @JvmField val std: OutputStream?) {
    @JvmField var pos = 0L
    @JvmField var eof = false
    private var wbuf: ByteArray? = null
    private var wlen = 0
    private var wpos = 0L
    private var rbuf: ByteArray? = null
    private var rstart = 0L
    private var rlen = 0

    fun flush() {
        if (wlen == 0) return
        try {
            if (std != null) { std.write(wbuf!!, 0, wlen); std.flush() }
            else { raf!!.seek(wpos); raf.write(wbuf!!, 0, wlen) }
        } catch (e: IOException) { throw UncheckedIOException(e) }
        wlen = 0
    }
    fun write(src: ByteArray, off: Int, n: Int) {
        rlen = 0
        if (wbuf == null) wbuf = ByteArray(BUFSZ)
        if (wlen > 0 && pos != wpos + wlen) flush()
        if (wlen == 0) wpos = pos
        if (wlen + n > BUFSZ) {
            flush()
            if (n > BUFSZ) {
                try {
                    if (std != null) std.write(src, off, n)
                    else { raf!!.seek(pos); raf.write(src, off, n) }
                } catch (e: IOException) { throw UncheckedIOException(e) }
                pos += n
                return
            }
            wpos = pos
        }
        System.arraycopy(src, off, wbuf!!, wlen, n)
        wlen += n
        pos += n
    }
    fun read(dst: ByteArray, off: Int, n: Int): Int {
        flush()
        var got = 0
        while (got < n) {
            if (rlen > 0 && pos >= rstart && pos < rstart + rlen) {
                val a = (pos - rstart).toInt()
                val m = minOf(n - got, rlen - a)
                System.arraycopy(rbuf!!, a, dst, off + got, m)
                got += m; pos += m
                continue
            }
            if (rbuf == null) rbuf = ByteArray(BUFSZ)
            rstart = pos
            try {
                raf!!.seek(pos)
                val r = raf.read(rbuf!!, 0, BUFSZ)
                rlen = if (r < 0) 0 else r
            } catch (e: IOException) { throw UncheckedIOException(e) }
            if (rlen == 0) break
        }
        return got
    }
    fun getc(): Int {
        if (wlen == 0 && rlen > 0 && pos >= rstart && pos < rstart + rlen) {
            val c = rbuf!![(pos - rstart).toInt()].toInt() and 255
            pos++
            return c
        }
        val t = ByteArray(1)
        return if (read(t, 0, 1) == 1) t[0].toInt() and 255 else -1
    }
    fun size(): Long {
        flush()
        try { return raf!!.length() } catch (e: IOException) { throw UncheckedIOException(e) }
    }
    companion object { const val BUFSZ = 1 shl 16 }
}

object R {
    // ------------------------------------------------------------ arithmetic with C semantics
    @JvmStatic fun D2I(x: Double): Int {                // (int)x on x86-64: NaN or out of range gives INT_MIN
        if (!(x > -2147483649.0 && x < 2147483648.0)) return Int.MIN_VALUE
        return x.toInt()
    }
    @JvmStatic fun D2L(x: Double): Long {
        if (!(x > -9223372036854775809.0 && x < 9223372036854775808.0)) return Long.MIN_VALUE
        return x.toLong()
    }
    @JvmStatic fun post(r: DRef?, d: Int): Double { val o = r!!.get(); r.set(o + d); return o }
    @JvmStatic fun post(r: FRef?, d: Int): Float { val o = r!!.get(); r.set(o + d); return o }
    @JvmStatic fun post(r: LRef?, d: Int): Long { val o = r!!.get(); r.set(o + d); return o }
    @JvmStatic fun <T> comma(a: Any?, b: T): T = b
    @JvmStatic fun free(o: Any?) {}

    // ------------------------------------------------------------ strings and memory
    @JvmStatic fun lit(s: String): CPtr {
        val b = ByteArray(s.length + 1)
        for (i in s.indices) b[i] = s[i].code.toByte()
        return CPtr(b, 0)
    }
    @JvmStatic fun buf(n: Int): CPtr = CPtr(ByteArray(maxOf(n, 1)), 0)
    @JvmStatic fun buf(n: Long): CPtr = buf(n.toInt())
    @JvmStatic fun bufFrom(src: CPtr?, n: Int): CPtr {
        val p = buf(n)
        val m = minOf(strlenI(src), n)
        System.arraycopy(src!!.b, src.o, p.b, 0, m)
        return p
    }
    @JvmStatic fun bufOf(n: Int, vals: IntArray): CPtr {
        val p = buf(n)
        for (i in vals.indices) p.b[i] = vals[i].toByte()
        return p
    }
    @JvmStatic fun bufs(n: Int, m: Int): Array<CPtr?> = Array(n) { buf(m) }
    @JvmStatic fun copyBuf(p: CPtr?): CPtr? = CPtr(p!!.b.clone(), p.o)
    @JvmStatic fun cloneGrid(g: Array<DoubleArray?>?): Array<DoubleArray?> = Array(g!!.size) { g[it]!!.clone() }
    @JvmStatic fun cloneGrid(g: Array<IntArray?>?): Array<IntArray?> = Array(g!!.size) { g[it]!!.clone() }
    @JvmStatic fun cloneGrid(g: Array<FloatArray?>?): Array<FloatArray?> = Array(g!!.size) { g[it]!!.clone() }
    @JvmStatic fun cloneGrid(g: Array<LongArray?>?): Array<LongArray?> = Array(g!!.size) { g[it]!!.clone() }
    @JvmStatic fun cloneGrid(g: Array<CPtr?>?): Array<CPtr?> = Array(g!!.size) { copyBuf(g[it]) }
    @JvmStatic fun peq(a: CPtr?, b: CPtr?): Boolean = a === b || (a != null && b != null && a.b === b.b && a.o == b.o)

    @JvmStatic fun realloc(p: CPtr?, n: Int): CPtr {
        val q = buf(n)
        if (p != null) System.arraycopy(p.b, p.o, q.b, 0, minOf(n, p.b.size - p.o))
        return q
    }
    @JvmStatic fun realloc(a: DoubleArray?, n: Int): DoubleArray = a?.copyOf(n) ?: DoubleArray(n)
    @JvmStatic fun realloc(a: IntArray?, n: Int): IntArray = a?.copyOf(n) ?: IntArray(n)
    @JvmStatic fun realloc(a: LongArray?, n: Int): LongArray = a?.copyOf(n) ?: LongArray(n)
    @JvmStatic fun realloc(a: FloatArray?, n: Int): FloatArray = a?.copyOf(n) ?: FloatArray(n)
    @JvmStatic fun <T> realloc(a: Array<T?>?, n: Int): Array<T?> = a!!.copyOf(n)
    @JvmStatic fun <T> realloc(a: Array<T?>?, n: Int, mk: () -> T): Array<T?> {
        val r = a!!.copyOf(n)
        for (i in a.size until n) r[i] = mk()
        return r
    }
    @JvmStatic fun memset(p: CPtr?, v: Int, n: Long) { p!!.b.fill(v.toByte(), p.o, p.o + n.toInt()) }
    @JvmStatic fun memset(a: DoubleArray?, v: Int, n: Long) { a!!.fill(0.0, 0, minOf(a.size.toLong(), n / 8).toInt()) }
    @JvmStatic fun memset(a: IntArray?, v: Int, n: Long) { a!!.fill(0, 0, minOf(a.size.toLong(), n / 4).toInt()) }
    @JvmStatic fun memset(a: FloatArray?, v: Int, n: Long) { a!!.fill(0f, 0, minOf(a.size.toLong(), n / 4).toInt()) }
    @JvmStatic fun memset(a: LongArray?, v: Int, n: Long) { a!!.fill(0L, 0, minOf(a.size.toLong(), n / 8).toInt()) }

    @JvmStatic fun strlenI(p: CPtr?): Int { var e = p!!.o; val b = p.b; while (b[e].toInt() != 0) e++; return e - p.o }
    @JvmStatic fun strlen(p: CPtr?): Long = strlenI(p).toLong()
    @JvmStatic fun bytesOf(p: CPtr?): ByteArray = p!!.b.copyOfRange(p.o, p.o + strlenI(p))
    @JvmStatic fun strOf(p: CPtr?): String = String(p!!.b, p.o, strlenI(p), Charsets.ISO_8859_1)
    @JvmStatic fun strcpy(d: CPtr?, s: CPtr?): CPtr? { System.arraycopy(s!!.b, s.o, d!!.b, d.o, strlenI(s) + 1); return d }
    @JvmStatic fun strcat(d: CPtr?, s: CPtr?): CPtr? { strcpy(d!!.add(strlenI(d)), s); return d }
    @JvmStatic fun strcmp(a: CPtr?, b: CPtr?): Int {
        var i = a!!.o; var j = b!!.o
        while (true) {
            val c = a.b[i].toInt() and 255; val d = b.b[j].toInt() and 255
            if (c != d) return c - d
            if (c == 0) return 0
            i++; j++
        }
    }
    @JvmStatic fun strchr(s: CPtr?, c: Int): CPtr? {
        val c = c and 255
        var i = s!!.o
        while (true) {
            if ((s.b[i].toInt() and 255) == c) return CPtr(s.b, i)
            if (s.b[i].toInt() == 0) return null
            i++
        }
    }
    @JvmStatic fun strrchr(s: CPtr?, c: Int): CPtr? {
        val c = c and 255
        val e = s!!.o + strlenI(s)
        if (c == 0) return CPtr(s.b, e)
        var i = e - 1
        while (i >= s.o) { if ((s.b[i].toInt() and 255) == c) return CPtr(s.b, i); i-- }
        return null
    }
    @JvmStatic fun strstr(h: CPtr?, n: CPtr?): CPtr? {
        val hl = strlenI(h); val nl = strlenI(n)
        var i = 0
        outer@ while (i + nl <= hl) {
            for (k in 0 until nl) if (h!!.b[h.o + i + k] != n!!.b[n.o + k]) { i++; continue@outer }
            return CPtr(h!!.b, h.o + i)
        }
        return null
    }
    @JvmStatic fun strcspn(s: CPtr?, rej: CPtr?): Long {
        val sb = bytesOf(s); val rb = bytesOf(rej)
        for (i in sb.indices) for (r in rb) if (sb[i] == r) return i.toLong()
        return sb.size.toLong()
    }
    private var tokNext: CPtr? = null
    private fun inSet(c: Byte, set: ByteArray): Boolean { for (x in set) if (x == c) return true; return false }
    @JvmStatic fun strtok(s: CPtr?, delim: CPtr?): CPtr? {
        if (s != null) tokNext = s
        val p = tokNext ?: return null
        val ds = bytesOf(delim)
        val b = p.b
        var i = p.o
        while (b[i].toInt() != 0 && inSet(b[i], ds)) i++
        if (b[i].toInt() == 0) { tokNext = null; return null }
        val start = i
        while (b[i].toInt() != 0 && !inSet(b[i], ds)) i++
        if (b[i].toInt() != 0) { b[i] = 0; tokNext = CPtr(b, i + 1) }
        else tokNext = null
        return CPtr(b, start)
    }
    @JvmStatic fun toupper(c: Int): Int = if (c in 97..122) c - 32 else c

    private fun isSpace(c: Int): Boolean = c == 32 || (c in 9..13)
    private fun low(c: Int): Int = if (c in 65..90) c or 0x20 else c
    @JvmField val DEFAULT_NAN = Double.fromBits(-2251799813685248L)    // 0xfff8000000000000, x86's default NaN
    @JvmField val POS_NAN = Double.fromBits(0x7ff8000000000000L)

    /** parse a C floating number at b[i..] (at most maxw chars); sets scanEnd (-1: no number) */
    private var scanEnd = 0
    private var scanText: String? = null
    private fun scanDouble(b: ByteArray, i0: Int, maxw: Int, skipSpace: Boolean): Double {
        var i = i0
        val n = b.size
        if (skipSpace) while (i < n && isSpace(b[i].toInt())) i++
        val lim = if (maxw < 0) n else i + maxw
        fun g(k: Int): Int = if (k < lim && k < n) b[k].toInt() and 255 else 0
        var neg = false
        if (g(i) == 43 || g(i) == 45) { neg = g(i) == 45; i++ }
        if (low(g(i)) == 105 && low(g(i + 1)) == 110 && low(g(i + 2)) == 102) {
            i += 3
            if (low(g(i)) == 105 && low(g(i + 1)) == 110 && low(g(i + 2)) == 105 && low(g(i + 3)) == 116 && low(g(i + 4)) == 121) i += 5
            scanEnd = i; scanText = null
            return if (neg) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY
        }
        if (low(g(i)) == 110 && low(g(i + 1)) == 97 && low(g(i + 2)) == 110) {
            i += 3
            if (g(i) == 40) {
                var k = i + 1
                while (Character.isLetterOrDigit(g(k)) || g(k) == 95) k++
                if (g(k) == 41) i = k + 1
            }
            scanEnd = i; scanText = null
            return if (neg) DEFAULT_NAN else POS_NAN
        }
        val ms = i
        var nd = 0
        while (g(i) in 48..57) { i++; nd++ }
        if (g(i) == 46) { i++; while (g(i) in 48..57) { i++; nd++ } }
        if (nd == 0) { scanEnd = -1; return 0.0 }
        if (low(g(i)) == 101) {
            var k = i + 1
            if (g(k) == 43 || g(k) == 45) k++
            if (g(k) in 48..57) { while (g(k) in 48..57) k++; i = k }
        }
        val text = (if (neg) "-" else "") + String(b, ms, i - ms, Charsets.ISO_8859_1)
        scanText = text
        scanEnd = i
        return text.toDouble()                     // correctly rounded, as glibc's strtod
    }
    @JvmStatic fun strtod(s: CPtr?, end: ORef<CPtr?>?): Double {
        val v = scanDouble(s!!.b, s.o, -1, true)
        end?.set(if (scanEnd < 0) s else CPtr(s.b, scanEnd))
        return if (scanEnd < 0) 0.0 else v
    }
    @JvmStatic fun atof(s: CPtr?): Double = strtod(s, null)
    @JvmStatic fun atoi(s: CPtr?): Int {
        val b = s!!.b
        var i = s.o
        while (b[i].toInt() != 0 && isSpace(b[i].toInt())) i++
        var neg = false
        if (b[i].toInt() == 43 || b[i].toInt() == 45) { neg = b[i].toInt() == 45; i++ }
        var v = 0L
        var over = false
        while (b[i] in 48..57) {
            if (v > (Long.MAX_VALUE - (b[i] - 48)) / 10) over = true
            else v = v * 10 + (b[i] - 48)
            i++
        }
        if (over) v = if (neg) Long.MIN_VALUE else Long.MAX_VALUE
        else if (neg) v = -v
        return v.toInt()                           // strtol's long, then (int): keeps the low 32 bits
    }

    // ------------------------------------------------------------ sscanf
    private fun ch(b: ByteArray, j: Int): Int = if (j < b.size) b[j].toInt() and 255 else 0
    @JvmStatic fun sscanf(s: CPtr?, fmt: CPtr?, vararg args: Any?): Int {
        val b = s!!.b
        var i = s.o
        val f = bytesOf(fmt)
        var k = 0; var ai = 0; var count = 0
        var anyInput = false
        val n = b.size; val nf = f.size
        while (k < nf) {
            val fc = f[k].toInt() and 255
            if (isSpace(fc)) {
                while (k < nf && isSpace(f[k].toInt())) k++
                while (ch(b, i) != 0 && isSpace(ch(b, i))) i++
                continue
            }
            if (fc != 37) {
                if (ch(b, i) == 0) return if (anyInput || count > 0) count else -1
                if (ch(b, i) != fc) return count
                i++; k++
                continue
            }
            k++
            if (f[k].toInt() == 37) {
                while (ch(b, i) != 0 && isSpace(ch(b, i))) i++
                if (ch(b, i) != 37) return count
                i++; k++
                continue
            }
            var suppress = false
            if (f[k].toInt() == 42) { suppress = true; k++ }
            var width = 0
            while (k < nf && f[k] >= 48 && f[k] <= 57) { width = width * 10 + f[k] - 48; k++ }
            var lmod = 0
            while (k < nf && (f[k].toInt() == 108 || f[k].toInt() == 104 || f[k].toInt() == 76)) { if (f[k].toInt() == 108) lmod++; k++ }
            val conv = f[k].toInt() and 255
            k++
            if (conv != 99 && conv != 91 && conv != 110) while (ch(b, i) != 0 && isSpace(ch(b, i))) i++
            if (ch(b, i) == 0 && conv != 110) return if (count == 0 && !anyInput) -1 else count
            if (conv == 100 || conv == 105 || conv == 117) {
                var j = i
                val lim = if (width != 0) i + width else n
                var neg = false
                if (j < lim && (ch(b, j) == 43 || ch(b, j) == 45)) { neg = ch(b, j) == 45; j++ }
                val ds = j
                var v = 0L
                var over = false
                while (j < lim && ch(b, j) >= 48 && ch(b, j) <= 57) {
                    val dgt = ch(b, j) - 48
                    if (v > (Long.MAX_VALUE - dgt) / 10) over = true else v = v * 10 + dgt
                    j++
                }
                if (j == ds) return count
                i = j
                anyInput = true
                if (over) v = if (neg) Long.MIN_VALUE else Long.MAX_VALUE
                else if (neg) v = -v
                if (!suppress) {
                    val a = args[ai++]
                    when (a) {
                        is IRef -> a.set(v.toInt())
                        is LRef -> a.set(v)
                        else -> throw IllegalArgumentException("sscanf %d target $a")
                    }
                    count++
                }
            } else if (conv == 102 || conv == 101 || conv == 103) {
                val v = scanDouble(b, i, if (width != 0) width else -1, false)
                if (scanEnd < 0) return count
                i = scanEnd
                anyInput = true
                if (!suppress) {
                    val a = args[ai++]
                    when (a) {
                        is DRef -> a.set(v)
                        is FRef -> a.set(scanText?.toFloat() ?: v.toFloat())
                        else -> throw IllegalArgumentException("sscanf %f target $a")
                    }
                    count++
                }
            } else if (conv == 115) {
                var j = i
                val w = if (width != 0) width else 1 shl 30
                var m = 0
                while (ch(b, j) != 0 && !isSpace(ch(b, j)) && m < w) { j++; m++ }
                if (!suppress) {
                    val d = args[ai++] as CPtr
                    System.arraycopy(b, i, d.b, d.o, m)
                    d.b[d.o + m] = 0
                    count++
                }
                i = j
                anyInput = true
            } else if (conv == 99) {
                val cnt = if (width != 0) width else 1
                if (ch(b, i) == 0) return if (count == 0 && !anyInput) -1 else count
                val a = if (suppress) null else args[ai++]
                var t = 0
                while (t < cnt && ch(b, i) != 0) {
                    if (!suppress) {
                        if (a is CPtr) a.b[a.o + t] = b[i]
                        else (a as IRef).set(b[i].toInt())
                    }
                    i++; t++
                }
                anyInput = true
                if (!suppress) count++
            } else {
                throw IllegalArgumentException("sscanf %" + conv.toChar())
            }
        }
        return count
    }

    // ------------------------------------------------------------ printf
    private fun fixedDigits(a: Double, prec: Int, alt: Boolean): String {
        var s = BigDecimal(a).setScale(prec, RoundingMode.HALF_EVEN).toPlainString()
        if (prec == 0 && alt) s += "."
        return s
    }
    /** digits of a rounded to prec+1 significant digits; X[0] gets the decimal exponent */
    private fun expRaw(a: Double, prec: Int, X: IntArray): String {
        if (a == 0.0) { X[0] = 0; return "0".repeat(prec + 1) }
        val r = BigDecimal(a).round(MathContext(prec + 1, RoundingMode.HALF_EVEN))
        var d = r.unscaledValue().toString()
        val e = d.length - 1 - r.scale()
        if (d.length < prec + 1) d += "0".repeat(prec + 1 - d.length)
        else if (d.length > prec + 1) d = d.substring(0, prec + 1)
        X[0] = e
        return d
    }
    private fun expDigits(a: Double, prec: Int, alt: Boolean, upper: Boolean): String {
        val X = IntArray(1)
        val d = expRaw(a, prec, X)
        val mant = d.substring(0, 1) + (if (prec > 0) "." + d.substring(1) else (if (alt) "." else ""))
        val ax = Math.abs(X[0])
        return mant + (if (upper) "E" else "e") + (if (X[0] < 0) "-" else "+") + (if (ax < 10) "0$ax" else ax.toString())
    }
    private fun formatFloat(a: Double, conv: Int, prec: Int, alt: Boolean): String {
        val lc = conv or 0x20
        if (lc == 'f'.code) return fixedDigits(a, prec, alt)
        if (lc == 'e'.code) return expDigits(a, prec, alt, conv == 'E'.code)
        val P = if (prec == 0) 1 else prec
        val X = IntArray(1)
        if (a != 0.0) expRaw(a, P - 1, X)
        var s = if (P > X[0] && X[0] >= -4) fixedDigits(a, P - 1 - X[0], alt) else expDigits(a, P - 1, alt, conv == 'G'.code)
        if (!alt) {
            val ep = maxOf(s.indexOf('e'), s.indexOf('E'))
            var mant = if (ep >= 0) s.substring(0, ep) else s
            val ex = if (ep >= 0) s.substring(ep) else ""
            if (mant.indexOf('.') >= 0) {
                var e = mant.length
                while (e > 0 && mant[e - 1] == '0') e--
                if (e > 0 && mant[e - 1] == '.') e--
                mant = mant.substring(0, e)
            }
            s = mant + ex
        }
        return s
    }
    private fun toLong(a: Any?): Long = when (a) {
        is Int -> a.toLong()
        is Long -> a
        is Double -> a.toLong()
        is Float -> a.toLong()
        else -> throw IllegalArgumentException("printf integer arg $a")
    }
    private fun toDouble(a: Any?): Double = when (a) {
        is Double -> a
        is Float -> a.toDouble()
        is Int -> a.toDouble()
        is Long -> a.toDouble()
        else -> throw IllegalArgumentException("printf float arg $a")
    }
    @JvmStatic fun format(fmt: CPtr?, args: Array<out Any?>): String {
        val f = fmt!!.b
        var k = fmt.o
        var ai = 0
        val o = StringBuilder()
        while (f[k].toInt() != 0) {
            if (f[k].toInt() != 37) { o.append((f[k].toInt() and 255).toChar()); k++; continue }
            k++
            if (f[k].toInt() == 37) { o.append('%'); k++; continue }
            var left = false; var plus = false; var space = false; var alt = false; var zero = false
            while (true) {
                when (f[k].toInt().toChar()) {
                    '-' -> left = true
                    '+' -> plus = true
                    ' ' -> space = true
                    '#' -> alt = true
                    '0' -> zero = true
                    else -> break
                }
                k++
            }
            var width = 0
            var prec = -1
            if (f[k].toInt() == '*'.code) { width = toLong(args[ai++]).toInt(); if (width < 0) { left = true; width = -width }; k++ }
            else while (f[k] >= '0'.code.toByte() && f[k] <= '9'.code.toByte()) { width = width * 10 + f[k] - '0'.code; k++ }
            if (f[k].toInt() == '.'.code) {
                k++; prec = 0
                if (f[k].toInt() == '*'.code) { prec = toLong(args[ai++]).toInt(); if (prec < 0) prec = -1; k++ }
                else while (f[k] >= '0'.code.toByte() && f[k] <= '9'.code.toByte()) { prec = prec * 10 + f[k] - '0'.code; k++ }
            }
            while (f[k].toInt().toChar() in "lhLzjtq") k++
            val conv = f[k].toInt()
            k++
            var body: String
            var sign = ""
            var numeric = true
            when (conv.toChar()) {
                'd', 'i' -> {
                    val v = toLong(args[ai++])
                    if (v < 0) sign = "-" else if (plus) sign = "+" else if (space) sign = " "
                    body = if (v == Long.MIN_VALUE) "9223372036854775808" else Math.abs(v).toString()
                    if (prec >= 0) { zero = false; if (prec == 0 && v == 0L) body = ""; while (body.length < prec) body = "0$body" }
                }
                'u', 'x', 'X', 'o' -> {
                    var v = toLong(args[ai++])
                    if (args[ai - 1] is Int) v = v and 0xFFFFFFFFL
                    body = when (conv.toChar()) {
                        'u' -> java.lang.Long.toUnsignedString(v)
                        'o' -> java.lang.Long.toOctalString(v)
                        'x' -> java.lang.Long.toHexString(v)
                        else -> java.lang.Long.toHexString(v).uppercase()
                    }
                    if (prec >= 0) { zero = false; if (prec == 0 && v == 0L) body = ""; while (body.length < prec) body = "0$body" }
                }
                'c' -> {
                    numeric = false
                    body = (toLong(args[ai++]) and 255).toInt().toChar().toString()
                }
                's' -> {
                    numeric = false
                    val p = args[ai++] as CPtr?
                    var s = if (p == null) "(null)" else strOf(p)
                    if (prec >= 0 && s.length > prec) s = s.substring(0, prec)
                    body = s
                }
                'f', 'F', 'e', 'E', 'g', 'G' -> {
                    val v = toDouble(args[ai++])
                    if (prec < 0) prec = 6
                    if ((v.toRawBits() ushr 63) != 0L) sign = "-" else if (plus) sign = "+" else if (space) sign = " "
                    if (v.isNaN() || v.isInfinite()) {
                        body = if (v.isNaN()) "nan" else "inf"
                        if (conv == 'F'.code || conv == 'E'.code || conv == 'G'.code) body = body.uppercase()
                        zero = false
                    } else body = formatFloat(Math.abs(v), conv, prec, alt)
                }
                else -> throw IllegalArgumentException("printf %" + conv.toChar())
            }
            val len = sign.length + body.length
            if (len >= width) o.append(sign).append(body)
            else if (left) o.append(sign).append(body).append(" ".repeat(width - len))
            else if (zero && numeric) o.append(sign).append("0".repeat(width - len)).append(body)
            else o.append(" ".repeat(width - len)).append(sign).append(body)
        }
        return o.toString()
    }
    @JvmStatic fun fprintf(f: CFile?, fmt: CPtr?, vararg args: Any?): Int {
        val s = format(fmt, args)
        if (f == null) return -1
        val b = s.toByteArray(Charsets.ISO_8859_1)
        f.write(b, 0, b.size)
        return b.size
    }
    @JvmStatic fun snprintf(b: CPtr?, n: Int, fmt: CPtr?, vararg args: Any?): Int {
        val s = format(fmt, args)
        if (n > 0) {
            val m = minOf(s.length, n - 1)
            for (i in 0 until m) b!!.b[b.o + i] = s[i].code.toByte()
            b!!.b[b.o + m] = 0
        }
        return s.length
    }

    // ------------------------------------------------------------ files
    @JvmField val stdout = CFile(null, FileOutputStream(FileDescriptor.out))

    @JvmStatic fun fopen(name: CPtr?, mode: CPtr?): CFile? {
        val fn = strOf(name)
        val m = strOf(mode).replace("t", "").replace("b", "")
        try {
            val file = File(fn)
            if (m.startsWith("r") && !file.isFile) return null
            val raf = RandomAccessFile(file, if (m == "r") "r" else "rw")
            val f = CFile(raf, null)
            if (m.startsWith("w")) raf.setLength(0)
            if (m.startsWith("a")) f.pos = raf.length()
            return f
        } catch (e: IOException) {
            return null
        }
    }
    @JvmStatic fun fclose(f: CFile?): Int {
        if (f == null) return -1
        f.flush()
        if (f.raf != null) { try { f.raf.close() } catch (e: IOException) { return -1 } }
        return 0
    }
    @JvmStatic fun fflush(f: CFile?): Int { f?.flush(); return 0 }
    @JvmStatic fun feof(f: CFile?): Int = if (f!!.eof) 1 else 0
    @JvmStatic fun rewind(f: CFile?) { f!!.flush(); f.pos = 0; f.eof = false }
    @JvmStatic fun fseek(f: CFile?, off: Long, whence: Int): Int {
        f!!.flush()
        val p = if (whence == 0) off else if (whence == 1) f.pos + off else f.size() + off
        if (p < 0) return -1
        f.pos = p
        f.eof = false
        return 0
    }
    @JvmStatic fun ftell(f: CFile?): Long = f!!.pos
    @JvmStatic fun fgets(b: CPtr?, n: Int, f: CFile?): CPtr? {
        var m = 0
        while (m < n - 1) {
            val c = f!!.getc()
            if (c < 0) { f.eof = true; break }
            b!!.b[b.o + m++] = c.toByte()
            if (c == 10) break
        }
        if (m == 0) return null
        b!!.b[b.o + m] = 0
        return b
    }
    @JvmStatic fun remove(name: CPtr?): Int {
        try { Files.delete(Paths.get(strOf(name))); return 0 } catch (e: IOException) { return -1 }
    }
    @JvmStatic fun mkstemp(template: CPtr?): Int {
        val n = strlenI(template)
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val rnd = java.util.Random()
        for (t in 0 until 1000) {
            for (i in n - 6 until n) template!!.s(i, chars[rnd.nextInt(chars.length)].code)
            try {
                Files.createFile(Paths.get(strOf(template)))
                return 3
            } catch (e: FileAlreadyExistsException) {
                continue
            } catch (e: IOException) {
                return -1
            }
        }
        return -1
    }
    @JvmStatic fun realpathOf(p: String): String =
        try { File(p).toPath().toRealPath().toString() } catch (e: IOException) { File(p).absolutePath }

    // binary I/O: one value (its first `size` bytes, little-endian) or an array
    private val io = ByteArray(8)
    private fun put(bits: Long, n: Int) { for (i in 0 until n) io[i] = (bits ushr (8 * i)).toByte() }
    private fun get(n: Int): Long { var v = 0L; for (i in 0 until n) v = v or ((io[i].toLong() and 255L) shl (8 * i)); return v }
    @JvmStatic fun fwrite1(v: Double, size: Int, f: CFile?): Long { put(v.toRawBits(), 8); f!!.write(io, 0, size); return 1 }
    @JvmStatic fun fwrite1(v: Float, size: Int, f: CFile?): Long { put(v.toRawBits().toLong() and 0xFFFFFFFFL, 4); f!!.write(io, 0, size); return 1 }
    @JvmStatic fun fwrite1(v: Int, size: Int, f: CFile?): Long { put(v.toLong(), 4); f!!.write(io, 0, size); return 1 }
    @JvmStatic fun fwrite1(v: Long, size: Int, f: CFile?): Long { put(v, 8); f!!.write(io, 0, size); return 1 }
    @JvmStatic fun fread1(r: DRef?, size: Int, f: CFile?): Long {
        put(r!!.get().toRawBits(), 8)
        if (f!!.read(io, 0, size) < size) { f.eof = true; return 0 }
        r.set(Double.fromBits(get(8)))
        return 1
    }
    @JvmStatic fun fread1(r: FRef?, size: Int, f: CFile?): Long {
        put(r!!.get().toRawBits().toLong(), 4)
        if (f!!.read(io, 0, size) < size) { f.eof = true; return 0 }
        r.set(Float.fromBits(get(4).toInt()))
        return 1
    }
    @JvmStatic fun fread1(r: IRef?, size: Int, f: CFile?): Long {
        put(r!!.get().toLong(), 4)
        if (f!!.read(io, 0, size) < size) { f.eof = true; return 0 }
        r.set(get(4).toInt())
        return 1
    }
    @JvmStatic fun fread1(r: LRef?, size: Int, f: CFile?): Long {
        put(r!!.get(), 8)                          // a short read keeps the upper bytes of the old value
        if (f!!.read(io, 0, size) < size) { f.eof = true; return 0 }
        r.set(get(8))
        return 1
    }
    @JvmStatic fun fwrite(a: DoubleArray?, off: Int, n: Int, f: CFile?): Long { for (i in 0 until n) fwrite1(a!![off + i], 8, f); return n.toLong() }
    @JvmStatic fun fwrite(a: FloatArray?, off: Int, n: Int, f: CFile?): Long { for (i in 0 until n) fwrite1(a!![off + i], 4, f); return n.toLong() }
    @JvmStatic fun fwrite(a: IntArray?, off: Int, n: Int, f: CFile?): Long { for (i in 0 until n) fwrite1(a!![off + i], 4, f); return n.toLong() }
    @JvmStatic fun fwrite(a: LongArray?, off: Int, n: Int, f: CFile?): Long { for (i in 0 until n) fwrite1(a!![off + i], 8, f); return n.toLong() }
    @JvmStatic fun fwrite(p: CPtr?, off: Int, n: Int, f: CFile?): Long { f!!.write(p!!.b, p.o + off, n); return n.toLong() }
    @JvmStatic fun fread(a: DoubleArray?, off: Int, n: Int, f: CFile?): Long {
        for (i in 0 until n) { if (f!!.read(io, 0, 8) < 8) { f.eof = true; return i.toLong() }; a!![off + i] = Double.fromBits(get(8)) }
        return n.toLong()
    }
    @JvmStatic fun fread(a: FloatArray?, off: Int, n: Int, f: CFile?): Long {
        for (i in 0 until n) { if (f!!.read(io, 0, 4) < 4) { f.eof = true; return i.toLong() }; a!![off + i] = Float.fromBits(get(4).toInt()) }
        return n.toLong()
    }
    @JvmStatic fun fread(a: IntArray?, off: Int, n: Int, f: CFile?): Long {
        for (i in 0 until n) { if (f!!.read(io, 0, 4) < 4) { f.eof = true; return i.toLong() }; a!![off + i] = get(4).toInt() }
        return n.toLong()
    }
    @JvmStatic fun fread(a: LongArray?, off: Int, n: Int, f: CFile?): Long {
        for (i in 0 until n) { if (f!!.read(io, 0, 8) < 8) { f.eof = true; return i.toLong() }; a!![off + i] = get(8) }
        return n.toLong()
    }
    @JvmStatic fun fread(p: CPtr?, off: Int, n: Int, f: CFile?): Long {
        val got = f!!.read(p!!.b, p.o + off, n)
        if (got < n) f.eof = true
        return got.toLong()
    }

    // ------------------------------------------------------------ time
    @JvmStatic fun time(): Long = System.currentTimeMillis() / 1000
    @JvmStatic fun ctime(t: Long): CPtr {
        val d = java.time.Instant.ofEpochSecond(t).atZone(java.time.ZoneId.systemDefault())
        val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val mons = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        return lit(String.format("%s %s %2d %02d:%02d:%02d %d\n", days[d.dayOfWeek.value - 1], mons[d.monthValue - 1],
                                 d.dayOfMonth, d.hour, d.minute, d.second, d.year))
    }
}
