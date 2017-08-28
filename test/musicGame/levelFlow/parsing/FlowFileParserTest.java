package musicGame.levelFlow.parsing;

import static org.hamcrest.CoreMatchers.contains;
import static org.hamcrest.CoreMatchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import musicGame.levelFlow.parsing.FlowFileToken.FlowFileTokenType;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;
import org.mockito.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Animation;

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
		mockery.checking(new Expectations() {{

		}});
		this.parser.close();
	}
	
	private void checkingKVBuilding(final FlowFileTokenType key, final List<String> values) throws Exception {
		mockery.checking(new Expectations() {{
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EQUALS));
			for (String value : values) {
				when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.ARG, value));
			}
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.SONG));
			when(token.tokenType()).thenReturn(key);
		}});
	}
	
	@Test
	public void parseArgumentsShouldReturnAListOfArguments() throws Exception {
		final int numArgs = 3;
		mockery.checking(new Expectations() {{
			atMost(numArgs).of(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.ARG, "some/file.png")));
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EOF));
		}});
		List<String> args = this.parser.parseArguments();
		assertThat(args, notNullValue());
		assertThat(args, hasSize(numArgs));
		assertThat(args, contains("some/file.png", "some/file.png", "some/file.png"));
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseKVPairShouldThrowFlowFileParserExceptionIfValueNotFollowedByEquals() throws Exception {
		mockery.checking(new Expectations() {{
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.BEGIN));
			when(token.text()).thenReturn(FlowFileTokenType.SONG.toString());
			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseKVPairShouldThrowFlowFileParserExceptionIfEqualsNotFollowedByArg() throws Exception {
		mockery.checking(new Expectations() {{
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EQUALS));
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.BEGIN));
			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetTempoOnBuilderWhenGivenOne() throws Exception {
		final List<String> bpm = Arrays.asList("123");
		this.checkingKVBuilding(FlowFileTokenType.TEMPO, bpm);
		mockery.checking(new Expectations() {{

			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetKeysOnBuilderWhenGivenOne() throws Exception {
		final List<String> keys = Arrays.asList("a s k l");
		this.checkingKVBuilding(FlowFileTokenType.KEYS, keys);
		mockery.checking(new Expectations() {{

			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetOffsetOnBuilderWhenGivenOne() throws Exception {
		final List<String> offset = Arrays.asList("5000");
		this.checkingKVBuilding(FlowFileTokenType.OFFSET, offset);
		mockery.checking(new Expectations() {{

			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetSongOnBuilderWhenGivenOne() throws Exception {
		final List<String> song = Arrays.asList("bla/bli/blou.ogg");
		this.checkingKVBuilding(FlowFileTokenType.SONG, song);
		mockery.checking(new Expectations() {{

			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void isConfigurationValueShouldReturnTrueWhenGivenAConfigurationValue() {
		mockery.checking(new Expectations() {{
			when(token.tokenType()).thenReturn(FlowFileTokenType.TEMPO);
			when(token.tokenType()).thenReturn(FlowFileTokenType.KEYS);
			when(token.tokenType()).thenReturn(FlowFileTokenType.OFFSET);
			when(token.tokenType()).thenReturn(FlowFileTokenType.SONG);
		}});
		assertThat(this.parser.isConfigurationValue(token), is(true));
		assertThat(this.parser.isConfigurationValue(token), is(true));
		assertThat(this.parser.isConfigurationValue(token), is(true));
		assertThat(this.parser.isConfigurationValue(token), is(true));
	}
	
	@Test
	public void isConfigurationValueShouldReturnFalseWhenGivenANonConfigurationValue() {
		mockery.checking(new Expectations() {{
			when(token.tokenType()).thenReturn(FlowFileTokenType.ARG);
			when(token.tokenType()).thenReturn(FlowFileTokenType.BEGIN);
			when(token.tokenType()).thenReturn(FlowFileTokenType.END);
			when(token.tokenType()).thenReturn(FlowFileTokenType.EOF);
			when(token.tokenType()).thenReturn(FlowFileTokenType.EQUALS);
			when(token.tokenType()).thenReturn(FlowFileTokenType.LINE);
		}});
		assertThat(this.parser.isConfigurationValue(token), is(false));
		assertThat(this.parser.isConfigurationValue(token), is(false));
		assertThat(this.parser.isConfigurationValue(token), is(false));
		assertThat(this.parser.isConfigurationValue(token), is(false));
		assertThat(this.parser.isConfigurationValue(token), is(false));
		assertThat(this.parser.isConfigurationValue(token), is(false));
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseShouldThrowFlowFileParserExceptionWhenBeginNotPresent() throws Exception {
		mockery.checking(new Expectations() {{
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EQUALS, "="));
			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parse();
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseLevelShouldThrowFlowFileParserExceptionWhenEndNotPresent() throws Exception {
		mockery.checking(new Expectations() {{
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.BEGIN, "BEGIN"));
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EOF, "EOF"));
			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parse();
	}
	
	@Test
	public void parseLinesSetsBeatsOnBuilderForEachLineRead() throws Exception {
		mockery.checking(new Expectations() {{
			exactly(3).of(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.LINE, "-x-")));
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.EOF, "EOF"));



		}});
		this.parser.parseLines();
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseThrowsFlowFileParserExceptionWhenEOFReachedButTokensAreLeft() throws Exception {
		mockery.checking(new Expectations() {{
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.BEGIN, "BEGIN"));
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.END, "END"));
			when(tokenizer.next()).thenReturn(new FlowFileToken(FlowFileTokenType.LINE, "--x-"));
			when(tokenizer.getLineNumber()).thenReturn(1);
		}});
		this.parser.parse();
	}
}
