package believe.action;

import believe.gamestate.GameStateBase;
import believe.graphicstransitions.GraphicsTransitionPairFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ChangeStateAction<StateT extends GameState> implements ComponentListener {
  private static final int DEFAULT_TRANSITION_LENGTH = 65;
  private static final GraphicsTransitionPairFactory DEFAULT_TRANSITIONS =
      new GraphicsTransitionPairFactory(
          () -> new FadeOutTransition(Color.black, DEFAULT_TRANSITION_LENGTH),
          () -> new FadeInTransition(Color.black, DEFAULT_TRANSITION_LENGTH));

  private final GraphicsTransitionPairFactory transitions;

  protected Class<StateT> state;
  protected StateBasedGame game;

  public ChangeStateAction(Class<StateT> state, StateBasedGame game) {
    this(state, game, DEFAULT_TRANSITIONS);
  }

  public ChangeStateAction(
      Class<StateT> state,
      StateBasedGame game,
      GraphicsTransitionPairFactory graphicsTransitionPairFactory) {
    this.state = state;
    this.game = game;
    this.transitions = graphicsTransitionPairFactory;
  }

  @Override
  public void componentActivated(AbstractComponent component) {
    game.enterState(
        GameStateBase.getStateID(state),
        transitions.createLeaveTransition(),
        transitions.createEnterTransition());
  }
}
