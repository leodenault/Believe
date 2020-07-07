package believe.datamodel.protodata

import believe.datamodel.DataManager
import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory
import com.google.protobuf.Message
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject
import kotlin.reflect.KClass

/** Manages loading levels from the file system. */
class BinaryProtoFileManager<M : Message, D>(
    private val messageType: KClass<M>,
    private val binaryProtoFileFactory: BinaryProtoFileFactory,
    private val fileDirectory: String,
    private inline val parse: (M) -> D?
) : DataManager<D> {

    private val nameToFileMap: MutableMap<String, D> = mutableMapOf()

    /**
     * Fetches proto data from the file system or, if the data is cached, returns the cached
     * instance.
     *
     * @param name the name of the file to load.
     * @return data of type [D] which is managed by this instance.
     */
    override fun getDataFor(name: String): D? {
        val data = nameToFileMap[name]
        if (data != null) return data

        val fileLocation = "$fileDirectory/$name.pb"
        val message = binaryProtoFileFactory.create(messageType, fileLocation).load()

        if (message == null) {
            Log.error("Failed to read file at '$fileLocation'.")
            return null
        }

        val parsedData = parse(message)
        if (parsedData == null) {
            Log.error("Failed to parse file at '$fileLocation'.")
            return null
        }

        nameToFileMap[name] = parsedData
        return parsedData
    }

    /** A factory for creating instances of [BinaryProtoFileManager]. */
    @Reusable
    class Factory @Inject internal constructor(
        val binaryProtoFileFactory: BinaryProtoFileFactory
    ) {

        /**
         * Creates an instance of [BinaryProtoFileManager].
         *
         * Use this method in Java.
         *
         * @param messageType the type of proto message contained within the files to be managed.
         * @param fileDirectory the directory from which files will be loaded.
         * @param parse parses the proto data into an object more useful for runtime.
         * @param M the type of proto message contained in the files which will be loaded.
         * @param D the type of data managed by the instance created by this call.
         */
        fun <M : Message, D> create(
            messageType: Class<M>, fileDirectory: String, parser: ProtoDataParser<M, D>
        ) = BinaryProtoFileManager(
            messageType.kotlin,
            binaryProtoFileFactory,
            fileDirectory
        ) { parser.parse(it) }

        /**
         * Creates an instance of [BinaryProtoFileManager].
         *
         * @param fileDirectory the directory from which files will be loaded.
         * @param parse parses the proto data into an object more useful for runtime.
         * @param M the type of proto message contained in the files which will be loaded.
         * @param D the type of data managed by the instance created by this call.
         */
        inline fun <reified M : Message, D> create(
            fileDirectory: String, noinline parse: (M) -> D?
        ) = BinaryProtoFileManager(M::class, binaryProtoFileFactory, fileDirectory, parse)
    }

    /** Parser of proto data of type [M] into an object of type [D]. */
    interface ProtoDataParser<M : Message, D> {
        /** Parse [message] into an object of type [D]. */
        fun parse(message: M): D?
    }
}
