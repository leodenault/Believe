package musicGame.levelFlow.parsing;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import musicGame.levelFlow.parsing.FlowFileToken.FlowFileTokenType;

public class FlowFileTokenizer {
	
	public static final String TOP_BAR_IMAGE_WORD = "topbarimage";
	public static final String SONG_WORD = "song";
	public static final String KEYS_WORD = "keys";
	public static final String TEMPO_WORD = "tempo";
	public static final String OFFSET_WORD = "offset";
	public static final String SUBDIVISION_IMAGES_WORD = "subdivisionimages";
	public static final String EQUALS_WORD = "=";
	public static final String BEGIN_WORD = "begin";
	public static final String END_WORD = "end";
	
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
			return new FlowFileToken(FlowFileTokenType.TOP_BAR_IMAGE, FlowFileTokenType.TOP_BAR_IMAGE.toString());
		}
		else if (word.equals(SONG_WORD)) {
			return new FlowFileToken(FlowFileTokenType.SONG, FlowFileTokenType.SONG.toString());
		}
		else if (word.equals(KEYS_WORD)) {
			return new FlowFileToken(FlowFileTokenType.KEYS, FlowFileTokenType.KEYS.toString());
		}
		else if (word.equals(TEMPO_WORD)) {
			return new FlowFileToken(FlowFileTokenType.TEMPO, FlowFileTokenType.TEMPO.toString());
		}
		else if (word.equals(OFFSET_WORD)) {
			return new FlowFileToken(FlowFileTokenType.OFFSET, FlowFileTokenType.OFFSET.toString());
		}
		else if (word.equals(SUBDIVISION_IMAGES_WORD)) {
			return new FlowFileToken(FlowFileTokenType.SUBDIVISION_IMAGES, FlowFileTokenType.SUBDIVISION_IMAGES.toString());
		}
		else if (word.equals(EQUALS_WORD)) {
			return new FlowFileToken(FlowFileTokenType.EQUALS, EQUALS_WORD);
		}
		else if (word.equals(BEGIN_WORD)) {
			return new FlowFileToken(FlowFileTokenType.BEGIN, FlowFileTokenType.BEGIN.toString());
		}
		else if (word.equals(END_WORD)) {
			return new FlowFileToken(FlowFileTokenType.END, FlowFileTokenType.END.toString());
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
}
