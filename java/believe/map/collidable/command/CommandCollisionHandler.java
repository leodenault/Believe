package believe.map.collidable.command;

import believe.physics.collision.CollisionHandler;

/**
 * Specialization of a {@link CollisionHandler} for handling collisions between commands and other
 * entities.
 *
 * @param <C> the type of the collidable object that can be handled by this command collision
 *     handler.
 */
public interface CommandCollisionHandler<C extends CommandCollidable<C>>
    extends CollisionHandler<Command<C>, C> {
  /** An empty placeholder for when no collision handler applies. */
  static <C extends CommandCollidable<C>> CommandCollisionHandler<C> empty() {
    return (first, second) -> {};
  }
}
