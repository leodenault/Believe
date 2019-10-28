package believe.dialogue;

import believe.command.Command;
import believe.dialogue.InternalQualifiers.ModulePrivate;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.react.ObservableValue;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import java.util.Optional;

@AutoFactory
final class DialogueCommand implements Command {
  private final ObservableValue<Optional<DialogueData>> mutableDialogue;
  private final DialogueData dialogueData;

  private boolean hasTriggered;

  DialogueCommand(
      @Provided @ModulePrivate ObservableValue<Optional<DialogueData>> mutableDialogue,
      DialogueData dialogueData) {
    this.mutableDialogue = mutableDialogue;
    this.dialogueData = dialogueData;
    this.hasTriggered = false;
  }

  @Override
  public void execute() {
    if (hasTriggered) {
      return;
    }
    hasTriggered = true;
    mutableDialogue.setValue(Optional.of(dialogueData));
  }
}
