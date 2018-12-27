package believe.core.io;

import java.awt.Font;
import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;

public class FontLoader {
  interface FontFactory {
    Font create();
  }

  private static FontLoader INSTANCE;

  private Font baseFont;
  private TrueTypeFont verdana;

  private FontLoader() {}

  public static FontLoader getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FontLoader();
    }

    return INSTANCE;
  }

  public void load() {
    baseFont = new Font("verdana", Font.PLAIN, 20);
    verdana = new TrueTypeFont(new Font("verdana", Font.PLAIN, 20), /* antiAlias= */ true);
  }

  public TrueTypeFont getBaseFont() {
    return verdana;
  }

  public TrueTypeFont getBaseFontAtSize(float size) {
    return new TrueTypeFont(baseFont.deriveFont(size), /* antiAlias= */ true);
  }
}
