package musicGame.levelFlow;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;

public class BeatTest {
	@Mock private GUIContext context;
	@Mock private Animation animation;
	@Mock private Input input;
	
	private Beat beat;
	
	@Before
	public void setUp() {
		initMocks(this);
		mockery.checking(new Expectations() {{
			when(context.getInput()).thenReturn(input);




			when(animation.getWidth()).thenReturn(0);
			when(animation.getHeight()).thenReturn(0);
		}});
		this.beat = new Beat(context, animation, 0);
	}

	@Test
	public void setLocationShouldCorrectlySetBeatLocation() {
		this.beat.setLocation(10, 10);
		assertThat(this.beat.getX(), is(10));
		assertThat(this.beat.getY(), is(10));
	}
	
	@Test
	public void setLocationShouldRoundCoordinates() {
		this.beat.setLocation(500.5, 60.3);
		assertThat(this.beat.getX(), is(501));
		assertThat(this.beat.getY(), is(60));
	}
	
	@Test
	public void consumeShouldPutBeatInPlayingState() {
		mockery.checking(new Expectations() {{
			when(animation.copy()).thenReturn(animation);

			when(animation.isStopped()).thenReturn(false);
		}});
		this.beat.consume();
		assertThat(this.beat.isPlaying(), is(true));
	}
	
	@Test
	public void resetShouldResetPlayingStateOfBeat() {
		mockery.checking(new Expectations() {{
			when(animation.copy()).thenReturn(animation);


		}});
		this.beat.consume();
		this.beat.reset();
		assertThat(this.beat.isPlaying(), is(false));
	}
	
	@Test
	public void cloneShouldReturnBeatInstanceWithSameProperties() {
		mockery.checking(new Expectations() {{
			allowing(animation).copy();
			allowing(context).getInput();
		}});
		Beat newBeat = this.beat.clone();
		assertThat(newBeat, not(this.beat));
		assertThat(newBeat.getPosition(), is(this.beat.getPosition()));
		assertThat(newBeat.getX(), is(this.beat.getX()));
		assertThat(newBeat.getY(), is(this.beat.getY()));
	}
}
