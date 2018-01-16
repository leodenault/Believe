package musicGame.xml;

public class XMLInteger extends XMLPrimitive<Integer> {

  @Override
  protected Integer extractValue(String content) {
    int intValue = Integer.parseInt(content);
    return intValue;
  }
}
