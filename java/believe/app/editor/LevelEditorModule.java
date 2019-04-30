package believe.app.editor;

import believe.app.ApplicationModule;
import believe.app.ApplicationTitle;
import believe.gamestate.levelstate.platformingstate.EventActions;
import believe.app.FirstState;
import believe.app.GameStates;
import believe.action.ExitTemporaryStateAction;
import believe.gamestate.temporarystate.GameOverState;
import believe.gamestate.temporarystate.GamePausedOverlay;
import believe.gamestate.GameStateBase;
import believe.gamestate.levelstate.platformingstate.PlatformingState;
import believe.util.Util;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.util.Set;
import java.util.function.Function;

/** Dagger module for the application running the Believe Level Editor. */
@Module(includes = ApplicationModule.class)
abstract class LevelEditorModule {
  @Provides
  @ApplicationTitle
  static String provideApplicationTitle() {
    return "Believe Level Editor";
  }

  @Binds
  @FirstState
  abstract GameStateBase bindFirstState(PlatformingState platformingState);

  @Provides
  @ElementsIntoSet
  @GameStates
  static Set<GameStateBase> provideGameStates(
      GamePausedOverlay gamePausedOverlay, GameOverState gameOverState) {
    return Util.hashSetOf(gamePausedOverlay, gameOverState);
  }

  @Provides
  static ExitTemporaryStateAction provideExitTemporaryStateAction(GameContainer gameContainer) {
    return gameContainer::exit;
  }

  @Provides
  @IntoMap
  @IntKey(Input.KEY_R)
  @EventActions
  static Function<PlatformingState, Void> provideResetLevelAction(GameContainer gameContainer) {
    return platformingState -> {
      try {
        platformingState.reloadLevel(gameContainer);
      } catch (SlickException e) {
        Log.error("Failed to reload level.", e);
      }
      return null;
    };
  }
}
