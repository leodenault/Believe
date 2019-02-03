package believe.logging.testing;

import static com.google.common.truth.Truth.assertThat;

import believe.logging.testing.LogMessage;
import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import org.junit.Test;

/**
 * Unit tests for {@link VerifiableLogSystem}.
 */
public final class VerifiableLogSystemTest {
  private final VerifiableLogSystem logSystem = new VerifiableLogSystem();

  @Test
  public void error_usingThrowable_recordsThrowableMessage() {
    Throwable throwable = new Throwable();

    logSystem.error(throwable);

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.ERROR)
        .setThrowable(throwable)
        .build());
  }

  @Test
  public void error_usingString_recordsStringMessage() {
    logSystem.error("message");

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.ERROR)
        .setMessage("message")
        .build());
  }

  @Test
  public void error_usingStringAndThrowable_recordsMessageWithStringAndThrowable() {
    Throwable throwable = new Throwable();
    logSystem.error("message", throwable);

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.ERROR)
        .setMessage("message")
        .setThrowable(throwable)
        .build());
  }

  @Test
  public void warn_usingString_recordsStringMessage() {
    logSystem.warn("message");

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.WARNING)
        .setMessage("message")
        .build());
  }

  @Test
  public void warn_usingStringAndThrowable_recordsMessageWithStringAndThrowable() {
    Throwable throwable = new Throwable();
    logSystem.warn("message", throwable);

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.WARNING)
        .setMessage("message")
        .setThrowable(throwable)
        .build());
  }

  @Test
  public void info_recordsMessage() {
    logSystem.info("message");

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.INFO)
        .setMessage("message")
        .build());
  }

  @Test
  public void debug_recordsMessage() {
    logSystem.debug("message");

    assertThat(logSystem.getLogMessages()).containsExactly(LogMessage
        .newBuilder(LogSeverity.DEBUG)
        .setMessage("message")
        .build());
  }
}
