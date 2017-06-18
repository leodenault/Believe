package musicGame.statemachine;

import static musicGame.statemachine.State.Action.JUMP;
import static musicGame.statemachine.State.Action.LAND;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class StateTest {

	@Mock private Transition transition1;
	@Mock private Transition transition2;
	
	private State state1;
	private State state2;
	
	@Before
	public void setUp() {
		initMocks(this);
		state1 = State.newBuilder().addTransition(JUMP, transition1).build();
		state2 = State.newBuilder().build();
	}
	
	@Test
	public void transitionCorrectlyActivatesTransition() {
		when(transition1.execute()).thenReturn(state2);
		assertThat(state1.transition(JUMP), is(state2));
	}

	@Test
	public void nonExistingTransitionReturnsSameState() {
		assertThat(state1.transition(LAND), is(state1));
		verify(transition1, never()).execute();
	}
}
