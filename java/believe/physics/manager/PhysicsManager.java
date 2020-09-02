package believe.physics.manager;

import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandlerExecutor;
import believe.physics.gravity.GravityObject;
import dagger.Reusable;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A manager for physical interactions in the game. This object manages all instances of objects
 * that are affected by gravity or that can collide with each other.
 */
@Reusable
public class PhysicsManager {
  public static final float GRAVITY = 0.00125f; // Pixels per millisecond^2

  private final CollisionHandlerExecutor collisionHandlerExecutor;
  private Set<GravityObject> gravityObjects;
  private Set<Collidable<?>> collidables;
  private Set<Collidable<?>> staticCollidables;
  private Set<Collidable<?>> removed;

  @Inject
  PhysicsManager(CollisionHandlerExecutor collisionHandlerExecutor) {
    this(collisionHandlerExecutor, new HashSet<>(), new HashSet<>(), new HashSet<>());
  }

  protected PhysicsManager(
      CollisionHandlerExecutor collisionHandlerExecutor,
      Set<GravityObject> gravityObjects,
      Set<Collidable<?>> collidables,
      Set<Collidable<?>> staticCollidables) {
    this.collisionHandlerExecutor = collisionHandlerExecutor;
    this.gravityObjects = gravityObjects;
    this.collidables = collidables;
    this.staticCollidables = staticCollidables;
    this.removed = new HashSet<>();
  }

  public void reset() {
    this.gravityObjects.clear();
    this.collidables.clear();
    this.staticCollidables.clear();
  }

  public void addGravityObject(GravityObject gravityObject) {
    gravityObjects.add(gravityObject);
  }

  public void addGravityObjects(Collection<? extends GravityObject> gravityObjects) {
    this.gravityObjects.addAll(gravityObjects);
  }

  public boolean removeGravityObject(GravityObject gravityObject) {
    return gravityObjects.remove(gravityObject);
  }

  /** All collidables added here will interact with all other collidables */
  public void addCollidable(Collidable<?> collidable) {
    collidables.add(collidable);
  }

  /** All collidables added here will interact with all other collidables */
  public void addCollidables(Collection<? extends Collidable<?>> collidables) {
    this.collidables.addAll(collidables);
  }

  /** All collidables added here do not interact with each other. */
  public void addStaticCollidable(Collidable<?> collidable) {
    this.staticCollidables.add(collidable);
  }

  /** All collidables added to this list do not interact with each other. */
  public void addStaticCollidables(Collection<? extends Collidable<?>> collidables) {
    this.staticCollidables.addAll(collidables);
  }

  public void removeCollidable(Collidable<?> collidable) {
    this.removed.add(collidable);
  }

  public void update(int delta) {
    applyGravity(delta);
    checkCollisions();
  }

  private void applyGravity(int delta) {
    for (GravityObject grav : gravityObjects) {
      grav.setVerticalSpeed(grav.getVerticalSpeed() + GRAVITY * delta);
    }
  }

  // This might end up needing some spacial data structures
  // for optimization, as it's currently not terribly efficient
  private void checkCollisions() {
    for (Collidable<?> collidable : removed) {
      collidables.remove(collidable);
    }

    for (Collidable<?> staticCollidable : removed) {
      staticCollidables.remove(staticCollidable);
    }

    removed.clear();

    Collidable<?>[] coll = new Collidable<?>[collidables.size()];
    Collidable<?>[] statColl = new Collidable<?>[staticCollidables.size()];
    collidables.toArray(coll);
    staticCollidables.toArray(statColl);

    for (int i = 0; i < coll.length; i++) {
      Collidable<?> c1 = coll[i];

      // Interact with all static collidables first
      for (Collidable<?> sc2 : statColl) {
        engageCollision(c1, sc2);
      }

      // Then interact with the normal collidables
      for (int k = i + 1; k < coll.length; k++) {
        Collidable<?> c2 = coll[k];
        engageCollision(c1, c2);
      }
    }
  }

  private void engageCollision(Collidable<?> c1, Collidable<?> c2) {
    if (c1.rect().intersects(c2.rect())) {
        collisionHandlerExecutor.execute(c1, c2);
    }
  }
}
