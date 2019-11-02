package believe.map.collidable.command;

import believe.command.Command;
import believe.command.CommandGenerator;
import believe.geometry.Rectangle;
import believe.map.collidable.command.InternalQualifiers.CommandParameter;
import believe.map.collidable.command.InternalQualifiers.ShouldDespawnParameter;
import believe.map.data.GeneratedMapEntityData;
import believe.map.io.ObjectParser;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObject;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Optional;

/** Generates a {@link Command} from a tile within a {@link TiledMap}. */
@Reusable
final class CollidableCommandGenerator implements ObjectParser {
  private final CommandGenerator commandGenerator;
  private final CollidableCommandFactory collidableCommandFactory;
  private final String commandParameter;
  private final String shouldDespawnParameter;

  @Inject
  CollidableCommandGenerator(
      CommandGenerator commandGenerator,
      CollidableCommandFactory collidableCommandFactory,
      @CommandParameter String commandParameter,
      @ShouldDespawnParameter String shouldDespawnParameter) {
    this.commandGenerator = commandGenerator;
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

    Optional<String> commandName = tiledObject.propertyProvider().getProperty(commandParameter);
    if (!commandName.isPresent()) {
      Log.error(
          "Attempted to generate a command without specifying a '"
              + commandParameter
              + "' parameter.");
      return;
    }

    boolean shouldDespawn;
    Optional<String> optionalShouldDespawn =
        tiledObject.propertyProvider().getProperty(shouldDespawnParameter);
    if (optionalShouldDespawn.isPresent()) {
      shouldDespawn = Boolean.valueOf(optionalShouldDespawn.get());
    } else {
      Log.warn("Missing a '" + shouldDespawnParameter + "' parameter.");
      shouldDespawn = false;
    }

    Optional<Command> optionalCommand =
        commandGenerator.generateCommand(commandName.get(), tiledObject.propertyProvider());

    optionalCommand.ifPresent(
        command ->
            generatedMapEntityData.addPhysicsManageable(
                physicsManager ->
                    physicsManager.addStaticCollidable(
                        collidableCommandFactory.create(
                            shouldDespawn,
                            command,
                            new Rectangle(
                                tiledObject.x(),
                                tiledObject.y(),
                                tiledObject.width(),
                                tiledObject.height())))));
  }
}
