package believe.command;

import believe.core.PropertyProvider;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Map;
import java.util.Optional;

/** Default implementation of {@link CommandGenerator}. */
@Reusable
final class CommandGeneratorImpl implements CommandGenerator {
  private final Map<String, CommandSupplier> commandSupplierMap;

  @Inject
  CommandGeneratorImpl(Map<String, CommandSupplier> commandSupplierMap) {
    this.commandSupplierMap = commandSupplierMap;
  }

  @Override
  public Optional<Command> generateCommand(String commandName, PropertyProvider propertyProvider) {
    CommandSupplier commandSupplier = commandSupplierMap.get(commandName);
    if (commandSupplier == null) {
      Log.error("The command named '" + commandName + "' is not recognized as a command.");
      return Optional.empty();
    }
    return commandSupplier.supplyCommand(propertyProvider);
  }
}
