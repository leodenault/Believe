package believe.map.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import believe.gui.AbstractContainer;
import believe.gui.CanvasContainer;
import believe.gui.ComponentBase;
import believe.map.data.MapData;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import believe.core.display.Camera;
import believe.core.display.Camera.Layerable;
import believe.util.Util;

@AutoFactory(allowSubclasses = true)
public class PlayArea extends AbstractContainer {
  private class DynamicHudChild {
    public ComponentBase child;
    public float offsetX;
    public float offsetY;

    public DynamicHudChild(ComponentBase child, float offsetX, float offsetY) {
      this.child = child;
      this.offsetX = offsetX;
      this.offsetY = offsetY;
    }
  }

  private final LevelMapFactory levelMapFactory;

  private boolean border;
  private LevelMap levelMap;
  private Camera camera;
  private ComponentBase focus;
  private CanvasContainer hud;
  private List<DynamicHudChild> dynamicHudChildren;

  /**
   * Creates the PlayArea using width and height as percentages of the screen size. Assumes that the
   * origin is at 0,0.
   */
  public PlayArea(
      @Provided GUIContext container,
      @Provided LevelMapFactory levelMapFactory,
      MapData mapData,
      ComponentBase focus,
      float clipWidth,
      float clipHeight) {
    super(
        container,
        0,
        0,
        convertPercentageToPixels(clipWidth, container.getWidth(), 0),
        convertPercentageToPixels(clipHeight, container.getHeight(), 0));
    this.levelMapFactory = levelMapFactory;
    int width = convertPercentageToPixels(clipWidth, container.getWidth(), 0);
    int height = convertPercentageToPixels(clipHeight, container.getHeight(), 0);
    border = false;
    camera = new Camera(width, height);
    levelMap = levelMapFactory.create(mapData);
    this.focus = focus;
    this.hud = new CanvasContainer(container, 0, 0, width, height);
    handleMap();
    camera.scale(
        (float) container.getWidth() / LevelMap.TARGET_WIDTH,
        (float) container.getHeight() / LevelMap.TARGET_HEIGHT);
    this.dynamicHudChildren = new LinkedList<DynamicHudChild>();
  }

  private void handleMap() {
    camera.addChild(levelMap);
    levelMap.setFocus(focus);

    for (MapBackground background : levelMap.getBackgrounds()) {
      camera.addChild(background);
    }
  }

  public void reloadMap(MapData newMapData) {
    camera.removeChild(levelMap);
    levelMap = levelMapFactory.create(newMapData);
    handleMap();
  }

  public void setBorder(boolean border) {
    this.border = border;
  }

  @Override
  public void resetLayout() {
    levelMap.reset();
  }

  // This is so we can handle moving the children in the graphics
  // rendering part. The play area behaves a little differently from
  // GUI components
  @Override
  public void setLocation(int x, int y) {
    if (rect != null) {
      rect.setLocation(x, y);
    }
  }

  public void addChild(Layerable child) {
    camera.addChild(child);
  }

  public void addHudChild(ComponentBase child) {
    hud.addChild(child);
  }

  public void addHudChild(ComponentBase child, float minX, float minY, float maxX, float maxY) {
    hud.addChild(child);

    int x1 = convertPercentageToPixels(minX, getWidth(), getX());
    int y1 = convertPercentageToPixels(minY, getHeight(), getY());
    int x2 = convertPercentageToPixels(maxX, getWidth(), getX());
    int y2 = convertPercentageToPixels(maxY, getHeight(), getY());
    child.setLocation(x1, y1);
    child.setWidth(x2 - x1);
    child.setHeight(y2 - y1);
  }

  public void attachHudChildToFocus(
      ComponentBase child, float offsetX, float offsetY, float width, float height) {
    hud.addChild(child);
    child.setWidth(convertPercentageToPixels(width, getWidth(), 0));
    child.setHeight(convertPercentageToPixels(height, getHeight(), 0));
    dynamicHudChildren.add(new DynamicHudChild(child, offsetX, offsetY));
  }

  public void removeHudChild(ComponentBase child) {
    hud.removeChild(child);
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    g.pushTransform();
    Rectangle oldClip = Util.changeClipContext(g, rect);
    g.translate(getX(), getY());

    camera.render(context, g);

    Util.resetClipContext(g, oldClip);
    g.popTransform();

    if (border) {
      g.setColor(new Color(0xffffff));
      g.draw(rect);
    }

    hud.render(context, g);
  }

  public void update(int delta) {
    levelMap.update(delta);
    Rectangle rect = focus.rect();
    camera.center(rect.getCenterX(), rect.getCenterY());

    Rectangle camRect = camera.getRect();
    if (camRect.getX() < 0) {
      camRect.setX(0);
    } else if (camRect.getMaxX() > levelMap.getWidth()) {
      camRect.setX(levelMap.getWidth() - camRect.getWidth());
    }

    if (camRect.getY() < 0) {
      camRect.setY(0);
    } else if (camRect.getMaxY() > levelMap.getHeight()) {
      camRect.setY(levelMap.getHeight() - camRect.getHeight());
    }

    updateDynamicHudChildren();
  }

  private void updateDynamicHudChildren() {
    Rectangle rect = focus.rect();
    Vector2f focusLocation = camera.cameraToWindow(new Vector2f(rect.getCenter()));

    for (DynamicHudChild child : dynamicHudChildren) {
      int ox = convertPercentageToPixels(child.offsetX, getWidth(), 0);
      int oy = convertPercentageToPixels(child.offsetY, getHeight(), 0);
      child.child.setLocation((int) (focusLocation.x + ox), (int) (focusLocation.y + oy));
    }
  }

  private static int convertPercentageToPixels(float percentage, float length, float offset) {
    return (int) ((percentage * length) + offset);
  }
}
