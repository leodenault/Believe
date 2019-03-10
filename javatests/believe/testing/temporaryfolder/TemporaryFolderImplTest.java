package believe.testing.temporaryfolder;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link TemporaryFolderImpl}.
 */
public final class TemporaryFolderImplTest {
  private static final String FOLDER_NAME = "temp";

  private final TemporaryFolderImpl folder = new TemporaryFolderImpl(FOLDER_NAME);

  @Test
  public void create_generatesFolderInTemporaryDirectory() throws IOException {
    folder.create();

    assertThat(folder.getFolder().exists()).isTrue();
  }

  @Test
  public void create_directoryIsAlreadyCreated_doesNothingAndReturnsSameInstance()
      throws IOException {
    folder.create();
    folder.create();

    assertThat(folder.getFolder().exists()).isTrue();
  }

  @Test
  public void getFolder_returnsFolderRepresentingDirectory() throws IOException {
    folder.create();

    File fileSystemFolder = this.folder.getFolder();

    assertThat(fileSystemFolder.getName()).startsWith(FOLDER_NAME);
  }

  @Test
  public void getFolder_temporaryFolderNotCreatedYet_throwsNullPointerException() {
    assertThrows(NullPointerException.class, folder::getFolder);
  }

  @Test
  public void getFolder_temporaryFolderRemoved_returnsValidFileReference() throws IOException {
    folder.create();
    folder.remove();

    File fileSystemFolder = this.folder.getFolder();

    assertThat(fileSystemFolder.exists()).isFalse();
  }

  @Test
  public void getFile_fileExists_returnsValidFile() throws IOException {
    folder.create();
    folder.writeToFile("some_file.txt");

    assertThat(folder.getFile("some_file.txt").exists()).isTrue();
  }

  @Test
  public void getFile_fileDoesNotExist_throwsFileNotFoundException() throws IOException {
    folder.create();

    assertThrows(FileNotFoundException.class, () -> folder.getFile("some_file.txt"));
  }

  @Test
  public void writeToFile_returnsOutputStreamToFile() throws IOException {
    byte[] expectedFileContents = {1, 2, 3};
    folder.create();
    createFile("somewhere").close();

    OutputStream out = folder.writeToFile("somewhere");
    out.write(expectedFileContents);

    File[] files = folder.getFolder().listFiles();
    assertThat(files).asList().hasSize(1);
    assertThat(readFileContents(files[0])).containsExactly(1, 2, 3);
  }

  @Test
  public void writeToFile_fileDoesNotExist_createsFile() throws IOException {
    folder.create();

    folder.writeToFile("somewhere");

    assertThat(folder.getFolder().listFiles()).asList().hasSize(1);
  }

  @Test
  public void writeToFile_pathContainsIntermediateDirectories_createsDirectories()
      throws IOException {
    folder.create();

    folder.writeToFile("somewhere/over/the/rainbow");

    assertThat(folder.getFolder().listFiles()).asList().hasSize(1);
    assertThat(folder.getFolder().listFiles()[0].listFiles()).asList().hasSize(1);
    assertThat(folder.getFolder().listFiles()[0].listFiles()[0].listFiles()).asList().hasSize(1);
    assertThat(folder.getFolder().listFiles()[0].listFiles()[0].listFiles()[0].listFiles())
        .asList()
        .hasSize(1);
  }

  @Test
  public void readFile_returnsInputStreamWithFileContents() throws IOException {
    folder.create();
    OutputStream outputStream = createFile("somewhere");
    outputStream.write(new byte[]{3, 4, 5});

    List<Integer> contents = readFileContents(folder.readFile("somewhere"));

    assertThat(contents).containsExactly(3, 4, 5);
  }

  @Test
  public void readFile_fileDoesNotExist_throwsFileNotFoundException() throws IOException {
    folder.create();

    assertThrows(FileNotFoundException.class, () -> folder.readFile("nonexistent"));
  }

  @Test
  public void methodCalled_temporaryFolderDoesNotExist_throwsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> folder.writeToFile("somewhere"));
    assertThrows(IllegalStateException.class, () -> folder.readFile("somewhere"));
    assertThrows(IllegalStateException.class, folder::remove);
  }

  @Test
  public void remove_directoryExistsAndIsEmpty_removesDirectory() throws IOException {
    folder.create();

    folder.remove();

    assertThat(folder.getFolder().exists()).isFalse();
  }

  @Test
  public void remove_directoryExistsAndContainsChildren_removesDirectory() throws IOException {
    folder.create();
    folder.writeToFile("some_file.txt");
    folder.writeToFile("some_directory/some_other_file.txt");

    folder.remove();

    assertThat(folder.getFolder().exists()).isFalse();
  }

  @Test
  public void remove_directoryDoesNotExist_doesNothing() throws IOException {
    folder.create();
    folder.remove();

    folder.remove();

    assertThat(folder.getFolder().exists()).isFalse();
  }

  private OutputStream createFile(String path) throws IOException {
    return new FileOutputStream(folder.getFolder().getCanonicalPath() + "/" + path);
  }

  private List<Integer> readFileContents(InputStream inputStream) throws IOException {
    List<Integer> contents = new ArrayList<>();

    int content;
    while ((content = inputStream.read()) >= 0) {
      contents.add(content);
    }
    inputStream.close();
    return contents;
  }

  private List<Integer> readFileContents(File file) throws IOException {
    return readFileContents(new FileInputStream(file));
  }
}
