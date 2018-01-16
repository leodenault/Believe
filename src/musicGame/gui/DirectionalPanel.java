package musicGame.gui;

import java.util.Iterator;

import musicGame.gui.base.AbstractContainer;
import musicGame.gui.base.ComponentBase;
import org.newdawn.slick.gui.GUIContext;

public class DirectionalPanel extends AbstractContainer {

  protected int spacing;

  private boolean autoposition;
  private int itemWidth;
  private int itemHeight;

  public DirectionalPanel(GUIContext container, int itemWidth, int itemHeight) {
    this(container, itemWidth, itemHeight, 0);
  }

  public DirectionalPanel(GUIContext container, int itemWidth, int itemHeight, int spacing) {
    this(container, 0, 0, itemWidth, itemHeight, spacing);
    autoposition = true;
    rect.setCenterX(container.getWidth() / 2);
    rect.setCenterY(container.getHeight() / 2);
  }

  public DirectionalPanel(GUIContext container, int x, int y, int itemWidth, int itemHeight) {
    this(container, x, y, itemWidth, itemHeight, 0);
  }

  public DirectionalPanel(GUIContext container, int x, int y, int itemWidth, int itemHeight, int spacing) {
    super(container, x, y, itemWidth, 0);
    this.spacing = spacing;
    this.itemWidth = itemWidth;
    this.itemHeight = itemHeight;
    this.autoposition = false;
  }

  public void addChild(ComponentBase child) {
    this.children.add(child);
    child.setWidth(itemWidth);
    child.setHeight(itemHeight);

    int numChildren = this.children.size();
    int totalSpace = (numChildren - 1) * spacing;
    rect.setHeight(numChildren * itemHeight + totalSpace);

    if (autoposition) {
      rect.setCenterX(container.getWidth() / 2);
      rect.setCenterY(container.getHeight() / 2);
    }

    resetLayout();
  }

  @Override
  public void removeChild(ComponentBase child) {
    this.children.remove(child);
    this.resetLayout();
  }

  public void clear() {
    children.clear();
  }

  @Override
  public void resetLayout() {
    int position = 0;
    int parentY = (int)rect.getY();

    for (Iterator<ComponentBase> it = children.iterator(); it.hasNext(); position++) {
      ComponentBase item = it.next();
      int y = parentY + (position * (itemHeight + spacing));
      item.setLocation((int)rect.getX(), y);
    }
  }
}
