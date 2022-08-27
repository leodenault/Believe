package believe.level

import believe.audio.Music
import believe.command.Command
import believe.command.CommandGenerator
import believe.command.proto.CommandProto
import believe.datamodel.loadableDataOf
import believe.level.LevelData.Parser
import believe.level.proto.LevelProto.Level
import believe.map.data.MapData
import believe.map.data.TiledMapData
import believe.map.data.proto.MapMetadataProto
import believe.map.io.MapMetadataParser
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import java.util.Optional

internal class LevelDataTest {
    private var commandGenerator: CommandGenerator = mock {
        on { generateCommand(any()) } doReturn COMMAND
    }
    private var mapMetadataParser: MapMetadataParser = MapMetadataParser { Optional.of(MAP_DATA) }
    private val backgroundMusic = loadableDataOf(mock<Music>())
    private val parser: Parser by lazy {
        Parser(
            commandGenerator,
            mapMetadataParser,
            { backgroundMusic }
        )
    }

    @Test
    internal fun parse_returnsValidLevelData() {
        assertThat(parser.parseLevel(LEVEL)).isEqualTo(
            LevelData(
                MAP_DATA,
                COMMAND,
                backgroundMusic
            )
        )
    }

    @Test
    internal fun parse_commandDoesNotExist_doesNotInvokeCommandGeneratorAndReturnsEmptyCommand() {
        assertThat(parser.parseLevel(LEVEL_WITHOUT_COMMAND)).isEqualTo(
            LevelData(MAP_DATA, Command.EMPTY, backgroundMusic)
        )
        verify(commandGenerator, never()).generateCommand(any())
    }

    @Test
    internal fun parse_commandCannotBeParsed_returnsNull() {
        whenever(commandGenerator.generateCommand(any())).thenReturn(null)

        assertThat(parser.parseLevel(LEVEL)).isNull()
    }

    @Test
    internal fun parse_mapCannotBeParsed_returnsNull() {
        mapMetadataParser = MapMetadataParser { Optional.empty() }

        assertThat(parser.parseLevel(LEVEL)).isNull()
    }

    companion object {
        val COMMAND: Command = object : Command {
            override fun execute() {}
        }
        private const val BACKGROUND_MUSIC_LOCATION = "some music location"
        val LEVEL_WITHOUT_COMMAND: Level =
            Level.newBuilder().setMapMetadata(MapMetadataProto.MapMetadata.getDefaultInstance())
                .setBackgroundMusicLocation(BACKGROUND_MUSIC_LOCATION).build()
        val LEVEL: Level =
            Level.newBuilder().setMapMetadata(MapMetadataProto.MapMetadata.getDefaultInstance())
                .setInitialCommand(CommandProto.Command.getDefaultInstance())
                .setBackgroundMusicLocation(BACKGROUND_MUSIC_LOCATION).build()
        val MAP_DATA: MapData = MapData.newBuilder(
            TiledMapData.newBuilder(
                1, 2, 3, 4
            ).build()
        ).build()
    }
}
