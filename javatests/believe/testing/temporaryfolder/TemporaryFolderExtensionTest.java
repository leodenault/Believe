package believe.testing.temporaryfolder;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Parameter;

/**
 * Unit tests for {@link TemporaryFolderExtension}.
 */
@InstantiateMocksIn
public final class TemporaryFolderExtensionTest {
  /**
   * No-op class for allowing tests to retrieve an instance of {@link Parameter} from its methods.
   */
  private static final class NoOpTestClass {
    public void noOpMethod(TemporaryFolder temporaryFolder, Object object) {}
  }

  @Mock private ParameterContext parameterContext;
  @Mock private ExtensionContext extensionContext;
  @Mock private InstantiableTemporaryFolder temporaryFolder;

  private TemporaryFolderExtension extension;

  @BeforeEach
  public void setUp() {
    extension = new TemporaryFolderExtension(temporaryFolder);
  }

  @Test
  public void supportsParameter_parameterTemporaryFolder_returnsTrue()
      throws NoSuchMethodException {
    when(parameterContext.getParameter()).thenReturn(NoOpTestClass.class
        .getMethod("noOpMethod", TemporaryFolder.class, Object.class)
        .getParameters()[0]);

    assertThat(extension.supportsParameter(parameterContext, extensionContext)).isTrue();
  }

  @Test
  public void supportsParameter_parameterIsNotTemporaryFolder_returnsFalse()
      throws NoSuchMethodException {
    when(parameterContext.getParameter()).thenReturn(NoOpTestClass.class
        .getMethod("noOpMethod", TemporaryFolder.class, Object.class)
        .getParameters()[1]);

    assertThat(extension.supportsParameter(parameterContext, extensionContext)).isFalse();
  }

  @Test
  public void resolveParameter_returnsTemporaryFolder() {
    assertThat(extension.resolveParameter(parameterContext, extensionContext)).isEqualTo(
        temporaryFolder);
  }

  @Test
  public void resolveParameter_exceptionIsThrown_exceptionIsBubbledUp() throws IOException {
    doThrow(new IOException()).when(temporaryFolder).create();

    Assertions.assertThrows(
        RuntimeException.class,
        () -> extension.resolveParameter(parameterContext, extensionContext));
  }

  @Test
  public void afterEach_removesDirectory() {
    extension.afterEach(extensionContext);

    verify(temporaryFolder).remove();
  }
}
