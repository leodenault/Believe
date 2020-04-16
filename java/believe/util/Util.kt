package believe.util

import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Rectangle
import java.io.File
import java.util.*

object Util {
    private const val DEFAULT_DIRECTORY = "customFlowFiles"
    private const val FILE_EXTENSION = ".lfl"

    @JvmStatic
    @get:Throws(SecurityException::class)
    val flowFiles: Array<File>
        get() {
            val parent = File(DEFAULT_DIRECTORY)
            return parent.listFiles { pathname ->
                pathname.name.toLowerCase().endsWith(FILE_EXTENSION)
            } ?: arrayOf()
        }

    /**
     * Clips to the child clip in the context of the graphics object's current clip. The result is the
     * intersection of the two clips.
     *
     * @param childClip The new clipping area to clip to.
     * @return The clipping context used before executing this operation.
     */
    @JvmStatic
    fun Graphics.changeClipContext(childClip: Rectangle?): Rectangle? {
        val oldClip: Rectangle? = clip
        clip = childClip
        // Create a new Rectangle reference since Graphics will clear it upon calling setClip.
        return oldClip?.let { Rectangle(it.x, it.y, it.width, it.height) }
    }

    /** Sets the clip on [g] to [oldClip]. */
    @Deprecated(
        "This method doesn't add any value.",
        ReplaceWith("changeClipContext(g, oldClip)"),
        DeprecationLevel.WARNING
    )
    @JvmStatic
    fun resetClipContext(g: Graphics, oldClip: Rectangle) {
        g.clip = oldClip
    }

    @SafeVarargs
    @JvmStatic
    fun <T> hashSetOf(vararg elements: T): Set<T> = elements.toHashSet()

    @JvmStatic
    fun <T> immutableSetOf(vararg elements: T): Set<T> =
        Collections.unmodifiableSet(elements.toSet())

    @SafeVarargs
    @JvmStatic
    fun <K, V> hashMapOf(vararg entries: MapEntry<out K, out V>): HashMap<K, V> =
        hashMapOf(*entries.map { Pair(it.key, it.value) }.toTypedArray())

    @SafeVarargs
    @JvmStatic
    fun <K, V> immutableMapOf(vararg entries: MapEntry<out K, out V>): Map<K, V> {
        return Collections.unmodifiableMap(hashMapOf(*entries))
    }
}
