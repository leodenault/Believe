package believe.geometry

import kotlin.math.max
import kotlin.math.min

/** An object containing information on a rectangular shape. */
interface Rectangle {
    /** The X value of this [Rectangle]. */
    val x: Float
    /** The minimum X value of this [Rectangle]. */
    val minX: Float
    /** The maximum X value of this [Rectangle]. */
    val maxX: Float
    /** The Y value of this [Rectangle]. */
    val y: Float
    /** The minimum Y value of this [Rectangle]. */
    val minY: Float
    /** The maximum Y value of this [Rectangle]. */
    val maxY: Float
    /** The value of X at the center of this [Rectangle]. */
    val centerX: Float
    /** The value of Y at the center of this [Rectangle]. */
    val centerY: Float
    /** The width of this [Rectangle]. */
    val width: Float
    /** The height of this [Rectangle]. */
    val height: Float
    /** The center of this [Rectangle] expressed as a [FloatPoint]. */
    val center: FloatPoint

    /** Returns whether this [Rectangle] instersects with [other]. */
    fun intersects(other: Rectangle): Boolean

    /** Returns a [Rectangle] that is the intersection of this and [other]. */
    fun intersection(other: Rectangle): Rectangle

    /**
     * Returns true if the direction of the collision is to the right, otherwise false.
     *
     * @param other the rectangle that must move as a result of the collision.
     */
    fun horizontalCollisionDirection(other: Rectangle): Boolean

    /**
     * Returns true if the direction of the collision is down, otherwise false
     *
     * @param other the rectangle that must move as a result of the collision.
     */
    fun verticalCollisionDirection(other: Rectangle): Boolean

    /** Returns a [org.newdawn.slick.geom.Rectangle] based on this instance. */
    fun asSlickRectangle(): org.newdawn.slick.geom.Rectangle
}

/** An object containing information on a rectangular shape with mutable properties. */
interface MutableRectangle : Rectangle {
    override var x: Float
    override var y: Float
    override var centerX: Float
    override var centerY: Float
    override var width: Float
    override var height: Float
}

private class RectangleImpl internal constructor(
    private val internalRect: org.newdawn.slick.geom.Rectangle
) : Rectangle, MutableRectangle {

    override var x: Float
        get() = internalRect.x
        set(value) {
            internalRect.x = value
        }
    override val minX: Float
        get() = internalRect.minX
    override val maxX: Float
        get() = internalRect.maxX
    override var y: Float
        get() = internalRect.y
        set(value) {
            internalRect.y = value
        }
    override val minY: Float
        get() = internalRect.minY
    override val maxY: Float
        get() = internalRect.maxY
    override var centerX: Float
        get() = internalRect.centerX
        set(value) {
            internalRect.centerX = value
        }
    override var centerY: Float
        get() = internalRect.centerY
        set(value) {
            internalRect.centerY = value
        }
    override var width: Float
        get() = internalRect.width
        set(value) {
            internalRect.width = value
        }
    override var height: Float
        get() = internalRect.height
        set(value) {
            internalRect.height = value
        }
    override val center: FloatPoint
        get() = DeferredFloatPoint(this::centerX, this::centerY)

    override fun intersects(other: Rectangle): Boolean =
        !(x >= other.maxX || maxX <= other.x || y >= other.maxY || maxY <= other.y)

    /** Returns a [Rectangle] that is the intersection of this and [other]. */
    override fun intersection(other: Rectangle): Rectangle {
        if (!this.intersects(other)) {
            return RectangleImpl(org.newdawn.slick.geom.Rectangle(0f, 0f, 0f, 0f))
        }

        if (other.minX >= minX && other.maxX <= maxX && other.minY >= minY && other.maxY <= maxY) {
            return RectangleImpl(
                org.newdawn.slick.geom.Rectangle(
                    other.x, other.y, other.width, other.height
                )
            )
        } else if (minX >= other.minX && maxX <= other.maxX && minY >= other.minY && maxY <= other.maxY) {
            return RectangleImpl(org.newdawn.slick.geom.Rectangle(x, y, width, height))
        }

        val ix = max(other.minX, minX)
        val iy = max(other.minY, minY)
        val iwidth = min(other.maxX, maxX) - ix
        val iheight = min(other.maxY, maxY) - iy
        return RectangleImpl(org.newdawn.slick.geom.Rectangle(ix, iy, iwidth, iheight))
    }

    override fun horizontalCollisionDirection(other: Rectangle) = other.centerX > this.centerX

    override fun verticalCollisionDirection(other: Rectangle) = other.centerY > this.centerY

    override fun asSlickRectangle() = org.newdawn.slick.geom.Rectangle(x, y, width, height)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rectangle

        return x == other.x && y == other.y && width == other.width && height == other.height
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }

    override fun toString(): String {
        return "Rectangle(x=$x, y=$y, width=$width, height=$height)"
    }

    private class DeferredFloatPoint(
        internal val getX: () -> Float, internal val getY: () -> Float
    ) : FloatPoint {
        override val x: Float
            get() = getX()
        override val y: Float
            get() = getY()
    }
}

/** Returns a [Rectangle] based on [x], [y], [width], and [height]. */
fun rectangle(x: Float, y: Float, width: Float, height: Float): Rectangle =
    mutableRectangle(x, y, width, height)

/** Returns a [Rectangle] based on the properties within [slickRectangle]. */
fun rectangle(slickRectangle: org.newdawn.slick.geom.Rectangle): Rectangle =
    with(slickRectangle) { mutableRectangle(x, y, width, height) }

/** Returns a [MutableRectangle] based on [x], [y], [width], and [height]. */
fun mutableRectangle(x: Float, y: Float, width: Float, height: Float): MutableRectangle =
    RectangleImpl(org.newdawn.slick.geom.Rectangle(x, y, width, height))

/** Returns a [MutableRectangle] based on the properties within [slickRectangle]. */
fun mutableRectangle(slickRectangle: org.newdawn.slick.geom.Rectangle): MutableRectangle =
    with(slickRectangle) { mutableRectangle(x, y, width, height) }
