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

/** Unit tests for {@link CommandGenerator}. */
final class CommandGeneratorTest {
  private static final Command COMMAND = CommandGeneratorTest::doNothing;
  private static final PropertyProvider PROPERTY_PROVIDER = CommandGeneratorTest::provideProperty;

  private CommandGenerator commandGenerator;

  @BeforeEach
  void setUp() {
    commandGenerator =
        new CommandGenerator(hashMapOf(entry("valid_command", propertyProvider -> COMMAND)));
  }

  @Test
  void generateCommand_generatesValidCommand() {
    assertThat(commandGenerator.generateCommand("valid_command", PROPERTY_PROVIDER))
        .hasValue(COMMAND);
  }

  @Test
  @VerifiesLoggingCalls
  void generateCommand_commandCannotBeFound_returnsEmptyAndLogsError(
      VerifiableLogSystem logSystem) {
    assertThat(commandGenerator.generateCommand("invalid_command", PROPERTY_PROVIDER)).isEmpty();
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
