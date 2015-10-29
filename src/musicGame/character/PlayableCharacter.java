package musicGame.character;

import musicGame.core.Camera;
import musicGame.core.DynamicCollidable;
import musicGame.core.MovementDirection;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class PlayableCharacter extends Character implements DynamicCollidable {
	private static final float JUMP_SPEED = -0.5f;
	
	private boolean canJump;
	private int direction;
	private float verticalSpeed;
	private float horizontalSpeed;
	
	public PlayableCharacter(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		canJump = true;
		direction = 1;
		verticalSpeed = 0;
		horizontalSpeed = 0;
		anim.setAutoUpdate(false);
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
		
		if (canJump) {
			anim.setLooping(true);
		}
	}
	
	public void update(int delta) {
		if (canJump) {
			if (horizontalSpeed == 0 && !anim.equals(animSet.get("idle"))) {
				anim.stop();
				anim = animSet.get("idle");
			} else if (horizontalSpeed != 0 && !anim.equals(animSet.get("move"))) {
				anim = animSet.get("move");
				anim.start();
			}
		}
		
		if (horizontalSpeed != 0) {
			setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
			
			if (horizontalSpeed < 0) {
				direction = -1;
			} else if (horizontalSpeed > 0) {
				direction = 1;
			}
		}
		
		anim.update(delta);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		horizontalSpeed += MovementDirection.directionForKey(key).getValue() * Camera.SCROLL_SPEED;
		
		if (canJump && key == Input.KEY_Z) {
			canJump = false;
			verticalSpeed = JUMP_SPEED;
			anim.stop();
			anim.setLooping(false);
			anim = animSet.get("jump");
			anim.start();
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		horizontalSpeed -= MovementDirection.directionForKey(key).getValue() * Camera.SCROLL_SPEED;
	}

	@Override
	protected String getSheetName() {
		return "stickFigure";
	}
	
	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		anim.getCurrentFrame().getFlippedCopy(direction == -1, false).draw(rect.getX(), rect.getY());
	}
}
