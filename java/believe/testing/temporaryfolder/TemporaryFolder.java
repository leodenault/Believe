package believe.testing.temporaryfolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A folder that will be created, temporarily, for the purposes of unit testing.
 */
public interface TemporaryFolder {
  /**
   * Writes to a file inside the temporary folder, creating it if necessary.
   *
   * @param location the location within the temporary folder at which to create the file.
   * @return an {@link OutputStream} allowing to write data to the file.
   */
  OutputStream writeToFile(String location) throws IOException;

  /**
   * Read data from a file within the temporary folder.
   *
   * @param location the location within the temporary folder of the file that should be read.
   * @return an {@link InputStream} allowing the file's contents to be read.
   * @throws FileNotFoundException if the file does not exist.
   */
  InputStream readFile(String location) throws IOException;

  /**
   * Returns the folder created by this abstraction.
   *
   * <p>If this method is called after the folder was created and subsequently removed then the
   * {@link File} reference will remain valid but will return false to any calls to {@link
   * File#exists()}.
   *
   * @throws NullPointerException if the folder has not been created yet.
   */
  File getFolder();

  /**
   * Returns a {@link File} reference located at {@code location}.
   *
   * @throws IOException if there is an error finding the file.
   */
  File getFile(String location) throws IOException;

  /**
   * Returns the path name to the file found at {@code location}, whether the file actually exists
   * there or not.
   *
   * @param location the location of the file, relative to the temporary folder, for which to
   * retrieve the full path name.
   */
  String getPathToFile(String location) throws IOException;
}
