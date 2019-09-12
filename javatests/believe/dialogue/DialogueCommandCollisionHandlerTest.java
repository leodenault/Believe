package believe.dialogue;

import static com.google.common.truth.Truth8.assertThat;

import believe.character.playable.PlayableCharacter;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.map.collidable.command.Command;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;
import believe.map.tiled.testing.FakeTiledObjectFactory;
import believe.react.ObservableValue;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

/** Unit tests for {@link DialogueCommandCollisionHandler}. */
@InstantiateMocksIn
final class DialogueCommandCollisionHandlerTest {
  private static final TiledObject TILED_OBJECT = FakeTiledObjectFactory.create(EntityType.COMMAND);
  private static final Dialogue DIALOGUE = Dialogue.getDefaultInstance();

  private final ObservableValue<Optional<Dialogue>> observableDialogue =
      new ObservableValue<>(Optional.empty());
  private final DialogueCommandCollisionHandler dialogueCommandCollisionHandler =
      new DialogueCommandCollisionHandler(observableDialogue);

  @Mock private PlayableCharacter playableCharacter;

  @Test
  void handleCollision_updatesDialogue() {
    dialogueCommandCollisionHandler.handleCollision(
        Command.create(dialogueCommandCollisionHandler, TILED_OBJECT, DIALOGUE), playableCharacter);

    assertThat(observableDialogue.get()).hasValue(DIALOGUE);
  }

  @Test
  void handleCollision_dataDoesNotExist_updatesDialogueToEmpty() {
    observableDialogue.setValue(Optional.of(DIALOGUE));
    dialogueCommandCollisionHandler.handleCollision(
        Command.create(dialogueCommandCollisionHandler, TILED_OBJECT), playableCharacter);

    assertThat(observableDialogue.get()).isEmpty();
  }

  @Test
  void handleCollision_commandPreviouslyCollided_doesNotCallDialogueListener() {
    Command<PlayableCharacter, Dialogue> command =
        Command.create(dialogueCommandCollisionHandler, TILED_OBJECT, DIALOGUE);

    dialogueCommandCollisionHandler.handleCollision(command, playableCharacter);
    observableDialogue.setValue(Optional.empty());
    dialogueCommandCollisionHandler.handleCollision(command, playableCharacter);

    assertThat(observableDialogue.get()).isEmpty();
  }
}
