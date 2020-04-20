package believe.geometry

class Rectangle(
    x: Int, y: Int, width: Int, height: Int
) : org.newdawn.slick.geom.Rectangle(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat()) {

    fun intersects(other: Rectangle): Boolean {
        if (x >= other.x + other.width || x + width <= other.x) {
            return false
        }
        return !(y >= other.y + other.height || y + height <= other.y)
    }

    fun intersection(rect: Rectangle): Rectangle {
        val xmin = rect.getMinX().toInt()
        val xmax = rect.getMaxX().toInt()
        val ymin = rect.getMinY().toInt()
        val ymax = rect.getMaxY().toInt()
        if (this.intersects(rect)) {
            if (xmin >= minX && xmax <= maxX && ymin >= minY && ymax <= maxY) {
                return Rectangle(
                    rect.getX().toInt(),
                    rect.getY().toInt(),
                    rect.getWidth().toInt(),
                    rect.getHeight().toInt()
                )
            } else if (minX >= xmin && maxX <= xmax && minY >= ymin && maxY <= ymax) {
                return Rectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt())
            }
            val ix = Math.max(xmin.toFloat(), minX)
            val iy = Math.max(ymin.toFloat(), minY)
            val iwidth = Math.min(xmax.toFloat(), maxX) - ix
            val iheight = Math.min(ymax.toFloat(), maxY) - iy
            return Rectangle(ix.toInt(), iy.toInt(), iwidth.toInt(), iheight.toInt())
        }
        return Rectangle(0, 0, 0, 0)
    }


    /**
     * @param rect The rectangle that must move
     * @return True if direction is right, otherwise false
     */
    fun horizontalCollisionDirection(rect: org.newdawn.slick.geom.Rectangle): Boolean {
        return rect.centerX > this.centerX
    }

    /**
     * @param rect The rectangle that must move
     * @return True if direction is down, otherwise false
     */
    fun verticalCollisionDirection(rect: org.newdawn.slick.geom.Rectangle): Boolean {
        return rect.centerY > this.centerY
    }

    fun copy() = Rectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt())
}