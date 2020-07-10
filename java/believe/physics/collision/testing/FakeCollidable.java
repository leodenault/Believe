package believe.physics.collision.testing;

import static believe.geometry.RectangleKt.rectangle;

import believe.geometry.Rectangle;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;

import java.util.Collections;
import java.util.Set;

/** Fake {@link Collidable} for testing purposes. */
public final class FakeCollidable implements Collidable<FakeCollidable> {
  private final Rectangle rect = rectangle(0, 0, 0, 0);
  private final Set<CollisionHandler<? super FakeCollidable, ? extends Collidable<?>>> leftHandlers;
  private final Set<CollisionHandler<? extends Collidable<?>, ? super FakeCollidable>>
      rightHandlers;

  public FakeCollidable() {
    this(Collections.emptySet(), Collections.emptySet());
  }

  public FakeCollidable(
      Set<CollisionHandler<? super FakeCollidable, ? extends Collidable<?>>> leftHandlers,
      Set<CollisionHandler<? extends Collidable<?>, ? super FakeCollidable>> rightHandlers) {
    this.leftHandlers = leftHandlers;
    this.rightHandlers = rightHandlers;
  }

  @Override
  public Set<CollisionHandler<? super FakeCollidable, ? extends Collidable<?>>>
      leftCompatibleHandlers() {
    return leftHandlers;
  }

  @Override
  public Set<CollisionHandler<? extends Collidable<?>, ? super FakeCollidable>>
      rightCompatibleHandlers() {
    return rightHandlers;
  }

  @Override
  public Rectangle rect() {
    return rect;
  }
}
