package believe.character

import believe.animation.BidirectionalAnimation
import believe.core.display.Bindable
import believe.input.InputAdapter
import believe.statemachine.MutableStateV2
import believe.statemachine.StateMachineV2
import dagger.Reusable
import javax.inject.Inject

internal class HorizontalMovementStateMachine private constructor(
    private val inputAdapter: InputAdapter<CharacterMovementInputAction>,
    idleAnimation: BidirectionalAnimation,
    private val movementAnimation: BidirectionalAnimation,
    private val movementSpeed: Float
) : Bindable {
    private val stateFactory = MutableStateV2.Factory<Data, InputAction>()
    private val idleState = stateFactory.create(Data(idleAnimation, 0f))
    private val stateMachine = StateMachineV2(initialState = idleState) {
        val movingState = stateFactory.create(Data(movementAnimation, movementSpeed))

        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_LEFT), idleState, movingState
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_LEFT), idleState, movingState
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_RIGHT), idleState, movingState
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_RIGHT), idleState, movingState
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_LEFT), movingState, idleState
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_LEFT), movingState, idleState
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_RIGHT), movingState, idleState
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_RIGHT), movingState, idleState
        )
    }

    internal val data: Data
        get() = stateMachine.data

    override fun bind() {
        inputAdapter.addActionStartListener(
            CharacterMovementInputAction.MOVE_LEFT, this::transitionLeftStart
        )
        inputAdapter.addActionEndListener(
            CharacterMovementInputAction.MOVE_LEFT, this::transitionLeftEnd
        )
        inputAdapter.addActionStartListener(
            CharacterMovementInputAction.MOVE_RIGHT, this::transitionRightStart
        )
        inputAdapter.addActionEndListener(
            CharacterMovementInputAction.MOVE_RIGHT, this::transitionRightEnd
        )
    }

    override fun unbind() {
        inputAdapter.removeActionStartListener(
            CharacterMovementInputAction.MOVE_LEFT, this::transitionLeftStart
        )
        inputAdapter.removeActionEndListener(
            CharacterMovementInputAction.MOVE_LEFT, this::transitionLeftEnd
        )
        inputAdapter.removeActionStartListener(
            CharacterMovementInputAction.MOVE_RIGHT, this::transitionRightStart
        )
        inputAdapter.removeActionEndListener(
            CharacterMovementInputAction.MOVE_RIGHT, this::transitionRightEnd
        )
    }

    private fun transitionLeftStart() =
        stateMachine.transition(InputAction.starting(CharacterMovementInputAction.MOVE_LEFT))

    private fun transitionLeftEnd() =
        stateMachine.transition(InputAction.ending(CharacterMovementInputAction.MOVE_LEFT))

    private fun transitionRightStart() =
        stateMachine.transition(InputAction.starting(CharacterMovementInputAction.MOVE_RIGHT))

    private fun transitionRightEnd() =
        stateMachine.transition(InputAction.ending(CharacterMovementInputAction.MOVE_RIGHT))

    internal class Data(
        val bidirectionalAnimation: BidirectionalAnimation, val movementSpeed: Float
    )

    @Reusable
    internal class Factory @Inject internal constructor(
        private val inputAdapter: InputAdapter<CharacterMovementInputAction>
    ) {
        fun create(
            idleAnimation: BidirectionalAnimation,
            movementAnimation: BidirectionalAnimation,
            movementSpeed: Float
        ) = HorizontalMovementStateMachine(
            inputAdapter,
            idleAnimation,
            movementAnimation,
            movementSpeed
        )
    }
}
