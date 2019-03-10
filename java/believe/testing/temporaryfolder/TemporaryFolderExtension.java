package believe.testing.temporaryfolder;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;

/**
 * JUnit extension for creating a temporary directory used as the root for testing file creation.
 */
public final class TemporaryFolderExtension implements ParameterResolver, AfterEachCallback {
  private static final String DEFAULT_DIRECTORY_LOCATION = "test_data";

  private final InstantiableTemporaryFolder temporaryFolder;

  public TemporaryFolderExtension() {
    this(new TemporaryFolderImpl(DEFAULT_DIRECTORY_LOCATION));
  }

  TemporaryFolderExtension(InstantiableTemporaryFolder temporaryFolder) {
    this.temporaryFolder = temporaryFolder;
  }

  @Override
  public void afterEach(ExtensionContext context) {
    temporaryFolder.remove();
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(TemporaryFolder.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    try {
      temporaryFolder.create();
      return temporaryFolder;
    } catch (IOException e) {
      throw new RuntimeException("Could not create temporary folder.", e);
    }
  }
}
