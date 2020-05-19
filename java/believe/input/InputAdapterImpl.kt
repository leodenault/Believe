package believe.input

import java.util.*

/**
 * Default implementation of [InputAdapter].
 *
 * @param mapInput maps input from data of type [A] to data of type [B].
 * @param A the format of the input signal provided by Slick.
 * @param B the format of the input signal understood by this instance's listeners.
 */
class InputAdapterImpl<A, B>(private val mapInput: (A) -> B) : InputAdapter<B> {
    private var internalStartListeners = ListenerMap<B>()
    private var internalEndListeners = ListenerMap<B>()

    /** The list of listeners registered to listen for action starts. */
    val startListeners: Map<B, List<() -> Unit>>
        get() = internalStartListeners.listenerMap

    /** The list of listeners registered to listen for action ends. */
    val endListeners: Map<B, List<() -> Unit>>
        get() = internalEndListeners.listenerMap

    override fun addActionStartListener(action: B, listener: () -> Unit) =
        internalStartListeners.addListener(action, listener)

    override fun removeActionStartListener(action: B, listener: () -> Unit) {
        internalStartListeners.removeListener(action, listener)
    }

    override fun addActionEndListener(action: B, listener: () -> Unit) =
        internalEndListeners.addListener(action, listener)

    override fun removeActionEndListener(action: B, listener: () -> Unit) {
        internalEndListeners.removeListener(action, listener)
    }

    /** Delegates this call to each listener. */
    fun actionStarted(action: A) = internalStartListeners.callListeners(mapInput(action))

    /** Delegates this call to each listener. */
    fun actionEnded(action: A) = internalEndListeners.callListeners(mapInput(action))

    override fun pushListeners() {
        internalStartListeners.pushListeners()
        internalEndListeners.pushListeners()
    }

    override fun popListeners() {
        internalStartListeners.popListeners()
        internalEndListeners.popListeners()
    }

    private class ListenerMap<A> {
        private val listenerMapStack: Deque<Map<A, List<() -> Unit>>> = ArrayDeque()

        internal var listenerMap: MutableMap<A, MutableList<() -> Unit>> = mutableMapOf()

        internal fun addListener(action: A, listener: () -> Unit) {
            listenerMap[action].let { listeners ->
                listeners?.add(listener) ?: mutableListOf<() -> Unit>().let {
                    it.add(listener)
                    listenerMap[action] = it
                }
            }
        }

        internal fun removeListener(action: A, listener: () -> Unit) {
            listenerMap[action]?.remove(listener)
        }

        internal fun callListeners(action: A) =
            listenerMap[action].let innerCallListeners@{ listeners ->
                if (listeners == null) return@innerCallListeners
                else List(listeners.size) { i -> listeners[i] }.forEach { it() }
            }

        internal fun pushListeners() {
            listenerMapStack.push(listenerMap)
            listenerMap = mutableMapOf()
        }

        internal fun popListeners() {
            listenerMapStack.poll()?.forEach { entry ->
                entry.value.forEach { addListener(entry.key, it) }
            }
        }
    }
}
