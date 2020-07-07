package believe.character

internal data class InputAction(
    internal val action: CharacterMovementInputAction, internal val started: Boolean
) {
    companion object {
        internal fun starting(action: CharacterMovementInputAction) = InputAction(action, true)
        internal fun ending(action: CharacterMovementInputAction) = InputAction(action, false)
    }
}