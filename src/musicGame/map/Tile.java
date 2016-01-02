package musicGame.map;

import musicGame.geometry.Rectangle;
import musicGame.physics.Collidable;

public class Tile implements Collidable {
	
	private boolean topNeighbour;
	private boolean bottomNeighbour;
	private boolean leftNeighbour;
	private boolean rightNeighbour;
	private Rectangle rect;
	
	public Tile(int x, int y, int tileWidth, int tileHeight) {
		rect = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
		topNeighbour = false;
		bottomNeighbour = false;
		leftNeighbour = false;
		rightNeighbour = false;
	}
	
	public boolean hasTopNeighbour() {
		return topNeighbour;
	}

	public void setTopNeighbour(boolean topNeighbour) {
		this.topNeighbour = topNeighbour;
	}

	public boolean hasBottomNeighbour() {
		return bottomNeighbour;
	}

	public void setBottomNeighbour(boolean bottomNeighbour) {
		this.bottomNeighbour = bottomNeighbour;
	}

	public boolean hasLeftNeighbour() {
		return leftNeighbour;
	}

	public void setLeftNeighbour(boolean leftNeighbour) {
		this.leftNeighbour = leftNeighbour;
	}

	public boolean hasRightNeighbour() {
		return rightNeighbour;
	}

	public void setRightNeighbour(boolean rightNeighbour) {
		this.rightNeighbour = rightNeighbour;
	}

	@Override
	public Rectangle getRect() {
		return rect;
	}

	@Override
	public void collision(Collidable other) {}

	@Override
	public CollidableType getType() {
		return CollidableType.TILE;
	}
}
