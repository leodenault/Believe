package believe.gamestate;

import believe.gui.DirectionalPanel;
import believe.gui.MenuSelection;
import believe.gui.MenuSelectionGroup;
import believe.gui.TextComponent;
import javax.annotation.Nullable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

public final class GameOverState extends GameStateBase implements TemporaryState<PrecedingState> {

  private final StateBasedGame game;
  private final ExitTemporaryStateAction exitGameOverStateAction;
  private final MenuSelection retrySelection;
  private final MenuSelection exitSelection;
  private final MenuSelectionGroup menuSelections;
  private final DirectionalPanel directionalPanel;

  @Nullable private ChangeStateAction<?> returnToPreviousStateAction;
  @Nullable private ComponentListener exitSelectedAction;

  public GameOverState(
      GameContainer gameContainer,
      StateBasedGame game,
      ExitTemporaryStateAction exitGameOverStateAction) throws SlickException {
    super(gameContainer);
    this.game = game;
    this.exitGameOverStateAction = exitGameOverStateAction;
    this.retrySelection =
        new MenuSelection(gameContainer, gameContainer.getGraphics().getFont(), "Retry");
    this.exitSelection =
        new MenuSelection(gameContainer, gameContainer.getGraphics().getFont(), "Exit");

    TextComponent
        title =
        new TextComponent(gameContainer, gameContainer.getGraphics().getFont(), "Game Over");

    directionalPanel =
        new DirectionalPanel(gameContainer,
            gameContainer.getWidth() / 2,
            (gameContainer.getHeight() - 200) / 3,
            50);
    menuSelections = new MenuSelectionGroup();

    directionalPanel.addChild(title);
    directionalPanel.addChild(retrySelection);
    directionalPanel.addChild(exitSelection);
    menuSelections.add(retrySelection);
    menuSelections.add(exitSelection);
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    directionalPanel.render(container, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {

  }

  @Override
  public void keyPressed(int key, char c) {
    switch (key) {
      case Input.KEY_ENTER:
        menuSelections.getCurrentSelection().activate();
        break;
      case Input.KEY_DOWN:
        menuSelections.selectNext();
        break;
      case Input.KEY_UP:
        menuSelections.selectPrevious();
        break;
    }
  }

  @Override
  public void setPrecedingState(PrecedingState state) {
    if (returnToPreviousStateAction != null) {
      retrySelection.removeListener(returnToPreviousStateAction);
    }
    if (exitSelectedAction != null) {
      exitSelection.removeListener(exitSelectedAction);
    }

    returnToPreviousStateAction = new ChangeStateAction<>(state.getClass(), game);
    exitSelectedAction = listener -> {
      state.exitingFollowingState();
      exitGameOverStateAction.exitTemporaryState();
    };

    retrySelection.addListener(returnToPreviousStateAction);
    exitSelection.addListener(exitSelectedAction);
  }
}
