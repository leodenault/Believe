package musicGame.physics;

import musicGame.statemachine.State.Action;

public interface CommandCollidable extends Collidable {
	void executeCommand(Action command);
}
