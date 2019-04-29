package believe.gamestate;

import believe.app.proto.GameOptionsProto.GameOptions;
import believe.app.proto.GameOptionsProto.GameplayOptions;
import believe.datamodel.MutableDataCommitter;
import believe.gui.MenuSelection;
import believe.gui.NumberPicker;
import believe.gui.VerticalKeyboardScrollpanel;
import javax.inject.Inject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class OptionsMenuState extends GameStateBase {
  private boolean scrollPanelFocused;
  private MenuSelection back;
  private VerticalKeyboardScrollpanel scrollPanel;

  @Inject
  public OptionsMenuState(
      GameContainer container, StateBasedGame game, MutableDataCommitter<GameOptions> gameOptions) {
    int cWidth = container.getWidth();
    int cHeight = container.getHeight();
    scrollPanelFocused = false;

    back =
        new MenuSelection(
            container,
            container.getGraphics().getFont(),
            cWidth / 80,
            cHeight / 80,
            cWidth / 4,
            cHeight / 12,
            "Back");
    scrollPanel =
        new VerticalKeyboardScrollpanel(
            container,
            (int) (cWidth * 0.37),
            cHeight / 80,
            (int) (cWidth * 0.6),
            cHeight / 8,
            (int) (cHeight * 0.95));

    final NumberPicker flowSpeed =
        new NumberPicker(
            container,
            container.getGraphics().getFont(),
            "Flow Speed",
            gameOptions.get().getGameplayOptions().getFlowSpeed(),
            1,
            20);
    flowSpeed.addListener(
        (component) -> {
          if (scrollPanelFocused) {
            gameOptions.update(
                gameOptions
                    .get()
                    .toBuilder()
                    .mergeGameplayOptions(
                        GameplayOptions.newBuilder().setFlowSpeed(flowSpeed.getValue()).build())
                    .build());
          }

          scrollPanelFocused = !scrollPanelFocused;
        });

    scrollPanel.addChild(flowSpeed);
    back.addListener(
        (component) -> {
          gameOptions.commit();
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
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);

    if (back.isSelected()) {
      back.toggleSelect();
      scrollPanel.reset();
    }
  }

  @Override
  public void render(GameContainer context, StateBasedGame game, Graphics g) throws SlickException {
    back.render(context, g);
    scrollPanel.render(context, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {}

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {}
}
