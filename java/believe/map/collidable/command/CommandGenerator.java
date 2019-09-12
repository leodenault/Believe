package believe.map.collidable.command;

import believe.map.collidable.command.InternalQualifiers.CommandParameter;
import believe.map.data.GeneratedMapEntityData;
import believe.map.io.ObjectParser;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObject;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Map;
import java.util.Optional;

/** Generates a {@link Command} from a tile within a {@link TiledMap}. */
@Reusable
final class CommandGenerator implements ObjectParser {
  private final Map<String, CommandSupplier<?, ?>> commandSupplierMap;
  private final String commandParameter;

  @Inject
  CommandGenerator(
      Map<String, CommandSupplier<?, ?>> commandSupplierMap,
      @CommandParameter String commandParameter) {
    this.commandSupplierMap = commandSupplierMap;
    this.commandParameter = commandParameter;
  }

  @Override
  public void parseObject(
      TiledObject tiledObject, GeneratedMapEntityData.Builder generatedMapEntityData) {
    if (tiledObject.entityType() != EntityType.COMMAND) {
      return;
    }

    Optional<String> commandName = tiledObject.getProperty(commandParameter);
    if (!commandName.isPresent()) {
      Log.error(
          "Attempted to generate a command without specifying a '"
              + commandParameter
              + "' parameter.");
      return;
    }

    CommandSupplier<?, ?> commandSupplier =
        commandSupplierMap.get(commandName.get());
    if (commandSupplier == null) {
      Log.error("The command named '" + commandName + "' is not recognized as a command.");
      return;
    }

    generatedMapEntityData.addPhysicsManageable(commandSupplier.supplyCommand(tiledObject));
  }
}
