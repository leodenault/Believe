package musicGame.gui;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import musicGame.gui.MenuSelection;
import musicGame.gui.MenuSelectionGroup;

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
	
	@Test
	public void constructorShouldCreateEmptyGroup() {
		assertThat(this.group.getSelections(), is(empty()));
		assertThat(this.group.getCurrentSelection(), is(nullValue()));
	}
	
	@Test
	public void groupShouldContainOneElementWhenAddingInEmptyGroup() {
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
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection2).select();
		}});
		this.group.select(1);
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void selectNextShouldThrowIndexOutOfBOundsExceptionWhenEmpty() {
		this.group.selectNext();
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void selectPreviousShouldThrowIndexOutOfBOundsExceptionWhenEmpty() {
		this.group.selectPrevious();
	}
	
	@Test
	public void selectNextShouldSelectSameElementWhenGroupContainsSingleElement() {
		this.group.add(selection);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection).select();
		}});
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectPreviousShouldSelectSameElementWhenGroupContainsSingleElement() {
		this.group.add(selection);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection).select();
		}});
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectNextShouldSelectNextItemInGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection2).select();
		}});
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void selectPreviousShouldSelectNextItemInGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection).select();
			oneOf(selection2).deselect();
			oneOf(selection2).select();
		}});
		this.group.select(1);
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}

	@Test
	public void selectNextShouldSelectFirstItemWhenAtEndOfGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection).select();
			oneOf(selection2).deselect();
			oneOf(selection2).select();
		}});
		this.group.select(1);
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectPreviousShouldSelectLastItemWhenAtBeginningOfGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		mockery.checking(new Expectations() {{
			oneOf(selection).deselect();
			oneOf(selection2).select();
		}});
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void setPlaySoundShouldSetPlaySoundOnEachMenuSelection() {
		this.group.add(selection);
		this.group.add(selection);
		mockery.checking(new Expectations() {{
			exactly(2).of(selection).setPlaySound(true);
		}});
		this.group.setPlaySound(true);
	}
	
	@Test
	public void clearShouldRemoveAllSelections() {
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
}
