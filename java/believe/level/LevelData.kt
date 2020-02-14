package believe.level

import believe.command.Command
import believe.command.CommandGenerator
import believe.level.proto.LevelProto.Level
import believe.map.data.MapData
import believe.map.io.MapMetadataParser
import dagger.Reusable
import javax.inject.Inject

data class LevelData(val mapData: MapData, val initialCommand: Command) {
    @Reusable
    class Parser @Inject internal constructor(
        private val commandGenerator: CommandGenerator,
        private val mapMetadataParser: MapMetadataParser
    ) : LevelParser {
        override fun parseLevel(level: Level): LevelData? {
            val initialCommand = if (level.hasInitialCommand()) {
                commandGenerator.generateCommand(level.initialCommand)
            } else Command.EMPTY
            val mapData = mapMetadataParser.parse(level.mapMetadata)
            return when {
                !mapData.isPresent -> null
                initialCommand == null -> null
                else -> LevelData(mapData.get(), initialCommand)
            }
        }
    }
}
