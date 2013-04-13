package musicGame.levelFlow.parsing;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import musicGame.levelFlow.parsing.FlowFileToken.FlowFileTokenType;
import musicGame.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.SlickException;

public class FlowFileParser {

	private FlowFileTokenizer tokenizer;
	private FlowComponentBuilder builder;
	private FlowFileToken currentToken;
	
	public FlowFileParser(Reader reader, FlowComponentBuilder builder) {
		this(new FlowFileTokenizer(reader), builder);
	}
	
	protected FlowFileParser(FlowFileTokenizer tokenizer, FlowComponentBuilder builder) {
		this.tokenizer = tokenizer;
		this.builder = builder;
	}
	
	public void parse() throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException {
		this.currentToken = this.tokenizer.next();
		this.parseConfig();
	}
	
	protected void parseConfig()
			throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException {
		while (this.isConfigurationValue(this.currentToken)) {
			this.parseKVPair(this.currentToken);
		}
	}
	
	protected void parseKVPair(FlowFileToken configValue)
			throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException {
		this.currentToken = this.tokenizer.next();
		if (this.currentToken.tokenType() != FlowFileTokenType.EQUALS) {
			throw new FlowFileParserException(String.format("Was expecting [%s] after [%s]", "=", configValue.text()));
		}
		List<String> arguments = this.parseArguments();
		if (arguments.isEmpty()) {
			throw new FlowFileParserException(String.format("Was expecting %s after [%s]", "arguments", "="));
		}
		this.setConfigurationValue(configValue, arguments);
	}
	
	protected List<String> parseArguments() throws IOException {
		LinkedList<String> args = new LinkedList<String>();
		while ((this.currentToken = this.tokenizer.next()).tokenType() == FlowFileTokenType.ARG) {
			args.add(this.currentToken.text());
		}
		return args;
	}
	
	protected boolean isConfigurationValue(FlowFileToken token) {
		switch (token.tokenType()) {
			case TOP_BAR_IMAGE:
			case TEMPO:
			case KEYS:
			case OFFSET:
			case SONG:
				return true;
			default:
				return false;
		}
	}
	
	private void setConfigurationValue(FlowFileToken key, List<String> value)
			throws FlowFileParserException, SlickException, FlowComponentBuilderException {
		switch(key.tokenType()) {
			case TOP_BAR_IMAGE:
				this.builder.topBarImage(value);
				return;
			case TEMPO:
				this.builder.tempo(value);
				return;
			case KEYS:
				this.builder.inputKeys(value);
				return;
			case OFFSET:
				this.builder.offset(value);
				return;
			case SONG:
				this.builder.song(value);
				return;
			default:
				return;
		}
	}
	
	public void close() throws IOException {
		this.tokenizer.close();
	}
}
