package musicGame.levelFlow.component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import musicGame.gui.ComponentBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.display.Camera.Layerable;
import musicGame.core.FontLoader;
import musicGame.core.SynchedComboPattern;
import musicGame.core.SynchedComboPattern.TimeKeyPair;
import musicGame.levelFlow.component.FlowComponentListener;

public class ComboSyncher extends ComponentBase implements Layerable {
  private static final Color NOT_ACTIVATED = new Color(0xffffff);
  private static final Color SUCCESS = new Color(0x00ff00);
  private static final Color MISSED = new Color(0xff0000);
  private static final Color BAR_PULSE = new Color(0xffffff);
  private static final Color BAR_NORMAL = new Color(0x0000ff);

  // Visible for testing
  protected static final float ERROR_LENGTH = 0.15f; // In seconds
  protected static final float BUFFER_TIME = 2.5f; // In seconds

  private final TrueTypeFont font;

  private int bpm;
  private float max;
  private float start;
  private float length;
  private float actionSectionLength;
  private float actionSectionWidth;
  private float startWidth;
  private float errorWidth;
  private float trackerSpeed;
  private SynchedComboPattern pattern;
  private Line tracker;
  private Music music;
  private List<TimeKeyPair> actions;
  private Queue<TimeKeyPair> actionsLeft;
  private HashMap<TimeKeyPair, Color> actionColors;

  public ComboSyncher(GUIContext container, int bpm) {
    this(container, new SynchedComboPattern(), bpm, 0, 0, 0, 0);
  }

  public ComboSyncher(GUIContext container, SynchedComboPattern pattern, int bpm, int x, int y, int width, int height) {
    this(container, pattern, FontLoader.getInstance().getFont("verdana"),
        bpm, x, y, width, height);
  }

  /**
   * Used for testing
   */
  protected ComboSyncher(GUIContext container, SynchedComboPattern pattern, TrueTypeFont font, int bpm, int x, int y, int width, int height) {
    super(container, x, y, width, height);
    this.bpm = bpm;
    this.pattern = pattern;
    this.tracker = new Line(x, y, x, y + height);
    this.font = font;
    this.actionColors = new HashMap<SynchedComboPattern.TimeKeyPair, Color>();
    setUpPattern();
  }

  private void setUpPattern() {
    actions = pattern.getActions();

    if (actions.size() > 0) {
      max = actions.get(actions.size() - 1).time;
      actionSectionLength = max * 60.0f / bpm;
    }
  }

  public void setPattern(SynchedComboPattern pattern) {
    this.pattern = pattern;
    setUpPattern();
  }

  private float getActionLocation(TimeKeyPair action) {
    return (actionSectionWidth * action.time / max) + startWidth + rect.getX();
  }

  private void checkForMissed() {
    if (!actionsLeft.isEmpty()) {
      TimeKeyPair current = actionsLeft.peek();
      if (tracker.getX() - getActionLocation(current) > errorWidth) {
        actionColors.put(current, MISSED);
        actionsLeft.remove();
        for (Object listener : listeners) {
          if (listener instanceof FlowComponentListener) {
            ((FlowComponentListener)listener).beatMissed();
          }
        }
      }
    }
  }

  private void resetCombo() {
    for (TimeKeyPair action : actions) {
      actionColors.put(action, NOT_ACTIVATED);
    }
    actionsLeft = new LinkedList<TimeKeyPair>(actions);
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) {
    if (music != null) {
      g.setLineWidth(2f);
      g.setColor(new Color(0x00aaaa));
      g.fill(rect);

      g.setLineWidth(1f);
      for (int i = 0; i < actions.get(actions.size() - 1).time + 1; i++) {
        float x = (actionSectionWidth * i / max) + startWidth + rect.getX();
        float trackerX = tracker.getX();
        float pulseLength = 4 * errorWidth;
        float start = x - pulseLength;
        float end = x + pulseLength;
        if (trackerX >= start && trackerX < end) {
          float relPosSq = (float)Math.pow(trackerX - x, 2);
          float pulseLengthSq = (float)Math.pow(pulseLength, 2);
          float progress = 1 + (relPosSq / (3 * pulseLengthSq))
              * (((4 * relPosSq) / pulseLengthSq) - 7);
          Color pulseColour = new Color(BAR_PULSE);
          Color normalColour = new Color(BAR_NORMAL);
          pulseColour.scale(progress);
          normalColour.scale(1 - progress);
          pulseColour.add(normalColour);
          g.setColor(pulseColour);
        } else {
          g.setColor(BAR_NORMAL);
        }
        g.drawLine(x, rect.getY(), x, rect.getMaxY());
      }

      g.setLineWidth(2f);
      g.setColor(new Color(0xff0000));
      g.draw(tracker);
      g.setColor(new Color(0xffffff));
      g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

      g.setFont(font);
      for (TimeKeyPair action : actions) {
        g.setColor(actionColors.get(action));
        String key = String.valueOf(Character.toUpperCase(action.key));
        int centerX = font.getWidth(String.valueOf(action.key)) / 2;
        int centerY = g.getFont().getHeight(key) / 2;
        g.drawString(key, getActionLocation(action) - centerX,
            rect.getCenterY() - centerY);
      }
    }
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);

    if (music != null && !actionsLeft.isEmpty()) {
      TimeKeyPair current = actionsLeft.peek();
        int index = actions.indexOf(current);

        if (current.key == c &&
            Math.abs(tracker.getX() - getActionLocation(current)) < errorWidth) {
          actionsLeft.remove();
          actionColors.put(current, SUCCESS);

          for (Object listener : listeners) {
            if (listener instanceof FlowComponentListener) {
              ((FlowComponentListener)listener).beatSuccess(index);
            }
          }
          return;
        }

      for (Object listener : listeners) {
        if (listener instanceof FlowComponentListener) {
          ((FlowComponentListener)listener).beatFailed();
        }
      }
    }
  }

  public void start(Music music) {
    float width = getWidth();

    if (width > 0) {
      resetCombo();
      this.music = music;
      float secondsInBeat = 60.0f / bpm;
      start = music.getPosition();
      float startIn = -(start + BUFFER_TIME) % (secondsInBeat) + BUFFER_TIME;
      length = startIn + actionSectionLength + BUFFER_TIME;
      trackerSpeed = width / length;
      startWidth = trackerSpeed * startIn;
      actionSectionWidth = trackerSpeed * actionSectionLength;
      errorWidth = trackerSpeed * ERROR_LENGTH;
    }
  }

  public void update() {
    if (music != null) {
      float progress = music.getPosition() - start;

      if (progress < length) {
        float x = progress * trackerSpeed + rect.getX();
        tracker.set(x, rect.getY(), x, rect.getMaxY());
        checkForMissed();
      } else {
        music = null;
        for (Object listener : listeners) {
          if (listener instanceof FlowComponentListener) {
            ((FlowComponentListener)listener).songEnded();
          }
        }
      }

    }
  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public void renderComponent(
      GUIContext context, Graphics g, float xMin, float xMax) throws SlickException {
    renderComponent(context, g);
  }

  @Override
  public void resetLayout() {}
}
