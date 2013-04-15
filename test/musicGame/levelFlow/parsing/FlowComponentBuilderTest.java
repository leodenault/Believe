package musicGame.levelFlow.parsing;

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
import org.newdawn.slick.Animation;
import org.newdawn.slick.gui.GUIContext;

public class FlowComponentBuilderTest {
	
	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private FlowComponentBuilder builder;
	
	@Mock private GUIContext container;
	@Mock private Animation animation;
	
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
		this.builder.addBeatLine("--", this.animation, 1);
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void addBeatLineShouldThrowFlowComponentBuilderExceptionWhenLessBeatsThanKeys() throws Exception {
		this.builder.inputKeys(Arrays.asList("p", "q"));
		this.builder.addBeatLine("-", this.animation, 1);
	}
	
	@Test(expected=FlowComponentBuilderException.class)
	public void addBeatLineShouldThrowFlowComponentBuilderExceptionWhenMoreBeatsThanKeys() throws Exception {
		this.builder.inputKeys(Arrays.asList("p", "q"));
		this.builder.addBeatLine("---", this.animation, 1);
	}
}
