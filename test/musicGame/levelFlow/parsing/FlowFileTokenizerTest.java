package musicGame.levelFlow.parsing;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;

import musicGame.levelFlow.parsing.FlowFileToken.FlowFileTokenType;

import org.hamcrest.Matchers;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class FlowFileTokenizerTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery()  {{ 
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};;

	private FlowFileTokenizer tokenizer;

	@After
	public void tearDown() throws IOException {
		this.tokenizer.close();
	}
	
	@Test
	public void nextShouldSkipWhitespace() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader(" \n\t "));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.EOF));
	}
	
	@Test
	public void nextShouldNormalizeInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("tOpBaRiMaGe"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.TOP_BAR_IMAGE));
	}
	
	@Test
	public void nextShouldReturnEOFTokenWhenAtEndOfFile() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader(""));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.EOF));
	}
	
	@Test
	public void nextShouldReturnTopBarImageTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("topbarimage"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.TOP_BAR_IMAGE));
	}
	
	@Test
	public void nextShouldReturnSongTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("song"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.SONG));
	}
	
	@Test
	public void nextShouldReturnKeysTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("keys"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.KEYS));
	}
	
	@Test
	public void nextShouldReturnTempoTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("tempo"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.TEMPO));
	}
	
	@Test
	public void nextShouldReturnOffsetTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("offset"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.OFFSET));
	}
	
	@Test
	public void nextShouldReturnSubdivisionImageTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("subdivisionimages"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.SUBDIVISION_IMAGES));
	}
	
	@Test
	public void nextShouldReturnEqualsTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("="));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.EQUALS));
	}
	
	@Test
	public void nextShouldReturnArgTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("some/arg/thing.png"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.ARG));
	}
	
	@Test
	public void nextShouldReturnBeginTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("begin"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.BEGIN));
	}
	
	@Test
	public void nextShouldReturnEndTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("end"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.END));
	}
	
	@Test
	public void nextShouldReturnLineTokenWhenGivenAsInput() throws Exception {
		this.tokenizer = new FlowFileTokenizer(new StringReader("--x"));
		assertThat(this.tokenizer.next().tokenType(), is(FlowFileTokenType.LINE));
	}
	
	@Test
	public void nextShouldSetTokenTextForArgToInputText() throws Exception {
		String tokenText = "blou/bli/bla.png";
		this.tokenizer = new FlowFileTokenizer(new StringReader(tokenText));
		assertThat(this.tokenizer.next().text(), is(tokenText));
	}
	
	@Test
	public void nextShouldSetTokenTextForEqualsToEqualsWord() throws Exception {
		String tokenText = "=";
		this.tokenizer = new FlowFileTokenizer(new StringReader(tokenText));
		assertThat(this.tokenizer.next().text(), is(FlowFileTokenizer.EQUALS_WORD));
	}
	
	@Test
	public void nextShouldSetTokenTextForLineToInputText() throws Exception {
		String tokenText = "--x-";
		this.tokenizer = new FlowFileTokenizer(new StringReader(tokenText));
		assertThat(this.tokenizer.next().text(), is(tokenText));
	}
	
	@Test
	public void nextShouldSetTokenTextForConfigValueToEnumName() throws Exception {
		StringBuilder builder = new StringBuilder();
		String[] tokens = new String[] { FlowFileTokenizer.TOP_BAR_IMAGE_WORD, FlowFileTokenizer.BEGIN_WORD,
				FlowFileTokenizer.END_WORD, FlowFileTokenizer.KEYS_WORD, FlowFileTokenizer.OFFSET_WORD,
				FlowFileTokenizer.SONG_WORD, FlowFileTokenizer.SUBDIVISION_IMAGES_WORD, FlowFileTokenizer.TEMPO_WORD };
		for (String token : tokens) {
			builder.append(token);
			builder.append(" ");
		}
		String tokenText = builder.toString();
		this.tokenizer = new FlowFileTokenizer(new StringReader(tokenText));
		FlowFileToken currentToken;
		FlowFileTokenType[] tokenTypes = FlowFileTokenType.values();
		String[] typeNames = new String[tokenTypes.length];
		for (int i = 0; i < tokenTypes.length; i++) {
			typeNames[i] = tokenTypes[i].toString();
		}
		while ((currentToken = this.tokenizer.next()).tokenType() != FlowFileTokenType.EOF) {
			assertThat(currentToken.text(), isIn(typeNames));
		}
	}
	
	@Test
	public void nextShouldSetTokenTextForLine() throws Exception {
		String tokenText = "-x--x";
		this.tokenizer = new FlowFileTokenizer(new StringReader(tokenText));
		assertThat(this.tokenizer.next().text(), is(tokenText));
	}
	
	@Test
	public void nextShouldNotNormalizeArg() throws Exception {
		String tokenText = "tEsT.pNg";
		this.tokenizer = new FlowFileTokenizer(new StringReader(tokenText));
		assertThat(this.tokenizer.next().text(), is(tokenText));
	}
}