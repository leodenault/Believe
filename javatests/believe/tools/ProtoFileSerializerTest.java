package believe.tools;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.mockito.InstantiateMocksIn;
import believe.testing.proto.TestProto;
import believe.testing.proto.TestProto.TestMessage;
import believe.testing.temporaryfolder.TemporaryFolder;
import believe.testing.temporaryfolder.UsesTemporaryFolder;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

@InstantiateMocksIn
public final class ProtoFileSerializerTest {
  private static final class OutputStreamAppendable implements Appendable {
    private final OutputStream outputStream;

    OutputStreamAppendable(OutputStream outputStream) {
      this.outputStream = outputStream;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      for (int c : csq.chars().toArray()) {
        outputStream.write(c);
      }
      return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      for (int i = start; i < end; i++) {
        outputStream.write(csq.charAt(i));
      }
      return this;
    }

    @Override
    public Appendable append(char c) throws IOException {
      outputStream.write(c);
      return this;
    }
  }

  private static final String PROTO_MESSAGE_CLASS = "believe.testing.proto.TestProto$TestMessage";

  @Test
  @UsesTemporaryFolder
  public void main_wrongNumberOfArgs_doesNothing(TemporaryFolder temporaryFolder) throws Exception {
    ProtoFileSerializer.main(new String[] {});

    assertThat(temporaryFolder.getFolder().list()).isEmpty();
  }

  @Test
  @UsesTemporaryFolder
  public void main_createsCorrespondingOutputFile(TemporaryFolder temporaryFolder)
      throws Exception {
    temporaryFolder.writeToFile("some_proto.textproto").close();
    temporaryFolder.writeToFile("subdir/dummy.txt"); // Create dummy file to generate a directory.

    String inputFilePath = temporaryFolder.getFile("some_proto.textproto").getCanonicalPath();

    ProtoFileSerializer.main(
        new String[] {
          PROTO_MESSAGE_CLASS, temporaryFolder.getFile("subdir").getCanonicalPath(), inputFilePath
        });

    assertThat(temporaryFolder.getFile("subdir/some_proto.pb").exists()).isTrue();
  }

  @Test
  @UsesTemporaryFolder
  @VerifiesLoggingCalls
  public void main_fileDoesNotExist_logsErrorAndSkips(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws Exception {
    String nonExistentFilePath =
        temporaryFolder.getFolder().getCanonicalPath() + "doesnt_exist.textproto";

    ProtoFileSerializer.main(
        new String[] {
          PROTO_MESSAGE_CLASS, temporaryFolder.getFolder().getCanonicalPath(), nonExistentFilePath
        });

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  @Test
  @UsesTemporaryFolder
  public void main_outputFileAlreadyExists_isOverwritten(TemporaryFolder temporaryFolder)
      throws Exception {
    temporaryFolder.writeToFile("already_exists.textproto").close();
    temporaryFolder.writeToFile("already_exists.pb").close();
    String inputFilePath = temporaryFolder.getFile("already_exists.textproto").getCanonicalPath();

    ProtoFileSerializer.main(
        new String[] {
          PROTO_MESSAGE_CLASS, temporaryFolder.getFolder().getCanonicalPath(), inputFilePath
        });

    assertThat(temporaryFolder.getFile("already_exists.pb").exists()).isTrue();
  }

  @Test
  public void main_protoClassDoesNotExist_throwsClassNotFoundException() {
    assertThrows(
        ClassNotFoundException.class,
        () ->
            ProtoFileSerializer.main(
                new String[] {"believe.tools.TestProto$DoesNotExist", "", ""}));
  }

  @Test
  public void main_protoClassIsNotProto_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> ProtoFileSerializer.main(new String[] {"believe.tools.ProtoFileSerializer", "", ""}));
  }

  @Test
  @UsesTemporaryFolder
  @VerifiesLoggingCalls
  public void main_protoContentsAreInvalid_logsErrorAndSkips(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws Exception {
    OutputStream outputStream = temporaryFolder.writeToFile("some_proto.textproto");
    outputStream.write(new byte[] {1, 2, 3});
    outputStream.close();
    String inputFilePath = temporaryFolder.getPathToFile("some_proto.textproto");

    ProtoFileSerializer.main(
        new String[] {
          PROTO_MESSAGE_CLASS, temporaryFolder.getFolder().getCanonicalPath(), inputFilePath
        });

    InputStream binaryFileContents = temporaryFolder.readFile("some_proto.pb");
    assertThat(binaryFileContents.read()).isEqualTo(-1);
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern(
            "Could not read "
                + "'.*some_proto\\.textproto' as "
                + "believe\\.testing\\.proto\\.TestProto\\$TestMessage.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(ParseException.class);
  }

  @Test
  @UsesTemporaryFolder
  void main_multipleInputFiles_outputsIndependentFiles(TemporaryFolder temporaryFolder)
      throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    TestMessage expectedMessage1 = TestMessage.newBuilder().addContent("content 1").build();
    TestMessage expectedMessage2 = TestMessage.newBuilder().addContent("content 2").build();
    OutputStream outputStream1 = temporaryFolder.writeToFile("some_proto_1.textproto");
    OutputStream outputStream2 = temporaryFolder.writeToFile("some_proto_2.textproto");
    TextFormat.printer().print(expectedMessage1, new OutputStreamAppendable(outputStream1));
    TextFormat.printer().print(expectedMessage2, new OutputStreamAppendable(outputStream2));
    outputStream1.close();
    outputStream2.close();
    String inputFilePath1 = temporaryFolder.getPathToFile("some_proto_1.textproto");
    String inputFilePath2 = temporaryFolder.getPathToFile("some_proto_2.textproto");

    ProtoFileSerializer.main(
        new String[] {
          PROTO_MESSAGE_CLASS,
          temporaryFolder.getFolder().getCanonicalPath(),
          inputFilePath1,
          inputFilePath2
        });

    InputStream inputStream1 = temporaryFolder.readFile("some_proto_1.pb");
    InputStream inputStream2 = temporaryFolder.readFile("some_proto_2.pb");
    TestMessage actualMessage1 = TestMessage.parseFrom(inputStream1);
    TestMessage actualMessage2 = TestMessage.parseFrom(inputStream2);
    assertThat(actualMessage1).isEqualTo(expectedMessage1);
    assertThat(actualMessage2).isEqualTo(expectedMessage2);
  }

  @Test
  @UsesTemporaryFolder
  public void convert_producesBinaryProto(TemporaryFolder temporaryFolder) throws IOException {
    ProtoFileSerializer converter = new ProtoFileSerializer();
    TestMessage inputMessage = TestMessage.newBuilder().addContent("proto contents").build();
    TextFormat.print(
        inputMessage,
        new OutputStreamAppendable(temporaryFolder.writeToFile("some_proto.textproto")));
    InputStream inputFileContents = temporaryFolder.readFile("some_proto.textproto");
    OutputStream outputFileContents = temporaryFolder.writeToFile("some_proto.pb");

    converter.convert(TestMessage.newBuilder(), inputFileContents, outputFileContents);

    TestMessage outputMessage = TestMessage.parseFrom(temporaryFolder.readFile("some_proto.pb"));
    assertThat(outputMessage.getContent(0)).isEqualTo("proto contents");
  }

  @Test
  @UsesTemporaryFolder
  public void convert_fileCannotBeReadAsProto_throwsParseException(TemporaryFolder temporaryFolder)
      throws IOException {
    ProtoFileSerializer converter = new ProtoFileSerializer();
    OutputStream outputStream = temporaryFolder.writeToFile("some_proto.textproto");
    outputStream.write(new byte[] {1, 2, 3});
    outputStream.close();
    InputStream inputFileContents = temporaryFolder.readFile("some_proto.textproto");
    OutputStream outputFileContents = temporaryFolder.writeToFile("some_proto.pb");

    assertThrows(
        ParseException.class,
        () -> converter.convert(TestMessage.newBuilder(), inputFileContents, outputFileContents));
    assertThat(temporaryFolder.readFile("some_proto.pb").read()).isEqualTo(-1);
  }
}
