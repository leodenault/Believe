package believe.gamestate

import believe.core.Updatable
import believe.core.display.Renderable
import believe.gamestate.transition.GameStateTransition
import dagger.Reusable
import org.newdawn.slick.Graphics
import javax.inject.Inject

/** Default implementation of [GameStateRunner]. */
@Reusable
class GameStateRunnerImpl @Inject constructor() : GameStateRunner {
    private var previousState: GameState = EmptyGameState
    private var currentUpdatableAndRenderable: UpdatableAndRenderable<*> =
        UpdatableAndRenderable(EmptyGameState)

    override fun update(delta: Int) = currentUpdatableAndRenderable.update(delta)

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

    private object EmptyGameState : GameState {
        override fun enter() {}
        override fun leave() {}
        override fun update(delta: Int) {}
        override fun render(g: Graphics) {}
    }

    private class UpdatableAndRenderable<T> internal constructor(
        private var value: T
    ) : Updatable, Renderable where T : Updatable, T : Renderable {

        override fun update(delta: Int) {
            value.update(delta)
        }

        override fun render(g: Graphics) {
            value.render(g)
        }
    }
}