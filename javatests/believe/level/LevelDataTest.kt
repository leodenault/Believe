package believe.level

import believe.command.Command
import believe.command.CommandSequenceParser
import believe.command.proto.CommandSequenceProto
import believe.level.LevelData.Parser
import believe.level.proto.LevelProto.Level
import believe.map.data.MapData
import believe.map.data.TiledMapData
import believe.map.data.proto.MapMetadataProto
import believe.map.io.MapMetadataParser
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class LevelDataTest {
    @Test
    internal fun parse_returnsValidLevelData() {
        val parser =
            Parser(FakeCommandSequenceParser(), MapMetadataParser { Optional.of(MAP_DATA) })

        assertThat(parser.parseLevel(LEVEL)).isEqualTo(LevelData(MAP_DATA, COMMAND))
    }

    @Test
    internal fun parse_mapCannotBeParsed_returnsEmpty() {
        val parser = Parser(FakeCommandSequenceParser(), MapMetadataParser { Optional.empty() })

        assertThat(parser.parseLevel(LEVEL)).isNull()
    }

    class FakeCommandSequenceParser : CommandSequenceParser {
        override fun parseSequence(sequence: String): Optional<Command> = Optional.empty()

        override fun parseSequence(commandSequence: CommandSequenceProto.CommandSequence): Command {
            return COMMAND
        }
    }

    companion object {
        val COMMAND: Command = Command { }
        val LEVEL: Level =
            Level.newBuilder().setMapMetadata(MapMetadataProto.MapMetadata.getDefaultInstance())
                .build()
        val MAP_DATA: MapData = MapData.newBuilder(
            TiledMapData.newBuilder(
                1, 2, 3, 4
            ).build()
        ).build()
    }
}
