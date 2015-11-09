package musicGame.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import musicGame.geometry.Rectangle;

public class Camera {
	public interface Layerable {
		int getLayer();
		void renderComponent(GUIContext context, Graphics g, float xMin, float xMax) throws SlickException;
	}
	
	public static final float SCROLL_SPEED = 0.2f; // Pixels per millisecond
	private static final float LAYER_SPEED = 0.1f;

	private float scaleX;
	private float scaleY;
	private Rectangle rect;
	private List<Layerable>[] children;
	
	@SuppressWarnings("unchecked")
	public Camera(float width, float height) {
		scaleX = 1;
		scaleY = 1;
		rect = new Rectangle(0, 0, width, height);
		children = (List<Layerable>[])new List[1];
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public void center(float x, float y) {
		rect.setCenterX(x);
		rect.setCenterY(y);
	}

	public void scale(float x, float y) {
		scaleX = x;
		scaleY = y;
		rect.setSize(rect.getWidth() * (1/x), rect.getHeight() * (1/y));
	}
	
	public void addChild(Layerable child) {
		int layer = child.getLayer();
		
		if (layer < 0) {
			layer = 0;
		}
		
		if (children.length < layer + 1) {
			children = Arrays.copyOf(children, layer + 1);
		}
		
		List<Layerable> layerChildren = children[layer];
		
		if (layerChildren == null) {
			layerChildren = new LinkedList<Layerable>();
			children[layer] = layerChildren;
		}
		
		layerChildren.add(child);
	}
	
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.pushTransform();
		g.scale(scaleX, scaleY);
		
		for (int layer = children.length - 1; layer >= 0; layer--) {
			List<Layerable> layerChildren = children[layer];
			if (layerChildren != null) {
				for (Layerable child : children[layer]) {
					float speed = layer < 0 ? 1 : 1 / (1 + layer * LAYER_SPEED);

					g.pushTransform();
					g.translate(-rect.getX() * speed, -rect.getY() * speed);
					float alteredWidth = rect.getWidth() / (2 * speed);
					float cx = rect.getX() * speed + rect.getWidth() / 2;
					child.renderComponent(context, g, cx - alteredWidth, cx + alteredWidth);
					g.popTransform();
				}
			}
		}
		
		g.popTransform();

	}
}
