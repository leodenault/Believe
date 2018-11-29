package believe.gamestate;

import java.io.IOException;
import java.io.InputStreamReader;

import believe.character.Faction;
import believe.map.gui.MapManager;
import believe.physics.manager.PhysicsManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import believe.character.playable.PlayableCharacter;
import believe.core.Options;
import believe.map.gui.PlayArea;
import believe.levelFlow.component.FlowComponent;
import believe.levelFlow.component.FlowComponentListener;
import believe.levelFlow.parsing.FlowComponentBuilder;
import believe.levelFlow.parsing.FlowFileParser;
import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;
import believe.map.gui.LevelMap;

public class ArcadeState extends LevelState implements FlowComponentListener {

  private static final float FOCUS_RECHARGE_TIME = 45f; // Time in seconds for draining focus fully
  private static final float FOCUS_RECHARGE_RATE = PlayableCharacter.MAX_FOCUS / (FOCUS_RECHARGE_TIME * 1000f);
  private static final float HEALTH_PER_SUCCESS = 0.01f;
  private static final float DAMAGE_PER_FAILURE = 0.03f;

  private FlowComponent component;

  public ArcadeState(GameContainer container, StateBasedGame game, PhysicsManager physicsManager) throws SlickException {
    super(container, game, MapManager.defaultManager(), physicsManager);
    FlowComponentBuilder builder = new FlowComponentBuilder(container, (int)(0.2 * container.getWidth()));
    try {
      new FlowFileParser(
          new InputStreamReader(
              ResourceLoader.getResourceAsStream("levelFlowFiles/Drive.lfl")), builder).parse();
      component = builder.buildFlowComponent();
      component.setSpeedMultiplier(Options.getInstance().flowSpeed);
      component.setLocation((int)(0.8 * container.getWidth()), 0);
      component.setHeight(container.getHeight());
      component.addListener(this);
    } catch (IOException | FlowFileParserException | FlowComponentBuilderException e) {
      throw new RuntimeException(String.format(
          "Could not start arcade state because of the following exception:\n\n %s\n%s",
          e.getMessage(),
          e.getStackTrace()));
    }
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    super.update(container, game, delta);
    component.update(delta);
    if (!component.isPlaying()) {
      component.play();
    }
    player.inflictDamage(delta * FOCUS_RECHARGE_RATE, Faction.NONE);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    super.render(container, game, g);
    component.render(container, g);
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);
    if (key == Input.KEY_ESCAPE) {
      this.component.pause();
    }
  }

  @Override
  public void reset() {
    super.reset();
    component.reset();
  }

  @Override
  public void levelEnter(GameContainer container, StateBasedGame game) throws SlickException {
    component.stop();
  }

  @Override
  protected boolean isOnRails() {
    return true;
  }

  @Override
  protected String getMapName() {
    return "pipeTown";
  }

  @Override
  protected String getMusicLocation() {
    return "/res/music/Evasion.ogg";
  }

  @Override
  protected PlayArea providePlayArea(GameContainer container, LevelMap map, PlayableCharacter player) {
    return new PlayArea(container, map, player, 0.8f, 1f);
  }

  @Override
  public void componentActivated(AbstractComponent source) {}

  @Override
  public void beatSuccess(int index) {
    player.heal(HEALTH_PER_SUCCESS);
  }

  @Override
  public void beatFailed() {
    player.inflictDamage(DAMAGE_PER_FAILURE, Faction.NONE);
  }

  @Override
  public void beatMissed() {}

  @Override
  public void songEnded() {}
}
