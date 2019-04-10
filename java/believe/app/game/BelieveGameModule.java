package believe.app.game;

import believe.app.ApplicationTitle;
import believe.gamestate.ChangeStateAction;
import believe.gamestate.ExitTemporaryStateAction;
import believe.gamestate.MainMenuState;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

/** Dagger module for the application running the elieve video game. */
@Module
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
}
