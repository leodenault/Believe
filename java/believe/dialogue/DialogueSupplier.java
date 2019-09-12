package believe.dialogue;

import believe.character.playable.PlayableCharacter;
import believe.dialogue.InternalQualifiers.DialogueNameProperty;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.map.collidable.command.Command;
import believe.map.collidable.command.CommandSupplier;
import believe.map.tiled.TiledObject;
import dagger.Reusable;
import java.util.Optional;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

/** Manages fetching dialogue and loading it from disk. */
@Reusable
public final class DialogueSupplier implements CommandSupplier<PlayableCharacter, Dialogue> {
  private final Supplier<DialogueMap> dialogueMap;
  private final DialogueCommandCollisionHandler dialogueCommandCollisionHandler;
  private final String dialogueNameProperty;

  @Inject
  DialogueSupplier(
      Supplier<DialogueMap> dialogueMap,
      DialogueCommandCollisionHandler dialogueCommandCollisionHandler,
      @DialogueNameProperty String dialogueNameProperty) {
    this.dialogueMap = dialogueMap;
    this.dialogueCommandCollisionHandler = dialogueCommandCollisionHandler;
    this.dialogueNameProperty = dialogueNameProperty;
  }

  @Override
  public Command<PlayableCharacter, Dialogue> supplyCommand(TiledObject tiledObject) {
    Optional<String> dialogueName = tiledObject.getProperty(dialogueNameProperty);
    if (!dialogueName.isPresent()) {
      Log.error("Dialogue name missing in Tiled map object.");
      return Command.create(dialogueCommandCollisionHandler, tiledObject);
    }

    Dialogue dialogue = dialogueMap.get().getDialoguesMap().get(dialogueName.get());
    if (dialogue == null) {
      Log.error("Could not find dialogue with name '" + dialogueName.get() + "'.");
      return Command.create(dialogueCommandCollisionHandler, tiledObject);
    }

    return Command.create(dialogueCommandCollisionHandler, tiledObject, dialogue);
  }
}
