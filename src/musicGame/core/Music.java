package musicGame.core;

import org.newdawn.slick.SlickException;

public class Music extends org.newdawn.slick.Music {
  private int bpm;

  public Music(String ref, int bpm) throws SlickException {
    super(ref);
    this.bpm = bpm;
  }

  public int getBpm() {
    return bpm;
  }

  public boolean paused() {
    return !playing() && getPosition() > 0;
  }
}
