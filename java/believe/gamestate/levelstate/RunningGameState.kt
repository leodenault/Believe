package believe.gamestate.levelstate

import believe.audio.Music
import believe.character.CharacterV2
import believe.core.display.Graphics
import believe.gamestate.GameState
import believe.gui.GuiAction
import believe.gui.GuiBuilders.canvasContainer
import believe.gui.GuiBuilders.progressBar
import believe.gui.GuiElement
import believe.gui.GuiLayoutFactory
import believe.input.InputAdapter
import believe.physics.manager.PhysicsManager
import believe.scene.Camera
import believe.scene.LevelMap
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided

@AutoFactory
class RunningGameState constructor(
    @Provided private val inputAdapter: InputAdapter<GuiAction>,
    @Provided private val stateController: LevelStateController,
    @Provided private val physicsManager: PhysicsManager,
    @Provided private val guiLayoutFactory: GuiLayoutFactory,
    @Provided private val levelMap: LevelMap,
    @Provided private var camera: Camera,
    @Provided private val player: CharacterV2,
    @Provided private val backgroundMusic: Music,
) : GameState {
    private val hud = guiLayoutFactory.create(
        canvasContainer<GuiElement> {
            add {
                progressBar {
                    initialProgress = 1f
                    getProgressObservable = { player.observableFocus }
                } from p(0.05f, 0.05f) to p(0.25f, 0.075f)
            }
        }
    )

    override fun enter() {
        inputAdapter.addActionStartListener(GuiAction.GO_BACK, this::pause)
        levelMap.bind()
        camera.bind()
        hud.bind()
        with(backgroundMusic) { if (isPaused()) resume() else loop() }
    }

    private fun pause() {
        stateController.navigateToPauseMenu()
    }

    override fun leave() {
        inputAdapter.removeActionStartListener(GuiAction.GO_BACK, this::pause)
        levelMap.unbind()
        camera.unbind()
        hud.unbind()
        backgroundMusic.pause()
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
