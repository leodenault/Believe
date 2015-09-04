package musicGame.gui;

import musicGame.core.DynamicCollidable;
import musicGame.geometry.Rectangle;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public abstract class ComponentBase extends AbstractComponent implements DynamicCollidable {

	protected boolean rendering;
	protected Rectangle rect;

	/**
	 * Creates a new ComponentBase.
	 * 
	 * @param container	The context in which this component will be created and rendered.
	 */
	public ComponentBase(GUIContext container) {
		this(container, 0, 0, 0, 0);
	}

	/**
	 * Creates a new ComponentBase.
	 * 
	 * @param container	The context in which this component will be created and rendered.
	 * @param x			The x position of this component.
	 * @param y			The y position of this component.
	 */
	public ComponentBase(GUIContext container, int x, int y) {
		this(container, x, y, 0, 0);
	}

	/**
	 * Creates a new ComponentBase.
	 * 
	 * @param container	The context in which this component will be created and rendered.
	 * @param x			The x position of this component.
	 * @param y			The y position of this component.
	 * @param width		The width of this component.
	 * @param height	The height of this component.
	 */
	public ComponentBase(GUIContext container, int x, int y, int width, int height) {
		super(container);
		this.rect = new Rectangle(x, y, width, height);
		this.rendering = true;
	}
	
	@Override
	public int getHeight() {
		return (int)getFloatHeight();
	}
	
	public void setHeight(int height) {
		rect.setHeight(height);
	}

	@Override
	public int getWidth() {
		return (int)getFloatWidth();
	}
	
	public void setWidth(int width) {
		rect.setWidth(width);
	}
	
	public float getFloatHeight() {
		return rect.getHeight();
	}
	
	public void setHeight(float height) {
		rect.setHeight(height);
	}
	
	public float getFloatWidth() {
		return rect.getWidth();
	}
	
	public void setWidth(float width) {
		rect.setWidth(width);
	}

	@Override
	public int getX() {
		return (int)getFloatX();
	}

	@Override
	public int getY() {
		return (int)getFloatY();
	}
	
	public float getFloatX() {
		return rect.getX();
	}
	
	public float getFloatY() {
		return rect.getY();
	}

	@Override
	public void setLocation(int x, int y) {
		if (rect != null) {
			rect.setLocation(x, y);
			resetLayout();
		}
	}
	
	public void setLocation(float x, float y) {
		if (rect != null) {
			rect.setLocation(x, y);
			resetLayout();
		}
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public boolean isRendering() {
		return rendering;
	}
	
	public void setRendering(boolean rendering) {
		this.rendering = rendering;
	}

	@Override
	public final void render(GUIContext context, Graphics g) throws SlickException {
		if (rendering){
			renderComponent(context, g);
		}
	}

	/**
	 * Resets the layout of the children of this component.
	 */
	protected abstract void resetLayout();
	/**
	 * Renders the component within the context of a GUI component
	 */
	protected abstract void renderComponent(GUIContext context, Graphics g);
}
