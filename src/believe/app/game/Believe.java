package believe.app.game;

import static believe.util.Util.immutableSetOf;

import believe.app.Launcher;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.gamestate.ArcadeState;
import believe.gamestate.FlowFilePickerMenuState;
import believe.gamestate.GamePausedOverlay;
import believe.gamestate.MainMenuState;
import believe.gamestate.OptionsMenuState;
import believe.gamestate.PlatformingState;
import believe.gamestate.PlayFlowFileState;

public class Believe {
  public static void main(String[] args) {
    AppFlags flags = CommandLineParser.parse(AppFlags.class, args);
    Launcher.setUpAndLaunch(
        "Believe",
        immutableSetOf(MainMenuState::new,
            OptionsMenuState::new,
            FlowFilePickerMenuState::new,
            (container, game) -> new PlayFlowFileState(game),
            GamePausedOverlay::new,
            PlatformingState::new,
            ArcadeState::new),
        MainMenuState.class,
        flags.width(),
        flags.height(),
        flags.windowed());
  }
}
