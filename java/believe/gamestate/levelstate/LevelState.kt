package believe.gamestate.levelstate

import believe.action.ChangeToTemporaryStateAction
import believe.character.Character.DamageListener
import believe.character.Faction
import believe.character.playable.PlayableCharacter
import believe.character.playable.PlayableCharacterFactory
import believe.core.io.FontLoader
import believe.datamodel.MutableValue
import believe.gamestate.GameStateBase
import believe.gamestate.temporarystate.GameOverState
import believe.gamestate.temporarystate.GamePausedOverlay
import believe.gamestate.temporarystate.OverlayablePrecedingState
import believe.gamestate.temporarystate.PrecedingState
import believe.gui.ProgressBar
import believe.level.LevelData
import believe.level.LevelManager
import believe.map.data.LayerData
import believe.map.data.MapData
import believe.map.data.ObjectLayerData
import believe.map.gui.PlayArea
import believe.physics.manager.PhysicsManageable
import believe.physics.manager.PhysicsManager
import believe.scene.GeneratedMapEntityData
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.newdawn.slick.Input
import org.newdawn.slick.SlickException
import org.newdawn.slick.state.StateBasedGame
import java.util.*
import java.util.stream.Stream

abstract class LevelState(
    private val container: GameContainer,
    protected var game: StateBasedGame,
    private val levelManager: LevelManager,
    private val physicsManager: PhysicsManager,
    fontLoader: FontLoader,
    private val playableCharacterFactory: PlayableCharacterFactory,
    private val currentPlayableCharacter: MutableValue<Optional<PlayableCharacter>>
) : GameStateBase(), OverlayablePrecedingState, DamageListener {

    private val pauseAction: ChangeToTemporaryStateAction<OverlayablePrecedingState> =
        ChangeToTemporaryStateAction(
            GamePausedOverlay::class.java, this, game
        )
    private val gameOverAction: ChangeToTemporaryStateAction<PrecedingState> =
        ChangeToTemporaryStateAction(GameOverState::class.java, this, game)
    private var enteringFromPauseMenu = false
    private var levelData: LevelData? = null
    private val focusBar: ProgressBar =
        ProgressBar(container, fontLoader.getBaseFontAtSize(15f)).apply {
            setBorderSize(1)
            setTextPadding(0)
        }
    protected var playArea: PlayArea? = null
    protected var player: PlayableCharacter? = null

    protected abstract val isOnRails: Boolean
    protected abstract val levelName: String?
    protected abstract val musicLocation: String?

    @Throws(SlickException::class)
    override fun init(container: GameContainer, game: StateBasedGame) {
    }

    @Throws(SlickException::class)
    override fun render(
        container: GameContainer, game: StateBasedGame, g: Graphics
    ) {
        playArea?.render(container, g)
    }

    @Throws(SlickException::class)
    override fun update(
        container: GameContainer, game: StateBasedGame, delta: Int
    ) {
        playArea?.update(delta)
        physicsManager.update(delta.toLong())
        player?.let {
            it.update(delta.toLong())
            focusBar.setProgress(it.focus)
        }
    }

    override fun keyPressed(key: Int, c: Char) {
        super.keyPressed(key, c)
        when (key) {
            Input.KEY_ESCAPE -> {
                enteringFromPauseMenu = true
                pauseAction.activate()
            }
        }
    }

    override fun reset() {
        levelData?.let {
            player?.setLocation(
                it.mapData.tiledMapData().playerStartX(),
                it.mapData.tiledMapData().playerStartY() - (player?.height ?: 0)
            )
        }
        player?.verticalSpeed = 0f
        player?.heal(1f)
        playArea?.resetLayout()
        initPhysics()
    }

    @Throws(SlickException::class)
    override fun enter(container: GameContainer, game: StateBasedGame) {
        super.enter(container, game)
        if (!enteringFromPauseMenu) {
            levelData = getLevelData()
            levelData?.let {
                val mapData = it.mapData
                player = playableCharacterFactory.create(
                    this,
                    isOnRails,
                    mapData.tiledMapData().playerStartX(),
                    mapData.tiledMapData().playerStartY()
                ).also { playableCharacter ->
                    currentPlayableCharacter.update(Optional.of(playableCharacter))
                }
                playArea = providePlayArea(mapData, player)
            }
            focusBar.setText("Focus")
            playArea?.addHudChild(focusBar, 0.02f, 0.05f, 0.15f, 0.07f)
            initPhysics()
            levelEnter(container, game)
            levelData?.initialCommand?.execute()
        }
    }

    private fun getLevelData(): LevelData {
        return levelName?.let { levelManager.getLevel(it) }
            ?: // TODO(#16): Show an error screen instead of throwing an exception.
            throw IllegalStateException("Cannot initialize level due to missing level data.")
    }

    fun reloadLevel() {
        val playerX = player?.x ?: 0
        val playerY = player?.y ?: 0
        levelData = getLevelData()
        levelData?.let {
            playArea?.reloadMap(it.mapData)
        }
        reset()
        player?.setLocation(playerX, playerY)
    }

    override fun exitingFollowingState() {
        enteringFromPauseMenu = false
    }

    private fun initPhysics() {
        physicsManager.reset()
        val mapData = levelData!!.mapData
        val generatedMapEntityDataBuilder = GeneratedMapEntityData.newBuilder()
        mapData.tiledMapData().objectLayers().stream().forEach { objectLayerData: ObjectLayerData ->
            objectLayerData.objectFactories()
                .forEach { it.createAndAddTo(generatedMapEntityDataBuilder) }
        }

        generatedMapEntityDataBuilder.build().physicsManageables()
            .forEach { entity: PhysicsManageable ->
                entity.addToPhysicsManager(physicsManager)
            }
        player!!.addToPhysicsManager(physicsManager)
    }

    @Throws(SlickException::class)
    override fun getCurrentScreenshot(): Image {
        val screenshot = Image(container.width, container.height)
        container.graphics.copyArea(screenshot, 0, 0)
        return screenshot
    }

    protected abstract fun providePlayArea(mapData: MapData?, player: PlayableCharacter?): PlayArea?

    @Throws(SlickException::class)
    protected abstract fun levelEnter(container: GameContainer?, game: StateBasedGame?)

    override fun damageInflicted(
        currentFocus: Float, inflictor: Faction
    ) {
        if (currentFocus <= 0 && inflictor != Faction.NONE && inflictor != player?.faction) {
            gameOverAction.activate()
        }
    }
}