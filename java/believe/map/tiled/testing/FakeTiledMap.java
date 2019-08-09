package believe.map.tiled.testing;

import believe.map.tiled.TiledMap;

import java.util.Optional;

/** Fake {@link TiledMap} for use in testing. */
public final class FakeTiledMap extends TiledMap {
  private static final String NO_TILE_PROPERTY = "no tile property";

  private final String tilePropertyValue;

  private FakeTiledMap(String tilePropertyValue) {
    super("", "");
    this.tilePropertyValue = tilePropertyValue;
  }

  public static FakeTiledMap tiledMapWithTilePropertyValue(String tilePropertyValue) {
    return new FakeTiledMap(tilePropertyValue);
  }

  public static FakeTiledMap tiledMapWithDefaultTilePropertyValue() {
    return new FakeTiledMap(NO_TILE_PROPERTY);
  }

  @Override
  public Optional<String> getTileProperty(int tileID, String propertyName) {
    if (tilePropertyValue.equals(NO_TILE_PROPERTY)) {
      return Optional.empty();
    }
    return Optional.of(tilePropertyValue);
  }

  @Override
  public void load() {
    // Do nothing to avoid attempting to load resources from disk.
  }
}
