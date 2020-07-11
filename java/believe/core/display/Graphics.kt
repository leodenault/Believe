package believe.core.display

import believe.geometry.Rectangle
import believe.geometry.rectangle
import org.newdawn.slick.Color
import org.newdawn.slick.Font
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.newdawn.slick.geom.Shape
import java.util.*
import kotlin.math.floor

/**
 * A graphics context used in rendering information to the screen.
 *
 * @param slickGraphics the Slick [Graphics] object this instance will delegate to.
 */
open class Graphics(private val slickGraphics: Graphics) {
    private val clipContexts: Deque<org.newdawn.slick.geom.Rectangle> = ArrayDeque()

    /** Draws the outline of [rectangle] to the screen using [colour] and [lineWidth]. */
    open fun draw(rectangle: Rectangle, colour: Color, lineWidth: Float = 1f) = with(rectangle) {
        slickGraphics.color = colour
        slickGraphics.lineWidth = lineWidth
        slickGraphics.drawRect(floor(x), floor(y), floor(width), floor(height))
    }

    /** Fills the dimensions of [rectangle] on the screen using [colour]. */
    open fun fill(rectangle: Rectangle, colour: Color) = with(rectangle) {
        slickGraphics.color = colour
        slickGraphics.fillRect(floor(x), floor(y), floor(width), floor(height))
    }

    /** Fills the dimensions of [shape] on the screen using [colour]. */
    open fun fill(shape: Shape, colour: Color) {
        slickGraphics.color = colour
        slickGraphics.fill(shape)
    }

    /** Draws [s] to the screen at position ([x], [y]) using [colour] and [font]. */
    open fun drawString(s: String, x: Float, y: Float, colour: Color, font: Font) {
        slickGraphics.color = colour
        slickGraphics.font = font
        slickGraphics.drawString(s, floor(x), floor(y))
    }

    /** Draws [image] to the screen at position ([x], [y]). */
    open fun drawImage(image: Image, x: Float, y: Float) =
        slickGraphics.drawImage(image, floor(x), floor(y))

    /** Sets [clip] as the current rendering border and pushes the old border back. */
    open fun pushClip(clip: Rectangle) {
        if (slickGraphics.clip != null) {
            clipContexts.push(slickGraphics.clip)
        }
        slickGraphics.clip = clip.asSlickRectangle()
    }

    /** Returns the current rendering border and reinstates the previous one. */
    open fun popClip(): Rectangle? = with(slickGraphics) {
        clip?.let { rectangle(clip) }.also {
            clip = clipContexts.poll()
        }
    }

    open fun pushTransform() = slickGraphics.pushTransform()

    open fun popTransform() = slickGraphics.popTransform()

    open fun scale(scaleX: Float, scaleY: Float) = slickGraphics.scale(scaleX, scaleY)

    open fun translate(x: Float, y: Float) = slickGraphics.translate(x, y)
}