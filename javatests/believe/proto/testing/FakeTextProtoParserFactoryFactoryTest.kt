package believe.proto.testing

import believe.proto.testing.proto.FakeProto.FakeMessage
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.TextFormat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream

internal class FakeTextProtoParserFactoryFactoryTest {
    @Test
    fun create_createsFactoryThatOutputsValidParsers() {
        assertThat(
            FakeTextProtoParserFactoryFactory.create().create(FakeMessage::class.java).parse(
                ByteArrayInputStream(
                    TextFormat.printer().printToString(PROTO_CONTENTS).toByteArray()
                )
            )
        ).isEqualTo(PROTO_CONTENTS)
    }

    @Test
    fun createFailing_createsFactoryThatOutputsFailingParsers() {
        assertThrows<TextFormat.ParseException> {
            FakeTextProtoParserFactoryFactory.createFailing().create(FakeMessage::class.java).parse(
                ByteArrayInputStream(PROTO_CONTENTS.toByteArray())
            )
        }
    }

    companion object {
        val PROTO_CONTENTS: FakeMessage = FakeMessage.newBuilder().setData("some data").build()
    }
}