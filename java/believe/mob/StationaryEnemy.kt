package believe.mob

import believe.core.display.Graphics
import believe.geometry.rectangle
import believe.scene.SceneElement
import org.newdawn.slick.Color

internal class StationaryEnemy(
    override var x: Float, override var y: Float
) : SceneElement {
    private val bounds = rectangle(x = x, y = y, width = 32f, height = 96f)
    private val colour = Color(0xff0000)

    override fun render(g: Graphics) = g.fill(bounds, colour)

    override fun bind() {}

    override fun unbind() {}

    override fun update(delta: Long) {}
}
