package believe.map.collidable.command;

import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import believe.command.Command;
import believe.core.PropertyProvider;
import believe.geometry.Rectangle;
import believe.map.data.GeneratedMapEntityData;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;
import believe.physics.manager.PhysicsManager;
import believe.util.Util;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

/** Unit tests for {@link CollidableCommandGenerator}. */
public final class CollidableCommandGeneratorTest {
  private static final String COMMAND_PARAMETER_NAME = "command";
  private static final Command COMMAND = CollidableCommandGeneratorTest::doNothing;

  private final GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
      GeneratedMapEntityData.newBuilder();

  private final FakeCollidableCommandFactory collidableCommandFactory =
      new FakeCollidableCommandFactory();

  @Test
  void parseObject_generatesValidCommand() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            CollidableCommandGeneratorTest::generateCommand,
            collidableCommandFactory,
            COMMAND_PARAMETER_NAME);

    collidableCommandGenerator.parseObject(
        TiledObject.create(
            EntityType.COMMAND,
            key -> Optional.of("valid_command"),
            /* x= */ 100,
            /* y= */ 200,
            /* width= */ 100,
            /* height= */ 200),
        generatedMapEntityDataBuilder);

    // Ensure that collidableCommandFactory is triggered so we can assert on the parameters passed
    // to it.
    generatedMapEntityDataBuilder.build().physicsManageables().stream()
        .findFirst()
        .ifPresent(
            physicsManageable -> physicsManageable.addToPhysicsManager(new FakePhysicsManager()));
    assertThat(collidableCommandFactory.command).isEqualTo(COMMAND);
    assertThat(collidableCommandFactory.rectangle.getX()).isWithin(0).of(100);
    assertThat(collidableCommandFactory.rectangle.getY()).isWithin(0).of(200);
    assertThat(collidableCommandFactory.rectangle.getWidth()).isWithin(0).of(100);
    assertThat(collidableCommandFactory.rectangle.getHeight()).isWithin(0).of(200);
  }

  @Test
  void parseObject_propertyCannotBeFound_doesNothing() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            CollidableCommandGeneratorTest::generateCommand,
            collidableCommandFactory,
            COMMAND_PARAMETER_NAME);

    collidableCommandGenerator.parseObject(
        TiledObject.create(
            EntityType.COLLIDABLE_TILE,
            key -> Optional.empty(),
            /* x= */ 0,
            /* y= */ 0,
            /* width= */ 0,
            /* height= */ 0),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  @Test
  void parseObject_commandGeneratorReturnsEmpty_doesNothing() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            (commandName, propertyProvider) -> Optional.empty(),
            collidableCommandFactory,
            COMMAND_PARAMETER_NAME);

    collidableCommandGenerator.parseObject(
        TiledObject.create(
            EntityType.COLLIDABLE_TILE,
            key -> Optional.of("valid_command"),
            /* x= */ 0,
            /* y= */ 0,
            /* width= */ 0,
            /* height= */ 0),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  private static void doNothing() {}

  private static Optional<believe.command.Command> generateCommand(
      String commandName, PropertyProvider propertyProvider) {
    return Optional.of(COMMAND);
  }

  private static final class FakeCollidableCommandFactory extends CollidableCommandFactory {
    @Nullable Command command = null;
    @Nullable Rectangle rectangle = null;

    FakeCollidableCommandFactory() {
      super(Collections::emptySet);
    }

    @Override
    CollidableCommand create(Command command, Rectangle rectangle) {
      this.command = command;
      this.rectangle = rectangle;
      return null;
    }
  }

  private static final class FakePhysicsManager extends PhysicsManager {
    FakePhysicsManager() {
      super(
          /* collisionHandlerExecutor= */ null,
          /* gravityObjects= */ hashSetOf(),
          /* collidables= */ hashSetOf(),
          /* staticCollidables= */ hashSetOf());
    }
  }
}
