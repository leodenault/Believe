package believe.command;

import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import believe.core.PropertyProvider;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.mockito.InstantiateMocksIn;
import com.google.common.truth.Truth;
import com.google.protobuf.TextFormat.ParseException;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

/** Unit tests for {@link CommandSequenceSupplier}. */
@InstantiateMocksIn
final class CommandSequenceSupplierTest {
  private static final class CommandUsingParams implements Command {
    static final String COMMAND_PARAM_KEY = "key";
    static final String COMMAND_PARAM_VALUE = "value";

    @Nullable PropertyProvider propertyProvider = null;
    boolean hasExecuted = false;

    CommandUsingParams() {}

    @Override
    public void execute() {
      hasExecuted = true;
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
          + CommandUsingParams.COMMAND_PARAM_KEY
          + "' value: '"
          + CommandUsingParams.COMMAND_PARAM_VALUE
          + "' }}";

  private final CommandUsingParams commandUsingParams = new CommandUsingParams();
  private final CommandSequenceSupplier supplier =
      new CommandSequenceSupplier(() -> this::generateCommand, SEQUENCE_PARAMETER);

  @Mock private Command subcommand1;
  @Mock private Command subcommand2;

  @Test
  void supplyCommand_returnsCommandContainingSubcommands() {
    Optional<Command> command = supplier.supplyCommand(key -> Optional.of(COMMAND_SEQUENCE_SPEC));

    assertThat(command).isPresent();
    command.get().execute();
    verify(subcommand1).execute();
    verify(subcommand2).execute();
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_sequenceParameterMissing_logsWarningAndReturnsEmpty(
      VerifiableLogSystem logSystem) {
    assertThat(supplier.supplyCommand(key -> Optional.empty())).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Command sequence value missing.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_sequenceFormatIsInvalid_logsWarningAndReturnsEmpty(
      VerifiableLogSystem logSystem) {
    assertThat(supplier.supplyCommand(key -> Optional.of(INVALID_SEQUENCE_SPEC))).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Command sequence format is invalid.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(ParseException.class);
  }

  @Test
  void supplyCommand_subcommandDoesNotExist_skipsSubcommand() {
    Optional<Command> command =
        supplier.supplyCommand(key -> Optional.of(COMMAND_SEQUENCE_SPEC_WITH_INVALID_COMMAND));

    assertThat(command).isPresent();
    command.get().execute();
    verify(subcommand1).execute();
    verify(subcommand2, never()).execute();
  }

  @Test
  void supplyCommand_subcommandRequiresParams_providesParamsToSubcommand() {
    Optional<Command> command =
        supplier.supplyCommand(
            key -> Optional.of(COMMAND_SEQUENCE_SPEC_WITH_COMMAND_CONTAINING_PARAMS));

    assertThat(command).isPresent();
    Truth.assertThat(commandUsingParams.propertyProvider).isNotNull();
    command.get().execute();
    Truth.assertThat(commandUsingParams.hasExecuted).isTrue();
  }

  private Optional<Command> generateCommand(String commandName, PropertyProvider propertyProvider) {
    switch (commandName) {
      case COMMAND_1_NAME:
        return Optional.of(subcommand1);
      case COMMAND_2_NAME:
        return Optional.of(subcommand2);
      case COMMAND_WITH_PARAMS_NAME:
        commandUsingParams.propertyProvider = propertyProvider;
        return Optional.of(commandUsingParams);
      default:
        return Optional.empty();
    }
  }
}
