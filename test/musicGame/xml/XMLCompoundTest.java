package musicGame.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLCompoundTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
	}};
	
	private static final String PRIMITIVE = "primitive";
	private static final String COMPOUND = "compound";
	private static final String LIST = "list";
	
	private XMLCompound compound;
	private List<ChildDef> defs;
	
	@Mock private XMLElement element;
	@Mock private XMLElementList elList;
	@Mock private XMLNode node;
	@Mock private XMLNode primitive;
	@Mock private XMLNode list;
	@Mock private XMLNode childCompound;
	
	@Before
	public void setUp() {
		defs = Arrays.asList(
				new StringDef(PRIMITIVE),
				new CompoundDef(COMPOUND),
				new ListDef(LIST, null)
				);
		compound = new XMLCompound(defs);
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowExceptionIfNumberOfChildrenIsWrong() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(element).getName(); will(returnValue("someElement"));
			oneOf(element).getChildren(); will(returnValue(elList));
			oneOf(elList).size(); will(returnValue(2));
		}});
		compound.fillNode(element, node);
	}

	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowExceptionIfNodeHasIncorrectName() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(element).getName(); will(returnValue("someElement"));
			oneOf(element).getChildren(); will(returnValue(elList));
			oneOf(elList).size(); will(returnValue(3));
			oneOf(elList).get(0); will(returnValue(element));
			oneOf(element).getName(); will(returnValue("Doesn't exist"));
		}});
		compound.fillNode(element, node);
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowExceptionIfTwoNodesHaveSameName() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(element).getName(); will(returnValue("someElement"));
			oneOf(element).getChildren(); will(returnValue(elList));
			oneOf(elList).size(); will(returnValue(3));
			oneOf(elList).get(0); will(returnValue(element));
			oneOf(element).getName(); will(returnValue(COMPOUND));
			ignoring(node).fillNode(element);
			oneOf(elList).get(1); will(returnValue(element));
			oneOf(element).getName(); will(returnValue(COMPOUND));
		}});
		compound.fillNode(element, node);
	}
	
	@Test
	public void fillNodeShouldPopulateCompoundNodeWithChildren() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(element).getName(); will(returnValue("someElement"));
			oneOf(element).getChildren(); will(returnValue(elList));
			oneOf(elList).size(); will(returnValue(3));
			oneOf(elList).get(0); will(returnValue(element));
			oneOf(element).getName(); will(returnValue(LIST));
			oneOf(node).fillNode(element); will(returnValue(node));
			oneOf(elList).get(1); will(returnValue(element));
			oneOf(element).getName(); will(returnValue(COMPOUND));
			oneOf(node).fillNode(element); will(returnValue(node));
			oneOf(elList).get(2); will(returnValue(element));
			oneOf(element).getName(); will(returnValue(PRIMITIVE));
			oneOf(node).fillNode(element); will(returnValue(node));
		}});
		compound.fillNode(element, node);
		assertThat(compound.getValue(LIST), is(node));
		assertThat(compound.getValue(COMPOUND), is(node));
		assertThat(compound.getValue(PRIMITIVE), is(node));
	}
	
	@Test(expected=NullPointerException.class)
	public void getValueShouldThrowExceptionWhenKeyDoesntExist() {
		@SuppressWarnings("serial")
		HashMap<String, XMLNode> map = new HashMap<String, XMLNode>() {{
			put("apple", primitive);
		}};
		XMLCompound compound = new XMLCompound(defs, map);
		compound.getValue("wrong!");
	}
	
	@Test(expected=ClassCastException.class)
	public void getValueShouldThrowExceptionWhenValueTypeIncorrect() {
		final String key = "orange";
		@SuppressWarnings("serial")
		HashMap<String, XMLNode> map = new HashMap<String, XMLNode>() {{
			put(key, primitive);
		}};
		XMLCompound compound = new XMLCompound(defs, map);
		@SuppressWarnings("unused")
		XMLCompound result = compound.getValue(key);
	}
	
	@Test
	public void getValueShouldReturnValueAssociatedToKey() {
		final String compoundName = "violet";
		final String listName = "red";
		final String primitiveName = "white";
		@SuppressWarnings("serial")
		HashMap<String, XMLNode> map = new HashMap<String, XMLNode>() {{
			put(compoundName, compound);
			put(listName, list);
			put(primitiveName, primitive);
		}};
		XMLCompound compoundResult = new XMLCompound(defs, map);
		
		Assert.assertEquals(compoundResult.getValue(compoundName), compound);
		Assert.assertEquals(compoundResult.getValue(listName), list);
		Assert.assertEquals(compoundResult.getValue(primitiveName), primitive);
	}
}
