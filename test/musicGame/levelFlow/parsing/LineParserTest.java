package musicGame.levelFlow.parsing;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.util.NoSuchElementException;

import musicGame.levelFlow.parsing.exceptions.FlowFileParserException;

import org.junit.Test;


public class LineParserTest {
	
	private LineParser parser;
	
	@Test
	public void nextShouldReturnFalseForDashes() throws Exception {
		this.parser = new LineParser(new StringReader("---"));
		assertThat(this.parser.next(), is(false));
		assertThat(this.parser.next(), is(false));
		assertThat(this.parser.next(), is(false));
	}
	
	@Test
	public void nextShouldReturnTrueForXs() throws Exception {
		this.parser = new LineParser(new StringReader("xxxx"));
		assertThat(this.parser.next(), is(true));
		assertThat(this.parser.next(), is(true));
		assertThat(this.parser.next(), is(true));
		assertThat(this.parser.next(), is(true));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void nextShouldThrowNoSuchElementExceptionWhenAtEndOfLine() throws Exception {
		this.parser = new LineParser(new StringReader("x"));
		this.parser.next();
		this.parser.next();
	}
	
	@Test(expected=FlowFileParserException.class)
	public void nextShouldThrowFlowFileParserExceptionWhenUnkownCharacterEncountered() throws Exception {
		this.parser = new LineParser(new StringReader("u"));
		this.parser.next();
	}
	
	@Test
	public void hasNextShouldReturnTrueWhenNextElementIsAvailable() throws Exception {
		this.parser = new LineParser(new StringReader("x"));
		assertThat(this.parser.hasNext(), is(true));
	}
	
	@Test
	public void hasNextShouldReturnFalseWhenNextElementIsNotAvailable() throws Exception {
		this.parser = new LineParser(new StringReader(""));
		assertThat(this.parser.hasNext(), is(false));
	}
}
