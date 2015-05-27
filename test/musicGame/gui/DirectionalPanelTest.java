package musicGame.gui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;

public class DirectionalPanelTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private static final int X = 400;
	private static final int Y = 500;
	private static final int COMPONENT_HEIGHT = 12;
	private static final int COMPONENT_WIDTH = 10;
	private static final int SPACING = 30;

	private DirectionalPanel panel;
	
	@Mock private GUIContext context;
	@Mock private Input input;
	@Mock private ComponentBase component;
	@Mock private ComponentBase component2;
	
	@Before
	public void setUp() {
		mockery.checking(new Expectations() {{
			oneOf(context).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(DirectionalPanel.class)));
		}});
		this.panel = new DirectionalPanel(context, X, Y, COMPONENT_WIDTH, COMPONENT_HEIGHT, SPACING);
	}
	
	@Test
	public void constructionShouldHaveEmptyPanelWithDefaultPositioning() {
		assertThat(this.panel.getHeight(), is(0));
		assertThat(this.panel.getWidth(), is(COMPONENT_WIDTH));
		assertThat(this.panel.getX(), is(X));
		assertThat(this.panel.getY(), is(Y));
		assertThat(this.panel.iterator().hasNext(), is(false));
	}
	
	@Test
	public void getHeightShouldReturnTotalHeightOfVisibleItems() {
		mockery.checking(new Expectations() {{
			exactly(2).of(component).setWidth(COMPONENT_WIDTH);
			exactly(2).of(component).setHeight(COMPONENT_HEIGHT);
			exactly(3).of(component).setLocation(with(any(Integer.class)), with(any(Integer.class)));
		}});
		this.panel.addChild(component);
		this.panel.addChild(component);
		assertThat(this.panel.getHeight(), is((COMPONENT_HEIGHT * 2) + SPACING));
	}
	
	@Test
	public void removingItemShouldRepeatItemLayout() {
		mockery.checking(new Expectations() {{
			// Adding elements
			exactly(3).of(component).setWidth(COMPONENT_WIDTH);
			exactly(3).of(component).setHeight(COMPONENT_HEIGHT);
			exactly(6).of(component).setLocation(with(any(Integer.class)), with(any(Integer.class)));
			
			// Removing one element
			oneOf(component).setLocation(X, Y);
			oneOf(component).setLocation(X, Y + COMPONENT_HEIGHT + SPACING);
		}});
		this.panel.addChild(component);
		this.panel.addChild(component);
		this.panel.addChild(component);
		this.panel.removeChild(component);
	}
	
	@Test
	public void constructionWithoutXYShouldBuildIntoMiddleOfScreen() {
		final int screenWidth = 1000;
		final int screenHeight = 500;
		
		mockery.checking(new Expectations() {{
			oneOf(context).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(DirectionalPanel.class)));
			exactly(2).of(context).getWidth(); will(returnValue(screenWidth));
			exactly(2).of(context).getHeight(); will(returnValue(screenHeight));
			
			oneOf(component).setWidth(COMPONENT_WIDTH);
			oneOf(component).setHeight(COMPONENT_HEIGHT);
			oneOf(component).setLocation((screenWidth - COMPONENT_WIDTH) / 2, (screenHeight - COMPONENT_HEIGHT) / 2);
		}});
		DirectionalPanel panel = new DirectionalPanel(context, COMPONENT_WIDTH, COMPONENT_HEIGHT);
		assertThat(panel.getX(), is((screenWidth - COMPONENT_WIDTH) / 2));
		assertThat(panel.getY(), is(screenHeight / 2));
		
		panel.addChild(component);
		assertThat(panel.getX(), is((screenWidth - COMPONENT_WIDTH) / 2));
		assertThat(panel.getY(), is((screenHeight - COMPONENT_HEIGHT) / 2));
	}
}
