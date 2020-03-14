package believe.gamestate

import believe.gamestate.transition.EmptyGameStateTransition
import dagger.Reusable
import javax.inject.Inject

@Reusable
class StateController @Inject internal constructor(
    private val gameStateRunner: GameStateRunner,
    private val mainMenuState: MainMenuStateV2Factory,
    private val optionsMenuState: OptionsMenuStateV2Factory
) {
    fun navigateToMainMenu() {
        gameStateRunner.transitionTo(
            mainMenuState.create(), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }

    fun navigateToOptionsMenu() {
        gameStateRunner.transitionTo(
            optionsMenuState.create(), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }
}