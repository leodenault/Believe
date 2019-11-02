package believe.map.collidable.command;

import believe.command.Command;
import believe.geometry.Rectangle;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.Collections;
import java.util.Set;

@AutoFactory(allowSubclasses = true)
final class CollidableCommand implements Collidable<CollidableCommand> {
  private final boolean shouldDespawn;
  private final Command command;
  private final Rectangle rectangle;
  private final Set<CollisionHandler<? super CollidableCommand, ? extends Collidable<?>>>
      collisionHandlers;

  CollidableCommand(
      @Provided
          Set<CollisionHandler<? super CollidableCommand, ? extends Collidable<?>>>
              collisionHandlers,
      boolean shouldDespawn,
      Command command,
      Rectangle rectangle) {
    this.shouldDespawn = shouldDespawn;
    this.command = command;
    this.rectangle = rectangle;
    this.collisionHandlers = collisionHandlers;
  }

  @Override
  public Set<CollisionHandler<? super CollidableCommand, ? extends Collidable<?>>>
      leftCompatibleHandlers() {
    return collisionHandlers;
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super CollidableCommand>>
      rightCompatibleHandlers() {
    return Collections.emptySet();
  }

  @Override
  public Rectangle rect() {
    return rectangle;
  }

  Command command() {
    return command;
  }

  boolean shouldDespawn() {
    return shouldDespawn;
  }
}
