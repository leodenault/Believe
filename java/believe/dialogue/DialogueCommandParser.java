package believe.dialogue;

import believe.command.Command;
import believe.command.CommandGenerator;
import believe.command.CommandParser;
import believe.dialogue.proto.DialogueProto;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import dagger.Lazy;
import dagger.Reusable;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.function.Supplier;

/** Manages fetching dialogue and loading it from disk. */
@Reusable
public final class DialogueCommandParser implements CommandParser<DialogueProto.DialogueCommand> {
  private final Supplier<DialogueMap> dialogueMap;
  private final DialogueCommandFactory dialogueCommandFactory;
  private final Lazy<CommandGenerator> commandGenerator;

  @Inject
  DialogueCommandParser(
      Supplier<DialogueMap> dialogueMap,
      DialogueCommandFactory dialogueCommandFactory,
      Lazy<CommandGenerator> commandGenerator) {
    this.dialogueMap = dialogueMap;
    this.dialogueCommandFactory = dialogueCommandFactory;
    this.commandGenerator = commandGenerator;
  }

  @Override
  public int getExtensionNumber() {
    return DialogueProto.DialogueCommand.dialogueCommand.getNumber();
  }

  @Override
  @Nullable
  public Command parseCommand(DialogueProto.DialogueCommand command) {
    Dialogue dialogue = dialogueMap.get().getDialoguesMap().get(command.getDialogueName());
    if (dialogue == null) {
      Log.error("Could not find dialogue with name '" + command.getDialogueName() + "'.");
      return null;
    }

    DialogueData.Builder dialogueData = DialogueData.newBuilder(dialogue);
    if (command.hasFollowupCommand()) {
      Command followupCommand =
          commandGenerator.get().generateCommand(command.getFollowupCommand());

      if (followupCommand != null) {
        dialogueData.setFollowupCommand(followupCommand);
      }
    }

    return dialogueCommandFactory.create(dialogueData.build());
  }
}
