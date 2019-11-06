package believe.command;

import believe.command.proto.CommandSequenceProto.CommandSequence;

import java.util.Optional;

/** Parses a string sequence of commands into a {@link Command} object. */
public interface CommandSequenceParser {
  /**
   * Parses {@code sequence} into a {@link Command}.
   *
   * <p>It is expected that {@code sequence} is in the format of {@link
   * believe.command.proto.CommandSequenceProto.CommandSequence}.
   */
  Optional<Command> parseSequence(String sequence);

  /** Parses a {@code commandSequence} into a {@link Command}. */
  Command parseSequence(CommandSequence commandSequence);
}
