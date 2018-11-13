package believe.app.editor;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;

import believe.app.Launcher;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.core.io.ReloadableFileSystemLocation;
import believe.gamestate.PlatformingState;
import believe.map.gui.MapManager;
import javax.swing.JFileChooser;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.FileSystemLocation;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;
import java.util.Collections;

public class LevelEditor {

  public static void main(String[] args) {
    AppFlags flags = CommandLineParser.parse(AppFlags.class, args);

    // Reset resource locations to force ordering the ReloadableFileSystemLocation to be invoked
    // first.
    ResourceLoader.removeAllResourceLocations();
    ResourceLoader.addResourceLocation(new ClasspathLocation());
    ResourceLoader.addResourceLocation(new ReloadableFileSystemLocation());

    Launcher.setUpAndLaunch(
        "Believe Level Editor",
        Collections.singletonList((container, game) -> new PlatformingState(container,
            game,
            MapManager.defaultManager(),
            hashMapOf(entry(Input.KEY_R,
                state -> LevelEditor.resetLevel(state, container, game))))),
        flags.width(),
        flags.height(),
        flags.windowed());
  }

  private static Void resetLevel(
      PlatformingState state, GameContainer container, StateBasedGame game) {
    try {
      state.reloadLevel(container);
    } catch (SlickException e) {
      Log.error("Could not refresh level after attempting to reload maps.", e);
    }
    return null;
  }
}
