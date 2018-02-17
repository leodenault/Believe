package believe.gamestate;

import java.io.File;
import java.io.IOException;

import believe.gui.MenuSelection;
import believe.gui.TextComponent;
import believe.gui.VerticalKeyboardScrollpanel;
import believe.util.Util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class FlowFilePickerMenuState extends GameStateBase {

  private MenuSelection back;
  private TextComponent noFilesMessage;
  private VerticalKeyboardScrollpanel scrollPanel;

  public FlowFilePickerMenuState(GameContainer container, StateBasedGame game) throws SlickException {
    int cWidth = container.getWidth();
    int cHeight = container.getHeight();

    back = new MenuSelection(container, cWidth / 80, cHeight / 80, cWidth / 4, cHeight / 12, "Back");
    noFilesMessage =
        new TextComponent(container, (int)(cWidth * 0.37), cHeight / 80, (int)(cWidth * 0.6), cHeight / 8, "");
    scrollPanel = new VerticalKeyboardScrollpanel(container, (int)(cWidth * 0.37), cHeight / 80,
        (int)(cWidth * 0.6), cHeight / 8, (int)(cHeight * 0.95));

    back.addListener(new ChangeStateAction(MainMenuState.class, game));
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);
    switch (key) {
      case Input.KEY_LEFT:
      case Input.KEY_RIGHT:
        if (this.scrollPanel.isRendering()) {
          this.back.toggleSelect();
          this.scrollPanel.toggleFocus();
        }
        break;
      case Input.KEY_ENTER:
        if (this.back.isSelected()) {
          this.back.activate();
        } else {
          this.scrollPanel.activateSelection();
        }
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

  @Override
  public void init(GameContainer container, StateBasedGame game)
      throws SlickException {
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game)
      throws SlickException {
    super.enter(container, game);

    try {
      File[] files = Util.getFlowFiles();

      if (files == null || files.length == 0) {
        this.noFilesMessage.setText("Looks like there aren't any flow files to load!");
        this.noFilesMessage.setRendering(true);
        this.scrollPanel.setRendering(false);
        if (!this.back.isSelected()) {
          this.back.toggleSelect();
        }
      } else {
        this.noFilesMessage.setRendering(false);
        this.scrollPanel.setRendering(true);
        this.scrollPanel.clear();
        if (this.back.isSelected()) {
          this.back.toggleSelect();
        }

        for (File file : files) {
          final String name = file.getName().substring(0, file.getName().lastIndexOf("."));

          MenuSelection selection = new MenuSelection(container, name);
          selection.addListener(
              new ExternalLoadGameAction(
                  PlayFlowFileState.class, file.getCanonicalPath(), game));
          scrollPanel.addChild(selection);
        }
      }
    } catch (SecurityException | IOException e) {
      this.noFilesMessage.setText("Something went wrong when trying to find the flow files!");
      this.noFilesMessage.setRendering(true);
      this.scrollPanel.setRendering(false);
      this.scrollPanel.clear();
      this.back.toggleSelect(); // We know it's not selected, so we select it here
    }
  }

  @Override
  public void render(GameContainer context, StateBasedGame game, Graphics g)
      throws SlickException {
    back.render(context, g);
    noFilesMessage.render(context, g);
    scrollPanel.render(context, g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {

  }
}
