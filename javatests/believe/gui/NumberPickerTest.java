package believe.gui;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.audio.Sound;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Font;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;

public class NumberPickerTest {
  private NumberPicker picker;

  @Mock private GUIContext container;
  @Mock private Input input;
  @Mock private Sound sound;
  @Mock private Font font;

  @Before
  public void setUp() {
    initMocks(this);
    when(container.getInput()).thenReturn(input);
    picker = new NumberPicker(container, font, 0, 0, 0, 0, "", 3, 1, 5, sound, sound, sound);
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
