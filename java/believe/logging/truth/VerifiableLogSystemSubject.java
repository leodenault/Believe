package believe.logging.truth;

import static com.google.common.truth.Truth.assertAbout;

import believe.logging.testing.LogMessage;
import believe.logging.testing.VerifiableLogSystem;
import com.google.common.truth.ExpectFailure;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.StandardSubjectBuilder;
import com.google.common.truth.Subject;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * Makes assertions about a {@link VerifiableLogSystem}.
 */
public final class VerifiableLogSystemSubject
    extends Subject<VerifiableLogSystemSubject, VerifiableLogSystem> {
  /**
   * Callback defining a set of assertions on a {@link LogMessageListSubject}. This is typically
   * used in determining that a particular logging call was never performed.
   */
  public interface LogMessageListSubjectCallback {
    /**
     * Runs assertions that a particular logging call was performed.
     *
     * @param loggedMessage the {@link LogMessageListSubject} used in running assertions for a
     * particular logging call.
     */
    void assertLoggingCall(LogMessageListSubject loggedMessage);
  }

  private VerifiableLogSystemSubject(
      FailureMetadata metadata, VerifiableLogSystem actual) {
    super(metadata, actual);
  }

  /**
   * Delegates verification of messages recorded by the log system so that tests may read in
   * English.
   */
  public static final class LogMessageListSubjectDelegator {
    private final List<LogMessage> logMessages;
    private final StandardSubjectBuilder subjectBuilder;
    private final int expectedNumMessages;

    LogMessageListSubjectDelegator(
        List<LogMessage> logMessages,
        StandardSubjectBuilder subjectBuilder,
        int expectedNumMessages) {
      this.logMessages = logMessages;
      this.subjectBuilder = subjectBuilder;
      this.expectedNumMessages = expectedNumMessages;
    }

    public LogMessageListSubject messagesThat() {
      int numLogMessages = logMessages.size();
      subjectBuilder
          .withMessage("Number of messages logged was different from expected.")
          .that(numLogMessages)
          .isAtLeast(expectedNumMessages);
      return LogMessageListSubject.withMessages(logMessages, expectedNumMessages);
    }
  }

  static Factory<VerifiableLogSystemSubject, VerifiableLogSystem> verifiableLogSystems() {
    return VerifiableLogSystemSubject::new;
  }

  /**
   * Runs assertions on a {@link VerifiableLogSystem}.
   */
  public static VerifiableLogSystemSubject assertThat(VerifiableLogSystem verifiableLogSystem) {
    return assertAbout(verifiableLogSystems()).that(verifiableLogSystem);
  }

  /**
   * Begins asserting that a single log message was logged with whatever properties are to be
   * specified in future calls.
   */
  public LogMessageListSubject loggedAtLeastOneMessageThat() {
    return loggedAtLeast(1).messagesThat();
  }

  /**
   * Begins asserting that at least {@code numExpectedMessages} log messages were logged with
   * whatever properties are to be specified in future calls.
   */
  public LogMessageListSubjectDelegator loggedAtLeast(int numExpectedMessages) {
    return new LogMessageListSubjectDelegator(actual().getLogMessages(),
        check(),
        numExpectedMessages);
  }

  /**
   * Begins asserting that no log message was logged with whatever properties are to be specified in
   * future calls.
   */
  public void neverLoggedMessageThat(LogMessageListSubjectCallback callback) {
    Assertions.assertThrows(AssertionError.class,
        () -> callback.assertLoggingCall(loggedAtLeastOneMessageThat()),
        "Expected no messages to be logged with given properties, but at least one was logged.");
  }
}
