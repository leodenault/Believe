package believe.app

import believe.app.flags.AppFlags
import dagger.Reusable
import org.newdawn.slick.AppGameContainer
import javax.inject.Inject

/** Factory for creating and initialising instances of [AppGameContainer]. */
@Reusable
class AppGameContainerFactory @Inject internal constructor(
    private val environmentConfigurator: EnvironmentConfigurator,
    private val appFlags: AppFlags,
    private val game: Game
) {
    fun create(): AppGameContainer {
        environmentConfigurator.configure()

        val width = appFlags.width()
        val height = appFlags.height()
        val isWindowed = appFlags.windowed()

        return AppGameContainer(game).apply {
            val gameWidth = if (width < 0) screenWidth else width
            val gameHeight = if (height < 0) screenHeight else height

            setShowFPS(false)
            setDisplayMode(gameWidth, gameHeight, !isWindowed)
            isMouseGrabbed = !isWindowed
        }
    }
}