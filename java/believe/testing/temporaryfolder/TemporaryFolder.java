package believe.testing.temporaryfolder;

/**
 * A folder that will be created, temporarily, for the purposes of unit testing.
 */
public interface TemporaryFolder {
  /**
   * Returns the location of the file represented by this instance as a string.
   */
  String location();
}
