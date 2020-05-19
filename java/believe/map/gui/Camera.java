package believe.map.gui;

import believe.core.display.Renderable;
import believe.geometry.Rectangle;
import believe.react.ObservableValue;
import believe.react.Observer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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

  Camera(int width, int height, int mapWidth, int mapHeight) {
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    scaleX = 1;
    scaleY = 1;
    rect = ObservableValue.of(new Rectangle(0, 0, width, height));
    children = new ArrayList<>();
  }

  Rectangle getRect() {
    return rect.get();
  }

  void center(float x, float y) {
    Rectangle originalRectangle = rect.get();
    Rectangle.Builder newRectangleBuilder =
        Rectangle.newBuilder()
            .setCenterX(x)
            .setCenterY(y)
            .setWidth(originalRectangle.getWidth())
            .setHeight(originalRectangle.getHeight());
    Rectangle newRectangle = newRectangleBuilder.build();

    if (newRectangle.getX() < 0) {
      newRectangleBuilder.setX(0);
    } else if (newRectangle.getMaxX() > mapWidth) {
      newRectangleBuilder.setX(mapWidth - newRectangle.getWidth());
    }

    if (newRectangle.getY() < 0) {
      newRectangleBuilder.setY(0);
    } else if (newRectangle.getMaxY() > mapHeight) {
      newRectangleBuilder.setY(mapHeight - newRectangle.getHeight());
    }

    rect.setValue(newRectangleBuilder.build());
  }

  void scale(float x, float y) {
    scaleX = x;
    scaleY = y;
    Rectangle originalRectangle = rect.get();
    rect.setValue(
        new Rectangle(
            originalRectangle.getX(),
            originalRectangle.getY(),
            originalRectangle.getWidth() * (1 / x),
            originalRectangle.getHeight() * (1 / y)));
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
  public void render(Graphics g) throws SlickException {
    Rectangle rectangle = rect.get();
    g.pushTransform();
    g.scale(scaleX, scaleY);
    g.translate(-rectangle.getX(), -rectangle.getY());

    for (Renderable child : children) {
      child.render(g);
    }

    g.popTransform();
  }
}
