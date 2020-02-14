package believe.tools

import java.io.IOException

/** Entry point to the [ProtoFileSerializer] tool. */
object ProtoFileSerializerRunner {
    private const val USAGE =
        "Usage:\n    bazel-out/.../proto_file_serializer com.proto.MessageType /path/to/out/dir proto1.textproto [other text protos]"

    @Throws(IOException::class, ClassNotFoundException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (!verifyArgs(args)) {
            return
        }
        DaggerProtoFileSerializerComponent.create().protoFileSerializer.serialize(args)
    }

    private fun verifyArgs(args: Array<String>): Boolean {
        if (args.size < 3) {
            println(USAGE)
            return false
        }
        return true
    }
}