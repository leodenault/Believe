package musicGame.character;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.Camera;
import musicGame.core.MovementDirection;
import musicGame.core.Music;
import musicGame.core.SynchedComboPattern;
import musicGame.physics.DamageHandler.Faction;
import musicGame.physics.PhysicsManager;

public class PlayableCharacter extends Character {
	public interface SynchedComboListener {
		void activateCombo(SynchedComboPattern pattern);
	}
	
	// Number of milliseconds to wait for gravity to be constantly
	// applied before denying a jump (We don't want a character to
	// jump after having fallen off a platform)
	private static final int FALLING_DELAY = 20;
	private static final float JUMP_SPEED = -0.5f;
	private static final float DENY_JUMP_SPEED = FALLING_DELAY * PhysicsManager.GRAVITY; // Speed at which we start denying jumps
	private static final float MAX_FOCUS = 1.0f;
	private static final float FOCUS_RECHARGE_TIME = 60f; // Time in seconds for recharging focus fully
	private static final float FOCUS_RECHARGE_RATE = MAX_FOCUS / (FOCUS_RECHARGE_TIME * 1000f);
	
	private boolean canJump;
	private int direction;
	private float verticalSpeed;
	private float horizontalSpeed;
	private float focus;
	private SynchedComboPattern pattern;
	private List<SynchedComboListener> comboListeners;
	
	public PlayableCharacter(GUIContext container, Music music, int x, int y) throws SlickException {
		super(container, x, y);
		canJump = true;
		direction = 1;
		verticalSpeed = 0;
		horizontalSpeed = 0;
		focus = MAX_FOCUS;
		pattern = new SynchedComboPattern();
		pattern.addAction(0, 's');
		pattern.addAction(1, 's');
		pattern.addAction(2, 'a');
		pattern.addAction(2.5f, 'd');
		pattern.addAction(3, 'a');
		comboListeners = new LinkedList<SynchedComboListener>();
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
	
	public float getFocus() {
		return focus;
	}
	
	public void addComboListener(SynchedComboListener listener) {
		this.comboListeners.add(listener);
	}
	
	public void update(int delta) {
		if (verticalSpeed > DENY_JUMP_SPEED) {
			canJump = false;
		}
		
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
		
		focus = Math.min(MAX_FOCUS, focus + (delta * FOCUS_RECHARGE_RATE));
		anim.update(delta);
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		horizontalSpeed += MovementDirection.directionForKey(key).getValue() * Camera.SCROLL_SPEED;
		
		switch (key) {
			case Input.KEY_SPACE:
				if (canJump) {
					canJump = false;
					verticalSpeed = JUMP_SPEED;
					anim.stop();
					anim = animSet.get("jump");
					anim.start();
				}
			break;
			case Input.KEY_C:
				for (SynchedComboListener listener : comboListeners) {
					listener.activateCombo(pattern);
				}
				break;
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
	protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
		anim.getCurrentFrame().getFlippedCopy(direction == -1, false).draw(rect.getX(), rect.getY());
	}

	@Override
	public Faction getFaction() {
		return Faction.GOOD;
	}

	@Override
	public void inflictDamage(float damage) {
		focus -= damage;
	}
}
