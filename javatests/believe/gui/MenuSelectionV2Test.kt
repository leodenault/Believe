package believe.gui

import believe.audio.Sound
import believe.core.display.Renderable
import believe.geometry.Rectangle
import believe.gui.GuiBuilders.menuSelection
import believe.gui.testing.DaggerGuiTestComponent
import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.newdawn.slick.Font
import org.newdawn.slick.Graphics

internal class MenuSelectionV2Test {
    private val inputAdapter = FakeInputAdapter<GuiAction>()
    private val font: Font = mock()
    private val focusSound: Sound = mock()
    private val selectedSound: Sound = mock()
    private val graphics: Graphics = mock()
    private val layoutFactory: GuiLayoutFactory =
        DaggerGuiTestComponent.builder().addGuiConfiguration(
            MenuSelectionV2.Configuration(
                inputAdapter, focusSound, selectedSound
            )
        ).addGuiConfiguration(TextBox.Configuration(font)).build().guiLayoutFactory
    private var layoutBuilder: MenuSelectionV2.Builder = menuSelection { }

    private val menuSelection: MenuSelectionV2 by lazy {
        layoutFactory.create(layoutBuilder, POSITION_DATA).apply { bind() }
    }

    @Test
    fun unbind_removesListenerFromInputAdapter() {
        menuSelection.render(graphics) // Builds the menuSelection through lazy invocation.

        assertThat(inputAdapter.listeners).hasSize(1) // Bind was called in the lazy invocation.

        menuSelection.unbind()

        assertThat(inputAdapter.listeners).isEmpty()
    }

    @Test
    fun inputActionStarted_menuSelectionIsFocusedAndActionIsExecute_executesAction() {
        val selectionAction = mock<() -> Unit>()
        layoutBuilder = menuSelection { executeSelectionAction = selectionAction }

        menuSelection.focus()
        inputAdapter.listeners[0].actionStarted(GuiAction.EXECUTE_ACTION)

        verify(selectedSound).play()
        verify(selectionAction).invoke()
    }

    @Test
    fun inputActionStarted_menuSelectionIsNotFocused_doesNotExecuteAction() {
        val selectionAction = mock<() -> Unit>()
        layoutBuilder = menuSelection { executeSelectionAction = selectionAction }
        menuSelection.render(graphics)

        inputAdapter.listeners[0].actionStarted(GuiAction.EXECUTE_ACTION)

        verifyZeroInteractions(selectionAction)
    }

    @Test
    fun inputActionStarted_actionIsNotExecute_doesNotExecuteAction() {
        val selectionAction = mock<() -> Unit>()
        layoutBuilder = menuSelection { executeSelectionAction = selectionAction }
        menuSelection.render(graphics)

        menuSelection.focus()
        inputAdapter.listeners[0].actionStarted(GuiAction.SELECT_UP)

        verifyZeroInteractions(selectionAction)
    }

    @Test
    fun focus_playsFocusSound() {
        menuSelection.focus()

        verify(focusSound).play()
    }

    @Test
    fun focus_highlightsText() {
        val textDisplay: MenuSelectionV2.TextDisplay = mock()
        layoutBuilder = menuSelection { createTextDisplay = { _, _, _ -> textDisplay } }

        menuSelection.focus()

        verify(textDisplay).highlight()
    }

    @Test
    fun unfocus_unhighlightsText() {
        val textDisplay: MenuSelectionV2.TextDisplay = mock()
        layoutBuilder = menuSelection { createTextDisplay = { _, _, _ -> textDisplay } }

        menuSelection.unfocus()

        verify(textDisplay).unhighlight()
    }

    @Test
    fun render_successfullyRendersMenuSelection() {
        menuSelection.render(graphics)

        verify(graphics).fill(POSITION_DATA)
    }

    @Test
    fun render_rendersTextCorrectly() {
        val textDisplay: MenuSelectionV2.TextDisplay = mock()
        var actualText = ""
        layoutBuilder = menuSelection {
            +"some text"
            createTextDisplay = { _, _, text ->
                textDisplay.also {
                    actualText = text
                }
            }
        }

        menuSelection.render(graphics)

        assertThat(actualText).isEqualTo("some text")
        verify(textDisplay).render(graphics)
    }

    companion object {
        private val POSITION_DATA = Rectangle(100, 1000, 1000, 100)
    }
}
