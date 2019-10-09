package believe.command;

/** An executable block that has effects on components within the game. */
public interface Command {
  /** Executes the command. */
  void execute();
}
