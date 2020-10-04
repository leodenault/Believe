package believe.animation.testing

import believe.gui.testing.FakeImage
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet

/**
 * A fake [SpriteSheet] used in testing which is based on fake images.
 *
 * @param numRows the number of rows in the [SpriteSheet].
 * @param numColumns the number of columns in the [SpriteSheet].
 */
class FakeSpriteSheet(
    private val numRows: Int, private val numColumns: Int
) : SpriteSheet(FakeImage(), 1, 1) {

    private val tiles: List<List<FakeImage>> =
        (0 until numColumns).map { (0 until numRows).map { FakeImage() } }

    override fun getHorizontalCount(): Int = numColumns
    override fun getVerticalCount(): Int = numRows
    override fun getSprite(x: Int, y: Int): Image = tiles[x][y]

    /**
     * Returns the image at [index] assuming that indices are laid out left to right, top to bottom,
     * such as:
     *
     * ```
     * 0123
     * 4567
     * ```
     */
    fun imageAt(index: Int) = getSprite(index % horizontalCount, index / horizontalCount)

    /**
     * Returns the images associated with the indices expressed in [indexRange]. Indices are laid
     * out as is explained in [imageAt].
     */
    fun imagesBetween(indexRange: IntRange) = indexRange.map { imageAt(it) }
}
