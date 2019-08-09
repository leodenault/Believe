package believe.physics.manager;

import believe.physics.collision.Collidable;
import believe.physics.gravity.GravityObject;

/** An object which knows how to it needs to be managed by the {@link PhysicsManager}. */
public interface PhysicsManageable {
  /**
   * Adds this instance to {@code physicsManager} if appropriate to do so. This instance has the
   * option to add itself to all relevant management lists. For example, if this instance is both
   * affected by gravity and can collide with others, it could add itself through {@link
   * PhysicsManager#addGravityObject(GravityObject)} and {@link
   * PhysicsManager#addCollidable(Collidable)}.
   */
  void addToPhysicsManager(PhysicsManager physicsManager);
}
