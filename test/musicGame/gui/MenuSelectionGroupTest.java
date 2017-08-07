package musicGame.gui;

import static org.hamcrest.CoreMatchers.empty;
import static org.hamcrest.CoreMatchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MenuSelectionGroupTest {
	
	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};

	@Mock private MenuSelection selection;
	@Mock private MenuSelection selection2;

	private MenuSelectionGroup group;
	
	@Before
	public void setUp() {
		this.group = new MenuSelectionGroup();
	}
	
	private void expectInitialToggle() {
		mockery.checking(new Expectations() {{
			oneOf(selection).toggleSelect(true);
		}});
	}
	
	@Test
	public void constructorShouldCreateEmptyGroup() {
		assertThat(this.group.getSelections(), is(empty()));
		assertThat(this.group.getCurrentSelection(), is(nullValue()));
	}
	
	@Test
	public void groupShouldContainOneElementWhenAddingInEmptyGroup() {
		expectInitialToggle();
		this.group.add(selection);
		assertThat(this.group.getSelections(), hasSize(1));
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void selectShouldThrowIndexOutOfBoundsExceptionWhenEmpty() {
		this.group.select(0);
	}
	
	@Test
	public void currentSelectionShouldUpdateWhenSelectingDifferentItem() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection2).isSelected(); will(returnValue(false));
			oneOf(selection).isSelected(); will(returnValue(true));
			oneOf(selection).toggleSelect();
			oneOf(selection2).toggleSelect(true);
		}});
		this.group.select(1);
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void selectNextShouldDoNothingWhenGroupContainsSingleElement() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectPreviousShouldDoNothingWhenGroupContainsSingleElement() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectNextShouldSelectNextItemInGroup() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).toggleSelect();
			oneOf(selection2).toggleSelect();
		}});
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void selectPreviousShouldSelectPreviousItemInGroup() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			exactly(2).of(selection).toggleSelect();
			oneOf(selection2).toggleSelect(true);
			oneOf(selection2).toggleSelect();
			oneOf(selection).isSelected(); will(returnValue(true));
			oneOf(selection2).isSelected(); will(returnValue(false));
		}});
		this.group.select(1);
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}

	@Test
	public void selectNextShouldSelectFirstItemWhenAtEndOfGroup() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			exactly(2).of(selection).toggleSelect();
			oneOf(selection2).toggleSelect(true);
			oneOf(selection2).toggleSelect();
			oneOf(selection).isSelected(); will(returnValue(true));
			oneOf(selection2).isSelected(); will(returnValue(false));
		}});
		this.group.select(1);
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectPreviousShouldSelectLastItemWhenAtBeginningOfGroup() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).toggleSelect();
			oneOf(selection2).toggleSelect();
		}});
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void clearShouldRemoveAllSelections() {
		expectInitialToggle();
		this.group.add(selection);
		this.group.add(selection);
		assertThat(this.group.getSelections(), hasSize(2));
		this.group.clear();
		assertThat(this.group.getSelections(), is(empty()));
	}
	
	@Test
	public void clearShouldDoNothingWhenSelectionListEmpty() {
		assertThat(this.group.getSelections(), is(empty()));
		this.group.clear();
		assertThat(this.group.getSelections(), is(empty()));
	}
	
	@Test
	public void selectShouldOnlyBeExecutedIfSelectionNotAlreadySelected() {
		mockery.checking(new Expectations() {{
			oneOf(selection).toggleSelect(true);
			oneOf(selection).isSelected(); will(returnValue(true));
		}});
		this.group.add(selection);
		this.group.select(0);
	}
	
	@Test
	public void selectShouldNotDeselectCurrentSelectionIfSameAsSelected() {
		mockery.checking(new Expectations() {{
			oneOf(selection).toggleSelect(true);
			exactly(2).of(selection).isSelected(); will(returnValue(false));
			oneOf(selection).toggleSelect(true);
		}});
		this.group.add(selection);
		this.group.select(0);
	}
	
	@Test
	public void selectShouldNotDeselectCurrentSelectionIfNotSelected() {
		mockery.checking(new Expectations() {{
			oneOf(selection).toggleSelect(true);
			oneOf(selection).isSelected(); will(returnValue(false));
			oneOf(selection2).isSelected(); will(returnValue(false));
			oneOf(selection2).toggleSelect(true);
		}});
		this.group.add(selection);
		this.group.add(selection2);
		this.group.select(1);
	}
}
