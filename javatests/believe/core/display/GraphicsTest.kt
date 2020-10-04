package believe.core.display

import believe.animation.proto.AnimationProto
import believe.animation.proto.AnimationProto.Animation.IterationMode
import believe.animation.testing.fakeAnimation
import believe.geometry.rectangle
import believe.gui.testing.FakeImage
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Test
import org.newdawn.slick.Color
import org.newdawn.slick.Font
import org.newdawn.slick.geom.Shape

internal class GraphicsTest {
    private val slickGraphics: org.newdawn.slick.Graphics = mock()
    private val graphics = Graphics(slickGraphics)

    @Test
    fun draw_floorsNumbersAndDelegatesToSlickGraphics() {
        val colour = Color(0x123123)

        graphics.draw(rectangle(1.1f, 2.499f, 3.5f, 4.9f), colour, 2f)

        inOrder(slickGraphics) {
            verify(slickGraphics).color = colour
            verify(slickGraphics).lineWidth = 2f
            verify(slickGraphics).drawRect(1f, 2f, 3f, 4f)
        }
    }

    @Test
    fun fill_fillRectangle_floorsNumbersAndDelegatesToSlickGraphics() {
        val colour = Color(0x123123)

        graphics.fill(rectangle(1.1f, 2.499f, 3.5f, 4.9f), colour)

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
    fun drawImage_floorsNumbersAndDelegatesToSlickGraphics() {
        val image = FakeImage()

        graphics.drawImage(image, 2.499f, 3.9999f)

        verify(slickGraphics).drawImage(image, 2f, 3f)
    }

    @Test
    fun drawAnimation_floorsNumbersAndDelegatesToSlickGraphics() {
        val animation = fakeAnimation(IterationMode.LINEAR, false, 2, 2)

        graphics.drawAnimation(animation, 2.499f, 3.9999f)

        verify(slickGraphics).drawAnimation(any(), eq(2f), eq(3f))
    }

    @Test
    fun popClip_noClipAvailable_returnsNull() {
        assertThat(graphics.popClip()).isNull()
    }

    @Test
    fun popClip_returnsLastPushedClip() {
        val pushedClip1 = rectangle(0f, 0f, 0f, 0f)
        val pushedClip2 = rectangle(10f, 10f, 10f, 10f)
        val graphics = Graphics(fakeSlickGraphics())

        graphics.pushClip(pushedClip1)
        graphics.pushClip(pushedClip2)

        val popClip = graphics.popClip()
        assertThat(popClip).isEqualTo(pushedClip2)
        assertThat(graphics.popClip()).isEqualTo(pushedClip1)
    }

    @Test
    fun pushTransform_delegatesToSlickGraphics() {
        graphics.pushTransform()

        verify(slickGraphics).pushTransform()
    }

    @Test
    fun popTransform_delegatesToSlickGraphics() {
        graphics.popTransform()

        verify(slickGraphics).popTransform()
    }

    @Test
    fun scale_delegatesToSlickGraphics() {
        graphics.scale(123f, 234f)

        verify(slickGraphics).scale(123f, 234f)
    }

    @Test
    fun translate_delegatesToSlickGraphics() {
        graphics.translate(4.999f, 3.45f)

        verify(slickGraphics).translate(4.999f, 3.45f)
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