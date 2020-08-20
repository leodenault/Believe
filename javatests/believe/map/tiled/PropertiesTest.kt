package believe.map.tiled

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.tiled.testing.fakeElement
import believe.map.tiled.testing.fakeProperties
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class PropertiesTest {
    private val properties = Properties.parser()(
        fakeElement(
            children = arrayOf(
                fakeProperties(
                    "prop1" to "value1.1",
                    "prop2" to "value2",
                    "prop1" to "value1.2",
                    "prop3" to "value3.1"
                )
            )
        )
    )

    @Test
    fun getProperty_propertyExists_returnsPropertyValue() {
        with(properties) {
            assertThat(getProperty("prop1")).isEqualTo("value1.2")
            assertThat(getProperty("prop2")).isEqualTo("value2")
        }
    }

    @Test
    fun getProperty_propertyDoesNotExist_returnsNull() {
        assertThat(properties.getProperty("nope")).isNull()
    }

    @Test
    fun overrideWith_mergesAndOverridesValues() {
        properties.overrideWith(
            Properties.parser()(
                fakeElement(
                    children = arrayOf(fakeProperties("prop3" to "value3.2", "prop4" to "value4"))
                )
            )
        )

        with(properties) {
            assertThat(getProperty("prop1")).isEqualTo("value1.2")
            assertThat(getProperty("prop2")).isEqualTo("value2")
            assertThat(getProperty("prop3")).isEqualTo("value3.2")
            assertThat(getProperty("prop4")).isEqualTo("value4")
        }
    }

    @Test
    fun fromJava_createsEquivalentPropertiesInstance() {
        val javaProperties = java.util.Properties().apply {
            put("prop1", "value1")
            put("prop2", "value2")
        }

        with(Properties.fromJava(javaProperties)) {
            assertThat(getProperty("prop1")).isEqualTo("value1")
            assertThat(getProperty("prop2")).isEqualTo("value2")
        }
    }

    @Test
    @VerifiesLoggingCalls
    fun fromJava_nonStringPropertiesExist_ignoresAndLogsWarning(logSystem: VerifiableLogSystem) {
        val javaProperties = java.util.Properties().apply {
            put(123, 456f)
            put("prop2", listOf(5, "whoa", 6))
            put(123.456, "another value")
        }

        val properties = Properties.fromJava(javaProperties)

        assertThat(properties.getProperty("prop2")).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.WARNING
        ).hasPattern(Regex.escape("Could not adopt Java property: ${123} -> ${456f}"))
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.WARNING
        ).hasPattern(
            Regex.escape("Could not adopt Java property: prop2 -> ${listOf(5, "whoa", 6)}")
        )
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasSeverity(
            LogSeverity.WARNING
        ).hasPattern(Regex.escape("Could not adopt Java property: ${123.456} -> another value"))
    }
}
