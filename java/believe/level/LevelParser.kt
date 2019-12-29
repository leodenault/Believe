package believe.level

import believe.level.proto.LevelProto.Level

/** Parses a [Level] proto into a [LevelData] instance. */
interface LevelParser {
    /** Return a [LevelData] instance based on the contents of [level], or null on failure. */
    fun parseLevel(level: Level): LevelData?
}