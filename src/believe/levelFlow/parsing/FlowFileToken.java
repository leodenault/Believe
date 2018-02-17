package believe.levelFlow.parsing;

public class FlowFileToken {

  public enum FlowFileTokenType {
    SONG,
    KEYS,
    TEMPO,
    OFFSET,
    SUBDIVISION_IMAGES,
    EQUALS,
    ARG,
    BEGIN,
    END,
    LINE,
    EOF
  }

  private FlowFileTokenType tokenType;
  private String text;

  protected FlowFileToken(FlowFileTokenType tokenType) {
    this(tokenType, "");
  }

  protected FlowFileToken(FlowFileTokenType tokenType, String text) {
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
