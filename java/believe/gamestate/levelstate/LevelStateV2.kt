package believe.gamestate.levelstate

import believe.audio.Music
import believe.character.CharacterV2
import believe.core.display.Graphics
import believe.core.io.FontLoader
import believe.gamestate.GameState
import believe.gamestate.GameStateRunner
import believe.gamestate.StateController
import believe.geometry.FloatPoint
import believe.geometry.rectangle
import believe.gui.GuiAction
import believe.gui.GuiConfigurations
import believe.gui.ImageSupplier
import believe.input.InputAdapter
import believe.io.ResourceManager
import believe.level.LevelManager
import believe.physics.manager.PhysicsManager
import believe.react.Observable
import believe.react.Observer
import believe.scene.Camera
import believe.scene.LevelMap
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.google.protobuf.ExtensionRegistry
import org.newdawn.slick.GameContainer
import org.newdawn.slick.gui.GUIContext
import org.newdawn.slick.util.Log
import java.util.function.Supplier
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
    @Provided private val levelManager: LevelManager,
    @Provided private val levelMapFactory: LevelMap.Factory,
    @Provided private val playerSupplier: Supplier<CharacterV2?>,
    @Provided private val physicsManager: PhysicsManager,
    private val levelName: String
) : GameState {
    private var levelStateController: LevelStateController? = null
    private var levelStateRunner: GameStateRunner? = null

    override fun enter() {
        val levelData =
            levelManager.getLevel(levelName)
                ?: throw RuntimeException("Could not load level map for '$levelName' level name.")
        val levelMap = levelMapFactory.create(levelData.mapData, emptySet())
        val player: CharacterV2 = playerSupplier.get()
            ?: return Unit.also { Log.error("No player was loaded from the map.") }
        val camera = Camera(
            object : Observable<FloatPoint> {
                override fun addObserver(camera: Observer<FloatPoint>): Observable<FloatPoint> {
                    player.addObserver { newValue ->
                        camera.valueChanged(newValue.center)
                    }
                    return this
                }
            },
            rectangle(levelMap.x, levelMap.y, levelMap.width, levelMap.height),
            cameraWidth = TARGET_WIDTH,
            cameraHeight = TARGET_HEIGHT,
            scaleX = guiContext.width / TARGET_WIDTH,
            scaleY = guiContext.height / TARGET_HEIGHT
        )
        camera.bounds.addObserver(levelMap)

        val levelStateComponent: LevelStateComponent = DaggerLevelStateComponent.factory().create(
            guiContext,
            gameContainer,
            stateController,
            resourceManager,
            extensionRegistry,
            fontLoader,
            inputAdapter,
            imageSupplier,
            guiConfigurations,
            levelMap,
            camera,
            player,
            levelData.backgroundMusic.load() ?: Music.EMPTY,
            physicsManager
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

    companion object {
        private const val TARGET_WIDTH = 1920f
        private const val TARGET_HEIGHT = 1080f
    }
}
