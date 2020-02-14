package believe.command

import believe.command.proto.CommandProto
import believe.command.proto.CommandProto.CommandSequence
import com.google.common.truth.Truth.assertThat
import dagger.Lazy
import org.junit.jupiter.api.Test

/** Unit tests for [CommandSequenceParser].  */
internal class CommandSequenceParserTest {
    private val commandGenerator = FakeCommandGenerator()
    private val parser = CommandSequenceParser(Lazy { commandGenerator })

    @Test
    internal fun parseCommand_returnsCommandExecutingAllSubcommands() {
        val subcommand1 = FakeCommand()
        val subcommand2 = FakeCommand()
        with(commandGenerator) {
            addReturnValue(subcommand1)
            addReturnValue(subcommand2)
        }

        val command = parser.parseCommand(COMMAND_SEQUENCE_PROTO)
        command?.execute()

        assertThat(subcommand1.hasExecuted).isTrue()
        assertThat(subcommand2.hasExecuted).isTrue()
    }

    companion object {
        private val COMMAND_SEQUENCE_PROTO: CommandSequence =
            CommandSequence.newBuilder().addCommands(
                CommandProto.Command.getDefaultInstance()
            ).addCommands(
                CommandProto.Command.getDefaultInstance()
            ).build()
    }

    private class FakeCommand internal constructor() : Command {
        var hasExecuted = false
        override fun execute() {
            hasExecuted = true
        }
    }

    private class FakeCommandGenerator : CommandGenerator {
        private val returnValues: MutableList<FakeCommand> = ArrayList()
        private var it: Iterator<FakeCommand> = returnValues.iterator()

        fun addReturnValue(command: FakeCommand) {
            returnValues.add(command)
            it = returnValues.iterator()
        }

        override fun generateCommand(commandData: CommandProto.Command): Command? =
            it.takeIf { it.hasNext() }?.next()
    }
}
