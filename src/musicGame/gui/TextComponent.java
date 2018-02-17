package musicGame.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import musicGame.util.Util;

public class TextComponent extends ComponentBase {

  protected static final class ColorSet {
    public boolean textOnly;
    public int color;
    public int borderColor;
    public int textColor;

    public ColorSet(int textColor) {
      this(0x000000, 0x000000, textColor, true);
    }

    public ColorSet(int color, int borderColor, int textColor) {
      this(color, borderColor, textColor, false);
    }

    public ColorSet(int color, int borderColor, int textColor, boolean textOnly) {
      this.color = color;
      this.borderColor = borderColor;
      this.textColor = textColor;
      this.textOnly = textOnly;
    }
  }

  private static final int DEFAULT_PADDING = 5;
  private static final ColorSet DEFAULT_COLOR_SET = new ColorSet(0x000000, 0x898989, 0xffffff);

  protected int padding;
  protected String text;
  protected ColorSet colorSet;

  public TextComponent(GUIContext container, String text) {
    this(container, 0, 0, 0, 0, text);
  }

  public TextComponent(GUIContext container, int x, int y, int width, int height, String text) {
    super(container, x, y, width, height);
    this.text = text;
    this.colorSet = DEFAULT_COLOR_SET;
    this.padding = DEFAULT_PADDING;
  }

  public void setColorSet(ColorSet set) {
    this.colorSet = set;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) {
    if (!colorSet.textOnly) {
      drawButton(context, g);
    }
    drawText(context, g);
  }

  private void drawButton(GUIContext context, Graphics g) {
    // Colour the button
    g.setColor(new Color(colorSet.color));
    g.fill(rect);

    // Colour the border
    g.setColor(new Color(colorSet.borderColor));
    g.setLineWidth(padding);
    g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }

  protected void drawText(GUIContext context, Graphics g) {
    Rectangle oldClip = Util.changeClipContext(g, rect);

    // Draw the text
    g.setColor(new Color(colorSet.textColor));
    int textWidth = g.getFont().getWidth(text);
    int textHeight = g.getFont().getHeight(text);
    g.pushTransform();
    g.translate(-textWidth/2, -textHeight/2);
    g.drawString(text, rect.getCenterX(), rect.getCenterY());
    g.popTransform();

    Util.resetClipContext(g, oldClip);
  }

  @Override
  public void resetLayout() {}
}
