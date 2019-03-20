package believe.app.game;

import believe.app.StateInstantiator;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.app.game.Qualifiers.ApplicationTitle;
import believe.gamestate.ArcadeState;
import believe.gamestate.ChangeStateAction;
import believe.gamestate.ExitTemporaryStateAction;
import believe.gamestate.FlowFilePickerMenuState;
import believe.gamestate.GameOverState;
import believe.gamestate.GamePausedOverlay;
import believe.gamestate.MainMenuState;
import believe.gamestate.OptionsMenuState;
import believe.gamestate.PlatformingState;
import believe.gamestate.PlayFlowFileState;
import believe.physics.manager.PhysicsManager;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Dagger module for the application running the elieve video game.
 */
@Module
final class BelieveGameModule {
  private final String[] commandLineArgs;

  BelieveGameModule(String[] commandLineArgs) {
    this.commandLineArgs = commandLineArgs;
  }

  @Provides
  @ApplicationTitle
  static String provideApplicationTitle() {
    return "Believe";
  }

  @Provides
  static PhysicsManager providePhysicsManager() {
    return PhysicsManager.getInstance();
  }

  @Provides
  @Reusable
  static ChangeStateAction<MainMenuState> provideChangeToMainMenuStateAction(
      BelieveGame believeGame) {
    return new ChangeStateAction<>(MainMenuState.class, believeGame);
  }

  @Provides
  @Reusable
  static ExitTemporaryStateAction provideExitTemporaryStateAction(
      Lazy<ChangeStateAction<MainMenuState>> mainMenuStateChangeStateAction) {
    return () ->
        mainMenuStateChangeStateAction.get().componentActivated(null);
  }

  @Provides
  static List<StateInstantiator> provideStateInstantiators(
      PhysicsManager physicsManager, ExitTemporaryStateAction exitTemporaryStateAction) {
    return Arrays.asList((container, game, fontLoader) -> new MainMenuState(container, game),
        (container, game, fontLoader) -> new OptionsMenuState(container, game),
        (container, game, fontLoader) -> new FlowFilePickerMenuState(container, game),
        (container, game, fontLoader) -> new PlayFlowFileState(container, game),
        (container, game, fontLoader) -> new GamePausedOverlay(container,
            game,
            exitTemporaryStateAction),
        (container, game, fontLoader) -> new PlatformingState(container,
            game,
            physicsManager,
            fontLoader),
        (container, game, fontLoader) -> new ArcadeState(container,
            game,
            physicsManager,
            fontLoader),
        (container, game, fontLoader) -> new GameOverState(container,
            game,
            exitTemporaryStateAction));
  }

  @Provides
  AppFlags provideAppFlags() {
    return CommandLineParser.parse(AppFlags.class, commandLineArgs);
  }

  @Provides
  Optional<AppGameContainer> provideAppGameContainer(BelieveGame believeGame, AppFlags appFlags) {
    try {
      AppGameContainer gameContainer = new AppGameContainer(believeGame);

      gameContainer.setShowFPS(false);

      int flagWidth = appFlags.width();
      int flagHeight = appFlags.height();
      boolean flagIsWindowed = appFlags.windowed();

      int gameWidth = flagWidth < 0 ? gameContainer.getScreenWidth() : flagWidth;
      int gameHeight = flagHeight < 0 ? gameContainer.getScreenHeight() : flagHeight;

      gameContainer.setDisplayMode(gameWidth, gameHeight, !flagIsWindowed);
      gameContainer.setMouseGrabbed(!flagIsWindowed);

      return Optional.of(gameContainer);
    } catch (SlickException e) {
      Log.error("Could not successfully create an AppGameContainer object.", e);
    }
    return Optional.empty();
  }
}
