package believe.util

class MapEntry<K, V> private constructor(val key: K, val value: V) {
    companion object {
        @JvmStatic
        fun <K, V> entry(key: K, value: V): MapEntry<K, V> {
            return MapEntry(key, value)
        }
    }
}