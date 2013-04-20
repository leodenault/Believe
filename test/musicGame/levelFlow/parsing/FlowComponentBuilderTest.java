package musicGame.levelFlow.parsing;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentBuilderTest {
	
	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private FlowComponentBuilder builder;
	
	@Mock private GUIContext container;
	
	@Before
	public void setUp() {
		this.builder = new FlowComponentBuilder(this.container, 32);
	}

	@Test(expected=FlowComponentBuilderException.class)
	public void buildFlowComponentShouldThrowFlowComponentBuilderExceptionIfValuesAreMissing()
			throws FlowComponentBuilderException {
		this.builder.buildFlowComponent();
	}

	@Test(expected=FlowComponentBuilderException.class)
	public void topBarImageShouldThrowFlowComponentBuilderExceptionIfNotOneValue() throws Exception {
		this.builder.topBarImage(Arrays.asList("", ""));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void subdivisionImagesShouldThrowFlowComponentBuilderExceptionIfEmptyValues() throws Exception {
		this.builder.topBarImage(new ArrayList<String>());
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void songShouldThrowFlowComponentBuilderExceptionIfNotOneValue() throws Exception {
		this.builder.song(Arrays.asList("", ""));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void inputKeysShouldThrowFlowComponentBuilderExceptionIfNoValues() throws Exception {
		this.builder.inputKeys(new ArrayList<String>());
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void inputKeysShouldThrowFlowComponentBuilderExceptionIfValuesAreNotChars() throws Exception {
		this.builder.inputKeys(Arrays.asList("sd"));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void bpmShouldThrowFlowComponentBuilderExceptionIfNotOneValue() throws Exception {
		this.builder.tempo(Arrays.asList("", ""));
	}
	
	@Test(expected=NumberFormatException.class)
	public void bpmShouldThrowNumberFormatExceptionIfValueIsNotANumber() throws Exception {
		this.builder.tempo(Arrays.asList("sd"));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void bpmShouldThrowFlowComponentBuilderExceptionIfValueIsZero() throws Exception {
		this.builder.tempo(Arrays.asList("0"));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void bpmShouldThrowFlowComponentBuilderExceptionIfValueIsNegative() throws Exception {
		this.builder.tempo(Arrays.asList("-987"));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void offsetShouldThrowFlowComponentBuilderExceptionIfNotOneValue() throws Exception {
		this.builder.tempo(Arrays.asList("", ""));
	}
	
	@Test(expected=NumberFormatException.class)
	public void offsetShouldThrowNumberFormatExceptionIfValueIsNotANumber() throws Exception {
		this.builder.tempo(Arrays.asList("sd"));
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void addBeatLineShouldThrowFlowComponentBuilderExceptionWhenKeysNotYetSet() throws Exception {
		this.builder.addBeatLine("--", 1, 1);
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void addBeatLineShouldThrowFlowComponentBuilderExceptionWhenLessBeatsThanKeys() throws Exception {
		this.builder.inputKeys(Arrays.asList("p", "q"));
		this.builder.addBeatLine("-", 1, 1);
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void addBeatLineShouldThrowFlowComponentBuilderExceptionWhenMoreBeatsThanKeys() throws Exception {
		this.builder.inputKeys(Arrays.asList("p", "q"));
		this.builder.addBeatLine("---", 1, 1);
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void getImageIndexShouldThrowFlowComponentBuilderExceptionIfSubdivisionImagesAreNull() throws Exception {
		this.builder.getImageIndex(0);
	}
	
	@Test
	public void recursiveGetImageIndexShouldReturnCorrectIndexBasedOnBeatPosition() throws Exception {
		int startingIndex = 16;
		int step = startingIndex / 2;
		int imageIndex = 1;
		assertThat(this.builder.recursiveGetImageIndex(2, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(4, startingIndex, step, imageIndex), is(3));
		assertThat(this.builder.recursiveGetImageIndex(6, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(8, startingIndex, step, imageIndex), is(2));
		assertThat(this.builder.recursiveGetImageIndex(10, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(12, startingIndex, step, imageIndex), is(3));
		assertThat(this.builder.recursiveGetImageIndex(14, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(16, startingIndex, step, imageIndex), is(1));
		assertThat(this.builder.recursiveGetImageIndex(18, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(20, startingIndex, step, imageIndex), is(3));
		assertThat(this.builder.recursiveGetImageIndex(22, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(24, startingIndex, step, imageIndex), is(2));
		assertThat(this.builder.recursiveGetImageIndex(26, startingIndex, step, imageIndex), is(4));
		assertThat(this.builder.recursiveGetImageIndex(28, startingIndex, step, imageIndex), is(3));
		assertThat(this.builder.recursiveGetImageIndex(30, startingIndex, step, imageIndex), is(4));
	}
}
