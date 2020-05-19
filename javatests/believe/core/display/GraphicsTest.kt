package believe.core.display

import believe.geometry.Rectangle
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doReturnConsecutively
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import org.newdawn.slick.Color
import org.newdawn.slick.Font
import org.newdawn.slick.geom.Shape
import org.newdawn.slick.geom.Transform
import org.w3c.dom.css.Rect

internal class GraphicsTest {
    private val slickGraphics: org.newdawn.slick.Graphics = mock()
    private val graphics = Graphics(slickGraphics)

    @Test
    fun draw_floorsNumbersAndDelegatesToSlickGraphics() {
        val colour = Color(0x123123)

        graphics.draw(Rectangle(1.1f, 2.499f, 3.5f, 4.9f), colour, 2f)

        inOrder(slickGraphics) {
            verify(slickGraphics).color = colour
            verify(slickGraphics).lineWidth = 2f
            verify(slickGraphics).drawRect(1f, 2f, 3f, 4f)
        }
    }

    @Test
    fun fill_fillRectangle_floorsNumbersAndDelegatesToSlickGraphics() {
        val colour = Color(0x123123)

        graphics.fill(Rectangle(1.1f, 2.499f, 3.5f, 4.9f), colour)

        inOrder(slickGraphics) {
            verify(slickGraphics).color = colour
            verify(slickGraphics).fillRect(1f, 2f, 3f, 4f)
        }
    }

    @Test
    fun fill_fillShape_delegatesToSlickGraphics() {
        val colour = Color(0x123123)
        val shape: Shape = mock()

        graphics.fill(shape, colour)

        inOrder(slickGraphics) {
            verify(slickGraphics).color = colour
            verify(slickGraphics).fill(shape)
        }
    }

    @Test
    fun drawString_floorsNumbersAndDelegatesToSlickGraphics() {
        val colour = Color(0x123123)
        val font: Font = mock()

        graphics.drawString("some string", 3.499f, 4.5f, colour, font)

        inOrder(slickGraphics) {
            verify(slickGraphics).color = colour
            verify(slickGraphics).font = font
            verify(slickGraphics).drawString("some string", 3f, 4f)
        }
    }

    @Test
    fun popClip_noClipAvailable_returnsNull() {
        assertThat(graphics.popClip()).isNull()
    }

    @Test
    fun popClip_returnsLastPushedClip() {
        val pushedClip1 = Rectangle(0f, 0f, 0f, 0f)
        val pushedClip2 = Rectangle(10f, 10f, 10f, 10f)
        val graphics = Graphics(fakeSlickGraphics())

        graphics.pushClip(pushedClip1)
        graphics.pushClip(pushedClip2)

        val popClip = graphics.popClip()
        assertThat(popClip).isEqualTo(pushedClip2)
        assertThat(graphics.popClip()).isEqualTo(pushedClip1)
    }

    companion object {
        fun fakeSlickGraphics() = object : org.newdawn.slick.Graphics() {
            private var clip: org.newdawn.slick.geom.Rectangle? = null

            override fun setClip(rect: org.newdawn.slick.geom.Rectangle?) {
                clip = rect
            }

            override fun getClip(): org.newdawn.slick.geom.Rectangle? = clip
        }
    }
}