package believe.character

import dagger.Reusable
import org.newdawn.slick.Input
import javax.inject.Inject

@Reusable
internal class CharacterKeyboardActionMap @Inject constructor(
) : (Int) -> CharacterMovementInputAction {

    override fun invoke(key: Int) = when (key) {
        Input.KEY_LEFT -> CharacterMovementInputAction.MOVE_LEFT
        Input.KEY_RIGHT -> CharacterMovementInputAction.MOVE_RIGHT
        Input.KEY_SPACE -> CharacterMovementInputAction.JUMP
        else -> CharacterMovementInputAction.UNKNOWN
    }
}
