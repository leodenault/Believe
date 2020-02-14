package believe.proto

import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Message
import com.google.protobuf.Parser
import dagger.Reusable
import org.newdawn.slick.util.Log
import java.io.InputStream
import javax.inject.Inject
import kotlin.reflect.full.staticFunctions

/**
 * A default implementation of [ProtoParser].
 *
 * @param extensionRegistry the [ExtensionRegistry] used in identifying and parsing extensions.
 */
class ProtoParserImpl<M : Message> constructor(
    private val extensionRegistry: ExtensionRegistry, private val internalParser: Parser<M>
) : ProtoParser<M> {

    override fun parse(inputStream: InputStream): M? {
        return try {
            val parsedMessage = internalParser.parseFrom(inputStream, extensionRegistry)
            if (parsedMessage.unknownFields.serializedSize > 0) {
                Log.error("Could not parse all fields in ${parsedMessage.descriptorForType.name}.")
            }
            parsedMessage
        } catch (e: InvalidProtocolBufferException) {
            Log.error("Failed to parse proto contents.", e)
            null
        }
    }

    /**
     * A factory for creating [ProtoParserImpl] instances.
     *
     * @param extensionRegistry the [ExtensionRegistry] used in identifying and parsing extensions.
     */
    @Reusable
    class Factory @Inject constructor(
        private val extensionRegistry: ExtensionRegistry
    ) {
        constructor() : this(ExtensionRegistry.getEmptyRegistry())

        /** Returns a [ProtoParserImpl] using [messageParser] as its underlying parser. */
        fun <M : Message> create(messageParser: Parser<M>): ProtoParserImpl<M> =
            ProtoParserImpl(extensionRegistry, messageParser)

        /** Returns a [ProtoParserImpl] for protos of type [M]. */
        inline fun <reified M : Message> create(): ProtoParserImpl<M> {
            // Safe cast: parser() is a static method present on any subclass of Message.
            @Suppress("UNCHECKED_CAST") val messageParser: Parser<M> =
                M::class.staticFunctions.single { it.name == "parser" }.call() as Parser<M>
            return create(messageParser)
        }
    }
}