package believe.input.testing

import believe.input.InputAdapter
import believe.input.InputAdapterImpl

/** A fake implementation of [InputAdapter] for testing. */
class FakeInputAdapter<A> private constructor(
    private val inputAdapterImpl: InputAdapterImpl<A, A>
) : InputAdapter<A> by inputAdapterImpl {
    val startListeners: Map<A, List<() -> Unit>>
        get() = inputAdapterImpl.startListeners
    val endListeners: Map<A, List<() -> Unit>>
        get() = inputAdapterImpl.endListeners

    fun actionStarted(action: A) = inputAdapterImpl.actionStarted(action)
    fun actionEnded(action: A) = inputAdapterImpl.actionEnded(action)

    companion object {
        /** Creates a [FakeInputAdapter] for use in testing. */
        @JvmStatic
        fun <A> create(): FakeInputAdapter<A> = FakeInputAdapter(InputAdapterImpl { it })
    }
}
