package musicGame.xml;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class XMLNodeFactoryTest {

	private XMLNodeFactory factory;
	
	@Before
	public void setUp() {
		factory = XMLNodeFactory.getIntance();
	}
	
	@Test
	public void createNodeReturnsPrimitiveWhenGivenPrimitive() {
		ChildDef def = new ChildDef("");
		XMLNode node = factory.createNode(def);
		assertThat(node, Matchers.instanceOf(XMLPrimitive.class));
	}
	
	@Test
	public void createNodeReturnsListWhenGivenList() {
		ChildDef def = new ListDef("", new CompoundDef(""));
		XMLNode node = factory.createNode(def);
		assertThat(node, Matchers.instanceOf(XMLList.class));
	}
	
	@Test
	public void createNodeReturnsCompoundWhenGivenCompound() {
		ChildDef def = new CompoundDef("");
		XMLNode node = factory.createNode(def);
		assertThat(node, Matchers.instanceOf(XMLCompound.class));
	}
}
