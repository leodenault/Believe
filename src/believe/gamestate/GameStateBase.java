package believe.gamestate;

import believe.core.io.FontLoader;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;

public abstract class GameStateBase extends BasicGameState {

  public GameStateBase(GameContainer gameContainer) {
    GameStateRegistry.getInstance().addEntry(this);
    gameContainer.getGraphics().setFont(FontLoader.getInstance().getBaseFont());
  }

  public static int getStateID(Class<? extends GameState> state) {
    return GameStateRegistry.getInstance().getEntryID(state);
  }

  public static <T extends GameState> T getStateInstance(Class<T> state) {
    return GameStateRegistry.getInstance().getEntryState(state);
  }

  @Override
  public int getID() {
    return GameStateRegistry.getInstance().getEntryID(this.getClass());
  }
}
