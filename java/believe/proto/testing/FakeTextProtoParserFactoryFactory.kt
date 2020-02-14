package believe.proto.testing

import believe.proto.TextProtoParser
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message
import java.io.InputStream

/** Creates fake [TextProtoParser.Factory] instances for testing. */
object FakeTextProtoParserFactoryFactory {
    /**
     * Creates a [TextProtoParser.Factory] instance using [extensionRegistry] for parsing extensions.
     */
    fun create(
        extensionRegistry: ExtensionRegistry = ExtensionRegistry.getEmptyRegistry()
    ): TextProtoParser.Factory = TextProtoParser.Factory(extensionRegistry)

    /**
     * Creates a [TextProtoParser.Factory] instance that outputs [TextProtoParser] instances that
     * fail to parse any proto on calls to [TextProtoParser.parse].
     */
    fun createFailing(): TextProtoParser.Factory = FailingTextProtoParserFactory

    private object FailingTextProtoParserFactory :
        TextProtoParser.Factory(ExtensionRegistry.getEmptyRegistry()) {

        override fun <M : Message> create(protoClass: Class<M>) = FailingTextProtoParser(protoClass)

        private class FailingTextProtoParser<M : Message>(
            protoClass: Class<M>
        ) : TextProtoParser<M>(ExtensionRegistry.getEmptyRegistry(), protoClass) {

            override fun parse(inputStream: InputStream): M? = null
        }
    }
}