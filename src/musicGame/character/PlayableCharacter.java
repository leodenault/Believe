package musicGame.character;

import musicGame.core.Camera;
import musicGame.core.DynamicCollidable;
import musicGame.core.MovementDirection;
import musicGame.gui.ComponentBase;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.GUIContext;

public class Character extends ComponentBase implements DynamicCollidable {
	private static final float JUMP_SPEED = -0.5f;
	
	private boolean canJump;
	private float verticalSpeed;
	private float horizontalSpeed;
	private Animation anim;
	
	public Character(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		canJump = true;
		verticalSpeed = 0;
		anim = new Animation(new SpriteSheet("/res/graphics/sprites/stickFigure.png", 64, 128), 100);
		rect = new musicGame.geometry.Rectangle(x, y - anim.getHeight(), anim.getWidth(), anim.getHeight());
		anim.setLooping(true);
		anim.stop();
		anim.setCurrentFrame(0);
	}
	
	public void update(int delta) {
		if (horizontalSpeed == 0 && !anim.isStopped()) {
			anim.stop();
			anim.setCurrentFrame(0);
		} else if (horizontalSpeed != 0 && anim.isStopped()) {
			anim.start();
		}
		
		if (horizontalSpeed != 0) {
			setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
		}
	}
	
	@Override
	public float getFloatX() {
		return rect.getX();
	}
	
	@Override
	public float getFloatY() {
		return rect.getY();
	}
	
	@Override
	public void setLocation(float x, float y) {
		if (rect != null) {
			rect.setLocation(x, y);
			resetLayout();
		}
	}
	
	@Override
	public float getVerticalSpeed() {
		return verticalSpeed;
	}

	@Override
	public void setVerticalSpeed(float speed) {
		verticalSpeed = speed;
	}
	
	@Override
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		horizontalSpeed += MovementDirection.directionForKey(key).getValue() * Camera.SCROLL_SPEED;
		
		if (canJump && key == Input.KEY_Z) {
			canJump = false;
			verticalSpeed = JUMP_SPEED;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		horizontalSpeed -= MovementDirection.directionForKey(key).getValue() * Camera.SCROLL_SPEED;
	}

	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		anim.draw(rect.getX(), rect.getY());
	}
}
