package believe.map.data.testing

import believe.map.data.ObjectFactory
import believe.scene.GeneratedMapEntityData
import com.google.common.truth.FailureMetadata
import com.google.common.truth.IterableSubject
import com.google.common.truth.Subject
import com.google.common.truth.Subject.Factory

/** A Google Truth subject for making test assertions about [ObjectFactory] instances. */
class ObjectFactorySubject(
    metadata: FailureMetadata, actual: ObjectFactory
) : Subject(metadata, actual) {
    private val generatedMapEntityData: GeneratedMapEntityData =
        GeneratedMapEntityData.newBuilder().apply {
            actual.createAndAddTo(this)
        }.build()

    /**
     * Begins an assertion on the elements contained within the
     * [believe.physics.manager.PhysicsManageable] instances output by the [ObjectFactory].
     */
    fun outputPhysicsManageableSet(): IterableSubject = check(
        "generatedMapEntityData.physicsManageables"
    ).that(generatedMapEntityData.physicsManageables())

    /**
     * Begins an assertion on the elements contained within the [believe.scene.SceneElement]
     * instances output by the [ObjectFactory].
     */
    fun outputSceneElementSet(): IterableSubject = check(
        "generatedMapEntityData.sceneElements"
    ).that(generatedMapEntityData.sceneElements())

    companion object {
        /** Returns a subject [Factory] for making assertions about an [ObjectFactory]. */
        @JvmStatic
        fun objectFactories(): Factory<ObjectFactorySubject, ObjectFactory> =
            Factory { failureMetadata, actual -> ObjectFactorySubject(failureMetadata, actual) }
    }
}