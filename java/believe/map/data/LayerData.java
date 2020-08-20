package believe.map.data;

import believe.map.tiled.Layer;
import believe.map.tiled.TiledMap;
import com.google.auto.value.AutoValue;

/** The data model for representing a layer of tiles on a {@link MapData} instance. */
@AutoValue
public abstract class LayerData {
  /** Generated entities that are part of the map. */
  public abstract GeneratedMapEntityData generatedMapEntityData();

  /** The identifier for this layer within its containing map. */
  public abstract Layer layer();

  /** Whether this layer should be displayed in front of the player character or behind them. */
  public abstract boolean isFrontLayer();

  /**
   * Whether this layer and its contents should be drawn on the screen.
   *
   * <p>Note that this does not affect the entities contained within this layer, just the layer's
   * graphical tiles.
   */
  public abstract boolean isVisible();

  public static Builder newBuilder(Layer layer) {
    return new AutoValue_LayerData.Builder()
        .setLayer(layer)
        .setGeneratedMapEntityData(GeneratedMapEntityData.newBuilder().build())
        .setIsFrontLayer(false)
        .setIsVisible(true);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    /** Sets the {@link GeneratedMapEntityData} associated with the map. */
    public abstract Builder setGeneratedMapEntityData(
        GeneratedMapEntityData generatedMapEntityData);

    /** Sets the identifier for this layer within its containing map. */
    abstract Builder setLayer(Layer layer);

    /**
     * Sets whether this layer should be displayed in front of the player character or behind them.
     */
    public abstract Builder setIsFrontLayer(boolean isFrontLayer);

    /**
     * Sets whether this layer and its contents should be drawn on the screen.
     *
     * <p>Note that this does not affect the entities contained within this layer, just the layer's
     * graphical tiles.
     */
    public abstract Builder setIsVisible(boolean isVisible);

    /** Builds a {@link LayerData} instance based on the provided builder parameters. */
    public abstract LayerData build();
  }
}
