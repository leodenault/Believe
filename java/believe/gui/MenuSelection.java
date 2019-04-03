package believe.gui;

import believe.audio.Sound;
import org.newdawn.slick.Font;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class MenuSelection extends TextComponent {

  private static final ColorSet INACTIVE = new ColorSet(0x0cffb2, 0xcf0498, 0xcf0498);
  private static final ColorSet ACTIVE = new ColorSet(0xff0000, 0xffffff, 0xffffff);
  private static final String DEFAULT_SELECTION_SOUND = "res/sfx/selection.ogg";
  private static final String DEFAULT_ACTIVATION_SOUND = "res/sfx/selection_picked.ogg";

  private Sound selectionSound;

  protected Sound activationSound;

  public MenuSelection(GUIContext container, Font font, String text) {
    this(container, font, 0, 0, 0, 0, text);
  }

  public MenuSelection(
      GUIContext container, Font font, int x, int y, int width, int height, String text) {
    this(
        container,
        font,
        x,
        y,
        width,
        height,
        text,
        new Sound(DEFAULT_SELECTION_SOUND),
        new Sound(DEFAULT_ACTIVATION_SOUND));
  }

  /** Used for testing. */
  protected MenuSelection(
      GUIContext container,
      Font font,
      int x,
      int y,
      int width,
      int height,
      String text,
      Sound selectionSound,
      Sound activationSound) {
    super(container, font, x, y, width, height, text);
    this.colorSet = INACTIVE;
    this.selectionSound = selectionSound;
    this.activationSound = activationSound;
  }

  public void toggleSelect() {
    toggleSelect(false);
  }

  public void toggleSelect(boolean silent) {
    if (colorSet.equals(INACTIVE)) {
      colorSet = ACTIVE;

      if (!silent) {
        this.selectionSound.play();
      }
    } else {
      colorSet = INACTIVE;
    }
  }

  public void activate() {
    activationSound.play();
    for (Object listener : listeners) {
      ((ComponentListener) listener).componentActivated(this);
    }
  }

  public boolean isSelected() {
    return colorSet.equals(ACTIVE);
  }
}
