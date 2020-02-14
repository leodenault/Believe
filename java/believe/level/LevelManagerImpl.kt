package believe.level

import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory
import believe.level.proto.LevelProto.Level
import dagger.Reusable
import org.newdawn.slick.util.Log
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/** Default implementation of [LevelManager].  */
@Reusable
class LevelManagerImpl @Inject internal constructor(
    private val binaryProtoFileFactory: BinaryProtoFileFactory,
    private val levelDataParser: LevelParser,
    @LevelDefinitionsDirectory
    private val levelDefinitionsDirectory: String
) : LevelManager {

    private val nameToLevelDataMap: MutableMap<String, LevelData> = HashMap()

    override fun getLevel(name: String): LevelData? {
        val levelData = nameToLevelDataMap[name]
        if (levelData != null) {
            return levelData
        }

        val levelLocation = "$levelDefinitionsDirectory/$name.pb"
        val level = binaryProtoFileFactory.create<Level>(levelLocation).load()

        if (level == null) {
            Log.error("Failed to read level at '$levelLocation'.")
            return null
        }

        val parsedLevelData = levelDataParser.parseLevel(level)
        if (parsedLevelData == null) {
            Log.error("Failed to parse level at '$levelLocation' into LevelData instance.")
            return null
        }

        nameToLevelDataMap[name] = parsedLevelData
        return parsedLevelData
    }
}
