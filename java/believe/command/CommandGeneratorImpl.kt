package believe.command

import believe.command.proto.CommandProto
import com.google.protobuf.Message
import org.newdawn.slick.util.Log
import javax.inject.Inject

internal class CommandGeneratorImpl @Inject constructor(
    extensionParsers: Set<@JvmSuppressWildcards CommandParser<*>>
) : CommandGenerator {
    private val extensionParserMap: Map<Int, CommandParser<*>> =
        extensionParsers.associateBy({ it.extensionNumber }, { it })

    override fun generateCommand(commandData: CommandProto.Command): Command? {
        val commandExtension = commandData.allFields.entries.singleOrNull { it.key.isExtension }
        if (commandExtension == null) {
            Log.error("Expected exactly 1 extension to be present in command.")
            return null
        }

        val commandParser = extensionParserMap[commandExtension.key.number]
        return if (commandParser == null) {
            Log.error(
                "No command parser instance was registered for command extension of type '${commandExtension.key.messageType.fullName}'."
            )
            null
        } else parseCommand(commandParser, commandExtension.value)

    }

    private fun <M : Message> parseCommand(
        commandParser: CommandParser<M>, command: Any
    ): Command? {
        // Safe cast: The code executing the method containing this statement fetches the command
        // parser using the extension number. The only way for the class cast to fail is if the
        // command parser's key does not correspond to the correct extension number of the command
        // proto being parsed. If that happens, then we definitely want this cast to crash.
        @Suppress("UNCHECKED_CAST") return commandParser.parseCommand(command as M)
    }
}