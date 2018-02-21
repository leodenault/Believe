package believe.app.flag_parsers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class IntegerFlagParserTest {
  private IntegerFlagParser parser;

  @Before
  public void setUp() {
    parser = new IntegerFlagParser();
  }

  @Test
  public void parseFlagReturnsCorrectInteger() {
    assertThat(parser.parseFlag("10"), is(equalTo(10)));
  }

  @Test(expected = NumberFormatException.class)
  public void parseFlagThrowsNumberFormatExceptionIfNoNumberExists() {
    parser.parseFlag("");
    parser.parseFlag("asd");
    parser.parseFlag("54asd");
  }
}
