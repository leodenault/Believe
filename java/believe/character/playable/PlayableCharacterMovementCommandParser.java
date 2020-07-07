package believe.character.playable;

import believe.character.playable.proto.PlayableCharacterMovementCommandProto;
import believe.character.playable.proto.PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand.Type;
import believe.command.Command;
import believe.command.CommandParser;
import dagger.Reusable;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

@Reusable
final class PlayableCharacterMovementCommandParser
    implements CommandParser<
        PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand> {
  private final PlayableCharacterMovementCommandFactory commandFactory;

  @Inject
  PlayableCharacterMovementCommandParser(PlayableCharacterMovementCommandFactory commandFactory) {
    this.commandFactory = commandFactory;
  }

  @Override
  public int getExtensionNumber() {
    return PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand
        .playableCharacterMovementCommand
        .getNumber();
  }

  @Override
  @Nullable
  public Command parseCommand(
      PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand command) {
    Type action = command.getAction();
    if (action == Type.UNKNOWN_COMMAND) {
      Log.error(
          "Playable character movement command specified unknown action. No command will be output.");
      return null;
    }
    return commandFactory.create(action);
  }
}
