package believe.dialogue;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import org.newdawn.slick.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages fetching dialogue and loading it from disk.
 */
public final class DialogueManager {
  private final String dialogueDirectory;
  private final Map<String, DialogueMap> loadedDialogueMaps;

  public DialogueManager(String dialogueDirectory) {
    this.dialogueDirectory = dialogueDirectory;
    loadedDialogueMaps = new HashMap<>();
  }

  /**
   * Loads a specific {@link DialogueMap} from disk. Use this for loading {@link DialogueMap}
   * instances when loading other resources.
   *
   * @param setName the name of the set which should be loaded.
   */
  public void loadDialogueMap(String setName) throws IOException {
    innerLoadDialogueSet(setName);
  }

  private DialogueMap innerLoadDialogueSet(String setName) throws IOException {
    return DialogueMap.parseFrom(new FileInputStream(dialogueDirectory + "/" + setName + ".pb"));
  }

  /**
   * Fetches a specific {@link Dialogue} using {@code mapName} and {@code name} for identification.
   *
   * <p><b>Note</b>: If the {@link DialogueMap} named {@code mapName} has not yet been loaded it
   * will automatically be loaded which may incur performance costs. See {@link
   * #loadDialogueMap(String)} for loading a {@link DialogueMap} at a more convenient time.
   *
   * @param mapName the name of the set from which to fetch the {@link Dialogue} instance.
   * @param name the name of the {@link Dialogue} instance to be fetched.
   * @return an {@link Optional} {@link Dialogue} if a {@link Dialogue} with named {@code name}
   * exists. Otherwise, {@link Optional#empty()}.
   */
  public Optional<Dialogue> getDialogue(String mapName, String name) {
    DialogueMap dialogueMap = loadedDialogueMaps.computeIfAbsent(mapName, unused -> {
      try {
        return innerLoadDialogueSet(mapName);
      } catch (IOException e) {
        return null;
      }
    });

    if (dialogueMap == null) {
      Log.error("Could not find dialogue map with name '" + mapName + "'.");
      return Optional.empty();
    }

    Optional<Dialogue> dialogue = Optional.ofNullable(dialogueMap.getDialoguesMap().get(name));
    if (!dialogue.isPresent()) {
      Log.error("Could not find dialogue with name '"
          + name
          + "' within dialogue map named '"
          + mapName
          + "'.");
    }
    return dialogue;
  }
}
