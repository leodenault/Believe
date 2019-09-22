package believe.map.data;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The data model for a map representing a playable environment. */
@AutoValue
public abstract class MapData {
  /** The x component of the player's starting position. */
  public abstract int playerStartX();

  /** The y component of the player's starting position. */
  public abstract int playerStartY();

  /** The width of the map in pixels. */
  public abstract int width();

  /** The height of the map in pixels. */
  public abstract int height();

  /**
   * The set of {@link BackgroundSceneData} instances for backgrounds which should be displayed
   * behind the map.
   */
  public abstract List<BackgroundSceneData> backgroundScenes();

  /** The list of {@link LayerData} instances representing each layer of tiles in the map. */
  public abstract List<LayerData> layers();

  /**
   * The list of {@link ObjectLayerData} instances representing each layer of objects in the map.
   */
  public abstract List<ObjectLayerData> objectLayers();

  /** Returns a new builder for building instances of {@link MapData}. */
  public static Builder newBuilder(
      int playerStartX,
      int playerStartY,
      int width,
      int height,
      List<BackgroundSceneData> backgroundScenes) {
    return new AutoValue_MapData.Builder()
        .setPlayerStartX(playerStartX)
        .setPlayerStartY(playerStartY)
        .setWidth(width)
        .setHeight(height)
        .setBackgroundScenes(Collections.unmodifiableList(backgroundScenes));
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

    abstract Builder setBackgroundScenes(List<BackgroundSceneData> backgroundScenes);

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

    abstract MapData autoBuild();

    /** Builds an instance of {@link MapData}. */
    public MapData build() {
      return setLayers(layers).setObjectLayers(objectLayers).autoBuild();
    }
  }
}