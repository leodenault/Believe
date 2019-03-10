package believe.testing.temporaryfolder;

import static believe.app.annotation.Nullability.checkNotNull;
import static believe.app.annotation.Nullability.notNull;

import javax.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

final class TemporaryFolderImpl implements InstantiableTemporaryFolder {
  private final String folderName;

  @Nullable private File folder;

  TemporaryFolderImpl(String folderName) {
    this.folderName = folderName;
  }

  @Override
  public void create() throws IOException {
    folder = Files.createTempDirectory(folderName).toFile();
  }

  @Override
  public void remove() {
    checkFolderExists();
    removeChildren(notNull(folder));
  }

  @Override
  public OutputStream writeToFile(String location) throws IOException {
    checkFolderExists();
    String filePath = notNull(folder).getCanonicalPath() + "/" + location;
    File fileDirectory = new File(filePath.substring(0, filePath.lastIndexOf("/")));
    fileDirectory.mkdirs();
    return new FileOutputStream(filePath);
  }

  @Override
  public InputStream readFile(String location) throws IOException {
    checkFolderExists();
    return new FileInputStream(notNull(folder).getCanonicalPath() + "/" + location);
  }

  @Override
  public File getFolder() {
    return checkNotNull(folder);
  }

  @Override
  public File getFile(String location) throws IOException {
    checkFolderExists();
    File file = new File(notNull(folder).getCanonicalPath() + "/" + location);

    if (!file.exists()) {
      throw new FileNotFoundException("Could not find file named '"
          + location
          + "' within temporary folder.");
    }

    return file;
  }

  private void removeChildren(File parent) {
    File[] files = parent.listFiles();
    if (files != null) {
      for (File child : files) {
        removeChildren(child);
      }
    }
    parent.delete();
  }

  private void checkFolderExists() {
    if (folder == null) {
      throw new IllegalStateException(
          "Expected temporary folder to exist. Did you forget to call create()?");
    }
  }
}
