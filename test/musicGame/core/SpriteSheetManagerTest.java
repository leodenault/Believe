package musicGame.core;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import musicGame.core.exception.SpriteSheetLoadingException;

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

public class SpriteSheetManagerTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
	}};
	
	private SpriteSheetManager manager;
	
	@Mock private XMLElement datum;
	@Mock private XMLElementList list;
	
	@Before
	public void setUp() {
		manager = new SpriteSheetManager();
	}
	
	private void expectDataPoint(final int item, final String name, final String content) {
		mockery.checking(new Expectations() {{
			oneOf(list).get(item); will(returnValue(datum));
			oneOf(datum).getName(); will(returnValue(name));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(0));
			oneOf(datum).getContent(); will(returnValue(content));
		}});
	}
	
	@Test(expected=SpriteSheetLoadingException.class)
	public void extractSheetShouldThrowExceptionWhenElementNotNamedSpriteSheet()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spork"));
		}});
		manager.extractSheet(datum);
	}
	
	@Test(expected=SpriteSheetLoadingException.class)
	public void extractSheetShouldThrowExceptionWhenElementDoesntHaveFiveChildren()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spriteSheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(4));
		}});
		manager.extractSheet(datum);
	}
	
	@Test(expected=SpriteSheetLoadingException.class)
	public void extractSheetShouldThrowExceptionWhenChildrenAreIncorrect()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spriteSheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(5));
			oneOf(list).get(0); will(returnValue(datum));
			oneOf(datum).getName(); will(returnValue("asdasd"));
		}});
		manager.extractSheet(datum);
	}
	
	@Test(expected=SpriteSheetLoadingException.class)
	public void extractSheetShouldThrowExceptionWhenDatumHasChildren()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spriteSheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(5));
			oneOf(list).get(0); will(returnValue(datum));
			oneOf(datum).getName(); will(returnValue("sheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(1));
		}});
		manager.extractSheet(datum);
	}
	
	@Test(expected=NumberFormatException.class)
	public void extractSheetShouldThrowExceptionWhenNumericalDatumCannotBeRead()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spriteSheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(5));
		}});
		expectDataPoint(0, "sheet", "/some/location/somewhere.png");
		expectDataPoint(1, "frameWidth", "1as!!3");
		manager.extractSheet(datum);
	}
	
	@Test(expected=SpriteSheetLoadingException.class)
	public void extractSheetShouldThrowExceptionWhenSameNameAppearsMultipleTimes()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spriteSheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(5));
			oneOf(list).get(1); will(returnValue(datum));
			oneOf(datum).getName(); will(returnValue("sheet"));
		}});
		expectDataPoint(0, "sheet", "/some/location/somewhere.png");
		manager.extractSheet(datum);
	}

	@Test
	public void extractSheetShouldReturnSpriteSheetDatumWithXMLContents()
			throws NumberFormatException, SpriteSheetLoadingException {
		mockery.checking(new Expectations() {{
			oneOf(datum).getName(); will(returnValue("spriteSheet"));
			oneOf(datum).getChildren(); will(returnValue(list));
			oneOf(list).size(); will(returnValue(5));
		}});
		expectDataPoint(0, "sheet", "/some/location/somewhere.png");
		expectDataPoint(1, "frameWidth", "123");
		expectDataPoint(2, "name", "someAnimation");
		expectDataPoint(3, "frameHeight", "133");
		expectDataPoint(4, "frameLength", "11");
		
		SpriteSheetDatum item = manager.extractSheet(datum);
		assertThat(item.name, is("someAnimation"));
		assertThat(item.sheet, is("/some/location/somewhere.png"));
		assertThat(item.frameWidth, is(123));
		assertThat(item.frameHeight, is(133));
		assertThat(item.frameLength, is(11));
	}
}
