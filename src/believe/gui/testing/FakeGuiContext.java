package believe.gui.testing;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.opengl.ImageData;

/**
 * Fake implementation of {@link GUIContext} for use within tests.
 */
public final class FakeGuiContext implements GUIContext {
  private static final Input INPUT = new Input(0);
  private static final Font DEFAULT_FONT = new Font() {
    @Override
    public int getWidth(String s) {
      return 0;
    }

    @Override
    public int getHeight(String s) {
      return 0;
    }

    @Override
    public int getLineHeight() {
      return 0;
    }

    @Override
    public void drawString(float v, float v1, String s) {

    }

    @Override
    public void drawString(float v, float v1, String s, Color color) {

    }

    @Override
    public void drawString(float v, float v1, String s, Color color, int i, int i1) {

    }
  };

  private final int width;
  private final int height;

  public FakeGuiContext(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public Input getInput() {
    return INPUT;
  }

  @Override
  public long getTime() {
    return 0;
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
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Font getDefaultFont() {
    return DEFAULT_FONT;
  }

  @Override
  public void setMouseCursor(String s, int i, int i1) throws SlickException {

  }

  @Override
  public void setMouseCursor(ImageData imageData, int i, int i1) throws SlickException {

  }

  @Override
  public void setMouseCursor(Cursor cursor, int i, int i1) throws SlickException {

  }

  @Override
  public void setDefaultMouseCursor() {

  }
}
