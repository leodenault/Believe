package believe.map.tiled.command

import believe.command.Command
import believe.core.PropertyProvider

/** Supplies a [Command] based on the contents of a [PropertyProvider].  */
interface TiledCommandParser {
    /**
     * Return a [Command] based on the contents of [propertyProvider], or null if parsing fails.
     *
     * @param propertyProvider the [PropertyProvider] from which the command should be
     * generated.
     */
    fun parseTiledCommand(propertyProvider: PropertyProvider): Command?
}