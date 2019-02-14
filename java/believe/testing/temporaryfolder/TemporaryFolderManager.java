package believe.testing.temporaryfolder;

import java.io.IOException;

/**
 * A manager object for creating and cleaning up {@link TemporaryFolder} instances.
 */
interface TemporaryFolderManager {
  /**
   * Creates the temporary folder which will serve as the root directory to any test files that will
   * be created.
   */
  TemporaryFolder create(String location) throws IOException;

  /**
   * Deletes the temporary folder and all of its children.
   *
   * <p>{@link #create(String)} <b>must</b> be called before calling this method.
   */
  void cleanUp();
}
