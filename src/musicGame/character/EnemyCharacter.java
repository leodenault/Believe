package musicGame.character;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class EnemyCharacter extends Character {

	public EnemyCharacter(GUIContext container, int x, int y)
			throws SlickException {
		super(container, x, y);
	}

	@Override
	public void setCanJump(boolean canJump) {}

	@Override
	public float getVerticalSpeed() {
		return 0;
	}

	@Override
	public void setVerticalSpeed(float speed) {}

	@Override
	public void update(int delta) {}
}
