package believe.statemachine

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ConcurrentStateMachineV2ImplTest {
    private val state11 = MutableStateV2<String, Int>("state 1.1")
    private val state12 = MutableStateV2<String, Int>("state 1.2")
    private val state21 = MutableStateV2<String, Int>("state 2.1")
    private val state22 = MutableStateV2<String, Int>("state 2.2")
    private val concurrentStateMachine = ConcurrentStateMachineV2Impl<FakeData, Int>(FakeData("", "")) {
        add(StateMachineV2(state11) {
            addTransition(
                VALID_ACTION_1, state11, state12
            )
        }) { fakeData -> fakeData.first = this }

        add(StateMachineV2(state21) {
            addTransition(
                VALID_ACTION_2, state21, state22
            )
        }) { fakeData -> fakeData.second = this }
    }

    @Test
    fun data_returnsCorrectData() {
        assertThat(concurrentStateMachine.data).isEqualTo(FakeData("state 1.1", "state 2.1"))
    }

    @Test
    fun transition_actionIsValid_transitionsInternalStateMachines() {
        concurrentStateMachine.transition(VALID_ACTION_1)

        assertThat(concurrentStateMachine.data).isEqualTo(FakeData("state 1.2", "state 2.1"))

        concurrentStateMachine.transition(VALID_ACTION_2)

        assertThat(concurrentStateMachine.data).isEqualTo(FakeData("state 1.2", "state 2.2"))
    }

    private data class FakeData(internal var first: String, internal var second: String)

    companion object {
        private const val VALID_ACTION_1 = 123
        private const val VALID_ACTION_2 = 456
    }
}