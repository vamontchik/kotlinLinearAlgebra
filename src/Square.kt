import java.awt.Font
import java.awt.Graphics
import java.lang.RuntimeException

data class Square(val x: Int, val y: Int,
                  val width: Int, val height: Int,
                  var isBold: Boolean = false,
                  var labelCorner: String = "") {
    fun draw(g: Graphics) {
        g.drawRect(x, y, width, height)

        if (g.font == null) throw RuntimeException("g.font in Square::draw was null!")
        if (isBold) {
            g.font = g.font.deriveFont(Font.BOLD)
        } else {
            g.font = g.font.deriveFont(Font.PLAIN)
        }

        g.drawString(labelCorner, x + 1, y + 11)
    }

    fun contains(x: Int, y: Int): Boolean =
        x >= this.x && y >= this.y &&
        x < (this.x + width) && y < (this.y + height)
}