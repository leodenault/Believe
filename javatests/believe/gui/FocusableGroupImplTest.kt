package believe.gui

import believe.input.testing.FakeInputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.Test

internal class FocusableGroupImplTest {
    private val inputAdapter = FakeInputAdapter.create<GuiAction>()
    private val focusable1: Focusable = mock()
    private val focusable2: Focusable = mock()
    private val group = FocusableGroupImpl(inputAdapter).also { it.bind() }

    @Test
    fun bind_addsListenersToInputAdapter() {
        assertThat(inputAdapter.startListeners).hasSize(2)
    }

    @Test
    fun unbind_removesListenersFromInputAdapter() {
        group.unbind()

        assertThat(inputAdapter.startListeners[GuiAction.SELECT_UP]).isEmpty()
        assertThat(inputAdapter.startListeners[GuiAction.SELECT_DOWN]).isEmpty()
    }

    @Test
    fun add_firstAddition_focusesAddedFocusable() {
        group.add(focusable1)

        verify(focusable1).focus()
    }

    @Test
    fun actionStarted_noFocusables_doesNothing() {
        inputAdapter.actionStarted(GuiAction.SELECT_UP)
    }

    @Test
    fun actionStarted_singleFocusable_doesNothing() {
        group.add(focusable1)

        inputAdapter.actionStarted(GuiAction.SELECT_UP)
        inputAdapter.actionStarted(GuiAction.SELECT_DOWN)

        // The focusable should have only been focused a single time when it was added to the list.
        verify(focusable1, times(1)).focus()
        verifyNoMoreInteractions(focusable1)
    }

    @Test
    fun actionStarted_multipleFocusables_unfocusesCurrentAndFocusesNew() {
        group.add(focusable1)
        group.add(focusable2)

        inputAdapter.actionStarted(GuiAction.SELECT_DOWN)

        verify(focusable1).focus()
        verify(focusable1).focus()
    }

    @Test
    fun actionStarted_selectsPastEndOfGroup_wrapsAround() {
        group.add(focusable1)
        group.add(focusable2)

        inputAdapter.actionStarted(GuiAction.SELECT_DOWN)
        inputAdapter.actionStarted(GuiAction.SELECT_DOWN)

        verify(focusable1, times(2)).focus()
        verify(focusable2).focus()
    }

    @Test
    fun actionStarted_actionIsUnrecognized_doesNothing() {
        group.add(focusable1)

        inputAdapter.actionStarted(GuiAction.UNKNOWN)

        verify(focusable1, times(1)).focus()
        verifyNoMoreInteractions(focusable1)
    }
}