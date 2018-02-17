package believe.levelFlow.component;

import static believe.levelFlow.component.ComboSyncher.BUFFER_TIME;
import static believe.levelFlow.component.ComboSyncher.ERROR_LENGTH;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.core.SynchedComboPattern;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;

public class ComboSyncherTest {
  private static final int BPM = 132;
  private static final int FIRST_BEAT = 4;
  private static final int SECOND_BEAT = 8;
  private static final int LAST_BEAT = 13;
  private static final float SECONDS_IN_BEAT = 60.0f / BPM;
  private static final char FIRST_KEY = 'd';
  private static final char SECOND_KEY = 't';



  private ComboSyncher combo;
  private SynchedComboPattern pattern;

  @Mock private GUIContext container;
  @Mock private Input input;
  @Mock private TrueTypeFont font;
  @Mock private Music music;
  @Mock private FlowComponentListener listener;

  @Before
  public void setUp() {
    initMocks(this);
    when(container.getInput()).thenReturn(input);
    pattern = new SynchedComboPattern();
    pattern.addAction(FIRST_BEAT, FIRST_KEY);
    pattern.addAction(SECOND_BEAT, SECOND_KEY);
    pattern.addAction(LAST_BEAT, '[');
    combo = new ComboSyncher(container, pattern, font, BPM, 0, 0, 100, 10);
    combo.addListener(listener);
  }

  private float computeUpToBeat(float musicPosition, float beat) {
    return -(musicPosition + BUFFER_TIME) % (SECONDS_IN_BEAT)
        + BUFFER_TIME + (beat * SECONDS_IN_BEAT);
  }

  @Test
  public void updateNotifiesEndOfCombo() {
    final float musicStart = 2.3f;
    final float comboLength = computeUpToBeat(musicStart, LAST_BEAT) + BUFFER_TIME;
    when(music.getPosition()).thenReturn(musicStart);
    when(music.getPosition()).thenReturn(musicStart + comboLength);
    combo.start(music);
    combo.update();

  }

  @Test
  public void updateNotifiesMissedComboBeats() {
    final float musicStart = 4.8f;
    final float lengthToBeat = computeUpToBeat(musicStart, FIRST_BEAT);
    when(music.getPosition()).thenReturn(musicStart);
    when(music.getPosition()).thenReturn(musicStart + lengthToBeat + 1.4f);
    combo.start(music);
    combo.update();
  }

  @Test
  public void updateNotifiesFailedBeatWhenPressingAtWrongTime() {
    final float musicStart = 0.2f;
    final float lengthToBeat = computeUpToBeat(musicStart, FIRST_BEAT);
    when(music.getPosition()).thenReturn(musicStart);
    when(music.getPosition()).thenReturn(musicStart + lengthToBeat - 0.65f);
    when(music.getPosition()).thenReturn(musicStart + lengthToBeat);
    combo.start(music);
    combo.update();
    combo.keyPressed(0, FIRST_KEY);
    combo.update();
    combo.keyPressed(0, SECOND_KEY);
  }

  @Test
  public void updateNotifiesSuccessfulBeatsWhenPressingAtRightTime() {
    final float musicStart = 45.8f;
    final float lengthToBeat = computeUpToBeat(musicStart, FIRST_BEAT);
    when(music.getPosition()).thenReturn(musicStart);
    when(music.getPosition()).thenReturn(musicStart + lengthToBeat);
    combo.start(music);
    combo.update();
    combo.keyPressed(0, FIRST_KEY);
  }

  @Test
  public void beatsLandOnCorrectTime() {
    final float lengthToFirstBeat = computeUpToBeat(0, FIRST_BEAT);
    final float lengthToSecondBeat = computeUpToBeat(0, SECOND_BEAT);
    // Remove 0.01 since it's not quite exact
    final float error = ERROR_LENGTH + 0.01f;
    when(music.getPosition()).thenReturn(0f);
    when(music.getPosition()).thenReturn(lengthToFirstBeat - error);
    when(music.getPosition()).thenReturn(lengthToFirstBeat);
    when(music.getPosition()).thenReturn(lengthToSecondBeat - error);
    when(music.getPosition()).thenReturn(lengthToSecondBeat + error);
    combo.start(music);
    // Hit before first beat
    combo.update();
    combo.keyPressed(0, FIRST_KEY);
    // Hit right on first beat
    combo.update();
    combo.keyPressed(0, FIRST_KEY);
    // Hit before second beat
    combo.update();
    combo.keyPressed(0, SECOND_KEY);
    // Miss second beat
    combo.update();
  }
}
