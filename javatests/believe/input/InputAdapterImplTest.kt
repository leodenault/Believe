package believe.input

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class InputAdapterImplTest {
    private val listener: InputAdapter.Listener<String> = mock()
    private val inputAdapter =
        InputAdapterImpl<Int, String> { MAPPED_OUTPUT }.apply { addListener(listener) }

    @Test
    fun actionStarted_notifiesListeners() {
        inputAdapter.actionStarted(123)

        verify(listener).actionStarted(MAPPED_OUTPUT)
    }

    @Test
    fun actionEnded_notifiesListeners() {
        inputAdapter.actionEnded(123)

        verify(listener).actionEnded(MAPPED_OUTPUT)
    }

    companion object {
        private const val MAPPED_OUTPUT = "output"
    }
}