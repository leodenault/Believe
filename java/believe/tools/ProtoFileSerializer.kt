package believe.tools

import believe.proto.TextProtoParser
import com.google.protobuf.Message
import dagger.Reusable
import org.newdawn.slick.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

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
@Reusable
class ProtoFileSerializer @Inject constructor(
    private val textProtoParserFactory: TextProtoParser.Factory
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
                    val parsedProto = textProtoParser.parse(textProtoInputStream)
                    if (parsedProto == null) {
                        Log.error("Failed to generate $fileName due to a parsing error.")
                    } else {
                        parsedProto.writeTo(binaryProtoOutputStream)
                    }
                }
            }
        }
    }
}