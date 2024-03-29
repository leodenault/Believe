package believe.datamodel.protodata.testing

import believe.datamodel.protodata.BinaryProtoFile
import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory
import believe.io.testing.FakeResourceManagerFactory
import believe.proto.ProtoParserImpl
import java.io.InputStream
import java.io.OutputStream

/** A [BinaryProtoFile] factory used in testing. */
object FakeBinaryProtoFileFactory {
    private val PROTO_PARSER_FACTORY = ProtoParserImpl.Factory()

    /**
     * Creates a [BinaryProtoFileFactory] that returns [BinaryProtoFile] instances that fail at any
     * operation.
     */
    @JvmStatic
    fun createForEmptyFile(): BinaryProtoFileFactory {
        return BinaryProtoFileFactory(FakeResourceManagerFactory.createNoOp(), PROTO_PARSER_FACTORY)
    }

    /**
     * Creates a [BinaryProtoFileFactory] that returns [BinaryProtoFile] instances that can only
     * read from [inputStream].
     */
    @JvmStatic
    fun createForFileReadingFrom(inputStream: InputStream): BinaryProtoFileFactory {
        return BinaryProtoFileFactory(
            FakeResourceManagerFactory.managerReadingFrom(inputStream), PROTO_PARSER_FACTORY
        )
    }

    /**
     * Creates a [BinaryProtoFileFactory] that returns [BinaryProtoFile] instances that can only
     * write to [outputStream].
     */
    @JvmStatic
    fun createForFileWritingTo(outputStream: OutputStream): BinaryProtoFileFactory {
        return BinaryProtoFileFactory(
            FakeResourceManagerFactory.managerWritingTo(outputStream), PROTO_PARSER_FACTORY
        )
    }
}