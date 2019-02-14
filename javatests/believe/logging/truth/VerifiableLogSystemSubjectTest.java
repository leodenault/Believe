package believe.logging.truth;

import static believe.logging.truth.VerifiableLogSystemSubject.verifiableLogSystems;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;

import believe.logging.testing.VerifiableLogSystem;
import com.google.common.truth.ExpectFailure;
import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Unit tests for {@link VerifiableLogSystemSubject}.
 */
public final class VerifiableLogSystemSubjectTest {
  private final VerifiableLogSystem logSystem = new VerifiableLogSystem();

  @Test
  public void loggedAtLeastOneMessageThat_atLeastOneMessageExists_passes() {
    logSystem.error("Some error");

    VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat();
  }

  @Test
  public void loggedAtLeastOneMessageThat_noMessagesExist_fails() {
    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting.that(logSystem).loggedAtLeastOneMessageThat());

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("1");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("0");
  }

  @Test
  public void loggedAtLeast_expectedMessagesLowerThanTotalLogged_passes() {
    logSystem.error("OH NO");
    logSystem.debug("useless info");
    logSystem.info("okay, this is kind of useful");
    logSystem.warn("do not do it again or else I will warn you again");

    VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeast(3).messagesThat();
  }

  @Test
  public void loggedAtLeast_expectedMessagesGreaterThanTotalLogged_fails() {
    logSystem.error("OH NO");
    logSystem.debug("useless info");
    logSystem.info("okay, this is kind of useful");

    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting.that(logSystem).loggedAtLeast(4).messagesThat());

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("4");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("3");
  }

  @Test
  public void loggedAtLeast_onlyFailsOnMessagesThat() {
    // Make sure to omit the messagesThat() call to ensure that nothing fails before then.
    VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeast(2);
  }

  @Test
  public void neverLoggedMessageThat_messageNeverLogged_succeeds() {
    VerifiableLogSystemSubject
        .assertThat(logSystem)
        .neverLoggedMessageThat(message -> message.hasPattern("a pattern"));
  }

  @Test
  public void neverLoggedMessageThat_messageLogged_fails() {
    logSystem.warn("a pattern");

    Assertions.assertThrows(AssertionError.class,
        () -> VerifiableLogSystemSubject
            .assertThat(logSystem)
            .neverLoggedMessageThat(message -> message.hasPattern("a pattern")));
  }

  private AssertionError expectFailure(
      SimpleSubjectBuilderCallback<VerifiableLogSystemSubject, VerifiableLogSystem> callback) {
    return expectFailureAbout(verifiableLogSystems(), callback);
  }
}
