package believe.gamestate

import believe.core.display.Graphics
import believe.gui.GuiBuilders.menuSelection
import believe.gui.GuiBuilders.verticalContainer
import believe.gui.GuiElement
import believe.gui.GuiLayoutFactory
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.GameContainer

@AutoFactory
class MainMenuStateV2 constructor(
    @Provided private val container: GameContainer, @Provided stateController: StateController,
    @Provided guiLayoutFactory: GuiLayoutFactory
) : GameState {

    private var guiLayout: GuiElement = guiLayoutFactory.create(verticalContainer {
        +menuSelection {
            +"Play Platforming Level"
        }
        +menuSelection {
            +"Play Arcade Level"
        }
        +menuSelection {
            +"Options"
            executeSelectionAction = stateController::navigateToOptionsMenu
        }
        +menuSelection {
            +"Exit"
            executeSelectionAction = container::exit
        }
    })

    override fun enter() = guiLayout.bind()

    override fun leave() = guiLayout.unbind()

    override fun render(g: Graphics) {
        guiLayout.render(g)
    }

    override fun update(delta: Int) {}
}