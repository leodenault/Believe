package believe.dialogue;

import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import javax.annotation.Nullable;
import org.newdawn.slick.util.Log;

import java.awt.Dialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Manages fetching dialogue and loading it from disk.
 */
public final class DialogueManager {
  private final File dialogueDirectory;
  private final Map<String, DialogueMap> loadedDialogueMaps;

  public DialogueManager(File dialogueDirectory) {
    this.dialogueDirectory = dialogueDirectory;
    loadedDialogueMaps = new HashMap<>();
  }

  /**
   * Loads a specific {@link DialogueMap} from disk. Use this for loading {@link DialogueMap}
   * instances when loading other resources.
   *
   * @param mapName the name of the dialogue map which should be loaded.
   */
  public void loadDialogueMap(String mapName) {
    innerLoadDialogueMap(mapName);
  }

  @Nullable
  private DialogueMap innerLoadDialogueMap(String mapName) {
    File[] files = dialogueDirectory.listFiles();
    if (files == null) {
      Log.error("Could not load dialogue map. Provided directory is actually a file.");
      return DialogueMap.getDefaultInstance();
    }

    Optional<File>
        dialogueMapFile =
        Stream
            .of(files)
            .filter(file -> file.getName().equals(mapName + ".pb"))
            .findFirst(); // There should only be a single file.

    if (!dialogueMapFile.isPresent()) {
      Log.error(mapName + ".pb does not exist.");
      return DialogueMap.getDefaultInstance();
    }

    try {
      return DialogueMap.parseFrom(new FileInputStream(dialogueMapFile.get()));
    } catch (IOException e) {
      Log.error("An error occurred when reading dialogue map at '" + mapName + ".pb'.", e);
      return DialogueMap.getDefaultInstance();
    }
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
    DialogueMap
        dialogueMap =
        loadedDialogueMaps.computeIfAbsent(mapName, unused -> innerLoadDialogueMap(mapName));

    if (dialogueMap == null) {
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
