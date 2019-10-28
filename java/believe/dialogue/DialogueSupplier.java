package believe.dialogue;

import believe.command.Command;
import believe.command.CommandSequenceParser;
import believe.command.CommandSupplier;
import believe.core.PropertyProvider;
import believe.dialogue.InternalQualifiers.DialogueNameProperty;
import believe.dialogue.InternalQualifiers.FollowupCommandsProperty;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import dagger.Reusable;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

import java.util.Optional;
import java.util.function.Supplier;

/** Manages fetching dialogue and loading it from disk. */
@Reusable
public final class DialogueSupplier implements CommandSupplier {
  private final Supplier<DialogueMap> dialogueMap;
  private final DialogueCommandFactory dialogueCommandFactory;
  private final String dialogueNameProperty;
  private final String followupCommandsProperty;
  private final CommandSequenceParser commandSequenceParser;

  @Inject
  DialogueSupplier(
      Supplier<DialogueMap> dialogueMap,
      DialogueCommandFactory dialogueCommandFactory,
      @DialogueNameProperty String dialogueNameProperty,
      @FollowupCommandsProperty String followupCommandsProperty,
      CommandSequenceParser commandSequenceParser) {
    this.dialogueMap = dialogueMap;
    this.dialogueCommandFactory = dialogueCommandFactory;
    this.dialogueNameProperty = dialogueNameProperty;
    this.followupCommandsProperty = followupCommandsProperty;
    this.commandSequenceParser = commandSequenceParser;
  }

  @Override
  public Optional<Command> supplyCommand(PropertyProvider propertyProvider) {
    Optional<String> dialogueName = propertyProvider.getProperty(dialogueNameProperty);
    if (!dialogueName.isPresent()) {
      Log.error("Dialogue name missing in Tiled map object.");
      return Optional.empty();
    }

    Dialogue dialogue = dialogueMap.get().getDialoguesMap().get(dialogueName.get());
    if (dialogue == null) {
      Log.error("Could not find dialogue with name '" + dialogueName.get() + "'.");
      return Optional.empty();
    }

    DialogueData.Builder dialogueData = DialogueData.newBuilder(dialogue);

    Optional<String> followupCommandText = propertyProvider.getProperty(followupCommandsProperty);
    if (followupCommandText.isPresent()) {
      Optional<Command> followupCommand =
          commandSequenceParser.parseSequence(followupCommandText.get());
      followupCommand.ifPresent(dialogueData::setFollowupCommand);
    }

    return Optional.of(dialogueCommandFactory.create(dialogueData.build()));
  }
}
