package musicGame.xml;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLParser;

public class XMLDataParser {
	private String file;
	private ChildDef schema;
	private XMLNodeFactory factory;
	private XMLElement root;
	
	public XMLDataParser(String file, ChildDef schema) {
		this.file = file;
		this.schema = schema;
		this.factory = XMLNodeFactory.getIntance();
	}
	
	protected XMLDataParser(XMLElement root, ChildDef schema) {
		this("", schema);
		this.root = root;
	}
	
	public <T extends XMLNode> T loadFile() throws SlickException, XMLLoadingException {
		T top = factory.createNode(schema);
		XMLParser parser = new XMLParser();
		
		if (root == null) {
			root = parser.parse(file);
		}
		
		return top.fillNode(root);
	}
}
