package believe.command;

import believe.command.InternalQualifiers.SequenceParameter;
import believe.command.proto.CommandSequenceProto.CommandSequence;
import believe.core.PropertyProvider;
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
final class CommandSequenceSupplier implements CommandSupplier {
  private final Lazy<CommandGenerator> commandGenerator;
  private final String sequenceParameter;

  @Inject
  CommandSequenceSupplier(
      Lazy<CommandGenerator> commandGenerator, @SequenceParameter String sequenceParameter) {
    this.commandGenerator = commandGenerator;
    this.sequenceParameter = sequenceParameter;
  }

  @Override
  public Optional<Command> supplyCommand(PropertyProvider propertyProvider) {
    Optional<String> sequenceValue = propertyProvider.getProperty(sequenceParameter);

    if (!sequenceValue.isPresent()) {
      Log.error("Command sequence value missing.");
      return Optional.empty();
    }

    CommandSequence.Builder commandSequence = CommandSequence.newBuilder();
    try {
      TextFormat.getParser().merge(sequenceValue.get(), commandSequence);
    } catch (ParseException e) {
      Log.error("Command sequence format is invalid.", e);
      return Optional.empty();
    }

    List<Command> subcommands =
        commandSequence.build().getCommandsList().stream()
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

    return Optional.of(() -> {
      for (Command subcommand : subcommands) {
        subcommand.execute();
      }
    });
  }
}
