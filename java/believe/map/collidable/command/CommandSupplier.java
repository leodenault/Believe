package believe.map.collidable.command;

import believe.map.tiled.TiledObject;

/**
 * Supplies a {@link Command} based on the contents of a {@link TiledObject}.
 *
 * @param <C> the type of {@link CommandCollidable} compatible with this command during collisions.
 * @param <D> the type of extra data associated with this command, if any.
 */
public interface CommandSupplier<C extends CommandCollidable<C>, D> {
  /**
   * Return a {@link Command} based on the contents of {@code tiledObject}.
   *
   * <p>It may be assumed that {@link TiledObject#entityType()} will return {@link
   * believe.map.tiled.EntityType#COMMAND}.
   *
   * @param tiledObject the {@link TiledObject} from which the command should be generated.
   */
  Command<C, D> supplyCommand(TiledObject tiledObject);

  /**
   * Returns a {@link Command} based on {@code commandCollisionHandler}.
   *
   * <p>Assumes that the generated command will hold no data.
   *
   * @param commandCollisionHandler the {@link CommandCollisionHandler} that will be used to handle
   *     collisions between the generated command and other entities.
   * @param <C> the type of {@link CommandCollidable} compatible with this command during
   *     collisions.
   */
  static <C extends CommandCollidable<C>> CommandSupplier<C, Void> from(
      CommandCollisionHandler<C, Void> commandCollisionHandler) {
    return tiledObject -> Command.create(commandCollisionHandler, tiledObject);
  }
}
