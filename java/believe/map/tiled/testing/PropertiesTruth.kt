package believe.map.tiled.testing

import believe.map.tiled.Properties
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject
import com.google.common.truth.Truth

/** Façade for writing assertions about [Properties]. */
object Truth {
    /** Begins an assertion about [properties]. */
    @JvmStatic
    fun assertThat(properties: Properties): PropertiesSubject =
        Truth.assertAbout(PropertiesSubject.properties()).that(properties)
}

/** A [Subject] for writing assertions about [Properties]. */
class PropertiesSubject private constructor(
    failureMetadata: FailureMetadata, private val actual: Properties
) : Subject(failureMetadata, actual) {

    /**
     * Asserts that the actual [Properties] instance contains all key-value pairs in [properties].
     */
    fun containsAll(vararg properties: Pair<String, String>) = containsAllIn(properties)

    /**
     * Asserts that the actual [Properties] instance contains all key-value pairs in [properties].
     */
    fun containsAllIn(properties: Array<out Pair<String, String>>) {
        val actualProperties = properties.map {
            Pair(it.first, actual.getProperty(it.first))
        }.filter { it.second != null }.toMap()
        check("entries").that(actualProperties).containsExactlyEntriesIn(properties.toMap())
    }

    companion object {
        /** Returns a [Subject.Factory] for generating instances of [PropertiesSubject]. */
        fun properties(): Factory<PropertiesSubject, Properties> =
            Factory { failureMetadata, actual -> PropertiesSubject(failureMetadata, actual) }
    }
}