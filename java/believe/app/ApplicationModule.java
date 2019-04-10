package believe.app;

import believe.app.proto.GameOptionsProto.GameOptions;
import believe.core.io.FontLoader;
import believe.datamodel.DataCommitter;
import believe.datamodel.DataProvider;
import believe.datamodel.protodata.ProtoDataCommitter;
import believe.gamestate.ChangeStateAction;
import believe.gamestate.MainMenuState;
import believe.physics.manager.PhysicsManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/** Dagger module used in all application components. */
@Module
public abstract class ApplicationModule {
  private static final String GAME_OPTIONS_FILE_NAME = "game_options.pb";

  @Provides
  @Singleton
  static DataCommitter<GameOptions> provideGameOptionsCommitter() {
    ProtoDataCommitter<GameOptions> optionsProvider =
        new ProtoDataCommitter<>(
            GAME_OPTIONS_FILE_NAME, GameOptions.parser(), GameOptions.getDefaultInstance());
    optionsProvider.load();
    return optionsProvider;
  }

  @Provides
  static DataProvider<GameOptions> provideGameOptionsProvider(
      DataCommitter<GameOptions> gameOptions) {
    return gameOptions;
  }

  @Provides
  static PhysicsManager providePhysicsManager() {
    return PhysicsManager.getInstance();
  }

  @Binds
  abstract StateBasedGame bindStateBasedGame(Application application);

  @Provides
  static GameContainer provideGameContainer(AppGameContainerSupplier appGameContainer) {
    return appGameContainer.get();
  }

  @Provides
  @Reusable
  static ChangeStateAction<MainMenuState> provideChangeToMainMenuStateAction(
      StateBasedGame stateBasedGame) {
    return new ChangeStateAction<>(MainMenuState.class, stateBasedGame);
  }

  @Provides
  @Reusable
  static FontLoader provideFontLoader(GameContainer gameContainer) {
    FontLoader fontLoader = new FontLoader(gameContainer.getWidth(), gameContainer.getHeight());
    fontLoader.load();
    return fontLoader;
  }
}
