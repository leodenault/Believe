package musicGame.statemachine;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TransitionTest {
	
	private Transition transition;
	
	@Mock private State state;
	@Mock private Runnable runnable;
	
	@Before
	public void setUp() {
		initMocks(this);
		transition = new Transition(runnable, state);
	}

	@Test
	public void executeRunsFunctionThenReturnsState() {
		assertThat(transition.execute(), is(state));
		verify(runnable).run();
	}

}
