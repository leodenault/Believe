package musicGame.gui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * A simple GUI container for holding components.
 */
public abstract class AbstractContainer extends ComponentBase implements Iterable<ComponentBase> {

  protected List<ComponentBase> children;

  /**
   * Creates a new AbstractContainer.
   *
   * @param container  The context in which this container will be created and rendered.
   */
  public AbstractContainer(GUIContext container) {
    this(container, 0, 0, 0, 0);
  }

  /**
   * Creates a new AbstractContainer.
   *
   * @param container  The context in which this container will be created and rendered.
   * @param x      The x position of this container.
   * @param y      The y position of this container.
   */
  public AbstractContainer(GUIContext container, int x, int y) {
    this(container, x, y, 0, 0);
  }

  /**
   * Creates a new AbstractContainer.
   *
   * @param container  The context in which this container will be created and rendered.
   * @param x      The x position of this container.
   * @param y      The y position of this container.
   * @param width    The width of this container.
   * @param height  The height of this container.
   */
  public AbstractContainer(GUIContext container, int x, int y, int width, int height) {
    super(container, x, y, width, height);
    this.children = new LinkedList<ComponentBase>();
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    for (ComponentBase child : this.children) {
      child.renderComponent(container, g);
    }
  }

  @Override
  public void setLocation(int x, int y) {
    if (rect != null) {
      int deltaX = x - (int)rect.getX();
      int deltaY = y - (int)rect.getY();

      if (this.children != null) {
        for (AbstractComponent child : this.children) {
          child.setLocation(child.getX() + deltaX, child.getY() + deltaY);
        }
      }

      rect.setLocation(x, y);
    }
  }

  @Override
  public Iterator<ComponentBase> iterator() {
    return children.iterator();
  }

  /**
   * Adds a component as a child to this container.
   *
   * @param child  The component to be added.
   */
  public void addChild(ComponentBase child) {
    this.children.add(child);
    this.resetLayout();
  }

  /**
   * Removes the child component from this container.
   *
   * @param child  The component to be removed.
   */
  public void removeChild(ComponentBase child) {
    this.children.remove(child);
    this.resetLayout();
  }
}
