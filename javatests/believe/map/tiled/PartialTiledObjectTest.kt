package believe.map.tiled

import believe.map.tiled.testing.fakeElement
import believe.map.tiled.testing.fakeProperties
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import java.util.Properties
import java.util.function.Supplier

internal class PartialTiledObjectTest {
    private val tileSetGroup = TileSetGroup {
        add(mock {
            on { contains(VALID_GID) } doReturn true
            on { getPropertiesForTile(VALID_GID) } doReturn TILE_PROPERTIES
        })
    }
    private val parseTiledObject = PartialTiledObject.Parser(tileSetGroup)

    @Test
    fun parse_correctlyParsesAttributes() {
        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "name" to "a name",
                    "type" to "object type",
                    "x" to "123",
                    "y" to "234",
                    "width" to "345",
                    "height" to "456"
                ), children = arrayOf(
                    fakeProperties("prop1" to "val1"), fakeProperties("prop2" to "val2")
                )
            )
        )

        with(tiledObject) {
            assertThat(name).isEqualTo("a name")
            assertThat(type).isEqualTo("object type")
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
            assertThat(width).isEqualTo(345f)
            assertThat(height).isEqualTo(456f)
            assertThat(properties.getProperty("prop1")).isEqualTo("val1")
            assertThat(properties.getProperty("prop2")).isEqualTo("val2")
        }
    }

    @Test
    fun parse_gidIsSpecified_overridesTileProperties() {
        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf("gid" to VALID_GID.toString()),
                children = arrayOf(fakeProperties("prop2" to "value2.2", "prop3" to "value3"))
            )
        )

        with(tiledObject) {
            assertThat(properties.getProperty("prop1")).isEqualTo("value1")
            assertThat(properties.getProperty("prop2")).isEqualTo("value2.2")
            assertThat(properties.getProperty("prop3")).isEqualTo("value3")
        }
    }

    @Test
    fun parse_gidIsSpecifiedButDoesNotExist_returnsTiledObject() {
        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf("gid" to INVALID_GID.toString()),
                children = arrayOf(fakeProperties("prop2" to "value2.2", "prop3" to "value3"))
            )
        )

        with(tiledObject) {
            assertThat(properties.getProperty("prop2")).isEqualTo("value2.2")
            assertThat(properties.getProperty("prop3")).isEqualTo("value3")
        }
    }

    @Test
    fun overrideWith_overridesFields() {
        val partialObject1 = parseTiledObject(fakeElement())
        val partialObject2 = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "name" to "a name",
                    "type" to "object type",
                    "x" to "123",
                    "y" to "234",
                    "width" to "345",
                    "height" to "456"
                ), children = arrayOf(
                    fakeProperties("prop1" to "val1"), fakeProperties("prop2" to "val2")
                )
            )
        )

        partialObject1.overrideWith(partialObject2)

        with(partialObject1) {
            assertThat(name).isEqualTo("a name")
            assertThat(type).isEqualTo("object type")
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
            assertThat(width).isEqualTo(345f)
            assertThat(height).isEqualTo(456f)
            assertThat(properties.getProperty("prop1")).isEqualTo("val1")
            assertThat(properties.getProperty("prop2")).isEqualTo("val2")
        }
    }

    @Test
    fun overrideWith_otherFieldsAreNull_reusesExistingFields() {
        val partialObject1 = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "name" to "a name",
                    "type" to "object type",
                    "x" to "123",
                    "y" to "234",
                    "width" to "345",
                    "height" to "456"
                ), children = arrayOf(
                    fakeProperties("prop1" to "val1"), fakeProperties("prop2" to "val2")
                )
            )
        )
        val partialObject2 = parseTiledObject(fakeElement())

        partialObject1.overrideWith(partialObject2)

        with(partialObject1) {
            assertThat(name).isEqualTo("a name")
            assertThat(type).isEqualTo("object type")
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
            assertThat(width).isEqualTo(345f)
            assertThat(height).isEqualTo(456f)
            assertThat(properties.getProperty("prop1")).isEqualTo("val1")
            assertThat(properties.getProperty("prop2")).isEqualTo("val2")
        }
    }

    @Test
    fun toTiledObject_returnsCorrectTiledObject() {
        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "name" to "a name",
                    "type" to "object type",
                    "x" to "123",
                    "y" to "234",
                    "width" to "345",
                    "height" to "456"
                ), children = arrayOf(
                    fakeProperties("prop1" to "val1"), fakeProperties("prop2" to "val2")
                )
            )
        ).toTiledObject()

        with(tiledObject) {
            assertThat(name).isEqualTo("a name")
            assertThat(type).isEqualTo("object type")
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
            assertThat(width).isEqualTo(345f)
            assertThat(height).isEqualTo(456f)
            assertThat(getProperty("prop1")).isEqualTo("val1")
            assertThat(getProperty("prop2")).isEqualTo("val2")
        }
    }

    @Test
    fun toTiledObject_fieldsAreNull_returnsTiledObjectWithDefaults() {
        val tiledObject = parseTiledObject(fakeElement()).toTiledObject()

        with(tiledObject) {
            assertThat(name).isNull()
            assertThat(type).isNull()
            assertThat(x).isEqualTo(0f)
            assertThat(y).isEqualTo(0f)
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(0f)
        }
    }

    companion object {
        private const val VALID_GID = 456
        private const val INVALID_GID = 654
        private val TILE_PROPERTIES = Properties().apply {
            put("prop1", "value1")
            put("prop2", "value2.1")
        }
    }
}
