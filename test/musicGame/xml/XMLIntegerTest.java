package musicGame.xml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLIntegerTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private XMLInteger primitive;
	
	@Mock private XMLElement element;
	@Mock private XMLElementList list;
	
	@Before
	public void setUp() {
		primitive = new XMLInteger();
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowXMLLoadingExceptionWhenThereAreChildren() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(element).getName(); will(returnValue("haha"));
			oneOf(element).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(2));
		}});
		primitive.fillNode(element);
	}
	
	@Test(expected=NumberFormatException.class)
	public void fillNodeShouldThrowExceptionWhenValueIsntInt() throws XMLLoadingException {
		final String name = "anode";
		final String stringValue = "123asna!@$";
		
		mockery.checking(new Expectations() {{
			oneOf(element).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(0));
			oneOf(element).getName(); will(returnValue(name));
			oneOf(element).getContent(); will(returnValue(stringValue));
		}});
		
		primitive.fillNode(element);
	}
	
	@Test
	public void fillNodeShouldPopulateNameAndValue() throws XMLLoadingException {
		final String name = "anode";
		final String stringValue = "654";
		final int value = 654;
		
		mockery.checking(new Expectations() {{
			oneOf(element).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(0));
			oneOf(element).getName(); will(returnValue(name));
			oneOf(element).getContent(); will(returnValue(stringValue));
		}});
		
		primitive.fillNode(element);
		assertThat(primitive.name, is(name));
		assertThat(primitive.value, is(value));
	}

}
