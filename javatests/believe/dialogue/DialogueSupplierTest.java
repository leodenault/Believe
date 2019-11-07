package believe.dialogue;

import static com.google.common.truth.Truth8.assertThat;

import believe.command.Command;
import believe.command.CommandSequenceParser;
import believe.command.proto.CommandSequenceProto;
import believe.command.proto.CommandSequenceProto.CommandSequence;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.react.ObservableValue;
import com.google.protobuf.TextFormat;
import javax.annotation.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link DialogueSupplier}. */
final class DialogueSupplierTest {
  private static final String DIALOGUE_ID_PROPERTY = "dialogue_id";
  private static final String FOLLOW_UP_COMMAND_PROPERTY = "follow_up_commands";
  private static final String VALID_DIALOGUE_NAME = "rainbow";
  private static final Dialogue DIALOGUE =
      Dialogue.newBuilder()
          .addResponses(
              Response.newBuilder()
                  .setPortraitLocation("/somewhere/over/the/rainbow.jpg")
                  .setResponseText("Look at me I'm a rainbow! :D"))
          .build();
  private static final String COMMAND_NAME = "command_name";
  private static final CommandSequence FOLLOWUP_COMMANDS =
      CommandSequence.newBuilder()
          .addCommands(CommandSequenceProto.Command.newBuilder().setName(COMMAND_NAME))
          .build();
  private static final DialogueMap DIALOGUE_MAP =
      DialogueMap.newBuilder().putDialogues(VALID_DIALOGUE_NAME, DIALOGUE).build();
  private static final Command PARSED_COMMAND = DialogueSupplierTest::doNothing;

  private final ObservableValue<Optional<DialogueData>> observableDialogue =
      ObservableValue.of(Optional.empty());
  private final DialogueSupplier dialogueSupplier =
      new DialogueSupplier(
          () -> DIALOGUE_MAP,
          new DialogueCommandFactory(() -> observableDialogue),
          DIALOGUE_ID_PROPERTY,
          FOLLOW_UP_COMMAND_PROPERTY,
          new FakeCommandSequenceParser());

  private boolean followupCommandExists = true;

  @Nullable private String dialogueName = null;

  @Test
  void supplyCommand_returnsCommandForCorrespondingProperties() {
    dialogueName = VALID_DIALOGUE_NAME;

    Optional<Command> command = dialogueSupplier.supplyCommand(this::getProperty);

    assertThat(command).isPresent();
    command.get().execute();
    assertThat(observableDialogue.get())
        .hasValue(DialogueData.newBuilder(DIALOGUE).setFollowupCommand(PARSED_COMMAND).build());
  }

  @Test
  void supplyCommand_followupCommandIsEmpty_dialogueDataContainsEmptyFollowupCommand() {
    dialogueName = VALID_DIALOGUE_NAME;
    followupCommandExists = false;

    Optional<Command> command = dialogueSupplier.supplyCommand(this::getProperty);

    assertThat(command).isPresent();
    command.get().execute();
    assertThat(observableDialogue.get()).hasValue(DialogueData.newBuilder(DIALOGUE).build());
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_dialogueNameNotSpecified_returnsEmptyAndLogsError(
      VerifiableLogSystem logSystem) {
    assertThat(dialogueSupplier.supplyCommand(this::getProperty)).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Dialogue name missing.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_dialogueDoesNotExist_returnsEmptyAndLogsError(VerifiableLogSystem logSystem) {
    dialogueName = "bogus_name";
    assertThat(dialogueSupplier.supplyCommand(this::getProperty)).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*bogus_name.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  private Optional<String> getProperty(String key) {
    switch (key) {
      case DIALOGUE_ID_PROPERTY:
        return Optional.ofNullable(dialogueName);
      case FOLLOW_UP_COMMAND_PROPERTY:
        return Optional.of(TextFormat.printer().printToString(FOLLOWUP_COMMANDS));
    }
    return Optional.empty();
  }

  private static void doNothing() {}

  private final class FakeCommandSequenceParser implements CommandSequenceParser {
    @Override
    public Optional<Command> parseSequence(String sequence) {
      return followupCommandExists ? Optional.of(PARSED_COMMAND) : Optional.empty();
    }

    // Not used in this test.
    @Override
    public Command parseSequence(CommandSequence commandSequence) {
      return () -> {};
    }
  }
}
