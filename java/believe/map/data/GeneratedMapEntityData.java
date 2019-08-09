package believe.map.data;

import believe.core.Updatable;
import believe.core.display.Renderable;
import believe.physics.manager.PhysicsManageable;
import com.google.auto.value.AutoValue;

import java.util.HashSet;
import java.util.Set;

/** A data object containing sets of generated entities that are contained within a map. */
@AutoValue
public abstract class GeneratedMapEntityData {
  /**
   * Returns the set of {@link PhysicsManageable} instances that should be handled by the physics
   * engine.
   */
  public abstract Set<PhysicsManageable> physicsManageables();

  /** Returns the set of {@link Renderable} instances that should be drawn to the screen. */
  public abstract Set<Renderable> renderables();

  /**
   * Returns the set of {@link Updatable} instances that should be updated regularly as part of the
   * game loop.
   */
  public abstract Set<Updatable> updatables();

  /** Returns a new builder instance for constructing {@link GeneratedMapEntityData} instances. */
  public static Builder newBuilder() {
    return new AutoValue_GeneratedMapEntityData.Builder();
  }

  /** Builder for constructing instances of {@link GeneratedMapEntityData}. */
  @AutoValue.Builder
  public abstract static class Builder {
    private final Set<PhysicsManageable> physicsManageables = new HashSet<>();
    private final Set<Renderable> renderables = new HashSet<>();
    private final Set<Updatable> updatables = new HashSet<>();

    abstract Builder setPhysicsManageables(Set<PhysicsManageable> physicsManageables);

    abstract Builder setRenderables(Set<Renderable> renderables);

    abstract Builder setUpdatables(Set<Updatable> updatables);

    /** Adds a {@link PhysicsManageable} to this builder. */
    public Builder addPhysicsManageable(PhysicsManageable physicsManageable) {
      physicsManageables.add(physicsManageable);
      return this;
    }

    /** Adds a {@link Renderable} to this builder. */
    public Builder addRenderable(Renderable renderable) {
      renderables.add(renderable);
      return this;
    }

    /** Adds an {@link Updatable} to this builder. */
    public Builder addUpdatable(Updatable updatable) {
      updatables.add(updatable);
      return this;
    }

    abstract GeneratedMapEntityData autoBuild();

    /** Builds a {@link GeneratedMapEntityData} based on the contents of this builder. */
    public GeneratedMapEntityData build() {
      return setPhysicsManageables(physicsManageables)
          .setRenderables(renderables)
          .setUpdatables(updatables)
          .autoBuild();
    }

    Builder() {}
  }
}
