package believe.gui;

import static believe.geometry.RectangleKt.mutableRectangle;

import believe.core.display.Renderable;
import believe.geometry.MutableRectangle;
import believe.geometry.Rectangle;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public abstract class ComponentBase extends AbstractComponent implements Renderable {

  protected boolean rendering;
  protected MutableRectangle rect;

  /**
   * Creates a new ComponentBase.
   *
   * @param container The context in which this component will be created and rendered.
   */
  public ComponentBase(GUIContext container) {
    this(container, 0, 0, 0, 0);
  }

  /**
   * Creates a new ComponentBase.
   *
   * @param container The context in which this component will be created and rendered.
   * @param x The x position of this component.
   * @param y The y position of this component.
   */
  public ComponentBase(GUIContext container, int x, int y) {
    this(container, x, y, 0, 0);
  }

  /**
   * Creates a new ComponentBase.
   *
   * @param container The context in which this component will be created and rendered.
   * @param x The x position of this component.
   * @param y The y position of this component.
   * @param width The width of this component.
   * @param height The height of this component.
   */
  public ComponentBase(GUIContext container, int x, int y, int width, int height) {
    super(container);
    this.rect = mutableRectangle(x, y, width, height);
    this.rendering = true;
    // These are necessary because Slick makes it possible to enter an infinite loop. It
    // unfortunately has the capacity for modifying the list of input listeners it's iterating over.
    input.removeListener(this);
    input.addListener(this);
  }

  @Override
  public int getHeight() {
    return (int) rect.getHeight();
  }

  public void setHeight(int height) {
    rect.setHeight(height);
  }

  @Override
  public int getWidth() {
    return (int) rect.getWidth();
  }

  public void setWidth(int width) {
    rect.setWidth(width);
  }

  @Override
  public int getX() {
    return (int) rect.getX();
  }

  @Override
  public int getY() {
    return (int) rect.getY();
  }

  @Override
  public void setLocation(int x, int y) {
    if (rect != null) {
      rect.setX(x);
      rect.setY(y);
      resetLayout();
    }
  }

  public Rectangle rect() {
    return rect;
  }

  public boolean isRendering() {
    return rendering;
  }

  public void setRendering(boolean rendering) {
    this.rendering = rendering;
  }

  // TODO: Migrate the render methods below to stop using GUIContext.
  @Override
  public void render(Graphics g) throws SlickException {
    if (rendering) {
      renderComponent(container, g);
    }
  }

  @Override
  public final void render(GUIContext context, Graphics g) throws SlickException {
    if (rendering) {
      renderComponent(context, g);
    }
  }

  /** Resets the layout of the children of this component. */
  public abstract void resetLayout();
  /** Renders the component within the context of a GUI component */
  protected abstract void renderComponent(GUIContext context, Graphics g) throws SlickException;
}
