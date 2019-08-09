package believe.physics.collision;

import static believe.util.Util.hashSetOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import believe.geometry.Rectangle;
import believe.physics.collision.testing.FakeCollidable;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Set;

/** Unit tests for {@link CollisionHandlerExecutorImpl}. */
@InstantiateMocksIn
final class CollisionHandlerExecutorImplTest {
  private static final class UnappliableCollidable implements Collidable<UnappliableCollidable> {
    @Override
    public Set<CollisionHandler<? super UnappliableCollidable, ? extends Collidable<?>>>
        leftCompatibleHandlers() {
      return Collections.emptySet();
    }

    @Override
    public Set<CollisionHandler<? extends Collidable<?>, ? super UnappliableCollidable>>
        rightCompatibleHandlers() {
      return Collections.emptySet();
    }

    @Override
    public Rectangle rect() {
      return new Rectangle(0, 0, 0, 0);
    }
  }

  private static final UnappliableCollidable UNAPPLIABLE_COLLIDABLE = new UnappliableCollidable();

  private final CollisionHandlerExecutorImpl resolver = new CollisionHandlerExecutorImpl();

  private FakeCollidable fakeCollidable1;
  private FakeCollidable fakeCollidable2;

  @Mock private CollisionHandler<FakeCollidable, FakeCollidable> collisionHandler;

  @BeforeEach
  void setUp() {
    fakeCollidable1 = new FakeCollidable(hashSetOf(collisionHandler), Collections.emptySet());
    fakeCollidable2 = new FakeCollidable(Collections.emptySet(), hashSetOf(collisionHandler));
  }

  @Test
  void execute_executesHandler() {
    resolver.execute(fakeCollidable1, fakeCollidable2);

    verify(collisionHandler).handleCollision(fakeCollidable1, fakeCollidable2);
  }

  @Test
  void execute_paramsAreInverted_executesHandlerCorrectly() {
    resolver.execute(fakeCollidable2, fakeCollidable1);

    verify(collisionHandler).handleCollision(fakeCollidable1, fakeCollidable2);
  }

  @Test
  void execute_firstParamDoesNotApply_skipsHandler() {
    resolver.execute(UNAPPLIABLE_COLLIDABLE, fakeCollidable1);

    verify(collisionHandler, never()).handleCollision(any(), any());
  }

  @Test
  void execute_secondParamDoesNotApply_skipsHandler() {
    resolver.execute(fakeCollidable1, UNAPPLIABLE_COLLIDABLE);

    verify(collisionHandler, never()).handleCollision(any(), any());
  }
}
