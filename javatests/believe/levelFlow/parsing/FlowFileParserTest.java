package believe.levelFlow.parsing;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Animation;

import believe.levelFlow.parsing.FlowFileToken.FlowFileTokenType;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;

public class FlowFileParserTest {
  private FlowFileParser parser;

  @Mock private FlowFileTokenizer tokenizer;
  @Mock private FlowComponentBuilder builder;
  @Mock private FlowFileToken token;
  @Mock private Animation animation;

  @Before
  public void setupParser() {
    initMocks(this);
    this.parser = new FlowFileParser(this.tokenizer, this.builder);
  }

  @After
  public void tearDown() throws IOException {
    this.parser.close();
  }

  private void checkingKVBuilding(final FlowFileTokenType key, final List<String> values) throws Exception {
    FlowFileToken[] tokens = Stream.concat(
      values.stream().map(value -> new FlowFileToken(FlowFileTokenType.ARG, value)),
      Stream.of(new FlowFileToken(FlowFileTokenType.SONG)))
      .toArray(FlowFileToken[]::new);
    when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EQUALS), tokens);
    when(token.tokenType()).thenReturn(key);
  }

  @Test
  public void parseArgumentsShouldReturnAListOfArguments() throws Exception {
    when(tokenizer.next())
      .thenReturn(
        new FlowFileToken(FlowFileTokenType.ARG, "some/file.png"),
        new FlowFileToken(FlowFileTokenType.ARG, "some/file.png"),
        new FlowFileToken(FlowFileTokenType.ARG, "some/file.png"),
        new FlowFileToken(FlowFileTokenType.EOF));
    List<String> args = this.parser.parseArguments();
    assertThat(args, notNullValue());
    assertThat(args, hasSize(3));
    assertThat(args, contains("some/file.png", "some/file.png", "some/file.png"));
  }

  @Test(expected=FlowFileParserException.class)
  public void parseKVPairShouldThrowFlowFileParserExceptionIfValueNotFollowedByEquals() throws Exception {
    when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.BEGIN));
    when(token.text()).thenReturn(FlowFileTokenType.SONG.toString());
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parseKVPair(token);
  }

  @Test(expected=FlowFileParserException.class)
  public void parseKVPairShouldThrowFlowFileParserExceptionIfEqualsNotFollowedByArg() throws Exception {
    when(tokenizer.next()).thenReturn(
        new FlowFileToken(FlowFileTokenType.EQUALS),
        new FlowFileToken(FlowFileTokenType.BEGIN));
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parseKVPair(token);
  }

  @Test
  public void parseKVPairShouldSetTempoOnBuilderWhenGivenOne() throws Exception {
    final List<String> bpm = Arrays.asList("123");
    this.checkingKVBuilding(FlowFileTokenType.TEMPO, bpm);
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parseKVPair(token);
  }

  @Test
  public void parseKVPairShouldSetKeysOnBuilderWhenGivenOne() throws Exception {
    final List<String> keys = Arrays.asList("a s k l");
    this.checkingKVBuilding(FlowFileTokenType.KEYS, keys);
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parseKVPair(token);
  }

  @Test
  public void parseKVPairShouldSetOffsetOnBuilderWhenGivenOne() throws Exception {
    final List<String> offset = Arrays.asList("5000");
    this.checkingKVBuilding(FlowFileTokenType.OFFSET, offset);
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parseKVPair(token);
  }

  @Test
  public void parseKVPairShouldSetSongOnBuilderWhenGivenOne() throws Exception {
    final List<String> song = Arrays.asList("bla/bli/blou.ogg");
    this.checkingKVBuilding(FlowFileTokenType.SONG, song);
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parseKVPair(token);
  }

  @Test
  public void isConfigurationValueShouldReturnTrueWhenGivenAConfigurationValue() {
    when(token.tokenType()).thenReturn(
        FlowFileTokenType.TEMPO,
        FlowFileTokenType.KEYS,
        FlowFileTokenType.OFFSET,
        FlowFileTokenType.SONG);
    assertThat(this.parser.isConfigurationValue(token), is(true));
    assertThat(this.parser.isConfigurationValue(token), is(true));
    assertThat(this.parser.isConfigurationValue(token), is(true));
    assertThat(this.parser.isConfigurationValue(token), is(true));
  }

  @Test
  public void isConfigurationValueShouldReturnFalseWhenGivenANonConfigurationValue() {
    when(token.tokenType()).thenReturn(
        FlowFileTokenType.ARG,
        FlowFileTokenType.BEGIN,
        FlowFileTokenType.END,
        FlowFileTokenType.EOF,
        FlowFileTokenType.EQUALS,
        FlowFileTokenType.LINE);
    assertThat(this.parser.isConfigurationValue(token), is(false));
    assertThat(this.parser.isConfigurationValue(token), is(false));
    assertThat(this.parser.isConfigurationValue(token), is(false));
    assertThat(this.parser.isConfigurationValue(token), is(false));
    assertThat(this.parser.isConfigurationValue(token), is(false));
    assertThat(this.parser.isConfigurationValue(token), is(false));
  }

  @Test(expected=FlowFileParserException.class)
  public void parseShouldThrowFlowFileParserExceptionWhenBeginNotPresent() throws Exception {
    when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EQUALS, "="));
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parse();
  }

  @Test(expected=FlowFileParserException.class)
  public void parseLevelShouldThrowFlowFileParserExceptionWhenEndNotPresent() throws Exception {
    when(tokenizer.next()).thenReturn(
        new FlowFileToken(FlowFileTokenType.BEGIN, "BEGIN"),
        new FlowFileToken(FlowFileTokenType.EOF, "EOF"));
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parse();
  }

  @Test
  public void parseLinesSetsBeatsOnBuilderForEachLineRead() throws Exception {
    when(tokenizer.next()).thenReturn(
        new FlowFileToken(FlowFileTokenType.LINE, "-x-"),
        new FlowFileToken(FlowFileTokenType.LINE, "-x-"),
        new FlowFileToken(FlowFileTokenType.LINE, "-x-"),
        new FlowFileToken(FlowFileTokenType.EOF, "EOF"));
    this.parser.parseLines();
  }

  @Test(expected=FlowFileParserException.class)
  public void parseThrowsFlowFileParserExceptionWhenEOFReachedButTokensAreLeft() throws Exception {
    when(tokenizer.next()).thenReturn(
        new FlowFileToken(FlowFileTokenType.BEGIN, "BEGIN"),
        new FlowFileToken(FlowFileTokenType.END, "END"),
        new FlowFileToken(FlowFileTokenType.LINE, "--x-"));
    when(tokenizer.getLineNumber()).thenReturn(1);
    this.parser.parse();
  }
}
