import math.*
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertTrue

class LinAlgTest {
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

    private fun vectorEquals(first: Vector, second: Vector, precision: Double = 0.0001) {
        for (i in 0 until first.size) {
            val firstDouble: Double = first[i]
            val secondDouble: Double = second[i]

            println("first vector at $i: $firstDouble, second vector at $i: $secondDouble")

            assertTrue { doubleComparison(firstDouble, secondDouble, precision) }
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

    private fun isScalarMultipleOfEachVector(first: Matrix, second: Matrix) {
        for (i in 0 until first.size) {
            println("isScalarMultipleOf ${first[i].contentDeepToString()} with ${second[i].contentDeepToString()}...")
            isScalarMultipleOf(first[i], second[i])
        }
    }

    private fun isScalarMultipleOf(first: Vector, second: Vector) {
        val guessedScalar: Double = first[0] / second[0]
        println("Guessed scalar: $guessedScalar")
        for (i in 1 until first.size) {
            println("Zero check with ${first[i]} and ${second[i]}...")
            val canSkip = zeroCheck(first[i], second[i])
            if (canSkip) continue
            println("Comparing $guessedScalar with ${first[i]/second[i]}...")
            assertTrue { doubleComparison(guessedScalar, first[i] / second[i]) }
        }
    }

    private fun zeroCheck(first: Double, second: Double): Boolean {
        if (doubleComparison(0.0, second)) {
            assertTrue { doubleComparison(0.0, first) }
            return true
        }
        return false
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
            arrayOf(6.0, 7.0, 8.0)
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
    fun matrixMatrixMultiplyTest() {
        val matrixOne: Matrix = arrayOf(
            arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
            arrayOf(6.0, 7.0, 8.0, 9.0, 10.0)
        )
        val matrixTwo: Matrix = arrayOf(
            arrayOf(11.0, 12.0),
            arrayOf(13.0, 14.0),
            arrayOf(15.0, 16.0),
            arrayOf(17.0, 18.0),
            arrayOf(19.0, 20.0)
        )
        val result: Matrix = arrayOf(
            arrayOf(245.0, 260.0),
            arrayOf(620.0, 660.0)
        )
        val multiplied = multiply(matrixOne, matrixTwo)
        matrixEquals(result, multiplied)
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

    @Test
    fun vectorMatrixMultiplyTest() {
        val matrix: Matrix = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0),
            arrayOf(10.0, 11.0, 12.0),
            arrayOf(13.0, 14.0, 15.0)
        )
        val vector: Vector = arrayOf(
            16.0, 17.0, 18.0, 19.0, 20.0
        )
        val result: Vector = arrayOf(660.0, 750.0, 840.0)
        val multiplied: Vector = multiply(vector, matrix)
        vectorEquals(multiplied, result)
    }

    @Test
    fun vectorVectorMultiplyTest() {
        val vectorOne: Vector = arrayOf(
            11.0, 12.0, 13.0, 14.0, 15.0
        )
        val vectorTwo: Vector = arrayOf(
            21.0, 22.0, 23.0, 24.0, 25.0
        )
        val result: Matrix = arrayOf(
            arrayOf(231.0, 242.0, 253.0, 264.0, 275.0),
            arrayOf(252.0, 264.0, 276.0, 288.0, 300.0),
            arrayOf(273.0, 286.0, 299.0, 312.0, 325.0),
            arrayOf(294.0, 308.0, 322.0, 336.0, 350.0),
            arrayOf(315.0, 330.0, 345.0, 360.0, 375.0)
        )
        val multiplied: Matrix = multiply(vectorOne, vectorTwo)
        matrixEquals(multiplied, result)
    }

    @Test
    fun normTwoTest() {
        val vector: Vector = arrayOf(
            5.0, 6.0, 7.0, 8.0, 9.0, 10.0
        )
        val result = sqrt(355.0)
        assertTrue { doubleComparison(norm(vector, 2.0), result) }
    }

    @Test
    fun scalarMultiplyTest() {
        val base: Matrix = arrayOf(
            arrayOf(3.0, 6.0, 9.0, 12.0),
            arrayOf(15.0, 18.0, 21.0, 24.0)
        )
        val scalar = 3.0
        val result: Matrix = arrayOf(
            arrayOf(9.0, 18.0, 27.0, 36.0),
            arrayOf(45.0, 54.0, 63.0, 72.0)
        )
        matrixEquals(scalarMultiply(scalar, base), result)
    }

    @Test
    fun scalarDivideTest() {
        val base: Vector = arrayOf(
            3.0, 6.0, 9.0, 12.0
        )
        val scalar = 3.0
        val result: Vector = arrayOf(
            1.0, 2.0, 3.0, 4.0
        )
        vectorEquals(scalarDivide(scalar, base), result)
    }

    @Test
    fun simpleEigenTest() {
        val m: Matrix = arrayOf(
            arrayOf(2456.0, 1234.0),
            arrayOf(111.0, 987.0)
        )
        val eigenvaluesResult: Vector = arrayOf(
            2543.97, 899.026
        )
        val eigenvectorsResult: Matrix = arrayOf(
            arrayOf(14.0268, 1.0),
            arrayOf(-0.792563, 1.0)
        )
        val (eigenvalues, eigenvectors) = eigen(m)

        println("eigenvalues found: ${eigenvalues.contentDeepToString()}")
        printMatrix("eigenvectors", eigenvectors)

        vectorEquals(eigenvalues, eigenvaluesResult, 0.01)
        isScalarMultipleOfEachVector(eigenvectors, eigenvectorsResult)
    }
}