package musicGame.character;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.Camera;
import musicGame.core.DynamicCollidable;
import musicGame.core.MovementDirection;
import musicGame.core.Music;
import musicGame.core.SynchedComboPattern;
import musicGame.gui.ComboSyncher;

public class PlayableCharacter extends Character implements DynamicCollidable {
	private static final float JUMP_SPEED = -0.5f;
	
	private boolean canJump;
	private int direction;
	private float verticalSpeed;
	private float horizontalSpeed;
	private float focus;
	private Music music;
	private ComboSyncher comboSyncher;
	
	public PlayableCharacter(GUIContext container, Music music, int x, int y) throws SlickException {
		super(container, x, y);
		canJump = true;
		direction = 1;
		verticalSpeed = 0;
		horizontalSpeed = 0;
		focus = 1.0f;
		anim.setAutoUpdate(false);
		SynchedComboPattern pattern = new SynchedComboPattern();
		pattern.addAction(0, 's');
		pattern.addAction(1, 's');
		pattern.addAction(2, 's');
		pattern.addAction(3, 's');
		pattern.addAction(4, 'a');
		pattern.addAction(4.5f, 'd');
		pattern.addAction(5, 'a');
		pattern.addAction(6, 'a');
		pattern.addAction(6.5f, 'd');
		pattern.addAction(7, 'a');
		this.music = music;
		comboSyncher = new ComboSyncher(container, pattern, music.getBpm(), 120, 1312);
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
	
	public float getFocus() {
		return focus;
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
		comboSyncher.setLocation(getX() + getWidth(), getY() - getHeight());
		comboSyncher.update();
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
					anim.setLooping(false);
					anim = animSet.get("jump");
					anim.start();
				}
			break;
			case Input.KEY_C:
				comboSyncher.start(music);
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
		comboSyncher.render(context, g);
	}
}
