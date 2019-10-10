package believe.command;

import believe.core.PropertyProvider;

import java.util.Optional;

/** Generates a {@link Command} instances when prompted. */
public interface CommandGenerator {
  /**
   * Generates a command based on {@code commandName} and the properties available in {@code
   * propertyProvider}.
   */
  Optional<Command> generateCommand(String commandName, PropertyProvider propertyProvider);
}
