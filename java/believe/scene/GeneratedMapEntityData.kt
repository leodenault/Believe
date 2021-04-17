package believe.scene

import believe.core.Updatable
import believe.core.display.Renderable
import believe.physics.manager.PhysicsManageable
import com.google.auto.value.AutoValue
import org.newdawn.slick.Graphics
import java.util.HashSet

/** A data object containing sets of generated entities that are contained within a map. */
@AutoValue
abstract class GeneratedMapEntityData internal constructor() {
    /**
     * Returns the set of [PhysicsManageable] instances that should be handled by the physics
     * engine.
     */
    abstract fun physicsManageables(): Set<PhysicsManageable>

    /**
     * Returns the set of [Renderable] instances that should be drawn to the screen.
     */
    @Deprecated("use {@link #sceneElements()}.")
    abstract fun renderables(): Set<Renderable>

    /**
     * Returns the set of [Updatable] instances that should be updated regularly as part of the
     * game loop.
     */
    @Deprecated("use {@link #sceneElements()}.")
    abstract fun updatables(): Set<Updatable>

    /** Returns the set of [SceneElement] instances that should be drawn to the screen. */
    abstract fun sceneElements(): Set<SceneElement>

    /** Builder for constructing instances of [GeneratedMapEntityData]. */
    @AutoValue.Builder
    abstract class Builder internal constructor() {
        private val physicsManageables: MutableSet<PhysicsManageable> = HashSet()
        private val renderables: MutableSet<Renderable> = HashSet()
        private val updatables: MutableSet<Updatable> = HashSet()
        private val sceneElements: MutableSet<SceneElement> = HashSet()

        abstract fun setPhysicsManageables(
            physicsManageables: Set<@JvmSuppressWildcards PhysicsManageable>
        ): Builder

        abstract fun setRenderables(renderables: Set<@JvmSuppressWildcards Renderable>): Builder
        abstract fun setUpdatables(updatables: Set<@JvmSuppressWildcards Updatable>): Builder
        abstract fun setSceneElements(
            sceneElements: Set<@JvmSuppressWildcards SceneElement>
        ): Builder

        /** Adds a [PhysicsManageable] to this builder. */
        fun addPhysicsManageable(physicsManageable: PhysicsManageable): Builder {
            physicsManageables.add(physicsManageable)
            return this
        }

        /** Adds a [SceneElement] to this builder. */
        fun addSceneElement(sceneElement: SceneElement): Builder {
            sceneElements.add(sceneElement)
            renderables.add(Renderable { g: Graphics ->
                sceneElement.render(g)
            })
            updatables.add(sceneElement)
            return this
        }

        abstract fun autoBuild(): GeneratedMapEntityData

        /** Builds a [GeneratedMapEntityData] based on the contents of this builder. */
        fun build(): GeneratedMapEntityData {
            return setPhysicsManageables(physicsManageables).setSceneElements(sceneElements)
                .setRenderables(renderables).setUpdatables(updatables).autoBuild()
        }
    }

    companion object {
        /** Returns a new builder instance for constructing [GeneratedMapEntityData] instances. */
        @JvmStatic
        fun newBuilder(): Builder {
            return AutoValue_GeneratedMapEntityData.Builder()
        }
    }
}