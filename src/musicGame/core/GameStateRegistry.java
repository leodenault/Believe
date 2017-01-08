package musicGame.core;

import java.util.HashMap;

import org.newdawn.slick.state.GameState;

/**
 * Keeps track of GameState ids to avoid collisions.
 */
public class GameStateRegistry {
	
	private static final class Entry<T extends GameState> {
		public Integer ID;
		public T state;
		
		public Entry(Integer ID, T state) {
			this.ID = ID;
			this.state = state;
		}
	}

	private static GameStateRegistry registry;
	private static int currentId = 0;
	
	private HashMap<Class<? extends GameState>, Entry<? extends GameState>> entries;
	
	private GameStateRegistry() {
		entries = new HashMap<Class<? extends GameState>, Entry<? extends GameState>>();
	}
	
	public static GameStateRegistry getInstance() {
		if (registry == null) {
			registry = new GameStateRegistry();
		}
		return registry;
	}
	
	public <T extends GameState> int addEntry(T entry) {
		Class<? extends GameState> type = entry.getClass();
		
		if (!this.entries.containsKey(type)) {
			this.entries.put(type, new Entry<GameState>(currentId++, entry));
		}
		return this.entries.get(type).ID;
	}
	
	public int getEntryID(Class<? extends GameState> entry) {
		System.out.println(String.format("(>'-'>) %s", entry));
		return getEntry(entry).ID;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GameState> T getEntryState(Class<T> entry) {
		return (T)getEntry(entry).state;
	}
	
	private <T extends GameState> Entry<? extends GameState> getEntry(Class<T> entry) {
		if (this.entries.containsKey(entry)) {
			return this.entries.get(entry);
		} else {
			throw new IllegalArgumentException(String.format("The given state '%s' does not exist in the game", entry));
		}
	}
}
