package believe.map.collidable.command;

import believe.character.playable.PlayableCharacter;
import believe.map.collidable.command.InternalQualifiers.SequenceParameter;
import believe.command.proto.CommandSequenceProto.CommandSequence;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Reusable
final class CommandSequenceSupplier implements CommandSupplier<PlayableCharacter, Void> {
  private final Map<String, CommandSupplier<PlayableCharacter, ?>> commandSupplierMap;
  private final String sequenceParameter;

  @Inject
  CommandSequenceSupplier(
      Map<String, CommandSupplier<PlayableCharacter, ?>> commandSupplierMap,
      @SequenceParameter String sequenceParameter) {
    this.commandSupplierMap = commandSupplierMap;
    this.sequenceParameter = sequenceParameter;
  }

  @Override
  public Command<PlayableCharacter, Void> supplyCommand(TiledObject tiledObject) {
    Optional<String> sequenceValue = tiledObject.getProperty(sequenceParameter);

    if (!sequenceValue.isPresent()) {
      Log.error("Command sequence value missing.");
      return Command.create((first, second) -> {}, tiledObject);
    }

    CommandSequence.Builder commandSequence = CommandSequence.newBuilder();
    try {
      TextFormat.getParser().merge(sequenceValue.get(), commandSequence);
    } catch (ParseException e) {
      Log.error("Command sequence format is invalid.", e);
    }

    List<? extends Command<PlayableCharacter, ?>> subcommands =
        commandSequence.build().getCommandsList().stream()
            .filter(
                command -> {
                  boolean commandExists = commandSupplierMap.containsKey(command.getName());
                  if (!commandExists) {
                    Log.error(
                        "The command named '" + command.getName() + "' is not a valid command.");
                  }
                  return commandExists;
                })
            .map(
                command ->
                    commandSupplierMap
                        .get(command.getName())
                        .supplyCommand(
                            TiledObject.create(
                                key -> Optional.ofNullable(command.getParametersMap().get(key)),
                                EntityType.COMMAND)))
            .collect(Collectors.toList());

    return Command.create(
        (command, playableCharacter) -> {
          for (Command<PlayableCharacter, ?> subcommand : subcommands) {
            triggerSubcommand(subcommand, playableCharacter);
          }
        },
        tiledObject);
  }

  private <D> void triggerSubcommand(
      Command<PlayableCharacter, D> subcommand, PlayableCharacter playableCharacter) {
    subcommand.getCommandCollisionHandler().handleCollision(subcommand, playableCharacter);
  }
}
