package musicGame.gui;

import static musicGame.gui.ComboSyncher.BUFFER_TIME;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;

import musicGame.core.SynchedComboPattern;
import musicGame.levelFlow.FlowComponentListener;

public class ComboSyncherTest {
	
	private static final int BPM = 132;
	private static final int FIRST_BEAT = 4;
	private static final int SECOND_BEAT = 8;
	private static final int LAST_BEAT = 13;
	private static final float SECONDS_IN_BEAT = 60.0f / BPM;
	private static final char FIRST_KEY = 'd';
	private static final char SECOND_KEY = 't';

	@Rule
	public JUnitRuleMockery mockery = new JUnitRuleMockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
	}};
	
	private ComboSyncher combo;
	private SynchedComboPattern pattern;
	
	@Mock private GUIContext container;
	@Mock private Input input;
	@Mock private TrueTypeFont font;
	@Mock private Music music;
	@Mock private FlowComponentListener listener;
	
	@Before
	public void setUp() {
		mockery.checking(new Expectations() {{
			oneOf(container).getInput(); will(returnValue(input));
			oneOf(input).addPrimaryListener(with(any(ComboSyncher.class)));
		}});
		
		pattern = new SynchedComboPattern();
		pattern.addAction(FIRST_BEAT, FIRST_KEY);
		pattern.addAction(SECOND_BEAT, SECOND_KEY);
		pattern.addAction(LAST_BEAT, '[');
		combo = new ComboSyncher(container, pattern, font, BPM, 0, 0, 100, 10);
		combo.addListener(listener);
	}
	
	private float computeUpToBeat(float musicPosition, float beat) {
		return -(musicPosition + BUFFER_TIME) % (SECONDS_IN_BEAT)
				+ BUFFER_TIME + (beat * SECONDS_IN_BEAT);
	}
	
	private float computeLength(float musicPosition, float beat) {
		return computeUpToBeat(musicPosition, beat) + BUFFER_TIME;
	}
	
	@Test
	public void updateNotifiesEndOfCombo() {
		final float musicStart = 2.3f;
		final float comboLength = computeLength(musicStart, LAST_BEAT);
		
		mockery.checking(new Expectations() {{
			oneOf(music).getPosition(); will(returnValue(musicStart));
			oneOf(music).getPosition(); will(returnValue(musicStart + comboLength));
			oneOf(listener).songEnded();
		}});
		
		combo.start(music);
		combo.update();
		
	}
	
	@Test
	public void updateNotifiesMissedComboBeats() {
		final float musicStart = 4.8f;
		final float lengthToBeat = computeUpToBeat(musicStart, FIRST_BEAT);
		
		mockery.checking(new Expectations() {{
			oneOf(music).getPosition(); will(returnValue(musicStart));
			oneOf(music).getPosition(); will(returnValue(musicStart + lengthToBeat + 1.4f));
			oneOf(listener).beatMissed();
		}});
		
		combo.start(music);
		combo.update();
	}
	
	@Test
	public void updateNotifiesFailedBeatWhenPressingAtWrongTime() {
		final float musicStart = 0.2f;
		final float lengthToBeat = computeUpToBeat(musicStart, FIRST_BEAT);
		
		mockery.checking(new Expectations() {{
			oneOf(music).getPosition(); will(returnValue(musicStart));
			oneOf(music).getPosition(); will(returnValue(musicStart + lengthToBeat - 0.65f));
			oneOf(music).getPosition(); will(returnValue(musicStart + lengthToBeat));
			oneOf(listener).beatFailed();
			oneOf(listener).beatFailed();
		}});
		
		combo.start(music);
		combo.update();
		combo.keyPressed(0, FIRST_KEY);
		combo.update();
		combo.keyPressed(0, SECOND_KEY);
	}
	
	@Test
	public void updateNotifiesSuccessfulBeatsWhenPressingAtRightTime() {
		final float musicStart = 45.8f;
		final float lengthToBeat = computeUpToBeat(musicStart, FIRST_BEAT);
		
		mockery.checking(new Expectations() {{
			oneOf(music).getPosition(); will(returnValue(musicStart));
			oneOf(music).getPosition(); will(returnValue(musicStart + lengthToBeat));
			oneOf(listener).beatSuccess(0);
		}});
		
		combo.start(music);
		combo.update();
		combo.keyPressed(0, FIRST_KEY);
	}

}
