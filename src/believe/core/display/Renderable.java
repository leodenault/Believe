package believe.core.display;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public interface Renderable {
  void render(GUIContext context, Graphics g);
}
