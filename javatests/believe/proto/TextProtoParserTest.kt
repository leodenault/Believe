package believe.proto

import believe.proto.testing.proto.FakeProto.FakeMessage
import believe.proto.testing.proto.FakeProto.FakeMessageExtension
import believe.proto.testing.proto.FakeProto.FakeMessageExtension2
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.TextFormat
import com.google.protobuf.TextFormat.ParseException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.InputStream

internal class TextProtoParserTest {
    val parser = TextProtoParser(
        ExtensionRegistry.newInstance().apply { add(FakeMessageExtension.fakeMessageExtension) },
        FakeMessage::class.java)

    @Test
    internal fun parse_fileCannotBeReadAsProto_throwsParseException() {
        assertThrows<ParseException> { parser.parse("invalid contents".toInputStream()) }
    }

    @Test
    internal fun parse_returnsCorrectProtoMessage() {
        assertThat(parser.parse(FAKE_PROTO.asTextProto().toInputStream())).isEqualTo(FAKE_PROTO)
    }

    @Test
    internal fun parse_protoContentsContainUnregisteredExtension_throwsParseException() {
        assertThrows<ParseException> {
            parser.parse(FAKE_PROTO_WITH_UNREGISTERED_EXTENSION.asTextProto().toInputStream())
        }
    }

    @Test
    internal fun parse_protoContentsContainRegisteredExtension_returnsCorrectProtoMessage() {
        assertThat(
            parser.parse(FAKE_PROTO_WITH_REGISTERED_EXTENSION.asTextProto().toInputStream())
        ).isEqualTo(
            FAKE_PROTO_WITH_REGISTERED_EXTENSION
        )
    }

    companion object {
        val FAKE_PROTO: FakeMessage = FakeMessage.newBuilder().setData("some data").build();
        val FAKE_PROTO_WITH_REGISTERED_EXTENSION: FakeMessage = FAKE_PROTO.toBuilder().setExtension(
            FakeMessageExtension.fakeMessageExtension,
            FakeMessageExtension.newBuilder().setExtensionData("extension data").build()
        ).build()
        val FAKE_PROTO_WITH_UNREGISTERED_EXTENSION: FakeMessage =
            FAKE_PROTO.toBuilder().setExtension(
                FakeMessageExtension2.fakeMessageExtension2,
                FakeMessageExtension2.newBuilder().setExtensionData("extension data").build()
            ).build()

        private fun FakeMessage.asTextProto(): String {
            return TextFormat.printer().printToString(this)
        }

        private fun String.toInputStream(): InputStream {
            return ByteArrayInputStream(toByteArray())
        }
    }
}