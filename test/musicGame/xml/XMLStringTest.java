package musicGame.xml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLStringTest {
	private XMLString primitive;
	
	@Mock private XMLElement element;
	@Mock private XMLElementList list;
	
	@Before
	public void setUp() {
		initMocks(this);
		primitive = new XMLString();
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowXMLLoadingExceptionWhenThereAreChildren() throws XMLLoadingException {
		when(element.getName()).thenReturn("haha");
		when(element.getChildren()).thenReturn(list);
		when(list.size()).thenReturn(2);
		primitive.fillNode(element);
	}
	
	@Test
	public void fillNodeShouldPopulateNameAndValue() throws XMLLoadingException {
		final String name = "anode";
		final String value = "cathode";
		
		when(element.getChildren()).thenReturn(list);
		when(list.size()).thenReturn(0);
		when(element.getName()).thenReturn(name);
		when(element.getContent()).thenReturn(value);
		
		primitive.fillNode(element);
		assertThat(primitive.name, is(name));
		assertThat(primitive.value, is(value));
	}

}
