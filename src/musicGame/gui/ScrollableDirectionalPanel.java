package musicGame.gui;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class ScrollableDirectionalPanel<T extends AbstractComponent> extends DirectionalPanel<T>
	implements MenuSelectionGroup.Listener {

	public ScrollableDirectionalPanel(GUIContext context) {
		super(context);
	}

	@Override
	public void nextSelected(AbstractComponent next) {
		for (T item : this.items) {
			item.setLocation(item.getX(), item.getY() - (item.getHeight() + this.spacing));
		}
	}

	@Override
	public void previousSelected(AbstractComponent previous) {
		
	}
}
