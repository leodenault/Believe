package believe.gui

import believe.audio.Sound
import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.gui.GuiBuilders.menuSelection
import believe.gui.testing.DaggerGuiTestComponent
import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.newdawn.slick.Font

internal class MenuSelectionV2Test {
    private val inputAdapter = FakeInputAdapter.create<GuiAction>()
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

        // Bind was called in the lazy invocation.
        assertThat(inputAdapter.startListeners[GuiAction.EXECUTE_ACTION]).hasSize(1)

        menuSelection.unbind()

        assertThat(inputAdapter.startListeners[GuiAction.EXECUTE_ACTION]).isEmpty()
    }

    @Test
    fun inputActionStarted_menuSelectionIsFocusedAndActionIsExecute_executesAction() {
        val selectionAction = mock<() -> Unit>()
        layoutBuilder = menuSelection { executeSelectionAction = selectionAction }

        menuSelection.focus()
        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        verify(selectedSound).play()
        verify(selectionAction).invoke()
    }

    @Test
    fun inputActionStarted_menuSelectionIsNotFocused_doesNotExecuteAction() {
        val selectionAction = mock<() -> Unit>()
        layoutBuilder = menuSelection { executeSelectionAction = selectionAction }
        menuSelection.render(graphics)

        inputAdapter.actionStarted(GuiAction.EXECUTE_ACTION)

        verifyZeroInteractions(selectionAction)
    }

    @Test
    fun focus_playsFocusSound() {
        menuSelection.focus()

        verify(focusSound).play()
    }

    @Test
    fun focus_highlightsText() {
        val textDisplay: TextDisplay = mock()
        layoutBuilder = menuSelection { createTextDisplay = { _, _, _ -> textDisplay } }

        menuSelection.focus()

        verify(textDisplay).highlight()
    }

    @Test
    fun unfocus_unhighlightsText() {
        val textDisplay: TextDisplay = mock()
        layoutBuilder = menuSelection { createTextDisplay = { _, _, _ -> textDisplay } }

        menuSelection.unfocus()

        verify(textDisplay).unhighlight()
    }

    @Test
    fun render_successfullyRendersMenuSelection() {
        menuSelection.render(graphics)

        verify(graphics).fill(eq(POSITION_DATA), any())
        verify(graphics).draw(eq(with(POSITION_DATA) {
            rectangle(x + 3, y + 3, width - 6, height - 6)
        }), any(), any())
    }

    @Test
    fun render_rendersTextCorrectly() {
        val textDisplay: TextDisplay = mock()
        var actualText = ""
        var actualPositionData = rectangle(x = 0f, y = 0f, width = 0f, height = 0f)
        layoutBuilder = menuSelection {
            +"some text"
            createTextDisplay = { _, positionData, text ->
                textDisplay.also {
                    actualText = text
                    actualPositionData = positionData
                }
            }
        }

        menuSelection.render(graphics)

        assertThat(actualText).isEqualTo("some text")
        assertThat(actualPositionData).isEqualTo(with(POSITION_DATA) {
            rectangle(x + 6, y + 6, width - 12, height - 12)
        })
        verify(textDisplay).render(graphics)
    }

    companion object {
        private val POSITION_DATA = rectangle(100f, 1000f, 1000f, 100f)
    }
}
