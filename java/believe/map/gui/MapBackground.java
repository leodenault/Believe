package believe.map.gui;

import believe.core.display.Camera.Layerable;
import believe.gui.ComponentBase;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class MapBackground extends ComponentBase implements Layerable {
  private Image image;
  private int layer;

  public MapBackground(GUIContext container, Image image, int layer, int y) {
    super(container, 0, y);
    this.image = image;
    this.layer = layer;
    rect.setWidth(this.image.getWidth());
    rect.setHeight(this.image.getHeight());
  }

  @Override
  public int getLayer() {
    return layer;
  }

  @Override
  public void resetLayout() {}

  @Override
  public void renderComponent(GUIContext context, Graphics g, float xMin, float xMax)
      throws SlickException {
    float left;
    for (left = getX() + xMin - (xMin % getWidth()); left < xMax; left += getWidth()) {
      g.drawImage(image, left, getY());
    }
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    renderComponent(context, g, 0, 0);
  }
}
