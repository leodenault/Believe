package believe.testing.temporaryfolder;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class TemporaryFolderManagerImpl implements TemporaryFolderManager {
  private static final class TemporaryFolderImpl implements TemporaryFolder {
    private final File folder;

    private TemporaryFolderImpl(String folderName) throws IOException {
      folder = Files.createTempDirectory(folderName).toFile();
    }

    @Override
    public String location() {
      return folder.getAbsolutePath();
    }

    void remove() {
      removeChildren(folder);
    }

    void removeChildren(File parent) {
      File[] files = parent.listFiles();
      if (files != null) {
        for (File child : files) {
          removeChildren(child);
        }
      }
      parent.delete();
    }
  }

  @Nullable private TemporaryFolderImpl temporaryFolder;

  @Override
  public TemporaryFolder create(String location) throws IOException {
    if (temporaryFolder == null) {
      temporaryFolder = new TemporaryFolderImpl(location);
    }
    return temporaryFolder;
  }

  @Override
  public void cleanUp() {
    if (temporaryFolder == null) {
      return;
    }

    temporaryFolder.remove();
    temporaryFolder = null;
  }
}
