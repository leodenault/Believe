package musicGame.core;

import java.util.LinkedList;
import java.util.List;

import musicGame.gui.ComponentBase;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.InputAdapter;

public class Camera extends InputAdapter {
	public static final float SCROLL_SPEED = 0.1f; // Pixels per millisecond

	private float x;
	private float y;
	MovementDirection direction;
	private List<ComponentBase> children;
	
	public Camera() {
		x = 0;
		y = 0;
		direction = MovementDirection.NONE;
		children = new LinkedList<ComponentBase>();
	}
	
	public void addChild(ComponentBase child) {
		children.add(child);
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.pushTransform();
		g.translate(-x, -y);
		for (ComponentBase child : children) {
			child.render(context, g);
		}
		g.popTransform();
	}
	
	public void update(int delta) {
		if (direction != MovementDirection.NONE) {
			float distance = delta * SCROLL_SPEED;

			if (direction == MovementDirection.LEFT) {
				distance = -distance;
			}
			
			x += distance;
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		direction = MovementDirection.directionForKey(key);
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (key == direction.getKey()) {
			direction = MovementDirection.NONE;
		}
	}
}
