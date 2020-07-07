package believe.statemachine

/**
 * An implementation of [StateV2] whose properties can be mutated.
 *
 * @param D the type of data stored within this state.
 * @param A the type of action used in transitioning states.
 */
class MutableStateV2<D, A>(override val data: D) : StateV2<D, A> {
    override val transitions: MutableMap<A, StateV2<D, A>> = mutableMapOf()

    /**
     * Factory for generating instances of [MutableStateV2].
     *
     * Use this as a convenience to avoid having to repeat generic type arguments.
     *
     * @param D the type of data stored within the created states.
     * @param A the type of action used in transitioning states.
     */
    class Factory<D, A> {
        /** Creates a [MutableStateV2] based on [data]. */
        fun create(data: D) = MutableStateV2<D, A>(data)
    }
}
