package believe.levelFlow.component;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;

public class LaneTest {
  @Mock public GUIContext context;
  @Mock public Input input;
  @Mock public Animation animation;

  private final double BPM = 120;

  private Lane lane;

  @Before
  public void setUp() {
    initMocks(this);
    when(context.getInput()).thenReturn(input);
    this.lane = new Lane(context, 4, BPM, 0);
  }

  @Test
  public void playChangesPlayStatusToPlay() {
    this.lane.play();
    assertThat(this.lane.isPlaying(), is(true));
  }

  @Test
  public void pauseChangesPlayStatusToNotPlay() {
    this.lane.play();
    this.lane.pause();
    assertThat(this.lane.isPlaying(), is(false));
  }

  @Test
  public void stopChangesPlayStatusToNotPlay() {
    assertThat(this.lane.isPlaying(), is(false));
    this.lane.play();
    this.lane.stop();
    assertThat(this.lane.isPlaying(), is(false));
  }

  @Test
  public void stopShouldResetBeatPositions() throws Exception {
    when(context.getInput()).thenReturn(input);
    when(animation.getWidth()).thenReturn(123456789);
    when(animation.getHeight()).thenReturn(123);
    Beat[] beats = new Beat[] { new Beat(this.context, animation, 1000) };
    this.lane.setSpeedMultiplier(10);
    this.lane.addBeats(beats);
    int beatY = beats[0].getY();
    this.lane.play();
    this.lane.updateBeats(500);
    assertThat(beats[0].getY(), is(not(beatY)));
    this.lane.stop();
    assertThat(beats[0].getY(), is(beatY));
  }
}
