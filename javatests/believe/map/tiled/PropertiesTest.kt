package believe.map.tiled

import believe.map.tiled.testing.Truth.assertThat
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
        assertThat(properties).containsAll(
            "prop1" to "value1.2", "prop2" to "value2", "prop3" to "value3.1"
        )
    }

    @Test
    fun getProperty_propertyExistsAsTextContent_returnsPropertyValue() {
        val properties = Properties.parser()(
            fakeElement(
                children = arrayOf(
                    fakeElement(
                        tagName = "properties", children = arrayOf(
                            fakeElement(
                                tagName = "property",
                                attributes = arrayOf("name" to "prop1"),
                                textContent = "value1"
                            )
                        )
                    )
                )
            )
        )

        assertThat(properties).containsAll("prop1" to "value1")
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

        assertThat(properties).containsAll(
            "prop1" to "value1.2", "prop2" to "value2", "prop3" to "value3.2", "prop4" to "value4"
        )
    }

    @Test
    fun copyFrom_createsEquivalentPropertiesInstance() {
        val otherProperties = mapOf(
            "prop1" to "value1", "prop2" to "value2"
        )

        val mapProperties = Properties.fromMap(otherProperties)

        assertThat(mapProperties).containsAll("prop1" to "value1", "prop2" to "value2")
    }
}
