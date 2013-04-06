package musicGame.GUI;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * A simple GUI container for holding components.
 */
public abstract class AbstractContainer extends AbstractComponent {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected List<AbstractComponent> children;

	/**
	 * Creates a new AbstractContainer.
	 * 
	 * @param container	The context in which this container will be created and rendered.
	 */
	public AbstractContainer(GUIContext container) {
		super(container);
		this.init(0, 0, 0, 0);
	}

	/**
	 * Creates a new AbstractContainer.
	 * 
	 * @param container	The context in which this container will be created and rendered.
	 * @param x			The x position of this container.
	 * @param y			The y position of this container.
	 */
	public AbstractContainer(GUIContext container, int x, int y) {
		super(container);
		this.init(x, y, 0, 0);
	}

	/**
	 * Creates a new AbstractContainer.
	 * 
	 * @param container	The context in which this container will be created and rendered.
	 * @param x			The x position of this container.
	 * @param y			The y position of this container.
	 * @param width		The width of this container.
	 * @param height	The height of this container.
	 */
	public AbstractContainer(GUIContext container, int x, int y, int width, int height) {
		super(container);
		this.init(x, y, width, height);
	}

	private void init(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.children = new LinkedList<AbstractComponent>();
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		for (AbstractComponent child : this.children) {
			child.render(container, g);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		int deltaX = x - this.x;
		int deltaY = y - this.y;

		if (this.children != null) {
			for (AbstractComponent child : this.children) {
				child.setLocation(child.getX() + deltaX, child.getY() + deltaY);
			}
		}

		this.x = x;
		this.y = y;
	}

	/**
	 * Adds a component as a child to this container.
	 * 
	 * @param child	The component to be added.
	 */
	public abstract void addChild(AbstractComponent child);
	/**
	 * Removes the child component from this container.
	 * 
	 * @param child	The component to be removed.
	 */
	public abstract void removeChild(AbstractComponent child);

}
