package believe.gamestate

import believe.app.proto.GameOptionsProto.GameOptions
import believe.core.display.Graphics
import believe.datamodel.DataCommitter
import believe.datamodel.MutableValue
import believe.gui.GuiBuilders.menuSelection
import believe.gui.GuiBuilders.numberPicker
import believe.gui.GuiBuilders.verticalContainer
import believe.gui.GuiLayoutFactory
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.GameContainer

@AutoFactory
class OptionsMenuStateV2 constructor(
    @Provided private val container: GameContainer, @Provided guiLayoutFactory: GuiLayoutFactory,
    @Provided stateController: StateController, @Provided
    mutableGameOptions: MutableValue<GameOptions>, @Provided
    gameOptionsCommitter: DataCommitter<GameOptions>
) : GameState {
    val gui = guiLayoutFactory.create(verticalContainer {
        +menuSelection {
            +"Back"
            executeSelectionAction = stateController::navigateToMainMenu
        }
        +numberPicker {
            +"Flow Speed"
            initialValue = mutableGameOptions.get().gameplayOptions.flowSpeed
            minValue = 1
            maxValue = 20
        }
    })

    override fun enter() = gui.bind()

    override fun leave() = gui.unbind()

    override fun render(g: Graphics) = gui.render(g)

    override fun update(delta: Int) {}
}