import math.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class LinAlgTest {
    private fun doubleComparison(value: Double, target: Double): Boolean {
        val tolerance = 0.0001
        return (abs(value) - target < tolerance)
    }

    private fun matrixEquals(first: Matrix, second: Matrix) {
        for (row in 0 until getHeight(first)) {
            for (col in 0 until getWidth(first)) {
                val firstDouble: Double = first[row][col]
                val secondDouble: Double = second[row][col]

                println("first matrix at ($row, $col): $firstDouble, second matrix at ($row, $col): $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }

    private fun vectorEquals(first: Vector, second: Vector) {
        for (i in 0 until first.size) {
            val firstDouble: Double = first[i]
            val secondDouble: Double = second[i]

            println("first vector at $i: $firstDouble, second vector at $i: $secondDouble")

            assertTrue { doubleComparison(firstDouble, secondDouble) }
        }
    }

    private fun vectorToAllMatrixRowsEquals(m: Matrix) {
        for (row in 0 until getHeight(m)) {
            val v: Vector = getRow(m, row)

            for (col in 0 until getWidth(m)) {
                val firstDouble: Double = m[row][col]
                val secondDouble: Double = v[col]

                println("matrix at ($row, $col): $firstDouble, vector at $col: $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }

    private fun vectorToAllMatrixColumnsEquals(m: Matrix) {
        for (col in 0 until getWidth(m)) {
            val v: Vector = getCol(m, col)

            for (row in 0 until getHeight(m)) {
                val firstDouble: Double = v[row]
                val secondDouble: Double = m[row][col]

                println("matrix at ($row, $col): $firstDouble, vector at $row: $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }

    @Test
    fun rowAndWidthTest() {
        val m: Matrix = arrayOf(
            arrayOf(7.0, 4.0, 1.0),
            arrayOf(4.0, 4.0, 4.0),
            arrayOf(1.0, 4.0, 7.0)
        )
        vectorToAllMatrixRowsEquals(m)
    }

    @Test
    fun colAndHeightTest() {
        val m: Matrix = arrayOf(
            arrayOf(7.0, 4.0, 1.0),
            arrayOf(4.0, 4.0, 4.0),
            arrayOf(1.0, 4.0, 7.0)
        )
        vectorToAllMatrixColumnsEquals(m)
    }

    @Test
    fun matrixTransposeTest() {
        val transposeBase: Matrix = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )
        val result: Matrix = arrayOf(
            arrayOf(1.0, 4.0, 7.0),
            arrayOf(2.0, 5.0, 8.0),
            arrayOf(3.0, 6.0, 9.0)
        )
        matrixEquals(transpose(transposeBase), result)
    }

    @Test
    fun matrixAddTest() {
        val addBase: Matrix = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )
        val toAdd: Matrix = arrayOf(
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0)
        )
        val result: Matrix = arrayOf(
            arrayOf(2.0, 3.0, 4.0),
            arrayOf(5.0, 6.0, 7.0),
            arrayOf(8.0, 9.0, 10.0)
        )
        matrixEquals(add(addBase, toAdd), result)
    }

    @Test
    fun matrixSubtractTest() {
        val subtractBase: Matrix = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )
        val toSubtract: Matrix = arrayOf(
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0)
        )
        val result = arrayOf(
            arrayOf(0.0, 1.0, 2.0),
            arrayOf(3.0, 4.0, 5.0),
            arrayOf(6.0, 8.0, 9.0)
        )
        matrixEquals(subtract(subtractBase, toSubtract), result)
    }

    @Test
    fun vectorDotTest() {
        val vectorOne: Vector = arrayOf(1.0, 2.0, 3.0, 4.0)
        val vectorTwo: Vector = arrayOf(5.0, 6.0, 7.0, 8.0)
        val result = 70.0
        assertTrue { doubleComparison(dot(vectorOne, vectorTwo), result) }
    }

    @Test
    fun matrixVectorMultiplyTest() {
        val matrix: Matrix = arrayOf(
            arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
            arrayOf(6.0, 7.0, 8.0, 9.0, 10.0),
            arrayOf(11.0, 12.0, 13.0, 14.0, 15.0)
        )
        val vector: Vector = arrayOf(
            16.0, 17.0, 18.0, 19.0, 20.0
        )
        val result: Vector = arrayOf(280.0, 730.0, 1180.0)
        val multiplied = multiply(matrix, vector)
        vectorEquals(multiplied, result)
    }

//    @Test
//    fun eigenTest() {
//        println("eigenTest...")
//        val (eigenvalues, eigenvectors) = eigen(basicBase)
//        vectorEquals(eigenvalues, eigenvaluesResult)
//        matrixEquals(eigenvectors, eigenvectorsResult)
//    }
}