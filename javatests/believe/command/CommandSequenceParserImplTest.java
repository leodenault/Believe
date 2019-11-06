package believe.command;

import static com.google.common.truth.Truth8.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import believe.command.proto.CommandSequenceProto;
import believe.command.proto.CommandSequenceProto.CommandSequence;
import believe.core.PropertyProvider;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.mockito.InstantiateMocksIn;
import com.google.common.truth.Truth;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

/** Unit tests for {@link CommandSequenceParserImpl}. */
@InstantiateMocksIn
final class CommandSequenceParserImplTest {
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

  private static final String COMMAND_1_NAME = "command 1 name";
  private static final String COMMAND_2_NAME = "command 2 name";
  private static final String COMMAND_WITH_PARAMS_NAME = "command with params";
  private static final String INVALID_COMMAND_NAME = "invalid command";
  private static final CommandSequence COMMAND_SEQUENCE =
      CommandSequence.newBuilder()
          .addCommands(CommandSequenceProto.Command.newBuilder().setName(COMMAND_1_NAME))
          .addCommands(CommandSequenceProto.Command.newBuilder().setName(COMMAND_2_NAME))
          .build();
  private static final String INVALID_SEQUENCE_SPEC = "not off to a good start";
  private static final CommandSequence COMMAND_SEQUENCE_WITH_INVALID_COMMAND =
      CommandSequence.newBuilder()
          .addCommands(CommandSequenceProto.Command.newBuilder().setName(COMMAND_1_NAME))
          .addCommands(CommandSequenceProto.Command.newBuilder().setName(INVALID_COMMAND_NAME))
          .build();
  private static final CommandSequence COMMAND_SEQUENCE_WITH_COMMAND_CONTAINING_PARAMS =
      CommandSequence.newBuilder()
          .addCommands(
              CommandSequenceProto.Command.newBuilder()
                  .setName(COMMAND_WITH_PARAMS_NAME)
                  .putParameters(
                      CommandUsingParams.COMMAND_PARAM_KEY, CommandUsingParams.COMMAND_PARAM_VALUE))
          .build();

  private final CommandUsingParams commandUsingParams = new CommandUsingParams();
  private final CommandSequenceParserImpl parser =
      new CommandSequenceParserImpl(() -> this::generateCommand);

  @Mock private Command subcommand1;
  @Mock private Command subcommand2;

  @Test
  void parseSequence_returnsCommandContainingSubcommands() {
    Optional<Command> command =
        parser.parseSequence(TextFormat.printer().printToString(COMMAND_SEQUENCE));

    assertThat(command).isPresent();
    command.get().execute();
    verify(subcommand1).execute();
    verify(subcommand2).execute();
  }

  @Test
  @VerifiesLoggingCalls
  void parseSequence_sequenceFormatIsInvalid_logsWarningAndReturnsEmpty(
      VerifiableLogSystem logSystem) {
    assertThat(parser.parseSequence(INVALID_SEQUENCE_SPEC)).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Command sequence format is invalid.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(ParseException.class);
  }

  @Test
  void parseSequence_subcommandDoesNotExist_skipsSubcommand() {
    Optional<Command> command =
        parser.parseSequence(
            TextFormat.printer().printToString(COMMAND_SEQUENCE_WITH_INVALID_COMMAND));

    assertThat(command).isPresent();
    command.get().execute();
    verify(subcommand1).execute();
    verify(subcommand2, never()).execute();
  }

  @Test
  void parseSequence_subcommandRequiresParams_providesParamsToSubcommand() {
    Optional<Command> command =
        parser.parseSequence(
            TextFormat.printer().printToString(COMMAND_SEQUENCE_WITH_COMMAND_CONTAINING_PARAMS));

    assertThat(command).isPresent();
    Truth.assertThat(commandUsingParams.propertyProvider).isNotNull();
    command.get().execute();
    Truth.assertThat(commandUsingParams.hasExecuted).isTrue();
  }

  @Test
  void parseSequence_sequenceIsProto_returnsValidCommand() {
    Command command = parser.parseSequence(COMMAND_SEQUENCE);

    command.execute();
    verify(subcommand1).execute();
    verify(subcommand2).execute();
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
