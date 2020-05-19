package believe.input.keyboard

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
    private val startListener: () -> Unit = mock()
    private val endListener: () -> Unit = mock()
    private val inputAdapter =
        KeyboardInputAdapter.Factory(guiContext).create { ACTION }.apply {
            addActionStartListener(ACTION, startListener)
            addActionEndListener(ACTION, endListener)
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

        verify(startListener).invoke()
    }

    @Test
    fun keyReleased_notifiesListeners() {
        inputAdapter.keyReleased(123, 'a')

        verify(endListener).invoke()
    }


    companion object {
        private const val ACTION = "mapped output"
    }
}