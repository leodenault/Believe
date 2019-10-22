package believe.map.collidable.command;

import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import dagger.Reusable;
import javax.inject.Inject;

/** Handles collisions between commands and arbitrary {@link Collidable} instances. */
@Reusable
public final class CollidableCommandCollisionHandler
    implements CollisionHandler<CollidableCommand, Collidable<?>> {
  @Inject
  CollidableCommandCollisionHandler() {}

  @Override
  public void handleCollision(CollidableCommand first, Collidable<?> second) {
    first.command().execute();
  }
}
