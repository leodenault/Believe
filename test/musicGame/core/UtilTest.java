package musicGame.core;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class UtilTest {

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
		}
	};
	
	@Mock private Graphics g;
	@Mock private musicGame.geometry.Rectangle childRect;
	@Mock private Rectangle parentRect;
	
	@Test
	public void changeClipContextShouldReturnIntersectionOfParentAndChildClips() {
		final musicGame.geometry.Rectangle newClip = new musicGame.geometry.Rectangle(50, 50, 50, 50);
		final float x = 0.0f;
		final float y = 0.0f;
		final float width = 100.0f;
		final float height = 100.0f;
		
		mockery.checking(new Expectations() {{
				oneOf(g).getClip(); will(returnValue(parentRect));
				oneOf(parentRect).getX(); will(returnValue(x));
				oneOf(parentRect).getY(); will(returnValue(y));
				oneOf(parentRect).getWidth(); will(returnValue(width));
				oneOf(parentRect).getHeight(); will(returnValue(height));
				oneOf(childRect).intersection(with(any(Rectangle.class))); will(returnValue(newClip));
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
				oneOf(g).getClip(); will(returnValue(null));
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
}
