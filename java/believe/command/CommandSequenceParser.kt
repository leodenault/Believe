package believe.command

import believe.command.proto.CommandProto
import dagger.Lazy
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CommandSequenceParser @Inject internal constructor(
    private val lazyCommandGenerator: Lazy<CommandGenerator>
) : CommandParser<CommandProto.CommandSequence> {
    override val extensionNumber = CommandProto.CommandSequence.commandSequence.number

    override fun parseCommand(command: CommandProto.CommandSequence): Command? {
        val commandGenerator = lazyCommandGenerator.get()
        val subCommands = command.commandsList.mapNotNull { subcommand: CommandProto.Command ->
            commandGenerator.generateCommand(subcommand)
        }
        return object : Command {
            override fun execute() {
                for (subCommand in subCommands) {
                    subCommand.execute()
                }
            }
        }
    }
}