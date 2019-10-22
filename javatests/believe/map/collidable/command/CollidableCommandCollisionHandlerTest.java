package believe.map.collidable.command;

import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import believe.geometry.Rectangle;
import believe.physics.collision.testing.FakeCollidable;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link CollidableCommandCollisionHandler}. */
final class CollidableCommandCollisionHandlerTest {
  private final CollidableCommandCollisionHandler collisionHandler =
      new CollidableCommandCollisionHandler();

  private boolean commandWasExecuted = false;

  @Test
  void handleCollision_executesCommand() {
    collisionHandler.handleCollision(
        new CollidableCommand(
            hashSetOf(collisionHandler), this::executeCommand, new Rectangle(0, 0, 0, 0)),
        new FakeCollidable());

    assertThat(commandWasExecuted).isTrue();
  }

  private void executeCommand() {
    commandWasExecuted = true;
  }
}
