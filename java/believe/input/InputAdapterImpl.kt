package believe.input

/**
 * Default implementation of [InputAdapter].
 *
 * @param mapInput maps input from data of type [A] to data of type [B].
 * @param A the format of the input signal provided by Slick.
 * @param B the format of the input signal understood by this instance's listeners.
 */
class InputAdapterImpl<A, B>(private val mapInput: (A) -> B) : InputAdapter<B> {
    private val listeners = mutableListOf<InputAdapter.Listener<B>>()

    override fun addListener(listener: InputAdapter.Listener<B>) {
        listeners.add(listener)
    }

    /** Signals that the input system has started sending a signal corresponding to [action]. */
    fun actionStarted(action: A) {
        listeners.forEach { it.actionStarted(mapInput(action)) }
    }

    /** Signals that the input system has stopped sending a signal corresponding to [action]. */
    fun actionEnded(action: A) {
        listeners.forEach { it.actionEnded(mapInput(action)) }
    }
}