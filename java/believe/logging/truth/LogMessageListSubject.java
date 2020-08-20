package believe.logging.truth;

import static com.google.common.truth.Truth.assertAbout;

import believe.logging.testing.LogMessage;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Subject class for executing assertions on a list of {@link LogMessage} instances. */
public final class LogMessageListSubject extends Subject<LogMessageListSubject, List<LogMessage>> {
  private final int expectedNumMessages;
  private final List<LogMessage> allMessages;
  private List<LogMessage> actualMessages;

  private LogMessageListSubject(
      FailureMetadata metadata, List<LogMessage> actual, int expectedNumMessages) {
    super(metadata, actual);

    this.expectedNumMessages = expectedNumMessages;
    this.allMessages = new ArrayList<>(actual);
    this.actualMessages = new ArrayList<>(actual);
  }

  static Factory<LogMessageListSubject, List<LogMessage>> logMessageLists(int expectedNumMessages) {
    return (metadata, actual) -> new LogMessageListSubject(metadata, actual, expectedNumMessages);
  }

  static LogMessageListSubject withMessages(List<LogMessage> logMessages, int expectedNumMessages) {
    return assertAbout(logMessageLists(expectedNumMessages)).that(logMessages);
  }

  /**
   * Asserts that the set of messages that were logged contains at least the number of messages with
   * the given pattern.
   */
  public LogMessageListSubject hasPattern(String regex) {
    filterMessages(
        logMessage -> logMessage.message() != null && Pattern.matches(regex, logMessage.message()),
        "matching regex '%s'.",
        regex);
    return this;
  }

  /**
   * Asserts that the set of messages that were logged contains at least the number of messages
   * containing the exact text in {@code text}.
   */
  public LogMessageListSubject containsExactly(String text) {
    filterMessages(logMessage -> text.equals(logMessage.message()), "contains exactly '%s'.", text);
    return this;
  }

  /**
   * Asserts that the set of messages that were logged contains at least the number of messages
   * containing {@code substring}.
   */
  public LogMessageListSubject contains(String substring) {
    filterMessages(
        logMessage -> logMessage.message() != null && logMessage.message().contains(substring),
        "contains '%s'.",
        substring);
    return this;
  }

  /**
   * Asserts that the set of messages that were logged contains at least the number of expected
   * number of messages with the given pattern.
   */
  public LogMessageListSubject hasSeverity(LogSeverity severity) {
    filterMessages(
        logMessage -> logMessage.logSeverity() == severity, "having severity %s", severity.name());
    return this;
  }

  /**
   * Asserts that the set of messages that were logged contains at least the number of expected
   * number of messages with a {@link Throwable} of type {@code clazz}.
   */
  public LogMessageListSubject hasThrowable(Class<? extends Throwable> clazz) {
    filterMessages(
        logMessage -> {
          Throwable throwable = logMessage.throwable();
          return throwable != null && throwable.getClass().equals(clazz);
        },
        "having throwable of type %s",
        clazz.getName());
    return this;
  }

  private static String pluralSuffix(int number) {
    return number == 1 ? "" : "s";
  }

  private void filterMessages(
      Predicate<? super LogMessage> predicate,
      String messageCondition,
      Object... messageConditionParams) {
    actualMessages = actualMessages.stream().filter(predicate).collect(Collectors.toList());
    int numRemainingMessages = actualMessages.size();

    int numMessageParams = 4 + messageConditionParams.length;
    Object[] messageParams = new Object[numMessageParams];
    messageParams[0] = expectedNumMessages;
    messageParams[1] = pluralSuffix(expectedNumMessages);
    for (int i = 0; i < messageConditionParams.length; i++) {
      messageParams[i + 2] = messageConditionParams[i];
    }
    messageParams[numMessageParams - 2] = numRemainingMessages;
    messageParams[numMessageParams - 1] = pluralSuffix(numRemainingMessages);

    check()
        .withMessage(
            "Expected at least %s message%s "
                + messageCondition
                + ". Found %s matching message%s instead.\n\n"
                + "Logged messages:\n"
                + allMessages.stream().map(LogMessage::toString).collect(Collectors.joining("\n\n"))
                + "\n",
            messageParams)
        .that(numRemainingMessages)
        .isAtLeast(expectedNumMessages);
  }
}
