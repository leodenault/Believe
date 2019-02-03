package believe.logging.truth;

import believe.logging.testing.LogMessage;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import com.google.common.truth.ExpectFailure;
import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link LogMessageListSubject}.
 */
public final class LogMessageListSubjectTest {
  private final List<LogMessage> logMessageList = new ArrayList<>();

  @Test
  public void hasPattern_messageListContainsPattern_succeeds() {
    logMessages(LogMessage.newBuilder(LogSeverity.ERROR).setMessage("a logged message").build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setMessage("another logged message").build());

    LogMessageListSubject.withMessages(logMessageList, 2).hasPattern("a.* logged message");
  }

  @Test
  public void hasPattern_messageListDoesNotContainCorrectNumberOfPattern_fails() {
    logMessages(LogMessage.newBuilder(LogSeverity.ERROR).setMessage("a logged message").build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setMessage("not really logged").build());

    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting.that(logMessageList).hasPattern("not .+ logged"),
            2);

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("2");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("1");
  }

  @Test
  public void hasSeverity_messageListContainsSeverity_succeeds() {
    logMessages(LogMessage.newBuilder(LogSeverity.ERROR).setMessage("a logged message").build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setMessage("another logged message").build());

    LogMessageListSubject.withMessages(logMessageList, 1).hasSeverity(LogSeverity.DEBUG);
  }

  @Test
  public void hasSeverity_messageListDoesNotContainCorrectNumberOfSeverity_fails() {
    logMessages(LogMessage.newBuilder(LogSeverity.ERROR).setMessage("a logged message").build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setMessage("another logged message").build());

    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting.that(logMessageList).hasSeverity(LogSeverity.INFO),
            1);

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("1");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("0");
  }

  @Test
  public void hasThrowable_messageListContainsThrowable_succeeds() {
    logMessages(
        LogMessage.newBuilder(LogSeverity.ERROR).setThrowable(new RuntimeException()).build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setThrowable(new IllegalAccessError()).build());

    LogMessageListSubject.withMessages(logMessageList, 1).hasThrowable(RuntimeException.class);
  }

  @Test
  public void hasThrowable_messageListDoesNotContainCorrectNumberOfThrowable_fails() {
    logMessages(
        LogMessage.newBuilder(LogSeverity.ERROR).setThrowable(new RuntimeException()).build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setThrowable(new IllegalAccessError()).build());

    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting
            .that(logMessageList)
            .hasThrowable(IllegalArgumentException.class), 1);

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("1");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("0");
  }

  @Test
  public void subjectCreatedWithZeroExpectedMessages_messagesDoNotExist_assertionsPass() {
    logMessages(
        LogMessage.newBuilder(LogSeverity.ERROR).setThrowable(new RuntimeException()).build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setMessage("not really logged").build());

    LogMessageListSubject
        .withMessages(logMessageList, 0)
        .hasThrowable(IllegalArgumentException.class);
    LogMessageListSubject.withMessages(logMessageList, 0).hasPattern("always logged");
    LogMessageListSubject.withMessages(logMessageList, 0).hasSeverity(LogSeverity.INFO);
  }

  @Test
  public void assertionRequiresMultipleCriteria_subjectIsChainable() {
    logMessages(
        LogMessage.newBuilder(LogSeverity.ERROR).setThrowable(new RuntimeException()).build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setThrowable(new RuntimeException()).build(),
        LogMessage.newBuilder(LogSeverity.DEBUG).setMessage("another message").build());

    LogMessageListSubject
        .withMessages(logMessageList, 1)
        .hasThrowable(RuntimeException.class)
        .hasSeverity(LogSeverity.DEBUG);
    LogMessageListSubject
        .withMessages(logMessageList, 1)
        .hasSeverity(LogSeverity.ERROR)
        .hasThrowable(RuntimeException.class);
    LogMessageListSubject
        .withMessages(logMessageList, 1)
        .hasPattern("another message")
        .hasSeverity(LogSeverity.DEBUG);
  }

  private AssertionError expectFailure(
      SimpleSubjectBuilderCallback<LogMessageListSubject, List<LogMessage>> callback,
      int numExpectedMessages) {
    return ExpectFailure.expectFailureAbout(LogMessageListSubject.logMessageLists(
        numExpectedMessages), callback);
  }

  private void logMessages(LogMessage... messages) {
    logMessageList.addAll(Arrays.asList(messages));
  }
}
