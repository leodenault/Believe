package musicGame.gui;

import musicGame.core.Util;
import musicGame.map.LevelMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class PlayArea extends AbstractContainer {

	private boolean border;
	
	public PlayArea(GUIContext container, LevelMap map) {
		super(container, 0, 0, container.getWidth(), container.getHeight());
		border = false;
	}
	
	public void setBorder(boolean border) {
		this.border = border;
	}
	
	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		g.pushTransform();
		Rectangle oldClip = Util.changeClipContext(g, rect);
		float sx = rect.getWidth() / LevelMap.TARGET_WIDTH;
		float sy = rect.getHeight() / LevelMap.TARGET_HEIGHT;
		g.scale(sx, sy);
		super.renderComponent(context, g);
		Util.resetClipContext(g, oldClip);
		g.popTransform();

		if (border) {
			g.setColor(new Color(0xffffff));
			g.draw(rect);
		}
	}
}
