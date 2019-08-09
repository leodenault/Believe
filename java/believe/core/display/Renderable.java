package believe.core.display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/** An object that can be drawn to a screen. */
public interface Renderable {
  /**
   * Draws the object to the screen.
   *
   * @param context the application window within which this instance will be drawn.
   * @param g the {@link Graphics} instance that will be used to draw this object to the screen.
   */
  void render(GUIContext context, Graphics g) throws SlickException;
}
