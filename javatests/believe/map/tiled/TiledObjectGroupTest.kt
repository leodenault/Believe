package believe.map.tiled

import believe.map.tiled.testing.fakeElement
import believe.map.tiled.testing.fakeProperties
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test

internal class TiledObjectGroupTest {
    private val tiledObject = mock<TiledObject>()
    private val tiledObjectParser = mock<ElementParser<TiledObject>> {
        on { invoke(any()) } doReturn tiledObject
    }
    private val parseGroup = TiledObjectGroup.Parser(tiledObjectParser)

    @Test
    fun parse_nothingToParse_returnsDefaultValues() {
        val receiver = parseGroup(fakeElement())
        with(receiver) {
            assertThat(name).isEmpty()
            assertThat(width).isEqualTo(0f)
            assertThat(height).isEqualTo(0f)
            assertThat(objects).isEmpty()
        }
    }

    @Test
    fun parse_returnsValidTiledObjectGroup() {
        val parsedGroup = parseGroup(
            fakeElement(
                tagName = "objectgroup", attributes = arrayOf(
                    "name" to "a great name", "width" to "123", "height" to "234"
                ), children = arrayOf(
                    fakeElement(tagName = "object"),
                    fakeElement(tagName = "object"),
                    fakeElement(tagName = "object"),
                    fakeProperties("prop1" to "value1", "prop2" to "value2")
                )
            )
        )

        with(parsedGroup) {
            assertThat(name).isEqualTo("a great name")
            assertThat(width).isEqualTo(123f)
            assertThat(height).isEqualTo(234f)
            assertThat(objects).containsExactly(tiledObject, tiledObject, tiledObject)
            assertThat(getProperty("prop1")).isEqualTo("value1")
            assertThat(getProperty("prop2")).isEqualTo("value2")
        }
    }
}
