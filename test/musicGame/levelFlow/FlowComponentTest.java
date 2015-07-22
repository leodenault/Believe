package musicGame.levelFlow;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentTest {
	
	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	@Mock private GUIContext context;
	@Mock private Image image;
	@Mock private Music song;
	@Mock private Input input;
	@Mock private FlowComponentListener listener;
	@Mock private AbstractComponent abstractComponent;
	
	private FlowComponent flowComponent;
	
	private static final char[] INPUT_KEYS = { 'a', 's', 'k', 'l' };
	private static final int NUM_LANES = 4;
	private static final int LANE_WIDTH = 32;
	private static final int SUBDIVISION = 4;
	private static final int BPM = 120;
	private static final int OFFSET = 0;
	
	@Before
	public void setUp() {
		mockery.checking(new Expectations() {{
			atLeast(1).of(context).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(FlowComponent.class)));
			atLeast(4).of(input).addPrimaryListener(with(any(Lane.class)));
			atLeast(1).of(image).getHeight(); will(returnValue(123));
		}});
		this.flowComponent = new FlowComponent(context, image, song, INPUT_KEYS, NUM_LANES, LANE_WIDTH, SUBDIVISION, BPM, OFFSET);
	}

	@Test
	public void componentActivatedShouldNotifyListenersThatABeatWasMissed() {
		mockery.checking(new Expectations() {{
			oneOf(listener).beatMissed();
		}});
		this.flowComponent.addListener(this.listener);
		this.flowComponent.componentActivated(this.abstractComponent);
	}

	@Test
	public void playShouldActivateMusic() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
		}});
		this.flowComponent.play();
	}
	
	@Test
	public void pauseShouldPauseMusic() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
			oneOf(song).pause();
		}});
		this.flowComponent.play();
		this.flowComponent.pause();
	}
	
	@Test
	public void playShouldResumeMusicAfterPausing() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
			oneOf(song).pause();
			oneOf(song).resume();
		}});
		this.flowComponent.play();
		this.flowComponent.pause();
		this.flowComponent.play();
	}
	
	@Test
	public void stopShouldStopMusicAfterPlaying() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
			oneOf(song).stop();
		}});
		this.flowComponent.play();
		this.flowComponent.stop();
	}
	
	@Test
	public void stopShouldStopMusicAfterPausing() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
			oneOf(song).pause();
			oneOf(song).stop();
		}});
		this.flowComponent.play();
		this.flowComponent.pause();
		this.flowComponent.stop();
	}
	
	@Test
	public void isPlayingReturnsTrueIfComponentIsPlaying() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
		}});
		this.flowComponent.play();
		assertThat(this.flowComponent.isPlaying(), is(true));
	}
	
	@Test
	public void isPlayingReturnsFalseIfComponentIsPaused() {
		mockery.checking(new Expectations() {{
			oneOf(song).play();
			oneOf(song).pause();
		}});
		this.flowComponent.play();
		this.flowComponent.pause();
		assertThat(this.flowComponent.isPlaying(), is(false));
	}
	
	@Test
	public void isPlayingReturnsTrueIfComponentIsStopped() {
		assertThat(this.flowComponent.isPlaying(), is(false));
	}
	
}

