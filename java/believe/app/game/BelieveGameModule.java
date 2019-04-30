package believe.app.game;

import believe.action.ChangeStateAction;
import believe.action.ExitTemporaryStateAction;
import believe.app.ApplicationModule;
import believe.app.ApplicationTitle;
import believe.app.FirstState;
import believe.app.GameStates;
import believe.gamestate.levelstate.arcadestate.ArcadeState;
import believe.gamestate.FlowFilePickerMenuState;
import believe.gamestate.GameStateBase;
import believe.gamestate.MainMenuState;
import believe.gamestate.OptionsMenuState;
import believe.gamestate.levelstate.platformingstate.PlatformingState;
import believe.gamestate.PlayFlowFileState;
import believe.gamestate.temporarystate.GameOverState;
import believe.gamestate.temporarystate.GamePausedOverlay;
import believe.util.Util;
import dagger.Binds;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ElementsIntoSet;

import java.util.Set;

/** Dagger module for the application running the elieve video game. */
@Module(includes = {ApplicationModule.class})
abstract class BelieveGameModule {
  @Provides
  @ApplicationTitle
  static String provideApplicationTitle() {
    return "Believe";
  }

  @Provides
  @Reusable
  static ExitTemporaryStateAction provideExitTemporaryStateAction(
      Lazy<ChangeStateAction<MainMenuState>> mainMenuStateChangeStateAction) {
    return () -> mainMenuStateChangeStateAction.get().componentActivated(null);
  }

  @Binds
  @FirstState
  abstract GameStateBase bindFirstState(MainMenuState mainMenuState);

  @Provides
  @ElementsIntoSet
  @GameStates
  static Set<GameStateBase> provideGameStates(
      OptionsMenuState optionsMenuState,
      ArcadeState arcadeState,
      PlayFlowFileState playFlowFileState,
      FlowFilePickerMenuState flowFilePickerMenuState,
      PlatformingState platformingState,
      GamePausedOverlay gamePausedOverlay,
      GameOverState gameOverState) {
    return Util.hashSetOf(
        optionsMenuState,
        arcadeState,
        playFlowFileState,
        flowFilePickerMenuState,
        platformingState,
        gamePausedOverlay,
        gameOverState);
  }
}
