package believe.map.collidable.command;

import static believe.util.Util.hashSetOf;

import believe.geometry.Rectangle;
import believe.map.tiled.TiledObject;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * A command block drawn on a tile map which, when executed, affects gameplay.
 *
 * @param <C> the type of {@link CommandCollidable} compatible with this command during collisions.
 * @param <D> the type of extra data associated with this command, if any.
 */
public class Command<C extends CommandCollidable<C>, D>
    implements Collidable<Command<C, D>>, PhysicsManageable {
  private final CommandCollisionHandler<C, D> commandCollisionHandler;
  private final Rectangle rect;
  @Nullable private final D data;

  private Command(
      CommandCollisionHandler<C, D> commandCollisionHandler,
      TiledObject tiledObject,
      @Nullable D data) {
    this.commandCollisionHandler = commandCollisionHandler;
    rect =
        new Rectangle(tiledObject.x(), tiledObject.y(), tiledObject.width(), tiledObject.height());
    this.data = data;
  }

  /**
   * Creates a {@link Command} that, when triggered, will execute {@code commandCollisionHandler}.
   *
   * @param commandCollisionHandler the handler executed when the command is triggered.
   * @param tiledObject the {@link TiledObject} from which the command is generated.
   * @param <C> the type of {@link CommandCollidable} compatible with this command during
   *     collisions.
   * @param <D> the type of extra data associated with this command, if any.
   */
  public static <C extends CommandCollidable<C>, D> Command<C, D> create(
      CommandCollisionHandler<C, D> commandCollisionHandler, TiledObject tiledObject) {
    return new Command<>(commandCollisionHandler, tiledObject, /* data= */ null);
  }

  /**
   * Creates a {@link Command} that, when triggered, will execute {@code commandCollisionHandler}.
   *
   * @param commandCollisionHandler the handler executed when the command is triggered.
   * @param tiledObject the {@link TiledObject} from which the command is generated.
   * @param data the extra data which should be stored on the command for use at execution time.
   * @param <C> the type of {@link CommandCollidable} compatible with this command during
   *     collisions.
   * @param <D> the type of extra data associated with this command, if any.
   */
  public static <C extends CommandCollidable<C>, D> Command<C, D> create(
      CommandCollisionHandler<C, D> commandCollisionHandler, TiledObject tiledObject, D data) {
    return new Command<>(commandCollisionHandler, tiledObject, data);
  }

  CommandCollisionHandler<C, D> getCommandCollisionHandler() {
    return commandCollisionHandler;
  }

  @Override
  public Set<CollisionHandler<? super Command<C, D>, ? extends Collidable<?>>>
      leftCompatibleHandlers() {
    return hashSetOf(commandCollisionHandler);
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super Command<C, D>>>
      rightCompatibleHandlers() {
    return Collections.emptySet();
  }

  @Override
  public Rectangle rect() {
    return rect;
  }

  public Optional<D> data() {
    return Optional.ofNullable(data);
  }

  @Override
  public void addToPhysicsManager(PhysicsManager physicsManager) {
    physicsManager.addStaticCollidable(this);
  }
}
