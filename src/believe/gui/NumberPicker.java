package believe.gui;

import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.GUIContext;

import believe.util.Util;


public class NumberPicker extends MenuSelection {

  @SuppressWarnings("serial")
  private static final Polygon ARROW = new Polygon() {{
    addPoint(-0.5f, 0);
    addPoint(0.5f, -1);
    addPoint(0.5f, 1);
  }};
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

  public NumberPicker(GUIContext container, String text, int value) throws SlickException {
    this(container, text, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public NumberPicker(GUIContext container, String text, int value, int min, int max) throws SlickException {
    this(container, 0, 0, 0, 0, text, value, min, max);
  }

  public NumberPicker(GUIContext container, int x, int y, int width,
      int height, String text, int value) throws SlickException {
    this(container, x, y, width, height, text, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public NumberPicker(GUIContext container, int x, int y, int width,
      int height, String text, int value, int min, int max) throws SlickException {
    super(container, x, y, width, height, text);
    this.value = value;
    this.activated = false;
    this.min = min;
    this.max = max;
    this.pressSound = new Sound(DEFAULT_PRESS_SOUND);
  }

  /**
   * Used for testing.
   */
  protected NumberPicker(GUIContext container, int x, int y, int width,
      int height, String text, int value, int min, int max,
      Sound selectionSound,  Sound activationSound, Sound pressSound) throws SlickException {
    super(container, x, y, width, height, text, selectionSound, activationSound);
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
  protected void drawText(GUIContext context, Graphics g) {
      Rectangle oldClip = Util.changeClipContext(g, rect);

      // Draw the text
      g.setColor(new Color(colorSet.textColor));
      int textWidth = g.getFont().getWidth(text);
      int textHeight = g.getFont().getHeight(text);

      g.pushTransform();
      g.translate(-textWidth/2, -textHeight);
      g.drawString(text, rect.getCenterX(), rect.getCenterY());
      g.popTransform();

      // Draw the value
      String stringValue = String.valueOf(value);
      int valueWidth = g.getFont().getWidth(stringValue);
      int valueHeight = g.getFont().getHeight(stringValue);

      g.pushTransform();
      g.translate(-valueWidth/2, valueHeight);
      g.drawString(stringValue, rect.getCenterX(), rect.getCenterY());
      g.popTransform();

      // Draw the plus and minus signs if the component is active
      if (activated) {

        if (value > min) {
          g.setColor(arrowColor(leftPressed));
          Shape left = ARROW.transform(Transform.createScaleTransform(rect.getWidth() / 20, rect.getHeight() * 0.75f / ARROW.getHeight()))
              .transform(Transform.createTranslateTransform(rect.getX() + rect.getWidth() / 10, rect.getCenterY()));
          g.fill(left);
        }

        if (value < max) {
          g.setColor(arrowColor(rightPressed));
          Shape right = ARROW.transform(Transform.createScaleTransform(-rect.getWidth() / 20, rect.getHeight() * 0.75f / ARROW.getHeight()))
              .transform(Transform.createTranslateTransform(rect.getMaxX() - rect.getWidth() / 10, rect.getCenterY()));
          g.fill(right);
        }
      }

      Util.resetClipContext(g, oldClip);
  }

  private void schedulePressTimer(final boolean left, boolean pressed) {
    if (!pressed) {
      if (left) {
        leftPressed = true;
      } else {
        rightPressed = true;
      }
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          if (left) {
            leftPressed = false;
          } else {
            rightPressed = false;
          }
        }
      }, PRESS_DELAY);
    }
  }

  private Color arrowColor(boolean pressed) {
    return pressed ? ARROW_PRESSED : ARROW_DEPRESSED;
  }
}
