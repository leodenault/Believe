package believe.statemachine

/**
 * An entity that transitions between various states, exposing its current stateful data through
 * [data].
 *
 * @param initialState the [StateV2] that will act as this machine's initial state.
 * @param D the type of data stored within this state.
 * @param A the type of action used in transitioning states.
 */
class StateMachineV2<D, A> private constructor(initialState: StateV2<D, A>) {
    private var currentState = initialState

    /** Returns the data stored within this machine's current state. */
    val data
        get() = currentState.data

    /** Attempts to transition to another state, where the transition is triggered by [action]. */
    fun transition(action: A) {
        currentState = currentState.transitions[action] ?: return
    }

    /**
     * A builder for constructing [StateMachineV2] instances.
     *
     * @param initialState the [StateV2] that will act as this machine's initial state.
     */
    class Builder<D, A> constructor(private val initialState: MutableStateV2<D, A>) {
        /**
         * Adds a transition between [state1] and [state2].
         *
         * @param action the action that would trigger the transition.
         * @param state1 the state from which the transition may be triggered.
         * @param state2 the state that the machine will transition to.
         */
        fun addTransition(action: A, state1: MutableStateV2<D, A>, state2: MutableStateV2<D, A>) {
            state1.transitions[action] = state2
        }

        /** Returns a [StateMachineV2] based on the contents of this builder. */
        fun build() = StateMachineV2(initialState)
    }

    companion object {
        /**
         * Returns a [StateMachineV2] based on [initialState] and the contents of the builder.
         *
         * @param initialState the [StateV2] that will act as this machine's initial state.
         * @param configure the function that constructs the [StateMachineV2] using [Builder] as the
         * receiver.
         */
        inline operator fun <D, A> invoke(
            initialState: MutableStateV2<D, A>, configure: Builder<D, A>.() -> Unit
        ): StateMachineV2<D, A> = Builder(initialState).apply(configure).build()
    }
}
