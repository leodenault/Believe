package believe.app.game;

import believe.app.StateInstantiator;
import believe.app.game.InternalQualifiers.ApplicationTitle;
import believe.app.game.InternalQualifiers.ModulePrivate;
import believe.core.io.FontLoader;
import believe.gamestate.ChangeStateAction;
import believe.gamestate.ExitTemporaryStateAction;
import believe.gamestate.GameOverState;
import believe.gamestate.GamePausedOverlay;
import believe.gamestate.MainMenuState;
import believe.physics.manager.PhysicsManager;
import dagger.Binds;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  @ModulePrivate
  static Set<StateInstantiator> provideStateInstantiators(
      ExitTemporaryStateAction exitTemporaryStateAction,
      @GameStateInstantiators Set<StateInstantiator> stateInstantiators) {
    return Stream.concat(
            Stream.of(
                (container, game, fontLoader) ->
                    new GamePausedOverlay(container, game, exitTemporaryStateAction),
                (container, game, fontLoader) ->
                    new GameOverState(container, game, exitTemporaryStateAction)),
            stateInstantiators.stream())
        .collect(Collectors.toSet());
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
