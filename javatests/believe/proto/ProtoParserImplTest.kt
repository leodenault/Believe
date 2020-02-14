package believe.proto

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.proto.testing.proto.FakeProto.FakeMessage
import believe.proto.testing.proto.FakeProto.FakeMessageExtension
import believe.proto.testing.proto.FakeProto.FakeMessageExtension2
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.InvalidProtocolBufferException
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class ProtoParserImplTest {
    private val parser = ProtoParserImpl.Factory(ExtensionRegistry.newInstance().apply {
        add(FakeMessageExtension.fakeMessageExtension)
    }).create<FakeMessage>()

    @Test
    @VerifiesLoggingCalls
    fun parse_internalParserFails_logsErrorAndReturnsNull(logSystem: VerifiableLogSystem) {
        assertThat(
            parser.parse(ByteArrayInputStream("invalid".toByteArray()))
        ).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Failed to parse proto contents.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
            .hasThrowable(InvalidProtocolBufferException::class.java)
    }

    @Test
    @VerifiesLoggingCalls
    fun parse_extensionNotRegistered_logsErrorAndReturnsRestOfProto(logSystem: VerifiableLogSystem) {
        val protoData = FakeMessage.newBuilder().setData("some data").setExtension(
            FakeMessageExtension2.fakeMessageExtension2,
            FakeMessageExtension2.newBuilder().setExtensionData("invalid extension data").build()
        ).build()

        val parsedMessage = parser.parse(protoData.toInputStream())

        assertThat(
            parsedMessage?.unknownFields?.hasField(
                FakeMessageExtension2.fakeMessageExtension2.number
            )
        ).isTrue()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not parse all fields in FakeMessage.")
            .hasSeverity(VerifiableLogSystem.LogSeverity.ERROR)
    }

    @Test
    @VerifiesLoggingCalls
    fun parse_returnsValidProto(logSystem: VerifiableLogSystem) {
        val protoData: FakeMessage = FakeMessage.newBuilder().setData("some data").setExtension(
            FakeMessageExtension.fakeMessageExtension,
            FakeMessageExtension.newBuilder().setExtensionData("extension data").build()
        ).build()

        assertThat(parser.parse(protoData.toInputStream())).isEqualTo(protoData)
        VerifiableLogSystemSubject.assertThat(logSystem)
            .neverLoggedMessageThat { it.hasPattern("Could not parse all fields in FakeMessage.") }
    }

    private fun FakeMessage.toInputStream() = ByteArrayInputStream(toByteArray())
}