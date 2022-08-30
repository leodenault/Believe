package believe.gamestate.levelstate

import believe.gamestate.GameStateRunner
import believe.gamestate.StateController
import believe.gamestate.transition.EmptyGameStateTransition
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class LevelStateControllerImpl @Inject internal constructor(
    @LevelStateRunner private val levelStateRunner: GameStateRunner,
    private val stateController: StateController,
    private val runningGameStateFactory: RunningGameStateFactory,
    private val pauseStateFactory: PauseStateFactory
) : LevelStateController {

    override fun navigateToRunningGameState() {
        levelStateRunner.transitionTo(
            runningGameStateFactory.create(), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }

    override fun resetAndNavigateToRunningGameState() {
        levelStateRunner.exitCurrentState(EmptyGameStateTransition())
        stateController.navigateToPlatformingLevel()
    }

    override fun navigateToMainMenu() {
        levelStateRunner.exitCurrentState(EmptyGameStateTransition())
        stateController.navigateToMainMenu()
    }

    override fun navigateToPauseMenu() {
        levelStateRunner.transitionTo(
            pauseStateFactory.create(), EmptyGameStateTransition(), EmptyGameStateTransition()
        )
    }
}
