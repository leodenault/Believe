package musicGame.levelFlow.component;

import org.newdawn.slick.gui.ComponentListener;

/**
 * Interface for objects that receive events from a FlowComponent.
 * 
 * @see FlowComponent
 */
public interface FlowComponentListener extends ComponentListener {
	
	/**
	 * Invoked when a beat was successfully consumed.
	 * 
	 * @param index	The index of the lane that was activated.
	 */
	void beatSuccess(int index);
	
	/**
	 * Invoked when an attempt to consume a beat failed.
	 * In other words, the timing for consuming the beat is off.
	 */
	void beatFailed();
	
	/**
	 * Invoked when a beat was discarded without being consumed.
	 */
	void beatMissed();
	
	/**
	 * Invoked when the song has reached its end and no more beats are scrolling
	 */
	void songEnded();
}
