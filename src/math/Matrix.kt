package math

import java.lang.RuntimeException

// TODO: change from primary constructor to two constructors,
//       where one takes in an Array<Array<Double>> and saves it,
//       and the other takes in the an int size and (Int) -> T function,
//       which are used with Array's constructor
data class Matrix(private val matrix: Array<Array<Double>>) {
    val width: Int = matrix.size
    val height: Int = matrix[0].size

    fun getRow(index: Int): Array<Double> {
        if (index < 0 || index >= matrix.size) {
            throw RuntimeException("Index is invalid! index: $index, matrix size: " + matrix.size)
        }
        return matrix[index]
    }

    fun getCol(index: Int): Array<Double> {
        if (index < 0 || index >= matrix[0].size) {
            throw RuntimeException("Index is invalid! index: $index, matrix size: " + matrix[0].size)
        }
        return matrix.map { it[index] }.toTypedArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        return matrix.contentDeepHashCode()
    }

    override fun toString(): String {
        return matrix.contentDeepToString()
    }
}