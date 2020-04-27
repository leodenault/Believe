package believe.input.testing

import believe.input.InputAdapter
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class FakeInputAdapterTest {
    private val listener1: InputAdapter.Listener<String> = mock()
    private val listener2: InputAdapter.Listener<String> = mock()
    private val adapter = FakeInputAdapter<String>().also {
        it.addListener(listener1)
        it.addListener(listener2)
    }

    @Test
    fun listeners_returnsListOfAddedListeners() {
        assertThat(adapter.listeners).containsExactly(listener1, listener2).inOrder()
    }

    @Test
    fun removeListener_removesListenerFromInstance() {
        adapter.removeListener(listener2)

        assertThat(adapter.listeners).containsExactly(listener1)
    }

    @Test
    fun actionStarted_delegatesToListeners() {
        adapter.actionStarted("some action")

        verify(listener1).actionStarted("some action")
        verify(listener2).actionStarted("some action")
    }
}