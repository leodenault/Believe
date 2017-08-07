package musicGame.levelFlow.parsing;

import static org.hamcrest.CoreMatchers.contains;
import static org.hamcrest.CoreMatchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import musicGame.levelFlow.parsing.FlowFileToken.FlowFileTokenType;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Animation;

public class FlowFileParserTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private FlowFileParser parser;
	
	@Mock private FlowFileTokenizer tokenizer;
	@Mock private FlowComponentBuilder builder;
	@Mock private FlowFileToken token;
	@Mock private Animation animation;
	
	@Before
	public void setupParser() {
		this.parser = new FlowFileParser(this.tokenizer, this.builder);
	}
	
	@After
	public void tearDown() throws IOException {
		mockery.checking(new Expectations() {{
			oneOf(tokenizer).close();
		}});
		this.parser.close();
	}
	
	private void checkingKVBuilding(final FlowFileTokenType key, final List<String> values) throws Exception {
		mockery.checking(new Expectations() {{
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.EQUALS)));
			for (String value : values) {
				oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.ARG, value)));
			}
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.SONG)));
			oneOf(token).tokenType(); will(returnValue(key));
		}});
	}
	
	@Test
	public void parseArgumentsShouldReturnAListOfArguments() throws Exception {
		final int numArgs = 3;
		mockery.checking(new Expectations() {{
			atMost(numArgs).of(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.ARG, "some/file.png")));
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.EOF)));
		}});
		List<String> args = this.parser.parseArguments();
		assertThat(args, notNullValue());
		assertThat(args, hasSize(numArgs));
		assertThat(args, contains("some/file.png", "some/file.png", "some/file.png"));
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseKVPairShouldThrowFlowFileParserExceptionIfValueNotFollowedByEquals() throws Exception {
		mockery.checking(new Expectations() {{
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.BEGIN)));
			oneOf(token).text(); will(returnValue(FlowFileTokenType.SONG.toString()));
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseKVPairShouldThrowFlowFileParserExceptionIfEqualsNotFollowedByArg() throws Exception {
		mockery.checking(new Expectations() {{
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.EQUALS)));
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.BEGIN)));
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetTempoOnBuilderWhenGivenOne() throws Exception {
		final List<String> bpm = Arrays.asList("123");
		this.checkingKVBuilding(FlowFileTokenType.TEMPO, bpm);
		mockery.checking(new Expectations() {{
			oneOf(builder).tempo(bpm);
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetKeysOnBuilderWhenGivenOne() throws Exception {
		final List<String> keys = Arrays.asList("a s k l");
		this.checkingKVBuilding(FlowFileTokenType.KEYS, keys);
		mockery.checking(new Expectations() {{
			oneOf(builder).inputKeys(keys);
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetOffsetOnBuilderWhenGivenOne() throws Exception {
		final List<String> offset = Arrays.asList("5000");
		this.checkingKVBuilding(FlowFileTokenType.OFFSET, offset);
		mockery.checking(new Expectations() {{
			oneOf(builder).offset(offset);
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void parseKVPairShouldSetSongOnBuilderWhenGivenOne() throws Exception {
		final List<String> song = Arrays.asList("bla/bli/blou.ogg");
		this.checkingKVBuilding(FlowFileTokenType.SONG, song);
		mockery.checking(new Expectations() {{
			oneOf(builder).song(song);
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parseKVPair(token);
	}
	
	@Test
	public void isConfigurationValueShouldReturnTrueWhenGivenAConfigurationValue() {
		mockery.checking(new Expectations() {{
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.TEMPO));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.KEYS));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.OFFSET));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.SONG));
		}});
		assertThat(this.parser.isConfigurationValue(token), is(true));
		assertThat(this.parser.isConfigurationValue(token), is(true));
		assertThat(this.parser.isConfigurationValue(token), is(true));
		assertThat(this.parser.isConfigurationValue(token), is(true));
	}
	
	@Test
	public void isConfigurationValueShouldReturnFalseWhenGivenANonConfigurationValue() {
		mockery.checking(new Expectations() {{
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.ARG));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.BEGIN));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.END));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.EOF));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.EQUALS));
			oneOf(token).tokenType(); will(returnValue(FlowFileTokenType.LINE));
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
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.EQUALS, "=")));
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parse();
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseLevelShouldThrowFlowFileParserExceptionWhenEndNotPresent() throws Exception {
		mockery.checking(new Expectations() {{
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.BEGIN, "BEGIN")));
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.EOF, "EOF")));
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parse();
	}
	
	@Test
	public void parseLinesSetsBeatsOnBuilderForEachLineRead() throws Exception {
		mockery.checking(new Expectations() {{
			exactly(3).of(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.LINE, "-x-")));
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.EOF, "EOF")));
			oneOf(builder).addBeatLine("-x-", 0);
			oneOf(builder).addBeatLine("-x-", 1);
			oneOf(builder).addBeatLine("-x-", 2);
		}});
		this.parser.parseLines();
	}
	
	@Test(expected=FlowFileParserException.class)
	public void parseThrowsFlowFileParserExceptionWhenEOFReachedButTokensAreLeft() throws Exception {
		mockery.checking(new Expectations() {{
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.BEGIN, "BEGIN")));
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.END, "END")));
			oneOf(tokenizer).next(); will(returnValue(new FlowFileToken(FlowFileTokenType.LINE, "--x-")));
			oneOf(tokenizer).getLineNumber(); will(returnValue(1));
		}});
		this.parser.parse();
	}
}
