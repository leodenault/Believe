package believe.geometry

import kotlin.math.max
import kotlin.math.min

/** An object containing information on a rectangular shape. */
class Rectangle internal constructor(internalRect: org.newdawn.slick.geom.Rectangle) {
    constructor(
        x: Float, y: Float, width: Float, height: Float
    ) : this(org.newdawn.slick.geom.Rectangle(x, y, width, height))

    constructor(configure: XBuilder.() -> Builder) : this(configure(Builder()).innerRectangle)

    /** The X value of this [Rectangle]. */
    val x = internalRect.x
    /** The minimum X value of this [Rectangle]. */
    val minX = internalRect.minX
    /** The maximum X value of this [Rectangle]. */
    val maxX = internalRect.maxX
    /** The Y value of this [Rectangle]. */
    val y = internalRect.y
    /** The minimum Y value of this [Rectangle]. */
    val minY = internalRect.minY
    /** The maximum Y value of this [Rectangle]. */
    val maxY = internalRect.maxY
    /** The value of X at the center of this [Rectangle]. */
    val centerX = internalRect.centerX
    /** The value of Y at the center of this [Rectangle]. */
    val centerY = internalRect.centerY
    /** The width of this [Rectangle]. */
    val width = internalRect.width
    /** The height of this [Rectangle]. */
    val height = internalRect.height

    /** Returns whether this [Rectangle] instersects with [other]. */
    fun intersects(other: Rectangle): Boolean {
        if (x >= other.x + other.width || x + width <= other.x) {
            return false
        }
        return !(y >= other.y + other.height || y + height <= other.y)
    }

    /** Returns a [Rectangle] that is the intersection of this and [other]. */
    fun intersection(other: Rectangle): Rectangle {
        if (!this.intersects(other)) {
            return Rectangle(0f, 0f, 0f, 0f)
        }

        if (other.minX >= minX && other.maxX <= maxX && other.minY >= minY && other.maxY <= maxY) {
            return Rectangle(other.x, other.y, other.width, other.height)
        } else if (minX >= other.minX && maxX <= other.maxX && minY >= other.minY && maxY <= other.maxY) {
            return Rectangle(x, y, width, height)
        }

        val ix = max(other.minX, minX)
        val iy = max(other.minY, minY)
        val iwidth = min(other.maxX, maxX) - ix
        val iheight = min(other.maxY, maxY) - iy
        return Rectangle(ix, iy, iwidth, iheight)
    }

    /**
     * Returns true if the direction of the collision is to the right, otherwise false.
     *
     * @param other the rectangle that must move as a result of the collision.
     */
    fun horizontalCollisionDirection(other: Rectangle) = other.centerX > this.centerX

    /**
     * Returns true if the direction of the collision is down, otherwise false
     *
     * @param other the rectangle that must move as a result of the collision.
     */
    fun verticalCollisionDirection(other: Rectangle) = other.centerY > this.centerY

    /** Returns a [org.newdawn.slick.geom.Rectangle] based on this instance. */
    fun asSlickRectangle() = org.newdawn.slick.geom.Rectangle(x, y, width, height)

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


    /** A builder for defining the X position of a [Rectangle]. */
    interface XBuilder {
        /** Sets the X position of the [Rectangle] to [x]. */
        fun setX(x: Float): YBuilder

        /** Sets the center X position of the [Rectangle] to [centerX]. */
        fun setCenterX(centerX: Float): YBuilder
    }

    /** A builder for defining the Y position of a [Rectangle]. */
    interface YBuilder {
        /** Sets the Y position of the [Rectangle] to [y]. */
        fun setY(y: Float): WidthBuilder

        /** Sets the center Y position of the [Rectangle] to [centerY]. */
        fun setCenterY(centerY: Float): WidthBuilder
    }

    /** A builder for defining the width of a [Rectangle]. */
    interface WidthBuilder {
        /** Sets the width of the [Rectangle] to [width]. */
        fun setWidth(width: Float): HeightBuilder
    }

    /** A builder for defining the height of a [Rectangle]. */
    interface HeightBuilder {
        /** Sets the height of the [Rectangle] to [height]. */
        fun setHeight(height: Float): Builder
    }

    /** A builder of [Rectangle] instances. */
    class Builder internal constructor() : XBuilder, YBuilder, WidthBuilder, HeightBuilder {
        internal val innerRectangle = org.newdawn.slick.geom.Rectangle(0f, 0f, 0f, 0f)

        override fun setX(x: Float): YBuilder = this.also { innerRectangle.x = x }

        override fun setCenterX(centerX: Float): YBuilder =
            this.also { innerRectangle.centerX = centerX }

        override fun setY(y: Float): WidthBuilder = this.also { innerRectangle.y = y }

        override fun setCenterY(centerY: Float): WidthBuilder =
            this.also { innerRectangle.centerY = centerY }

        override fun setWidth(width: Float): HeightBuilder =
            this.also { innerRectangle.width = width }

        override fun setHeight(height: Float): Builder =
            this.also { innerRectangle.height = height }

        fun build() = Rectangle(innerRectangle)
    }

    companion object {
        /** Constructs a [Rectangle] from a Slick [org.newdawn.slick.geom.Rectangle]. */
        @JvmStatic
        fun from(slickRectangle: org.newdawn.slick.geom.Rectangle) = with(slickRectangle) {
            Rectangle(x, y, width, height)
        }

        /** Returns a new builder instance for constructing a [Rectangle]. */
        @JvmStatic
        fun newBuilder(): XBuilder = Builder()
    }
}
