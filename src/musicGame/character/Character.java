package musicGame.character;

import musicGame.core.Camera;
import musicGame.core.MovementDirection;
import musicGame.gui.ComponentBase;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;

public class Character extends ComponentBase {

	private MovementDirection direction;
	private Animation anim;
	
	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		direction = MovementDirection.NONE;
		anim = new Animation(new SpriteSheet("/res/graphics/sprites/stickFigure.png", 64, 128), 100);
		rect = new musicGame.geometry.Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
		anim.setLooping(true);
		anim.stop();
		anim.setCurrentFrame(0);
	}
	
	public void update(int delta) {
		if (direction != MovementDirection.NONE) {
			float distance = delta * Camera.SCROLL_SPEED;

			if (direction == MovementDirection.LEFT) {
				distance = -distance;
			}
			
			setLocation(getFloatX() + distance, getFloatY());
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
			MovementDirection newDirection = MovementDirection.directionForKey(key);
			
			if (direction == MovementDirection.NONE || newDirection != MovementDirection.NONE) {
				direction = newDirection;
				anim.start();
			}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		if (key == direction.getKey()) {
			direction = MovementDirection.NONE;
			anim.stop();
			anim.setCurrentFrame(0);
		}
	}

	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		anim.draw(rect.getX(), rect.getY());
	}
}
