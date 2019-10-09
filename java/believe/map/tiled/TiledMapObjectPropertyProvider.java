package believe.map.tiled;

import believe.core.PropertyProvider;
import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
abstract class TiledMapObjectPropertyProvider implements PropertyProvider {
  abstract TiledMap tiledMap();

  abstract int layerId();

  abstract int objectId();

  @Override
  public Optional<String> getProperty(String key) {
    return tiledMap().getObjectProperty(layerId(), objectId(), key);
  }

  static TiledMapObjectPropertyProvider create(TiledMap tiledMap, int layerId, int objectId) {
    return new AutoValue_TiledMapObjectPropertyProvider(tiledMap, layerId, objectId);
  }
}
