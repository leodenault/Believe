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

public abstract class LevelState extends GameStateBase implements PausableState, DamageListener {

  private final MapManager mapManager;
  private final GameContainer container;

  private boolean enteringFromPauseMenu;
  @Nullable
  private LevelMap map;
  private ProgressBar focusBar;
  @Nullable
  private PhysicsManager physics;
  @Nullable
  private Graphics graphics;

  protected StateBasedGame game;
  @Nullable
  protected PlayArea playArea;
  @Nullable
  protected PlayableCharacter player;

  public LevelState(GameContainer container, StateBasedGame game, MapManager mapManager) {
    this.mapManager = mapManager;
    this.container = container;
    this.game = game;
    this.focusBar = new ProgressBar(container);
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    playArea.render(container, g);
    graphics = g;
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    playArea.update(delta);
    map.update(delta);
    player.update(delta);
    physics.update(delta);
    focusBar.setProgress(player.getFocus());
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);

    switch (key) {
      case Input.KEY_ESCAPE:
        enteringFromPauseMenu = true;
        Image backgroundImage = null;
        try {
          backgroundImage = new Image(container.getWidth(), container.getHeight());
        } catch (SlickException e) {
          e.printStackTrace();
        }
        graphics.copyArea(backgroundImage, 0, 0);
        new PauseGameAction(GamePausedOverlay.class, this, game).pause(backgroundImage);
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
  public void exitFromPausedState() {
    this.enteringFromPauseMenu = false;
  }

  private void initPhysics() {
    physics = PhysicsManager.getInstance();
    physics.reset();
    physics.addStaticCollidables(map.getCollidableTiles());
    physics.addStaticCollidables(map.getCommands());
    List<EnemyCharacter> enemies = map.getEnemies();
    physics.addCollidables(enemies);
    physics.addCollidable(player);
    physics.addGravityObject(player);
    physics.addGravityObjects(enemies);
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
      new ChangeStateAction(GameOverState.class, game).componentActivated(/* component= */ null);
    }
  }
}
