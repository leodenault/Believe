package musicGame.geometry;

public class Rectangle extends org.newdawn.slick.geom.Rectangle {

	private static final long serialVersionUID = -1114860881512242744L;

	public Rectangle(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public Rectangle intersection(org.newdawn.slick.geom.Rectangle rect) {
		float xmin = rect.getMinX();
		float xmax = rect.getMaxX();
		
		float ymin = rect.getMinY();
		float ymax = rect.getMaxY();
		
		if (this.intersects(rect)) {
			if (xmin >= minX && xmax <= maxX && ymin >= minY && ymax <= maxY) {
				return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			} else if (minX >= xmin && maxX <= xmax && minY >= ymin && maxY <= ymax) {
				return new Rectangle(x, y, width, height);
			}
			
			float ix = Math.max(xmin, minX);
			float iy = Math.max(ymin, minY);
			float iwidth = Math.min(xmax, maxX) - ix;
			float iheight = Math.min(ymax, maxY) - iy;
			
			return new Rectangle(ix, iy, iwidth, iheight);
		}
		
		return new Rectangle(0, 0, 0, 0);
	}
}