package believe.map.tiled.command

import believe.command.Command
import believe.command.CommandGenerator
import believe.command.proto.CommandProto
import believe.core.PropertyProvider
import believe.logging.testing.VerifiableLogSystem
import believe.logging.testing.VerifiableLogSystem.LogSeverity
import believe.logging.testing.VerifiesLoggingCalls
import believe.logging.truth.VerifiableLogSystemSubject
import believe.proto.TextProtoParser
import believe.proto.testing.FakeTextProtoParserFactoryFactory
import com.google.common.truth.Truth.assertThat
import com.google.protobuf.TextFormat
import org.junit.jupiter.api.Test
import java.util.*

/** Unit tests for [TiledCommandParserImpl]. */
internal class TiledCommandParserImplTest {
    private var textProtoParser: TextProtoParser<CommandProto.Command> =
        FakeTextProtoParserFactoryFactory.create().create()
    private val commandParserImpl: TiledCommandParserImpl by lazy {
        TiledCommandParserImpl(COMMAND_PARAMETER, textProtoParser, object : CommandGenerator {
            override fun generateCommand(commandData: CommandProto.Command) = COMMAND
        })
    }

    @Test
    fun parseTiledCommand_returnsCommandGeneratorResult() {
        assertThat(
            commandParserImpl.parseTiledCommand(propertyProviderReturning(COMMAND_PARAMETER))
        ).isEqualTo(COMMAND)
    }

    @Test
    @VerifiesLoggingCalls
    fun parseTiledCommand_commandCannotBeFound_returnsNullAndLogsError(
        logSystem: VerifiableLogSystem
    ) {
        assertThat(
            commandParserImpl.parseTiledCommand(propertyProviderReturning(null))
        ).isNull()
        VerifiableLogSystemSubject.assertThat(logSystem).loggedAtLeastOneMessageThat()
            .hasPattern("Expected to find a '$COMMAND_PARAMETER' parameter.")
            .hasSeverity(LogSeverity.ERROR)
    }

    @Test
    fun parseTiledCommand_protoParserFails_returnsNull() {
        textProtoParser = FakeTextProtoParserFactoryFactory.createFailing().create()

        assertThat(
            commandParserImpl.parseTiledCommand(propertyProviderReturning(COMMAND_PARAMETER))
        ).isNull()
    }

    companion object {
        private val COMMAND_PARAMETER =
            TextFormat.printer().printToString(CommandProto.Command.getDefaultInstance())
        private val COMMAND = object : Command {
            override fun execute() {}
        }
    }
}

private fun propertyProviderReturning(value: String?) = object : PropertyProvider {
    override fun getProperty(key: String) = value
}
