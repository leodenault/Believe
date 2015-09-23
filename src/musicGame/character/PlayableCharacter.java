package musicGame.character;

import musicGame.core.Camera;
import musicGame.core.DynamicCollidable;
import musicGame.core.MovementDirection;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class PlayableCharacter extends Character implements DynamicCollidable {
	private static final float JUMP_SPEED = -0.5f;
	
	private boolean canJump;
	private float verticalSpeed;
	float horizontalSpeed;
	public PlayableCharacter(GUIContext container, int x, int y) throws SlickException {
		super(container, x, y);
		canJump = true;
		verticalSpeed = 0;
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
	
	public void update(int delta) {
		if (horizontalSpeed == 0 && anim.equals(animSet.get("move"))) {
			anim.stop();
			anim = animSet.get("idle");
		} else if (horizontalSpeed != 0 && anim.equals(animSet.get("idle"))) {
			anim = animSet.get("move");
			anim.start();
		}
		
		if (horizontalSpeed != 0) {
			setLocation(getFloatX() + delta * horizontalSpeed, getFloatY());
		}
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
	protected String getSheetName() {
		return "stickFigure";
	}
}
