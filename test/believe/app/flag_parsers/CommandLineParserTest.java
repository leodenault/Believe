package believe.app.flag_parsers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import java.util.UnknownFormatFlagsException;

public class CommandLineParserTest {
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

  @Test(expected = IllegalArgumentException.class)
  public void parseThrowsIllegalArgumentExceptionWhenClazzIsNotInterface() {
    CommandLineParser.parse(NotAnInterface.class, new String[]{});
  }

  @Test(expected = UnknownFormatFlagsException.class)
  public void parseThrowsUnknownFormatFlagsExceptionWhenFlagNotPrecededWithDashes() {
    CommandLineParser.parse(TestFlags.class, new String[]{"integer_flag", "256"});
  }

  @Test(expected = IllegalStateException.class)
  public void parseThrowsIllegalStateExceptionWhenNameIsEmpty() {
    CommandLineParser.parse(NamelessFlags.class, new String[]{});
  }

  @Test(expected = UnknownFormatFlagsException.class)
  public void parseThrowsUnknownFormatFlagsExceptionWhenFlagIsUnkown() {
    CommandLineParser.parse(TestFlags.class, new String[]{"--unkown_flag"});
  }

  @Test
  public void parseCorrectlyParsesIntegers() {
    assertThat(CommandLineParser
        .parse(TestFlags.class, new String[]{"--integer_flag", "10"})
        .integerFlag(), is(equalTo(10)));
  }

  @Test
  public void parseAcceptsIntegerDefaults() {
    assertThat(CommandLineParser.parse(TestFlags.class, new String[]{}).integerFlag(),
        is(equalTo(20)));
    assertThat(CommandLineParser.parse(TestFlags.class, new String[]{}).integerFlagDefault(),
        is(equalTo(0)));
  }

  @Test(expected = IllegalStateException.class)
  public void parseIntegerRejectsNonIntegerMethods() {
    CommandLineParser.parse(NotAnIntegerFlags.class, new String[]{});
  }

  @Test
  public void parseCorrectlyParsesBooleans() {
    assertThat(CommandLineParser
        .parse(TestFlags.class, new String[]{"--boolean_flag"})
        .booleanFlag(), is(equalTo(true)));
  }

  @Test
  public void parseAcceptsBooleanDefaults() {
    assertThat(CommandLineParser.parse(TestFlags.class, new String[]{}).booleanFlag(),
        is(equalTo(true)));
    assertThat(CommandLineParser.parse(TestFlags.class, new String[]{}).booleanFlagDefault(),
        is(equalTo(false)));
  }

  @Test(expected = IllegalStateException.class)
  public void parseBooleanRejectsNonBooleanMethods() {
    CommandLineParser.parse(NotABooleanFlags.class, new String[]{});
  }

  @Test
  public void parseAllowsBooleansToBeFollowed() {
    TestFlags flags = CommandLineParser.parse(TestFlags.class,
        new String[]{"--integer_flag", "256", "--boolean_flag", "--integer_flag_default", "652"});

    assertThat(flags.booleanFlag(), is(equalTo(true)));
    assertThat(flags.integerFlag(), is(equalTo(256)));
    assertThat(flags.integerFlagDefault(), is(equalTo(652)));
  }
}
