package believe.dialogue;

import believe.character.playable.PlayableCharacter;
import believe.dialogue.InternalQualifiers.ModulePrivate;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.map.collidable.command.Command;
import believe.map.collidable.command.CommandCollisionHandler;
import believe.react.ObservableValue;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
final class DialogueCommandCollisionHandler
    implements CommandCollisionHandler<PlayableCharacter, Dialogue> {
  private final ObservableValue<Optional<Dialogue>> mutableDialogue;
  private final Set<Command<PlayableCharacter, Dialogue>> previouslyTriggeredCommands;

  @Inject
  DialogueCommandCollisionHandler(
      @ModulePrivate ObservableValue<Optional<Dialogue>> mutableDialogue) {
    this.mutableDialogue = mutableDialogue;
    this.previouslyTriggeredCommands = new HashSet<>();
  }

  @Override
  public void handleCollision(
      Command<PlayableCharacter, Dialogue> command, PlayableCharacter playableCharacter) {
    if (previouslyTriggeredCommands.contains(command)) {
      return;
    }
    previouslyTriggeredCommands.add(command);
    mutableDialogue.setValue(command.data());
  }
}
