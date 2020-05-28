package believe.statemachine

/**
 * A state within a state machine that holds details about some arbitrary data.
 *
 * @param D the type of data stored within this state.
 * @param A the type of action used in transitioning states.
 */
interface StateV2<D, A> {
    /**
     * The data stored within this state used in executing code within the context of the
     * state machine.
     */
    val data: D
    /**
     * The mapping of actions to states that represent valid transitions from this
     * state to other states.
     */
    val transitions: Map<A, StateV2<D, A>>
}
