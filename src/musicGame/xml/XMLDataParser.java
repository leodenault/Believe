package musicGame.xml;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLParser;

public class XMLDataParser {
	private String file;
	private ChildDef schema;
	private XMLNodeFactory factory;
	
	public XMLDataParser(String file, ChildDef schema) {
		this.file = file;
		this.schema = schema;
		this.factory = XMLNodeFactory.getIntance();
	}
	
	protected XMLDataParser() {}
	
	public XMLNode loadFile() throws SlickException, XMLLoadingException {
		XMLNode top = factory.createNode(schema);
		XMLParser parser = new XMLParser();
		XMLElement root = parser.parse(file);
		return top.fillNode(root);
	}
}
