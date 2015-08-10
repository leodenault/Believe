package musicGame.core;

import java.util.LinkedList;
import java.util.List;

public class SynchedComboPattern {
	public class TimeKeyPair {
		public int time;
		public char key;
		
		public TimeKeyPair(int time, char key) {
			this.time = time;
			this.key = key;
		}
	}
	
	List<TimeKeyPair> actions;
	
	public SynchedComboPattern() {
		actions = new LinkedList<TimeKeyPair>();
	}
	
	public List<TimeKeyPair> getActions() {
		return actions;
	}
	
	public void addAction(int time, char key) {
		actions.add(new TimeKeyPair(time, key));
	}
}
