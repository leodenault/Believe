package believe.character.playable;

import believe.map.collidable.command.Command;
import believe.map.collidable.command.CommandCollisionHandler;
import believe.statemachine.State.Action;

/** A {@link CommandCollisionHandler} that affects the movement of an entity. */
final class PlayableCharacterMovementCommandCollisionHandler
    implements CommandCollisionHandler<PlayableCharacter, Void> {
  private final Action movementAction;

  PlayableCharacterMovementCommandCollisionHandler(Action movementAction) {
    this.movementAction = movementAction;
  }

  @Override
  public void handleCollision(
      Command<PlayableCharacter, Void> command, PlayableCharacter playableCharacter) {
    playableCharacter.transition(movementAction);
  }
}
