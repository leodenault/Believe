package believe.dialogue;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import dagger.Reusable;
import java.util.Optional;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.newdawn.slick.util.Log;

/** Manages fetching dialogue and loading it from disk. */
@Reusable
public final class DialogueSupplier {
  private final Supplier<DialogueMap> dialogueMap;

  @Inject
  DialogueSupplier(Supplier<DialogueMap> dialogueMap) {
    this.dialogueMap = dialogueMap;
  }

  /**
   * Fetches a specific {@link Dialogue} using {@code name} for identification.
   *
   * @param name the name of the {@link Dialogue} instance to be fetched.
   * @return an {@link Optional} {@link Dialogue} if a {@link Dialogue} with name {@code name}
   *     exists. Otherwise, {@link Optional#empty()}.
   */
  public Optional<Dialogue> getDialogue(String name) {
    Optional<Dialogue> dialogue =
        Optional.ofNullable(dialogueMap.get().getDialoguesMap().get(name));
    if (!dialogue.isPresent()) {
      Log.error("Could not find dialogue with name '" + name + "'.");
    }
    return dialogue;
  }
}
