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
	public void createNodeReturnsStringWhenGivenString() {
		ChildDef def = new StringDef("");
		XMLNode node = factory.createNode(def);
		assertThat(node, Matchers.instanceOf(XMLString.class));
	}
	
	@Test
	public void createNodeReturnsIntWhenGivenInt() {
		ChildDef def = new IntegerDef("");
		XMLNode node = factory.createNode(def);
		assertThat(node, Matchers.instanceOf(XMLInteger.class));
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
