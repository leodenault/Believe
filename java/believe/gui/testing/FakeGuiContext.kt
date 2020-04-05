package believe.gui.testing

import dagger.Reusable
import org.lwjgl.input.Cursor
import org.newdawn.slick.Color
import org.newdawn.slick.Font
import org.newdawn.slick.Input
import org.newdawn.slick.SlickException
import org.newdawn.slick.gui.GUIContext
import org.newdawn.slick.opengl.ImageData
import javax.inject.Inject

/** Fake implementation of [GUIContext] for use within tests.  */
@Reusable
class FakeGuiContext constructor(private val width: Int, private val height: Int) : GUIContext {
    @Inject
    internal constructor() : this(width = 0, height = 0)

    override fun getInput(): Input = INPUT

    override fun getTime() = 0L

    override fun getScreenWidth() = 0

    override fun getScreenHeight() = 0

    override fun getWidth(): Int = width

    override fun getHeight(): Int = height

    override fun getDefaultFont() = DEFAULT_FONT

    @Throws(SlickException::class)
    override fun setMouseCursor(s: String, i: Int, i1: Int) {
    }

    @Throws(SlickException::class)
    override fun setMouseCursor(
        imageData: ImageData, i: Int, i1: Int
    ) {
    }

    @Throws(SlickException::class)
    override fun setMouseCursor(
        cursor: Cursor, i: Int, i1: Int
    ) {
    }

    override fun setDefaultMouseCursor() {}

    companion object {
        private val INPUT = Input(0)
        private val DEFAULT_FONT: Font = object : Font {
            override fun getWidth(s: String) = 0

            override fun getHeight(s: String) = 0

            override fun getLineHeight() = 0

            override fun drawString(
                v: Float, v1: Float, s: String
            ) {
            }

            override fun drawString(
                v: Float, v1: Float, s: String, color: Color
            ) {
            }

            override fun drawString(
                v: Float, v1: Float, s: String, color: Color, i: Int, i1: Int
            ) {
            }
        }
    }

}