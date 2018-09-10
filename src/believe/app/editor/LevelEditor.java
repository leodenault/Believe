package believe.app.editor;

import believe.app.Launcher;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.gamestate.PlatformingState;

import java.util.Collections;

public class LevelEditor {

  public static void main(String[] args) {
    AppFlags flags = CommandLineParser.parse(AppFlags.class, args);
    Launcher.setUpAndLaunch(
        "Believe Level Editor",
        Collections.singletonList(PlatformingState::new),
        flags.width(),
        flags.height(),
        flags.windowed());
  }
}
