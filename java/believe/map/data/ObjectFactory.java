package believe.map.data;

import believe.scene.GeneratedMapEntityData;

/** A factory for generating new instances of map objects. */
public interface ObjectFactory {
  ObjectFactory EMPTY = generatedMapEntityDataBuilder -> {};

  /**
   * Creates a new instance of a map object and inserts it into {@code
   * generatedMapEntityDataBuilder}.
   */
  void createAndAddTo(GeneratedMapEntityData.Builder generatedMapEntityDataBuilder);
}
