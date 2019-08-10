import java.awt.Font
import java.awt.Graphics
import java.lang.RuntimeException

data class Square(val x: Int, val y: Int,
                  val width: Int, val height: Int,
                  var isBold: Boolean = false,
                  var labelCorner: String = "",
                  val textXOffset: Int = 5, val textYOffset: Int = 20) {

    fun draw(g: Graphics) {
        g.drawRect(x, y, width, height)

        if (g.font == null) throw RuntimeException("g.font in Square::draw was null!")

        // set bold for corners
        if (isBold) {
            g.font = g.font.deriveFont(Font.BOLD)
        } else {
            g.font = g.font.deriveFont(Font.PLAIN)
        }

        // set font size
        g.font = g.font.deriveFont(20.0f)

        g.drawString(labelCorner, x + textXOffset, y + textYOffset)
    }

    fun contains(x: Int, y: Int): Boolean =
        x >= this.x && y >= this.y &&
        x < (this.x + width) && y < (this.y + height)
}