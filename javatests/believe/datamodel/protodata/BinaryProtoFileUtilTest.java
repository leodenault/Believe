package believe.datamodel.protodata;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;

import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.proto.TestProto.TestMessage;
import believe.testing.temporaryfolder.TemporaryFolder;
import believe.testing.temporaryfolder.UsesTemporaryFolder;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/** Unit tests for {@link BinaryProtoFileUtil}. */
public final class BinaryProtoFileUtilTest {
  private static final String FILE_NAME = "proto.pb";
  private static final TestMessage PROTO =
      TestMessage.newBuilder().addContent("proto content").build();

  @Test
  @UsesTemporaryFolder
  public void load_returnsContentsFromFile(TemporaryFolder temporaryFolder) throws IOException {
    PROTO.writeTo(temporaryFolder.writeToFile(FILE_NAME));

    assertThat(
            BinaryProtoFileUtil.load(
                temporaryFolder.getPathToFile(FILE_NAME), TestMessage.parser()))
        .hasValue(PROTO);
  }

  @Test
  @VerifiesLoggingCalls
  public void load_fileDoesNotExist_returnsEmpty(VerifiableLogSystem logSystem) {
    assertThat(BinaryProtoFileUtil.load("", TestMessage.parser())).isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not find binary proto file.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(RuntimeException.class);
  }

  @Test
  @UsesTemporaryFolder
  @VerifiesLoggingCalls
  public void load_errorReadingFromFile_returnsEmpty(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    OutputStream outputStream = temporaryFolder.writeToFile(FILE_NAME);
    outputStream.write(new byte[] {1, 2, 3});
    outputStream.close();

    assertThat(
            BinaryProtoFileUtil.load(
                temporaryFolder.getPathToFile(FILE_NAME), TestMessage.parser()))
        .isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not parse contents.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(InvalidProtocolBufferException.class);
  }

  @Test
  @UsesTemporaryFolder
  public void write_writesContentsToDisk(TemporaryFolder temporaryFolder) throws IOException {
    BinaryProtoFileUtil.write(temporaryFolder.getPathToFile(FILE_NAME), PROTO);

    assertThat(TestMessage.parseFrom(temporaryFolder.readFile(FILE_NAME))).isEqualTo(PROTO);
  }

  @Test
  @UsesTemporaryFolder
  @VerifiesLoggingCalls
  public void write_errorWritingToDisk_logsErrorMessage(
      TemporaryFolder temporaryFolder, VerifiableLogSystem logSystem) throws IOException {
    BinaryProtoFileUtil.write(temporaryFolder.getPathToFile("nonexistent/file/location.pb"), PROTO);

    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not write binary proto file .* to disk.")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(FileNotFoundException.class);
  }
}
