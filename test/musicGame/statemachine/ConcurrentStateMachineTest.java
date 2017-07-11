package musicGame.statemachine;

import static musicGame.statemachine.State.Action.JUMP;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ConcurrentStateMachineTest {

	@Mock private State state1;
	@Mock private State state2;
	@Mock private State state3;

	private ConcurrentStateMachine machine;

	@SuppressWarnings("serial")
	@Before
	public void setUp() {
		initMocks(this);
		machine = new ConcurrentStateMachine(new HashSet<State>() {{
			add(state1);
			add(state3);
		}});
	}

	@Test
	public void getStatesReturnsStartStatesWhenNoModificationMade() {
		assertThat(machine.getStates(), containsInAnyOrder(state1, state3));
	}

	@Test
	public void transitionCorrectlyTransitionsForAllStates() {
		when(state1.transition(JUMP)).thenReturn(state2);
		when(state3.transition(JUMP)).thenReturn(state3);
		assertThat(machine.transition(JUMP), containsInAnyOrder(state2, state3));
		assertThat(machine.getStates(), containsInAnyOrder(state2, state3));
	}
}
