package believe.logging.truth;

import static believe.logging.truth.VerifiableLogSystemSubject.verifiableLogSystems;
import static com.google.common.truth.ExpectFailure.expectFailureAbout;

import believe.logging.testing.VerifiableLogSystem;
import com.google.common.truth.ExpectFailure;
import com.google.common.truth.ExpectFailure.SimpleSubjectBuilderCallback;
import org.junit.Test;

/**
 * Unit tests for {@link VerifiableLogSystemSubject}.
 */
public final class VerifiableLogSystemSubjectTest {
  private final VerifiableLogSystem logSystem = new VerifiableLogSystem();

  @Test
  public void loggedMessageThat_atLeastOneMessageExists_passes() {
    logSystem.error("Some error");

    VerifiableLogSystemSubject.assertThat(logSystem).loggedMessageThat();
  }

  @Test
  public void loggedMessageThat_noMessagesExist_fails() {
    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting.that(logSystem).loggedMessageThat());

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("1");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("0");
  }

  @Test
  public void logged_expectedMessagesLowerThanTotalLogged_passes() {
    logSystem.error("OH NO");
    logSystem.debug("useless info");
    logSystem.info("okay, this is kind of useful");
    logSystem.warn("do not do it again or else I will warn you again");

    VerifiableLogSystemSubject.assertThat(logSystem).logged(3).messagesThat();
  }

  @Test
  public void logged_expectedMessagesGreaterThanTotalLogged_fails() {
    logSystem.error("OH NO");
    logSystem.debug("useless info");
    logSystem.info("okay, this is kind of useful");

    AssertionError
        assertionError =
        expectFailure(whenTesting -> whenTesting.that(logSystem).logged(4).messagesThat());

    ExpectFailure.assertThat(assertionError).factValue("expected to be at least").isEqualTo("4");
    ExpectFailure.assertThat(assertionError).factValue("but was").isEqualTo("3");
  }

  @Test
  public void logged_onlyFailsOnMessagesThat() {
    // Make sure to omit the messagesThat() call to ensure that nothing fails before then.
    VerifiableLogSystemSubject.assertThat(logSystem).logged(2);
  }

  private AssertionError expectFailure(
      SimpleSubjectBuilderCallback<VerifiableLogSystemSubject, VerifiableLogSystem> callback) {
    return expectFailureAbout(verifiableLogSystems(), callback);
  }
}
