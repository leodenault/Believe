package believe.gamestate

import believe.core.Updatable
import believe.core.display.Graphics
import believe.core.display.RenderableV2
import believe.gamestate.transition.EmptyGameStateTransition
import believe.gamestate.transition.GameStateTransition
import javax.inject.Inject

/** Default implementation of [GameStateRunner]. */
class GameStateRunnerImpl @Inject constructor() : GameStateRunner {
    private var previousState: GameState = EmptyGameState
    private var currentUpdatableAndRenderable: UpdatableAndRenderable<*> =
        UpdatableAndRenderable(EmptyGameState)

    override fun update(delta: Long) = currentUpdatableAndRenderable.update(delta)

    override fun render(g: Graphics) = currentUpdatableAndRenderable.render(g)

    override fun transitionTo(
        nextState: GameState,
        leaveTransition: GameStateTransition,
        enterTransition: GameStateTransition
    ) {
        previousState.leave()
        enterTransition.addListener(object : GameStateTransition.Listener {
            override fun transitionEnded() {
                currentUpdatableAndRenderable = UpdatableAndRenderable(nextState)
                previousState = nextState
                nextState.enter()
            }
        })
        leaveTransition.addListener(object : GameStateTransition.Listener {
            override fun transitionEnded() {
                currentUpdatableAndRenderable = UpdatableAndRenderable(enterTransition)
            }
        })
        currentUpdatableAndRenderable = UpdatableAndRenderable(leaveTransition)
    }

    override fun exitCurrentState(leaveTransition: GameStateTransition) =
        transitionTo(EmptyGameState, leaveTransition, EmptyGameStateTransition())

    private object EmptyGameState : GameState {
        override fun enter() {}
        override fun leave() {}
        override fun update(delta: Long) {}
        override fun render(g: Graphics) {}
    }

    private class UpdatableAndRenderable<T> internal constructor(
        private var value: T
    ) : Updatable, RenderableV2 where T : Updatable, T : RenderableV2 {

        override fun update(delta: Long) {
            value.update(delta)
        }

        override fun render(g: Graphics) {
            value.render(g)
        }
    }
}