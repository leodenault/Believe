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
    private val runningGameStateFactory: RunningGameStateFactory
) : LevelStateController {

    override fun navigateToRunningGameState(levelName: String) {
        levelStateRunner.transitionTo(
            runningGameStateFactory.create(levelName),
            EmptyGameStateTransition(),
            EmptyGameStateTransition()
        )
    }

    override fun navigateToMainMenu() {
        levelStateRunner.exitCurrentState(EmptyGameStateTransition())
        stateController.navigateToMainMenu()
    }
}
