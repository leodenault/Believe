package believe.input.keyboard

import believe.input.InputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import org.newdawn.slick.Input
import org.newdawn.slick.gui.GUIContext

internal class KeyboardInputAdapterTest {
    private val input: Input = mock()
    private val guiContext: GUIContext = mock {
        on { input }.thenReturn(input)
    }
    private val listener: InputAdapter.Listener<String> = mock()
    private val inputAdapter =
        KeyboardInputAdapter.Factory(guiContext).create { MAPPED_OUTPUT }.apply {
            addListener(listener)
        }

    @Test
    fun new_registersListenerOnInput() {
        verify(input).addKeyListener(inputAdapter)
    }

    @Test
    fun isAcceptingInput_returnsTrue() {
        assertThat(inputAdapter.isAcceptingInput).isTrue()
    }

    @Test
    fun keyPressed_notifiesListeners() {
        inputAdapter.keyPressed(123, 'a')

        verify(listener).actionStarted(MAPPED_OUTPUT)
    }

    @Test
    fun keyReleased_notifiesListeners() {
        inputAdapter.keyReleased(123, 'a')

        verify(listener).actionEnded(MAPPED_OUTPUT)
    }


    companion object {
        private const val MAPPED_OUTPUT = "mapped output"
    }
}