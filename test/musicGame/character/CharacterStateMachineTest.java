package musicGame.character;

import static musicGame.character.CharacterStateMachine.Action.JUMP;
import static musicGame.character.CharacterStateMachine.Action.LAND;
import static musicGame.character.CharacterStateMachine.Action.SELECT_LEFT;
import static musicGame.character.CharacterStateMachine.Action.SELECT_RIGHT;
import static musicGame.character.CharacterStateMachine.Action.STOP;
import static musicGame.character.CharacterStateMachine.State.AIRBORNE_MOVING_LEFT;
import static musicGame.character.CharacterStateMachine.State.AIRBORNE_MOVING_RIGHT;
import static musicGame.character.CharacterStateMachine.State.AIRBORNE_STATIONARY;
import static musicGame.character.CharacterStateMachine.State.GROUNDED_MOVING_LEFT;
import static musicGame.character.CharacterStateMachine.State.GROUNDED_MOVING_RIGHT;
import static musicGame.character.CharacterStateMachine.State.GROUNDED_STATIONARY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CharacterStateMachineTest {
	private CharacterStateMachine machine;
	
	@Test
	public void groundedRightTransitionsAreCorrect() {
		machine = new CharacterStateMachine(GROUNDED_STATIONARY);
		assertThat(machine.transition(SELECT_RIGHT), is(GROUNDED_MOVING_RIGHT));
		assertThat(machine.transition(STOP), is(GROUNDED_STATIONARY));
	}
	
	@Test
	public void groundedLeftTransitionsAreCorrect() {
		machine = new CharacterStateMachine(GROUNDED_STATIONARY);
		assertThat(machine.transition(SELECT_LEFT), is(GROUNDED_MOVING_LEFT));
		assertThat(machine.transition(STOP), is(GROUNDED_STATIONARY));
	}
	
	@Test
	public void airborneRightTransitionsAreCorrect() {
		machine = new CharacterStateMachine(AIRBORNE_STATIONARY);
		assertThat(machine.transition(SELECT_RIGHT), is(AIRBORNE_MOVING_RIGHT));
		assertThat(machine.transition(STOP), is(AIRBORNE_STATIONARY));
	}
	
	@Test
	public void airborneLeftTransitionsAreCorrect() {
		machine = new CharacterStateMachine(AIRBORNE_STATIONARY);
		assertThat(machine.transition(SELECT_LEFT), is(AIRBORNE_MOVING_LEFT));
		assertThat(machine.transition(STOP), is(AIRBORNE_STATIONARY));
	}
	
	@Test
	public void groundToAirTransitionsAreCorrect() {
		machine = new CharacterStateMachine(GROUNDED_MOVING_LEFT);
		assertThat(machine.transition(JUMP), is(AIRBORNE_MOVING_LEFT));
		assertThat(machine.transition(LAND), is(GROUNDED_MOVING_LEFT));
		machine = new CharacterStateMachine(GROUNDED_STATIONARY);
		assertThat(machine.transition(JUMP), is(AIRBORNE_STATIONARY));
		assertThat(machine.transition(LAND), is(GROUNDED_STATIONARY));
		machine = new CharacterStateMachine(GROUNDED_MOVING_RIGHT);
		assertThat(machine.transition(JUMP), is(AIRBORNE_MOVING_RIGHT));
		assertThat(machine.transition(LAND), is(GROUNDED_MOVING_RIGHT));
	}
	
	@Test
	public void invalidActionsAreIgnored() {
		// Ground transitions
		machine = new CharacterStateMachine(GROUNDED_MOVING_LEFT);
		assertThat(machine.transition(SELECT_LEFT), is(GROUNDED_MOVING_LEFT));
		assertThat(machine.transition(SELECT_RIGHT), is(GROUNDED_MOVING_LEFT));
		machine = new CharacterStateMachine(GROUNDED_MOVING_RIGHT);
		assertThat(machine.transition(SELECT_RIGHT), is(GROUNDED_MOVING_RIGHT));
		assertThat(machine.transition(SELECT_LEFT), is(GROUNDED_MOVING_RIGHT));
		machine = new CharacterStateMachine(GROUNDED_STATIONARY);
		assertThat(machine.transition(STOP), is(GROUNDED_STATIONARY));

		// Air transitions
		machine = new CharacterStateMachine(AIRBORNE_MOVING_LEFT);
		assertThat(machine.transition(SELECT_LEFT), is(AIRBORNE_MOVING_LEFT));
		assertThat(machine.transition(SELECT_RIGHT), is(AIRBORNE_MOVING_LEFT));
		machine = new CharacterStateMachine(AIRBORNE_MOVING_RIGHT);
		assertThat(machine.transition(SELECT_RIGHT), is(AIRBORNE_MOVING_RIGHT));
		assertThat(machine.transition(SELECT_LEFT), is(AIRBORNE_MOVING_RIGHT));
		machine = new CharacterStateMachine(AIRBORNE_STATIONARY);
		assertThat(machine.transition(STOP), is(AIRBORNE_STATIONARY));
	}
}
