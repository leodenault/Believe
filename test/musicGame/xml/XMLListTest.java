package musicGame.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLListTest {
	private static final CompoundDef DEF = new CompoundDef("node_type");
	
	private XMLList list;
	
	@Mock XMLElement element;
	@Mock XMLCompound child;
	@Mock XMLElementList children;
	
	@Before
	public void setUp() {
		initMocks(this);
		list = new XMLList(DEF);
	}
	
	@Test(expected=XMLLoadingException.class)
	public void fillNodeShouldThrowExceptionIfNodesAreNotOfSpecifiedType() throws XMLLoadingException {
		final String name = "someNodeList";
		
		mockery.checking(new Expectations() {{
			when(element.getName()).thenReturn(name);
			when(element.getChildren()).thenReturn(children);
			when(children.size()).thenReturn(1);
			when(children.get(0)).thenReturn(element);
			when(element.getName()).thenReturn("wrongName");
		}});
		
		list.fillNode(element);
	}
	
	@Test
	public void fillNodeShouldFillZeroElementsIfEmpty() throws XMLLoadingException {
		final String name = "emptyList";
		
		mockery.checking(new Expectations() {{
			when(element.getName()).thenReturn(name);
			when(element.getChildren()).thenReturn(children);
			when(children.size()).thenReturn(0);
		}});
		
		list.fillNode(element);
		assertThat(list.name, is(name));
	}
	
	@Test
	public void fillNodeShouldFillWithChildren() throws XMLLoadingException {
		final String name = "someNodeList";
		final int numNodes = 5;
		
		mockery.checking(new Expectations() {{
			when(element.getName()).thenReturn(name);
			when(element.getChildren()).thenReturn(children);
			when(children.size()).thenReturn(numNodes);
			
			for (int i = 0; i < numNodes; i++) {
				when(children.get(i)).thenReturn(element);
				when(element.getName()).thenReturn(DEF.name);
				ignoring(child).fillNode(element);
			}
		}});
		
		list.fillNode(element, child);
		assertThat(list.name, is(name));
		assertThat(list.children.size(), is(numNodes));
	}
}
