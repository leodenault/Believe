package believe.datamodel.protodata;

import static com.google.common.truth.Truth.assertThat;

import believe.testing.proto.TestProto.TestMessage;
import believe.testing.temporaryfolder.TemporaryFolder;
import believe.testing.temporaryfolder.UsesTemporaryFolder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/** Unit tests for {@link MutableProtoDataCommitter}. */
public final class MutableProtoDataCommitterTest {
  private static final String DATA_LOCATION = "test_proto.pb";
  private static final TestMessage PROTO_DATA =
      TestMessage.newBuilder().addContent("initial content").build();

  @Test
  public void get_dataNotLoaded_returnsDefaultProtoData() {
    assertThat(mutableProtoDataCommitter().get()).isEqualTo(TestMessage.getDefaultInstance());
  }

  @Test
  @UsesTemporaryFolder
  public void get_dataLoaded_returnsProtoDataFromDisk(TemporaryFolder temporaryFolder)
      throws IOException {
    PROTO_DATA.writeTo(temporaryFolder.writeToFile(DATA_LOCATION));
    MutableProtoDataCommitter<TestMessage> mutableProtoDataCommitter =
        mutableProtoDataCommitter(temporaryFolder);

    mutableProtoDataCommitter.load();

    assertThat(mutableProtoDataCommitter.get()).isEqualTo(PROTO_DATA);
  }

  @Test
  public void get_valueWasUpdated_returnsUpdatedProtoData() {
    MutableProtoDataCommitter<TestMessage> mutableProtoDataCommitter = mutableProtoDataCommitter();
    mutableProtoDataCommitter.update(PROTO_DATA);

    assertThat(mutableProtoDataCommitter.get()).isEqualTo(PROTO_DATA);
  }

  @Test
  @UsesTemporaryFolder
  public void commit_commitsProtoDataToDisk(TemporaryFolder temporaryFolder) throws IOException {
    MutableProtoDataCommitter<TestMessage> mutableProtoDataCommitter =
        mutableProtoDataCommitter(temporaryFolder);
    mutableProtoDataCommitter.update(PROTO_DATA);

    mutableProtoDataCommitter.commit();

    assertThat(temporaryFolder.getFile(DATA_LOCATION).exists()).isTrue();
  }

  private static MutableProtoDataCommitter<TestMessage> mutableProtoDataCommitter() {
    return new MutableProtoDataCommitter<>(
        "", TestMessage.parser(), TestMessage.getDefaultInstance());
  }

  private static MutableProtoDataCommitter<TestMessage> mutableProtoDataCommitter(
      TemporaryFolder temporaryFolder) throws IOException {
    return new MutableProtoDataCommitter<>(
        temporaryFolder.getPathToFile(DATA_LOCATION),
        TestMessage.parser(),
        TestMessage.getDefaultInstance());
  }
}
