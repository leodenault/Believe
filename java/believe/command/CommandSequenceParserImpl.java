package believe.command;

import believe.command.proto.CommandSequenceProto.CommandSequence;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import dagger.Lazy;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Reusable
public final class CommandSequenceParserImpl implements CommandSequenceParser {
  private final Lazy<CommandGenerator> commandGenerator;

  @Inject
  CommandSequenceParserImpl(Lazy<CommandGenerator> commandGenerator) {
    this.commandGenerator = commandGenerator;
  }

  @Override
  public Optional<Command> parseSequence(String sequence) {
    CommandSequence.Builder commandSequence = CommandSequence.newBuilder();
    try {
      TextFormat.getParser().merge(sequence, commandSequence);
    } catch (ParseException e) {
      Log.error("Command sequence format is invalid.", e);
      return Optional.empty();
    }

    return Optional.of(parseSequence(commandSequence.build()));
  }

  @Override
  public Command parseSequence(CommandSequence commandSequence) {
    List<Command> subCommands =
        commandSequence.getCommandsList().stream()
            .map(
                command ->
                    commandGenerator
                        .get()
                        .generateCommand(
                            command.getName(),
                            key -> Optional.ofNullable(command.getParametersMap().get(key))))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    return () -> {
      for (Command subCommand : subCommands) {
        subCommand.execute();
      }
    };
  }
}
