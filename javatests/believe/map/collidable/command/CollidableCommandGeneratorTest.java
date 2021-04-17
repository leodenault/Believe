package believe.map.collidable.command;

import static believe.map.data.testing.Truth.assertThat;
import static believe.map.tiled.testing.TiledFakes.fakeTiledObject;
import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static believe.util.Util.hashSetOf;
import static com.google.common.truth.Truth.assertThat;

import believe.command.Command;
import believe.core.PropertyProvider;
import believe.geometry.Rectangle;
import believe.geometry.RectangleKt;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.data.ObjectFactory;
import believe.map.data.testing.Truth;
import believe.physics.manager.PhysicsManageable;
import believe.scene.GeneratedMapEntityData;
import believe.physics.manager.PhysicsManager;
import javax.annotation.Nullable;
import com.google.common.truth.Correspondence;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/** Unit tests for {@link CollidableCommandGenerator}. */
public final class CollidableCommandGeneratorTest {
  private static final String SHOULD_DESPAWN_PARAMETER = "should_despawn";
  private static final boolean SHOULD_DESPAWN = true;
  private static final String SHOULD_DESPAWN_STRING_VALUE = String.valueOf(SHOULD_DESPAWN);
  private static final String UNPARSABLE_SHOULD_DESPAWN_PARAMETER = "unparsable_should_despawn";
  private static final String UNPARSABLE_SHOULD_DESPAWN_VALUE = "7ru3";
  private static final Command COMMAND = CollidableCommandGeneratorTest::doNothing;
  private static Map<String, String> PROPERTIES =
      hashMapOf(
          entry(SHOULD_DESPAWN_PARAMETER, SHOULD_DESPAWN_STRING_VALUE),
          entry(UNPARSABLE_SHOULD_DESPAWN_PARAMETER, UNPARSABLE_SHOULD_DESPAWN_VALUE));

  private final GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
      GeneratedMapEntityData.newBuilder();
  private final FakeCollidableCommandFactory collidableCommandFactory =
      new FakeCollidableCommandFactory();
  private final Correspondence<PhysicsManageable, CommandDetails> COMMAND_CORRESPONDENCE =
      Correspondence.from(
          (actual, expectedCommandDetails) -> {
            FakePhysicsManager physicsManager = new FakePhysicsManager();
            actual.addToPhysicsManager(physicsManager);
            CommandDetails actualCommandDetails = collidableCommandFactory.commandDetails;

            return Objects.equals(actualCommandDetails, expectedCommandDetails);
          },
          "");

  @Test
  void parseObject_generatesValidCommand() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            CollidableCommandGeneratorTest::generateCommand,
            collidableCommandFactory,
            SHOULD_DESPAWN_PARAMETER);

    ObjectFactory factory =
        collidableCommandGenerator.parseObject(
            fakeTiledObject(
                "",
                "",
                /* x= */ 100,
                /* y= */ 200,
                /* width= */ 100,
                /* height= */ 200,
                PROPERTIES));

    assertThat(factory)
        .outputPhysicsManageableSet()
        .comparingElementsUsing(COMMAND_CORRESPONDENCE)
        .containsExactly(
            new CommandDetails(SHOULD_DESPAWN, COMMAND, RectangleKt.rectangle(100, 200, 100, 200)));
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

    ObjectFactory factory =
        collidableCommandGenerator.parseObject(
            fakeTiledObject(
                "", "", /* x= */ 100, /* y= */ 200, /* width= */ 100, /* height= */ 200));

    assertThat(factory)
        .outputPhysicsManageableSet()
        .comparingElementsUsing(COMMAND_CORRESPONDENCE)
        .containsExactly(
            new CommandDetails(
                /* shouldDespawn= */ false, COMMAND, RectangleKt.rectangle(100, 200, 100, 200)));
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

    ObjectFactory factory =
        collidableCommandGenerator.parseObject(
            fakeTiledObject(
                "", "", /* x= */ 100, /* y= */ 200, /* width= */ 100, /* height= */ 200));

    assertThat(factory)
        .outputPhysicsManageableSet()
        .comparingElementsUsing(COMMAND_CORRESPONDENCE)
        .containsExactly(
            new CommandDetails(
                /* shouldDespawn= */ false, COMMAND, RectangleKt.rectangle(100, 200, 100, 200)));
  }

  @Test
  void parseObject_commandGeneratorReturnsEmpty_doesNothing() {
    CollidableCommandGenerator collidableCommandGenerator =
        new CollidableCommandGenerator(
            (propertyProvider) -> null, collidableCommandFactory, SHOULD_DESPAWN_PARAMETER);

    ObjectFactory factory =
        collidableCommandGenerator.parseObject(
            fakeTiledObject("", "", /* x= */ 0, /* y= */ 0, /* width= */ 0, /* height= */ 0));

    assertThat(factory).isEqualTo(ObjectFactory.EMPTY);
  }

  private static void doNothing() {}

  private static Command generateCommand(PropertyProvider propertyProvider) {
    return COMMAND;
  }

  private static final class CommandDetails {
    final boolean shouldDespawn;
    final Command command;
    final Rectangle rectangle;

    CommandDetails(boolean shouldDespawn, Command command, Rectangle rectangle) {
      this.shouldDespawn = shouldDespawn;
      this.command = command;
      this.rectangle = rectangle;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CommandDetails that = (CommandDetails) o;
      return shouldDespawn == that.shouldDespawn
          && command.equals(that.command)
          && rectangle.equals(that.rectangle);
    }

    @Override
    public int hashCode() {
      return Objects.hash(shouldDespawn, command, rectangle);
    }
  }

  private static final class FakeCollidableCommandFactory extends CollidableCommandFactory {
    @Nullable CommandDetails commandDetails;

    FakeCollidableCommandFactory() {
      super(Collections::emptySet);
    }

    @Override
    CollidableCommand create(boolean shouldDespawn, Command command, Rectangle rectangle) {
      this.commandDetails = new CommandDetails(shouldDespawn, command, rectangle);
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
