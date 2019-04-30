package believe.gamestate.temporarystate;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** A {@link PrecedingState} which can also provide a screenshot of its current state. */
public interface OverlayablePrecedingState extends PrecedingState {
  /**
   * Provides a an image of this state's current rendering. The image is used for overlaying
   * another state on top of it.
   */
  Image getCurrentScreenshot() throws SlickException;
}
