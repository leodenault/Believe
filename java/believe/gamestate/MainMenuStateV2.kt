package believe.gamestate

import believe.gui.DirectionalPanel
import believe.gui.MenuSelection
import believe.gui.MenuSelectionGroup
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.Font
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import javax.inject.Inject

@AutoFactory
class MainMenuStateV2 constructor(
    @Provided private val container: GameContainer, @Provided font: Font, @Provided
    stateController: StateController
) : GameState {

    private val panel: DirectionalPanel
    private val selections: MenuSelectionGroup

    init {
        val playPlatformingLevel = MenuSelection(container, font, "Play Platforming Level")
        val playArcadeLevel = MenuSelection(container, font, "Play Arcade Level")
        val playFlowFile = MenuSelection(container, font, "Play Flow File")
        val options = MenuSelection(container, font, "Options")
        val exit = MenuSelection(container, font, "Exit")
        panel = DirectionalPanel(
            container, container.width / 2, (container.height - 250) / 5, 50
        ).apply {
            addChild(playPlatformingLevel)
            addChild(playArcadeLevel)
            addChild(playFlowFile)
            addChild(options)
            addChild(exit)
        }
        //        playPlatformingLevel.addListener(
        //            ChangeStateAction(
        //                PlatformingState::class.java, game
        //            )
        //        )
        //        playArcadeLevel.addListener(ChangeStateAction(ArcadeState::class.java, game))
        //        playFlowFile.addListener(
        //            ChangeStateAction(
        //                FlowFilePickerMenuState::class.java, game
        //            )
        //        )
        options.addListener { stateController.navigateToOptionsMenu() }

        //        (ChangeStateAction(OptionsMenuState::class.java, game))

        exit.addListener { container.exit() }
        selections = MenuSelectionGroup().apply {
            add(playPlatformingLevel)
            add(playArcadeLevel)
            add(playFlowFile)
            add(options)
            add(exit)
        }

        selections.select(0)
    }

    //    override fun keyPressed(key: Int, c: Char) {
    //        when (key) {
    //            Input.KEY_DOWN -> selections.selectNext()
    //            Input.KEY_UP -> selections.selectPrevious()
    //            Input.KEY_ENTER -> selections.currentSelection.activate()
    //        }
    //    }

    override fun render(g: Graphics) {
        panel.render(container, g)
    }

    override fun update(delta: Int) {}
}