package musicGame.statemachine;

import static musicGame.statemachine.State.Action.JUMP;
import static musicGame.util.Util.hashSetOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ConcurrentStateMachineTest {

  @Mock private State state1;
  @Mock private State state2;
  @Mock private State state3;
  @Mock private ConcurrentStateMachine.Listener listener;

  private ConcurrentStateMachine machine;

  @Before
  public void setUp() {
    initMocks(this);
    machine = new ConcurrentStateMachine(hashSetOf(state1, state3));
    when(state1.transition(JUMP)).thenReturn(state2);
    when(state3.transition(JUMP)).thenReturn(state3);
  }

  @Test
  public void getStatesReturnsStartStatesWhenNoModificationMade() {
    assertThat(machine.getStates(), containsInAnyOrder(state1, state3));
  }

  @Test
  public void transitionCorrectlyTransitionsForAllStates() {
    assertThat(machine.transition(JUMP), containsInAnyOrder(state2, state3));
    assertThat(machine.getStates(), containsInAnyOrder(state2, state3));
  }

  @Test
  public void listenersAreUpdatedUponTransitioning() {
    machine.addListener(listener);
    machine.transition(JUMP);
    verify(listener).transitionEnded(hashSetOf(state2, state3));
  }
}
