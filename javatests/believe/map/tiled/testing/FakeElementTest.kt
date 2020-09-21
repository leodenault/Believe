package believe.map.tiled.testing

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.w3c.dom.Element

internal class FakeElementTest {
    @Test
    fun hasAttribute_attributeExists_returnsTrue() {
        assertThat(
            fakeElement(attributes = arrayOf("yes" to "affirmative")).hasAttribute("yes")
        ).isTrue()
    }

    @Test
    fun getAttribute_attributeExists_returnsValue() {
        assertThat(
            fakeElement(attributes = arrayOf("yes" to "affirmative")).getAttribute("yes")
        ).isEqualTo("affirmative")
    }

    @Test
    fun hasAttribute_attributeDoesNotExist_returnsFalse() {
        assertThat(fakeElement().hasAttribute("no")).isFalse()
    }

    @Test
    fun getAttribute_attributeDoesNotExist_returnsEmptyString() {
        assertThat(fakeElement().getAttribute("no")).isEmpty()
    }

    @Test
    fun tagName_tagNameSpecified_returnsTagName() {
        assertThat(fakeElement(tagName = "some tag").tagName).isEqualTo("some tag")
    }

    @Test
    fun tagName_tagNameNotSpecified_returnsEmptyString() {
        assertThat(fakeElement().tagName).isEmpty()
    }

    @Test
    fun textContent_exists_returnsTextContent() {
        assertThat(fakeElement(textContent = "some content").textContent).isEqualTo("some content")
    }

    @Test
    fun textContent_doesNotexist_returnsNull() {
        assertThat(fakeElement().textContent).isNull()
    }

    @Test
    fun getElementsByTagName_returnsCorrectlyConfiguredNodeList() {
        val element = fakeElement(
            children = arrayOf(
                fakeElement(tagName = "tag1", attributes = arrayOf("yes" to "affirmative1")),
                fakeElement(tagName = "tag1", attributes = arrayOf("yes" to "affirmative2")),
                fakeElement(tagName = "tag2", attributes = arrayOf("maybe" to "perhaps"))
            )
        )

        val tag1NodeList = element.getElementsByTagName("tag1")
        val tag2NodeList = element.getElementsByTagName("tag2")

        with(tag1NodeList) {
            assertThat(length).isEqualTo(2)
            assertThat((item(0) as Element).tagName).isEqualTo("tag1")
            assertThat((item(0) as Element).getAttribute("yes")).isEqualTo("affirmative1")
            assertThat((item(1) as Element).tagName).isEqualTo("tag1")
            assertThat((item(1) as Element).getAttribute("yes")).isEqualTo("affirmative2")
        }
        with(tag2NodeList) {
            assertThat(length).isEqualTo(1)
            assertThat((item(0) as Element).tagName).isEqualTo("tag2")
            assertThat((item(0) as Element).getAttribute("maybe")).isEqualTo("perhaps")
        }
    }

    @Test
    fun getElementsByTagName_noSuchTagExists_returnsEmptyNodeList() {
        val nodeList = fakeElement().getElementsByTagName("nope")

        assertThat(nodeList.length).isEqualTo(0)
        assertThrows<IndexOutOfBoundsException> { nodeList.item(0) }
    }

    @Test
    fun fakeProperties_returnsElementsStructuredAsProperties() {
        val properties = fakeProperties("first_prop" to "first_val", "second_prop" to "second_val")

        assertThat(properties.tagName).isEqualTo("properties")
        val children = properties.getChildrenWithTagName("property")
        assertThat(children).hasSize(2)

        with(children[0]) {
            assertThat(tagName).isEqualTo("property")
            assertThat(getAttribute("name")).isEqualTo("first_prop")
            assertThat(getAttribute("value")).isEqualTo("first_val")
        }
        with(children[1]) {
            assertThat(tagName).isEqualTo("property")
            assertThat(getAttribute("name")).isEqualTo("second_prop")
            assertThat(getAttribute("value")).isEqualTo("second_val")
        }
    }

    @Test
    fun fakeGridTileSet_returnsCorrectElement() {
        val tileSet = fakeGridTileSet(
            name = "some tile set",
            tileWidth = 32,
            tileHeight = 64,
            columns = 2,
            children = arrayOf(
                fakeElement(tagName = "child"), fakeElement(tagName = "child")
            )
        )

        with(tileSet) {
            assertThat(tagName).isEqualTo("tileset")
            assertThat(getAttribute("name")).isEqualTo("some tile set")
            assertThat(getAttribute("tilewidth")).isEqualTo("32")
            assertThat(getAttribute("tileheight")).isEqualTo("64")
            assertThat(getAttribute("columns")).isEqualTo("2")
            assertThat(getChildrenWithTagName("child")).hasSize(2)
        }
    }

    @Test
    fun fakeMultiImageTileSet_returnsCorrectElement() {
        val tileSet = fakeMultiImageTileSet(
            name = "some tile set",
            children = arrayOf(fakeElement(tagName = "child"), fakeElement(tagName = "child"))
        )

        with(tileSet) {
            assertThat(tagName).isEqualTo("tileset")
            assertThat(getAttribute("name")).isEqualTo("some tile set")
            assertThat(getChildrenWithTagName("child")).hasSize(2)
        }
    }

    @Test
    fun fakeGridTile_returnsCorrectElement() {
        val tile = fakeGridTile(id = 1, type = "some type")

        with(tile) {
            assertThat(tagName).isEqualTo("tile")
            assertThat(getAttribute("id")).isEqualTo("1")
            assertThat(getAttribute("type")).isEqualTo("some type")
        }
    }

    @Test
    fun fakeSingleImageTile_returnsCorrectElement() {
        val tile = fakeSingleImageTile(id = 3, type = "some type")

        with(tile) {
            assertThat(tagName).isEqualTo("tile")
            assertThat(getAttribute("id")).isEqualTo("3")
            assertThat(getAttribute("type")).isEqualTo("some type")
            with(getChildrenWithTagName("image").first()) {
                assertThat(getAttribute("source")).isEqualTo("")
            }
        }
    }
}

private fun Element.getChildrenWithTagName(tagName: String): List<Element> =
    getElementsByTagName(tagName).let { nodeList ->
        return (0 until nodeList.length).map { nodeList.item(it) as Element }
    }
