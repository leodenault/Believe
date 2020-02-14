package believe.statemachine;

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

  @Mock private Transition<String> transition1;
  @Mock private Runnable runnable;

  private State<String> state1;
  private State<String> state2;
  private State<String> state3;
  private State<String> state4;

  @Before
  public void setUp() {
    initMocks(this);
    state1 = new State<String>().addTransition("JUMP", transition1);
    state4 = new State<>();
    state3 = new State<String>().addTransition("JUMP", state4);
    state2 = new State<String>().addTransition("LAND", runnable, state3);
  }

  @Test
  public void transitionCorrectlyActivatesTransition() {
    when(transition1.execute()).thenReturn(state2);
    assertThat(state1.transition("JUMP"), is(state2));
    assertThat(state2.transition("LAND"), is(state3));
    assertThat(state3.transition("JUMP"), is(state4));
    verify(runnable).run();
  }

  @Test
  public void nonExistingTransitionReturnsSameState() {
    assertThat(state1.transition("LAND"), is(state1));
    verify(transition1, never()).execute();
  }
}
