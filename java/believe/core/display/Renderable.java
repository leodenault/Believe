package believe.core.display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 * An object that can be drawn to a screen.
 *
 * @deprecated use {@link RenderableV2} instead.
 */
@Deprecated
public interface Renderable {
  /**
   * Draws the object to the screen.
   *
   * @param g the {@link Graphics} instance that will be used to draw this object to the screen.
   */
  void render(Graphics g) throws SlickException;

  /**
   * Draws the object to the screen.
   *
   * @param context the {@link GUIContext} providing context about the containing application.
   * @param g the {@link Graphics} instance that will be used to draw this object to the screen.
   * @deprecated this is a legacy method that exists only as the rendering code is being migrated.
   *     Please use {@link #render(Graphics)} instead.
   */
  @Deprecated
  default void render(GUIContext context, Graphics g) throws SlickException {
    render(g);
  }
}
