package believe.datamodel.protodata

import believe.datamodel.DataCommitter
import believe.datamodel.LoadableData
import believe.io.ResourceManager
import believe.proto.ProtoParser
import believe.proto.ProtoParserImpl
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.Message
import com.google.protobuf.Parser
import dagger.Reusable
import believe.util.KotlinHelpers.whenNull
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
    private val protoParser: ProtoParser<M>
) : DataCommitter<M>, LoadableData<M> {

    /** Loads the proto data from disk. */
    override fun load(): M? = resourceManager.getResourceAsStream(dataLocation).whenNull {
        Log.error("Could not load binary proto file '$dataLocation'.")
    }?.use(protoParser::parse).whenNull {
        Log.error("Could not parse contents of binary proto file '$dataLocation'.")
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
        private val resourceManager: ResourceManager,
        val protoParserFactory: ProtoParserImpl.Factory
    ) {
        fun <M : Message> create(
            dataLocation: String, protoParser: ProtoParser<M>
        ): BinaryProtoFile<M> {
            return BinaryProtoFile(resourceManager, dataLocation, protoParser)
        }

        fun <M : Message> create(
            dataLocation: String, protoParser: Parser<M>
        ): BinaryProtoFile<M> {
            return BinaryProtoFile(
                resourceManager, dataLocation, protoParserFactory.create(protoParser)
            )
        }

        inline fun <reified M : Message> create(dataLocation: String): BinaryProtoFile<M> {
            return create(dataLocation, protoParserFactory.create())
        }
    }
}
