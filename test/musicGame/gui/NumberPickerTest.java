package musicGame.gui;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.gui.GUIContext;

public class NumberPickerTest {
	private NumberPicker picker;
	
	@Mock private GUIContext container;
	@Mock private Input input;
	@Mock private Sound sound;
	
	@Before
	public void setUp() throws SlickException {
		initMocks(this);
		mockery.checking(new Expectations() {{
			when(container.getInput()).thenReturn(input);

		}});
		
		picker = new NumberPicker(container, 0, 0, 0, 0, "", 3, 1, 5, sound, sound, sound);
	}
	
	@Test
	public void pressingLeftOrRightWhenNotActivatedShouldDoNothing() {
		assertThat(picker.getValue(), is(3));
		picker.keyPressed(Input.KEY_LEFT, '`');
		assertThat(picker.getValue(), is(3));
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(3));
	}
	
	@Test
	public void pressingLeftShouldDecreaseValue() {
		mockery.checking(new Expectations() {{
			exactly(2).of(sound).play();
		}});

		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_LEFT, '`');
		assertThat(picker.getValue(), is(2));
	}
	
	@Test
	public void pressingRightShouldDecreaseValue() {
		mockery.checking(new Expectations() {{
			exactly(2).of(sound).play();
		}});

		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(4));
	}
	
	@Test
	public void valueShouldNotDecreaseBelowMinimum() {
		mockery.checking(new Expectations() {{
			exactly(3).of(sound).play();
		}});
		
		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_LEFT, '`');
		picker.keyPressed(Input.KEY_LEFT, '`');
		assertThat(picker.getValue(), is(1));
		picker.keyPressed(Input.KEY_LEFT, '`');
		assertThat(picker.getValue(), is(1));
	}
	
	@Test
	public void valueShouldNotIncreaseBeyondMaximum() {
		mockery.checking(new Expectations() {{
			exactly(3).of(sound).play();
		}});
		
		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_RIGHT, '`');
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(5));
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(5));
	}
}
