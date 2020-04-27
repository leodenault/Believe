package believe.gamestate

import believe.gui.FocusableGroupImplFactory
import believe.gui.GuiBuilders.menuSelection
import believe.gui.GuiBuilders.verticalLayoutContainer
import believe.gui.GuiElement
import believe.gui.GuiLayoutFactory
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics

@AutoFactory
class MainMenuStateV2 constructor(
    @Provided private val container: GameContainer, @Provided stateController: StateController,
    @Provided guiLayoutFactory: GuiLayoutFactory
) : GameState {

    private val guiLayout: GuiElement = guiLayoutFactory.create(verticalLayoutContainer {
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