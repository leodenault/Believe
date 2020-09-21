package believe.map.tiled.testing

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.w3c.dom.Element
import org.w3c.dom.NodeList

/**
 * Returns a fake [Element] based on the provided parameters.
 *
 * @param tagName the name of the XML tag represented by the element.
 * @param attributes the group of key-value pairs defining the attributes set on the element.
 * @param children the child elements contained within the element returned by this.
 */
fun fakeElement(
    tagName: String = "",
    attributes: Array<Pair<String, String>> = arrayOf(),
    textContent: String? = null,
    children: Array<Element> = arrayOf()
) = mock<Element> {
    val attributeMap = attributes.associateBy({ it.first }, { it.second })

    on { this.tagName } doReturn tagName
    on { hasAttribute(any()) } doAnswer { invocation ->
        attributeMap.containsKey(invocation.arguments[0])
    }
    on { getAttribute(any()) } doAnswer { invocation ->
        attributeMap.getOrDefault(invocation.arguments[0], "")
    }

    on { this.textContent } doReturn textContent

    on { getElementsByTagName(any()) } doAnswer { invocation ->
        val childTagName = invocation.arguments[0]
        val childElementListMap = children.groupBy { it.tagName }

        object : NodeList {
            override fun item(index: Int) =
                childElementListMap[childTagName]?.get(index) ?: throw IndexOutOfBoundsException()

            override fun getLength() = childElementListMap[childTagName]?.size ?: 0
        }
    }
}

/**
 * Returns a fake [Element] representing a Tiled map `properties` XML node.
 *
 * @param properties the key-value pairs used to describe properties.
 */
fun fakeProperties(vararg properties: Pair<String, String>) = fakeElement(
    tagName = "properties", children = properties.map {
        fakeElement(
            tagName = "property", attributes = arrayOf("name" to it.first, "value" to it.second)
        )
    }.toTypedArray()
)


fun fakeGridTileSet(
    name: String? = null,
    firstGid: Int? = null,
    tileWidth: Int? = null,
    tileHeight: Int? = null,
    columns: Int? = null,
    tileCount: Int? = null,
    children: Array<Element> = arrayOf()
) = fakeElement(
    tagName = "tileset",
    attributes = arrayOf(
        "name" to name,
        "tilewidth" to tileWidth,
        "tileheight" to tileHeight,
        "columns" to columns,
        "tilecount" to tileCount,
        "firstgid" to firstGid
    ).filter { it.second != null }.map { Pair(it.first, "${it.second}") }.toTypedArray(),
    children = arrayOf(
        fakeElement(
            tagName = "image", attributes = arrayOf("source" to "")
        )
    ) + children
)

fun fakeMultiImageTileSet(
    name: String? = null, firstGid: Int? = null, children: Array<Element> = arrayOf()
) = fakeElement(tagName = "tileset", attributes = arrayOf(
    "name" to name, "firstgid" to firstGid, "columns" to 0
).filter { it.second != null }.map {
    Pair(it.first, "${it.second}")
}.toTypedArray(), children = children
)

fun fakeGridTile(id: Int, type: String) = fakeElement(
    tagName = "tile", attributes = arrayOf("id" to "$id", "type" to type)
)

fun fakeSingleImageTile(id: Int, type: String) = fakeElement(
    tagName = "tile", attributes = arrayOf("id" to "$id", "type" to type), children = arrayOf(
        fakeElement(tagName = "image", attributes = arrayOf("source" to ""))
    )
)

