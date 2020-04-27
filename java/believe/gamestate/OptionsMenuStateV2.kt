package believe.gamestate

import believe.app.proto.GameOptionsProto.GameOptions
import believe.app.proto.GameOptionsProto.GameplayOptions
import believe.datamodel.DataCommitter
import believe.datamodel.MutableValue
import believe.gui.FocusableGroup
import believe.gui.FocusableGroupImplFactory
import believe.gui.GuiBuilders
import believe.gui.GuiBuilders.menuSelection
import believe.gui.GuiBuilders.verticalLayoutContainer
import believe.gui.GuiLayoutFactory
import believe.gui.MenuSelection
import believe.gui.NumberPicker
import believe.gui.VerticalKeyboardScrollpanel
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.Font
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import javax.inject.Inject

@AutoFactory
class OptionsMenuStateV2 constructor(
    @Provided private val container: GameContainer, @Provided guiLayoutFactory: GuiLayoutFactory,
    @Provided stateController: StateController, @Provided
    mutableGameOptions: MutableValue<GameOptions>, @Provided
    gameOptionsCommitter: DataCommitter<GameOptions>
) : GameState {
    val gui = guiLayoutFactory.create(verticalLayoutContainer {
        +menuSelection {
            +"Back"
            executeSelectionAction = stateController::navigateToMainMenu
        }
        +menuSelection {
            +"${mutableGameOptions.get().gameplayOptions.flowSpeed}"
        }
    })

    override fun enter() = gui.bind()

    override fun leave() = gui.unbind()

    override fun render(g: Graphics) = gui.render(g)

    override fun update(delta: Int) {}
}