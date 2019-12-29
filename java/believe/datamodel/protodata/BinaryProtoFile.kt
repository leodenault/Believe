package believe.datamodel.protodata

import believe.datamodel.DataCommitter
import believe.datamodel.LoadableData
import believe.io.ResourceManager
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Message
import com.google.protobuf.Parser
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject

/**
 * A file storing proto data in binary format.
 *
 * @param [M] the type of the proto message.
 */
class BinaryProtoFile<M : Message> internal constructor(
    private val resourceManager: ResourceManager,
    private val dataLocation: String,
    private val messageParser: Parser<M>
) : DataCommitter<M>, LoadableData<M> {

    /** Loads the proto data from disk. */
    override fun load(): M? {
        val inputStream = resourceManager.getResourceAsStream(dataLocation)

        if (inputStream == null) {
            Log.error("Could not load binary proto file '$dataLocation'.")
            return null
        }

        return try {
            inputStream.use { messageParser.parseFrom(it) }
        } catch (e: InvalidProtocolBufferException) {
            Log.error(
                "Could not parse contents of binary proto file '$dataLocation'.", e
            )
            null
        }
    }

    override fun commit(data: M) {
        val outputStream = resourceManager.getOutputStreamToFileResource(dataLocation)
        if (outputStream == null) {
            Log.error("Could not write proto data to file at '$dataLocation'.")
            return
        }

        data.writeTo(outputStream)
    }

    @Reusable
    class BinaryProtoFileFactory @Inject constructor(
        private val resourceManager: ResourceManager
    ) {
        fun <M : Message> create(
            dataLocation: String, messageParser: Parser<M>
        ): BinaryProtoFile<M> {
            return BinaryProtoFile(resourceManager, dataLocation, messageParser)
        }
    }
}
