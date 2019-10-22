package believe.app;

import believe.action.ChangeStateAction;
import believe.app.proto.GameOptionsProto.GameOptions;
import believe.character.playable.PlayableDaggerModule;
import believe.command.CommandDaggerModule;
import believe.core.io.FontLoader;
import believe.datamodel.MutableDataCommitter;
import believe.datamodel.protodata.MutableProtoDataCommitter;
import believe.dialogue.DialogueDaggerModule;
import believe.gamestate.MainMenuState;
import believe.gamestate.levelstate.platformingstate.EventActions;
import believe.gamestate.levelstate.platformingstate.PlatformingState;
import believe.map.collidable.command.CollidableCommandDaggerModule;
import believe.map.collidable.tile.CollidableTileDaggerModule;
import believe.map.io.MapParsingDaggerModule;
import believe.physics.collision.CollisionDaggerModule;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.Multibinds;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/** Dagger module used in all application components. */
@Module(
    includes = {
      CollidableTileDaggerModule.class,
      CollisionDaggerModule.class,
      CollidableCommandDaggerModule.class,
      CommandDaggerModule.class,
      DialogueDaggerModule.class,
      MapParsingDaggerModule.class,
      PlayableDaggerModule.class
    })
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

  @Binds
  abstract GUIContext bindGuiContext(GameContainer gameContainer);

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
