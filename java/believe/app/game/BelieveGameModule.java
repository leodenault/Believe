package believe.app.game;

import believe.app.StateInstantiator;
import believe.app.game.Qualifiers.ApplicationTitle;
import believe.core.io.FontLoader;
import believe.gamestate.ArcadeStateInstantiator;
import believe.gamestate.ChangeStateAction;
import believe.gamestate.ExitTemporaryStateAction;
import believe.gamestate.FlowFilePickerMenuState;
import believe.gamestate.GameOverState;
import believe.gamestate.GamePausedOverlay;
import believe.gamestate.MainMenuState;
import believe.gamestate.OptionsMenuStateInstantiator;
import believe.gamestate.PlatformingState;
import believe.gamestate.PlayFlowFileStateInstantiator;
import believe.physics.manager.PhysicsManager;
import dagger.Binds;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Arrays;
import java.util.List;

/** Dagger module for the application running the elieve video game. */
@Module
abstract class BelieveGameModule {
  @Provides
  @ApplicationTitle
  static String provideApplicationTitle() {
    return "Believe";
  }

  @Provides
  static PhysicsManager providePhysicsManager() {
    return PhysicsManager.getInstance();
  }

  @Binds
  abstract StateBasedGame bindStateBasedGame(BelieveGame believeGame);

  @Provides
  @Reusable
  static ChangeStateAction<MainMenuState> provideChangeToMainMenuStateAction(
      StateBasedGame stateBasedGame) {
    return new ChangeStateAction<>(MainMenuState.class, stateBasedGame);
  }

  @Provides
  @Reusable
  static ExitTemporaryStateAction provideExitTemporaryStateAction(
      Lazy<ChangeStateAction<MainMenuState>> mainMenuStateChangeStateAction) {
    return () -> mainMenuStateChangeStateAction.get().componentActivated(null);
  }

  @Provides
  static List<StateInstantiator> provideStateInstantiators(
      PhysicsManager physicsManager,
      ExitTemporaryStateAction exitTemporaryStateAction,
      @OptionsMenuStateInstantiator StateInstantiator optionsMenuStateInstantiator,
      @ArcadeStateInstantiator StateInstantiator arcadeStateInstantiator,
      @PlayFlowFileStateInstantiator StateInstantiator playFlowFileStateInstantiator) {
    return Arrays.asList(
        (container, game, fontLoader) -> new MainMenuState(container, game),
        optionsMenuStateInstantiator,
        (container, game, fontLoader) -> new FlowFilePickerMenuState(container, game),
        playFlowFileStateInstantiator,
        (container, game, fontLoader) ->
            new GamePausedOverlay(container, game, exitTemporaryStateAction),
        (container, game, fontLoader) ->
            new PlatformingState(container, game, physicsManager, fontLoader),
        arcadeStateInstantiator,
        (container, game, fontLoader) ->
            new GameOverState(container, game, exitTemporaryStateAction));
  }

  @Binds
  abstract GameContainer bindGameContainer(AppGameContainer appGameContainer);

  @Provides
  @Reusable
  static FontLoader provideFontLoader(GameContainer gameContainer) {
    FontLoader fontLoader = new FontLoader(gameContainer.getWidth(), gameContainer.getHeight());
    fontLoader.load();
    return fontLoader;
  }
}
