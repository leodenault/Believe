package believe.map.tiled

import org.newdawn.slick.util.Log

class TileSetGroup private constructor(val tileSets: List<TileSet>) {
    fun tileSetForGid(gid: Int): TileSet? = tileSets.binarySearch {
        if (it.contains(gid)) 0 else it.firstGID - gid
    }.let {
        if (it < 0) null.also {
            Log.warn("No tile set containing tile with GID=$gid was found.")
        } else tileSets[it]
    }

    companion object {
        inline operator fun invoke(configure: MutableList<TileSet>.() -> Unit) =
            create(mutableListOf<TileSet>().apply(configure))

        @JvmStatic
        fun create(tileSets: Collection<TileSet>) =
            TileSetGroup(tileSets.sortedWith(Comparator { o1, o2 -> o1.firstGID - o2.firstGID }))
    }
}
