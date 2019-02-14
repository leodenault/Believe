package believe.testing.temporaryfolder;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.newdawn.slick.util.Log;

import java.io.IOException;

/**
 * JUnit extension for creating a temporary directory used as the root for testing file creation.
 */
public final class TemporaryFolderExtension implements ParameterResolver, AfterEachCallback {
  private static final String DEFAULT_DIRECTORY_LOCATION = "test_data";

  private final String directoryLocation;
  private final TemporaryFolderManager temporaryFolderManager;

  public TemporaryFolderExtension() {
    this(DEFAULT_DIRECTORY_LOCATION, new TemporaryFolderManagerImpl());
  }

  TemporaryFolderExtension(
      String directoryLocation,
      TemporaryFolderManager temporaryFolderManager) {
    this.directoryLocation = directoryLocation;
    this.temporaryFolderManager = temporaryFolderManager;
  }

  @Override
  public void afterEach(ExtensionContext context) {
    temporaryFolderManager.cleanUp();
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
      return temporaryFolderManager.create(directoryLocation);
    } catch (IOException e) {
      Log.error("Could not create temporary folder '" + directoryLocation + "'.", e);
    }
    return null;
  }
}
