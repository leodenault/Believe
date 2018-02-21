package believe.app;

import believe.core.FontLoader;
import believe.core.JarClasspathLocation;
import believe.gamestate.GameStateBase;
import believe.gamestate.MainMenuState;
import believe.util.Util;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import java.util.Set;


public class Launcher extends StateBasedGame {
  private final AppGameContainer gameContainer;
  private final Set<StateInstantiator> gameStates;
  private final Class<? extends GameState> startState;

  private Launcher(
      String title,
      Set<StateInstantiator> gameStates,
      Class<? extends GameState> startState,
      int width,
      int height,
      boolean windowed) throws SlickException {
    super(title);
    ResourceLoader.addResourceLocation(new JarClasspathLocation());
    gameContainer = new AppGameContainer(this);
    this.gameStates = gameStates;
    this.startState = startState;
    gameContainer.setShowFPS(false);

    int gameWidth = width < 0 ? gameContainer.getScreenWidth() : width;
    int gameHeight = height < 0 ? gameContainer.getScreenHeight() : height;

    gameContainer.setDisplayMode(gameWidth, gameHeight, !windowed);
    gameContainer.setMouseGrabbed(!windowed);
  }

  public static void setUpAndLaunch(
      String title,
      Set<StateInstantiator> gameStates,
      Class<? extends GameState> startState,
      int width,
      int height,
      boolean windowed) {
    try {
      Util.setNativePath();
      Launcher launcher = new Launcher(title, gameStates, startState, width, height, windowed);
      launcher.launch();
    } catch (Exception e) {
      Log.info(String.format("An error was caught and unfortunately, it was not graceful: %s",
          e.getMessage()));
      e.printStackTrace();
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
    enterState(GameStateBase.getStateID(startState));
    this.enterState(GameStateBase.getStateID(startState));
  }

  private void loadResources() {
    Log.info("Loading resources...");
    FontLoader.getInstance().load();
    Log.info("Finished loading resources.");
  }
}
