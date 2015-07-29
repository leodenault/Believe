package musicGame.gui;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class NumberPickerTest {
	
	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};

	private NumberPicker picker;
	
	@Mock private GUIContext container;
	@Mock private Input input;
	
	@Before
	public void setUp() throws SlickException {
		mockery.checking(new Expectations() {{
			oneOf(container).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(NumberPicker.class)));
		}});
		
		picker = new NumberPicker(container, "", 3, 1, 5);
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
		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_LEFT, '`');
		assertThat(picker.getValue(), is(2));
	}
	
	@Test
	public void pressingRightShouldDecreaseValue() {
		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(4));
	}
	
	@Test
	public void valueShouldNotDecreaseBelowMinimum() {
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
		assertThat(picker.getValue(), is(3));
		picker.activate();
		picker.keyPressed(Input.KEY_RIGHT, '`');
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(5));
		picker.keyPressed(Input.KEY_RIGHT, '`');
		assertThat(picker.getValue(), is(5));
	}
}
