package believe.gui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DirectionalPanelTest {
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
    initMocks(this);
    when(context.getInput()).thenReturn(input);
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
    this.panel.addChild(component);
    this.panel.addChild(component);
    assertThat(this.panel.getHeight(), is((COMPONENT_HEIGHT * 2) + SPACING));
  }

  @Test
  public void removingItemShouldRepeatItemLayout() {
    this.panel.addChild(component);
    this.panel.addChild(component);
    this.panel.addChild(component);
    this.panel.removeChild(component);
  }

  @Test
  public void constructionWithoutXYShouldBuildIntoMiddleOfScreen() {
    final int screenWidth = 1000;
    final int screenHeight = 500;
    when(context.getWidth()).thenReturn(screenWidth);
    when(context.getHeight()).thenReturn(screenHeight);

    DirectionalPanel panel = new DirectionalPanel(context, COMPONENT_WIDTH, COMPONENT_HEIGHT);
    assertThat(panel.getX(), is((screenWidth - COMPONENT_WIDTH) / 2));
    assertThat(panel.getY(), is(screenHeight / 2));

    panel.addChild(component);
    assertThat(panel.getX(), is((screenWidth - COMPONENT_WIDTH) / 2));
    assertThat(panel.getY(), is((screenHeight - COMPONENT_HEIGHT) / 2));
  }
}
