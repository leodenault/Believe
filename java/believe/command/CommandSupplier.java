package believe.command;

import believe.core.PropertyProvider;

/** Supplies a {@link Command} based on the contents of a {@link PropertyProvider}. */
public interface CommandSupplier {
  /**
   * Return a {@link Command} based on the contents of {@code propertyProvider}.
   *
   * @param propertyProvider the {@link PropertyProvider} from which the command should be
   *     generated.
   */
  Command supplyCommand(PropertyProvider propertyProvider);
}
