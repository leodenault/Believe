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
  private final ObservableValue<Optional<Dialogue>> mutableDialogue;
  private final Dialogue dialogue;

  private boolean hasTriggered;

  DialogueCommand(
      @Provided @ModulePrivate ObservableValue<Optional<Dialogue>> mutableDialogue,
      Dialogue dialogue) {
    this.mutableDialogue = mutableDialogue;
    this.dialogue = dialogue;
    this.hasTriggered = false;
  }

  @Override
  public void execute() {
    if (hasTriggered) {
      return;
    }
    hasTriggered = true;
    mutableDialogue.setValue(Optional.of(dialogue));
  }
}
