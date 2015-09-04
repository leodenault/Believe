package musicGame.core;

import java.util.LinkedList;
import java.util.List;

import musicGame.geometry.Rectangle;
import musicGame.gui.ComponentBase;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class Camera {
	public static final float SCROLL_SPEED = 0.2f; // Pixels per millisecond

	private float scaleX;
	private float scaleY;
	private Rectangle rect;
	private List<ComponentBase> children;
	
	public Camera(float width, float height) {
		scaleX = 1;
		scaleY = 1;
		rect = new Rectangle(0, 0, width, height);
		children = new LinkedList<ComponentBase>();
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public void center(float x, float y) {
		rect.setCenterX(x);
		rect.setCenterY(y);
	}

	public void scale(float x, float y) {
		scaleX = x;
		scaleY = y;
		rect.setSize(rect.getWidth() * (1/x), rect.getHeight() * (1/y));
	}
	
	public void addChild(ComponentBase child) {
		children.add(child);
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.pushTransform();

		g.scale(scaleX, scaleY);
		g.translate(-rect.getX(), -rect.getY());
		for (ComponentBase child : children) {
			child.render(context, g);
		}
		
		g.popTransform();

	}
}
