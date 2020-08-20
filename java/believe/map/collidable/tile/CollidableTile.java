package believe.map.collidable.tile;

import static believe.geometry.RectangleKt.rectangle;

import believe.geometry.Rectangle;
import believe.map.data.TileData;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.manager.PhysicsManageable;
import believe.physics.manager.PhysicsManager;
import believe.util.Util;

import java.util.Collections;
import java.util.Set;

/** An invisible tile that can take part in collisions. */
public final class CollidableTile implements Collidable<CollidableTile>, PhysicsManageable {
  private final Rectangle rectangle;
  private final Set<CollisionHandler<? super CollidableTile, ? extends Collidable<?>>>
      leftCompatibleHandlers;

  CollidableTile(TileData tileData, CollidableTileCollisionHandler collisionHandler) {
    this.rectangle =
        rectangle(
            tileData.getPixelX(), tileData.getPixelY(), tileData.getWidth(), tileData.getHeight());
    this.leftCompatibleHandlers = Collections.unmodifiableSet(Util.hashSetOf(collisionHandler));
  }

  @Override
  public Set<CollisionHandler<? super CollidableTile, ? extends Collidable<?>>>
      leftCompatibleHandlers() {
    return leftCompatibleHandlers;
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super CollidableTile>>
      rightCompatibleHandlers() {
    return Collections.emptySet();
  }

  @Override
  public Rectangle rect() {
    return rectangle;
  }

  @Override
  public void addToPhysicsManager(PhysicsManager physicsManager) {
    physicsManager.addStaticCollidable(this);
  }
}
