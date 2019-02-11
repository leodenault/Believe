package believe.testing.mockito;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Unit tests for {@link MockitoExtension}.
 */
public final class MockitoExtensionTest {
  private interface Mockable {}

  private static final class TestClass {
    @Mock Mockable mockable;
  }

  private final MockitoExtension extension = new MockitoExtension();

  @Test
  public void beforeEach_initializesMocks() throws Exception {
    ExtensionContext extensionContext = Mockito.mock(ExtensionContext.class);
    TestClass testClass = new TestClass();
    when(extensionContext.getRequiredTestInstance()).thenReturn(testClass);

    extension.beforeEach(extensionContext);

    assertThat(testClass.mockable).isNotNull();
  }
}
