package believe.command;

import believe.core.PropertyProvider;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Map;
import java.util.Optional;

/** Generates a {@link Command} instances when prompted. */
@Reusable
public final class CommandGenerator {
  private final Map<String, CommandSupplier> commandSupplierMap;

  @Inject
  CommandGenerator(Map<String, CommandSupplier> commandSupplierMap) {
    this.commandSupplierMap = commandSupplierMap;
  }

  /**
   * Generates a command based on {@code commandName} and the properties available in {@code
   * propertyProvider}.
   */
  public Optional<Command> generateCommand(String commandName, PropertyProvider propertyProvider) {
    CommandSupplier commandSupplier = commandSupplierMap.get(commandName);
    if (commandSupplier == null) {
      Log.error("The command named '" + commandName + "' is not recognized as a command.");
      return Optional.empty();
    }
    return Optional.of(commandSupplier.supplyCommand(propertyProvider));
  }
}
