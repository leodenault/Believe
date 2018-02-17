package musicGame.gui;

import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import musicGame.util.Util;


public class VerticalKeyboardScrollpanel extends AbstractContainer {

  private static final int DEFAULT_SPACING = 10;
  private static final int DEFAULT_BORDER_COLOR = 0x898989;
  private static final int DEFAULT_SCROLLBAR_WIDTH = 20;
  private static final float DEFAULT_BORDER_WIDTH = 5.0f;

  private int panelX;
  private int panelY;
  private MenuSelectionGroup selections;
  private DirectionalPanel panel;
  private ScrollBar scrollBar;

  public VerticalKeyboardScrollpanel(GUIContext container, int x, int y, int width, int itemHeight, int height) {
    super(container, x, y, width, height);
    this.selections = new MenuSelectionGroup();
    int borderWidth = (int)DEFAULT_BORDER_WIDTH;
    panelX = x + borderWidth;
    panelY = y + borderWidth;
    this.scrollBar = new ScrollBar(container, x + width - DEFAULT_SCROLLBAR_WIDTH, y, DEFAULT_SCROLLBAR_WIDTH, height);
    this.panel = new DirectionalPanel(container, panelX, panelY,
        width - (borderWidth * 2) - DEFAULT_SCROLLBAR_WIDTH, itemHeight, DEFAULT_SPACING);
    this.children.add(this.panel);
    this.children.add(this.scrollBar);
  }

  @Override
  public void resetLayout() {
    for (ComponentBase child : children) {
      child.resetLayout();
    }
    this.panel.setLocation(panelX, panelY);
  }

  @Override
  public Iterator<ComponentBase> iterator() {
    return this.panel.iterator();
  }

  @Override
  public void addChild(ComponentBase child) {
    this.panel.addChild(child);
    this.scrollBar.setViewingHeight(this.panel.getHeight());
    this.selections.add((MenuSelection)child); // TODO: Fix the whole selectable hierarchy stuff
  }

  @Override
  public void removeChild(ComponentBase child) {
    this.panel.removeChild(child);
    this.selections.remove((MenuSelection)child);
  }

  public void clear() {
    this.panel.clear();
    this.selections.clear();
    this.resetLayout();
  }

  public boolean isEmpty() {
    return this.selections.isEmpty();
  }

  public void toggleFocus() {
    this.selections.getCurrentSelection().toggleSelect();
  }

  public void reset() {
    if (!this.selections.isEmpty()) {
      this.selections.select(0);
    }
  }

  public void activateSelection() {
    this.selections.getCurrentSelection().activate();
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    Rectangle oldClip = Util.changeClipContext(g, rect);
    super.renderComponent(context, g);
    Util.resetClipContext(g, oldClip);

    g.setColor(new Color(DEFAULT_BORDER_COLOR));
    g.setLineWidth(DEFAULT_BORDER_WIDTH);
    g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }

  protected boolean shouldScrollDown() {
    MenuSelection selection = this.selections.getCurrentSelection();
    List<MenuSelection> list = this.selections.getSelections();
    MenuSelection lastSelection = list.get(list.size() - 1);

    int containerHeight = (int)rect.getHeight();
    int containerY = (int)rect.getY();
    int lastSelectionBottom = lastSelection.getY() + lastSelection.getHeight();
    int selectionBottom = selection.getY() + selection.getHeight();

    return this.isPastMiddle(true, selectionBottom, containerY, containerHeight)
        && lastSelectionBottom - containerY > containerHeight;
  }

  protected boolean shouldScrollUp() {
    MenuSelection selection = this.selections.getCurrentSelection();
    List<MenuSelection> list = this.selections.getSelections();
    MenuSelection firstSelection = list.get(0);
    int containerY = (int)rect.getY();

    return this.isPastMiddle(false, selection.getY(), containerY, container.getHeight())
        && containerY > firstSelection.getY();
  }

  protected boolean isPastMiddle(boolean goingDown, int selectionY, int parentY, int parentHeight) {
    if (goingDown) {
      return selectionY >= (parentY + (parentHeight / 2));
    }

    return selectionY <= (parentY + (parentHeight / 2));
  }

  public void scrollUp() {
    MenuSelection selection = selections.getCurrentSelection();
     List<MenuSelection> list = selections.getSelections();
     int selectionIndex = list.indexOf(selection);

     if (selectionIndex == 0) {
       this.scrollBar.setScrollPosition(1);
       this.panel.setLocation(panelX, panelY - panel.getHeight() + (int)(rect.getHeight() - (DEFAULT_BORDER_WIDTH * 2)));
     } else {
       MenuSelection nextSelection = list.get(selectionIndex - 1);
       int start = selection.getY();
       int end = nextSelection.getY();
       int distance = end - start;

       if (this.shouldScrollUp()) {
         this.scrollBar.scrollBy(distance);
         this.panel.setLocation(panelX, panel.getY() - distance);
       }
     }
     selections.selectPrevious();
  }

   public void scrollDown() {
     MenuSelection selection = selections.getCurrentSelection();
     List<MenuSelection> list = selections.getSelections();
     int selectionIndex = list.indexOf(selection);

     if (selectionIndex == list.size() - 1) {
       this.scrollBar.setScrollPosition(0);
       this.panel.setLocation(panelX, panelY);
     } else {
       MenuSelection nextSelection = list.get(selectionIndex + 1);
       int start = selection.getY();
       int end = nextSelection.getY();
       int distance = end - start;

       if (this.shouldScrollDown()) {
         this.scrollBar.scrollBy(distance);
         this.panel.setLocation(panelX, panel.getY() - distance);
       }
     }
     selections.selectNext();
  }
}
