package believe.map.tiled.command

import believe.command.Command
import believe.command.CommandGenerator
import believe.command.proto.CommandProto
import believe.core.PropertyProvider
import believe.proto.TextProtoParser
import dagger.Reusable
import org.newdawn.slick.util.Log
import java.io.ByteArrayInputStream
import believe.util.KotlinHelpers.whenNull
import javax.inject.Inject

/** Default implementation of [TiledCommandParser]. */
@Reusable
internal class TiledCommandParserImpl @Inject constructor(
    @TiledCommandParameter private val commandParameter: String,
    @ModulePrivate
    private val textProtoParser: TextProtoParser<CommandProto.Command>, private val commandGenerator: CommandGenerator
) : TiledCommandParser {

    override fun parseTiledCommand(propertyProvider: PropertyProvider): Command? =
        propertyProvider.getProperty(commandParameter).whenNull {
            Log.error("Expected to find a '$commandParameter' parameter.")
        }?.let { commandText ->
            textProtoParser.parse(ByteArrayInputStream(commandText.toByteArray()))
                ?.let { parsedProto ->
                    commandGenerator.generateCommand(parsedProto)
                }
        }
}