package musicGame.GUI;

import java.util.Iterator;
import java.util.LinkedList;

public class MenuSelectionGroup implements Iterable<MenuSelection> {

	private static final MenuSelection EMPTY = null;
	
	private LinkedList<MenuSelection> selections;
	private MenuSelection currentSelection;
	
	public MenuSelectionGroup() {
		this.selections = new LinkedList<MenuSelection>();
	}
	
	protected LinkedList<MenuSelection> getSelections() {
		return this.selections;
	}
	
	public MenuSelection getCurrentSelection() {
		return this.currentSelection;
	}
	
	public void setPlaySound(boolean playSound) {
		for (MenuSelection selection : this.selections) {
			selection.setPlaySound(playSound);
		}
	}

	public void add(MenuSelection selection) {
		this.selections.add(selection);
		if (this.selections.size() == 1) {
			this.currentSelection = selection;
		}
	}
	
	public void select(int index) {
		this.checkIfEmpty();
		this.currentSelection.deselect();
		this.currentSelection = this.selections.get(index);
		this.currentSelection.select();
	}
	
	public void selectNext() {
		this.checkIfEmpty();
		this.currentSelection.deselect();
		int currentIndex = this.selections.indexOf(this.currentSelection);
		this.currentSelection = this.selections.get((currentIndex + 1) % this.selections.size());
		this.currentSelection.select();
	}
	
	public void selectPrevious() {
		this.checkIfEmpty();
		this.currentSelection.deselect();
		int currentIndex = this.selections.indexOf(this.currentSelection);
		this.currentSelection = this.selections.get(
				(currentIndex == 0) ? this.selections.size() - 1 : currentIndex - 1);
		this.currentSelection.select();
	}
	
	@Override
	public Iterator<MenuSelection> iterator() {
		return selections.iterator();
	}
	
	private void checkIfEmpty() {
		if (this.currentSelection == EMPTY) {
			throw new IndexOutOfBoundsException("Cannot select from empty group");
		}
	}
}
