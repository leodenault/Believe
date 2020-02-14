package believe.command

import believe.command.proto.CommandProto
import com.google.protobuf.Message

/** Parses [CommandProto.Command] into a [Command]. */
interface CommandParser<M : Message> {
    /**
     * The number of the command extension parsed by this instance.
     *
     * Traditionally this would be implemented through a Dagger map multibinding which required
     * statically declaring the key at the binding location. Since proto extension numbers cannot be
     * retrieved statically, we're forced to resort to this solution.
     */
    val extensionNumber: Int

    /** Returns a [Command] based on the contents of [command], or null on failure. */
    fun parseCommand(command: M): Command?
}