package believe.datamodel.protodata

import believe.io.ResourceManager
import believe.io.testing.FakeResourceManagerFactory
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.proto.ProtoParser
import believe.proto.ProtoParserImpl
import believe.testing.proto.TestProto.TestMessage
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.google.protobuf.InvalidProtocolBufferException
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class BinaryProtoFileTest {
    val resourceManager: ResourceManager = mock {
        on { getResourceAsStream(any()) } doReturn ByteArrayInputStream(PROTO.toByteArray())
    }
    val protoParser: ProtoParser<TestMessage> = mock {
        on { parse(any()) } doReturn PROTO
    }
    val binaryProtoFile = BinaryProtoFile(resourceManager, FILE_NAME, protoParser)

    @Test
    fun load_returnsContentsFromFile() {
        assertThat(binaryProtoFile.load()).isEqualTo(PROTO)
    }

    @Test
    fun load_cannotGetInputStream_returnsNull() {
        whenever(resourceManager.getResourceAsStream(any())) doReturn null

        assertThat(binaryProtoFile(FakeResourceManagerFactory.createNoOp()).load()).isNull()
    }

    @Test
    @VerifiesLoggingCalls
    fun load_errorReadingFromFile_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        whenever(protoParser.parse(any())) doReturn null

        assertThat(binaryProtoFile.load()).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not parse contents of binary proto file '$FILE_NAME'.")
            .hasSeverity(LogSeverity.ERROR)
    }

    @Test
    fun write_writesContentsToDisk() {
        val outputStream = ByteArrayOutputStream()
        whenever(resourceManager.getOutputStreamToFileResource(any())) doReturn outputStream

        binaryProtoFile.commit(PROTO)

        assertThat(outputStream.toByteArray()).isEqualTo(PROTO.toByteArray())
    }

    @Test
    @VerifiesLoggingCalls
    fun write_cannotGetOutputStream_logsError(logSystem: VerifiableLogSystem) {
        whenever(resourceManager.getOutputStreamToFileResource(any())) doReturn null

        binaryProtoFile.commit(PROTO)

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not write proto data to file at '$FILE_NAME'.*")
            .hasSeverity(LogSeverity.ERROR)
    }

    companion object {
        private val FILE_NAME = "proto.pb"
        private val PROTO = TestMessage.newBuilder().addContent("proto content").build()
        private val PROTO_PARSER_FACTORY = ProtoParserImpl.Factory()

        private fun binaryProtoFile(
            resourceManager: ResourceManager
        ): BinaryProtoFile<TestMessage> {
            return BinaryProtoFile(resourceManager, FILE_NAME, PROTO_PARSER_FACTORY.create())
        }
    }
}
