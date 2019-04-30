package believe.app;

import believe.app.proto.GameOptionsProto.GameOptions;
import believe.core.io.FontLoader;
import believe.datamodel.MutableDataCommitter;
import believe.datamodel.protodata.MutableProtoDataCommitter;
import believe.action.ChangeStateAction;
import believe.gamestate.MainMenuState;
import believe.gamestate.levelstate.platformingstate.EventActions;
import believe.gamestate.levelstate.platformingstate.PlatformingState;
import believe.map.gui.MapManager;
import believe.physics.manager.PhysicsManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.Multibinds;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/** Dagger module used in all application components. */
@Module
public abstract class ApplicationModule {
  private static final String GAME_OPTIONS_FILE_NAME = "game_options.pb";

  @Provides
  @Singleton
  static MutableDataCommitter<GameOptions> provideGameOptionsCommitter() {
    MutableProtoDataCommitter<GameOptions> optionsProvider =
        new MutableProtoDataCommitter<>(
            GAME_OPTIONS_FILE_NAME, GameOptions.parser(), GameOptions.getDefaultInstance());
    optionsProvider.load();
    return optionsProvider;
  }

  @Binds
  abstract Supplier<GameOptions> provideGameOptionsProvider(
      MutableDataCommitter<GameOptions> gameOptions);

  @Provides
  static PhysicsManager providePhysicsManager() {
    return PhysicsManager.getInstance();
  }

  @Provides
  static MapManager provideMapManager() {
    return MapManager.defaultManager();
  }

  @Multibinds
  @Reusable
  @EventActions
  abstract Map<Integer, Function<PlatformingState, Void>> bindEventActionMap();

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
