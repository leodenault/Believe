package believe.testing.temporaryfolder;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiableLogSystemExtension;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  private static final String DIRECTORY_LOCATION = "/somehwere/over/the/rainbow";

  @Mock private ParameterContext parameterContext;
  @Mock private ExtensionContext extensionContext;
  @Mock private TemporaryFolderManager temporaryFolderManager;
  @Mock private TemporaryFolder temporaryFolder;

  private TemporaryFolderExtension extension;

  @BeforeEach
  public void setUp() {
    extension = new TemporaryFolderExtension(DIRECTORY_LOCATION, temporaryFolderManager);
  }

  @Test
  public void supportsParameter_parameterIsString_returnsTrue() throws NoSuchMethodException {
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
  public void resolveParameter_returnsTemporaryFolder() throws IOException {
    when(temporaryFolderManager.create(DIRECTORY_LOCATION)).thenReturn(temporaryFolder);

    assertThat(extension.resolveParameter(parameterContext, extensionContext)).isInstanceOf(
        TemporaryFolder.class);
  }

  @Test
  @ExtendWith(VerifiableLogSystemExtension.class)
  public void resolveParameter_exceptionIsThrown_logsExceptionAndReturnsNull(
      VerifiableLogSystem logSystem) throws IOException {
    when(temporaryFolderManager.create(DIRECTORY_LOCATION)).thenThrow(new IOException());

    assertThat(extension.resolveParameter(parameterContext, extensionContext)).isNull();
    VerifiableLogSystemSubject
        .assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern("Could not create temporary folder.*")
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(IOException.class);
  }

  @Test
  public void afterEach_removesDirectory() {
    extension.afterEach(extensionContext);

    verify(temporaryFolderManager).cleanUp();
  }
}
