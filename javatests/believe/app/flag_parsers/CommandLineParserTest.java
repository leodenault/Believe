package believe.app.flag_parsers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import java.util.UnknownFormatFlagsException;

public class CommandLineParserTest {
  @Test(expected = IllegalArgumentException.class)
  public void parseThrowsIllegalArgumentExceptionWhenClazzIsNotInterface() {
    new CommandLineParser<>(NotAnInterface.class, new String[] {}).parse();
  }

  @Test(expected = UnknownFormatFlagsException.class)
  public void parseThrowsUnknownFormatFlagsExceptionWhenFlagNotPrecededWithDashes() {
    new CommandLineParser<>(TestFlags.class, new String[] {"integer_flag", "256"}).parse();
  }

  @Test(expected = IllegalStateException.class)
  public void parseThrowsIllegalStateExceptionWhenNameIsEmpty() {
    new CommandLineParser<>(NamelessFlags.class, new String[] {}).parse();
  }

  @Test(expected = UnknownFormatFlagsException.class)
  public void parseThrowsUnknownFormatFlagsExceptionWhenFlagIsUnkown() {
    new CommandLineParser<>(TestFlags.class, new String[] {"--unkown_flag"}).parse();
  }

  @Test
  public void parseCorrectlyParsesIntegers() {
    assertThat(
        new CommandLineParser<>(TestFlags.class, new String[] {"--integer_flag", "10"})
            .parse()
            .integerFlag(),
        is(equalTo(10)));
  }

  @Test
  public void parseAcceptsIntegerDefaults() {
    assertThat(
        new CommandLineParser<>(TestFlags.class, new String[] {}).parse().integerFlag(),
        is(equalTo(20)));
    assertThat(
        new CommandLineParser<>(TestFlags.class, new String[] {}).parse().integerFlagDefault(),
        is(equalTo(0)));
  }

  @Test(expected = IllegalStateException.class)
  public void parseIntegerRejectsNonIntegerMethods() {
    new CommandLineParser<>(NotAnIntegerFlags.class, new String[] {}).parse();
  }

  @Test
  public void parseCorrectlyParsesBooleans() {
    assertThat(
        new CommandLineParser<>(TestFlags.class, new String[] {"--boolean_flag"})
            .parse()
            .booleanFlag(),
        is(equalTo(true)));
  }

  @Test
  public void parseAcceptsBooleanDefaults() {
    assertThat(
        new CommandLineParser<>(TestFlags.class, new String[] {}).parse().booleanFlag(),
        is(equalTo(true)));
    assertThat(
        new CommandLineParser<>(TestFlags.class, new String[] {}).parse().booleanFlagDefault(),
        is(equalTo(false)));
  }

  @Test(expected = IllegalStateException.class)
  public void parseBooleanRejectsNonBooleanMethods() {
    new CommandLineParser<>(NotABooleanFlags.class, new String[] {}).parse();
  }

  @Test
  public void parseAllowsBooleansToBeFollowed() {
    TestFlags flags =
        new CommandLineParser<>(
                TestFlags.class,
                new String[] {
                  "--integer_flag", "256", "--boolean_flag", "--integer_flag_default", "652"
                })
            .parse();

    assertThat(flags.booleanFlag(), is(equalTo(true)));
    assertThat(flags.integerFlag(), is(equalTo(256)));
    assertThat(flags.integerFlagDefault(), is(equalTo(652)));
  }

  interface TestFlags {
    @Flag.Integer(name = "integer_flag", defaultValue = 20)
    int integerFlag();

    @Flag.Integer(name = "integer_flag_default")
    int integerFlagDefault();

    @Flag.Boolean(name = "boolean_flag", defaultValue = true)
    boolean booleanFlag();

    @Flag.Boolean(name = "boolean_flag_default")
    boolean booleanFlagDefault();
  }

  interface NamelessFlags {
    @Flag.Integer(name = "")
    int namelessFlag();
  }

  interface NotAnIntegerFlags {
    @Flag.Integer(name = "not_an_integer")
    boolean notAnIntegerFlag();
  }

  interface NotABooleanFlags {
    @Flag.Boolean(name = "not_a_boolean")
    int notABooleanFlag();
  }

  static class NotAnInterface {
    @Flag.Integer(name = "not_an_interface")
    int notAnInterface() {
      return 0;
    }
  }
}
