package believe.map.collidable.command;

import believe.map.collidable.command.InternalQualifiers.CommandParameter;
import believe.map.data.GeneratedMapEntityData;
import believe.map.io.TileParser;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.TiledMap;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Map;
import java.util.Optional;

/** Generates a {@link Command} from a tile within a {@link TiledMap}. */
@Reusable
final class CommandGenerator implements TileParser {
  private final Map<String, CommandCollisionHandler<?>> commandCollisionHandlerMap;
  private final String commandParameter;

  @Inject
  CommandGenerator(
      Map<String, CommandCollisionHandler<?>> commandCollisionHandlerMap,
      @CommandParameter String commandParameter) {
    this.commandCollisionHandlerMap = commandCollisionHandlerMap;
    this.commandParameter = commandParameter;
  }

  @Override
  public void parseTile(
      TiledMap map, Tile tile, GeneratedMapEntityData.Builder generatedMapEntityData) {
    if (tile.entityType() != EntityType.COMMAND) {
      return;
    }

    Optional<String> commandName = tile.getProperty(commandParameter);
    if (!commandName.isPresent()) {
      Log.error(
          "Attempted to generate a command tile without specifying a '"
              + commandParameter
              + "' parameter.");
      return;
    }

    CommandCollisionHandler<?> commandCollisionHandler =
        commandCollisionHandlerMap.get(commandName.get());
    if (commandCollisionHandler == null) {
      Log.error("The command named '" + commandName + "' is not recognized as a command.");
      return;
    }

    Command<?> command = new Command<>(commandCollisionHandler, tile);
    generatedMapEntityData.addPhysicsManageable(command);
  }
}
