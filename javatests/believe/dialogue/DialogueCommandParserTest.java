package believe.dialogue;

import static com.google.common.truth.Truth.assertThat;

import believe.command.Command;
import believe.command.proto.CommandProto;
import believe.dialogue.proto.DialogueProto;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.react.ObservableValue;
import com.google.common.truth.Truth8;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link DialogueCommandParser}. */
final class DialogueCommandParserTest {
  private static final String VALID_DIALOGUE_NAME = "rainbow";
  private static final Dialogue DIALOGUE =
      Dialogue.newBuilder()
          .addResponses(
              Response.newBuilder()
                  .setPortraitLocation("/somewhere/over/the/rainbow.jpg")
                  .setResponseText("Look at me I'm a rainbow! :D"))
          .build();
  private static final CommandProto.Command FOLLOWUP_COMMANDS =
      CommandProto.Command.newBuilder()
          .setExtension(
              DialogueProto.DialogueCommand.dialogueCommand,
              DialogueProto.DialogueCommand.getDefaultInstance())
          .build();
  private static final DialogueMap DIALOGUE_MAP =
      DialogueMap.newBuilder().putDialogues(VALID_DIALOGUE_NAME, DIALOGUE).build();
  private static final Command PARSED_COMMAND = DialogueCommandParserTest::doNothing;

  private final ObservableValue<Optional<DialogueData>> observableDialogue =
      ObservableValue.of(Optional.empty());
  private final DialogueCommandParser dialogueCommandParser =
      new DialogueCommandParser(
          () -> DIALOGUE_MAP,
          new DialogueCommandFactory(() -> observableDialogue),
          () -> commandData -> PARSED_COMMAND);

  @Test
  void parseCommand_returnsValidCommand() {
    Command command =
        dialogueCommandParser.parseCommand(
            DialogueProto.DialogueCommand.newBuilder()
                .setDialogueName(VALID_DIALOGUE_NAME)
                .setFollowupCommand(FOLLOWUP_COMMANDS)
                .build());

    command.execute();
    Truth8.assertThat(observableDialogue.get())
        .hasValue(DialogueData.newBuilder(DIALOGUE).setFollowupCommand(PARSED_COMMAND).build());
  }

  @Test
  void parseCommand_followupCommandIsEmpty_dialogueDataContainsEmptyFollowupCommand() {
    Command command =
        dialogueCommandParser.parseCommand(
            DialogueProto.DialogueCommand.newBuilder()
                .setDialogueName(VALID_DIALOGUE_NAME)
                .build());

    command.execute();
    Truth8.assertThat(observableDialogue.get()).hasValue(DialogueData.newBuilder(DIALOGUE).build());
  }

  @Test
  @VerifiesLoggingCalls
  void parseCommand_dialogueDoesNotExist_returnsEmptyAndLogsError(VerifiableLogSystem logSystem) {
    assertThat(
            dialogueCommandParser.parseCommand(
                DialogueProto.DialogueCommand.newBuilder().setDialogueName("bogus_name").build()))
        .isNull();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*bogus_name.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  private static void doNothing() {}
}
