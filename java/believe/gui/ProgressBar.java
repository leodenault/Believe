package believe.gui;

import believe.gui.TextComponent.ColorSet;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class ProgressBar extends AbstractContainer {

  private static final Color DEFAULT_FOREGROUND_COLOUR = new Color(0x0066ff);
  private static final Color DEFAULT_BACKGROUND_COLOUR = new Color(0x000000);
  private static final ColorSet DEFAULT_TEXT_COLOR = new ColorSet(0xffffff);
  private static final int DEFAULT_PADDING = 2;

  private int padding;
  private float progress;
  private Color foreground;
  private Color background;
  private TextComponent text;

  public ProgressBar(GUIContext container, Font font) {
    this(container, font, 0, 0, 0, 0, "");
  }

  public ProgressBar(
      GUIContext container, Font font, int x, int y, int width, int height, String text) {
    super(container, x, y, width, height);
    this.padding = DEFAULT_PADDING;
    this.progress = 0f;
    this.foreground = DEFAULT_FOREGROUND_COLOUR;
    this.background = DEFAULT_BACKGROUND_COLOUR;
    this.text = new TextComponent(container, font, x, y, width, height, text);
    this.text.setColorSet(DEFAULT_TEXT_COLOR);
    this.children.add(this.text);
  }

  public void setProgress(float progress) {
    if (progress < 0f) {
      progress = 0f;
    } else if (progress > 1f) {
      progress = 1f;
    }

    this.progress = progress;
  }

  public void setText(String text) {
    this.text.setText(text);
  }

  public void setBorderSize(int borderSize) {
    text.setBorderSize(borderSize);
  }

  public void setTextPadding(int textPadding) {
    text.setTextPadding(textPadding);
  }

  @Override
  public void setHeight(int height) {
    super.setHeight(height);
    text.setHeight(height);
  }

  @Override
  public void setWidth(int width) {
    super.setWidth(width);
    text.setWidth(width);
  }

  @Override
  public void resetLayout() {}

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    float x = getX();
    float y = getY();
    float width = getWidth();
    float height = getHeight();

    g.setColor(foreground);
    g.setLineWidth(padding);
    g.drawRect(x, y, width, height);

    g.setColor(background);
    g.fillRect(x, y, width, height);

    g.setColor(foreground);
    g.fillRect(x, y, width * progress, height);

    text.render(context, g);
  }
}
