package believe.geometry

/** A 2-dimensional point on a plane. */
interface Point {
    /** The value of the point's position on the X axis. */
    val x: Float
    /** The value of the point's position on the Y axis. */
    val y: Float
}

/** A [Point] whose values are mutable. */
interface MutablePoint : Point {
    override var x: Float
    override var y: Float
}

private class PointImpl(override var x: Float, override var y: Float) : Point, MutablePoint

/** Returns a [Point] located at {[x], [y]}. */
fun point(x: Float, y: Float): Point = mutablePoint(x, y)

/** Returns a [MutablePoint] located at {[x], [y]}. */
fun mutablePoint(x: Float, y: Float): MutablePoint = PointImpl(x, y)
