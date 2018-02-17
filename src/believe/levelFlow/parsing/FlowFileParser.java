package believe.levelFlow.parsing;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import believe.levelFlow.parsing.FlowFileToken.FlowFileTokenType;
import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;

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

  public void parse() throws IOException, FlowFileParserException, FlowComponentBuilderException {
    this.currentToken = this.tokenizer.next();
    this.parseConfig();
    this.parseLevel();
    if (this.tokenizer.next().tokenType() != FlowFileTokenType.EOF) {
      throw new FlowFileParserException(
          String.format("Unreachable lines found at line %d", this.tokenizer.getLineNumber()));
    }
  }

  protected void parseConfig()
      throws IOException, FlowFileParserException, FlowComponentBuilderException {
    while (this.isConfigurationValue(this.currentToken)) {
      this.parseKVPair(this.currentToken);
    }
  }

  protected void parseLevel() throws FlowFileParserException, IOException, FlowComponentBuilderException {
    if (this.currentToken.tokenType() != FlowFileTokenType.BEGIN) {
      throw new FlowFileParserException(String.format("Was expecting [%s] but got [%s] at line %d",
          FlowFileTokenType.BEGIN, this.currentToken.text(), this.tokenizer.getLineNumber()));
    }
    this.parseLines();
    if (this.currentToken.tokenType() != FlowFileTokenType.END) {
      throw new FlowFileParserException(String.format("Was expecting [%s] but got [%s] at line %d",
          FlowFileTokenType.END, this.currentToken.text(), this.tokenizer.getLineNumber()));
    }
  }

  protected void parseKVPair(FlowFileToken configValue)
      throws IOException, FlowFileParserException, FlowComponentBuilderException {
    int line = this.tokenizer.getLineNumber();
    this.currentToken = this.tokenizer.next();
    if (this.currentToken.tokenType() != FlowFileTokenType.EQUALS) {
      throw new FlowFileParserException(String.format("Was expecting [%s] after [%s] at line %d",
          "=", configValue.text(), line));
    }
    List<String> arguments = this.parseArguments();
    if (arguments.isEmpty()) {
      throw new FlowFileParserException(String.format("Was expecting %s after [%s] at line %d",
          "arguments", "=", line));
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

  protected void parseLines()
      throws IOException, FlowFileParserException, FlowComponentBuilderException {
    int flowLineNumber = 0; // The index of the beat line. Doesn't matter how the lines are placed.
    while ((this.currentToken = this.tokenizer.next()).tokenType() == FlowFileTokenType.LINE) {
      this.builder.addBeatLine(this.currentToken.text(), flowLineNumber);
      flowLineNumber++;
    }
  }

  protected boolean isConfigurationValue(FlowFileToken token) {
    switch (token.tokenType()) {
      case SUBDIVISION_IMAGES:
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
      throws FlowFileParserException, FlowComponentBuilderException {
    switch(key.tokenType()) {
      case SUBDIVISION_IMAGES:
        this.builder.subdivisionImages(value);
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
