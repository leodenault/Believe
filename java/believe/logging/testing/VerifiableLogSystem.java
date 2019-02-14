package believe.logging.testing;

import org.newdawn.slick.util.LogSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link LogSystem} implementation which can be used for verifying proper logging within tests.
 */
public final class VerifiableLogSystem implements LogSystem {
  /**
   * The severity level of the log message.
   */
  public enum LogSeverity {DEBUG, INFO, WARNING, ERROR}

  private final List<LogMessage> logMessages;

  public VerifiableLogSystem() {
    logMessages = new ArrayList<>();
  }

  public List<LogMessage> getLogMessages() {
    return new ArrayList<>(logMessages);
  }

  @Override
  public void error(String s, Throwable throwable) {
    logMessages.add(LogMessage
        .newBuilder(LogSeverity.ERROR)
        .setMessage(s)
        .setThrowable(throwable)
        .build());
  }

  @Override
  public void error(Throwable throwable) {
    logMessages.add(LogMessage.newBuilder(LogSeverity.ERROR).setThrowable(throwable).build());
  }

  @Override
  public void error(String s) {
    logMessages.add(LogMessage.newBuilder(LogSeverity.ERROR).setMessage(s).build());
  }

  @Override
  public void warn(String s) {
    logMessages.add(LogMessage.newBuilder(LogSeverity.WARNING).setMessage(s).build());
  }

  @Override
  public void warn(String s, Throwable throwable) {
    logMessages.add(LogMessage
        .newBuilder(LogSeverity.WARNING)
        .setMessage(s)
        .setThrowable(throwable)
        .build());
  }

  @Override
  public void info(String s) {
    logMessages.add(LogMessage.newBuilder(LogSeverity.INFO).setMessage(s).build());
  }

  @Override
  public void debug(String s) {
    logMessages.add(LogMessage.newBuilder(LogSeverity.DEBUG).setMessage(s).build());
  }
}
