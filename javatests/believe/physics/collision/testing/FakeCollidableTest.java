package believe.physics.collision.testing;

import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/** Unit tests for {@link FakeCollidable}. */
@InstantiateMocksIn
public final class FakeCollidableTest {
  private FakeCollidable fakeCollidable;

  @Mock private CollisionHandler<? super FakeCollidable, ? extends Collidable<?>> leftHandler;
  @Mock private CollisionHandler<? extends Collidable<?>, ? super FakeCollidable> rightHandler;

  @BeforeEach
  void setUp() {
    fakeCollidable = new FakeCollidable(hashSetOf(leftHandler), hashSetOf(rightHandler));
  }

  @Test
  void leftCompatibleHandlers_returnsProvidedHandlers() {
    assertThat(fakeCollidable.leftCompatibleHandlers()).containsExactly(leftHandler);
  }

  @Test
  void rightCompatibleHandlers_returnsProvidedHandlers() {
    assertThat(fakeCollidable.rightCompatibleHandlers()).containsExactly(rightHandler);
  }
}
