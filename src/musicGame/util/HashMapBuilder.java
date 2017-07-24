package musicGame.util;

import java.util.HashMap;

public class HashMapBuilder<K, V> {
	private HashMap<K, V> map;
	
	private HashMapBuilder() {
		map = new HashMap<>();
	}
	
	public static <K, V> HashMapBuilder<K, V> newBuilder() {
		return new HashMapBuilder<K, V>();
	}
	
	public HashMapBuilder<K, V> put(K key, V value) {
		map.put(key, value);
		return this;
	}
	
	public HashMap<K, V> build() {
		return new HashMap<>(map);
	}
}
