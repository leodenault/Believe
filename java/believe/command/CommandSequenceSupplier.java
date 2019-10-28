package believe.command;

import believe.command.InternalQualifiers.SequenceParameter;
import believe.core.PropertyProvider;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Optional;

@Reusable
final class CommandSequenceSupplier implements CommandSupplier {
  private final String sequenceParameter;
  private final CommandSequenceParser commandSequenceParser;

  @Inject
  CommandSequenceSupplier(
      @SequenceParameter String sequenceParameter, CommandSequenceParser commandSequenceParser) {
    this.sequenceParameter = sequenceParameter;
    this.commandSequenceParser = commandSequenceParser;
  }

  @Override
  public Optional<Command> supplyCommand(PropertyProvider propertyProvider) {
    Optional<String> sequenceValue = propertyProvider.getProperty(sequenceParameter);

    if (!sequenceValue.isPresent()) {
      Log.error("Command sequence value missing.");
      return Optional.empty();
    }

    return commandSequenceParser.parseSequence(sequenceValue.get());
  }
}
