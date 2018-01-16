package musicGame.xml;

import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public abstract class XMLPrimitive<Value> implements XMLNode {
  public String name;
  public Value value;

  @SuppressWarnings("unchecked")
  @Override
  public <T extends XMLNode> T fillNode(XMLElement element) throws XMLLoadingException {
    name = element.getName();
    XMLElementList children = element.getChildren();

    if (children.size() != 0) {
      throw new XMLLoadingException(String.format(
          "The element named '%s' contains children when it shouldn't", name));
    }

    value = extractValue(element.getContent());
    return (T) this;
  }

  protected abstract Value extractValue(String content);
}
