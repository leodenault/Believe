package believe.command

/** An executable block that has effects on components within the game. */
interface Command {
    /** Executes the command. */
    fun execute()

    companion object {
        /** A [Command] that does nothing when executed. */
        val EMPTY = object : Command {
            override fun execute() {}
        }
    }
}