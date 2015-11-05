package musicGame.xml;

import org.newdawn.slick.util.xml.XMLElement;

public interface XMLNode {
	XMLNode fillNode(XMLElement element) throws XMLLoadingException;
}
