package believe.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLCompound implements XMLNode {

  public String name;

  private List<ChildDef> childDefs;
  private HashMap<String, ChildDef> nameTypes;
  private HashMap<String, XMLNode> values;
  private XMLNodeFactory factory;

  public XMLCompound(List<ChildDef> childDefs) {
    this(childDefs, new HashMap<String, XMLNode>());
  }

  protected XMLCompound(List<ChildDef> childDefs,
      HashMap<String, XMLNode> values) {
    this.values = values;
    this.childDefs = childDefs;
    this.factory = XMLNodeFactory.getIntance();
    processChildDefs();
  }

  @SuppressWarnings("unchecked")
  @Override
  public XMLCompound fillNode(XMLElement element) throws XMLLoadingException {
    return fillNode(element, null);
  }

  /**
   * Used for testing
   */
  protected XMLCompound fillNode(XMLElement element, XMLNode childNode) throws XMLLoadingException {
    name = element.getName();
    XMLElementList children = element.getChildren();
    List<String> processed = new LinkedList<String>();
    int numChildren = children.size();
    int numDefs = childDefs.size();

    if (numChildren != numDefs) {
      throw new XMLLoadingException(String.format("Was expecting compound node '%s' to have %d children"
          + " but had %d children instead", name, numDefs, numChildren));
    }

    for (int i = 0; i < numChildren; i++) {
      XMLElement child = children.get(i);
      String childName = child.getName();

      if (!nameTypes.keySet().contains(childName)) {
        throw new XMLLoadingException(String.format("Expected to find one of %s"
            + " in compound node '%s' but found '%s' instead",
            createNodeNameListString(nameTypes.keySet()), name, childName));
      } else if (processed.contains(childName)) {
        throw new XMLLoadingException(String.format("'%s' has already been specified"
            + " within compound node '%s'",
            childName, name));
      }

      XMLNode newNode;
      if (childNode == null) {
        newNode = factory.createNode(nameTypes.get(childName));
      } else {
        newNode = childNode;
      }

      newNode.fillNode(child);
      values.put(childName, newNode);
      processed.add(childName);
    }

    return this;
  }

  @SuppressWarnings("unchecked")
  public <T extends XMLNode> T getValue(String name) {
    XMLNode result = values.get(name);

    if (result == null) {
      throw new NullPointerException(String.format("No value exists with name '%s' for compound node '%s'", name, this.name));
    }

    return (T) result;
  }

  private void processChildDefs() {
    nameTypes = new HashMap<String, ChildDef>();
    for (ChildDef def : childDefs) {
      nameTypes.put(def.name, def);
    }
  }

  private String createNodeNameListString(Collection<String> nodes) {
    StringBuilder builder = new StringBuilder();
    for (String node : nodes) {
      builder.append("'")
        .append(node)
        .append("'")
        .append(", ");
    }
    builder.delete(builder.length() - 2, builder.length());
    return builder.toString();
  }
}
