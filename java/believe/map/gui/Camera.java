package believe.map.gui;

import believe.core.display.Renderable;
import believe.geometry.Rectangle;
import believe.react.ObservableValue;
import believe.react.Observer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class Camera implements Renderable {
  private final ObservableValue<Rectangle> rect;
  private final List<Renderable> children;
  private final int mapWidth;
  private final int mapHeight;

  private float scaleX;
  private float scaleY;

  Camera(float width, float height, int mapWidth, int mapHeight) {
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    scaleX = 1;
    scaleY = 1;
    rect = new ObservableValue<>(new Rectangle(0, 0, width, height));
    children = new ArrayList<>();
  }

  Rectangle getRect() {
    return rect.get();
  }

  void center(float x, float y) {
    Rectangle newRectangle = Rectangle.copyFrom(rect.get());
    newRectangle.setCenterX(x);
    newRectangle.setCenterY(y);

    if (newRectangle.getX() < 0) {
      newRectangle.setX(0);
    } else if (newRectangle.getMaxX() > mapWidth) {
      newRectangle.setX(mapWidth - newRectangle.getWidth());
    }

    if (newRectangle.getY() < 0) {
      newRectangle.setY(0);
    } else if (newRectangle.getMaxY() > mapHeight) {
      newRectangle.setY(mapHeight - newRectangle.getHeight());
    }

    rect.setValue(newRectangle);
  }

  void scale(float x, float y) {
    scaleX = x;
    scaleY = y;
    Rectangle newRectangle = Rectangle.copyFrom(rect.get());
    newRectangle.setSize(newRectangle.getWidth() * (1 / x), newRectangle.getHeight() * (1 / y));
    rect.setValue(newRectangle);
  }

  void addAllObservers(Collection<? extends Observer<Rectangle>> observers) {
    rect.addAllObservers(observers);
  }

  void addChild(Renderable child) {
    children.add(child);
  }

  void addAllChildren(Collection<? extends Renderable> children) {
    this.children.addAll(children);
  }

  void removeChild(Renderable child) {
    children.remove(child);
  }

  Vector2f cameraToWindow(Vector2f point) {
    return new Vector2f(
        (point.x - rect.get().getX()) * scaleX, (point.y - rect.get().getY()) * scaleY);
  }

  @Override
  public void render(GUIContext context, Graphics g) throws SlickException {
    Rectangle rectangle = rect.get();
    g.pushTransform();
    g.scale(scaleX, scaleY);
    g.translate(-rectangle.getX(), -rectangle.getY());

    for (Renderable child : children) {
      child.render(context, g);
    }

    g.popTransform();
  }
}
