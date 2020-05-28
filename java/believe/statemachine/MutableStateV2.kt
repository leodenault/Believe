package believe.statemachine

/**
 * An implementation of [StateV2] whose properties can be mutated.
 *
 * @param D the type of data stored within this state.
 * @param A the type of action used in transitioning states.
 */
class MutableStateV2<D, A>(override val data: D) : StateV2<D, A> {
    override val transitions: MutableMap<A, StateV2<D, A>> = mutableMapOf()
}
