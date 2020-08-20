package believe.map.tiled

import believe.core.PropertyProvider
import org.newdawn.slick.util.Log

class Properties private constructor() : PropertyProvider {
    private val internalProperties = mutableMapOf<String, String>()

    override fun getProperty(key: String): String? = internalProperties[key]

    internal fun overrideWith(other: Properties) {
        internalProperties.putAll(other.internalProperties)
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
            stringAttributeParser("value") withSetter { value = it })
        ).reduce { put(it.name, it.value) }).reduce { internalProperties.putAll(it) }


        internal fun parser() = PARSER
        internal fun empty() = Properties()
        internal fun fromJava(javaProperties: java.util.Properties) = Properties().apply {
            javaProperties.entries.partition { it.key is String && it.value is String }.also {
                it.first.forEach { entry ->
                    internalProperties[entry.key as String] = entry.value as String
                }
                it.second.forEach { entry ->
                    Log.warn("Could not adopt Java property: ${entry.key} -> ${entry.value}")
                }
            }
        }
    }
}
