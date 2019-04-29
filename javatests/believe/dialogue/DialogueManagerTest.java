package believe.dialogue;

import static com.google.common.truth.Truth.assertThat;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiableLogSystemExtension;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.temporaryfolder.TemporaryFolder;
import believe.testing.temporaryfolder.UsesTemporaryFolder;
import com.google.common.truth.Truth8;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Optional;

public final class DialogueManagerTest {
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

  @Test
  @VerifiesLoggingCalls
  public void loadDialogueMap_directoryDoesNotExist_logsError(VerifiableLogSystem logSystem) {
    DialogueManager manager = new DialogueManager("nonexistent_directory");

    manager.loadDialogueMap("doesn't matter");

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("The directory '.*nonexistent_directory' does not exist.")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @UsesTemporaryFolder
  @VerifiesLoggingCalls
  public void loadDialogueMap_mapExists_correctlyLoads(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    DIALOGUE_MAP.writeTo(temporaryFolder.writeToFile("some_mapping.pb"));
    DialogueManager manager = new DialogueManager(temporaryFolder.getFolder().getCanonicalPath());

    manager.loadDialogueMap("some_mapping");

    VerifiableLogSystemSubject.assertThat(logSystem)
        .neverLoggedMessageThat(loggedMessage -> loggedMessage.hasPattern("Could not find.*"));
  }

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void loadDialogueMap_fileIsNotAFolder_logsError(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    temporaryFolder.writeToFile("some_mapping.pb").close();
    DialogueManager manager =
        new DialogueManager(temporaryFolder.getFile("some_mapping.pb").getCanonicalPath());

    manager.loadDialogueMap("irrelevant_file.pb");

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern(".*directory is actually a file.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void loadDialogueMap_mapDoesNotExist_logsError(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    temporaryFolder.writeToFile("some_mapping.txt").close();
    DialogueManager manager = new DialogueManager(temporaryFolder.getFolder().getCanonicalPath());

    manager.loadDialogueMap("some_mapping");

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("some_mapping.pb does not exist.")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @UsesTemporaryFolder
  public void getDialogue_returnsDialogueForGivenName(TemporaryFolder temporaryFolder)
      throws IOException {
    DIALOGUE_MAP.writeTo(temporaryFolder.writeToFile("some_mapping.pb"));
    DialogueManager manager = new DialogueManager(temporaryFolder.getFolder().getCanonicalPath());

    Optional<Dialogue> dialogue = manager.getDialogue("some_mapping", DIALOGUE_NAME);

    Truth8.assertThat(dialogue).isPresent();
    assertThat(dialogue.get()).isEqualTo(DIALOGUE);
  }

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void getDialogue_dialogueMapDoesNotExist_logsErrorAndReturnsEmpty(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    DialogueManager manager = new DialogueManager(temporaryFolder.getFolder().getCanonicalPath());

    Optional<Dialogue> dialogue = manager.getDialogue("non-existent map", DIALOGUE_NAME);

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("non-existent map.pb does not exist.");
    Truth8.assertThat(dialogue).isEmpty();
  }

  @Test
  @UsesTemporaryFolder
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void getDialogue_dialogueDoesNotExist_logsErrorAndReturnsEmpty(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    DialogueMap.getDefaultInstance().writeTo(temporaryFolder.writeToFile("some_mapping.pb"));
    DialogueManager manager = new DialogueManager(temporaryFolder.getFolder().getCanonicalPath());

    Optional<Dialogue> dialogue = manager.getDialogue("some_mapping", "bogus name");

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*");
    Truth8.assertThat(dialogue).isEmpty();
  }
}
