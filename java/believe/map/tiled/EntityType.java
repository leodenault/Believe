package believe.map.tiled;

import java.util.Arrays;

/** Represents a type of entity generated from a tile in a tile map. */
public enum EntityType {
  UNKNOWN,
  NONE,
  ENEMY,
  COMMAND,
  COLLIDABLE_TILE;

  /**
   * Returns an {@link EntityType} with the given {@code name}, or {@link #UNKNOWN} if none match.
   * Unlike {@link EntityType#valueOf(String)} this method does not result in exceptions being
   * thrown.
   */
  public static EntityType forName(String name) {
    return Arrays.stream(EntityType.values())
        .filter(entityType -> entityType.name().equals(name.toUpperCase()))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
