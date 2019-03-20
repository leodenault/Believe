package believe.logging.testing;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for unit tests for handling {@link VerifiableLogSystem} objects.
 */
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(VerifiableLogSystemExtension.class)
@Target(ElementType.METHOD)
public @interface VerifiesLoggingCalls {}
