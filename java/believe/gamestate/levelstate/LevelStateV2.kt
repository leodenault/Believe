package believe.gamestate.levelstate

import believe.core.display.Graphics
import believe.gamestate.GameState
import believe.gamestate.GameStateRunner
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided

@AutoFactory
class LevelStateV2 constructor(
    @Provided @LevelStateRunner private val stateRunner: GameStateRunner,
    @Provided private val levelStateController: LevelStateController,
    private val levelName: String
) : GameState {

    override fun enter() {
        levelStateController.navigateToRunningGameState(levelName)
    }

    override fun leave() {}

    override fun update(delta: Long) = stateRunner.update(delta)

    override fun render(g: Graphics) = stateRunner.render(g)
}
