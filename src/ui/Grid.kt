package ui

import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class Grid : JPanel() {
    val listOfSquares: List<Square>
    private val numRowCol: Int = 16
    val squareLength: Int = 75

    init {
        preferredSize = Dimension(numRowCol*squareLength, numRowCol*squareLength)
        val tempList: MutableList<Square> = ArrayList()
        for (i in 0 until numRowCol*squareLength step squareLength) {
            for (j in 0 until numRowCol*squareLength step squareLength) {
                tempList.add(Square(i, j, squareLength, squareLength))
            }
        }
        listOfSquares = tempList
    }

    override fun paintComponent(g: Graphics?) {
        if (g == null) throw RuntimeException("Graphics object in ui.Grid::paintComponent was null!")
        super.paintComponent(g)
        for (square in listOfSquares) {
            square.draw(g)
        }
    }

    fun findSquare(x: Int, y: Int): Square {
        return listOfSquares.find { it.contains(x,y) } ?:
               throw RuntimeException("listOfSquares could not find a square at x: $x, y: $y")
    }
}