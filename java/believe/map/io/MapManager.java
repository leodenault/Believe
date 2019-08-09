package believe.map.io;

import believe.map.data.MapData;
import org.newdawn.slick.SlickException;

/** Manages loading maps from the file system. */
public interface MapManager {
  /**
   * Fetches a map from the file system.
   *
   * @param name the name of the map to load.
   * @return a {@link MapData} instance containing all details associated with the map.
   * @throws SlickException if an error occurs while loading the map.
   */
  MapData getMap(String name) throws SlickException;
}
