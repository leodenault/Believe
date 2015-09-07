package musicGame.gui;

import musicGame.core.Camera;
import musicGame.core.Util;
import musicGame.map.LevelMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.Log;

public class PlayArea extends AbstractContainer {

	private boolean border;
	private Camera camera;
	private LevelMap map;
	private ComponentBase center;
	
	public PlayArea(GUIContext container, LevelMap map, ComponentBase center) {
		super(container, 0, 0, container.getWidth(), container.getHeight());
		border = false;
		camera = new Camera(getWidth(), getHeight());
		this.map = map;
		this.center = center;
		camera.addChild(map);
		camera.addChild(center);
		camera.scale(rect.getWidth() / LevelMap.TARGET_WIDTH,
				rect.getHeight() / LevelMap.TARGET_HEIGHT);
	}
	
	public void setBorder(boolean border) {
		this.border = border;
	}
	
	@Override
	public void addChild(ComponentBase child) {
		camera.addChild(child);
	}

	@Override
	protected void resetLayout() {}

	// This is so we can handle moving the children in the graphics
	// rendering part. The play area behaves a little differently from
	// GUI components
	@Override
	public void setLocation(int x, int y) {
		if (rect != null) {
			rect.setLocation(x, y);
		}
	}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) {
		g.pushTransform();
		Rectangle oldClip = Util.changeClipContext(g, rect);
		g.translate(getX(), getY());
		
		// TODO: Find a good way to avoid this try/catch
		try {
			camera.render(context, g);
		} catch (SlickException e) {
			Log.error("The camera encountered an error while rendering");
		}
		
		Util.resetClipContext(g, oldClip);
		g.popTransform();

		if (border) {
			g.setColor(new Color(0xffffff));
			g.draw(rect);
		}
	}
	
	public void update(int delta) {
		Rectangle rect = center.getRect();
		camera.center(rect.getCenterX(), rect.getCenterY());
		
		Rectangle camRect = camera.getRect();
		if (camRect.getX() < 0) {
			camRect.setX(0);
		} else if (camRect.getMaxX() > map.getWidth()) {
			camRect.setX(map.getWidth() - camRect.getWidth());
		}
	}
}