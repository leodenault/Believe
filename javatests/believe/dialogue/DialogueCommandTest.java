package believe.dialogue;

import static com.google.common.truth.Truth8.assertThat;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.react.ObservableValue;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link DialogueCommand}. */
final class DialogueCommandTest {
  private static final Dialogue DIALOGUE = Dialogue.getDefaultInstance();

  private final ObservableValue<Optional<Dialogue>> observableDialogue =
      new ObservableValue<>(Optional.empty());
  private final DialogueCommand dialogueCommand = new DialogueCommand(observableDialogue, DIALOGUE);

  @Test
  void handleCollision_updatesDialogue() {
    dialogueCommand.execute();

    assertThat(observableDialogue.get()).hasValue(DIALOGUE);
  }

  @Test
  void handleCollision_commandPreviouslyTriggered_doesNotCallDialogueListener() {
    dialogueCommand.execute();
    observableDialogue.setValue(Optional.empty());
    dialogueCommand.execute();

    assertThat(observableDialogue.get()).isEmpty();
  }
}
