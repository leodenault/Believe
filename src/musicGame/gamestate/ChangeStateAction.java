package musicGame.gamestate;

import musicGame.gamestate.GameStateBase;

import org.newdawn.slick.Color;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ChangeStateAction implements ComponentListener {

  private static final int DEFAULT_TRANSITION_LENGTH = 65;

  protected Class<? extends GameState> state;
  protected int transitionLength;
  protected StateBasedGame game;

  public ChangeStateAction(Class<? extends GameState> state, StateBasedGame game) {
    this(state, game, DEFAULT_TRANSITION_LENGTH);
  }

  public ChangeStateAction(Class<? extends GameState> state, StateBasedGame game, int transitionLength) {
    this.state = state;
    this.game = game;
    this.transitionLength = transitionLength;
  }

  @Override
  public void componentActivated(AbstractComponent component) {
    game.enterState(GameStateBase.getStateID(state),
        new FadeOutTransition(Color.black, transitionLength), new FadeInTransition(Color.black, transitionLength));

  }

}
