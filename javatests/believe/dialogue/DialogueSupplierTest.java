package believe.dialogue;

import static com.google.common.truth.Truth.assertThat;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystemExtension;
import believe.logging.truth.VerifiableLogSystemSubject;
import com.google.common.truth.Truth8;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Unit tests for {@link DialogueSupplier}. */
public final class DialogueSupplierTest {
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

  private final DialogueSupplier dialogueSupplier = new DialogueSupplier(() -> DIALOGUE_MAP);

  @Test
  public void getDialogue_returnsDialogueForGivenName() {

    Optional<Dialogue> dialogue = dialogueSupplier.getDialogue(DIALOGUE_NAME);

    Truth8.assertThat(dialogue).isPresent();
    assertThat(dialogue.get()).isEqualTo(DIALOGUE);
  }

  @Test
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void getDialogue_dialogueDoesNotExist_logsErrorAndReturnsEmpty(
      VerifiableLogSystem logSystem) {

    Optional<Dialogue> dialogue = dialogueSupplier.getDialogue("bogus_name");

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*bogus_name.*");
    Truth8.assertThat(dialogue).isEmpty();
  }
}
