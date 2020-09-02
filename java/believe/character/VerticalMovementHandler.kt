package believe.character

import believe.core.display.Bindable
import believe.input.InputAdapter
import dagger.Reusable
import javax.inject.Inject
import kotlin.math.abs

internal class VerticalMovementHandler private constructor(
    private val inputAdapter: InputAdapter<CharacterMovementInputAction>,
    private val characterStateMachine: CharacterStateMachine,
    private val initialJumpVelocity: Float,
    private val maximumLandedVerticalVelocityTolerance: Float
) : Bindable {

    private var isAirborne = false

    internal var verticalVelocity = 0f
        set(value) {
            field = value
            if (!isAirborne && abs(value) > abs(maximumLandedVerticalVelocityTolerance)) {
                isAirborne = true
                characterStateMachine.jump()
                inputAdapter.removeActionStartListener(
                    CharacterMovementInputAction.JUMP, this::jump
                )
            }
        }

    override fun bind() =
        inputAdapter.addActionStartListener(CharacterMovementInputAction.JUMP, this::jump)

    override fun unbind() =
        inputAdapter.removeActionStartListener(CharacterMovementInputAction.JUMP, this::jump)

    internal fun jump() {
        if (!isAirborne) {
            isAirborne = true
            inputAdapter.removeActionStartListener(CharacterMovementInputAction.JUMP, this::jump)
            verticalVelocity = initialJumpVelocity
        }
    }

    internal fun land() {
        if (isAirborne) {
            isAirborne = false
            inputAdapter.addActionStartListener(CharacterMovementInputAction.JUMP, this::jump)
        }
    }

    @Reusable
    internal class Factory @Inject internal constructor(
        private val inputAdapter: InputAdapter<CharacterMovementInputAction>
    ) {
        internal fun create(
            characterStateMachine: CharacterStateMachine,
            initialJumpVelocity: Float,
            maximumLandedVerticalVelocityTolerance: Float
        ) = VerticalMovementHandler(
            inputAdapter,
            characterStateMachine,
            initialJumpVelocity,
            maximumLandedVerticalVelocityTolerance
        )
    }
}
