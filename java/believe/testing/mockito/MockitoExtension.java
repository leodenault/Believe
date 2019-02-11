package believe.testing.mockito;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockitoAnnotations;

final class MockitoExtension implements BeforeEachCallback {
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    MockitoAnnotations.initMocks(context.getRequiredTestInstance());
  }
}
