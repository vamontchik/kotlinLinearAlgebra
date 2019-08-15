import math.*
import kotlin.math.abs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LinAlgTest {
    companion object {
        lateinit var m: Matrix
    }

    @BeforeTest
    fun setup() {
        m = arrayOf(
            arrayOf(7.0, 4.0, 1.0),
            arrayOf(4.0, 4.0, 4.0),
            arrayOf(1.0, 4.0, 7.0)
        )
    }

    private fun doubleComparison(value: Double, target: Double): Boolean {
        val tolerance = 0.0001
        return (abs(value) - target < tolerance)
    }

    @Test
    fun rowAndWidthTest() {
        println("rowAndWidthTest...")
        for (i in 0 until getHeight(m)) {
            val v: Vector = getRow(m, i)
            for (j in 0 until getWidth(m)) {
                val firstDouble: Double = v[j]
                val secondDouble: Double = m[i][j]
                println("Checking at ($i, $j): $firstDouble, with vector pos $j: $secondDouble")
                assertTrue { doubleComparison(v[j], m[i][j]) }
            }
        }
    }

    @Test
    fun colAndHeightTest() {
        println("colAndHeightTest...")
        for (i in 0 until getWidth(m)) {
            val v: Vector = getCol(m, i)
            for (j in 0 until getHeight(m)) {
                val firstDouble: Double = v[j]
                val secondDouble: Double = m[j][i]
                println("Checking at ($j, $i): $firstDouble, with vector pos $j: $secondDouble")
                assertTrue() { doubleComparison(v[j], m[j][i]) }
            }
        }
    }


//    @Test
//    fun eigenTest() {
//        val m: Matrix = arrayOf(
//            arrayOf(7.0, 4.0, 1.0),
//            arrayOf(4.0, 4.0, 4.0),
//            arrayOf(1.0, 4.0, 7.0)
//        )
//
//        val (eigenvalues, eigenvectors) = eigen(m)
//
//        println("eigenvalues: " + eigenvalues.contentDeepToString())
//        printMatrix("eigenvectors", eigenvectors)
//
//        assertTrue { abs(eigenvalues[0] - 12.0) < 0.0001 }
//        assertTrue { abs(eigenvalues[1] - 6.0) < 0.0001 }
//        assertTrue { abs(eigenvalues[2]) < 0.001 }
//    }
}