package musicGame.gui;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class MenuSelectionGroupTest {
	@Mock private MenuSelection selection;
	@Mock private MenuSelection selection2;

	private MenuSelectionGroup group;
	
	@Before
	public void setUp() {
		initMocks(this);
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
		when(selection2.isSelected()).thenReturn(false);
		when(selection.isSelected()).thenReturn(true);
		this.group.select(1);
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void selectNextShouldDoNothingWhenGroupContainsSingleElement() {
		this.group.add(selection);
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectPreviousShouldDoNothingWhenGroupContainsSingleElement() {
		this.group.add(selection);
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectNextShouldSelectNextItemInGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection2));
	}
	
	@Test
	public void selectPreviousShouldSelectPreviousItemInGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		when(selection.isSelected()).thenReturn(true);
		when(selection2.isSelected()).thenReturn(false);
		this.group.select(1);
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}

	@Test
	public void selectNextShouldSelectFirstItemWhenAtEndOfGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		when(selection.isSelected()).thenReturn(true);
		when(selection2.isSelected()).thenReturn(false);
		this.group.select(1);
		this.group.selectNext();
		assertThat(this.group.getCurrentSelection(), is(selection));
	}
	
	@Test
	public void selectPreviousShouldSelectLastItemWhenAtBeginningOfGroup() {
		this.group.add(selection);
		this.group.add(selection2);
		this.group.selectPrevious();
		assertThat(this.group.getCurrentSelection(), is(selection2));
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
	
	@Test
	public void selectShouldOnlyBeExecutedIfSelectionNotAlreadySelected() {
		when(selection.isSelected()).thenReturn(true);
		this.group.add(selection);
		this.group.select(0);
	}
	
	@Test
	public void selectShouldNotDeselectCurrentSelectionIfSameAsSelected() {
		when(selection.isSelected()).thenReturn(false);
		this.group.add(selection);
		this.group.select(0);
	}
	
	@Test
	public void selectShouldNotDeselectCurrentSelectionIfNotSelected() {
		when(selection.isSelected()).thenReturn(false);
		when(selection2.isSelected()).thenReturn(false);
		this.group.add(selection);
		this.group.add(selection2);
		this.group.select(1);
	}
}
