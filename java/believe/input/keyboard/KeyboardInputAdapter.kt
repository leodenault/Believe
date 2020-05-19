package believe.input.keyboard

import believe.input.InputAdapter
import believe.input.InputAdapterImpl
import dagger.Reusable
import org.newdawn.slick.Input
import org.newdawn.slick.KeyListener
import org.newdawn.slick.gui.GUIContext
import javax.inject.Inject

/** Wraps Slick's keyboard input system and maps its signals to actions of type [A].
 *
 * @param guiContext the [GUIContext] which provides the input system so that this adapter may
 * register itself as a listener.
 * @param inputAdapter the underlying [InputAdapterImpl] for managing base adapter logic.
 * @param A the type of action that Slick's keyboard input signals should be mapped to.
 */
class KeyboardInputAdapter<A> private constructor(
    guiContext: GUIContext, private val inputAdapter: InputAdapterImpl<Int, A>
) : KeyListener, InputAdapter<A> by inputAdapter {

    init {
        guiContext.input.addKeyListener(this)
    }

    override fun inputStarted() {}

    override fun setInput(input: Input?) {}

    override fun inputEnded() {}

    override fun isAcceptingInput(): Boolean = true

    override fun keyPressed(key: Int, char: Char) = inputAdapter.actionStarted(key)

    override fun keyReleased(key: Int, char: Char) = inputAdapter.actionEnded(key)

    /**
     * Factory for creating [KeyboardInputAdapter] instances.
     *
     * @param guiContext the [GUIContext] which provides the input system so that this adapter may
     * register itself as a listener.
     */
    @Reusable
    class Factory @Inject internal constructor(
        private val guiContext: GUIContext) {
        /**
         * Creates a [KeyboardInputAdapter] instance.
         *
         * @param mapInput maps input from Slick's keyboard input data to actions of type [A].
         * @param A the type of action that Slick's keyboard input signals should be mapped to.
         */
        fun <A> create(mapInput: (Int) -> A) =
            KeyboardInputAdapter(guiContext, InputAdapterImpl(mapInput))
    }
}
