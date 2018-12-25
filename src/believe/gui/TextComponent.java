package believe.gui;

import believe.util.Util;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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

  public enum WrappingMode {
    OVERFLOW, WRAP
  }

  public enum HorizontalTextAlignment {
    LEFT, CENTERED
  }

  public enum VerticalTextAlignment {
    TOP, MIDDLE
  }

  private static final int DEFAULT_PADDING = 5;
  private static final ColorSet DEFAULT_COLOR_SET = new ColorSet(0x000000, 0x898989, 0xffffff);

  private final Font font;
  private final List<String> textFragments;

  private WrappingMode wrappingMode;
  private HorizontalTextAlignment horizontalTextAlignment;
  private VerticalTextAlignment verticalTextAlignment;

  protected int padding;
  protected ColorSet colorSet;
  protected int textHeight;

  public TextComponent(GUIContext container, Font font, String text) {
    this(container, font, 0, 0, 0, 0, text);
  }

  public TextComponent(
      GUIContext container, Font font, int x, int y, int width, int height, String text) {
    super(container, x, y, width, height);
    this.font = font;

    this.textFragments = new ArrayList<>();
    this.wrappingMode = WrappingMode.WRAP;
    this.horizontalTextAlignment = HorizontalTextAlignment.CENTERED;
    this.verticalTextAlignment = VerticalTextAlignment.MIDDLE;
    this.textHeight = calculateTextHeight(this.font);
    this.colorSet = DEFAULT_COLOR_SET;
    this.padding = DEFAULT_PADDING;
    chopText(text);
  }

  public void setColorSet(ColorSet set) {
    this.colorSet = set;
  }

  public void setText(String text) {
    chopText(text);
    this.textHeight = calculateTextHeight(font);
  }

  public void setWrappingMode(WrappingMode wrappingMode) {
    this.wrappingMode = wrappingMode;
    chopText(String.join(" ", textFragments));
  }

  public void setHorizontalTextAlignment(HorizontalTextAlignment horizontalTextAlignment) {
    this.horizontalTextAlignment = horizontalTextAlignment;
    chopText(String.join(" ", textFragments));
  }

  public void setVerticalTextAlignment(VerticalTextAlignment verticalTextAlignment) {
    this.verticalTextAlignment= verticalTextAlignment;
    chopText(String.join(" ", textFragments));
  }

  @Override
  public void setWidth(int width) {
    super.setWidth(width);
    chopText(String.join(" ", textFragments));
    textHeight = calculateTextHeight(font);
  }

  @Override
  public void resetLayout() {}

  @Override
  protected void renderComponent(GUIContext context, Graphics graphics) {
    if (!colorSet.textOnly) {
      drawButton(context, graphics);
    }

    Rectangle oldClip = Util.changeClipContext(graphics, rect);
    Font previousFont = graphics.getFont();
    graphics.setFont(font);
    graphics.setColor(new Color(colorSet.textColor));
    layoutChoppedText(graphics, createXPositionCalculator(), calculateYPosition());
    drawAuxiliaryText(graphics, font);
    graphics.setFont(previousFont);
    Util.resetClipContext(graphics, oldClip);
  }

  protected void drawAuxiliaryText(Graphics graphics, Font font) {
    layoutChoppedText(graphics, createXPositionCalculator(), calculateYPosition());
  }

  protected Function<String, Integer> createXPositionCalculator() {
    switch (horizontalTextAlignment) {
      case CENTERED:
        return textFragment -> (int) rect.getCenterX() - font.getWidth(textFragment) / 2;
      case LEFT:
      default:
        return textFragment -> getX();
    }
  }

  protected int calculateYPosition() {
    switch (verticalTextAlignment) {
      case MIDDLE:
        return (int) rect.getCenterY() - textHeight / 2;
      case TOP:
      default:
        return getY();
    }
  }

  protected void layoutChoppedText(
      Graphics g, Function<String, Integer> xPositionCalculator, int y) {
    int cumulativeHeight = 0;

    for (String choppedText : textFragments) {
      g.drawString(choppedText, xPositionCalculator.apply(choppedText), y + cumulativeHeight);
      cumulativeHeight += g.getFont().getHeight(choppedText);
    }
  }

  private int calculateTextHeight(Font font) {
    return textFragments.stream().mapToInt(font::getHeight).sum();
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

  private void chopText(String text) {
    textFragments.clear();

    if (wrappingMode == WrappingMode.OVERFLOW) {
      textFragments.add(text);
      return;
    }

    String remainingText = text;
    String fragment;
    while (!(fragment = generateFragment(remainingText)).equals(remainingText)) {
      textFragments.add(fragment);
      remainingText = remainingText.substring(fragment.length() + 1);
    }
    textFragments.add(remainingText);
  }

  private String generateFragment(String text) {
    int textWidth = font.getWidth(text);
    int componentWidth = getWidth();
    if (textWidth <= componentWidth || componentWidth <= 0) {
      return text;
    }

    // Find the index in the string that guarantees the string text won't exceed the width of the
    // component.
    int sliceIndex = (int) ((((float) componentWidth) / textWidth) * text.length());
    String fragment = text.substring(0, sliceIndex);
    while (font.getWidth(fragment) > componentWidth) {
      fragment = text.substring(0, --sliceIndex);
    }

    // Make sure to only slice the string on spaces. Words should ideally be fully within view
    // unless the word itself is wider than the width of the component.
    if (text.charAt(sliceIndex) != ' ') {
      sliceIndex--;
      while (sliceIndex > -1 && text.charAt(sliceIndex) != ' ') {
        sliceIndex--;
      }

      // If we reach index 0, then we know the word is wider than the width of the component. We
      // have no choice but to clip the word.
      if (sliceIndex < 0) {
        sliceIndex++;
        while (sliceIndex < text.length() && text.charAt(sliceIndex) != ' ') {
          sliceIndex++;
        }
      }
      fragment = text.substring(0, sliceIndex);
    }

    return fragment;
  }
}
