package believe.tools

import believe.proto.DaggerProtoComponent
import believe.proto.TextProtoParserFactory
import com.google.protobuf.Message
import com.google.protobuf.TextFormat
import org.newdawn.slick.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Takes a proto message reference and an arbitrary number of textproto files and serializes the
 * textprotos into corresponding `*.txt` files.
 *
 * Usage:
 * ```
 * java -jar proto_file_serializer.jar \
 *     com.proto.MessageType \
 *     proto1.textproto \
 *     proto2.textproto
 * ```
 */
class ProtoFileSerializer internal constructor(
    private val textProtoParserFactory: TextProtoParserFactory
) {

    @Throws(ClassNotFoundException::class, IOException::class)
    fun serialize(args: Array<String>) {
        val protoClass = Class.forName(args[0])
        require(Message::class.java.isAssignableFrom(protoClass)) {
            "Class '" + args[0] + "' is not a proto message."
        }
        // Safe cast
        @Suppress("UNCHECKED_CAST") val textProtoParser =
            textProtoParserFactory.create(protoClass as Class<out Message>)
        val outputDir = args[1]
        for (i in 2 until args.size) {
            val arg = args[i]
            val inputFile = File(arg)
            if (!inputFile.exists()) {
                Log.error("Could not find file '$arg'. Skipping...")
                continue
            }
            val fileName = arg.substring(arg.lastIndexOf("/") + 1, arg.lastIndexOf(".")) + ".pb"
            val filePath = "$outputDir/$fileName"
            val outputFile = File(filePath)
            FileInputStream(inputFile).use { textProtoInputStream ->
                FileOutputStream(outputFile).use { binaryProtoOutputStream ->
                    try {
                        textProtoParser.parse(textProtoInputStream).writeTo(binaryProtoOutputStream)
                    } catch (e: TextFormat.ParseException) {
                        Log.error("Failed to generate $fileName due to a parsing error.", e)
                    }
                }
            }
        }
    }

    companion object {
        private const val USAGE =
            "Usage:\n    bazel-out/.../proto_file_serializer com.proto.MessageType /path/to/out/dir proto1.textproto [other text protos]"

        @Throws(IOException::class, ClassNotFoundException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            if (!verifyArgs(args)) {
                return
            }
            ProtoFileSerializer(DaggerProtoComponent.create().textProtoParserFactory).serialize(
                args
            )
        }

        private fun verifyArgs(args: Array<String>): Boolean {
            if (args.size < 3) {
                println(USAGE)
                return false
            }
            return true
        }
    }

}