package musicGame.physics.collision;

import musicGame.statemachine.State.Action;

public interface CommandCollidable extends Collidable {
	void executeCommand(Action command);
}
