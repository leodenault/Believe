package musicGame.levelFlow;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import musicGame.levelFlow.component.Beat;
import musicGame.levelFlow.component.FlowComponent;
import musicGame.levelFlow.component.FlowComponentListener;
import musicGame.levelFlow.component.Lane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
		when(context.getInput()).thenReturn(input);
		flowComponent = new FlowComponent(context, song, INPUT_KEYS, NUM_LANES, LANE_WIDTH, SUBDIVISION, BPM, OFFSET, 0, 0, 0, font);
		flowComponent.setSpeedMultiplier(1);
	}
	
	@Test
	public void missedBeatsSignaledAtCorrectTime() {
		when(animation.getWidth()).thenReturn(0);
		when(animation.getHeight()).thenReturn(0);
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
