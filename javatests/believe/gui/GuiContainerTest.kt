package believe.gui

import believe.core.display.Renderable
import believe.geometry.Rectangle
import believe.gui.GuiBuilders.verticalLayoutContainer
import believe.gui.testing.DaggerGuiTestComponent
import believe.gui.testing.FakeLayoutBuilder
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Graphics

internal class GuiContainerTest {
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().build().guiLayoutFactory
    private var layoutBuilder = verticalLayoutContainer { }
    private val graphics: Graphics = mock()
    private val container: Renderable by lazy {
        layoutFactory.create(
            layoutBuilder, Rectangle(0f, 0f, 1000f, 100f)
        )
    }

    @Test
    fun render_rendersChildren() {
        val child: Renderable = mock()
        layoutBuilder = verticalLayoutContainer { +FakeLayoutBuilder<Unit>(child) }

        container.render(graphics)

        verify(child).render(graphics)
    }

    @Test
    fun build_correctlyLaysOutChildren() {
        val child1: Renderable = mock()
        val child2: Renderable = mock()
        val child3: Renderable = mock()
        val layoutBuilder1 = FakeLayoutBuilder<Unit>(child1)
        val layoutBuilder2 = FakeLayoutBuilder<Unit>(child2)
        val layoutBuilder3 = FakeLayoutBuilder<Unit>(child3)
        layoutBuilder = verticalLayoutContainer {
            +layoutBuilder1
            +layoutBuilder2
            +layoutBuilder3
        }

        container.render(graphics)

        with(layoutBuilder1) {
            assertThat(receivedPositionData?.x).isEqualTo(250f)
            assertThat(receivedPositionData?.y).isEqualTo(6.25f)
            assertThat(receivedPositionData?.width).isEqualTo(500f)
            assertThat(receivedPositionData?.height).isEqualTo(25f)
        }
        with(layoutBuilder2) {
            assertThat(receivedPositionData?.x).isEqualTo(250f)
            assertThat(receivedPositionData?.y).isEqualTo(37.5f)
            assertThat(receivedPositionData?.width).isEqualTo(500f)
            assertThat(receivedPositionData?.height).isEqualTo(25f)
        }
        with(layoutBuilder3) {
            assertThat(receivedPositionData?.x).isEqualTo(250f)
            assertThat(receivedPositionData?.y).isEqualTo(68.75f)
            assertThat(receivedPositionData?.width).isEqualTo(500f)
            assertThat(receivedPositionData?.height).isEqualTo(25f)
        }
    }
}