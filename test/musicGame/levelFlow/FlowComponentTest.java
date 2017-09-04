package musicGame.levelFlow;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentTest {
	@Mock private GUIContext context;
	@Mock private Music song;
	@Mock private Input input;
	@Mock private FlowComponentListener listener;
	@Mock private AbstractComponent abstractComponent;
	@Mock private TrueTypeFont font;
	
	private FlowComponent flowComponent;
	
	private static final char[] INPUT_KEYS = { 'a', 's', 'k', 'l' };
	private static final int NUM_LANES = 4;
	private static final int LANE_WIDTH = 32;
	private static final int SUBDIVISION = 4;
	private static final int BPM = 120;
	private static final int OFFSET = 0;
	
	@Before
	public void setUp() {
		initMocks(this);
		when(context.getInput()).thenReturn(input);
		this.flowComponent = new FlowComponent(context, song, INPUT_KEYS, NUM_LANES, LANE_WIDTH, SUBDIVISION, BPM, OFFSET, 0, 0, 0, font);
	}

	@Test
	public void componentActivatedShouldNotifyListenersThatABeatWasMissed() {
		this.flowComponent.addListener(this.listener);
		this.flowComponent.componentActivated(this.abstractComponent);
	}

	@Test
	public void playShouldActivateMusic() {
		this.flowComponent.play();
	}
	
	@Test
	public void pauseShouldPauseMusic() {
		this.flowComponent.play();
		this.flowComponent.pause();
	}
	
	@Test
	public void playShouldResumeMusicAfterPausing() {
		this.flowComponent.play();
		this.flowComponent.pause();
		this.flowComponent.play();
	}
	
	@Test
	public void stopShouldStopMusicAfterPlaying() {
		this.flowComponent.play();
		this.flowComponent.stop();
	}
	
	@Test
	public void stopShouldStopMusicAfterPausing() {
		this.flowComponent.play();
		this.flowComponent.pause();
		this.flowComponent.stop();
	}
	
	@Test
	public void isPlayingReturnsTrueIfComponentIsPlaying() {
		this.flowComponent.play();
		assertThat(this.flowComponent.isPlaying(), is(true));
	}
	
	@Test
	public void isPlayingReturnsFalseIfComponentIsPaused() {
		this.flowComponent.play();
		this.flowComponent.pause();
		assertThat(this.flowComponent.isPlaying(), is(false));
	}
	
	@Test
	public void isPlayingReturnsTrueIfComponentIsStopped() {
		assertThat(this.flowComponent.isPlaying(), is(false));
	}
	
}

