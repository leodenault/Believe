package believe.gamestate

import believe.character.Animations
import believe.character.CharacterV2
import believe.character.Faction
import believe.character.proto.CharacterAnimationsProto.CharacterAnimations
import believe.core.Updatable
import believe.core.display.Bindable
import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.geometry.Point
import believe.geometry.point
import believe.geometry.rectangle
import believe.gui.GuiAction
import believe.input.InputAdapter
import believe.level.LevelManager
import believe.react.Observable
import believe.react.Observer
import believe.scene.Camera
import believe.scene.LevelMap
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.gui.GUIContext

@AutoFactory
class LevelStateV2 constructor(
    @Provided private val guiContext: GUIContext,
    @Provided private val levelManager: LevelManager,
    @Provided
    private val inputAdapter: InputAdapter<GuiAction>,
    @Provided
    private val stateController: StateController,
    @Provided
    private val characterFactory: CharacterV2.Factory,
    @Provided
    private val animationsParser: Animations.Parser, private val levelName: String
) : GameState {

    private var loadedState: LoadedState? = null

    override fun enter() {
        val levelMap =
            levelManager.getLevel(levelName)?.let { LevelMap.create(it.mapData, emptyList()) }
                ?: throw RuntimeException("Could not load level map.")
        val characterData = CharacterAnimations.newBuilder().setSpriteSheetName("jacob")
            .setIdleAnimationName("idle").setMovementAnimationName("moving")
            .setJumpAnimationName("jumping").build()
        val character = characterFactory.create(
            animationsParser.parse(
                characterData
            ), CharacterV2.DamageListener.NONE, 0f, 0f, Faction.GOOD
        )
        val camera = Camera(
            object : Observable<Point> {
                override fun addObserver(camera: Observer<Point>): Observable<Point> {
                    character.addObserver { newValue ->
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

        loadedState = LoadedState(
            inputAdapter, stateController, levelMap, character, camera
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
        private var character: CharacterV2,
        private var camera: Camera
    ) : Bindable, Updatable, RenderableV2 {

        override fun bind() {
            inputAdapter.addActionStartListener(GuiAction.EXECUTE_ACTION, this::leaveState)
            levelMap.bind()
            character.bind()
            camera.bind()
        }

        private fun leaveState() = stateController.navigateToMainMenu()

        override fun unbind() {
            inputAdapter.removeActionStartListener(GuiAction.EXECUTE_ACTION, this::leaveState)
            levelMap.unbind()
            character.unbind()
            camera.bind()
        }

        override fun update(delta: Int) = character.update(delta)

        override fun render(g: Graphics) {
            camera.pushTransformOn(g)
            levelMap.render(g)
            character.render(g)
            g.popTransform()
        }
    }
}
