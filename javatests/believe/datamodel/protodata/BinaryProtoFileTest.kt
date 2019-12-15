package believe.datamodel.protodata

import believe.io.ResourceManager
import believe.io.testing.FakeResourceManagerFactory
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.testing.proto.TestProto.TestMessage
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.InvalidProtocolBufferException
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class BinaryProtoFileTest {
    @Test
    fun load_returnsContentsFromFile() {
        val binaryProtoFile = binaryProtoFile(
            FakeResourceManagerFactory.managerReadingFrom(ByteArrayInputStream(PROTO.toByteArray()))
        )

        assertThat(binaryProtoFile.load()).isEqualTo(PROTO)
    }

    @Test
    fun load_cannotGetInputStream_returnsNull() {
        assertThat(binaryProtoFile(FakeResourceManagerFactory.createNoOp()).load()).isNull()
    }

    @Test
    @VerifiesLoggingCalls
    fun load_errorReadingFromFile_logsErrorAndReturnsNull(
        logSystem: VerifiableLogSystem
    ) {
        val binaryProtoFile = binaryProtoFile(
            FakeResourceManagerFactory.managerReadingFrom(
                ByteArrayInputStream(
                    byteArrayOf(
                        1, 2, 3
                    )
                )
            )
        )

        assertThat(binaryProtoFile.load()).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not parse contents of binary proto file '$FILE_NAME'.")
            .hasSeverity(LogSeverity.ERROR).hasThrowable(InvalidProtocolBufferException::class.java)
    }

    @Test
    fun write_writesContentsToDisk() {
        val outputStream = ByteArrayOutputStream()
        val binaryProtoFile =
            binaryProtoFile(FakeResourceManagerFactory.managerWritingTo(outputStream))

        binaryProtoFile.commit(PROTO)

        assertThat(outputStream.toByteArray()).isEqualTo(PROTO.toByteArray())
    }

    @Test
    @VerifiesLoggingCalls
    fun write_cannotGetOutputStream_logsError(logSystem: VerifiableLogSystem) {
        val binaryProtoFile = binaryProtoFile(FakeResourceManagerFactory.createNoOp())

        binaryProtoFile.commit(PROTO)

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not write proto data to file at '$FILE_NAME'.*")
            .hasSeverity(LogSeverity.ERROR)
    }

    companion object {
        private val FILE_NAME = "proto.pb"
        private val PROTO = TestMessage.newBuilder().addContent("proto content").build()

        private fun binaryProtoFile(
            resourceManager: ResourceManager
        ): BinaryProtoFile<TestMessage> {
            return BinaryProtoFile(resourceManager, FILE_NAME, TestMessage.parser())
        }
    }
}
