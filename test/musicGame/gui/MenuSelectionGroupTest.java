package musicGame.gui;

import static org.hamcrest.CoreMatchers.empty;
import static org.hamcrest.CoreMatchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MenuSelectionGroupTest {
	@Mock private MenuSelection selection;
	@Mock private MenuSelection selection2;

	private MenuSelectionGroup group;
	
	@Before
	public void setUp() {
		initMocks(this);
		this.group = new MenuSelectionGroup();
	}
	
	private void expectInitialToggle() {
		mockery.checking(new Expectations() {{

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
			when(selection2.isSelected()).thenReturn(false);
			when(selection.isSelected()).thenReturn(true);


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


			when(selection.isSelected()).thenReturn(true);
			when(selection2.isSelected()).thenReturn(false);
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


			when(selection.isSelected()).thenReturn(true);
			when(selection2.isSelected()).thenReturn(false);
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

			when(selection.isSelected()).thenReturn(true);
		}});
		this.group.add(selection);
		this.group.select(0);
	}
	
	@Test
	public void selectShouldNotDeselectCurrentSelectionIfSameAsSelected() {
		mockery.checking(new Expectations() {{

			exactly(2).of(selection).isSelected(); will(returnValue(false));

		}});
		this.group.add(selection);
		this.group.select(0);
	}
	
	@Test
	public void selectShouldNotDeselectCurrentSelectionIfNotSelected() {
		mockery.checking(new Expectations() {{

			when(selection.isSelected()).thenReturn(false);
			when(selection2.isSelected()).thenReturn(false);

		}});
		this.group.add(selection);
		this.group.add(selection2);
		this.group.select(1);
	}
}
