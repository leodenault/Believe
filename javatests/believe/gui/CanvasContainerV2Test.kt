package believe.gui

import believe.core.display.Graphics
import believe.geometry.Rectangle
import believe.geometry.truth.Truth.assertThat
import believe.gui.GuiBuilders.canvasContainer
import believe.gui.testing.DaggerGuiTestComponent
import believe.gui.testing.FakeLayoutBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class CanvasContainerV2Test {
    private val child1: GuiElement = mock()
    private val child2: GuiElement = mock()
    private val graphics: Graphics = mock()
    private val childBuilder1 = FakeLayoutBuilder<Unit, GuiElement>(child1)
    private val childBuilder2 = FakeLayoutBuilder<Unit, GuiElement>(child2)
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().build().guiLayoutFactory
    private val container: CanvasContainerV2<GuiElement> = layoutFactory.create(
        canvasContainer<GuiElement> {
            add { childBuilder1 from p(0.1f, 0.2f) to p(0.9f, 1f) }
            add { childBuilder2 from p(0f, 0.2f) to p(0.1f, 0.5f) }
        }, Rectangle(100f, 200f, 100f, 200f)
    )

    @Test
    fun build_providesCorrectPositionData() {
        assertThat(childBuilder1.receivedPositionData!!).isWithin(0.1f)
            .of(Rectangle(110f, 240f, 80f, 160f))
        assertThat(childBuilder2.receivedPositionData!!).isWithin(0.1f)
            .of(Rectangle(100f, 240f, 10f, 60f))
    }

    @Test
    fun bind_bindsAllChildren() {
        container.bind()

        verify(child1).bind()
        verify(child2).bind()
    }

    @Test
    fun unbind_unbindsAllChildren() {
        container.unbind()

        verify(child1).unbind()
        verify(child2).unbind()
    }

    @Test
    fun render_rendersAllChildrenCorrectly() {
        container.render(graphics)

        verify(child1).render(graphics)
        verify(child2).render(graphics)
    }
}