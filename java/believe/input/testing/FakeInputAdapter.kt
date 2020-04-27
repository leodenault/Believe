package believe.input.testing

import believe.input.InputAdapter

/** A fake implementation of [InputAdapter] for testing. */
class FakeInputAdapter<A> : InputAdapter<A> {
    private val internalListeners: MutableList<InputAdapter.Listener<A>> = mutableListOf()

    /** The list of [InputAdapter.Listener] instances registered with this instance. */
    val listeners
        get() = internalListeners

    override fun addListener(listener: InputAdapter.Listener<A>) {
        internalListeners.add(listener)
    }

    override fun removeListener(listener: InputAdapter.Listener<A>) {
        internalListeners.remove(listener)
    }

    /** Delegates this call to each listener's [InputAdapter.Listener.actionStarted] method. */
    fun actionStarted(action: A) = internalListeners.forEach { it.actionStarted(action) }
}