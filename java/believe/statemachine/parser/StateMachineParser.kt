package believe.statemachine.parser

import believe.statemachine.MutableStateV2
import believe.statemachine.StateMachineV2
import believe.statemachine.proto.StateMachineProto
import org.newdawn.slick.util.Log

/**
 * A parser that constructs state machines based on protos.
 *
 * @param parseState parses the state data into an instance of [D2].
 * @param parseTransitionAction parses an action within a transition to an instance of [A].
 * @param D the type of data stored within the state machine states.
 * @param A the type of action that triggers transitions.
 */
class StateMachineParser<D, A>(
    private val parseState: (StateMachineProto.State) -> D,
    private val parseTransitionAction: (StateMachineProto.TransitionAction) -> A
) {

    /** Parses [data] into a [StateMachineV2]. */
    fun parse(data: StateMachineProto.StateMachine): StateMachineV2<D, A> {
        val states = data.statesList.map {
            Pair(it.key, MutableStateV2<D, A>(parseState(it)))
        }
        val statesByKey = states.associate { it }
        return StateMachineV2(states[0].second) {
            data.transitionsList.forEach setupTransitions@{
                val fromState = statesByKey[it.from]
                if (fromState == null) {
                    Log.error(
                        "Cannot find 'from' state key for use in transition."
                    ).also { return@setupTransitions }
                }

                val toState = statesByKey[it.to]
                if (toState == null) {
                    Log.error(
                        "Cannot find 'to' state key for use in transition."
                    ).also { return@setupTransitions }
                }

                addTransition(parseTransitionAction(it.action), fromState!!, toState!!)
            }
        }
    }
}