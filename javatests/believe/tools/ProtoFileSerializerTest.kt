package believe.tools

import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.proto.TextProtoParser
import believe.proto.testing.FakeTextProtoParserFactoryFactory
import believe.testing.proto.TestProto.TestMessage
import believe.testing.temporaryfolder.TemporaryFolder
import believe.testing.temporaryfolder.UsesTemporaryFolder
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.TextFormat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.io.OutputStream

internal class ProtoFileSerializerTest {
    private var textProtoParserFactory: TextProtoParser.Factory =
        FakeTextProtoParserFactoryFactory.create()
    private val serializer: ProtoFileSerializer by lazy {
        ProtoFileSerializer(textProtoParserFactory)
    }

    @Test
    @UsesTemporaryFolder
    @Throws(Exception::class)
    fun serialize_createsCorrespondingOutputFile(temporaryFolder: TemporaryFolder) {
        val inputFilePath = with(temporaryFolder) {
            writeToFile("some_proto.textproto").close()
            writeToFile("subdir/dummy.txt").close() // Create dummy file to generate a directory.
            getFile("some_proto.textproto").canonicalPath
        }

        serializer.serialize(
            arrayOf(
                PROTO_MESSAGE_CLASS, temporaryFolder.getFile("subdir").canonicalPath, inputFilePath
            )
        )

        assertThat(temporaryFolder.getFile("subdir/some_proto.pb").exists()).isTrue()
    }

    @Test
    @UsesTemporaryFolder
    @VerifiesLoggingCalls
    @Throws(Exception::class)
    fun serialize_fileDoesNotExist_logsErrorAndSkips(
        temporaryFolder: TemporaryFolder, logSystem: VerifiableLogSystem
    ) {
        val nonExistentFilePath = "${temporaryFolder.folder.canonicalPath}doesnt_exist.textproto"

        serializer.serialize(
            arrayOf(
                PROTO_MESSAGE_CLASS, temporaryFolder.folder.canonicalPath, nonExistentFilePath
            )
        )

        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Could not find.*").hasSeverity(LogSeverity.ERROR)
    }

    @Test
    @UsesTemporaryFolder
    @Throws(Exception::class)
    fun serialize_outputFileAlreadyExists_isOverwritten(temporaryFolder: TemporaryFolder) {
        val inputFilePath = with(temporaryFolder) {
            writeToFile("already_exists.textproto").close()
            writeToFile("already_exists.pb").close()
            getFile("already_exists.textproto").canonicalPath
        }

        serializer.serialize(
            arrayOf(
                PROTO_MESSAGE_CLASS, temporaryFolder.folder.canonicalPath, inputFilePath
            )
        )
        assertThat(temporaryFolder.getFile("already_exists.pb").exists()).isTrue()
    }

    @Test
    fun serialize_protoClassDoesNotExist_throwsClassNotFoundException() {
        assertThrows<ClassNotFoundException> {
            serializer.serialize(arrayOf("believe.tools.TestProto\$DoesNotExist", "", ""))
        }
    }

    @Test
    fun serialize_protoClassIsNotProto_throwsIllegalArgumentException() {
        assertThrows<IllegalArgumentException> {
            serializer.serialize(arrayOf("believe.tools.ProtoFileSerializer", "", ""))
        }
    }

    @Test
    @UsesTemporaryFolder
    @VerifiesLoggingCalls
    @Throws(Exception::class)
    fun serialize_textProtoParserReturnsNull_logsErrorAndSkips(
        temporaryFolder: TemporaryFolder, logSystem: VerifiableLogSystem
    ) {
        textProtoParserFactory = FakeTextProtoParserFactoryFactory.createFailing()
        val inputFilePath = with(temporaryFolder) {
            writeToFile("some_proto.textproto").close()
            getFile("some_proto.textproto").canonicalPath
        }

        serializer.serialize(
            arrayOf(
                PROTO_MESSAGE_CLASS, temporaryFolder.getFile(".").canonicalPath, inputFilePath
            )
        )

        temporaryFolder.readFile("some_proto.pb").use {
            assertThat(it.read()).isEqualTo(-1)
        }
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat().hasPattern(
            "Failed to generate some_proto.pb due to a parsing error."
        ).hasSeverity(LogSeverity.ERROR)
    }

    @Test
    @UsesTemporaryFolder
    @Throws(IOException::class, ClassNotFoundException::class)
    fun serialize_multipleInputFiles_outputsIndependentFiles(temporaryFolder: TemporaryFolder) {
        val expectedMessage1 = TestMessage.newBuilder().addContent("content 1").build()
        val expectedMessage2 = TestMessage.newBuilder().addContent("content 2").build()
        with(temporaryFolder) {
            writeToFile("some_proto_1.textproto").use {
                TextFormat.printer().print(expectedMessage1, OutputStreamAppendable(it))
            }
            writeToFile("some_proto_2.textproto").use {
                TextFormat.printer().print(expectedMessage2, OutputStreamAppendable(it))
            }
        }
        val inputFilePath1 = temporaryFolder.getPathToFile("some_proto_1.textproto")
        val inputFilePath2 = temporaryFolder.getPathToFile("some_proto_2.textproto")

        serializer.serialize(
            arrayOf(
                PROTO_MESSAGE_CLASS,
                temporaryFolder.folder.canonicalPath,
                inputFilePath1,
                inputFilePath2
            )
        )

        with(temporaryFolder) {
            val actualMessage1 = readFile("some_proto_1.pb").use { TestMessage.parseFrom(it) }
            val actualMessage2 = readFile("some_proto_2.pb").use { TestMessage.parseFrom(it) }
            assertThat(actualMessage1).isEqualTo(expectedMessage1)
            assertThat(actualMessage2).isEqualTo(expectedMessage2)
        }
    }

    companion object {
        private const val PROTO_MESSAGE_CLASS = "believe.testing.proto.TestProto\$TestMessage"
    }

    private class OutputStreamAppendable internal constructor(
        private val outputStream: OutputStream
    ) : Appendable {

        @Throws(IOException::class)
        override fun append(csq: CharSequence): Appendable {
            for (c in csq.chars().toArray()) {
                outputStream.write(c)
            }
            return this
        }

        @Throws(IOException::class)
        override fun append(csq: CharSequence, start: Int, end: Int): Appendable {
            for (i in start until end) {
                outputStream.write(csq[i].toInt())
            }
            return this
        }

        @Throws(IOException::class)
        override fun append(c: Char): Appendable {
            outputStream.write(c.toInt())
            return this
        }
    }
}