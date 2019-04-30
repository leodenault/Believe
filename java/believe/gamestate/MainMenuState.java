package believe.gamestate;

import believe.action.ChangeStateAction;
import believe.gamestate.levelstate.arcadestate.ArcadeState;
import believe.gamestate.levelstate.platformingstate.PlatformingState;
import javax.inject.Inject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import believe.gui.DirectionalPanel;
import believe.gui.MenuSelection;
import believe.gui.MenuSelectionGroup;

public class MainMenuState extends GameStateBase {

  private DirectionalPanel panel;
  private MenuSelectionGroup selections;

  @Inject
  public MainMenuState(GameContainer container, StateBasedGame game) {
    MenuSelection playPlatformingLevel =
        new MenuSelection(container, container.getGraphics().getFont(), "Play Platforming Level");
    MenuSelection playArcadeLevel =
        new MenuSelection(container, container.getGraphics().getFont(), "Play Arcade Level");
    MenuSelection playFlowFile =
        new MenuSelection(container, container.getGraphics().getFont(), "Play Flow File");
    MenuSelection options =
        new MenuSelection(container, container.getGraphics().getFont(), "Options");
    MenuSelection exit = new MenuSelection(container, container.getGraphics().getFont(), "Exit");
    panel =
        new DirectionalPanel(
            container, container.getWidth() / 2, (container.getHeight() - 250) / 5, 50);
    panel.addChild(playPlatformingLevel);
    panel.addChild(playArcadeLevel);
    panel.addChild(playFlowFile);
    panel.addChild(options);
    panel.addChild(exit);

    playPlatformingLevel.addListener(new ChangeStateAction<>(PlatformingState.class, game));
    playArcadeLevel.addListener(new ChangeStateAction<>(ArcadeState.class, game));
    playFlowFile.addListener(new ChangeStateAction<>(FlowFilePickerMenuState.class, game));
    options.addListener(new ChangeStateAction<>(OptionsMenuState.class, game));
    exit.addListener((component) -> container.exit());

    this.selections = new MenuSelectionGroup();
    this.selections.add(playPlatformingLevel);
    this.selections.add(playArcadeLevel);
    this.selections.add(playFlowFile);
    this.selections.add(options);
    this.selections.add(exit);
  }

  @Override
  public void keyPressed(int key, char c) {
    switch (key) {
      case Input.KEY_DOWN:
        this.selections.selectNext();
        break;
      case Input.KEY_UP:
        this.selections.selectPrevious();
        break;
      case Input.KEY_ENTER:
        this.selections.getCurrentSelection().activate();
        break;
    }
  }

  @Override
  public void init(final GameContainer container, StateBasedGame game) throws SlickException {}

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    panel.render(container, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) {}

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    this.selections.select(0);
  }
}
