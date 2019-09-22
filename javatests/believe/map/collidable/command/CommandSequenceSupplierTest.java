package believe.map.collidable.command;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import believe.character.playable.PlayableCharacter;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;
import believe.map.tiled.testing.FakeTiledMap;
import believe.map.tiled.testing.FakeTiledObjectFactory;
import believe.testing.mockito.InstantiateMocksIn;
import com.google.protobuf.TextFormat.ParseException;
import javax.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;

/** Unit tests for {@link CommandSequenceSupplier}. */
@InstantiateMocksIn
final class CommandSequenceSupplierTest {
  private static final class CommandSupplierUsingParams
      implements CommandSupplier<PlayableCharacter, Void> {
    static final String COMMAND_PARAM_KEY = "key";
    static final String COMMAND_PARAM_VALUE = "value";
    @Nullable String retrievedParamValue = null;

    @Override
    public Command<PlayableCharacter, Void> supplyCommand(TiledObject tiledObject) {
      retrievedParamValue = tiledObject.getProperty(COMMAND_PARAM_KEY).orElse(null);
      return Command.create((first, second) -> {}, tiledObject);
    }
  }

  private static final String SEQUENCE_PARAMETER = "sequence";
  private static final String COMMAND_1_NAME = "command 1 name";
  private static final String COMMAND_2_NAME = "command 2 name";
  private static final String COMMAND_WITH_PARAMS_NAME = "command with params";
  private static final String INVALID_COMMAND_NAME = "invalid command";
  private static final String COMMAND_SEQUENCE_SPEC =
      "commands { name: '" + COMMAND_1_NAME + "' } commands { name: '" + COMMAND_2_NAME + "' }";
  private static final String INVALID_SEQUENCE_SPEC = "not off to a good start";
  private static final String COMMAND_SEQUENCE_SPEC_WITH_INVALID_COMMAND =
      "commands { name: '"
          + COMMAND_1_NAME
          + "' } commands { name: '"
          + INVALID_COMMAND_NAME
          + "' }";
  private static final String COMMAND_SEQUENCE_SPEC_WITH_COMMAND_CONTAINING_PARAMS =
      "commands { name: '"
          + COMMAND_WITH_PARAMS_NAME
          + "' parameters: { key: '"
          + CommandSupplierUsingParams.COMMAND_PARAM_KEY
          + "' value: '"
          + CommandSupplierUsingParams.COMMAND_PARAM_VALUE
          + "' }}";

  private final CommandSupplierUsingParams commandSupplierUsingParams =
      new CommandSupplierUsingParams();

  private Map<String, CommandSupplier<PlayableCharacter, ?>> commandSupplierMap;
  private CommandSequenceSupplier supplier;

  @Mock private CommandCollisionHandler<PlayableCharacter, Void> commandCollisionHandler;
  @Mock private PlayableCharacter playableCharacter;

  @BeforeEach
  void setUp() {
    commandSupplierMap =
        hashMapOf(
            entry(COMMAND_1_NAME, CommandSupplier.from(commandCollisionHandler)),
            entry(COMMAND_2_NAME, CommandSupplier.from(commandCollisionHandler)),
            entry(COMMAND_WITH_PARAMS_NAME, commandSupplierUsingParams));
    supplier = new CommandSequenceSupplier(commandSupplierMap, SEQUENCE_PARAMETER);
  }

  @Test
  void supplyCommand_createsCommandWithHandlerThatExecutesSubcommands() {
    Command<PlayableCharacter, Void> command =
        supplier.supplyCommand(
            FakeTiledObjectFactory.create(
                FakeTiledMap.tiledMapWithObjectPropertyValue(COMMAND_SEQUENCE_SPEC),
                EntityType.COMMAND));

    command.getCommandCollisionHandler().handleCollision(command, playableCharacter);

    verify(commandCollisionHandler, times(2)).handleCollision(any(), any());
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_sequenceParameterMissing_logsWarningAndReturnsEmptyCommand(
      VerifiableLogSystem logSystem) {
    Command<PlayableCharacter, Void> command =
        supplier.supplyCommand(
            FakeTiledObjectFactory.create(
                FakeTiledMap.tiledMapWithDefaultPropertyValues(), EntityType.COMMAND));

    command.getCommandCollisionHandler().handleCollision(command, playableCharacter);

    verify(commandCollisionHandler, never()).handleCollision(any(), any());
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Command sequence value missing.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_sequenceFormatIsInvalid_logsWarningAndReturnsEmptyCommand(
      VerifiableLogSystem logSystem) {
    Command<PlayableCharacter, Void> command =
        supplier.supplyCommand(
            FakeTiledObjectFactory.create(
                FakeTiledMap.tiledMapWithObjectPropertyValue(INVALID_SEQUENCE_SPEC),
                EntityType.COMMAND));

    command.getCommandCollisionHandler().handleCollision(command, playableCharacter);

    verify(commandCollisionHandler, never()).handleCollision(any(), any());
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Command sequence format is invalid.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(ParseException.class);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_subcommandDoesNotExist_logsWarningAndSkipsSubcommand(
      VerifiableLogSystem logSystem) {
    Command<PlayableCharacter, Void> command =
        supplier.supplyCommand(
            FakeTiledObjectFactory.create(
                FakeTiledMap.tiledMapWithObjectPropertyValue(
                    COMMAND_SEQUENCE_SPEC_WITH_INVALID_COMMAND),
                EntityType.COMMAND));

    command.getCommandCollisionHandler().handleCollision(command, playableCharacter);

    verify(commandCollisionHandler).handleCollision(any(), any());
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern(".*'" + INVALID_COMMAND_NAME + "' is not a valid command.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  void supplyCommand_subcommandRequiresParams_providesParamsToSubcommand() {
    supplier.supplyCommand(
        FakeTiledObjectFactory.create(
            FakeTiledMap.tiledMapWithObjectPropertyValue(
                COMMAND_SEQUENCE_SPEC_WITH_COMMAND_CONTAINING_PARAMS),
            EntityType.COMMAND));

    assertThat(commandSupplierUsingParams.retrievedParamValue)
        .isEqualTo(CommandSupplierUsingParams.COMMAND_PARAM_VALUE);
  }
}
