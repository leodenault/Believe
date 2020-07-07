package believe.gamestate

/** Controller object for navigating to various game states. */
interface StateController {
    /** Navigates to the main menu state of the game. */
    fun navigateToMainMenu()

    /** Navigates to the options menu state of the game. */
    fun navigateToOptionsMenu()

    /** Navigates to a level, loading its data and starting it. */
    fun navigateToPlatformingLevel()
}