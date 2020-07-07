package believe.statemachine.parser

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.statemachine.proto.StateMachineProto.State
import believe.statemachine.proto.StateMachineProto.StateData
import believe.statemachine.proto.StateMachineProto.StateKey
import believe.statemachine.proto.StateMachineProto.StateMachine
import believe.statemachine.proto.StateMachineProto.Transition
import believe.statemachine.proto.StateMachineProto.TransitionAction
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeStateData
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeStateKey
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeTransitionAction
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class StateMachineParserTest {
    private val parser =
        StateMachineParser({ it.data.getExtension(FakeStateData.fakeStateData).fakeData },
            { it.getExtension(FakeTransitionAction.fakeTransitionAction).fakeAction })

    @Test
    fun parse_returnsCorrectStateMachine() {
        val stateMachine = parser.parse(
            StateMachine.newBuilder().addStates(STATE_1).addStates(STATE_2).addTransitions(
                Transition.newBuilder().setAction(
                    TransitionAction.newBuilder().setExtension(
                        FakeTransitionAction.fakeTransitionAction,
                        FakeTransitionAction.newBuilder().setFakeAction("action").build()
                    )
                ).setFrom(STATE_1_KEY).setTo(STATE_2_KEY)
            ).build()
        )

        assertThat(stateMachine.data).isEqualTo("state 1 data")
        stateMachine.transition("action")
        assertThat(stateMachine.data).isEqualTo("state 2 data")
    }

    @Test
    @VerifiesLoggingCalls
    fun parse_transitionFromStateDoesNotExist_logErrorAndSkips(logSystem: VerifiableLogSystem) {
        val stateMachine = parser.parse(
            StateMachine.newBuilder().addStates(STATE_1).addStates(STATE_2).addTransitions(
                Transition.newBuilder().setAction(
                    TransitionAction.newBuilder().setExtension(
                        FakeTransitionAction.fakeTransitionAction,
                        FakeTransitionAction.newBuilder().setFakeAction("action").build()
                    )
                ).setFrom(StateKey.getDefaultInstance()).setTo(STATE_2_KEY)
            ).build()
        )

        assertThat(stateMachine.data).isEqualTo("state 1 data")
        stateMachine.transition("action")
        assertThat(stateMachine.data).isEqualTo("state 1 data")
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Cannot find 'from' state key for use in transition.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun parse_transitionToStateDoesNotExist_logErrorAndSkips(logSystem: VerifiableLogSystem) {
        val stateMachine = parser.parse(
            StateMachine.newBuilder().addStates(STATE_1).addStates(STATE_2).addTransitions(
                Transition.newBuilder().setAction(
                    TransitionAction.newBuilder().setExtension(
                        FakeTransitionAction.fakeTransitionAction,
                        FakeTransitionAction.newBuilder().setFakeAction("action").build()
                    )
                ).setFrom(STATE_1_KEY).setTo(StateKey.getDefaultInstance())
            ).build()
        )

        assertThat(stateMachine.data).isEqualTo("state 1 data")
        stateMachine.transition("action")
        assertThat(stateMachine.data).isEqualTo("state 1 data")
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Cannot find 'to' state key for use in transition.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    companion object {
        private val STATE_1_KEY = StateKey.newBuilder().setExtension(
            FakeStateKey.fakeStateKey, FakeStateKey.newBuilder().setFakeKey("state 1 key").build()
        )
        private val STATE_2_KEY = StateKey.newBuilder().setExtension(
            FakeStateKey.fakeStateKey, FakeStateKey.newBuilder().setFakeKey("state 2 key").build()
        )
        private val STATE_1 = State.newBuilder().setKey(STATE_1_KEY).setData(
            StateData.newBuilder().setExtension(
                FakeStateData.fakeStateData,
                FakeStateData.newBuilder().setFakeData("state 1 data").build()
            )
        )
        private val STATE_2 = State.newBuilder().setKey(STATE_2_KEY).setData(
            StateData.newBuilder().setExtension(
                FakeStateData.fakeStateData,
                FakeStateData.newBuilder().setFakeData("state 2 data").build()
            )
        )
    }
}
