package believe.physics.collision;

import believe.geometry.Rectangle;

import java.util.Set;

/**
 * An entity which can be a participant in a collision.
 *
 * @param <CollidableT> the type of the implementor of this interface.
 */
public interface Collidable<CollidableT extends Collidable<CollidableT>> {
  /**
   * Returns the set of {@link CollisionHandler} instances that are compatible with this {@link
   * Collidable} where this collidable's type is compatible with the first type parameter of the
   * {@link CollisionHandler} instances .
   */
  Set<CollisionHandler<? super CollidableT, ? extends Collidable<?>>> leftCompatibleHandlers();

  /**
   * Returns the set of {@link CollisionHandler} instances that are compatible with this {@link
   * Collidable} where this collidable's type is compatible with the second type parameter of the
   * {@link CollisionHandler} instances.
   */
  Set<CollisionHandler<? extends Collidable<?>, ? super CollidableT>> rightCompatibleHandlers();

  /** Returns the bounding rectangle of this {@link Collidable} instance. */
  Rectangle rect();
}
