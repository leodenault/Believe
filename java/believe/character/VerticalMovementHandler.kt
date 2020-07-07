package believe.character

import believe.core.display.Bindable
import believe.input.InputAdapter
import dagger.Reusable
import javax.inject.Inject

internal class VerticalMovementHandler private constructor(
    private val inputAdapter: InputAdapter<CharacterMovementInputAction>,
    private val characterStateMachine: CharacterStateMachine,
    private val initialJumpVelocity: Float,
    private val maximumLandedVerticalVelocityTolerance: Float
) : Bindable {

    internal var verticalVelocity = 0f
        set(value) {
            field = value
            if (value > maximumLandedVerticalVelocityTolerance || value < -maximumLandedVerticalVelocityTolerance) characterStateMachine.jump()
        }

    override fun bind() =
        inputAdapter.addActionStartListener(CharacterMovementInputAction.JUMP, this::jump)

    override fun unbind() =
        inputAdapter.removeActionStartListener(CharacterMovementInputAction.JUMP, this::jump)

    private fun jump() {
        if (!characterStateMachine.isJumping()) verticalVelocity = initialJumpVelocity
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
