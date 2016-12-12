package musicGame.physics;

import musicGame.character.PlayableCharacter;

public interface CommandCollidable extends Collidable {
	void executeCommand(PlayableCharacter.Action command);
}
