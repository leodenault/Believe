package musicGame.GUI;

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
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class DirectionalPanelTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private DirectionalPanel<AbstractComponent> panel;
	
	@Mock private GUIContext context;
	@Mock private Input input;
	@Mock private AbstractComponent component;
	@Mock private AbstractComponent component2;
	
	@Before
	public void setUp() {
		mockery.checking(new Expectations() {{
			oneOf(context).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(DirectionalPanel.class)));
		}});
		this.panel = new DirectionalPanel<AbstractComponent>(context);
	}
	
	@Test
	public void constructionShouldHaveEmptyPanelWithDefaultPositioning() {
		assertThat(this.panel.getHeight(), is(0));
		assertThat(this.panel.getWidth(), is(0));
		assertThat(this.panel.getX(), is(0));
		assertThat(this.panel.getY(), is(0));
		assertThat(this.panel.iterator().hasNext(), is(false));
	}
	
	@Test
	public void getHeightShouldReturnTotalHeightOfVisibleItems() {
		final int componentHeight = 100;
		mockery.checking(new Expectations() {{
			ignoring(component).getWidth();
			ignoring(component).setLocation(0, 0);
			ignoring(component2).getWidth();
			ignoring(component2).setLocation(0, 50);
			exactly(2).of(component).getHeight(); will(returnValue(0));
			oneOf(component2).getHeight(); will(returnValue(0));
			oneOf(component).getHeight(); will(returnValue(componentHeight));
			oneOf(component2).getHeight(); will(returnValue(componentHeight));
		}});
		int spacing = 50;
		this.panel.setSpacing(50);
		this.panel.add(component);
		this.panel.add(component2);
		assertThat(this.panel.getHeight(), is((componentHeight * 2) + spacing));
	}
	
	@Test
	public void getWidthShouldReturnMaximumWidthOfVisibleItems() {
		final int largest = 300;
		mockery.checking(new Expectations() {{
			ignoring(component).getHeight();
			ignoring(component2).getHeight();
			exactly(4).of(component).getWidth(); will(returnValue(0));
			exactly(2).of(component).setLocation(0, 0);
			exactly(2).of(component2).getWidth(); will(returnValue(0));
			oneOf(component2).setLocation(0, 0);
			oneOf(component).getWidth(); will(returnValue(100));
			oneOf(component2).getWidth(); will(returnValue(largest));
		}});
		this.panel.add(component);
		this.panel.add(component2);
		assertThat(this.panel.getWidth(), is(largest));
	}
	
	@Test
	public void resetItemLocationsShouldProperlyPlaceEachItem() {
		this.panel.setLocation(10, 10);
		this.panel.setSpacing(20);
		
		final int width1 = 10;
		final int height1 = 20;
		final int width2 = 20;
		final int height2 = 10;
		mockery.checking(new Expectations() {{
			exactly(2).of(component).getWidth(); will(returnValue(width1));
			oneOf(component).getHeight(); will(returnValue(height1));
			oneOf(component).setLocation(10, 10);
			
			oneOf(component).getWidth(); will(returnValue(width1));
			oneOf(component).getWidth(); will(returnValue(width2));
			
			oneOf(component).getWidth(); will(returnValue(width1));
			oneOf(component).getHeight(); will(returnValue(height1));
			oneOf(component).setLocation(15, 10);
			
			oneOf(component).getWidth(); will(returnValue(width2));
			oneOf(component).getHeight(); will(returnValue(height2));
			oneOf(component).setLocation(10, 50);
		}});
		this.panel.add(component);
		this.panel.add(component);
	}
}
