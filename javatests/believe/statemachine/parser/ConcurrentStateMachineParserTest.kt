package believe.statemachine.parser

import believe.statemachine.proto.StateMachineProto
import believe.statemachine.proto.StateMachineProto.ConcurrentStateMachine
import believe.statemachine.proto.StateMachineProto.State
import believe.statemachine.proto.StateMachineProto.StateData
import believe.statemachine.proto.StateMachineProto.StateMachine
import believe.statemachine.proto.StateMachineProto.SubStateMachine
import believe.statemachine.proto.StateMachineProto.Transition
import believe.statemachine.proto.StateMachineProto.TransitionAction
import believe.statemachine.proto.testing.FakeStateMachineProto
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeStateData
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeSubStateMachineKey
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeSubStateMachineKey.FakeKeyType
import believe.statemachine.proto.testing.FakeStateMachineProto.FakeTransitionAction
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ConcurrentStateMachineParserTest {
    private val parser = ConcurrentStateMachineParser { key ->
        when (key.getExtension(FakeSubStateMachineKey.fakeSubStateMachineKey).fakeKeyType!!) {
            FakeKeyType.FAKE_KEY_1 -> FakeSubStateMachineParserConfiguration.forState1()
            FakeKeyType.FAKE_KEY_2 -> FakeSubStateMachineParserConfiguration.forState2()
        }
    }

    @Test
    fun parse_correctlyParsesMachine() {
        val stateMachine = parser.parse(
            ConcurrentStateMachine.newBuilder().addSubStateMachines(
                SubStateMachine.newBuilder().setKey(
                    StateMachineProto.SubStateMachineKey.newBuilder().setExtension(
                        FakeSubStateMachineKey.fakeSubStateMachineKey,
                        FakeSubStateMachineKey.newBuilder().setFakeKeyType(
                            FakeKeyType.FAKE_KEY_1
                        ).build()
                    )
                ).setStateMachine(
                    StateMachine.newBuilder().addStates(
                        State.newBuilder().setKey(STATE_11_KEY).setData(
                            StateData.newBuilder().setExtension(
                                FakeStateData.fakeStateData,
                                FakeStateData.newBuilder().setFakeData("state 1.1 data").build()
                            )
                        )
                    ).addStates(
                        State.newBuilder().setKey(STATE_12_KEY).setData(
                            StateData.newBuilder().setExtension(
                                FakeStateData.fakeStateData,
                                FakeStateData.newBuilder().setFakeData("state 1.2 data").build()
                            )
                        )
                    ).addTransitions(
                        Transition.newBuilder().setAction(
                            TransitionAction.newBuilder().setExtension(
                                FakeTransitionAction.fakeTransitionAction,
                                FakeTransitionAction.newBuilder().setFakeAction("action 1").build()
                            )
                        ).setFrom(STATE_11_KEY).setTo(STATE_12_KEY)
                    )
                )
            ).addSubStateMachines(
                SubStateMachine.newBuilder().setKey(
                    StateMachineProto.SubStateMachineKey.newBuilder().setExtension(
                        FakeSubStateMachineKey.fakeSubStateMachineKey,
                        FakeSubStateMachineKey.newBuilder().setFakeKeyType(
                            FakeKeyType.FAKE_KEY_2
                        ).build()
                    )
                ).setStateMachine(
                    StateMachine.newBuilder().addStates(
                        State.newBuilder().setKey(STATE_21_KEY).setData(
                            StateData.newBuilder().setExtension(
                                FakeStateData.fakeStateData,
                                FakeStateData.newBuilder().setFakeData("state 2.1 data").build()
                            )
                        )
                    ).addStates(
                        State.newBuilder().setKey(STATE_22_KEY).setData(
                            StateData.newBuilder().setExtension(
                                FakeStateData.fakeStateData,
                                FakeStateData.newBuilder().setFakeData("state 2.2 data").build()
                            )
                        )
                    ).addTransitions(
                        Transition.newBuilder().setAction(
                            TransitionAction.newBuilder().setExtension(
                                FakeTransitionAction.fakeTransitionAction,
                                FakeTransitionAction.newBuilder().setFakeAction("action 2").build()
                            )
                        ).setFrom(STATE_21_KEY).setTo(STATE_22_KEY)
                    )
                )
            ).build(), FakeMachineData("", "")
        )

        assertThat(stateMachine.data).isEqualTo(FakeMachineData("state 1.1 data", "state 2.1 data"))
        stateMachine.transition("action 1")
        assertThat(stateMachine.data).isEqualTo(FakeMachineData("state 1.2 data", "state 2.1 data"))
        stateMachine.transition("action 2")
        assertThat(stateMachine.data).isEqualTo(FakeMachineData("state 1.2 data", "state 2.2 data"))
    }

    private data class FakeMachineData(
        internal var state1Data: String, internal var state2Data: String
    )

    private class FakeSubStateMachineParserConfiguration private constructor(
        isForState1: Boolean
    ) : SubStateMachineConfiguration<FakeMachineData, String, String> {

        override val parseState: (State) -> String = {
            it.data.getExtension(FakeStateData.fakeStateData).fakeData
        }
        override val parseTransitionAction: (TransitionAction) -> String = {
            it.getExtension(FakeTransitionAction.fakeTransitionAction).fakeAction
        }
        override val applyTo: String.(FakeMachineData) -> Unit = {
            if (isForState1) it.state1Data = this else it.state2Data = this
        }

        companion object {
            internal fun forState1() = FakeSubStateMachineParserConfiguration(true)

            internal fun forState2() = FakeSubStateMachineParserConfiguration(false)
        }
    }

    companion object {
        private val STATE_11_KEY = StateMachineProto.StateKey.newBuilder().setExtension(
            FakeStateMachineProto.FakeStateKey.fakeStateKey,
            FakeStateMachineProto.FakeStateKey.newBuilder().setFakeKey("state 1.1 key").build()
        )
        private val STATE_12_KEY = StateMachineProto.StateKey.newBuilder().setExtension(
            FakeStateMachineProto.FakeStateKey.fakeStateKey,
            FakeStateMachineProto.FakeStateKey.newBuilder().setFakeKey("state 1.2 key").build()
        )
        private val STATE_21_KEY = StateMachineProto.StateKey.newBuilder().setExtension(
            FakeStateMachineProto.FakeStateKey.fakeStateKey,
            FakeStateMachineProto.FakeStateKey.newBuilder().setFakeKey("state 2.1 key").build()
        )
        private val STATE_22_KEY = StateMachineProto.StateKey.newBuilder().setExtension(
            FakeStateMachineProto.FakeStateKey.fakeStateKey,
            FakeStateMachineProto.FakeStateKey.newBuilder().setFakeKey("state 2.2 key").build()
        )
    }
}