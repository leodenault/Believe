package believe.logging.truth;

import static com.google.common.truth.Truth.assertAbout;

import believe.logging.testing.LogMessage;
import believe.logging.testing.VerifiableLogSystem;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.StandardSubjectBuilder;
import com.google.common.truth.Subject;

import java.util.List;

/**
 * Makes assertions about a {@link VerifiableLogSystem}.
 */
public final class VerifiableLogSystemSubject
    extends Subject<VerifiableLogSystemSubject, VerifiableLogSystem> {
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
  public LogMessageListSubject loggedMessageThat() {
    return logged(1).messagesThat();
  }

  /**
   * Begins asserting that a no log message was logged with whatever properties are to be specified
   * in future calls.
   */
  public LogMessageListSubject neverLoggedMessageThat() {
    return logged(0).messagesThat();
  }

  /**
   * Begins asserting the {@code expectedNumMessages} log messages were logged with whatever
   * properties are to be specified in future calls.
   *
   * @param numExpectedMessages the number of log messages expected to be logged.
   */
  public LogMessageListSubjectDelegator logged(int numExpectedMessages) {
    return new LogMessageListSubjectDelegator(actual().getLogMessages(),
        check(),
        numExpectedMessages);
  }
}
