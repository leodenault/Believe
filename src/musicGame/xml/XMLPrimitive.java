package musicGame.xml;

import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLPrimitive implements XMLNode {
	public String name;
	public String value;
	
	@Override
	public XMLPrimitive fillNode(XMLElement element) throws XMLLoadingException {
		name = element.getName();
		XMLElementList children = element.getChildren();
		
		if (children.size() != 0) {
			throw new XMLLoadingException(String.format(
					"The element named '%s' contains children when it shouldn't", name));
		}
		
		value = element.getContent();
		return this;
	}
}
