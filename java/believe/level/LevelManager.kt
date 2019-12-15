package believe.level

/** Manages loading levels from the file system.  */
interface LevelManager {
    /**
     * Fetches a level from the file system.
     *
     * @param name the name of the level to load.
     * @return [LevelData] associated with the name, or null if none such instance exists.
     */
    fun getLevel(name: String): LevelData?
}
