package believe.gamestate.levelstate

import believe.core.display.Graphics
import believe.gamestate.GameState
import believe.gui.GuiBuilders.menuSelection
import believe.gui.GuiBuilders.verticalContainer
import believe.gui.GuiElement
import believe.gui.GuiLayoutFactory
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.GameContainer

@AutoFactory
class PauseState(
    @Provided private val guiLayoutFactory: GuiLayoutFactory,
    @Provided private val gameContainer: GameContainer,
    @Provided levelStateController: LevelStateController
) : GameState {
    private val gui: GuiElement = guiLayoutFactory.create(verticalContainer {
        +menuSelection {
            +"Resume Level"
            executeSelectionAction = levelStateController::navigateToRunningGameState
        }
        +menuSelection {
            +"Reset Level"
            executeSelectionAction = levelStateController::resetAndNavigateToRunningGameState
        }
        +menuSelection {
            +"Exit Level"
            executeSelectionAction = levelStateController::navigateToMainMenu
        }
        +menuSelection {
            +"Exit Game"
            executeSelectionAction = gameContainer::exit
        }
    })

    override fun enter() = gui.bind()

    override fun leave() = gui.unbind()

    override fun update(delta: Long) {}

    override fun render(g: Graphics) = gui.render(g)
}
