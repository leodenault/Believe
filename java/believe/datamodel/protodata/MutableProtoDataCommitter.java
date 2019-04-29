package believe.datamodel.protodata;

import believe.datamodel.MutableDataCommitter;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import java.util.Optional;

/**
 * A {@link MutableDataCommitter} that handles proto instances.
 *
 * @param <M> the type of the proto message.
 */
public final class MutableProtoDataCommitter<M extends Message> implements MutableDataCommitter<M> {
  private final String dataLocation;
  private final Parser<M> messageParser;

  private M data;

  public MutableProtoDataCommitter(
      String dataLocation, Parser<M> messageParser, M defaultInstance) {
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
