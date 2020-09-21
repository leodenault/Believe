package believe.map.tiled.testing

import believe.map.tiled.Properties
import believe.map.tiled.testing.PropertiesSubject.Companion.properties
import believe.map.tiled.testing.Truth.assertThat
import com.google.common.truth.ExpectFailure
import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback
import com.google.common.truth.SimpleSubjectBuilder
import org.junit.jupiter.api.Test

internal class PropertiesTruthTest {
    @Test
    fun containsAll_allPropertiesExist_returnsTrue() {
        assertThat(
            Properties.fromMap(mapOf("prop1" to "val1", "prop2" to "val2"))
        ).containsAll("prop1" to "val1", "prop2" to "val2")
    }

    @Test
    fun containsAll_notAllPropertiesExist_returnsTrue() {
        expectFailure { whenTesting ->
            whenTesting.that(
                Properties.fromMap(mapOf("prop2" to "val2"))
            ).containsAll("prop1" to "val1", "prop2" to "val2")
        }
    }

    private fun expectFailure(
        callback: (SimpleSubjectBuilder<PropertiesSubject, Properties>) -> Unit
    ) = ExpectFailure.expectFailureAbout(properties(),
        SimpleSubjectBuilderCallback<PropertiesSubject, Properties> { callback(it) })
}
