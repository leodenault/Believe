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
    @Provided guiLayoutFactory: GuiLayoutFactory, @Provided stateController: StateController,
    @Provided
    private val mutableGameOptions: MutableValue<GameOptions>,
    @Provided
    private val gameOptionsCommitter: DataCommitter<GameOptions>
) : GameState {
    private var flowSpeed: Int = mutableGameOptions.get().gameplayOptions.flowSpeed

    private val gui = guiLayoutFactory.create(verticalContainer {
        +menuSelection {
            +"Back"
            executeSelectionAction = stateController::navigateToMainMenu
        }
        +numberPicker {
            +"Flow Speed"
            initialValue = flowSpeed
            minValue = 1
            maxValue = 20
            confirmNumber = { flowSpeed = it }
        }
    })

    override fun enter() = gui.bind()

    override fun leave() {
        gui.unbind()
        val gameOptions: GameOptions = mutableGameOptions.get()
        val newGameOptions: GameOptions = gameOptions.toBuilder().setGameplayOptions(
            gameOptions.gameplayOptions.toBuilder().setFlowSpeed(
                flowSpeed
            )
        ).build()
        mutableGameOptions.update(newGameOptions)
        gameOptionsCommitter.commit(newGameOptions)
    }

    override fun render(g: Graphics) = gui.render(g)

    override fun update(delta: Long) {}
}