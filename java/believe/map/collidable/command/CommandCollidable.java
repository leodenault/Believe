package believe.map.collidable.command;

import believe.physics.collision.Collidable;

/**
 * Empty interface used only in identifying objects which should be allowed to collide with
 * commands.
 */
public interface CommandCollidable<C extends CommandCollidable<C>> extends Collidable<C> {}
