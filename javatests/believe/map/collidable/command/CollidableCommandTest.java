package believe.map.collidable.command;

import static believe.geometry.RectangleKt.rectangle;
import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import believe.command.Command;
import believe.geometry.Rectangle;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link CollidableCommand}. */
public final class CollidableCommandTest {
  private static final Rectangle RECTANGLE =
      rectangle(/* x= */ 10, /* y= */ 20, /* width= */ 100, /* height= */ 200);
  private static final CollisionHandler<? super CollidableCommand, ? extends Collidable<?>>
      COLLISION_HANDLER = CollidableCommandTest::handleCollision;
  private static final Command COMMAND = CollidableCommandTest::executeCommand;

  private final CollidableCommand collidableCommand =
      new CollidableCommand(
          hashSetOf(COLLISION_HANDLER), /* shouldDespawn= */ true, COMMAND, RECTANGLE);

  @Test
  void getRect_returnsOriginalRectangle() {
    assertThat(collidableCommand.rect()).isEqualTo(RECTANGLE);
  }

  @Test
  void leftCompatibleHandlers_returnsProvidedCollisionHandler() {
    assertThat(collidableCommand.leftCompatibleHandlers()).containsExactly(COLLISION_HANDLER);
  }

  @Test
  void rightCompatibleHandlers_returnsEmpty() {
    assertThat(collidableCommand.rightCompatibleHandlers()).isEmpty();
  }

  @Test
  void getCommand_returnsCommand() {
    assertThat(collidableCommand.command()).isEqualTo(COMMAND);
  }

  @Test
  void shouldDespawn_returnsDespawn() {
    assertThat(collidableCommand.shouldDespawn()).isTrue();
  }

  private static void handleCollision(
      CollidableCommand collidableCommand, Collidable<?> collidable) {}

  private static void executeCommand() {}
}
