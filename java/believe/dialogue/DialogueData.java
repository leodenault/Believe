package believe.dialogue;

import believe.command.Command;
import believe.dialogue.proto.DialogueProto.Dialogue;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/** Data about a dialogue, including subsequent commands. */
@AutoValue
public abstract class DialogueData {
  /** The {@link Dialogue} containing details about the strings that should be displayed. */
  public abstract Dialogue dialogue();

  /** The {@link Command} to be executed after the dialogue is dismissed. */
  @Nullable
  public abstract Command followupCommand();

  static Builder newBuilder(Dialogue dialogue) {
    return new AutoValue_DialogueData.Builder().setDialogue(dialogue);
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setDialogue(Dialogue dialogue);

    abstract Builder setFollowupCommand(Command command);

    abstract DialogueData build();
  }
}
