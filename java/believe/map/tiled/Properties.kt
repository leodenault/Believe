package believe.map.tiled

import believe.core.PropertyProvider
import org.newdawn.slick.util.Log
import org.w3c.dom.Element

class Properties private constructor() : PropertyProvider {
    private val internalProperties = mutableMapOf<String, String>()

    override fun getProperty(key: String): String? = internalProperties[key]

    internal fun overrideWith(other: Properties) {
        internalProperties.putAll(other.internalProperties)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Properties

        if (internalProperties != other.internalProperties) return false

        return true
    }

    override fun hashCode(): Int {
        return internalProperties.hashCode()
    }

    private class MutablePropertyEntry {
        internal var name = ""
        internal var value = ""
    }

    companion object {
        private val PARSER = { Properties() }.byAccumulating("properties").withParser({
            mutableMapOf<String, String>()
        }.byAccumulating("property").withParser({
            MutablePropertyEntry()
        }.byCombining(stringAttributeParser("name") withSetter { name = it },
            stringAttributeParser("value") withSetter { value = it },
            { element: Element -> element.textContent } withSetter { value = it })
        ).reduce { put(it.name, it.value) }).reduce { internalProperties.putAll(it) }


        internal fun parser() = PARSER
        internal fun empty() = Properties()

        fun fromMap(other: Map<String, String>) = Properties().apply {
            other.forEach { internalProperties[it.key] = it.value }
        }
    }
}
