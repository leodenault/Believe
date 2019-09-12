package believe.dialogue;

import static com.google.common.truth.Truth8.assertThat;

import believe.character.playable.PlayableCharacter;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.dialogue.proto.DialogueProto.Response;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.map.collidable.command.Command;
import believe.map.tiled.EntityType;
import believe.map.tiled.testing.FakeTiledMap;
import believe.map.tiled.testing.FakeTiledObjectFactory;
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
  private static final DialogueCommandCollisionHandler DIALOGUE_COMMAND_COLLISION_HANDLER =
      new DialogueCommandCollisionHandler(new ObservableValue<>(Optional.empty()));

  private final DialogueSupplier dialogueSupplier =
      new DialogueSupplier(
          () -> DIALOGUE_MAP, DIALOGUE_COMMAND_COLLISION_HANDLER, DIALOGUE_ID_PROPERTY);

  @Test
  void supplyCommand_returnsCommandForCorrespondingTiledObject() {
    Command<PlayableCharacter, Dialogue> command =
        dialogueSupplier.supplyCommand(
            FakeTiledObjectFactory.create(
                FakeTiledMap.tiledMapWithObjectPropertyValue(DIALOGUE_NAME), EntityType.COMMAND));

    assertThat(command.data()).hasValue(DIALOGUE);
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_dialogueNameNotSpecified_returnsCommandWithoutDialogueAndLogsError(
      VerifiableLogSystem logSystem) {
    Command<PlayableCharacter, Dialogue> command =
        dialogueSupplier.supplyCommand(FakeTiledObjectFactory.create(EntityType.COMMAND));

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Dialogue name missing.*")
        .hasSeverity(LogSeverity.ERROR);
    assertThat(command.data()).isEmpty();
  }

  @Test
  @VerifiesLoggingCalls
  void supplyCommand_dialogueDoesNotExist_returnsCommandWithoutDialogueAndLogsError(
      VerifiableLogSystem logSystem) {
    Command<PlayableCharacter, Dialogue> command =
        dialogueSupplier.supplyCommand(
            FakeTiledObjectFactory.create(
                FakeTiledMap.tiledMapWithObjectPropertyValue("bogus_name"), EntityType.COMMAND));

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find dialogue.*bogus_name.*")
        .hasSeverity(LogSeverity.ERROR);
    assertThat(command.data()).isEmpty();
  }
}
