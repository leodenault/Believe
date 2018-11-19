package believe.graphics_transitions;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.CrossStateTransition;

public final class CrossFadeTransition extends CrossStateTransition {
  private final int transitionLength;

  private float duration;

  public CrossFadeTransition(GameState secondState, int transitionLength) {
    super(secondState);
    this.transitionLength = transitionLength;
    this.duration = 0;
  }

  @Override
  public boolean isComplete() {
    return duration > transitionLength;
  }

  @Override
  public void init(GameState firstState, GameState secondState) {}

  @Override
  public void update(StateBasedGame game, GameContainer container, int delta)
      throws SlickException {
    duration += delta;
  }

  @Override
  public void preRenderSecondState(
      StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    float completion = duration / (float) transitionLength;
    g.setDrawMode(Graphics.MODE_ALPHA_MAP);
    g.clearAlphaMap();
    g.setColor(new Color(1f, 1f, 1f, completion));
    g.fillRect(0, 0, container.getWidth(), container.getHeight());
    g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
  }

  @Override
  public void postRenderSecondState(
      StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
    g.setDrawMode(Graphics.MODE_NORMAL);
  }
}
