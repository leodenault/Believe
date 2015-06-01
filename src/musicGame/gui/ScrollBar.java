package musicGame.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class ScrollBar extends ComponentBase {

	private static final Color BAR_COLOR = new Color(0x000057);
	private static final Color CURSOR_COLOR = new Color(0x00ff00);
	
	private int viewingHeight;
	private Rectangle cursor;
	
	public ScrollBar(GUIContext container, int x, int y, int width, int height) {
		super(container, x, y, width, height);
		cursor = new Rectangle(x, y, width, 0);
	}
	
	/**
	 * Sets the height that can be viewed by the scroller, not the actual
	 * scroller height.
	 * 
	 * @param height The height that can be viewed
	 */
	public void setViewingHeight(int height) {
		viewingHeight = height;
		float rectHeight = rect.getHeight();
		if (height > rectHeight) {
			cursor.setHeight(rectHeight * rectHeight / height);
		} else {
			cursor.setHeight(0.0f);
		}
	}
	
	/**
	 * Scroll by a given number of pixels towards the bottom or top of
	 * the viewport
	 * 
	 * @param pixels The number of viewport pixels by which to scroll
	 */
	public void scrollBy(float pixels) {
		if (cursor.getHeight() > 0) {
			float distance = (pixels / viewingHeight) * rect.getHeight();
			float newPosition = cursor.getY() + distance;
			
			if (newPosition < 0) {
				cursor.setY(0);
			} else if (newPosition > rect.getMaxY()) {
				cursor.setY(rect.getMaxY() - cursor.getHeight());
			} else {
				cursor.setY(newPosition);
			}
		}
	}
	
	public void setScrollPosition(float percentage) {
		if (cursor.getHeight() > 0 && percentage >= 0 && percentage <= 1) {
			cursor.setY((rect.getHeight() - cursor.getHeight()) * percentage);
		}
	}

	@Override
	protected void resetLayout() {
		cursor.setLocation(rect.getX(), rect.getY());
		cursor.setHeight(0);
	}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		g.setColor(BAR_COLOR);
		g.fill(rect);
		g.setColor(CURSOR_COLOR);
		g.fill(cursor);
	}

}
