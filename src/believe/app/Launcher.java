package believe.app;

import believe.core.FontLoader;
import believe.core.JarClasspathLocation;
import believe.util.Util;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import java.util.List;


public class Launcher extends StateBasedGame {
  private final AppGameContainer gameContainer;
  private final List<StateInstantiator> gameStates;

  private Launcher(
      String title, List<StateInstantiator> gameStates, int width, int height, boolean windowed)
      throws SlickException {
    super(title);
    ResourceLoader.addResourceLocation(new JarClasspathLocation());
    gameContainer = new AppGameContainer(this);
    this.gameStates = gameStates;
    gameContainer.setShowFPS(false);

    int gameWidth = width < 0 ? gameContainer.getScreenWidth() : width;
    int gameHeight = height < 0 ? gameContainer.getScreenHeight() : height;

    gameContainer.setDisplayMode(gameWidth, gameHeight, !windowed);
    gameContainer.setMouseGrabbed(!windowed);
  }

  /**
   * Launches the game using the settings provided.
   *
   * @param title      The title of the window conatining the game.
   * @param gameStates The list of states that make up the game. The first state in the list should
   *                   be the first state in the game to be entered.
   * @param width      The width of the window for the game, or -1 for the entire width of the
   *                   device's
   *                   screen.
   * @param height     The height of the window for the game, or -1 for the entire height of the
   *                   device's
   *                   screen.
   * @param windowed   Whether the game should be displayed in a window or at fullscreen.
   */
  public static void setUpAndLaunch(
      String title, List<StateInstantiator> gameStates, int width, int height, boolean windowed) {
    try {
      Util.setNativePath();
      Launcher launcher = new Launcher(title, gameStates, width, height, windowed);
      launcher.launch();
    } catch (Exception e) {
      Log.error(String.format("An error was caught and unfortunately, it was not graceful: %s",
          e.getMessage()), e);
    }
  }

  public void launch() throws SlickException {
    gameContainer.start();
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    loadResources();
    for (StateInstantiator instantiator : gameStates) {
      addState(instantiator.create(container, this));
    }
  }

  private void loadResources() {
    Log.info("Loading resources...");
    FontLoader.getInstance().load();
    Log.info("Finished loading resources.");
  }
}
