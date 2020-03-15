package believe.input

/**
 * An adapter that wraps Slick's input system.
 *
 * @param A the format of the input signal understood by this instance's listeners.
 */
interface InputAdapter<A> {
    /**
     * An object that listens to input events generated by an [InputAdapter].
     *
     * @param A the type of input action supported by this instance.
     */
    interface Listener<A> {
        /**
         * Signals that the input system has started sending a signal corresponding to [action].
         */
        fun actionStarted(action: A)

        /**
         * Signals that the input system has stopped sending a signal corresponding to [action].
         */
        fun actionEnded(action: A)
    }

    /** Adds [listener] to this instance so that it may be notified of input events. */
    fun addListener(listener: Listener<A>)
}