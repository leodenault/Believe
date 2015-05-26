package musicGame.core;

import java.util.HashMap;

/**
 * Keeps track of GameState ids to avoid collisions.
 */
public class GameStateRegistry {

	private static GameStateRegistry registry;
	private static int currentId = 0;
	
	private HashMap<Class<?>, Integer> entries;
	
	private GameStateRegistry() {
		entries = new HashMap<Class<?>, Integer>();
	}
	
	public static GameStateRegistry getInstance() {
		if (registry == null) {
			registry = new GameStateRegistry();
		}
		return registry;
	}
	
	public int addEntry(Class<?> entry) {
		Integer id = this.entries.get(entry);
		if (id == null) {
			id = currentId++;
			this.entries.put(entry, id);
		}
		return id;
	}
	
	public int getEntry(Class<?> entry) {
		if (this.entries.containsKey(entry)) {
			return this.entries.get(entry);
		} else {
			throw new IllegalArgumentException("The given state does not exist in the game");
		}
	}
}
