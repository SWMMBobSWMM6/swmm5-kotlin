// Refs.kt - C's pointer-to-one-object, for the Kotlin port of EPA SWMM 5.2.4 (a Kotlin port of the Java
// port's Refs.java). A scalar pointer parameter becomes a DRef/IRef/LRef/FRef (double/int/long/float) or an
// ORef<T>; the caller passes a box (a local whose address is taken), an array element, a struct field or a
// static variable. The getters and setters are fun interfaces with primitive signatures, so a field or
// variable reference does not box its value. Written by Claude (Anthropic) for Robert Dickinson; see README.md.
@file:Suppress("FunctionName")
package swmm5

abstract class DRef { abstract fun get(): Double; abstract fun set(v: Double): Double }
abstract class IRef { abstract fun get(): Int; abstract fun set(v: Int): Int }
abstract class LRef { abstract fun get(): Long; abstract fun set(v: Long): Long }
abstract class FRef { abstract fun get(): Float; abstract fun set(v: Float): Float }
abstract class ORef<T> { abstract fun get(): T; abstract fun set(v: T): T }

class DBox(@JvmField var v: Double) : DRef() {
    override fun get(): Double = v
    override fun set(v: Double): Double { this.v = v; return v }
}
class IBox(@JvmField var v: Int) : IRef() {
    override fun get(): Int = v
    override fun set(v: Int): Int { this.v = v; return v }
}
class LBox(@JvmField var v: Long) : LRef() {
    override fun get(): Long = v
    override fun set(v: Long): Long { this.v = v; return v }
}
class FBox(@JvmField var v: Float) : FRef() {
    override fun get(): Float = v
    override fun set(v: Float): Float { this.v = v; return v }
}
class OBox<T>(@JvmField var v: T) : ORef<T>() {
    override fun get(): T = v
    override fun set(v: T): T { this.v = v; return v }
}

class DElem(@JvmField val a: DoubleArray, @JvmField val i: Int) : DRef() {
    override fun get(): Double = a[i]
    override fun set(v: Double): Double { a[i] = v; return v }
}
class IElem(@JvmField val a: IntArray, @JvmField val i: Int) : IRef() {
    override fun get(): Int = a[i]
    override fun set(v: Int): Int { a[i] = v; return v }
}
class LElem(@JvmField val a: LongArray, @JvmField val i: Int) : LRef() {
    override fun get(): Long = a[i]
    override fun set(v: Long): Long { a[i] = v; return v }
}
class FElem(@JvmField val a: FloatArray, @JvmField val i: Int) : FRef() {
    override fun get(): Float = a[i]
    override fun set(v: Float): Float { a[i] = v; return v }
}
class OElem<T>(@JvmField val a: Array<T>, @JvmField val i: Int) : ORef<T>() {
    override fun get(): T = a[i]
    override fun set(v: T): T { a[i] = v; return v }
}

fun interface DGet<T> { fun get(o: T): Double }
fun interface DSet<T> { fun set(o: T, x: Double) }
fun interface IGet<T> { fun get(o: T): Int }
fun interface ISet<T> { fun set(o: T, x: Int) }
fun interface LGet<T> { fun get(o: T): Long }
fun interface LSet<T> { fun set(o: T, x: Long) }
fun interface FGet<T> { fun get(o: T): Float }
fun interface FSet<T> { fun set(o: T, x: Float) }

class DField<T>(@JvmField val o: T, @JvmField val g: DGet<T>, @JvmField val s: DSet<T>) : DRef() {
    override fun get(): Double = g.get(o)
    override fun set(v: Double): Double { s.set(o, v); return v }
}
class IField<T>(@JvmField val o: T, @JvmField val g: IGet<T>, @JvmField val s: ISet<T>) : IRef() {
    override fun get(): Int = g.get(o)
    override fun set(v: Int): Int { s.set(o, v); return v }
}
class LField<T>(@JvmField val o: T, @JvmField val g: LGet<T>, @JvmField val s: LSet<T>) : LRef() {
    override fun get(): Long = g.get(o)
    override fun set(v: Long): Long { s.set(o, v); return v }
}
class FField<T>(@JvmField val o: T, @JvmField val g: FGet<T>, @JvmField val s: FSet<T>) : FRef() {
    override fun get(): Float = g.get(o)
    override fun set(v: Float): Float { s.set(o, v); return v }
}
class OField<T, V>(@JvmField val o: T, @JvmField val g: (T) -> V, @JvmField val s: (T, V) -> Unit) : ORef<V>() {
    override fun get(): V = g(o)
    override fun set(v: V): V { s(o, v); return v }
}

fun interface DSup { fun get(): Double }
fun interface DCon { fun set(x: Double) }
fun interface ISup { fun get(): Int }
fun interface ICon { fun set(x: Int) }
fun interface LSup { fun get(): Long }
fun interface LCon { fun set(x: Long) }
fun interface FSup { fun get(): Float }
fun interface FCon { fun set(x: Float) }

class DAcc(@JvmField val g: DSup, @JvmField val s: DCon) : DRef() {
    override fun get(): Double = g.get()
    override fun set(v: Double): Double { s.set(v); return v }
}
class IAcc(@JvmField val g: ISup, @JvmField val s: ICon) : IRef() {
    override fun get(): Int = g.get()
    override fun set(v: Int): Int { s.set(v); return v }
}
class LAcc(@JvmField val g: LSup, @JvmField val s: LCon) : LRef() {
    override fun get(): Long = g.get()
    override fun set(v: Long): Long { s.set(v); return v }
}
class FAcc(@JvmField val g: FSup, @JvmField val s: FCon) : FRef() {
    override fun get(): Float = g.get()
    override fun set(v: Float): Float { s.set(v); return v }
}
class OAcc<T>(@JvmField val g: () -> T, @JvmField val s: (T) -> Unit) : ORef<T>() {
    override fun get(): T = g()
    override fun set(v: T): T { s(v); return v }
}

class CharRef(@JvmField val p: CPtr, @JvmField val i: Int) : IRef() {
    override fun get(): Int = p.g(i)
    override fun set(v: Int): Int = p.s(i, v)
}
