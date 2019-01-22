package believe.physics.collision;

import believe.statemachine.State.Action;

public interface CommandCollidable extends Collidable {
  void executeCommand(Action command);
}
