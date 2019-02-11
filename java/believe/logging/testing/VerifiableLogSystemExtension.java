package believe.logging.testing;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.newdawn.slick.util.Log;

/**
 * JUnit test rule for gaining access to a {@link VerifiableLogSystem} when needing to run
 * assertions on log messages that were logged using Slick's logging mechanism.
 */
public final class VerifiableLogSystemExtension implements ParameterResolver {

  private final VerifiableLogSystem logSystem;

  public VerifiableLogSystemExtension() {
    logSystem = new VerifiableLogSystem();
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(VerifiableLogSystem.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    Log.setLogSystem(logSystem);
    return logSystem;
  }
}
