package believe.xml;

public class XMLString extends XMLPrimitive<String> {

  @Override
  protected String extractValue(String content) {
    return content;
  }
}
