package believe.logging.testing;

import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/**
 * A message that was logged with a logger.
 */
@AutoValue
public abstract class LogMessage {
  public abstract LogSeverity logSeverity();

  @Nullable
  public abstract String message();

  @Nullable
  public abstract Throwable throwable();

  public static Builder newBuilder(LogSeverity logSeverity) {
    return new AutoValue_LogMessage.Builder().setLogSeverity(logSeverity);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setLogSeverity(LogSeverity logSeverity);

    public abstract Builder setThrowable(Throwable throwable);

    public abstract Builder setMessage(String message);

    public abstract LogMessage build();
  }
}
