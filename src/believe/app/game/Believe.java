package believe.app.game;

import believe.app.Launcher;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.gamestate.ArcadeState;
import believe.gamestate.ChangeStateAction;
import believe.gamestate.FlowFilePickerMenuState;
import believe.gamestate.GameOverState;
import believe.gamestate.GamePausedOverlay;
import believe.gamestate.MainMenuState;
import believe.gamestate.OptionsMenuState;
import believe.gamestate.PlatformingState;
import believe.gamestate.PlayFlowFileState;
import javax.annotation.Nullable;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Arrays;

public class Believe {
  @Nullable
  private static ChangeStateAction<MainMenuState> returnToMainMenuAction;

  public static void main(String[] args) {
    AppFlags flags = CommandLineParser.parse(AppFlags.class, args);
    Launcher.setUpAndLaunch(
        "Believe",
        Arrays.asList(MainMenuState::new,
            OptionsMenuState::new,
            FlowFilePickerMenuState::new,
            PlayFlowFileState::new,
            (container, game) -> new GamePausedOverlay(container,
                game,
                () -> getOrCreateExitToMainMenuAction(game).componentActivated(null)),
            PlatformingState::new,
            ArcadeState::new,
            (container, game) -> new GameOverState(container,
                game,
                () -> getOrCreateExitToMainMenuAction(game).componentActivated(null))),
        flags.width(),
        flags.height(),
        flags.windowed());
  }

  private static ChangeStateAction<MainMenuState> getOrCreateExitToMainMenuAction(
      StateBasedGame game) {
    if (returnToMainMenuAction == null) {
      returnToMainMenuAction = new ChangeStateAction<>(MainMenuState.class, game);
    }
    return returnToMainMenuAction;
  }
}
