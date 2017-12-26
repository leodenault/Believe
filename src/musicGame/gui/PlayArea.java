package musicGame.gui;

import java.util.LinkedList;
import java.util.List;

import musicGame.gui.base.AbstractContainer;
import musicGame.gui.base.ComponentBase;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.display.Camera;
import musicGame.core.display.Camera.Layerable;
import musicGame.map.gui.LevelMap;
import musicGame.map.gui.MapBackground;
import musicGame.util.Util;

public class PlayArea extends AbstractContainer {
	private class DynamicHudChild {
		public ComponentBase child;
		public float offsetX;
		public float offsetY;
		
		public DynamicHudChild(ComponentBase child, float offsetX, float offsetY) {
			this.child = child;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}
	}

	private boolean border;
	private Camera camera;
	private LevelMap map;
	private ComponentBase focus;
	private CanvasContainer hud;
	private List<DynamicHudChild> dynamicHudChildren;
	
	public PlayArea(GUIContext container, LevelMap map, ComponentBase focus) {
		this(container, map, focus, 1f, 1f);
	}
	
	/**
	 * Creates the PlayArea using width and height as percentages of the screen size. Assumes that the origin is at 0,0.
	 */
	public PlayArea(GUIContext container, LevelMap map, ComponentBase focus, float clipWidth, float clipHeight) {
		super(container,
				0,
				0,
				convertPercentageToPixels(clipWidth, container.getWidth(), 0),
				convertPercentageToPixels(clipHeight, container.getHeight(), 0));
		int width = convertPercentageToPixels(clipWidth, container.getWidth(), 0);
		int height = convertPercentageToPixels(clipHeight, container.getHeight(), 0);
		border = false;
		camera = new Camera(width, height);
		this.map = map;
		this.focus = focus;
		this.hud = new CanvasContainer(container, 0, 0, width, height);
		handleMap();
		camera.scale((float)container.getWidth() / LevelMap.TARGET_WIDTH,
				(float)container.getHeight() / LevelMap.TARGET_HEIGHT);
		this.dynamicHudChildren = new LinkedList<DynamicHudChild>();
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
	public void resetLayout() {}

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
	
	public void addHudChild(ComponentBase child) {
		hud.addChild(child);
	}
	
	public void addHudChild(ComponentBase child, float minX, float minY, float maxX, float maxY) {
		hud.addChild(child);
		
		int x1 = convertPercentageToPixels(minX, getWidth(), getX());
		int y1 = convertPercentageToPixels(minY, getHeight(), getY());
		int x2 = convertPercentageToPixels(maxX, getWidth(), getX());
		int y2 = convertPercentageToPixels(maxY, getHeight(), getY());
		child.setLocation(x1, y1);
		child.setWidth(x2 - x1);
		child.setHeight(y2 - y1);
	}
	
	public void attachHudChildToFocus(ComponentBase child, float offsetX, float offsetY, float width, float height) {
		hud.addChild(child);
		child.setWidth(convertPercentageToPixels(width, getWidth(), 0));
		child.setHeight(convertPercentageToPixels(height, getHeight(), 0));
		dynamicHudChildren.add(new DynamicHudChild(child, offsetX, offsetY));
	}
	
	public void removeHudChild(ComponentBase child) {
		hud.removeChild(child);
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
		
		hud.render(context, g);
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
		
		updateDynamicHudChildren();
	}
	
	private void updateDynamicHudChildren() {
		Rectangle rect = focus.getRect();
		Vector2f focusLocation = camera.cameraToWindow(new Vector2f(rect.getCenter()));

		for (DynamicHudChild child : dynamicHudChildren) {
			int ox = convertPercentageToPixels(child.offsetX, getWidth(), 0);
			int oy = convertPercentageToPixels(child.offsetY, getHeight(), 0);
			child.child.setLocation((int)(focusLocation.x + ox), (int)(focusLocation.y + oy));
		}
	}
	
	private static int convertPercentageToPixels(float percentage, float length, float offset) {
		return (int)((percentage * length) + offset);
	}
}
