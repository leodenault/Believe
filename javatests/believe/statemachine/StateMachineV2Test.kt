package believe.statemachine

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class StateMachineV2Test {
    private val state1 = MutableStateV2<String, Int>("state 1")
    private val state2 = MutableStateV2<String, Int>("state 2")
    private val stateMachine = StateMachineV2(state1) {
        addTransition(VALID_ACTION, state1, state2)
    }

    @Test
    fun data_returnsCurrentStateData() {
        assertThat(stateMachine.data).isEqualTo("state 1")
    }

    @Test
    fun transition_actionIsValid_transitionsToNextState() {
        stateMachine.transition(VALID_ACTION)

        assertThat(stateMachine.data).isEqualTo("state 2")
    }

    @Test
    fun transition_actionIsInvalid_doesNotTransitionToNextState() {
        stateMachine.transition(INVALID_ACTION)

        assertThat(stateMachine.data).isEqualTo("state 1")
    }

    companion object {
        private const val VALID_ACTION = 123
        private const val INVALID_ACTION = 987
    }
}
