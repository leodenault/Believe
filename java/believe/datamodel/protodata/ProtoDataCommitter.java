package believe.datamodel.protodata;

import believe.datamodel.DataCommitter;
import believe.datamodel.DataProvider;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;

import java.util.Optional;

/**
 * A {@link DataProvider} that provides proto instances.
 *
 * @param <M> the type of the proto message.
 */
public final class ProtoDataCommitter<M extends Message> implements DataCommitter<M> {
  private final String dataLocation;
  private final Parser<M> messageParser;

  private M data;

  public ProtoDataCommitter(String dataLocation, Parser<M> messageParser, M defaultInstance) {
    this.dataLocation = dataLocation;
    this.messageParser = messageParser;
    data = defaultInstance;
  }

  /** Loads the proto data from disk. */
  public void load() {
    Optional<M> loadedData = BinaryProtoFileUtil.load(dataLocation, messageParser);
    loadedData.ifPresent(m -> data = m);
  }

  @Override
  public M get() {
    return data;
  }

  @Override
  public void update(M data) {
    this.data = data;
  }

  @Override
  public void commit() {
    BinaryProtoFileUtil.write(dataLocation, data);
  }
}
