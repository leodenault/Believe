package believe.map.tiled

import org.w3c.dom.Element

typealias ElementParser<T> = (Element) -> T
internal typealias ValueSetter<V, T> = T.(V) -> Unit

internal class ParserAndApplier<V, T>(
    private val parseFrom: ElementParser<V?>, private val setValue: ValueSetter<V, T>
) {
    fun parseFromAndSet(element: Element, receiver: T) {
        val parsedValue: V? = parseFrom(element)
        if (parsedValue != null) {
            receiver.setValue(parsedValue)
        }
    }
}

internal fun <T> (() -> T).byCombining(
    vararg subParsersAndAppliers: ParserAndApplier<*, T>
): ElementParser<T> = { element: Element ->
    this@byCombining().apply {
        subParsersAndAppliers.forEach { it.parseFromAndSet(element, this) }
    }
}

internal fun <T> (() -> T).byAccumulating(tagName: String) = Pair(this, tagName)

internal fun <T, S> Pair<() -> T, String>.withParser(
    elementParser: ElementParser<S>
) = Triple(this.first, this.second, elementParser)

internal fun <T> Pair<() -> MutableList<T>, String>.withParser(
    elementParser: ElementParser<T>
) = Triple(this.first, this.second, elementParser).reduce { add(it) }

internal inline fun <T, S> Triple<() -> T, String, ElementParser<S>>.reduce(
    crossinline reduce: T.(S) -> Unit
): ElementParser<T> = { element: Element ->
    val (generateReceiver, tagName, parseElement) = this
    generateReceiver().apply {
        element.getElementsByTagName(tagName).let { nodeList ->
            (0 until nodeList.length).forEach { index ->
                parseElement(nodeList.item(index) as Element)?.let { reduce(it) }
            }
        }
    }
}

internal infix fun <V, T> ElementParser<V?>.withSetter(valueSetter: ValueSetter<V, T>) =
    ParserAndApplier(this, valueSetter)

internal inline fun <T> attributeParser(
    attributeName: String, crossinline parseFrom: (String) -> T?
): ElementParser<T?> = { element: Element ->
    if (element.hasAttribute(attributeName)) {
        parseFrom(element.getAttribute(attributeName))
    } else null
}

internal fun integerAttributeParser(attributeName: String): ElementParser<Int?> =
    attributeParser(attributeName) { it.toIntOrNull() }

internal fun floatAttributeParser(attributeName: String): ElementParser<Float?> =
    attributeParser(attributeName) { it.toFloatOrNull() }

internal fun stringAttributeParser(attributeName: String): ElementParser<String?> =
    attributeParser(attributeName) { it }
