package believe.map.data;

import believe.core.Updatable;
import believe.core.display.Renderable;
import believe.physics.manager.PhysicsManageable;
import believe.scene.SceneElement;
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

  /**
   * Returns the set of {@link Renderable} instances that should be drawn to the screen.
   *
   * @deprecated use {@link #sceneElements()}.
   */
  @Deprecated
  public abstract Set<Renderable> renderables();

  /**
   * Returns the set of {@link Updatable} instances that should be updated regularly as part of the
   * game loop.
   *
   * @deprecated use {@link #sceneElements()}.
   */
  @Deprecated
  public abstract Set<Updatable> updatables();

  /** Returns the set of {@link SceneElement} instances that should be drawn to the screen. */
  public abstract Set<SceneElement> sceneElements();

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
    private final Set<SceneElement> sceneElements = new HashSet<>();

    abstract Builder setPhysicsManageables(Set<PhysicsManageable> physicsManageables);

    abstract Builder setRenderables(Set<Renderable> renderables);

    abstract Builder setUpdatables(Set<Updatable> updatables);

    abstract Builder setSceneElements(Set<SceneElement> sceneElements);

    /** Adds a {@link PhysicsManageable} to this builder. */
    public Builder addPhysicsManageable(PhysicsManageable physicsManageable) {
      physicsManageables.add(physicsManageable);
      return this;
    }

    /** Adds a {@link SceneElement} to this builder. */
    public Builder addSceneElement(SceneElement sceneElement) {
      sceneElements.add(sceneElement);
      renderables.add(g -> sceneElement.render(g));
      updatables.add(sceneElement);
      return this;
    }

    abstract GeneratedMapEntityData autoBuild();

    /** Builds a {@link GeneratedMapEntityData} based on the contents of this builder. */
    public GeneratedMapEntityData build() {
      return setPhysicsManageables(physicsManageables)
          .setSceneElements(sceneElements)
          .setRenderables(renderables)
          .setUpdatables(updatables)
          .autoBuild();
    }

    Builder() {}
  }
}
