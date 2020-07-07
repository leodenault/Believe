package believe.statemachine.parser

import believe.statemachine.ConcurrentStateMachineV2
import believe.statemachine.ConcurrentStateMachineV2Impl
import believe.statemachine.proto.StateMachineProto
import believe.statemachine.proto.StateMachineProto.SubStateMachineKey

/**
 * A parser producing a state machine running multiple internal state machines concurrently.
 *
 * @param getConfiguration returns a [SubStateMachineConfiguration] corresponding to the
 * [SubStateMachineKey].
 */
class ConcurrentStateMachineParser<D, A>(
    private val getConfiguration: (SubStateMachineKey) -> SubStateMachineConfiguration<D, *, A>
) {

    /**
     * Parses [data] into a [ConcurrentStateMachineV2], where [initialData] will be used to
     * initialize the machine.
     */
    fun parse(data: StateMachineProto.ConcurrentStateMachine, initialData: D) =
        ConcurrentStateMachineV2Impl<D, A>(initialData) {
            data.subStateMachinesList.forEach {
                addStateMachine(getConfiguration(it.key), it.stateMachine)
            }
        }

    private fun <D2> ConcurrentStateMachineV2Impl.Builder<D, A>.addStateMachine(
        configuration: SubStateMachineConfiguration<D, D2, A>,
        stateMachine: StateMachineProto.StateMachine
    ) {
        add(
            StateMachineParser(
                configuration.parseState, configuration.parseTransitionAction
            ).parse(stateMachine), configuration.applyTo
        )
    }
}