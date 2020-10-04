package believe.mob

import believe.core.display.Graphics
import believe.scene.SceneElement

internal class StationaryEnemy(
    override var x: Float,
    override var y: Float,
    private val stateMachine: StationaryEnemyStateMachine
) : SceneElement {

    override fun render(g: Graphics) = g.drawAnimation(stateMachine.animation, x, y)

    override fun bind() = stateMachine.bind()

    override fun unbind() = stateMachine.unbind()

    override fun update(delta: Long) = stateMachine.update(delta)
}
