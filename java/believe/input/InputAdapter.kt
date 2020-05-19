package believe.input

/**
 * An adapter that wraps Slick's input system.
 *
 * @param A the format of the input signal understood by this instance's listeners.
 */
interface InputAdapter<A> {
    /**
     * Adds [listener] to this instance so that it may be notified of input events starting for
     * [action].
     */
    fun addActionStartListener(action: A, listener: () -> Unit)

    /** Removes [listener] from the group of listeners listening for [action] to start. */
    fun removeActionStartListener(action: A, listener: () -> Unit)

    /**
     * Adds [listener] to this instance so that it may be notified of input events ending for
     * [action].
     */
    fun addActionEndListener(action: A, listener: () -> Unit)

    /** Removes [listener] from the group of listeners listening for [action] to end. */
    fun removeActionEndListener(action: A, listener: () -> Unit)

    /**
     * Pushes the listeners currently registered with this instance onto the front of a stack,
     * temporarily disabling their ability to receive input from this instance.
     */
    fun pushListeners()

    /**
     * Pops the last group of listeners previously registered with this instance from a stack,
     * re-enabling their ability to receive input from this instance.
     *
     * **Note**: This operation does **not** remove any listeners currently registered with this
     * instance.
     */
    fun popListeners()
}