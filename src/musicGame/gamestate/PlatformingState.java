package musicGame.gamestate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.character.PlayableCharacter;
import musicGame.character.PlayableCharacter.SynchedComboListener;
import musicGame.core.Music;
import musicGame.core.SynchedComboPattern;
import musicGame.gui.ComboSyncher;
import musicGame.gui.PlayArea;
import musicGame.map.gui.LevelMap;

public class PlatformingState extends LevelState implements SynchedComboListener {
  public PlatformingState(GameContainer container, StateBasedGame game) throws SlickException {
    super(container, game);
    this.music = new Music(getMusicLocation(), BPM);
    this.comboSyncher = new ComboSyncher(container, BPM);
  }

  private static final int BPM = 150;
  private static final float FOCUS_DRAIN_TIME = 60f; // Time in seconds for recharging focus fully
  private static final float FOCUS_DRAIN_RATE = PlayableCharacter.MAX_FOCUS / (FOCUS_DRAIN_TIME * 1000f);

  private ComboSyncher comboSyncher;
  private Music music;

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
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
  }

  @Override
  public void levelEnter(GameContainer container, StateBasedGame game) throws SlickException {
    playArea.attachHudChildToFocus(comboSyncher, 0.05f, -0.08f, 0.3f, 0.05f);
    player.addComboListener(this);
    music.stop();
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
  protected PlayArea providePlayArea(GameContainer container, LevelMap map, PlayableCharacter player) {
    return new PlayArea(container, map, player);
  }
}
