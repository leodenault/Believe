package believe.input

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.jupiter.api.Test

internal class InputAdapterImplTest {
    private val listener: InputAdapter.Listener<String> = mock()
    private val concurrentlyModifyinglistener = object : InputAdapter.Listener<String> {
        override fun actionStarted(action: String) {
            inputAdapter.addListener(mock())
        }

        override fun actionEnded(action: String) {
            inputAdapter.addListener(mock())
        }
    }
    private val inputAdapter =
        InputAdapterImpl<Int, String> { MAPPED_OUTPUT }.apply { addListener(listener) }

    @Test
    fun actionStarted_notifiesListeners() {
        inputAdapter.actionStarted(123)

        verify(listener).actionStarted(MAPPED_OUTPUT)
    }

    @Test
    fun actionStarted_guardsAgainstConcurrentModification() {
        inputAdapter.addListener(concurrentlyModifyinglistener)

        inputAdapter.actionStarted(987)
    }

    @Test
    fun actionEnded_notifiesListeners() {
        inputAdapter.actionEnded(123)

        verify(listener).actionEnded(MAPPED_OUTPUT)
    }

    @Test
    fun actionEnded_guardsAgainstConcurrentModification() {
        inputAdapter.addListener(concurrentlyModifyinglistener)

        inputAdapter.actionStarted(987)
    }

    @Test
    fun removeListener_removesListenerFromInstance() {
        inputAdapter.removeListener(listener)

        inputAdapter.actionStarted(456)

        verifyZeroInteractions(listener)
    }

    companion object {
        private const val MAPPED_OUTPUT = "output"
    }
}