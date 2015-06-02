package musicGame.core;

import java.util.HashMap;

import musicGame.state.GameStateBase;

/**
 * Keeps track of GameState ids to avoid collisions.
 */
public class GameStateRegistry {
	
	private static final class Entry<T extends GameStateBase> {
		public Integer ID;
		public T state;
		
		public Entry(Integer ID, T state) {
			this.ID = ID;
			this.state = state;
		}
	}

	private static GameStateRegistry registry;
	private static int currentId = 0;
	
	private HashMap<Class<? extends GameStateBase>, Entry<? extends GameStateBase>> entries;
	
	private GameStateRegistry() {
		entries = new HashMap<Class<? extends GameStateBase>, Entry<? extends GameStateBase>>();
	}
	
	public static GameStateRegistry getInstance() {
		if (registry == null) {
			registry = new GameStateRegistry();
		}
		return registry;
	}
	
	public <T extends GameStateBase> int addEntry(T entry) {
		Class<? extends GameStateBase> type = entry.getClass();
		
		if (!this.entries.containsKey(type)) {
			this.entries.put(type, new Entry<GameStateBase>(currentId++, entry));
		}
		return this.entries.get(type).ID;
	}
	
	public int getEntryID(Class<? extends GameStateBase> entry) {
		return getEntry(entry).ID;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GameStateBase> T getEntryState(Class<T> entry) {
		return (T)getEntry(entry).state;
	}
	
	private <T extends GameStateBase> Entry<? extends GameStateBase> getEntry(Class<T> entry) {
		if (this.entries.containsKey(entry)) {
			return this.entries.get(entry);
		} else {
			throw new IllegalArgumentException("The given state does not exist in the game");
		}
	}
}
