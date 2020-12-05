package believe.gamestate.levelstate

import believe.core.display.Graphics
import believe.core.io.FontLoader
import believe.gamestate.GameState
import believe.gamestate.GameStateRunner
import believe.gamestate.StateController
import believe.gui.GuiAction
import believe.gui.GuiConfigurations
import believe.gui.ImageSupplier
import believe.input.InputAdapter
import believe.io.ResourceManager
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.google.protobuf.ExtensionRegistry
import org.newdawn.slick.GameContainer
import org.newdawn.slick.gui.GUIContext
import javax.inject.Inject

@AutoFactory
class LevelStateV2 @Inject internal constructor(
    @Provided private val guiContext: GUIContext,
    @Provided private val gameContainer: GameContainer,
    @Provided private val stateController: StateController,
    @Provided private val resourceManager: ResourceManager,
    @Provided private val extensionRegistry: ExtensionRegistry,
    @Provided private val fontLoader: FontLoader,
    @Provided private val inputAdapter: InputAdapter<GuiAction>,
    @Provided private val imageSupplier: ImageSupplier,
    @Provided @GuiConfigurations private val guiConfigurations: Set<*>,
    private val levelName: String
) : GameState {
    private var levelStateController: LevelStateController? = null
    private var levelStateRunner: GameStateRunner? = null

    override fun enter() {
        val levelStateComponent: LevelStateComponent = DaggerLevelStateComponent.factory().create(
            levelName,
            guiContext,
            gameContainer,
            stateController,
            resourceManager,
            extensionRegistry,
            fontLoader,
            inputAdapter,
            imageSupplier,
            guiConfigurations
        )
        levelStateController = levelStateComponent.levelStateController
        levelStateRunner = levelStateComponent.levelStateRunner
        levelStateComponent.levelStateController.navigateToRunningGameState()
    }

    override fun leave() {
        levelStateController = null
        levelStateRunner = null
    }

    override fun update(delta: Long) {
        levelStateRunner?.update(delta)
    }

    override fun render(g: Graphics) {
        levelStateRunner?.render(g)
    }
}
