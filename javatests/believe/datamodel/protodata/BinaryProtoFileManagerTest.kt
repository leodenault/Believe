package believe.datamodel.protodata

import believe.datamodel.protodata.testing.FakeBinaryProtoFileFactory
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.proto.testing.proto.FakeProto.FakeMessage
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class BinaryProtoFileManagerTest {
    private var binaryProtoFileFactory = FakeBinaryProtoFileFactory.createForFileReadingFrom(
        ByteArrayInputStream(PROTO_DATA.toByteArray())
    )
    private var parse = { it: FakeMessage -> it.data }
    private val manager: BinaryProtoFileManager<FakeMessage, String> by lazy {
        BinaryProtoFileManager.Factory(binaryProtoFileFactory).create(FILE_DIRECTORY, parse)
    }

    @Test
    @VerifiesLoggingCalls
    internal fun getDataFor_metadataCannotBeRead_logsErrorAndReturnsEmpty(
        logSystem: VerifiableLogSystem
    ) {
        binaryProtoFileFactory = FakeBinaryProtoFileFactory.createForEmptyFile()

        assertThat(manager.getDataFor("file_does_not_exist")).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasSeverity(LogSeverity.ERROR).hasPattern(
                "Failed to read file at '$FILE_DIRECTORY/file_does_not_exist.pb'."
            )
    }

    @Test
    @VerifiesLoggingCalls
    internal fun getDataFor_metadataCannotBeParsed_logsErrorAndReturnsEmpty(
        logSystem: VerifiableLogSystem
    ) {
        parse = { null }

        assertThat(manager.getDataFor("file_cannot_be_parsed")).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasSeverity(LogSeverity.ERROR).hasPattern(
                "Failed to parse file at '$FILE_DIRECTORY/file_cannot_be_parsed.pb'."
            )
    }

    @Test
    internal fun getDataFor_dataExistsAndHasNotYetBeenLoaded_loadsDataAndReturnsIt() {
        assertThat(manager.getDataFor("test_file")).isEqualTo(EXPECTED_FILE_DATA)
    }

    @Test
    internal fun getDataFor_dataExistsAndHasBeenLoaded_doesNotReparseData() {
        parse = mock {
            on { invoke(PROTO_DATA) } doReturn EXPECTED_FILE_DATA
        }

        manager.getDataFor("test_file")
        manager.getDataFor("test_file")

        verify(parse, times(1)).invoke(PROTO_DATA)
    }

    companion object {
        private const val FILE_DIRECTORY: String = "some_directory"
        private const val EXPECTED_FILE_DATA = "fake data"
        private val PROTO_DATA: FakeMessage =
            FakeMessage.newBuilder().setData(EXPECTED_FILE_DATA).build()
    }
}
