package believe.app;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public interface StateInstantiator {
  GameState create(GameContainer container, StateBasedGame game) throws SlickException;
}
