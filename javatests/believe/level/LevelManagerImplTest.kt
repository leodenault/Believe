package believe.level

import believe.command.Command
import believe.command.proto.CommandSequenceProto.CommandSequence
import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory
import believe.datamodel.protodata.testing.FakeBinaryProtoFileFactory
import believe.level.proto.LevelProto.Level
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.map.data.MapData
import believe.map.data.TiledMapData
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class LevelManagerImplTest {
    @Test
    @VerifiesLoggingCalls
    internal fun getLevel_metadataCannotBeRead_logsErrorAndReturnsEmpty(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(
            levelManagerImpl(FakeBinaryProtoFileFactory.createForEmptyFile()) { null }.getLevel(
                "level_does_not_exist"
            )
        ).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasSeverity(LogSeverity.ERROR).hasPattern(
                "Failed to read level at '$LEVEL_DIRECTORY/level_does_not_exist.pb'."
            )
    }

    @Test
    @VerifiesLoggingCalls
    internal fun getLevel_metadataCannotBeParsed_logsErrorAndReturnsEmpty(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(
            levelManagerImpl(validBinaryProtoFileFactory()) { null }.getLevel(
                "level_cannot_be_parsed"
            )
        ).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasSeverity(LogSeverity.ERROR).hasPattern(
                "Failed to parse level at '$LEVEL_DIRECTORY/level_cannot_be_parsed.pb' into LevelData instance."
            )
    }

    @Test
    internal fun getLevel_mapExistsAndHasNotYetBeenLoaded_loadsMapAndReturnsMapData() {
        assertThat(
            levelManagerImpl(
                validBinaryProtoFileFactory()
            ) { EXPECTED_LEVEL_DATA }.getLevel("test_level")
        ).isEqualTo(EXPECTED_LEVEL_DATA)
    }

    @Test
    internal fun getLevel_mapExistsAndHasBeenLoaded_doesNotReparseMap() {
        val levelManager: LevelManagerImpl =
            levelManagerImpl(validBinaryProtoFileFactory(), FAKE_LEVEL_PARSER)

        levelManager.getLevel("test_level")
        levelManager.getLevel("test_level")

        assertThat(FAKE_LEVEL_PARSER.calls).isEqualTo(1)
    }

    private fun levelManagerImpl(
        binaryProtoFileFactory: BinaryProtoFileFactory, levelParser: (Level) -> LevelData?
    ): LevelManagerImpl {
        return LevelManagerImpl(
            binaryProtoFileFactory, object : LevelParser {
                override fun parseLevel(level: Level): LevelData? = levelParser(level)
            }, LEVEL_DIRECTORY
        )
    }

    companion object {
        private val LEVEL_DIRECTORY: String = "some_directory"
        private val LEVEL: Level =
            Level.newBuilder().setInitialCommands(CommandSequence.getDefaultInstance()).build()
        private val EXPECTED_LEVEL_DATA: LevelData = LevelData(
            MapData.newBuilder(TiledMapData.newBuilder(0, 0, 0, 0).build()).build(),
            Command { })
        private val FAKE_LEVEL_PARSER = object : (Level) -> LevelData? {
            var calls = 0

            override fun invoke(p1: Level): LevelData? {
                calls++
                return EXPECTED_LEVEL_DATA
            }
        }

        private fun validBinaryProtoFileFactory(): BinaryProtoFileFactory {
            return FakeBinaryProtoFileFactory.createForFileReadingFrom(
                ByteArrayInputStream(LEVEL.toByteArray())
            )
        }
    }
}
