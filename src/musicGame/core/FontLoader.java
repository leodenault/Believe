package musicGame.core;

import java.awt.Font;
import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;

public class FontLoader {
  private static FontLoader INSTANCE;

  private HashMap<String, TrueTypeFont> fonts;

  private FontLoader() {
    fonts = new HashMap<String, TrueTypeFont>();
  }

  public static FontLoader getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new FontLoader();
    }

    return INSTANCE;
  }

  public void load() {
    fonts.put("verdana", new TrueTypeFont(new Font("Verdana", Font.PLAIN, 20), true));
  }

  public TrueTypeFont getFont(String name) {
    return fonts.get(name);
  }
}
