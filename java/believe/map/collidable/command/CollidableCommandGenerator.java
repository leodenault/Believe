package believe.map.collidable.command;

import static believe.geometry.RectangleKt.rectangle;

import believe.command.Command;
import believe.map.collidable.command.InternalQualifiers.CommandParameter;
import believe.map.collidable.command.InternalQualifiers.ShouldDespawnParameter;
import believe.map.data.GeneratedMapEntityData;
import believe.map.io.ObjectParser;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObject;
import believe.map.tiled.command.TiledCommandParser;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Optional;

/** Generates a {@link Command} from a tile within a {@link TiledMap}. */
@Reusable
final class CollidableCommandGenerator implements ObjectParser {
  private final TiledCommandParser tiledCommandParser;
  private final CollidableCommandFactory collidableCommandFactory;
  private final String commandParameter;
  private final String shouldDespawnParameter;

  @Inject
  CollidableCommandGenerator(
      TiledCommandParser tiledCommandParser,
      CollidableCommandFactory collidableCommandFactory,
      @CommandParameter String commandParameter,
      @ShouldDespawnParameter String shouldDespawnParameter) {
    this.tiledCommandParser = tiledCommandParser;
    this.collidableCommandFactory = collidableCommandFactory;
    this.commandParameter = commandParameter;
    this.shouldDespawnParameter = shouldDespawnParameter;
  }

  @Override
  public void parseObject(
      TiledObject tiledObject, GeneratedMapEntityData.Builder generatedMapEntityData) {
    if (tiledObject.entityType() != EntityType.COMMAND) {
      return;
    }

    Command command = tiledCommandParser.parseTiledCommand(tiledObject.propertyProvider());
    if (command == null) {
      return;
    }

    boolean shouldDespawn;
    Optional<String> optionalShouldDespawn =
        tiledObject.propertyProvider().getProperty(shouldDespawnParameter);
    if (optionalShouldDespawn.isPresent()) {
      shouldDespawn = Boolean.parseBoolean(optionalShouldDespawn.get());
    } else {
      Log.warn("Missing a '" + shouldDespawnParameter + "' parameter.");
      shouldDespawn = false;
    }

    generatedMapEntityData.addPhysicsManageable(
        physicsManager ->
            physicsManager.addStaticCollidable(
                collidableCommandFactory.create(
                    shouldDespawn,
                    command,
                    rectangle(
                        tiledObject.x(),
                        tiledObject.y(),
                        tiledObject.width(),
                        tiledObject.height()))));
  }
}
