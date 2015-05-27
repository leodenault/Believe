package musicGame.gui;

import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class ScrollableDirectionalPanel extends DirectionalPanel
	implements MenuSelectionGroup.Listener {

	public ScrollableDirectionalPanel(GUIContext context, int itemWidth, int itemHeight) {
		super(context, itemWidth, itemHeight);
	}

	@Override
	public void nextSelected(AbstractComponent next) {
		for (AbstractComponent item : this.children) {
			item.setLocation(item.getX(), item.getY() - (item.getHeight() + this.spacing));
		}
	}

	@Override
	public void previousSelected(AbstractComponent previous) {
		
	}
}
