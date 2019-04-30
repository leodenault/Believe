package believe.graphicstransitions;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public final class CrossFadeTransition implements Transition {

  private final Image previousStateScreenshot;
  private final Image nextStateScreenshot;
  private final int transitionLength;

  private float duration;

  public CrossFadeTransition(
      Image previousStateScreenshot, Image nextStateScreenshot, int transitionLength) {
    this.previousStateScreenshot = previousStateScreenshot;
    this.nextStateScreenshot = nextStateScreenshot;
    this.transitionLength = transitionLength;
    this.duration = 0;
  }

  @Override
  public boolean isComplete() {
    return duration >= transitionLength;
  }

  @Override
  public void init(GameState firstState, GameState secondState) {}

  @Override
  public void update(StateBasedGame game, GameContainer container, int delta)
      throws SlickException {
    duration += delta;
  }

  @Override
  public void preRender(StateBasedGame game, GameContainer container, Graphics g)
      throws SlickException {}

  @Override
  public void postRender(StateBasedGame game, GameContainer container, Graphics g)
      throws SlickException {
    g.drawImage(previousStateScreenshot, 0, 0);
    g.drawImage(nextStateScreenshot,
        0,
        0,
        new Color(1f, 1f, 1f, duration / (float) transitionLength));

  }
}
