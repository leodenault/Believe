package believe.command

import believe.command.proto.CommandProto

/** Generates commands based on the contents of a [CommandProto.Command] proto. */
interface CommandGenerator {
    /** Returns a [Command] instance based on the contents of [commandData]. */
    fun generateCommand(commandData: CommandProto.Command): Command?
}