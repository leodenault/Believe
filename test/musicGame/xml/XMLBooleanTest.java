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

public class XMLBooleanTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
	
	private XMLBoolean primitive;
	
	@Mock private XMLElement element;
	@Mock private XMLElementList list;
	
	@Before
	public void setUp() {
		primitive = new XMLBoolean();
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowXMLLoadingExceptionWhenThereAreChildren() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(element).getName(); will(returnValue("nope"));
			oneOf(element).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(2));
		}});
		primitive.fillNode(element);
	}

	@Test
	public void fillNodeShouldHaveFalseValueIfGivenFalseOrIncorrectTextBoolean() throws XMLLoadingException {
		final String name = "something";
		final String falseValue = "false";
		final String incorrectValue = "truer";
		
		mockery.checking(new Expectations() {{
			exactly(2).of(element).getChildren(); will(returnValue(list));
			exactly(2).of(list).size(); will(returnValue(0));
			exactly(2).of(element).getName(); will(returnValue(name));
			oneOf(element).getContent(); will(returnValue(falseValue));
			oneOf(element).getContent(); will(returnValue(incorrectValue));
		}});
		
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
