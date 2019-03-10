package believe.testing.temporaryfolder;

import java.io.IOException;

/**
 * A {@link TemporaryFolder} that can be created and destroyed on disk.
 */
interface InstantiableTemporaryFolder extends TemporaryFolder {
  /**
   * Create the temporary folder.
   *
   * @throws IOException if en error occurs while creating the folder.
   */
  void create() throws IOException;

  /**
   * Removes the temporary folder from disk, if it exists.
   */
  void remove();
}
