package believe.gamestate;

import believe.character.Character.DamageListener;
import believe.character.Faction;
import believe.character.playable.EnemyCharacter;
import believe.character.playable.PlayableCharacter;
import believe.gui.ProgressBar;
import believe.map.gui.LevelMap;
import believe.map.gui.MapManager;
import believe.map.gui.PlayArea;
import believe.physics.manager.PhysicsManager;
import javax.annotation.Nullable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;

public abstract class LevelState extends GameStateBase
    implements OverlayablePrecedingState, DamageListener {

  private final MapManager mapManager;
  private final GameContainer container;
  private final ChangeToTemporaryStateAction<OverlayablePrecedingState> pauseAction;
  private final ChangeToTemporaryStateAction<PrecedingState> gameOverAction;
  private final PhysicsManager physicsManager;

  private boolean enteringFromPauseMenu;
  @Nullable
  private LevelMap map;
  private ProgressBar focusBar;
  @Nullable
  private PhysicsManager physics;

  protected StateBasedGame game;
  @Nullable
  protected PlayArea playArea;
  @Nullable
  protected PlayableCharacter player;

  public LevelState(
      GameContainer container,
      StateBasedGame game,
      MapManager mapManager,
      PhysicsManager physicsManager) {
    this.container = container;
    this.game = game;
    this.mapManager = mapManager;
    this.physicsManager = physicsManager;
    this.pauseAction = new ChangeToTemporaryStateAction<>(GamePausedOverlay.class, this, game);
    this.gameOverAction = new ChangeToTemporaryStateAction<>(GameOverState.class, this, game);
    this.focusBar = new ProgressBar(container);
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    playArea.render(container, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    playArea.update(delta);
    map.update(delta);
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
    map.reset();
    player.setLocation(map.getPlayerStartX(), map.getPlayerStartY() - player.getHeight());
    player.setVerticalSpeed(0);
    player.heal(1f);
    initPhysics();
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    if (!enteringFromPauseMenu) {
      map = mapManager.getMap(getMapName(), container, false);
      player =
          new PlayableCharacter(container,
              this,
              physicsManager,
              isOnRails(),
              map.getPlayerStartX(),
              map.getPlayerStartY());
      playArea = providePlayArea(container, map, player);
      focusBar.setText("Focus");
      playArea.addHudChild(focusBar, 0.02f, 0.05f, 0.15f, 0.07f);
      initPhysics();

      levelEnter(container, game);
    }
  }

  public void reloadLevel(GameContainer container) throws SlickException {
    int playerX = player.getX();
    int playerY = player.getY();
    map = mapManager.getMap(getMapName(), container, true);
    playArea.reloadMap(map);
    reset();
    player.setLocation(playerX, playerY);
  }

  @Override
  public void exitingFollowingState() {
    this.enteringFromPauseMenu = false;
  }

  private void initPhysics() {
    physicsManager.reset();
    physicsManager.addStaticCollidables(map.getCollidableTiles());
    physicsManager.addStaticCollidables(map.getCommands());
    List<EnemyCharacter> enemies = map.getEnemies();
    physicsManager.addCollidables(enemies);
    physicsManager.addCollidable(player);
    physicsManager.addGravityObject(player);
    physicsManager.addGravityObjects(enemies);
  }

  @Override
  public Image getCurrentScreenshot() throws SlickException {
    Image screenshot = new Image(container.getWidth(), container.getHeight());
    container.getGraphics().copyArea(screenshot, 0, 0);
    return screenshot;
  }

  protected abstract boolean isOnRails();

  protected abstract String getMapName();

  protected abstract String getMusicLocation();

  protected abstract PlayArea providePlayArea(
      GameContainer container, LevelMap map, PlayableCharacter player);

  protected abstract void levelEnter(GameContainer container, StateBasedGame game)
      throws SlickException;

  @Override
  public void damageInflicted(float currentFocus, Faction inflictor) {
    if (currentFocus <= 0 && inflictor != Faction.NONE && inflictor != player.getFaction()) {
      gameOverAction.activate();
    }
  }
}
