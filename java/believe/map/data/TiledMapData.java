package believe.map.data;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

/** The data model for a Tiled map. */
@AutoValue
public abstract class TiledMapData {
  /** The x component of the player's starting position. */
  public abstract int playerStartX();

  /** The y component of the player's starting position. */
  public abstract int playerStartY();

  /** The width of the map in pixels. */
  public abstract int width();

  /** The height of the map in pixels. */
  public abstract int height();

  /** The list of {@link LayerData} instances representing each layer of tiles in the map. */
  public abstract List<LayerData> layers();

  /**
   * The list of {@link ObjectLayerData} instances representing each layer of objects in the map.
   */
  public abstract List<ObjectLayerData> objectLayers();

  /** Returns a new builder for building instances of {@link MapData}. */
  public static TiledMapData.Builder newBuilder(
      int playerStartX, int playerStartY, int width, int height) {
    return new AutoValue_TiledMapData.Builder()
        .setPlayerStartX(playerStartX)
        .setPlayerStartY(playerStartY)
        .setWidth(width)
        .setHeight(height);
  }

  /** Builder class for constructing instances of {@link MapData}. */
  @AutoValue.Builder
  public abstract static class Builder {
    private final List<LayerData> layers = new ArrayList<>();
    private final List<ObjectLayerData> objectLayers = new ArrayList<>();

    abstract Builder setPlayerStartX(int playerStartX);

    abstract Builder setPlayerStartY(int playerStartY);

    abstract Builder setWidth(int width);

    abstract Builder setHeight(int height);

    abstract Builder setLayers(List<LayerData> layers);

    abstract Builder setObjectLayers(List<ObjectLayerData> objectLayers);

    /** Adds a layer of tiles to the map. */
    public Builder addLayer(LayerData layer) {
      layers.add(layer);
      return this;
    }

    /** Adds a layer of objects to the map. */
    public Builder addObjectLayer(ObjectLayerData objectLayerData) {
      objectLayers.add(objectLayerData);
      return this;
    }

    abstract TiledMapData autoBuild();

    /** Builds an instance of {@link TiledMapData}. */
    public TiledMapData build() {
      return setLayers(layers).setObjectLayers(objectLayers).autoBuild();
    }
  }
}
