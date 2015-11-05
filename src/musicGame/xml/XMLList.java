package musicGame.xml;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLList implements XMLNode {
	public String name;
	public List<XMLCompound> children;
	
	private CompoundDef childDef;
	private XMLNodeFactory factory;
	
	public XMLList(CompoundDef childDef) {
		this.childDef = childDef;
		factory = XMLNodeFactory.getIntance();
	}
	
	@Override
	public XMLList fillNode(XMLElement element) throws XMLLoadingException {
		return fillNode(element, null);
	}
	
	protected XMLList fillNode(XMLElement element, XMLCompound compound) throws XMLLoadingException {
		name = element.getName();
		XMLElementList xmlChildren = element.getChildren();
		int numChildren = xmlChildren.size();
		XMLCompound child;
		children = new LinkedList<XMLCompound>();
		
		for (int i = 0; i < numChildren; i++) {
			XMLElement xmlChild = xmlChildren.get(i);
			
			String childName = xmlChild.getName();
			if (!childName.equals(childDef.name)) {
				throw new XMLLoadingException(String.format(
						"Children of node '%s' should be named '%s' but was '%s'", name, childDef.name, childName));
			}
			
			if (compound == null) {
				child = factory.createNode(childDef);
			} else {
				child = compound;
			}
			
			children.add(child.fillNode(xmlChild));
		}
		
		return this;
	}
}
