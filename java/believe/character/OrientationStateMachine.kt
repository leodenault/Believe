package believe.character

import believe.animation.Animation
import believe.animation.BidirectionalAnimation
import believe.core.display.Bindable
import believe.input.InputAdapter
import believe.statemachine.MutableStateV2
import believe.statemachine.StateMachineV2
import dagger.Reusable
import javax.inject.Inject

internal class OrientationStateMachine private constructor(
    private val inputAdapter: InputAdapter<CharacterMovementInputAction>
) : Bindable {

    private val facingRightData = Data(1, BidirectionalAnimation::rightAnimation)
    private val facingLeftData = Data(-1, BidirectionalAnimation::leftAnimation)
    private val facingRightIdle = MutableStateV2<Data, InputAction>(facingRightData)
    private val stateMachine = StateMachineV2(initialState = facingRightIdle) {
        val facingRightMoving = MutableStateV2<Data, InputAction>(facingRightData)
        val facingLeftIdle = MutableStateV2<Data, InputAction>(facingLeftData)
        val facingLeftMoving = MutableStateV2<Data, InputAction>(facingLeftData)

        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_RIGHT),
            facingRightIdle,
            facingRightMoving
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_LEFT),
            facingRightIdle,
            facingRightMoving
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_LEFT),
            facingRightIdle,
            facingLeftMoving
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_RIGHT),
            facingRightIdle,
            facingLeftMoving
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_RIGHT),
            facingRightMoving,
            facingRightIdle
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_LEFT),
            facingRightMoving,
            facingRightIdle
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_LEFT),
            facingLeftMoving,
            facingLeftIdle
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_RIGHT),
            facingLeftMoving,
            facingLeftIdle
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_LEFT),
            facingLeftIdle,
            facingLeftMoving
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_RIGHT),
            facingLeftIdle,
            facingLeftMoving
        )
        addTransition(
            InputAction.starting(CharacterMovementInputAction.MOVE_RIGHT),
            facingLeftIdle,
            facingRightMoving
        )
        addTransition(
            InputAction.ending(CharacterMovementInputAction.MOVE_LEFT),
            facingLeftIdle,
            facingRightMoving
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
        internal var movementMultiplier: Int,
        internal var chooseAnimationDirection: (BidirectionalAnimation) -> Animation
    )

    @Reusable
    internal class Factory @Inject internal constructor(
        private val inputAdapter: InputAdapter<CharacterMovementInputAction>
    ) {
        internal fun create() = OrientationStateMachine(inputAdapter)
    }
}