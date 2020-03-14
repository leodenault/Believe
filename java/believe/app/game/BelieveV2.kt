package believe.app.game

import believe.app.ApplicationComponentV2
import believe.app.DaggerApplicationComponentV2
import believe.app.StateSelector
import believe.gamestate.StateController

object BelieveV2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val component: ApplicationComponentV2 =
            DaggerApplicationComponentV2.factory().create(args, "Believe", object : StateSelector {
                override fun selectState(stateController: StateController) =
                    stateController.navigateToMainMenu()
            })
        component.application().start()
    }
}