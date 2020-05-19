package believe.input

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test

internal class InputAdapterImplTest {
    private val startListener: () -> Unit = mock()
    private val endListener: () -> Unit = mock()
    private val inputAdapter = InputAdapterImpl<Int, String> { ACTION }.apply {
        addActionStartListener(ACTION, startListener)
        addActionEndListener(ACTION, endListener)
    }

    @Test
    fun startListeners_returnsCorrectMapOfListeners() {
        assertThat(inputAdapter.startListeners).containsExactly(ACTION, listOf(startListener))
    }

    @Test
    fun endListeners_returnsCorrectMapOfListeners() {
        assertThat(inputAdapter.endListeners).containsExactly(ACTION, listOf(endListener))
    }

    @Test
    fun actionStarted_notifiesListeners() {
        inputAdapter.actionStarted(123)

        verify(startListener).invoke()
    }

    @Test
    fun actionStarted_guardsAgainstConcurrentModification() {
        inputAdapter.addActionStartListener(
            ACTION
        ) { inputAdapter.addActionStartListener(ACTION, mock()) }

        inputAdapter.actionStarted(987)
    }

    @Test
    fun actionEnded_notifiesListeners() {
        inputAdapter.actionEnded(123)

        verify(endListener).invoke()
    }

    @Test
    fun actionEnded_guardsAgainstConcurrentModification() {
        inputAdapter.addActionEndListener(ACTION) {
            inputAdapter.addActionEndListener(ACTION, mock())
        }

        inputAdapter.actionStarted(987)
    }

    @Test
    fun removeListener_removesListenerFromInstance() {
        inputAdapter.removeActionStartListener(ACTION, startListener)
        inputAdapter.removeActionEndListener(ACTION, endListener)

        inputAdapter.actionStarted(456)
        inputAdapter.actionEnded(456)

        assertThat(inputAdapter.startListeners).containsExactly(ACTION, listOf<() -> Unit>())
        assertThat(inputAdapter.endListeners).containsExactly(ACTION, listOf<() -> Unit>())
        verifyZeroInteractions(startListener)
        verifyZeroInteractions(endListener)
    }

    @Test
    fun pushListeners_ignoresPushedListeners() {
        val pushedStartListener: () -> Unit = mock()
        val pushedEndListener: () -> Unit = mock()

        inputAdapter.pushListeners()
        inputAdapter.addActionStartListener(ACTION, pushedStartListener)
        inputAdapter.addActionEndListener(ACTION, pushedEndListener)
        inputAdapter.actionStarted(123)
        inputAdapter.actionEnded(123)

        assertThat(inputAdapter.startListeners).containsExactly(ACTION, listOf(pushedStartListener))
        assertThat(inputAdapter.endListeners).containsExactly(ACTION, listOf(pushedEndListener))
        verifyZeroInteractions(startListener)
        verifyZeroInteractions(endListener)
        verify(pushedStartListener).invoke()
        verify(pushedEndListener).invoke()
    }

    @Test
    fun popListeners_reEnablesPushedListeners() {
        val pushedStartListener: () -> Unit = mock()
        val pushedEndListener: () -> Unit = mock()

        inputAdapter.pushListeners()
        inputAdapter.addActionStartListener(ACTION, pushedStartListener)
        inputAdapter.addActionEndListener(ACTION, pushedEndListener)
        inputAdapter.popListeners()
        inputAdapter.actionStarted(123)
        inputAdapter.actionEnded(123)

        assertThat(inputAdapter.startListeners).containsExactlyEntriesIn(
            mapOf(Pair(ACTION, listOf(pushedStartListener, startListener)))
        )
        assertThat(inputAdapter.endListeners).containsExactlyEntriesIn(
            mapOf(Pair(ACTION, listOf(pushedEndListener, endListener)))
        )
        verify(startListener).invoke()
        verify(endListener).invoke()
        verify(pushedStartListener).invoke()
        verify(pushedEndListener).invoke()
    }

    @Test
    fun popListeners_nothingWasPushed_doesNothing() {
        inputAdapter.popListeners()
        inputAdapter.actionStarted(789)
        inputAdapter.actionEnded(789)

        assertThat(inputAdapter.startListeners).containsExactly(ACTION, listOf(startListener))
        assertThat(inputAdapter.endListeners).containsExactly(ACTION, listOf(endListener))
        verify(startListener).invoke()
        verify(endListener).invoke()
    }

    companion object {
        private const val ACTION = "some action"
    }

    private class FakeListener : () -> Unit {
        internal var wasInvoked = false

        override fun invoke() {
            wasInvoked = true
        }
    }
}