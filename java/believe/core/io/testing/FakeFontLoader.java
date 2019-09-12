package believe.core.io.testing;

import believe.core.io.FontLoader;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

/** A fake {@link FontLoader} used in testing. */
public final class FakeFontLoader extends FontLoader {
  private static final Font FONT =
      new Font() {
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
        public void drawString(float v, float v1, String s) {}

        @Override
        public void drawString(float v, float v1, String s, Color color) {}

        @Override
        public void drawString(float v, float v1, String s, Color color, int i, int i1) {}
      };

  public FakeFontLoader() {
    super(500, 500);
  }

  @Override
  public Font getBaseFont() {
    return FONT;
  }

  @Override
  public Font getBaseFontAtSize(float size) {
    return FONT;
  }
}
