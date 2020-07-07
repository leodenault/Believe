package believe.gui.testing

import org.newdawn.slick.Color
import org.newdawn.slick.Image
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import java.util.*
import kotlin.math.sqrt

class FakeImage @JvmOverloads constructor(
    width: Int = 0, height: Int = 0, colours: List<List<Int>> = (0 until width).map {
        (0 until height).toList()
    }
) : Image() {

    private val internalColours: List<List<Color>>

    init {
        if (colours.size != width || colours.none { it.size == height }) {
            throw IllegalArgumentException(
                "Color list sizes must match width=$width and height=$height."
            )
        }

        internalColours = colours.map { it.map { colourValue -> Color(colourValue) } }
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getScaledCopy(scale: Float): Image {
        return FakeImage((width * scale).toInt(), (height * scale).toInt())
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FakeImage) {
            return false
        }
        return width == other.width && height == other.height
    }

    override fun hashCode(): Int {
        return Objects.hash(
            Integer.hashCode(width), Integer.hashCode(height)
        )
    }

    override fun getColor(x: Int, y: Int): Color {
        return internalColours[x][y]
    }

    init {
        this.width = width
        this.height = height
    }
}