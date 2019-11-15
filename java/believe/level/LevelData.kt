package believe.level

import believe.command.Command
import believe.command.CommandSequenceParser
import believe.level.proto.LevelProto.Level
import believe.map.data.MapData
import believe.map.io.MapMetadataParser
import javax.inject.Inject

data class LevelData(val mapData: MapData, val initialCommand: Command) {
    class Parser @Inject internal constructor(
        private val commandSequenceParser: CommandSequenceParser,
        private val mapMetadataParser: MapMetadataParser
    ) {
        fun parseLevel(level: Level): LevelData? {
            val initialCommand = commandSequenceParser.parseSequence(level.initialCommands)
            val mapData = mapMetadataParser.parse(level.mapMetadata)
            return if (mapData.isPresent) LevelData(mapData.get(), initialCommand) else null
        }
    }
}
