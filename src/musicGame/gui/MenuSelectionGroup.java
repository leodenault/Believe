package musicGame.gui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.gui.AbstractComponent;

public class MenuSelectionGroup implements Iterable<MenuSelection> {

	public interface Listener {
		public void nextSelected(AbstractComponent next);
		public void previousSelected(AbstractComponent previous);
	}
	
	private boolean playSound;
	private LinkedList<MenuSelection> selections;
	private MenuSelection currentSelection;
	private List<Listener> listeners;
	
	public MenuSelectionGroup() {
		this.selections = new LinkedList<MenuSelection>();
		this.listeners = new LinkedList<Listener>();
	}
	
	public List<MenuSelection> getSelections() {
		return this.selections;
	}
	
	public MenuSelection getCurrentSelection() {
		return this.currentSelection;
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void setPlaySound(boolean playSound) {
		this.playSound = playSound;
		for (MenuSelection selection : this.selections) {
			selection.setPlaySound(playSound);
		}
	}

	public void add(MenuSelection selection) {
		this.selections.add(selection);
		selection.setPlaySound(this.playSound);
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
		this.notifyListenersNext();
	}
	
	public void selectPrevious() {
		this.checkIfEmpty();
		this.currentSelection.deselect();
		int currentIndex = this.selections.indexOf(this.currentSelection);
		this.currentSelection = this.selections.get(
				(currentIndex == 0) ? this.selections.size() - 1 : currentIndex - 1);
		this.currentSelection.select();
		this.notifyListenersPrevious();
	}
	
	public void clear() {
		this.selections.clear();
	}
	
	@Override
	public Iterator<MenuSelection> iterator() {
		return selections.iterator();
	}
	
	private void checkIfEmpty() {
		if (this.selections.isEmpty()) {
			throw new IndexOutOfBoundsException("Cannot select from empty group");
		}
	}
	
	private void notifyListenersPrevious() {
		/*for (Listener listener : listeners) {
			listener.previousSelected(this.currentSelection);
		}*/
	}
	
	private void notifyListenersNext() {
		/*for (Listener listener : listeners) {
			listener.nextSelected(this.currentSelection);
		}*/
	}
}
