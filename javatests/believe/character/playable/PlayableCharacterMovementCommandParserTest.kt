package believe.character.playable

import believe.character.playable.proto.PlayableCharacterMovementCommandProto
import believe.character.playable.proto.PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand.Type
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.nhaarman.mockitokotlin2.mock
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Supplier

internal class PlayableCharacterMovementCommandParserTest {
    val commandFactory: PlayableCharacterMovementCommandFactory = mock {
        on { create(Type.SELECT_LEFT) } doReturn COMMAND
    }
    val commandParser = PlayableCharacterMovementCommandParser(commandFactory)

    @Test
    @VerifiesLoggingCalls
    internal fun parseCommand_actionIsUnknown_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(commandParser.parseCommand(createCommand(Type.UNKNOWN_COMMAND))).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Playable character movement command specified unknown action. No command will be output.")
            .hasSeverity(LogSeverity.ERROR)
    }

    @Test
    internal fun parseCommand_actionIsKnown_returnsValidCommand() {
        assertThat(commandParser.parseCommand(createCommand(Type.SELECT_LEFT))).isEqualTo(COMMAND)
    }

    companion object {
        val COMMAND = PlayableCharacterMovementCommand(
            Supplier { Optional.empty<PlayableCharacter>() }, Type.SELECT_LEFT
        )

        private fun createCommand(
            action: Type
        ): PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand {
            return PlayableCharacterMovementCommandProto.PlayableCharacterMovementCommand.newBuilder()
                .setAction(action).build()
        }
    }
}