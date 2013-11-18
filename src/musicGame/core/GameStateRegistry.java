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
	
	public void addEntry(Class<?> entry) {
		this.entries.put(entry, currentId++);
	}
	
	public int getEntry(Class<?> entry) {
		return this.entries.get(entry);
	}
}
