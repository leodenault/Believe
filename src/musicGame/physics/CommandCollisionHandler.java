package musicGame.physics;

import musicGame.map.Command;
import musicGame.physics.Collidable.CollidableType;

public class CommandCollisionHandler implements CollisionHandler<CommandCollidable> {

	@Override
	public void handleCollision(CommandCollidable caller, Collidable other) {
		if (other.getType() == CollidableType.COMMAND) {
			Command command = (Command) other;
			caller.executeCommand(command.getCommandType());
		}
	}
}
