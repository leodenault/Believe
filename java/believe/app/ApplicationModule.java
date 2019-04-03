package believe.app;

import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.app.proto.GameOptionsProto.GameOptions;
import believe.datamodel.DataCommitter;
import believe.datamodel.DataProvider;
import believe.datamodel.protodata.ProtoDataCommitter;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import javax.inject.Singleton;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/** Dagger module used in all application components. */
@Module
public final class ApplicationModule {
  private static final String GAME_OPTIONS_FILE_NAME = "game_options.pb";

  private final String[] commandLineArgs;

  public ApplicationModule(String[] commandLineArgs) {
    this.commandLineArgs = commandLineArgs;
  }

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
  @Reusable
  AppFlags provideAppFlags() {
    return CommandLineParser.parse(AppFlags.class, commandLineArgs);
  }

  @Provides
  @Singleton
  AppGameContainer provideAppGameContainer(StateBasedGame game, AppFlags appFlags) {
    try {
      AppGameContainer gameContainer = new AppGameContainer(game);

      gameContainer.setShowFPS(false);

      int flagWidth = appFlags.width();
      int flagHeight = appFlags.height();
      boolean flagIsWindowed = appFlags.windowed();

      int gameWidth = flagWidth < 0 ? gameContainer.getScreenWidth() : flagWidth;
      int gameHeight = flagHeight < 0 ? gameContainer.getScreenHeight() : flagHeight;

      gameContainer.setDisplayMode(gameWidth, gameHeight, !flagIsWindowed);
      gameContainer.setMouseGrabbed(!flagIsWindowed);

      return gameContainer;
    } catch (SlickException e) {
      throw new RuntimeException("Could not successfully create an AppGameContainer object.", e);
    }
  }
}
