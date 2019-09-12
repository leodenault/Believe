package believe.map.collidable.command;

import believe.physics.collision.CollisionHandler;

/**
 * Specialization of a {@link CollisionHandler} for handling collisions between commands and other
 * entities.
 *
 * @param <C> the type of the collidable object that can be handled by this command collision
 *     handler.
 * @param <D> the type of extra data associated with this command, if any.
 */
public interface CommandCollisionHandler<C extends CommandCollidable<C>, D>
    extends CollisionHandler<Command<C, D>, C> {
  /** An empty placeholder for when no collision handler applies. */
  static <C extends CommandCollidable<C>, D> CommandCollisionHandler<C, D> empty() {
    return (first, second) -> {};
  }
}
