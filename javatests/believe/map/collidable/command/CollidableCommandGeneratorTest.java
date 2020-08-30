package believe.map.collidable.command;

import static believe.map.tiled.testing.TiledFakes.fakeTiledObject;
import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import believe.command.Command;
import believe.core.PropertyProvider;
import believe.geometry.Rectangle;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.data.GeneratedMapEntityData;
import believe.map.data.EntityType;
import believe.physics.manager.PhysicsManager;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

/** Unit tests for {@link CollidableCommandGenerator}. */
public final class CollidableCommandGeneratorTest {
  private static final String SHOULD_DESPAWN_PARAMETER = "should_despawn";
  private static final String SHOULD_DESPAWN_VALUE = "true";
  private static final String UNPARSABLE_SHOULD_DESPAWN_PARAMETER = "unparsable_should_despawn";
  private static final String UNPARSABLE_SHOULD_DESPAWN_VALUE = "7ru3";
  private static final Command COMMAND = CollidableCommandGeneratorTest::doNothing;
  private static Map<String, String> PROPERTIES =
      hashMapOf(
          entry(SHOULD_DESPAWN_PARAMETER, SHOULD_DESPAWN_VALUE),
          entry(UNPARSABLE_SHOULD_DESPAWN_PARAMETER, UNPARSABLE_SHOULD_DESPAWN_VALUE));

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
            SHOULD_DESPAWN_PARAMETER);

    collidableCommandGenerator.parseObject(
        EntityType.COMMAND,
        fakeTiledObject(
            "", "", /* x= */ 100, /* y= */ 200, /* width= */ 100, /* height= */ 200, PROPERTIES),
        generatedMapEntityDataBuilder);

    // Ensure that collidableCommandFactory is triggered so we can assert on the parameters passed
    // to it.
    generatedMapEntityDataBuilder.build().physicsManageables().stream()
        .findFirst()
        .ifPresent(
            physicsManageable -> physicsManageable.addToPhysicsManager(new FakePhysicsManager()));
    assertThat(collidableCommandFactory.command).isEqualTo(COMMAND);
    assertThat(collidableCommandFactory.shouldDespawn)
        .isEqualTo(Boolean.valueOf(SHOULD_DESPAWN_VALUE));
    assertThat(collidableCommandFactory.rectangle.getX()).isWithin(0).of(100);
    assertThat(collidableCommandFactory.rectangle.getY()).isWithin(0).of(200);
    assertThat(collidableCommandFactory.rectangle.getWidth()).isWithin(0).of(100);
    assertThat(collidableCommandFactory.rectangle.getHeight()).isWithin(0).of(200);
  }

  @Test
  void parseObject_objectIsNotCommand_doesNothing() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            CollidableCommandGeneratorTest::generateCommand,
            collidableCommandFactory,
            SHOULD_DESPAWN_PARAMETER);

    collidableCommandGenerator.parseObject(
        EntityType.COLLIDABLE_TILE, fakeTiledObject(""), generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  @Test
  @VerifiesLoggingCalls
  void parseObject_shouldDespawnCannotBeFound_defaultsToFalseAndLogsWarning(
      VerifiableLogSystem logSystem) {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            CollidableCommandGeneratorTest::generateCommand,
            collidableCommandFactory,
            "should_despawn key with no value");

    collidableCommandGenerator.parseObject(
        EntityType.COMMAND,
        fakeTiledObject("", "", /* x= */ 100, /* y= */ 200, /* width= */ 100, /* height= */ 200),
        generatedMapEntityDataBuilder);

    assertThat(collidableCommandFactory.shouldDespawn).isFalse();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasSeverity(LogSeverity.WARNING)
        .hasPattern("Missing a 'should_despawn key with no value' parameter.");
  }

  @Test
  void parseObject_shouldDespawnIsUnparsable_defaultsToFalse() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            CollidableCommandGeneratorTest::generateCommand,
            collidableCommandFactory,
            UNPARSABLE_SHOULD_DESPAWN_PARAMETER);

    collidableCommandGenerator.parseObject(
        EntityType.COMMAND,
        fakeTiledObject("", "", /* x= */ 100, /* y= */ 200, /* width= */ 100, /* height= */ 200),
        generatedMapEntityDataBuilder);

    assertThat(collidableCommandFactory.shouldDespawn).isFalse();
  }

  @Test
  void parseObject_commandGeneratorReturnsEmpty_doesNothing() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            (propertyProvider) -> null, collidableCommandFactory, SHOULD_DESPAWN_PARAMETER);

    collidableCommandGenerator.parseObject(
        EntityType.COLLIDABLE_TILE,
        fakeTiledObject("", "", /* x= */ 0, /* y= */ 0, /* width= */ 0, /* height= */ 0),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  private static void doNothing() {}

  private static Command generateCommand(PropertyProvider propertyProvider) {
    return COMMAND;
  }

  private static final class FakeCollidableCommandFactory extends CollidableCommandFactory {
    boolean shouldDespawn;
    @Nullable Command command = null;
    @Nullable Rectangle rectangle = null;

    FakeCollidableCommandFactory() {
      super(Collections::emptySet);
    }

    @Override
    CollidableCommand create(boolean shouldDespawn, Command command, Rectangle rectangle) {
      this.shouldDespawn = shouldDespawn;
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
