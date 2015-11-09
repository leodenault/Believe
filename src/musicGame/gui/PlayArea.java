package musicGame.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.Camera;
import musicGame.core.Camera.Layerable;
import musicGame.core.Util;
import musicGame.map.LevelMap;
import musicGame.map.MapBackground;

public class PlayArea extends AbstractContainer {

	private boolean border;
	private Camera camera;
	private LevelMap map;
	private ComponentBase focus;
	
	public PlayArea(GUIContext container, LevelMap map, ComponentBase focus) {
		super(container, 0, 0, container.getWidth(), container.getHeight());
		border = false;
		camera = new Camera(getWidth(), getHeight());
		this.map = map;
		this.focus = focus;
		handleMap();
		camera.scale(rect.getWidth() / LevelMap.TARGET_WIDTH,
				rect.getHeight() / LevelMap.TARGET_HEIGHT);
	}

	private void handleMap() {
		camera.addChild(map);
		map.setFocus(focus);
		
		for (MapBackground background : map.getBackgrounds()) {
			camera.addChild(background);
		}
	}
	
	public void setBorder(boolean border) {
		this.border = border;
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
	
	public void addChild(Layerable child) {
		camera.addChild(child);
	}

	@Override
	protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
		g.pushTransform();
		Rectangle oldClip = Util.changeClipContext(g, rect);
		g.translate(getX(), getY());
		
		camera.render(context, g);
		
		Util.resetClipContext(g, oldClip);
		g.popTransform();

		if (border) {
			g.setColor(new Color(0xffffff));
			g.draw(rect);
		}
	}
	
	public void update(int delta) {
		Rectangle rect = focus.getRect();
		camera.center(rect.getCenterX(), rect.getCenterY());
		
		Rectangle camRect = camera.getRect();
		if (camRect.getX() < 0) {
			camRect.setX(0);
		} else if (camRect.getMaxX() > map.getWidth()) {
			camRect.setX(map.getWidth() - camRect.getWidth());
		}
		
		if (camRect.getY() < 0) {
			camRect.setY(0);
		} else if (camRect.getMaxY() > map.getHeight()) {
			camRect.setY(map.getHeight() - camRect.getHeight());
		}
	}
}
