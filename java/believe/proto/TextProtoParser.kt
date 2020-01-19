package believe.proto

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message
import com.google.protobuf.TextFormat
import java.io.InputStream
import java.nio.CharBuffer
import java.text.ParseException
import java.util.*

/** A parser that parses Google protobufs in text format. */
@AutoFactory(allowSubclasses = true)
open class TextProtoParser(
    @Provided
    private val extensionRegistry: ExtensionRegistry, private val protoClass: Class<out Message>
) {
    /**
     * Parses the contents of [inputStream] and returns a [Message] containing its contents.
     *
     * @throws TextFormat.ParseException if the proto format is invalid or they contain an
     * unregistered extension.
     */
    @Throws(TextFormat.ParseException::class)
    open fun parse(inputStream: InputStream): Message {
        val builder: Message.Builder =
            protoClass.getMethod("newBuilder").invoke(null) as Message.Builder
        val scanner: Scanner = Scanner(inputStream).useDelimiter("\\A")
        TextFormat.getParser().merge({ cb: CharBuffer ->
            if (!scanner.hasNext()) {
                return@merge -1
            }
            val result = scanner.next()
            cb.append(result)
            result.length
        }, extensionRegistry, builder)
        return builder.build()
    }
}