package believe.gui;

import believe.audio.Sound;
import believe.audio.SoundImpl;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.GUIContext;

import java.util.Timer;
import java.util.TimerTask;

public class NumberPicker extends MenuSelection {

  @SuppressWarnings("serial")
  private static final Polygon ARROW =
      new Polygon() {
        {
          addPoint(-0.5f, 0);
          addPoint(0.5f, -1);
          addPoint(0.5f, 1);
        }
      };

  private static final Color ARROW_DEPRESSED = Color.white;
  private static final Color ARROW_PRESSED = new Color(0x999999);
  private static final int PRESS_DELAY = 100;
  private static final String DEFAULT_PRESS_SOUND = "res/sfx/tick.ogg";

  private boolean activated;
  private boolean leftPressed;
  private boolean rightPressed;
  private int value;
  private int min;
  private int max;
  private Sound pressSound;

  public NumberPicker(GUIContext container, Font font, String text, int value) {
    this(container, font, text, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public NumberPicker(GUIContext container, Font font, String text, int value, int min, int max) {
    this(container, font, 0, 0, 0, 0, text, value, min, max);
  }

  public NumberPicker(
      GUIContext container,
      Font font,
      int x,
      int y,
      int width,
      int height,
      String text,
      int value) {
    this(container, font, x, y, width, height, text, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public NumberPicker(
      GUIContext container,
      Font font,
      int x,
      int y,
      int width,
      int height,
      String text,
      int value,
      int min,
      int max) {
    super(container, font, x, y, width, height, text);
    this.value = value;
    this.activated = false;
    this.min = min;
    this.max = max;
    this.pressSound = new SoundImpl(DEFAULT_PRESS_SOUND);
  }

  /** Used for testing. */
  protected NumberPicker(
      GUIContext container,
      Font font,
      int x,
      int y,
      int width,
      int height,
      String text,
      int value,
      int min,
      int max,
      Sound selectionSound,
      Sound activationSound,
      Sound pressSound) {
    super(container, font, x, y, width, height, text, selectionSound, activationSound);
    this.value = value;
    this.activated = false;
    this.min = min;
    this.max = max;
    this.pressSound = pressSound;
  }

  public int getValue() {
    return value;
  }

  @Override
  public void activate() {
    super.activate();
    activated = !activated;
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);

    if (activated) {
      if (key == Input.KEY_LEFT && value > min) {
        value--;
        pressSound.play();
        schedulePressTimer(true, leftPressed);
      } else if (key == Input.KEY_RIGHT && value < max) {
        value++;
        pressSound.play();
        schedulePressTimer(false, rightPressed);
      }
    }
  }

  @Override
  protected int calculateYPosition() {
    return (int) rect.getCenterY() - textHeight;
  }

  @Override
  protected void drawAuxiliaryText(Graphics g, Font font) {
    // Draw the value
    String stringValue = String.valueOf(value);
    int valueWidth = font.getWidth(stringValue);
    int valueHeight = font.getHeight(stringValue);

    g.pushTransform();
    g.translate(-valueWidth / 2, valueHeight);
    g.drawString(stringValue, rect.getCenterX(), rect.getCenterY());
    g.popTransform();

    // Draw the plus and minus signs if the component is active
    if (activated) {

      if (value > min) {
        g.setColor(arrowColor(leftPressed));
        Shape left =
            ARROW
                .transform(
                    Transform.createScaleTransform(
                        rect.getWidth() / 20, rect.getHeight() * 0.75f / ARROW.getHeight()))
                .transform(
                    Transform.createTranslateTransform(
                        rect.getX() + rect.getWidth() / 10, rect.getCenterY()));
        g.fill(left);
      }

      if (value < max) {
        g.setColor(arrowColor(rightPressed));
        Shape right =
            ARROW
                .transform(
                    Transform.createScaleTransform(
                        -rect.getWidth() / 20, rect.getHeight() * 0.75f / ARROW.getHeight()))
                .transform(
                    Transform.createTranslateTransform(
                        rect.getMaxX() - rect.getWidth() / 10, rect.getCenterY()));
        g.fill(right);
      }
    }
  }

  private void schedulePressTimer(final boolean left, boolean pressed) {
    if (!pressed) {
      if (left) {
        leftPressed = true;
      } else {
        rightPressed = true;
      }
      new Timer()
          .schedule(
              new TimerTask() {
                @Override
                public void run() {
                  if (left) {
                    leftPressed = false;
                  } else {
                    rightPressed = false;
                  }
                }
              },
              PRESS_DELAY);
    }
  }

  private Color arrowColor(boolean pressed) {
    return pressed ? ARROW_PRESSED : ARROW_DEPRESSED;
  }
}
