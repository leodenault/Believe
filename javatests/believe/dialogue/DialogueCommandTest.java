package believe.dialogue;

import static com.google.common.truth.Truth8.assertThat;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.react.ObservableValue;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link DialogueCommand}. */
final class DialogueCommandTest {
  private static final Dialogue DIALOGUE = Dialogue.getDefaultInstance();

  private final ObservableValue<Optional<DialogueData>> observableDialogue =
      ObservableValue.of(Optional.empty());
  private final DialogueCommand dialogueCommand =
      new DialogueCommand(observableDialogue, DialogueData.newBuilder(DIALOGUE).build());

  @Test
  void handleCollision_updatesDialogue() {
    dialogueCommand.execute();

    Truth.assertThat(observableDialogue.get().get().dialogue()).isEqualTo(DIALOGUE);
  }

  @Test
  void handleCollision_commandPreviouslyTriggered_doesNotCallDialogueListener() {
    dialogueCommand.execute();
    observableDialogue.setValue(Optional.empty());
    dialogueCommand.execute();

    assertThat(observableDialogue.get()).isEmpty();
  }
}
