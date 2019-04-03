package believe.audio;

import javax.annotation.Nullable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/** Wrapper for {@link org.newdawn.slick.Sound} instances. */
public final class Sound {
  @Nullable private org.newdawn.slick.Sound slickSound;

  public Sound(String location) {
    try {
      slickSound = new org.newdawn.slick.Sound(location);
    } catch (SlickException e) {
      Log.error("Could not load sound at '" + location + "'.", e);
    }
  }

  public void play() {
    if (slickSound != null) {
      slickSound.play();
    }
  }
}
