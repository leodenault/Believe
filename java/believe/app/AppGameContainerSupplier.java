package believe.app;

import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import java.util.function.Supplier;

@Singleton
public final class AppGameContainerSupplier implements Supplier<AppGameContainer> {
  private static final class EmptyGame implements Game {
    @Override
    public void init(GameContainer container) {}

    @Override
    public void update(GameContainer container, int delta) {}

    @Override
    public void render(GameContainer container, Graphics g) {}

    @Override
    public boolean closeRequested() {
      return false;
    }

    @Override
    public String getTitle() {
      return "";
    }
  }

  private AppGameContainer appGameContainer;

  @Inject
  AppGameContainerSupplier() {
    try {
      appGameContainer = new AppGameContainer(new EmptyGame());
    } catch (SlickException e) {
      Log.error("Could not create empty app game container.", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public AppGameContainer get() {
    return appGameContainer;
  }

  void initAppGameContainer(String[] commandLineArgs, StateBasedGame game)
      throws SlickException {
    AppFlags appFlags = CommandLineParser.parse(AppFlags.class, commandLineArgs);
    appGameContainer = new AppGameContainer(game);

    appGameContainer.setShowFPS(false);

    int flagWidth = appFlags.width();
    int flagHeight = appFlags.height();
    boolean flagIsWindowed = appFlags.windowed();

    int gameWidth = flagWidth < 0 ? appGameContainer.getScreenWidth() : flagWidth;
    int gameHeight = flagHeight < 0 ? appGameContainer.getScreenHeight() : flagHeight;

    appGameContainer.setDisplayMode(gameWidth, gameHeight, !flagIsWindowed);
    appGameContainer.setMouseGrabbed(!flagIsWindowed);
  }
}
