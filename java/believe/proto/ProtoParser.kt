package believe.proto

import com.google.protobuf.Message
import java.io.InputStream
import kotlin.reflect.KClass

/** A parser that accepts protos in binary format. */
interface ProtoParser<M: Message> {
    /** Parses the bytes from [inputStream] into a proto of type [M] or returns null on failure. */
    fun parse(inputStream: InputStream): M?
}
