package believe.gamestate.levelstate

/** A controller for navigating to different substates within [LevelStateV2]. */
interface LevelStateController {
    /** Navigates to the [RunningGameState], using [levelName] to fetch the level. */
    fun navigateToRunningGameState(levelName: String)

    /** Navigates to the main menu while properly exiting the [LevelStateV2] context. */
    fun navigateToMainMenu()
}