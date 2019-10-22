package believe.dialogue;

import static com.google.common.truth.Truth8.assertThat;

import believe.command.Command;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.react.ObservableValue;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link DialogueSupplier}. */
final class DialogueSupplierTest {
  private static final String DIALOGUE_ID_PROPERTY = "dialogue_id";
  private static final String DIALOGUE_NAME = "rainbow";
  private static final Dialogue DIALOGUE =
      Dialogue.newBuilder()
          .addResponses(
              Response.newBuilder()
                  .setPortraitLocation("/somewhere/over/the/rainbow.jpg")
                  .setResponseText("Look at me I'm a rainbow! :D"))
          .build();
  private static final DialogueMap DIALOGUE_MAP =
      DialogueMap.newBuilder().putDialogues(DIALOGUE_NAME, DIALOGUE).build();

  private final ObservableValue<Optional<Dialogue>> observableDialogue =
      new ObservableValue<>(Optional.empty());
  private final DialogueSupplier dialogueSupplier =
      new DialogueSupplier(
          () -> DIALOGUE_MAP,
          new DialogueCommandFactory(() -> observableDialogue),
          DIALOGUE_ID_PROPERTY);

  @Test
  void supplyCommand_returnsCommandForCorrespondingProperties() {
    Optional<Command> command = dialogueSupplier.supplyCommand(key -> Optional.of(DIALOGUE_NAME));

    assertThat(command).isPresent();
    command.get().execute();
    assertThat(observableDialogue.get()).hasValue(DIALOGUE);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_dialogueNameNotSpecified_returnsEmptyAndLogsError(
      VerifiableLogSystem logSystem) {
    assertThat(dialogueSupplier.supplyCommand(key -> Optional.empty())).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Dialogue name missing.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_dialogueDoesNotExist_returnsEmptyAndLogsError(VerifiableLogSystem logSystem) {
    assertThat(dialogueSupplier.supplyCommand(key -> Optional.of("bogus_name"))).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*bogus_name.*")
        .hasSeverity(LogSeverity.ERROR);
  }
}
