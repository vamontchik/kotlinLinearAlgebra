import java.awt.Dimension
import java.awt.Graphics
import java.lang.RuntimeException
import javax.swing.JPanel

class Grid : JPanel() {
    private val listOfSquares: List<Square>
    private val squareLength: Int = 50

    init {
        preferredSize = Dimension(10*squareLength, 10*squareLength)

        val tempList: MutableList<Square> = ArrayList()
        for (i in 0..10*squareLength step squareLength) {
            for (j in 0..10*squareLength step squareLength) {
                tempList.add(Square(i, j, squareLength, squareLength))
            }
        }
        listOfSquares = tempList
    }

    override fun paintComponent(g: Graphics?) {
        if (g == null) throw RuntimeException("Graphics object in Grid::paintComponent was null!")
        super.paintComponent(g)
        for (square in listOfSquares) {
            square.draw(g)
        }
    }

    fun spiralPath() {
        var x = width / 2
        var y = height / 2

        setNumber(x, y, "0")
        y -= squareLength
        setNumber(x, y, "1")
        x -= squareLength
        setNumber(x, y, "2")
        // ...
    }

    private fun setNumber(x: Int, y: Int, setTo: String) {
        val currSquare = findSquare(x, y)
        currSquare.labelCorner = setTo
    }

    private fun findSquare(x: Int, y: Int): Square {
        for (square in listOfSquares) {
            if (square.contains(x, y)) {
                return square
            }
        }
        throw RuntimeException("Could not find square based on x: " + x + "y: " + y)
    }
}