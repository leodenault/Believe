package believe.map.data;

import java.util.Arrays;

/** Represents a type of entity generated from a tile in a tile map. */
public enum EntityType {
  UNKNOWN,
  NONE,
  ENEMY,
  COMMAND,
  COLLIDABLE_TILE;

  /**
   * Returns an [EntityType] with the given `name`, or [UNKNOWN] if none match. Unlike
   * [EntityType.valueOf] this method does not result in exceptions being thrown.
   */
  public static EntityType forName(String name) {
    return Arrays.stream(values())
        .filter(value -> name.toUpperCase().equals(value.name()))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
