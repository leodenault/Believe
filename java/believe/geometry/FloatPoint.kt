package believe.geometry

typealias FloatPoint = Point<Float>
typealias IntPoint = Point<Int>
typealias MutableFloatPoint = MutablePoint<Float>
typealias MutableIntPoint = MutablePoint<Int>

/** A 2-dimensional point on a plane. */
interface Point<out N : Number> {
    /** The value of the point's position on the X axis. */
    val x: N
    /** The value of the point's position on the Y axis. */
    val y: N
}

/** A [FloatPoint] whose values are mutable. */
interface MutablePoint<N : Number> : Point<N> {
    override var x: N
    override var y: N
}

private class PointImpl<N : Number>(
    override var x: N, override var y: N
) : Point<N>, MutablePoint<N>

/** Returns a [FloatPoint] located at {[x], [y]}. */
fun floatPoint(x: Float, y: Float): FloatPoint = mutableFloatPoint(x, y)

/** Returns an [IntPoint] located at {[x], [y]}. */
fun intPoint(x: Int, y: Int): IntPoint = mutableIntPoint(x, y)

/** Returns a [MutableFloatPoint] located at {[x], [y]}. */
fun mutableFloatPoint(x: Float, y: Float): MutableFloatPoint = PointImpl(x, y)

/** Returns a [MutableIntPoint] located at {[x], [y]}. */
fun mutableIntPoint(x: Int, y: Int): MutableIntPoint = PointImpl(x, y)
