package musicGame.gui;



public class VerticalKeyboardScrollpanelTest {
	
	/*@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private VerticalKeyboardScrollpanel scrollPanel;
	
	@Mock private ControlBuilder selectionBuilder;
	@Mock private ControlBuilder selectionBuilder2;
	@Mock private Element selection;
	@Mock private Element selection2;
	@Mock private Element container;
	@Mock private Element parentContainer;
	@Mock private Element scroller;
	@Mock private MenuSelection menuSelection;
	@Mock private MenuSelection menuSelection2;
	@Mock private Attributes attributes;
	
	@Before
	public void setUp() {
		mockery.checking(new Expectations() {{
			oneOf(attributes).get("gap"); will(returnValue(null));
		}});
		this.scrollPanel = new VerticalKeyboardScrollpanel();
		this.scrollPanel.init(null, attributes);
	}
	
	private void expectationsForAdd(final ControlBuilder selectionBuilder,
			final Element selection, final MenuSelection menuSelection) {
		mockery.checking(new Expectations() {{
			oneOf(selectionBuilder).build(null, null, null); will(returnValue(selection));
			oneOf(selection).getControl(MenuSelection.class); will(returnValue(menuSelection));
			oneOf(menuSelection).setPlaySound(with(any(Boolean.class)));
			oneOf(selectionBuilder).marginBottom(with(any(String.class)));
			oneOf(selection).getParent(); will(returnValue(container));
			oneOf(container).getParent(); will(returnValue(parentContainer));
		}});
	}
	
	@Test
	public void shouldScrollDownShouldReturnTrueIfLastSelectionOutsideScrollpanel() {
		this.expectationsForAdd(this.selectionBuilder, this.selection, this.menuSelection);
		this.scrollPanel.setScroller(this.scroller);
		mockery.checking(new Expectations() {{
			oneOf(selection).getParent(); will(returnValue(container));
			oneOf(container).getParent(); will(returnValue(parentContainer));
			exactly(2).of(parentContainer).getHeight(); will(returnValue(20));
			exactly(2).of(parentContainer).getY(); will(returnValue(10));
			exactly(3).of(selection).getY(); will(returnValue(50));
			exactly(3).of(selection).getHeight(); will(returnValue(10));
			oneOf(scroller).show();
		}});
		
		this.scrollPanel.add(selectionBuilder);
		assertThat(this.scrollPanel.shouldScrollDown(), is(true));
	}

	@Test
	public void shouldScrollDownShouldReturnFalseIfLastSelectionInsideScrollpanel() {
		this.expectationsForAdd(this.selectionBuilder, this.selection, this.menuSelection);
		mockery.checking(new Expectations() {{
			oneOf(selection).getParent(); will(returnValue(container));
			oneOf(container).getParent(); will(returnValue(parentContainer));
			exactly(2).of(parentContainer).getHeight(); will(returnValue(50));
			exactly(2).of(parentContainer).getY(); will(returnValue(30));
			exactly(3).of(selection).getY(); will(returnValue(40));
			exactly(3).of(selection).getHeight(); will(returnValue(15));
		}});
		
		this.scrollPanel.add(selectionBuilder);
		assertThat(this.scrollPanel.shouldScrollDown(), is(false));
	}
	
	@Test
	public void shouldScrollUpShouldReturnTrueIfFirstSelectionOutsideScrollpanel() {
		this.expectationsForAdd(this.selectionBuilder, this.selection, this.menuSelection);
		this.expectationsForAdd(this.selectionBuilder2, this.selection2, this.menuSelection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).getParent(); will(returnValue(container));
			oneOf(container).getParent(); will(returnValue(parentContainer));
			exactly(3).of(parentContainer).getY(); will(returnValue(30));
			exactly(3).of(parentContainer).getHeight(); will(returnValue(500));
			exactly(3).of(selection).getY(); will(returnValue(20));
			oneOf(selection).getHeight(); will(returnValue(25));
			oneOf(selection2).getHeight(); will(returnValue(35));
			oneOf(selection2).getY(); will(returnValue(35));
		}});
		
		this.scrollPanel.add(selectionBuilder);
		this.scrollPanel.add(selectionBuilder2);
		assertThat(this.scrollPanel.shouldScrollUp(), is(true));
	}
	
	@Test
	public void shouldScrollUpShouldReturnFalseIfFirstSelectionInsideScrollpanel() {
		this.expectationsForAdd(this.selectionBuilder, this.selection, this.menuSelection);
		mockery.checking(new Expectations() {{
			oneOf(selection).getParent(); will(returnValue(container));
			oneOf(container).getParent(); will(returnValue(parentContainer));
			exactly(2).of(parentContainer).getY(); will(returnValue(55));
			exactly(2).of(parentContainer).getHeight(); will(returnValue(600));
			exactly(3).of(selection).getY(); will(returnValue(55));
			oneOf(selection).getHeight(); will(returnValue(20));
		}});
		this.scrollPanel.add(selectionBuilder);
		assertThat(this.scrollPanel.shouldScrollUp(), is(false));
	}
	
	@Test
	public void shouldScrollUpShouldReturnFalseIfFirstSelectionNull() {
		assertThat(this.scrollPanel.shouldScrollUp(), is(false));
	}
	
	@Test
	public void shouldScrollDownShouldReturnFalseIfLastSelectionNull() {
		assertThat(this.scrollPanel.shouldScrollDown(), is(false));
	}
	
	@Test
	public void getScrollDistanceShouldZeroIfNoSelections() {
		assertThat(this.scrollPanel.getScrollDistance(), is(0));
	}
	
	@Test
	public void getScrollDistanceShouldReturnSelectionHeightPlusMarginBottom() {
		this.expectationsForAdd(this.selectionBuilder, this.selection, this.menuSelection);
		mockery.checking(new Expectations() {{
			exactly(2).of(selection).getHeight(); will(returnValue(15));
			oneOf(selection).getY(); will(returnValue(0));
			oneOf(parentContainer).getHeight(); will(returnValue(500));
			oneOf(parentContainer).getY(); will(returnValue(30));
			oneOf(selection).getParent(); will(returnValue(container));
			oneOf(container).getHeight(); will(returnValue(200));
		}});
		this.scrollPanel.add(selectionBuilder);
		
		assertThat(this.scrollPanel.getScrollDistance(), is(25));
	}
	
	@Test
	public void isPastMiddleShouldReturnFalseWhenNotPastMiddleGoingDown() {
		assertThat(this.scrollPanel.isPastMiddle(true, 21, 6, 500), is(false));
	}
	
	@Test
	public void isPastMiddleShouldReturnFalseWhenNotPastMiddleGoingUp() {
		assertThat(this.scrollPanel.isPastMiddle(false, 653, 9, 654), is(false));
	}
	
	@Test
	public void isPastMiddleShouldReturnTrueWhenPastMiddleGoingDown() {
		assertThat(this.scrollPanel.isPastMiddle(true, 257, 6, 500), is(true));
	}
	
	@Test
	public void isPastMiddleShouldReturnTrueWhenPastMiddleGoingUp() {
		assertThat(this.scrollPanel.isPastMiddle(false, 300, 9, 654), is(true));
	}
	
	@Test
	public void scrollerShouldBeVisibleWhenLastSelectionOutsideOfWindow() {
		this.expectationsForAdd(selectionBuilder, selection, menuSelection);
		this.scrollPanel.setScroller(this.scroller);
		mockery.checking(new Expectations() {{
			oneOf(selection).getHeight(); will(returnValue(500));
			oneOf(selection).getY(); will(returnValue(0));
			oneOf(parentContainer).getY(); will(returnValue(0));
			oneOf(parentContainer).getHeight(); will(returnValue(250));
			oneOf(scroller).show();
		}});
		this.scrollPanel.add(selectionBuilder);
	}*/
}
