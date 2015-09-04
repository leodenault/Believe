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
	
	public PlayArea(GUIContext container, LevelMap map) {
		super(container, 0, 0, container.getWidth(), container.getHeight());
		border = false;
		camera = new Camera();
		container.getInput().addKeyListener(camera);
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
		float sx = rect.getWidth() / LevelMap.TARGET_WIDTH;
		float sy = rect.getHeight() / LevelMap.TARGET_HEIGHT;
		g.translate(getX(), getY());
		g.scale(sx, sy);
		
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
		camera.update(delta);
	}
}
