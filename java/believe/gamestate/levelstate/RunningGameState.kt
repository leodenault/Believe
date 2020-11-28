package believe.gamestate.levelstate

import believe.character.CharacterV2
import believe.core.Updatable
import believe.core.display.Bindable
import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.gamestate.GameState
import believe.geometry.FloatPoint
import believe.geometry.rectangle
import believe.gui.GuiAction
import believe.gui.GuiBuilders.canvasContainer
import believe.gui.GuiBuilders.progressBar
import believe.gui.GuiElement
import believe.gui.GuiLayoutFactory
import believe.input.InputAdapter
import believe.level.LevelManager
import believe.physics.manager.PhysicsManager
import believe.react.Observable
import believe.react.Observer
import believe.scene.Camera
import believe.scene.LevelMap
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.gui.GUIContext
import org.newdawn.slick.util.Log
import java.util.function.Supplier

@AutoFactory
class RunningGameState constructor(
    @Provided private val guiContext: GUIContext,
    @Provided private val levelManager: LevelManager,
    @Provided private val inputAdapter: InputAdapter<GuiAction>,
    @Provided private val stateController: LevelStateController,
    @Provided private val playerSupplier: Supplier<CharacterV2?>,
    @Provided private val levelMapFactory: LevelMap.Factory,
    @Provided private val physicsManager: PhysicsManager,
    @Provided private val guiLayoutFactory: GuiLayoutFactory,
    private val levelName: String
) : GameState {

    private var loadedState: LoadedState? = null

    override fun enter() {
        val levelMap = levelManager.getLevel(levelName)?.let {
            levelMapFactory.create(it.mapData, emptyList())
        } ?: throw RuntimeException("Could not load level map.")
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

        loadedState = LoadedState(
            inputAdapter,
            stateController,
            levelMap,
            physicsManager,
            camera,
            player,
            guiLayoutFactory
        ).also { it.bind() }
    }

    override fun leave() {
        loadedState?.unbind()?.also { loadedState = null }
    }

    override fun update(delta: Long) {
        loadedState?.update(delta)
    }

    override fun render(g: Graphics) {
        loadedState?.render(g)
    }

    companion object {
        private const val TARGET_WIDTH = 1920f
        private const val TARGET_HEIGHT = 1080f
    }

    class LoadedState(
        private val inputAdapter: InputAdapter<GuiAction>,
        private val stateController: LevelStateController,
        private val levelMap: LevelMap,
        private val physicsManager: PhysicsManager,
        private var camera: Camera,
        private val player: CharacterV2,
        guiLayoutFactory: GuiLayoutFactory
    ) : Bindable, Updatable, RenderableV2 {

        private val hud = guiLayoutFactory.create(canvasContainer<GuiElement> {
            add {
                progressBar {
                    initialProgress = 1f
                    getProgressObservable = { player.observableFocus }
                } from p(0.05f, 0.05f) to p(0.25f, 0.075f)
            }
        })

        override fun bind() {
            inputAdapter.addActionStartListener(GuiAction.EXECUTE_ACTION, this::leaveState)
            levelMap.bind()
            camera.bind()
            hud.bind()
        }

        private fun leaveState() = stateController.navigateToMainMenu()

        override fun unbind() {
            inputAdapter.removeActionStartListener(GuiAction.EXECUTE_ACTION, this::leaveState)
            levelMap.unbind()
            camera.unbind()
            hud.unbind()
        }

        override fun update(delta: Long) {
            levelMap.update(delta)
            physicsManager.update(delta)
        }

        override fun render(g: Graphics) {
            camera.pushTransformOn(g)
            levelMap.render(g)
            g.popTransform()
            hud.render(g)
        }
    }
}
