package believe.proto.testing

import believe.proto.TextProtoParser
import believe.proto.TextProtoParserFactory
import believe.proto.testing.proto.FakeProto
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message
import com.google.protobuf.TextFormat
import java.io.InputStream
import javax.inject.Provider

/** Creates fake [TextProtoParserFactory] instances for testing. */
object FakeTextProtoParserFactoryFactory {
    /**
     * Creates a [TextProtoParserFactory] instance using [extensionRegistry] for parsing extensions.
     */
    fun create(
        extensionRegistry: ExtensionRegistry = ExtensionRegistry.getEmptyRegistry()
    ): TextProtoParserFactory = TextProtoParserFactory(Provider { extensionRegistry })

    /**
     * Creates a [TextProtoParserFactory] instance that outputs [TextProtoParser] instances that
     * fail to parse any proto on calls to [TextProtoParser.parse].
     */
    fun createFailing(): TextProtoParserFactory = FailingTextProtoParserFactory

    private object FailingTextProtoParserFactory :
        TextProtoParserFactory(Provider { ExtensionRegistry.getEmptyRegistry() }) {

        override fun create(protoClass: Class<out Message>): TextProtoParser =
            FailingTextProtoParser

        private object FailingTextProtoParser : TextProtoParser(
            ExtensionRegistry.getEmptyRegistry(), FakeProto.FakeMessage::class.java
        ) {

            override fun parse(inputStream: InputStream): Message {
                throw TextFormat.ParseException("Intentionally failed parsing.")
            }
        }
    }
}