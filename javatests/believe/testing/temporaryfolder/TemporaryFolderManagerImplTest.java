package believe.testing.temporaryfolder;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Unit tests for {@link TemporaryFolderManagerImpl}.
 */
public final class TemporaryFolderManagerImplTest {
  private static final String DIRECTORY_NAME = "TEMPerature";
  private static final String SIBLING_DIRECTORY_NAME = "sibling";

  private final TemporaryFolderManager temporaryFolderManager = new TemporaryFolderManagerImpl();

  @Test
  public void create_generatesFolderInTestDirectory() throws IOException {
    TemporaryFolder temporaryFolder = temporaryFolderManager.create(DIRECTORY_NAME);

    File directory = new File(temporaryFolder.location());
    assertThat(temporaryFolder.location()).isEqualTo(directory.getAbsolutePath());
    assertThat(directory.exists()).isTrue();

    cleanUpDirectory(temporaryFolder.location());
  }

  @Test
  public void create_directoryIsAlreadyCreated_doesNothingAndReturnsSameInstance()
      throws IOException {
    TemporaryFolder temporaryFolder1 = temporaryFolderManager.create(DIRECTORY_NAME);
    TemporaryFolder temporaryFolder2 = temporaryFolderManager.create(DIRECTORY_NAME);

    assertThat(temporaryFolder1).isSameAs(temporaryFolder2);
    File directory = new File(temporaryFolder1.location());
    assertThat(directory.exists()).isTrue();
    assertThat(directory.getParentFile().listFiles((dir, name) -> name.startsWith(DIRECTORY_NAME)))
        .asList()
        .hasSize(1);

    cleanUpDirectory(temporaryFolder1.location());
  }

  @Test
  public void cleanUp_directoryExistsAndIsEmpty_removesDirectory() throws IOException {
    TemporaryFolder temporaryFolder = temporaryFolderManager.create(DIRECTORY_NAME);
    temporaryFolderManager.cleanUp();

    assertThat(new File(temporaryFolder.location()).exists()).isFalse();
  }

  @Test
  public void cleanUp_directoryExistsAndContainsChildren_removesDirectory() throws IOException {
    TemporaryFolder temporaryFolder = temporaryFolderManager.create(DIRECTORY_NAME);
    new File(temporaryFolder.location() + "/nothing.txt").createNewFile();
    temporaryFolderManager.cleanUp();

    assertThat(new File(temporaryFolder.location()).exists()).isFalse();
  }

  @Test
  public void cleanUp_directoryDoesNotExist_doesNothing() throws IOException {
    File siblingDirectory = File.createTempFile(SIBLING_DIRECTORY_NAME, "");
    temporaryFolderManager.cleanUp();

    assertThat(new File(siblingDirectory.getAbsolutePath()).exists()).isTrue();

    siblingDirectory.delete();
  }

  @Test
  public void create_afterCleaningUp_createsNewTemporaryFolderInstance() throws IOException {
    TemporaryFolder temporaryFolder1 = temporaryFolderManager.create(DIRECTORY_NAME);
    temporaryFolderManager.cleanUp();
    TemporaryFolder temporaryFolder2 = temporaryFolderManager.create(DIRECTORY_NAME);

    assertThat(temporaryFolder1).isNotSameAs(temporaryFolder2);

    cleanUpDirectory(temporaryFolder2.location());
  }

  private static void cleanUpDirectory(String pathName) {
    File directory = new File(pathName);
    if (directory.exists()) {
      directory.delete();
    }
  }
}
