package believe.gamestate.levelstate

/** A controller for navigating to different substates within [LevelStateV2]. */
interface LevelStateController {
    /** Navigates to the [RunningGameState]. */
    fun navigateToRunningGameState()

    /** Navigates to the main menu while properly exiting the [LevelStateV2] context. */
    fun navigateToMainMenu()

    /** Navigates to the pause menu. */
    fun navigateToPauseMenu()
}