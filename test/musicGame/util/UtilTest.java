package musicGame.util;

import static musicGame.util.MapEntry.entry;
import static org.hamcrest.CoreMatchers.containsInAnyOrder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class UtilTest {
	@Mock private Graphics g;
	@Mock private musicGame.geometry.Rectangle childRect;
	@Mock private Rectangle parentRect;
	
	@Before
	public void setUp() {
		initMocks(this);
	}
	
	@Test
	public void changeClipContextShouldReturnIntersectionOfParentAndChildClips() {
		final musicGame.geometry.Rectangle newClip = new musicGame.geometry.Rectangle(50, 50, 50, 50);
		final float x = 0.0f;
		final float y = 0.0f;
		final float width = 100.0f;
		final float height = 100.0f;
		
		mockery.checking(new Expectations() {{
				when(g.getClip()).thenReturn(parentRect);
				when(parentRect.getX()).thenReturn(x);
				when(parentRect.getY()).thenReturn(y);
				when(parentRect.getWidth()).thenReturn(width);
				when(parentRect.getHeight()).thenReturn(height);
				when(childRect.intersection(with(any(Rectangle.class)))).thenReturn(newClip);
				oneOf(g).setClip(newClip);
		}});
		Rectangle oldClip = Util.changeClipContext(g, childRect);
		assertThat(oldClip.getX(), is(x));
		assertThat(oldClip.getY(), is(y));
		assertThat(oldClip.getWidth(), is(width));
		assertThat(oldClip.getHeight(), is(height));
	}

	@Test
	public void changeClipContextShouldReturnChildClipIfParentClipIsNull() {
		mockery.checking(new Expectations() {{
				when(g.getClip()).thenReturn(null);
				oneOf(g).setClip(childRect);
		}});
		Rectangle oldClip = Util.changeClipContext(g, childRect);
		assertThat(oldClip, nullValue());
	}
	
	@Test
	public void resetClipContextShouldSetGraphicsClipToGivenRectangle() {
		mockery.checking(new Expectations() {{
			oneOf(g).setClip(parentRect);
		}});
		Util.resetClipContext(g, parentRect);
	}
	
	@Test
	public void hashSetOfReturnsProperHashSet() {
		assertThat(
				Util.hashSetOf("one", "two", "three"), containsInAnyOrder("one", "two", "three"));
	}
	
	@Test
	public void hashMapOfReturnsCorrectHashMap() {
		HashMap<String, String> expectedMap = new HashMap<>();
		expectedMap.put("key", "value");
		expectedMap.put("key2", "value2");
		assertThat(Util.hashMapOf(entry("key", "value"), entry("key2", "value2")), is(expectedMap));

		HashMap<String, String> emptyMap = new HashMap<>();
		assertThat(Util.hashMapOf(), is(emptyMap));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void immutableMapOfCannotBeModified() {
		Map<String, String> map = Util.immutableMapOf(entry("key", "value"));
		map.put("key", "value2");
	}
}
