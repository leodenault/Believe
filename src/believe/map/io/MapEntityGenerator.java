package believe.map.io;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;

public interface MapEntityGenerator<T> {
  T generateMapEntity(
      TiledMap map,
      GUIContext container,
      int tileId,
      int x,
      int y,
      int tileWidth,
      int tileHeight, int layer)
      throws SlickException;
}
