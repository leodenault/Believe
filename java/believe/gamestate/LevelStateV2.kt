package believe.gamestate

import believe.character.Animations
import believe.character.CharacterV2
import believe.character.Faction
import believe.character.proto.CharacterAnimationsProto
import believe.character.proto.CharacterAnimationsProto.CharacterAnimations
import believe.core.display.Graphics
import believe.gui.GuiAction
import believe.input.InputAdapter
import believe.level.LevelManager
import believe.scene.LevelMap
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided

@AutoFactory
class LevelStateV2 constructor(
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
    private val leaveState = { stateController.navigateToMainMenu() }

    private var levelMap: LevelMap? = null
    private var character: CharacterV2? = null

    override fun enter() {
        levelMap =
            levelManager.getLevel(levelName)?.let { LevelMap.create(it.mapData, emptyList()) }
        val characterData = CharacterAnimations.newBuilder().setSpriteSheetName("jacob")
            .setIdleAnimationName("idle").setMovementAnimationName("moving")
            .setJumpAnimationName("jumping").build()
        character = characterFactory.create(
            animationsParser.parse(
                characterData
            ), CharacterV2.DamageListener.NONE, 0f, 0f, Faction.GOOD
        )
        inputAdapter.addActionStartListener(GuiAction.EXECUTE_ACTION, leaveState)
        levelMap?.bind()
        character?.bind()
    }

    override fun leave() {
        inputAdapter.removeActionStartListener(GuiAction.EXECUTE_ACTION, leaveState)
        levelMap?.unbind()
        character?.unbind()
    }

    override fun update(delta: Int) {
        character?.update(delta)
    }

    override fun render(g: Graphics) {
        levelMap?.render(g)
        character?.render(g)
    }

}