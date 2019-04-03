package believe.tools;

import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import org.newdawn.slick.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * Takes a proto message reference and an arbitrary number of textproto files and serializes the
 * textprotos into corresponding {@code *.txt} files.
 *
 *
 * <p>Usage:
 * <pre>
 *   java -jar proto_file_serializer.jar com.proto.MessageType proto1.textproto proto2.textproto
 * </pre>
 */
public class ProtoFileSerializer {
  private static final String
      USAGE =
      "Usage:\n"
          + "    bazel-out/.../proto_file_serializer com.proto.MessageType /path/to/out/dir "
          + "proto1.textproto [other text protos]";

  public static void main(String[] args) throws
      IOException,
      ClassNotFoundException,
      NoSuchMethodException,
      InvocationTargetException,
      IllegalAccessException {
    if (!verifyArgs(args)) {
      return;
    }

    Message.Builder builder = verifyProtoClass(args[0]);
    String outputDir = args[1];
    ProtoFileSerializer serializer = new ProtoFileSerializer();

    for (int i = 2; i < args.length; i++) {
      String arg = args[i];

      File inputFile = new File(arg);
      if (!inputFile.exists()) {
        Log.error("Could not find file '" + arg + "'. Skipping...");
        continue;
      }

      String fileName = arg.substring(arg.lastIndexOf("/") + 1, arg.lastIndexOf(".")) + ".pb";
      String filePath = outputDir + "/" + fileName;
      File outputFile = new File(filePath);
      try (
          FileInputStream textProtoInputStream = new FileInputStream(inputFile);
          FileOutputStream binaryProtoOutputStream = new FileOutputStream(outputFile)) {
        serializer.convert(builder, textProtoInputStream, binaryProtoOutputStream);
      } catch (ParseException e) {
        Log.error("Could not read '" + arg + "' as " + args[0] + ". Skipping...", e);
      }
    }
  }

  private static boolean verifyArgs(String[] args) {
    if (args.length < 3) {
      System.out.println(USAGE);
      return false;
    }
    return true;
  }

  private static Message.Builder verifyProtoClass(String className) throws
      ClassNotFoundException,
      NoSuchMethodException,
      InvocationTargetException,
      IllegalAccessException {
    Class<?> protoClass = Class.forName(className);

    if (!(Message.class.isAssignableFrom(protoClass))) {
      throw new IllegalArgumentException("Class '" + className + "' is not a proto message.");
    }

    Object builder = protoClass.getMethod("newBuilder").invoke(null);
    return (Message.Builder) builder;
  }

  void convert(
      Message.Builder builder,
      InputStream textProtoInputStream,
      OutputStream binaryProtoOutputStream) throws IOException {
    Scanner s = new Scanner(textProtoInputStream).useDelimiter("\\A");
    TextFormat.getParser().merge(cb -> {
      if (!s.hasNext()) {
        return -1;
      }
      String result = s.next();
      cb.append(result);
      return result.length();
    }, builder);
    builder.build().writeTo(binaryProtoOutputStream);
  }
}
