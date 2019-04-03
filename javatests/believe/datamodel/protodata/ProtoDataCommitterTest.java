package believe.datamodel.protodata;

import static com.google.common.truth.Truth.assertThat;

import believe.testing.proto.TestProto.TestMessage;
import believe.testing.temporaryfolder.TemporaryFolder;
import believe.testing.temporaryfolder.UsesTemporaryFolder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/** Unit tests for {@link ProtoDataCommitter}. */
public final class ProtoDataCommitterTest {
  private static final String DATA_LOCATION = "test_proto.pb";
  private static final TestMessage PROTO_DATA =
      TestMessage.newBuilder().setContent("initial content").build();

  @Test
  public void get_dataNotLoaded_returnsDefaultProtoData() {
    assertThat(protoDataProvider().get()).isEqualTo(TestMessage.getDefaultInstance());
  }

  @Test
  @UsesTemporaryFolder
  public void get_dataLoaded_returnsProtoDataFromDisk(TemporaryFolder temporaryFolder)
      throws IOException {
    PROTO_DATA.writeTo(temporaryFolder.writeToFile(DATA_LOCATION));
    ProtoDataCommitter<TestMessage> protoDataCommitter = protoDataProvider(temporaryFolder);

    protoDataCommitter.load();

    assertThat(protoDataCommitter.get()).isEqualTo(PROTO_DATA);
  }

  @Test
  public void get_valueWasUpdated_returnsUpdatedProtoData() {
    ProtoDataCommitter<TestMessage> protoDataCommitter = protoDataProvider();
    protoDataCommitter.update(PROTO_DATA);

    assertThat(protoDataCommitter.get()).isEqualTo(PROTO_DATA);
  }

  @Test
  @UsesTemporaryFolder
  public void commit_commitsProtoDataToDisk(TemporaryFolder temporaryFolder) throws IOException {
    ProtoDataCommitter<TestMessage> protoDataCommitter = protoDataProvider(temporaryFolder);
    protoDataCommitter.update(PROTO_DATA);

    protoDataCommitter.commit();

    assertThat(temporaryFolder.getFile(DATA_LOCATION).exists()).isTrue();
  }

  private static ProtoDataCommitter<TestMessage> protoDataProvider() {
    return new ProtoDataCommitter<>("", TestMessage.parser(), TestMessage.getDefaultInstance());
  }

  private static ProtoDataCommitter<TestMessage> protoDataProvider(TemporaryFolder temporaryFolder)
      throws IOException {
    return new ProtoDataCommitter<>(
        temporaryFolder.getPathToFile(DATA_LOCATION),
        TestMessage.parser(),
        TestMessage.getDefaultInstance());
  }
}
