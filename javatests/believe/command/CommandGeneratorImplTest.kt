package believe.command

import believe.command.proto.CommandProto
import believe.command.testing.proto.FakeCommandProto
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CommandGeneratorImplTest {
    private var createExtensionParserSet: () -> Set<CommandParser<*>> = {
        setOf(COMMAND_PARSER_1, COMMAND_PARSER_2)
    }
    private val commandGeneratorImpl: CommandGeneratorImpl by lazy {
        CommandGeneratorImpl(createExtensionParserSet())
    }

    @Test
    @VerifiesLoggingCalls
    fun generateCommand_moreThanOneExtensionExists_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(commandGeneratorImpl.generateCommand(createCommand {
            setFakeCommandExtension("some data")
            setFakeCommand2Extension("some other data")
        })).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Expected exactly 1 extension to be present in command.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun generateCommand_noExtensionsExist_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(
            commandGeneratorImpl.generateCommand(CommandProto.Command.getDefaultInstance())
        ).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Expected exactly 1 extension to be present in command.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun generateCommand_correspondingCommandParserDoesNotExist_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        createExtensionParserSet = {
            setOf()
        }

        assertThat(commandGeneratorImpl.generateCommand(createCommand {
            setFakeCommandExtension("some data")
        })).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("No command parser instance was registered for command extension of type '${FakeCommandProto.FakeCommand.getDescriptor().fullName}'.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    fun generateCommand_returnsCommandGeneratedByCommandParser() {
        assertThat(
            commandGeneratorImpl.generateCommand(createCommand {
                setFakeCommandExtension("some data")
            })
        ).isSameAs(COMMAND)
    }

    companion object {
        private val COMMAND = object : Command {
            override fun execute() {}
        }
        private val COMMAND_PARSER_1 = object : CommandParser<FakeCommandProto.FakeCommand> {
            override val extensionNumber = FakeCommandProto.FakeCommand.fakeCommand.number
            override fun parseCommand(command: FakeCommandProto.FakeCommand): Command? = COMMAND
        }
        private val COMMAND_PARSER_2 = object : CommandParser<FakeCommandProto.FakeCommand2> {
            override val extensionNumber = FakeCommandProto.FakeCommand2.fakeCommand2.number
            override fun parseCommand(command: FakeCommandProto.FakeCommand2): Command? = COMMAND
        }

        private inline fun createCommand(
            init: CommandProto.Command.Builder.() -> Unit
        ): CommandProto.Command {
            return CommandProto.Command.newBuilder().apply(init).build()
        }

        private fun CommandProto.Command.Builder.setFakeCommandExtension(
            fakeData: String
        ): CommandProto.Command.Builder {
            return setExtension(
                FakeCommandProto.FakeCommand.fakeCommand,
                FakeCommandProto.FakeCommand.newBuilder().setData(fakeData).build()
            )
        }

        private fun CommandProto.Command.Builder.setFakeCommand2Extension(
            fakeData: String
        ): CommandProto.Command.Builder {
            return setExtension(
                FakeCommandProto.FakeCommand2.fakeCommand2,
                FakeCommandProto.FakeCommand2.newBuilder().setData(fakeData).build()
            )
        }
    }
}