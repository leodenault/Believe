package musicGame.core;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {

	private Timer timer;
	
	@Before
	public void setUp() {
		timer = new Timer();
	}
	
	@Test
	public void updateShouldAddDeltaToElapsedTime() {
		timer.play();
		timer.update(10);
		assertThat(timer.getElapsedTime(), is(10L));
	}
	
	@Test
	public void getElapsedTimeShouldBeZeroWhenNotPlaying() {
		timer.update(56);
		assertThat(timer.getElapsedTime(), is(0L));
	}

	@Test
	public void getElapsedTimeShouldBeTheSameWhenPaused() {
		timer.pause();
		assertThat(timer.getElapsedTime(), is(0L));
		timer.play();
		timer.update(789);
		timer.pause();
		assertThat(timer.getElapsedTime(), is(789L));
		timer.update(654);
		assertThat(timer.getElapsedTime(), is(789L));
	}
	
	@Test
	public void getElapsedTimeShouldResetAfterPlayingThenStopped() {
		timer.stop();
		assertThat(timer.getElapsedTime(), is(0L));
		timer.play();
		timer.update(12);
		assertThat(timer.getElapsedTime(), is(12L));
		timer.stop();
		assertThat(timer.getElapsedTime(), is(0L));
		timer.update(564654);
		assertThat(timer.getElapsedTime(), is(0L));
	}
	
	@Test
	public void getElapsedTimeShouldContinueIncrementingAfterPausingAndThenPlaying() {
		timer.play();
		timer.update(89);
		timer.pause();
		assertThat(timer.getElapsedTime(), is(89L));
		timer.play();
		timer.update(25);
		assertThat(timer.getElapsedTime(), is(114L));
	}
}

