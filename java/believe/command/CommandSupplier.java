package believe.command;

import believe.core.PropertyProvider;

import java.util.Optional;

/** Supplies a {@link Command} based on the contents of a {@link PropertyProvider}. */
public interface CommandSupplier {
  /**
   * Return an optional {@link Command} based on the contents of {@code propertyProvider}.
   *
   * @param propertyProvider the {@link PropertyProvider} from which the command should be
   *     generated.
   */
  Optional<Command> supplyCommand(PropertyProvider propertyProvider);
}
