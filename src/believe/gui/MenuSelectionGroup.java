package believe.gui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.gui.AbstractComponent;

public class MenuSelectionGroup implements Iterable<MenuSelection> {

  public interface Listener {
    public void nextSelected(AbstractComponent next);
    public void previousSelected(AbstractComponent previous);
  }

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

  public void add(MenuSelection selection) {
    this.selections.add(selection);
    if (this.selections.size() == 1) {
      this.currentSelection = selection;
      this.currentSelection.toggleSelect(true);
    }
  }

  public void remove(MenuSelection selection) {
    this.selections.remove(selection);
  }

  public void select(int index) {
    if (this.selections.isEmpty()) {
      throw new IndexOutOfBoundsException("Cannot select from empty group");
    }

    MenuSelection newSelection = this.selections.get(index);

    if (!newSelection.isSelected()) {
      if (this.currentSelection.isSelected()) {
        this.currentSelection.toggleSelect();
      }
      this.currentSelection = newSelection;
      this.currentSelection.toggleSelect(true);
    }
  }

  public void selectNext() {
    if (this.selections.size() > 1) {
      this.currentSelection.toggleSelect();
      int currentIndex = this.selections.indexOf(this.currentSelection);
      this.currentSelection = this.selections.get((currentIndex + 1) % this.selections.size());
      this.currentSelection.toggleSelect();
      this.notifyListenersNext();
    }
  }

  public void selectPrevious() {
    if (this.selections.size() > 1) {
      this.currentSelection.toggleSelect();
      int currentIndex = this.selections.indexOf(this.currentSelection);
      this.currentSelection = this.selections.get(
          (currentIndex == 0) ? this.selections.size() - 1 : currentIndex - 1);
      this.currentSelection.toggleSelect();
      this.notifyListenersPrevious();
    }
  }

  public void clear() {
    this.selections.clear();
  }

  public boolean isEmpty() {
    return this.selections.isEmpty();
  }

  @Override
  public Iterator<MenuSelection> iterator() {
    return selections.iterator();
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
