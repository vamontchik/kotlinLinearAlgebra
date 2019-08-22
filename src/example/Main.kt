package example

import example.getEquationVerticalUp
import example.spiralPath
import example.Grid
import java.lang.RuntimeException
import javax.swing.JFrame

fun main() {
    val jFrame = JFrame()
    val grid = Grid()

    jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    if (jFrame.contentPane != null) {
        jFrame.contentPane.add(grid)
    } else {
        throw RuntimeException("JFrame.contentPane was null!")
    }
    jFrame.pack()
    jFrame.setLocationRelativeTo(null)
    jFrame.isVisible = true

    spiralPath(grid)
    getEquationVerticalUp(grid.width / 2, grid.height / 2, grid)
}