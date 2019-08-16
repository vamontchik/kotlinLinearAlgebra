import math.*
import kotlin.math.abs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LinAlgTest {
    companion object {
        lateinit var basicBase: Matrix

        lateinit var addBase: Matrix
        lateinit var toAdd: Matrix
        lateinit var addResult: Matrix

        lateinit var subtractBase: Matrix
        lateinit var toSubtract: Matrix
        lateinit var subtractResult: Matrix

        lateinit var transposeBase: Matrix
        lateinit var transposeResult: Matrix

        lateinit var eigenvaluesResult: Vector
        lateinit var eigenvectorsResult: Matrix

        lateinit var dotVectorBase: Vector
        lateinit var dotVectorTwoBase: Vector
        var dotVectorResult: Double = 0.0 // dummy for compiler

        lateinit var multiplyMatrix: Matrix
        lateinit var multiplyVector: Vector
        lateinit var multiplyMatrixVectorResult: Vector
    }

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

                assertTrue { doubleComparison(v[row], basicBase[row][col]) }
            }
        }
    }

    @BeforeTest
    fun setup() {
        basicBase = arrayOf(
            arrayOf(7.0, 4.0, 1.0),
            arrayOf(4.0, 4.0, 4.0),
            arrayOf(1.0, 4.0, 7.0)
        )

        //

        addBase = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )

        toAdd = arrayOf(
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(1.0, 1.0, 1.0)
        )

        addResult = Array(addBase.size) {
            Array(addBase[0].size) { it2 -> addBase[it][it2] + toAdd[it][it2] }
        }

        //

        subtractBase = addBase

        toSubtract = toAdd

        subtractResult = Array(subtractBase.size) {
            Array(subtractBase[0].size) { it2 -> subtractBase[it][it2] - toSubtract[it][it2] }
        }

        //

        transposeBase = arrayOf(
            arrayOf(1.0, 2.0, 3.0),
            arrayOf(4.0, 5.0, 6.0),
            arrayOf(7.0, 8.0, 9.0)
        )

        transposeResult = arrayOf(
            arrayOf(1.0, 4.0, 7.0),
            arrayOf(2.0, 5.0, 8.0),
            arrayOf(3.0, 6.0, 9.0)
        )

        //

        eigenvaluesResult = arrayOf(12.0, 6.0, 0.0)
        
        eigenvectorsResult = arrayOf(
            arrayOf(1.0, 1.0, 1.0),
            arrayOf(-1.0, 0.0, 1.0),
            arrayOf(1.0, -2.0, 1.0)
        )

        //

        dotVectorBase = arrayOf(1.0, 2.0, 3.0, 4.0)

        dotVectorTwoBase = arrayOf(5.0, 6.0, 7.0, 8.0)

        dotVectorResult = dotVectorBase.foldIndexed(0.0) {
            index, acc, _ -> acc.plus(dotVectorBase[index] * dotVectorTwoBase[index])
        }

        //

        multiplyMatrix = Array(5) {
            Array(10) { it.toDouble() }
        }

        multiplyVector = Array(10) { it.toDouble() }

        multiplyMatrixVectorResult = Array(5) { 0.0 }
        for (i in 0 until multiplyMatrixVectorResult.size) {
            multiplyMatrixVectorResult[i] = multiplyMatrix[i].mapIndexed {
                index, _ -> multiplyMatrix[i][index] * multiplyVector[index]
            }.sum()
        }
    }

    @Test
    fun rowAndWidthTest() {
        println("rowAndWidthTest...")
        vectorToAllMatrixRowsEquals(basicBase)
    }

    @Test
    fun colAndHeightTest() {
        println("colAndHeightTest...")
        vectorToAllMatrixColumnsEquals(basicBase)
    }

    @Test
    fun matrixTransposeTest() {
        println("matrixTransposeTest...")
        matrixEquals(transpose(transposeBase), transposeResult)
    }

    @Test
    fun matrixAddTest() {
        println("matrixAddTest...")
        matrixEquals(add(addBase, toAdd), addResult)
    }

    @Test
    fun matrixSubtractTest() {
        println("matrixSubtractTest...")
        matrixEquals(subtract(subtractBase, toSubtract), subtractResult)
    }

    @Test
    fun vectorDotTest() {
        println("vectorDotTest...")
        assertTrue { doubleComparison(dot(dotVectorBase, dotVectorTwoBase), dotVectorResult) }
    }

    @Test
    fun matrixVectorMultiplyTest() {
        println("matrixVectorMultiplyTest...")
        val multiplied = multiply(multiplyMatrix, multiplyVector)
        val result = multiplyMatrixVectorResult
        vectorEquals(multiplied, result)
    }

    @Test
    fun eigenTest() {
        println("eigenTest...")
        val (eigenvalues, eigenvectors) = eigen(basicBase)
        vectorEquals(eigenvalues, eigenvaluesResult)
        matrixEquals(eigenvectors, eigenvectorsResult)
    }
}