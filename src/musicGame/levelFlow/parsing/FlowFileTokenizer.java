package musicGame.levelFlow.parsing;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

public class FlowFileTokenizer {
	
	private static final String TOP_BAR_IMAGE_WORD = "topbarimage";
	private static final String SONG_WORD = "song";
	private static final String KEYS_WORD = "keys";
	private static final String TEMPO_WORD = "tempo";
	private static final String OFFSET_WORD = "offset";
	private static final String SUBDIVISION_IMAGES_WORD = "subdivisionimages";
	private static final String EQUALS_WORD = "=";
	private static final String COMMA_WORD = ",";
	private static final String BEGIN_WORD = "begin";
	private static final String END_WORD = "end";
	
	private static final Pattern LINE = Pattern.compile("(-|x)+");
	
	private static final int EOF = -1;

	private int currentCode;
	private Reader reader;
	
	public FlowFileTokenizer(Reader reader) {
		this.reader = reader;
	}
	
	public FlowFileToken next() throws IOException {
		if (this.fastForward()) {
			return new FlowFileToken(FlowFileTokenType.EOF);
		}
		String word = this.readWord();
		return this.createToken(word);
	}
	
	private boolean fastForward() throws IOException {
		if (this.currentCode == EOF) {
			return true;
		}
		this.skipWhitespace();
		return this.currentCode == EOF;
	}
	
	private void skipWhitespace() throws IOException {
		do {
			this.currentCode = this.reader.read();
		}
		while (Character.isWhitespace(this.currentCode));
	}
	
	private String readWord() throws IOException {
		StringBuilder wordBuilder = new StringBuilder();
		while (!atEndOfWord()) {
			wordBuilder.append((char)this.currentCode);
			this.currentCode = this.reader.read();
		}
		return wordBuilder.toString();
	}
	
	private boolean atEndOfWord() {
		return this.currentCode == EOF || Character.isWhitespace(this.currentCode);
	}
	
	private FlowFileToken createToken(String word) {
		word = word.toLowerCase();
		if (word.equals(TOP_BAR_IMAGE_WORD)) {
			return new FlowFileToken(FlowFileTokenType.TOP_BAR_IMAGE);
		}
		else if (word.equals(SONG_WORD)) {
			return new FlowFileToken(FlowFileTokenType.SONG);
		}
		else if (word.equals(KEYS_WORD)) {
			return new FlowFileToken(FlowFileTokenType.KEYS);
		}
		else if (word.equals(TEMPO_WORD)) {
			return new FlowFileToken(FlowFileTokenType.TEMPO);
		}
		else if (word.equals(OFFSET_WORD)) {
			return new FlowFileToken(FlowFileTokenType.OFFSET);
		}
		else if (word.equals(SUBDIVISION_IMAGES_WORD)) {
			return new FlowFileToken(FlowFileTokenType.SUBDIVISION_IMAGES);
		}
		else if (word.equals(EQUALS_WORD)) {
			return new FlowFileToken(FlowFileTokenType.EQUALS);
		}
		else if (word.equals(COMMA_WORD)) {
			return new FlowFileToken(FlowFileTokenType.COMMA);
		}
		else if (word.equals(BEGIN_WORD)) {
			return new FlowFileToken(FlowFileTokenType.BEGIN);
		}
		else if (word.equals(END_WORD)) {
			return new FlowFileToken(FlowFileTokenType.END);
		}
		else if (LINE.matcher(word).matches()) {
			return new FlowFileToken(FlowFileTokenType.LINE, word);
		}
		else {
			return new FlowFileToken(FlowFileTokenType.ARG, word);
		}
	}
	
	public void close() throws IOException {
		this.reader.close();
	}
	
	public enum FlowFileTokenType {
		TOP_BAR_IMAGE,
		SONG,
		KEYS,
		TEMPO,
		OFFSET,
		SUBDIVISION_IMAGES,
		EQUALS,
		COMMA,
		ARG,
		BEGIN,
		END,
		LINE,
		EOF
	}
	
	public class FlowFileToken {
		
		private FlowFileTokenType tokenType;
		private String text;
		
		private FlowFileToken(FlowFileTokenType tokenType) {
			this(tokenType, "");
		}
		
		private FlowFileToken(FlowFileTokenType tokenType, String text) {
			this.tokenType = tokenType;
			this.text = text;
		}
		
		public FlowFileTokenType tokenType() {
			return this.tokenType;
		}
		
		public String text() {
			return this.text;
		}
	}
}
