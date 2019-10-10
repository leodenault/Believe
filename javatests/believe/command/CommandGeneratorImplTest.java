package believe.command;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static com.google.common.truth.Truth8.assertThat;

import believe.logging.testing.VerifiableLogSystem;
import believe.logging.testing.VerifiableLogSystem.LogSeverity;
import believe.logging.testing.VerifiesLoggingCalls;
import believe.logging.truth.VerifiableLogSystemSubject;
import believe.core.PropertyProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/** Unit tests for {@link CommandGeneratorImpl}. */
final class CommandGeneratorImplTest {
  private static final Command COMMAND = CommandGeneratorImplTest::doNothing;
  private static final PropertyProvider PROPERTY_PROVIDER =
      CommandGeneratorImplTest::provideProperty;

  private CommandGeneratorImpl commandGeneratorImpl;

  @BeforeEach
  void setUp() {
    commandGeneratorImpl =
        new CommandGeneratorImpl(
            hashMapOf(entry("valid_command", propertyProvider -> Optional.of(COMMAND))));
  }

  @Test
  void generateCommand_generatesValidCommand() {
    assertThat(commandGeneratorImpl.generateCommand("valid_command", PROPERTY_PROVIDER))
        .hasValue(COMMAND);
  }

  @Test
  @VerifiesLoggingCalls
  void generateCommand_commandCannotBeFound_returnsEmptyAndLogsError(
      VerifiableLogSystem logSystem) {
    assertThat(commandGeneratorImpl.generateCommand("invalid_command", PROPERTY_PROVIDER))
        .isEmpty();
    VerifiableLogSystemSubject.assertThat(logSystem)
        .loggedAtLeastOneMessageThat()
        .hasPattern(".*'invalid_command' is not recognized.*")
        .hasSeverity(LogSeverity.ERROR);
  }

  private static Optional<String> provideProperty(String key) {
    return Optional.empty();
  }

  private static void doNothing() {}
}
