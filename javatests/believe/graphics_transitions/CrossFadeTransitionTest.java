package believe.graphics_transitions;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/** Unit tests for {@link CrossFadeTransition}. */
public final class CrossFadeTransitionTest {
  private static final int TRANSITION_LENGTH = 50;

  private CrossFadeTransition transition;

  @Mock
  private Image previousScreenshot;
  @Mock
  private Image nextScreenshot;
  @Mock
  private StateBasedGame game;
  @Mock
  private GameContainer gameContainer;
  @Mock
  private Graphics g;

  @Before
  public void setUp() {
    initMocks(this);
    transition = new CrossFadeTransition(previousScreenshot, nextScreenshot, TRANSITION_LENGTH);
  }

  @Test
  public void postRender_fadesSecondStateIn() throws SlickException {
    transition.postRender(game, gameContainer, g);
    transition.update(game, gameContainer, TRANSITION_LENGTH / 2);
    transition.postRender(game, gameContainer, g);
    transition.update(game, gameContainer, TRANSITION_LENGTH / 2);
    transition.postRender(game, gameContainer, g);

    InOrder inOrder = inOrder(g);
    inOrder.verify(g).drawImage(previousScreenshot, 0, 0);
    inOrder.verify(g).drawImage(nextScreenshot, 0, 0, new Color(1f, 1f, 1f, 0f));
    inOrder.verify(g).drawImage(previousScreenshot, 0, 0);
    inOrder.verify(g).drawImage(nextScreenshot, 0, 0, new Color(1f, 1f, 1f, 0.5f));
    inOrder.verify(g).drawImage(previousScreenshot, 0, 0);
    inOrder.verify(g).drawImage(nextScreenshot, 0, 0, new Color(1f, 1f, 1f, 1f));
  }

  @Test
  public void isComplete_transitionLengthNotReached_returnsFalse() {
    assertThat(transition.isComplete(), is(false));
  }

  @Test
  public void isComplete_transitionLengthReached_returnsTrue() throws SlickException {
    transition.update(game, gameContainer, TRANSITION_LENGTH);

    assertThat(transition.isComplete(), is(true));
  }
}
