package believe.gui

import believe.geometry.Rectangle
import believe.gui.GuiBuilders.verticalLayoutContainer
import believe.gui.testing.DaggerGuiTestComponent
import believe.gui.testing.FakeLayoutBuilder
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Graphics

internal class GuiContainerTest {
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().addGuiConfiguration(GuiContainer.Configuration(mock {
            on { create() } doReturn mock()
        })).build().guiLayoutFactory
    private var layoutBuilder = verticalLayoutContainer { }
    private val graphics: Graphics = mock()
    private val container: GuiElement by lazy {
        layoutFactory.create(
            layoutBuilder, Rectangle(0, 0, 1000, 100)
        )
    }

    @Test
    fun render_rendersChildren() {
        val child: FakeFocusable = mock()
        layoutBuilder = verticalLayoutContainer { +FakeLayoutBuilder<Unit, FakeFocusable>(child) }

        container.render(graphics)

        verify(child).render(graphics)
    }

    @Test
    fun build_correctlyLaysOutChildren() {
        val child1: FakeFocusable = mock()
        val child2: FakeFocusable = mock()
        val child3: FakeFocusable = mock()
        val layoutBuilder1 = FakeLayoutBuilder<Unit, FakeFocusable>(child1)
        val layoutBuilder2 = FakeLayoutBuilder<Unit, FakeFocusable>(child2)
        val layoutBuilder3 = FakeLayoutBuilder<Unit, FakeFocusable>(child3)
        layoutBuilder = verticalLayoutContainer {
            +layoutBuilder1
            +layoutBuilder2
            +layoutBuilder3
        }

        container.render(graphics)

        with(layoutBuilder1) {
            assertThat(receivedPositionData?.x).isEqualTo(250f)
            assertThat(receivedPositionData?.y).isEqualTo(6f)
            assertThat(receivedPositionData?.width).isEqualTo(500f)
            assertThat(receivedPositionData?.height).isEqualTo(25f)
        }
        with(layoutBuilder2) {
            assertThat(receivedPositionData?.x).isEqualTo(250f)
            assertThat(receivedPositionData?.y).isEqualTo(38f)
            assertThat(receivedPositionData?.width).isEqualTo(500f)
            assertThat(receivedPositionData?.height).isEqualTo(25f)
        }
        with(layoutBuilder3) {
            assertThat(receivedPositionData?.x).isEqualTo(250f)
            assertThat(receivedPositionData?.y).isEqualTo(69f)
            assertThat(receivedPositionData?.width).isEqualTo(500f)
            assertThat(receivedPositionData?.height).isEqualTo(25f)
        }
    }

    @Test
    fun build_addsChildrenToFocusableGroup() {
        val child: FakeFocusable = mock()
        val group: FocusableGroup = mock()
        layoutBuilder = verticalLayoutContainer {
            +FakeLayoutBuilder<Unit, FakeFocusable>(child)
            focusableGroup = group
        }

        container

        verify(group).add(child)
    }

    @Test
    fun bind_bindsChildrenAndGroup() {
        val child: FakeFocusable = mock()
        val group: FocusableGroup = mock()
        layoutBuilder = verticalLayoutContainer {
            +FakeLayoutBuilder<Unit, FakeFocusable>(child)
            focusableGroup = group
        }

        container.bind()

        verify(child).bind()
        verify(group).bind()
    }

    @Test
    fun unbind_unbindsChildrenAndGroup() {
        val child: FakeFocusable = mock()
        val group: FocusableGroup = mock()
        layoutBuilder = verticalLayoutContainer {
            +FakeLayoutBuilder<Unit, FakeFocusable>(child)
            focusableGroup = group
        }

        container.unbind()

        verify(child).unbind()
        verify(group).unbind()
    }

    interface FakeFocusable : Focusable, GuiElement
}