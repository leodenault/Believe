package believe.gamestate

import believe.character.Animations
import believe.character.CharacterV2
import believe.core.Updatable
import believe.core.display.Bindable
import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.geometry.Point
import believe.geometry.rectangle
import believe.gui.GuiAction
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
class LevelStateV2 constructor(
    @Provided private val guiContext: GUIContext, @Provided private val levelManager: LevelManager,
    @Provided
    private val inputAdapter: InputAdapter<GuiAction>,
    @Provided
    private val stateController: StateController,
    @Provided
    private val playerSupplier: Supplier<CharacterV2?>,
    @Provided
    private val levelMapFactory: LevelMap.Factory,
    @Provided
    private val physicsManager: PhysicsManager,
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
            object : Observable<Point> {
                override fun addObserver(camera: Observer<Point>): Observable<Point> {
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
            inputAdapter, stateController, levelMap, physicsManager, camera
        ).also { it.bind() }
    }

    override fun leave() {
        loadedState?.unbind()?.also { loadedState = null }
    }

    override fun update(delta: Int) {
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
        private val stateController: StateController,
        private val levelMap: LevelMap,
        private val physicsManager: PhysicsManager,
        private var camera: Camera
    ) : Bindable, Updatable, RenderableV2 {

        override fun bind() {
            inputAdapter.addActionStartListener(GuiAction.EXECUTE_ACTION, this::leaveState)
            levelMap.bind()
            camera.bind()
        }

        private fun leaveState() = stateController.navigateToMainMenu()

        override fun unbind() {
            inputAdapter.removeActionStartListener(GuiAction.EXECUTE_ACTION, this::leaveState)
            levelMap.unbind()
            camera.unbind()
        }

        override fun update(delta: Int) {
            levelMap.update(delta)
            physicsManager.update(delta)
        }

        override fun render(g: Graphics) {
            camera.pushTransformOn(g)
            levelMap.render(g)
            g.popTransform()
        }
    }
}
