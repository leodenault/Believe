package believe.app;

import believe.core.io.FontLoader;
import believe.core.io.JarClasspathLocation;
import believe.gamestate.GameStateBase;
import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import java.util.Set;

@Singleton
final class Application extends StateBasedGame {
  private final Lazy<GameStateBase> firstState;
  private final Lazy<Set<GameStateBase>> gameStates;
  private final Lazy<FontLoader> fontLoader;

  @Inject
  Application(
      @ApplicationTitle String title,
      @FirstState Lazy<GameStateBase> firstState,
      @GameStates Lazy<Set<GameStateBase>> otherGameStates,
      Lazy<FontLoader> fontLoader) {
    super(title);
    this.firstState = firstState;
    this.gameStates = otherGameStates;
    this.fontLoader = fontLoader;

    ResourceLoader.addResourceLocation(new JarClasspathLocation());
  }

  @Override
  public void initStatesList(GameContainer container) {
    container.getGraphics().setFont(fontLoader.get().getBaseFont());
    addState(firstState.get());
    for (GameStateBase gameState : gameStates.get()) {
      addState(gameState);
    }
  }

  @Override
  protected void preUpdateState(GameContainer container, int delta) throws SlickException {
    super.preUpdateState(container, delta);
    container.getGraphics().setFont(fontLoader.get().getBaseFont());
  }
}
