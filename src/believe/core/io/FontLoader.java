package believe.core.io;

import org.newdawn.slick.TrueTypeFont;

import java.awt.Font;
import java.awt.image.SinglePixelPackedSampleModel;

public class FontLoader {
  // We use a linear relationship between the fonts we want at different resolutions.
  private static final int FONT_SIZE_1 = 20;
  private static final int FONT_SIZE_2 = 30;
  private static final int RESOLUTION_1 = 800 * 600;
  private static final int RESOLUTION_2 = 1920 * 1080;
  private static final float
      FONT_SIZE_FUNCTION_SLOPE =
      (float) (FONT_SIZE_2 - FONT_SIZE_1) / (RESOLUTION_2 - RESOLUTION_1);
  private static final float
      FONT_SIZE_FUNCTION_CONSTANT =
      FONT_SIZE_2 - FONT_SIZE_FUNCTION_SLOPE * RESOLUTION_2;

  private Font baseFont;
  private TrueTypeFont verdana;
  private float baseSize;

  public FontLoader(int screenWidth, int screenHeight) {
    baseSize = calculateFontSize(screenWidth, screenHeight);
  }

  public void load() {
    baseFont = new Font("verdana", Font.PLAIN, (int) baseSize);
    verdana = new TrueTypeFont(baseFont, /* antiAlias= */ true);
  }

  public void resetFontsForScreenDimensions(int screenWidth, int screenHeight) {
    baseSize = calculateFontSize(screenWidth, screenHeight);
  }

  public TrueTypeFont getBaseFont() {
    return verdana;
  }

  public TrueTypeFont getBaseFontAtSize(float size) {
    return new TrueTypeFont(baseFont.deriveFont(size), /* antiAlias= */ true);
  }

  private static int calculateFontSize(int screenWidth, int screenHeight) {
    return (int) (
        FONT_SIZE_FUNCTION_SLOPE * screenWidth * screenHeight
            + FONT_SIZE_FUNCTION_CONSTANT);
  }
}
