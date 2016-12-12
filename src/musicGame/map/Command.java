package musicGame.map;

import musicGame.character.PlayableCharacter.Action;
import musicGame.geometry.Rectangle;
import musicGame.physics.Collidable;

public class Command implements Collidable {
	private Action type;
	private Rectangle rect;
	
	public Command(int x, int y, Action type) {
		rect = new Rectangle(x, y, 0, 0);
		this.type = type;
	}
	
	public Action getCommandType() {
		return type;
	}

	@Override
	public void collision(Collidable other) {}

	@Override
	public CollidableType getType() {
		return CollidableType.COMMAND;
	}

	@Override
	public Rectangle getRect() {
		return rect;
	}
}
