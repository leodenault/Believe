package musicGame.map;

import static musicGame.map.MapProperties.COLLISION;
import static musicGame.map.MapProperties.FRONT;
import static musicGame.map.MapProperties.NO_PROP_DEFAULT;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.tiled.TiledMap;

public class MapPropertiesTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
	}};

	private MapProperties properties;
	
	@Mock private TiledMap map;
	
	@Before
	public void setUp() {
		properties = new MapProperties();
	}

	@Test
	public void fetchLayersShouldPlaceLayersWithFrontPropertyInFront() {
		mockery.checking(new Expectations() {{
			exactly(7).of(map).getLayerCount(); will(returnValue(6));
			oneOf(map).getLayerProperty(0, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(1, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(2, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(3, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(4, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(5, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));

			oneOf(map).getLayerProperty(0, FRONT, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(1, FRONT, NO_PROP_DEFAULT); will(returnValue(FRONT));
			oneOf(map).getLayerProperty(2, FRONT, NO_PROP_DEFAULT); will(returnValue(FRONT));
			oneOf(map).getLayerProperty(3, FRONT, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(4, FRONT, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(5, FRONT, NO_PROP_DEFAULT); will(returnValue(FRONT));
		}});
		
		MapProperties.fetchLayers(map, properties);
		assertThat(properties.frontLayers, contains(1, 2, 5));
		assertThat(properties.rearLayers, contains(0, 3, 4));
	}

	@Test
	public void fetchLayersShouldIgnoreCollisionLayers() {
		mockery.checking(new Expectations() {{
			exactly(5).of(map).getLayerCount(); will(returnValue(4));
			oneOf(map).getLayerProperty(0, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
			oneOf(map).getLayerProperty(1, COLLISION, NO_PROP_DEFAULT); will(returnValue(COLLISION));
			oneOf(map).getLayerProperty(2, COLLISION, NO_PROP_DEFAULT); will(returnValue(COLLISION));
			oneOf(map).getLayerProperty(3, COLLISION, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));

			oneOf(map).getLayerProperty(0, FRONT, NO_PROP_DEFAULT); will(returnValue(FRONT));
			oneOf(map).getLayerProperty(3, FRONT, NO_PROP_DEFAULT); will(returnValue(NO_PROP_DEFAULT));
		}});
		
		MapProperties.fetchLayers(map, properties);
		assertThat(properties.frontLayers, contains(0));
		assertThat(properties.rearLayers, contains(3));
	}

}
