package believe.app.game;

import believe.app.StateInstantiator;
import believe.app.game.Qualifiers.ApplicationTitle;
import believe.core.io.FontLoader;
import believe.core.io.JarClasspathLocation;
import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import java.util.List;

@Singleton
final class BelieveGame extends StateBasedGame {
  private final List<StateInstantiator> gameStates;
  private final Lazy<FontLoader> fontLoader;

  @Inject
  BelieveGame(
      @ApplicationTitle String title,
      List<StateInstantiator> gameStates,
      Lazy<FontLoader> fontLoader) {
    super(title);
    this.gameStates = gameStates;
    this.fontLoader = fontLoader;

    ResourceLoader.addResourceLocation(new JarClasspathLocation());
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    container.getGraphics().setFont(fontLoader.get().getBaseFont());
    for (StateInstantiator instantiator : gameStates) {
      addState(instantiator.create(container, this, fontLoader.get()));
    }
  }

  @Override
  protected void preUpdateState(GameContainer container, int delta) throws SlickException {
    super.preUpdateState(container, delta);
    container.getGraphics().setFont(fontLoader.get().getBaseFont());
  }
}
