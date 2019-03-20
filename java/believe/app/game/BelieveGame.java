package believe.app.game;

import believe.app.StateInstantiator;
import believe.app.game.Qualifiers.ApplicationTitle;
import believe.core.io.FontLoader;
import believe.core.io.JarClasspathLocation;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import java.util.List;

@Singleton
final class BelieveGame extends StateBasedGame {
  private final List<StateInstantiator> gameStates;

  private FontLoader fontLoader;

  @Inject
  BelieveGame(@ApplicationTitle String title, List<StateInstantiator> gameStates) {
    super(title);
    this.gameStates = gameStates;

    ResourceLoader.addResourceLocation(new JarClasspathLocation());
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    loadResources(container);
    container.getGraphics().setFont(fontLoader.getBaseFont());
    for (StateInstantiator instantiator : gameStates) {
      addState(instantiator.create(container, this, fontLoader));
    }
  }

  @Override
  protected void preUpdateState(GameContainer container, int delta) throws SlickException {
    super.preUpdateState(container, delta);
    container.getGraphics().setFont(fontLoader.getBaseFont());
  }

  private void loadResources(GameContainer container) {
    Log.info("Loading resources...");
    fontLoader = new FontLoader(container.getWidth(), container.getHeight());
    fontLoader.load();
    Log.info("Finished loading resources.");
  }
}
