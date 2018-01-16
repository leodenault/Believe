package musicGame.gamestate;

import musicGame.gamestate.base.GameStateBase;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import musicGame.core.action.ChangeStateAction;
import musicGame.gui.DirectionalPanel;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;

public class MainMenuState extends GameStateBase {

  private DirectionalPanel panel;
  private MenuSelectionGroup selections;

  public MainMenuState(GameContainer container, StateBasedGame game) throws SlickException {
    MenuSelection playPlatformingLevel = new MenuSelection(container, "Play Platforming Level");
    MenuSelection playArcadeLevel = new MenuSelection(container, "Play Arcade Level");
    MenuSelection playFlowFile = new MenuSelection(container, "Play Flow File");
    MenuSelection options = new MenuSelection(container, "Options");
    MenuSelection exit = new MenuSelection(container, "Exit");
    panel = new DirectionalPanel(
        container, container.getWidth() / 2, (container.getHeight() - 250) / 5, 50);
    panel.addChild(playPlatformingLevel);
    panel.addChild(playArcadeLevel);
    panel.addChild(playFlowFile);
    panel.addChild(options);
    panel.addChild(exit);

    playPlatformingLevel.addListener(new ChangeStateAction(PlatformingState.class, game));
    playArcadeLevel.addListener(new ChangeStateAction(ArcadeState.class, game));
    playFlowFile.addListener(new ChangeStateAction(FlowFilePickerMenuState.class, game));
    options.addListener(new ChangeStateAction(OptionsMenuState.class, game));
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
  public void init(final GameContainer container, StateBasedGame game)
      throws SlickException {
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    panel.render(container, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game)
      throws SlickException {
    super.enter(container, game);
    this.selections.select(0);
  }
}
