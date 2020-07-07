package believe.statemachine.parser

import believe.statemachine.proto.StateMachineProto

/**
 * A configuration provided to a [ConcurrentStateMachineParser] which is used in parsing a specific
 * sub-machine of the resulting concurrent state machine.
 *
 * @param D1 the type of data held by the concurrent state machine in its entirety.
 * @param D2 the type of data held by the sub-machine corresponding to this configuration.
 * @param A the type of action used in triggering transitions.
 */
interface SubStateMachineConfiguration<D1, D2, A> {
    /** Parses the state data into an instance of [D2]. */
    val parseState: (StateMachineProto.State) -> D2
    /** Parses an action within a transition to an instance of [A]. */
    val parseTransitionAction: (StateMachineProto.TransitionAction) -> A
    /** Applies the data in an instance of [D2] to an instance of [D1]. */
    val applyTo: D2.(D1) -> Unit
}
