package believe.map.collidable.command;

import believe.command.Command;
import believe.command.CommandGenerator;
import believe.geometry.Rectangle;
import believe.map.collidable.command.InternalQualifiers.CommandParameter;
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

  @Inject
  CollidableCommandGenerator(
      CommandGenerator commandGenerator,
      CollidableCommandFactory collidableCommandFactory,
      @CommandParameter String commandParameter) {
    this.commandGenerator = commandGenerator;
    this.collidableCommandFactory = collidableCommandFactory;
    this.commandParameter = commandParameter;
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

    Optional<Command> optionalCommand =
        commandGenerator.generateCommand(commandName.get(), tiledObject.propertyProvider());

    optionalCommand.ifPresent(
        command ->
            generatedMapEntityData.addPhysicsManageable(
                physicsManager ->
                    physicsManager.addStaticCollidable(
                        collidableCommandFactory.create(
                            command,
                            new Rectangle(
                                tiledObject.x(),
                                tiledObject.y(),
                                tiledObject.width(),
                                tiledObject.height())))));
  }
}
