package musicGame.xml;

import org.newdawn.slick.util.xml.XMLElement;

public interface XMLNode {
	<T extends XMLNode> T fillNode(XMLElement element) throws XMLLoadingException;
}
