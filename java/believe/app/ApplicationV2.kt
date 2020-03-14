package believe.app

import dagger.Reusable
import org.newdawn.slick.AppGameContainer
import javax.inject.Inject

/** The entry point for an application running on the Believe stack. */
@Reusable
class ApplicationV2 @Inject internal constructor(
    private val appGameContainer: AppGameContainer
) {
    /** Starts running the application. */
    fun start() = appGameContainer.start()
}