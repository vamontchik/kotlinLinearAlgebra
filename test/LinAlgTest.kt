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
    }

    fun doubleComparison(value: Double, target: Double): Boolean {
        val tolerance = 0.0001
        return (abs(value) - target < tolerance)
    }

    fun matrixEquals(first: Matrix, second: Matrix) {
        for (row in 0 until getHeight(first)) {
            for (col in 0 until getWidth(first)) {
                val firstDouble: Double = first[row][col]
                val secondDouble: Double = second[row][col]

                println("first matrix at ($row, $col): $firstDouble, second matrix at ($row, $col): $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
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
    }

    @Test
    fun rowAndWidthTest() {
        println("rowAndWidthTest...")

        for (row in 0 until getHeight(basicBase)) {
            val v: Vector = getRow(basicBase, row)

            for (col in 0 until getWidth(basicBase)) {
                val firstDouble: Double = v[col]
                val secondDouble: Double = basicBase[row][col]

                println("matrix at ($row, $col): $firstDouble, vector at $row: $secondDouble")

                assertTrue { doubleComparison(v[col], basicBase[row][col]) }
            }
        }
    }

    @Test
    fun colAndHeightTest() {
        println("colAndHeightTest...")

        for (col in 0 until getWidth(basicBase)) {
            val v: Vector = getCol(basicBase, col)

            for (row in 0 until getHeight(basicBase)) {
                val firstDouble: Double = v[row]
                val secondDouble: Double = basicBase[row][col]

                println("matrix at ($row, $col): $firstDouble, vector at $row: $secondDouble")

                assertTrue { doubleComparison(v[row], basicBase[row][col]) }
            }
        }
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
    fun matrixTransposeTest() {
        println("matrixTransposeTest...")
        matrixEquals(transpose(transposeBase), transposeResult)
    }

    
    @Test
    fun eigenTest() {
        println("eigenTest...")

        val (eigenvalues, eigenvectors) = eigen(basicBase)

        println("eigenvalues: " + eigenvalues.contentDeepToString())
        printMatrix("eigenvectors", eigenvectors)

        for (row in 0 until getHeight(eigenvectors)) {
            assertTrue { doubleComparison(eigenvalues[row], eigenvaluesResult[row]) }
            
            for (col in 0 until getWidth(eigenvectors)) {
                val firstDouble: Double = eigenvectors[row][col]
                val secondDouble: Double = eigenvectorsResult[row][col]

                println("first matrix at ($row, $col): $firstDouble, second matrix at ($row, $col): $secondDouble")

                assertTrue { doubleComparison(firstDouble, secondDouble) }
            }
        }
    }
}