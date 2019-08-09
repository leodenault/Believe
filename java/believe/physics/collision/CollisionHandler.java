package believe.physics.collision;

/**
 * Handles collisions between two {@link Collidable} instances.
 *
 * @param <A> the type of the first entity in the collision.
 * @param <B> the type of the second entity in the collision.
 */
public interface CollisionHandler<A extends Collidable, B extends Collidable> {
  /** Handles a collision between {@code first} and {@code second}. */
  void handleCollision(A first, B second);
}
