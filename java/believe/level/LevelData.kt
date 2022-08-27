package believe.level

import believe.audio.Music
import believe.audio.musicFrom
import believe.command.Command
import believe.command.CommandGenerator
import believe.datamodel.LoadableData
import believe.level.proto.LevelProto.Level
import believe.map.data.MapData
import believe.map.io.MapMetadataParser
import dagger.Reusable
import javax.inject.Inject

data class LevelData(
    val mapData: MapData,
    val initialCommand: Command,
    val backgroundMusic: LoadableData<Music>
) {
    @Reusable
    class Parser internal constructor(
        private val commandGenerator: CommandGenerator,
        private val mapMetadataParser: MapMetadataParser,
        private val provideLoadableMusic: (String) -> LoadableData<Music>
    ) : LevelParser {

        @Inject
        internal constructor(
            commandGenerator: CommandGenerator,
            mapMetadataParser: MapMetadataParser
        ) : this(commandGenerator, mapMetadataParser, { musicFrom(it) })

        override fun parseLevel(level: Level): LevelData? {
            val initialCommand = if (level.hasInitialCommand()) {
                commandGenerator.generateCommand(level.initialCommand)
            } else Command.EMPTY
            val mapData = mapMetadataParser.parse(level.mapMetadata)
            return when {
                !mapData.isPresent -> null
                initialCommand == null -> null
                else -> LevelData(
                    mapData.get(),
                    initialCommand,
                    provideLoadableMusic(level.backgroundMusicLocation)
                )
            }
        }
    }
}
