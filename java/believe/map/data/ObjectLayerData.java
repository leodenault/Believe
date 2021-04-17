package believe.map.data;

import com.google.auto.value.AutoValue;
import java.util.HashSet;
import java.util.Set;

/** Data on a Tiled map object layer. */
@AutoValue
public abstract class ObjectLayerData {
  /** Generated entities that are part of the map. */
  public abstract Set<ObjectFactory> objectFactories();

  public static Builder newBuilder() {
    return new AutoValue_ObjectLayerData.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    private final Set<ObjectFactory> objectFactories = new HashSet<>();

    abstract Builder setObjectFactories(Set<ObjectFactory> objectFactories);

    public Builder addObjectFactory(ObjectFactory objectFactory) {
      objectFactories.add(objectFactory);
      return this;
    }

    abstract ObjectLayerData autoBuild();

    public ObjectLayerData build() {
      return setObjectFactories(objectFactories).autoBuild();
    }
  }
}
