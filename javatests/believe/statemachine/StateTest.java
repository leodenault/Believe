package believe.statemachine;

import static believe.statemachine.State.Action.JUMP;
import static believe.statemachine.State.Action.LAND;
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
  @Mock private Runnable runnable;

  private State state1;
  private State state2;
  private State state3;
  private State state4;

  @Before
  public void setUp() {
    initMocks(this);
    state1 = new State()
        .addTransition(JUMP, transition1);
    state4 = new State();
    state3 = new State()
        .addTransition(JUMP, state4);
    state2 = new State()
        .addTransition(LAND, runnable, state3);
  }

  @Test
  public void transitionCorrectlyActivatesTransition() {
    when(transition1.execute()).thenReturn(state2);
    assertThat(state1.transition(JUMP), is(state2));
    assertThat(state2.transition(LAND), is(state3));
    assertThat(state3.transition(JUMP), is(state4));
    verify(runnable).run();
  }

  @Test
  public void nonExistingTransitionReturnsSameState() {
    assertThat(state1.transition(LAND), is(state1));
    verify(transition1, never()).execute();
  }
}
