package believe.statemachine

/**
 * A state machine with multiple internal state machines running concurrently.
 *
 * @param D the type of data encompassing the collective state of the state machine.
 * @param A the type of action used in transitioning states.
 */
interface ConcurrentStateMachineV2<D, A> {
    /**
     * Returns the data stored within this machine's current state.
     *
     * Note that since there are multiple concurrently running state machines, [data] returns the
     * combination of their individual states' data.
     */
    val data: D

    /** Attempts to transition to another state, where the transition is triggered by [action]. */
    fun transition(action: A)
}