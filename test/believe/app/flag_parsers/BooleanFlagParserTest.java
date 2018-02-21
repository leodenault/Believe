package believe.app.flag_parsers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BooleanFlagParserTest {
  private BooleanFlagParser parser;

  @Before
  public void setUp() {
    parser = new BooleanFlagParser();
  }

  @Test
  public void parseFlagReturnsTrue() {
    assertThat(parser.parseFlag(""), is(equalTo(true)));
    assertThat(parser.parseFlag("something"), is(equalTo(true)));
  }
}
