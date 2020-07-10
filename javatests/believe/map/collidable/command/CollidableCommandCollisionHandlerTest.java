package believe.map.collidable.command;

import static believe.geometry.RectangleKt.rectangle;
import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import believe.physics.collision.testing.FakeCollidable;
import believe.physics.manager.PhysicsManager;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/** Unit tests for {@link CollidableCommandCollisionHandler}. */
@InstantiateMocksIn
final class CollidableCommandCollisionHandlerTest {
  private CollidableCommandCollisionHandler collisionHandler;

  @Mock private PhysicsManager physicsManager;

  @BeforeEach
  void setUp() {
    collisionHandler = new CollidableCommandCollisionHandler(physicsManager);
  }

  private boolean commandWasExecuted = false;

  @Test
  void handleCollision_executesCommand() {
    collisionHandler.handleCollision(
        new CollidableCommand(
            hashSetOf(collisionHandler),
            /* shouldDespawn= */ false,
            this::executeCommand,
            rectangle(0, 0, 0, 0)),
        new FakeCollidable());

    assertThat(commandWasExecuted).isTrue();
  }

  @Test
  void handleCollision_commandShouldDespawn_despawnsCommand() {
    CollidableCommand collidableCommand =
        new CollidableCommand(
            hashSetOf(collisionHandler),
            /* shouldDespawn= */ true,
            this::executeCommand,
            rectangle(0, 0, 0, 0));

    collisionHandler.handleCollision(collidableCommand, new FakeCollidable());

    verify(physicsManager).removeCollidable(collidableCommand);
  }

  private void executeCommand() {
    commandWasExecuted = true;
  }
}
