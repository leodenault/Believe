package musicGame.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.mockito.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLCompoundTest {
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
		initMocks(this);
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
			when(element.getName()).thenReturn("someElement");
			when(element.getChildren()).thenReturn(elList);
			when(elList.size()).thenReturn(2);
		}});
		compound.fillNode(element, node);
	}

	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowExceptionIfNodeHasIncorrectName() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			when(element.getName()).thenReturn("someElement");
			when(element.getChildren()).thenReturn(elList);
			when(elList.size()).thenReturn(3);
			when(elList.get(0)).thenReturn(element);
			when(element.getName()).thenReturn("Doesn't exist");
		}});
		compound.fillNode(element, node);
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowExceptionIfTwoNodesHaveSameName() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			when(element.getName()).thenReturn("someElement");
			when(element.getChildren()).thenReturn(elList);
			when(elList.size()).thenReturn(3);
			when(elList.get(0)).thenReturn(element);
			when(element.getName()).thenReturn(COMPOUND);
			ignoring(node).fillNode(element);
			when(elList.get(1)).thenReturn(element);
			when(element.getName()).thenReturn(COMPOUND);
		}});
		compound.fillNode(element, node);
	}
	
	@Test
	public void fillNodeShouldPopulateCompoundNodeWithChildren() throws XMLLoadingException {
		mockery.checking(new Expectations() {{
			when(element.getName()).thenReturn("someElement");
			when(element.getChildren()).thenReturn(elList);
			when(elList.size()).thenReturn(3);
			when(elList.get(0)).thenReturn(element);
			when(element.getName()).thenReturn(LIST);
			when(node.fillNode(element)).thenReturn(node);
			when(elList.get(1)).thenReturn(element);
			when(element.getName()).thenReturn(COMPOUND);
			when(node.fillNode(element)).thenReturn(node);
			when(elList.get(2)).thenReturn(element);
			when(element.getName()).thenReturn(PRIMITIVE);
			when(node.fillNode(element)).thenReturn(node);
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
