package believe.gamestate

import believe.app.proto.GameOptionsProto.GameOptions
import believe.app.proto.GameOptionsProto.GameplayOptions
import believe.datamodel.DataCommitter
import believe.datamodel.MutableValue
import believe.gui.MenuSelection
import believe.gui.NumberPicker
import believe.gui.VerticalKeyboardScrollpanel
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import org.newdawn.slick.Font
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import javax.inject.Inject

@AutoFactory
class OptionsMenuStateV2 constructor(
    @Provided private val container: GameContainer, @Provided font: Font, @Provided
    stateController: StateController, @Provided mutableGameOptions: MutableValue<GameOptions>,
    @Provided gameOptionsCommitter: DataCommitter<GameOptions>
) : GameState {

    private var scrollPanelFocused: Boolean
    private val back: MenuSelection
    private val scrollPanel: VerticalKeyboardScrollpanel

    init {
        val cWidth = container.width
        val cHeight = container.height
        scrollPanelFocused = false
        back = MenuSelection(
            container, font, cWidth / 80, cHeight / 80, cWidth / 4, cHeight / 12, "Back"
        )
        scrollPanel = VerticalKeyboardScrollpanel(
            container,
            (cWidth * 0.37).toInt(),
            cHeight / 80,
            (cWidth * 0.6).toInt(),
            cHeight / 8,
            (cHeight * 0.95).toInt()
        )
        val flowSpeed = NumberPicker(
            container, font, "Flow Speed", mutableGameOptions.get().gameplayOptions.flowSpeed, 1, 20
        )
        flowSpeed.addListener {
            if (scrollPanelFocused) {
                mutableGameOptions.update(
                    mutableGameOptions.get().toBuilder().mergeGameplayOptions(
                        GameplayOptions.newBuilder().setFlowSpeed(flowSpeed.value).build()
                    ).build()
                )
            }
            scrollPanelFocused = !scrollPanelFocused
        }
        scrollPanel.addChild(flowSpeed)
        back.addListener {
            gameOptionsCommitter.commit(mutableGameOptions.get())
            stateController.navigateToMainMenu()
        }
    }

    //    override fun keyPressed(key: Int, c: Char) {
    //        super.keyPressed(key, c)
    //        if (key == Input.KEY_ENTER) {
    //            if (back.isSelected) {
    //                back.activate()
    //            } else {
    //                scrollPanel.activateSelection()
    //            }
    //        }
    //        if (!scrollPanelFocused) {
    //            when (key) {
    //                Input.KEY_LEFT, Input.KEY_RIGHT -> {
    //                    back.toggleSelect()
    //                    scrollPanel.toggleFocus()
    //                }
    //                Input.KEY_UP -> if (!back.isSelected) {
    //                    scrollPanel.scrollUp()
    //                }
    //                Input.KEY_DOWN -> if (!back.isSelected) {
    //                    scrollPanel.scrollDown()
    //                }
    //            }
    //        }
    //    }

    override fun enter() {}

    override fun leave() {}

    override fun update(delta: Int) {}

    override fun render(g: Graphics) {
        back.render(container, g)
        scrollPanel.render(container, g)
    }
}