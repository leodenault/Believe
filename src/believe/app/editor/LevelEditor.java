package believe.app.editor;

import static believe.util.Util.hashSetOf;

import believe.app.Launcher;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.gamestate.PlatformingState;

public class LevelEditor {
  public static void main(String[] args) {
    AppFlags flags = CommandLineParser.parse(AppFlags.class, args);
    Launcher.setUpAndLaunch(
        "Believe Level Editor",
        hashSetOf(PlatformingState::new),
        PlatformingState.class,
        flags.width(),
        flags.height(),
        flags.windowed());
  }
}
