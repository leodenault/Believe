package believe.levelFlow.parsing;

import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;

import believe.levelFlow.parsing.exceptions.FlowFileParserException;

public class LineParser {

  private static int EOF = -1;

  private Reader reader;
  private int next;

  public LineParser(Reader reader) throws IOException {
    this.reader = reader;
    this.next = this.reader.read();
  }

  public boolean next() throws IOException, FlowFileParserException {
    if (this.next == EOF) {
      throw new NoSuchElementException();
    }
    else if ((char)next == FlowFileTokenizer.MARK_CHAR) {
      this.next = reader.read();
      return true;
    }
    else if ((char)next == FlowFileTokenizer.DASH_CHAR) {
      this.next = reader.read();
      return false;
    }
    throw new FlowFileParserException(
        String.format("The character [%s] cannot be recognized. Only the [%c] and [%c] characters are supported",
            (char)next, FlowFileTokenizer.DASH_CHAR, FlowFileTokenizer.MARK_CHAR));
  }

  public boolean hasNext() {
    return this.next != EOF;
  }

  public void close() throws IOException {
    this.reader.close();
  }
}
