package believe.gamestate;

import believe.gamestate.PauseGameAction.OverlayState;
import believe.graphics_transitions.CrossFadeTransition;
import believe.graphics_transitions.GraphicsTransitionPairFactory;
import believe.gui.DirectionalPanel;
import believe.gui.MenuSelection;
import believe.gui.MenuSelectionGroup;
import javax.annotation.Nullable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.util.Log;

public class GamePausedOverlay extends GameStateBase implements OverlayState {
  public interface ExitPausedStateAction {
    void exitPausedState();
  }

  private static final int PAUSED_TO_STATE_TRANSITION_LENGTH = 500; // In milliseconds.

  private final GameContainer gameContainer;

  private MenuSelectionGroup selections;
  private DirectionalPanel panel;
  private StateBasedGame game;
  private PausableState pausedState;
  private MenuSelection resume;
  private MenuSelection restart;
  @Nullable
  private ChangeStateAction resumeAction;
  private ComponentListener restartAction;

  @Nullable
  private Image backgroundImage;

  public GamePausedOverlay(
      GameContainer container, StateBasedGame game, ExitPausedStateAction exitPausedStateAction)
      throws SlickException {
    this.game = game;
    this.gameContainer = container;
    panel =
        new DirectionalPanel(container,
            container.getWidth() / 2,
            (container.getHeight() - 200) / 3,
            50);
    resume = new MenuSelection(container, "Resume");
    restart = new MenuSelection(container, "Restart");
    MenuSelection exitLevel = new MenuSelection(container, "Exit Level");

    exitLevel.addListener(component -> {
      pausedState.exitFromPausedState();
      exitPausedStateAction.exitPausedState();
    });

    panel.addChild(resume);
    panel.addChild(restart);
    panel.addChild(exitLevel);

    this.selections = new MenuSelectionGroup();
    this.selections.add(resume);
    this.selections.add(restart);
    this.selections.add(exitLevel);
  }

  @Override
  public void keyPressed(int key, char c) {
    switch (key) {
      case Input.KEY_ENTER:
        this.selections.getCurrentSelection().activate();
        break;
      case Input.KEY_DOWN:
        this.selections.selectNext();
        break;
      case Input.KEY_UP:
        this.selections.selectPrevious();
        break;
      case Input.KEY_ESCAPE:
        resumeAction.componentActivated(null);
        break;
    }
  }


  @Override
  public void init(GameContainer container, final StateBasedGame game) throws SlickException {
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0);
    }
    panel.render(container, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {

  }

  @Override
  public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
    super.enter(container, game);
    this.selections.select(0);

    restartAction = (ComponentListener) (component) -> {
      pausedState.reset();
      new ChangeStateAction(pausedState.getClass(), game).componentActivated(null);
    };
    resume.addListener(resumeAction);
    restart.addListener(restartAction);
  }

  @Override
  public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    super.leave(container, game);
    resume.removeListener(resumeAction);
    restart.removeListener(restartAction);
  }

  @Override
  public void setPausedStateInfo(PausableState state, Image backgroundImage) {
    this.pausedState = state;
    this.backgroundImage = backgroundImage;

    GraphicsTransitionPairFactory transitionPairFactory;
    transitionPairFactory = new GraphicsTransitionPairFactory(() -> {
      Image pauseOverlayScreenshot;
      try {
        pauseOverlayScreenshot = new Image(gameContainer.getWidth(), gameContainer.getHeight());
      } catch (SlickException e) {
        Log.error("Failed to fetch pause overlay screenshot.", e);
        return new EmptyTransition();
      }
      gameContainer.getGraphics().copyArea(pauseOverlayScreenshot, 0, 0);
      return new CrossFadeTransition(pauseOverlayScreenshot,
          backgroundImage,
          PAUSED_TO_STATE_TRANSITION_LENGTH);
    }, EmptyTransition::new);
    resumeAction = new ChangeStateAction(pausedState.getClass(), game, transitionPairFactory);
  }
}
