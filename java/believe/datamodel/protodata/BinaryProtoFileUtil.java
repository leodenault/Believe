package believe.datamodel.protodata;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/** Utility for reading and writing binary protos to and from disk. */
final class BinaryProtoFileUtil {
  private BinaryProtoFileUtil() {}

  /**
   * Loads a proto resource by name using {@code protoParser}.
   *
   * @param resourceName the name of the resource containing the proto data.
   * @param protoParser the {@link Parser} instance to use to parse the proto data.
   * @param <M> the type of the proto message.
   * @return an instance of a proto message of type {@code M} or empty should the read operation
   *     fail.
   */
  static <M extends Message> Optional<M> load(String resourceName, Parser<M> protoParser) {
    try (InputStream inputStream = ResourceLoader.getResourceAsStream(resourceName)) {
      return Optional.of(protoParser.parseFrom(inputStream));
    } catch (RuntimeException e) {
      Log.error("Could not find binary proto file '" + resourceName + "'. Returning empty.", e);
    } catch (InvalidProtocolBufferException e) {
      Log.error(
          "Could not parse contents of binary proto file '" + resourceName + "'. Returning empty.",
          e);
    } catch (IOException e) {
      Log.error(
          "An unknown error occurred while attempting to read binary proto file '"
              + resourceName
              + "'.");
    }
    return Optional.empty();
  }

  /**
   * Writes {@code proto} to a file named {@code fileName}.
   *
   * <p><b>NOTE</b>: Resources stored within JARs are not writable. Use this for writing to files
   * outside of a JAR file.
   *
   * @param fileName the name of the file to which to output the contents of the proto.
   * @param proto the proto whose contents will be written to the output file.
   * @param <M> the type of the proto message.
   */
  static <M extends Message> void write(String fileName, M proto) {
    try {
      OutputStream outputStream = new FileOutputStream(fileName);
      proto.writeTo(outputStream);
    } catch (IOException e) {
      Log.error("Could not write binary proto file '" + fileName + "' to disk.", e);
    }
  }
}
