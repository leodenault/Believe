package believe.character.playable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.character.playable.proto.PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand.Type;
import org.junit.jupiter.api.Test;

import java.util.Optional;

final class PlayableCharacterMovementCommandTest {
  private PlayableCharacterMovementCommand command;

  @Test
  void execute_movesCharacter() {
    PlayableCharacter playableCharacter = mock(PlayableCharacter.class);
    command =
        new PlayableCharacterMovementCommand(
            () -> Optional.of(playableCharacter), Type.SELECT_LEFT);

    command.execute();

    verify(playableCharacter).transition(Type.SELECT_LEFT);
  }

  @Test
  @VerifiesLoggingCalls
  void execute_characterIsEmpty_logsWarningAndDoesNothing(VerifiableLogSystem logSystem) {
    command = new PlayableCharacterMovementCommand(Optional::empty, Type.JUMP);

    command.execute();

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasSeverity(LogSeverity.WARNING)
        .hasPattern("Playable character is empty.*");
  }
}
