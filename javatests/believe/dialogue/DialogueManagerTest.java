package believe.dialogue;

import static com.google.common.truth.Truth.assertThat;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystemExtension;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.temporaryfolder.TemporaryFolder;
import believe.testing.temporaryfolder.UsesTemporaryFolder;
import com.google.common.truth.Truth8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public final class DialogueManagerTest {
  private static final String DIALOGUE_NAME = "rainbow";
  private static final Dialogue
      DIALOGUE =
      Dialogue
          .newBuilder()
          .addResponses(Response
              .newBuilder()
              .setPortraitLocation("/somewhere/over/the/rainbow.jpg")
              .setResponseText("Look at me I'm a rainbow! :D"))
          .build();
  private static final DialogueMap
      DIALOGUE_MAP =
      DialogueMap.newBuilder().putDialogues(DIALOGUE_NAME, DIALOGUE).build();

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void loadDialogueMap_mapExists_correctlyLoads(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    DIALOGUE_MAP.writeTo(new FileOutputStream(temporaryFolder.location() + "/some_mapping.pb"));
    DialogueManager manager = new DialogueManager(temporaryFolder.location());

    manager.loadDialogueMap("some_mapping");

    VerifiableLogSystemSubject
        .assertThat(logSystem)
        .neverLoggedMessageThat(loggedMessage -> loggedMessage.hasPattern("Could not find.*"));
  }

  @Test
  @UsesTemporaryFolder
  public void loadDialogueMap_mapDoesNotExist_throwsIoException(TemporaryFolder temporaryFolder) {
    DialogueManager manager = new DialogueManager(temporaryFolder.location());

    Assertions.assertThrows(IOException.class, () -> manager.loadDialogueMap("non-existent map"));
  }

  @Test
  @UsesTemporaryFolder
  public void getDialogue_returnsDialogueForGivenName(TemporaryFolder temporaryFolder)
      throws IOException {
    DIALOGUE_MAP.writeTo(new FileOutputStream(temporaryFolder.location() + "/some_mapping.pb"));
    DialogueManager manager = new DialogueManager(temporaryFolder.location());

    Optional<Dialogue> dialogue = manager.getDialogue("some_mapping", DIALOGUE_NAME);

    Truth8.assertThat(dialogue).isPresent();
    assertThat(dialogue.get()).isEqualTo(DIALOGUE);
  }

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void getDialogue_dialogueMapDoesNotExist_logsErrorAndReturnsEmpty(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) {
    DialogueManager manager = new DialogueManager(temporaryFolder.location());

    Optional<Dialogue> dialogue = manager.getDialogue("non-existent map", DIALOGUE_NAME);

    VerifiableLogSystemSubject
        .assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue map.*");
    Truth8.assertThat(dialogue).isEmpty();
  }

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void getDialogue_dialogueDoesNotExist_logsErrorAndReturnsEmpty(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    DialogueMap
        .getDefaultInstance()
        .writeTo(new FileOutputStream(temporaryFolder.location() + "/some_mapping.pb"));
    DialogueManager manager = new DialogueManager(temporaryFolder.location());

    Optional<Dialogue> dialogue = manager.getDialogue("some_mapping", "bogus name");

    VerifiableLogSystemSubject
        .assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*");
    Truth8.assertThat(dialogue).isEmpty();
  }

}
