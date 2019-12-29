package believe.gamestate.levelstate;

import believe.action.ChangeToTemporaryStateAction;
import believe.character.Character.DamageListener;
import believe.character.Faction;
import believe.character.playable.PlayableCharacter;
import believe.character.playable.PlayableCharacterFactory;
import believe.core.io.FontLoader;
import believe.datamodel.MutableValue;
import believe.gamestate.GameStateBase;
import believe.gamestate.temporarystate.GameOverState;
import believe.gamestate.temporarystate.GamePausedOverlay;
import believe.gamestate.temporarystate.OverlayablePrecedingState;
import believe.gamestate.temporarystate.PrecedingState;
import believe.gui.ProgressBar;
import believe.level.LevelData;
import believe.level.LevelManager;
import believe.map.data.MapData;
import believe.map.gui.PlayArea;
import believe.physics.manager.PhysicsManager;
import javax.annotation.Nullable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class LevelState extends GameStateBase
    implements OverlayablePrecedingState, DamageListener {

  private final LevelManager levelManager;
  private final GameContainer container;
  private final ChangeToTemporaryStateAction<OverlayablePrecedingState> pauseAction;
  private final ChangeToTemporaryStateAction<PrecedingState> gameOverAction;
  private final PlayableCharacterFactory playableCharacterFactory;
  private final MutableValue<Optional<PlayableCharacter>> currentPlayableCharacter;
  private final PhysicsManager physicsManager;

  private boolean enteringFromPauseMenu;
  @Nullable private LevelData levelData;
  private ProgressBar focusBar;

  protected StateBasedGame game;
  @Nullable protected PlayArea playArea;
  @Nullable protected PlayableCharacter player;

  public LevelState(
      GameContainer container,
      StateBasedGame game,
      LevelManager levelManager,
      PhysicsManager physicsManager,
      FontLoader fontLoader,
      PlayableCharacterFactory playableCharacterFactory,
      MutableValue<Optional<PlayableCharacter>> currentPlayableCharacter) {
    this.container = container;
    this.game = game;
    this.levelManager = levelManager;
    this.physicsManager = physicsManager;
    this.pauseAction = new ChangeToTemporaryStateAction<>(GamePausedOverlay.class, this, game);
    this.gameOverAction = new ChangeToTemporaryStateAction<>(GameOverState.class, this, game);
    this.playableCharacterFactory = playableCharacterFactory;
    this.currentPlayableCharacter = currentPlayableCharacter;
    this.focusBar = new ProgressBar(container, fontLoader.getBaseFontAtSize(15));
    this.focusBar.setBorderSize(1);
    this.focusBar.setTextPadding(0);
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {}

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    playArea.render(container, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    playArea.update(delta);
    player.update(delta);
    physicsManager.update(delta);
    focusBar.setProgress(player.getFocus());
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);

    switch (key) {
      case Input.KEY_ESCAPE:
        enteringFromPauseMenu = true;
        pauseAction.activate();
        break;
    }
  }

  @Override
  public void reset() {
    player.setLocation(
        levelData.getMapData().tiledMapData().playerStartX(),
        levelData.getMapData().tiledMapData().playerStartY() - player.getHeight());
    player.setVerticalSpeed(0);
    player.heal(1f);
    playArea.resetLayout();
    initPhysics();
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    if (!enteringFromPauseMenu) {
      levelData = getLevelData();
      MapData mapData = levelData.getMapData();
      player =
          playableCharacterFactory.create(
              this,
              isOnRails(),
              mapData.tiledMapData().playerStartX(),
              mapData.tiledMapData().playerStartY());
      currentPlayableCharacter.update(Optional.of(player));
      playArea = providePlayArea(mapData, player);
      focusBar.setText("Focus");
      playArea.addHudChild(focusBar, 0.02f, 0.05f, 0.15f, 0.07f);
      initPhysics();

      levelEnter(container, game);
      levelData.getInitialCommand().execute();
    }
  }

  private LevelData getLevelData() {
    LevelData levelData = levelManager.getLevel(getLevelName());
    if (levelData == null) {
      // TODO(#16): Show an error screen instead of throwing an exception.
      throw new IllegalStateException("Cannot initialize level due to missing level data.");
    }
    return levelData;
  }

  public void reloadLevel() {
    int playerX = player.getX();
    int playerY = player.getY();
    levelData = getLevelData();
    playArea.reloadMap(levelData.getMapData());
    reset();
    player.setLocation(playerX, playerY);
  }

  @Override
  public void exitingFollowingState() {
    this.enteringFromPauseMenu = false;
  }

  private void initPhysics() {
    physicsManager.reset();
    MapData mapData = levelData.getMapData();
    Stream.concat(
            mapData.tiledMapData().objectLayers().stream()
                .flatMap(
                    objectLayerData ->
                        objectLayerData.generatedMapEntityData().physicsManageables().stream()),
            mapData.tiledMapData().layers().stream()
                .flatMap(
                    layerData -> layerData.generatedMapEntityData().physicsManageables().stream()))
        .forEach(entity -> entity.addToPhysicsManager(physicsManager));
    player.addToPhysicsManager(physicsManager);
  }

  @Override
  public Image getCurrentScreenshot() throws SlickException {
    Image screenshot = new Image(container.getWidth(), container.getHeight());
    container.getGraphics().copyArea(screenshot, 0, 0);
    return screenshot;
  }

  protected abstract boolean isOnRails();

  protected abstract String getLevelName();

  protected abstract String getMusicLocation();

  protected abstract PlayArea providePlayArea(MapData mapData, PlayableCharacter player);

  protected abstract void levelEnter(GameContainer container, StateBasedGame game)
      throws SlickException;

  @Override
  public void damageInflicted(float currentFocus, Faction inflictor) {
    if (currentFocus <= 0 && inflictor != Faction.NONE && inflictor != player.getFaction()) {
      gameOverAction.activate();
    }
  }
}
