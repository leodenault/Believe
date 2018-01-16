package musicGame.map.collidable;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;

import musicGame.geometry.Rectangle;
import musicGame.map.io.MapEntityGenerator;
import musicGame.physics.collision.Collidable;

public class Tile implements Collidable {
  public static class Generator implements MapEntityGenerator<Tile> {
    @Override
    public Tile generateMapEntity(
        TiledMap map,
        GUIContext container,
        int tileId,
        int x,
        int y,
        int tileWidth,
        int tileHeight,
        int layer)
            throws SlickException {
      Tile tile = new Tile(x, y, tileWidth, tileHeight);
      findNeighbours(map, tile, x, y, layer);
      return tile;
    }

    private static void findNeighbours(TiledMap map, Tile tile, int x, int y, int layer) {
      if (x > 0 && map.getTileId(x - 1, y, layer) != EMPTY_TILE) {
        tile.setLeftNeighbour(true);
      }

      if (x + 1 < map.getWidth() && map.getTileId(x + 1, y, layer) != EMPTY_TILE) {
        tile.setRightNeighbour(true);
      }

      if (y > 0 && map.getTileId(x, y - 1, layer) != EMPTY_TILE) {
        tile.setTopNeighbour(true);
      }

      if (y + 1 < map.getHeight() && map.getTileId(x, y + 1, layer) != EMPTY_TILE) {
        tile.setBottomNeighbour(true);
      }
    }
  }

  public static final int EMPTY_TILE = 0;

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
