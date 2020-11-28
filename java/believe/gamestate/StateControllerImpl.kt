package believe.gamestate

import believe.app.ApplicationGameStateRunner
import believe.gamestate.levelstate.LevelStateV2Factory
import believe.gamestate.transition.EmptyGameStateTransition
import dagger.Reusable
import javax.inject.Inject

/** Default implementation of [StateController]. */
@Reusable
class StateControllerImpl @Inject internal constructor(
    @ApplicationGameStateRunner private val gameStateRunner: GameStateRunner,
    private val mainMenuState: MainMenuStateV2Factory,
    private val optionsMenuState: OptionsMenuStateV2Factory,
    private val levelState: LevelStateV2Factory
) : StateController {

    override fun navigateToMainMenu() {
        gameStateRunner.transitionTo(
            mainMenuState.create(), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }

    override fun navigateToOptionsMenu() {
        gameStateRunner.transitionTo(
            optionsMenuState.create(), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }

    override fun navigateToPlatformingLevel() {
        gameStateRunner.transitionTo(
            levelState.create("snow"), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }
}