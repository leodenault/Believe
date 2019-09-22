package believe.gamestate.levelstate.platformingstate;

import believe.audio.Music;
import believe.character.playable.PlayableCharacter;
import believe.character.playable.PlayableCharacter.SynchedComboListener;
import believe.character.playable.PlayableCharacterFactory;
import believe.core.SynchedComboPattern;
import believe.core.io.FontLoader;
import believe.gamestate.levelstate.LevelState;
import believe.levelFlow.component.ComboSyncher;
import believe.map.data.MapData;
import believe.map.io.MapManager;
import believe.map.gui.PlayArea;
import believe.map.gui.PlayAreaFactory;
import believe.physics.manager.PhysicsManager;
import javax.inject.Inject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Map;
import java.util.function.Function;

public class PlatformingState extends LevelState implements SynchedComboListener {

  private static final int BPM = 150;
  private static final float FOCUS_DRAIN_TIME = 60f; // Time in seconds for recharging focus fully
  private static final float FOCUS_DRAIN_RATE =
      PlayableCharacter.MAX_FOCUS / (FOCUS_DRAIN_TIME * 1000f);

  private final ComboSyncher comboSyncher;
  private final Map<Integer, Function<PlatformingState, Void>> eventActions;
  private final PlayAreaFactory playAreaFactory;

  private Music music;

  @Inject
  public PlatformingState(
      GameContainer container,
      StateBasedGame game,
      MapManager mapManager,
      PhysicsManager physicsManager,
      @EventActions Map<Integer, Function<PlatformingState, Void>> eventActions,
      FontLoader fontLoader,
      PlayAreaFactory playAreaFactory,
      PlayableCharacterFactory playableCharacterFactory) {
    super(container, game, mapManager, physicsManager, fontLoader, playableCharacterFactory);
    this.playAreaFactory = playAreaFactory;
    this.comboSyncher = new ComboSyncher(container, fontLoader.getBaseFont(), BPM);
    this.eventActions = eventActions;
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    super.update(container, game, delta);

    comboSyncher.update();
    if (music.paused()) {
      music.resume();
    } else if (!music.playing()) {
      music.loop();
    }
    player.heal(delta * FOCUS_DRAIN_RATE);
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);

    switch (key) {
      case Input.KEY_ESCAPE:
        music.pause();
        break;
    }

    if (eventActions.containsKey(key)) {
      eventActions.get(key).apply(this);
    }
  }

  @Override
  public void levelEnter(GameContainer container, StateBasedGame game) throws SlickException {
    music = new Music(getMusicLocation(), BPM);
    playArea.attachHudChildToFocus(comboSyncher, 0.05f, -0.08f, 0.3f, 0.05f);
    player.addComboListener(this);
    music.stop();
  }

  @Override
  public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    super.leave(container, game);
    music.pause();
  }

  @Override
  public void reset() {
    super.reset();
    music.stop();
  }

  @Override
  public void activateCombo(SynchedComboPattern pattern) {
    comboSyncher.setPattern(pattern);
    comboSyncher.start(music);
  }

  @Override
  protected boolean isOnRails() {
    return false;
  }

  @Override
  protected String getMapName() {
    return "snow";
  }

  @Override
  protected String getMusicLocation() {
    return "/res/music/Passepied.ogg";
  }

  @Override
  protected PlayArea providePlayArea(MapData map, PlayableCharacter player) {
    return playAreaFactory.create(map, player, 1f, 1f);
  }
}