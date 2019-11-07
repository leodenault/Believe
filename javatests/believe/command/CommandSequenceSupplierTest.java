package believe.command;

import static com.google.common.truth.Truth8.assertThat;

import believe.command.proto.CommandSequenceProto;
import believe.command.proto.CommandSequenceProto.CommandSequence;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import com.google.common.truth.Truth;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

final class CommandSequenceSupplierTest {
  private static final String SEQUENCE_PARAMETER = "sequence";
  private static final String COMMAND_PARAMETER = "some param";
  private static final String COMMAND_VALUE = "some value";
  private static final CommandSequenceProto.CommandSequence COMMAND_SEQUENCE =
      CommandSequenceProto.CommandSequence.newBuilder()
          .addCommands(
              CommandSequenceProto.Command.newBuilder()
                  .putParameters(COMMAND_PARAMETER, COMMAND_VALUE))
          .build();

  private final CommandSequenceSupplier commandSequenceSupplier =
      new CommandSequenceSupplier(SEQUENCE_PARAMETER, new FakeCommandSequenceParser());

  @Test
  void supplyCommand_returnsValidCommand() {
    Optional<Command> optionalCommand =
        commandSequenceSupplier.supplyCommand(
            key -> Optional.of(TextFormat.printer().printToString(COMMAND_SEQUENCE)));

    assertThat(optionalCommand).isPresent();
    Truth.assertThat(((FakeCommand) optionalCommand.get()).commandSequence)
        .isEqualTo(COMMAND_SEQUENCE);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_sequenceIsMissing_returnsEmptyAndLogsError(VerifiableLogSystem logSystem) {
    assertThat(commandSequenceSupplier.supplyCommand(key -> Optional.empty())).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasSeverity(LogSeverity.ERROR)
        .hasPattern("Command sequence value missing.*");
  }

  private static final class FakeCommand implements Command {
    final CommandSequenceProto.CommandSequence commandSequence;

    FakeCommand(String sequence) throws ParseException {
      CommandSequenceProto.CommandSequence.Builder commandSequence =
          CommandSequenceProto.CommandSequence.newBuilder();
      TextFormat.getParser().merge(sequence, commandSequence);
      this.commandSequence = commandSequence.build();
    }

    @Override
    public void execute() {}
  }

  private static final class FakeCommandSequenceParser implements CommandSequenceParser {
    @Override
    public Optional<Command> parseSequence(String sequence) {
      try {
        return Optional.of(new FakeCommand(sequence));
      } catch (ParseException e) {
        throw new RuntimeException("Unexpected parsing exception.", e);
      }
    }

    // Not used in this test.
    @Override
    public Command parseSequence(CommandSequence commandSequence) {
      return () -> {};
    }
  }
}
