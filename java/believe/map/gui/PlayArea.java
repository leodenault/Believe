package believe.map.gui;

import believe.gui.AbstractContainer;
import believe.gui.CanvasContainer;
import believe.gui.ComponentBase;
import believe.map.data.MapData;
import believe.util.Util;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import java.util.LinkedList;
import java.util.List;

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

  private static final int TARGET_WIDTH = 1600;
  private static final int TARGET_HEIGHT = 900;

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
    camera =
        new Camera(width, height, mapData.tiledMapData().width(), mapData.tiledMapData().height());
    levelMap = levelMapFactory.create(mapData);
    this.focus = focus;
    this.hud = new CanvasContainer(container, 0, 0, width, height);
    handleMap();
    camera.scale(
        (float) container.getWidth() / TARGET_WIDTH, (float) container.getHeight() / TARGET_HEIGHT);
    this.dynamicHudChildren = new LinkedList<DynamicHudChild>();
  }

  private void handleMap() {
    camera.addAllChildren(levelMap.getBackgrounds());
    camera.addAllObservers(levelMap.getBackgrounds());
    camera.addChild(levelMap);
    levelMap.setFocus(focus);
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
      rect.setX(x);
      rect.setY(y);
    }
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
    Rectangle oldClip = Util.changeClipContext(g, rect.asSlickRectangle());
    g.translate(getX(), getY());

    camera.render(context, g);

    Util.resetClipContext(g, oldClip);
    g.popTransform();

    if (border) {
      g.setColor(new Color(0xffffff));
      g.draw(rect.asSlickRectangle());
    }

    hud.render(context, g);
  }

  public void update(int delta) {
    levelMap.update(delta);
    believe.geometry.Rectangle focusRect = focus.rect();
    camera.center(focusRect.getCenterX(), focusRect.getCenterY());
    updateDynamicHudChildren();
  }

  private void updateDynamicHudChildren() {
    believe.geometry.Rectangle rect = focus.rect();
    Vector2f focusLocation =
        camera.cameraToWindow(new Vector2f(rect.getCenterX(), rect.getCenterY()));

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
