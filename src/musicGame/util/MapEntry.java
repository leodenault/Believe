package musicGame.util;

public class MapEntry<K, V> {
  private K key;
  private V value;

  private MapEntry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return new MapEntry<K, V>(key, value);
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }
}
