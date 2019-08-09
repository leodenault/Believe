package believe.map.collidable.command;

import static believe.util.Util.hashSetOf;

import believe.geometry.Rectangle;
import believe.map.tiled.Tile;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;

import java.util.Collections;
import java.util.Set;

/** A command block drawn on a tile map which, when executed, affects gameplay. */
public class Command<C extends CommandCollidable<C>> implements Collidable<Command<C>>,
    PhysicsManageable {
  private final CommandCollisionHandler<C> commandCollisionHandler;
  private final Rectangle rect;

  Command(CommandCollisionHandler<C> commandCollisionHandler, Tile tile) {
    this.commandCollisionHandler = commandCollisionHandler;
    rect = new Rectangle(tile.pixelX(), tile.pixelY(), tile.width(), tile.height());
  }

  @Override
  public Set<CollisionHandler<? super Command<C>, ? extends Collidable<?>>>
      leftCompatibleHandlers() {
    return hashSetOf(commandCollisionHandler);
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super Command<C>>>
      rightCompatibleHandlers() {
    return Collections.emptySet();
  }

  @Override
  public Rectangle rect() {
    return rect;
  }

  @Override
  public void addToPhysicsManager(PhysicsManager physicsManager) {
    physicsManager.addStaticCollidable(this);
  }
}
