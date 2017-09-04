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

public class XMLBooleanTest {
	private XMLBoolean primitive;
	
	@Mock private XMLElement element;
	@Mock private XMLElementList list;
	
	@Before
	public void setUp() {
		initMocks(this);
		primitive = new XMLBoolean();
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowXMLLoadingExceptionWhenThereAreChildren() throws XMLLoadingException {
		when(element.getName()).thenReturn("nope");
		when(element.getChildren()).thenReturn(list);
		when(list.size()).thenReturn(2);
		primitive.fillNode(element);
	}

	@Test
	public void fillNodeShouldHaveFalseValueIfGivenFalseOrIncorrectTextBoolean() throws XMLLoadingException {
		final String name = "something";
		final String falseValue = "false";
		final String incorrectValue = "truer";
		
		when(element.getChildren()).thenReturn(list);
		when(list.size()).thenReturn(0);
		when(element.getName()).thenReturn(name);
		when(element.getContent()).thenReturn(falseValue);
		when(element.getContent()).thenReturn(incorrectValue);
		
		primitive.fillNode(element);
		assertThat(primitive.value, is(false));
		primitive.fillNode(element);
		assertThat(primitive.value, is(false));
	}
	
	@Test
	public void fillNodeShouldPopulateNameAndValue() throws XMLLoadingException {
		final String name = "mynode";
		final String stringValue = "true";
		final boolean value = true;
		
		when(element.getChildren()).thenReturn(list);
		when(list.size()).thenReturn(0);
		when(element.getName()).thenReturn(name);
		when(element.getContent()).thenReturn(stringValue);
		
		primitive.fillNode(element);
		assertThat(primitive.name, is(name));
		assertThat(primitive.value, is(value));
	}

}
