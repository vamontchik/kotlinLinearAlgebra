import java.awt.Graphics

data class Square(val x: Int, val y: Int, val width: Int, val height: Int) {
    var labelCorner: String = ""

    fun draw(g: Graphics) {
        g.drawRect(x, y, width, height)
        g.drawString(labelCorner, x + 1, y + 11)
    }

    fun contains(x: Int, y: Int): Boolean =
        x >= this.x && y >= this.y &&
        x < (this.x + width) && y < (this.y + height)
}