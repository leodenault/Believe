package musicGame.levelFlow;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentIntegrationTest {
	private static final char[] INPUT_KEYS = { 'a', 's', 'k', 'l' };
	private static final int NUM_LANES = 4;
	private static final int LANE_WIDTH = 32;
	private static final int SUBDIVISION = 4;
	private static final int BPM = 120;
	private static final int OFFSET = 0;
	private static final int MILLIS_IN_BEAT = (int)((60 * 1000) / BPM);
	private static final double MILLIS_PER_PIXEL = 1000.0 / Lane.DEFAULT_SPEED;
	
	@Mock private GUIContext context;
	@Mock private Music song;
	@Mock private Input input;
	@Mock private Animation animation;
	@Mock private FlowComponentListener listener;
	@Mock private AbstractComponent abstractComponent;
	@Mock private TrueTypeFont font;
	
	private FlowComponent flowComponent;
	
	@Before
	public void setUp() {
		initMocks(this);
		mockery.checking(new Expectations() {{
			atLeast(1).of(context).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(FlowComponent.class)));
			atLeast(4).of(input).addPrimaryListener(with(any(Lane.class)));
			oneOf(song).addListener(with(any(FlowComponent.class)));
		}});
		flowComponent = new FlowComponent(context, song, INPUT_KEYS, NUM_LANES, LANE_WIDTH, SUBDIVISION, BPM, OFFSET, 0, 0, 0, font);
		flowComponent.setSpeedMultiplier(1);
	}
	
	@Test
	public void missedBeatsSignaledAtCorrectTime() {
		mockery.checking(new Expectations() {{
			oneOf(animation).getWidth(); will(returnValue(0));
			oneOf(animation).getHeight(); will(returnValue(0));
			oneOf(animation).setCurrentFrame(0);
			oneOf(animation).setLooping(false);
			oneOf(animation).stop();
			oneOf(song).play();
			oneOf(input).addPrimaryListener(with(any(Beat.class)));
			oneOf(listener).beatMissed();
		}});
		flowComponent.addListener(listener);
		Beat[][] beats = new Beat[][] {
			new Beat [] {
					new Beat(context, animation, 2 * SUBDIVISION)	
			},
			new Beat[0],
			new Beat[0],
			new Beat[0]
		};
		flowComponent.addBeats(beats);
		flowComponent.play();
		flowComponent.update(2 * MILLIS_IN_BEAT);
		assertThat(beats[0][0].getY(), is(Lane.MIN_BANNER / 2));
		flowComponent.update((int)(((Lane.MIN_BANNER / 2) + 1) * MILLIS_PER_PIXEL));
		assertThat(beats[0][0].getY(), is(-1));
	}
}
