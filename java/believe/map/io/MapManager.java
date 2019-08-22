package believe.map.io;

import believe.map.data.MapData;
import org.newdawn.slick.SlickException;

import java.util.Optional;

/** Manages loading maps from the file system. */
public interface MapManager {
  /**
   * Fetches a map from the file system.
   *
   * @param name the name of the map to load.
   * @return an optional {@link MapData} instance containing all details associated with the map. If
   *     loading the map was unsuccessful, then {@link Optional#empty()} is returned.
   * @throws SlickException if an error occurs while loading the map.
   */
  Optional<MapData> getMap(String name) throws SlickException;
}
