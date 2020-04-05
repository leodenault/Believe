package believe.gui.testing

import org.newdawn.slick.Image
import java.util.*

class FakeImage @JvmOverloads constructor(width: Int = 0, height: Int = 0) : Image() {
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

    init {
        this.width = width
        this.height = height
    }
}