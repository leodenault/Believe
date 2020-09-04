package believe.map.tiled

import believe.datamodel.DataManager
import believe.map.tiled.testing.fakeElement
import believe.map.tiled.testing.fakeProperties
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import java.util.function.Supplier

internal class TiledObjectTest {
    private val templateDataManager = mock<DataManager<PartialTiledObject>>()
    private val parsePartialTiledObject = PartialTiledObject.Parser(TileSetGroup {})
    private val parseTiledObject = TiledObject.Parser(
        parsePartialTiledObject, templateDataManager
    )

    @Test
    fun parse_noTemplateOrGidSpecified_returnsTiledObject() {
        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "type" to "object type",
                    "x" to "123",
                    "y" to "234",
                    "width" to "345",
                    "height" to "456"
                ), children = arrayOf(
                    fakeProperties("prop1" to "value1", "prop2" to "value2")
                )
            )
        )

        with(tiledObject) {
            assertThat(type).isEqualTo("object type")
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
            assertThat(width).isEqualTo(345f)
            assertThat(height).isEqualTo(456f)
            assertThat(getProperty("prop1")).isEqualTo("value1")
            assertThat(getProperty("prop2")).isEqualTo("value2")
        }
    }

    @Test
    fun parse_templateIsSpecified_overridesTemplateProperties() {
        val templateObject = parsePartialTiledObject(
            fakeElement(
                attributes = arrayOf("width" to "345", "height" to "456"),
                children = arrayOf(fakeProperties("prop2" to "value2.1", "prop3" to "value3"))
            )
        )
        whenever(templateDataManager.getDataFor("template/location.tx")) doReturn templateObject

        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "template" to "template/location.tx", "x" to "123", "y" to "234"
                ), children = arrayOf(
                    fakeProperties("prop1" to "value1"), fakeProperties("prop2" to "value2.2")
                )
            )
        )

        with(tiledObject) {
            assertThat(type).isNull()
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
            assertThat(width).isEqualTo(345f)
            assertThat(height).isEqualTo(456f)
            assertThat(getProperty("prop1")).isEqualTo("value1")
            assertThat(getProperty("prop2")).isEqualTo("value2.2")
            assertThat(getProperty("prop3")).isEqualTo("value3")
        }
    }

    @Test
    fun parse_templateIsSpecifiedButDoesNotExist_returnsTiledObject() {
        val tiledObject = parseTiledObject(
            fakeElement(
                attributes = arrayOf(
                    "template" to "template/location.tx", "x" to "123", "y" to "234"
                )
            )
        )

        with(tiledObject) {
            assertThat(x).isEqualTo(123f)
            assertThat(y).isEqualTo(234f)
        }
    }
}
