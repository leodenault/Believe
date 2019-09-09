package believe.map.tiled.testing;

import believe.map.tiled.TiledMap;
import javax.annotation.Nullable;

import java.util.Optional;

/** Fake {@link TiledMap} for use in testing. */
public final class FakeTiledMap extends TiledMap {
  private static final String NO_PROPERTY = "no property";

  private final String tilePropertyValue;
  private final String objectPropertyValue;

  private FakeTiledMap(String tilePropertyValue, String objectPropertyValue) {
    super("", "");
    this.tilePropertyValue = tilePropertyValue;
    this.objectPropertyValue = objectPropertyValue;
  }

  public static FakeTiledMap tiledMapWithTilePropertyValue(String tilePropertyValue) {
    return new FakeTiledMap(tilePropertyValue, NO_PROPERTY);
  }

  public static FakeTiledMap tiledMapWithDefaultPropertyValues() {
    return new FakeTiledMap(NO_PROPERTY, NO_PROPERTY);
  }

  public static FakeTiledMap tiledMapWithObjectPropertyValue(String objectPropertyValue) {
    return new FakeTiledMap(NO_PROPERTY, objectPropertyValue);
  }

  @Override
  public Optional<String> getTileProperty(int tileID, String propertyName) {
    if (tilePropertyValue.equals(NO_PROPERTY)) {
      return Optional.empty();
    }
    return Optional.of(tilePropertyValue);
  }

  @Override
  public Optional<String> getObjectProperty(int groupID, int objectID, String propertyName) {
    if (objectPropertyValue.equals(NO_PROPERTY)) {
      return Optional.empty();
    }
    return Optional.of(objectPropertyValue);
  }

  @Override
  public void load() {
    // Do nothing to avoid attempting to load resources from disk.
  }
}
