package believe.map.data;

import com.google.auto.value.AutoValue;

/** Data on a Tiled map object layer. */
@AutoValue
public abstract class ObjectLayerData {
  /** Generated entities that are part of the map. */
  public abstract GeneratedMapEntityData generatedMapEntityData();

  /**
   * Instantiates an {@link ObjectLayerData}.
   *
   * @param generatedMapEntityData the {@link GeneratedMapEntityData} associated with the instance.
   */
  public static ObjectLayerData create(GeneratedMapEntityData generatedMapEntityData) {
    return new AutoValue_ObjectLayerData(generatedMapEntityData);
  }
}
