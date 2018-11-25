package believe.gamestate;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import believe.core.Options;
import believe.gui.MenuSelection;
import believe.gui.NumberPicker;
import believe.gui.VerticalKeyboardScrollpanel;

public class OptionsMenuState extends GameStateBase {

  private boolean scrollPanelFocused;
  private MenuSelection back;
  private VerticalKeyboardScrollpanel scrollPanel;

  public OptionsMenuState(GameContainer container, StateBasedGame game) throws SlickException {
    int cWidth = container.getWidth();
    int cHeight = container.getHeight();
    final Options options = Options.getInstance();
    scrollPanelFocused = false;

    back = new MenuSelection(container, cWidth / 80, cHeight / 80, cWidth / 4, cHeight / 12, "Back");
    scrollPanel = new VerticalKeyboardScrollpanel(container, (int)(cWidth * 0.37), cHeight / 80,
        (int)(cWidth * 0.6), cHeight / 8, (int)(cHeight * 0.95));

    final NumberPicker flowSpeed = new NumberPicker(container, "Flow Speed", options.flowSpeed, 1, 20);
    flowSpeed.addListener((component) -> {
      if (scrollPanelFocused) {
        options.flowSpeed = flowSpeed.getValue();
      }

      scrollPanelFocused = !scrollPanelFocused;
    });

    scrollPanel.addChild(flowSpeed);
    back.addListener((component) -> {
      options.save();
      new ChangeStateAction<>(MainMenuState.class, game).componentActivated(component);
    });
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);
    if (key == Input.KEY_ENTER) {
      if (this.back.isSelected()) {
        this.back.activate();
      } else {
        this.scrollPanel.activateSelection();
      }
    }

    if (!scrollPanelFocused) {
      switch (key) {
        case Input.KEY_LEFT:
        case Input.KEY_RIGHT:
          this.back.toggleSelect();
          this.scrollPanel.toggleFocus();
          break;
        case Input.KEY_UP:
          if (!this.back.isSelected()) {
            this.scrollPanel.scrollUp();
          }
          break;
        case Input.KEY_DOWN:
          if (!this.back.isSelected()) {
            this.scrollPanel.scrollDown();
          }
          break;
      }
    }
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game)
      throws SlickException {
    super.enter(container, game);

    if (back.isSelected()) {
      back.toggleSelect();
      scrollPanel.reset();
    }
  }

  @Override
  public void render(GameContainer context, StateBasedGame game, Graphics g)
      throws SlickException {
    back.render(context, g);
    scrollPanel.render(context, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
  }

}
