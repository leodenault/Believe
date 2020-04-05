package believe.gui

import believe.audio.Sound
import believe.geometry.Rectangle
import believe.gui.GuiBuilders.menuSelection
import believe.gui.testing.DaggerGuiTestComponent
import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test
import org.newdawn.slick.Graphics

internal class MenuSelectionV2Test {
    private val inputAdapter = FakeInputAdapter<GuiAction>()
    private val focusSound: Sound = mock()
    private val selectedSound: Sound = mock()
    private val graphics: Graphics = mock()
    private val layoutFactory: GuiLayoutFactory = DaggerGuiTestComponent.builder()
        .addGuiConfiguration(MenuSelectionV2.Configuration(inputAdapter, focusSound, selectedSound))
        .build().guiLayoutFactory
    private var layoutBuilder: MenuSelectionV2.Builder = menuSelection { }
    private val positionData = Rectangle(100f, 1000f, 1000f, 100f)
    private val menuSelection: MenuSelectionV2 by lazy {
        layoutFactory.create(layoutBuilder, positionData) as MenuSelectionV2
    }

    @Test
    fun new_registersListenerWithInputAdapter() {
        menuSelection.render(graphics) // Builds the menuSelection through lazy invocation.

        assertThat(inputAdapter.listeners).hasSize(1)
    }

    @Test
    fun build_focusableGroupIsDefined_addsMenuSelectionToGroup() {
        val focusableGroup: FocusableGroup = mock()
        layoutBuilder = menuSelection { this.focusableGroup = focusableGroup }

        menuSelection.render(graphics) // Builds the menuSelection through lazy invocation.

        verify(focusableGroup).add(menuSelection)
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
    fun render_successfullyRendersMenuSelection() {
        menuSelection.render(graphics)

        verify(graphics).fill(positionData)
    }
}
