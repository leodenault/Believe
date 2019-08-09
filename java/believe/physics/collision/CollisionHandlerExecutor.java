package believe.physics.collision;

/** Identifies a {@link CollisionHandler} for a pair of {@link Collidable} objects. */
public interface CollisionHandlerExecutor {
  /**
   * Executes a collision between {@code collidable1} and {@code collidable2} assuming that the two
   * have overlapping collision handlers.
   */
  <A extends Collidable<A>, B extends Collidable<B>> void execute(
      Collidable<A> collidable1, Collidable<B> collidable2);
}
