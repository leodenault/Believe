package believe.logging.testing;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.Mock;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.LogSystem;

import java.lang.reflect.Parameter;

/**
 * Unit tests for {@link VerifiableLogSystemExtension}.
 */
public final class VerifiableLogSystemExtensionTest {
  /**
   * No-op class for allowing tests to retrieve an instance of {@link Parameter} from its methods.
   */
  private static final class NoOpClass {
    public void noOpMethod(
        VerifiableLogSystem verifiableLogSystem, LogSystem nonVerifiableLogSystem) {}
  }

  private final VerifiableLogSystemExtension extension = new VerifiableLogSystemExtension();

  @Mock private ExtensionContext extensionContext;
  @Mock private ParameterContext parameterContext;
  @Mock private LogSystem logSystem;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void supportsParameter_parameterIsVerifiableLogger_returnsTrue() throws Exception {
    when(parameterContext.getParameter()).thenReturn(NoOpClass.class
        .getMethod("noOpMethod", VerifiableLogSystem.class, LogSystem.class)
        .getParameters()[0]);

    assertThat(extension.supportsParameter(parameterContext, extensionContext)).isTrue();
  }

  @Test
  public void supportsParameter_parameterIsVerifiableLogger_returnsFalse() throws Exception {
    when(parameterContext.getParameter()).thenReturn(NoOpClass.class
        .getMethod("noOpMethod", VerifiableLogSystem.class, LogSystem.class)
        .getParameters()[1]);

    assertThat(extension.supportsParameter(parameterContext, extensionContext)).isFalse();
  }

  @Test
  public void resolveParameter_registersLogger() {
    Log.setLogSystem(logSystem);

    extension.resolveParameter(parameterContext, extensionContext);
    Log.error("Some error.");

    verify(logSystem, never()).error(any(String.class));
  }

  @Test
  public void resolveParameter_returnsVerifiableLogger() {
    assertThat(extension.resolveParameter(parameterContext, extensionContext)).isInstanceOf(
        VerifiableLogSystem.class);
  }
}
