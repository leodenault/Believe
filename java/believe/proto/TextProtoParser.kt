package believe.proto

import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message
import com.google.protobuf.TextFormat
import com.google.protobuf.TextFormat.merge
import dagger.Reusable
import org.newdawn.slick.util.Log
import java.io.InputStream
import java.nio.CharBuffer
import java.util.*
import javax.inject.Inject

/** A parser that parses Google protobufs in text format. */
open class TextProtoParser<M : Message>(
    private val extensionRegistry: ExtensionRegistry, private val protoClass: Class<M>
) {
    /**
     * Parses the contents of [inputStream] and returns a [Message] containing its contents.
     *
     * @throws TextFormat.ParseException if the proto format is invalid or they contain an
     * unregistered extension.
     */
    @Suppress("UNCHECKED_CAST")
    open fun parse(inputStream: InputStream): M? {
        val builder: Message.Builder =
            protoClass.getMethod("newBuilder").invoke(null) as Message.Builder
        val scanner: Scanner = Scanner(inputStream).useDelimiter("\\A")
        return TextFormat.getParser().runCatching {
            merge({ cb: CharBuffer ->
                if (!scanner.hasNext()) {
                    return@merge -1
                }
                val result = scanner.next()
                cb.append(result)
                result.length
            }, extensionRegistry, builder)
            builder.build() as M
        }.getOrElse {
            Log.error("Failed to parse textproto.", it)
            null
        }
    }

    @Reusable
    open class Factory @Inject constructor(private val extensionRegistry: ExtensionRegistry) {
        open fun <M : Message> create(protoClass: Class<M>): TextProtoParser<M> =
            TextProtoParser(extensionRegistry, protoClass)

        inline fun <reified M : Message> create() = create(M::class.java)
    }
}