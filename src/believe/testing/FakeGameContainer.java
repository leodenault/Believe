package believe.testing;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;

public class FakeGameContainer extends GameContainer {
  public FakeGameContainer(Game game) {
    super(game);
  }

  @Override
  public int getScreenWidth() {
    return 0;
  }

  @Override
  public int getScreenHeight() {
    return 0;
  }

  @Override
  public void setIcon(String s) throws SlickException {

  }

  @Override
  public void setIcons(String[] strings) throws SlickException {

  }

  @Override
  public void setMouseCursor(String s, int i, int i1) throws SlickException {

  }

  @Override
  public void setMouseCursor(ImageData imageData, int i, int i1) throws SlickException {

  }

  @Override
  public void setMouseCursor(Image image, int i, int i1) throws SlickException {

  }

  @Override
  public void setMouseCursor(Cursor cursor, int i, int i1) throws SlickException {

  }

  @Override
  public void setDefaultMouseCursor() {

  }

  @Override
  public void setMouseGrabbed(boolean b) {

  }

  @Override
  public boolean isMouseGrabbed() {
    return false;
  }

  @Override
  public boolean hasFocus() {
    return false;
  }

  @Override
  public long getTime() {
    return 0;
  }

  @Override
  public Input getInput() {
    return new FakeInput(0);
  }
}
