package believe.map.collidable.command;

import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.manager.PhysicsManager;
import dagger.Reusable;
import javax.inject.Inject;

/** Handles collisions between commands and arbitrary {@link Collidable} instances. */
@Reusable
public final class CollidableCommandCollisionHandler
    implements CollisionHandler<CollidableCommand, Collidable<?>> {
  private final PhysicsManager physicsManager;

  @Inject
  CollidableCommandCollisionHandler(PhysicsManager physicsManager) {
    this.physicsManager = physicsManager;
  }

  @Override
  public void handleCollision(CollidableCommand first, Collidable<?> second) {
    first.command().execute();

    if (first.shouldDespawn()) {
      physicsManager.removeCollidable(first);
    }
  }
}
