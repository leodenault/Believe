package believe.character

/** An **input** action, specifically initiated by the user, that affects character movement. */
internal enum class CharacterMovementInputAction {
    UNKNOWN, MOVE_LEFT, MOVE_RIGHT, JUMP, LAND
}
