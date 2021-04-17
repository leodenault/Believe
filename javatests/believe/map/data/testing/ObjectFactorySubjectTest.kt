package believe.map.data.testing

import believe.map.data.ObjectFactory
import believe.map.data.testing.Truth.assertThat
import believe.physics.manager.PhysicsManageable
import believe.scene.SceneElement
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test

class ObjectFactorySubjectTest {
    @Test
    fun outputsPhysicsManageableSet_returnsIterableSubjectForPhysicsManageables() {
        val physicsManageables = List(2) { mock<PhysicsManageable>() }
        val objectFactory = ObjectFactory {
            physicsManageables.forEach { physicsManageable ->
                it.addPhysicsManageable(physicsManageable)
            }
        }

        assertThat(objectFactory).outputPhysicsManageableSet()
            .containsExactlyElementsIn(physicsManageables)
    }

    @Test
    fun outputsSceneElementSet_returnsIterableSubjectForSceneElements() {
        val sceneElements = List(3) { mock<SceneElement>() }
        val objectFactory = ObjectFactory {
            sceneElements.forEach { sceneElement -> it.addSceneElement(sceneElement) }
        }

        assertThat(objectFactory).outputSceneElementSet().containsExactlyElementsIn(sceneElements)
    }
}
