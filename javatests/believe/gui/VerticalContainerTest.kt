package believe.gui

import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.gui.GuiBuilders.verticalContainer
import believe.gui.testing.DaggerGuiTestComponent
import believe.gui.testing.FakeLayoutBuilder
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class VerticalContainerTest {
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().addGuiConfiguration(VerticalContainer.Configuration(mock {
            on { create() } doReturn mock()
        })).build().guiLayoutFactory
    private var layoutBuilder = verticalContainer { }
    private val graphics: Graphics = mock()
    private val container: GuiElement by lazy {
        layoutFactory.create(
            layoutBuilder, rectangle(0f, 0f, 1000f, 100f)
        )
    }

    @Test
    fun render_rendersChildren() {
        val child: FakeFocusable = mock()
        layoutBuilder = verticalContainer { +FakeLayoutBuilder<Unit, FakeFocusable>(child) }

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
        layoutBuilder = verticalContainer {
            +layoutBuilder1
            +layoutBuilder2
            +layoutBuilder3
        }

        container.render(graphics)

        assertThat(layoutBuilder1.receivedPositionData).isEqualTo(rectangle(250f, 6.25f, 500f, 25f))
        assertThat(layoutBuilder2.receivedPositionData).isEqualTo(rectangle(250f, 37.5f, 500f, 25f))
        assertThat(layoutBuilder3.receivedPositionData).isEqualTo(
            rectangle(
                250f,
                68.75f,
                500f,
                25f
            )
        )
    }

    @Test
    fun build_addsChildrenToFocusableGroup() {
        val child: FakeFocusable = mock()
        val group: FocusableGroup = mock()
        layoutBuilder = verticalContainer {
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
        layoutBuilder = verticalContainer {
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
        layoutBuilder = verticalContainer {
            +FakeLayoutBuilder<Unit, FakeFocusable>(child)
            focusableGroup = group
        }

        container.unbind()

        verify(child).unbind()
        verify(group).unbind()
    }

    interface FakeFocusable : Focusable, GuiElement
}