package believe.map.collidable.command;

import static believe.geometry.RectangleKt.rectangle;

import believe.command.Command;
import believe.map.collidable.command.InternalQualifiers.ShouldDespawnParameter;
import believe.map.data.ObjectFactory;
import believe.map.io.ObjectParser;
import believe.map.tiled.TiledMap;
import believe.map.tiled.TiledObject;
import believe.map.tiled.command.TiledCommandParser;
import dagger.Reusable;
import org.newdawn.slick.util.Log;
import javax.annotation.Nullable;
import javax.inject.Inject;

/** Generates a {@link Command} from a tile within a {@link TiledMap}. */
@Reusable
final class CollidableCommandGenerator implements ObjectParser {
  private final TiledCommandParser tiledCommandParser;
  private final CollidableCommandFactory collidableCommandFactory;
  private final String shouldDespawnParameter;

  @Inject
  CollidableCommandGenerator(
      TiledCommandParser tiledCommandParser,
      CollidableCommandFactory collidableCommandFactory,
      @ShouldDespawnParameter String shouldDespawnParameter) {
    this.tiledCommandParser = tiledCommandParser;
    this.collidableCommandFactory = collidableCommandFactory;
    this.shouldDespawnParameter = shouldDespawnParameter;
  }

  @Override
  public ObjectFactory parseObject(TiledObject tiledObject) {
    Command command = tiledCommandParser.parseTiledCommand(tiledObject);
    if (command == null) {
      return ObjectFactory.EMPTY;
    }

    boolean shouldDespawn;
    @Nullable String optionalShouldDespawn = tiledObject.getProperty(shouldDespawnParameter);
    if (optionalShouldDespawn != null) {
      shouldDespawn = Boolean.parseBoolean(optionalShouldDespawn);
    } else {
      Log.warn("Missing a '" + shouldDespawnParameter + "' parameter.");
      shouldDespawn = false;
    }

    return generatedMapEntityData ->
        generatedMapEntityData.addPhysicsManageable(
            physicsManager ->
                physicsManager.addStaticCollidable(
                    collidableCommandFactory.create(
                        shouldDespawn,
                        command,
                        rectangle(
                            tiledObject.getX(),
                            tiledObject.getY(),
                            tiledObject.getWidth(),
                            tiledObject.getHeight()))));
  }
}
