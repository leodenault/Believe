package believe.character.playable;

import believe.command.Command;
import believe.command.CommandSupplier;
import believe.core.PropertyProvider;
import believe.statemachine.State.Action;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.newdawn.slick.util.Log;

import java.util.Optional;
import java.util.function.Supplier;

/** A {@link Command} that affects the movement of a {@link PlayableCharacter}. */
@AutoFactory
final class PlayableCharacterMovementCommand implements Command, CommandSupplier {
  private final Supplier<Optional<PlayableCharacter>> playableCharacter;
  private final Action movementAction;

  PlayableCharacterMovementCommand(
      @Provided Supplier<Optional<PlayableCharacter>> playableCharacter, Action movementAction) {
    this.playableCharacter = playableCharacter;
    this.movementAction = movementAction;
  }

  @Override
  public void execute() {
    if (!playableCharacter.get().isPresent()) {
      Log.warn("Playable character is empty. Ignoring command.");
      return;
    }

    playableCharacter.get().get().transition(movementAction);
  }

  @Override
  public Optional<Command> supplyCommand(PropertyProvider propertyProvider) {
    return Optional.of(this);
  }
}
