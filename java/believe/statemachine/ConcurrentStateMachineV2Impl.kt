package believe.statemachine

/** A state machine with multiple internal state machines running concurrently. */
class ConcurrentStateMachineV2Impl<D, A> private constructor(
    private val subStateMachines: List<SubStateMachine<D, *, A>>, initialData: D
) : ConcurrentStateMachineV2<D, A> {

    /**
     * Returns the data stored within this machine's current state.
     *
     * Note that since there are multiple concurrently running state machines, [data] returns the
     * combination of their individual states' data.
     */
    override val data: D = initialData.apply { updateData(this) }

    /** Attempts to transition to another state, where the transition is triggered by [action]. */
    override fun transition(action: A) {
        subStateMachines.forEach { it.stateMachine.transition(action) }
        updateData(data)
    }

    private fun updateData(data: D) = subStateMachines.forEach { it.applyTo(data) }

    /**
     * A builder for constructing [ConcurrentStateMachineV2] instances.
     *
     * @param initialData the data held by the [ConcurrentStateMachineV2] in its initial state.
     */
    class Builder<D1, A>(private val initialData: D1) {
        private val subStateSubStateMachines: MutableList<SubStateMachine<D1, *, A>> =
            mutableListOf()

        /**
         * Adds a [StateMachineV2] to this builder.
         *
         * @param stateMachine the [StateMachineV2] to add to the builder.
         * @param applyTo the function to apply when some state in the [ConcurrentStateMachineV2]
         * changes, which updates the data currently held by the [ConcurrentStateMachineV2].
         */
        fun <D2> add(stateMachine: StateMachineV2<D2, A>, applyTo: D2.(D1) -> Unit) {
            subStateSubStateMachines.add(SubStateMachine(stateMachine, applyTo))
        }

        /** Returns a [ConcurrentStateMachineV2] based on the contents of this builder. */
        fun build() = ConcurrentStateMachineV2Impl(subStateSubStateMachines, initialData)
    }

    private class SubStateMachine<D1, D2, A>(
        internal val stateMachine: StateMachineV2<D2, A>, private val applyTo: D2.(D1) -> Unit
    ) {
        internal fun applyTo(data: D1) = applyTo(stateMachine.data, data)
    }

    companion object {
        /**
         * Returns a [ConcurrentStateMachineV2] based on [initialData] and the contents of the
         * builder.
         *
         * @param initialData the data held by the [ConcurrentStateMachineV2] in its initial state.
         * @param configure the function that constructs the [ConcurrentStateMachineV2] using [Builder]
         * as the receiver.
         */
        inline operator fun <D, A> invoke(initialData: D, configure: Builder<D, A>.() -> Unit) =
            Builder<D, A>(initialData).apply(configure).build()
    }
}
