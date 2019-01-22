package believe.statemachine;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityStateMachineTest {
  private enum Action {
    ACTION_1, ACTION_2
  }

  private enum State {
    STATE_1, STATE_2
  }

  @Mock private Function<Void, State> callback;

  private EntityStateMachine<Action, State, Void> machine;

  @SuppressWarnings("serial")
  @Before
  public void setUp() {
    initMocks(this);
    machine = new EntityStateMachine<Action, State, Void>(
        new HashMap<State, Map<Action, Function<Void, State>>>() {{
          put(State.STATE_1, new HashMap<Action, Function<Void, State>>() {{
            put(Action.ACTION_2, callback);
          }});
        }}, State.STATE_1);
  }

  @Test
  public void transitionDoesNothingWhenActionDoesntExistForState() {
    assertThat(machine.transition(Action.ACTION_1), is(State.STATE_1));
  }

  @Test
  public void correctTransitionChangesStateAndExecutesCallback() {
    when(callback.apply(null)).thenReturn(State.STATE_2);
    assertThat(machine.transition(Action.ACTION_2), is(State.STATE_2));
  }
}
