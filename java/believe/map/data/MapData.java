package believe.map.data;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

/** The data model for a map representing a playable environment. */
@AutoValue
public abstract class MapData {
  /** The {@link TiledMapData} containing details provided by a Tiled map. */
  public abstract TiledMapData tiledMapData();

  /**
   * The set of {@link BackgroundSceneData} instances for backgrounds which should be displayed
   * behind the map.
   */
  public abstract List<BackgroundSceneData> backgroundScenes();

  /** Returns a new builder for building instances of {@link MapData}. */
  public static Builder newBuilder(TiledMapData tiledMapData) {
    return new AutoValue_MapData.Builder().setTiledMapData(tiledMapData);
  }

  /** Builder class for constructing instances of {@link MapData}. */
  @AutoValue.Builder
  public abstract static class Builder {
    private List<BackgroundSceneData> backgroundScenes = new ArrayList<>();

    abstract Builder setTiledMapData(TiledMapData tiledMapData);

    abstract Builder setBackgroundScenes(List<BackgroundSceneData> backgroundScenes);

    public Builder addBackgroundScene(BackgroundSceneData backgroundSceneData) {
      backgroundScenes.add(backgroundSceneData);
      return this;
    }

    abstract MapData autoBuild();

    /** Builds an instance of {@link MapData}. */
    public MapData build() {
      return setBackgroundScenes(backgroundScenes).autoBuild();
    }
  }
}
