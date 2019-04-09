package believe.gamestate;

import believe.graphics_transitions.CrossFadeTransition;
import believe.graphics_transitions.GraphicsTransitionPairFactory;
import believe.gui.DirectionalPanel;
import believe.gui.MenuSelection;
import believe.gui.MenuSelectionGroup;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.util.Log;

public class GamePausedOverlay extends GameStateBase
    implements TemporaryState<OverlayablePrecedingState> {
  private static final int PAUSED_TO_STATE_TRANSITION_LENGTH = 500; // In milliseconds.

  private final GameContainer gameContainer;
  private final ExitTemporaryStateAction exitPauseStateAction;
  private final MenuSelection resume;
  private final MenuSelection restart;
  private final MenuSelection exitLevel;

  private MenuSelectionGroup selections;
  private DirectionalPanel panel;
  private StateBasedGame game;
  @Nullable private PrecedingState pausedState;
  @Nullable private ChangeStateAction<?> resumeAction;
  @Nullable private ComponentListener restartAction;
  @Nullable private ComponentListener exitSelectedAction;

  @Nullable private Image backgroundImage;

  @Inject
  public GamePausedOverlay(
      GameContainer container, StateBasedGame game, ExitTemporaryStateAction exitPauseStateAction) {
    this.game = game;
    this.gameContainer = container;
    this.exitPauseStateAction = exitPauseStateAction;
    this.resume = new MenuSelection(container, container.getGraphics().getFont(), "Resume");
    this.restart = new MenuSelection(container, container.getGraphics().getFont(), "Restart");
    this.exitLevel = new MenuSelection(container, container.getGraphics().getFont(), "Exit Level");
    panel =
        new DirectionalPanel(container,
            container.getWidth() / 2,
            (container.getHeight() - 200) / 3,
            50);

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
        if (resumeAction != null) {
          resumeAction.componentActivated(null);
        } else {
          Log.error("Could not find an action for resuming the game.");
        }
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
  }

  @Override
  public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    super.leave(container, game);
  }


  @Override
  public void setPrecedingState(OverlayablePrecedingState state) {
    this.pausedState = state;
    GraphicsTransitionPairFactory transitionPairFactory;
    try {
      backgroundImage = state.getCurrentScreenshot();
    } catch (SlickException e) {
      Log.error("Failed to fetch screenshot for background image used activate overlay.");
    }

    transitionPairFactory = new GraphicsTransitionPairFactory(() -> {
      Image pauseOverlayScreenshot;
      try {
        pauseOverlayScreenshot = new Image(gameContainer.getWidth(), gameContainer.getHeight());
      } catch (SlickException e) {
        Log.error("Failed to fetch screenshot for activate overlay.", e);
        return new EmptyTransition();
      }
      gameContainer.getGraphics().copyArea(pauseOverlayScreenshot, 0, 0);
      return new CrossFadeTransition(pauseOverlayScreenshot,
          backgroundImage,
          PAUSED_TO_STATE_TRANSITION_LENGTH);
    }, EmptyTransition::new);

    if (resumeAction != null) {
      resume.removeListener(resumeAction);
    }
    if (restartAction != null) {
      restart.removeListener(restartAction);
    }
    if (exitSelectedAction != null) {
      exitLevel.removeListener(exitSelectedAction);
    }

    resumeAction = new ChangeStateAction<>(pausedState.getClass(), game, transitionPairFactory);
    restartAction = (component) -> {
      if (pausedState != null) {
        pausedState.reset();
        new ChangeStateAction<>(pausedState.getClass(), game).componentActivated(null);
      }
    };
    exitSelectedAction = component -> {
      pausedState.exitingFollowingState();
      exitPauseStateAction.exitTemporaryState();
    };

    resume.addListener(resumeAction);
    restart.addListener(restartAction);
    exitLevel.addListener(exitSelectedAction);
  }
}
